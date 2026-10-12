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
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;

public class EndianUtils_getUIntLE_25_0_Test {

    private EndianUtils endianUtils;

    @BeforeEach
    public void setUp() {
        endianUtils = new EndianUtils();
    }

    @Test
    public void testGetUIntLE() {
        byte[] data = { 0x00, 0x00, 0x00, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        int offset = 4;
        long expected = 0xFFFFFFFFL;
        long result = endianUtils.getUIntLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntLEWithZeroOffset() {
        byte[] data = { 0x00, 0x00, 0x00, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        int offset = 0;
        long expected = 0x01000000L;
        long result = endianUtils.getUIntLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntLEWithNegativeOffset() {
        byte[] data = { 0x00, 0x00, 0x00, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        int offset = -4;
        Executable executable = () -> endianUtils.getUIntLE(data, offset);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }

    @Test
    public void testGetUIntLEWithOffsetGreaterThanArrayLength() {
        byte[] data = { 0x00, 0x00, 0x00, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        int offset = 8;
        Executable executable = () -> endianUtils.getUIntLE(data, offset);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }
}
