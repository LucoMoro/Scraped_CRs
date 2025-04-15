/*resolved conflicts

Change-Id:I6d795d11ef624c1175d1097b3c6715e191a14a3d*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WapPushOverSms.java b/telephony/java/com/android/internal/telephony/WapPushOverSms.java
//Synthetic comment -- index aab32ed..0928c0f 100644

//Synthetic comment -- @@ -134,12 +134,7 @@
break;
case WspTypeDecoder.CONTENT_TYPE_B_EMN:
	mimeType = WspTypeDecoder.CONTENT_MIME_TYPE_B_EMN;
	break;
default:
if (Config.LOGD) {
Log.w(LOG_TAG,








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WspTypeDecoder.java b/telephony/java/com/android/internal/telephony/WspTypeDecoder.java
//Synthetic comment -- index 867dac0..7af76af 100644

//Synthetic comment -- @@ -39,12 +39,8 @@
public static final int CONTENT_TYPE_B_PUSH_SL = 0x30;
public static final int CONTENT_TYPE_B_PUSH_CO = 0x32;
public static final int CONTENT_TYPE_B_MMS = 0x3e;
public static final int CONTENT_TYPE_B_VND_DOCOMO_PF = 0x0310;
public static final int CONTENT_TYPE_B_EMN = 0x030A;

public static final String CONTENT_MIME_TYPE_B_DRM_RIGHTS_XML =
"application/vnd.oma.drm.rights+xml";
//Synthetic comment -- @@ -54,12 +50,8 @@
public static final String CONTENT_MIME_TYPE_B_PUSH_SL = "application/vnd.wap.slc";
public static final String CONTENT_MIME_TYPE_B_PUSH_CO = "application/vnd.wap.coc";
public static final String CONTENT_MIME_TYPE_B_MMS = "application/vnd.wap.mms-message";
public static final String CONTENT_MIME_TYPE_B_VND_DOCOMO_PF = "application/vnd.docomo.pf";
public static final String CONTENT_MIME_TYPE_B_EMN = "application/vnd.wap.emn+wbxml";

public static final int PARAMETER_ID_X_WAP_APPLICATION_ID = 0x2f;








