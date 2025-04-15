/*Set ChromeClient to webview to gurantee the getprogress() value*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewClientTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewClientTest.java
//Synthetic comment -- index 49d68c6..0de14e7 100644

//Synthetic comment -- @@ -31,7 +31,8 @@
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@TestTargetClass(android.webkit.WebViewClient.class)
public class WebViewClientTest extends ActivityInstrumentationTestCase2<WebViewStubActivity> {
private static final long TEST_TIMEOUT = 5000;
//Synthetic comment -- @@ -47,6 +48,7 @@
protected void setUp() throws Exception {
super.setUp();
mWebView = getActivity().getWebView();
}

@Override







