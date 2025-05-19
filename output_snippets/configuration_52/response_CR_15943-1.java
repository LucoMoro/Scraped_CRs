//<Beginning of snippet n. 0>
loadUrl(url, new LoadUrlCallback() {
    @Override
    public void onLoadComplete() {
        new DelayedCheck(NETWORK_OPERATION_DELAY) {
            @Override
            protected boolean check() {
                CacheResult result = CacheManager.getCacheFile(url, null);
                return result != null && isCacheValid(result);
            }
        }.run();

        CacheResult result = CacheManager.getCacheFile(url, null);
        if (result != null && isCacheValid(result)) {
            assertNotNull(result);
            assertTrue(isCacheValid(result));
            OutputStream outputStream = getOutputStreamForCache();
            if (outputStream != null) {
                saveCacheFile(outputStream);
            } else {
                System.err.println("Output stream for cache is null.");
            }
        }
    }
});
//<End of snippet n. 0>