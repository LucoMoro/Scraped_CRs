/*Mms: Fix for sending long SMS to multiple recipients

When a long SMS is sent to multiple recipients, it is sent only to the first
recipient. MMS app checks extra EXTRA_MESSAGE_SENT_SEND_NEXT for true before
sending SMS to other recipients. Since same pending intent is used for all
fragments, this is never true. Fixed this issue by using differnet pending
intent for last fragment with extra EXTRA_MESSAGE_SENT_SEND_NEXT set to true.

Change-Id:I9f61274e0f20698544090419a85e0e6b8daff3e0*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsSingleRecipientSender.java b/src/com/android/mms/transaction/SmsSingleRecipientSender.java
//Synthetic comment -- index 7d88488..5f39dd3 100644

//Synthetic comment -- @@ -86,9 +86,10 @@
SmsReceiver.class);
if (i == messageCount -1) {
intent.putExtra(SmsReceiverService.EXTRA_MESSAGE_SENT_SEND_NEXT, true);
}
            sentIntents.add(PendingIntent.getBroadcast(
                    mContext, 0, intent, 0));
}
try {
smsManager.sendMultipartTextMessage(mDest, mServiceCenter, messages, sentIntents, deliveryIntents);







