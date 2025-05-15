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
Log.d(TAG, "refNumber=" + refNumber + " seqNumber=" + seqNumber);
String oldPduString = cursor.getString(PDU_COLUMN);
byte[] oldPdu = HexDump.hexStringToByteArray(oldPduString);
if (!Arrays.equals(oldPdu, pdu)) {
    Log.e(TAG, "Warning: dup message segment PDU of length " + pdu.length
            + " is different from existing PDU of length " + oldPdu.length);
    deleteStoredData(refNumber);
}
long oldTimestamp = cursor.getLong(cursor.getColumnIndex("timestamp")); // Assuming a timestamp column exists
long newTimestamp = System.currentTimeMillis();
if (Math.abs(newTimestamp - oldTimestamp) >= 256) {
    deleteStoredData(refNumber);
}
return Intents.RESULT_SMS_HANDLED;
} catch (Exception e) {
    Log.e(TAG, "Error handling SMS", e);
} finally {
    if (cursor != null) {
        cursor.close();
    }
}

// not a dup, query for all other segments of this concatenated message
String where = "address=? AND reference_number=?";
String[] whereArgs = new String[] {address, refNumber};
cursor = mResolver.query(mRawUri, PDU_SEQUENCE_PORT_PROJECTION, where, whereArgs, null);

int cursorCount = cursor.getCount();
if (cursorCount != messageCount - 1) {
    deleteStoredData(refNumber);
}
//<End of snippet n. 0>