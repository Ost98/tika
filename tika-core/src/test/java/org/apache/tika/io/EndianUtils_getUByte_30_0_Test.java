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

public class EndianUtils_getUByte_30_0_Test {

    @Test
    public void testGetUByte() {
        byte[] data = { (byte) 0xFF, (byte) 0x00, (byte) 0x01, (byte) 0x02 };
        int offset = 0;
        short expected = 0xFF;
        short result = EndianUtils.getUByte(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUByteWithOffset() {
        byte[] data = { (byte) 0x00, (byte) 0xFF, (byte) 0x01, (byte) 0x02 };
        int offset = 1;
        short expected = 0xFF;
        short result = EndianUtils.getUByte(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUByteWithNegativeOffset() {
        byte[] data = { (byte) 0x00, (byte) 0xFF, (byte) 0x01, (byte) 0x02 };
        int offset = -1;
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUByte(data, offset));
    }

    @Test
    public void testGetUByteWithLargeOffset() {
        byte[] data = { (byte) 0x00, (byte) 0xFF, (byte) 0x01, (byte) 0x02 };
        int offset = data.length;
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUByte(data, offset));
    }

    @Test
    public void testGetUByteWithNullData() {
        byte[] data = null;
        int offset = 0;
        assertThrows(NullPointerException.class, () -> EndianUtils.getUByte(data, offset));
    }
}
