package org.apache.tika.io;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_ubyteToInt_29_0_Test {

    @Mock
    private InputStream inputStream;

    @Test
    public void testUbyteToInt() throws IOException {
        byte input = (byte) 0xFF;
        when(inputStream.read()).thenReturn((int) input & 0xFF);
        int result = EndianUtils.ubyteToInt(input);
        assertEquals(255, result);
    }

    @Test
    public void testUbyteToIntWithNegativeValue() throws IOException {
        byte input = (byte) -1;
        when(inputStream.read()).thenReturn((int) input & 0xFF);
        int result = EndianUtils.ubyteToInt(input);
        assertEquals(255, result);
    }

    @Test
    public void testUbyteToIntWithZeroValue() throws IOException {
        byte input = 0;
        when(inputStream.read()).thenReturn((int) input & 0xFF);
        int result = EndianUtils.ubyteToInt(input);
        assertEquals(0, result);
    }
}
