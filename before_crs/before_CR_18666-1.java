/*update CookieManagerTest.java

Change-Id:I45ecadd4bd3ac592df0af7386c4cb4dc8dd34af5*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/CookieManagerTest.java b/tests/tests/webkit/src/android/webkit/cts/CookieManagerTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index fea44ef..ca1b76f

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.view.animation.cts.DelayedCheck;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import java.util.Date;
//Synthetic comment -- @@ -50,6 +51,9 @@
super.setUp();
mWebView = getActivity().getWebView();

mCookieManager = CookieManager.getInstance();
assertNotNull(mCookieManager);
}







