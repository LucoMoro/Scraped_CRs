/*Wait Properly in CacheManagerTest#testCacheFile

Bug 2814482

This test properly waited for the cache to be cleared, but it didn't
wait to check the cache after loading the url which is an asynchronous
operation.

Change-Id:I4a36ab04a1695a60fc31ff6cdc891517b48768e6*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/CacheManagerTest.java b/tests/tests/webkit/src/android/webkit/cts/CacheManagerTest.java
//Synthetic comment -- index bf66bad..3e9a073 100644

//Synthetic comment -- @@ -100,14 +100,19 @@
new DelayedCheck(NETWORK_OPERATION_DELAY) {
@Override
protected boolean check() {
                CacheResult result = CacheManager.getCacheFile(url, null);
return result == null;
}
}.run();

loadUrl(url);
        new DelayedCheck(NETWORK_OPERATION_DELAY) {
            @Override
            protected boolean check() {
                CacheResult result = CacheManager.getCacheFile(url, null);
                return result != null;
            }
        }.run();

// Can not test saveCacheFile(), because the output stream is null and
// saveCacheFile() will throw a NullPointerException.  There is no







