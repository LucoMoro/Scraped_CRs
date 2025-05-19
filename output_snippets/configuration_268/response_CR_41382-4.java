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
    // used by several query selection arguments
    String refNumber = Integer.toString(referenceNumber);
    String seqNumber = Integer.toString(sequenceNumber);
    Log.d(TAG, "refNumber=" + refNumber + " seqNumber=" + seqNumber);

    cursor = mResolver.query(mRawUri, PDU_SEQUENCE_PORT_PROJECTION, "address=? AND reference_number=?", new String[]{address, refNumber}, null);
    if (cursor == null) {
        Log.e(TAG, "Cursor is null after querying.");
        return Intents.RESULT_SMS_NOT_HANDLED;
    }
    
    if (!cursor.moveToFirst()) {
        Log.e(TAG, "Cursor is empty after querying.");
        return Intents.RESULT_SMS_NOT_HANDLED;
    }
    
    String oldPduString = cursor.getString(PDU_COLUMN);
    byte[] oldPdu = HexDump.hexStringToByteArray(oldPduString);
    if (!Arrays.equals(oldPdu, pdu)) {
        Log.e(TAG, "Warning: dup message segment PDU of length " + pdu.length
                + " is different from existing PDU of length " + oldPdu.length);
    }
    
    long currentTimeStamp = System.currentTimeMillis(); // assume timestamp retrieval
    if (currentTimeStamp - messageTimestamp > 256) {
        // deletion logic
        String deleteWhere = "address=? AND reference_number=? AND sequence=? AND pdu<>?";
        String[] deleteArgs = new String[]{address, refNumber, seqNumber, oldPduString};
        
        mDatabase.beginTransaction();
        try {
            int deletedRows = mDatabase.delete(mRawUri, deleteWhere, deleteArgs);
            Log.d(TAG, "Deleted rows: " + deletedRows);
            mDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error during deletion: " + e.getMessage());
            // Implement rollback on error
        } finally {
            mDatabase.endTransaction();
        }
    }

    return Intents.RESULT_SMS_HANDLED;
} catch (Exception e) {
    Log.e(TAG, "Error handling SMS: " + e.getMessage());
} finally {
    if (cursor != null && !cursor.isClosed()) {
        cursor.close();
    }
}

String where = "address=? AND reference_number=?";
String[] whereArgs = new String[]{address, refNumber};
cursor = mResolver.query(mRawUri, PDU_SEQUENCE_PORT_PROJECTION, where, whereArgs, null);
if (cursor != null) {
    int cursorCount = cursor.getCount();
    if (cursorCount != messageCount - 1) {
        // We don't have all the parts yet, store this one away
    }
}

//<End of snippet n. 0>