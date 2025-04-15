/*BT: resolve an ANR in startDiscovery

Some synchronizations in BluetoothService.java were
not needed, since the called function is already synchronized,
and were blocking the startDiscovery function.

Change-Id:I46b172b7c9458ef26a4a102207832a7f978b0b86Author: Cedric Bondier <cedricx.bondier@intel.com>
Signed-off-by: Cedric Bondier <cedricx.bondier@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 43880*/




//Synthetic comment -- diff --git a/core/java/android/server/BluetoothService.java b/core/java/android/server/BluetoothService.java
//Synthetic comment -- index 6296b11..d87cc19 100755

//Synthetic comment -- @@ -1163,12 +1163,12 @@
return removeDeviceNative(getObjectPathFromAddress(address));
}

    public String[] listBonds() {
mContext.enforceCallingOrSelfPermission(BLUETOOTH_PERM, "Need BLUETOOTH permission");
return mBondState.listInState(BluetoothDevice.BOND_BONDED);
}

    /*package*/ String[] listInState(int state) {
return mBondState.listInState(state);
}








