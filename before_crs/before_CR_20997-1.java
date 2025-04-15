/*Adding the delay time of 1000ms for waiting the WebView to create.

Change-Id:I8b335a0aa0e72af00fc170c6f26c0ade0ddc3db3*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java
//Synthetic comment -- index b9d054f..37c687e 100644

//Synthetic comment -- @@ -80,6 +80,7 @@
protected void setUp() throws Exception {
super.setUp();
mWebView = getActivity().getWebView();
File f = getActivity().getFileStreamPath("snapshot");
if (f.exists()) {
f.delete();







