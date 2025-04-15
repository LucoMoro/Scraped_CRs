/*Add support for national language tables in SMS alphabets

Implementation of the national languages feature for SMS.
3GPP 23.040 9.2.3.24.15 and 9.2.3.24.16 specifies how national
languages are implemented in SMS. 3GPP 23.038 specifies the
actual language tables.

The change also contains some tests for the national characters
support.*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/SmsMessage.java b/telephony/java/android/telephony/SmsMessage.java
//Synthetic comment -- index a284ea5..e03a1efb 100644

//Synthetic comment -- @@ -19,7 +19,8 @@
import android.os.Parcel;
import android.util.Log;

import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
//Synthetic comment -- @@ -254,23 +255,43 @@
*         space chars.  If false, and if the messageBody contains
*         non-7-bit encodable characters, length is calculated
*         using a 16-bit encoding.
     * @return an int[4] with int[0] being the number of SMS's
*         required, int[1] the number of code units used, and
*         int[2] is the number of code units remaining until the
*         next message. int[3] is an indicator of the encoding
*         code unit size (see the ENCODING_* definitions in this
     *         class).
*/
public static int[] calculateLength(CharSequence msgBody, boolean use7bitOnly) {
int activePhone = TelephonyManager.getDefault().getPhoneType();
TextEncodingDetails ted = (PHONE_TYPE_CDMA == activePhone) ?
com.android.internal.telephony.cdma.SmsMessage.calculateLength(msgBody, use7bitOnly) :
com.android.internal.telephony.gsm.SmsMessage.calculateLength(msgBody, use7bitOnly);
        int ret[] = new int[4];
ret[0] = ted.msgCount;
ret[1] = ted.codeUnitCount;
ret[2] = ted.codeUnitsRemaining;
ret[3] = ted.codeUnitSize;
return ret;
}

