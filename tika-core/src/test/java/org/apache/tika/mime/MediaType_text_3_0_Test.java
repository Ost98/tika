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

public class MediaType_text_3_0_Test {

    @Test
    public void testTextMethod() {
        try {
            // Create a MediaType instance using reflection
            Class<?> mediaTypeClass = Class.forName("org.apache.tika.mime.MediaType");
            java.lang.reflect.Method textMethod = mediaTypeClass.getDeclaredMethod("text", String.class);
            textMethod.setAccessible(true);
            // Invoke the text method
            Object result = textMethod.invoke(null, "plain");
            // Verify the result
            assertEquals("text/plain", result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
