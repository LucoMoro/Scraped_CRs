//<Beginning of snippet n. 0>

static final String TAG = "SMS";    // accessed from inner class
private static final String SEND_NEXT_MSG_EXTRA = "SendNextMsg";

/** Permission required to receive SMS and SMS-CB messages. */
public static final String RECEIVE_SMS_PERMISSION = "android.permission.RECEIVE_SMS";

"destination_port"
};

private static final int PDU_COLUMN = 0;
private static final int SEQUENCE_COLUMN = 1;
private static final int DESTINATION_PORT_COLUMN = 2;

/** New SMS received. */
protected static final int EVENT_NEW_SMS = 1;
byte[][] pdus = null;
Cursor cursor = null;

try {
    // used by several query selection arguments
    String refNumber = Integer.toString(referenceNumber);
    String seqNumber = Integer.toString(sequenceNumber);
    Log.d(TAG, "Processing SMS: refNumber=" + refNumber + " seqNumber=" + seqNumber);
    String oldPduString = cursor.getString(PDU_COLUMN);
    byte[] oldPdu = HexDump.hexStringToByteArray(oldPduString);

    if (!Arrays.equals(oldPdu, pdu)) {
        Log.e(TAG, "Warning: dup message segment PDU of length " + pdu.length
                + " is different from existing PDU of length " + oldPdu.length);

        // Deletion logic for data mismatch
        String deleteWhere = "reference_number=?";
        String[] deleteArgs = new String[]{refNumber};
        mResolver.delete(mRawUri, deleteWhere, deleteArgs);
    }

    // Timestamp comparison logic
    long oldTimestamp = cursor.getLong(cursor.getColumnIndex("timestamp")); // Assuming "timestamp" column exists
    long currentTimestamp = System.currentTimeMillis(); // Example: get current time in millis
    if (Math.abs(currentTimestamp - oldTimestamp) > 256) {
        String deleteWhere = "reference_number=?";
        String[] deleteArgs = new String[]{refNumber};
        mResolver.delete(mRawUri, deleteWhere, deleteArgs);
    }

    return Intents.RESULT_SMS_HANDLED;
} catch (Exception e) {
    Log.e(TAG, "Error processing SMS: " + e.getMessage());
} finally {
    if (cursor != null) {
        cursor.close();
    }
}

String where = "address=? AND reference_number=?";
String[] whereArgs = new String[]{address, refNumber};
cursor = mResolver.query(mRawUri, PDU_SEQUENCE_PORT_PROJECTION, where, whereArgs, null);

int cursorCount = cursor.getCount();
if (cursorCount != messageCount - 1) {
    // We don't have all the parts yet, store this one away

//<End of snippet n. 0>