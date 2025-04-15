/*defcontainer: close autoOut stream after copyFile()

ParcelFileDescriptor.AutoCloseOutputStream only take care of
calling ParcelFileDescriptor.close() for you when the stream
is closed. So if you don't close this stream , the stream and
the file associated will be open forever.
This case is happen when constantly install the same app, which
offen happens if you are developping a app. The unclosed file
will never be really deleted from /data(this can be seen from
the output of lsof command after re-launch the app in eclipse),
so it will exhauste the space left on /data after several rounds.
Signed-off-by: dannoy.lee <dannoy.lee@gmail.com>

Change-Id:I2b6bca48734d67d6a46a6da45faf10f6e9926437*/




//Synthetic comment -- diff --git a/packages/DefaultContainerService/src/com/android/defcontainer/DefaultContainerService.java b/packages/DefaultContainerService/src/com/android/defcontainer/DefaultContainerService.java
//Synthetic comment -- index a28b8a4..0967578 100644

//Synthetic comment -- @@ -129,6 +129,7 @@

try {
copyFile(packageURI, autoOut, encryptionParams);
                autoOut.close();
return PackageManager.INSTALL_SUCCEEDED;
} catch (FileNotFoundException e) {
Slog.e(TAG, "Could not copy URI " + packageURI.toString() + " FNF: "







