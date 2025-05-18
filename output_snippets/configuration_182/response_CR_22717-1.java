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

private static DirectoryLoadStatus loadDirectory(String basePath, String dirName, ArrayList<IAndroidTarget> list, ISdkLog log) {
    validatePath(basePath, dirName);
    File directory = new File(basePath, dirName);
    if (!directory.mkdirs()) {
        log.error("Failed to create directory: " + directory.getAbsolutePath());
        throw new DirectoryCreationException("Could not create directory due to permission issues or other errors: " + directory.getAbsolutePath());
    }

    File[] files = directory.listFiles();
    int loadedItemsCount = 0;
    if (files != null) {
        // Assuming that the list is filled from files here.
        loadedItemsCount = files.length;
    }
    
    return new DirectoryLoadStatus(true, loadedItemsCount);
}

private static void validatePath(String basePath, String dirName) {
    if (basePath == null || dirName == null || basePath.length() > 255 || dirName.length() > 255 || basePath.contains("..") || dirName.contains("..")) {
        throw new IllegalArgumentException("Invalid path: basePath and dirName must not be null or exceed length limits, and must not contain forbidden characters.");
    }
}

class DirectoryCreationException extends RuntimeException {
    public DirectoryCreationException(String message) {
        super(message);
    }
}

class DirectoryLoadStatus {
    private final boolean success;
    private final int itemCount;

    public DirectoryLoadStatus(boolean success, int itemCount) {
        this.success = success;
        this.itemCount = itemCount;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getItemCount() {
        return itemCount;
    }
}
//<End of snippet n. 1>