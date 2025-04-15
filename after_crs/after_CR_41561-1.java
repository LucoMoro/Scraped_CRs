/*[BT]: fixes null pointer related to profile init

The 'address' value is eventually passed to a thread init
function as the thread's name. It must not be null.

Change-Id:I08aeef8b1a426d6c87b177d4ec47e71ac982d6d0Author: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 31025*/




//Synthetic comment -- diff --git a/core/java/android/server/BluetoothService.java b/core/java/android/server/BluetoothService.java
//Synthetic comment -- index 6296b11..f6c30db 100755

//Synthetic comment -- @@ -2472,7 +2472,9 @@
}
for (String path : bonds) {
String address = getAddressFromObjectPath(path);
            if (address != null) {
                BluetoothDeviceProfileState state = addProfileState(address, false);
            }
}
}








