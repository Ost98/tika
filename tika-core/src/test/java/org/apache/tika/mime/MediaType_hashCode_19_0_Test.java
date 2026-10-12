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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MediaType_hashCode_19_0_Test {

    @Test
    public void testHashCode() throws Exception {
        // Arrange
        String type = "application";
        String subtype = "json";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("charset", "UTF-8");
        MediaType mediaType = new MediaType(type, subtype, parameters);
        Method hashCodeMethod = MediaType.class.getDeclaredMethod("hashCode");
        hashCodeMethod.setAccessible(true);
        // Act
        int result = (int) hashCodeMethod.invoke(mediaType);
        // Assert
        // CORRECTION IFT3913 (C20) : toString() inclut les parametres ; le LLM les avait oublies.
        assertEquals(type + '/' + subtype + "; charset=UTF-8", mediaType.toString());
        assertEquals(mediaType.toString().hashCode(), result);
    }
}
