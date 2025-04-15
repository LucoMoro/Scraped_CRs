/*Modify YuvImageTest.java/ CookieManagerTest.java*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/YuvImageTest.java b/tests/tests/graphics/src/android/graphics/cts/YuvImageTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index ccacc62..6613e92

//Synthetic comment -- @@ -245,9 +245,13 @@
Bitmap expected = null;
Bitmap actual = null;
boolean sameRect = rect1.equals(rect2) ? true : false;
        expected = Bitmap.createBitmap(testBitmap, rect1.left, rect1.top,
                rect1.width(), rect1.height());
        actual = compressDecompress(image, rect2);
compareBitmaps(expected, actual, mMseMargin, sameRect);
}









//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/CookieManagerTest.java b/tests/tests/webkit/src/android/webkit/cts/CookieManagerTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index fea44ef..c27449f

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







