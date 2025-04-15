/*Release the cache resource after used.

The cache resource was acuqired but never released.
It will invoke NPE when handle resume activity.

Change-Id:I7d084fbc4d9b76c45067041d7879cd2e7e7ce79cSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/util/ImageCacheService.java b/src/com/android/mms/util/ImageCacheService.java
//Synthetic comment -- index 9cd82f9..a97339c 100644

//Synthetic comment -- @@ -69,6 +69,8 @@
}
} catch (IOException ex) {
// ignore.
}
return null;
}
//Synthetic comment -- @@ -84,6 +86,8 @@
mCache.insert(cacheKey, buffer.array());
} catch (IOException ex) {
// ignore.
}
}
}







