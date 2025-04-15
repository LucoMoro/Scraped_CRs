/*Fix the delivery report error

Although TP-status is PENDING or FAILED, delivery report is displayed.
Only in case of SUCCESS, it should be displayed to users.

Change-Id:I00eeed61f8797ff1d468c0bf23a7f3431ed02faeSigned-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessageStatusReceiver.java b/src/com/android/mms/transaction/MessageStatusReceiver.java
//Synthetic comment -- index 73a4b18..c284cc0 100644

//Synthetic comment -- @@ -48,28 +48,29 @@
Uri messageUri = intent.getData();
byte[] pdu = (byte[]) intent.getExtra("pdu");

            boolean isStatusMessage = updateMessageStatus(context, messageUri, pdu);

// Called on the UI thread so don't block.
            MessagingNotification.nonBlockingUpdateNewMessageIndicator(context,
                    true, isStatusMessage);
}
}

    private boolean updateMessageStatus(Context context, Uri messageUri, byte[] pdu) {
// Create a "status/#" URL and use it to update the
// message's status in the database.
        boolean isStatusReport = false;
Cursor cursor = SqliteWrapper.query(context, context.getContentResolver(),
messageUri, ID_PROJECTION, null, null, null);
try {
if (cursor.moveToFirst()) {
int messageId = cursor.getInt(0);

Uri updateUri = ContentUris.withAppendedId(STATUS_URI, messageId);
                SmsMessage message = SmsMessage.createFromPdu(pdu);
int status = message.getStatus();
                isStatusReport = message.isStatusReportMessage();
ContentValues contentValues = new ContentValues(1);

if (Log.isLoggable(LogTag.TAG, Log.DEBUG)) {
//Synthetic comment -- @@ -86,7 +87,7 @@
} finally {
cursor.close();
}
        return isStatusReport;
}

private void error(String message) {







