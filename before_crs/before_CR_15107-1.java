/*Detect conflict due to change in number/list of ABI being exported.

A change in ABI (number, or one replacing another) in the ABIs of a project
would trigger a change in the generated build info, so we force
increment of versionCode in this case.

Also now display the actual project property that changed and is
causing a conflict for better troubleshooting.

Change-Id:Ibb654022f1bed0e81fa0c6e649dc69dd98f2f9e0*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
//Synthetic comment -- index 697ce8b..f365886 100644

//Synthetic comment -- @@ -506,6 +506,7 @@

// store the project that doesn't match.
ProjectConfig badMatch = null;

// recorded whether we checked the version code. this is for when we compare
// a project config
//Synthetic comment -- @@ -584,7 +585,8 @@
map.put(prop.substring(0, equalPos), prop.substring(equalPos + 1));
}

                        if (found.compareToProperties(map) == false) {
// bad project config, record the project
badMatch = found;

//Synthetic comment -- @@ -602,9 +604,10 @@

if (badMatch != null) {
throw new ExportException(
                        "Config for project %1$s has changed from previous export with versionCode %2$d.\n" +
"Any change in the multi-apk configuration requires an increment of the versionCode in export.properties.",
                        badMatch.getRelativePath(), mVersionCode);
} else if (projectsToCheck.size() > 0) {
throw new ExportException(
"Project %1$s was not part of the previous export with versionCode %2$d.\n" +








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ProjectConfig.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ProjectConfig.java
//Synthetic comment -- index 8b7d653..280b6f8 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -68,6 +69,9 @@
private final boolean mSplitByAbi;
private final boolean mSplitByDensity;
private final Map<String, String> mLocaleFilters;

static ProjectConfig create(File projectFolder, String relativePath,
ManifestData manifestData) throws ExportException {
//Synthetic comment -- @@ -104,6 +108,11 @@
mSplitByAbi = splitByAbi;
mSplitByDensity = splitByDensity;
mLocaleFilters = localeFilters;
}

public File getProjectFolder() {
//Synthetic comment -- @@ -129,9 +138,8 @@
Map<String, String> softVariants = computeSoftVariantMap();

if (mSplitByAbi) {
            List<String> abis = findAbis();
            if (abis.size() > 0) {
                for (String abi : abis) {
list.add(new ApkData(this, abi, softVariants));
}
} else {
//Synthetic comment -- @@ -234,7 +242,22 @@
}

if (onlyManifestData == false) {
            LogHelper.write(sb, PROP_ABI, mSplitByAbi);
LogHelper.write(sb, PROP_DENSITY, Boolean.toString(mSplitByDensity));

if (mLocaleFilters.size() > 0) {
//Synthetic comment -- @@ -249,57 +272,77 @@
* Compares the current project config to a list of properties.
* These properties are in the format output by {@link #getConfigString()}.
* @param values the properties to compare to.
     * @return true if the properties exactly match the current config.
*/
    boolean compareToProperties(Map<String, String> values) {
String tmp;
// Note that most properties must always be present in the map.
try {
// api must always be there
if (mMinSdkVersion != Integer.parseInt(values.get(PROP_API))) {
                return false;
}

tmp = values.get(PROP_GL); // GL is optional in the config string.
if (tmp != null) {
if (mGlEsVersion != Integer.decode(tmp)) {
                    return false;
}
}
} catch (NumberFormatException e) {
// failed to convert an integer? consider the configs not equal.
            return false;
}

tmp = values.get(PROP_DENSITY);
if (tmp == null || mSplitByDensity != Boolean.valueOf(tmp)) {
            return false;
}

tmp = values.get(PROP_ABI);
        if (tmp == null || mSplitByAbi != Boolean.valueOf(tmp)) {
            return false;
}

tmp = values.get(PROP_SCREENS);
if (tmp != null) {
SupportsScreens supportsScreens = new SupportsScreens(tmp);
if (supportsScreens.equals(mSupportsScreens) == false) {
                return false;
}
} else {
            return false;
}

tmp = values.get(PROP_LOCALEFILTERS);
if (tmp != null) {
if (mLocaleFilters.equals(ApkSettings.readLocaleFilters(tmp)) == false) {
                return false;
}
} else {
// do nothing. locale filter is optional in the config string.
}

        return true;
}
}







