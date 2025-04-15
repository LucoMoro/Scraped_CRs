/*Enhance WapPushOverSms/WspTypeDecoder to accept Content Type Parameters
and increase awareness of Well-Known content types.

This patch enables WspTypeDecoder to correctly parse content type
parameters as described in the Wap230 WSP specifications (wap-230-wsp-20010705-a
section 8.4.2.24) which are then passed on as part of the WAP_PUSH intent notification.

It also recognises all Well Known WSP Content types, and simplifies
their retrieval (i.e. a well known content type will always be available
through the WspTypeDecoder.getValueString() method).*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WapPushOverSms.java b/telephony/java/com/android/internal/telephony/WapPushOverSms.java
//Synthetic comment -- index 2b70162..2b58236 100644

//Synthetic comment -- @@ -22,8 +22,10 @@
import android.provider.Telephony.Sms.Intents;
import android.util.Config;
import android.util.Log;
import com.android.internal.telephony.gsm.SimUtils;


/**
* WAP push handler class.
//Synthetic comment -- @@ -102,77 +104,33 @@
if (Config.LOGD) Log.w(LOG_TAG, "Received PDU. Header Content-Type error.");
return;
}
        int binaryContentType;
String mimeType = pduDecoder.getValueString();
if (mimeType == null) {
            binaryContentType = (int)pduDecoder.getValue32();
            switch (binaryContentType) {
                case WspTypeDecoder.CONTENT_TYPE_B_DRM_RIGHTS_XML:
                    mimeType = WspTypeDecoder.CONTENT_MIME_TYPE_B_DRM_RIGHTS_XML;
                    break;
                case WspTypeDecoder.CONTENT_TYPE_B_DRM_RIGHTS_WBXML:
                    mimeType = WspTypeDecoder.CONTENT_MIME_TYPE_B_DRM_RIGHTS_WBXML;
                    break;
                case WspTypeDecoder.CONTENT_TYPE_B_PUSH_SI:
                    mimeType = WspTypeDecoder.CONTENT_MIME_TYPE_B_PUSH_SI;
                    break;
                case WspTypeDecoder.CONTENT_TYPE_B_PUSH_SL:
                    mimeType = WspTypeDecoder.CONTENT_MIME_TYPE_B_PUSH_SL;
                    break;
                case WspTypeDecoder.CONTENT_TYPE_B_PUSH_CO:
                    mimeType = WspTypeDecoder.CONTENT_MIME_TYPE_B_PUSH_CO;
                    break;
                case WspTypeDecoder.CONTENT_TYPE_B_MMS:
                    mimeType = WspTypeDecoder.CONTENT_MIME_TYPE_B_MMS;
                    break;
                default:
                    if (Config.LOGD) {
                        Log.w(LOG_TAG,
                                "Received PDU. Unsupported Content-Type = " + binaryContentType);
                    }
                return;
}
        } else {
            if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_DRM_RIGHTS_XML)) {
                binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_DRM_RIGHTS_XML;
            } else if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_DRM_RIGHTS_WBXML)) {
                binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_DRM_RIGHTS_WBXML;
            } else if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_PUSH_SI)) {
                binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_PUSH_SI;
            } else if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_PUSH_SL)) {
                binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_PUSH_SL;
            } else if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_PUSH_CO)) {
                binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_PUSH_CO;
            } else if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_MMS)) {
                binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_MMS;
            } else {
                if (Config.LOGD) Log.w(LOG_TAG, "Received PDU. Unknown Content-Type = " + mimeType);
                return;
            }
}
index += pduDecoder.getDecodedDataLength();

int dataIndex = headerStartIndex + headerLength;
        boolean dispatchedByApplication = false;
        switch (binaryContentType) {
            case WspTypeDecoder.CONTENT_TYPE_B_PUSH_CO:
                dispatchWapPdu_PushCO(pdu, transactionId, pduType);
                dispatchedByApplication = true;
                break;
            case WspTypeDecoder.CONTENT_TYPE_B_MMS:
                dispatchWapPdu_MMS(pdu, transactionId, pduType, dataIndex);
                dispatchedByApplication = true;
                break;
            default:
                break;
}
        if (dispatchedByApplication == false) {
            dispatchWapPdu_default(pdu, transactionId, pduType, mimeType, dataIndex);
        }
}

private void dispatchWapPdu_default(
            byte[] pdu, int transactionId, int pduType, String mimeType, int dataIndex) {
byte[] data;

data = new byte[pdu.length - dataIndex];
//Synthetic comment -- @@ -183,6 +141,7 @@
intent.putExtra("transactionId", transactionId);
intent.putExtra("pduType", pduType);
intent.putExtra("data", data);

sendBroadcast(intent, "android.permission.RECEIVE_WAP_PUSH");
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WspTypeDecoder.java b/telephony/java/com/android/internal/telephony/WspTypeDecoder.java
//Synthetic comment -- index 2984fa8..66ca1f9 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.internal.telephony;


/**
*  Implement the WSP data type decoder.
//Synthetic comment -- @@ -24,12 +25,174 @@
*/
public class WspTypeDecoder {

