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

public class MediaType_compareTo_20_0_Test {

    @Test
    public void testCompareTo() {
        // CORRECTION IFT3913 (C19) : (1) le contrat de Comparable garantit seulement un resultat negatif,
        // pas -1 (ici -14 = 'j' - 'x') ; (2) suppression du try/catch(Exception) qui avalait les erreurs.
        // Create two MediaType objects
        MediaType mediaType1 = new MediaType("application", "json");
        MediaType mediaType2 = new MediaType("application", "xml");
        // Compare the two MediaType objects
        int result = mediaType1.compareTo(mediaType2);
        // Assert that the result is as expected
        assertTrue(result < 0);
        // Create another(MediaType objects
        MediaType mediaType3 = new MediaType("application", "json");
        // Compare the two MediaType objects
        result = mediaType1.compareTo(mediaType3);
        // Assert that the result is as expected
        assertEquals(0, result);
    }
}
