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
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.*;
import org.mockito.*;

public class MediaType_toString_17_0_Test {

    private MediaType mediaType;

    @BeforeEach
    public void setUp() {
        mediaType = new MediaType("text", "plain");
    }

    @Test
    public void testToString() {
        assertEquals("text/plain", mediaType.toString());
    }

    @Test
    public void testToStringWithParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("charset", "UTF-8");
        MediaType mediaTypeWithParams = new MediaType("text", "plain", parameters);
        assertEquals("text/plain; charset=UTF-8", mediaTypeWithParams.toString());
    }

    @Test
    public void testToStringWithEmptyParameters() {
        Map<String, String> parameters = Collections.emptyMap();
        MediaType mediaTypeWithEmptyParams = new MediaType("text", "plain", parameters);
        assertEquals("text/plain", mediaTypeWithEmptyParams.toString());
    }
}
