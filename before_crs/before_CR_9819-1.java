/*SMS status report notifications.*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessageStatusReceiver.java b/src/com/android/mms/transaction/MessageStatusReceiver.java
//Synthetic comment -- index d51a92b..4c3f9ae 100644

//Synthetic comment -- @@ -47,32 +47,43 @@
Uri messageUri = intent.getData();
byte[] pdu = (byte[]) intent.getExtra("pdu");

            updateMessageStatus(context, messageUri, pdu);
            MessagingNotification.updateNewMessageIndicator(context, true);
}
}

    private void updateMessageStatus(Context context, Uri messageUri, byte[] pdu) {
// Create a "status/#" URL and use it to update the
// message's status in the database.
Cursor cursor = SqliteWrapper.query(context, context.getContentResolver(),
                            messageUri, ID_PROJECTION, null, null, null);
if ((cursor != null) && cursor.moveToFirst()) {
            int messageId = cursor.getInt(0);

cursor.close();

            Uri updateUri = ContentUris.withAppendedId(STATUS_URI, messageId);
            SmsMessage message = SmsMessage.createFromPdu(pdu);
            int status = message.getStatus();
            ContentValues contentValues = new ContentValues(1);

            contentValues.put(Sms.STATUS, status);
            SqliteWrapper.update(context, context.getContentResolver(),
                                updateUri, contentValues, null, null);
        } else {
            error("Can't find message for status update: " + messageUri);
}
}

private void error(String message) {








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessagingNotification.java b/src/com/android/mms/transaction/MessagingNotification.java
//Synthetic comment -- index fccbbd2..531a374 100644

//Synthetic comment -- @@ -79,7 +79,7 @@

// This must be consistent with the column constants below.
private static final String[] SMS_STATUS_PROJECTION = new String[] {
        Sms.THREAD_ID, Sms.DATE, Sms.ADDRESS, Sms.SUBJECT, Sms.BODY };

// These must be consistent with MMS_STATUS_PROJECTION and
// SMS_STATUS_PROJECTION.
//Synthetic comment -- @@ -90,6 +90,7 @@
private static final int COLUMN_SUBJECT     = 3;
private static final int COLUMN_SUBJECT_CS  = 4;
private static final int COLUMN_SMS_BODY    = 4;

private static final String NEW_INCOMING_SM_CONSTRAINT =
"(" + Sms.TYPE + " = " + Sms.MESSAGE_TYPE_INBOX
//Synthetic comment -- @@ -172,6 +173,13 @@
updateNewMessageIndicator(context);
}

private static final int accumulateNotificationInfo(
SortedSet set, MmsSmsNotificationInfo info) {
if (info != null) {
//Synthetic comment -- @@ -299,6 +307,39 @@
}
}

private static final MmsSmsNotificationInfo getNewMessageNotificationInfo(
String address,
String body,
//Synthetic comment -- @@ -606,4 +647,20 @@
cancelNotification(context, DOWNLOAD_FAILED_NOTIFICATION_ID);
}
}
}







