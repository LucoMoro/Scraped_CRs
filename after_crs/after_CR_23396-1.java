/*android.webkit.cts.WebViewTest#testSetInitialScale  sets screen density value 0.75 for browser to work with QVGA devices, the value needs to be set to 0.8 to support Chinese characters.

Change-Id:I957f17ed605fff5dc0a8aa95f9745c23ac543b3c*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java
//Synthetic comment -- index bd805a3..93fe789 100644

//Synthetic comment -- @@ -1439,8 +1439,11 @@
String p = "<p style=\"height:1000px;width:1000px\">Test setInitialScale.</p>";
mWebView.loadData("<html><body>" + p + "</body></html>", "text/html", "UTF-8");
waitForLoadComplete(mWebView, TEST_TIMEOUT);
        final float defaultScale = (
            ( getInstrumentation().getTargetContext().getResources().getDisplayMetrics().density > 0.8f )
            ? getInstrumentation().getTargetContext().getResources().getDisplayMetrics().density
            : 0.8f
        );
assertEquals(defaultScale, mWebView.getScale(), 0f);

mWebView.setInitialScale(0);







