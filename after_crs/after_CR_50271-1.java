/*update storage information as adding or deleting via MTP

Currently, do not update storage information of settings as adding
or deleting via MTP in "Settings -> Storage" screen.
Of cource, update information if re-enter storage setting.
So, to update storage information, use ACTION_MEDIA_SCANNER_SCAN_FILE intent
when adding or deleting via MTP.

Steps to reproduce:
1. connect as MTP
2. Settings -> Storage
3. add or delete via MTP
4. do not update storage information

Change-Id:I31375a7ed1bbd431d7c04b10022505e9fd4e0652*/




//Synthetic comment -- diff --git a/media/java/android/mtp/MtpDatabase.java b/media/java/android/mtp/MtpDatabase.java
//Synthetic comment -- index 487585e..b51ee00 100644

//Synthetic comment -- @@ -330,6 +330,7 @@
} else {
mMediaScanner.scanMtpFile(path, mVolumeName, handle, format);
}
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE));
} else {
deleteFile(handle);
}
//Synthetic comment -- @@ -950,6 +951,7 @@
Log.e(TAG, "failed to unhide/rescan for " + path);
}
}
                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE));
return MtpConstants.RESPONSE_OK;
} else {
return MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE;







