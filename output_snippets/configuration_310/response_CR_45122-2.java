//<Beginning of snippet n. 0>
private static MemoryMappedFile mapData() {
    String path = "/data/misc/tzdata";
    File file = new File(path);
    
    if (!file.exists()) {
        throw new IOException("File not found: " + path);
    }
    
    try {
        return MemoryMappedFile.mmapRO(path);
    } catch (ErrnoException errnoException) {
        throw new IOException("Error accessing file: " + errnoException.getMessage(), errnoException);
    }
}
//<End of snippet n. 0>