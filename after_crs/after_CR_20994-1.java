/*Adding the delay time of 1000ms for waiting the WebView to create.

Change-Id:Ie8bbd146c0d003d8989a2e8ef1a5158c1610366d*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewClientTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewClientTest.java
//Synthetic comment -- index 49d68c6..8066d88 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
protected void setUp() throws Exception {
super.setUp();
mWebView = getActivity().getWebView();
        Thread.sleep(1000);
}

@Override







