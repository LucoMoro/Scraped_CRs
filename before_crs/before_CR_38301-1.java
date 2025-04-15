/*Fix CTS fail of the test case "android.webkit.cts.WebViewTest#testLoadDataWithBaseUrl"

We faced CTS fail in testLoadDataWithBaseUrl.
The cause of the fail is that "mIconDb" is not closed in the test case by redefining the variable with the same name.

The variable must be closed:
 - mIconDb in WebChromeClientTest class.

The variable was redefined:
 - mIconDb in run() method of worker thread in testOnReceivedIcon method.

Change-Id:I4947fc164623a6367992b37a88c97e79070d8afaSigned-off-by: nao <nao.tanaka.cy@kyocera.jp>*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebChromeClientTest.java b/tests/tests/webkit/src/android/webkit/cts/WebChromeClientTest.java
//Synthetic comment -- index cf20217..9027b7a 100644

//Synthetic comment -- @@ -128,7 +128,7 @@
@Override
public void run() {
// getInstance must run on the UI thread
                WebIconDatabase mIconDb = WebIconDatabase.getInstance();
String dbPath = getActivity().getFilesDir().toString() + "/icons";
mIconDb.open(dbPath);
mIconDb.removeAllIcons();







