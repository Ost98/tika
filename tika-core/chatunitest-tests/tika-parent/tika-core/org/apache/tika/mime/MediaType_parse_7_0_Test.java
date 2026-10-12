package org.apache.tika.mime;

import org.apache.tika.mime.MediaType;
import org.junit.jupiter.api.function.Executable;
import java.lang.reflect.Method;
import java.util.Collections;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaType_parse_7_0_Test {

    @Test
    public void testParseNull() throws Exception {
        Method method = MediaType.class.getDeclaredMethod("parse", String.class);
        method.setAccessible(true);
        assertNull(method.invoke(null, (String) null));
    }

    @Test
    public void testParseInvalid() throws Exception {
        Method method = MediaType.class.getDeclaredMethod("parse", String.class);
        method.setAccessible(true);
        assertNull(method.invoke(null, "invalid"));
    }

    @Test
    public void testParseSimple() throws Exception {
        Method method = MediaType.class.getDeclaredMethod("parse", String.class);
        method.setAccessible(true);
        MediaType mediaType = (MediaType) method.invoke(null, "text/plain");
        assertEquals("text/plain", mediaType.toString());
    }

    @Test
    public void testParseComplex() throws Exception {
        Method method = MediaType.class.getDeclaredMethod("parse", String.class);
        method.setAccessible(true);
        MediaType mediaType = (MediaType) method.invoke(null, "application/xml; charset=UTF-8");
        assertEquals("application/xml; charset=UTF-8", mediaType.toString());
    }

    @Test
    public void testParseOptimization() throws Exception {
        Method method = MediaType.class.getDeclaredMethod("parse", String.class);
        method.setAccessible(true);
        MediaType mediaType1 = (MediaType) method.invoke(null, "text/plain");
        MediaType mediaType2 = (MediaType) method.invoke(null, "text/plain");
        assertSame(mediaType1, mediaType2);
    }

    @Test
    public void testParseNoOptimization() throws Exception {
        Method method = MediaType.class.getDeclaredMethod("parse", String.class);
        method.setAccessible(true);
        MediaType mediaType1 = (MediaType) method.invoke(null, "text/plain");
        MediaType mediaType2 = (MediaType) method.invoke(null, "text/plain; charset=UTF-8");
        assertNotSame(mediaType1, mediaType2);
    }
}
