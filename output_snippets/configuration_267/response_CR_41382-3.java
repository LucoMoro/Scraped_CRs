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
SQLiteDatabase db = null;

try {
    String refNumber = Integer.toString(referenceNumber);
    String seqNumber = Integer.toString(sequenceNumber);
    String oldPduString = cursor.getString(PDU_COLUMN);
    byte[] oldPdu = HexDump.hexStringToByteArray(oldPduString);
    
    if (!Arrays.equals(oldPdu, pdu)) {
        Log.e(TAG, "Warning: dup message segment PDU of length " + pdu.length
                + " is different from existing PDU of length " + oldPdu.length);

        String where = "address=? AND reference_number=?";
        String[] whereArgs = new String[] { address, refNumber };
        cursor = mResolver.query(mRawUri, PDU_SEQUENCE_PORT_PROJECTION, where, whereArgs, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            db = mResolver.acquireDatabase(); // Acquire database for transaction
            db.beginTransaction();
            try {
                do {
                    long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
                    if (System.currentTimeMillis() - timestamp >= MAX_CONCATENATED_SEGMENT_TIME) {
                        // Delete all matching segments
                        mResolver.delete(mRawUri, where, whereArgs);
                    }
                } while (cursor.moveToNext());
                db.setTransactionSuccessful();
            } catch (SQLException e) {
                Log.e(TAG, "SQL error during deletion", e);
            } catch (Exception e) {
                Log.e(TAG, "Error during deletion", e);
            } finally {
                db.endTransaction();
            }
        }

        if (cursor != null) {
            cursor.close();
        }
    }
    
    return Intents.RESULT_SMS_HANDLED;

} catch (Exception e) {
    Log.e(TAG, "Error processing SMS", e);
    if (cursor != null) {
        cursor.close();
    }
}

//<End of snippet n. 0>