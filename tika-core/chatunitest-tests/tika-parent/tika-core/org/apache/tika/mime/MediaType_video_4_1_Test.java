package org.apache.tika.mime;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
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
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MediaType_video_4_1_Test {

    @Test
    public void testVideo() throws Exception {
        // Create an instance of the focal class with necessary parameters
        MediaType mediaType = new MediaType("video", "mp4", Collections.emptyMap());
        // Get the video method using reflection
        Method videoMethod = mediaType.getClass().getDeclaredMethod("video", String.class);
        videoMethod.setAccessible(true);
        // Call the video method with a parameter
        String type = "mp4";
        MediaType result = (MediaType) videoMethod.invoke(mediaType, type);
        // Verify the result
        assertEquals("video/mp4", result.toString());
    }
}
