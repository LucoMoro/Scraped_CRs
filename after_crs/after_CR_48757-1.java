/*ExternalFormat Screen stay display "Unmounting SD card" when format SD card again.

Add support for multiple storage parameter.

Change-Id:I0cb51f5d0e1c7211da7585a27d3ff6e17ef95fb6Author: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 47043 20038*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/app/ExternalMediaFormatActivity.java b/core/java/com/android/internal/app/ExternalMediaFormatActivity.java
//Synthetic comment -- index 5ab9217..afd8a24 100644

//Synthetic comment -- @@ -26,7 +26,8 @@
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.os.storage.StorageVolume;
import android.os.storage.StorageManager;
/**
* This activity is shown to the user to confirm formatting of external media.
* It uses the alert dialog style. It will be launched from a notification, or from settings
//Synthetic comment -- @@ -34,6 +35,8 @@
public class ExternalMediaFormatActivity extends AlertActivity implements DialogInterface.OnClickListener {

private static final int POSITIVE_BUTTON = AlertDialog.BUTTON_POSITIVE;
    private StorageVolume mStorageVolume;
    private StorageManager mStorageManager;

/** Used to detect when the media state changes, in case we need to call finish() */
private BroadcastReceiver mStorageReceiver = new BroadcastReceiver() {
//Synthetic comment -- @@ -56,6 +59,7 @@
super.onCreate(savedInstanceState);

Log.d("ExternalMediaFormatActivity", "onCreate!");
        mStorageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
// Set up the "dialog"
final AlertController.AlertParams p = mAlertParams;
p.mIconId = com.android.internal.R.drawable.stat_sys_warning;
//Synthetic comment -- @@ -95,7 +99,20 @@
if (which == POSITIVE_BUTTON) {
Intent intent = new Intent(ExternalStorageFormatter.FORMAT_ONLY);
intent.setComponent(ExternalStorageFormatter.COMPONENT_NAME);
            StorageVolume[] storageVolumes = mStorageManager.getVolumeList();
            int length = storageVolumes.length;
            if ( storageVolumes != null && storageVolumes.length > 1 ) {
                //Transfer the storage volume to the new intent
                mStorageVolume = getIntent().getParcelableExtra(
                    StorageVolume.EXTRA_STORAGE_VOLUME);
                if( mStorageVolume != null ) {
                    intent.putExtra(StorageVolume.EXTRA_STORAGE_VOLUME,mStorageVolume);
                    Log.d("ExternalMediaFormatActivity","onClick! storage path is " + mStorageVolume.getPath());
                } else {
                    Log.d("ExternalMediaFormatActivity","onClick! There is no extra storage volume!");
                }
            }
           startService(intent);
}

// No matter what, finish the activity








//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/usb/StorageNotification.java b/packages/SystemUI/src/com/android/systemui/usb/StorageNotification.java
//Synthetic comment -- index 91fc67a..f564144 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.util.Slog;
import android.os.storage.StorageVolume;

public class StorageNotification extends StorageEventListener {
private static final String TAG = "StorageNotification";
//Synthetic comment -- @@ -80,6 +81,25 @@
onUsbMassStorageConnectionChanged(connected);
}


    private Intent intentForFormat(String formatpath) {
        Intent intent = new Intent ();
        intent.setClass(mContext, com.android.internal.app.ExternalMediaFormatActivity.class);

        StorageVolume[] storageVolumes = mStorageManager.getVolumeList();
        //If it's multiple storage, pass storagevolume to FormatActivity;
        if ( storageVolumes != null && storageVolumes.length > 1 ) {
            int length = storageVolumes.length;
            for (int i = 0; i < length; i++) {
                StorageVolume storageVolume = storageVolumes[i];
                if ( formatpath.equals( storageVolume.getPath() ) ){
                    intent.putExtra(StorageVolume.EXTRA_STORAGE_VOLUME, storageVolume);
                    break;
                }
            }
        }
        return intent;
    }
/*
* @override com.android.os.storage.StorageEventListener
*/
//Synthetic comment -- @@ -202,8 +222,7 @@
* Storage has no filesystem. Show blank media notification,
* and enable UMS notification if connected.
*/
            Intent intent = intentForFormat(path);
PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, 0);

setMediaStorageNotification(
//Synthetic comment -- @@ -216,8 +235,7 @@
* Storage is corrupt. Show corrupt media notification,
* and enable UMS notification if connected.
*/
            Intent intent = intentForFormat(path);
PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, 0);

setMediaStorageNotification(







