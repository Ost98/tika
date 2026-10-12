/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika.mime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Collections;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

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
