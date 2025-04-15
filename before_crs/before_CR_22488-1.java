/*Wait until WebViewCore initialization is completed

WebViewCore initialization is running on its own thread,
not on UI thread. So Instrumentation#waitForIdleSync() may not
wait for the completion of WebViewCore initialization.
If a key event is sent on WebView before the initialization of
WebViewCore completes, then WebViewClient will not receive the callback.

Change-Id:I1bb2c4bd4cc9a14cbd7712309e65ecb53be79ead*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewClientTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewClientTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 0de14e7..ce1d940

//Synthetic comment -- @@ -221,6 +221,8 @@
final MockWebViewClient webViewClient = new MockWebViewClient();
mWebView.setWebViewClient(webViewClient);

runTestOnUiThread(new Runnable() {
public void run() {
mWebView.requestFocus();







