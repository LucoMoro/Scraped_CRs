/*Enhance WspTypeDecoder to decode Content Type Parameters

This patch enables WspTypeDecoder to correctly parse content type
parameters as described in the Wap230 WSP specifications
(wap-230-wsp-20010705-a section 8.4.2.24) which are then passed on
as part of the WAP_PUSH intent notification.

It also recognises all Well Known WSP Content types, and simplifies
their retrieval (i.e. a well known content type will always be
available through the WspTypeDecoder.getValueString() method).

Change-Id:I0eb3f9ac287aa7cb53312777c4be54b1939fa857*/
//Synthetic comment -- diff --git a/core/java/android/provider/Telephony.java b/core/java/android/provider/Telephony.java
//Synthetic comment -- index d271e93..fa5cd8b 100644

//Synthetic comment -- @@ -562,15 +562,24 @@
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
//Synthetic comment -- @@ -583,7 +592,7 @@
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
public static final String SIM_FULL_ACTION =
                "android.provider.Telephony.SIM_FULL";

/**
* Broadcast Action: An incoming SMS has been rejected by the








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WapPushOverSms.java b/telephony/java/com/android/internal/telephony/WapPushOverSms.java
//Synthetic comment -- index a636a4b..d125563 100644

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
//Synthetic comment -- @@ -102,136 +103,49 @@
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
            // TODO we should have more generic way to map binaryContentType code to mimeType.
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
                case WspTypeDecoder.CONTENT_TYPE_B_VND_DOCOMO_PF:
                    mimeType = WspTypeDecoder.CONTENT_MIME_TYPE_B_VND_DOCOMO_PF;
                    break;
                default:
                    if (Config.LOGD) {
                        Log.w(LOG_TAG,
                                "Received PDU. Unsupported Content-Type = " + binaryContentType);
                    }
                return Intents.RESULT_SMS_HANDLED;
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
            } else if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_VND_DOCOMO_PF)) {
                binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_VND_DOCOMO_PF;
            } else {
                if (Config.LOGD) Log.w(LOG_TAG, "Received PDU. Unknown Content-Type = " + mimeType);
                return Intents.RESULT_SMS_HANDLED;
            }
        }
index += pduDecoder.getDecodedDataLength();

        boolean dispatchedByApplication = false;
        switch (binaryContentType) {
            case WspTypeDecoder.CONTENT_TYPE_B_PUSH_CO:
                dispatchWapPdu_PushCO(pdu, transactionId, pduType, headerStartIndex, headerLength);
                dispatchedByApplication = true;
                break;
            case WspTypeDecoder.CONTENT_TYPE_B_MMS:
                dispatchWapPdu_MMS(pdu, transactionId, pduType, headerStartIndex, headerLength);
                dispatchedByApplication = true;
                break;
            default:
                break;
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
        byte[] data;

        data = new byte[pdu.length - dataIndex];
        System.arraycopy(pdu, dataIndex, data, 0, data.length);

Intent intent = new Intent(Intents.WAP_PUSH_RECEIVED_ACTION);
intent.setType(mimeType);
intent.putExtra("transactionId", transactionId);
intent.putExtra("pduType", pduType);
intent.putExtra("header", header);
        intent.putExtra("data", data);

        mSmsDispatcher.dispatch(intent, "android.permission.RECEIVE_WAP_PUSH");
}

    private void dispatchWapPdu_PushCO(byte[] pdu, int transactionId, int pduType,
                                       int headerStartIndex, int headerLength) {
        byte[] header = new byte[headerLength];
        System.arraycopy(pdu, headerStartIndex, header, 0, header.length);

        Intent intent = new Intent(Intents.WAP_PUSH_RECEIVED_ACTION);
        intent.setType(WspTypeDecoder.CONTENT_MIME_TYPE_B_PUSH_CO);
        intent.putExtra("transactionId", transactionId);
        intent.putExtra("pduType", pduType);
        intent.putExtra("header", header);
        intent.putExtra("data", pdu);

        mSmsDispatcher.dispatch(intent, "android.permission.RECEIVE_WAP_PUSH");
    }

    private void dispatchWapPdu_MMS(byte[] pdu, int transactionId, int pduType,
                                    int headerStartIndex, int headerLength) {
        byte[] header = new byte[headerLength];
        System.arraycopy(pdu, headerStartIndex, header, 0, header.length);
        int dataIndex = headerStartIndex + headerLength;
        byte[] data = new byte[pdu.length - dataIndex];
        System.arraycopy(pdu, dataIndex, data, 0, data.length);

        Intent intent = new Intent(Intents.WAP_PUSH_RECEIVED_ACTION);
        intent.setType(WspTypeDecoder.CONTENT_MIME_TYPE_B_MMS);
        intent.putExtra("transactionId", transactionId);
        intent.putExtra("pduType", pduType);
        intent.putExtra("header", header);
        intent.putExtra("data", data);

        mSmsDispatcher.dispatch(intent, "android.permission.RECEIVE_MMS");
    }
}

\ No newline at end of file








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WspTypeDecoder.java b/telephony/java/com/android/internal/telephony/WspTypeDecoder.java
//Synthetic comment -- index 336bc82..786f3fb 100644

//Synthetic comment -- @@ -16,49 +16,191 @@

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
    public static final int CONTENT_TYPE_B_PUSH_SI = 0x2e;
    public static final int CONTENT_TYPE_B_PUSH_SL = 0x30;
public static final int CONTENT_TYPE_B_PUSH_CO = 0x32;
public static final int CONTENT_TYPE_B_MMS = 0x3e;
    public static final int CONTENT_TYPE_B_VND_DOCOMO_PF = 0x0310;

    public static final String CONTENT_MIME_TYPE_B_DRM_RIGHTS_XML =
            "application/vnd.oma.drm.rights+xml";
    public static final String CONTENT_MIME_TYPE_B_DRM_RIGHTS_WBXML =
            "application/vnd.oma.drm.rights+wbxml";
    public static final String CONTENT_MIME_TYPE_B_PUSH_SI = "application/vnd.wap.sic";
    public static final String CONTENT_MIME_TYPE_B_PUSH_SL = "application/vnd.wap.slc";
    public static final String CONTENT_MIME_TYPE_B_PUSH_CO = "application/vnd.wap.coc";
    public static final String CONTENT_MIME_TYPE_B_MMS = "application/vnd.wap.mms-message";
    public static final String CONTENT_MIME_TYPE_B_VND_DOCOMO_PF = "application/vnd.docomo.pf";

    public static final int PARAMETER_ID_X_WAP_APPLICATION_ID = 0x2f;


byte[] wspData;
int    dataLength;
long   unsigned32bit;
String stringValue;

public WspTypeDecoder(byte[] pdu) {
wspData = pdu;
}
//Synthetic comment -- @@ -69,17 +211,17 @@
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
//Synthetic comment -- @@ -87,13 +229,33 @@
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
//Synthetic comment -- @@ -111,7 +273,7 @@
*
* @return false when error(not a Long-integer) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeLongInteger(int startIndex) {
int lengthMultiOctet = wspData[startIndex] & 0xff;
//Synthetic comment -- @@ -120,10 +282,10 @@
return false;
}
unsigned32bit = 0;
        for (int i=1; i<=lengthMultiOctet; i++) {
            unsigned32bit = (unsigned32bit << 8) | (wspData[startIndex+i] & 0xff);
}
        dataLength = 1+lengthMultiOctet;
return true;
}

//Synthetic comment -- @@ -134,7 +296,7 @@
*
* @return false when error(not a Integer-Value) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeIntegerValue(int startIndex) {
if (decodeShortInteger(startIndex) == true) {
//Synthetic comment -- @@ -150,10 +312,10 @@
*
* @return false when error(not a Uintvar-integer) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeUintvarInteger(int startIndex) {
        int  index = startIndex;

unsigned32bit = 0;
while ((wspData[index] & 0x80) != 0) {
//Synthetic comment -- @@ -175,7 +337,7 @@
*
* @return false when error(not a Value-length) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeValueLength(int startIndex) {
if ((wspData[startIndex] & 0xff) > WAP_PDU_LENGTH_QUOTE) {
//Synthetic comment -- @@ -185,22 +347,22 @@
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
//Synthetic comment -- @@ -212,7 +374,7 @@
index++;
}

        dataLength  = index - startIndex + 1;
stringValue = new String(wspData, startIndex, dataLength - 1);

return rtrn;
//Synthetic comment -- @@ -225,7 +387,7 @@
*
* @return false when error(not a Constrained-encoding) occur
*         return value can be retrieved first by getValueString() and second by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeConstrainedEncoding(int startIndex) {
if (decodeShortInteger(startIndex) == true) {
//Synthetic comment -- @@ -240,31 +402,162 @@
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
//Synthetic comment -- @@ -272,7 +565,7 @@
*
* @return false when error(not a Content length) occur
*         return value can be retrieved by getValue32() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeContentLength(int startIndex) {
return decodeIntegerValue(startIndex);
//Synthetic comment -- @@ -285,7 +578,7 @@
*
* @return false when error(not a Content location) occur
*         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeContentLocation(int startIndex) {
return decodeTextString(startIndex);
//Synthetic comment -- @@ -298,7 +591,8 @@
*
* @return false when error(not a X-Wap-Application-Id) occur
*         return value can be retrieved first by getValueString() and second by getValue32()
     *         method length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeXWapApplicationId(int startIndex) {
if (decodeIntegerValue(startIndex) == true) {
//Synthetic comment -- @@ -315,7 +609,7 @@
*
* @return false when error(not a X-Wap-Content-URI) occur
*         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeXWapContentURI(int startIndex) {
return decodeTextString(startIndex);
//Synthetic comment -- @@ -328,7 +622,7 @@
*
* @return false when error(not a X-Wap-Initiator-URI) occur
*         return value can be retrieved by getValueString() method
     *         length of data in pdu can be retrieved by getValue32() method
*/
public boolean decodeXWapInitiatorURI(int startIndex) {
return decodeTextString(startIndex);
//Synthetic comment -- @@ -354,4 +648,18 @@
public String getValueString() {
return stringValue;
}
}








//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/com/android/internal/telephony/Wap230WspContentTypeTest.java b/telephony/tests/telephonytests/src/com/android/internal/telephony/Wap230WspContentTypeTest.java
new file mode 100644
//Synthetic comment -- index 0000000..d31b294

//Synthetic comment -- @@ -0,0 +1,853 @@
\ No newline at end of file







