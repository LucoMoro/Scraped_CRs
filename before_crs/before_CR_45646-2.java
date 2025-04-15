/*Telephony: Add bc type field to RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS

New type field is added to distinguish between different types
of broadcast to eliminate the dependence on pdu length for type
distinction.

Change-Id:I3f29b54f2df3f6d3daadf30f53cb2a04af2962d4*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 6131792..ace1d2e 100755

//Synthetic comment -- @@ -49,6 +49,7 @@
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.IccCardApplicationStatus;
//Synthetic comment -- @@ -2464,7 +2465,7 @@
case RIL_UNSOL_RESTRICTED_STATE_CHANGED: ret = responseInts(p); break;
case RIL_UNSOL_RESPONSE_SIM_STATUS_CHANGED:  ret =  responseVoid(p); break;
case RIL_UNSOL_RESPONSE_CDMA_NEW_SMS:  ret =  responseCdmaSms(p); break;
            case RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS:  ret =  responseRaw(p); break;
case RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL:  ret =  responseVoid(p); break;
case RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_UNSOL_CDMA_CALL_WAITING: ret = responseCdmaCallWaiting(p); break;
//Synthetic comment -- @@ -2918,6 +2919,15 @@
}

private Object
responseString(Parcel p) {
String response;









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/src/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 9295773..56eab93 100644

//Synthetic comment -- @@ -375,9 +375,11 @@
*/
private void handleBroadcastSms(AsyncResult ar) {
try {
            byte[] receivedPdu = (byte[])ar.result;

            if (false) {
for (int i = 0; i < receivedPdu.length; i += 8) {
StringBuilder sb = new StringBuilder("SMS CB pdu data: ");
for (int j = i; j < i + 8 && j < receivedPdu.length; j++) {
//Synthetic comment -- @@ -391,7 +393,7 @@
}
}

            SmsCbHeader header = new SmsCbHeader(receivedPdu);
String plmn = SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_NUMERIC);
int lac = -1;
int cid = -1;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmSmsCbMessage.java b/src/java/com/android/internal/telephony/gsm/GsmSmsCbMessage.java
//Synthetic comment -- index 78590fb..9511270 100644

//Synthetic comment -- @@ -27,10 +27,30 @@

/**
* Parses a GSM or UMTS format SMS-CB message into an {@link SmsCbMessage} object. The class is
 * public because {@link #createSmsCbMessage(SmsCbLocation, byte[][])} is used by some test cases.
*/
public class GsmSmsCbMessage {

/**
* Languages in the 0000xxxx DCS group as defined in 3GPP TS 23.038, section 5.
*/
//Synthetic comment -- @@ -51,9 +71,61 @@

private static final int PDU_BODY_PAGE_LENGTH = 82;

    /** Utility class with only static methods. */
private GsmSmsCbMessage() { }

/**
* Create a new SmsCbMessage object from a header object plus one or more received PDUs.
*
//Synthetic comment -- @@ -91,10 +163,38 @@
*
* @param location the location (geographical scope) for the message
* @param pdus PDU bytes
*/
public static SmsCbMessage createSmsCbMessage(SmsCbLocation location, byte[][] pdus)
throws IllegalArgumentException {
        SmsCbHeader header = new SmsCbHeader(pdus[0]);
return createSmsCbMessage(header, location, pdus);
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SmsCbHeader.java b/src/java/com/android/internal/telephony/gsm/SmsCbHeader.java
//Synthetic comment -- index 5692044..8169da2 100644

//Synthetic comment -- @@ -61,12 +61,12 @@
/**
* Length of GSM pdus
*/
    private static final int PDU_LENGTH_GSM = 88;

/**
* Maximum length of ETWS primary message GSM pdus
*/
    private static final int PDU_LENGTH_ETWS = 56;

private final int geographicalScope;

//Synthetic comment -- @@ -90,12 +90,16 @@
/** CMAS warning notification info. */
private final SmsCbCmasInfo mCmasInfo;

    public SmsCbHeader(byte[] pdu) throws IllegalArgumentException {
if (pdu == null || pdu.length < PDU_HEADER_LENGTH) {
throw new IllegalArgumentException("Illegal PDU");
}

        if (pdu.length <= PDU_LENGTH_ETWS) {
format = FORMAT_ETWS_PRIMARY;
geographicalScope = (pdu[0] & 0xc0) >> 6;
serialNumber = ((pdu[0] & 0xff) << 8) | (pdu[1] & 0xff);
//Synthetic comment -- @@ -117,7 +121,7 @@
warningSecurityInfo);
mCmasInfo = null;
return;     // skip the ETWS/CMAS initialization code for regular notifications
        } else if (pdu.length <= PDU_LENGTH_GSM) {
// GSM pdus are no more than 88 bytes
format = FORMAT_GSM;
geographicalScope = (pdu[0] & 0xc0) >> 6;








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/gsm/GsmSmsCbTest.java b/tests/telephonytests/src/com/android/internal/telephony/gsm/GsmSmsCbTest.java
//Synthetic comment -- index 82c6944..85a417a 100644

//Synthetic comment -- @@ -35,12 +35,11 @@

private static final SmsCbLocation sTestLocation = new SmsCbLocation("94040", 1234, 5678);

    private static SmsCbMessage createFromPdu(byte[] pdu) {
try {
            SmsCbHeader header = new SmsCbHeader(pdu);
byte[][] pdus = new byte[1][];
pdus[0] = pdu;
            return GsmSmsCbMessage.createSmsCbMessage(header, sTestLocation, pdus);
} catch (IllegalArgumentException e) {
return null;
}
//Synthetic comment -- @@ -48,26 +47,26 @@

private static void doTestGeographicalScopeValue(byte[] pdu, byte b, int expectedGs) {
pdu[0] = b;
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected geographical scope decoded", expectedGs, msg
.getGeographicalScope());
}

public void testCreateNullPdu() {
        SmsCbMessage msg = createFromPdu(null);
assertNull("createFromPdu(byte[] with null pdu should return null", msg);
}

public void testCreateTooShortPdu() {
byte[] pdu = new byte[4];
        SmsCbMessage msg = createFromPdu(pdu);

assertNull("createFromPdu(byte[] with too short pdu should return null", msg);
}

public void testGetGeographicalScope() {
        byte[] pdu = {
(byte)0xC0, (byte)0x00, (byte)0x00, (byte)0x32, (byte)0x40, (byte)0x11, (byte)0x41,
(byte)0xD0, (byte)0x71, (byte)0xDA, (byte)0x04, (byte)0x91, (byte)0xCB, (byte)0xE6,
(byte)0x70, (byte)0x9D, (byte)0x4D, (byte)0x07, (byte)0x85, (byte)0xD9, (byte)0x70,
//Synthetic comment -- @@ -91,7 +90,7 @@
}

public void testGetGeographicalScopeUmts() {
        byte[] pdu = {
(byte)0x01, (byte)0x00, (byte)0x32, (byte)0xC0, (byte)0x00, (byte)0x40,

(byte)0x01,
//Synthetic comment -- @@ -114,7 +113,7 @@
(byte)0x34
};

        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected geographical scope decoded",
SmsCbMessage.GEOGRAPHICAL_SCOPE_CELL_WIDE, msg.getGeographicalScope());
//Synthetic comment -- @@ -136,7 +135,7 @@
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00
};
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected 7-bit string decoded",
"A GSM default alphabet message with carriage return padding",
//Synthetic comment -- @@ -166,7 +165,7 @@

(byte)0x34
};
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected 7-bit string decoded",
"A GSM default alphabet message with carriage return padding",
//Synthetic comment -- @@ -213,7 +212,7 @@

(byte)0x0A
};
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected multipage 7-bit string decoded",
"First page+Second page",
//Synthetic comment -- @@ -236,7 +235,7 @@
(byte)0x90, (byte)0xFB, (byte)0x0D, (byte)0x82, (byte)0x87, (byte)0xC9, (byte)0xE4,
(byte)0xB4, (byte)0xFB, (byte)0x1C, (byte)0x02
};
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals(
"Unexpected 7-bit string decoded",
//Synthetic comment -- @@ -268,7 +267,7 @@

(byte)0x52
};
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals(
"Unexpected 7-bit string decoded",
//Synthetic comment -- @@ -293,7 +292,7 @@
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00
};
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected 7-bit string decoded",
"A GSM default alphabet message with carriage return padding",
//Synthetic comment -- @@ -318,7 +317,7 @@
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00
};
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected 7-bit string decoded",
"A GSM default alphabet message with carriage return padding",
//Synthetic comment -- @@ -350,7 +349,7 @@

(byte)0x37
};
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected 7-bit string decoded",
"A GSM default alphabet message with carriage return padding",
//Synthetic comment -- @@ -375,7 +374,7 @@
(byte)0x42, (byte)0x43, (byte)0x44, (byte)0x45, (byte)0x46, (byte)0x47, (byte)0x41,
(byte)0x42, (byte)0x43, (byte)0x44, (byte)0x45
};
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("8-bit message body should be empty", "", msg.getMessageBody());
}
//Synthetic comment -- @@ -396,7 +395,7 @@
(byte)0x63, (byte)0x00, (byte)0x74, (byte)0x00, (byte)0x65, (byte)0x00, (byte)0x72,
(byte)0x00, (byte)0x0D, (byte)0x00, (byte)0x0D
};
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected 7-bit string decoded",
"A UCS2 message containing a \u0434 character", msg.getMessageBody());
//Synthetic comment -- @@ -425,7 +424,7 @@

