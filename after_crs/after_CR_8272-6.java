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
//Synthetic comment -- index aef04e6..122478c 100644

//Synthetic comment -- @@ -542,17 +542,23 @@
* values:</p>
*
* <ul>
             *   <li><em>transactionId (Integer)</em> - The WAP transaction ID</li>
*   <li><em>pduType (Integer)</em> - The WAP PDU type</li>
*   <li><em>data</em> - The data payload of the message</li>
             *   <li><em>contentTypeParameters (HashMap&lt;String,String&gt;)</em>
             *   - Any parameters associated with the content type
             *   (decoded from the WSP Content-Type header)</li>
* </ul>
*
* <p>If a BroadcastReceiver encounters an error while processing
* this intent it should set the result code appropriately.</p>
             *
             * <p>The contentTypeParameters extra value is map of content parameters keyed by
             * their names.</p>
             *
             * <p>If any unassigned well-known parameters are encountered, the key of the map will
             * be 'unassigned/0x...', where '...' is the hex value of the unassigned parameter.  If
             * a parameter has No-Value the value in the map will be null.</p>
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
public static final String WAP_PUSH_RECEIVED_ACTION =
//Synthetic comment -- @@ -565,7 +571,7 @@
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
public static final String SIM_FULL_ACTION =
                    "android.provider.Telephony.SIM_FULL";

/**
* Broadcast Action: An incoming SMS has been rejected by the








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WapPushOverSms.java b/telephony/java/com/android/internal/telephony/WapPushOverSms.java
//Synthetic comment -- index 3504c66..69a492c 100644

//Synthetic comment -- @@ -60,7 +60,7 @@
*/
public int dispatchWapPdu(byte[] pdu) {

        if (Config.DEBUG) Log.d(LOG_TAG, "Rx: " + IccUtils.bytesToHexString(pdu));

int index = 0;
int transactionId = pdu[index++] & 0xFF;
//Synthetic comment -- @@ -69,7 +69,7 @@

if ((pduType != WspTypeDecoder.PDU_TYPE_PUSH) &&
(pduType != WspTypeDecoder.PDU_TYPE_CONFIRMED_PUSH)) {
            if (Config.DEBUG) Log.w(LOG_TAG, "Received non-PUSH WAP PDU. Type = " + pduType);
return Intents.RESULT_SMS_HANDLED;
}

//Synthetic comment -- @@ -82,7 +82,7 @@
* So it will be encoded in no more than 5 octets.
*/
if (pduDecoder.decodeUintvarInteger(index) == false) {
            if (Config.DEBUG) Log.w(LOG_TAG, "Received PDU. Header Length error.");
return Intents.RESULT_SMS_GENERIC_ERROR;
}
headerLength = (int)pduDecoder.getValue32();
//Synthetic comment -- @@ -103,10 +103,12 @@
* Length = Uintvar-integer
*/
if (pduDecoder.decodeContentType(index) == false) {
            if (Config.DEBUG) Log.w(LOG_TAG, "Received PDU. Header Content-Type error.");
return Intents.RESULT_SMS_GENERIC_ERROR;
}

        int binaryContentType;

String mimeType = pduDecoder.getValueString();
if (mimeType == null) {
binaryContentType = (int)pduDecoder.getValue32();
//Synthetic comment -- @@ -130,7 +132,7 @@
mimeType = WspTypeDecoder.CONTENT_MIME_TYPE_B_MMS;
break;
default:
                    if (Config.DEBUG) {
Log.w(LOG_TAG,
"Received PDU. Unsupported Content-Type = " + binaryContentType);
}
//Synthetic comment -- @@ -150,13 +152,14 @@
} else if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_MMS)) {
binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_MMS;
} else {
                if (Config.DEBUG) {
                    Log.w(LOG_TAG, "Received PDU. Unknown Content-Type = " + mimeType);
                }
return Intents.RESULT_SMS_HANDLED;
}
}
index += pduDecoder.getDecodedDataLength();

int dataIndex = headerStartIndex + headerLength;

if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_PUSH_CO)) {
//Synthetic comment -- @@ -164,7 +167,7 @@
} else if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_MMS)) {
dispatchWapPdu_MMS(pdu, transactionId, pduType, dataIndex);
} else {
            dispatchWapPdu_default(pdu, transactionId, pduType, mimeType, dataIndex,
pduDecoder.getContentParameters());
}
return Activity.RESULT_OK;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WspTypeDecoder.java b/telephony/java/com/android/internal/telephony/WspTypeDecoder.java
//Synthetic comment -- index ace15c3..8f9cdcf 100644

//Synthetic comment -- @@ -19,9 +19,9 @@
import java.util.HashMap;

/**
 * Implement the WSP data type decoder.
*
 * @hide
*/
public class WspTypeDecoder {

//Synthetic comment -- @@ -31,168 +31,166 @@
public static final int PDU_TYPE_PUSH = 0x06;
public static final int PDU_TYPE_CONFIRMED_PUSH = 0x07;

    private final static HashMap<Integer, String> WELL_KNOWN_MIME_TYPES =
            new HashMap<Integer, String>();

    private final static HashMap<Integer, String> WELL_KNOWN_PARAMETERS =
            new HashMap<Integer, String>();

private static final int Q_VALUE = 0x00;