    private static final int WAP_PDU_SHORT_LENGTH_MAX = 30;
    private static final int WAP_PDU_LENGTH_QUOTE = 31;

public static final int PDU_TYPE_PUSH = 0x06;
public static final int PDU_TYPE_CONFIRMED_PUSH = 0x07;

public static final int CONTENT_TYPE_B_DRM_RIGHTS_XML = 0x4a;
public static final int CONTENT_TYPE_B_DRM_RIGHTS_WBXML = 0x4b;
public static final int CONTENT_TYPE_B_PUSH_SI = 0x2e;
//Synthetic comment -- @@ -54,6 +217,8 @@
long   unsigned32bit;
String stringValue;

public WspTypeDecoder(byte[] pdu) {
wspData = pdu;
}
//Synthetic comment -- @@ -65,7 +230,7 @@
*
* @return false when error(not a Text-string) occur
*         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeTextString(int startIndex) {
int index = startIndex;
//Synthetic comment -- @@ -82,13 +247,33 @@
}

/**
* Decode the "Short-integer" type for WSP pdu
*
* @param startIndex The starting position of the "Short-integer" in this pdu
*
* @return false when error(not a Short-integer) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeShortInteger(int startIndex) {
if ((wspData[startIndex] & 0x80) == 0) {
//Synthetic comment -- @@ -106,7 +291,7 @@
*
* @return false when error(not a Long-integer) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeLongInteger(int startIndex) {
int lengthMultiOctet = wspData[startIndex] & 0xff;
//Synthetic comment -- @@ -129,7 +314,7 @@
*
* @return false when error(not a Integer-Value) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeIntegerValue(int startIndex) {
if (decodeShortInteger(startIndex) == true) {
//Synthetic comment -- @@ -145,7 +330,7 @@
*
* @return false when error(not a Uintvar-integer) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeUintvarInteger(int startIndex) {
int  index = startIndex;
//Synthetic comment -- @@ -170,7 +355,7 @@
*
* @return false when error(not a Value-length) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeValueLength(int startIndex) {
if ((wspData[startIndex] & 0xff) > WAP_PDU_LENGTH_QUOTE) {
//Synthetic comment -- @@ -193,7 +378,7 @@
*
* @return false when error(not a Extension-media) occur
*         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeExtensionMedia(int startIndex) {
int index = startIndex;
//Synthetic comment -- @@ -212,7 +397,7 @@
*
* @return false when error(not a Constrained-encoding) occur
*         return value can be retrieved first by getValueString() and second by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeConstrainedEncoding(int startIndex) {
if (decodeShortInteger(startIndex) == true) {
//Synthetic comment -- @@ -227,31 +412,133 @@
*
* @param startIndex The starting position of the "Content-type" in this pdu
*
     * @return false when error(not a Content-type) occur
     *         return value can be retrieved first by getValueString() and second by getValue32()
     *         method length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeContentType(int startIndex) {
int mediaPrefixLength;
        long mediaFieldLength;

if (decodeValueLength(startIndex) == false) {
            return decodeConstrainedEncoding(startIndex);
}
mediaPrefixLength = getDecodedDataLength();
        mediaFieldLength = getValue32();
if (decodeIntegerValue(startIndex + mediaPrefixLength) == true) {
dataLength += mediaPrefixLength;
stringValue = null;
            return true;
}
if (decodeExtensionMedia(startIndex + mediaPrefixLength) == true) {
dataLength += mediaPrefixLength;
            return true;
}
return false;
}

/**
* Decode the "Content length" type for WSP pdu
*
//Synthetic comment -- @@ -259,7 +546,7 @@
*
* @return false when error(not a Content length) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeContentLength(int startIndex) {
return decodeIntegerValue(startIndex);
//Synthetic comment -- @@ -272,7 +559,7 @@
*
* @return false when error(not a Content location) occur
*         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeContentLocation(int startIndex) {
return decodeTextString(startIndex);
//Synthetic comment -- @@ -285,7 +572,7 @@
*
* @return false when error(not a X-Wap-Application-Id) occur
*         return value can be retrieved first by getValueString() and second by getValue32()
     *         method length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeXWapApplicationId(int startIndex) {
if (decodeIntegerValue(startIndex) == true) {
//Synthetic comment -- @@ -302,7 +589,7 @@
*
* @return false when error(not a X-Wap-Content-URI) occur
*         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeXWapContentURI(int startIndex) {
return decodeTextString(startIndex);
//Synthetic comment -- @@ -315,7 +602,7 @@
*
* @return false when error(not a X-Wap-Initiator-URI) occur
*         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeXWapInitiatorURI(int startIndex) {
return decodeTextString(startIndex);
//Synthetic comment -- @@ -341,4 +628,17 @@
public String getValueString() {
return stringValue;
}
}








//Synthetic comment -- diff --git a/tests/CoreTests/com/android/internal/telephony/TelephonyTests.java b/tests/CoreTests/com/android/internal/telephony/TelephonyTests.java
//Synthetic comment -- index eb2bd23..914cfd0 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.internal.telephony.gsm.SimPhoneBookTest;
import com.android.internal.telephony.gsm.SimSmsTest;
import com.android.internal.telephony.gsm.SimUtilsTest;

import junit.framework.TestSuite;

//Synthetic comment -- @@ -47,6 +48,7 @@
suite.addTestSuite(SimUtilsTest.class);
suite.addTestSuite(SimPhoneBookTest.class);
suite.addTestSuite(SimSmsTest.class);

return suite;
}








//Synthetic comment -- diff --git a/tests/CoreTests/com/android/internal/telephony/gsm/Wap230WspContentTypeTest.java b/tests/CoreTests/com/android/internal/telephony/gsm/Wap230WspContentTypeTest.java
new file mode 100644
//Synthetic comment -- index 0000000..de3df85

//Synthetic comment -- @@ -0,0 +1,737 @@







