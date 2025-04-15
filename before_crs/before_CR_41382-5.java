/*Delete incomplete concatenated message.

Unnecessary Data in the Database may remain
when the concatenated msg is received incompletely.
After the new Concatenated Message is received,
there exists the possibility to occurs the problem.
Therefore Unnecessary Data should be deleted.

Unnecessary data conditions are as follows:
1. address, reference_number and sequence is same but pdu is not same.
2. Concatenated Message TimeStamp Value exceeds 256.
Above two cases, Unnecessary Data is deleted.

Signed-off-by: kyunga kim <kyunga1.kim@lge.com>
Change-Id:I255bd3b77d4600fe8c39d4fea09ecd8f39e1b2cf*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/SMSDispatcher.java b/src/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index 4a6c5dc..aa627f5 100644

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
//Synthetic comment -- @@ -540,6 +553,8 @@
byte[][] pdus = null;
Cursor cursor = null;
try {
// used by several query selection arguments
String refNumber = Integer.toString(referenceNumber);
String seqNumber = Integer.toString(sequenceNumber);
//Synthetic comment -- @@ -555,11 +570,32 @@
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

//Synthetic comment -- @@ -567,7 +603,6 @@
String where = "address=? AND reference_number=?";
String[] whereArgs = new String[] {address, refNumber};
cursor = mResolver.query(mRawUri, PDU_SEQUENCE_PORT_PROJECTION, where, whereArgs, null);

int cursorCount = cursor.getCount();
if (cursorCount != messageCount - 1) {
// We don't have all the parts yet, store this one away







