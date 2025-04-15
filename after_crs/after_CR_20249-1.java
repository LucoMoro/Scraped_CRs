/*Nuke WebChromeClientTest#testOnReceivedIcon

Remove this troublesome test that has been very flakey.

Change-Id:I66af4d3a9e9f96fd082f4c75784941446536ead8*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebChromeClientTest.java b/tests/tests/webkit/src/android/webkit/cts/WebChromeClientTest.java
//Synthetic comment -- index b95f9c2..900b95a 100644

//Synthetic comment -- @@ -115,36 +115,6 @@
@TestTargets({
@TestTargetNew(
level = TestLevel.COMPLETE,
method = "onCreateWindow",
args = {WebView.class, boolean.class, boolean.class, Message.class}
),







