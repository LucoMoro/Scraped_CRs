//<Beginning of snippet n. 0>
static final String TAG = "SMS";    // accessed from inner class
private static final String SEND_NEXT_MSG_EXTRA = "SendNextMsg";

/** Permission required to receive SMS and SMS-CB messages. */
public static final String RECEIVE_SMS_PERMISSION = "android.permission.RECEIVE_SMS";

private static final int PDU_COLUMN = 0;
private static final int SEQUENCE_COLUMN = 1;
private static final int DESTINATION_PORT_COLUMN = 2;

/** New SMS received. */
protected static final int EVENT_NEW_SMS = 1;
byte[][] pdus = null;
Cursor cursor = null;

try {
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
            deleteDataIfNecessary(cursor, pdu, referenceNumber, sequenceNumber);
        }
    }
} catch (Exception e) {
    Log.e(TAG, "Error processing SMS: " + e.getMessage());
} finally {
    if (cursor != null) {
        cursor.close();
    }
}

private void deleteDataIfNecessary(Cursor cursor, byte[] pdu, int referenceNumber, int sequenceNumber) {
    long timestamp = System.currentTimeMillis();  // Assuming you need to add timestamp logic
    if (timestamp > 256) {
        String whereDelete = "address=? AND reference_number=? AND sequence=?";
        String[] whereArgsDelete = new String[] {address, Integer.toString(referenceNumber), Integer.toString(sequenceNumber)};
        mResolver.delete(mRawUri, whereDelete, whereArgsDelete);
    }
}
//<End of snippet n. 0>