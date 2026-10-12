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

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_ubyteToInt_29_0_Test {

    // CORRECTION IFT3913 (C18) : suppression d'un mock InputStream et de ses stubbings, inutiles car
    // ubyteToInt(byte) ne lit aucun flux. Mockito (strict stubs) faisait echouer les 3 tests
    // avec UnnecessaryStubbingException.

    @Test
    public void testUbyteToInt() throws IOException {
        byte input = (byte) 0xFF;
        int result = EndianUtils.ubyteToInt(input);
        assertEquals(255, result);
    }

    @Test
    public void testUbyteToIntWithNegativeValue() throws IOException {
        byte input = (byte) -1;
        int result = EndianUtils.ubyteToInt(input);
        assertEquals(255, result);
    }

    @Test
    public void testUbyteToIntWithZeroValue() throws IOException {
        byte input = 0;
        int result = EndianUtils.ubyteToInt(input);
        assertEquals(0, result);
    }
}
