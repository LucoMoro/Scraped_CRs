//<Beginning of snippet n. 1>

private static void loadPlatforms(String sdkOsPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
    File platformFolder = new File(sdkOsPath, SdkConstants.FD_PLATFORMS);
    if (!createDirectoryIfMissing(platformFolder, log)) {
        log.warning(String.format("Could not create directory: %s", platformFolder.getAbsolutePath()));
        return;
    }
    File[] platforms = platformFolder.listFiles();
    if (platforms != null) {
        // Logic to populate the list with platforms
    } else {
        log.warning(String.format("%s is not a folder or cannot be read.", platformFolder.getAbsolutePath()));
    }
}

private static void loadAddOns(String osSdkPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
    File addonFolder = new File(osSdkPath, SdkConstants.FD_ADDONS);
    if (!createDirectoryIfMissing(addonFolder, log)) {
        log.warning(String.format("Could not create directory: %s", addonFolder.getAbsolutePath()));
        return;
    }
    File[] addons = addonFolder.listFiles();
    if (addons != null) {
        // Logic to populate the list with add-ons
    } else {
        log.warning(String.format("%s is not a folder or cannot be read.", addonFolder.getAbsolutePath()));
    }
}

private static boolean createDirectoryIfMissing(File directory, ISdkLog log) {
    if (directory.exists()) {
        log.warning(String.format("Using existing directory: %s", directory.getAbsolutePath()));
        return true;
    }
    if (!directory.mkdirs()) {
        return false;
    }
    if (!directory.isDirectory()) {
        log.warning(String.format("%s is not a directory.", directory.getAbsolutePath()));
        return false;
    }
    return true;
}

//<End of snippet n. 1>