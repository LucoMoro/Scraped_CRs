/*[BT]: fixes crash when turning off BT while pairing

The crash was occuring due to a null pointer.
This patch introduces a sanity check for this error.

Change-Id:I0a2597559526100154b9b40df06d12ca47fa0befAuthor: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 31025*/
//Synthetic comment -- diff --git a/core/java/android/server/BluetoothBondState.java b/core/java/android/server/BluetoothBondState.java
//Synthetic comment -- index 0446f02..ce6077e 100644

//Synthetic comment -- @@ -122,7 +122,11 @@
mState.clear();
if (DBG) Log.d(TAG, "found " + bonds.length + " bonded devices");
for (String device : bonds) {
            mState.put(mService.getAddressFromObjectPath(device).toUpperCase(),
BluetoothDevice.BOND_BONDED);
}
}







