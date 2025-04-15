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

private MessageClass messageClass;

//Synthetic comment -- @@ -789,17 +790,17 @@
}

/**
         * Interprets the user data payload as KSC5601 characters, and
* decodes them into a String
*
* @param byteCount the number of bytes in the user data payload
* @return a String with the decoded characters
*/
        String getUserDataKSC5601(int byteCount) {
String ret;

try {
                ret = new String(pdu, cur, byteCount, "KSC5601");
} catch (UnsupportedEncodingException ex) {
// Should return same as ENCODING_UNKNOWN on error.
ret = null;
//Synthetic comment -- @@ -1088,9 +1089,13 @@

case 1: // 8 bit data
case 3: // reserved
                    Log.w(LOG_TAG, "1 - Unsupported SMS data coding scheme "
                            + (dataCodingScheme & 0xff));
                    encodingType = ENCODING_8BIT;
break;
}
}
//Synthetic comment -- @@ -1136,13 +1141,36 @@
Log.w(LOG_TAG, "MWI for fax, email, or other "
+ (dataCodingScheme & 0xff));
}
} else {
Log.w(LOG_TAG, "3 - Unsupported SMS data coding scheme "
+ (dataCodingScheme & 0xff));
            if (SimRegionCache.getRegion() == SimRegionCache.MCC_KOREAN) {
                Log.w(LOG_TAG, "Korean SIM, using KSC5601 for decoding.");
                encodingType = ENCODING_KSC5601;
            }
}

// set both the user data and the user data header.
//Synthetic comment -- @@ -1166,8 +1194,13 @@
break;

case ENCODING_KSC5601:
            messageBody = p.getUserDataKSC5601(count);
break;
}

if (Config.LOGV) Log.v(LOG_TAG, "SMS message body (raw): '" + messageBody + "'");







