/*bluetooth/opp: always show the latest device name on the toast when transferring file

Currently the device name shown on the toast is not consistent with the
remote device name if it is changed.
In this patch, we always ask the bluetooth service for the latest device
name, instead of asking the local OPP manager for it (which is a cached
value).

Change-Id:I82207a8f83f5e8fb3af1f00a1296bd8cfaa5a716Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppReceiver.java b/src/com/android/bluetooth/opp/BluetoothOppReceiver.java
//Synthetic comment -- index 60f58f1..efe45de 100644

//Synthetic comment -- @@ -99,7 +99,7 @@
mOppManager.startTransfer(remoteDevice);

// Display toast message
            String deviceName = remoteDevice.getName();
String toastMsg;
int batchSize = mOppManager.getBatchSize();
if (mOppManager.mMultipleFlag) {







