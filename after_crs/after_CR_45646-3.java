/*Telephony: Add bc type field to RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS

New type field is added to distinguish between different types
of broadcast to eliminate the dependence on pdu length for type
distinction.

Change-Id:I3f29b54f2df3f6d3daadf30f53cb2a04af2962d4*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index dbfe5d9..6f6226e 100644

//Synthetic comment -- @@ -49,6 +49,7 @@
import android.text.TextUtils;
import android.telephony.Rlog;

import com.android.internal.telephony.gsm.GsmSmsCbMessage;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
//Synthetic comment -- @@ -2502,7 +2503,7 @@
case RIL_UNSOL_RESTRICTED_STATE_CHANGED: ret = responseInts(p); break;
case RIL_UNSOL_RESPONSE_SIM_STATUS_CHANGED:  ret =  responseVoid(p); break;
case RIL_UNSOL_RESPONSE_CDMA_NEW_SMS:  ret =  responseCdmaSms(p); break;
            case RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS:  ret =  responseBroadcastSms(p); break;
case RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL:  ret =  responseVoid(p); break;
case RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_UNSOL_CDMA_CALL_WAITING: ret = responseCdmaCallWaiting(p); break;
//Synthetic comment -- @@ -2956,6 +2957,15 @@
}

private Object
    responseBroadcastSms(Parcel p) {
        int type = p.readInt();
        byte[] pdu = p.createByteArray();

        GsmSmsCbMessage sms = new GsmSmsCbMessage(type, pdu);
        return sms;
    }

    private Object
responseString(Parcel p) {
String response;









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/src/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index c73dbe6..bdf6239 100644

//Synthetic comment -- @@ -376,9 +376,11 @@
*/
private void handleBroadcastSms(AsyncResult ar) {
try {
            GsmSmsCbMessage sms = (GsmSmsCbMessage)ar.result;
            byte[] receivedPdu = sms.getPdu();

            if (true) {
                Rlog.d(TAG, "type=" + sms.getType());
for (int i = 0; i < receivedPdu.length; i += 8) {
StringBuilder sb = new StringBuilder("SMS CB pdu data: ");
for (int j = i; j < i + 8 && j < receivedPdu.length; j++) {
//Synthetic comment -- @@ -392,7 +394,7 @@
}
}

            SmsCbHeader header = new SmsCbHeader(sms);
String plmn = SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_NUMERIC);
int lac = -1;
int cid = -1;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmSmsCbMessage.java b/src/java/com/android/internal/telephony/gsm/GsmSmsCbMessage.java
//Synthetic comment -- index 78590fb..9511270 100644

//Synthetic comment -- @@ -27,10 +27,30 @@

/**
* Parses a GSM or UMTS format SMS-CB message into an {@link SmsCbMessage} object. The class is
 * public because {@link #createSmsCbMessage(SmsCbLocation, byte[][], int)}
 * is used by some test cases.
*/
public class GsmSmsCbMessage {

    /** Unknown type */
    public static final int CB_NOTIFICATION_TYPE_UNKNOWN = -1;

    /** 0x00 - ETWS Primary */
    public static final int ETWS_NOTIFICATION_TYPE_PRIMARY = 0;

    /** 0x01 - ETWS Secondary GSM */
    public static final int ETWS_NOTIFICATION_TYPE_SECONDARY_GSM = 1;

    /** 0x02 - ETWS Secondary UMTS */
    public static final int ETWS_NOTIFICATION_TYPE_SECONDARY_UMTS = 2;

    /** 0x03 - GSM CB */
    public static final int CB_NOTIFICATION_TYPE_GSM = 3;

    /** 0x04 - UMTS CB */
    public static final int CB_NOTIFICATION_TYPE_UMTS = 4;


/**
* Languages in the 0000xxxx DCS group as defined in 3GPP TS 23.038, section 5.
*/
//Synthetic comment -- @@ -51,9 +71,61 @@

private static final int PDU_BODY_PAGE_LENGTH = 82;

    private int type;
    private byte[] pdu;

private GsmSmsCbMessage() { }

    public GsmSmsCbMessage(int type, byte[] pdu) {
        this.pdu = pdu;

        switch (type) {
            case ETWS_NOTIFICATION_TYPE_PRIMARY:
            case ETWS_NOTIFICATION_TYPE_SECONDARY_GSM:
            case ETWS_NOTIFICATION_TYPE_SECONDARY_UMTS:
            case CB_NOTIFICATION_TYPE_GSM:
            case CB_NOTIFICATION_TYPE_UMTS:
                this.type = type;
                break;

            default:
                this.type = CB_NOTIFICATION_TYPE_UNKNOWN;

        }
    }

    byte[] getPdu() {
        return this.pdu;
    }

    int getType() {
        return this.type;
    }

    boolean isEtwsPrimary() {
        return (ETWS_NOTIFICATION_TYPE_PRIMARY == getType());
    }

    boolean isEtwsSecondaryGsm() {
        return (ETWS_NOTIFICATION_TYPE_SECONDARY_GSM == getType());
    }

    boolean isEtwsSecondaryUmts() {
        return (ETWS_NOTIFICATION_TYPE_SECONDARY_UMTS == getType());
    }

    boolean isCbGsm() {
        return (CB_NOTIFICATION_TYPE_GSM == getType());
    }

    boolean isCbUmts() {
        return (CB_NOTIFICATION_TYPE_UMTS == getType());
    }

    boolean isUnknownType() {
        return (CB_NOTIFICATION_TYPE_UNKNOWN == getType());
    }

/**
* Create a new SmsCbMessage object from a header object plus one or more received PDUs.
*
//Synthetic comment -- @@ -91,10 +163,38 @@
*
* @param location the location (geographical scope) for the message
* @param pdus PDU bytes
     * @deprecated
*/
public static SmsCbMessage createSmsCbMessage(SmsCbLocation location, byte[][] pdus)
throws IllegalArgumentException {
        // type of pdu was not given, guess type from pdu length
        int type = CB_NOTIFICATION_TYPE_UMTS;

        if (pdus[0] == null || pdus[0].length < SmsCbHeader.PDU_HEADER_LENGTH) {
            type = CB_NOTIFICATION_TYPE_UNKNOWN;
        } else if (pdus[0].length <= SmsCbHeader.PDU_LENGTH_ETWS) {
            type = ETWS_NOTIFICATION_TYPE_PRIMARY;
        } else if (pdus[0].length <= SmsCbHeader.PDU_LENGTH_GSM) {
            // can be etws secondary gsm also, but parsing is same as cb gsm
            type = CB_NOTIFICATION_TYPE_GSM;
        }
        GsmSmsCbMessage sms = new GsmSmsCbMessage(type, pdus[0]);
        SmsCbHeader header = new SmsCbHeader(sms);
        return createSmsCbMessage(header, location, pdus);
    }

    /**
     * Create a new SmsCbMessage object from one or more received PDUs. This is used by some
     * CellBroadcastReceiver test cases, because SmsCbHeader is now package local.
     *
     * @param location the location (geographical scope) for the message
     * @param pdus PDU bytes
     * @param type of pdu (etws primary, etws gsm secondary, ....)
     */
    public static SmsCbMessage createSmsCbMessage(SmsCbLocation location, byte[][] pdus, int type)
            throws IllegalArgumentException {
        GsmSmsCbMessage sms = new GsmSmsCbMessage(type, pdus[0]);
        SmsCbHeader header = new SmsCbHeader(sms);
return createSmsCbMessage(header, location, pdus);
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SmsCbHeader.java b/src/java/com/android/internal/telephony/gsm/SmsCbHeader.java
//Synthetic comment -- index 995f5b1..ce0c4a5 100644

//Synthetic comment -- @@ -61,12 +61,12 @@
/**
* Length of GSM pdus
*/
    static final int PDU_LENGTH_GSM = 88;

/**
* Maximum length of ETWS primary message GSM pdus
*/
    static final int PDU_LENGTH_ETWS = 56;

private final int geographicalScope;

//Synthetic comment -- @@ -90,55 +90,56 @@
/** CMAS warning notification info. */
private final SmsCbCmasInfo mCmasInfo;

    public SmsCbHeader(GsmSmsCbMessage sms) throws IllegalArgumentException {
        byte[] pdu = sms.getPdu();
if (pdu == null || pdu.length < PDU_HEADER_LENGTH) {
throw new IllegalArgumentException("Illegal PDU");
}
        if (sms.isUnknownType()) {
            throw new IllegalArgumentException("Illegal pdu type");
        }

        if (sms.isEtwsPrimary()) {
            format = FORMAT_ETWS_PRIMARY;
geographicalScope = (pdu[0] & 0xc0) >>> 6;
serialNumber = ((pdu[0] & 0xff) << 8) | (pdu[1] & 0xff);
messageIdentifier = ((pdu[2] & 0xff) << 8) | (pdu[3] & 0xff);
            dataCodingScheme = -1;
            pageIndex = -1;
            nrOfPages = -1;
            boolean emergencyUserAlert = (pdu[4] & 0x1) != 0;
            boolean activatePopup = (pdu[5] & 0x80) != 0;
            int warningType = (pdu[4] & 0xfe) >>> 1;
            byte[] warningSecurityInfo;
            // copy the Warning-Security-Information, if present
            if (pdu.length > PDU_HEADER_LENGTH) {
                warningSecurityInfo = Arrays.copyOfRange(pdu, 6, pdu.length);
} else {
                warningSecurityInfo = null;
}
            mEtwsInfo = new SmsCbEtwsInfo(warningType, emergencyUserAlert, activatePopup,
                    warningSecurityInfo);
            mCmasInfo = null;
            return;     // skip the ETWS/CMAS initialization code for regular notifications
        } else if (sms.isCbGsm() || sms.isEtwsSecondaryGsm()) {
            // GSM pdus are no more than 88 bytes
            format = FORMAT_GSM;
            geographicalScope = (pdu[0] & 0xc0) >>> 6;
            serialNumber = ((pdu[0] & 0xff) << 8) | (pdu[1] & 0xff);
            messageIdentifier = ((pdu[2] & 0xff) << 8) | (pdu[3] & 0xff);
            dataCodingScheme = pdu[4] & 0xff;

            // Check for invalid page parameter
            int pageIndex = (pdu[5] & 0xf0) >>> 4;
            int nrOfPages = pdu[5] & 0x0f;

            if (pageIndex == 0 || nrOfPages == 0 || pageIndex > nrOfPages) {
                pageIndex = 1;
                nrOfPages = 1;
            }

            this.pageIndex = pageIndex;
            this.nrOfPages = nrOfPages;
} else {
// UMTS pdus are always at least 90 bytes since the payload includes
// a number-of-pages octet and also one length octet per page








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/gsm/GsmSmsCbTest.java b/tests/telephonytests/src/com/android/internal/telephony/gsm/GsmSmsCbTest.java
//Synthetic comment -- index 943e36d..828b772 100644

//Synthetic comment -- @@ -35,12 +35,11 @@

private static final SmsCbLocation sTestLocation = new SmsCbLocation("94040", 1234, 5678);

    private static SmsCbMessage createFromPdu(byte[] pdu, int type) {
try {
byte[][] pdus = new byte[1][];
pdus[0] = pdu;
            return GsmSmsCbMessage.createSmsCbMessage(sTestLocation, pdus, type);
} catch (IllegalArgumentException e) {
return null;
}
//Synthetic comment -- @@ -48,26 +47,26 @@

private static void doTestGeographicalScopeValue(byte[] pdu, byte b, int expectedGs) {
pdu[0] = b;
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM);

assertEquals("Unexpected geographical scope decoded", expectedGs, msg
.getGeographicalScope());
}

public void testCreateNullPdu() {
        SmsCbMessage msg = createFromPdu(null, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UNKNOWN);
assertNull("createFromPdu(byte[] with null pdu should return null", msg);
}

public void testCreateTooShortPdu() {
byte[] pdu = new byte[4];
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UNKNOWN);

assertNull("createFromPdu(byte[] with too short pdu should return null", msg);
}

public void testGetGeographicalScope() {
        byte[] pdu = { //gsm
(byte)0xC0, (byte)0x00, (byte)0x00, (byte)0x32, (byte)0x40, (byte)0x11, (byte)0x41,
(byte)0xD0, (byte)0x71, (byte)0xDA, (byte)0x04, (byte)0x91, (byte)0xCB, (byte)0xE6,
(byte)0x70, (byte)0x9D, (byte)0x4D, (byte)0x07, (byte)0x85, (byte)0xD9, (byte)0x70,
//Synthetic comment -- @@ -91,7 +90,7 @@
}

public void testGetGeographicalScopeUmts() {
        byte[] pdu = { //umts
(byte)0x01, (byte)0x00, (byte)0x32, (byte)0xC0, (byte)0x00, (byte)0x40,

(byte)0x01,
//Synthetic comment -- @@ -114,7 +113,7 @@
(byte)0x34
};

        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS);

assertEquals("Unexpected geographical scope decoded",
SmsCbMessage.GEOGRAPHICAL_SCOPE_CELL_WIDE, msg.getGeographicalScope());
//Synthetic comment -- @@ -136,7 +135,7 @@
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00
};
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM);

assertEquals("Unexpected 7-bit string decoded",
"A GSM default alphabet message with carriage return padding",
//Synthetic comment -- @@ -166,7 +165,7 @@

(byte)0x34
};
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS);

assertEquals("Unexpected 7-bit string decoded",
"A GSM default alphabet message with carriage return padding",
//Synthetic comment -- @@ -213,7 +212,7 @@

(byte)0x0A
};
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS);

assertEquals("Unexpected multipage 7-bit string decoded",
"First page+Second page",
//Synthetic comment -- @@ -236,7 +235,7 @@
(byte)0x90, (byte)0xFB, (byte)0x0D, (byte)0x82, (byte)0x87, (byte)0xC9, (byte)0xE4,
(byte)0xB4, (byte)0xFB, (byte)0x1C, (byte)0x02
};
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM);

assertEquals(
"Unexpected 7-bit string decoded",
//Synthetic comment -- @@ -268,7 +267,7 @@

(byte)0x52
};
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS);

