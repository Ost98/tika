package org.apache.tika.mime;

import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MediaTypeRegistry;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.Patterns;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class Patterns_add_1_1_Test {

    private Patterns patterns;

    private MediaTypeRegistry registry;

    private Map<String, MimeType> globs;

    private Map<String, MimeType> names;

    private Map<String, MimeType> extensions;

    @BeforeEach
    public void setUp() {
        registry = mock(MediaTypeRegistry.class);
        patterns = new Patterns(registry);
        globs = new HashMap<>();
        names = new HashMap<>();
        extensions = new HashMap<>();
    }

    @Test
    public void testAddWithConflictingNamePattern() throws MimeTypeException {
        MimeType type1 = mock(MimeType.class);
        when(type1.getType()).thenReturn(MediaType.parse("application/json"));
        MimeType type2 = mock(MimeType.class);
        when(type2.getType()).thenReturn(MediaType.parse("application/xml"));
        patterns.add("example.json", false, type1);
        assertThrows(MimeTypeException.class, () -> patterns.add("example.json", false, type2));
    }

    @Test
    public void testAddWithConflictingExtensionPattern() throws MimeTypeException {
        MimeType type1 = mock(MimeType.class);
        when(type1.getType()).thenReturn(MediaType.parse("application/json"));
        MimeType type2 = mock(MimeType.class);
        when(type2.getType()).thenReturn(MediaType.parse("application/xml"));
        patterns.add("*.json", false, type1);
        assertThrows(MimeTypeException.class, () -> patterns.add("*.json", false, type2));
    }

    @Test
    public void testAddWithConflictingGlobPattern() throws MimeTypeException {
        MimeType type1 = mock(MimeType.class);
        when(type1.getType()).thenReturn(MediaType.parse("application/json"));
        MimeType type2 = mock(MimeType.class);
        when(type2.getType()).thenReturn(MediaType.parse("application/xml"));
        patterns.add("example.*", false, type1);
        assertThrows(MimeTypeException.class, () -> patterns.add("example.*", false, type2));
    }

    @Test
    public void testAddWithNullPattern() {
        MimeType type = mock(MimeType.class);
        when(type.getType()).thenReturn(MediaType.parse("application/json"));
        assertThrows(IllegalArgumentException.class, () -> patterns.add(null, false, type));
    }
}
