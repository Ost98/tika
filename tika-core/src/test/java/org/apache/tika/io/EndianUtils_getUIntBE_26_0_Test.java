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

public class EndianUtils_getUIntBE_26_0_Test {

    @Test
    public void testGetUIntBE() {
        // CORRECTION IFT3913 (C4) : le LLM croyait que getUIntBE lit 8 octets (car il retourne un long).
        // Il lit 4 octets non signes : on garde l'intention (valeur 1) avec un tableau de 4 octets.
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x01 };
        long expected = 1L;
        long result = EndianUtils.getUIntBE(data);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntBEWithOffset() {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        long expected = 1L;
        // CORRECTION IFT3913 (C5) : a l'offset 8 on lit {02,03,04,05}. La valeur 1 est aux octets 4..7 -> offset 4.
        long result = EndianUtils.getUIntBE(data, 4);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntBEWithNegativeValues() {
        // CORRECTION IFT3913 (C6) : meme confusion 8 octets ; les 4 octets lus doivent etre FF FF FF FE.
        byte[] data = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE };
        long expected = 4294967294L;
        long result = EndianUtils.getUIntBE(data);
        assertEquals(expected, result);
    }
}
