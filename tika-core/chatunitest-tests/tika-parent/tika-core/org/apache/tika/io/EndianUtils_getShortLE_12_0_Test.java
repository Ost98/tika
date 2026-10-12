package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.junit.jupiter.api.function.Executable;
import java.io.IOException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getShortLE_12_0_Test {

    @Test
    public void testGetShortLE() throws IOException {
        byte[] data = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0 };
        short result = EndianUtils.getShortLE(data);
        assertEquals(0x3412, result);
    }

    @Test
    public void testGetShortLEWithOffset() throws IOException {
        byte[] data = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0 };
        short result = EndianUtils.getShortLE(data, 2);
        assertEquals(0x7856, result);
    }

    @Test
    public void testGetShortLEWithEmptyArray() {
        byte[] data = {};
        Executable executable = () -> EndianUtils.getShortLE(data);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }

    @Test
    public void testGetShortLEWithOffsetOutOfRange() {
        byte[] data = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78 };
        Executable executable = () -> EndianUtils.getShortLE(data, 4);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }
}
