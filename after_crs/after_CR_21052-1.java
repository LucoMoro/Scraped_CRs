/*Telephony: Fix cb sms implementation

Change-Id:Ie2a678e3ab99f0d5b9dd68cc4a79e1c5a58ecbe2*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index e68df2d..ad34550 100755

//Synthetic comment -- @@ -395,6 +395,7 @@
mCm.reportSmsMemoryStatus(mStorageAvailable,
obtainMessage(EVENT_REPORT_MEMORY_STATUS_DONE));
}
            break;

case EVENT_NEW_BROADCAST_SMS:
handleBroadcastSms((AsyncResult)msg.obj);







