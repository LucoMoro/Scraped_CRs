/*Fixing inappropriate handling of Broadcast SMS on Radio On.

Change-Id:I2d5278f6d515babf1f204eb77b3e747575afad56Signed-off-by: David Sobreira Marques <dpsmarques@gmail.com>*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index f93e494..c729c82 100644

//Synthetic comment -- @@ -392,8 +392,13 @@
mCm.reportSmsMemoryStatus(mStorageAvailable,
obtainMessage(EVENT_REPORT_MEMORY_STATUS_DONE));
}

case EVENT_NEW_BROADCAST_SMS:
handleBroadcastSms((AsyncResult)msg.obj);
break;
}