assertEquals(
"Unexpected 7-bit string decoded",
//Synthetic comment -- @@ -293,7 +292,7 @@
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00
};
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM);

assertEquals("Unexpected 7-bit string decoded",
"A GSM default alphabet message with carriage return padding",
//Synthetic comment -- @@ -318,7 +317,7 @@
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00
};
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM);

assertEquals("Unexpected 7-bit string decoded",
"A GSM default alphabet message with carriage return padding",
//Synthetic comment -- @@ -350,7 +349,7 @@

(byte)0x37
};
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS);

assertEquals("Unexpected 7-bit string decoded",
"A GSM default alphabet message with carriage return padding",
//Synthetic comment -- @@ -375,7 +374,7 @@
(byte)0x42, (byte)0x43, (byte)0x44, (byte)0x45, (byte)0x46, (byte)0x47, (byte)0x41,
(byte)0x42, (byte)0x43, (byte)0x44, (byte)0x45
};
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM);

assertEquals("8-bit message body should be empty", "", msg.getMessageBody());
}
//Synthetic comment -- @@ -396,7 +395,7 @@
(byte)0x63, (byte)0x00, (byte)0x74, (byte)0x00, (byte)0x65, (byte)0x00, (byte)0x72,
(byte)0x00, (byte)0x0D, (byte)0x00, (byte)0x0D
};
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM);

