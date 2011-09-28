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

import org.squilla.xbee.ATCommandResponse;
import org.squilla.xbee.ATCommand;
import org.jazzkaffe.IEEEAddress;
import org.jazzkaffe.NetworkAddress;
import org.squilla.io.FrameBuffer;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class RemoteCommandResponse extends ATCommandResponse {

    private IEEEAddress address64;
    private NetworkAddress address16;
    
    public void drain(FrameBuffer frameBuffer) {
        frameType = frameBuffer.getInt8();
        frameID = frameBuffer.getInt8();
        address64 = IEEEAddress.getByAddress(frameBuffer.getBytes(IEEEAddress.ADDRESS_SIZE));
        address16 = NetworkAddress.getByAddress(frameBuffer.getBytes(NetworkAddress.ADDRESS_SIZE));
        command = new String(frameBuffer.getBytes(ATCommand.COMMAND_SIZE));
        status = frameBuffer.getInt8();
        if (frameBuffer.getRemaining() > 0) {
            data = frameBuffer.getBytes(frameBuffer.getRemaining());
        }
    }

    /**
     * @return the address64
     */
    public IEEEAddress getAddress64() {
        return address64;
    }

    /**
     * @return the address16
     */
    public NetworkAddress getAddress16() {
        return address16;
    }
}
