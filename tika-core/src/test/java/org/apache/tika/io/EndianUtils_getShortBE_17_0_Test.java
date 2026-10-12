/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika.io;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

public class EndianUtils_getShortBE_17_0_Test {

    @Test
    public void testGetShortBE() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 0;
        // CORRECTION IFT3913 (C2) : ordre little-endian attendu par erreur (0x0201) ; big-endian = 0x0102.
        short expected = (short) 0x0102;
        short result = EndianUtils.getShortBE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetShortBEWithOffset() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 2;
        // CORRECTION IFT3913 (C3) : meme erreur d'ordre des octets (0x0403) ; big-endian = 0x0304.
        short expected = (short) 0x0304;
        short result = EndianUtils.getShortBE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetShortBEWithEmptyArray() {
        byte[] data = {};
        int offset = 0;
        assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getShortBE(data, offset);
        });
    }

    @Test
    public void testGetShortBEWithNegativeOffset() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = -1;
        assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getShortBE(data, offset);
        });
    }

    @Test
    public void testGetShortBEWithOffsetGreaterThanArrayLength() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = data.length;
        assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getShortBE(data, offset);
        });
    }
}
