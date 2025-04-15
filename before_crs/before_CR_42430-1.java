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
import android.telephony.SmsCbMessage;
import android.telephony.cdma.CdmaSmsCbProgramData;
import android.text.format.Time;
import android.util.Log;
//Synthetic comment -- @@ -586,7 +585,6 @@
byte[] payload = encodeUtf16(uData.payloadStr);
int udhBytes = udhData.length + 1;  // Add length octet.
int udhCodeUnits = (udhBytes + 1) / 2;
        int udhPadding = udhBytes % 2;
int payloadCodeUnits = payload.length / 2;
uData.msgEncoding = UserData.ENCODING_UNICODE_16;
uData.msgEncodingSet = true;
//Synthetic comment -- @@ -594,7 +592,7 @@
uData.payload = new byte[uData.numFields * 2];
uData.payload[0] = (byte)udhData.length;
System.arraycopy(udhData, 0, uData.payload, 1, udhData.length);
        System.arraycopy(payload, 0, uData.payload, udhBytes + udhPadding, payload.length);
}

private static void encodeEmsUserDataPayload(UserData uData)
//Synthetic comment -- @@ -969,27 +967,37 @@
private static String decodeUtf8(byte[] data, int offset, int numFields)
throws CodingException
{
        if (numFields < 0 || (numFields + offset) > data.length) {
            throw new CodingException("UTF-8 decode failed: offset or length out of range");
        }
        try {
            return new String(data, offset, numFields, "UTF-8");
        } catch (java.io.UnsupportedEncodingException ex) {
            throw new CodingException("UTF-8 decode failed: " + ex);
        }
}

private static String decodeUtf16(byte[] data, int offset, int numFields)
throws CodingException
{
        int byteCount = numFields * 2;
        if (byteCount < 0 || (byteCount + offset) > data.length) {
            throw new CodingException("UTF-16 decode failed: offset or length out of range");
}
try {
            return new String(data, offset, byteCount, "utf-16be");
} catch (java.io.UnsupportedEncodingException ex) {
            throw new CodingException("UTF-16 decode failed: " + ex);
}
}

//Synthetic comment -- @@ -1045,14 +1053,7 @@
private static String decodeLatin(byte[] data, int offset, int numFields)
throws CodingException
{
        if (numFields < 0 || (numFields + offset) > data.length) {
            throw new CodingException("ISO-8859-1 decode failed: offset or length out of range");
        }
        try {
            return new String(data, offset, numFields, "ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException ex) {
            throw new CodingException("ISO-8859-1 decode failed: " + ex);
        }
}

private static void decodeUserDataPayload(UserData userData, boolean hasUserDataHeader)








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/cdma/sms/CdmaSmsTest.java b/tests/telephonytests/src/com/android/internal/telephony/cdma/sms/CdmaSmsTest.java
//Synthetic comment -- index bb37b65..850babe 100644

//Synthetic comment -- @@ -17,27 +17,28 @@
package com.android.internal.telephony.cdma.sms;

import android.telephony.TelephonyManager;

import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.cdma.SmsMessage;
import com.android.internal.telephony.cdma.sms.BearerData;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.telephony.cdma.sms.CdmaSmsAddress;
import com.android.internal.telephony.GsmAlphabet.TextEncodingDetails;
import com.android.internal.util.BitwiseInputStream;
import com.android.internal.util.BitwiseOutputStream;
import com.android.internal.util.HexDump;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import android.util.Log;

import java.util.ArrayList;

public class CdmaSmsTest extends AndroidTestCase {
    private final static String LOG_TAG = "XXX CdmaSmsTest XXX";

@SmallTest
public void testCdmaSmsAddrParsing() throws Exception {
//Synthetic comment -- @@ -811,23 +812,51 @@

@SmallTest
public void testUserDataHeaderWithEightCharMsg() throws Exception {
BearerData bearerData = new BearerData();
bearerData.messageType = BearerData.MESSAGE_TYPE_DELIVER;
bearerData.messageId = 55;
        SmsHeader.ConcatRef concatRef = new SmsHeader.ConcatRef();
        concatRef.refNumber = 0xEE;
        concatRef.msgCount = 2;
        concatRef.seqNumber = 2;
        concatRef.isEightBits = true;
SmsHeader smsHeader = new SmsHeader();
        smsHeader.concatRef = concatRef;
UserData userData = new UserData();
        userData.payloadStr = "01234567";
userData.userDataHeader = smsHeader;
bearerData.userData = userData;
byte[] encodedSms = BearerData.encode(bearerData);
BearerData revBearerData = BearerData.decode(encodedSms);
assertEquals(userData.payloadStr, revBearerData.userData.payloadStr);
}

@SmallTest
//Synthetic comment -- @@ -881,7 +910,27 @@
if (isCdmaPhone) {
ArrayList<String> fragments = android.telephony.SmsMessage.fragmentText(text2);
assertEquals(3, fragments.size());
}

}
}







