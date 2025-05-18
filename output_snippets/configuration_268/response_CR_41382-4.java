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

String refNumber = Integer.toString(referenceNumber);
String seqNumber = Integer.toString(sequenceNumber);
String oldPduString = cursor.getString(PDU_COLUMN);
byte[] oldPdu = HexDump.hexStringToByteArray(oldPduString);
if (!Arrays.equals(oldPdu, pdu)) {
    Log.e(TAG, "Warning: dup message segment PDU of length " + pdu.length
            + " is different from existing PDU of length " + oldPdu.length);
    return Intents.RESULT_SMS_HANDLED;
}

String where = "address=? AND reference_number=? AND sequence=?";
String[] whereArgs = new String[] {address, refNumber, seqNumber};
cursor = mResolver.query(mRawUri, PDU_SEQUENCE_PORT_PROJECTION, where, whereArgs, null);

try {
    if (cursor != null && cursor.moveToFirst()) {
        int messageTimestamp = cursor.getInt(cursor.getColumnIndex("timestamp"));
        String existingPDU = cursor.getString(PDU_COLUMN);
        byte[] existingPduBytes = HexDump.hexStringToByteArray(existingPDU);
        if (messageTimestamp > 256 || !Arrays.equals(existingPduBytes, pdu)) {
            mResolver.delete(mRawUri, where, whereArgs);
        } else {
            int cursorCount = cursor.getCount();
            if (cursorCount != messageCount - 1) {
                Log.e(TAG, "Cursor count mismatch: expected " + (messageCount - 1) + ", found " + cursorCount);
                // Additional handling can be placed here (e.g., alerts or retries)
            }
        }
    } else {
        Log.e(TAG, "Cursor is null or empty, cannot process messages.");
    }
} finally {
    if (cursor != null) {
        cursor.close();
    }
}

//<End of snippet n. 0>