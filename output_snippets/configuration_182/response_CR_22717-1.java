//<Beginning of snippet n. 1>

private static void loadPlatforms(String sdkOsPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
    File platformFolder = new File(sdkOsPath, SdkConstants.FD_PLATFORMS);
    
    // Ensure the directory exists or is created
    if (!platformFolder.exists()) {
        if (platformFolder.mkdirs()) {
            log.verbose(String.format("Created directory: %s", platformFolder.getAbsolutePath()));
        } else {
            log.error(String.format("Failed to create directory: %s", platformFolder.getAbsolutePath()));
            throw new DirectoryCreationException(String.format("%s is missing and could not be created.", platformFolder.getAbsolutePath()));
        }
    }
    
    if (!platformFolder.isDirectory()) {
        log.error(String.format("%s is not a folder.", platformFolder.getAbsolutePath()));
        throw new DirectoryCreationException(String.format("%s is not a folder.", platformFolder.getAbsolutePath()));
    }

    File[] platforms = platformFolder.listFiles();
    // Additional logic to populate 'list' with platforms can be added here
}

private static void loadAddOns(String osSdkPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
    File addonFolder = new File(osSdkPath, SdkConstants.FD_ADDONS);
    
    // Ensure the directory exists or is created
    if (!addonFolder.exists()) {
        if (addonFolder.mkdirs()) {
            log.verbose(String.format("Created directory: %s", addonFolder.getAbsolutePath()));
        } else {
            log.error(String.format("Failed to create directory: %s", addonFolder.getAbsolutePath()));
            throw new DirectoryCreationException(String.format("%s is missing and could not be created.", addonFolder.getAbsolutePath()));
        }
    }
    
    if (!addonFolder.isDirectory()) {
        log.error(String.format("%s is not a folder.", addonFolder.getAbsolutePath()));
        throw new DirectoryCreationException(String.format("%s is not a folder.", addonFolder.getAbsolutePath()));
    }

    File[] addons = addonFolder.listFiles();
    // Additional logic to populate 'list' with add-ons can be added here
}

//<End of snippet n. 1>