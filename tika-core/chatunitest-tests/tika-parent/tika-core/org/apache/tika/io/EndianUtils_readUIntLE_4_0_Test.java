package org.apache.tika.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.EndianUtils.BufferUnderrunException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EndianUtils_readUIntLE_4_0_Test {

    @Test
    void testReadUIntLE() throws IOException, TikaException {
        InputStream mockStream = mock(InputStream.class);
        when(mockStream.read()).thenReturn(0, 0, 0, 1);
        long result = EndianUtils.readUIntLE(mockStream);
        assertEquals(1, result);
    }

    @Test
    void testReadUIntLEBufferUnderrun() throws IOException {
        InputStream mockStream = mock(InputStream.class);
        when(mockStream.read()).thenReturn(0, 0, 0, -1);
        assertThrows(BufferUnderrunException.class, () -> EndianUtils.readUIntLE(mockStream));
    }
}
