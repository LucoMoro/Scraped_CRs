/*code left opened files behind
verified with lsof DownloadProvider after downloading a file shows:
${proc} 338    10034   33w   REG      179,0  167634    5 /sdcard/download/fw4-1.pdf

Change-Id:I8e2412fe9a6348f5ece6f5ca3a9ebf99a4474bce*/
//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadThread.java b/src/com/android/providers/downloads/DownloadThread.java
//Synthetic comment -- index d899314..d0a33bd 100644

//Synthetic comment -- @@ -694,8 +694,10 @@
FileUtils.setPermissions(filename, 0644, -1, -1);

// Sync to storage after completion
try {
                        new FileOutputStream(filename, true).getFD().sync();
} catch (FileNotFoundException ex) {
Log.w(Constants.TAG, "file " + filename + " not found: " + ex);
} catch (SyncFailedException ex) {
//Synthetic comment -- @@ -704,6 +706,14 @@
Log.w(Constants.TAG, "IOException trying to sync " + filename + ": " + ex);
} catch (RuntimeException ex) {
Log.w(Constants.TAG, "exception while syncing file: ", ex);
}
}
}







