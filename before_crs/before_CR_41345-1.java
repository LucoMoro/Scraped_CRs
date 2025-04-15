/*Storage Setting: show MISC file size for primary storage

In some platform, primary storage is not emulated and it is mounted
by vold service. As Android designed, vold mount primary storage
with fmask and dmask to be 0702, which means that system user
has no any permission to access primary storage.

Since MISC file size calculation needs to access primary storage,
system doesn't allow this, which finally makes the primary usage bar
cannot show the correct usage percent.

This patch will do a workaround to calculate the correct MISC file
sizes. But due to the permission, there is no way for setting
application to get the size for each MISC file, then there is no
way for MISC application to touch misc files. Thus when the user
click misc category, setting cannot jump to misc application.

Change-Id:I9461b8b7c6c0b595ebc09a9b4f9fb757c4c6bb73Author: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 37828*/
//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/StorageMeasurement.java b/src/com/android/settings/deviceinfo/StorageMeasurement.java
//Synthetic comment -- index 2792d09..65c13f5 100644

//Synthetic comment -- @@ -498,7 +498,18 @@
File top = new File(mStorageVolume.getPath());
mFileInfoForMisc = new ArrayList<FileInfo>();
File[] files = top.listFiles();
        if (files == null) return;
final int len = files.length;
// Get sizes of all top level nodes except the ones already computed...
long counter = 0;








//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/StorageVolumePreferenceCategory.java b/src/com/android/settings/deviceinfo/StorageVolumePreferenceCategory.java
//Synthetic comment -- index 0211c77..5cb7043 100644

//Synthetic comment -- @@ -440,6 +440,8 @@
// TODO Create a Videos category, type = vnd.android.cursor.dir/video
intent.setType("vnd.android.cursor.dir/image");
} else if (preference == mPreferences[MISC]) {
Context context = getContext().getApplicationContext();
if (mMeasurement.getMiscSize() > 0) {
intent = new Intent(context, MiscFilesHandler.class);







