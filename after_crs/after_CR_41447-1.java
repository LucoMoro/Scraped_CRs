/*ExternalStorageFormatter.java

ExternalStorageFormatter doesn't support multiple storage erasing.

Change-Id:Idc53e9565b2dcb9dc472372277cc4bd9646cf3a7Author: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 47043, 18522*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/os/storage/ExternalStorageFormatter.java b/core/java/com/android/internal/os/storage/ExternalStorageFormatter.java
//Synthetic comment -- index 3905c88..e87f481 100644

//Synthetic comment -- @@ -21,6 +21,8 @@

import com.android.internal.R;

import java.io.File;

/**
* Takes care of unmounting and formatting external storage.
*/
//Synthetic comment -- @@ -51,6 +53,33 @@
private boolean mFactoryReset = false;
private boolean mAlwaysReset = false;

    private static final File EXTERNAL_STORAGE_DIRECTORY_EXT
             = new File( "/mnt/sdcard_ext");


    private boolean supportMultipleStorage(){
        StorageVolume[] storageVolumes = mStorageManager.getVolumeList();

        if( storageVolumes.length == 1 )
            return false;
        else
            return true;
    }

    private String getExternalStorageState() {
            return mStorageManager.getVolumeState( getExternalStoragePath() );
    }

    private  String getExternalStoragePath() {
        String storagePath;
        if ( supportMultipleStorage() )
            storagePath = EXTERNAL_STORAGE_DIRECTORY_EXT.toString();
        else
            storagePath = Environment.getExternalStorageDirectory().toString();

        return storagePath;
    }

StorageEventListener mStorageListener = new StorageEventListener() {
@Override
public void onStorageStateChanged(String path, String oldState, String newState) {
//Synthetic comment -- @@ -122,8 +151,7 @@
public void onCancel(DialogInterface dialog) {
IMountService mountService = getMountService();
String extStoragePath = mStorageVolume == null ?
                getExternalStoragePath(): mStorageVolume.getPath();
try {
mountService.mountVolume(extStoragePath);
} catch (RemoteException e) {
//Synthetic comment -- @@ -142,7 +170,7 @@

void updateProgressState() {
String status = mStorageVolume == null ?
                getExternalStorageState():
mStorageManager.getVolumeState(mStorageVolume.getPath());
if (Environment.MEDIA_MOUNTED.equals(status)
|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(status)) {
//Synthetic comment -- @@ -163,7 +191,7 @@
updateProgressDialog(R.string.progress_erasing);
final IMountService mountService = getMountService();
final String extStoragePath = mStorageVolume == null ?
                    getExternalStoragePath():
mStorageVolume.getPath();
if (mountService != null) {
new Thread() {







