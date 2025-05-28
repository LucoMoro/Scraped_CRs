//<Beginning of snippet n. 0>
private static MemoryMappedFile mapData() throws CustomException {
    String path = "/data/misc/tzdata";
    File tzDataDirectory = new File(path);
    
    if (!tzDataDirectory.exists()) {
        throw new CustomException("Timezone data directory does not exist: " + path);
    }
    
    try {
        MemoryMappedFile mmapFile = MemoryMappedFile.mmapRO(path);
        // Log success
        Logger.log("Successfully mapped timezone data.");
        return mmapFile;
    } catch (ErrnoException errnoException) {
        throw new CustomException("Error accessing timezone data: " + errnoException.getMessage());
    }
}
//<End of snippet n. 0>