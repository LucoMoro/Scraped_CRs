//<Beginning of snippet n. 1>

private static void loadPlatforms(String sdkOsPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
    File platformFolder = new File(sdkOsPath, SdkConstants.FD_PLATFORMS);
    createDirectoryIfMissing(platformFolder, log);
    File[] platforms = platformFolder.listFiles();
    if (platforms != null) {
        // Logic to populate the list with platforms
    } else {
        log.warning(String.format("%s is not a folder or cannot be read.", platformFolder.getAbsolutePath()));
    }
}

private static void loadAddOns(String osSdkPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
    File addonFolder = new File(osSdkPath, SdkConstants.FD_ADDONS);
    createDirectoryIfMissing(addonFolder, log);
    File[] addons = addonFolder.listFiles();
    if (addons != null) {
        // Logic to populate the list with add-ons
    } else {
        log.warning(String.format("%s is not a folder or cannot be read.", addonFolder.getAbsolutePath()));
    }
}

private static void createDirectoryIfMissing(File directory, ISdkLog log) {
    if (!directory.exists() && !directory.mkdirs()) {
        throw new RuntimeException(String.format("Failed to create directory: %s", directory.getAbsolutePath()));
    }
    if (!directory.isDirectory()) {
        throw new RuntimeException(String.format("%s is not a directory.", directory.getAbsolutePath()));
    }
    log.warning(String.format("Using existing directory: %s", directory.getAbsolutePath()));
}

//<End of snippet n. 1>