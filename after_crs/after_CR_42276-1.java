/*Created MediaMetadataRetrieverTest

Created MediaMetadataRetrieverTest and added a test case for
setDataSource(null)

Change-Id:Iff0bda5e035893824525b9f4ec9c023cef13c898*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaMetadataRetrieverTest.java b/tests/tests/media/src/android/media/cts/MediaMetadataRetrieverTest.java
new file mode 100644
//Synthetic comment -- index 0000000..8c5b729

//Synthetic comment -- @@ -0,0 +1,44 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.media.cts;

import android.media.MediaMetadataRetriever;
import android.test.AndroidTestCase;

public class MediaMetadataRetrieverTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSetDataSourceNull() {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        try {
            retriever.setDataSource((String)null);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException ex) {
            // Expected, test passed.
        }
    }
}







