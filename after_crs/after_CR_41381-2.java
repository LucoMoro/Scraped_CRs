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
        case UserData.ENCODING_SHIFT_JIS:
            userData.payloadStr = decodeShiftJis(userData.payload, offset, userData.numFields);
            break;
default:
throw new CodingException("unsupported user data encoding ("
+ userData.msgEncoding + ")");
//Synthetic comment -- @@ -1935,4 +1938,21 @@
}
return null;
}
    /**
     * Converts the byte[] to a string using SHIFT_JIS
     *
     * @param data payload of userData
     * @param offset  the starting offset in data
     * @param numFields length of data
     * @return String  Converted character string
     */
    private static String decodeShiftJis(byte[] data, int offset, int numFields)
        throws CodingException
    {
        try {
            return new String(data, 0, data.length, "SJIS");
        } catch (java.io.UnsupportedEncodingException ex) {
            throw new CodingException("SJIS decode failed: " + ex);
        }
    }
}







