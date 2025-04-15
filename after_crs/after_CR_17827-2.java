/*Modify YuvImageTest.java/ CookieManagerTest.java

Change-Id:I37be498562a9a582b19d4fb7b38aebfc7f0c278a*/




//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/YuvImageTest.java b/tests/tests/graphics/src/android/graphics/cts/YuvImageTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index ccacc62..6613e92

//Synthetic comment -- @@ -245,9 +245,13 @@
Bitmap expected = null;
Bitmap actual = null;
boolean sameRect = rect1.equals(rect2) ? true : false;

		Rect actualRect = new Rect(rect2);
        actual = compressDecompress(image, actualRect);

        Rect expectedRect = sameRect ? actualRect : rect1;
        expected = Bitmap.createBitmap(testBitmap, expectedRect.left, expectedRect.top, expectedRect.width(), expectedRect.height());
        
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
import android.webkit.WebChromeClient; // LGE_WEB
import android.webkit.WebView;

import java.util.Date;
//Synthetic comment -- @@ -49,6 +50,9 @@
protected void setUp() throws Exception {
super.setUp();
mWebView = getActivity().getWebView();
        
        // LGE_WEB : Set a web chrome client in order to receive progress updates.
        mWebView.setWebChromeClient(new WebChromeClient());

mCookieManager = CookieManager.getInstance();
assertNotNull(mCookieManager);







