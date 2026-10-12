package org.apache.tika.mime;

import org.apache.tika.mime.MediaType;
import java.util.HashMap;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaType_compareTo_20_0_Test {

    @Test
    public void testCompareTo() {
        try {
            // Create two MediaType objects
            MediaType mediaType1 = new MediaType("application", "json");
            MediaType mediaType2 = new MediaType("application", "xml");
            // Compare the two MediaType objects
            int result = mediaType1.compareTo(mediaType2);
            // Assert that the result is as expected
            assertEquals(-1, result);
            // Create another(MediaType objects
            MediaType mediaType3 = new MediaType("application", "json");
            // Compare the two MediaType objects
            result = mediaType1.compareTo(mediaType3);
            // Assert that the result is as expected
            assertEquals(0, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
