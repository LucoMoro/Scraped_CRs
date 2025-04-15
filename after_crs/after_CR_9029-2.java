/*Code to reuse PlatformAddress objects. Minimizing garbage creation on the framework level and reducing the number of runtime GC-hiccups for OpenGL apps and animations.
2nd version: complete rewrite using 8 bit hash, up to 5 probes, cycle through probes cache replacement. (Thank you MichaelDt for your help.)
- 1st amend: applied requested modifications*/




//Synthetic comment -- diff --git a/libcore/luni/src/main/java/org/apache/harmony/luni/platform/PlatformAddressFactory.java b/libcore/luni/src/main/java/org/apache/harmony/luni/platform/PlatformAddressFactory.java
//Synthetic comment -- index 5c31980..b6f7be0 100644

//Synthetic comment -- @@ -24,15 +24,64 @@

public class PlatformAddressFactory {

    // BEGIN android-added
    /*
     * Defines the number of PlatformAddress objects to be cached. Must be a
     * power of two. Caching PlatformAddress objects minimizes the creation
     * of garbage and reduces the number of GC-hiccups in OpenGL animations.
     */
    private final static int CACHE_SIZE = 1<<8;

    /*
     * A mask with all bits set, matching the size of the cache.
     */
    private final static int CACHE_MASK = CACHE_SIZE - 1;

    /*
     * Defines the maximum number of probes taken per hash, used for looking
     * up an empty cache slot or a previously stored PlatformAddress.
     */
    private final static int MAX_PROBES = 5;

    /*
     * A cycling index (0 to MAX_PROBES-1) used to replace elements in the cache.
     */
    private static int replacementIndex = 0;

    /*
     * Array of PlatformAddress references kept from garbage collection.
     */
    private static PlatformAddress[] cache = new PlatformAddress[CACHE_SIZE];
    // END android-added


    // BEGIN android-changed
    public synchronized static PlatformAddress on(int value, long size) {
        if (value == 0) {
            return PlatformAddress.NULL;
        }
        int idx = value >> 5;
        for (int probe = 0; probe < MAX_PROBES; probe++)
        {
            PlatformAddress cachedObj = cache[(idx + probe) & CACHE_MASK];
            if (cachedObj == null) {
                return cache[(idx + probe) & CACHE_MASK] =
                    new PlatformAddress(value, size);
            }
            if (cachedObj.osaddr == value && cachedObj.size == size) {
                return cachedObj;
            }
        }
        replacementIndex = (replacementIndex + 1) % MAX_PROBES;
        return cache[(idx + replacementIndex) & CACHE_MASK] =
            new PlatformAddress(value, size);
    }
    // END android-changed

public static PlatformAddress on(int value) {
return PlatformAddressFactory.on(value, PlatformAddress.UNKNOWN);
}

public static MappedPlatformAddress mapOn(int value, long size) {
MappedPlatformAddress addr = new MappedPlatformAddress(value, size);
return addr;







