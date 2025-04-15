/*Fix ANR when stopping OPP transfer

This issue occurs because a long lasting operation (SQL
delete) is performed on the main thread. This patch moves
this operation on a worker thread.

Change-Id:I5260390870c782cbc2782a1d2aef2f79bbd18bcfAuthor: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 39065*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppTransferActivity.java b/src/com/android/bluetooth/opp/BluetoothOppTransferActivity.java
//Synthetic comment -- index 87a695d..107454d 100644

//Synthetic comment -- @@ -393,7 +393,11 @@
case DialogInterface.BUTTON_NEGATIVE:
if (mWhichDialog == DIALOG_RECEIVE_ONGOING || mWhichDialog == DIALOG_SEND_ONGOING) {
// "Stop" button
                    new Thread (new Runnable() {
                        public void run() {
                            getContentResolver().delete(mUri, null, null);
                        }
                    }).start();

String msg = "";
if (mWhichDialog == DIALOG_RECEIVE_ONGOING) {







