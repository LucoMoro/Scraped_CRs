/*New MIME-Type added(application/vnd.wap.connectivity-wbxml).
dispatchWapPdu method modified for content types with value-length parameters.
This MIME-Type is used for wap-push messages which sends wbxml data to the device(configuration SMS).*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/SMSDispatcher.java
//Synthetic comment -- index daf4b9c..826fae5 100644

//Synthetic comment -- @@ -86,6 +86,10 @@
private static final byte WAP_CO_MIME_PORT = (byte)0xb2;

private static final String WAP_CO_MIME_TYPE = "application/vnd.wap.coc";
 
    private static final byte WAP_CON_WBXML_MIME_PORT = (byte)0xb6;
    
    private static final String WAP_CON_WBXML_MIME_TYPE = "application/vnd.wap.connectivity-wbxml";

private static final int WAP_PDU_SHORT_LENGTH_MAX = 30;

//Synthetic comment -- @@ -652,6 +656,7 @@
int transactionId = pdu[index++] & 0xFF;
int pduType = pdu[index++] & 0xFF;
int headerLength = 0;
        int contentTypeLength = 0;

if ((pduType != WAP_PDU_TYPE_PUSH) &&
(pduType != WAP_PDU_TYPE_CONFIRMED_PUSH)) {
//Synthetic comment -- @@ -685,20 +690,33 @@
* Short-length = <Any octet 0-30>   (octet <= WAP_PDU_SHORT_LENGTH_MAX)
* Length-quote = <Octet 31>         (WAP_PDU_LENGTH_QUOTE)
* Length = Uintvar-integer
         * Constrained-media = Constrained-encoding
         * Constrained-encoding = Extension-Media | Short-integer
         * Extension-media = *TEXT End-of-string
*/
        //Is the Content-type-value a Constrained-media or Content-general-form
        if((pdu[index] & 0x80) == 0){
        
	        // Parse Value-length.
	        if ((pdu[index] & 0xff) <= WAP_PDU_SHORT_LENGTH_MAX) {
	            // Short-length.
	        	contentTypeLength = pdu[index];
	            index++;
	        } else if (pdu[index] == WAP_PDU_LENGTH_QUOTE) {
	            // Skip Length-quote.
	            index++;
	            // we have an uintvar again
	            temp = 0;
	            do {
	                temp = pdu[index++];
	                contentTypeLength = contentTypeLength << 7;
	                contentTypeLength |= temp & 0x7F;
	            } while ((temp & 0x80) != 0);
	        }
}
        int contentType = index;
String mimeType;
        switch (pdu[contentType])
{
case DRM_RIGHTS_XML:
mimeType = DRM_RIGHTS_XML_MIME_TYPE;
//Synthetic comment -- @@ -716,6 +734,9 @@
case WAP_CO_MIME_PORT:
mimeType = WAP_CO_MIME_TYPE;
break;
        case WAP_CON_WBXML_MIME_PORT:
        	mimeType = WAP_CON_WBXML_MIME_TYPE;
        	break;
default:
int start = index;

//Synthetic comment -- @@ -731,7 +752,7 @@
// XXX Skip the remainder of the header for now
int dataIndex = headerStartIndex + headerLength;
byte[] data;
        if (pdu[contentType] == WAP_CO_MIME_PORT)
{
// because SMSDispatcher can't parse push headers "Content-Location" and
// X-Wap-Content-URI, so pass the whole push to CO application.







