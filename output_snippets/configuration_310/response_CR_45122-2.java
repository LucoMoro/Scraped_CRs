//<Beginning of snippet n. 0>

private static MemoryMappedFile mapData() {
    String path = "/data/misc/tzdata";
    File tzdataFile = new File(path);

    if (!tzdataFile.exists()) {
        path = System.getenv("ANDROID_ROOT") + "/usr/share/zoneinfo/tzdata";
        tzdataFile = new File(path);
    }

    if (!tzdataFile.exists()) {
        throw new FileNotFoundException("Timezone data file not found at: " + path);
    }

    try {
        return MemoryMappedFile.mmapRO(path);
    } catch (ErrnoException errnoException) {
        throw new AssertionError(errnoException);
    } catch (IOException ioException) {
        throw new AssertionError("Error reading timezone data file: " + ioException.getMessage());
    }
}

//<End of snippet n. 0>