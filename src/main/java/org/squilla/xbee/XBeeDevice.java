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

import java.io.IOException;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public interface XBeeDevice {
    
    public static final String DEVICE_CATEGORY_NAME = "org.squilla.xbee";

    public static final byte FRAME_DELIMITER = 0x7E;
    public static final int API_AT_COMMAND = 0x08;
    public static final int API_AT_COMMAND_QPV = 0x9;
    public static final int API_ZB_TRANSMIT_REQ = 0x10;
    public static final int API_EXPLICIT_ADDR_ZB_CMD_FRM = 0x11;
    public static final int API_REMOTE_COMMAND_REQ = 0x17;
    public static final int API_CREATE_SOURCE_ROUTE = 0x21;
    public static final int API_AT_COMMAND_RESP = 0x88;
    public static final int API_MODEM_STATUS = 0x8A;
    public static final int API_ZB_TRANSMIT_STATUS = 0x8B;
    public static final int API_ZB_RECEIVE_PACKET = 0x90;
    public static final int API_ZB_EXPLICIT_RX_IND = 0x91;
    public static final int API_ZB_IOD_SAMPLE_RX_IND = 0x92;
    public static final int API_XB_SENSOR_READ_IND = 0x94;
    public static final int API_NODE_ID_IND = 0x95;
    public static final int API_REMOTE_COMMAND_RESP = 0x97;
    public static final int API_OTA_FIRM_UPDATE_STATUS = 0xA0;
    public static final int API_ROUTE_REC_IND = 0xA1;
    public static final int API_TX_REQ_64 = 0x00;
    public static final int API_TX_REQ_16 = 0x01;
    public static final int API_TX_STATUS = 0x89;
    public static final int API_RX_PACKET_64 = 0x80;
    public static final int API_RX_PACKET_16 = 0x81;
    public static final int API_ZB_REGIST_JOINING_DEV = 0x24;
    public static final int API_ZB_DEV_AUTH_IND = 0xA2;
    public static final int API_ZB_REGIST_JOINING_DEV_STAT = 0xA4;
    public static final int VR_TYPE_MASK = 0x0F00;
    public static final int VR_COORDINATOR = 0x0100;
    public static final int VR_ROUTER = 0x0300;
    public static final int VR_END_DEVICE = 0x0900;
    public static final int VR_FIRM_MASK = 0xF000;
    public static final int VR_ZN = 0x1000;
    public static final int VR_ZB = 0x2000;
    public static final int VR_SE = 0x3000;
    public static final int HV_MASK = 0xFF00;
    public static final int HV_S1 = 0x1700;
    public static final int HV_S1_PRO = 0x1800;
    public static final int HV_S2 = 0x1900;
    public static final int HV_S2_PRO = 0x1A00;
    public static final int HV_S2B_PRO = 0x1E00;

    public void addDeviceListener(XBeeDeviceListener listener);

    public XBeeResponse syncSubmit(XBeeRequest request, int timeout) throws IOException;
    
    public int asyncSubmit(XBeeRequest request, boolean needResponse);
}
