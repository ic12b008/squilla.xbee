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
import org.squilla.xbee.XBeeFrameIDResponse;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class TransmitStatus extends XBeeFrameIDResponse {

    public static final byte STATUS_SUCCESS = 0;
    public static final byte STATUS_NO_ACK = 1;
    public static final byte STATUS_CCA_FAILURE = 2;
    public static final byte STATUS_PURGED = 3;
    
    private byte status;

    public void drain(FrameBuffer frameBuffer) {
        super.drain(frameBuffer);
        this.status = frameBuffer.getByte();

    }

    /**
     * @return the status
     */
    public byte getStatus() {
        return status;
    }
}
