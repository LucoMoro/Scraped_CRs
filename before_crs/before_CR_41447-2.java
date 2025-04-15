/*ExternalStorageFormatter.java

ExternalStorageFormatter doesn't support multiple storage erasing.

Change-Id:Idc53e9565b2dcb9dc472372277cc4bd9646cf3a7Author: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 47043, 18522*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/os/storage/ExternalStorageFormatter.java b/core/java/com/android/internal/os/storage/ExternalStorageFormatter.java
//Synthetic comment -- index 3905c88..b867516 100644

//Synthetic comment -- @@ -21,6 +21,8 @@

import com.android.internal.R;

/**
* Takes care of unmounting and formatting external storage.
*/
//Synthetic comment -- @@ -51,6 +53,34 @@
private boolean mFactoryReset = false;
private boolean mAlwaysReset = false;

StorageEventListener mStorageListener = new StorageEventListener() {
@Override
public void onStorageStateChanged(String path, String oldState, String newState) {
//Synthetic comment -- @@ -122,8 +152,7 @@
public void onCancel(DialogInterface dialog) {
IMountService mountService = getMountService();
String extStoragePath = mStorageVolume == null ?
                Environment.getExternalStorageDirectory().toString() :
                mStorageVolume.getPath();
try {
mountService.mountVolume(extStoragePath);
} catch (RemoteException e) {
//Synthetic comment -- @@ -142,7 +171,7 @@

void updateProgressState() {
String status = mStorageVolume == null ?
                Environment.getExternalStorageState() :
mStorageManager.getVolumeState(mStorageVolume.getPath());
if (Environment.MEDIA_MOUNTED.equals(status)
|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(status)) {
//Synthetic comment -- @@ -163,7 +192,7 @@
updateProgressDialog(R.string.progress_erasing);
final IMountService mountService = getMountService();
final String extStoragePath = mStorageVolume == null ?
                    Environment.getExternalStorageDirectory().toString() :
mStorageVolume.getPath();
if (mountService != null) {
new Thread() {







