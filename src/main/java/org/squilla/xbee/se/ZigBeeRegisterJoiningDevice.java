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
import org.squilla.xbee.XBeeDevice;
import org.squilla.xbee.s2.XBeeAddressingRequest;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class ZigBeeRegisterJoiningDevice extends XBeeAddressingRequest {

    private byte options;
    private byte[] key;
    
    public ZigBeeRegisterJoiningDevice() {
        setFrameType(XBeeDevice.API_ZB_REGIST_JOINING_DEV);
    }

    public void pull(FrameBuffer frameBuffer) {
        super.pull(frameBuffer);
        frameBuffer.putInt8(options);
        if (key != null) {
            frameBuffer.put(key);
        }
    }

    public int quote() {
        int q = super.quote() + 1;
        if (key != null) {
            q += key.length;
        }
        return q;
    }

    /**
     * @return the options
     */
    public byte getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(byte options) {
        this.options = options;
    }

    /**
     * @return the key
     */
    public byte[] getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(byte[] key) {
        this.key = key;
    }
}
