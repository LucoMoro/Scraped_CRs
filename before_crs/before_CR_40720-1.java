/*Fix the ANR in Settings

The ANR occured because of performing a long-lasting
operation on the main thread. The patch moves this
operation to a worker thread.

Change-Id:I3ae3999ff073d048dc2a7d38b5adbf7063cd897aAuthor: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 39143*/
//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/BluetoothDevicePreference.java b/src/com/android/settings/bluetooth/BluetoothDevicePreference.java
//Synthetic comment -- index c659f70..e16df9d 100644

//Synthetic comment -- @@ -71,7 +71,11 @@

mCachedDevice.registerCallback(this);

        onDeviceAttributesChanged();
}

CachedBluetoothDevice getCachedDevice() {







