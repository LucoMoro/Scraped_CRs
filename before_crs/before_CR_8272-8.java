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
//Synthetic comment -- index bf9e854..7a3d147 100644

//Synthetic comment -- @@ -561,15 +561,24 @@
* values:</p>
*
* <ul>
             *   <li><em>transactionId (Integer)</em> - The WAP transaction
             *    ID</li>
*   <li><em>pduType (Integer)</em> - The WAP PDU type</li>
*   <li><em>header (byte[])</em> - The header of the message</li>
*   <li><em>data (byte[])</em> - The data payload of the message</li>
* </ul>
*
* <p>If a BroadcastReceiver encounters an error while processing
* this intent it should set the result code appropriately.</p>
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
public static final String WAP_PUSH_RECEIVED_ACTION =
//Synthetic comment -- @@ -582,7 +591,7 @@
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
public static final String SIM_FULL_ACTION =
                "android.provider.Telephony.SIM_FULL";

/**
* Broadcast Action: An incoming SMS has been rejected by the








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WapPushOverSms.java b/telephony/java/com/android/internal/telephony/WapPushOverSms.java
//Synthetic comment -- index a636a4b..f3a68b7 100644

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
//Synthetic comment -- @@ -133,7 +136,7 @@
mimeType = WspTypeDecoder.CONTENT_MIME_TYPE_B_VND_DOCOMO_PF;
break;
default:
                    if (Config.LOGD) {
Log.w(LOG_TAG,
"Received PDU. Unsupported Content-Type = " + binaryContentType);
}
//Synthetic comment -- @@ -155,7 +158,9 @@
} else if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_VND_DOCOMO_PF)) {
binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_VND_DOCOMO_PF;
} else {
                if (Config.LOGD) Log.w(LOG_TAG, "Received PDU. Unknown Content-Type = " + mimeType);
return Intents.RESULT_SMS_HANDLED;
}
}
//Synthetic comment -- @@ -176,13 +181,14 @@
}
if (dispatchedByApplication == false) {
dispatchWapPdu_default(pdu, transactionId, pduType, mimeType,
                                   headerStartIndex, headerLength);
}
return Activity.RESULT_OK;
}

private void dispatchWapPdu_default(byte[] pdu, int transactionId, int pduType,
                                        String mimeType, int headerStartIndex, int headerLength) {
byte[] header = new byte[headerLength];
System.arraycopy(pdu, headerStartIndex, header, 0, header.length);
int dataIndex = headerStartIndex + headerLength;
//Synthetic comment -- @@ -197,6 +203,7 @@
intent.putExtra("pduType", pduType);
intent.putExtra("header", header);
intent.putExtra("data", data);

mSmsDispatcher.dispatch(intent, "android.permission.RECEIVE_WAP_PUSH");
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WspTypeDecoder.java b/telephony/java/com/android/internal/telephony/WspTypeDecoder.java
//Synthetic comment -- index 336bc82..4b5245e 100644

//Synthetic comment -- @@ -16,22 +16,180 @@

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

    // TODO we should have mapping between those binary code and mime type string.
    //  see http://www.openmobilealliance.org/tech/omna/omna-wsp-content-type.aspx

public static final int CONTENT_TYPE_B_DRM_RIGHTS_XML = 0x4a;
public static final int CONTENT_TYPE_B_DRM_RIGHTS_WBXML = 0x4b;
//Synthetic comment -- @@ -53,12 +211,13 @@

public static final int PARAMETER_ID_X_WAP_APPLICATION_ID = 0x2f;


byte[] wspData;
int    dataLength;
long   unsigned32bit;
String stringValue;

public WspTypeDecoder(byte[] pdu) {
wspData = pdu;
}
//Synthetic comment -- @@ -69,17 +228,17 @@
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
//Synthetic comment -- @@ -87,13 +246,33 @@
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
//Synthetic comment -- @@ -111,7 +290,7 @@
*
* @return false when error(not a Long-integer) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeLongInteger(int startIndex) {
int lengthMultiOctet = wspData[startIndex] & 0xff;
//Synthetic comment -- @@ -120,10 +299,10 @@
return false;
}
unsigned32bit = 0;
        for (int i=1; i<=lengthMultiOctet; i++) {
            unsigned32bit = (unsigned32bit << 8) | (wspData[startIndex+i] & 0xff);
}
        dataLength = 1+lengthMultiOctet;
return true;
}

//Synthetic comment -- @@ -134,7 +313,7 @@
*
* @return false when error(not a Integer-Value) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeIntegerValue(int startIndex) {
if (decodeShortInteger(startIndex) == true) {
//Synthetic comment -- @@ -150,10 +329,10 @@
*
* @return false when error(not a Uintvar-integer) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeUintvarInteger(int startIndex) {
        int  index = startIndex;

unsigned32bit = 0;
while ((wspData[index] & 0x80) != 0) {
//Synthetic comment -- @@ -175,7 +354,7 @@
*
* @return false when error(not a Value-length) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeValueLength(int startIndex) {
if ((wspData[startIndex] & 0xff) > WAP_PDU_LENGTH_QUOTE) {
//Synthetic comment -- @@ -185,22 +364,22 @@
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
//Synthetic comment -- @@ -212,7 +391,7 @@
index++;
}

        dataLength  = index - startIndex + 1;
stringValue = new String(wspData, startIndex, dataLength - 1);

return rtrn;
//Synthetic comment -- @@ -225,7 +404,7 @@
*
* @return false when error(not a Constrained-encoding) occur
*         return value can be retrieved first by getValueString() and second by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeConstrainedEncoding(int startIndex) {
if (decodeShortInteger(startIndex) == true) {
//Synthetic comment -- @@ -240,31 +419,152 @@
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
//Synthetic comment -- @@ -272,7 +572,7 @@
*
* @return false when error(not a Content length) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeContentLength(int startIndex) {
return decodeIntegerValue(startIndex);
//Synthetic comment -- @@ -285,7 +585,7 @@
*
* @return false when error(not a Content location) occur
*         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeContentLocation(int startIndex) {
return decodeTextString(startIndex);
//Synthetic comment -- @@ -298,7 +598,8 @@
*
* @return false when error(not a X-Wap-Application-Id) occur
*         return value can be retrieved first by getValueString() and second by getValue32()
     *         method length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeXWapApplicationId(int startIndex) {
if (decodeIntegerValue(startIndex) == true) {
//Synthetic comment -- @@ -315,7 +616,7 @@
*
* @return false when error(not a X-Wap-Content-URI) occur
*         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeXWapContentURI(int startIndex) {
return decodeTextString(startIndex);
//Synthetic comment -- @@ -328,7 +629,7 @@
*
* @return false when error(not a X-Wap-Initiator-URI) occur
*         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeXWapInitiatorURI(int startIndex) {
return decodeTextString(startIndex);
//Synthetic comment -- @@ -354,4 +655,18 @@
public String getValueString() {
return stringValue;
}
}








//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/com/android/internal/telephony/Wap230WspContentTypeTest.java b/telephony/tests/telephonytests/src/com/android/internal/telephony/Wap230WspContentTypeTest.java
new file mode 100644
//Synthetic comment -- index 0000000..b7b6894

//Synthetic comment -- @@ -0,0 +1,739 @@







