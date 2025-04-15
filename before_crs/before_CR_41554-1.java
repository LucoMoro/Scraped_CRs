/*[BT]: fixes ANR related to bond state retrieval

This ANR occurs because of excessive locking.
This patch removes the locking associated
with the method call which eventually causes the ANR.

Change-Id:I0f55cae64def5298ba15621a859e78be54044230Author: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 35907*/
//Synthetic comment -- diff --git a/core/java/android/server/BluetoothService.java b/core/java/android/server/BluetoothService.java
//Synthetic comment -- index 6296b11..9913d69 100755

//Synthetic comment -- @@ -1172,7 +1172,7 @@
return mBondState.listInState(state);
}

    public synchronized int getBondState(String address) {
mContext.enforceCallingOrSelfPermission(BLUETOOTH_PERM, "Need BLUETOOTH permission");
if (!BluetoothAdapter.checkBluetoothAddress(address)) {
return BluetoothDevice.ERROR;







