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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

/**
 * IFT3913 tache 2 : tests ecrits a la main pour tuer les mutants de MediaType
 * restes vivants apres les tests originaux et les tests generes par ChatUniTest.
 */
public class MediaTypeMutationTest {

    @Test
    public void testFactoryMethodsBuildTypeOfTheirFamily() {
        assertEquals("application/xml", MediaType.application("xml").toString());
        assertEquals("audio/mpeg", MediaType.audio("mpeg").toString());
        assertEquals("text/html", MediaType.text("html").toString());
    }

    @Test
    public void testParseCharsetFirst() {
        // format inverse envoye par certains serveurs web (TIKA-350)
        MediaType type = MediaType.parse("charset=UTF-8; text/html");
        assertEquals("text/html; charset=UTF-8", type.toString());
    }

    @Test
    public void testAccessorsIgnoreParameters() {
        MediaType type = MediaType.parse("text/plain; charset=UTF-8");
        assertEquals("text", type.getType());
        assertEquals("plain", type.getSubtype());
        assertEquals(MediaType.TEXT_PLAIN, type.getBaseType());
        assertFalse(type.getBaseType().hasParameters());
    }

    @Test
    public void testAddParametersToTypeWithoutParameters() {
        MediaType type = new MediaType(MediaType.TEXT_PLAIN, Map.of("charset", "UTF-8"));
        assertEquals("text/plain; charset=UTF-8", type.toString());
    }

    @Test
    public void testAddNoParametersKeepsExistingOnes() {
        MediaType base = MediaType.parse("text/plain; charset=UTF-8");
        MediaType type = new MediaType(base, Collections.emptyMap());
        assertEquals("text/plain; charset=UTF-8", type.toString());
    }

    @Test
    public void testAddParametersMergesAndNewValueWins() {
        MediaType base = MediaType.parse("text/plain; charset=UTF-8; format=flowed");
        MediaType type = new MediaType(base, Map.of("charset", "ISO-8859-1"));
        // format conserve depuis base, charset remplace ; parametres tries par cle
        assertEquals("text/plain; charset=ISO-8859-1; format=flowed", type.toString());
    }

    @Test
    public void testSetOfTypesIgnoresNull() {
        Set<MediaType> set = MediaType.set(MediaType.TEXT_PLAIN, null, MediaType.TEXT_HTML);
        assertEquals(2, set.size());
        assertTrue(set.contains(MediaType.TEXT_PLAIN));
        assertTrue(set.contains(MediaType.TEXT_HTML));
    }

    @Test
    public void testSetOfStringsIgnoresUnparsable() {
        Set<MediaType> set = MediaType.set("text/plain", "pas-un-type", "text/html");
        assertEquals(2, set.size());
        assertTrue(set.contains(MediaType.TEXT_PLAIN));
        assertTrue(set.contains(MediaType.TEXT_HTML));
    }

    @Test
    public void testParseNormalizesUpperCase() {
        // les majuscules ne sont pas des « noms simples » : la chaine passe par l'expression
        // reguliere, qui normalise en minuscules. Type ET sous-type entierement en majuscules :
        // avec une seule minuscule, le chemin lent normaliserait meme si isSimpleName
        // acceptait a tort les majuscules.
        assertEquals("text/plain", MediaType.parse("TEXT/PLAIN").toString());
        assertEquals("text/plain", MediaType.parse("TEXT/Plain").toString());
    }

    /** compteur pour obtenir un nom jamais vu par le cache statique de MediaType */
    private static final AtomicInteger UNIQUE = new AtomicInteger();

    @Test
    public void testParseCachesSimpleNames() {
        // optimisation documentee dans parse() (« Optimization for the common cases ») :
        // un nom simple (minuscules, chiffres, - + . _) est mis en cache et le meme objet
        // est retourne. Le nom contient les bornes 'a', 'z', '0', '9' et est different a
        // chaque execution : pitest execute plusieurs mutants dans la meme JVM et le cache
        // statique SIMPLE_TYPES survit d'un mutant a l'autre (un nom fixe y serait deja).
        String name = "ift3913-az" + UNIQUE.incrementAndGet() + "/za09";
        MediaType first = MediaType.parse(name);
        assertSame(first, MediaType.parse(name));
    }
}
