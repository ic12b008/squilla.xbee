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
import org.jazzkaffe.NetworkAddress;
import org.squilla.xbee.ATCommandResponse;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class NodeDiscover {

    private NetworkAddress remoteAddress16;
    private IEEEAddress remoteAddress64;
    private byte receivedSignalStrength;
    private String ni;

    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.remoteAddress64 != null ? this.remoteAddress64.hashCode() : 0);
        return hash;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof NodeDiscover) {
            return remoteAddress64.equals(((NodeDiscover) obj).remoteAddress64);
        }
        return false;
    }

    public static NodeDiscover parse(ATCommandResponse atResponse) {
        NodeDiscover nodeInfo = new NodeDiscover();
        byte[] data = atResponse.getData();
        FrameBuffer frameBuffer = new FrameBuffer(data);
        frameBuffer.setByteOrder(FrameBuffer.BO_BIG_ENDIAN);

        nodeInfo.remoteAddress16 = NetworkAddress.getByAddress(frameBuffer.getBytes(NetworkAddress.ADDRESS_SIZE));
        nodeInfo.remoteAddress64 = IEEEAddress.getByAddress(frameBuffer.getBytes(IEEEAddress.ADDRESS_SIZE));
        nodeInfo.receivedSignalStrength = frameBuffer.getByte();
        
        frameBuffer.mark();
        int strLen = 1;
        while (frameBuffer.getByte() != 0x00) {
            strLen++;
        }
        frameBuffer.reset();
        nodeInfo.ni = new String(frameBuffer.getBytes(strLen));

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
     * @return the receivedSignalStrength
     */
    public byte getReceivedSignalStrength() {
        return receivedSignalStrength;
    }

    /**
     * @param receivedSignalStrength the receivedSignalStrength to set
     */
    public void setReceivedSignalStrength(byte receivedSignalStrength) {
        this.receivedSignalStrength = receivedSignalStrength;
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

}
