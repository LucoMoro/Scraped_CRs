/*WebChromeClient needs to set for WebView to gurantee the getProgress() value.*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewClientTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewClientTest.java
//Synthetic comment -- index 49d68c6..836388c 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;

@TestTargetClass(android.webkit.WebViewClient.class)
public class WebViewClientTest extends ActivityInstrumentationTestCase2<WebViewStubActivity> {
//Synthetic comment -- @@ -190,6 +191,8 @@
public void testOnReceivedHttpAuthRequest() throws Exception {
final MockWebViewClient webViewClient = new MockWebViewClient();
mWebView.setWebViewClient(webViewClient);
        mWebView.setWebChromeClient(new WebChromeClient());

mWebServer = new CtsTestServer(getActivity());

assertFalse(webViewClient.hasOnReceivedHttpAuthRequestCalled());







