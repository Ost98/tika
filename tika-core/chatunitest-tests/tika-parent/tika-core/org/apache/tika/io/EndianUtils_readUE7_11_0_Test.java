package org.apache.tika.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.tika.exception.TikaException;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.InputStream;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class EndianUtils_readUE7_11_0_Test {

    @Test
    public void testReadUE7() throws IOException {
        // Test case 1: Normal input
        InputStream inputStream1 = new ByteArrayInputStream(new byte[] { (byte) 0x7F, (byte) 0x00 });
        assertEquals(127, EndianUtils.readUE7(inputStream1));
        // Test case 2: Input with multiple bytes
        InputStream inputStream2 = new ByteArrayInputStream(new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x00 });
        assertEquals(2047, EndianUtils.readUE7(inputStream2));
        // Test case 3: Input with max bytes
        InputStream inputStream3 = new ByteArrayInputStream(new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x00 });
        assertEquals(8191, EndianUtils.readUE7(inputStream3));
        // Test case 4: Input with buffer underun
        InputStream inputStream4 = new ByteArrayInputStream(new byte[] { (byte) 0x80 });
        assertThrows(IOException.class, () -> EndianUtils.readUE7(inputStream4), "Buffer underun; expected one more byte");
    }
}
