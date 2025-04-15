/*android.webkit.cts.WebViewTest#testSetInitialScale sets screen density value 0.75 for browser to work with QVGA devices, the value needs to be set to 0.8 to support Chinese characters.*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java
//Synthetic comment -- index bd805a3..93fe789 100644

//Synthetic comment -- @@ -1439,8 +1439,11 @@
String p = "<p style=\"height:1000px;width:1000px\">Test setInitialScale.</p>";
mWebView.loadData("<html><body>" + p + "</body></html>", "text/html", "UTF-8");
waitForLoadComplete(mWebView, TEST_TIMEOUT);
        final float defaultScale = getInstrumentation().getTargetContext().getResources().
            getDisplayMetrics().density;
assertEquals(defaultScale, mWebView.getScale(), 0f);

mWebView.setInitialScale(0);







