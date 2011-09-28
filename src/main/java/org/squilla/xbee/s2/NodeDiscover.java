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
import org.squilla.io.FrameBuffer;
import org.jazzkaffe.IEEEAddress;
import org.jazzkaffe.NetworkAddress;


/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class NodeDiscover {

    private NetworkAddress remoteAddress16;
    private IEEEAddress remoteAddress64;
    private String ni;
    private NetworkAddress parentAddress16;
    private byte deviceType;
    private byte status;
    private int profileID;
    private int manufacturerID;

    public static NodeDiscover parse(ATCommandResponse atResponse) {
        NodeDiscover nodeInfo = new NodeDiscover();
        byte[] data = atResponse.getData();
        FrameBuffer frameBuffer = new FrameBuffer(data);
        frameBuffer.setByteOrder(FrameBuffer.BO_BIG_ENDIAN);

        nodeInfo.remoteAddress16 = NetworkAddress.getByAddress(frameBuffer.getBytes(NetworkAddress.ADDRESS_SIZE));
        nodeInfo.remoteAddress64 = IEEEAddress.getByAddress(frameBuffer.getBytes(IEEEAddress.ADDRESS_SIZE));
        
        frameBuffer.mark();
        int strLen = 1;
        while (frameBuffer.getByte() != 0x00) {
            strLen++;
        }
        frameBuffer.reset();
        nodeInfo.ni = new String(frameBuffer.getBytes(strLen));
        
        nodeInfo.parentAddress16 = NetworkAddress.getByAddress(frameBuffer.getBytes(NetworkAddress.ADDRESS_SIZE));
        nodeInfo.deviceType = frameBuffer.getByte();
        nodeInfo.status = frameBuffer.getByte();
        nodeInfo.profileID = frameBuffer.getInt16();
        nodeInfo.manufacturerID = frameBuffer.getInt16();

        return nodeInfo;
    }

    /**
     * @return the remoteAddress16
     */
    public NetworkAddress getRemoteAddress16() {
        return remoteAddress16;
    }

    /**
     * @param remoteAddress16 the remoteAddress16 to set
     */
    public void setRemoteAddress16(NetworkAddress remoteAddress16) {
        this.remoteAddress16 = remoteAddress16;
    }

    /**
     * @return the remoteAddress64
     */
    public IEEEAddress getRemoteAddress64() {
        return remoteAddress64;
    }

    /**
     * @param remoteAddress64 the remoteAddress64 to set
     */
    public void setRemoteAddress64(IEEEAddress remoteAddress64) {
        this.remoteAddress64 = remoteAddress64;
    }

    /**
     * @return the ni
     */
    public String getNI() {
        return ni;
    }

    /**
     * @param ni the ni to set
     */
    public void setNI(String ni) {
        this.ni = ni;
    }

    /**
     * @return the parentAddress16
     */
    public NetworkAddress getParentAddress16() {
        return parentAddress16;
    }

    /**
     * @param parentAddress16 the parentAddress16 to set
     */
    public void setParentAddress16(NetworkAddress parentAddress16) {
        this.parentAddress16 = parentAddress16;
    }

    /**
     * @return the deviceType
     */
    public byte getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType the deviceType to set
     */
    public void setDeviceType(byte deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return the status
     */
    public byte getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(byte status) {
        this.status = status;
    }

    /**
     * @return the profileId
     */
    public int getProfileID() {
        return profileID;
    }

    /**
     * @param profileID the profileId to set
     */
    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    /**
     * @return the manufacturerId
     */
    public int getManufacturerID() {
        return manufacturerID;
    }

    /**
     * @param manufacturerID the manufacturerId to set
     */
    public void setManufacturerID(int manufacturerID) {
        this.manufacturerID = manufacturerID;
    }

    public boolean equals(Object obj) {
        if (obj instanceof NodeDiscover) {
            NodeDiscover e = (NodeDiscover) obj;
            if (e.remoteAddress16.equals(this.remoteAddress16) &&
                    e.remoteAddress64.equals(this.remoteAddress64)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return remoteAddress16.hashCode() + remoteAddress64.hashCode();
    }
}
