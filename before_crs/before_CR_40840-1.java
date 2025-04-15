/*Implement to decode GSM-Data-Coding-Scheme type message

Modify to decode the Message that encoded by GSM-Data-Coding-Scheme type based on 3GPP2 CR1001-E v1.0 table 9.1-1
1. import java.io.UnsupportedEncodingException; add.
2. " case UserData.ENCODING_GSM_DCS: " statement in decodeUserDataPayload() function.
	if UserData.msgEncoding value same ENCODING_GSM_DCS , the device need to decode the message by GSM_DCS type according to Section 4 of 3GPP TS 23.038.
	so, call the decodeGsmDCS() function. (we implement..)
3. Add decodeGsmDCS() function.
	Modify the com.android.internal.telephony.gsm.SmsMessage.parseUserData() ,because GSM DCS type follows  3GPP TS 23.038.
Signed-off-by: kyunga kim <kyunga1.kim@lge.com>

Change-Id:I38d484d7362e854d69d3dc55ef79e82704828a0d*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/sms/BearerData.java b/src/java/com/android/internal/telephony/cdma/sms/BearerData.java
old mode 100755
new mode 100644
//Synthetic comment -- index 7e7347f..b3ffcaa

//Synthetic comment -- @@ -35,6 +35,7 @@
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
* An object to encode and decode CDMA SMS bearer data.
//Synthetic comment -- @@ -1106,6 +1107,12 @@
case UserData.ENCODING_LATIN:
userData.payloadStr = decodeLatin(userData.payload, offset, userData.numFields);
break;
default:
throw new CodingException("unsupported user data encoding ("
+ userData.msgEncoding + ")");
//Synthetic comment -- @@ -1935,4 +1942,144 @@
}
return null;
}
}







