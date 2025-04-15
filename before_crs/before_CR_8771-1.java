/*Code to reuse PlatformAddress objects. Minimizing garbage creation on the framework level
and greatly reducing the number of runtime GC-hiccups for OpenGL apps and animations.
1st amend:
- Applied all requested style changes.
- Added synchronized clause to method.
- Added // BEGIN/END android-changed and -added remarks.
- No change to logic compared to initial version.*/
//Synthetic comment -- diff --git a/libcore/luni/src/main/java/org/apache/harmony/luni/platform/PlatformAddressFactory.java b/libcore/luni/src/main/java/org/apache/harmony/luni/platform/PlatformAddressFactory.java
old mode 100644
new mode 100755
//Synthetic comment -- index 5c31980..5f61fcc

//Synthetic comment -- @@ -24,15 +24,60 @@

public class PlatformAddressFactory {

    public static PlatformAddress on(int value) {
return PlatformAddressFactory.on(value, PlatformAddress.UNKNOWN);
}

public static PlatformAddress on(int value, long size) {
        PlatformAddress addr = (value == 0) ? PlatformAddress.NULL : new PlatformAddress(value, size);
return addr;
}

public static MappedPlatformAddress mapOn(int value, long size) {
MappedPlatformAddress addr = new MappedPlatformAddress(value, size);
return addr;







