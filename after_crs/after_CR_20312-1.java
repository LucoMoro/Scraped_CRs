/*Huawei CTS fix for receive progress during the web hitory item test set up

Change-Id:I2739d1d8425efbf2ce574b54f9a394faf652e087*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebHistoryItemTest.java b/tests/tests/webkit/src/android/webkit/cts/WebHistoryItemTest.java
//Synthetic comment -- index 4f73cdc..5369c98 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
@Override
protected void setUp() throws Exception {
super.setUp();
        getActivity().getWebView().setWebChromeClient(new android.webkit.WebChromeClient()); // add for receive progress
mWebServer = new CtsTestServer(getActivity());
}








