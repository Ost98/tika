package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getUShortLE_14_0_Test {

    @Test
    public void testGetUShortLE() throws IOException {
        byte[] data = { 0x01, 0x02 };
        int result = EndianUtils.getUShortLE(data);
        assertEquals(513, result);
    }
}
