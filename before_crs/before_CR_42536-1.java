/*Release the cache resource after used.

The cache resource was acuqired but never released.
It will invoke NPE when handle resume activity.

Change-Id:Ib9ea558804a8a4497611875d7e147f60b23b377cSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/gallery3d/data/ImageCacheService.java b/src/com/android/gallery3d/data/ImageCacheService.java
//Synthetic comment -- index 0e79313..ade45cc 100644

//Synthetic comment -- @@ -72,6 +72,8 @@
}
} catch (IOException ex) {
// ignore.
}
return false;
}
//Synthetic comment -- @@ -87,6 +89,8 @@
mCache.insert(cacheKey, buffer.array());
} catch (IOException ex) {
// ignore.
}
}
}







