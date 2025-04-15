/*Adding support for parsing of SMS-CB pdus in UMTS format.

This enables RIL to dispatch pdus in both GSM or UMTS format.
The RAT that was used will be transparent to clients.

Change-Id:I57c53ec79496d274e18b3fee196551af97c0857d*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/SmsCbMessage.java b/telephony/java/android/telephony/SmsCbMessage.java
//Synthetic comment -- index 3543275..5608402 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package android.telephony;

import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.gsm.SmsCbHeader;

//Synthetic comment -- @@ -58,10 +60,13 @@
try {
return new SmsCbMessage(pdu);
} catch (IllegalArgumentException e) {
return null;
}
}

/**
* Languages in the 0000xxxx DCS group as defined in 3GPP TS 23.038, section 5.
*/
//Synthetic comment -- @@ -80,6 +85,8 @@

private static final char CARRIAGE_RETURN = 0x0d;

private SmsCbHeader mHeader;

private String mLanguage;
//Synthetic comment -- @@ -149,6 +156,13 @@
return mHeader.updateNumber;
}

private void parseBody(byte[] pdu) {
int encoding;
boolean hasLanguageIndicator = false;
//Synthetic comment -- @@ -221,28 +235,81 @@
break;
}

switch (encoding) {
case SmsMessage.ENCODING_7BIT:
                mBody = GsmAlphabet.gsm7BitPackedToString(pdu, SmsCbHeader.PDU_HEADER_LENGTH,
                        (pdu.length - SmsCbHeader.PDU_HEADER_LENGTH) * 8 / 7);

                if (hasLanguageIndicator && mBody != null && mBody.length() > 2) {
                    mLanguage = mBody.substring(0, 2);
                    mBody = mBody.substring(3);
}
break;

case SmsMessage.ENCODING_16BIT:
                int offset = SmsCbHeader.PDU_HEADER_LENGTH;

                if (hasLanguageIndicator && pdu.length >= SmsCbHeader.PDU_HEADER_LENGTH + 2) {
                    mLanguage = GsmAlphabet.gsm7BitPackedToString(pdu,
                            SmsCbHeader.PDU_HEADER_LENGTH, 2);
offset += 2;
}

try {
                    mBody = new String(pdu, offset, (pdu.length & 0xfffe) - offset, "utf-16");
} catch (UnsupportedEncodingException e) {
// Eeeek
}
//Synthetic comment -- @@ -252,16 +319,18 @@
break;
}

        if (mBody != null) {
// Remove trailing carriage return
            for (int i = mBody.length() - 1; i >= 0; i--) {
                if (mBody.charAt(i) != CARRIAGE_RETURN) {
                    mBody = mBody.substring(0, i + 1);
break;
}
}
} else {
            mBody = "";
}
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsCbHeader.java b/telephony/java/com/android/internal/telephony/gsm/SmsCbHeader.java
//Synthetic comment -- index 5f27cfc..23319c0 100644

//Synthetic comment -- @@ -17,8 +17,31 @@
package com.android.internal.telephony.gsm;

public class SmsCbHeader {
public static final int PDU_HEADER_LENGTH = 6;

public final int geographicalScope;

public final int messageCode;
//Synthetic comment -- @@ -33,27 +56,55 @@

public final int nrOfPages;

public SmsCbHeader(byte[] pdu) throws IllegalArgumentException {
if (pdu == null || pdu.length < PDU_HEADER_LENGTH) {
throw new IllegalArgumentException("Illegal PDU");
}

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
}
}








//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/com/android/internal/telephony/GsmSmsCbTest.java b/telephony/tests/telephonytests/src/com/android/internal/telephony/GsmSmsCbTest.java
//Synthetic comment -- index 7136ea0..b131a01 100644

//Synthetic comment -- @@ -69,6 +69,36 @@
doTestGeographicalScopeValue(pdu, (byte)0xC0, SmsCbMessage.GEOGRAPHICAL_SCOPE_CELL_WIDE);
}

public void testGetMessageBody7Bit() {
byte[] pdu = {
(byte)0xC0, (byte)0x00, (byte)0x00, (byte)0x32, (byte)0x40, (byte)0x11, (byte)0x41,
//Synthetic comment -- @@ -92,6 +122,83 @@
msg.getMessageBody());
}

public void testGetMessageBody7BitFull() {
byte[] pdu = {
(byte)0xC0, (byte)0x00, (byte)0x00, (byte)0x32, (byte)0x40, (byte)0x11, (byte)0x41,
//Synthetic comment -- @@ -117,6 +224,38 @@
msg.getMessageBody());
}

public void testGetMessageBody7BitWithLanguage() {
byte[] pdu = {
(byte)0xC0, (byte)0x00, (byte)0x00, (byte)0x32, (byte)0x04, (byte)0x11, (byte)0x41,
//Synthetic comment -- @@ -167,6 +306,38 @@
assertEquals("Unexpected language indicator decoded", "sv", msg.getLanguageCode());
}

public void testGetMessageBody8Bit() {
byte[] pdu = {
(byte)0xC0, (byte)0x00, (byte)0x00, (byte)0x32, (byte)0x44, (byte)0x11, (byte)0x41,
//Synthetic comment -- @@ -210,6 +381,81 @@
"A UCS2 message containing a \u0434 character", msg.getMessageBody());
}

public void testGetMessageBodyUcs2WithLanguageInBody() {
byte[] pdu = {
(byte)0xC0, (byte)0x00, (byte)0x00, (byte)0x32, (byte)0x11, (byte)0x11, (byte)0x78,
//Synthetic comment -- @@ -234,6 +480,37 @@
assertEquals("Unexpected language indicator decoded", "xx", msg.getLanguageCode());
}

public void testGetMessageIdentifier() {
byte[] pdu = {
(byte)0xC0, (byte)0x00, (byte)0x30, (byte)0x39, (byte)0x40, (byte)0x11, (byte)0x41,
//Synthetic comment -- @@ -256,6 +533,35 @@
assertEquals("Unexpected message identifier decoded", 12345, msg.getMessageIdentifier());
}

public void testGetMessageCode() {
byte[] pdu = {
(byte)0x2A, (byte)0xA5, (byte)0x30, (byte)0x39, (byte)0x40, (byte)0x11, (byte)0x41,
//Synthetic comment -- @@ -278,6 +584,35 @@
assertEquals("Unexpected message code decoded", 682, msg.getMessageCode());
}

public void testGetUpdateNumber() {
byte[] pdu = {
(byte)0x2A, (byte)0xA5, (byte)0x30, (byte)0x39, (byte)0x40, (byte)0x11, (byte)0x41,
//Synthetic comment -- @@ -299,4 +634,33 @@

assertEquals("Unexpected update number decoded", 5, msg.getUpdateNumber());
}
}







