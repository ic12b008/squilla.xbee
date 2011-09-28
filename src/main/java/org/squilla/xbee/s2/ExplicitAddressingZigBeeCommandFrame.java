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

import org.squilla.io.FrameBuffer;
import org.squilla.xbee.XBeeDevice;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class ExplicitAddressingZigBeeCommandFrame extends XBeeAddressingRequest {

    // Cluster ID
    public final static short CLUSTER_BASIC = 0x0000;
    public final static short CLUSTER_TIME = 0x000A;
    public final static short CLUSTER_SIMPLEMETERING = 0x0702;

    private byte sourceEndpoint;
    private byte destinationEndpoint;
    private int clusterID;
    private int profileID;
    private byte broadcastRadius;
    private byte transmitOptions;
    private byte[] payload;
    
    public ExplicitAddressingZigBeeCommandFrame() {
        setFrameType(XBeeDevice.API_EXPLICIT_ADDR_ZB_CMD_FRM);
    }
    
    public void pull(FrameBuffer frameBuffer) {
        super.pull(frameBuffer);
        frameBuffer.putInt8(sourceEndpoint);
        frameBuffer.putInt8(destinationEndpoint);
        frameBuffer.putInt16(clusterID);
        frameBuffer.putInt16(profileID);
        frameBuffer.putInt8(broadcastRadius);
        frameBuffer.putInt8(transmitOptions);
        if (payload != null) {
            frameBuffer.put(payload);
        }
    }
    
    public int quote() {
        int q = super.quote() + 8;
        if (payload != null) {
            q += payload.length;
        }
        return q;
    }

    /**
     * @return the sourceEndpoint
     */
    public byte getSourceEndpoint() {
        return sourceEndpoint;
    }

    /**
     * @param sourceEndpoint the sourceEndpoint to set
     */
    public void setSourceEndpoint(byte sourceEndpoint) {
        this.sourceEndpoint = sourceEndpoint;
    }

    /**
     * @return the destinationEndpoint
     */
    public byte getDestinationEndpoint() {
        return destinationEndpoint;
    }

    /**
     * @param destinationEndpoint the destinationEndpoint to set
     */
    public void setDestinationEndpoint(byte destinationEndpoint) {
        this.destinationEndpoint = destinationEndpoint;
    }

    /**
     * @return the clusterID
     */
    public int getClusterID() {
        return clusterID;
    }

    /**
     * @param clusterID the clusterID to set
     */
    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }

    /**
     * @return the profileID
     */
    public int getProfileID() {
        return profileID;
    }

    /**
     * @param profileID the profileID to set
     */
    public void setProfileID(int profileID) {
        this.profileID = profileID;
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
     * @return the transmitOptions
     */
    public byte getTransmitOptions() {
        return transmitOptions;
    }

    /**
     * @param transmitOptions the transmitOptions to set
     */
    public void setTransmitOptions(byte transmitOptions) {
        this.transmitOptions = transmitOptions;
    }

    /**
     * @return the payload
     */
    public byte[] getPayload() {
        return payload;
    }

    /**
     * @param payload the payload to set
     */
    public void setPayload(byte[] payload) {
        this.payload = payload;
    }


}
