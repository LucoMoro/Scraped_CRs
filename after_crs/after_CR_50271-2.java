/*update storage information as adding or deleting via MTP

Currently, do not update storage information of settings as adding
or deleting via MTP in "Settings -> Storage" screen.
Of cource, update information if re-enter storage setting.
So, to update storage information, use ACTION_MTP_STORAGE_STATE intent
when adding or deleting via MTP.
ACTION_MTP_STORAGE_STATE intent is sent when MTP Storge is changed.

Steps to reproduce:
1. connect as MTP
2. Settings -> Storage
3. add or delete via MTP
4. do not update storage information

Change-Id:I31375a7ed1bbd431d7c04b10022505e9fd4e0652*/




//Synthetic comment -- diff --git a/media/java/android/mtp/MtpDatabase.java b/media/java/android/mtp/MtpDatabase.java
//Synthetic comment -- index 487585e..cc70232 100644

//Synthetic comment -- @@ -330,6 +330,7 @@
} else {
mMediaScanner.scanMtpFile(path, mVolumeName, handle, format);
}
            mContext.sendBroadcast(new Intent(MtpStorage.ACTION_MTP_STORAGE_STATE));
} else {
deleteFile(handle);
}
//Synthetic comment -- @@ -950,6 +951,7 @@
Log.e(TAG, "failed to unhide/rescan for " + path);
}
}
                mContext.sendBroadcast(new Intent(MtpStorage.ACTION_MTP_STORAGE_STATE));
return MtpConstants.RESPONSE_OK;
} else {
return MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE;








//Synthetic comment -- diff --git a/media/java/android/mtp/MtpStorage.java b/media/java/android/mtp/MtpStorage.java
//Synthetic comment -- index e20eabc..d675142 100644

//Synthetic comment -- @@ -28,6 +28,14 @@
*/
public class MtpStorage {

    /**
     * Broadcast Action:  A broadcast for MTP Storage state change event.
     *
     * This intent is sent when MTP Storage is changed by adding or deleting media.
     */
    public static final String ACTION_MTP_STORAGE_STATE =
        "android.mtp.action.MTP_STORAGE_STATE";

private final int mStorageId;
private final String mPath;
private final String mDescription;







