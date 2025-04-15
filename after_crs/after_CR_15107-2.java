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
            String errorMsg = null;

// recorded whether we checked the version code. this is for when we compare
// a project config
//Synthetic comment -- @@ -584,7 +585,8 @@
map.put(prop.substring(0, equalPos), prop.substring(equalPos + 1));
}

                        errorMsg = found.compareToProperties(map);
                        if (errorMsg != null) {
// bad project config, record the project
badMatch = found;

//Synthetic comment -- @@ -602,9 +604,10 @@

if (badMatch != null) {
throw new ExportException(
                        "Config for project %1$s has changed from previous export with versionCode %2$d:\n" +
                        "\t%3$s\n" +
"Any change in the multi-apk configuration requires an increment of the versionCode in export.properties.",
                        badMatch.getRelativePath(), mVersionCode, errorMsg);
} else if (projectsToCheck.size() > 0) {
throw new ExportException(
"Project %1$s was not part of the previous export with versionCode %2$d.\n" +








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ProjectConfig.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ProjectConfig.java
//Synthetic comment -- index 8b7d653..c25af2e 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -68,6 +69,9 @@
private final boolean mSplitByAbi;
private final boolean mSplitByDensity;
private final Map<String, String> mLocaleFilters;
    /** List of ABIs not defined in the properties but actually existing in the project as valid
     * .so files */
    private final List<String> mAbis;

static ProjectConfig create(File projectFolder, String relativePath,
ManifestData manifestData) throws ExportException {
//Synthetic comment -- @@ -104,6 +108,11 @@
mSplitByAbi = splitByAbi;
mSplitByDensity = splitByDensity;
mLocaleFilters = localeFilters;
        if (mSplitByAbi) {
            mAbis = findAbis();
        } else {
            mAbis = null;
        }
}

public File getProjectFolder() {
//Synthetic comment -- @@ -129,9 +138,8 @@
Map<String, String> softVariants = computeSoftVariantMap();

if (mSplitByAbi) {
            if (mAbis.size() > 0) {
                for (String abi : mAbis) {
list.add(new ApkData(this, abi, softVariants));
}
} else {
//Synthetic comment -- @@ -234,7 +242,22 @@
}

if (onlyManifestData == false) {
            if (mSplitByAbi) {
                // need to not only encode true, but also the list of ABIs that will be used when
                // the project is exported. This is because the hard property is not so much
                // whether an apk is generated per ABI, but *how many* of them (since they all take
                // a different build Info).
                StringBuilder value = new StringBuilder(Boolean.toString(true));
                for (String abi : mAbis) {
                    value.append('|').append(abi);
                }
                LogHelper.write(sb, PROP_ABI, value);
            } else {
                LogHelper.write(sb, PROP_ABI, false);
            }

            // in this case we're simply always going to make 3 versions (which may not make sense)
            // so the boolean is enough.
LogHelper.write(sb, PROP_DENSITY, Boolean.toString(mSplitByDensity));

if (mLocaleFilters.size() > 0) {
//Synthetic comment -- @@ -249,57 +272,79 @@
* Compares the current project config to a list of properties.
* These properties are in the format output by {@link #getConfigString()}.
* @param values the properties to compare to.
     * @return null if the properties exactly match the current config, an error message otherwise
*/
    String compareToProperties(Map<String, String> values) {
String tmp;
// Note that most properties must always be present in the map.
try {
// api must always be there
if (mMinSdkVersion != Integer.parseInt(values.get(PROP_API))) {
                return "Attribute minSdkVersion changed";
}
        } catch (NumberFormatException e) {
            // failed to convert an integer? consider the configs not equal.
            return "Failed to convert attribute minSdkVersion to an Integer";
        }

        try {
tmp = values.get(PROP_GL); // GL is optional in the config string.
if (tmp != null) {
if (mGlEsVersion != Integer.decode(tmp)) {
                    return "Attribute glEsVersion changed";
}
}
} catch (NumberFormatException e) {
// failed to convert an integer? consider the configs not equal.
            return "Failed to convert attribute glEsVersion to an Integer";
}

tmp = values.get(PROP_DENSITY);
if (tmp == null || mSplitByDensity != Boolean.valueOf(tmp)) {
            return "Property split.density changed or is missing from config file";
}

        // compare the ABI. If splitByAbi is true, then compares the ABIs present in the project
        // as they must match.
tmp = values.get(PROP_ABI);
        if (tmp == null) {
            return "Property split.abi is missing from config file";
        }
        String[] abis = tmp.split("\\|");
        System.out.println("ABIS: " + Arrays.toString(abis));
        System.out.println("current: " + mSplitByAbi);
        if (mSplitByAbi != Boolean.valueOf(abis[0])) { // first value is always the split boolean
            return "Property split.abi changed";
        }
        // now compare the rest.
        if (abis.length - 1 != mAbis.size()) {
            return "The number of ABIs available in the project changed";
        }
        for (int i = 1 ; i < abis.length ; i++) {
            if (mAbis.indexOf(abis[i]) == -1) {
                return "The list of ABIs available in the project changed";
            }
}

tmp = values.get(PROP_SCREENS);
if (tmp != null) {
SupportsScreens supportsScreens = new SupportsScreens(tmp);
if (supportsScreens.equals(mSupportsScreens) == false) {
                return "Supports-Screens value changed";
}
} else {
            return "Supports-screens value missing from config file";
}

tmp = values.get(PROP_LOCALEFILTERS);
if (tmp != null) {
if (mLocaleFilters.equals(ApkSettings.readLocaleFilters(tmp)) == false) {
                return "Locale resource filter changed";
}
} else {
// do nothing. locale filter is optional in the config string.
}

        return null;
}
}







