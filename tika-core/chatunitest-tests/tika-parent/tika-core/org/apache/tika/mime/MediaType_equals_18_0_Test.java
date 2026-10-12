package org.apache.tika.mime;

import org.apache.tika.mime.MediaType;
import org.junit.jupiter.api.function.Executable;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MediaType_equals_18_0_Test {

    @Test
    public void testEqualsDifferentObjects() {
        MediaType mediaType1 = new MediaType("application", "octet-stream");
        MediaType mediaType2 = new MediaType("application", "zip");
        assertFalse(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsSameObject() {
        MediaType mediaType1 = new MediaType("application", "octet-stream");
        assertTrue(mediaType1.equals(mediaType1));
    }

    @Test
    public void testEqualsNull() {
        MediaType mediaType1 = new MediaType("application", "octet-stream");
        assertFalse(mediaType1.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        MediaType mediaType1 = new MediaType("application", "octet-stream");
        assertFalse(mediaType1.equals("application/octet-stream"));
    }

    @Test
    public void testEqualsDifferentParameters() {
        MediaType mediaType1 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        MediaType mediaType2 = new MediaType("application", "octet-stream");
        assertFalse(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsSameParameters() {
        MediaType mediaType1 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        MediaType mediaType2 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        assertTrue(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsIgnoreCase() {
        MediaType mediaType1 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        MediaType mediaType2 = new MediaType("APPLICATION", "OCTET-STREAM", Collections.singletonMap("charset", "UTF-8"));
        assertTrue(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsDifferentCase() {
        MediaType mediaType1 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        MediaType mediaType2 = new MediaType("application", "zip", Collections.singletonMap("charset", "UTF-8"));
        assertFalse(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsDifferentCharset() {
        MediaType mediaType1 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        MediaType mediaType2 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "ISO-8859-1"));
        assertFalse(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsEmptyParameters() {
        MediaType mediaType1 = new MediaType("application", "octet-stream");
        MediaType mediaType2 = new MediaType("application", "octet-stream", Collections.emptyMap());
        assertTrue(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsNonEmptyParameters() {
        MediaType mediaType1 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        MediaType mediaType2 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        assertTrue(mediaType1.equals(mediaType2));
    }

    @Test
    public void testEqualsDifferentOrderParameters() {
        MediaType mediaType1 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        MediaType mediaType2 = new MediaType("application", "octet-stream", Collections.singletonMap("charset", "UTF-8"));
        assertTrue(mediaType1.equals(mediaType2));
    }
}
