/*Only delete the cache files if cache is null

No need to call getImageCacheService() to get the instance to delete
image cache files if the cache instance is null.
It will renew an instance for cache and lead to the memory leak
if renew too many instances by calling getImageCacheService()
multi-times.

Change-Id:I3ff83f49e9e8a8bc211dc26c773f3ccefc974881Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/util/ThumbnailManager.java b/src/com/android/mms/util/ThumbnailManager.java
//Synthetic comment -- index f0ae7b5..edafd16 100644

//Synthetic comment -- @@ -180,8 +180,16 @@

// Delete the on-disk cache, but leave the in-memory cache intact
public synchronized void clearBackingStore() {
        getImageCacheService().clear();
        mImageCacheService = null;  // force a re-init the next time getImageCacheService requested
}

public void removeThumbnail(Uri uri) {







