/*Adding support for parsing of SMS-CB pdus in UMTS format.

This enables RIL to dispatch pdus in both GSM or UMTS format.
The RAT that was used will be transparent to clients.

Change-Id:I57c53ec79496d274e18b3fee196551af97c0857d*/




//Synthetic comment -- diff --git a/telephony/java/android/telephony/SmsCbMessage.java b/telephony/java/android/telephony/SmsCbMessage.java
//Synthetic comment -- index 3543275..5608402 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package android.telephony;

import android.util.Log;

import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.gsm.SmsCbHeader;

//Synthetic comment -- @@ -58,10 +60,13 @@
try {
return new SmsCbMessage(pdu);
} catch (IllegalArgumentException e) {
            Log.w(LOG_TAG, "Failed parsing SMS-CB pdu", e);
return null;
}
}

    private static final String LOG_TAG = "SMSCB";

/**
* Languages in the 0000xxxx DCS group as defined in 3GPP TS 23.038, section 5.
*/
//Synthetic comment -- @@ -80,6 +85,8 @@

private static final char CARRIAGE_RETURN = 0x0d;

    private static final int PDU_BODY_PAGE_LENGTH = 82;

private SmsCbHeader mHeader;

private String mLanguage;
//Synthetic comment -- @@ -149,6 +156,13 @@
return mHeader.updateNumber;
}

    /**
     * Parse and unpack the body text according to the encoding in the DCS.
     * After completing successfully this method will have assigned the body
     * text into mBody, and optionally the language code into mLanguage
     *
     * @param pdu The pdu
     */
