/*SDK: unit test for SdkSourceProperties.

Also fix a name changed in the SdkUiLib test.

Change-Id:I30fa83ac607fc4735af161604514f09c2aff6a93*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceProperties.java
//Synthetic comment -- index e9da67f..7f7b8c2 100755

//Synthetic comment -- @@ -18,6 +18,8 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;

//Synthetic comment -- @@ -25,7 +27,9 @@
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

/**
//Synthetic comment -- @@ -125,47 +129,40 @@
}
}

    /**
     * Returns an internal string representation of the underlying Properties map,
     * sorted by ascending keys. Useful for debugging and testing purposes only.
     */
@Override
public String toString() {
        StringBuilder sb = new StringBuilder("<SdkSourceProperties");      //$NON-NLS-1$
synchronized (sSourcesProperties) {
            List<Object> keys = Collections.list(sSourcesProperties.keys());
            Collections.sort(keys, new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    return o1.toString().compareTo(o2.toString());
                }});

            for (Object key : keys) {
                sb.append('\n').append(key)
                  .append(" = ").append(sSourcesProperties.get(key));       //$NON-NLS-1$
}
}
        sb.append('>');
return sb.toString();
}

    /** Load state from persistent file. Expects sSourcesProperties to be synchronized. */
private void loadLocked() {
// Load state from persistent file
        if (loadProperties()) {
            // If it lacks our magic version key, don't use it
            if (sSourcesProperties.getProperty(KEY_VERSION) == null) {
                sSourcesProperties.clear();
}

            sModified = false;
}

if (sSourcesProperties.isEmpty()) {
//Synthetic comment -- @@ -177,7 +174,47 @@
}
}

    /**
     * Load properties from default file. Extracted so that it can be mocked in tests.
     *
     * @return True if actually loaded the file. False if there was an IO error or no
     *   file and nothing was loaded.
     */
    @VisibleForTesting(visibility=Visibility.PRIVATE)
    protected boolean loadProperties() {
        try {
            String folder = AndroidLocation.getFolder();
            File f = new File(folder, SRC_FILENAME);
            if (f.exists()) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(f);
                    sSourcesProperties.load(fis);
                } catch (IOException ignore) {
                    // nop
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException ignore) {}
                    }
                }

                return true;
            }
        } catch (AndroidLocationException ignore) {
            // nop
        }
        return false;
    }

    /**
     * Save file to disk. Expects sSourcesProperties to be synchronized.
     * Made accessible for testing purposes.
     * For public usage, please use {@link #save()} instead.
     */
    @VisibleForTesting(visibility=Visibility.PRIVATE)
    protected void saveLocked() {
// Persist it to the file
FileOutputStream fos = null;
try {
//Synthetic comment -- @@ -199,6 +236,14 @@
} catch (IOException ignore) {}
}
}
    }

    /** Empty current property list. Made accessible for testing purposes. */
    @VisibleForTesting(visibility=Visibility.PRIVATE)
    protected void clear() {
        synchronized (sSourcesProperties) {
            sSourcesProperties.clear();
            sModified = false;
        }
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkSourcePropertiesTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkSourcePropertiesTest.java
new file mode 100755
//Synthetic comment -- index 0000000..b4aa2e5

//Synthetic comment -- @@ -0,0 +1,138 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.internal.repository;


import junit.framework.TestCase;

public class SdkSourcePropertiesTest extends TestCase {

    private static class MockSdkSourceProperties extends SdkSourceProperties {
        private int mLoadCount;
        private int mSaveCount;

        public MockSdkSourceProperties() {
            clear();
        }

        public int getLoadCount() {
            return mLoadCount;
        }

        public int getSaveCount() {
            return mSaveCount;
        }

        @Override
        protected boolean loadProperties() {
            // Don't actually load anthing.
            mLoadCount++;
            return false;
        }

        @Override
        protected void saveLocked() {
            // Don't actually save anything.
            mSaveCount++;
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public final void testSdkSourceProperties() {
        MockSdkSourceProperties m = new MockSdkSourceProperties();

        assertEquals(0, m.getLoadCount());
        assertEquals(0, m.getSaveCount());
        assertEquals(
                "<SdkSourceProperties>",
                m.toString());

        assertNull(m.getProperty(SdkSourceProperties.KEY_DISABLED, "http://example.com/1", null));
        assertEquals("None",
                     m.getProperty(SdkSourceProperties.KEY_NAME, "http://example.com/2", "None"));
        assertEquals(1, m.getLoadCount());
        assertEquals(0, m.getSaveCount());
        assertEquals(
                "<SdkSourceProperties\n" +
                "@version@ = 1>",
                m.toString());

        m.setProperty(SdkSourceProperties.KEY_DISABLED, "http://example.com/1", "disabled");
        assertEquals("disabled",
                m.getProperty(SdkSourceProperties.KEY_DISABLED, "http://example.com/1", "None"));
        assertNull(m.getProperty(SdkSourceProperties.KEY_NAME, "http://example.com/1", null));
        assertEquals(
                "<SdkSourceProperties\n" +
                "@disabled@http://example.com/1 = disabled\n" +
                "@version@ = 1>",
                m.toString());

        m.setProperty(SdkSourceProperties.KEY_NAME, "http://example.com/2", "Site Name");
        assertEquals("Site Name",
                m.getProperty(SdkSourceProperties.KEY_NAME, "http://example.com/2", null));
        assertNull(m.getProperty(SdkSourceProperties.KEY_DISABLED, "http://example.com/2", null));
        assertEquals(1, m.getLoadCount());
        assertEquals(0, m.getSaveCount());
        assertEquals(
                "<SdkSourceProperties\n" +
                "@disabled@http://example.com/1 = disabled\n" +
                "@name@http://example.com/2 = Site Name\n" +
                "@version@ = 1>",
                m.toString());

        m.save();
        assertEquals(1, m.getSaveCount());

        // saving a 2nd time doesn't do anything if no property has been modified
        m.save();
        assertEquals(1, m.getSaveCount());

        // setting things to the same value doesn't actually mark the properties as modified
        m.setProperty(SdkSourceProperties.KEY_DISABLED, "http://example.com/1", "disabled");
        m.setProperty(SdkSourceProperties.KEY_NAME, "http://example.com/2", "Site Name");
        m.save();
        assertEquals(1, m.getSaveCount());

        m.setProperty(SdkSourceProperties.KEY_DISABLED, "http://example.com/1", "not disabled");
        m.setProperty(SdkSourceProperties.KEY_NAME, "http://example.com/2", "New Name");
        assertEquals(
                "<SdkSourceProperties\n" +
                "@disabled@http://example.com/1 = not disabled\n" +
                "@name@http://example.com/2 = New Name\n" +
                "@version@ = 1>",
                m.toString());
        m.save();
        assertEquals(2, m.getSaveCount());

        // setting a value to null deletes it
        m.setProperty(SdkSourceProperties.KEY_NAME, "http://example.com/2", null);
        assertEquals(
                "<SdkSourceProperties\n" +
                "@disabled@http://example.com/1 = not disabled\n" +
                "@version@ = 1>",
                m.toString());

        m.save();
        assertEquals(1, m.getLoadCount());
        assertEquals(3, m.getSaveCount());
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogicTest.java
//Synthetic comment -- index cf4b232..bc532a5 100755

//Synthetic comment -- @@ -1217,9 +1217,9 @@
"-- <NEW, pkg:The addon B from vendor 1, Android API 1, revision 7>\n" + // from src3+4
"PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
getTree(m, true /*displaySortByApi*/));
        // When sorting by source, the src4 source is listed, however since its
        // packages are the same as the ones from src2 or src3 the packages themselves
        // are not shown.
assertEquals(
"PkgCategorySource <source=repo1 (example.com), #items=3>\n" +
"-- <INSTALLED, pkg:Android SDK Tools, revision 3>\n" +
//Synthetic comment -- @@ -1228,7 +1228,8 @@
"PkgCategorySource <source=repo2 (example.com), #items=1>\n" +
"-- <NEW, pkg:The addon A from vendor 1, Android API 1, revision 5>\n" + // from src2+3+4
"PkgCategorySource <source=repo3 (example.com), #items=1>\n" +
                "-- <NEW, pkg:The addon B from vendor 1, Android API 1, revision 7>\n" + // from src3+4
                "PkgCategorySource <source=repo4 (4.example.com), #items=0>\n",
getTree(m, false /*displaySortByApi*/));
}








