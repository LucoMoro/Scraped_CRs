/*Code to reuse PlatformAddress objects. Minimizing garbage creation on the framework level and reducing the number of runtime GC-hiccups for OpenGL apps and animations.
2nd version: complete rewrite using 8 bit hash, up to 5 probes, cycle through probes cache replacement. (Thank you MichaelDt for your help.)*/




//Synthetic comment -- diff --git a/libcore/luni/src/main/java/org/apache/harmony/luni/platform/PlatformAddressFactory.java b/libcore/luni/src/main/java/org/apache/harmony/luni/platform/PlatformAddressFactory.java
//Synthetic comment -- index 5c31980..96c1a66 100644

//Synthetic comment -- @@ -24,14 +24,47 @@

public class PlatformAddressFactory {

    // BEGIN android-added
    final static int CACHESIZE = 0x100;   // values: 0x100, 0x200, ...
    final static int MAXPROBES = 5;       // values: 0 to 7
    private static PlatformAddress[] cache = new PlatformAddress[CACHESIZE];
    private static int replIdx = 0;
    // END android-added

public static PlatformAddress on(int value) {
return PlatformAddressFactory.on(value, PlatformAddress.UNKNOWN);
}

    // BEGIN android-changed
    /*
     * This will create a new PlatformAddress object -or- if available
     * reuse a previous PlatformAddress object with identical value+size
     * This will minimize garbage creation on the framework level and
     * greatly reduce the number of GC-hiccups in OpenGL animations.
     */
    public synchronized static PlatformAddress on(int value, long size) {
        if(value == 0) {
            return PlatformAddress.NULL;
        }
        int idx = value >> 5;
        int probe = 0;
        do
        {
            PlatformAddress cachedObj = cache[(idx + probe) & (CACHESIZE - 1)];
            if (cachedObj == null) {
                return cache[(idx + probe) & (CACHESIZE - 1)] = 
                    new PlatformAddress(value, size);
            }
            if (cachedObj.osaddr == value && cachedObj.size == size) {
                return cachedObj;
            }
        }
        while (probe++ < MAXPROBES);
        replIdx %= MAXPROBES + 1;
        return cache[(idx + replIdx++) & (CACHESIZE - 1)] = 
            new PlatformAddress(value, size);
}
    // END android-changed

public static MappedPlatformAddress mapOn(int value, long size) {
MappedPlatformAddress addr = new MappedPlatformAddress(value, size);







