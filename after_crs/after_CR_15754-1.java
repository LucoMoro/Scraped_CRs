/*Make HTTP Header class handle multiple cache-control fields.

The HTTP specification states the following about the fields:
Multiple message-header fields with the same field-name MAY be present
in a message if and only if the entire field-value for that header field
is defined as a comma-separated list [i.e., #(values)]. It MUST be
possible to combine the multiple header fields into one "field-name:
field-value" pair, without changing the semantics of the message, by
appending each subsequent field-value to the first, each separated by a
comma. The order in which header fields with the same field-name are
received is therefore significant to the interpretation of the combined
field value, and thus a proxy MUST NOT change the order of these field
values when a message is forwarded.

Change-Id:I1a6fe5cc8f541f8e80d559641d270d09eac9d85c*/




//Synthetic comment -- diff --git a/core/java/android/net/http/Headers.java b/core/java/android/net/http/Headers.java
//Synthetic comment -- index 09f6f4f..e989903 100644

//Synthetic comment -- @@ -262,7 +262,14 @@
break;
case HASH_CACHE_CONTROL:
if (name.equals(CACHE_CONTROL)) {
                // In case where we receive more than one header, create a ',' separated list.
                // This should be ok, according to RFC 2616 chapter 4.2
                if (mHeaders[IDX_CACHE_CONTROL] != null &&
                    mHeaders[IDX_CACHE_CONTROL].length() > 0) {
                    mHeaders[IDX_CACHE_CONTROL] += ("," + val);
                } else {
                    mHeaders[IDX_CACHE_CONTROL] = val;
                }
}
break;
case HASH_LAST_MODIFIED:








//Synthetic comment -- diff --git a/tests/CoreTests/android/core/HttpHeaderTest.java b/tests/CoreTests/android/core/HttpHeaderTest.java
new file mode 100644
//Synthetic comment -- index 0000000..a5d48578

//Synthetic comment -- @@ -0,0 +1,62 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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
package android.core;

import android.test.AndroidTestCase;
import org.apache.http.util.CharArrayBuffer;

import android.net.http.Headers;

public class HttpHeaderTest extends AndroidTestCase {

    static final String LAST_MODIFIED = "Last-Modified: Fri, 18 Jun 2010 09:56:47 GMT";
    static final String CACHE_CONTROL_MAX_AGE = "Cache-Control:max-age=15";
    static final String CACHE_CONTROL_PRIVATE = "Cache-Control: private";

    /**
     * Tests that cache control header supports multiple instances of the header,
     * according to HTTP specification.
     *
     * The HTTP specification states the following about the fields:
     * Multiple message-header fields with the same field-name MAY be present
     * in a message if and only if the entire field-value for that header field
     * is defined as a comma-separated list [i.e., #(values)]. It MUST be
     * possible to combine the multiple header fields into one "field-name:
     * field-value" pair, without changing the semantics of the message, by
     * appending each subsequent field-value to the first, each separated by a
     * comma. The order in which header fields with the same field-name are
     * received is therefore significant to the interpretation of the combined
     * field value, and thus a proxy MUST NOT change the order of these field
     * values when a message is forwarded.
     */
    public void testCacheControl() throws Exception {
        Headers h = new Headers();
        CharArrayBuffer buffer = new CharArrayBuffer(64);

        buffer.append(CACHE_CONTROL_MAX_AGE);
        h.parseHeader(buffer);

        buffer.clear();
        buffer.append(LAST_MODIFIED);
        h.parseHeader(buffer);
        assertEquals("max-age=15", h.getCacheControl());

        buffer.clear();
        buffer.append(CACHE_CONTROL_PRIVATE);
        h.parseHeader(buffer);
        assertEquals("max-age=15,private", h.getCacheControl());
    }
}







