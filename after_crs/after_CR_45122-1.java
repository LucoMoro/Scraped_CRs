/*Make libcore look in /data/misc for tzdata updates.

Bug: 7012465
Change-Id:Ia452f9c4482d0a75073e6a174f38114525984f15*/




//Synthetic comment -- diff --git a/luni/src/main/java/libcore/util/ZoneInfoDB.java b/luni/src/main/java/libcore/util/ZoneInfoDB.java
//Synthetic comment -- index d27231e..67e8c0c 100644

//Synthetic comment -- @@ -104,11 +104,21 @@
}

private static MemoryMappedFile mapData() {
        MemoryMappedFile result = mapData("/data/misc/zoneinfo/");
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








