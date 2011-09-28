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

import org.jazzkaffe.IEEEAddress;
import org.jazzkaffe.NetworkAddress;
import org.squilla.io.FrameBuffer;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class NodeIdentificationIndicator extends XBeeAddressingIndicator {

    private byte receiveOptions;
    private NetworkAddress remoteAddress16;
    private IEEEAddress remoteAddress64;
    private String ni;
    private NetworkAddress parentAddress16;
    private byte deviceType;
    private byte sourceEvent;
    private int profileID;
    private int manufacturerID;
    
    public void drain(FrameBuffer frameBuffer) {
        super.drain(frameBuffer);
        receiveOptions = frameBuffer.getByte();
        remoteAddress16 = NetworkAddress.getByAddress(frameBuffer.getBytes(NetworkAddress.ADDRESS_SIZE));
        remoteAddress64 = IEEEAddress.getByAddress(frameBuffer.getBytes(IEEEAddress.ADDRESS_SIZE));
        
        frameBuffer.mark();
        int strLen = 1;
        while (frameBuffer.getByte() != 0x00) {
            strLen++;
        }
        frameBuffer.reset();
        ni = new String(frameBuffer.getBytes(strLen));
        
        parentAddress16 = NetworkAddress.getByAddress(frameBuffer.getBytes(NetworkAddress.ADDRESS_SIZE));
        deviceType = frameBuffer.getByte();
        sourceEvent = frameBuffer.getByte();
        profileID = frameBuffer.getInt16();
        manufacturerID = frameBuffer.getInt16();
    }

    /**
     * @return the receiveOptions
     */
    public byte getReceiveOptions() {
        return receiveOptions;
    }
    
    /**
     * @return the remoteAddress64
     */
    public IEEEAddress getRemoteAddress64() {
        return remoteAddress64;
    }

    /**
     * @return the remoteAddress16
     */
    public NetworkAddress getRemoteAddress16() {
        return remoteAddress16;
    }

    /**
     * @return the ni
     */
    public String getNI() {
        return ni;
    }

    /**
     * @return the parentAddress16
     */
    public NetworkAddress getParentAddress16() {
        return parentAddress16;
    }

    /**
     * @return the deviceType
     */
    public byte getDeviceType() {
        return deviceType;
    }

    /**
     * @return the sourceEvent
     */
    public byte getSourceEvent() {
        return sourceEvent;
    }

    /**
     * @return the profileID
     */
    public int getProfileID() {
        return profileID;
    }

    /**
     * @return the manufacturerID
     */
    public int getManufacturerID() {
        return manufacturerID;
    }
}
