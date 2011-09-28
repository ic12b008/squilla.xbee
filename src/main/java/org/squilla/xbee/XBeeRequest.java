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

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class XBeeRequest implements Frame {

    public static final int DEFAULT_FRAME_ID = 1;
    public static final int DEFAULT_RETRY = 3;
    public static final int NO_RESPONSE_FRAME_ID = 0;

    private int frameType;
    private int frameID;
    private int retry;

    public XBeeRequest() {
        setRetry(DEFAULT_RETRY);
    }
    
    public void pull(FrameBuffer frameBuffer) {
        frameBuffer.setByteOrder(FrameBuffer.BO_BIG_ENDIAN);
        frameBuffer.putInt8(frameType);
        frameBuffer.putInt8(frameID);
    }

    public int quote() {
        return 2;
    }

    public final void drain(FrameBuffer frameBuffer) {
        throw new UnsupportedOperationException("Pull Only");
    }
    
    /**
     * @return the frameType
     */
    public int getFrameType() {
        return frameType;
    }

    /**
     * @param frameType the frameType to set
     */
    public void setFrameType(int frameType) {
        this.frameType = frameType;
    }

    /**
     * @return the frameId
     */
    public int getFrameID() {
        return frameID;
    }

    /**
     * @param frameID the frameId to set
     */
    public void setFrameID(int frameID) {
        this.frameID = frameID;
    }

    /**
     * @return the retry
     */
    public int getRetry() {
        return retry;
    }

    /**
     * @param retry the retry to set
     */
    public void setRetry(int retry) {
        this.retry = retry;
    }

    public int decrementRetry() {
        return retry--;
    }
}