    static {
        WELL_KNOWN_MIME_TYPES.put(0x00, "*/*");
        WELL_KNOWN_MIME_TYPES.put(0x01, "text/*");
        WELL_KNOWN_MIME_TYPES.put(0x02, "text/html");
        WELL_KNOWN_MIME_TYPES.put(0x03, "text/plain");
        WELL_KNOWN_MIME_TYPES.put(0x04, "text/x-hdml");
        WELL_KNOWN_MIME_TYPES.put(0x05, "text/x-ttml");
        WELL_KNOWN_MIME_TYPES.put(0x06, "text/x-vCalendar");
        WELL_KNOWN_MIME_TYPES.put(0x07, "text/x-vCard");
        WELL_KNOWN_MIME_TYPES.put(0x08, "text/vnd.wap.wml");
        WELL_KNOWN_MIME_TYPES.put(0x09, "text/vnd.wap.wmlscript");
        WELL_KNOWN_MIME_TYPES.put(0x0A, "text/vnd.wap.wta-event");
        WELL_KNOWN_MIME_TYPES.put(0x0B, "multipart/*");
        WELL_KNOWN_MIME_TYPES.put(0x0C, "multipart/mixed");
        WELL_KNOWN_MIME_TYPES.put(0x0D, "multipart/form-data");
        WELL_KNOWN_MIME_TYPES.put(0x0E, "multipart/byterantes");
        WELL_KNOWN_MIME_TYPES.put(0x0F, "multipart/alternative");
        WELL_KNOWN_MIME_TYPES.put(0x10, "application/*");
        WELL_KNOWN_MIME_TYPES.put(0x11, "application/java-vm");
        WELL_KNOWN_MIME_TYPES.put(0x12, "application/x-www-form-urlencoded");
        WELL_KNOWN_MIME_TYPES.put(0x13, "application/x-hdmlc");
        WELL_KNOWN_MIME_TYPES.put(0x14, "application/vnd.wap.wmlc");
        WELL_KNOWN_MIME_TYPES.put(0x15, "application/vnd.wap.wmlscriptc");
        WELL_KNOWN_MIME_TYPES.put(0x16, "application/vnd.wap.wta-eventc");
        WELL_KNOWN_MIME_TYPES.put(0x17, "application/vnd.wap.uaprof");
        WELL_KNOWN_MIME_TYPES.put(0x18, "application/vnd.wap.wtls-ca-certificate");
        WELL_KNOWN_MIME_TYPES.put(0x19, "application/vnd.wap.wtls-user-certificate");
        WELL_KNOWN_MIME_TYPES.put(0x1A, "application/x-x509-ca-cert");
        WELL_KNOWN_MIME_TYPES.put(0x1B, "application/x-x509-user-cert");
        WELL_KNOWN_MIME_TYPES.put(0x1C, "image/*");
        WELL_KNOWN_MIME_TYPES.put(0x1D, "image/gif");
        WELL_KNOWN_MIME_TYPES.put(0x1E, "image/jpeg");
        WELL_KNOWN_MIME_TYPES.put(0x1F, "image/tiff");
        WELL_KNOWN_MIME_TYPES.put(0x20, "image/png");
        WELL_KNOWN_MIME_TYPES.put(0x21, "image/vnd.wap.wbmp");
        WELL_KNOWN_MIME_TYPES.put(0x22, "application/vnd.wap.multipart.*");
        WELL_KNOWN_MIME_TYPES.put(0x23, "application/vnd.wap.multipart.mixed");
        WELL_KNOWN_MIME_TYPES.put(0x24, "application/vnd.wap.multipart.form-data");
        WELL_KNOWN_MIME_TYPES.put(0x25, "application/vnd.wap.multipart.byteranges");
        WELL_KNOWN_MIME_TYPES.put(0x26, "application/vnd.wap.multipart.alternative");
        WELL_KNOWN_MIME_TYPES.put(0x27, "application/xml");
        WELL_KNOWN_MIME_TYPES.put(0x28, "text/xml");
        WELL_KNOWN_MIME_TYPES.put(0x29, "application/vnd.wap.wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x2A, "application/x-x968-cross-cert");
        WELL_KNOWN_MIME_TYPES.put(0x2B, "application/x-x968-ca-cert");
        WELL_KNOWN_MIME_TYPES.put(0x2C, "application/x-x968-user-cert");
        WELL_KNOWN_MIME_TYPES.put(0x2D, "text/vnd.wap.si");
        WELL_KNOWN_MIME_TYPES.put(0x2E, "application/vnd.wap.sic");
        WELL_KNOWN_MIME_TYPES.put(0x2F, "text/vnd.wap.sl");
        WELL_KNOWN_MIME_TYPES.put(0x30, "application/vnd.wap.slc");
        WELL_KNOWN_MIME_TYPES.put(0x31, "text/vnd.wap.co");
        WELL_KNOWN_MIME_TYPES.put(0x32, "application/vnd.wap.coc");
        WELL_KNOWN_MIME_TYPES.put(0x33, "application/vnd.wap.multipart.related");
        WELL_KNOWN_MIME_TYPES.put(0x34, "application/vnd.wap.sia");
        WELL_KNOWN_MIME_TYPES.put(0x35, "text/vnd.wap.connectivity-xml");
        WELL_KNOWN_MIME_TYPES.put(0x36, "application/vnd.wap.connectivity-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x37, "application/pkcs7-mime");
        WELL_KNOWN_MIME_TYPES.put(0x38, "application/vnd.wap.hashed-certificate");
        WELL_KNOWN_MIME_TYPES.put(0x39, "application/vnd.wap.signed-certificate");
        WELL_KNOWN_MIME_TYPES.put(0x3A, "application/vnd.wap.cert-response");
        WELL_KNOWN_MIME_TYPES.put(0x3B, "application/xhtml+xml");
        WELL_KNOWN_MIME_TYPES.put(0x3C, "application/wml+xml");
        WELL_KNOWN_MIME_TYPES.put(0x3D, "text/css");
        WELL_KNOWN_MIME_TYPES.put(0x3E, "application/vnd.wap.mms-message");
        WELL_KNOWN_MIME_TYPES.put(0x3F, "application/vnd.wap.rollover-certificate");
        WELL_KNOWN_MIME_TYPES.put(0x40, "application/vnd.wap.locc+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x41, "application/vnd.wap.loc+xml");
        WELL_KNOWN_MIME_TYPES.put(0x42, "application/vnd.syncml.dm+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x43, "application/vnd.syncml.dm+xml");
        WELL_KNOWN_MIME_TYPES.put(0x44, "application/vnd.syncml.notification");
        WELL_KNOWN_MIME_TYPES.put(0x45, "application/vnd.wap.xhtml+xml");
        WELL_KNOWN_MIME_TYPES.put(0x46, "application/vnd.wv.csp.cir");
        WELL_KNOWN_MIME_TYPES.put(0x47, "application/vnd.oma.dd+xml");
        WELL_KNOWN_MIME_TYPES.put(0x48, "application/vnd.oma.drm.message");
        WELL_KNOWN_MIME_TYPES.put(0x49, "application/vnd.oma.drm.content");
        WELL_KNOWN_MIME_TYPES.put(0x4A, "application/vnd.oma.drm.rights+xml");
        WELL_KNOWN_MIME_TYPES.put(0x4B, "application/vnd.oma.drm.rights+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x4C, "application/vnd.wv.csp+xml");
        WELL_KNOWN_MIME_TYPES.put(0x4D, "application/vnd.wv.csp+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x4E, "application/vnd.syncml.ds.notification");
        WELL_KNOWN_MIME_TYPES.put(0x4F, "audio/*");
        WELL_KNOWN_MIME_TYPES.put(0x50, "video/*");
        WELL_KNOWN_MIME_TYPES.put(0x51, "application/vnd.oma.dd2+xml");
        WELL_KNOWN_MIME_TYPES.put(0x52, "application/mikey");
        WELL_KNOWN_MIME_TYPES.put(0x53, "application/vnd.oma.dcd");
        WELL_KNOWN_MIME_TYPES.put(0x54, "application/vnd.oma.dcdc");

        WELL_KNOWN_MIME_TYPES.put(0x0201, "application/vnd.uplanet.cacheop-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0202, "application/vnd.uplanet.signal");
        WELL_KNOWN_MIME_TYPES.put(0x0203, "application/vnd.uplanet.alert-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0204, "application/vnd.uplanet.list-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0205, "application/vnd.uplanet.listcmd-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0206, "application/vnd.uplanet.channel-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0207, "application/vnd.uplanet.provisioning-status-uri");
        WELL_KNOWN_MIME_TYPES.put(0x0208, "x-wap.multipart/vnd.uplanet.header-set");
        WELL_KNOWN_MIME_TYPES.put(0x0209, "application/vnd.uplanet.bearer-choice-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x020A, "application/vnd.phonecom.mmc-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x020B, "application/vnd.nokia.syncset+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x020C, "image/x-up-wpng");
        WELL_KNOWN_MIME_TYPES.put(0x0300, "application/iota.mmc-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0301, "application/iota.mmc-xml");
        WELL_KNOWN_MIME_TYPES.put(0x0302, "application/vnd.syncml+xml");
        WELL_KNOWN_MIME_TYPES.put(0x0303, "application/vnd.syncml+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0304, "text/vnd.wap.emn+xml");
        WELL_KNOWN_MIME_TYPES.put(0x0305, "text/calendar");
        WELL_KNOWN_MIME_TYPES.put(0x0306, "application/vnd.omads-email+xml");
        WELL_KNOWN_MIME_TYPES.put(0x0307, "application/vnd.omads-file+xml");
        WELL_KNOWN_MIME_TYPES.put(0x0308, "application/vnd.omads-folder+xml");
        WELL_KNOWN_MIME_TYPES.put(0x0309, "text/directory;profile=vCard");
        WELL_KNOWN_MIME_TYPES.put(0x030A, "application/vnd.wap.emn+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x030B, "application/vnd.nokia.ipdc-purchase-response");
        WELL_KNOWN_MIME_TYPES.put(0x030C, "application/vnd.motorola.screen3+xml");
        WELL_KNOWN_MIME_TYPES.put(0x030D, "application/vnd.motorola.screen3+gzip");
        WELL_KNOWN_MIME_TYPES.put(0x030E, "application/vnd.cmcc.setting+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x030F, "application/vnd.cmcc.bombing+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0310, "application/vnd.docomo.pf");
        WELL_KNOWN_MIME_TYPES.put(0x0311, "application/vnd.docomo.ub");
        WELL_KNOWN_MIME_TYPES.put(0x0312, "application/vnd.omaloc-supl-init");
        WELL_KNOWN_MIME_TYPES.put(0x0313, "application/vnd.oma.group-usage-list+xml");
        WELL_KNOWN_MIME_TYPES.put(0x0314, "application/oma-directory+xml");
        WELL_KNOWN_MIME_TYPES.put(0x0315, "application/vnd.docomo.pf2");
        WELL_KNOWN_MIME_TYPES.put(0x0316, "application/vnd.oma.drm.roap-trigger+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0317, "application/vnd.sbm.mid2");
        WELL_KNOWN_MIME_TYPES.put(0x0318, "application/vnd.wmf.bootstrap");
        WELL_KNOWN_MIME_TYPES.put(0x0319, "application/vnc.cmcc.dcd+xml");
        WELL_KNOWN_MIME_TYPES.put(0x031A, "application/vnd.sbm.cid");
        WELL_KNOWN_MIME_TYPES.put(0x031B, "application/vnd.oma.bcast.provisioningtrigger");

        WELL_KNOWN_PARAMETERS.put(0x00, "Q");
        WELL_KNOWN_PARAMETERS.put(0x01, "Charset");
        WELL_KNOWN_PARAMETERS.put(0x02, "Level");
        WELL_KNOWN_PARAMETERS.put(0x03, "Type");
        WELL_KNOWN_PARAMETERS.put(0x07, "Differences");
        WELL_KNOWN_PARAMETERS.put(0x08, "Padding");
        WELL_KNOWN_PARAMETERS.put(0x09, "Type");
        WELL_KNOWN_PARAMETERS.put(0x0E, "Max-Age");
        WELL_KNOWN_PARAMETERS.put(0x10, "Secure");
        WELL_KNOWN_PARAMETERS.put(0x11, "SEC");
        WELL_KNOWN_PARAMETERS.put(0x12, "MAC");
        WELL_KNOWN_PARAMETERS.put(0x13, "Creation-date");
        WELL_KNOWN_PARAMETERS.put(0x14, "Modification-date");
        WELL_KNOWN_PARAMETERS.put(0x15, "Read-date");
        WELL_KNOWN_PARAMETERS.put(0x16, "Size");
        WELL_KNOWN_PARAMETERS.put(0x17, "Name");
        WELL_KNOWN_PARAMETERS.put(0x18, "Filename");
        WELL_KNOWN_PARAMETERS.put(0x19, "Start");
        WELL_KNOWN_PARAMETERS.put(0x1A, "Start-info");
        WELL_KNOWN_PARAMETERS.put(0x1B, "Comment");
        WELL_KNOWN_PARAMETERS.put(0x1C, "Domain");
        WELL_KNOWN_PARAMETERS.put(0x1D, "Path");
    }

public static final int CONTENT_TYPE_B_DRM_RIGHTS_XML = 0x4a;
public static final int CONTENT_TYPE_B_DRM_RIGHTS_WBXML = 0x4b;
public static final int CONTENT_TYPE_B_PUSH_SI = 0x2e;
//Synthetic comment -- @@ -211,13 +209,12 @@

public static final int PARAMETER_ID_X_WAP_APPLICATION_ID = 0x2f;

byte[] wspData;
int    dataLength;
long   unsigned32bit;
String stringValue;

    HashMap<String, String> contentParameters;

public WspTypeDecoder(byte[] pdu) {
wspData = pdu;
//Synthetic comment -- @@ -229,17 +226,17 @@
* @param startIndex The starting position of the "Text-string" in this pdu
*
* @return false when error(not a Text-string) occur
     *         return value can be retrieved by getValueString() method length of data in pdu can be
     *         retrieved by getDecodedDataLength() method
*/
public boolean decodeTextString(int startIndex) {
int index = startIndex;
while (wspData[index] != 0) {
index++;
}
        dataLength = index - startIndex + 1;
if (wspData[startIndex] == 127) {
            stringValue = new String(wspData, startIndex + 1, dataLength - 2);
} else {
stringValue = new String(wspData, startIndex, dataLength - 1);
}
//Synthetic comment -- @@ -260,12 +257,12 @@
while (wspData[index] != 0) {
index++;
}
        dataLength = index - startIndex + 1;
stringValue = new String(wspData, startIndex, dataLength - 1);

return true;
}

/**
* Decode the "Short-integer" type for WSP pdu
*
//Synthetic comment -- @@ -300,10 +297,10 @@
return false;
}
unsigned32bit = 0;
        for (int i = 1; i <= lengthMultiOctet; i++) {
            unsigned32bit = (unsigned32bit << 8) | (wspData[startIndex + i] & 0xff);
}
        dataLength = 1 + lengthMultiOctet;
return true;
}

//Synthetic comment -- @@ -333,7 +330,7 @@
*         length of data in pdu can be retrieved by getDecodedDataLength() method
*/
public boolean decodeUintvarInteger(int startIndex) {
        int index = startIndex;

unsigned32bit = 0;
while ((wspData[index] & 0x80) != 0) {
//Synthetic comment -- @@ -365,8 +362,8 @@
unsigned32bit = wspData[startIndex];
dataLength = 1;
} else {
            decodeUintvarInteger(startIndex + 1);
            dataLength++;
}
return true;
}
//Synthetic comment -- @@ -377,9 +374,9 @@
* @param startIndex The starting position of the "Extension-media" in this PDU.
*
* @return false on error, such as if there is no Extension-media at startIndex.
     *         Side-effects: updates stringValue (available with
     *         getValueString()), which will be null on error. The length of the
     *         data in the PDU is available with getValue32(), 0 on error.
*/
public boolean decodeExtensionMedia(int startIndex) {
int index = startIndex;
//Synthetic comment -- @@ -392,7 +389,7 @@
index++;
}

        dataLength = index - startIndex + 1;
stringValue = new String(wspData, startIndex, dataLength - 1);

return rtrn;
//Synthetic comment -- @@ -421,10 +418,11 @@
* @param startIndex The starting position of the "Content-type" in this pdu
*
* @return false when error(not a Content-type) occurs
     *         If a content type exists in the headers (either as inline string, or as well-known
     *         value), getValueString() will return it. If a 'well known value' is encountered that
     *         cannot be mapped to a string mime type, getValueString() will return null, and
     *         getValue32() will return the unknown content type value.
     *         length of data in pdu can be retrieved by getDecodedDataLength() method
*         Any content type parameters will be accessible via getContentParameters()
*/
public boolean decodeContentType(int startIndex) {
//Synthetic comment -- @@ -438,7 +436,7 @@
}
return found;
}
        int headersLength = (int) unsigned32bit;
mediaPrefixLength = getDecodedDataLength();
if (decodeIntegerValue(startIndex + mediaPrefixLength) == true) {
dataLength += mediaPrefixLength;
//Synthetic comment -- @@ -447,7 +445,8 @@
expandWellKnownMimeType();
long wellKnownValue = unsigned32bit;
String mimeType = stringValue;
            if (readContentParameters(startIndex + dataLength,
                    (headersLength - (dataLength - mediaPrefixLength)), 0)) {
dataLength += readLength;
unsigned32bit = wellKnownValue;
stringValue = mimeType;
//Synthetic comment -- @@ -461,7 +460,8 @@
expandWellKnownMimeType();
long wellKnownValue = unsigned32bit;
String mimeType = stringValue;
            if (readContentParameters(startIndex + dataLength,
                    (headersLength - (dataLength - mediaPrefixLength)), 0)) {
dataLength += readLength;
unsigned32bit = wellKnownValue;
stringValue = mimeType;
//Synthetic comment -- @@ -476,8 +476,6 @@
int totalRead = 0;

if (leftToRead > 0) {
byte nextByte = wspData[startIndex];
String value = null;
String param = null;
//Synthetic comment -- @@ -488,32 +486,34 @@
} else { // typed
if (decodeIntegerValue(startIndex)) {
totalRead += dataLength;
                    int wellKnownParameterValue = (int) unsigned32bit;
param = WELL_KNOWN_PARAMETERS.get(wellKnownParameterValue);
if (param == null) {
param = "unassigned/0x" + Long.toHexString(wellKnownParameterValue);
}
                    // special case for the "Q" parameter, value is a uintvar
                    if (wellKnownParameterValue == Q_VALUE) {
                        if (decodeUintvarInteger(startIndex + totalRead)) {
                            totalRead += dataLength;
                            value = String.valueOf(unsigned32bit);
                            contentParameters.put(param, value);
                            return readContentParameters(startIndex + totalRead, leftToRead
                                                            - totalRead, accumulator + totalRead);
                        } else {
                            return false;
}
                    }
} else {
return false;
}
}

            if (decodeNoValue(startIndex + totalRead)) {
totalRead += dataLength;
                value = null;
            } else if (decodeIntegerValue(startIndex + totalRead)) {
                totalRead += dataLength;
                int intValue = (int) unsigned32bit;
if (intValue == 0) {
value = "";
} else {
//Synthetic comment -- @@ -527,21 +527,37 @@
// quoted string, so remove the quote
value = value.substring(1);
}
}
contentParameters.put(param, value);
            return readContentParameters(startIndex + totalRead, leftToRead - totalRead,
                                            accumulator + totalRead);

} else {
dataLength = accumulator;
return true;
        }
}

    /**
     * Check if the next byte is No-Value
     *
     * @param startIndex The starting position of the "Content length" in this pdu
     *
     * @return true if and only if the next byte is 0x00
     */
    private boolean decodeNoValue(int startIndex) {
        if (wspData[startIndex] == 0) {
            dataLength = 1;
            return true;
        } else {
            return false;
        }
    }

private void expandWellKnownMimeType() {
if (stringValue == null) {
            int binaryContentType = (int) unsigned32bit;
            stringValue = WELL_KNOWN_MIME_TYPES.get(binaryContentType);
} else {
unsigned32bit = -1;
}
//Synthetic comment -- @@ -580,7 +596,8 @@
*
* @return false when error(not a X-Wap-Application-Id) occur
*         return value can be retrieved first by getValueString() and second by getValue32()
     *         method
     *         length of data in pdu can be retrieved by getDecodedDataLength() method
*/
public boolean decodeXWapApplicationId(int startIndex) {
if (decodeIntegerValue(startIndex) == true) {
//Synthetic comment -- @@ -637,14 +654,15 @@
return stringValue;
}

/**
* Any parameters encountered as part of a decodeContentType() invocation.
     *
     * @return a map of content parameters keyed by their names, or null if
     *         decodeContentType() has not been called If any unassigned
     *         well-known parameters are encountered, the key of the map will be
     *         'unassigned/0x...', where '...' is the hex value of the
     *         unassigned parameter.  If a parameter has No-Value the value will be null.
     *
*/
public HashMap<String, String> getContentParameters() {
return contentParameters;








//Synthetic comment -- diff --git a/tests/CoreTests/com/android/internal/telephony/gsm/Wap230WspContentTypeTest.java b/tests/CoreTests/com/android/internal/telephony/gsm/Wap230WspContentTypeTest.java
//Synthetic comment -- index de3df85..81fa180 100644

//Synthetic comment -- @@ -27,137 +27,136 @@

public class Wap230WspContentTypeTest extends TestCase {

    public static final Map<Integer, String> WELL_KNOWN_MIME_TYPES = new HashMap<Integer, String>();
    public static final Map<Integer, String> WELL_KNOWN_PARAMETERS = new HashMap<Integer, String>();

    static {
        WELL_KNOWN_MIME_TYPES.put(0x00, "*/*");
        WELL_KNOWN_MIME_TYPES.put(0x01, "text/*");
        WELL_KNOWN_MIME_TYPES.put(0x02, "text/html");
        WELL_KNOWN_MIME_TYPES.put(0x03, "text/plain");
        WELL_KNOWN_MIME_TYPES.put(0x04, "text/x-hdml");
        WELL_KNOWN_MIME_TYPES.put(0x05, "text/x-ttml");
        WELL_KNOWN_MIME_TYPES.put(0x06, "text/x-vCalendar");
        WELL_KNOWN_MIME_TYPES.put(0x07, "text/x-vCard");
        WELL_KNOWN_MIME_TYPES.put(0x08, "text/vnd.wap.wml");
        WELL_KNOWN_MIME_TYPES.put(0x09, "text/vnd.wap.wmlscript");
        WELL_KNOWN_MIME_TYPES.put(0x0A, "text/vnd.wap.wta-event");
        WELL_KNOWN_MIME_TYPES.put(0x0B, "multipart/*");
        WELL_KNOWN_MIME_TYPES.put(0x0C, "multipart/mixed");
        WELL_KNOWN_MIME_TYPES.put(0x0D, "multipart/form-data");
        WELL_KNOWN_MIME_TYPES.put(0x0E, "multipart/byterantes");
        WELL_KNOWN_MIME_TYPES.put(0x0F, "multipart/alternative");
        WELL_KNOWN_MIME_TYPES.put(0x10, "application/*");
        WELL_KNOWN_MIME_TYPES.put(0x11, "application/java-vm");
        WELL_KNOWN_MIME_TYPES.put(0x12, "application/x-www-form-urlencoded");
        WELL_KNOWN_MIME_TYPES.put(0x13, "application/x-hdmlc");
        WELL_KNOWN_MIME_TYPES.put(0x14, "application/vnd.wap.wmlc");
        WELL_KNOWN_MIME_TYPES.put(0x15, "application/vnd.wap.wmlscriptc");
        WELL_KNOWN_MIME_TYPES.put(0x16, "application/vnd.wap.wta-eventc");
        WELL_KNOWN_MIME_TYPES.put(0x17, "application/vnd.wap.uaprof");
        WELL_KNOWN_MIME_TYPES.put(0x18, "application/vnd.wap.wtls-ca-certificate");
        WELL_KNOWN_MIME_TYPES.put(0x19, "application/vnd.wap.wtls-user-certificate");
        WELL_KNOWN_MIME_TYPES.put(0x1A, "application/x-x509-ca-cert");
        WELL_KNOWN_MIME_TYPES.put(0x1B, "application/x-x509-user-cert");
        WELL_KNOWN_MIME_TYPES.put(0x1C, "image/*");
        WELL_KNOWN_MIME_TYPES.put(0x1D, "image/gif");
        WELL_KNOWN_MIME_TYPES.put(0x1E, "image/jpeg");
        WELL_KNOWN_MIME_TYPES.put(0x1F, "image/tiff");
        WELL_KNOWN_MIME_TYPES.put(0x20, "image/png");
        WELL_KNOWN_MIME_TYPES.put(0x21, "image/vnd.wap.wbmp");
        WELL_KNOWN_MIME_TYPES.put(0x22, "application/vnd.wap.multipart.*");
        WELL_KNOWN_MIME_TYPES.put(0x23, "application/vnd.wap.multipart.mixed");
        WELL_KNOWN_MIME_TYPES.put(0x24, "application/vnd.wap.multipart.form-data");
        WELL_KNOWN_MIME_TYPES.put(0x25, "application/vnd.wap.multipart.byteranges");
        WELL_KNOWN_MIME_TYPES.put(0x26, "application/vnd.wap.multipart.alternative");
        WELL_KNOWN_MIME_TYPES.put(0x27, "application/xml");
        WELL_KNOWN_MIME_TYPES.put(0x28, "text/xml");
        WELL_KNOWN_MIME_TYPES.put(0x29, "application/vnd.wap.wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x2A, "application/x-x968-cross-cert");
        WELL_KNOWN_MIME_TYPES.put(0x2B, "application/x-x968-ca-cert");
        WELL_KNOWN_MIME_TYPES.put(0x2C, "application/x-x968-user-cert");
        WELL_KNOWN_MIME_TYPES.put(0x2D, "text/vnd.wap.si");
        WELL_KNOWN_MIME_TYPES.put(0x2E, "application/vnd.wap.sic");
        WELL_KNOWN_MIME_TYPES.put(0x2F, "text/vnd.wap.sl");
        WELL_KNOWN_MIME_TYPES.put(0x30, "application/vnd.wap.slc");
        WELL_KNOWN_MIME_TYPES.put(0x31, "text/vnd.wap.co");
        WELL_KNOWN_MIME_TYPES.put(0x32, "application/vnd.wap.coc");
        WELL_KNOWN_MIME_TYPES.put(0x33, "application/vnd.wap.multipart.related");
        WELL_KNOWN_MIME_TYPES.put(0x34, "application/vnd.wap.sia");
        WELL_KNOWN_MIME_TYPES.put(0x35, "text/vnd.wap.connectivity-xml");
        WELL_KNOWN_MIME_TYPES.put(0x36, "application/vnd.wap.connectivity-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x37, "application/pkcs7-mime");
        WELL_KNOWN_MIME_TYPES.put(0x38, "application/vnd.wap.hashed-certificate");
        WELL_KNOWN_MIME_TYPES.put(0x39, "application/vnd.wap.signed-certificate");
        WELL_KNOWN_MIME_TYPES.put(0x3A, "application/vnd.wap.cert-response");
        WELL_KNOWN_MIME_TYPES.put(0x3B, "application/xhtml+xml");
        WELL_KNOWN_MIME_TYPES.put(0x3C, "application/wml+xml");
        WELL_KNOWN_MIME_TYPES.put(0x3D, "text/css");
        WELL_KNOWN_MIME_TYPES.put(0x3E, "application/vnd.wap.mms-message");
        WELL_KNOWN_MIME_TYPES.put(0x3F, "application/vnd.wap.rollover-certificate");
        WELL_KNOWN_MIME_TYPES.put(0x40, "application/vnd.wap.locc+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x41, "application/vnd.wap.loc+xml");
        WELL_KNOWN_MIME_TYPES.put(0x42, "application/vnd.syncml.dm+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x43, "application/vnd.syncml.dm+xml");
        WELL_KNOWN_MIME_TYPES.put(0x44, "application/vnd.syncml.notification");
        WELL_KNOWN_MIME_TYPES.put(0x45, "application/vnd.wap.xhtml+xml");
        WELL_KNOWN_MIME_TYPES.put(0x46, "application/vnd.wv.csp.cir");
        WELL_KNOWN_MIME_TYPES.put(0x47, "application/vnd.oma.dd+xml");
        WELL_KNOWN_MIME_TYPES.put(0x48, "application/vnd.oma.drm.message");
        WELL_KNOWN_MIME_TYPES.put(0x49, "application/vnd.oma.drm.content");
        WELL_KNOWN_MIME_TYPES.put(0x4A, "application/vnd.oma.drm.rights+xml");
        WELL_KNOWN_MIME_TYPES.put(0x4B, "application/vnd.oma.drm.rights+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x4C, "application/vnd.wv.csp+xml");
        WELL_KNOWN_MIME_TYPES.put(0x4D, "application/vnd.wv.csp+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x4E, "application/vnd.syncml.ds.notification");
        WELL_KNOWN_MIME_TYPES.put(0x4F, "audio/*");
        WELL_KNOWN_MIME_TYPES.put(0x50, "video/*");
        WELL_KNOWN_MIME_TYPES.put(0x51, "application/vnd.oma.dd2+xml");
        WELL_KNOWN_MIME_TYPES.put(0x52, "application/mikey");
        WELL_KNOWN_MIME_TYPES.put(0x53, "application/vnd.oma.dcd");
        WELL_KNOWN_MIME_TYPES.put(0x54, "application/vnd.oma.dcdc");

        WELL_KNOWN_MIME_TYPES.put(0x0201, "application/vnd.uplanet.cacheop-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0202, "application/vnd.uplanet.signal");
        WELL_KNOWN_MIME_TYPES.put(0x0203, "application/vnd.uplanet.alert-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0204, "application/vnd.uplanet.list-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0205, "application/vnd.uplanet.listcmd-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0206, "application/vnd.uplanet.channel-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0207, "application/vnd.uplanet.provisioning-status-uri");
        WELL_KNOWN_MIME_TYPES.put(0x0208, "x-wap.multipart/vnd.uplanet.header-set");
        WELL_KNOWN_MIME_TYPES.put(0x0209, "application/vnd.uplanet.bearer-choice-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x020A, "application/vnd.phonecom.mmc-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x020B, "application/vnd.nokia.syncset+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x020C, "image/x-up-wpng");
        WELL_KNOWN_MIME_TYPES.put(0x0300, "application/iota.mmc-wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0301, "application/iota.mmc-xml");
        WELL_KNOWN_MIME_TYPES.put(0x0302, "application/vnd.syncml+xml");
        WELL_KNOWN_MIME_TYPES.put(0x0303, "application/vnd.syncml+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0304, "text/vnd.wap.emn+xml");
        WELL_KNOWN_MIME_TYPES.put(0x0305, "text/calendar");
        WELL_KNOWN_MIME_TYPES.put(0x0306, "application/vnd.omads-email+xml");
        WELL_KNOWN_MIME_TYPES.put(0x0307, "application/vnd.omads-file+xml");
        WELL_KNOWN_MIME_TYPES.put(0x0308, "application/vnd.omads-folder+xml");
        WELL_KNOWN_MIME_TYPES.put(0x0309, "text/directory;profile=vCard");
        WELL_KNOWN_MIME_TYPES.put(0x030A, "application/vnd.wap.emn+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x030B, "application/vnd.nokia.ipdc-purchase-response");
        WELL_KNOWN_MIME_TYPES.put(0x030C, "application/vnd.motorola.screen3+xml");
        WELL_KNOWN_MIME_TYPES.put(0x030D, "application/vnd.motorola.screen3+gzip");
        WELL_KNOWN_MIME_TYPES.put(0x030E, "application/vnd.cmcc.setting+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x030F, "application/vnd.cmcc.bombing+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0310, "application/vnd.docomo.pf");
        WELL_KNOWN_MIME_TYPES.put(0x0311, "application/vnd.docomo.ub");
        WELL_KNOWN_MIME_TYPES.put(0x0312, "application/vnd.omaloc-supl-init");
        WELL_KNOWN_MIME_TYPES.put(0x0313, "application/vnd.oma.group-usage-list+xml");
        WELL_KNOWN_MIME_TYPES.put(0x0314, "application/oma-directory+xml");
        WELL_KNOWN_MIME_TYPES.put(0x0315, "application/vnd.docomo.pf2");
        WELL_KNOWN_MIME_TYPES.put(0x0316, "application/vnd.oma.drm.roap-trigger+wbxml");
        WELL_KNOWN_MIME_TYPES.put(0x0317, "application/vnd.sbm.mid2");
        WELL_KNOWN_MIME_TYPES.put(0x0318, "application/vnd.wmf.bootstrap");
        WELL_KNOWN_MIME_TYPES.put(0x0319, "application/vnc.cmcc.dcd+xml");
        WELL_KNOWN_MIME_TYPES.put(0x031A, "application/vnd.sbm.cid");
        WELL_KNOWN_MIME_TYPES.put(0x031B, "application/vnd.oma.bcast.provisioningtrigger");

WELL_KNOWN_PARAMETERS.put(0x00, "Q");
WELL_KNOWN_PARAMETERS.put(0x01, "Charset");
//Synthetic comment -- @@ -180,209 +179,214 @@
WELL_KNOWN_PARAMETERS.put(0x1A, "Start-info");
WELL_KNOWN_PARAMETERS.put(0x1B, "Comment");
WELL_KNOWN_PARAMETERS.put(0x1C, "Domain");
        WELL_KNOWN_PARAMETERS.put(0x1D, "Path");

    }

    public void testWellKnownShortIntegerMimeTypeValues() {

        int numberFound = 0;

        for (int value : Wap230WspContentTypeTest.WELL_KNOWN_MIME_TYPES.keySet()) {
            if (value < 128) {
                WspTypeDecoder unit =
                    new WspTypeDecoder(HexDump.toByteArray((byte) (value | 0x80)));
assertTrue(unit.decodeContentType(0));
                String mimeType = unit.getValueString();
                int wellKnownValue = (int) unit.getValue32();
                assertEquals(Wap230WspContentTypeTest.WELL_KNOWN_MIME_TYPES.get(value), mimeType);
                assertEquals(value, wellKnownValue);
                assertEquals(1, unit.getDecodedDataLength());
                numberFound++;
            }
        }

        assertEquals(0x55, numberFound);
    }

    public void testWellKnownLongIntegerMimeTypeValues() {

        int numberFound = 0;

        for (int value : Wap230WspContentTypeTest.WELL_KNOWN_MIME_TYPES.keySet()) {
            if (value > 0x0200) {
                byte[] stuff = new byte[10];
                stuff[0] = 3; // Value Length Short Length
                stuff[1] = 2; // Long Integer Short Length
                stuff[2] = (byte) (value >> 8);
                stuff[3] = (byte) (value & 0xFF);
                WspTypeDecoder unit = new WspTypeDecoder(stuff);
                assertTrue(unit.decodeContentType(0));
                String mimeType = unit.getValueString();
                int wellKnownValue = (int) unit.getValue32();
                assertEquals(Wap230WspContentTypeTest.WELL_KNOWN_MIME_TYPES.get(value), mimeType);
                assertEquals(value, wellKnownValue);
                assertEquals(4, unit.getDecodedDataLength());
                numberFound++;
            }
        }

        assertEquals(0x28, numberFound);
    }

    public void testConstrainedMediaExtensionMedia() throws Exception {

        String testType = "application/wibble";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(testType.getBytes("US-ASCII"));
        out.write(0x00);
WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));
String mimeType = unit.getValueString();
        assertEquals("application/wibble", mimeType);
        assertEquals(-1, unit.getValue32());
        assertEquals(19, unit.getDecodedDataLength());
    }

    public void testGeneralFormShortLengthExtensionMedia() throws Exception {

        String testType = "12345678901234567890123456789";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(30);
        out.write(testType.getBytes("US-ASCII"));
        out.write(0x00);

        WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
        assertTrue(unit.decodeContentType(0));

        String mimeType = unit.getValueString();
        assertEquals("12345678901234567890123456789", mimeType);
        assertEquals(-1, unit.getValue32());
        assertEquals(31, unit.getDecodedDataLength());
    }

    public void testGeneralFormShortLengthWellKnownShortInteger() throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(0x01); // Value-length, short-length
        out.write(0x3F | 0x80); // Well-known-media, short-integer

        WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
        assertTrue(unit.decodeContentType(0));

        String mimeType = unit.getValueString();
        assertEquals("application/vnd.wap.rollover-certificate", mimeType);
        assertEquals(0x3F, unit.getValue32());
        assertEquals(2, unit.getDecodedDataLength());

    }

    public void testGeneralFormShortLengthWellKnownLongInteger() throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        out.write(0x03); // Value-length, short-length
        out.write(0x02); // Well-known-media, long-integer (2 octets)
        out.write(0x03); // long int byte 1
        out.write(0x14); // long int byte 2

        WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
        assertTrue(unit.decodeContentType(0));

        String mimeType = unit.getValueString();

        assertEquals("application/oma-directory+xml", mimeType);
        assertEquals(0x0314, unit.getValue32());
        assertEquals(4, unit.getDecodedDataLength());

    }

    public void testGeneralFormLengthQuoteWellKnownShortInteger() throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        out.write(31); // Value-length, length-quote
        out.write(0x01); // Length as UINTVAR
        out.write(0x3F | 0x80); // Well-known-media, short-integer

        WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
        assertTrue(unit.decodeContentType(0));

        String mimeType = unit.getValueString();
        assertEquals("application/vnd.wap.rollover-certificate", mimeType);
        assertEquals(0x3F, unit.getValue32());
        assertEquals(3, unit.getDecodedDataLength());

    }

    public void testGeneralFormLengthQuoteWellKnownLongInteger() throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        out.write(31); // Value-length, length-quote
        out.write(0x03); // Length as UINTVAR
        out.write(0x02); // Well-known-media, long-integer (2 octets)
        out.write(0x03); // long int byte 1
        out.write(0x14); // long int byte 2

        WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
        assertTrue(unit.decodeContentType(0));

        String mimeType = unit.getValueString();

assertEquals("application/oma-directory+xml", mimeType);
assertEquals(0x0314, unit.getValue32());
assertEquals(5, unit.getDecodedDataLength());

    }

    public void testGeneralFormLengthQuoteExtensionMedia() throws Exception {

        String testType = "application/wibble";
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        out.write(31); // Value-length, length-quote
        out.write(testType.length() + 1); // Length as UINTVAR

        out.write(testType.getBytes("US-ASCII"));
        out.write(0x00);

        WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
        assertTrue(unit.decodeContentType(0));

String mimeType = unit.getValueString();

assertEquals("application/wibble", mimeType);
assertEquals(-1, unit.getValue32());
assertEquals(21, unit.getDecodedDataLength());

    }

    public void testGeneralFormLengthQuoteExtensionMediaWithNiceLongMimeType() throws Exception {

        String testType =
                "01234567890123456789012345678901234567890123456789012345678901234567890123456789"
                + "01234567890123456789012345678901234567890123456789012345678901234567890123456789";
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        out.write(31); // Value-length, length-quote
        out.write(0x81); // Length as UINTVAR (161 decimal, 0xA1), 2 bytes
        out.write(0x21);

        out.write(testType.getBytes("US-ASCII"));
        out.write(0x00);

        WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));

String mimeType = unit.getValueString();

        assertEquals(
                "01234567890123456789012345678901234567890123456789012345678901234567890123456789"
                + "01234567890123456789012345678901234567890123456789012345678901234567890123456789",
                mimeType);
assertEquals(-1, unit.getValue32());
assertEquals(164, unit.getDecodedDataLength());

    }

    public void testConstrainedMediaExtensionMediaWithSpace() throws Exception {

String testType = " application/wibble";
ByteArrayOutputStream out = new ByteArrayOutputStream();
out.write(testType.getBytes("US-ASCII"));
//Synthetic comment -- @@ -390,37 +394,37 @@

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));

String mimeType = unit.getValueString();

assertEquals(" application/wibble", mimeType);
assertEquals(-1, unit.getValue32());
assertEquals(20, unit.getDecodedDataLength());

    }

    public void testTypedParamWellKnownShortIntegerNoValue() throws Exception {

ByteArrayOutputStream out = new ByteArrayOutputStream();
out.write(0x03); // Value-length, short-length
out.write(0x3F | 0x80); // Well-known-media, short-integer
out.write(0x1C | 0x80); // Well-known-parameter token ('Domain'),
        // Short-integer
out.write(0x00); // No value

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));

String mimeType = unit.getValueString();

assertEquals("application/vnd.wap.rollover-certificate", mimeType);
assertEquals(0x3F, unit.getValue32());

assertEquals(4, unit.getDecodedDataLength());

Map<String, String> params = unit.getContentParameters();
        assertEquals(null, params.get("Domain"));

}

public void testTypedParamWellKnownShortIntegerTokenText() throws Exception {
//Synthetic comment -- @@ -429,18 +433,18 @@
out.write(0x14); // Value-length, short-length
out.write(0x3F | 0x80); // Well-known-media, short-integer
out.write(0x1C | 0x80); // Well-known-parameter token ('Domain'),
        // Short-integer
out.write("wdstechnology.com".getBytes("US-ASCII")); // Token text
out.write(0x00); // EOS

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));

String mimeType = unit.getValueString();

assertEquals("application/vnd.wap.rollover-certificate", mimeType);
assertEquals(0x3F, unit.getValue32());

assertEquals(21, unit.getDecodedDataLength());

Map<String, String> params = unit.getContentParameters();
//Synthetic comment -- @@ -455,92 +459,93 @@
out.write(0x3F | 0x80); // Well-known-media, short-integer
out.write(0x01); // Short-length of well-known parameter token
out.write(0x1C); // Well-known-parameter token ('Domain'),
        // Long-integer
out.write("wdstechnology.com".getBytes("US-ASCII")); // Token text
out.write(0x00); // EOS

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));

String mimeType = unit.getValueString();

assertEquals("application/vnd.wap.rollover-certificate", mimeType);
assertEquals(0x3F, unit.getValue32());

assertEquals(22, unit.getDecodedDataLength());

Map<String, String> params = unit.getContentParameters();
assertEquals("wdstechnology.com", params.get("Domain"));

}

public void testTypedParamWellKnownShortIntegerQuotedText() throws Exception {

ByteArrayOutputStream out = new ByteArrayOutputStream();
out.write(0x15); // Value-length, short-length
out.write(0x3F | 0x80); // Well-known-media, short-integer
out.write(0x1C | 0x80); // Well-known-parameter token ('Domain'),
        // Short-integer
        out.write(34); // Quote
out.write("wdstechnology.com".getBytes("US-ASCII")); // Token text
out.write(0x00); // EOS

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));

String mimeType = unit.getValueString();

assertEquals("application/vnd.wap.rollover-certificate", mimeType);
assertEquals(0x3F, unit.getValue32());
assertEquals(22, unit.getDecodedDataLength());

Map<String, String> params = unit.getContentParameters();
assertEquals("wdstechnology.com", params.get("Domain"));

}

public void testTypedParamWellKnownShortIntegerCompactIntegerValue() throws Exception {

ByteArrayOutputStream out = new ByteArrayOutputStream();
out.write(0x3); // Value-length, short-length
out.write(0x3F | 0x80); // Well-known-media, short-integer
out.write(0x11 | 0x80); // Well-known-parameter token ('SEC'),
        // Short-integer
out.write(0x01 | 0x80); // Short-integer value

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));

String mimeType = unit.getValueString();

assertEquals("application/vnd.wap.rollover-certificate", mimeType);
assertEquals(0x3F, unit.getValue32());
assertEquals(4, unit.getDecodedDataLength());

Map<String, String> params = unit.getContentParameters();
assertEquals("1", params.get("SEC"));

}

public void testTypedParamWellKnownShortIntegerMultipleParameters() throws Exception {

ByteArrayOutputStream out = new ByteArrayOutputStream();
out.write(0x0B); // Value-length, short-length
out.write(0x3F | 0x80); // Well-known-media, short-integer
out.write(0x11 | 0x80); // Well-known-parameter token ('SEC'),
        // Short-integer
out.write(0x01 | 0x80); // Short-integer value

        out.write(0x12 | 0x80); // Well-known-parameter token ('MAC'), short
                                // integer
out.write(34);
out.write("imapc".getBytes("US-ASCII"));
out.write(0x00);

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));

String mimeType = unit.getValueString();

assertEquals("application/vnd.wap.rollover-certificate", mimeType);
assertEquals(0x3F, unit.getValue32());
assertEquals(12, unit.getDecodedDataLength());
//Synthetic comment -- @@ -549,15 +554,15 @@
assertEquals("1", params.get("SEC"));
assertEquals("imapc", params.get("MAC"));
}

public void testUntypedParamIntegerValueShortInteger() throws Exception {
ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(0x0A); // Value-length, short-length
        out.write(0x3F | 0x80); // Well-known-media, short-integer
        out.write("MYPARAM".getBytes("US-ASCII")); // Untyped Param
        out.write(0x00); // EOS
        out.write(0x45 | 0x80); // Short Integer value

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));

//Synthetic comment -- @@ -566,21 +571,21 @@
assertEquals("application/vnd.wap.rollover-certificate", mimeType);
assertEquals(0x3F, unit.getValue32());
assertEquals(11, unit.getDecodedDataLength());

Map<String, String> params = unit.getContentParameters();
assertEquals("69", params.get("MYPARAM"));
}

public void testUntypedParamIntegerValueLongInteger() throws Exception {
ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(0x0C); // Value-length, short-length
        out.write(0x3F | 0x80); // Well-known-media, short-integer
        out.write("MYPARAM".getBytes("US-ASCII")); // Untyped Param
        out.write(0x00); // EOS
        out.write(0x02); // Short Length
        out.write(0x42); // Long Integer byte 1
        out.write(0x69); // Long Integer byte 2

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));

//Synthetic comment -- @@ -589,19 +594,19 @@
assertEquals("application/vnd.wap.rollover-certificate", mimeType);
assertEquals(0x3F, unit.getValue32());
assertEquals(13, unit.getDecodedDataLength());

Map<String, String> params = unit.getContentParameters();
assertEquals("17001", params.get("MYPARAM"));
}

public void testUntypedParamTextNoValue() throws Exception {
ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(0x0A); // Value-length, short-length
        out.write(0x3F | 0x80); // Well-known-media, short-integer
        out.write("MYPARAM".getBytes("US-ASCII")); // Untyped Param
        out.write(0x00); // EOS
        out.write(0x00); // No value

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));
String mimeType = unit.getValueString();
//Synthetic comment -- @@ -609,21 +614,21 @@
assertEquals("application/vnd.wap.rollover-certificate", mimeType);
assertEquals(0x3F, unit.getValue32());
assertEquals(11, unit.getDecodedDataLength());

Map<String, String> params = unit.getContentParameters();
        assertEquals(null, params.get("MYPARAM"));

}

public void testUntypedParamTextTokenText() throws Exception {
ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(0x11); // Value-length, short-length
        out.write(0x3F | 0x80); // Well-known-media, short-integer
        out.write("MYPARAM".getBytes("US-ASCII")); // Untyped Param
        out.write(0x00); // EOS
        out.write("myvalue".getBytes("US-ASCII")); // Token text
        out.write(0x00); // EOS

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));
String mimeType = unit.getValueString();
//Synthetic comment -- @@ -631,21 +636,21 @@
assertEquals("application/vnd.wap.rollover-certificate", mimeType);
assertEquals(0x3F, unit.getValue32());
assertEquals(18, unit.getDecodedDataLength());

