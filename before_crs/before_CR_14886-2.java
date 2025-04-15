/*New log format for multi-apk export.

Also added support for supports-screens in the log.

Change-Id:I702fb511eb4da0094917d7c0d9fe792f582adeb2*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/MultiApkExportTask.java b/anttasks/src/com/android/ant/MultiApkExportTask.java
//Synthetic comment -- index 3d1131b..151b81b 100644

//Synthetic comment -- @@ -165,8 +165,8 @@
}

// set the version code, and filtering
                    String compositeVersionCode = getVersionCodeString(versionCode, apk);
                    addProp(subAnt, "version.code", compositeVersionCode);
System.out.println("Composite versionCode: " + compositeVersionCode);
String abi = apk.getAbi();
if (abi != null) {
//Synthetic comment -- @@ -279,20 +279,6 @@
}

/**
     * Computes and returns the composite version code
     * @param versionCode the major version code.
     * @param apkData the apk data.
     * @return the composite versionCode to be used in the manifest.
     */
    private String getVersionCodeString(int versionCode, ApkData apkData) {
        int trueVersionCode = versionCode * MultiApkExportHelper.OFFSET_VERSION_CODE;
        trueVersionCode += apkData.getBuildInfo() * MultiApkExportHelper.OFFSET_BUILD_INFO;
        trueVersionCode += apkData.getMinor();

        return Integer.toString(trueVersionCode);
    }

    /**
* Returns the {@link File} for the build log.
* @param appPackage
* @param versionCode








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java
//Synthetic comment -- index 9c3a8c5..f880079 100644

//Synthetic comment -- @@ -16,10 +16,11 @@

package com.android.sdklib.internal.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
* Class representing one apk that needs to be generated. This contains
//Synthetic comment -- @@ -30,16 +31,14 @@
*/
public class ApkData implements Comparable<ApkData> {

    private final static int INDEX_OUTPUTNAME = 0;
    private final static int INDEX_PROJECT    = 1;
    private final static int INDEX_MINOR      = 2;
    private final static int INDEX_MINSDK     = 3;
    private final static int INDEX_ABI        = 4;
    private final static int INDEX_OPENGL     = 5;
    private final static int INDEX_SCREENSIZE = 6;
    private final static int INDEX_LOCALES    = 7;
    private final static int INDEX_DENSITY    = 8;
    private final static int INDEX_MAX        = 9;

private String mOutputName;
private String mRelativePath;
//Synthetic comment -- @@ -50,13 +49,19 @@
// the following are used to sort the export data and generate buildInfo
private int mMinSdkVersion;
private String mAbi;
    private int mGlVersion;
    // screen size?

    public ApkData() {
// do nothing.
}

public ApkData(ApkData data) {
mRelativePath = data.mRelativePath;
mProject = data.mProject;
//Synthetic comment -- @@ -65,6 +70,7 @@
mMinSdkVersion = data.mMinSdkVersion;
mAbi = data.mAbi;
mGlVersion = data.mGlVersion;
}

public String getOutputName() {
//Synthetic comment -- @@ -111,10 +117,6 @@
return mMinSdkVersion;
}

    public void setMinSdkVersion(int minSdkVersion) {
        mMinSdkVersion = minSdkVersion;
    }

public String getAbi() {
return mAbi;
}
//Synthetic comment -- @@ -127,18 +129,49 @@
return mGlVersion;
}

    public void setGlVersion(int glVersion) {
        mGlVersion = glVersion;
}

@Override
public String toString() {
        StringBuilder sb = new StringBuilder(mOutputName);
        sb.append(" / ").append(mRelativePath);
        sb.append(" / ").append(mBuildInfo);
        sb.append(" / ").append(mMinor);
        sb.append(" / ").append(mMinSdkVersion);
        sb.append(" / ").append(mAbi);

return sb.toString();
}
//Synthetic comment -- @@ -149,9 +182,11 @@
return minSdkDiff;
}

if (mAbi != null) {
if (o.mAbi != null) {
                return mAbi.compareTo(o.mAbi);
} else {
return -1;
}
//Synthetic comment -- @@ -159,13 +194,17 @@
return 1;
}

        if (mGlVersion != 0) {
            if (o.mGlVersion != 0) {
                return mGlVersion - o.mGlVersion;
} else {
return -1;
}
        } else if (o.mGlVersion != 0) {
return 1;
}

//Synthetic comment -- @@ -173,79 +212,49 @@
}

