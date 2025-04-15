/*SDK Manager: fix parsing of empty AndroidVersion codename.

SDK Bug: 29952

To reproduce the bug, install any system image with the SDK
Manager and add the line "AndroidVersion.CodeName=" in the
source.properties. When the SDK Manager loads, it will
incorrectly flag the system image as "broken" because it
can't understand the empty codename.

This fixes it by sanitizing the codename when creating
an AndroidVersion and using that class when loading the
properties instead of hand checking the codename in
various places.

Change-Id:Ie4a02739e56f576c7644b5539697c943d0082aac*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersion.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersion.java
//Synthetic comment -- index 996aee4..38d3bbb 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib;

import com.android.annotations.Nullable;
import com.android.sdklib.repository.PkgProps;

import java.util.Properties;
//Synthetic comment -- @@ -61,7 +62,7 @@
*/
public AndroidVersion(int apiLevel, String codename) {
mApiLevel = apiLevel;
        mCodename = sanitizeCodename(codename);
}

/**
//Synthetic comment -- @@ -73,11 +74,12 @@
public AndroidVersion(Properties properties, int defaultApiLevel, String defaultCodeName) {
if (properties == null) {
mApiLevel = defaultApiLevel;
            mCodename = sanitizeCodename(defaultCodeName);
} else {
mApiLevel = Integer.parseInt(properties.getProperty(PkgProps.VERSION_API_LEVEL,
                                                                Integer.toString(defaultApiLevel)));
            mCodename = sanitizeCodename(
                            properties.getProperty(PkgProps.VERSION_CODENAME, defaultCodeName));
}
}

//Synthetic comment -- @@ -95,7 +97,8 @@
if (apiLevel != null) {
try {
mApiLevel = Integer.parseInt(apiLevel);
                mCodename = sanitizeCodename(properties.getProperty(PkgProps.VERSION_CODENAME,
                                                                    null/*defaultValue*/));
return;
} catch (NumberFormatException e) {
error = e;
//Synthetic comment -- @@ -298,4 +301,25 @@
public boolean isGreaterOrEqualThan(int api) {
return compareTo(api, null /*codename*/) >= 0;
}

    /**
     * Sanitizes the codename string according to the following rules:
     * - A codename should be {@code null} for a release version or it should be a non-empty
     *   string for an actual preview.
     * - In input, spacing is trimmed since it is irrelevant.
     * - An empty string or the special codename "REL" means a release version
     *   and is converted to {@code null}.
     *
     * @param codename A possible-null codename.
     * @return Null for a release version or a non-empty codename.
     */
    private @Nullable String sanitizeCodename(@Nullable String codename) {
        if (codename != null) {
            codename = codename.trim();
            if (codename.length() == 0 || SdkConstants.CODENAME_RELEASE.equals(codename)) {
                codename = null;
            }
        }
        return codename;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersionTest.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersionTest.java
new file mode 100755
//Synthetic comment -- index 0000000..c72f6b7

//Synthetic comment -- @@ -0,0 +1,63 @@
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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java
//Synthetic comment -- index 02688c0..ac8de23 100644

//Synthetic comment -- @@ -53,8 +53,7 @@
*
* @param sdkOsPath the root folder of the SDK
* @param platformOSPath the root folder of the platform component
     * @param apiVersion the API Level + codename.
* @param versionName the version name of the platform.
* @param revision the revision of the platform component.
* @param layoutlibVersion The {@link LayoutlibVersion}. May be null.
//Synthetic comment -- @@ -65,8 +64,7 @@
PlatformTarget(
String sdkOsPath,
String platformOSPath,
            AndroidVersion apiVersion,
String versionName,
int revision,
LayoutlibVersion layoutlibVersion,
//Synthetic comment -- @@ -77,7 +75,7 @@
}
mRootFolderOsPath = platformOSPath;
mProperties = Collections.unmodifiableMap(properties);
        mVersion = apiVersion;
mVersionName = versionName;
mRevision = revision;
mLayoutlibVersion = layoutlibVersion;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index 6e6c657..b5ff9da 100644

//Synthetic comment -- @@ -263,6 +263,9 @@
/** Name of the cache folder in the $HOME/.android. */
public final static String FD_CACHE = "cache";                      //$NON-NLS-1$

    /** API codename of a release (non preview) system image or platform. **/
    public final static String CODENAME_RELEASE = "REL";                //$NON-NLS-1$

/** Namespace for the resource XML, i.e. "http://schemas.android.com/apk/res/android" */
public final static String NS_RESOURCES =
"http://schemas.android.com/apk/res/android";                   //$NON-NLS-1$
//Synthetic comment -- @@ -377,7 +380,6 @@
FN_FRAMEWORK_RENDERSCRIPT + File.separator + FN_FRAMEWORK_INCLUDE_CLANG;

/* Folder paths relative to a addon folder */
/** Path of the images directory relative to a folder folder.
*  This is an OS path, ending with a separator. */
public final static String OS_ADDON_LIBS_FOLDER = FD_ADDON_LIBS + File.separator;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 8284054..5c570ce 100644

//Synthetic comment -- @@ -437,12 +437,10 @@
}
}

            // Codename must be either null or a platform codename.
            // REL means it's a release version and therefore the codename should be null.
            AndroidVersion apiVersion =
                new AndroidVersion(apiNumber, platformProp.get(PROP_VERSION_CODENAME));

// version string
String apiName = platformProp.get(PkgProps.PLATFORM_VERSION);
//Synthetic comment -- @@ -489,14 +487,13 @@
}

ISystemImage[] systemImages =
                getPlatformSystemImages(sdkOsPath, platformFolder, apiVersion);

// create the target.
PlatformTarget target = new PlatformTarget(
sdkOsPath,
platformFolder.getAbsolutePath(),
                    apiVersion,
apiName,
revision,
layoutlibVersion,
//Synthetic comment -- @@ -574,16 +571,14 @@
*
* @param sdkOsPath The path to the SDK.
* @param root Root of the platform target being loaded.
     * @param version API level + codename of platform being loaded.
* @return an array of ISystemImage containing all the system images for the target.
*              The list can be empty.
*/
private static ISystemImage[] getPlatformSystemImages(
String sdkOsPath,
File root,
            AndroidVersion version) {
Set<ISystemImage> found = new TreeSet<ISystemImage>();
Set<String> abiFound = new HashSet<String>();

//Synthetic comment -- @@ -592,8 +587,6 @@
// The actual directory names are irrelevant.
// If we find multiple occurrences of the same platform/abi, the first one read wins.

File[] firstLevelFiles = new File(sdkOsPath, SdkConstants.FD_SYSTEM_IMAGES).listFiles();
if (firstLevelFiles != null) {
for (File firstLevel : firstLevelFiles) {







