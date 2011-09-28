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
package org.squilla.xbee.s2;

import org.squilla.xbee.XBeeRequest;
import org.squilla.io.FrameBuffer;
import org.jazzkaffe.IEEEAddress;
import org.jazzkaffe.NetworkAddress;
import org.squilla.xbee.XBeeRequest;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class XBeeAddressingRequest extends XBeeRequest {
    
    public static final IEEEAddress XBEE_BROADCAST64 = IEEEAddress.getByAddress(new byte[] {
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xFF,
        (byte) 0xFF
    });
    public static final NetworkAddress XBEE_BROADCAST16 = NetworkAddress.getByAddress(new byte[] {
        (byte) 0xFF,
        (byte) 0xFE
    });

    private IEEEAddress address64;
    private NetworkAddress address16;

    public void pull(FrameBuffer frameBuffer) {
        super.pull(frameBuffer);
        frameBuffer.put(address64.array());
        frameBuffer.put(address16.array());
    }
    
    public int quote() {
        return super.quote() + 10;
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
     * @return the address16
     */
    public NetworkAddress getAddress16() {
        return address16;
    }

    /**
     * @param address16 the address16 to set
     */
    public void setAddress16(NetworkAddress address16) {
        this.address16 = address16;
    }
}
