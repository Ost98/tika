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
package org.apache.tika.mime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

public class MediaType_image_2_0_Test {

    @Test
    public void testImageMethodWithValidType() throws Exception {
        String type = "png";
        MediaType result = invokePrivateMethod("image", type);
        assertEquals("image/png", result.toString());
    }

    @Test
    public void testImageMethodWithNullType() throws Exception {
        String type = null;
        MediaType result = invokePrivateMethod("image", type);
        // CORRECTION IFT3913 (C21) : image(null) concatene "image/" + null puis parse -> "image/null".
        // Aucune specification (javadoc) ne dit que null doit donner null : oracle aligne sur le
        // comportement observe (test de caracterisation).
        assertEquals("image/null", result.toString());
    }

    @Test
    public void testImageMethodWithEmptyType() throws Exception {
        String type = "";
        MediaType result = invokePrivateMethod("image", type);
        assertNull(result);
    }

    @Test
    public void testImageMethodWithWhitespaceType() throws Exception {
        String type = "   ";
        MediaType result = invokePrivateMethod("image", type);
        assertNull(result);
    }

    @Test
    public void testImageMethodWithInvalidType() throws Exception {
        String type = "invalidType";
        MediaType result = invokePrivateMethod("image", type);
        // CORRECTION IFT3913 (C22) : MediaType normalise le type en minuscules (Locale.ENGLISH).
        assertEquals("image/invalidtype", result.toString());
    }

    private MediaType invokePrivateMethod(String methodName, String type) throws Exception {
        Class<?> clazz = MediaType.class;
        java.lang.reflect.Method method = clazz.getDeclaredMethod(methodName, String.class);
        method.setAccessible(true);
        return (MediaType) method.invoke(null, type);
    }
}