/**
     * Writes the apk description in the given writer. a single line is used to write
     * everything.
     * @param writer The {@link OutputStreamWriter} to write to.
     * @throws IOException
     *
     * @see {@link #read(String)}
     */
    public void write(OutputStreamWriter writer) throws IOException {
        for (int i = 0 ; i < ApkData.INDEX_MAX ; i++) {
            write(i, writer);
        }
    }

    /**
* reads the apk description from a log line.
* @param line The fields to read, comma-separated.
*
     * @see #write(FileWriter)
*/
    public void read(String line) {
        String[] dataStrs = line.split(",");
        for (int i = 0 ; i < ApkData.INDEX_MAX ; i++) {
            read(i, dataStrs);
}
}

    private void write(int index, OutputStreamWriter writer) throws IOException {
        switch (index) {
            case INDEX_OUTPUTNAME:
                writeValue(writer, mOutputName);
                break;
            case INDEX_PROJECT:
                writeValue(writer, mRelativePath);
                break;
            case INDEX_MINOR:
                writeValue(writer, mMinor);
                break;
            case INDEX_MINSDK:
                writeValue(writer, mMinSdkVersion);
                break;
            case INDEX_ABI:
                writeValue(writer, mAbi != null ? mAbi : "");
                break;
        }
}

    private void read(int index, String[] data) {
        switch (index) {
            case INDEX_OUTPUTNAME:
                mOutputName = data[index];
                break;
            case INDEX_PROJECT:
                mRelativePath = data[index];
                break;
            case INDEX_MINOR:
                mMinor = Integer.parseInt(data[index]);
                break;
            case INDEX_MINSDK:
                mMinSdkVersion = Integer.parseInt(data[index]);
                break;
            case INDEX_ABI:
                if (index < data.length && data[index].length() > 0) {
                    mAbi = data[index];
                }
                break;
        }
    }

    private static void writeValue(OutputStreamWriter writer, String value) throws IOException {
        writer.append(value).append(',');
    }

    private static void writeValue(OutputStreamWriter writer, int value) throws IOException {
        writeValue(writer, Integer.toString(value));
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
//Synthetic comment -- index 235e436..55dfbf0 100644

//Synthetic comment -- @@ -51,10 +51,10 @@
private final int mVersionCode;
private final Target mTarget;

    public final static int MAX_MINOR = 100;
    public final static int MAX_BUILDINFO = 100;
    public final static int OFFSET_BUILD_INFO = MAX_MINOR;
    public final static int OFFSET_VERSION_CODE = OFFSET_BUILD_INFO * MAX_BUILDINFO;

public static final class ExportException extends Exception {
private static final long serialVersionUID = 1L;
//Synthetic comment -- @@ -157,18 +157,25 @@
try {
writer = new OutputStreamWriter(buildLog.getOutputStream());

            writer.append("# Multi-APK BUILD log.\n");
            writer.append("# Only edit manually to change minor versions.\n");

writeValue(writer, "package", mAppPackage);
writeValue(writer, "versionCode", mVersionCode);

            writer.append("# what follows is one line per generated apk with its description.\n");
            writer.append("# the format is CSV in the following order:\n");
            writer.append("# apkname,project,minor, minsdkversion, abi filter,\n");

for (ApkData apk : apks) {
                apk.write(writer);
writer.append('\n');
}

//Synthetic comment -- @@ -199,7 +206,8 @@
* gets the projects to export from the property, checks they exist, validates them,
* loads their export info and return it.
* If a project does not exist or is not valid, this will throw a {@link BuildException}.
     * @param projects the Ant project.
* @throws ExportException
*/
private ApkData[] getProjects(String projects) throws ExportException {
//Synthetic comment -- @@ -321,9 +329,9 @@
manifests.add(new Manifest(androidManifest, manifestData));

ArrayList<ApkData> dataList = new ArrayList<ApkData>();
            ApkData data = new ApkData();
dataList.add(data);
            data.setMinSdkVersion(minSdkVersion);

// only look for more exports if the target is not clean.
if (mTarget != Target.CLEAN) {
//Synthetic comment -- @@ -386,7 +394,6 @@
bufferedReader = new BufferedReader(reader);
String line;
int lineIndex = 0;
            int apkIndex = 0;
while ((line = bufferedReader.readLine()) != null) {
line = line.trim();
if (line.length() == 0 || line.startsWith("#")) {
//Synthetic comment -- @@ -405,9 +412,8 @@
default:
// read apk description
ApkData data = new ApkData();
                        data.setBuildInfo(apkIndex++);
datalist.add(data);
                        data.read(line);
if (data.getMinor() >= MAX_MINOR) {
throw new ExportException(
"Valid minor version code values are 0-" + (MAX_MINOR-1));








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java
//Synthetic comment -- index 168125e..955f8ed 100644

//Synthetic comment -- @@ -152,6 +152,19 @@
private Boolean mNormalScreens;
private Boolean mLargeScreens;

/**
* Returns an instance of {@link SupportsScreens} initialized with the default values
* based on the given targetSdkVersion.
//Synthetic comment -- @@ -280,7 +293,7 @@

@Override
public String toString() {
            return String.format("AD: %1$s, RS: %2$s, SS: %3$s, NS: %4$s, LS: %5$s",
mAnyDensity, mResizeable, mSmallScreens, mNormalScreens, mLargeScreens);
}
}







