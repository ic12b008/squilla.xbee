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
package org.squilla.xbee.s2;

import org.squilla.xbee.XBeeDevice;
import org.squilla.io.FrameBuffer;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class ZigBeeTransmitRequest extends XBeeAddressingRequest {

    private byte broadcastRadius;
    private byte options;
    private byte[] packet;
    
    public ZigBeeTransmitRequest() {
        setFrameType(XBeeDevice.API_ZB_TRANSMIT_REQ);
    }
    
    public void pull(FrameBuffer frameBuffer) {
        super.pull(frameBuffer);
        frameBuffer.putInt8(broadcastRadius);
        frameBuffer.putInt8(options);
        if (packet != null) {
            frameBuffer.put(packet);
        }
    }

    public int quote() {
        int q = super.quote() + 2;
        if (packet != null) {
            q += packet.length;
        }
        return q;
    }

    /**
     * @return the broadcastRadius
     */
    public byte getBroadcastRadius() {
        return broadcastRadius;
    }

    /**
     * @param broadcastRadius the broadcastRadius to set
     */
    public void setBroadcastRadius(byte broadcastRadius) {
        this.broadcastRadius = broadcastRadius;
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
