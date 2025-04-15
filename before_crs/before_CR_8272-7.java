/*Enhance WapPushOverSms/WspTypeDecoder to accept Content Type Parameters
and increase awareness of Well-Known content types.

This patch enables WspTypeDecoder to correctly parse content type
parameters as described in the Wap230 WSP specifications (wap-230-wsp-20010705-a
 section 8.4.2.24) which are then passed on as part of the WAP_PUSH intent notification.

It also recognises all Well Known WSP Content types, and simplifies
their retrieval (i.e. a well known content type will always be available
through the WspTypeDecoder.getValueString() method).

Change-Id:I0eb3f9ac287aa7cb53312777c4be54b1939fa857*/
//Synthetic comment -- diff --git a/core/java/android/provider/Telephony.java b/core/java/android/provider/Telephony.java
//Synthetic comment -- index d8c5a53..122478c 100644

//Synthetic comment -- @@ -542,14 +542,23 @@
* values:</p>
*
* <ul>
             *   <li><em>transactionId (Integer)</em> - The WAP transaction
             *    ID</li>
*   <li><em>pduType (Integer)</em> - The WAP PDU type</li>
*   <li><em>data</em> - The data payload of the message</li>
* </ul>
*
* <p>If a BroadcastReceiver encounters an error while processing
* this intent it should set the result code appropriately.</p>
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
public static final String WAP_PUSH_RECEIVED_ACTION =
//Synthetic comment -- @@ -562,7 +571,7 @@
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
public static final String SIM_FULL_ACTION =
                "android.provider.Telephony.SIM_FULL";

/**
* Broadcast Action: An incoming SMS has been rejected by the








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WapPushOverSms.java b/telephony/java/com/android/internal/telephony/WapPushOverSms.java
//Synthetic comment -- index 99709406..69a492c 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.util.Config;
import android.util.Log;


/**
* WAP push handler class.
//Synthetic comment -- @@ -59,7 +60,7 @@
*/
public int dispatchWapPdu(byte[] pdu) {

        if (Config.LOGD) Log.d(LOG_TAG, "Rx: " + IccUtils.bytesToHexString(pdu));

int index = 0;
int transactionId = pdu[index++] & 0xFF;
//Synthetic comment -- @@ -68,7 +69,7 @@

if ((pduType != WspTypeDecoder.PDU_TYPE_PUSH) &&
(pduType != WspTypeDecoder.PDU_TYPE_CONFIRMED_PUSH)) {
            if (Config.LOGD) Log.w(LOG_TAG, "Received non-PUSH WAP PDU. Type = " + pduType);
return Intents.RESULT_SMS_HANDLED;
}

//Synthetic comment -- @@ -81,7 +82,7 @@
* So it will be encoded in no more than 5 octets.
*/
if (pduDecoder.decodeUintvarInteger(index) == false) {
            if (Config.LOGD) Log.w(LOG_TAG, "Received PDU. Header Length error.");
return Intents.RESULT_SMS_GENERIC_ERROR;
}
headerLength = (int)pduDecoder.getValue32();
//Synthetic comment -- @@ -102,10 +103,12 @@
* Length = Uintvar-integer
*/
if (pduDecoder.decodeContentType(index) == false) {
            if (Config.LOGD) Log.w(LOG_TAG, "Received PDU. Header Content-Type error.");
return Intents.RESULT_SMS_GENERIC_ERROR;
}
int binaryContentType;
String mimeType = pduDecoder.getValueString();
if (mimeType == null) {
binaryContentType = (int)pduDecoder.getValue32();
//Synthetic comment -- @@ -129,7 +132,7 @@
mimeType = WspTypeDecoder.CONTENT_MIME_TYPE_B_MMS;
break;
default:
                    if (Config.LOGD) {
Log.w(LOG_TAG,
"Received PDU. Unsupported Content-Type = " + binaryContentType);
}
//Synthetic comment -- @@ -149,34 +152,30 @@
} else if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_MMS)) {
binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_MMS;
} else {
                if (Config.LOGD) Log.w(LOG_TAG, "Received PDU. Unknown Content-Type = " + mimeType);
return Intents.RESULT_SMS_HANDLED;
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
return Activity.RESULT_OK;
}

