/*Uses some constants for default values for minSdkVersion and glEsVersion

Change-Id:Ieaf86ae56c7458e2bde90960c0262329ba77bad5*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java
//Synthetic comment -- index f880079..88f28f6 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.export;

import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.SupportsScreens;

import java.io.File;
//Synthetic comment -- @@ -49,7 +50,7 @@
// the following are used to sort the export data and generate buildInfo
private int mMinSdkVersion;
private String mAbi;
    private int mGlVersion = ManifestData.GL_ES_VERSION_NOT_SET;
private SupportsScreens mSupportsScreens;

ApkData() {
//Synthetic comment -- @@ -163,7 +164,7 @@
write(sb, PROP_PROJECT, mRelativePath);
write(sb, PROP_API, mMinSdkVersion);

        if (mGlVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
write(sb, PROP_GL, mGlVersion);
}

//Synthetic comment -- @@ -197,14 +198,14 @@
comp = mSupportsScreens.compareTo(o.mSupportsScreens);
if (comp != 0) return comp;

        if (mGlVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
            if (o.mGlVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
comp = mGlVersion - o.mGlVersion;
if (comp != 0) return comp;
} else {
return -1;
}
        } else if (o.mGlVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
return 1;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
//Synthetic comment -- index 55dfbf0..693a5f8 100644

//Synthetic comment -- @@ -296,7 +296,7 @@
}

int minSdkVersion = manifestData.getMinSdkVersion();
            if (minSdkVersion == ManifestData.MIN_SDK_CODENAME) {
throw new ExportException(
"Codename in minSdkVersion is not supported by multi-apk export.");
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java
//Synthetic comment -- index 955f8ed..da96cd4 100644

//Synthetic comment -- @@ -28,6 +28,19 @@
* Class containing the manifest info obtained during the parsing.
*/
public final class ManifestData {

    /**
     * Value returned by {@link #getMinSdkVersion()} when the value of the minSdkVersion attribute
     * in the manifest is a codename and not an integer value.
     */
    public final static int MIN_SDK_CODENAME = 0;

    /**
     * Value returned by {@link #getGlEsVersion()} when there are no <uses-feature> node with the
     * attribute glEsVersion set.
     */
    public final static int GL_ES_VERSION_NOT_SET = -1;

/** Application package */
String mPackage;
/** Application version Code, null if the attribute is not present. */
//Synthetic comment -- @@ -174,6 +187,9 @@
SupportsScreens result = new SupportsScreens();

result.mNormalScreens = Boolean.TRUE;
            // Screen size and density became available in Android 1.5/API3, so before that
            // non normal screens were not supported by default. After they are considered
            // supported.
result.mResizeable = result.mAnyDensity = result.mSmallScreens = result.mLargeScreens =
targetSdkVersion <= 3 ? Boolean.FALSE : Boolean.TRUE;

//Synthetic comment -- @@ -452,7 +468,7 @@
try {
mMinSdkVersion = Integer.parseInt(mMinSdkVersionString);
} catch (NumberFormatException e) {
                mMinSdkVersion = MIN_SDK_CODENAME;
}
}
}
//Synthetic comment -- @@ -517,13 +533,16 @@
return mFeatures.toArray(new UsesFeature[mFeatures.size()]);
}

    /**
     * Returns the glEsVersion from a <uses-feature> or {@link #GL_ES_VERSION_NOT_SET} if not set.
     */
public int getGlEsVersion() {
for (UsesFeature feature : mFeatures) {
if (feature.mGlEsVersion > 0) {
return feature.mGlEsVersion;
}
}
        return GL_ES_VERSION_NOT_SET;
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/AndroidManifestParserTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/AndroidManifestParserTest.java
//Synthetic comment -- index 61304b2..a9a7edf 100644

//Synthetic comment -- @@ -84,7 +84,7 @@
assertEquals(8, mManifestTestApp.getTargetSdkVersion());

assertEquals("foo", mManifestInstrumentation.getMinSdkVersionString());
        assertEquals(ManifestData.MIN_SDK_CODENAME, mManifestInstrumentation.getMinSdkVersion());
}

public void testGetActivities() {







