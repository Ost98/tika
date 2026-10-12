package org.apache.tika.mime;

import org.apache.tika.mime.MediaType;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.Locale;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaType_toString_17_0_Test {

    private MediaType mediaType;

    @BeforeEach
    public void setUp() {
        mediaType = new MediaType("text", "plain");
    }

    @Test
    public void testToString() {
        assertEquals("text/plain", mediaType.toString());
    }

    @Test
    public void testToStringWithParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("charset", "UTF-8");
        MediaType mediaTypeWithParams = new MediaType("text", "plain", parameters);
        assertEquals("text/plain; charset=UTF-8", mediaTypeWithParams.toString());
    }

    @Test
    public void testToStringWithEmptyParameters() {
        Map<String, String> parameters = Collections.emptyMap();
        MediaType mediaTypeWithEmptyParams = new MediaType("text", "plain", parameters);
        assertEquals("text/plain", mediaTypeWithEmptyParams.toString());
    }
}
