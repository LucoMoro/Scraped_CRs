/*update CookieManagerTest.java*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/CookieManagerTest.java b/tests/tests/webkit/src/android/webkit/cts/CookieManagerTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index fea44ef..206afb3

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.view.animation.cts.DelayedCheck;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.Date;
//Synthetic comment -- @@ -49,6 +50,9 @@
protected void setUp() throws Exception {
super.setUp();
mWebView = getActivity().getWebView();
        
        // Set a web chrome client in order to receive progress updates.
        mWebView.setWebChromeClient(new WebChromeClient());

mCookieManager = CookieManager.getInstance();
assertNotNull(mCookieManager);







