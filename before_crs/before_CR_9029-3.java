/*Code to reuse PlatformAddress objects. Minimizing garbage creation on the framework level and reducing the number of runtime GC-hiccups for OpenGL apps and animations.
2nd version: complete rewrite using 8 bit hash, up to 5 probes, cycle through probes cache replacement. (Thank you MichaelDt for your help.)
- 1st amend: applied requested modifications
- 2nd amend: comment openers, curly bracket*/
//Synthetic comment -- diff --git a/libcore/luni/src/main/java/org/apache/harmony/luni/platform/PlatformAddressFactory.java b/libcore/luni/src/main/java/org/apache/harmony/luni/platform/PlatformAddressFactory.java
//Synthetic comment -- index 5c31980..9ec0fcd 100644

//Synthetic comment -- @@ -24,15 +24,63 @@

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







