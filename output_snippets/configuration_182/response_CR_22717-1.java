//<Beginning of snippet n. 1>

private static void createDirectoryIfNotExists(File directory) {
    if (!directory.exists()) {
        if (!directory.mkdirs()) {
            throw new IllegalStateException("Failed to create directory: " + directory.getAbsolutePath());
        }
    }
}

/**
* Loads the Platforms from the SDK.
* @param sdkOsPath Location of the SDK
* @param list the list to fill with the platforms.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
*/
private static void loadPlatforms(String sdkOsPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
    File platformFolder = new File(sdkOsPath, SdkConstants.FD_PLATFORMS);
    createDirectoryIfNotExists(platformFolder);
    
    if (platformFolder.isDirectory()) {
        File[] platforms  = platformFolder.listFiles();
        
        return;
    }

    String message = null;
    if (!platformFolder.exists()) {
        message = "%s is missing.";
    } else {
        message = "%s is not a folder.";
    }

    throw new IllegalArgumentException(String.format(message, platformFolder.getAbsolutePath()));
}

/**
* @return an array of strings containing all the abi names for the target
*/
private static String[] getAbiList(String path) {
    ArrayList list = new ArrayList();

    File imagesFolder = new File(path + File.separator + SdkConstants.OS_IMAGES_FOLDER);
    File[] files = imagesFolder.listFiles();

    // Assuming further processing would be done here...

    return (String[]) list.toArray(new String[0]);
}

/**
* Loads the Add-on from the SDK.
* @param osSdkPath Location of the SDK
* @param list the list to fill with the add-ons.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
*/
private static void loadAddOns(String osSdkPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
    File addonFolder = new File(osSdkPath, SdkConstants.FD_ADDONS);
    createDirectoryIfNotExists(addonFolder);

    if (addonFolder.isDirectory()) {
        File[] addons  = addonFolder.listFiles();

        return;
    }

    String message = null;
    if (!addonFolder.exists()) {
        message = "%s is missing.";
    } else {
        message = "%s is not a folder.";
    }

    throw new IllegalArgumentException(String.format(message, addonFolder.getAbsolutePath()));
}

//<End of snippet n. 1>