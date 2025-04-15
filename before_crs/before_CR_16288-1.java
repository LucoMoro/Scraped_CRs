/*Mms: Delivery report should display recent message details.

On sending SMS to multiple recipients with delivery reports enabled,
the delivery reports for all the recipients display the details of first
recipient since the time stamps are same. To fix this, need to query in
the ascending order and display the details of last message which is the
most recent message.

Change-Id:I9917a7a3fd56daceccdc9fdc5f3cf1294c22c320*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessagingNotification.java b/src/com/android/mms/transaction/MessagingNotification.java
//Synthetic comment -- index e1e4db5..548f95d 100644

//Synthetic comment -- @@ -330,13 +330,13 @@
ContentResolver resolver = context.getContentResolver();
Cursor cursor = SqliteWrapper.query(context, resolver, Sms.CONTENT_URI,
SMS_STATUS_PROJECTION, NEW_DELIVERY_SM_CONSTRAINT,
                    null, Sms.DATE + " desc");

if (cursor == null)
return null;

try {
            if (!cursor.moveToFirst())
return null;

String address = cursor.getString(COLUMN_SMS_ADDRESS);