assertEquals("Unexpected 7-bit string decoded",
"A UCS2 message containing a \u0434 character", msg.getMessageBody());
//Synthetic comment -- @@ -425,7 +424,7 @@

(byte)0x4E
};
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS);

assertEquals("Unexpected 7-bit string decoded",
"A UCS2 message containing a \u0434 character", msg.getMessageBody());
//Synthetic comment -- @@ -471,7 +470,7 @@

(byte)0x06
};
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS);

assertEquals("Unexpected multipage UCS2 string decoded",
"AAABBB", msg.getMessageBody());
//Synthetic comment -- @@ -493,7 +492,7 @@
(byte)0x61, (byte)0x00, (byte)0x63, (byte)0x00, (byte)0x74, (byte)0x00, (byte)0x65,
(byte)0x00, (byte)0x72, (byte)0x00, (byte)0x0D
};
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM);

assertEquals("Unexpected 7-bit string decoded",
"A UCS2 message containing a \u0434 character", msg.getMessageBody());
//Synthetic comment -- @@ -524,7 +523,7 @@

(byte)0x50
};
        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS);

assertEquals("Unexpected 7-bit string decoded",
"A UCS2 message containing a \u0434 character", msg.getMessageBody());
//Synthetic comment -- @@ -549,7 +548,7 @@
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00
};

        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM);

