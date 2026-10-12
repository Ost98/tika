package org.apache.tika.io;

import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getIntLE_21_0_Test {

    @Test
    public void testGetIntLE() {
        byte[] data = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78 };
        int result = EndianUtils.getIntLE(data, 0);
        assertEquals(0x78563412, result);
    }
}
