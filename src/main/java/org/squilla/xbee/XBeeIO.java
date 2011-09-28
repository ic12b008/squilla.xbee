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

import java.io.IOException;
import org.squilla.io.ByteUtil;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class XBeeIO {
    
    public static final int DEFAULT_TIMEOUT = 1000;
    
    private XBeeDevice driver;
    private boolean queueing;
    private int timeout;
    
    public XBeeIO(XBeeDevice driver) {
        this.driver = driver;
        setTimeout(DEFAULT_TIMEOUT);
    }
    
    private void checkResponse(ATCommandResponse resp) throws IOException {
        if (resp.getStatus() != ATCommandResponse.STATUS_OK) {
            switch (resp.getStatus()) {
            case ATCommandResponse.STATUS_ERROR:
                throw new IOException("Status Error");
            case ATCommandResponse.STATUS_INVALID_COMMAND:
                throw new IOException("Invalid Command");
            case ATCommandResponse.STATUS_INVALID_PARAMETER:
                throw new IOException("Invalid Parameter");
            default:
                throw new IOException("Unknown Error");
            }
        }
    }
    
    public long read64(String cmd) throws IOException {
        return ByteUtil.BIG_ENDIAN.toInt64(readMulti8(cmd), 0);
    }
    
    public int read32(String cmd) throws IOException {
        return ByteUtil.BIG_ENDIAN.toInt32(readMulti8(cmd), 0);
    }
    
    public int read16(String cmd) throws IOException {
        return ByteUtil.BIG_ENDIAN.toInt16(readMulti8(cmd), 0);
    }
    
    public int read8(String cmd) throws IOException {
        return readMulti8(cmd)[0] & 0xFF;
    }
    
    public byte[] readMulti8(String cmd) throws IOException {
        ATCommandResponse resp = (ATCommandResponse) driver.syncSubmit(new ATCommand(cmd), timeout);
        checkResponse(resp);
        return resp.getData();
    }
    
    public void write64(String cmd, long dat) throws IOException {
        writeMulti8(cmd, ByteUtil.BIG_ENDIAN.toByteArray(dat));
    }
    
    public void write32(String cmd, int dat) throws IOException {
        writeMulti8(cmd, ByteUtil.BIG_ENDIAN.toByteArray(dat, ByteUtil.INT_32_SIZE));
    }
    
    public void write16(String cmd, int dat) throws IOException {
        writeMulti8(cmd, ByteUtil.BIG_ENDIAN.toByteArray(dat, ByteUtil.INT_16_SIZE));
    }
    
    public void write8(String cmd, int dat) throws IOException {
        writeMulti8(cmd, new byte[] {(byte) dat});
    }
    
    public void writeMulti8(String cmd, byte[] value) throws IOException {
        ATCommand at = new ATCommand(cmd);
        if (isQueueing()) {
            at.setFrameType(XBeeDevice.API_AT_COMMAND_QPV);
        }
        if (value != null) {
            at.setValue(value);
        }
        ATCommandResponse resp = (ATCommandResponse) driver.syncSubmit(at, timeout);
        checkResponse(resp);
    }

    /**
     * @return the queueing
     */
    public boolean isQueueing() {
        return queueing;
    }

    /**
     * @param queueing the queueing to set
     */
    public void setQueueing(boolean queueing) {
        this.queueing = queueing;
    }
    
    public void applyChanges() throws IOException {
        setQueueing(false);
        driver.asyncSubmit(new ATCommand("AC"), false);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }
    }
    
    public void write() throws IOException {
        driver.asyncSubmit(new ATCommand("WR"), false);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }
    }
    
    public void reset() throws IOException {
        driver.asyncSubmit(new ATCommand("FR"), false);
        try {
            Thread.sleep(2500);
        } catch (InterruptedException ex) {
        }
    }

    /**
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
