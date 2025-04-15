/*New log format for multi-apk export.

Also added support for supports-screens in the log.

Change-Id:I702fb511eb4da0094917d7c0d9fe792f582adeb2*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/MultiApkExportTask.java b/anttasks/src/com/android/ant/MultiApkExportTask.java
//Synthetic comment -- index 3d1131b..151b81b 100644

//Synthetic comment -- @@ -165,8 +165,8 @@
}

// set the version code, and filtering
                    int compositeVersionCode = apk.getCompositeVersionCode(versionCode);
                    addProp(subAnt, "version.code", Integer.toString(compositeVersionCode));
System.out.println("Composite versionCode: " + compositeVersionCode);
String abi = apk.getAbi();
if (abi != null) {
//Synthetic comment -- @@ -279,20 +279,6 @@
}

/**
* Returns the {@link File} for the build log.
* @param appPackage
* @param versionCode








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java
//Synthetic comment -- index 9c3a8c5..f880079 100644

//Synthetic comment -- @@ -16,10 +16,11 @@

package com.android.sdklib.internal.export;

import com.android.sdklib.xml.ManifestData.SupportsScreens;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
* Class representing one apk that needs to be generated. This contains
//Synthetic comment -- @@ -30,16 +31,14 @@
*/
public class ApkData implements Comparable<ApkData> {

    private static final String PROP_SCREENS = "screens";
    private static final String PROP_ABI = "abi";
    private static final String PROP_GL = "gl";
    private static final String PROP_API = "api";
    private static final String PROP_PROJECT = "project";
    private static final String PROP_MINOR = "minor";
    private static final String PROP_BUILDINFO = "buildinfo";
    private static final String PROP_OUTPUTNAME = "outputname";

private String mOutputName;
private String mRelativePath;
//Synthetic comment -- @@ -50,13 +49,19 @@
// the following are used to sort the export data and generate buildInfo
private int mMinSdkVersion;
private String mAbi;
    private int mGlVersion = -1;
    private SupportsScreens mSupportsScreens;

    ApkData() {
// do nothing.
}

    public ApkData(int minSdkVersion, SupportsScreens supportsScreens, int glEsVersion) {
        mMinSdkVersion = minSdkVersion;
        mSupportsScreens = supportsScreens;
        mGlVersion = glEsVersion;
    }

public ApkData(ApkData data) {
mRelativePath = data.mRelativePath;
mProject = data.mProject;
//Synthetic comment -- @@ -65,6 +70,7 @@
mMinSdkVersion = data.mMinSdkVersion;
mAbi = data.mAbi;
mGlVersion = data.mGlVersion;
        mSupportsScreens = data.mSupportsScreens;
}

public String getOutputName() {
//Synthetic comment -- @@ -111,10 +117,6 @@
return mMinSdkVersion;
}

public String getAbi() {
return mAbi;
}
//Synthetic comment -- @@ -127,18 +129,49 @@
return mGlVersion;
}

    public SupportsScreens getSupportsScreens() {
        return mSupportsScreens;
    }

    /**
     * Computes and returns the composite version code
     * @param versionCode the major version code.
     * @return the composite versionCode to be used in the manifest.
     */
    public int getCompositeVersionCode(int versionCode) {
        int trueVersionCode = versionCode * MultiApkExportHelper.OFFSET_VERSION_CODE;
        trueVersionCode += getBuildInfo() * MultiApkExportHelper.OFFSET_BUILD_INFO;
        trueVersionCode += getMinor();

        return trueVersionCode;
}

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

        if (mGlVersion != -1) {
            write(sb, PROP_GL, mGlVersion);
        }

        if (mAbi != null) {
            write(sb, PROP_ABI, mAbi);
        }

        write(sb, PROP_SCREENS, mSupportsScreens);

return sb.toString();
}
//Synthetic comment -- @@ -149,9 +182,11 @@
return minSdkDiff;
}

        int comp;
if (mAbi != null) {
if (o.mAbi != null) {
                comp = mAbi.compareTo(o.mAbi);
                if (comp != 0) return comp;
} else {
return -1;
}
//Synthetic comment -- @@ -159,13 +194,17 @@
return 1;
}

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

//Synthetic comment -- @@ -173,79 +212,49 @@
}

