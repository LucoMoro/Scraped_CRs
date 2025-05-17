//<Beginning of snippet n. 0>

static final String TAG = "SMS";    
private static final String SEND_NEXT_MSG_EXTRA = "SendNextMsg";
public static final String RECEIVE_SMS_PERMISSION = "android.permission.RECEIVE_SMS";
private static final int PDU_COLUMN = 0;
private static final int SEQUENCE_COLUMN = 1;
private static final int DESTINATION_PORT_COLUMN = 2;
protected static final int EVENT_NEW_SMS = 1;
byte[][] pdus = null;

try (Cursor cursor = mResolver.query(mRawUri, PDU_SEQUENCE_PORT_PROJECTION, 
    "address=? AND reference_number=? AND sequence=?", 
    new String[]{address, Integer.toString(referenceNumber), Integer.toString(sequenceNumber)})) {

    if (cursor != null && cursor.moveToFirst()) {
        String oldPduString = cursor.getString(PDU_COLUMN);
        byte[] oldPdu = HexDump.hexStringToByteArray(oldPduString);
        
        boolean shouldDelete = false;

        if (!Arrays.equals(oldPdu, pdu)) {
            Log.e(TAG, "Warning: dup message segment PDU of length " + pdu.length
                    + " is different from existing PDU of length " + oldPdu.length);
            shouldDelete = true;
        }

        long timestamp = getTimestampFromPDU(pdu); 
        if (timestamp > 256) {
            shouldDelete = true;
        }

        if (shouldDelete) {
            String deleteWhere = "address=? AND reference_number=? AND sequence=?";
            String[] deleteWhereArgs = new String[]{address, Integer.toString(referenceNumber), Integer.toString(sequenceNumber)};
            mResolver.delete(mRawUri, deleteWhere, deleteWhereArgs);
        }
    }

    return Intents.RESULT_SMS_HANDLED;
} catch (Exception e) {
    Log.e(TAG, "Database operation failed: " + e.getMessage(), e);
}

//<End of snippet n. 0>