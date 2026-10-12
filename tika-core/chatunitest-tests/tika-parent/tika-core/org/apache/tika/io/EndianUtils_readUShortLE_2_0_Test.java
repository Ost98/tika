package org.apache.tika.io;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.EndianUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_readUShortLE_2_0_Test {

    @Mock
    private InputStream mockStream;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testReadUShortLE() throws IOException, TikaException {
        // Arrange
        when(mockStream.read()).thenReturn(0x01, 0x02);
        // Act
        int result = EndianUtils.readUShortLE(mockStream);
        // Assert
        assertEquals(0x0201, result);
        verify(mockStream, times(2)).read();
    }

    @Test
    public void testReadUShortLEWithNegativeInput() throws IOException, TikaException {
        // Arrange
        when(mockStream.read()).thenReturn(0xFF, 0xFF);
        // Act & Assert
        assertThrows(BufferUnderflowException.class, () -> EndianUtils.readUShortLE(mockStream));
        verify(mockStream, times(2)).read();
    }
}