assertEquals("Unexpected message identifier decoded", 12345, msg.getServiceCategory());
}
//Synthetic comment -- @@ -578,7 +577,7 @@
(byte)0x34
};

        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS);

assertEquals("Unexpected message identifier decoded", 12345, msg.getServiceCategory());
}
//Synthetic comment -- @@ -600,7 +599,7 @@
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00
};

        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM);
int messageCode = (msg.getSerialNumber() & 0x3ff0) >> 4;

assertEquals("Unexpected message code decoded", 682, messageCode);
//Synthetic comment -- @@ -630,7 +629,7 @@
(byte)0x34
};

        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS);
int messageCode = (msg.getSerialNumber() & 0x3ff0) >> 4;

assertEquals("Unexpected message code decoded", 682, messageCode);
//Synthetic comment -- @@ -653,7 +652,7 @@
(byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00
};

        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_GSM);
int updateNumber = msg.getSerialNumber() & 0x000f;

assertEquals("Unexpected update number decoded", 5, updateNumber);
//Synthetic comment -- @@ -683,13 +682,13 @@
(byte)0x34
};

        SmsCbMessage msg = createFromPdu(pdu, GsmSmsCbMessage.CB_NOTIFICATION_TYPE_UMTS);
int updateNumber = msg.getSerialNumber() & 0x000f;