(byte)0x4E
};
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected 7-bit string decoded",
"A UCS2 message containing a \u0434 character", msg.getMessageBody());
//Synthetic comment -- @@ -471,7 +470,7 @@

(byte)0x06
};
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected multipage UCS2 string decoded",
"AAABBB", msg.getMessageBody());
//Synthetic comment -- @@ -493,7 +492,7 @@
(byte)0x61, (byte)0x00, (byte)0x63, (byte)0x00, (byte)0x74, (byte)0x00, (byte)0x65,
(byte)0x00, (byte)0x72, (byte)0x00, (byte)0x0D
};
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected 7-bit string decoded",
"A UCS2 message containing a \u0434 character", msg.getMessageBody());
//Synthetic comment -- @@ -524,7 +523,7 @@

(byte)0x50
};
        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected 7-bit string decoded",
"A UCS2 message containing a \u0434 character", msg.getMessageBody());
//Synthetic comment -- @@ -549,7 +548,7 @@
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00
};

        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected message identifier decoded", 12345, msg.getServiceCategory());
}
//Synthetic comment -- @@ -578,7 +577,7 @@
(byte)0x34
};

        SmsCbMessage msg = createFromPdu(pdu);

assertEquals("Unexpected message identifier decoded", 12345, msg.getServiceCategory());
}
//Synthetic comment -- @@ -600,7 +599,7 @@
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00
};

        SmsCbMessage msg = createFromPdu(pdu);
