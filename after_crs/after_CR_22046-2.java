/*CTS : modify "loadURL" method that wait until getProgress returns 100.

- It appears that the updates returned by getProgress are not accurate unless there is a WebChromeClient set on the WebView as seen in the code of CallbackProxy#onProgressChanged.

Change-Id:Ibc5e87743f915490bc875303d933d013fe3b042b*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/CacheManager_CacheResultTest.java b/tests/tests/webkit/src/android/webkit/cts/CacheManager_CacheResultTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 1a18a86..48a6a1f

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.test.ActivityInstrumentationTestCase2;
import android.view.animation.cts.DelayedCheck;
import android.webkit.CacheManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.CacheManager.CacheResult;

//Synthetic comment -- @@ -49,6 +50,7 @@
protected void setUp() throws Exception {
super.setUp();
mWebView = getActivity().getWebView();
        mWebView.setWebChromeClient(new WebChromeClient());
}

@Override







