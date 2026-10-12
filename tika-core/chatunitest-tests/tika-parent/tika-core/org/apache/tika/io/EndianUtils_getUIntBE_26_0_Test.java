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

public class EndianUtils_getUIntBE_26_0_Test {

    @Test
    public void testGetUIntBE() {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };
        long expected = 1L;
        long result = EndianUtils.getUIntBE(data);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntBEWithOffset() {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        long expected = 1L;
        long result = EndianUtils.getUIntBE(data, 8);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntBEWithNegativeValues() {
        byte[] data = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE };
        long expected = 4294967294L;
        long result = EndianUtils.getUIntBE(data);
        assertEquals(expected, result);
    }
}