/**
* reads the apk description from a log line.
* @param line The fields to read, comma-separated.
*
     * @see #getLogLine()
*/
    public void initFromLogLine(String line) {
        int colon = line.indexOf(':');
        mBuildInfo = Integer.parseInt(line.substring(0, colon));
        String[] properties = line.substring(colon+1).split(";");
        HashMap<String, String> map = new HashMap<String, String>();
        for (String prop : properties) {
            colon = prop.indexOf('=');
            map.put(prop.substring(0, colon), prop.substring(colon+1));
        }
        setValues(map);
    }

    private void setValues(Map<String, String> values) {
        mMinor = Integer.parseInt(values.get(PROP_MINOR));
        mRelativePath = values.get(PROP_PROJECT);
        mMinSdkVersion = Integer.parseInt(values.get(PROP_API));

        String tmp = values.get(PROP_GL);
        if (tmp != null) {
            mGlVersion = Integer.parseInt(tmp);
        }

        tmp = values.get(PROP_ABI);
        if (tmp != null) {
            mAbi = tmp;
        }

        tmp = values.get(PROP_SCREENS);
        if (tmp != null) {
            mSupportsScreens = new SupportsScreens(tmp);
}
}

    private void write(StringBuilder sb, String name, Object value) {
        sb.append(name + "=").append(value).append(';');
}

    private void write(StringBuilder sb, String name, int value) {
        sb.append(name + "=").append(value).append(';');
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
//Synthetic comment -- index 235e436..55dfbf0 100644

//Synthetic comment -- @@ -51,10 +51,10 @@
private final int mVersionCode;
private final Target mTarget;

    final static int MAX_MINOR = 100;
    final static int MAX_BUILDINFO = 100;
    final static int OFFSET_BUILD_INFO = MAX_MINOR;
    final static int OFFSET_VERSION_CODE = OFFSET_BUILD_INFO * MAX_BUILDINFO;

public static final class ExportException extends Exception {
private static final long serialVersionUID = 1L;
//Synthetic comment -- @@ -157,18 +157,25 @@
try {
writer = new OutputStreamWriter(buildLog.getOutputStream());

            writer.append(
                    "# Multi-APK BUILD LOG.\n" +
                    "# This file serves two purpose:\n" +
                    "# - A log of what was built, showing what went in each APK and their properties.\n" +
                    "#   You can refer to this if you get a bug report for a specific versionCode." +
                    "# - A way to update builds through minor revisions for specific APKs.\n" +
                    "# Only edit manually to change the minor properties for build you wish to respin.\n" +
                    "# Note that all APKs will be regenerated all the time.\n");

writeValue(writer, "package", mAppPackage);
writeValue(writer, "versionCode", mVersionCode);

            writer.append(
                    "# The format of the following lines is:\n" +
                    "# <build number>:<property1>;<property2>;<property3>;...\n" +
                    "# Properties are written as <name>=<value>\n");

for (ApkData apk : apks) {
                writer.append(apk.getLogLine());
writer.append('\n');
}

//Synthetic comment -- @@ -199,7 +206,8 @@
* gets the projects to export from the property, checks they exist, validates them,
* loads their export info and return it.
* If a project does not exist or is not valid, this will throw a {@link BuildException}.
     * @param projects the string containing all the relative paths to the projects. This is
     * usually read from export.properties.
* @throws ExportException
*/
private ApkData[] getProjects(String projects) throws ExportException {
//Synthetic comment -- @@ -321,9 +329,9 @@
manifests.add(new Manifest(androidManifest, manifestData));

ArrayList<ApkData> dataList = new ArrayList<ApkData>();
            ApkData data = new ApkData(minSdkVersion, manifestData.getSupportsScreensValues(),
                    manifestData.getGlEsVersion());
dataList.add(data);

// only look for more exports if the target is not clean.
if (mTarget != Target.CLEAN) {
//Synthetic comment -- @@ -386,7 +394,6 @@
bufferedReader = new BufferedReader(reader);
String line;
int lineIndex = 0;
while ((line = bufferedReader.readLine()) != null) {
line = line.trim();
if (line.length() == 0 || line.startsWith("#")) {
//Synthetic comment -- @@ -405,9 +412,8 @@
default:
// read apk description
ApkData data = new ApkData();
datalist.add(data);
                        data.initFromLogLine(line);
if (data.getMinor() >= MAX_MINOR) {
throw new ExportException(
"Valid minor version code values are 0-" + (MAX_MINOR-1));








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java
//Synthetic comment -- index 168125e..955f8ed 100644

//Synthetic comment -- @@ -152,6 +152,19 @@
private Boolean mNormalScreens;
private Boolean mLargeScreens;

        public SupportsScreens() {
        }

        public SupportsScreens(String value) {
            String[] values = value.split("\\|");

            mAnyDensity = Boolean.valueOf(values[0]);
            mResizeable = Boolean.valueOf(values[1]);
            mSmallScreens = Boolean.valueOf(values[2]);
            mNormalScreens = Boolean.valueOf(values[3]);
            mLargeScreens = Boolean.valueOf(values[4]);
        }

/**
* Returns an instance of {@link SupportsScreens} initialized with the default values
* based on the given targetSdkVersion.
//Synthetic comment -- @@ -280,7 +293,7 @@

@Override
public String toString() {
            return String.format("%1$s|%2$s|%3$s|%4$s|%5$s",
mAnyDensity, mResizeable, mSmallScreens, mNormalScreens, mLargeScreens);
}
}







