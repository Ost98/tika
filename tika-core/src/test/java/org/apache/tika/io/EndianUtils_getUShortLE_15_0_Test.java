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

public class EndianUtils_getUShortLE_15_0_Test {

    @Test
    public void testGetUShortLE() {
        byte[] data = { 0x01, 0x02 };
        int offset = 0;
        EndianUtils endianUtils = new EndianUtils();
        assertEquals(513, endianUtils.getUShortLE(data, offset));
    }

    @Test
    public void testGetUShortLE_WithOffset() {
        byte[] data = { 0x03, 0x04, 0x05, 0x06 };
        int offset = 2;
        EndianUtils endianUtils = new EndianUtils();
        // CORRECTION IFT3913 (C10) : 1028 = 0x0404 ne correspond a rien. Octets 2..3 = {05,06} en LE -> 0x0605.
        assertEquals(0x0605, endianUtils.getUShortLE(data, offset));
    }

    @Test
    public void testGetUShortLE_WithNegativeOffset() {
        byte[] data = { 0x07, 0x08 };
        int offset = -1;
        EndianUtils endianUtils = new EndianUtils();
        // CORRECTION IFT3913 (C11) : suppression de la verification du message exact de la JVM
        // ("Index 2, size 2" etait invente et depend de l'implementation de Java).
        assertThrows(IndexOutOfBoundsException.class, () -> {
            endianUtils.getUShortLE(data, offset);
        });
    }

    @Test
    public void testGetUShortLE_WithTooLargeOffset() {
        byte[] data = { 0x09, 0x0A };
        int offset = 2;
        EndianUtils endianUtils = new EndianUtils();
        // CORRECTION IFT3913 (C12) : meme probleme que C11.
        assertThrows(IndexOutOfBoundsException.class, () -> {
            endianUtils.getUShortLE(data, offset);
        });
    }
}
