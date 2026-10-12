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
package org.apache.tika.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

/**
 * IFT3913 tache 2 : tests ecrits a la main pour tuer les mutants d'EndianUtils
 * restes vivants apres les tests originaux et les tests generes par ChatUniTest.
 * <p>
 * Strategie commune aux methodes read* (voir readme.md, section tests manuels) :
 * <ul>
 * <li>valeur : octets tous differents et non nuls, pour que toute mutation d'un decalage
 * ou d'une addition change le resultat ;</li>
 * <li>zeros : tous les octets a 0, pour la limite de la condition {@code (ch1 | ...) < 0} ;</li>
 * <li>flux trop court : le dernier octet manque, pour la detection de fin de flux.</li>
 * </ul>
 */
public class EndianUtilsMutationTest {

    private static InputStream stream(int... bytes) {
        byte[] data = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            data[i] = (byte) bytes[i];
        }
        return new ByteArrayInputStream(data);
    }

    // ---------------------------------------------------------------- readLongLE / readLongBE

    @Test
    public void testReadLongLEDistinctBytes() throws Exception {
        // le 1er octet lu est le moins significatif ; 0x84 en 4e position verifie le cast
        // en long qui preserve le bit 31
        assertEquals(0x0807060584030201L,
                EndianUtils.readLongLE(stream(0x01, 0x02, 0x03, 0x84, 0x05, 0x06, 0x07, 0x08)));
    }

    @Test
    public void testReadLongBEDistinctBytes() throws Exception {
        // le 1er octet lu est le plus significatif ; 0x85 en 5e position (decalage de 24)
        assertEquals(0x0102030485060708L,
                EndianUtils.readLongBE(stream(0x01, 0x02, 0x03, 0x04, 0x85, 0x06, 0x07, 0x08)));
    }

    @Test
    public void testReadLongAllZeroBytes() throws Exception {
        assertEquals(0L, EndianUtils.readLongLE(stream(0, 0, 0, 0, 0, 0, 0, 0)));
        assertEquals(0L, EndianUtils.readLongBE(stream(0, 0, 0, 0, 0, 0, 0, 0)));
    }

    @Test
    public void testReadLongStreamTooShort() {
        assertThrows(EndianUtils.BufferUnderrunException.class,
                () -> EndianUtils.readLongLE(stream(1, 2, 3, 4, 5, 6, 7)));
        assertThrows(EndianUtils.BufferUnderrunException.class,
                () -> EndianUtils.readLongBE(stream(1, 2, 3, 4, 5, 6, 7)));
    }

    // ---------------------------------------------------------------- readIntLE / readIntBE

    @Test
    public void testReadIntLEDistinctBytes() throws Exception {
        // 0x84 en dernier : le resultat est un int negatif
        assertEquals(0x84030201, EndianUtils.readIntLE(stream(0x01, 0x02, 0x03, 0x84)));
    }

    @Test
    public void testReadIntBEDistinctBytes() throws Exception {
        assertEquals(0x81020304, EndianUtils.readIntBE(stream(0x81, 0x02, 0x03, 0x04)));
    }

    @Test
    public void testReadIntAllZeroBytes() throws Exception {
        assertEquals(0, EndianUtils.readIntLE(stream(0, 0, 0, 0)));
        assertEquals(0, EndianUtils.readIntBE(stream(0, 0, 0, 0)));
        assertEquals(0, EndianUtils.readIntME(stream(0, 0, 0, 0)));
    }

    @Test
    public void testReadIntStreamTooShort() {
        assertThrows(EndianUtils.BufferUnderrunException.class,
                () -> EndianUtils.readIntLE(stream(1, 2, 3)));
        assertThrows(EndianUtils.BufferUnderrunException.class,
                () -> EndianUtils.readIntBE(stream(1, 2, 3)));
    }

    // ---------------------------------------------------------------- readUIntLE / readUIntBE

    @Test
    public void testReadUIntBEHighBitIsUnsigned() throws Exception {
        // 0x87654321 est negatif en int ; la version non signee doit rester positive
        assertEquals(0x87654321L, EndianUtils.readUIntBE(stream(0x87, 0x65, 0x43, 0x21)));
    }

    @Test
    public void testReadUIntAllZeroBytes() throws Exception {
        assertEquals(0L, EndianUtils.readUIntLE(stream(0, 0, 0, 0)));
        assertEquals(0L, EndianUtils.readUIntBE(stream(0, 0, 0, 0)));
    }

    @Test
    public void testReadUIntBEStreamTooShort() {
        assertThrows(EndianUtils.BufferUnderrunException.class,
                () -> EndianUtils.readUIntBE(stream(1, 2, 3)));
    }

    // ---------------------------------------------------------------- 16 bits

    @Test
    public void testReadUShortBEDistinctBytes() throws Exception {
        assertEquals(0x1234, EndianUtils.readUShortBE(stream(0x12, 0x34)));
    }

    @Test
    public void testReadShortBENegative() throws Exception {
        // 0xFFFE en short signe = -2
        assertEquals((short) -2, EndianUtils.readShortBE(stream(0xFF, 0xFE)));
    }

    @Test
    public void testReadUShortAllZeroBytes() throws Exception {
        assertEquals(0, EndianUtils.readUShortLE(stream(0, 0)));
        assertEquals(0, EndianUtils.readUShortBE(stream(0, 0)));
    }

    @Test
    public void testReadUShortBEStreamTooShort() {
        assertThrows(EndianUtils.BufferUnderrunException.class,
                () -> EndianUtils.readUShortBE(stream(0x12)));
    }

    // ---------------------------------------------------------------- readUE7

    @Test
    public void testReadUE7StopsAfterSixBytes() throws Exception {
        // 7 octets : six 0x81 (continuation, valeur 1) puis 0x01. La boucle s'arrete apres
        // 6 octets (max = 6) : resultat = somme des 128^k pour k = 0..5. Le 7e octet est
        // consomme mais ignore.
        long expected = 1L + 128L + 16384L + 2097152L + 268435456L + 34359738368L;
        assertEquals(expected,
                EndianUtils.readUE7(stream(0x81, 0x81, 0x81, 0x81, 0x81, 0x81, 0x01)));
    }

    // ---------------------------------------------------------------- get* sur tableau

    @Test
    public void testGetIntLEAndUIntLEWithoutOffset() {
        // surcharges sans offset (offset implicite 0), jamais appelees ailleurs
        assertEquals(0x04030201, EndianUtils.getIntLE(new byte[]{1, 2, 3, 4}));
        assertEquals(0xFFFFFFFFL,
                EndianUtils.getUIntLE(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}));
    }

    @Test
    public void testGetLongLEDistinctBytes() {
        assertEquals(0x0807060504030201L,
                EndianUtils.getLongLE(new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, 0));
    }
}
