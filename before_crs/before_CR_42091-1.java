/*Telephony: Support ShiftJIS encoding and Skip unsupported bearer data subparam

Support Shift-JIS CHARi encoding type per 3GPP2 C.R1001 section 9.1.

3GPP2 C.S0015-B, v2.0, table 4.5-1 shows bearer data subparam ID
above 0x17 is reserved. Fix to skip reserved bearer data subparams
instead of throwing CodingException.

Change-Id:Ib83e7e72189a267421ba5fd77695b8f684ef00e2*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/sms/BearerData.java b/src/java/com/android/internal/telephony/cdma/sms/BearerData.java
//Synthetic comment -- index 7e7347f..158dd8a 100755

//Synthetic comment -- @@ -72,6 +72,9 @@
//private final static byte SUBPARAM_ENHANCED_VMN                     = 0x16;
//private final static byte SUBPARAM_ENHANCED_VMN_ACK                 = 0x17;

/**
* Supported message types for CDMA SMS messages
* (See 3GPP2 C.S0015-B, v2.0, table 4.5.1-1)
//Synthetic comment -- @@ -945,6 +948,27 @@
return decodeSuccess;
}

private static boolean decodeUserData(BearerData bData, BitwiseInputStream inStream)
throws BitwiseInputStream.AccessException
{
//Synthetic comment -- @@ -1055,6 +1079,16 @@
}
}

private static void decodeUserDataPayload(UserData userData, boolean hasUserDataHeader)
throws CodingException
{
//Synthetic comment -- @@ -1106,6 +1140,9 @@
case UserData.ENCODING_LATIN:
userData.payloadStr = decodeLatin(userData.payload, offset, userData.numFields);
break;
default:
throw new CodingException("unsupported user data encoding ("
+ userData.msgEncoding + ")");
//Synthetic comment -- @@ -1839,7 +1876,15 @@
while (inStream.available() > 0) {
int subparamId = inStream.read(8);
int subparamIdBit = 1 << subparamId;
                if ((foundSubparamMask & subparamIdBit) != 0) {
throw new CodingException("illegal duplicate subparameter (" +
subparamId + ")");
}
//Synthetic comment -- @@ -1903,10 +1948,13 @@
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







