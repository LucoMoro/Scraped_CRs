//<Beginning of snippet n. 0>
private static MemoryMappedFile mapData() {
    String path = "/data/misc/tzdata";
    File tzDataDir = new File(path);
    
    if (!tzDataDir.exists()) {
        throw new IllegalStateException("Directory not found: " + path);
    }
    if (!tzDataDir.canRead()) {
        throw new IllegalStateException("Permission denied for directory: " + path);
    }

    try {
        return MemoryMappedFile.mmapRO(path);
    } catch (ErrnoException errnoException) {
        throw new IllegalStateException("Failed to map tzdata at path: " + path, errnoException);
    } catch (Exception e) {
        throw new IllegalStateException("Unexpected error while mapping tzdata: " + e.getMessage(), e);
    }
}
//<End of snippet n. 0>