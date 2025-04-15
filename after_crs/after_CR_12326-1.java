/*Make multipart message follow 3GPP TS 23.040 V9.0.0 (2009-06), section 9.2.3.24.1 :
all the parts should have the same encoding*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaSMSDispatcher.java b/telephony/java/com/android/internal/telephony/cdma/CdmaSMSDispatcher.java
//Synthetic comment -- index ecdc8f6..fedb890 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsMessageBase.TextEncodingDetails;
import com.android.internal.telephony.cdma.SmsMessage;
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
//Synthetic comment -- @@ -286,8 +287,18 @@
*/

int refNumber = getNextConcatenatedRef() & 0x00FF;
        int msgCount = parts.size();
        int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

        for (int i = 0; i < msgCount; i++) {
            TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
            if (encoding != details.codeUnitSize
                    && (encoding == android.telephony.SmsMessage.ENCODING_UNKNOWN || encoding == android.telephony.SmsMessage.ENCODING_7BIT)) {
                encoding = details.codeUnitSize;
            }
        }

        for (int i = 0; i < msgCount; i++) {
SmsHeader.ConcatRef concatRef = new SmsHeader.ConcatRef();
concatRef.refNumber = refNumber;
concatRef.seqNumber = i + 1;  // 1-based sequence
//Synthetic comment -- @@ -309,6 +320,11 @@
UserData uData = new UserData();
uData.payloadStr = parts.get(i);
uData.userDataHeader = smsHeader;
            if(encoding == android.telephony.SmsMessage.ENCODING_7BIT){
                uData.msgEncoding = UserData.ENCODING_GSM_7BIT_ALPHABET;
            }else{ // assume UTF-16
                uData.msgEncoding = UserData.ENCODING_UNICODE_16;
            }

SmsMessage.SubmitPdu submitPdu = SmsMessage.getSubmitPdu(destAddr,
uData, deliveryIntent != null);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 2770ddc..3cb4f1ab 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.util.Log;

import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.SmsMessageBase.TextEncodingDetails;
import com.android.internal.telephony.gsm.SmsMessage;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.SMSDispatcher;
//Synthetic comment -- @@ -142,8 +143,18 @@
ArrayList<PendingIntent> deliveryIntents) {

int refNumber = getNextConcatenatedRef() & 0x00FF;
        int msgCount = parts.size();
        int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

        for (int i = 0; i < msgCount; i++) {
            TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
            if (encoding != details.codeUnitSize
                    && (encoding == android.telephony.SmsMessage.ENCODING_UNKNOWN || encoding == android.telephony.SmsMessage.ENCODING_7BIT)) {
                encoding = details.codeUnitSize;
            }
        }

        for (int i = 0; i < msgCount; i++) {
SmsHeader.ConcatRef concatRef = new SmsHeader.ConcatRef();
concatRef.refNumber = refNumber;
concatRef.seqNumber = i + 1;  // 1-based sequence
//Synthetic comment -- @@ -169,7 +180,7 @@
}

SmsMessage.SubmitPdu pdus = SmsMessage.getSubmitPdu(scAddress, destinationAddress,
                    parts.get(i), deliveryIntent != null, SmsHeader.toByteArray(smsHeader), encoding);

sendRawPdu(pdus.encodedScAddress, pdus.encodedMessage, sentIntent, deliveryIntent);
}
//Synthetic comment -- @@ -219,8 +230,18 @@
}

int refNumber = getNextConcatenatedRef() & 0x00FF;
        int msgCount = parts.size();
        int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

        for (int i = 0; i < msgCount; i++) {
            TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
            if (encoding != details.codeUnitSize
                    && (encoding == android.telephony.SmsMessage.ENCODING_UNKNOWN || encoding == android.telephony.SmsMessage.ENCODING_7BIT)) {
                encoding = details.codeUnitSize;
            }
        }

        for (int i = 0; i < msgCount; i++) {
SmsHeader.ConcatRef concatRef = new SmsHeader.ConcatRef();
concatRef.refNumber = refNumber;
concatRef.seqNumber = i + 1;  // 1-based sequence
//Synthetic comment -- @@ -240,7 +261,7 @@
}

SmsMessage.SubmitPdu pdus = SmsMessage.getSubmitPdu(scAddress, destinationAddress,
                    parts.get(i), deliveryIntent != null, SmsHeader.toByteArray(smsHeader), encoding);

