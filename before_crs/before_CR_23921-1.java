/*CTS : modify "loadURL" method that wait until getProgress returns 100.

CTS : modify "loadURL" method that wait until getProgress returns 100.

- It appears that the updates returned by getProgress are not accurate
unless there is a WebChromeClient set on the WebView as seen in the code
of CallbackProxy#onProgressChanged.

refer to :https://review.source.android.com//#change,22046Change-Id:I21991f70b40d917dcf3d785269f600965d603550*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/CacheManager_CacheResultTest.java b/tests/tests/webkit/src/android/webkit/cts/CacheManager_CacheResultTest.java
//Synthetic comment -- index 1a18a86..48a6a1f 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.test.ActivityInstrumentationTestCase2;
import android.view.animation.cts.DelayedCheck;
import android.webkit.CacheManager;
import android.webkit.WebView;
import android.webkit.CacheManager.CacheResult;

//Synthetic comment -- @@ -49,6 +50,7 @@
protected void setUp() throws Exception {
super.setUp();
mWebView = getActivity().getWebView();
}

@Override







