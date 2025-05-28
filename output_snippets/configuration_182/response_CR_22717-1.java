//<Beginning of snippet n. 1>
private static int loadPlatforms(String sdkOsPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
    if (sdkOsPath == null || list == null || log == null) {
        throw new IllegalArgumentException("Invalid input: sdkOsPath, list, or log cannot be null.");
    }

    return loadDirectory(sdkOsPath, SdkConstants.FD_PLATFORMS, list, log);
}

private static int loadAddOns(String osSdkPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
    if (osSdkPath == null || list == null || log == null) {
        throw new IllegalArgumentException("Invalid input: osSdkPath, list, or log cannot be null.");
    }

    return loadDirectory(osSdkPath, SdkConstants.FD_ADDONS, list, log);
}

private static int loadDirectory(String basePath, String dirName, ArrayList<IAndroidTarget> list, ISdkLog log) {
    File directory = new File(basePath, dirName);
    if (!directory.mkdirs() && !directory.isDirectory()) {
        log.error("Failed to create directory: " + directory.getAbsolutePath());
        throw new DirectoryCreationException("Could not create directory: " + directory.getAbsolutePath());
    }

    File[] files = directory.listFiles();
    if (files != null) {
        // Assuming that the list is filled from files here.
        return files.length;
    }
    return 0;
}

class DirectoryCreationException extends RuntimeException {
    public DirectoryCreationException(String message) {
        super(message);
    }
}
//<End of snippet n. 1>