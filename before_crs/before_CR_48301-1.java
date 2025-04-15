/*Mms: Notify user of Send SMS failure due to FDN

Upon Send SMS failure due to destination number or
service center number not present in the FDN list,
failure is not notified to the MessageNotification.
Due to this Sms remains in Sending state even
after the SMS send has failed.

This patch fixes the issue by notifying the message
failure and also moving the message to respective folder.

Change-Id:I1f8dfe2d9b276c8c6f4d921c2c147a8ef90643e6Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 48558*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsReceiverService.java b/src/com/android/mms/transaction/SmsReceiverService.java
//Synthetic comment -- index 724e863..c50b60f 100755

//Synthetic comment -- @@ -327,6 +327,7 @@
}
});
} else if (mResultCode == SmsManager.RESULT_ERROR_FDN_CHECK_FAILURE) {
mToastHandler.post(new Runnable() {
public void run() {
Toast.makeText(SmsReceiverService.this, getString(R.string.fdn_check_failure),







