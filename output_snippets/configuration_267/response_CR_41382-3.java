//<Beginning of snippet n. 0>

static final String TAG = "SMS";    // accessed from inner class
private static final String SEND_NEXT_MSG_EXTRA = "SendNextMsg";

/** Permission required to receive SMS and SMS-CB messages. */
public static final String RECEIVE_SMS_PERMISSION = "android.permission.RECEIVE_SMS";

private static final int PDU_COLUMN = 0;
private static final int SEQUENCE_COLUMN = 1;
private static final int DESTINATION_PORT_COLUMN = 2;
private static final int MAX_CONCATENATED_SEGMENT_TIME = 256;

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
    
    long storedTimestamp = cursor.getLong(cursor.getColumnIndex("timestamp")); // Retrieve stored message timestamp
    if (!Arrays.equals(oldPdu, pdu)) {
        Log.e(TAG, "Warning: dup message segment PDU of length " + pdu.length +
                " is different from existing PDU of length " + oldPdu.length);
        
        // Delete all stored data related to the reference number
        String deleteWhere = "address=? AND reference_number=?";
        String[] deleteWhereArgs = new String[] {address, refNumber};
        mResolver.delete(mRawUri, deleteWhere, deleteWhereArgs);
    } else {
        long timestampDifference = System.currentTimeMillis() - storedTimestamp;
        if (timestampDifference >= MAX_CONCATENATED_SEGMENT_TIME) {
            String deleteWhere = "address=? AND reference_number=?";
            String[] deleteWhereArgs = new String[] {address, refNumber};
            mResolver.delete(mRawUri, deleteWhere, deleteWhereArgs);
        }
    }
    return Intents.RESULT_SMS_HANDLED;
} catch (SQLException e) {
    Log.e(TAG, "SQL Error processing SMS: " + e.getMessage());
} catch (NullPointerException e) {
    Log.e(TAG, "Null Pointer Error processing SMS: " + e.getMessage());
} catch (Exception e) {
    Log.e(TAG, "Error processing SMS: " + e.getMessage());
} finally {
    if (cursor != null && !cursor.isClosed()) {
        cursor.close();
    }
}

// not a dup, query for all other segments of this concatenated message
String where = "address=? AND reference_number=?";
String[] whereArgs = new String[] {address, refNumber};
cursor = mResolver.query(mRawUri, PDU_SEQUENCE_PORT_PROJECTION, where, whereArgs, null);

if (cursor != null) {
    int cursorCount = cursor.getCount();
    if (cursorCount != messageCount - 1) {
        Log.e(TAG, "Cursor count does not match message count. Expected: " + (messageCount - 1) + ", Found: " + cursorCount);
        // Implement corrective measures or logging here
    }
}

//<End of snippet n. 0>