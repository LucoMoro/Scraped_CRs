/*StorageVolumePreferenceCategory: enable erasing operation

enable erasing operation for secondary external storage.

Change-Id:Ic6c9329275dcdb908e12a27f01169a4507440f34Author: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 47043*/
//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/StorageVolumePreferenceCategory.java b/src/com/android/settings/deviceinfo/StorageVolumePreferenceCategory.java
//Synthetic comment -- index 0211c77..f921b32 100644

//Synthetic comment -- @@ -175,7 +175,7 @@
mAllowFormat = mStorageVolume != null && !mStorageVolume.isEmulated();
// For now we are disabling reformatting secondary external storage
// until some interoperability problems with MTP are fixed
        if (!isPrimary) mAllowFormat = false;
}

public void init() {







