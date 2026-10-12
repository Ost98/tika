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

public class EndianUtils_getUShortBE_18_0_Test {

    @Test
    public void testGetUShortBE() {
        byte[] data = new byte[] { 0x01, 0x02 };
        int result = EndianUtils.getUShortBE(data);
        assertEquals(258, result);
    }

    @Test
    public void testGetUShortBEWithOffset() {
        byte[] data = new byte[] { 0x03, 0x04, 0x05, 0x06 };
        int result = EndianUtils.getUShortBE(data, 1);
        // CORRECTION IFT3913 (C7) : oracle copie-colle du test precedent (258). Octets 1..2 = {04,05} -> 0x0405.
        assertEquals(0x0405, result);
    }

    @Test
    public void testGetUShortBEWithEmptyArray() {
        byte[] data = new byte[] {};
        // CORRECTION IFT3913 (C8) : try/catch + comparaison de classe exacte. Echouait (la JVM leve la
        // sous-classe ArrayIndexOutOfBoundsException) et serait passe si aucune exception n'etait levee.
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUShortBE(data));
    }

    @Test
    public void testGetUShortBEWithShortArray() {
        byte[] data = new byte[] { 0x01 };
        // CORRECTION IFT3913 (C9) : meme probleme que C8.
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUShortBE(data));
    }
}