//Synthetic comment -- @@ -286,21 +307,14 @@
*/
public static ArrayList<String> fragmentText(String text) {
int activePhone = TelephonyManager.getDefault().getPhoneType();
TextEncodingDetails ted = (PHONE_TYPE_CDMA == activePhone) ?
com.android.internal.telephony.cdma.SmsMessage.calculateLength(text, false) :
com.android.internal.telephony.gsm.SmsMessage.calculateLength(text, false);

        // TODO(cleanup): The code here could be rolled into the logic
        // below cleanly if these MAX_* constants were defined more
        // flexibly...

        int limit;
        if (ted.msgCount > 1) {
            limit = (ted.codeUnitSize == ENCODING_7BIT) ?
                MAX_USER_DATA_SEPTETS_WITH_HEADER : MAX_USER_DATA_BYTES_WITH_HEADER;
        } else {
            limit = (ted.codeUnitSize == ENCODING_7BIT) ?
                MAX_USER_DATA_SEPTETS : MAX_USER_DATA_BYTES;
}

int pos = 0;  // Index in code units.
//Synthetic comment -- @@ -314,7 +328,9 @@
nextPos = pos + Math.min(limit, textLen - pos);
} else {
// For multi-segment messages, CDMA 7bit equals GSM 7bit encoding (EMS mode).
                    nextPos = GsmAlphabet.findGsmSeptetLimitIndex(text, pos, limit);
}
} else {  // Assume unicode.
nextPos = pos + Math.min(limit / 2, textLen - pos);
//Synthetic comment -- @@ -380,14 +396,14 @@
*/
public static SubmitPdu getSubmitPdu(String scAddress,
String destinationAddress, String message,
            boolean statusReportRequested, byte[] header) {
SubmitPduBase spb;
int activePhone = TelephonyManager.getDefault().getPhoneType();

if (PHONE_TYPE_CDMA == activePhone) {
spb = com.android.internal.telephony.cdma.SmsMessage.getSubmitPdu(scAddress,
destinationAddress, message, statusReportRequested,
                    SmsHeader.fromByteArray(header));
} else {
spb = com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(scAddress,
destinationAddress, message, statusReportRequested, header);








//Synthetic comment -- diff --git a/telephony/java/android/telephony/gsm/SmsMessage.java b/telephony/java/android/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 0c63c37..2f80d35 100644

//Synthetic comment -- @@ -375,7 +375,8 @@
SmsHeader.fromByteArray(header));
} else {
spb = com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(scAddress,
                    destinationAddress, message, statusReportRequested, header);
}

return new SubmitPdu(spb);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SmsHeader.java b/telephony/java/com/android/internal/telephony/SmsHeader.java
//Synthetic comment -- index 7872eec..4ad8a22 100644

//Synthetic comment -- @@ -18,17 +18,21 @@

import android.telephony.SmsMessage;

import com.android.internal.util.HexDump;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.util.ArrayList;

/**
* SMS user data header, as specified in TS 23.040 9.2.3.24.
*/
public class SmsHeader {

// TODO(cleanup): this datastructure is generally referred to as
// the 'user data header' or UDH, and so the class name should
//Synthetic comment -- @@ -66,6 +70,8 @@
public static final int ELT_ID_HYPERLINK_FORMAT_ELEMENT           = 0x21;
public static final int ELT_ID_REPLY_ADDRESS_ELEMENT              = 0x22;
public static final int ELT_ID_ENHANCED_VOICE_MAIL_INFORMATION    = 0x23;

public static final int PORT_WAP_PUSH = 2948;
public static final int PORT_WAP_WSP  = 9200;
//Synthetic comment -- @@ -95,6 +101,8 @@
public PortAddrs portAddrs;
public ConcatRef concatRef;
public ArrayList<MiscElt> miscEltList = new ArrayList<MiscElt>();

public SmsHeader() {}

//Synthetic comment -- @@ -157,6 +165,12 @@
portAddrs.areEightBits = false;
smsHeader.portAddrs = portAddrs;
break;
default:
MiscElt miscElt = new MiscElt();
miscElt.id = id;
//Synthetic comment -- @@ -174,9 +188,13 @@
* @return Byte array representing the SmsHeader
*/
public static byte[] toByteArray(SmsHeader smsHeader) {
        if ((smsHeader.portAddrs == null) &&
            (smsHeader.concatRef == null) &&
            (smsHeader.miscEltList.size() == 0)) {
return null;
}

//Synthetic comment -- @@ -196,6 +214,7 @@
outStream.write(concatRef.msgCount);
outStream.write(concatRef.seqNumber);
}
PortAddrs portAddrs = smsHeader.portAddrs;
if (portAddrs != null) {
if (portAddrs.areEightBits) {
//Synthetic comment -- @@ -212,14 +231,61 @@
outStream.write(portAddrs.origPort & 0x00FF);
}
}
for (MiscElt miscElt : smsHeader.miscEltList) {
outStream.write(miscElt.id);
outStream.write(miscElt.data.length);
outStream.write(miscElt.data, 0, miscElt.data.length);
}
return outStream.toByteArray();
}

@Override
public String toString() {
StringBuilder builder = new StringBuilder();
//Synthetic comment -- @@ -243,6 +309,8 @@
builder.append(", areEightBits=" + portAddrs.areEightBits);
builder.append(" }");
}
for (MiscElt miscElt : miscEltList) {
builder.append(", MiscElt ");
builder.append("{ id=" + miscElt.id);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SmsMessageBase.java b/telephony/java/com/android/internal/telephony/SmsMessageBase.java
//Synthetic comment -- index af6c5f8..2fab312 100644

//Synthetic comment -- @@ -18,6 +18,9 @@

import android.util.Log;
import com.android.internal.telephony.SmsHeader;
import java.util.Arrays;

import static android.telephony.SmsMessage.MessageClass;
//Synthetic comment -- @@ -118,6 +121,23 @@
*/
public int codeUnitSize;

@Override
public String toString() {
return "TextEncodingDetails " +
//Synthetic comment -- @@ -125,6 +145,9 @@
", codeUnitCount=" + codeUnitCount +
", codeUnitsRemaining=" + codeUnitsRemaining +
", codeUnitSize=" + codeUnitSize +
" }";
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmAlphabet.java b/telephony/java/com/android/internal/telephony/gsm/GsmAlphabet.java
new file mode 100644
//Synthetic comment -- index 0000000..7fbb6ab

//Synthetic comment -- @@ -0,0 +1,748 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmAlphabetTables.java b/telephony/java/com/android/internal/telephony/gsm/GsmAlphabetTables.java
new file mode 100644
//Synthetic comment -- index 0000000..0b3e6b9

//Synthetic comment -- @@ -0,0 +1,1939 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 40ee0b0..e704c44 100644

//Synthetic comment -- @@ -32,6 +32,7 @@

import com.android.internal.telephony.BaseCommands;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsHeader;
//Synthetic comment -- @@ -169,8 +170,20 @@
/** {@inheritDoc} */
protected void sendText(String destAddr, String scAddr, String text,
PendingIntent sentIntent, PendingIntent deliveryIntent) {
SmsMessage.SubmitPdu pdu = SmsMessage.getSubmitPdu(
                scAddr, destAddr, text, (deliveryIntent != null));
sendRawPdu(pdu.encodedScAddress, pdu.encodedMessage, sentIntent, deliveryIntent);
}

//Synthetic comment -- @@ -179,17 +192,25 @@
ArrayList<String> parts, ArrayList<PendingIntent> sentIntents,
ArrayList<PendingIntent> deliveryIntents) {

int refNumber = getNextConcatenatedRef() & 0x00FF;
int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

for (int i = 0; i < msgCount; i++) {
            TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
            if (encoding != details.codeUnitSize
                    && (encoding == android.telephony.SmsMessage.ENCODING_UNKNOWN
                            || encoding == android.telephony.SmsMessage.ENCODING_7BIT)) {
                encoding = details.codeUnitSize;
            }
}

for (int i = 0; i < msgCount; i++) {
//Synthetic comment -- @@ -204,7 +225,6 @@
// Note:  It's not sufficient to just flip this bit to true; it will have
// ripple effects (several calculations assume 8-bit ref).
concatRef.isEightBits = true;
            SmsHeader smsHeader = new SmsHeader();
smsHeader.concatRef = concatRef;

PendingIntent sentIntent = null;
//Synthetic comment -- @@ -218,8 +238,7 @@
}

SmsMessage.SubmitPdu pdus = SmsMessage.getSubmitPdu(scAddress, destinationAddress,
                    parts.get(i), deliveryIntent != null, SmsHeader.toByteArray(smsHeader),
                    encoding);

sendRawPdu(pdus.encodedScAddress, pdus.encodedMessage, sentIntent, deliveryIntent);
}
//Synthetic comment -- @@ -268,17 +287,24 @@
return;
}

int refNumber = getNextConcatenatedRef() & 0x00FF;
int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

for (int i = 0; i < msgCount; i++) {
            TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
            if (encoding != details.codeUnitSize
                    && (encoding == android.telephony.SmsMessage.ENCODING_UNKNOWN
                            || encoding == android.telephony.SmsMessage.ENCODING_7BIT)) {
                encoding = details.codeUnitSize;
            }
}

for (int i = 0; i < msgCount; i++) {
//Synthetic comment -- @@ -286,8 +312,7 @@
concatRef.refNumber = refNumber;
concatRef.seqNumber = i + 1;  // 1-based sequence
concatRef.msgCount = msgCount;
            concatRef.isEightBits = false;
            SmsHeader smsHeader = new SmsHeader();
smsHeader.concatRef = concatRef;

PendingIntent sentIntent = null;
//Synthetic comment -- @@ -301,8 +326,7 @@
}

SmsMessage.SubmitPdu pdus = SmsMessage.getSubmitPdu(scAddress, destinationAddress,
                    parts.get(i), deliveryIntent != null, SmsHeader.toByteArray(smsHeader),
                    encoding);

HashMap<String, Object> map = new HashMap<String, Object>();
map.put("smsc", pdus.encodedScAddress);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index f4c5e6c..8fd1cac 100644

//Synthetic comment -- @@ -23,11 +23,11 @@
import android.util.Log;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.SimRegionCache;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsMessageBase.TextEncodingDetails;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
//Synthetic comment -- @@ -39,7 +39,6 @@
import static android.telephony.SmsMessage.MAX_USER_DATA_BYTES;
import static android.telephony.SmsMessage.MAX_USER_DATA_BYTES_WITH_HEADER;
import static android.telephony.SmsMessage.MAX_USER_DATA_SEPTETS;
import static android.telephony.SmsMessage.MAX_USER_DATA_SEPTETS_WITH_HEADER;
import static android.telephony.SmsMessage.MessageClass;

/**
//Synthetic comment -- @@ -239,7 +238,7 @@
*/
public static SubmitPdu getSubmitPdu(String scAddress,
String destinationAddress, String message,
            boolean statusReportRequested, byte[] header) {
return getSubmitPdu(scAddress, destinationAddress, message, statusReportRequested, header,
ENCODING_UNKNOWN);
}
//Synthetic comment -- @@ -258,7 +257,7 @@
*/
public static SubmitPdu getSubmitPdu(String scAddress,
String destinationAddress, String message,
            boolean statusReportRequested, byte[] header, int encoding) {

// Perform null parameter checks.
if (message == null || destinationAddress == null) {
//Synthetic comment -- @@ -271,6 +270,7 @@
ByteArrayOutputStream bo = getSubmitPduHead(
scAddress, destinationAddress, mtiByte,
statusReportRequested, ret);
// User Data (and length)
byte[] userData;
if (encoding == ENCODING_UNKNOWN) {
//Synthetic comment -- @@ -279,10 +279,18 @@
}
try {
if (encoding == ENCODING_7BIT) {
                userData = GsmAlphabet.stringToGsm7BitPackedWithHeader(message, header);
} else { //assume UCS-2
try {
                    userData = encodeUCS2(message, header);
} catch(UnsupportedEncodingException uex) {
Log.e(LOG_TAG,
"Implausible UnsupportedEncodingException ",
//Synthetic comment -- @@ -294,7 +302,7 @@
// Encoding to the 7-bit alphabet failed. Let's see if we can
// send it as a UCS-2 encoded message
try {
                userData = encodeUCS2(message, header);
encoding = ENCODING_16BIT;
} catch(UnsupportedEncodingException uex) {
Log.e(LOG_TAG,
//Synthetic comment -- @@ -758,9 +766,15 @@
*/
String getUserDataGSM7Bit(int septetCount) {
String ret;

            ret = GsmAlphabet.gsm7BitPackedToString(pdu, cur, septetCount,
                    mUserDataSeptetPadding);

cur += (septetCount * 7) / 8;

//Synthetic comment -- @@ -824,28 +838,61 @@
*/
public static TextEncodingDetails calculateLength(CharSequence msgBody,
boolean use7bitOnly) {
        TextEncodingDetails ted = new TextEncodingDetails();
try {
            int septets = GsmAlphabet.countGsmSeptets(msgBody, !use7bitOnly);
            ted.codeUnitCount = septets;
            if (septets > MAX_USER_DATA_SEPTETS) {
                ted.msgCount = (septets + (MAX_USER_DATA_SEPTETS_WITH_HEADER - 1)) /
                        MAX_USER_DATA_SEPTETS_WITH_HEADER;
                ted.codeUnitsRemaining = (ted.msgCount *
                        MAX_USER_DATA_SEPTETS_WITH_HEADER) - septets;
} else {
ted.msgCount = 1;
                ted.codeUnitsRemaining = MAX_USER_DATA_SEPTETS - septets;
}
ted.codeUnitSize = ENCODING_7BIT;
} catch (EncodeException ex) {
int octets = msgBody.length() * 2;
ted.codeUnitCount = msgBody.length();
if (octets > MAX_USER_DATA_BYTES) {
                ted.msgCount = (octets + (MAX_USER_DATA_BYTES_WITH_HEADER - 1)) /
                        MAX_USER_DATA_BYTES_WITH_HEADER;
                ted.codeUnitsRemaining = ((ted.msgCount *
                        MAX_USER_DATA_BYTES_WITH_HEADER) - octets) / 2;
} else {
ted.msgCount = 1;
ted.codeUnitsRemaining = (MAX_USER_DATA_BYTES - octets)/2;








//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/com/android/internal/telephony/SmsNationalCharactersTest.java b/telephony/tests/telephonytests/src/com/android/internal/telephony/SmsNationalCharactersTest.java
new file mode 100644
//Synthetic comment -- index 0000000..6f0eb8e

//Synthetic comment -- @@ -0,0 +1,355 @@







