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

    // BEGIN android-added
    private final static int MAX_CACHE_ELEMENTS = 160;
    private static PlatformAddress[] platformAddressRingCache = null;
    private static int oldestElement = 0;
    private static int nextElement = 0;
    private static int elementCount = 0;
    // END android-added

	  public static PlatformAddress on(int value) {
return PlatformAddressFactory.on(value, PlatformAddress.UNKNOWN);
}

    // BEGIN android-changed
public static PlatformAddress on(int value, long size) {
        PlatformAddress addr = (value == 0) ? PlatformAddress.NULL : newPlatformAddress(value, size);
return addr;
}

    /*
     * This will create a new PlatformAddress object -or- if available
     * reuse a previous PlatformAddress object with identical value+size
     * This will minimize garbage creation on the framework level and
     * greatly reduce the number of GC-hiccups in OpenGL animations.
     */
    private synchronized static PlatformAddress newPlatformAddress(int value, long size) {
        if (platformAddressRingCache == null)
            platformAddressRingCache = new PlatformAddress[MAX_CACHE_ELEMENTS];

        // search platformAddressRingCache for PlatformAddress entry with same value+size
        for (int i=0; i<elementCount; i++) {
            int idx = oldestElement+i;
            if (idx >= MAX_CACHE_ELEMENTS) {
                idx -= MAX_CACHE_ELEMENTS;
            }
            if (platformAddressRingCache[idx].osaddr == value 
                  && platformAddressRingCache[idx].size == size) {
                return platformAddressRingCache[idx];
            }
        }

        PlatformAddress newObj = new PlatformAddress(value, size);
        if (elementCount < MAX_CACHE_ELEMENTS) {
            elementCount++;
        } else if (++oldestElement == MAX_CACHE_ELEMENTS) {
            oldestElement = 0;
        }
        platformAddressRingCache[nextElement] = newObj;
        if (++nextElement == MAX_CACHE_ELEMENTS) {
            nextElement = 0;
        }
        return newObj;
    }
    // END android-changed

public static MappedPlatformAddress mapOn(int value, long size) {
MappedPlatformAddress addr = new MappedPlatformAddress(value, size);
return addr;







