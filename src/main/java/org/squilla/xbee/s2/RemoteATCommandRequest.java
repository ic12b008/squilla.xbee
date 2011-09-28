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

import org.squilla.xbee.ATCommand;
import org.jazzkaffe.IEEEAddress;
import org.jazzkaffe.NetworkAddress;
import org.squilla.xbee.XBeeDevice;
import org.squilla.io.FrameBuffer;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class RemoteATCommandRequest extends ATCommand {
    
    private IEEEAddress address64;
    private NetworkAddress address16;
    private byte remoteCommandOptions;

    public RemoteATCommandRequest() {
        setFrameType(XBeeDevice.API_REMOTE_COMMAND_REQ);
    }
    
    public void pull(FrameBuffer frameBuffer) {
        frameBuffer.putInt8(getFrameType());
        frameBuffer.putInt8(getFrameID());
        frameBuffer.put(address64.array());
        frameBuffer.put(address16.array());
        frameBuffer.put(remoteCommandOptions);
        frameBuffer.putInt8(getCommand().charAt(0));
        frameBuffer.putInt8(getCommand().charAt(1));
        if (getValue() != null) {
            frameBuffer.put(getValue());
        }
    }

    public int quote() {
        int q = super.quote() + 11 + ATCommand.COMMAND_SIZE;
        if (getValue() != null) {
            q += getValue().length;
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

    /**
     * @return the remoteCommandOptions
     */
    public byte getRemoteCommandOptions() {
        return remoteCommandOptions;
    }

    /**
     * @param remoteCommandOptions the remoteCommandOptions to set
     */
    public void setRemoteCommandOptions(byte remoteCommandOptions) {
        this.remoteCommandOptions = remoteCommandOptions;
    }
}
