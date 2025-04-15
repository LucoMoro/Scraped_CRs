/*Wait for completing the initialization of WebViewCore

This findAll() will fail when WebView$mNativeClass field is not updated.
That field is updated after the initialization of WebViewCore.
The test may execute before the completion of it. Because the test case
waits for the completion of it on UI thread, the test cannot notice
the completion message of it. In that case, test will fail.
So the test should wait for the completion of init on non-UI thread.

Change-Id:I7ad811d2d8259e58afd78cae9736dbf78df547e6*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java
//Synthetic comment -- index 05ff747..2b632f9 100755

//Synthetic comment -- @@ -908,14 +908,17 @@
args = {String.class},
notes = "Cannot check highlighting"
)
    public void testFindAll() throws Throwable {
String p = "<p>Find all instances of find on the page and highlight them.</p>";

mWebView.loadData("<html><body>" + p + "</body></html>", "text/html", "UTF-8");
waitForLoadComplete(mWebView, TEST_TIMEOUT);

        runTestOnUiThread(new Runnable() {
            public void run() {
                assertEquals(2, mWebView.findAll("find"));
            }
        });
}

@TestTargets({







