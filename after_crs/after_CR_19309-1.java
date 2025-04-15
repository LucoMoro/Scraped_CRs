/*FIX: CM issue 1733 / AOSP issue 2989

This is a first idea to fix CM issue 1733 / AOSP issue 2989.
The original implementation just stored the local time as the
sent time and not the time that's indicated in the SMS message
by the SMSC. I intended to add a second Inbox.DATE value for the
real SMSC time but that would need a lot of changes on several
places in the source.
So this request can also be seen as some kind of RFC.

Change-Id:I5d115a8a32f23bad858324c60115a7f32bbfb60d*/




//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsReceiverService.java b/src/com/android/mms/transaction/SmsReceiverService.java
//Synthetic comment -- index 3e2dd61..246bea0 100755

//Synthetic comment -- @@ -478,10 +478,7 @@
ContentValues values = new ContentValues();

values.put(Inbox.ADDRESS, sms.getDisplayOriginatingAddress());
        values.put(Inbox.DATE, sms.getTimestampMillis());
values.put(Inbox.PROTOCOL, sms.getProtocolIdentifier());
values.put(Inbox.READ, 0);
values.put(Inbox.SEEN, 0);








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ClassZeroActivity.java b/src/com/android/mms/ui/ClassZeroActivity.java
//Synthetic comment -- index 260d72c..8961306 100644

//Synthetic comment -- @@ -183,10 +183,7 @@
ContentValues values = new ContentValues();

values.put(Inbox.ADDRESS, sms.getDisplayOriginatingAddress());
        values.put(Inbox.DATE, sms.getTimestampMillis());
values.put(Inbox.PROTOCOL, sms.getProtocolIdentifier());
values.put(Inbox.READ, Integer.valueOf(mRead ? 1 : 0));
values.put(Inbox.SEEN, Integer.valueOf(mRead ? 1 : 0));







