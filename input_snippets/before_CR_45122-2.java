
//<Beginning of snippet n. 0>


}

private static MemoryMappedFile mapData() {
try {
            String path = System.getenv("ANDROID_ROOT") + "/usr/share/zoneinfo/tzdata";
            return MemoryMappedFile.mmapRO(path);
} catch (ErrnoException errnoException) {
            throw new AssertionError(errnoException);
}
}


//<End of snippet n. 0>








