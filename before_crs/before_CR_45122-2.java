/*Make libcore look in /data/misc for tzdata updates.

Bug: 7012465
Change-Id:Ia452f9c4482d0a75073e6a174f38114525984f15*/
//Synthetic comment -- diff --git a/luni/src/main/java/libcore/util/ZoneInfoDB.java b/luni/src/main/java/libcore/util/ZoneInfoDB.java
//Synthetic comment -- index d27231e..4e35f93 100644

//Synthetic comment -- @@ -104,11 +104,21 @@
}

private static MemoryMappedFile mapData() {
try {
            String path = System.getenv("ANDROID_ROOT") + "/usr/share/zoneinfo/tzdata";
            return MemoryMappedFile.mmapRO(path);
} catch (ErrnoException errnoException) {
            throw new AssertionError(errnoException);
}
}








