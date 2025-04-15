/*Improved handling of Korean specific encoding of SIM

The earlier fix assumed KSC5601 for all reserved values of
DCS, this is however wrong. The field should be interpreted
as follows:

10xx0100  ASCII 7bit (normally reserved)
10xx0101  KSC5601 (normally reserved)
00001100  KSC5601 (normally reserved)
00xx01xx  KSC5601 (normally 8bit)

Change-Id:I1c898b84cadcb2cf9310a056687174535b1bd702*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index f4c5e6c..bc2823c 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
* Not a part of the public API, therefore not in order with those constants.
*/
private static final int ENCODING_KSC5601 = 4000;
    private static final int ENCODING_ASCII_7BIT = 4001;

private MessageClass messageClass;

//Synthetic comment -- @@ -789,17 +790,17 @@
}

/**
         * Interprets the user data payload with the provided charset and
* decodes them into a String
*
* @param byteCount the number of bytes in the user data payload
* @return a String with the decoded characters
*/
        String getUserDataWithCharset(int byteCount, String charset) {
String ret;

try {
                ret = new String(pdu, cur, byteCount, charset);
} catch (UnsupportedEncodingException ex) {
// Should return same as ENCODING_UNKNOWN on error.
ret = null;
//Synthetic comment -- @@ -1088,9 +1089,13 @@

case 1: // 8 bit data
case 3: // reserved
                    if (SimRegionCache.getRegion() == SimRegionCache.MCC_KOREAN) {
                        encodingType = ENCODING_KSC5601;
                    } else {
                        Log.w(LOG_TAG, "1 - Unsupported SMS data coding scheme "
                                + (dataCodingScheme & 0xff));
                        encodingType = ENCODING_8BIT;
                    }
break;
}
}
//Synthetic comment -- @@ -1136,13 +1141,36 @@
Log.w(LOG_TAG, "MWI for fax, email, or other "
+ (dataCodingScheme & 0xff));
}
        } else if ((dataCodingScheme & 0xC0) == 0x80 &&
                SimRegionCache.getRegion() == SimRegionCache.MCC_KOREAN) {
            // Bits 7..4 == 10xx && MCC Korea
            // Enter Korea specific decoding.
            automaticDeletion = false;
            userDataCompressed = (0 != (dataCodingScheme & 0x20));
            hasMessageClass = (0 != (dataCodingScheme & 0x10));

            if (userDataCompressed) {
                Log.w(LOG_TAG, "4 - Unsupported SMS data coding scheme "
                        + "(compression) " + (dataCodingScheme & 0xff));
            } else {
                switch ((dataCodingScheme >> 2) & 0x3) {
                case 0: // ASCII 7bit
                    encodingType = ENCODING_ASCII_7BIT;
                    break;

                case 1: // KSC5601
                    encodingType = ENCODING_KSC5601;
                    break;

                default:
                    Log.w(LOG_TAG, "1 - Unsupported SMS data coding scheme "
                            + (dataCodingScheme & 0xff));
                    break;
                }
            }
} else {
Log.w(LOG_TAG, "3 - Unsupported SMS data coding scheme "
+ (dataCodingScheme & 0xff));
}

// set both the user data and the user data header.
//Synthetic comment -- @@ -1166,8 +1194,13 @@
break;

case ENCODING_KSC5601:
            messageBody = p.getUserDataWithCharset(count, "KSC5601");
break;

        case ENCODING_ASCII_7BIT:
            messageBody = p.getUserDataWithCharset(count, "US-ASCII");
            break;

}

if (Config.LOGV) Log.v(LOG_TAG, "SMS message body (raw): '" + messageBody + "'");







