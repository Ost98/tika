package org.apache.tika.mime;

import org.apache.tika.mime.MediaType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ExtendWith(MockitoExtension.class)
public class MediaType_hashCode_19_0_Test {

    @Test
    public void testHashCode() throws Exception {
        // Arrange
        String type = "application";
        String subtype = "json";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("charset", "UTF-8");
        MediaType mediaType = new MediaType(type, subtype, parameters);
        Method hashCodeMethod = MediaType.class.getDeclaredMethod("hashCode");
        hashCodeMethod.setAccessible(true);
        // Act
        int result = (int) hashCodeMethod.invoke(mediaType);
        // Assert
        assertEquals(type + '/' + subtype, mediaType.toString());
        assertEquals(mediaType.toString().hashCode(), result);
    }
}
