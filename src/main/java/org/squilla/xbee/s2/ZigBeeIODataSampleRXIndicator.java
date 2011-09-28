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
package org.squilla.xbee.s2;

import org.squilla.io.FrameBuffer;
import org.squilla.util.Commons;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class ZigBeeIODataSampleRXIndicator extends XBeeAddressingIndicator {

    public static final int AD0 = 0x01;
    public static final int AD1 = 0x02;
    public static final int AD2 = 0x04;
    public static final int AD3 = 0x08;
    public static final int SV = 0x80;
    private byte receiveOptions;
    private byte numSamples;
    private int digitalChannelMask;
    private int analogChannelMask;
    private int digitalSample;
    private int[] analogSample;
    
    public void drain(FrameBuffer frameBuffer) {
        super.drain(frameBuffer);
        receiveOptions = frameBuffer.getByte();
        numSamples = frameBuffer.getByte();
        digitalChannelMask = frameBuffer.getInt16();
        analogChannelMask = frameBuffer.getInt8();
        
        if (digitalChannelMask > 0) {
            digitalSample = frameBuffer.getInt16();
        }
        
        if (analogChannelMask > 0) {
            int num = Commons.countBit(analogChannelMask);
            analogSample = new int[num];
            for (int i = 0; i < num; i++) {
                analogSample[i] = frameBuffer.getInt16();
            }
        }
    }

    /**
     * @return the receiveOption
     */
    public byte getReceiveOptions() {
        return receiveOptions;
    }

    /**
     * @return the numSamples
     */
    public byte getNumSamples() {
        return numSamples;
    }

    /**
     * @return the digitalChannelMask
     */
    public int getDigitalChannelMask() {
        return digitalChannelMask;
    }

    /**
     * @return the analogChannelMask
     */
    public int getAnalogChannelMask() {
        return analogChannelMask;
    }

    /**
     * @return the digitalSample
     */
    public int getDigitalSample() {
        return digitalSample;
    }

    /**
     * @return the analogSample
     */
    public int[] getAnalogSample() {
        return analogSample;
    }
}
