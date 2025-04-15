/*[BT]: fixes crash due to null string pointer

The crash occurs when calling a native function which
computes the length of a string. It seems that the failure
occurs when the string pointer is null. This patch
introduces a null pointer test for the string reference.

Change-Id:Ic0ea660a31090c260a1d3ad290d877bb520ef977Author: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 42815*/
//Synthetic comment -- diff --git a/core/java/android/server/BluetoothInputProfileHandler.java b/core/java/android/server/BluetoothInputProfileHandler.java
//Synthetic comment -- index 31764b0..ba4419d 100644

//Synthetic comment -- @@ -82,7 +82,8 @@
boolean connectInputDeviceInternal(BluetoothDevice device) {
String objectPath = mBluetoothService.getObjectPathFromAddress(device.getAddress());
handleInputDeviceStateChange(device, BluetoothInputDevice.STATE_CONNECTING);
        if (!mBluetoothService.connectInputDeviceNative(objectPath)) {
handleInputDeviceStateChange(device, BluetoothInputDevice.STATE_DISCONNECTED);
return false;
}







