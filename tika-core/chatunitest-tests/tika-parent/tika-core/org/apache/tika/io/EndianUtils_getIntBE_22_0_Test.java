package org.apache.tika.io;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_getIntBE_22_0_Test {

    @BeforeEach
    public void setUp() {
        // Setup if needed
    }

    @Test
    public void testGetIntBE() {
        // Test case 1: Normal case
        byte[] data1 = { 0x01, 0x02, 0x03, 0x04 };
        assertEquals(0x04030201, EndianUtils.getIntBE(data1));
        // Test case 2: Different byte order
        byte[] data2 = { 0x04, 0x03, 0x02, 0x01 };
        assertEquals(0x01020304, EndianUtils.getIntBE(data2));
        // Test case 3: Edge case - empty array
        byte[] data3 = {};
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getIntBE(data3));
        // Test case 4: Edge case - array with less than 4 bytes
        byte[] data4 = { 0x01, 0x02 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getIntBE(data4));
        // Test case 5: Edge case - array with exactly 4 bytes
        byte[] data5 = { 0x01, 0x02, 0x03, 0x04 };
        assertEquals(0x04030201, EndianUtils.getIntBE(data5));
    }
}
