/*Fix CDMA decoding of multipart UTF-16 SMS messages.

Recent changes to support CMAS over CDMA introduced a bug causing
an exception to be thrown when decoding multipart UTF-16 encoded
messages. This change fixes the exception by correctly subtracting
the header size from the number of bytes to decode. It also adds
more robust error handling to try to decode the maximum length
possible instead of throwing an exception if the length is still
larger than the user data length after subtracting the header.

This also fixes a bug in the encoder, which was padding the
UTF-16 user data to 16-bit alignment, which is incorrect (should
be padded to an 8-bit boundary). The code happened to work because
we always generated a UDH that was an even number of bytes
(including length) so the padding was a no-op. The decoder works
correctly.

Bug: 6939151
Change-Id:I4000fa2f4703b39e5ed7e5bd8490828303ef8979*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/sms/BearerData.java b/src/java/com/android/internal/telephony/cdma/sms/BearerData.java
old mode 100755
new mode 100644
//Synthetic comment -- index 7e7347f..914d86f

//Synthetic comment -- @@ -18,7 +18,6 @@

import android.content.res.Resources;
import android.telephony.SmsCbCmasInfo;
import android.telephony.cdma.CdmaSmsCbProgramData;
import android.text.format.Time;
import android.util.Log;
//Synthetic comment -- @@ -586,7 +585,6 @@
byte[] payload = encodeUtf16(uData.payloadStr);
int udhBytes = udhData.length + 1;  // Add length octet.
int udhCodeUnits = (udhBytes + 1) / 2;
int payloadCodeUnits = payload.length / 2;
uData.msgEncoding = UserData.ENCODING_UNICODE_16;
uData.msgEncodingSet = true;
//Synthetic comment -- @@ -594,7 +592,7 @@
uData.payload = new byte[uData.numFields * 2];
uData.payload[0] = (byte)udhData.length;
System.arraycopy(udhData, 0, uData.payload, 1, udhData.length);
        System.arraycopy(payload, 0, uData.payload, udhBytes, payload.length);
}

private static void encodeEmsUserDataPayload(UserData uData)
//Synthetic comment -- @@ -969,27 +967,37 @@
private static String decodeUtf8(byte[] data, int offset, int numFields)
throws CodingException
{
        return decodeCharset(data, offset, numFields, 1, "UTF-8");
}

private static String decodeUtf16(byte[] data, int offset, int numFields)
throws CodingException
{
        // Subtract header and possible padding byte (at end) from num fields.
        int padding = offset % 2;
        numFields -= (offset + padding) / 2;
        return decodeCharset(data, offset, numFields, 2, "utf-16be");
    }

    private static String decodeCharset(byte[] data, int offset, int numFields, int width,
            String charset) throws CodingException
    {
        if (numFields < 0 || (numFields * width + offset) > data.length) {
            // Try to decode the max number of characters in payload
            int padding = offset % width;
            int maxNumFields = (data.length - offset - padding) / width;
            if (maxNumFields < 0) {
                throw new CodingException(charset + " decode failed: offset out of range");
            }
            Log.e(LOG_TAG, charset + " decode error: offset = " + offset + " numFields = "
                    + numFields + " data.length = " + data.length + " maxNumFields = "
                    + maxNumFields);
            numFields = maxNumFields;
}
try {
            return new String(data, offset, numFields * width, charset);
} catch (java.io.UnsupportedEncodingException ex) {
            throw new CodingException(charset + " decode failed: " + ex);
}
}

//Synthetic comment -- @@ -1045,14 +1053,7 @@
private static String decodeLatin(byte[] data, int offset, int numFields)
throws CodingException
{
        return decodeCharset(data, offset, numFields, 1, "ISO-8859-1");
}

private static void decodeUserDataPayload(UserData userData, boolean hasUserDataHeader)








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/cdma/sms/CdmaSmsTest.java b/tests/telephonytests/src/com/android/internal/telephony/cdma/sms/CdmaSmsTest.java
//Synthetic comment -- index bb37b65..850babe 100644

//Synthetic comment -- @@ -17,27 +17,28 @@
package com.android.internal.telephony.cdma.sms;

import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.android.internal.telephony.GsmAlphabet.TextEncodingDetails;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.cdma.SmsMessage;
import com.android.internal.util.HexDump;

import java.util.ArrayList;
import java.util.Arrays;

public class CdmaSmsTest extends AndroidTestCase {

    // CJK ideographs, Hiragana, Katakana, full width letters, Cyrillic, etc.
    private static final String sUnicodeChars = "\u4e00\u4e01\u4e02\u4e03" +
            "\u4e04\u4e05\u4e06\u4e07\u4e08\u4e09\u4e0a\u4e0b\u4e0c\u4e0d" +
            "\u4e0e\u4e0f\u3041\u3042\u3043\u3044\u3045\u3046\u3047\u3048" +
            "\u30a1\u30a2\u30a3\u30a4\u30a5\u30a6\u30a7\u30a8" +
            "\uff10\uff11\uff12\uff13\uff14\uff15\uff16\uff17\uff18" +
            "\uff70\uff71\uff72\uff73\uff74\uff75\uff76\uff77\uff78" +
            "\u0400\u0401\u0402\u0403\u0404\u0405\u0406\u0407\u0408" +
            "\u00a2\u00a9\u00ae\u2122";

@SmallTest
public void testCdmaSmsAddrParsing() throws Exception {
//Synthetic comment -- @@ -811,23 +812,51 @@

@SmallTest
public void testUserDataHeaderWithEightCharMsg() throws Exception {
        encodeDecodeAssertEquals("01234567", 2, 2, false);
    }

    private void encodeDecodeAssertEquals(String payload, int index, int total,
            boolean oddLengthHeader) throws Exception {
BearerData bearerData = new BearerData();
bearerData.messageType = BearerData.MESSAGE_TYPE_DELIVER;
bearerData.messageId = 55;
SmsHeader smsHeader = new SmsHeader();
        if (oddLengthHeader) {
            // Odd length header to verify correct UTF-16 header padding
            SmsHeader.MiscElt miscElt = new SmsHeader.MiscElt();
            miscElt.id = 0x27;  // reserved for future use; ignored on decode
            miscElt.data = new byte[]{0x12, 0x34};
            smsHeader.miscEltList.add(miscElt);
        } else {
            // Even length header normally generated for concatenated SMS.
            SmsHeader.ConcatRef concatRef = new SmsHeader.ConcatRef();
            concatRef.refNumber = 0xEE;
            concatRef.msgCount = total;
            concatRef.seqNumber = index;
            concatRef.isEightBits = true;
            smsHeader.concatRef = concatRef;
        }
        byte[] encodeHeader = SmsHeader.toByteArray(smsHeader);
        if (oddLengthHeader) {
            assertEquals(4, encodeHeader.length);     // 5 bytes with UDH length
        } else {
            assertEquals(5, encodeHeader.length);     // 6 bytes with UDH length
        }
UserData userData = new UserData();
        userData.payloadStr = payload;
userData.userDataHeader = smsHeader;
bearerData.userData = userData;
byte[] encodedSms = BearerData.encode(bearerData);
BearerData revBearerData = BearerData.decode(encodedSms);
assertEquals(userData.payloadStr, revBearerData.userData.payloadStr);
        assertTrue(revBearerData.hasUserDataHeader);
        byte[] header = SmsHeader.toByteArray(revBearerData.userData.userDataHeader);
        if (oddLengthHeader) {
            assertEquals(4, header.length);     // 5 bytes with UDH length
        } else {
            assertEquals(5, header.length);     // 6 bytes with UDH length
        }
        assertTrue(Arrays.equals(encodeHeader, header));
}

@SmallTest
//Synthetic comment -- @@ -881,7 +910,27 @@
if (isCdmaPhone) {
ArrayList<String> fragments = android.telephony.SmsMessage.fragmentText(text2);
assertEquals(3, fragments.size());

            for (int i = 0; i < 3; i++) {
                encodeDecodeAssertEquals(fragments.get(i), i + 1, 3, false);
                encodeDecodeAssertEquals(fragments.get(i), i + 1, 3, true);
            }
}

        // Test case for multi-part UTF-16 message.
        String text3 = sUnicodeChars + sUnicodeChars + sUnicodeChars;
        ted = SmsMessage.calculateLength(text3, false);
        assertEquals(3, ted.msgCount);
        assertEquals(189, ted.codeUnitCount);
        assertEquals(3, ted.codeUnitSize);
        if (isCdmaPhone) {
            ArrayList<String> fragments = android.telephony.SmsMessage.fragmentText(text3);
            assertEquals(3, fragments.size());

            for (int i = 0; i < 3; i++) {
                encodeDecodeAssertEquals(fragments.get(i), i + 1, 3, false);
                encodeDecodeAssertEquals(fragments.get(i), i + 1, 3, true);
            }
        }
}
}