private void parseBody(byte[] pdu) {
int encoding;
boolean hasLanguageIndicator = false;
//Synthetic comment -- @@ -221,28 +235,81 @@
break;
}

        if (mHeader.format == SmsCbHeader.FORMAT_UMTS) {
            // Payload may contain multiple pages
            int nrPages = pdu[SmsCbHeader.PDU_HEADER_LENGTH];

            if (pdu.length < SmsCbHeader.PDU_HEADER_LENGTH + 1 + (PDU_BODY_PAGE_LENGTH + 1)
                    * nrPages) {
                throw new IllegalArgumentException("Pdu length " + pdu.length + " does not match "
                        + nrPages + " pages");
            }

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < nrPages; i++) {
                // Each page is 82 bytes followed by a length octet indicating
                // the number of useful octets within those 82
                int offset = SmsCbHeader.PDU_HEADER_LENGTH + 1 + (PDU_BODY_PAGE_LENGTH + 1) * i;
                int length = pdu[offset + PDU_BODY_PAGE_LENGTH];

                if (length > PDU_BODY_PAGE_LENGTH) {
                    throw new IllegalArgumentException("Page length " + length
                            + " exceeds maximum value " + PDU_BODY_PAGE_LENGTH);
                }

                sb.append(unpackBody(pdu, encoding, offset, length, hasLanguageIndicator));
            }
            mBody = sb.toString();
        } else {
            // Payload is one single page
            int offset = SmsCbHeader.PDU_HEADER_LENGTH;
            int length = pdu.length - offset;

            mBody = unpackBody(pdu, encoding, offset, length, hasLanguageIndicator);
        }
    }

    /**
     * Unpack body text from the pdu using the given encoding, position and
     * length within the pdu
     *
     * @param pdu The pdu
     * @param encoding The encoding, as derived from the DCS
     * @param offset Position of the first byte to unpack
     * @param length Number of bytes to unpack
     * @param hasLanguageIndicator true if the body text is preceded by a
     *            language indicator. If so, this method will as a side-effect
     *            assign the extracted language code into mLanguage
     * @return Body text
     */
    private String unpackBody(byte[] pdu, int encoding, int offset, int length,
            boolean hasLanguageIndicator) {
        String body = null;

switch (encoding) {
case SmsMessage.ENCODING_7BIT:
                body = GsmAlphabet.gsm7BitPackedToString(pdu, offset, length * 8 / 7);

                if (hasLanguageIndicator && body != null && body.length() > 2) {
                    // Language is two GSM characters followed by a CR.
                    // The actual body text is offset by 3 characters.
                    mLanguage = body.substring(0, 2);
                    body = body.substring(3);
}
break;

case SmsMessage.ENCODING_16BIT:
                if (hasLanguageIndicator && pdu.length >= offset + 2) {
                    // Language is two GSM characters.
                    // The actual body text is offset by 2 bytes.
                    mLanguage = GsmAlphabet.gsm7BitPackedToString(pdu, offset, 2);
offset += 2;
                    length -= 2;
}

try {
                    body = new String(pdu, offset, (length & 0xfffe), "utf-16");
} catch (UnsupportedEncodingException e) {
// Eeeek
}
//Synthetic comment -- @@ -252,16 +319,18 @@
break;
}

        if (body != null) {
// Remove trailing carriage return
            for (int i = body.length() - 1; i >= 0; i--) {
                if (body.charAt(i) != CARRIAGE_RETURN) {
                    body = body.substring(0, i + 1);
break;
}
}
} else {
            body = "";
}

        return body;
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsCbHeader.java b/telephony/java/com/android/internal/telephony/gsm/SmsCbHeader.java
//Synthetic comment -- index 5f27cfc..23319c0 100644

//Synthetic comment -- @@ -17,8 +17,31 @@
package com.android.internal.telephony.gsm;

public class SmsCbHeader {
    /**
     * Length of SMS-CB header
     */
public static final int PDU_HEADER_LENGTH = 6;

    /**
     * GSM pdu format, as defined in 3gpp TS 23.041, section 9.4.1
     */
    public static final int FORMAT_GSM = 1;

    /**
     * UMTS pdu format, as defined in 3gpp TS 23.041, section 9.4.2
     */
    public static final int FORMAT_UMTS = 2;

    /**
     * Message type value as defined in 3gpp TS 25.324, section 11.1.
     */
    private static final int MESSAGE_TYPE_CBS_MESSAGE = 1;

    /**
     * Length of GSM pdus
     */
    private static final int PDU_LENGTH_GSM = 88;

public final int geographicalScope;

public final int messageCode;
//Synthetic comment -- @@ -33,27 +56,55 @@

public final int nrOfPages;

    public final int format;

public SmsCbHeader(byte[] pdu) throws IllegalArgumentException {
if (pdu == null || pdu.length < PDU_HEADER_LENGTH) {
throw new IllegalArgumentException("Illegal PDU");
}

        if (pdu.length <= PDU_LENGTH_GSM) {
            // GSM pdus are no more than 88 bytes
            format = FORMAT_GSM;
            geographicalScope = (pdu[0] & 0xc0) >> 6;
            messageCode = ((pdu[0] & 0x3f) << 4) | ((pdu[1] & 0xf0) >> 4);
            updateNumber = pdu[1] & 0x0f;
            messageIdentifier = (pdu[2] << 8) | pdu[3];
            dataCodingScheme = pdu[4];

            // Check for invalid page parameter
            int pageIndex = (pdu[5] & 0xf0) >> 4;
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
            format = FORMAT_UMTS;

            int messageType = pdu[0];

            if (messageType != MESSAGE_TYPE_CBS_MESSAGE) {
                throw new IllegalArgumentException("Unsupported message type " + messageType);
            }

            messageIdentifier = (pdu[1] << 8) | pdu[2];
            geographicalScope = (pdu[3] & 0xc0) >> 6;
            messageCode = ((pdu[3] & 0x3f) << 4) | ((pdu[4] & 0xf0) >> 4);
            updateNumber = pdu[4] & 0x0f;
            dataCodingScheme = pdu[5];

            // We will always consider a UMTS message as having one single page
            // since there's only one instance of the header, even though the
            // actual payload may contain several pages.
pageIndex = 1;
nrOfPages = 1;
}
}
}








//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/com/android/internal/telephony/GsmSmsCbTest.java b/telephony/tests/telephonytests/src/com/android/internal/telephony/GsmSmsCbTest.java
//Synthetic comment -- index 7136ea0..b131a01 100644

//Synthetic comment -- @@ -69,6 +69,36 @@
doTestGeographicalScopeValue(pdu, (byte)0xC0, SmsCbMessage.GEOGRAPHICAL_SCOPE_CELL_WIDE);
}

    public void testGetGeographicalScopeUmts() {
        byte[] pdu = {
                (byte)0x01, (byte)0x00, (byte)0x32, (byte)0xC0, (byte)0x00, (byte)0x40,

                (byte)0x01,

                (byte)0x41, (byte)0xD0, (byte)0x71, (byte)0xDA, (byte)0x04, (byte)0x91,
                (byte)0xCB, (byte)0xE6, (byte)0x70, (byte)0x9D, (byte)0x4D, (byte)0x07,
                (byte)0x85, (byte)0xD9, (byte)0x70, (byte)0x74, (byte)0x58, (byte)0x5C,
                (byte)0xA6, (byte)0x83, (byte)0xDA, (byte)0xE5, (byte)0xF9, (byte)0x3C,
                (byte)0x7C, (byte)0x2E, (byte)0x83, (byte)0xEE, (byte)0x69, (byte)0x3A,
                (byte)0x1A, (byte)0x34, (byte)0x0E, (byte)0xCB, (byte)0xE5, (byte)0xE9,
                (byte)0xF0, (byte)0xB9, (byte)0x0C, (byte)0x92, (byte)0x97, (byte)0xE9,
                (byte)0x75, (byte)0xB9, (byte)0x1B, (byte)0x04, (byte)0x0F, (byte)0x93,
                (byte)0xC9, (byte)0x69, (byte)0xF7, (byte)0xB9, (byte)0xD1, (byte)0x68,
                (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3, (byte)0xD1,
                (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3,
                (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46,
                (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
                (byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00,

                (byte)0x34
        };

        SmsCbMessage msg = SmsCbMessage.createFromPdu(pdu);

        assertEquals("Unexpected geographical scope decoded",
                SmsCbMessage.GEOGRAPHICAL_SCOPE_CELL_WIDE, msg.getGeographicalScope());
    }

public void testGetMessageBody7Bit() {
byte[] pdu = {
(byte)0xC0, (byte)0x00, (byte)0x00, (byte)0x32, (byte)0x40, (byte)0x11, (byte)0x41,
//Synthetic comment -- @@ -92,6 +122,83 @@
msg.getMessageBody());
}

    public void testGetMessageBody7BitUmts() {
        byte[] pdu = {
                (byte)0x01, (byte)0x00, (byte)0x32, (byte)0xC0, (byte)0x00, (byte)0x40,

                (byte)0x01,

                (byte)0x41, (byte)0xD0, (byte)0x71, (byte)0xDA, (byte)0x04, (byte)0x91,
                (byte)0xCB, (byte)0xE6, (byte)0x70, (byte)0x9D, (byte)0x4D, (byte)0x07,
                (byte)0x85, (byte)0xD9, (byte)0x70, (byte)0x74, (byte)0x58, (byte)0x5C,
                (byte)0xA6, (byte)0x83, (byte)0xDA, (byte)0xE5, (byte)0xF9, (byte)0x3C,
                (byte)0x7C, (byte)0x2E, (byte)0x83, (byte)0xEE, (byte)0x69, (byte)0x3A,
                (byte)0x1A, (byte)0x34, (byte)0x0E, (byte)0xCB, (byte)0xE5, (byte)0xE9,
                (byte)0xF0, (byte)0xB9, (byte)0x0C, (byte)0x92, (byte)0x97, (byte)0xE9,
                (byte)0x75, (byte)0xB9, (byte)0x1B, (byte)0x04, (byte)0x0F, (byte)0x93,
                (byte)0xC9, (byte)0x69, (byte)0xF7, (byte)0xB9, (byte)0xD1, (byte)0x68,
                (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3, (byte)0xD1,
                (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3,
                (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46,
                (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
                (byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00,

                (byte)0x34
        };
        SmsCbMessage msg = SmsCbMessage.createFromPdu(pdu);

        assertEquals("Unexpected 7-bit string decoded",
                "A GSM default alphabet message with carriage return padding",
                msg.getMessageBody());
    }

    public void testGetMessageBody7BitMultipageUmts() {
        byte[] pdu = {
                (byte)0x01, (byte)0x00, (byte)0x01, (byte)0xC0, (byte)0x00, (byte)0x40,

                (byte)0x02,

                (byte)0xC6, (byte)0xB4, (byte)0x7C, (byte)0x4E, (byte)0x07, (byte)0xC1,
                (byte)0xC3, (byte)0xE7, (byte)0xF2, (byte)0xAA, (byte)0xD1, (byte)0x68,
                (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3, (byte)0xD1,
                (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3,
                (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46,
                (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
                (byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A,
                (byte)0x8D, (byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34,
                (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x68,
                (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3, (byte)0xD1,
                (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3,
                (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46,
                (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
                (byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00,

                (byte)0x0A,

                (byte)0xD3, (byte)0xF2, (byte)0xF8, (byte)0xED, (byte)0x26, (byte)0x83,
                (byte)0xE0, (byte)0xE1, (byte)0x73, (byte)0xB9, (byte)0xD1, (byte)0x68,
                (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3, (byte)0xD1,
                (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3,
                (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46,
                (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
                (byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A,
                (byte)0x8D, (byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34,
                (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x68,
                (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3, (byte)0xD1,
                (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3,
                (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46,
                (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
                (byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00,

                (byte)0x0A
        };
        SmsCbMessage msg = SmsCbMessage.createFromPdu(pdu);

        assertEquals("Unexpected multipage 7-bit string decoded",
                "First page+Second page",
                msg.getMessageBody());
    }

public void testGetMessageBody7BitFull() {
byte[] pdu = {
(byte)0xC0, (byte)0x00, (byte)0x00, (byte)0x32, (byte)0x40, (byte)0x11, (byte)0x41,
//Synthetic comment -- @@ -117,6 +224,38 @@
msg.getMessageBody());
}

    public void testGetMessageBody7BitFullUmts() {
        byte[] pdu = {
                (byte)0x01, (byte)0x00, (byte)0x32, (byte)0xC0, (byte)0x00, (byte)0x40,

                (byte)0x01,

                (byte)0x41, (byte)0xD0, (byte)0x71, (byte)0xDA, (byte)0x04, (byte)0x91,
                (byte)0xCB, (byte)0xE6, (byte)0x70, (byte)0x9D, (byte)0x4D, (byte)0x07,
                (byte)0x85, (byte)0xD9, (byte)0x70, (byte)0x74, (byte)0x58, (byte)0x5C,
                (byte)0xA6, (byte)0x83, (byte)0xDA, (byte)0xE5, (byte)0xF9, (byte)0x3C,
                (byte)0x7C, (byte)0x2E, (byte)0x83, (byte)0xC4, (byte)0xE5, (byte)0xB4,
                (byte)0xFB, (byte)0x0C, (byte)0x2A, (byte)0xE3, (byte)0xC3, (byte)0x63,
                (byte)0x3A, (byte)0x3B, (byte)0x0F, (byte)0xCA, (byte)0xCD, (byte)0x40,
                (byte)0x63, (byte)0x74, (byte)0x58, (byte)0x1E, (byte)0x1E, (byte)0xD3,
                (byte)0xCB, (byte)0xF2, (byte)0x39, (byte)0x88, (byte)0xFD, (byte)0x76,
                (byte)0x9F, (byte)0x59, (byte)0xA0, (byte)0x76, (byte)0x39, (byte)0xEC,
                (byte)0x4E, (byte)0xBB, (byte)0xCF, (byte)0x20, (byte)0x3A, (byte)0xBA,
                (byte)0x2C, (byte)0x2F, (byte)0x83, (byte)0xD2, (byte)0x73, (byte)0x90,
                (byte)0xFB, (byte)0x0D, (byte)0x82, (byte)0x87, (byte)0xC9, (byte)0xE4,
                (byte)0xB4, (byte)0xFB, (byte)0x1C, (byte)0x02,

                (byte)0x52
        };
        SmsCbMessage msg = SmsCbMessage.createFromPdu(pdu);

        assertEquals(
                "Unexpected 7-bit string decoded",
                "A GSM default alphabet message being exactly 93 characters long, " +
                "meaning there is no padding!",
                msg.getMessageBody());
    }

public void testGetMessageBody7BitWithLanguage() {
byte[] pdu = {
(byte)0xC0, (byte)0x00, (byte)0x00, (byte)0x32, (byte)0x04, (byte)0x11, (byte)0x41,
//Synthetic comment -- @@ -167,6 +306,38 @@
assertEquals("Unexpected language indicator decoded", "sv", msg.getLanguageCode());
}

    public void testGetMessageBody7BitWithLanguageInBodyUmts() {
        byte[] pdu = {
                (byte)0x01, (byte)0x00, (byte)0x32, (byte)0xC0, (byte)0x00, (byte)0x10,

                (byte)0x01,

                (byte)0x73, (byte)0x7B, (byte)0x23, (byte)0x08, (byte)0x3A, (byte)0x4E,
                (byte)0x9B, (byte)0x20, (byte)0x72, (byte)0xD9, (byte)0x1C, (byte)0xAE,
                (byte)0xB3, (byte)0xE9, (byte)0xA0, (byte)0x30, (byte)0x1B, (byte)0x8E,
                (byte)0x0E, (byte)0x8B, (byte)0xCB, (byte)0x74, (byte)0x50, (byte)0xBB,
                (byte)0x3C, (byte)0x9F, (byte)0x87, (byte)0xCF, (byte)0x65, (byte)0xD0,
                (byte)0x3D, (byte)0x4D, (byte)0x47, (byte)0x83, (byte)0xC6, (byte)0x61,
                (byte)0xB9, (byte)0x3C, (byte)0x1D, (byte)0x3E, (byte)0x97, (byte)0x41,
                (byte)0xF2, (byte)0x32, (byte)0xBD, (byte)0x2E, (byte)0x77, (byte)0x83,
                (byte)0xE0, (byte)0x61, (byte)0x32, (byte)0x39, (byte)0xED, (byte)0x3E,
                (byte)0x37, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3, (byte)0xD1,
                (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3,
                (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46,
                (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
                (byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00,

                (byte)0x37
        };
        SmsCbMessage msg = SmsCbMessage.createFromPdu(pdu);

        assertEquals("Unexpected 7-bit string decoded",
                "A GSM default alphabet message with carriage return padding",
                msg.getMessageBody());

        assertEquals("Unexpected language indicator decoded", "sv", msg.getLanguageCode());
    }

public void testGetMessageBody8Bit() {
byte[] pdu = {
(byte)0xC0, (byte)0x00, (byte)0x00, (byte)0x32, (byte)0x44, (byte)0x11, (byte)0x41,
//Synthetic comment -- @@ -210,6 +381,81 @@
"A UCS2 message containing a \u0434 character", msg.getMessageBody());
}

    public void testGetMessageBodyUcs2Umts() {
        byte[] pdu = {
                (byte)0x01, (byte)0x00, (byte)0x32, (byte)0xC0, (byte)0x00, (byte)0x48,

                (byte)0x01,

                (byte)0x00, (byte)0x41, (byte)0x00, (byte)0x20, (byte)0x00, (byte)0x55,
                (byte)0x00, (byte)0x43, (byte)0x00, (byte)0x53, (byte)0x00, (byte)0x32,
                (byte)0x00, (byte)0x20, (byte)0x00, (byte)0x6D, (byte)0x00, (byte)0x65,
                (byte)0x00, (byte)0x73, (byte)0x00, (byte)0x73, (byte)0x00, (byte)0x61,
                (byte)0x00, (byte)0x67, (byte)0x00, (byte)0x65, (byte)0x00, (byte)0x20,
                (byte)0x00, (byte)0x63, (byte)0x00, (byte)0x6F, (byte)0x00, (byte)0x6E,
                (byte)0x00, (byte)0x74, (byte)0x00, (byte)0x61, (byte)0x00, (byte)0x69,
                (byte)0x00, (byte)0x6E, (byte)0x00, (byte)0x69, (byte)0x00, (byte)0x6E,
                (byte)0x00, (byte)0x67, (byte)0x00, (byte)0x20, (byte)0x00, (byte)0x61,
                (byte)0x00, (byte)0x20, (byte)0x04, (byte)0x34, (byte)0x00, (byte)0x20,
                (byte)0x00, (byte)0x63, (byte)0x00, (byte)0x68, (byte)0x00, (byte)0x61,
                (byte)0x00, (byte)0x72, (byte)0x00, (byte)0x61, (byte)0x00, (byte)0x63,
                (byte)0x00, (byte)0x74, (byte)0x00, (byte)0x65, (byte)0x00, (byte)0x72,
                (byte)0x00, (byte)0x0D, (byte)0x00, (byte)0x0D,

                (byte)0x4E
        };
        SmsCbMessage msg = SmsCbMessage.createFromPdu(pdu);

        assertEquals("Unexpected 7-bit string decoded",
                "A UCS2 message containing a \u0434 character", msg.getMessageBody());
    }

    public void testGetMessageBodyUcs2MultipageUmts() {
        byte[] pdu = {
                (byte)0x01, (byte)0x00, (byte)0x32, (byte)0xC0, (byte)0x00, (byte)0x48,

                (byte)0x02,

                (byte)0x00, (byte)0x41, (byte)0x00, (byte)0x41, (byte)0x00, (byte)0x41,
                (byte)0x00, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,

                (byte)0x06,

                (byte)0x00, (byte)0x42, (byte)0x00, (byte)0x42, (byte)0x00, (byte)0x42,
                (byte)0x00, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
                (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,

                (byte)0x06
        };
        SmsCbMessage msg = SmsCbMessage.createFromPdu(pdu);

        assertEquals("Unexpected multipage UCS2 string decoded",
                "AAABBB", msg.getMessageBody());
    }

public void testGetMessageBodyUcs2WithLanguageInBody() {
byte[] pdu = {
(byte)0xC0, (byte)0x00, (byte)0x00, (byte)0x32, (byte)0x11, (byte)0x11, (byte)0x78,
//Synthetic comment -- @@ -234,6 +480,37 @@
assertEquals("Unexpected language indicator decoded", "xx", msg.getLanguageCode());
}

    public void testGetMessageBodyUcs2WithLanguageInBodyUmts() {
        byte[] pdu = {
                (byte)0x01, (byte)0x00, (byte)0x32, (byte)0xC0, (byte)0x00, (byte)0x11,

                (byte)0x01,

                (byte)0x78, (byte)0x3C, (byte)0x00, (byte)0x41, (byte)0x00, (byte)0x20,
                (byte)0x00, (byte)0x55, (byte)0x00, (byte)0x43, (byte)0x00, (byte)0x53,
                (byte)0x00, (byte)0x32, (byte)0x00, (byte)0x20, (byte)0x00, (byte)0x6D,
                (byte)0x00, (byte)0x65, (byte)0x00, (byte)0x73, (byte)0x00, (byte)0x73,
                (byte)0x00, (byte)0x61, (byte)0x00, (byte)0x67, (byte)0x00, (byte)0x65,
                (byte)0x00, (byte)0x20, (byte)0x00, (byte)0x63, (byte)0x00, (byte)0x6F,
                (byte)0x00, (byte)0x6E, (byte)0x00, (byte)0x74, (byte)0x00, (byte)0x61,
                (byte)0x00, (byte)0x69, (byte)0x00, (byte)0x6E, (byte)0x00, (byte)0x69,
                (byte)0x00, (byte)0x6E, (byte)0x00, (byte)0x67, (byte)0x00, (byte)0x20,
                (byte)0x00, (byte)0x61, (byte)0x00, (byte)0x20, (byte)0x04, (byte)0x34,
                (byte)0x00, (byte)0x20, (byte)0x00, (byte)0x63, (byte)0x00, (byte)0x68,
                (byte)0x00, (byte)0x61, (byte)0x00, (byte)0x72, (byte)0x00, (byte)0x61,
                (byte)0x00, (byte)0x63, (byte)0x00, (byte)0x74, (byte)0x00, (byte)0x65,
                (byte)0x00, (byte)0x72, (byte)0x00, (byte)0x0D,

                (byte)0x50
        };
        SmsCbMessage msg = SmsCbMessage.createFromPdu(pdu);

        assertEquals("Unexpected 7-bit string decoded",
                "A UCS2 message containing a \u0434 character", msg.getMessageBody());

        assertEquals("Unexpected language indicator decoded", "xx", msg.getLanguageCode());
    }

public void testGetMessageIdentifier() {
byte[] pdu = {
(byte)0xC0, (byte)0x00, (byte)0x30, (byte)0x39, (byte)0x40, (byte)0x11, (byte)0x41,
//Synthetic comment -- @@ -256,6 +533,35 @@
assertEquals("Unexpected message identifier decoded", 12345, msg.getMessageIdentifier());
}

    public void testGetMessageIdentifierUmts() {
        byte[] pdu = {
                (byte)0x01, (byte)0x30, (byte)0x39, (byte)0x2A, (byte)0xA5, (byte)0x40,

                (byte)0x01,

                (byte)0x41, (byte)0xD0, (byte)0x71, (byte)0xDA, (byte)0x04, (byte)0x91,
                (byte)0xCB, (byte)0xE6, (byte)0x70, (byte)0x9D, (byte)0x4D, (byte)0x07,
                (byte)0x85, (byte)0xD9, (byte)0x70, (byte)0x74, (byte)0x58, (byte)0x5C,
                (byte)0xA6, (byte)0x83, (byte)0xDA, (byte)0xE5, (byte)0xF9, (byte)0x3C,
                (byte)0x7C, (byte)0x2E, (byte)0x83, (byte)0xEE, (byte)0x69, (byte)0x3A,
                (byte)0x1A, (byte)0x34, (byte)0x0E, (byte)0xCB, (byte)0xE5, (byte)0xE9,
                (byte)0xF0, (byte)0xB9, (byte)0x0C, (byte)0x92, (byte)0x97, (byte)0xE9,
                (byte)0x75, (byte)0xB9, (byte)0x1B, (byte)0x04, (byte)0x0F, (byte)0x93,
                (byte)0xC9, (byte)0x69, (byte)0xF7, (byte)0xB9, (byte)0xD1, (byte)0x68,
                (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3, (byte)0xD1,
                (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3,
                (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46,
                (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
                (byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00,

                (byte)0x34
        };

        SmsCbMessage msg = SmsCbMessage.createFromPdu(pdu);

        assertEquals("Unexpected message identifier decoded", 12345, msg.getMessageIdentifier());
    }

public void testGetMessageCode() {
byte[] pdu = {
(byte)0x2A, (byte)0xA5, (byte)0x30, (byte)0x39, (byte)0x40, (byte)0x11, (byte)0x41,
//Synthetic comment -- @@ -278,6 +584,35 @@
assertEquals("Unexpected message code decoded", 682, msg.getMessageCode());
}

    public void testGetMessageCodeUmts() {
        byte[] pdu = {
                (byte)0x01, (byte)0x30, (byte)0x39, (byte)0x2A, (byte)0xA5, (byte)0x40,

                (byte)0x01,

                (byte)0x41, (byte)0xD0, (byte)0x71, (byte)0xDA, (byte)0x04, (byte)0x91,
                (byte)0xCB, (byte)0xE6, (byte)0x70, (byte)0x9D, (byte)0x4D, (byte)0x07,
                (byte)0x85, (byte)0xD9, (byte)0x70, (byte)0x74, (byte)0x58, (byte)0x5C,
                (byte)0xA6, (byte)0x83, (byte)0xDA, (byte)0xE5, (byte)0xF9, (byte)0x3C,
                (byte)0x7C, (byte)0x2E, (byte)0x83, (byte)0xEE, (byte)0x69, (byte)0x3A,
                (byte)0x1A, (byte)0x34, (byte)0x0E, (byte)0xCB, (byte)0xE5, (byte)0xE9,
                (byte)0xF0, (byte)0xB9, (byte)0x0C, (byte)0x92, (byte)0x97, (byte)0xE9,
                (byte)0x75, (byte)0xB9, (byte)0x1B, (byte)0x04, (byte)0x0F, (byte)0x93,
                (byte)0xC9, (byte)0x69, (byte)0xF7, (byte)0xB9, (byte)0xD1, (byte)0x68,
                (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3, (byte)0xD1,
                (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3,
                (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46,
                (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
                (byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00,

                (byte)0x34
        };

        SmsCbMessage msg = SmsCbMessage.createFromPdu(pdu);

        assertEquals("Unexpected message code decoded", 682, msg.getMessageCode());
    }

public void testGetUpdateNumber() {
byte[] pdu = {
(byte)0x2A, (byte)0xA5, (byte)0x30, (byte)0x39, (byte)0x40, (byte)0x11, (byte)0x41,
//Synthetic comment -- @@ -299,4 +634,33 @@

assertEquals("Unexpected update number decoded", 5, msg.getUpdateNumber());
}

    public void testGetUpdateNumberUmts() {
        byte[] pdu = {
                (byte)0x01, (byte)0x30, (byte)0x39, (byte)0x2A, (byte)0xA5, (byte)0x40,

                (byte)0x01,

                (byte)0x41, (byte)0xD0, (byte)0x71, (byte)0xDA, (byte)0x04, (byte)0x91,
                (byte)0xCB, (byte)0xE6, (byte)0x70, (byte)0x9D, (byte)0x4D, (byte)0x07,
                (byte)0x85, (byte)0xD9, (byte)0x70, (byte)0x74, (byte)0x58, (byte)0x5C,
                (byte)0xA6, (byte)0x83, (byte)0xDA, (byte)0xE5, (byte)0xF9, (byte)0x3C,
                (byte)0x7C, (byte)0x2E, (byte)0x83, (byte)0xEE, (byte)0x69, (byte)0x3A,
                (byte)0x1A, (byte)0x34, (byte)0x0E, (byte)0xCB, (byte)0xE5, (byte)0xE9,
                (byte)0xF0, (byte)0xB9, (byte)0x0C, (byte)0x92, (byte)0x97, (byte)0xE9,
                (byte)0x75, (byte)0xB9, (byte)0x1B, (byte)0x04, (byte)0x0F, (byte)0x93,
                (byte)0xC9, (byte)0x69, (byte)0xF7, (byte)0xB9, (byte)0xD1, (byte)0x68,
                (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3, (byte)0xD1,
                (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46, (byte)0xA3,
                (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D, (byte)0x46,
                (byte)0xA3, (byte)0xD1, (byte)0x68, (byte)0x34, (byte)0x1A, (byte)0x8D,
                (byte)0x46, (byte)0xA3, (byte)0xD1, (byte)0x00,

                (byte)0x34
        };

        SmsCbMessage msg = SmsCbMessage.createFromPdu(pdu);

        assertEquals("Unexpected update number decoded", 5, msg.getUpdateNumber());
    }
}







