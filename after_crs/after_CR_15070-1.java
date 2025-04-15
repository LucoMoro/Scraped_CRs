/*Add support for locale filters in multi-apk export.

Change-Id:Ia3faf7a6818de0e579cf491b139a41f4d379935e*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/MultiApkExportTask.java b/anttasks/src/com/android/ant/MultiApkExportTask.java
//Synthetic comment -- index fd33b92..d22b1e1 100644

//Synthetic comment -- @@ -205,7 +205,8 @@
fileSet.setIncludes("build.xml");
subAnt.addFileset(fileSet);

        // TODO: send the verbose flag from the main build.xml to the subAnt project.
        //subAnt.setVerbose(true);

if (mTarget == Target.RELEASE) {
// only do the compilation part if it's the first time we export








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java
//Synthetic comment -- index 4ba59b4..471070a 100644

//Synthetic comment -- @@ -16,12 +16,15 @@

package com.android.sdklib.internal.export;

import com.android.sdklib.internal.project.ApkSettings;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.SupportsScreens;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
* Class representing one apk that needs to be generated. This contains
//Synthetic comment -- @@ -40,6 +43,7 @@
private static final String PROP_MINOR = "minor";
private static final String PROP_BUILDINFO = "buildinfo";
private static final String PROP_DENSITY = "splitDensity";
    private static final String PROP_LOCALEFILTERS = "localeFilters";

/**
* List of ABI order.
//Synthetic comment -- @@ -53,6 +57,15 @@
new String[] { "armeabi", "armeabi-v7a" }
};

    /**
     * List of densities and their associated aapt filter.
     */
    private static final String[][] DENSITY_LIST = new String[][] {
        new String[] { "hdpi", "hdpi,nodpi" },
        new String[] { "mdpi", "mdpi,nodpi" },
        new String[] { "ldpi", "ldpi,nodpi" },
    };

private final HashMap<String, String> mOutputNames = new HashMap<String, String>();
private String mRelativePath;
private File mProject;
//Synthetic comment -- @@ -67,18 +80,20 @@

// additional apk generation that doesn't impact the build info.
private boolean mSplitDensity;
    private final HashMap<String, String> mLocaleFilters = new HashMap<String, String>();
    private Map<String, String> mSoftVariantMap;

ApkData() {
// do nothing.
}

    ApkData(int minSdkVersion, SupportsScreens supportsScreens, int glEsVersion) {
mMinSdkVersion = minSdkVersion;
mSupportsScreens = supportsScreens;
mGlVersion = glEsVersion;
}

    ApkData(ApkData data) {
mRelativePath = data.mRelativePath;
mProject = data.mProject;
mBuildInfo = data.mBuildInfo;
//Synthetic comment -- @@ -101,7 +116,7 @@
return mRelativePath;
}

    void setRelativePath(String relativePath) {
mRelativePath = relativePath;
}

//Synthetic comment -- @@ -109,7 +124,7 @@
return mProject;
}

    void setProject(File project) {
mProject = project;
}

//Synthetic comment -- @@ -117,7 +132,7 @@
return mBuildInfo;
}

    void setBuildInfo(int buildInfo) {
mBuildInfo = buildInfo;
}

//Synthetic comment -- @@ -125,7 +140,7 @@
return mMinor;
}

    void setMinor(int minor) {
mMinor = minor;
}

//Synthetic comment -- @@ -137,7 +152,7 @@
return mAbi;
}

    void setAbi(String abi) {
mAbi = abi;
}

//Synthetic comment -- @@ -162,27 +177,48 @@
return trueVersionCode;
}

    synchronized void setSplitDensity(boolean splitDensity) {
mSplitDensity = splitDensity;
        mSoftVariantMap = null;

}

    synchronized void setLocaleFilters(Map<String, String> localeFilters) {
        mLocaleFilters.clear();
        mLocaleFilters.putAll(localeFilters);
        mSoftVariantMap = null;
}

/**
* Returns a map of pair values (apk name suffix, aapt res filter) to be used to generate
* multiple soft apk variants.
*/
    public synchronized Map<String, String> getSoftVariantMap() {
        if (mSoftVariantMap == null) {
            HashMap<String, String> map = new HashMap<String, String>();

            if (mSplitDensity && mLocaleFilters.size() > 0) {
                for (String[] density : DENSITY_LIST) {
                    for (Entry<String,String> entry : mLocaleFilters.entrySet()) {
                        map.put(density[0] + "-" + entry.getKey(),
                                density[1] + "," + entry.getValue());
                    }
                }

            } else if (mSplitDensity) {
                for (String[] density : DENSITY_LIST) {
                    map.put(density[0], density[1]);
                }

            } else if (mLocaleFilters.size() > 0) {
                map.putAll(mLocaleFilters);

            }

            mSoftVariantMap = Collections.unmodifiableMap(map);
}

        return mSoftVariantMap;
}

@Override
//Synthetic comment -- @@ -211,6 +247,10 @@
write(sb, PROP_DENSITY, Boolean.toString(true));
}

            if (mLocaleFilters.size() > 0) {
                write(sb, PROP_LOCALEFILTERS, ApkSettings.writeLocaleFilters(mLocaleFilters));
            }

write(sb, PROP_SCREENS, mSupportsScreens.getEncodedValues());
} else {
write(sb, "resources", getSoftVariantMap().get(key));
//Synthetic comment -- @@ -249,6 +289,9 @@
if (comp != 0) return comp;
}

        // Do not compare mSplitDensity or mLocaleFilter because they do not generate build info,
        // and also, we should already have a difference at this point.

