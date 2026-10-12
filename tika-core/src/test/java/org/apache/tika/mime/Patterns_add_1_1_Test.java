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

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.*;
import org.mockito.*;

public class Patterns_add_1_1_Test {

    private Patterns patterns;

    private MediaTypeRegistry registry;

    private Map<String, MimeType> globs;

    private Map<String, MimeType> names;

    private Map<String, MimeType> extensions;

    @BeforeEach
    public void setUp() {
        registry = mock(MediaTypeRegistry.class);
        patterns = new Patterns(registry);
        globs = new HashMap<>();
        names = new HashMap<>();
        extensions = new HashMap<>();
    }

    @Test
    public void testAddWithConflictingNamePattern() throws MimeTypeException {
        MimeType type1 = mock(MimeType.class);
        when(type1.getType()).thenReturn(MediaType.parse("application/json"));
        MimeType type2 = mock(MimeType.class);
        when(type2.getType()).thenReturn(MediaType.parse("application/xml"));
        patterns.add("example.json", false, type1);
        assertThrows(MimeTypeException.class, () -> patterns.add("example.json", false, type2));
    }

    @Test
    public void testAddWithConflictingExtensionPattern() throws MimeTypeException {
        MimeType type1 = mock(MimeType.class);
        when(type1.getType()).thenReturn(MediaType.parse("application/json"));
        MimeType type2 = mock(MimeType.class);
        when(type2.getType()).thenReturn(MediaType.parse("application/xml"));
        patterns.add("*.json", false, type1);
        assertThrows(MimeTypeException.class, () -> patterns.add("*.json", false, type2));
    }

    @Test
    public void testAddWithConflictingGlobPattern() throws MimeTypeException {
        MimeType type1 = mock(MimeType.class);
        when(type1.getType()).thenReturn(MediaType.parse("application/json"));
        MimeType type2 = mock(MimeType.class);
        when(type2.getType()).thenReturn(MediaType.parse("application/xml"));
        patterns.add("example.*", false, type1);
        assertThrows(MimeTypeException.class, () -> patterns.add("example.*", false, type2));
    }

    @Test
    public void testAddWithNullPattern() {
        MimeType type = mock(MimeType.class);
        when(type.getType()).thenReturn(MediaType.parse("application/json"));
        assertThrows(IllegalArgumentException.class, () -> patterns.add(null, false, type));
    }
}
