/*Telephony: Support ShiftJIS encoding and Skip unsupported bearer data subparam

Support Shift-JIS CHARi encoding type per 3GPP2 C.R1001 section 9.1.

3GPP2 C.S0015-B, v2.0, table 4.5-1 shows bearer data subparam ID
above 0x17 is reserved. Fix to skip reserved bearer data subparams
instead of throwing CodingException.

Change-Id:Ib83e7e72189a267421ba5fd77695b8f684ef00e2*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/sms/BearerData.java b/src/java/com/android/internal/telephony/cdma/sms/BearerData.java
//Synthetic comment -- index 5518faa..e651356 100644

//Synthetic comment -- @@ -70,6 +70,9 @@
//private final static byte SUBPARAM_ENHANCED_VMN                     = 0x16;
//private final static byte SUBPARAM_ENHANCED_VMN_ACK                 = 0x17;

/**
* Supported message types for CDMA SMS messages
* (See 3GPP2 C.S0015-B, v2.0, table 4.5.1-1)
//Synthetic comment -- @@ -623,6 +626,14 @@
}
}

private static void encodeUserDataPayload(UserData uData)
throws CodingException
{
//Synthetic comment -- @@ -660,6 +671,9 @@
} else if (uData.msgEncoding == UserData.ENCODING_UNICODE_16) {
uData.payload = encodeUtf16(uData.payloadStr);
uData.numFields = uData.payloadStr.length();
} else {
throw new CodingException("unsupported user data encoding (" +
uData.msgEncoding + ")");
//Synthetic comment -- @@ -968,6 +982,27 @@
return decodeSuccess;
}

private static boolean decodeUserData(BearerData bData, BitwiseInputStream inStream)
throws BitwiseInputStream.AccessException
{
//Synthetic comment -- @@ -1081,6 +1116,16 @@
return decodeCharset(data, offset, numFields, 1, "ISO-8859-1");
}

private static void decodeUserDataPayload(UserData userData, boolean hasUserDataHeader)
throws CodingException
{
//Synthetic comment -- @@ -1132,6 +1177,9 @@
case UserData.ENCODING_LATIN:
userData.payloadStr = decodeLatin(userData.payload, offset, userData.numFields);
break;
default:
throw new CodingException("unsupported user data encoding ("
+ userData.msgEncoding + ")");
//Synthetic comment -- @@ -1870,7 +1918,15 @@
while (inStream.available() > 0) {
int subparamId = inStream.read(8);
int subparamIdBit = 1 << subparamId;
                if ((foundSubparamMask & subparamIdBit) != 0) {
throw new CodingException("illegal duplicate subparameter (" +
subparamId + ")");
}
//Synthetic comment -- @@ -1934,10 +1990,13 @@
decodeSuccess = decodeServiceCategoryProgramData(bData, inStream);
break;
default:
                    throw new CodingException("unsupported bearer data subparameter ("
                                              + subparamId + ")");
}
                if (decodeSuccess) foundSubparamMask |= subparamIdBit;
}
if ((foundSubparamMask & (1 << SUBPARAM_MESSAGE_IDENTIFIER)) == 0) {
throw new CodingException("missing MESSAGE_IDENTIFIER subparam");







