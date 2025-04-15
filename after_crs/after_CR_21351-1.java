/*Add wait to testCacheFile.

I found that sometimes NullPointerException is given off in the situation where CacheManager.getCacheFile() is executed before CacheManager finishes the preparation.

To avoid this, I added wait for maximum of 5000ms so that preparation of CacheManager finishes completely.*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/CacheManagerTest.java b/tests/tests/webkit/src/android/webkit/cts/CacheManagerTest.java
//Synthetic comment -- index 3e9a073..5a62c9b 100644

//Synthetic comment -- @@ -32,6 +32,7 @@

@TestTargetClass(android.webkit.CacheManager.class)
public class CacheManagerTest extends ActivityInstrumentationTestCase2<WebViewStubActivity> {
    private static final long CACHEMANAGER_INIT_TIMEOUT = 5000l;
private static final long NETWORK_OPERATION_DELAY = 10000l;

private WebView mWebView;
//Synthetic comment -- @@ -96,6 +97,14 @@
mWebServer = new CtsTestServer(getActivity());
final String url = mWebServer.getAssetUrl(TestHtmlConstants.EMBEDDED_IMG_URL);

        // Wait for CacheManager#init() finish.
        new DelayedCheck(CACHEMANAGER_INIT_TIMEOUT) {
            @Override
            protected boolean check() {
                return CacheManager.getCacheFileBaseDir() != null;
            }
        }.run();

mWebView.clearCache(true);
new DelayedCheck(NETWORK_OPERATION_DELAY) {
@Override







