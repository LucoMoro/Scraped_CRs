/*Enhance SMSDispatcher to correctly handle Wap230Wsp Content-type headers

This patch adds all Well Known WSP Content types and allows for the
parsing of Content Type Parameters, which are then passed on as part of
the WAP_PUSH intent notification.*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/SMSDispatcher.java
//Synthetic comment -- index daf4b9c..887a1c1 100644

//Synthetic comment -- @@ -66,30 +66,11 @@

private static final int WAP_PDU_TYPE_CONFIRMED_PUSH = 0x07;

    private static final byte DRM_RIGHTS_XML = (byte)0xca;

    private static final String DRM_RIGHTS_XML_MIME_TYPE = "application/vnd.oma.drm.rights+xml";

    private static final byte DRM_RIGHTS_WBXML = (byte)0xcb;

    private static final String DRM_RIGHTS_WBXML_MIME_TYPE =
            "application/vnd.oma.drm.rights+wbxml";

    private static final byte WAP_SI_MIME_PORT = (byte)0xae;

    private static final String WAP_SI_MIME_TYPE = "application/vnd.wap.sic";

    private static final byte WAP_SL_MIME_PORT = (byte)0xb0;

    private static final String WAP_SL_MIME_TYPE = "application/vnd.wap.slc";

private static final byte WAP_CO_MIME_PORT = (byte)0xb2;

    private static final String WAP_CO_MIME_TYPE = "application/vnd.wap.coc";

    private static final int WAP_PDU_SHORT_LENGTH_MAX = 30;

    private static final int WAP_PDU_LENGTH_QUOTE = 31;

private static final String MMS_MIME_TYPE = "application/vnd.wap.mms-message";

//Synthetic comment -- @@ -674,60 +655,9 @@

int headerStartIndex = index;

        /**
         * Parse Content-Type.
         * From wap-230-wsp-20010705-a section 8.4.2.24
         *
         * Content-type-value = Constrained-media | Content-general-form
         * Content-general-form = Value-length Media-type
         * Media-type = (Well-known-media | Extension-Media) *(Parameter)
         * Value-length = Short-length | (Length-quote Length)
         * Short-length = <Any octet 0-30>   (octet <= WAP_PDU_SHORT_LENGTH_MAX)
         * Length-quote = <Octet 31>         (WAP_PDU_LENGTH_QUOTE)
         * Length = Uintvar-integer
         */
        // Parse Value-length.
        if ((pdu[index] & 0xff) <= WAP_PDU_SHORT_LENGTH_MAX) {
            // Short-length.
            index++;
        } else if (pdu[index] == WAP_PDU_LENGTH_QUOTE) {
            // Skip Length-quote.
            index++;
            // Skip Length.
            // Now we assume 8bit is enough to store the content-type length.
            index++;
        }
        String mimeType;
        switch (pdu[headerStartIndex])
        {
        case DRM_RIGHTS_XML:
            mimeType = DRM_RIGHTS_XML_MIME_TYPE;
            break;
        case DRM_RIGHTS_WBXML:
            mimeType = DRM_RIGHTS_WBXML_MIME_TYPE;
            break;
        case WAP_SI_MIME_PORT:
            // application/vnd.wap.sic
            mimeType = WAP_SI_MIME_TYPE;
            break;
        case WAP_SL_MIME_PORT:
            mimeType = WAP_SL_MIME_TYPE;
            break;
        case WAP_CO_MIME_PORT:
            mimeType = WAP_CO_MIME_TYPE;
            break;
        default:
            int start = index;

            // Skip text-string.
            // Now we assume the mimetype is Extension-Media.
            while (pdu[index++] != '\0') {
                ;
            }
            mimeType = new String(pdu, start, index-start-1);
            break;
        }

// XXX Skip the remainder of the header for now
int dataIndex = headerStartIndex + headerLength;
byte[] data;
//Synthetic comment -- @@ -748,6 +678,7 @@
intent.putExtra("transactionId", transactionId);
intent.putExtra("pduType", pduType);
intent.putExtra("data", data);

if (mimeType.equals(MMS_MIME_TYPE)) {
mPhone.getContext().sendBroadcast(








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/Wap230WspContentType.java b/telephony/java/com/android/internal/telephony/gsm/Wap230WspContentType.java
new file mode 100644
//Synthetic comment -- index 0000000..1075494

//Synthetic comment -- @@ -0,0 +1,371 @@
\ No newline at end of file








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
//Synthetic comment -- index 0000000..bee450f

//Synthetic comment -- @@ -0,0 +1,531 @@







