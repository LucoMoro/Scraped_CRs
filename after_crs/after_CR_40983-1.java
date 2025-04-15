/*Enhancement of the Excpetion management

Android bluetooth application do not manage by default
all exception which can appear.
In our case when USB storage is mounted, the bluetooth
process do not crash anymore. Progress bar also
informed that sent action failed.

Change-Id:I91a8584c697c73481a84dd9436a89bb0067d090aAuthor: Sebastien Cayetanot <sebastien.cayetanot@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 7482 17106*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppObexClientSession.java b/src/com/android/bluetooth/opp/BluetoothOppObexClientSession.java
//Synthetic comment -- index ea7e4b2..757f2ac 100644

//Synthetic comment -- @@ -1,4 +1,4 @@
 /*
* Copyright (c) 2008-2009, Motorola, Inc.
*
* All rights reserved.
//Synthetic comment -- @@ -498,6 +498,8 @@
handleSendException(e.toString());
} catch (IndexOutOfBoundsException e) {
handleSendException(e.toString());
            } catch (IllegalStateException e) {
                handleSendException(e.toString());
} finally {
try {
fileInfo.mInputStream.close();








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppReceiver.java b/src/com/android/bluetooth/opp/BluetoothOppReceiver.java
//Synthetic comment -- index a061fa8..80c3fa2 100644

//Synthetic comment -- @@ -223,9 +223,16 @@

String toastMsg = null;
BluetoothOppTransferInfo transInfo = new BluetoothOppTransferInfo();
            try {
                transInfo = BluetoothOppUtility.queryRecord(context, intent.getData());
                if (transInfo == null) {
                    Log.e(TAG, "Error: Can not get data from db");
                    return;
                }
            } catch (IllegalStateException e) {
                toastMsg = context.getString(R.string.notification_sent_fail,
                                             transInfo.mFileName);
                Log.e(TAG, "Error: Excetpion in URL management");
return;
}








