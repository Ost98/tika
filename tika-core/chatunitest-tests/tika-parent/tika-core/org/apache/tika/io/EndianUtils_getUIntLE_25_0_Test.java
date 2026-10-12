package org.apache.tika.io;

import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getUIntLE_25_0_Test {

    private EndianUtils endianUtils;

    @BeforeEach
    public void setUp() {
        endianUtils = new EndianUtils();
    }

    @Test
    public void testGetUIntLE() {
        byte[] data = { 0x00, 0x00, 0x00, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        int offset = 4;
        long expected = 0xFFFFFFFFL;
        long result = endianUtils.getUIntLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntLEWithZeroOffset() {
        byte[] data = { 0x00, 0x00, 0x00, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        int offset = 0;
        long expected = 0x01000000L;
        long result = endianUtils.getUIntLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntLEWithNegativeOffset() {
        byte[] data = { 0x00, 0x00, 0x00, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        int offset = -4;
        Executable executable = () -> endianUtils.getUIntLE(data, offset);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }

    @Test
    public void testGetUIntLEWithOffsetGreaterThanArrayLength() {
        byte[] data = { 0x00, 0x00, 0x00, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        int offset = 8;
        Executable executable = () -> endianUtils.getUIntLE(data, offset);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }
}
