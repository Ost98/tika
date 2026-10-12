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

public class MediaType_image_2_0_Test {

    @Test
    public void testImageMethodWithValidType() throws Exception {
        String type = "png";
        MediaType result = invokePrivateMethod("image", type);
        assertEquals("image/png", result.toString());
    }

    @Test
    public void testImageMethodWithNullType() throws Exception {
        String type = null;
        MediaType result = invokePrivateMethod("image", type);
        assertNull(result);
    }

    @Test
    public void testImageMethodWithEmptyType() throws Exception {
        String type = "";
        MediaType result = invokePrivateMethod("image", type);
        assertNull(result);
    }

    @Test
    public void testImageMethodWithWhitespaceType() throws Exception {
        String type = "   ";
        MediaType result = invokePrivateMethod("image", type);
        assertNull(result);
    }

    @Test
    public void testImageMethodWithInvalidType() throws Exception {
        String type = "invalidType";
        MediaType result = invokePrivateMethod("image", type);
        assertEquals("image/invalidType", result.toString());
    }

    private MediaType invokePrivateMethod(String methodName, String type) throws Exception {
        Class<?> clazz = MediaType.class;
        java.lang.reflect.Method method = clazz.getDeclaredMethod(methodName, String.class);
        method.setAccessible(true);
        return (MediaType) method.invoke(null, type);
    }
}
