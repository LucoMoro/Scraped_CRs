/*[BT]: fixes crash caused by null BT address

The crash occures when trying to create a BluetoothDevice
with a null BT address. This patch prevents the call to the
BluetoothDevice constructor when the BT address is null.

Change-Id:I2da329370de69b1f7c88fb1a90c2e523f7e40943Author: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 37733*/
//Synthetic comment -- diff --git a/core/java/android/server/BluetoothA2dpService.java b/core/java/android/server/BluetoothA2dpService.java
//Synthetic comment -- index 08a99d2..a402df8 100644

//Synthetic comment -- @@ -212,6 +212,9 @@
String [] paths = devices.split(",");
for (String path: paths) {
String address = mBluetoothService.getAddressFromObjectPath(path);
BluetoothDevice device = mAdapter.getRemoteDevice(address);
ParcelUuid[] remoteUuids = mBluetoothService.getRemoteUuids(address);
if (remoteUuids != null)







