/*
 * Copyright 2011 Shotaro Uchida <fantom@xmaker.mx>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.squilla.xbee;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import org.squilla.io.FrameBuffer;
import org.jazzkaffe.IEEEAddress;
import org.jazzkaffe.NetworkAddress;
import org.squilla.service.ServiceThread;
import org.squilla.io.ByteUtil;
import org.squilla.service.WorkQueue;
import org.squilla.util.ArrayFifoQueue;
import org.squilla.util.Commons;
import org.squilla.util.SimpleLatch;
import org.squilla.xbee.s2.XBeeAddressingIndicator;
import org.squilla.xbee.s2.XBeeAddressingRequest;
import org.squilla.xbee.s2.ZigBeeTransmitStatus;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class XBeeDeviceImpl implements XBeeDevice {

    public static final int BUFFER_SIZE = 512;
    private InputStream is;
    private OutputStream os;
    private FrameBuffer writeBuffer;
    private byte[] readBuffer;
    private int callback = XBeeRequest.DEFAULT_FRAME_ID;
    private Vector listenerList;
    private Map addressMap;
    private final Object addressLock = new Object();
    private SimpleLatch[] respLatch;
    private WorkQueue eventQueue;
    private ArrayFifoQueue txQueue;
    private ServiceThread txDispatchThread;
    private ServiceThread rxEventThread;
    private Logger logger = LoggerFactory.getLogger(XBeeDeviceImpl.class);
    private volatile boolean frameTrace;

    public XBeeDeviceImpl(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
        writeBuffer = new FrameBuffer(new byte[BUFFER_SIZE]);
        readBuffer = new byte[BUFFER_SIZE];
        listenerList = new Vector();
        respLatch = new SimpleLatch[0xFF];
        for (int i = 0; i < 0xFF; i++) {
            respLatch[i] = new SimpleLatch();
        }
        eventQueue = new WorkQueue(10);
        txQueue = new ArrayFifoQueue(10);
        addressMap = new HashMap();
    }
    
    public void setFrameTrace(boolean b) {
        this.frameTrace = b;
    }
    
    public int getTXQueueSize() {
        return txQueue.size();
    }
    
    public int getEventRemainingSize() {
        return eventQueue.remainingTask();
    }

    public void activate() {
        if (txDispatchThread != null) {
            return;
        }

        eventQueue.activate();
        txDispatchThread = new TXDispatchThread();
        rxEventThread = new RXEventThread();
        txDispatchThread.activate();
        rxEventThread.activate();
    }

    public void shutdown() throws IOException {
        if (txDispatchThread == null) {
            return;
        }
        
        logger.trace("TX Shutdown");
        txDispatchThread.shutdown();
        logger.trace("RX Shutdown");
        rxEventThread.shutdown();
//        logger.trace("TX Close");
//        os.close();
//        logger.trace("RX Close");
//        is.close();
        logger.trace("Drain Latch");
        for (int i = 0; i < 0xFF; i++) {
            respLatch[i].get();
        }
        logger.trace("Queue Shutdown");
        eventQueue.shutdown();
    }

    private int nextCallback() {
        if (callback > 255 || callback == 0) {
            callback = XBeeRequest.DEFAULT_FRAME_ID;
        }
        return callback++;
    }

    public void addDeviceListener(XBeeDeviceListener listener) {
        listenerList.add(listener);
    }

    public XBeeResponse syncSubmit(XBeeRequest request, int timeout) throws IOException {
        int frameID = nextCallback();
        request.setFrameID(frameID);
        txQueue.enqueue(request);
        Object o = respLatch[frameID - 1].await(timeout);
        if (o == null) {
            throw new IOException("XBee Response Timeout");
        }
        XBeeFrameIDResponse response = (XBeeFrameIDResponse) o;
        if (response.getFrameID() != frameID) {
            throw new IOException("Frame ID not match !");
        }
        if (request instanceof XBeeAddressingRequest
                && response instanceof ZigBeeTransmitStatus) {
            XBeeAddressingRequest tx = (XBeeAddressingRequest) request;
            ZigBeeTransmitStatus status = (ZigBeeTransmitStatus) response;
            updateAddress(tx.getAddress64(), status.getAddress16());
        }
        return response;
    }

    public int asyncSubmit(XBeeRequest request, boolean needResponse) {
        final int frameID;
        if (needResponse) {
            frameID = nextCallback();
        } else {
            frameID = XBeeRequest.NO_RESPONSE_FRAME_ID;
        }
        request.setFrameID(frameID);
        txQueue.enqueue(request);
        return frameID;
    }

    private void rawReceive(final XBeeResponse response) {
        if (response instanceof XBeeAddressingIndicator) {
            XBeeAddressingIndicator rx = (XBeeAddressingIndicator) response;
            updateAddress(rx.getAddress64(), rx.getAddress16());
        } else if (response instanceof XBeeFrameIDResponse) {
            XBeeFrameIDResponse rx = (XBeeFrameIDResponse) response;
            SimpleLatch latch = respLatch[rx.getFrameID() - 1];
            if (latch.isAwaiting()) {
                latch.set(response);
                return;
            }
        }
        eventQueue.execute(new Runnable() {
            public void run() {
                for (int i = 0; i < listenerList.size(); i++) {
                    XBeeDeviceListener listener = (XBeeDeviceListener) listenerList.get(i);
                    listener.handleResponse(response);
                }
            }
        });
    }

    private void updateAddress(IEEEAddress address64, NetworkAddress address16) {
        synchronized (addressLock) {
            addressMap.put(address64, address16);
        }
    }

    private NetworkAddress resolveLocal(IEEEAddress address64) {
        synchronized (addressLock) {
            Object o = addressMap.get(address64);
            if (o != null) {
                return (NetworkAddress) o;
            }
        }
        return null;
    }

    private IEEEAddress resolveLocal(NetworkAddress address16) {
        synchronized (addressLock) {
            Set entrySet = addressMap.entrySet();
            for (Iterator it = entrySet.iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                if (entry.getValue().equals(address16)) {
                    return (IEEEAddress) entry.getKey();
                }
            }
        }
        return null;
    }

    private class TXDispatchThread extends ServiceThread {
        
        public TXDispatchThread() {
            setPriority(NORM_PRIORITY + 1);
        }

        public void taskLoop() {
            XBeeRequest request = (XBeeRequest) txQueue.blockingDequeue();
            try {
                if (request instanceof XBeeAddressingRequest) {
                    XBeeAddressingRequest tx = (XBeeAddressingRequest) request;
                    if (tx.getAddress16() == null) {
                        if (tx.getAddress64() == null) {
                            tx.setAddress64(XBeeAddressingRequest.XBEE_BROADCAST64);
                            tx.setAddress16(XBeeAddressingRequest.XBEE_BROADCAST16);
                        } else {
                            NetworkAddress resolved = resolveLocal(tx.getAddress64());
                            if (resolved != null) {
                                tx.setAddress16(resolved);
                            } else {
                                tx.setAddress16(XBeeAddressingRequest.XBEE_BROADCAST16);
                            }
                        }
                    } else if (tx.getAddress64() == null) {
                        IEEEAddress resolved = resolveLocal(tx.getAddress16());
                        if (resolved != null) {
                            tx.setAddress64(resolved);
                        } else {
                            throw new IOException("Can't resolve 16bit address.");
                        }
                    }
                }

                writeBuffer.rewind();
                writeBuffer.setByteOrder(FrameBuffer.BO_BIG_ENDIAN);
                writeBuffer.put(FRAME_DELIMITER);
                int length = request.quote();
                writeBuffer.putInt16(length);
                request.pull(writeBuffer);
                byte cs = XBeeUtil.calculateChecksum(writeBuffer.getRawArray(),
                        writeBuffer.getOffset() + 3, length);
                writeBuffer.put(cs);
                logger.trace("Write reqest");
                if (frameTrace) {
                    Commons.printDev(writeBuffer.getRawArray(), writeBuffer.getOffset(), length, false);
                }
                os.write(writeBuffer.getRawArray(),
                        writeBuffer.getOffset(),
                        writeBuffer.getPosition());
                os.flush();
            } catch (Exception ex) {
                logger.error("TXDispatchError");
                logger.error(ex);
            }
        }
    }

    private class RXEventThread extends ServiceThread {
        
        private DataInputStream dis = new DataInputStream(is);
        
        public RXEventThread() {
            setPriority(NORM_PRIORITY + 1);
        }

        public void taskLoop() {
            readBuffer[0] = 0;
            try {
                logger.trace("Read Delimiter");
                dis.readFully(readBuffer, 0, 1);
                if (readBuffer[0] == XBeeDevice.FRAME_DELIMITER) {
                    logger.trace("Read Length");
                    dis.readFully(readBuffer, 1, 2);
                    int length = ByteUtil.BIG_ENDIAN.toInt16(readBuffer, 1);
                    logger.trace("Read Payload + checksum");
                    dis.readFully(readBuffer, 3, length + 1);
                    if (frameTrace) {
                        Commons.printDev(readBuffer, 0, length + 4, true) ;
                    }
                    // Make sure frame is valid
                    byte checksum = XBeeUtil.calculateChecksum(readBuffer, 3, length + 1);
                    if (checksum == 0) {
                        FrameBuffer frameBuffer = new FrameBuffer(readBuffer, 3, length);
                        // Peek first octet to detect frame type.
                        int frameType = frameBuffer.peek() & 0xFF;
                        XBeeResponse response = XBeeResponse.createXBeeResponse(frameType);
                        if (response != null) {
                            response.drain(frameBuffer);
                            rawReceive(response);
                        }
                    } else {
                        logger.warn("Checksum Invalid");
                    }
                }
            } catch (Exception ex) {
                logger.error("RXEventError");
                logger.error(ex);
            }
        }
    }
}
