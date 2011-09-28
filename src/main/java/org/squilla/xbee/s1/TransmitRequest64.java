/*
 * Copyright 2011 Shotaro Uchida <fantom@xmaker.mx>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.squilla.xbee.s1;

import org.squilla.io.FrameBuffer;
import org.jazzkaffe.IEEEAddress;
import org.squilla.xbee.XBeeDevice;
import org.squilla.xbee.XBeeRequest;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class TransmitRequest64 extends XBeeRequest {

    public static final int MAX_PACKET_SIZE = 100;
    
    private IEEEAddress address64;
    private byte options;
    private byte[] packet;
    
    public TransmitRequest64() {
        setFrameType(XBeeDevice.API_TX_REQ_64);
    }

    public void pull(FrameBuffer frameBuffer) {
        frameBuffer.put(address64.array());
        frameBuffer.putInt8(options);
        if (packet != null) {
            frameBuffer.put(packet);
        }
    }

    public int quote() {
        int q = super.quote() + 9;
        if (packet != null) {
            q += packet.length;
        }
        return q;
    }
    
    /**
     * @return the address64
     */
    public IEEEAddress getAddress64() {
        return address64;
    }

    /**
     * @param address64 the address64 to set
     */
    public void setAddress64(IEEEAddress address64) {
        this.address64 = address64;
    }

    /**
     * @return the options
     */
    public byte getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(byte options) {
        this.options = options;
    }

    /**
     * @return the packet
     */
    public byte[] getPacket() {
        return packet;
    }

    /**
     * @param packet the packet to set
     */
    public void setPacket(byte[] packet) {
        this.packet = packet;
    }
}
