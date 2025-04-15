/*Code to reuse PlatformAddress objects. Minimizing garbage creation on the framework level
and greatly reducing the number of runtime GC-hiccups for OpenGL apps and animations.*/
//Synthetic comment -- diff --git a/libcore/luni/src/main/java/org/apache/harmony/luni/platform/PlatformAddressFactory.java b/libcore/luni/src/main/java/org/apache/harmony/luni/platform/PlatformAddressFactory.java
//Synthetic comment -- index 5c31980..64b50ab 100644

//Synthetic comment -- @@ -29,10 +29,57 @@
}

public static PlatformAddress on(int value, long size) {
        PlatformAddress addr = (value == 0) ? PlatformAddress.NULL : new PlatformAddress(value, size);
return addr;
}

public static MappedPlatformAddress mapOn(int value, long size) {
MappedPlatformAddress addr = new MappedPlatformAddress(value, size);
return addr;







