/*Delete uncompleted concatenated message.

We consider the way to delete uncompleted concatenated Message.
There is 2 ways. Implement the following case in com.android.internal.telephony.SMSDispatcher.processMessagePart()
1. Newly received into the existing data space -Compare the stored data and the new data. a. if matched , the new data is the duplicated data, so it should be discarded. b. if not matched, the stored data is obsoleted, so the ALL stored data related the concatenation reference number should be deleted.
2. Newly received into the lacked space -Compare timestamp of the stored data and the one of the new data ( The timestamp is incremented by 1 according to the TS23.040. So, the stored segments are obsoleted if delta>=256 ,delta same "VALID_TIMER_CONCAT_SEGMENT" variable in source code) a. if the delta <256 then the New data is the valid segment, so it should be concatenated. b. if the delta >=256 then, the stored data is obsoleted, so the ALL stored data related the concatenation reference number should be deleted.
To discard the ALL sored data related the concatenation reference number, we use "discard" variable.
Signed-off-by: kyunga kim <kyunga1.kim@lge.com>

Change-Id:I255bd3b77d4600fe8c39d4fea09ecd8f39e1b2cf*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/SMSDispatcher.java b/src/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index 4a6c5dc..57b92dd 100644

//Synthetic comment -- @@ -72,6 +72,8 @@
static final String TAG = "SMS";    // accessed from inner class
private static final String SEND_NEXT_MSG_EXTRA = "SendNextMsg";

/** Permission required to receive SMS and SMS-CB messages. */
public static final String RECEIVE_SMS_PERMISSION = "android.permission.RECEIVE_SMS";

//Synthetic comment -- @@ -95,9 +97,20 @@
"destination_port"
};

private static final int PDU_COLUMN = 0;
private static final int SEQUENCE_COLUMN = 1;
private static final int DESTINATION_PORT_COLUMN = 2;

/** New SMS received. */
protected static final int EVENT_NEW_SMS = 1;
//Synthetic comment -- @@ -540,11 +553,13 @@
byte[][] pdus = null;
Cursor cursor = null;
try {
            // used by several query selection arguments
String refNumber = Integer.toString(referenceNumber);
String seqNumber = Integer.toString(sequenceNumber);

            // Check for duplicate message segment
cursor = mResolver.query(mRawUri, PDU_PROJECTION,
"address=? AND reference_number=? AND sequence=?",
new String[] {address, refNumber, seqNumber}, null);
//Synthetic comment -- @@ -555,18 +570,50 @@
+ " refNumber=" + refNumber + " seqNumber=" + seqNumber);
String oldPduString = cursor.getString(PDU_COLUMN);
byte[] oldPdu = HexDump.hexStringToByteArray(oldPduString);
                if (!Arrays.equals(oldPdu, pdu)) {
Log.e(TAG, "Warning: dup message segment PDU of length " + pdu.length
+ " is different from existing PDU of length " + oldPdu.length);
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







