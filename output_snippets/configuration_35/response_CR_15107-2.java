//<Beginning of snippet n. 0>

ProjectConfig badMatch = null;
Map<String, String> previousExportedAbis = fetchPreviousExportedAbis(); // Assume this method fetches previous exported ABIs
Map<String, String> currentExportedAbis = getCurrentExportedAbis(); // Assume this method retrieves current exported ABIs

if (!previousExportedAbis.equals(currentExportedAbis)) {
    mVersionCode++; // Increment version code upon ABI changes
    
    StringBuilder changedProperties = new StringBuilder();
    for (String abi : currentExportedAbis.keySet()) {
        if (!previousExportedAbis.containsKey(abi)) {
            changedProperties.append("Added ABI: ").append(abi).append("\n");
        }
    }
    for (String abi : previousExportedAbis.keySet()) {
        if (!currentExportedAbis.containsKey(abi)) {
            changedProperties.append("Removed ABI: ").append(abi).append("\n");
        }
    }

    if (changedProperties.length() > 0) {
        throw new ExportException("ABI configuration has changed:\n" + changedProperties.toString() + 
            "Incremented versionCode from %1$d to %2$d.", 
            mVersionCode - 1, mVersionCode);
    }
}

if (found.compareToProperties(map) == false) {
    badMatch = found;
}

if (badMatch != null) {
    throw new ExportException(
        "Config for project %1$s has changed from previous export with versionCode %2$d.\n" +
        "Any change in the multi-apk configuration requires an increment of the versionCode in export.properties.",
        badMatch.getRelativePath(), mVersionCode);
} else if (projectsToCheck.size() > 0) {
    throw new ExportException(
        "Project %1$s was not part of the previous export with versionCode %2$d.\n" + 
        "Consider this inconsistency when checking project configurations.",
        projectsToCheck.get(0).getRelativePath(), mVersionCode);
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

    return projectFolder;
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
    if (tmp != null) {
        if (!mLocaleFilters.equals(ApkSettings.readLocaleFilters(tmp))) {
            return false;
        }
    }

    return true;
}

//<End of snippet n. 1>