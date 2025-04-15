/*Added support for wap messages of type application/vnd.wap.emn+wbxml.

Change-Id:Ic7d7d1cb971a29d4d390659342065d953addb079*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WapPushOverSms.java b/telephony/java/com/android/internal/telephony/WapPushOverSms.java
//Synthetic comment -- index 86d6523..9bc5630 100644

//Synthetic comment -- @@ -137,7 +137,8 @@
break;
default:
if (Config.LOGD) {
                        Log.w(LOG_TAG,"Received PDU. Unsupported Content-Type = " + binaryContentType);
}
return Intents.RESULT_SMS_HANDLED;
}
//Synthetic comment -- @@ -159,8 +160,9 @@
} else if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_EMN)) {
binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_EMN;
} else {
                if (Config.LOGD)
Log.w(LOG_TAG, "Received PDU. Unknown Content-Type = " + mimeType);
return Intents.RESULT_SMS_HANDLED;
}
}







