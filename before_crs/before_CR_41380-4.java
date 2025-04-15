/*Decode the msg encoded GSM-Data-Coding-Scheme type

To support the GSM-Data-Coding-Scheme(GSM DCS) type,
decodeGsmDCS() method is added newly.
(See 3GPP2 C.R1001-E v1.0 table 9.1-1)

GSM DCS type is  based on Section 4 of 3GPP TS 23.038.
decodeGsmDCS() method modify the a part of
com.android.internal.telephony.gsm.SmsMessage.parseUserData().

Change-Id:Ib75a3a91248b4ef50fb328e727edecc2a4fc960bSigned-off-by: kyunga kim <kyunga1.kim@lge.com>*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/sms/BearerData.java b/src/java/com/android/internal/telephony/cdma/sms/BearerData.java
old mode 100755
new mode 100644
//Synthetic comment -- index 7e7347f..ecd67eb

//Synthetic comment -- @@ -35,6 +35,7 @@
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
* An object to encode and decode CDMA SMS bearer data.
//Synthetic comment -- @@ -1106,6 +1107,10 @@
case UserData.ENCODING_LATIN:
userData.payloadStr = decodeLatin(userData.payload, offset, userData.numFields);
break;
default:
throw new CodingException("unsupported user data encoding ("
+ userData.msgEncoding + ")");
//Synthetic comment -- @@ -1935,4 +1940,109 @@
}
return null;
}
}







