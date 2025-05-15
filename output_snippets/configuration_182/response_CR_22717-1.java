//<Beginning of snippet n. 1>

private static void loadPlatforms(String sdkOsPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
    File platformFolder = new File(sdkOsPath, SdkConstants.FD_PLATFORMS);
    if (!platformFolder.exists()) {
        if (platformFolder.mkdirs()) {
            log.verbose(String.format("Created directory: %s", platformFolder.getAbsolutePath()));
        } else {
            log.error(String.format("Failed to create directory: %s", platformFolder.getAbsolutePath()));
            return;
        }
    } else if (!platformFolder.isDirectory()) {
        log.error(String.format("%s is not a folder.", platformFolder.getAbsolutePath()));
        return;
    }

    File[] platforms = platformFolder.listFiles();
    // Continue processing platforms...
}

private static String[] getAbiList(String path) {
    ArrayList<String> list = new ArrayList<>();
    File imagesFolder = new File(path + File.separator + SdkConstants.OS_IMAGES_FOLDER);
    File[] files = imagesFolder.listFiles();
    // Continue processing files...
    return list.toArray(new String[0]);
}

private static void loadAddOns(String osSdkPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
    File addonFolder = new File(osSdkPath, SdkConstants.FD_ADDONS);
    if (!addonFolder.exists()) {
        if (addonFolder.mkdirs()) {
            log.verbose(String.format("Created directory: %s", addonFolder.getAbsolutePath()));
        } else {
            log.error(String.format("Failed to create directory: %s", addonFolder.getAbsolutePath()));
            return;
        }
    } else if (!addonFolder.isDirectory()) {
        log.error(String.format("%s is not a folder.", addonFolder.getAbsolutePath()));
        return;
    }

    File[] addons = addonFolder.listFiles();
    // Continue processing addons...
}

//<End of snippet n. 1>