//<Beginning of snippet n. 0>

ProjectConfig badMatch = null;

map.put(prop.substring(0, equalPos), prop.substring(equalPos + 1));

Map<String, List<String>> previousAndCurrentAbis = compareToProperties(map);
if (previousAndCurrentAbis != null) {
    badMatch = found;
    mVersionCode++; // Increment version code due to configuration change
}

if (badMatch != null) {
    throw new ExportException(
        String.format("Config for project %1$s has changed from previous export with versionCode %2$d. " +
        "Previous exported ABIs: %3$s, Current ABIs: %4$s.\n" +
        "Any change in the multi-apk configuration requires an increment of the versionCode in export.properties.",
        badMatch.getRelativePath(), mVersionCode, previousAndCurrentAbis.get("previous"), previousAndCurrentAbis.get("current")));
} else if (!projectsToCheck.isEmpty()) {
    throw new ExportException(
        String.format("Project %1$s was not part of the previous export with versionCode %2$d.\n",
        found.getRelativePath(), mVersionCode));
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

static ProjectConfig create(File projectFolder, String relativePath,
ManifestData manifestData) throws ExportException {
    mSplitByAbi = splitByAbi;
    mSplitByDensity = splitByDensity;
    mLocaleFilters = localeFilters;
}

public File getProjectFolder() {
    Map<String, String> softVariants = computeSoftVariantMap();

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
            LogHelper.write(sb, PROP_LOCALEFILTERS, mLocaleFilters.toString());
        }
    }
}

Map<String, List<String>> compareToProperties(Map<String, String> values) {
    String tmp;

    List<String> previousAbis = getPreviousAbis(this); // Get previously exported ABIs
    List<String> currentAbis = getCurrentAbis(this); // Get current ABIs

    try {
        if (mMinSdkVersion != Integer.parseInt(values.get(PROP_API))) {
            return null;
        }

        tmp = values.get(PROP_GL);
        if (tmp != null && mGlEsVersion != Integer.decode(tmp)) {
            return null;
        }
    } catch (NumberFormatException e) {
        return null;
    }

    tmp = values.get(PROP_DENSITY);
    if (tmp == null || mSplitByDensity != Boolean.valueOf(tmp)) {
        return null;
    }

    tmp = values.get(PROP_ABI);
    if (tmp == null || mSplitByAbi != Boolean.valueOf(tmp) || !previousAbis.equals(currentAbis)) {
        return null;
    }

    tmp = values.get(PROP_SCREENS);
    if (tmp != null) {
        SupportsScreens supportsScreens = new SupportsScreens(tmp);
        if (!supportsScreens.equals(mSupportsScreens)) {
            return null;
        }
    } else {
        return null;
    }

    tmp = values.get(PROP_LOCALEFILTERS);
    if (tmp != null && !mLocaleFilters.equals(ApkSettings.readLocaleFilters(tmp))) {
        return null;
    }

    Map<String, List<String>> abisMap = new HashMap<>();
    abisMap.put("previous", previousAbis);
    abisMap.put("current", currentAbis);
    return abisMap;
}

//<End of snippet n. 1>