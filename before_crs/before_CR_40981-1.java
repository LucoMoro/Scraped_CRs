/*opp: Fix the issue the shown device name on toast is not up to date

At the begining of file sending, device name in the toast is not correct
if the remote device name has changed.
Switch to calling BluetoothDevice.getName().

Change-Id:I0403404b19bba057f777811ad957dafe5836f7c7Author: Stone Zhang <stone.zhang@borqs.com>
Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 11557 17106*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppReceiver.java b/src/com/android/bluetooth/opp/BluetoothOppReceiver.java
//Synthetic comment -- index a061fa8..cd2e948 100644

//Synthetic comment -- @@ -100,7 +100,7 @@
mOppManager.startTransfer(remoteDevice);

// Display toast message
            String deviceName = mOppManager.getDeviceName(remoteDevice);
String toastMsg;
int batchSize = mOppManager.getBatchSize();
if (mOppManager.mMultipleFlag) {







