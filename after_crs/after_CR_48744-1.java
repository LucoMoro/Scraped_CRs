/*Fix No notification while unmount SD Card

Check volume removable by volume's isRemovable insteaded of
Environment.isExternalStorageRemovable. isExternalStorageRemovable
is defined for primary storage only. So if the primary storage
cannot removable, but the secondary storage is removable, when removing
the secondary storage, StorageNotification will not send event for
the removable storage due to this

Change-Id:Ic9892516d3fb2f2c2b44f851003faa2fc11ef2e0Author: Li Gang <gang.g.li@intel.com>
Signed-off-by: Li Gang <gang.g.li@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 39364*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/usb/StorageNotification.java b/packages/SystemUI/src/com/android/systemui/usb/StorageNotification.java
//Synthetic comment -- index 91fc67a..9499703 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.os.UserHandle;
import android.os.storage.StorageEventListener;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.util.Slog;

//Synthetic comment -- @@ -127,8 +128,21 @@
}

private void onStorageStateChangedAsync(String path, String oldState, String newState) {
        boolean isRemovable = false;

Slog.i(TAG, String.format(
"Media {%s} state changed from {%s} -> {%s}", path, oldState, newState));

        StorageVolume[] storageVolumes = mStorageManager.getVolumeList();
        if (storageVolumes != null) {
            int length = storageVolumes.length;
            for (int i = 0; i < length; i++) {
                StorageVolume storageVolume = storageVolumes[i];
                if (path.equals(storageVolume.getPath())) {
                    isRemovable = storageVolume.isRemovable();
                }
            }
        }
if (newState.equals(Environment.MEDIA_SHARED)) {
/*
* Storage is now shared. Modify the UMS notification
//Synthetic comment -- @@ -177,7 +191,7 @@
* Show safe to unmount media notification, and enable UMS
* notification if connected.
*/
                    if (isRemovable) {
setMediaStorageNotification(
com.android.internal.R.string.ext_media_safe_unmount_notification_title,
com.android.internal.R.string.ext_media_safe_unmount_notification_message,







