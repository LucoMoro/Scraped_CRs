//<Beginning of snippet n. 0>
ProjectConfig badMatch = null;

Map<String, String> previousAbiMap = getPreviousAbiMap(); // Retrieve previous ABI map
Map<String, String> currentAbiMap = computeCurrentAbiMap(); // Current ABI map for comparison

if (!currentAbiMap.equals(previousAbiMap)) {
    mVersionCode++; // Increment version code on ABI change
    // Store current ABI state for future comparisons
    storeCurrentAbiMap(currentAbiMap);
    // Log the details of the ABI change
    logAbiChange(previousAbiMap, currentAbiMap);
}

if (!found.compareToProperties(map)) {
    badMatch = found;
}

if (badMatch != null) {
    throw new ExportException(
        "Config for project %1$s has changed from previous export with versionCode %2$d. ABI mismatch: old=%3$s, new=%4$s.",
        badMatch.getRelativePath(), mVersionCode, previousAbiMap, currentAbiMap);
} else if (!projectsToCheck.isEmpty()) {
    throw new ExportException(
        "Project %1$s was not part of the previous export with versionCode %2$d.",
        found.getRelativePath(), mVersionCode);
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

private final boolean mSplitByAbi;
private final boolean mSplitByDensity;
private final Map<String, String> mLocaleFilters;

static ProjectConfig create(File projectFolder, String relativePath, ManifestData manifestData) throws ExportException {
    mSplitByAbi = splitByAbi;
    mSplitByDensity = splitByDensity;
    mLocaleFilters = localeFilters;
}

public File getProjectFolder() {
    Map<String, String> softVariants = computeSoftVariantMap();
    List<ApkData> list = new ArrayList<>();

    if (mSplitByAbi) {
        List<String> abis = findAbis();
        if (!abis.isEmpty()) {
            for (String abi : abis) {
                list.add(new ApkData(this, abi, softVariants));
            }
        }
    }

    if (!onlyManifestData) {
        LogHelper.write(sb, PROP_ABI, mSplitByAbi);
        LogHelper.write(sb, PROP_DENSITY, Boolean.toString(mSplitByDensity));

        if (!mLocaleFilters.isEmpty()) {
            // Add locale filter logging if needed
        }
    }
}

boolean compareToProperties(Map<String, String> values) {
    String tmp;
    try {
        if (mMinSdkVersion != Integer.parseInt(values.get(PROP_API))) {
            return false;
        }

        tmp = values.get(PROP_GL);
        if (tmp != null && mGlEsVersion != Integer.decode(tmp)) {
            return false;
        }
    } catch (NumberFormatException e) {
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
        if (!supportsScreens.equals(mSupportsScreens)) {
            return false;
        }
    } else {
        return false;
    }

    tmp = values.get(PROP_LOCALEFILTERS);
    if (tmp != null && !mLocaleFilters.equals(ApkSettings.readLocaleFilters(tmp))) {
        return false;
    }

    return true;
}
//<End of snippet n. 1>