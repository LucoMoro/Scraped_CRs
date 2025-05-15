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
    
    String where = "address=? AND reference_number=?";
    String[] whereArgs = new String[] {address, refNumber};
    cursor = mResolver.query(mRawUri, PDU_SEQUENCE_PORT_PROJECTION, where, whereArgs, null);

    if (cursor != null && cursor.moveToFirst()) {
        String oldPduString = cursor.getString(PDU_COLUMN);
        byte[] oldPdu = HexDump.hexStringToByteArray(oldPduString);
        if (!Arrays.equals(oldPdu, pdu)) {
            Log.e(TAG, "Warning: dup message segment PDU of length " + pdu.length
                    + " is different from existing PDU of length " + oldPdu.length);
            deleteEntry(address, refNumber, seqNumber); // Implemented deletion logic for mismatched PDU
        }
    }
    
    if (currentTimestamp - concatenatedMessageTimestamp >= 256) {
        deleteEntry(address, refNumber, seqNumber); // Implemented deletion logic for timestamp
    }
    
    return Intents.RESULT_SMS_HANDLED;
} catch (Exception e) {
    Log.e(TAG, "Error processing SMS: " + e.getMessage());
} finally {
    if (cursor != null) {
        cursor.close(); // Ensure cursor closure in all scenarios
    }
}

private void deleteEntry(String address, String refNumber, String seqNumber) {
    String where = "address=? AND reference_number=? AND sequence=?";
    String[] whereArgs = new String[] {address, refNumber, seqNumber};
    try {
        mResolver.delete(mRawUri, where, whereArgs);
    } catch (Exception e) {
        Log.e(TAG, "Failed to delete entry: " + e.getMessage());
    }
}

//<End of snippet n. 0>