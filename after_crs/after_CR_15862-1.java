/*Added support for wap messages of type application/vnd.wap.emn+wbxml.

This allows for email notification messages to be accepted.

The Email project has also been changed to accept and process
the emn message.

seehttps://review.source.android.com/15830Change-Id:I9c5f5659f637794f06218c90d44ff462906baedfmodified:   telephony/java/com/android/internal/telephony/WapPushOverSms.java
	modified:   telephony/java/com/android/internal/telephony/WspTypeDecoder.java

	modified:   telephony/java/com/android/internal/telephony/WapPushOverSms.java
	modified:   telephony/java/com/android/internal/telephony/WspTypeDecoder.java*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WapPushOverSms.java b/telephony/java/com/android/internal/telephony/WapPushOverSms.java
//Synthetic comment -- index a636a4b..61a34f0 100644

//Synthetic comment -- @@ -132,6 +132,9 @@
case WspTypeDecoder.CONTENT_TYPE_B_VND_DOCOMO_PF:
mimeType = WspTypeDecoder.CONTENT_MIME_TYPE_B_VND_DOCOMO_PF;
break;
                case WspTypeDecoder.CONTENT_TYPE_B_EMN:
                    mimeType = WspTypeDecoder.CONTENT_MIME_TYPE_B_EMN;
                    break;
default:
if (Config.LOGD) {
Log.w(LOG_TAG,
//Synthetic comment -- @@ -154,6 +157,8 @@
binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_MMS;
} else if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_VND_DOCOMO_PF)) {
binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_VND_DOCOMO_PF;
            } else if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_EMN)) {
                binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_EMN;
} else {
if (Config.LOGD) Log.w(LOG_TAG, "Received PDU. Unknown Content-Type = " + mimeType);
return Intents.RESULT_SMS_HANDLED;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WspTypeDecoder.java b/telephony/java/com/android/internal/telephony/WspTypeDecoder.java
//Synthetic comment -- index 336bc82..eea9a78 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
public static final int CONTENT_TYPE_B_PUSH_SL = 0x30;
public static final int CONTENT_TYPE_B_PUSH_CO = 0x32;
public static final int CONTENT_TYPE_B_MMS = 0x3e;
    public static final int CONTENT_TYPE_B_EMN = 0x030A;
public static final int CONTENT_TYPE_B_VND_DOCOMO_PF = 0x0310;

public static final String CONTENT_MIME_TYPE_B_DRM_RIGHTS_XML =
//Synthetic comment -- @@ -49,6 +50,7 @@
public static final String CONTENT_MIME_TYPE_B_PUSH_SL = "application/vnd.wap.slc";
public static final String CONTENT_MIME_TYPE_B_PUSH_CO = "application/vnd.wap.coc";
public static final String CONTENT_MIME_TYPE_B_MMS = "application/vnd.wap.mms-message";
    public static final String CONTENT_MIME_TYPE_B_EMN = "application/vnd.wap.emn+wbxml";
public static final String CONTENT_MIME_TYPE_B_VND_DOCOMO_PF = "application/vnd.docomo.pf";

public static final int PARAMETER_ID_X_WAP_APPLICATION_ID = 0x2f;







