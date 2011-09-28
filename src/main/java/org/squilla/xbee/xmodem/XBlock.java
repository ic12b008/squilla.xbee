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
package org.squilla.xbee.xmodem;

import org.squilla.io.Frame;
import org.squilla.io.FrameBuffer;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class XBlock implements Frame {
    
    public static final int DATA_SIZE = 128;
    public static final byte CTRL_Z = 0x1A;
    private byte sequence;
    private byte[] data;

    public void pull(FrameBuffer fb) {
        fb.put(sequence);
        fb.put((byte) (0xFF - sequence & 0xFF));
        int dataLength = data.length;
        if (dataLength > DATA_SIZE) {
            dataLength = DATA_SIZE;
        }
        for (int i = 0; i < dataLength; i++) {
           fb.put(data[i]);
        }
        for (int i = 0; i < (DATA_SIZE - dataLength); i++) {
           fb.put(CTRL_Z);
        }
    }

    public int quote() {
        return 2 + DATA_SIZE;
    }

    public void drain(FrameBuffer fb) {
        this.sequence = fb.get();
        byte seqSub = fb.get();
        if (((sequence & seqSub) & 0xFF) != 0) {
            return;
        }
        data = new byte[DATA_SIZE];
        fb.get(data);
        int cs = 0;
        for (int i = 0; i < DATA_SIZE; i++) {
           cs += data[i];
        }
    }

    /**
     * @return the sequence
     */
    public byte getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(byte sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }
}
