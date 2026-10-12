package org.apache.tika.mime;

import org.apache.tika.mime.MediaType;
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

public class MediaType_application_0_0_Test {

    @Test
    public void testApplicationMethod() {
        try {
            // Get the private application method using reflection
            java.lang.reflect.Method applicationMethod = MediaType.class.getDeclaredMethod("application", String.class);
            applicationMethod.setAccessible(true);
            // Test with a valid type
            MediaType result1 = (MediaType) applicationMethod.invoke(null, "json");
            assertEquals("application/json", result1.toString());
            // Test with an empty type
            MediaType result2 = (MediaType) applicationMethod.invoke(null, "");
            assertNull(result2);
            // Test with a null type
            MediaType result3 = (MediaType) applicationMethod.invoke(null, null);
            assertNull(result3);
            // Test with a type containing spaces
            MediaType result4 = (MediaType) applicationMethod.invoke(null, " application/json ");
            assertEquals("application/json", result4.toString());
            // Test with a type containing special characters
            MediaType result5 = (MediaType) applicationMethod.invoke(null, "application/json; charset=UTF-8");
            assertEquals("application/json; charset=UTF-8", result5.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
