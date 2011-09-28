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

import org.squilla.xbee.XBeeFrameIDResponse;
import org.squilla.io.FrameBuffer;
import org.jazzkaffe.NetworkAddress;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class ZigBeeTransmitStatus extends XBeeFrameIDResponse {

    // Delivery Status
    public static final byte STATUS_SUCCESS = 0x00;
    public static final byte STATUS_MAC_ACK_FAILURE = 0x01;
    public static final byte STATUS_CCA_FAILURE = 0x02;
    public static final byte STATUS_INVAILD_DESTINATION_ENDPOINT = 0x15;
    public static final byte STATUS_NETWORK_ACK_FAILURE = 0x21;
    public static final byte STATUS_NOT_JOINED_NETWORK = 0x22;
    public static final byte STATUS_SELF_ADDRESSED = 0x23;
    public static final byte STATUS_ADDRESS_NOT_FOUND = 0x24;
    public static final byte STATUS_ROUTE_NOT_FOUND = 0x25;
    public static final byte STATUS_BROADCAST_SOURCE_FAILED = 0x26;
    public static final byte STATUS_INVALID_BINDING_TABLE_INDEX = 0x2B;
    public static final byte STATUS_RESOURCE_ERROR_1 = 0x2C;
    public static final byte STATUS_ATTEMPTED_BROADCAST_APS = 0x2D;
    public static final byte STATUS_ATTEMPTED_UNICAST_APS = 0x2E; // EE = 0
    public static final byte STATUS_RESOURCE_ERROR_2 = 0x32;
    public static final byte STATUS_DATA_PAYLOAD_TOO_LARGE = 0x74;
    public static final byte STATUS_INDIRECT_MESSAGE_UNREQUEATED = 0x75;
    
    // Discovery Status
    public static final byte STATUS_NO_DISCOVERY = 0x00;
    public static final byte STATUS_ADDRESS_DISCOVERY = 0x01;
    public static final byte STATUS_ROUTE_DISCOVERY = 0x02;
    public static final byte STATUS_ADDRESS_AND_ROUTE = 0x03;
    public static final byte STATUS_EXTENDED_TIMEOUT = 0x04;
    
    private NetworkAddress address16;
    private int transmitRetryCount;
    private byte deliveryStatus;
    private byte discoveryStatus;
    
    public void drain(FrameBuffer frameBuffer) {
        super.drain(frameBuffer);
        address16 = NetworkAddress.getByAddress(frameBuffer.getBytes(NetworkAddress.ADDRESS_SIZE));
        transmitRetryCount = frameBuffer.getInt8();
        deliveryStatus = frameBuffer.getByte();
        discoveryStatus = frameBuffer.getByte();
    }

    /**
     * @return the address16
     */
    public NetworkAddress getAddress16() {
        return address16;
    }

    /**
     * @return the transmitRetryCount
     */
    public int getTransmitRetryCount() {
        return transmitRetryCount;
    }

    /**
     * @return the deliveryStatus
     */
    public byte getDeliveryStatus() {
        return deliveryStatus;
    }

    /**
     * @return the discoveryStatus
     */
    public byte getDiscoveryStatus() {
        return discoveryStatus;
    }
}
