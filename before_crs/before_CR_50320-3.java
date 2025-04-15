/*update storage information as adding or deleting via MTP

Currently, do not update storage information of settings as adding
or deleting via MTP in "Settings -> Storage" screen.
Of cource, update information if re-enter storage setting.
So, to update storage information, use ACTION_MTP_STORAGE_STATE intent
when adding or deleting via MTP.

Steps to reproduce:
1. connect as MTP
2. Settings -> Storage
3. add or delete via MTP
4. do not update storage information

Change-Id:I82e108a8132dc44b2c20ced63449d102183856d8*/
//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/Memory.java b/src/com/android/settings/deviceinfo/Memory.java
//Synthetic comment -- index 825a7be..f57861d 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
//Synthetic comment -- @@ -127,6 +128,7 @@

intentFilter = new IntentFilter();
intentFilter.addAction(UsbManager.ACTION_USB_STATE);
getActivity().registerReceiver(mMediaScannerReceiver, intentFilter);

for (StorageVolumePreferenceCategory category : mCategories) {
//Synthetic comment -- @@ -256,6 +258,10 @@
for (StorageVolumePreferenceCategory category : mCategories) {
category.onMediaScannerFinished();
}
}
}
};








//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/StorageVolumePreferenceCategory.java b/src/com/android/settings/deviceinfo/StorageVolumePreferenceCategory.java
//Synthetic comment -- index 61188ec..e75df9b 100644

//Synthetic comment -- @@ -387,6 +387,10 @@
measure();
}

public void onCacheCleared() {
measure();
}







