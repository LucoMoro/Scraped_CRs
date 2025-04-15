/*Fixed to close the socket channel already opened, when there was no adb response.

Change-Id:I72515b27eb4ab1a81a543d5a985a0f54dfb20e55*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AdbHelper.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AdbHelper.java
old mode 100644
new mode 100755
//Synthetic comment -- index 5b5a41f..6a6b3c3

//Synthetic comment -- @@ -90,6 +90,8 @@

AdbResponse resp = readAdbResponse(adbChan, false);
if (resp.okay == false) {
throw new AdbCommandRejectedException(resp.message);
}








