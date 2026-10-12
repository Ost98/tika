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

import org.apache.tika.exception.TikaException;

public class EndianUtils_readShortLE_0_0_Test {

    @Test
    public void testReadShortLE() throws IOException, TikaException {
        EndianUtils endianUtils = new EndianUtils();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x12, 0x34 });
        short result = endianUtils.readShortLE(inputStream);
        assertEquals(0x3412, result);
    }

    @Test
    public void testReadShortLEWithBufferUnderrun() throws IOException, TikaException {
        EndianUtils endianUtils = new EndianUtils();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x12 });
        // CORRECTION IFT3913 (C13) : le LLM attendait java.nio.BufferUnderflowException (classe du JDK au nom
        // proche). Tika leve sa propre exception EndianUtils.BufferUnderrunException.
        assertThrows(EndianUtils.BufferUnderrunException.class, () -> {
            endianUtils.readShortLE(inputStream);
        });
    }

    @Test
    public void testReadShortLEWithNegativeValues() throws IOException, TikaException {
        EndianUtils endianUtils = new EndianUtils();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0xFF, (byte) 0xFE });
        short result = endianUtils.readShortLE(inputStream);
        // CORRECTION IFT3913 (C14) : 0xFEFF est un int (65279). La methode retourne un short signe -> -257.
        assertEquals((short) 0xFEFF, result);
    }
}
