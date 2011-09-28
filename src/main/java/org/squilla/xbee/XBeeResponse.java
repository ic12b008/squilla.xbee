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
package org.squilla.xbee;

import org.squilla.io.Frame;
import org.squilla.io.FrameBuffer;
import org.squilla.xbee.s1.ReceivePacket16;
import org.squilla.xbee.s1.ReceivePacket64;
import org.squilla.xbee.s1.TransmitStatus;
import org.squilla.xbee.s2.NodeIdentificationIndicator;
import org.squilla.xbee.s2.RemoteCommandResponse;
import org.squilla.xbee.s2.ZigBeeExplicitRxIndicator;
import org.squilla.xbee.s2.ZigBeeIODataSampleRXIndicator;
import org.squilla.xbee.s2.ZigBeeReceivePacket;
import org.squilla.xbee.s2.ZigBeeTransmitStatus;
import org.squilla.xbee.se.ZigBeeDeviceAuthenticatedIndicator;
import org.squilla.xbee.se.ZigBeeRegisterJoiningDeviceStatus;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class XBeeResponse implements Frame {

    protected int frameType;

    public final void pull(FrameBuffer frameBuffer) {
        throw new UnsupportedOperationException("Drain Only");
    }

    public int quote() {
        return 1;
    }

    public void drain(FrameBuffer frameBuffer) {
        frameBuffer.setByteOrder(FrameBuffer.BO_BIG_ENDIAN);
        frameType = frameBuffer.getInt8();
    }

    /**
     * @return the frameType
     */
    public int getFrameType() {
        return frameType;
    }

    public static XBeeResponse createXBeeResponse(int frameType) {
        XBeeResponse response = null;
        switch (frameType) {
        case XBeeDevice.API_MODEM_STATUS:
            response = new ModemStatus();
            break;
        case XBeeDevice.API_AT_COMMAND_RESP:
            response = new ATCommandResponse();
            break;
        case XBeeDevice.API_REMOTE_COMMAND_RESP:
            response = new RemoteCommandResponse();
            break;
        case XBeeDevice.API_ZB_IOD_SAMPLE_RX_IND:
            response = new ZigBeeIODataSampleRXIndicator();
            break;
        case XBeeDevice.API_NODE_ID_IND:
            response = new NodeIdentificationIndicator();
            break;
        case XBeeDevice.API_ZB_RECEIVE_PACKET:
            response = new ZigBeeReceivePacket();
            break;
        case XBeeDevice.API_RX_PACKET_64:  //802.15.4
            response = new ReceivePacket64();
            break;
        case XBeeDevice.API_RX_PACKET_16: //802.15.4
            response = new ReceivePacket16();
            break;
        case XBeeDevice.API_TX_STATUS: //802.15.4
            response = new TransmitStatus();
            break;
        case XBeeDevice.API_ZB_EXPLICIT_RX_IND:
            response = new ZigBeeExplicitRxIndicator();
            break;
        case XBeeDevice.API_ZB_TRANSMIT_STATUS:
            response = new ZigBeeTransmitStatus();
            break;
        case XBeeDevice.API_ZB_REGIST_JOINING_DEV_STAT:
            response = new ZigBeeRegisterJoiningDeviceStatus();
            break;
        case XBeeDevice.API_ZB_DEV_AUTH_IND:
            response = new ZigBeeDeviceAuthenticatedIndicator();
            break;
        }
        return response;
    }
}
