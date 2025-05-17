//<Beginning of snippet n. 0>
loadUrl(url, new LoadUrlCallback() {
    @Override
    public void onComplete() {
        CacheResult result = CacheManager.getCacheFile(url, null);
        assertNotNull(result);
    }

    @Override
    public void onError(Exception e) {
        fail("Loading URL failed: " + e.getMessage());
    }
});
//<End of snippet n. 0>