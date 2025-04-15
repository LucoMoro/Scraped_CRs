/*Bluetooth: fixed MAC address appearing instead of friendly name

The bug occurs only for unknown devices that are discovered
for the very first time.
For some devices, the friendly name is not given immediately
but notified to the framework by a later ACTION_NAME_CHANGED event.
Sometimes the Name is updated in time but not the Alias,
that is temporarily equal to the MAC address of the device.
The fix consists in reading the Name before the Alias,
and resorting to this one only when the name is null,
as it is supposed to be.

Change-Id:If3029b44a95698fa6660bb8cf522a7c8c06a2d37Author: Raffaele Aquilone <raffaelex.aquilone@intel.com>
Signed-off-by: Raffaele Aquilone <raffaelex.aquilone@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 27197*/
//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothDevice.java b/core/java/android/bluetooth/BluetoothDevice.java
//Synthetic comment -- index 56e1735..e30a6bc 100644

//Synthetic comment -- @@ -622,9 +622,9 @@
* @hide
*/
public String getAliasName() {
        String name = getAlias();
if (name == null) {
            name = getName();
}
return name;
}







