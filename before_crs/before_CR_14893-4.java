/*Implement mechanics for supports-screens in the multi-apk export.

- detect overlap (apk1 is small, large; apk2 is normal)
- detect non-strictly different supports-screens
- compareTo orders based on support for higher size.

Change-Id:I76251e10c83f1508c9a5b83b638a47b1c0922693*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java
//Synthetic comment -- index 88f28f6..4c7e606 100644

//Synthetic comment -- @@ -39,7 +39,6 @@
private static final String PROP_PROJECT = "project";
private static final String PROP_MINOR = "minor";
private static final String PROP_BUILDINFO = "buildinfo";
    private static final String PROP_OUTPUTNAME = "outputname";

private String mOutputName;
private String mRelativePath;
//Synthetic comment -- @@ -149,17 +148,13 @@

@Override
public String toString() {
        StringBuilder sb = new StringBuilder();
        write(sb, PROP_OUTPUTNAME, mOutputName);
        write(sb, PROP_BUILDINFO, mBuildInfo);
        sb.append(getLogLine());

        return sb.toString();
}

public String getLogLine() {
StringBuilder sb = new StringBuilder();
        sb.append(mBuildInfo).append(':');
write(sb, PROP_MINOR, mMinor);
write(sb, PROP_PROJECT, mRelativePath);
write(sb, PROP_API, mMinSdkVersion);
//Synthetic comment -- @@ -172,7 +167,7 @@
write(sb, PROP_ABI, mAbi);
}

        write(sb, PROP_SCREENS, mSupportsScreens);

return sb.toString();
}
//Synthetic comment -- @@ -195,7 +190,7 @@
return 1;
}

        comp = mSupportsScreens.compareTo(o.mSupportsScreens);
if (comp != 0) return comp;

if (mGlVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
//Synthetic comment -- @@ -220,7 +215,7 @@
*/
public void initFromLogLine(String line) {
int colon = line.indexOf(':');
        mBuildInfo = Integer.parseInt(line.substring(0, colon));
String[] properties = line.substring(colon+1).split(";");
HashMap<String, String> map = new HashMap<String, String>();
for (String prop : properties) {
//Synthetic comment -- @@ -231,6 +226,7 @@
}

private void setValues(Map<String, String> values) {
mMinor = Integer.parseInt(values.get(PROP_MINOR));
mRelativePath = values.get(PROP_PROJECT);
mMinSdkVersion = Integer.parseInt(values.get(PROP_API));








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
//Synthetic comment -- index 693a5f8..8d298df 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.sdklib.io.IAbstractFolder.FilenameFilter;
import com.android.sdklib.xml.AndroidManifestParser;
import com.android.sdklib.xml.ManifestData;

import org.xml.sax.SAXException;

//Synthetic comment -- @@ -161,7 +162,7 @@
"# Multi-APK BUILD LOG.\n" +
"# This file serves two purpose:\n" +
"# - A log of what was built, showing what went in each APK and their properties.\n" +
                    "#   You can refer to this if you get a bug report for a specific versionCode." +
"# - A way to update builds through minor revisions for specific APKs.\n" +
"# Only edit manually to change the minor properties for build you wish to respin.\n" +
"# Note that all APKs will be regenerated all the time.\n");
//Synthetic comment -- @@ -171,7 +172,7 @@

writer.append(
"# The format of the following lines is:\n" +
                    "# <build number>:<property1>;<property2>;<property3>;...\n" +
"# Properties are written as <name>=<value>\n");

for (ApkData apk : apks) {
//Synthetic comment -- @@ -309,19 +310,58 @@
// - GL version
// - ABI (not managed at the Manifest level).
// if those values are the same between 2 manifest, then it's an error.
                if (minSdkVersion == previousManifest.data.getMinSdkVersion() &&
                        manifestData.getSupportsScreensValues().equals(
                                previousManifest.data.getSupportsScreensValues()) &&
                        manifestData.getGlEsVersion() == previousManifest.data.getGlEsVersion()) {

                    throw new ExportException(String.format(
                            "Android manifests must differ in at least one of the following values:\n" +
                            "- minSdkVersion\n" +
                            "- SupportsScreen\n" +
                            "- GL ES version.\n" +
                            "%1$s and %2$s are considered identical for multi-apk export.",
                            androidManifest.getOsLocation(),
                            previousManifest.file.getOsLocation()));
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java
//Synthetic comment -- index da96cd4..911c283 100644

//Synthetic comment -- @@ -158,7 +158,7 @@
*
* To get an instance with all the actual values, use {@link #resolveSupportsScreensValues(int)}
*/
    public final static class SupportsScreens implements Comparable<SupportsScreens> {
private Boolean mResizeable;
private Boolean mAnyDensity;
private Boolean mSmallScreens;
//Synthetic comment -- @@ -168,6 +168,11 @@
public SupportsScreens() {
}

public SupportsScreens(String value) {
String[] values = value.split("\\|");

//Synthetic comment -- @@ -303,14 +308,143 @@
return false;
}

        public int compareTo(SupportsScreens o) {
return 0;
}

@Override
public String toString() {
            return String.format("%1$s|%2$s|%3$s|%4$s|%5$s",
                    mAnyDensity, mResizeable, mSmallScreens, mNormalScreens, mLargeScreens);
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/SupportsScreensTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/SupportsScreensTest.java
//Synthetic comment -- index c0cb12f..9eda533 100644

//Synthetic comment -- @@ -57,4 +57,74 @@
assertEquals(Boolean.TRUE, supportsScreens.getNormalScreens());
assertEquals(Boolean.FALSE, supportsScreens.getLargeScreens());
}
}







