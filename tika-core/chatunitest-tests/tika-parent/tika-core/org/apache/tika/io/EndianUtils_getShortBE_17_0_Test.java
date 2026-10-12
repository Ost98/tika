package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
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

public class EndianUtils_getShortBE_17_0_Test {

    @Test
    public void testGetShortBE() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 0;
        short expected = (short) 0x0201;
        short result = EndianUtils.getShortBE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetShortBEWithOffset() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 2;
        short expected = (short) 0x0403;
        short result = EndianUtils.getShortBE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetShortBEWithEmptyArray() {
        byte[] data = {};
        int offset = 0;
        assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getShortBE(data, offset);
        });
    }

    @Test
    public void testGetShortBEWithNegativeOffset() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = -1;
        assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getShortBE(data, offset);
        });
    }

    @Test
    public void testGetShortBEWithOffsetGreaterThanArrayLength() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = data.length;
        assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getShortBE(data, offset);
        });
    }
}
