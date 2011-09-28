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
public class ATCommand extends XBeeRequest {
    
    public static final int COMMAND_SIZE = 2;

    private String command;
    private byte[] value;

    public ATCommand() {
        setFrameType(XBeeDevice.API_AT_COMMAND);
    }
    
    public ATCommand(String command) {
        this();
        setCommand(command);
    }

    public void pull(FrameBuffer frameBuffer) {
        super.pull(frameBuffer);
        frameBuffer.putInt8(command.charAt(0));
        frameBuffer.putInt8(command.charAt(1));
        if (value != null) {
            frameBuffer.put(value);
        }
    }

    public int quote() {
        int q = super.quote() + COMMAND_SIZE;
        if (value != null) {
            q += value.length;
        }
        return q;
    }
    
    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * @param command the command to set
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @return the value
     */
    public byte[] getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(byte[] value) {
        this.value = value;
    }
}
