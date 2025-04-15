/*Settings: show low storage notify only for internal storage

low memory notification indecates the storage status for internal
storage. So before show this notification, check whether it is internal
storage

Change-Id:Ibba5cefde360a073ac470cdb42bbcc87be81b78dAuthor: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 62433*/




//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/StorageVolumePreferenceCategory.java b/src/com/android/settings/deviceinfo/StorageVolumePreferenceCategory.java
//Synthetic comment -- index 61188ec..c3c4b9d 100644

//Synthetic comment -- @@ -214,7 +214,7 @@

final IPackageManager pm = ActivityThread.getPackageManager();
try {
            if (mVolume == null && pm.isStorageLow()) {
mStorageLow = new Preference(context);
mStorageLow.setOrder(ORDER_STORAGE_LOW);
mStorageLow.setTitle(R.string.storage_low_title);







