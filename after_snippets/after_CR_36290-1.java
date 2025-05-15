
//<Beginning of snippet n. 0>


*/
public AndroidVersion(int apiLevel, String codename) {
mApiLevel = apiLevel;
        if (codename != null) {
            codename = codename.trim();
            if (codename.length() == 0) {
                codename = null;
            }
        }
mCodename = codename;
}

* {@link #saveProperties(Properties)}.
*/
public AndroidVersion(Properties properties, int defaultApiLevel, String defaultCodeName) {
        if (defaultCodeName != null) {
            defaultCodeName = defaultCodeName.trim();
            if (defaultCodeName.length() == 0) {
                defaultCodeName = null;
            }
        }
if (properties == null) {
mApiLevel = defaultApiLevel;
mCodename = defaultCodeName;

//<End of snippet n. 0>










//<Beginning of snippet n. 1>

new file mode 100755

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

package com.android.sdklib;

import junit.framework.TestCase;

/**
 * Unit tests for {@link AndroidVersion}.
 */
public class AndroidVersionTest extends TestCase {

    public final void testAndroidVersion() {
        AndroidVersion v = new AndroidVersion(1, "  CODENAME   ");
        assertEquals(1, v.getApiLevel());
        assertEquals("CODENAME", v.getApiString());
        assertTrue(v.isPreview());
        assertEquals("CODENAME", v.getCodename());
        assertEquals("CODENAME".hashCode(), v.hashCode());
        assertEquals("API 1, CODENAME preview", v.toString());


        v = new AndroidVersion(15, null);
        assertEquals(15, v.getApiLevel());
        assertEquals("15", v.getApiString());
        assertFalse(v.isPreview());
        assertNull(v.getCodename());
        assertTrue(v.equals(15));
        assertEquals(15, v.hashCode());
        assertEquals("API 15", v.toString());

        // An empty codename is like a null codename
        v = new AndroidVersion(15, "   ");
        assertFalse(v.isPreview());
        assertNull(v.getCodename());
        assertEquals("15", v.getApiString());

        v = new AndroidVersion(15, "");
        assertFalse(v.isPreview());
        assertNull(v.getCodename());
        assertEquals("15", v.getApiString());
        
        assertTrue(v.isGreaterOrEqualThan(0));
        assertTrue(v.isGreaterOrEqualThan(14));
        assertTrue(v.isGreaterOrEqualThan(15));
        assertFalse(v.isGreaterOrEqualThan(16));
        assertFalse(v.isGreaterOrEqualThan(Integer.MAX_VALUE));
   }

}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


}
}

            // Codename must be either null or a platform codename.
            // REL means it's a release version and therefore the codename should be null.
String apiCodename = platformProp.get(PROP_VERSION_CODENAME);
            if (apiCodename != null) {
                apiCodename = apiCodename.trim();
                if (apiCodename.equals("REL") || apiCodename.length() == 0) {
                    apiCodename = null;
                }
}

// version string

//<End of snippet n. 2>








