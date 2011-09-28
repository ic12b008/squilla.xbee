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

import org.squilla.io.FrameBuffer;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class ModemStatus extends XBeeResponse {

    public static final int MDMSTS_HW_RESET = 0;
    public static final int MDMSTS_WDT_RESET = 1;
    public static final int MDMSTS_ASSOCIATED = 2;
    public static final int MDMSTS_DISASSOCIATED = 3;
    public static final int MDMSTS_SYNC_LOST = 4;           // Obsolete
    public static final int MDMSTS_COORDINATOR_RALGN = 5;   // Obsolete
    public static final int MDMSTS_COORDINATOR_STARTED = 6;
    public static final int MDMSTS_VSUPPLY_LIMIT_EXCEED = 0x0D;
    public static final int MDMSTS_CONFIG_CHANGED = 0x11;
    public static final int MDMSTS_SE_KEYEST_COMPLETE = 0x10;
    public static final int MDMSTS_STACK_ERROR = 0x80;
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
