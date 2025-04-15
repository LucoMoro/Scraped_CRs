/*Some Assorted WebKit Test Fixes

Bug 2888212

The tests call a "loadUrl" method that waits until getProgress
returns 100. However, it appears that the updates returned by
getProgress are not accurrate unless there is a WebChromeClient
set on the WebView as seen in the code of
CallbackProxy#onProgressChanged. Setting the client should
fix the flakiness of the tests.

Call setInitialScale in WebSettingsTest so that the testFindNext
test will have to scroll to find the next instance of the word
"all" since the default scaling in QVGA will zoom out to show
two cases of "all" which won't require scrolling.

Change-Id:I3e313794e11f303b26ebd6dc1cde2720774ab439*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/HttpAuthHandlerTest.java b/tests/tests/webkit/src/android/webkit/cts/HttpAuthHandlerTest.java
//Synthetic comment -- index 029477d..e51f574 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.test.ActivityInstrumentationTestCase2;
import android.view.animation.cts.DelayedCheck;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//Synthetic comment -- @@ -45,6 +46,9 @@
protected void setUp() throws Exception {
super.setUp();
mWebView = getActivity().getWebView();
}

@Override








//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java b/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java
//Synthetic comment -- index 16c3e04..de80713 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.util.Log;
import android.view.animation.cts.DelayedCheck;
import android.webkit.MimeTypeMap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
//Synthetic comment -- @@ -57,6 +58,10 @@
protected void setUp() throws Exception {
super.setUp();
mWebView = getActivity().getWebView();
mSettings = mWebView.getSettings();
}









//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java
//Synthetic comment -- index 1e6625e..554acd8 100644

//Synthetic comment -- @@ -936,6 +936,9 @@
)
})
public void testFindNext() throws Throwable {
DisplayMetrics metrics = mWebView.getContext().getResources().getDisplayMetrics();
int dimension = Math.max(metrics.widthPixels, metrics.heightPixels);
// create a paragraph high enough to take up the entire screen







