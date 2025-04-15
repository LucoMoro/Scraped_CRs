/*Avoiding a race condition in testOnReceivedIcon.

A race condition in the
android.webkit.cts.WebChromeClientTest#testOnReceivedIcon test has been
avoided by putting a 250ms delay between the call to mIconDb.open(dbPath)
and the call to mIconDb.removeAllIcons() in the public void
testOnReceivedIcon() method of the WebChromeClientTest class in the
./cts/tests/tests/webkit/src/android/webkit/cts/WebChromeClientTest.java
module.

Change-Id:Iddf1c9247ccef9e62e879a7179425d4b021cc19b*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebChromeClientTest.java b/tests/tests/webkit/src/android/webkit/cts/WebChromeClientTest.java
//Synthetic comment -- index 40c6c93f..b95f9c2 100644

//Synthetic comment -- @@ -119,13 +119,14 @@
args = {WebView.class, Bitmap.class}
)
})
    public void testOnReceivedIcon() throws Exception {
final MockWebChromeClient webChromeClient = new MockWebChromeClient();
mWebView.setWebChromeClient(webChromeClient);

WebIconDatabase mIconDb = WebIconDatabase.getInstance();
String dbPath = getActivity().getFilesDir().toString() + "/icons";
mIconDb.open(dbPath);
        Thread.sleep(250);
mIconDb.removeAllIcons();

assertFalse(webChromeClient.hadOnReceivedIcon());