Map<String, String> params = unit.getContentParameters();
assertEquals("myvalue", params.get("MYPARAM"));
}

public void testUntypedParamTextQuotedString() throws Exception {
ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(0x11); // Value-length, short-length
        out.write(0x3F | 0x80); // Well-known-media, short-integer
        out.write("MYPARAM".getBytes("US-ASCII")); // Untyped Param
        out.write(0x00); // EOS
out.write(34);
        out.write("myvalue".getBytes("US-ASCII")); // Token text
        out.write(0x00); // EOS

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));
String mimeType = unit.getValueString();
//Synthetic comment -- @@ -653,20 +658,20 @@
assertEquals("application/vnd.wap.rollover-certificate", mimeType);
assertEquals(0x3F, unit.getValue32());
assertEquals(19, unit.getDecodedDataLength());

Map<String, String> params = unit.getContentParameters();
assertEquals("myvalue", params.get("MYPARAM"));

}

public void testTypedParamTextQValue() throws Exception {
ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(0x04); // Value-length, short-length
        out.write(0x3F | 0x80); // Well-known-media, short-integer
        out.write(0x00); // Typed Param ('Q')
        out.write(0x83); // Q value byte 1
        out.write(0x31); // Q value byte 2

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));
String mimeType = unit.getValueString();
//Synthetic comment -- @@ -674,12 +679,11 @@
assertEquals("application/vnd.wap.rollover-certificate", mimeType);
assertEquals(0x3F, unit.getValue32());
assertEquals(5, unit.getDecodedDataLength());

