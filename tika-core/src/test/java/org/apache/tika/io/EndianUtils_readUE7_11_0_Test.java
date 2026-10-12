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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.*;
import org.mockito.*;

public class EndianUtils_readUE7_11_0_Test {

    @Test
    public void testReadUE7() throws IOException {
        // Test case 1: Normal input
        InputStream inputStream1 = new ByteArrayInputStream(new byte[] { (byte) 0x7F, (byte) 0x00 });
        assertEquals(127, EndianUtils.readUE7(inputStream1));
        // CORRECTION IFT3913 (C15) : le LLM n'a pas compris l'encodage. C'est le bit 0x80 qui signale
        // « un octet de plus » ; 0x7F termine donc la lecture des le 1er octet (resultat 127).
        // Donnees corrigees pour garder l'intention (valeurs sur plusieurs octets) :
        // Test case 2: Input with multiple bytes : (0x0F << 7) + 0x7F = 2047
        InputStream inputStream2 = new ByteArrayInputStream(new byte[] { (byte) 0x8F, (byte) 0x7F });
        assertEquals(2047, EndianUtils.readUE7(inputStream2));
        // Test case 3: Input with three bytes : ((1 << 7) + 0) << 7 + 0 = 16384
        InputStream inputStream3 = new ByteArrayInputStream(new byte[] { (byte) 0x81, (byte) 0x80, (byte) 0x00 });
        assertEquals(16384, EndianUtils.readUE7(inputStream3));
        // Test case 4: Input with buffer underun
        InputStream inputStream4 = new ByteArrayInputStream(new byte[] { (byte) 0x80 });
        assertThrows(IOException.class, () -> EndianUtils.readUE7(inputStream4), "Buffer underun; expected one more byte");
    }
}
