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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * IFT3913 tache 2 : tests ecrits a la main pour tuer les mutants de Patterns
 * restes vivants apres les tests originaux et les tests generes par ChatUniTest.
 * Contrairement au test genere, on utilise de vrais MimeType et un vrai registre
 * (comme PatternsTest) et on verifie le resultat de matches().
 */
public class PatternsMutationTest {

    private Patterns patterns;
    private MimeType text;
    private MimeType xml;
    private MimeType html;

    @BeforeEach
    public void setUp() throws MimeTypeException {
        patterns = new Patterns(new MediaTypeRegistry());
        MimeTypes types = new MimeTypes();
        text = types.forName("text/plain");
        xml = types.forName("application/xml");
        html = types.forName("text/html");
    }

    @Test
    public void testExactNameHasPriorityOverExtension() throws MimeTypeException {
        patterns.add("*.txt", text);
        patterns.add("README.txt", xml);
        assertSame(xml, patterns.matches("README.txt"));
        assertSame(text, patterns.matches("notes.txt"));
    }

    @Test
    public void testLongestExtensionWins() throws MimeTypeException {
        patterns.add("*.gz", text);
        patterns.add("*.tar.gz", xml);
        assertSame(xml, patterns.matches("archive.tar.gz"));
        assertSame(text, patterns.matches("file.gz"));
        assertNull(patterns.matches("file.zip"));
    }

    @Test
    public void testGlobWildcardsAndLiteralDot() throws MimeTypeException {
        // '*' = n'importe quelle suite, '?' = exactement un caractere, '.' = point litteral
        patterns.add("*.x?z", html);
        assertSame(html, patterns.matches("a.xyz"));
        assertNull(patterns.matches("aaxyz"));
        assertNull(patterns.matches("a.xyyz"));
    }

    @Test
    public void testJavaRegexPatternIsUsedAsIs() throws MimeTypeException {
        patterns.add("[0-9]+\\.dat", true, text);
        assertSame(text, patterns.matches("123.dat"));
        assertNull(patterns.matches("abc.dat"));
    }
}
