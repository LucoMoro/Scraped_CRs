/*Delete uncompleted concatenated message.

We consider the way to delete uncompleted concatenated Message.
There is 2 ways. Implement the following case in com.android.internal.telephony.SMSDispatcher.processMessagePart()
1. Newly received into the existing data space -Compare the stored data and the new data. a. if matched , the new data is the duplicated data, so it should be discarded. b. if not matched, the stored data is obsoleted, so the ALL stored data related the concatenation reference number should be deleted.
2. Newly received into the lacked space -Compare timestamp of the stored data and the one of the new data ( The timestamp is incremented by 1 according to the TS23.040. So, the stored segments are obsoleted if delta>=256 ,delta same "VALID_TIMER_CONCAT_SEGMENT" variable in source code) a. if the delta <256 then the New data is the valid segment, so it should be concatenated. b. if the delta >=256 then, the stored data is obsoleted, so the ALL stored data related the concatenation reference number should be deleted.
To discard the ALL sored data related the concatenation reference number, we use "discard" variable.
Signed-off-by: kyunga kim <kyunga1.kim@lge.com>

Change-Id:I255bd3b77d4600fe8c39d4fea09ecd8f39e1b2cf*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/SMSDispatcher.java b/src/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index 4a6c5dc..42654a1 100644

//Synthetic comment -- @@ -72,6 +72,8 @@
static final String TAG = "SMS";    // accessed from inner class
private static final String SEND_NEXT_MSG_EXTRA = "SendNextMsg";

    /** Default timeout for SMS sent query */
    private static final int DEFAULT_SMS_TIMEOUT = 6000;
/** Permission required to receive SMS and SMS-CB messages. */
public static final String RECEIVE_SMS_PERMISSION = "android.permission.RECEIVE_SMS";

//Synthetic comment -- @@ -95,9 +97,20 @@
"destination_port"
};

    /** Query projection for combining concatenated message segments. */
    private static final String[] PDU_SEQUENCE_PORT_DATE_PROJECTION = new String[] {
            "pdu",
            "sequence",
            "destination_port",
            "date"
    };

    /** Validity timer for concatinated SMS segment */
    private static final int VALID_TIMER_CONCAT_SEGMENT = 256000;
private static final int PDU_COLUMN = 0;
private static final int SEQUENCE_COLUMN = 1;
private static final int DESTINATION_PORT_COLUMN = 2;
    private static final int DATE_COLUMN = 3; //check validity time for concatenated SMS segment

/** New SMS received. */
protected static final int EVENT_NEW_SMS = 1;
//Synthetic comment -- @@ -540,6 +553,8 @@
byte[][] pdus = null;
Cursor cursor = null;
try {
	    // used by discard stored data related the concatenation reference number should be deleted.
            boolean discarded = false; 
// used by several query selection arguments
String refNumber = Integer.toString(referenceNumber);
String seqNumber = Integer.toString(sequenceNumber);
//Synthetic comment -- @@ -555,18 +570,47 @@
+ " refNumber=" + refNumber + " seqNumber=" + seqNumber);
String oldPduString = cursor.getString(PDU_COLUMN);
byte[] oldPdu = HexDump.hexStringToByteArray(oldPduString);
                /*if (!Arrays.equals(oldPdu, pdu)) {
Log.e(TAG, "Warning: dup message segment PDU of length " + pdu.length
+ " is different from existing PDU of length " + oldPdu.length);
}
                return Intents.RESULT_SMS_HANDLED;*/

		//if duplicated message segment PDU is same, discard it 
		if (Arrays.equals(oldPdu, pdu)) {
		    return Intents.RESULT_SMS_HANDLED;
		}
		//if duplicated message segment PDU is different, 
		//"discard" field is true.
		Log.e(TAG, "Warning: dup message segment PDU is different"
		    + " from existing PDU. OldLen " + oldPdu.length + ", NewLen" + pdu.length);
		discarded = true;
}
cursor.close();

// not a dup, query for all other segments of this concatenated message
            //String where = "address=? AND reference_number=?";
            //String[] whereArgs = new String[] {address, refNumber};
            //cursor = mResolver.query(mRawUri, PDU_SEQUENCE_PORT_PROJECTION, where, whereArgs, null);
            String where = "reference_number=?";
            String[] whereArgs = new String[] {refNumber};
            cursor = mResolver.query(mRawUri, PDU_SEQUENCE_PORT_DATE_PROJECTION, where, whereArgs, null);

            if (cursor.moveToNext()) {
                // moveToNext() returns false if no other segements
		//check the validity time of this segments message.
                long diffTime = Math.abs(timestamp - cursor.getLong(DATE_COLUMN));
                if (diffTime >= VALID_TIMER_CONCAT_SEGMENT) {
                    Log.e(TAG, "Warning: Existing segments is too old");
                    discarded = true;
                }
            }
	    //The stored message is obsoleted, 
	    //so the all stored message related the concatenation reference number should be deleted.
            if (discarded) {
                Log.e(TAG, "Warning: Old PDU is discarded");
                mResolver.delete(mRawUri, where, whereArgs);
            }

int cursorCount = cursor.getCount();
if (cursorCount != messageCount - 1) {







