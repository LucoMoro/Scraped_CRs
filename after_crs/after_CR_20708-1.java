/*Changing the delay time from 250ms to 3000ms, because avoiding a race condition in testOnReceivedIcon.

Change-Id:I3bd5595cdb5c2f96794383382ebfe4231f878243*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebChromeClientTest.java b/tests/tests/webkit/src/android/webkit/cts/WebChromeClientTest.java
//Synthetic comment -- index b95f9c2..795ed615 100644

//Synthetic comment -- @@ -126,7 +126,7 @@
WebIconDatabase mIconDb = WebIconDatabase.getInstance();
String dbPath = getActivity().getFilesDir().toString() + "/icons";
mIconDb.open(dbPath);
        Thread.sleep(3000);
mIconDb.removeAllIcons();

assertFalse(webChromeClient.hadOnReceivedIcon());







