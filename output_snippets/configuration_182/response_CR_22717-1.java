//<Beginning of snippet n. 1>

private static void loadPlatforms(String sdkOsPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
    createDirectory(new File(sdkOsPath, SdkConstants.FD_PLATFORMS), "Platforms");
    File[] platforms = new File(sdkOsPath, SdkConstants.FD_PLATFORMS).listFiles();
    // Additional logic for processing platforms can be added here if necessary
}

private static String[] getAbiList(String path) {
    ArrayList<String> list = new ArrayList<>();
    File imagesFolder = new File(path + File.separator + SdkConstants.OS_IMAGES_FOLDER);
    
    if (imagesFolder.exists() && imagesFolder.isDirectory()) {
        File[] files = imagesFolder.listFiles();
        // Additional logic to populate the list can be added here if necessary
    }
    
    return list.toArray(new String[0]);
}

private static void loadAddOns(String osSdkPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
    createDirectory(new File(osSdkPath, SdkConstants.FD_ADDONS), "Add-ons");
    File[] addons = new File(osSdkPath, SdkConstants.FD_ADDONS).listFiles();
    // Additional logic for processing addons can be added here if necessary
}

private static void createDirectory(File directory, String context) {
    if (!directory.exists()) {
        if (!directory.mkdirs()) {
            throw new IllegalArgumentException(String.format("%s could not be created during %s loading.", directory.getAbsolutePath(), context));
        }
    } else if (!directory.isDirectory()) {
        throw new IllegalArgumentException(String.format("%s is not a folder during %s loading.", directory.getAbsolutePath(), context));
    }
}

//<End of snippet n. 1>