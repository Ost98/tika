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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.apache.tika.exception.TikaException;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_readUShortLE_2_0_Test {

    @Mock
    private InputStream mockStream;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testReadUShortLE() throws IOException, TikaException {
        // Arrange
        when(mockStream.read()).thenReturn(0x01, 0x02);
        // Act
        int result = EndianUtils.readUShortLE(mockStream);
        // Assert
        assertEquals(0x0201, result);
        verify(mockStream, times(2)).read();
    }

    @Test
    public void testReadUShortLEWithNegativeInput() throws IOException, TikaException {
        // Arrange
        // CORRECTION IFT3913 (C17) : 0xFF est un octet valide (255), pas une fin de flux. La fin de flux
        // est signalee par read() == -1, et Tika leve EndianUtils.BufferUnderrunException (pas BufferUnderflowException).
        when(mockStream.read()).thenReturn(0x01, -1);
        // Act & Assert
        assertThrows(EndianUtils.BufferUnderrunException.class, () -> EndianUtils.readUShortLE(mockStream));
        verify(mockStream, times(2)).read();
    }
}
