/*Fixed to close the fileinputstream, when the SyncProgressMonitor find the cancel message while there is something to read.

Change-Id:I64bb826671b4f79cd9ab3ec999340ad30e3f0e87*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/SyncService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/SyncService.java
old mode 100644
new mode 100755
//Synthetic comment -- index f112a12..6b2875f

//Synthetic comment -- @@ -665,6 +665,14 @@
while (true) {
// check if we're canceled
if (monitor.isCanceled() == true) {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException ioe) {
                        Log.e("ddms", "file close error.");
                    }
                    fis = null;
                }
throw new SyncException(SyncError.CANCELED);
}