Map<String, String> params = unit.getContentParameters();
assertEquals("433", params.get("Q"));

}

public void testTypedParamUnassignedWellKnownShortIntegerTokenText() throws Exception {

//Synthetic comment -- @@ -687,18 +691,18 @@
out.write(0x14); // Value-length, short-length
out.write(0x3F | 0x80); // Well-known-media, short-integer
out.write(0x42 | 0x80); // Unassigned Well-known-parameter token,
        // Short-integer
out.write("wdstechnology.com".getBytes("US-ASCII")); // Token text
out.write(0x00); // EOS

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));

String mimeType = unit.getValueString();

assertEquals("application/vnd.wap.rollover-certificate", mimeType);
assertEquals(0x3F, unit.getValue32());

assertEquals(21, unit.getDecodedDataLength());

Map<String, String> params = unit.getContentParameters();
//Synthetic comment -- @@ -713,25 +717,23 @@
out.write(0x3F | 0x80); // Well-known-media, short-integer
out.write(0x01); // Short-length of well-known parameter token
out.write(0x42); // Well-known-parameter token ('Domain'),
        // Long-integer
out.write("wdstechnology.com".getBytes("US-ASCII")); // Token text
out.write(0x00); // EOS

WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertTrue(unit.decodeContentType(0));

String mimeType = unit.getValueString();

assertEquals("application/vnd.wap.rollover-certificate", mimeType);
assertEquals(0x3F, unit.getValue32());

assertEquals(22, unit.getDecodedDataLength());

Map<String, String> params = unit.getContentParameters();
assertEquals("wdstechnology.com", params.get("unassigned/0x42"));

}

}







