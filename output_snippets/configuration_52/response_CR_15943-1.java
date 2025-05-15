//<Beginning of snippet n. 0>

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

CacheResult result = CacheManager.getCacheFile(url, null);
assertNotNull(result);

// Can not test saveCacheFile(), because the output stream is null and
// saveCacheFile() will throw a NullPointerException.  There is no

//<End of snippet n. 0>