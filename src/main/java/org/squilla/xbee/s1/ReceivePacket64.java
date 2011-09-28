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
import org.squilla.xbee.XBeeResponse;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class ReceivePacket64 extends XBeeResponse {

    private IEEEAddress address64;
    private byte rssi;
    private byte receiveOptions;
    private byte[] packet;

    public void drain(FrameBuffer frameBuffer) {
        super.drain(frameBuffer);
        address64 = IEEEAddress.getByAddress(frameBuffer.getBytes(IEEEAddress.ADDRESS_SIZE));
        rssi = frameBuffer.getByte();
        receiveOptions = frameBuffer.getByte();
        if (frameBuffer.getRemaining() > 0) {
            packet = frameBuffer.getBytes(frameBuffer.getRemaining());
        }
    }

    /**
     * @return the address64
     */
    public IEEEAddress getAddress64() {
        return address64;
    }

    /**
     * @return the RSSI
     */
    public byte getRSSI() {
        return rssi;
    }

    /**
     * @return the receiveOptions
     */
    public byte getReceiveOptions() {
        return receiveOptions;
    }

    /**
     * @return the packet
     */
    public byte[] getPacket() {
        return packet;
    }
}
