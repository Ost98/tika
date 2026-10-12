package org.apache.tika.io;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_getLongLE_28_0_Test {

    @InjectMocks
    private EndianUtils endianUtils;

    @Test
    public void testGetLongLEWithNegativeBytes() {
        byte[] data = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff };
        int offset = 0;
        long expected = -1L;
        long result = endianUtils.getLongLE(data, offset);
        assertEquals(expected, result);
    }
}
