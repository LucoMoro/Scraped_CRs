/*Uses some constants for default values for minSdkVersion and glEsVersion

Change-Id:Ieaf86ae56c7458e2bde90960c0262329ba77bad5*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java
//Synthetic comment -- index f880079..88f28f6 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.export;

import com.android.sdklib.xml.ManifestData.SupportsScreens;

import java.io.File;
//Synthetic comment -- @@ -49,7 +50,7 @@
// the following are used to sort the export data and generate buildInfo
private int mMinSdkVersion;
private String mAbi;
    private int mGlVersion = -1;
private SupportsScreens mSupportsScreens;

ApkData() {
//Synthetic comment -- @@ -163,7 +164,7 @@
write(sb, PROP_PROJECT, mRelativePath);
write(sb, PROP_API, mMinSdkVersion);

        if (mGlVersion != -1) {
write(sb, PROP_GL, mGlVersion);
}

//Synthetic comment -- @@ -197,14 +198,14 @@
comp = mSupportsScreens.compareTo(o.mSupportsScreens);
if (comp != 0) return comp;

        if (mGlVersion != -1) {
            if (o.mGlVersion != -1) {
comp = mGlVersion - o.mGlVersion;
if (comp != 0) return comp;
} else {
return -1;
}
        } else if (o.mGlVersion != -1) {
return 1;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
//Synthetic comment -- index 55dfbf0..693a5f8 100644

//Synthetic comment -- @@ -296,7 +296,7 @@
}

int minSdkVersion = manifestData.getMinSdkVersion();
            if (minSdkVersion == 0) { // means it's a codename
throw new ExportException(
"Codename in minSdkVersion is not supported by multi-apk export.");
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java
//Synthetic comment -- index 955f8ed..9f6702c 100644

//Synthetic comment -- @@ -28,6 +28,10 @@
* Class containing the manifest info obtained during the parsing.
*/
public final class ManifestData {
/** Application package */
String mPackage;
/** Application version Code, null if the attribute is not present. */
//Synthetic comment -- @@ -174,6 +178,9 @@
SupportsScreens result = new SupportsScreens();

result.mNormalScreens = Boolean.TRUE;
result.mResizeable = result.mAnyDensity = result.mSmallScreens = result.mLargeScreens =
targetSdkVersion <= 3 ? Boolean.FALSE : Boolean.TRUE;

//Synthetic comment -- @@ -452,7 +459,7 @@
try {
mMinSdkVersion = Integer.parseInt(mMinSdkVersionString);
} catch (NumberFormatException e) {
                mMinSdkVersion = 0; // 0 means it's a codename.
}
}
}
//Synthetic comment -- @@ -517,13 +524,16 @@
return mFeatures.toArray(new UsesFeature[mFeatures.size()]);
}

public int getGlEsVersion() {
for (UsesFeature feature : mFeatures) {
if (feature.mGlEsVersion > 0) {
return feature.mGlEsVersion;
}
}
        return -1;
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/AndroidManifestParserTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/AndroidManifestParserTest.java
//Synthetic comment -- index 61304b2..a9a7edf 100644

//Synthetic comment -- @@ -84,7 +84,7 @@
assertEquals(8, mManifestTestApp.getTargetSdkVersion());

assertEquals("foo", mManifestInstrumentation.getMinSdkVersionString());
        assertEquals(0, mManifestInstrumentation.getMinSdkVersion());
}

public void testGetActivities() {







