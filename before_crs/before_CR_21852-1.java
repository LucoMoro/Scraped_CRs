/*Telephony: Don't ack CB messages

SmsDispatcher receives its own broadcast to ack messages.
CB message needs to be excluded from the acking code since
it should not be acked.

Change-Id:Iaf8745101babb94ad07e95c4e2abe47fafaec34b*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index ad34550..e52907c 100755

//Synthetic comment -- @@ -998,6 +998,8 @@
} else if (intent.getAction().equals(Intent.ACTION_DEVICE_STORAGE_NOT_FULL)) {
mStorageAvailable = true;
mCm.reportSmsMemoryStatus(true, obtainMessage(EVENT_REPORT_MEMORY_STATUS_DONE));
} else {
// Assume the intent is one of the SMS receive intents that
// was sent as an ordered broadcast.  Check result and ACK.







