/*Fix database race condition for
CTSWebkitTestCases:WebChromeClientTest--testOnReceivedIcon

Change-Id:I607a06c5be524d53e181f2776cc9f4de6732bb81*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebChromeClientTest.java b/tests/tests/webkit/src/android/webkit/cts/WebChromeClientTest.java
//Synthetic comment -- index b95f9c2..786549c 100644

//Synthetic comment -- @@ -126,8 +126,7 @@
WebIconDatabase mIconDb = WebIconDatabase.getInstance();
String dbPath = getActivity().getFilesDir().toString() + "/icons";
mIconDb.open(dbPath);


assertFalse(webChromeClient.hadOnReceivedIcon());








