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

public class MediaType_application_0_0_Test {

    @Test
    public void testApplicationMethod() {
        try {
            // Get the private application method using reflection
            java.lang.reflect.Method applicationMethod = MediaType.class.getDeclaredMethod("application", String.class);
            applicationMethod.setAccessible(true);
            // Test with a valid type
            MediaType result1 = (MediaType) applicationMethod.invoke(null, "json");
            assertEquals("application/json", result1.toString());
            // Test with an empty type
            MediaType result2 = (MediaType) applicationMethod.invoke(null, "");
            assertNull(result2);
            // Test with a null type
            MediaType result3 = (MediaType) applicationMethod.invoke(null, null);
            assertNull(result3);
            // Test with a type containing spaces
            MediaType result4 = (MediaType) applicationMethod.invoke(null, " application/json ");
            assertEquals("application/json", result4.toString());
            // Test with a type containing special characters
            MediaType result5 = (MediaType) applicationMethod.invoke(null, "application/json; charset=UTF-8");
            assertEquals("application/json; charset=UTF-8", result5.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
