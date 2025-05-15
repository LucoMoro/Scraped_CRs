<<Beginning of snippet n. 0>>
private static MemoryMappedFile mapData() {
    try {
        String path = "/data/misc/tzdata";
        File directory = new File(path);
        
        if (!directory.exists() || !directory.canRead()) {
            throw new IllegalStateException("Directory does not exist or is not accessible: " + path);
        }

        return MemoryMappedFile.mmapRO(path);
    } catch (ErrnoException errnoException) {
        throw new RuntimeException("Error during memory mapping: " + errnoException.getMessage(), errnoException);
    } catch (Exception e) {
        throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
    }
}
<<End of snippet n. 0>>