assertEquals("Unexpected update number decoded", 5, updateNumber);
}

    /* Secondary ETWS Gsm Test message including header */
private static final byte[] etwsMessageNormal = IccUtils.hexStringToBytes("000011001101" +
"0D0A5BAE57CE770C531790E85C716CBF3044573065B930675730" +
"9707767A751F30025F37304463FA308C306B5099304830664E0B30553044FF086C178C615E81FF09" +
//Synthetic comment -- @@ -708,7 +707,8 @@
// FIXME: add example of ETWS primary notification PDU

public void testEtwsMessageNormal() {
        SmsCbMessage msg = createFromPdu(etwsMessageNormal,
                GsmSmsCbMessage.ETWS_NOTIFICATION_TYPE_SECONDARY_GSM);
Rlog.d(TAG, msg.toString());
assertEquals("GS mismatch", 0, msg.getGeographicalScope());
assertEquals("serial number mismatch", 0, msg.getSerialNumber());
//Synthetic comment -- @@ -718,7 +718,8 @@
}

public void testEtwsMessageCancel() {
        SmsCbMessage msg = createFromPdu(etwsMessageCancel,
                GsmSmsCbMessage.ETWS_NOTIFICATION_TYPE_SECONDARY_GSM);
Rlog.d(TAG, msg.toString());
assertEquals("GS mismatch", 0, msg.getGeographicalScope());
assertEquals("serial number mismatch", 0, msg.getSerialNumber());
//Synthetic comment -- @@ -728,7 +729,8 @@
}

public void testEtwsMessageTest() {
        SmsCbMessage msg = createFromPdu(etwsMessageTest,
                GsmSmsCbMessage.ETWS_NOTIFICATION_TYPE_SECONDARY_GSM);
Rlog.d(TAG, msg.toString());
assertEquals("GS mismatch", 0, msg.getGeographicalScope());
assertEquals("serial number mismatch", 0, msg.getSerialNumber());
//Synthetic comment -- @@ -748,7 +750,8 @@
}
try {
// this should return a SmsCbMessage object or null for invalid data
                // set type with some random number
                SmsCbMessage msg = createFromPdu(data, r.nextInt(10));
} catch (Exception e) {
Rlog.d(TAG, "exception thrown", e);
fail("Exception in decoder at run " + run + " length " + len + ": " + e);







