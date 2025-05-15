
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
            boolean discard = false;
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
                discard = true;
            }
            cursor.close();

            // Check the validity time of this segments message.
            cursor = mResolver.query(mRawUri,PDU_SEQUENCE_PORT_DATE_PROJECTION,
                    "reference_number=?", new String[] {refNumber}, null);
            if (cursor.moveToNext()) {
                // There is a next segment, validate its not to old.
                long diffTime = Math.abs(timestamp - cursor.getLong(DATE_COLUMN));
                if (diffTime >= MAX_CONCATENATED_SEGMENT_TIME) {
                    Log.w(TAG, "Warning: Existing segments is too old");
                    discard = true;
                }
             }
            if (discard) {
                Log.w(TAG, "Discard PDU");
                mResolver.delete(mRawUri, "reference_number=?", 
                        new String[] {refNumber});
}
cursor.close();

String where = "address=? AND reference_number=?";
String[] whereArgs = new String[] {address, refNumber};
cursor = mResolver.query(mRawUri, PDU_SEQUENCE_PORT_PROJECTION, where, whereArgs, null);
int cursorCount = cursor.getCount();
if (cursorCount != messageCount - 1) {
// We don't have all the parts yet, store this one away

//<End of snippet n. 0>








