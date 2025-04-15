/*Memory.java: disable remove unmount/mount button until unmount/mount is done

Disable unmount/mount button since the end user will click them again before
the setting is updated. So disable them until setting updated the status

Change-Id:I8bc6a06a76bd4636e4c65a334ee00503118d5bbfAuthor: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 47861*/




//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/Memory.java b/src/com/android/settings/deviceinfo/Memory.java
//Synthetic comment -- index 825a7be..fa1f1c5 100644

//Synthetic comment -- @@ -343,6 +343,7 @@
IMountService mountService = getMountService();
try {
if (mountService != null) {
                sLastClickedMountToggle.setEnabled(false);
mountService.mountVolume(sClickedMountPoint);
} else {
Log.e(TAG, "Mount service is null, can't mount");







