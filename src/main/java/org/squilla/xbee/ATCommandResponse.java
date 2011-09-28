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
public class ATCommandResponse extends XBeeFrameIDResponse {

    public static final int STATUS_OK = 0;
    public static final int STATUS_ERROR = 1;
    public static final int STATUS_INVALID_COMMAND = 2;
    public static final int STATUS_INVALID_PARAMETER = 3;
    protected String command;
    protected int status;
    protected byte[] data;
    
    public void drain(FrameBuffer frameBuffer) {
        super.drain(frameBuffer);
        command = new String(frameBuffer.getBytes(ATCommand.COMMAND_SIZE));
        status = frameBuffer.getInt8();
        if (frameBuffer.getRemaining() > 0) {
            data = frameBuffer.getBytes(frameBuffer.getRemaining());
        }
    }

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }
}
