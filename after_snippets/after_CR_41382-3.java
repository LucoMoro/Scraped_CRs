
//<Beginning of snippet n. 0>


static final String TAG = "SMS";    // accessed from inner class
private static final String SEND_NEXT_MSG_EXTRA = "SendNextMsg";

    /** Default timeout for SMS sent query */
    private static final int DEFAULT_SMS_TIMEOUT = 6000;
/** Permission required to receive SMS and SMS-CB messages. */
public static final String RECEIVE_SMS_PERMISSION = "android.permission.RECEIVE_SMS";

"destination_port"
};

    /** Query projection for combining concatenated message segments. */
    private static final String[] PDU_SEQUENCE_PORT_DATE_PROJECTION = new String[] {
            "pdu",
            "sequence",
            "destination_port",
            "date"
    };

    /** Max time value between concatenated SMS segments */
    private static final int MAX_CONCATENATED_SEGMENT_TIME = 256000;
private static final int PDU_COLUMN = 0;
private static final int SEQUENCE_COLUMN = 1;
private static final int DESTINATION_PORT_COLUMN = 2;
    private static final int DATE_COLUMN = 3;

/** New SMS received. */
protected static final int EVENT_NEW_SMS = 1;
byte[][] pdus = null;
Cursor cursor = null;
try {
             // When true the related concatenated data will be discarded.
            boolean discarded = false;
// used by several query selection arguments
String refNumber = Integer.toString(referenceNumber);
String seqNumber = Integer.toString(sequenceNumber);
+ " refNumber=" + refNumber + " seqNumber=" + seqNumber);
String oldPduString = cursor.getString(PDU_COLUMN);
byte[] oldPdu = HexDump.hexStringToByteArray(oldPduString);

               // Discard duplicated PDU segments.
                if (Arrays.equals(oldPdu, pdu)) {
                    return Intents.RESULT_SMS_HANDLED;
}
                Log.w(TAG, "Warning: dup message segment PDU is different"
               + " from existing PDU. OldLen " + oldPdu.length + ", NewLen" + pdu.length);
               discarded = true;
}
cursor.close();

// not a dup, query for all other segments of this concatenated message
            String where = "reference_number=?";
            String[] whereArgs = new String[] {refNumber};
            cursor = mResolver.query(mRawUri,PDU_SEQUENCE_PORT_DATE_PROJECTION,
                                    where,whereArgs,null);
            if (cursor.moveToNext()) {
                // moveToNext() returns false if no other segements
               // Check the validity time of this segments message.
                long diffTime = Math.abs(timestamp - cursor.getLong(DATE_COLUMN));
                if (diffTime >= MAX_CONCATENATED_SEGMENT_TIME) {
                    Log.w(TAG, "Warning: Existing segments is too old");
                    discarded = true;
                }
             }
            if (discarded) {
                Log.w(TAG, "Discard PDU");
                mResolver.delete(mRawUri, where, whereArgs);
            }

int cursorCount = cursor.getCount();
if (cursorCount != messageCount - 1) {

//<End of snippet n. 0>