HashMap<String, Object> map = new HashMap<String, Object>();
map.put("smsc", pdus.encodedScAddress);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index af59126..dca6df1 100644

//Synthetic comment -- @@ -225,6 +225,24 @@
public static SubmitPdu getSubmitPdu(String scAddress,
String destinationAddress, String message,
boolean statusReportRequested, byte[] header) {
        return getSubmitPdu(scAddress, destinationAddress, message, statusReportRequested, header, ENCODING_UNKNOWN);
    }
    
    
    /**
     * Get an SMS-SUBMIT PDU for a destination address and a message using the
     * specified encoding.
     *
     * @param scAddress Service Centre address.  Null means use default.
     * @param encoding Encoding defined by constants in android.telephony.SmsMessage.ENCODING_*
     * @return a <code>SubmitPdu</code> containing the encoded SC
     *         address, if applicable, and the encoded message.
     *         Returns null on encode error.
     * @hide
     */
    public static SubmitPdu getSubmitPdu(String scAddress,
            String destinationAddress, String message,
            boolean statusReportRequested, byte[] header, int encoding) {

// Perform null parameter checks.
if (message == null || destinationAddress == null) {
//Synthetic comment -- @@ -237,18 +255,43 @@
ByteArrayOutputStream bo = getSubmitPduHead(
scAddress, destinationAddress, mtiByte,
statusReportRequested, ret);
        // User Data (and length)
        byte[] userData;
        if(encoding == ENCODING_UNKNOWN){
// First, try encoding it with the GSM alphabet
            encoding = ENCODING_7BIT;
        }
        try {
            if(encoding == ENCODING_7BIT){
                userData = GsmAlphabet.stringToGsm7BitPackedWithHeader(message, header);
            }else{ //assume UCS-2
                try{
                    userData = encodeUCS2(message, header);
                }catch(UnsupportedEncodingException uex){
                    Log.e(LOG_TAG,
                            "Implausible UnsupportedEncodingException ",
                            uex);
                    return null;
                }
            }
        } catch (EncodeException ex) {
            // Encoding to the 7-bit alphabet failed. Let's see if we can
            // send it as a UCS-2 encoded message
            try{
                userData = encodeUCS2(message, header);
            }catch(UnsupportedEncodingException uex){
                Log.e(LOG_TAG,
                        "Implausible UnsupportedEncodingException ",
                        uex);
                return null;
            }
        }
        
        if(encoding == ENCODING_7BIT){
if ((0xff & userData[0]) > MAX_USER_DATA_SEPTETS) {
// Message too long
return null;
}
// TP-Data-Coding-Scheme
// Default encoding, uncompressed
// To test writing messages to the SIM card, change this value 0x00
//Synthetic comment -- @@ -258,58 +301,50 @@
// the receiver's SIM card. You can then send messages to yourself
// (on a phone with this change) and they'll end up on the SIM card.
bo.write(0x00);
        }else{ //assume UCS-2
            if ((0xff & userData[0]) > MAX_USER_DATA_BYTES) {
// Message too long
return null;
}
// TP-Data-Coding-Scheme
// Class 3, UCS-2 encoding, uncompressed
bo.write(0x0b);
}
        
        // (no TP-Validity-Period)
        bo.write(userData, 0, userData.length);
ret.encodedMessage = bo.toByteArray();
return ret;
}

/**
     * Packs header and UCS-2 encoded message. Includes TP-UDL & TP-UDHL if necessary
     * 
     * @return
     * @throws UnsupportedEncodingException 
     */
    private static byte[] encodeUCS2(String message, byte[] header) throws UnsupportedEncodingException {
        byte[] userData, textPart;
        textPart = message.getBytes("utf-16be");

        if (header != null) {
            // Need 1 byte for UDHL
            userData = new byte[header.length + textPart.length + 1];

            userData[0] = (byte)header.length;
            System.arraycopy(header, 0, userData, 1, header.length);
            System.arraycopy(textPart, 0, userData, header.length + 1, textPart.length);
        }
        else {
            userData = textPart;
        }
        byte[] ret = new byte[userData.length+1];
        ret[0] = (byte) (userData.length & 0xff );
        System.arraycopy(userData, 0, ret, 1, userData.length);
        return ret;
    }

    /**
* Get an SMS-SUBMIT PDU for a destination address and a message
*
* @param scAddress Service Centre address.  Null means use default.







