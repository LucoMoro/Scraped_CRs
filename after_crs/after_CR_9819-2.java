/*SMS status report notifications.*/




//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessageStatusReceiver.java b/src/com/android/mms/transaction/MessageStatusReceiver.java
//Synthetic comment -- index 73b4e16..720d982 100644

//Synthetic comment -- @@ -47,32 +47,43 @@
Uri messageUri = intent.getData();
byte[] pdu = (byte[]) intent.getExtra("pdu");

            SmsMessage message = SmsMessage.createFromPdu(pdu);
            if (message.isStatusReportMessage()) {
                Long messageId = getMessageId(context, messageUri);
                if (messageId == -1) {
                    error("Can't find message for status update: " + messageUri);
                } else {
                    updateMessageStatus(context, messageId, message);
                    MessagingNotification.showSmsDeliveryReportIndicator(context, messageId);
                }
            } else {  //  this should not happen
                MessagingNotification.updateNewMessageIndicator(context, true);
            }
}
}

    private void updateMessageStatus(Context context, Long messageId, SmsMessage message) {
// Create a "status/#" URL and use it to update the
// message's status in the database.
        Uri updateUri = ContentUris.withAppendedId(STATUS_URI, messageId);
        int status = message.getStatus();
        ContentValues contentValues = new ContentValues(1);

        contentValues.put(Sms.STATUS, status);
        SqliteWrapper.update(context, context.getContentResolver(),
                            updateUri, contentValues, null, null);
    }

    private Long getMessageId(Context context, Uri messageUri) {
        Long result = Long.valueOf(-1);
Cursor cursor = SqliteWrapper.query(context, context.getContentResolver(),
                messageUri, ID_PROJECTION, null, null, null);

if ((cursor != null) && cursor.moveToFirst()) {
            result = cursor.getLong(0);
cursor.close();
}
        return result;
}

private void error(String message) {








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessagingNotification.java b/src/com/android/mms/transaction/MessagingNotification.java
//Synthetic comment -- index fccbbd2..531a374 100644

//Synthetic comment -- @@ -79,7 +79,7 @@

// This must be consistent with the column constants below.
private static final String[] SMS_STATUS_PROJECTION = new String[] {
        Sms.THREAD_ID, Sms.DATE, Sms.ADDRESS, Sms.SUBJECT, Sms.BODY, Sms.STATUS };

// These must be consistent with MMS_STATUS_PROJECTION and
// SMS_STATUS_PROJECTION.
//Synthetic comment -- @@ -90,6 +90,7 @@
private static final int COLUMN_SUBJECT     = 3;
private static final int COLUMN_SUBJECT_CS  = 4;
private static final int COLUMN_SMS_BODY    = 4;
    private static final int COLUMN_SMS_STATUS  = 5;

private static final String NEW_INCOMING_SM_CONSTRAINT =
"(" + Sms.TYPE + " = " + Sms.MESSAGE_TYPE_INBOX
//Synthetic comment -- @@ -172,6 +173,13 @@
updateNewMessageIndicator(context);
}

    public static void showSmsDeliveryReportIndicator(Context context, Long messageId) {
        MmsSmsNotificationInfo info = getSmsDeliveryReportNotificationInfo(context, messageId);
        if (info != null) {
            info.deliver(context, true, 1, 1);
        }
    }

private static final int accumulateNotificationInfo(
SortedSet set, MmsSmsNotificationInfo info) {
if (info != null) {
//Synthetic comment -- @@ -299,6 +307,39 @@
}
}

    public static final MmsSmsNotificationInfo getSmsDeliveryReportNotificationInfo(
            Context context, Long messageId) {
        ContentResolver resolver = context.getContentResolver();
        String selection = "_id = " + messageId;

        Cursor cursor = SqliteWrapper.query(context, resolver, Sms.CONTENT_URI,
                            SMS_STATUS_PROJECTION, selection,
                            null, null);

        if (cursor == null) {
            return null;
        }

        try {
            if (!cursor.moveToFirst()) {
                return null;
            }

            String address = cursor.getString(COLUMN_SMS_ADDRESS);
            String body = getSmsStatusText(context, cursor.getInt(COLUMN_SMS_STATUS));
            long threadId = cursor.getLong(COLUMN_THREAD_ID);
            long timeMillis = cursor.getLong(COLUMN_DATE);

            MmsSmsNotificationInfo info = getNewMessageNotificationInfo(
                    address, body, context, R.drawable.stat_notify_sms,
                    null, threadId, timeMillis, cursor.getCount());

            return info;
        } finally {
            cursor.close();
        }
    }

private static final MmsSmsNotificationInfo getNewMessageNotificationInfo(
String address,
String body,
//Synthetic comment -- @@ -606,4 +647,20 @@
cancelNotification(context, DOWNLOAD_FAILED_NOTIFICATION_ID);
}
}

    private static String getSmsStatusText(Context context, int status) {
        if (status == Sms.STATUS_NONE) {
            // No delivery report requested
            return context.getString(R.string.status_none);
        } else if (status >= Sms.STATUS_FAILED) {
            // Failure
            return context.getString(R.string.status_failed);
        } else if (status >= Sms.STATUS_PENDING) {
            // Pending
            return context.getString(R.string.status_pending);
        } else {
            // Success
            return context.getString(R.string.status_received);
        }
    }
}







