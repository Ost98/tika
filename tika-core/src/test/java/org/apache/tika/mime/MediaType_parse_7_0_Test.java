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

import org.junit.jupiter.api.*;
import org.mockito.*;

public class MediaType_parse_7_0_Test {

    @Test
    public void testParseNull() throws Exception {
        Method method = MediaType.class.getDeclaredMethod("parse", String.class);
        method.setAccessible(true);
        assertNull(method.invoke(null, (String) null));
    }

    @Test
    public void testParseInvalid() throws Exception {
        Method method = MediaType.class.getDeclaredMethod("parse", String.class);
        method.setAccessible(true);
        assertNull(method.invoke(null, "invalid"));
    }

    @Test
    public void testParseSimple() throws Exception {
        Method method = MediaType.class.getDeclaredMethod("parse", String.class);
        method.setAccessible(true);
        MediaType mediaType = (MediaType) method.invoke(null, "text/plain");
        assertEquals("text/plain", mediaType.toString());
    }

    @Test
    public void testParseComplex() throws Exception {
        Method method = MediaType.class.getDeclaredMethod("parse", String.class);
        method.setAccessible(true);
        MediaType mediaType = (MediaType) method.invoke(null, "application/xml; charset=UTF-8");
        assertEquals("application/xml; charset=UTF-8", mediaType.toString());
    }

    @Test
    public void testParseOptimization() throws Exception {
        Method method = MediaType.class.getDeclaredMethod("parse", String.class);
        method.setAccessible(true);
        MediaType mediaType1 = (MediaType) method.invoke(null, "text/plain");
        MediaType mediaType2 = (MediaType) method.invoke(null, "text/plain");
        assertSame(mediaType1, mediaType2);
    }

    @Test
    public void testParseNoOptimization() throws Exception {
        Method method = MediaType.class.getDeclaredMethod("parse", String.class);
        method.setAccessible(true);
        MediaType mediaType1 = (MediaType) method.invoke(null, "text/plain");
        MediaType mediaType2 = (MediaType) method.invoke(null, "text/plain; charset=UTF-8");
        assertNotSame(mediaType1, mediaType2);
    }
}
