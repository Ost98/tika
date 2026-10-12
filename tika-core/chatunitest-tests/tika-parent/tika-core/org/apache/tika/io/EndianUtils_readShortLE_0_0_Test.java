package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.apache.tika.exception.TikaException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

public class EndianUtils_readShortLE_0_0_Test {

    @Test
    public void testReadShortLE() throws IOException, TikaException {
        EndianUtils endianUtils = new EndianUtils();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x12, 0x34 });
        short result = endianUtils.readShortLE(inputStream);
        assertEquals(0x3412, result);
    }

    @Test
    public void testReadShortLEWithBufferUnderrun() throws IOException, TikaException {
        EndianUtils endianUtils = new EndianUtils();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x12 });
        assertThrows(BufferUnderflowException.class, () -> {
            endianUtils.readShortLE(inputStream);
        });
    }

    @Test
    public void testReadShortLEWithNegativeValues() throws IOException, TikaException {
        EndianUtils endianUtils = new EndianUtils();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0xFF, (byte) 0xFE });
        short result = endianUtils.readShortLE(inputStream);
        assertEquals(0xFEFF, result);
    }
}
