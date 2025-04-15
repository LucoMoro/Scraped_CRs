/*Decode the Message that encoded by SHIFT_JIS type

There is need decode the msg encoded by SHIFT_JIS.
(Refer to 3GPP2 CR1001-E v1.0 table 9.1-1)
1. Add " case UserData.ENCODING_SHIFT_JIS: " statement
2. decodeShiftJis() : Create the new String based on SHIFT_JIS(SJIS).
Signed-off-by: kyunga kim <kyunga1.kim@lge.com>
Change-Id:Id5c96615f535dfc1a8d323ad00548c0786608861*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/sms/BearerData.java b/src/java/com/android/internal/telephony/cdma/sms/BearerData.java
old mode 100755
new mode 100644
//Synthetic comment -- index 7e7347f..053c0f0

//Synthetic comment -- @@ -1106,6 +1106,9 @@
case UserData.ENCODING_LATIN:
userData.payloadStr = decodeLatin(userData.payload, offset, userData.numFields);
break;
default:
throw new CodingException("unsupported user data encoding ("
+ userData.msgEncoding + ")");
//Synthetic comment -- @@ -1935,4 +1938,21 @@
}
return null;
}
}







