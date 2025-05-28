//<Beginning of snippet n. 0>
}

private static MemoryMappedFile mapData() {
    try {
        String path = System.getenv("ANDROID_ROOT") + "/data/misc/tzdata";
        return MemoryMappedFile.mmapRO(path);
    } catch (ErrnoException errnoException) {
        throw new AssertionError("Failed to map tzdata at path: " + path, errnoException);
    } catch (Exception e) {
        throw new AssertionError("Unexpected error while mapping tzdata: " + e.getMessage(), e);
    }
}
//<End of snippet n. 0>