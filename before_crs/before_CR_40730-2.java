/*Fix the issue the setting crashes after unmount SD card in landscape

When rotate the phone, mLastClickedMountToggle and mClickedMountPoint
would be destroied which made dialog access NULL pointer.

Change-Id:Ic96fce93828ca7a6f8749fad6a258eb47831e062Author: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 26569*/
//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/Memory.java b/src/com/android/settings/deviceinfo/Memory.java
//Synthetic comment -- index cb344bf..cabc6e9 100644

//Synthetic comment -- @@ -57,8 +57,8 @@
// The mountToggle Preference that has last been clicked.
// Assumes no two successive unmount event on 2 different volumes are performed before the first
// one's preference is disabled
    private Preference mLastClickedMountToggle;
    private String mClickedMountPoint;

// Access using getMountService()
private IMountService mMountService = null;







