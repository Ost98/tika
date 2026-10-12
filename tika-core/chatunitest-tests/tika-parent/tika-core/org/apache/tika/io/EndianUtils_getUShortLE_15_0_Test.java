package org.apache.tika.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

public class EndianUtils_getUShortLE_15_0_Test {

    @Test
    public void testGetUShortLE() {
        byte[] data = { 0x01, 0x02 };
        int offset = 0;
        EndianUtils endianUtils = new EndianUtils();
        assertEquals(513, endianUtils.getUShortLE(data, offset));
    }

    @Test
    public void testGetUShortLE_WithOffset() {
        byte[] data = { 0x03, 0x04, 0x05, 0x06 };
        int offset = 2;
        EndianUtils endianUtils = new EndianUtils();
        assertEquals(1028, endianUtils.getUShortLE(data, offset));
    }

    @Test
    public void testGetUShortLE_WithNegativeOffset() {
        byte[] data = { 0x07, 0x08 };
        int offset = -1;
        EndianUtils endianUtils = new EndianUtils();
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            endianUtils.getUShortLE(data, offset);
        });
        assertEquals("Index 2, size 2", exception.getMessage());
    }

    @Test
    public void testGetUShortLE_WithTooLargeOffset() {
        byte[] data = { 0x09, 0x0A };
        int offset = 2;
        EndianUtils endianUtils = new EndianUtils();
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            endianUtils.getUShortLE(data, offset);
        });
        assertEquals("Index 2, size 2", exception.getMessage());
    }
}
