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

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.*;
import org.mockito.*;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.EndianUtils.BufferUnderrunException;

class EndianUtils_readUIntLE_4_0_Test {

    @Test
    void testReadUIntLE() throws IOException, TikaException {
        InputStream mockStream = mock(InputStream.class);
        // CORRECTION IFT3913 (C16) : en little-endian le 1er octet lu est le moins significatif.
        // {0,0,0,1} donne 0x01000000 ; pour obtenir 1 il faut lire {1,0,0,0}.
        when(mockStream.read()).thenReturn(1, 0, 0, 0);
        long result = EndianUtils.readUIntLE(mockStream);
        assertEquals(1, result);
    }

    @Test
    void testReadUIntLEBufferUnderrun() throws IOException {
        InputStream mockStream = mock(InputStream.class);
        when(mockStream.read()).thenReturn(0, 0, 0, -1);
        assertThrows(BufferUnderrunException.class, () -> EndianUtils.readUIntLE(mockStream));
    }
}