private void dispatchWapPdu_default(
            byte[] pdu, int transactionId, int pduType, String mimeType, int dataIndex) {
byte[] data;

data = new byte[pdu.length - dataIndex];
//Synthetic comment -- @@ -187,6 +186,7 @@
intent.putExtra("transactionId", transactionId);
intent.putExtra("pduType", pduType);
intent.putExtra("data", data);

mSmsDispatcher.dispatch(intent, "android.permission.RECEIVE_WAP_PUSH");
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WspTypeDecoder.java b/telephony/java/com/android/internal/telephony/WspTypeDecoder.java
//Synthetic comment -- index 3bbe0e1..8f9cdcf 100644

//Synthetic comment -- @@ -16,20 +16,181 @@

package com.android.internal.telephony;


/**
 *  Implement the WSP data type decoder.
*
 *  @hide
*/
public class WspTypeDecoder {

    private static final int WAP_PDU_SHORT_LENGTH_MAX = 30;
    private static final int WAP_PDU_LENGTH_QUOTE = 31;

public static final int PDU_TYPE_PUSH = 0x06;
public static final int PDU_TYPE_CONFIRMED_PUSH = 0x07;

public static final int CONTENT_TYPE_B_DRM_RIGHTS_XML = 0x4a;
public static final int CONTENT_TYPE_B_DRM_RIGHTS_WBXML = 0x4b;
public static final int CONTENT_TYPE_B_PUSH_SI = 0x2e;
//Synthetic comment -- @@ -48,12 +209,13 @@

public static final int PARAMETER_ID_X_WAP_APPLICATION_ID = 0x2f;


byte[] wspData;
int    dataLength;
long   unsigned32bit;
String stringValue;

public WspTypeDecoder(byte[] pdu) {
wspData = pdu;
}
//Synthetic comment -- @@ -64,17 +226,17 @@
* @param startIndex The starting position of the "Text-string" in this pdu
*
* @return false when error(not a Text-string) occur
     *         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeTextString(int startIndex) {
int index = startIndex;
while (wspData[index] != 0) {
index++;
}
        dataLength  = index - startIndex + 1;
if (wspData[startIndex] == 127) {
            stringValue = new String(wspData, startIndex+1, dataLength - 2);
} else {
stringValue = new String(wspData, startIndex, dataLength - 1);
}
//Synthetic comment -- @@ -82,13 +244,33 @@
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
//Synthetic comment -- @@ -106,7 +288,7 @@
*
* @return false when error(not a Long-integer) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeLongInteger(int startIndex) {
int lengthMultiOctet = wspData[startIndex] & 0xff;
//Synthetic comment -- @@ -115,10 +297,10 @@
return false;
}
unsigned32bit = 0;
        for (int i=1; i<=lengthMultiOctet; i++) {
            unsigned32bit = (unsigned32bit << 8) | (wspData[startIndex+i] & 0xff);
}
        dataLength = 1+lengthMultiOctet;
return true;
}

//Synthetic comment -- @@ -129,7 +311,7 @@
*
* @return false when error(not a Integer-Value) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeIntegerValue(int startIndex) {
if (decodeShortInteger(startIndex) == true) {
//Synthetic comment -- @@ -145,10 +327,10 @@
*
* @return false when error(not a Uintvar-integer) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeUintvarInteger(int startIndex) {
        int  index = startIndex;

unsigned32bit = 0;
while ((wspData[index] & 0x80) != 0) {
//Synthetic comment -- @@ -170,7 +352,7 @@
*
* @return false when error(not a Value-length) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeValueLength(int startIndex) {
if ((wspData[startIndex] & 0xff) > WAP_PDU_LENGTH_QUOTE) {
//Synthetic comment -- @@ -180,22 +362,22 @@
unsigned32bit = wspData[startIndex];
dataLength = 1;
} else {
            decodeUintvarInteger(startIndex+1);
            dataLength ++;
}
return true;
}

/**
    * Decode the "Extension-media" type for WSP PDU.
    *
    * @param startIndex The starting position of the "Extension-media" in this PDU.
    *
    * @return false on error, such as if there is no Extension-media at startIndex.
    * Side-effects: updates stringValue (available with getValueString()), which will be
    * null on error. The length of the data in the PDU is available with getValue32(), 0
    * on error.
    */
public boolean decodeExtensionMedia(int startIndex) {
int index = startIndex;
dataLength = 0;
//Synthetic comment -- @@ -207,7 +389,7 @@
index++;
}

        dataLength  = index - startIndex + 1;
stringValue = new String(wspData, startIndex, dataLength - 1);

return rtrn;
//Synthetic comment -- @@ -220,7 +402,7 @@
*
* @return false when error(not a Constrained-encoding) occur
*         return value can be retrieved first by getValueString() and second by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeConstrainedEncoding(int startIndex) {
if (decodeShortInteger(startIndex) == true) {
//Synthetic comment -- @@ -235,31 +417,152 @@
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
//Synthetic comment -- @@ -267,7 +570,7 @@
*
* @return false when error(not a Content length) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeContentLength(int startIndex) {
return decodeIntegerValue(startIndex);
//Synthetic comment -- @@ -280,7 +583,7 @@
*
* @return false when error(not a Content location) occur
*         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeContentLocation(int startIndex) {
return decodeTextString(startIndex);
//Synthetic comment -- @@ -293,7 +596,8 @@
*
* @return false when error(not a X-Wap-Application-Id) occur
*         return value can be retrieved first by getValueString() and second by getValue32()
     *         method length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeXWapApplicationId(int startIndex) {
if (decodeIntegerValue(startIndex) == true) {
//Synthetic comment -- @@ -310,7 +614,7 @@
*
* @return false when error(not a X-Wap-Content-URI) occur
*         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeXWapContentURI(int startIndex) {
return decodeTextString(startIndex);
//Synthetic comment -- @@ -323,7 +627,7 @@
*
* @return false when error(not a X-Wap-Initiator-URI) occur
*         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeXWapInitiatorURI(int startIndex) {
return decodeTextString(startIndex);
//Synthetic comment -- @@ -349,4 +653,18 @@
public String getValueString() {
return stringValue;
}
}








//Synthetic comment -- diff --git a/tests/CoreTests/com/android/internal/telephony/TelephonyTests.java b/tests/CoreTests/com/android/internal/telephony/TelephonyTests.java
//Synthetic comment -- index fdfafe1..5231ceb3 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.internal.telephony.SimPhoneBookTest;
import com.android.internal.telephony.SimSmsTest;
import com.android.internal.telephony.SimUtilsTest;

import junit.framework.TestSuite;

//Synthetic comment -- @@ -48,6 +49,7 @@
suite.addTestSuite(SimPhoneBookTest.class);
suite.addTestSuite(SimSmsTest.class);
suite.addTestSuite(TelephonyUtilsTest.class);

return suite;
}








//Synthetic comment -- diff --git a/tests/CoreTests/com/android/internal/telephony/gsm/Wap230WspContentTypeTest.java b/tests/CoreTests/com/android/internal/telephony/gsm/Wap230WspContentTypeTest.java
new file mode 100644
//Synthetic comment -- index 0000000..81fa180

//Synthetic comment -- @@ -0,0 +1,739 @@







