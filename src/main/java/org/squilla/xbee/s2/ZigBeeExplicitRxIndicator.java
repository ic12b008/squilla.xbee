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

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class ZigBeeExplicitRxIndicator extends XBeeAddressingIndicator {

    static final byte ACK = 0x01;
    static final byte BROAD_CAST_PACKET = 0x02;
    static final byte ENCRYPTED_APS_ENCRYPTION = 0x03;
    static final byte SENT_FROM_ENDDEVICE = 0x04;
    private byte sourceEndpoint;
    private byte destinationEndpoint;
    private int clusterID;
    private int profileID;
    private byte receiveOptions;
    private byte[] payload;
    
    public void drain(FrameBuffer frameBuffer) {
        super.drain(frameBuffer);
        sourceEndpoint = frameBuffer.getByte();
        destinationEndpoint = frameBuffer.getByte();
        clusterID = frameBuffer.getInt16();
        profileID = frameBuffer.getInt16();
        receiveOptions = frameBuffer.getByte();
        if (frameBuffer.getRemaining() > 0) {
            payload = frameBuffer.getBytes(frameBuffer.getRemaining());
        }
    }

    /**
     * @return the sourceEndpoint
     */
    public byte getSourceEndpoint() {
        return sourceEndpoint;
    }

    /**
     * @return the destinationEndpoint
     */
    public byte getDestinationEndpoint() {
        return destinationEndpoint;
    }

    /**
     * @return the clusterID
     */
    public int getClusterID() {
        return clusterID;
    }

    /**
     * @return the profileID
     */
    public int getProfileID() {
        return profileID;
    }

    /**
     * @return the receiveOptions
     */
    public byte getReceiveOptions() {
        return receiveOptions;
    }

    /**
     * @return the receivedData
     */
    public byte[] getPayload() {
        return payload;
    }
}
