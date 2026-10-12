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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_getIntBE_22_0_Test {

    @BeforeEach
    public void setUp() {
        // Setup if needed
    }

    @Test
    public void testGetIntBE() {
        // Test case 1: Normal case
        // CORRECTION IFT3913 (C1) : le LLM attendait l'ordre little-endian (0x04030201).
        // En big-endian, le premier octet est le plus significatif.
        byte[] data1 = { 0x01, 0x02, 0x03, 0x04 };
        assertEquals(0x01020304, EndianUtils.getIntBE(data1));
        // Test case 2: Different byte order
        byte[] data2 = { 0x04, 0x03, 0x02, 0x01 };
        assertEquals(0x04030201, EndianUtils.getIntBE(data2));
        // Test case 3: Edge case - empty array
        byte[] data3 = {};
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getIntBE(data3));
        // Test case 4: Edge case - array with less than 4 bytes
        byte[] data4 = { 0x01, 0x02 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getIntBE(data4));
        // Test case 5: Edge case - array with exactly 4 bytes
        byte[] data5 = { 0x01, 0x02, 0x03, 0x04 };
        assertEquals(0x01020304, EndianUtils.getIntBE(data5));
    }
}