return 0;
}

//Synthetic comment -- @@ -279,7 +322,8 @@
if (mMinSdkVersion != apk.mMinSdkVersion ||
mSupportsScreens.equals(apk.mSupportsScreens) == false ||
mGlVersion != apk.mGlVersion ||
                mSplitDensity != apk.mSplitDensity ||
                mLocaleFilters.equals(apk.mLocaleFilters) == false) {
return false;
}

//Synthetic comment -- @@ -312,7 +356,7 @@
setValues(map);
}

    private synchronized void setValues(Map<String, String> values) {
String tmp;
try {
mBuildInfo = Integer.parseInt(values.get(PROP_BUILDINFO));
//Synthetic comment -- @@ -343,6 +387,12 @@
if (tmp != null) {
mSupportsScreens = new SupportsScreens(tmp);
}

        tmp = values.get(PROP_LOCALEFILTERS);
        if (tmp != null) {
            mLocaleFilters.putAll(ApkSettings.readLocaleFilters(tmp));
        }
        mSoftVariantMap = null;
}

private void write(StringBuilder sb, String name, Object value) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
//Synthetic comment -- index 9340c47..dad57e7 100644

//Synthetic comment -- @@ -179,14 +179,16 @@
writer.append(apk.getLogLine(null));
writer.append('\n');

                // display the soft variants for this apkData as comments to the log file.
                // since they all share the same Build Info and cannot be modified by the dev
                // and we won't read them back from the log.
Map<String, String> softVariants = apk.getSoftVariantMap();
if (softVariants.size() > 0) {
                    writer.append(" # Soft Variants -- DO NOT UNCOMMENT:\n");
}

for (String softVariant : softVariants.keySet()) {
                    writer.append(" # ");
writer.append(apk.getLogLine(softVariant));
writer.append('\n');
}
//Synthetic comment -- @@ -399,6 +401,11 @@
}

ApkSettings apkSettings = new ApkSettings(projectProp);

                // get the density/locale values
                boolean splitByDensity = apkSettings.isSplitByDensity();
                Map<String, String> localeFilters = apkSettings.getLocaleFilters();

if (apkSettings.isSplitByAbi()) {
// need to find the available ABIs.
List<String> abis = findAbis(projectFolder);
//Synthetic comment -- @@ -410,14 +417,10 @@
}

current.setAbi(abi);
                        current.setSplitDensity(splitByDensity);
                        current.setLocaleFilters(localeFilters);

                        current = null;
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ApkSettings.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ApkSettings.java
//Synthetic comment -- index 3f22416..3b8e85c 100644

//Synthetic comment -- @@ -16,16 +16,24 @@

package com.android.sdklib.internal.project;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;


/**
* Settings for multiple APK generation.
*/
public class ApkSettings {
    private final static char CHAR_EQUAL = ':';
    private final static char CHAR_SEP = '|';
    private final static String STR_SEP = Pattern.quote(new String(new char[] { CHAR_SEP }));

private boolean mSplitByDensity = false;
private boolean mSplitByAbi = false;
    private final Map<String, String> mSplitByLocale;

/**
* Creates an ApkSettings and fills it from the project settings read from a
//Synthetic comment -- @@ -36,6 +44,12 @@
ProjectProperties.PROPERTY_SPLIT_BY_DENSITY));
mSplitByAbi =  Boolean.parseBoolean(properties.getProperty(
ProjectProperties.PROPERTY_SPLIT_BY_ABI));
        String locale = properties.getProperty(ProjectProperties.PROPERTY_SPLIT_BY_LOCALE);
        if (locale != null && locale.length() > 0) {
            mSplitByLocale = readLocaleFilters(locale);
        } else {
            mSplitByLocale = Collections.unmodifiableMap(new HashMap<String, String>());
        }
}

/**
//Synthetic comment -- @@ -85,4 +99,39 @@
(mSplitByDensity ? 1 : 0) +
(mSplitByAbi ? 2 : 0)).hashCode();
}

    public static Map<String, String> readLocaleFilters(String locale) {
        HashMap<String, String> map = new HashMap<String, String>();
        String[] filters = locale.split(STR_SEP);
        for (String filter : filters) {
            int charPos = filter.indexOf(CHAR_EQUAL);
            if (charPos > 0) {
                map.put(filter.substring(0, charPos), filter.substring(charPos+1));
            }
        }

        return Collections.unmodifiableMap(map);
    }

    public static String writeLocaleFilters(Map<String, String> filterMap) {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (Entry<String, String> entry : filterMap.entrySet()) {
            if (first == false) {
                sb.append(CHAR_SEP);
            }
            first = false;

            sb.append(entry.getKey());
            sb.append(CHAR_EQUAL);
            sb.append(entry.getValue());
        }

        return sb.toString();
    }

    public Map<String, String> getLocaleFilters() {
        return mSplitByLocale;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java
//Synthetic comment -- index d05c9f6..523f45e 100644

//Synthetic comment -- @@ -51,6 +51,7 @@

public final static String PROPERTY_SPLIT_BY_DENSITY = "split.density";
public final static String PROPERTY_SPLIT_BY_ABI = "split.abi";
    public final static String PROPERTY_SPLIT_BY_LOCALE = "split.locale";

public final static String PROPERTY_TESTED_PROJECT = "tested.project.dir";








