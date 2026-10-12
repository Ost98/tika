package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getUByte_30_0_Test {

    @Test
    public void testGetUByte() {
        byte[] data = { (byte) 0xFF, (byte) 0x00, (byte) 0x01, (byte) 0x02 };
        int offset = 0;
        short expected = 0xFF;
        short result = EndianUtils.getUByte(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUByteWithOffset() {
        byte[] data = { (byte) 0x00, (byte) 0xFF, (byte) 0x01, (byte) 0x02 };
        int offset = 1;
        short expected = 0xFF;
        short result = EndianUtils.getUByte(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUByteWithNegativeOffset() {
        byte[] data = { (byte) 0x00, (byte) 0xFF, (byte) 0x01, (byte) 0x02 };
        int offset = -1;
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUByte(data, offset));
    }

    @Test
    public void testGetUByteWithLargeOffset() {
        byte[] data = { (byte) 0x00, (byte) 0xFF, (byte) 0x01, (byte) 0x02 };
        int offset = data.length;
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUByte(data, offset));
    }

    @Test
    public void testGetUByteWithNullData() {
        byte[] data = null;
        int offset = 0;
        assertThrows(NullPointerException.class, () -> EndianUtils.getUByte(data, offset));
    }
}
