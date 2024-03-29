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
package org.squilla.xbee;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class XBeeUtil {
    
    public static byte calculateChecksum(byte[] data, int offset, int length) {
        int c = 0;
        for (int i = offset; i < offset + length; i++) {
            c += data[i] & 0xFF;
        }
        return (byte) (~c & 0xFF);
    }
    
    public static int measureScanDuration(int channels, int sd) {
        return (int) (channels * (0x01 << sd) * 15.36);
    }
}
