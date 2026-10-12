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

import java.util.Collections;

import org.junit.jupiter.api.*;
import org.mockito.*;

public class MediaType_equals_18_0_Test {

    @Test
    public void testEqualsDifferentObjects() {
        MediaType mediaType1 = new MediaType("application", "octet-stream");
        MediaType mediaType2 = new MediaType("application", "zip");
        assertFalse(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsSameObject() {
        MediaType mediaType1 = new MediaType("application", "octet-stream");
        assertTrue(mediaType1.equals(mediaType1));
    }

    @Test
    public void testEqualsNull() {
        MediaType mediaType1 = new MediaType("application", "octet-stream");
        assertFalse(mediaType1.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        MediaType mediaType1 = new MediaType("application", "octet-stream");
        assertFalse(mediaType1.equals("application/octet-stream"));
    }

    @Test
    public void testEqualsDifferentParameters() {
        MediaType mediaType1 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        MediaType mediaType2 = new MediaType("application", "octet-stream");
        assertFalse(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsSameParameters() {
        MediaType mediaType1 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        MediaType mediaType2 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        assertTrue(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsIgnoreCase() {
        MediaType mediaType1 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        MediaType mediaType2 = new MediaType("APPLICATION", "OCTET-STREAM", Collections.singletonMap("charset", "UTF-8"));
        assertTrue(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsDifferentCase() {
        MediaType mediaType1 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        MediaType mediaType2 = new MediaType("application", "zip", Collections.singletonMap("charset", "UTF-8"));
        assertFalse(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsDifferentCharset() {
        MediaType mediaType1 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        MediaType mediaType2 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "ISO-8859-1"));
        assertFalse(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsEmptyParameters() {
        MediaType mediaType1 = new MediaType("application", "octet-stream");
        MediaType mediaType2 = new MediaType("application", "octet-stream", Collections.emptyMap());
        assertTrue(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsNonEmptyParameters() {
        MediaType mediaType1 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        MediaType mediaType2 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        assertTrue(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsDifferentOrderParameters() {
        MediaType mediaType1 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        MediaType mediaType2 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        assertTrue(mediaType1.equals(mediaType2));
    }
}
