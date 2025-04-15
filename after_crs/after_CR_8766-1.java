/*Code to reuse PlatformAddress objects. Minimizing garbage creation on the framework level
and greatly reducing the number of runtime GC-hiccups for OpenGL apps and animations.*/




//Synthetic comment -- diff --git a/libcore/luni/src/main/java/org/apache/harmony/luni/platform/PlatformAddressFactory.java b/libcore/luni/src/main/java/org/apache/harmony/luni/platform/PlatformAddressFactory.java
old mode 100644
new mode 100755
//Synthetic comment -- index 5c31980..989ca06

//Synthetic comment -- @@ -29,10 +29,48 @@
}

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
    private final static int maxCacheElements = 160;
    private static PlatformAddress[] platformAddressRingCache = null;
    private static int oldestElement = 0;
    private static int nextElement = 0;
    private static int elementCount = 0;

    private static PlatformAddress newPlatformAddress(int value, long size)
    {
      if(platformAddressRingCache==null)
        platformAddressRingCache = new PlatformAddress[maxCacheElements];

      // search platformAddressRingCache for PlatformAddress entry with same value+size
      for(int i=0; i<elementCount; i++) {
        int idx = oldestElement+i;
        if(idx>=maxCacheElements)
          idx-=maxCacheElements;
        if(platformAddressRingCache[idx].osaddr==value && platformAddressRingCache[idx].size==size)
          return platformAddressRingCache[idx];
      }

      PlatformAddress newObj = new PlatformAddress(value, size);
      if(elementCount<maxCacheElements)
        elementCount++;
      else
      if(++oldestElement==maxCacheElements)
        oldestElement=0;
      platformAddressRingCache[nextElement]=newObj;
      if(++nextElement==maxCacheElements)
        nextElement=0;
      return newObj;
    }

public static MappedPlatformAddress mapOn(int value, long size) {
MappedPlatformAddress addr = new MappedPlatformAddress(value, size);
return addr;







