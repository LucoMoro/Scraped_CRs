/*MountService: Java.lang.NullPointerException occurs after unplug SD card.

Mediaprovider get Null pointer when Mountservice read storagelist
file. Modify implementation:
1. Mountservice register with Locale intent;
2. Receive Locale intent and read storaglist file accordingly.

Change-Id:Iafabd07ae067ab6feb0f910ab5b7b7b5b8343029Author: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 56843*/
//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index c512bc1..66178c8 100644

//Synthetic comment -- @@ -585,6 +585,8 @@
removeVolumeLocked(volume);
}
}
}
}
};
//Synthetic comment -- @@ -1296,6 +1298,7 @@
final IntentFilter userFilter = new IntentFilter();
userFilter.addAction(Intent.ACTION_USER_ADDED);
userFilter.addAction(Intent.ACTION_USER_REMOVED);
mContext.registerReceiver(mUserReceiver, userFilter, null, mHandler);

// Watch for USB changes on primary volume







