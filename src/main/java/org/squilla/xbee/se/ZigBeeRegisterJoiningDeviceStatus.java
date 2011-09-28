/*
 * Copyright 2011 Shotaro Uchida <fantom@xmaker.mx>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.squilla.xbee.se;

import org.squilla.io.FrameBuffer;
import org.squilla.xbee.XBeeFrameIDResponse;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class ZigBeeRegisterJoiningDeviceStatus extends XBeeFrameIDResponse {

    public static final int DEVSTS_SUCCESS = 0;
    // 3x19 Only
    public static final int DEVSTS_ADDRESS_INVALID = 0xB3;
    public static final int DEVSTS_KEY_NOTFOUND = 0xFF;
    // 3x1A Only
    public static final int DEVSTS_KEY_TOO_LONG = 0x01;
    public static final int DEVSTS_ADDRESS_NOTFOUND = 0xB1;
    public static final int DEVSTS_KEY_INVALID = 0xB2;
    public static final int DEVSTS_KEYTABLE_FULL = 0xB4;
    private int status;

    public void drain(FrameBuffer frameBuffer) {
        super.drain(frameBuffer);
        status = frameBuffer.getInt8();
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }
}
