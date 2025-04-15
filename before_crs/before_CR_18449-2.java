/*update CookieManagerTest.java

Change-Id:Ie01e3e429633091bae713c0c029f4f14eca6d1d9*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/CookieManagerTest.java b/tests/tests/webkit/src/android/webkit/cts/CookieManagerTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index fea44ef..206afb3

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.view.animation.cts.DelayedCheck;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import java.util.Date;
//Synthetic comment -- @@ -49,6 +50,9 @@
protected void setUp() throws Exception {
super.setUp();
mWebView = getActivity().getWebView();

mCookieManager = CookieManager.getInstance();
assertNotNull(mCookieManager);







