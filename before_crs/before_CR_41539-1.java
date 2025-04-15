/*[BT]: fixes BT freeze issue in HID scenario

The root cause of this issue is a deadlock which
occurs quite systematically when trying to disconnect
HID devices.
The fix consists in synchronizing one of the methods
called during this phase.

Change-Id:Ie1f4699b35527e2c23e41817242fc4774f3b9fd9Author: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 26379*/
//Synthetic comment -- diff --git a/core/java/android/server/BluetoothService.java b/core/java/android/server/BluetoothService.java
//Synthetic comment -- index 6296b11..46dc116 100755

//Synthetic comment -- @@ -2177,7 +2177,7 @@
}
}

    public boolean disconnectInputDeviceInternal(BluetoothDevice device) {
synchronized (mBluetoothInputProfileHandler) {
return mBluetoothInputProfileHandler.disconnectInputDeviceInternal(device);
}







