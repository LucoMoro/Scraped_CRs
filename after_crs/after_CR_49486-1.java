/*Mtp: MtpService TOMBSTONE continue happen when do usb otg plug/unplug test.

Call new interface in MtpDatabase to delete global reference
in order to avoid GREF issue. The patch depend on MtpDatabase class.

Note: This patch is related to a change in platforms/frameworks/base

Change-Id:Id980f669c56461f1e4b48f52d416f3651f15589aAuthor: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 60891*/




//Synthetic comment -- diff --git a/src/com/android/providers/media/MtpService.java b/src/com/android/providers/media/MtpService.java
//Synthetic comment -- index 04033e9..123e600 100644

//Synthetic comment -- @@ -192,6 +192,9 @@
public void onDestroy() {
unregisterReceiver(mReceiver);
mStorageManager.unregisterListener(mStorageEventListener);
        if (mDatabase != null) {
            mDatabase.release();
        }
}

private final IMtpService.Stub mBinder =







