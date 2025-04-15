/*Fix the delivery report error

Although TP-status is PENDING or FAILED, delivery report is displayed.
Only in case of SUCCESS, it should be displayed to users.

Change-Id:I00eeed61f8797ff1d468c0bf23a7f3431ed02faeSigned-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessageStatusReceiver.java b/src/com/android/mms/transaction/MessageStatusReceiver.java
//Synthetic comment -- index 73a4b18..c284cc0 100644

//Synthetic comment -- @@ -48,28 +48,29 @@
Uri messageUri = intent.getData();
byte[] pdu = (byte[]) intent.getExtra("pdu");

            SmsMessage message = updateMessageStatus(context, messageUri, pdu);

// Called on the UI thread so don't block.
            if (message.getStatus() < Sms.STATUS_PENDING)
                MessagingNotification.nonBlockingUpdateNewMessageIndicator(context,
                        true, message.isStatusReportMessage());
}
}

    private SmsMessage updateMessageStatus(Context context, Uri messageUri, byte[] pdu) {
// Create a "status/#" URL and use it to update the
// message's status in the database.
Cursor cursor = SqliteWrapper.query(context, context.getContentResolver(),
messageUri, ID_PROJECTION, null, null, null);
        SmsMessage message = SmsMessage.createFromPdu(pdu);

try {
if (cursor.moveToFirst()) {
int messageId = cursor.getInt(0);

Uri updateUri = ContentUris.withAppendedId(STATUS_URI, messageId);
int status = message.getStatus();
                boolean isStatusReport = message.isStatusReportMessage();
ContentValues contentValues = new ContentValues(1);

if (Log.isLoggable(LogTag.TAG, Log.DEBUG)) {
//Synthetic comment -- @@ -86,7 +87,7 @@
} finally {
cursor.close();
}
        return message;
}

private void error(String message) {







