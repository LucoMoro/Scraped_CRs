/*Fixed DevicePicker showing all dev even if BT is off

The DevicePicker application shows a list of cached devices
that usually is not up to date with the current state
of the Bluetooth adapter. As a result, the list shows all
the Bluetooth devices nearby even if the Bluetooth
has been turned off.
To fix the problem a check has been inserted to remove
all devices in case the Bluetooth adapter is not turned on.

Change-Id:I5b8f65b0c8a4e6ff81cf8e2696294cac1701bcbcAuthor: Raffaele Aquilone <raffaelex.aquilone@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 45004*/




//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/DevicePickerFragment.java b/src/com/android/settings/bluetooth/DevicePickerFragment.java
//Synthetic comment -- index 87d31e7..a7f2fd5 100644

//Synthetic comment -- @@ -58,6 +58,8 @@
public void onResume() {
super.onResume();
addCachedDevices();
        if (mLocalAdapter.getBluetoothState() != BluetoothAdapter.STATE_ON)
            removeAllDevices();
if (mStartScanOnResume) {
mLocalAdapter.startScanning(true);
mStartScanOnResume = false;







