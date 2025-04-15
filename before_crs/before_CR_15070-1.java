/*Add support for locale filters in multi-apk export.

Change-Id:Ia3faf7a6818de0e579cf491b139a41f4d379935e*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/MultiApkExportTask.java b/anttasks/src/com/android/ant/MultiApkExportTask.java
//Synthetic comment -- index fd33b92..d22b1e1 100644

//Synthetic comment -- @@ -205,7 +205,8 @@
fileSet.setIncludes("build.xml");
subAnt.addFileset(fileSet);

//        subAnt.setVerbose(true);

if (mTarget == Target.RELEASE) {
// only do the compilation part if it's the first time we export








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java
//Synthetic comment -- index 4ba59b4..471070a 100644

//Synthetic comment -- @@ -16,12 +16,15 @@

package com.android.sdklib.internal.export;

import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.SupportsScreens;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
* Class representing one apk that needs to be generated. This contains
//Synthetic comment -- @@ -40,6 +43,7 @@
private static final String PROP_MINOR = "minor";
private static final String PROP_BUILDINFO = "buildinfo";
private static final String PROP_DENSITY = "splitDensity";

/**
* List of ABI order.
//Synthetic comment -- @@ -53,6 +57,15 @@
new String[] { "armeabi", "armeabi-v7a" }
};

private final HashMap<String, String> mOutputNames = new HashMap<String, String>();
private String mRelativePath;
private File mProject;
//Synthetic comment -- @@ -67,18 +80,20 @@

// additional apk generation that doesn't impact the build info.
private boolean mSplitDensity;

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
mBuildInfo = data.mBuildInfo;
//Synthetic comment -- @@ -101,7 +116,7 @@
return mRelativePath;
}

    public void setRelativePath(String relativePath) {
mRelativePath = relativePath;
}

//Synthetic comment -- @@ -109,7 +124,7 @@
return mProject;
}

    public void setProject(File project) {
mProject = project;
}

//Synthetic comment -- @@ -117,7 +132,7 @@
return mBuildInfo;
}

    public void setBuildInfo(int buildInfo) {
mBuildInfo = buildInfo;
}

//Synthetic comment -- @@ -125,7 +140,7 @@
return mMinor;
}

    public void setMinor(int minor) {
mMinor = minor;
}

//Synthetic comment -- @@ -137,7 +152,7 @@
return mAbi;
}

    public void setAbi(String abi) {
mAbi = abi;
}

//Synthetic comment -- @@ -162,27 +177,48 @@
return trueVersionCode;
}

    public void setSplitDensity(boolean splitDensity) {
mSplitDensity = splitDensity;
}

    public boolean isSplitDensity() {
        return mSplitDensity;
}

/**
* Returns a map of pair values (apk name suffix, aapt res filter) to be used to generate
* multiple soft apk variants.
*/
    public Map<String, String> getSoftVariantMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        if (mSplitDensity) {
            map.put("hdpi", "hdpi,nodpi");
            map.put("mdpi", "mdpi,nodpi");
            map.put("ldpi", "ldpi,nodpi");
}

        return map;
}

@Override
//Synthetic comment -- @@ -211,6 +247,10 @@
write(sb, PROP_DENSITY, Boolean.toString(true));
}

write(sb, PROP_SCREENS, mSupportsScreens.getEncodedValues());
} else {
write(sb, "resources", getSoftVariantMap().get(key));
//Synthetic comment -- @@ -249,6 +289,9 @@
if (comp != 0) return comp;
}

return 0;
}

//Synthetic comment -- @@ -279,7 +322,8 @@
if (mMinSdkVersion != apk.mMinSdkVersion ||
mSupportsScreens.equals(apk.mSupportsScreens) == false ||
mGlVersion != apk.mGlVersion ||
                mSplitDensity != apk.mSplitDensity) {
return false;
}

//Synthetic comment -- @@ -312,7 +356,7 @@
setValues(map);
}

    private void setValues(Map<String, String> values) {
String tmp;
try {
mBuildInfo = Integer.parseInt(values.get(PROP_BUILDINFO));
//Synthetic comment -- @@ -343,6 +387,12 @@
if (tmp != null) {
mSupportsScreens = new SupportsScreens(tmp);
}
}

private void write(StringBuilder sb, String name, Object value) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
//Synthetic comment -- index 9340c47..dad57e7 100644

//Synthetic comment -- @@ -179,14 +179,16 @@
writer.append(apk.getLogLine(null));
writer.append('\n');

                // display the soft variant as comments to the log file.
Map<String, String> softVariants = apk.getSoftVariantMap();
if (softVariants.size() > 0) {
                    writer.append("# Soft Variants -- DO NOT UNCOMMENT:\n");
}

for (String softVariant : softVariants.keySet()) {
                    writer.append("# ");
writer.append(apk.getLogLine(softVariant));
writer.append('\n');
}
//Synthetic comment -- @@ -399,6 +401,11 @@
}

ApkSettings apkSettings = new ApkSettings(projectProp);
if (apkSettings.isSplitByAbi()) {
// need to find the available ABIs.
List<String> abis = findAbis(projectFolder);
//Synthetic comment -- @@ -410,14 +417,10 @@
}

current.setAbi(abi);
                        current = null;
                    }
                }

                if (apkSettings.isSplitByDensity()) {
                    // set this value for all the apk that were create.
                    for (ApkData apk : dataList) {
                        apk.setSplitDensity(true);
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ApkSettings.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ApkSettings.java
//Synthetic comment -- index 3f22416..3b8e85c 100644

//Synthetic comment -- @@ -16,16 +16,24 @@

package com.android.sdklib.internal.project;


/**
* Settings for multiple APK generation.
*/
public class ApkSettings {
private boolean mSplitByDensity = false;
private boolean mSplitByAbi = false;

    public ApkSettings() {
    }

/**
* Creates an ApkSettings and fills it from the project settings read from a
//Synthetic comment -- @@ -36,6 +44,12 @@
ProjectProperties.PROPERTY_SPLIT_BY_DENSITY));
mSplitByAbi =  Boolean.parseBoolean(properties.getProperty(
ProjectProperties.PROPERTY_SPLIT_BY_ABI));
}

/**
//Synthetic comment -- @@ -85,4 +99,39 @@
(mSplitByDensity ? 1 : 0) +
(mSplitByAbi ? 2 : 0)).hashCode();
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java
//Synthetic comment -- index d05c9f6..523f45e 100644

//Synthetic comment -- @@ -51,6 +51,7 @@

public final static String PROPERTY_SPLIT_BY_DENSITY = "split.density";
public final static String PROPERTY_SPLIT_BY_ABI = "split.abi";

public final static String PROPERTY_TESTED_PROJECT = "tested.project.dir";








