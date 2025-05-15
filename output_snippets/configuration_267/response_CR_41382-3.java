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
String oldPduString = cursor.getString(PDU_COLUMN);
byte[] oldPdu = HexDump.hexStringToByteArray(oldPduString);

long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
long currentTime = System.currentTimeMillis();
if (Math.abs(currentTime - timestamp) >= 256 || !Arrays.equals(oldPdu, pdu)) {
    Log.e(TAG, "Message segment PDU mismatch or timestamp delta >= 256");
    // Implement deletion logic for segments tied to referenceNumber
    String deleteWhere = "address=? AND reference_number=?";
    String[] deleteWhereArgs = new String[] {address, refNumber};
    mResolver.delete(mRawUri, deleteWhere, deleteWhereArgs);
    return Intents.RESULT_SMS_HANDLED;
}

return Intents.RESULT_SMS_HANDLED;
}
cursor.close();

// not a dup, query for all other segments of this concatenated message
String where = "address=? AND reference_number=?";
String[] whereArgs = new String[] {address, refNumber};
cursor = mResolver.query(mRawUri, PDU_SEQUENCE_PORT_PROJECTION, where, whereArgs, null);

int cursorCount = cursor.getCount();
if (cursorCount != messageCount - 1) {

//<End of snippet n. 0>