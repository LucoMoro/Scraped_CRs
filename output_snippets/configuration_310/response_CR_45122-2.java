//<Beginning of snippet n. 0>
private static MemoryMappedFile mapData() {
    String path = "/data/misc/tzdata";
    File file = new File(path);
    
    if (!file.exists()) {
        throw new FileNotFoundException("File not found at: " + path);
    }
    
    try {
        return MemoryMappedFile.mmapRO(path);
    } catch (ErrnoException errnoException) {
        throw new IOException("Error mapping file at: " + path, errnoException);
    }
}
//<End of snippet n. 0>