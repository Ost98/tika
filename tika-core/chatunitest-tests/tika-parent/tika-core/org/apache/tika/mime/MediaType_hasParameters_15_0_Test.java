package org.apache.tika.mime;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaType_hasParameters_15_0_Test {

    private MediaType mediaType;

    @BeforeEach
    public void setUp() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("param1", "value1");
        mediaType = new MediaType("type", "subtype", parameters);
    }

    @Test
    public void testHasParameters() {
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasNoParameters() {
        Map<String, String> emptyParameters = Collections.emptyMap();
        mediaType = new MediaType("type", "subtype", emptyParameters);
        assertFalse(mediaType.hasParameters());
    }
}
