/*Using proper key for removing object from the map.

Stored value was used for map removal instead of key. The error was
silently ignore, because remove() method accepts Object type argument
and siletly does nothing when no value identified by such key is
found. Now proper key is used for removal. i.e. the same as for lookup.

Change-Id:I3a61fc219385cd0e7bcd4a33cd6ca23be220efe3*/
//Synthetic comment -- diff --git a/core/java/android/net/http/CertificateValidatorCache.java b/core/java/android/net/http/CertificateValidatorCache.java
//Synthetic comment -- index 47661d5..a89f75c 100644

//Synthetic comment -- @@ -137,8 +137,8 @@

if (domain != null && domain.length() != 0) {
if (secureHash != null && secureHash.length != 0) {
                CacheEntry cacheEntry = (CacheEntry)mCacheMap.get(
                    new Integer(mBigScrew ^ domain.hashCode()));
if (cacheEntry != null) {
if (!cacheEntry.expired()) {
rval = cacheEntry.has(domain, secureHash);
//Synthetic comment -- @@ -148,7 +148,7 @@
}
// TODO: debug only!
} else {
                        mCacheMap.remove(cacheEntry);
}
}
}







