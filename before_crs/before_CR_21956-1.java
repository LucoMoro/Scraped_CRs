/*The accuracy of WebView scale is less than density scale's one

The scale returned by getScale() is calculated as below;
- scale = (int)(100 * density)
- scale = scale/100.0f
So the scale is reduced its accuracy less than second decimal.
Therefore assertion should has delta 0.01f.

Change-Id:Ic5511e66a75d6e5dbc7dccec392b4a7e1b98e59b*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index b9d054f..c6bbc62

//Synthetic comment -- @@ -1458,13 +1458,13 @@
waitForLoadComplete(mWebView, TEST_TIMEOUT);
final float defaultScale = getInstrumentation().getTargetContext().getResources().
getDisplayMetrics().density;
        assertEquals(defaultScale, mWebView.getScale(), 0f);

mWebView.setInitialScale(0);
// modify content to fool WebKit into re-loading
mWebView.loadData("<html><body>" + p + "2" + "</body></html>", "text/html", "UTF-8");
waitForLoadComplete(mWebView, TEST_TIMEOUT);
        assertEquals(defaultScale, mWebView.getScale(), 0f);

mWebView.setInitialScale(50);
mWebView.loadData("<html><body>" + p + "3" + "</body></html>", "text/html", "UTF-8");
//Synthetic comment -- @@ -1474,7 +1474,7 @@
mWebView.setInitialScale(0);
mWebView.loadData("<html><body>" + p + "4" + "</body></html>", "text/html", "UTF-8");
waitForLoadComplete(mWebView, TEST_TIMEOUT);
        assertEquals(defaultScale, mWebView.getScale(), 0f);
}

@TestTargetNew(







