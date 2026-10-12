package org.apache.tika.io;

import java.io.IOException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getUShortBE_19_0_Test {

    @Test
    public void testGetUShortBE() throws IOException {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        int offset = 0;
        int expected = 0x0102;
        int result = EndianUtils.getUShortBE(data, offset);
        assertEquals(expected, result);
    }
}
