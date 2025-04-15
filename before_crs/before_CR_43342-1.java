/*Fix the issue that MediaScannerService partial wake lock never released

If some exceptions happen during scan, the partial wake lock will never
be released;so release it in exception handler

Change-Id:Iad998be3dbe7fb58f90bc8e7eaaa24c4e9a65448Signed-off-by: linyan <ylin8@marvell.com>*/
//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaScannerService.java b/src/com/android/providers/media/MediaScannerService.java
//Synthetic comment -- index 99bd734..ac2fa65 100644

//Synthetic comment -- @@ -264,6 +264,10 @@
}
} catch (Exception e) {
Log.e(TAG, "Exception in handleMessage", e);
}

stopSelf(msg.arg1);







