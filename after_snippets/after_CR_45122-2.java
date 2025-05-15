
//<Beginning of snippet n. 0>


}

private static MemoryMappedFile mapData() {
        MemoryMappedFile result = mapData(System.getenv("ANDROID_DATA") + "/misc/zoneinfo/");
        if (result == null) {
            result = mapData(System.getenv("ANDROID_ROOT") + "/usr/share/zoneinfo/");
            if (result == null) {
                throw new AssertionError("Couldn't find any tzdata!");
            }
        }
        return result;
    }

    private static MemoryMappedFile mapData(String directory) {
try {
            return MemoryMappedFile.mmapRO(directory + "tzdata");
} catch (ErrnoException errnoException) {
            return null;
}
}


//<End of snippet n. 0>