int messageCode = (msg.getSerialNumber() & 0x3ff0) >> 4;

assertEquals("Unexpected message code decoded", 682, messageCode);
//Synthetic comment -- @@ -630,7 +629,7 @@
(byte)0x34
};

        SmsCbMessage msg = createFromPdu(pdu);
int messageCode = (msg.getSerialNumber() & 0x3ff0) >> 4;

assertEquals("Unexpected message code decoded", 682, messageCode);
//Synthetic comment -- @@ -653,7 +652,7 @@
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00
};

        SmsCbMessage msg = createFromPdu(pdu);
int updateNumber = msg.getSerialNumber() & 0x000f;

assertEquals("Unexpected update number decoded", 5, updateNumber);
//Synthetic comment -- @@ -683,13 +682,13 @@
(byte)0x34
};

        SmsCbMessage msg = createFromPdu(pdu);
int updateNumber = msg.getSerialNumber() & 0x000f;

assertEquals("Unexpected update number decoded", 5, updateNumber);
}

    /* ETWS Test message including header */
private static final byte[] etwsMessageNormal = IccUtils.hexStringToBytes("000011001101" +
"0D0A5BAE57CE770C531790E85C716CBF3044573065B930675730" +
"9707767A751F30025F37304463FA308C306B5099304830664E0B30553044FF086C178C615E81FF09" +
//Synthetic comment -- @@ -708,7 +707,8 @@
// FIXME: add example of ETWS primary notification PDU

public void testEtwsMessageNormal() {
        SmsCbMessage msg = createFromPdu(etwsMessageNormal);
Log.d(TAG, msg.toString());
assertEquals("GS mismatch", 0, msg.getGeographicalScope());
assertEquals("serial number mismatch", 0, msg.getSerialNumber());
//Synthetic comment -- @@ -718,7 +718,8 @@
}

public void testEtwsMessageCancel() {
        SmsCbMessage msg = createFromPdu(etwsMessageCancel);
Log.d(TAG, msg.toString());
assertEquals("GS mismatch", 0, msg.getGeographicalScope());
assertEquals("serial number mismatch", 0, msg.getSerialNumber());
//Synthetic comment -- @@ -728,7 +729,8 @@
}

public void testEtwsMessageTest() {
        SmsCbMessage msg = createFromPdu(etwsMessageTest);
Log.d(TAG, msg.toString());
assertEquals("GS mismatch", 0, msg.getGeographicalScope());
assertEquals("serial number mismatch", 0, msg.getSerialNumber());
//Synthetic comment -- @@ -748,7 +750,8 @@
}
try {
// this should return a SmsCbMessage object or null for invalid data
                SmsCbMessage msg = createFromPdu(data);
} catch (Exception e) {
Log.d(TAG, "exception thrown", e);
fail("Exception in decoder at run " + run + " length " + len + ": " + e);







