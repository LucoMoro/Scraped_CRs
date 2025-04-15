/*Decode the msg encoded GSM-Data-Coding-Scheme type

Need to decode GSM-Data-Coding-Scheme type based on3GPP2 CR1001-E v1.0 table 9.1-1
Step one. Add import java.io.UnsupportedEncodingException;
Step two. " case UserData.ENCODING_GSM_DCS: " statement in decodeUserDataPayload() function.
Step three. Add decodeGsmDCS() function.(according to Section 4 of 3GPP TS 23.038. )
Modify the com.android.internal.telephony.gsm.SmsMessage.parseUserData() ,because GSM DCS type follows  3GPP TS 23.038.

Change-Id:Ib75a3a91248b4ef50fb328e727edecc2a4fc960bSigned-off-by: kyunga kim <kyunga1.kim@lge.com>*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/sms/BearerData.java b/src/java/com/android/internal/telephony/cdma/sms/BearerData.java
old mode 100755
new mode 100644
//Synthetic comment -- index 7e7347f..cee8536

//Synthetic comment -- @@ -35,6 +35,8 @@
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
* An object to encode and decode CDMA SMS bearer data.
//Synthetic comment -- @@ -1106,6 +1108,12 @@
case UserData.ENCODING_LATIN:
userData.payloadStr = decodeLatin(userData.payload, offset, userData.numFields);
break;
default:
throw new CodingException("unsupported user data encoding ("
+ userData.msgEncoding + ")");
//Synthetic comment -- @@ -1935,4 +1943,124 @@
}
return null;
}
}







