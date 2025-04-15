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
/**Add to check encoding exception*/
import java.io.UnsupportedEncodingException;

/**
* An object to encode and decode CDMA SMS bearer data.
//Synthetic comment -- @@ -1106,6 +1108,12 @@
case UserData.ENCODING_LATIN:
userData.payloadStr = decodeLatin(userData.payload, offset, userData.numFields);
break;
        /** Support GSM-Data-Coding-Scheme (See Section 4 of 3GPP TS 23.038) */
        case UserData.ENCODING_GSM_DCS:
        // Support GSM-Data-Coding-Scheme (See Section 4 of 3GPP TS 23.038)
            userData.payloadStr=decodeGsmDCS(userData.payload, offset, userData.numFields,
                 userData.msgType,hasUserDataHeader,userData.userDataHeader);
            break;
default:
throw new CodingException("unsupported user data encoding ("
+ userData.msgEncoding + ")");
//Synthetic comment -- @@ -1935,4 +1943,124 @@
}
return null;
}
     /**
     * User data encoding types.
     * (See 3GPP2 C.R1001-E, v1.0, table 9.1-1)
     * Add GSM-Data-Coding-Scheme encoding type
     * Converts the byte[] to a string using GSM-Data-Coding-Scheme
     *
     * @param data payload of userData
     * @param offset  the starting offset in data
     * @param datalen length of data
     * @param dataCodingScheme encoding type of data
     * @param hasUserDataHeader check if user data has a Header
     * @param userDataHeader data of userDataHeader
     * @return String  Converted character string
     */
    private static String decodeGsmDCS(byte[] data,int offset, int datalen,
    int dataCodingScheme, boolean hasUserDataHeader,SmsHeader userDataHeader)
    throws CodingException
    {
        boolean hasMessageClass = false;
        boolean userDataCompressed = false;
        boolean automaticDeletion = false;
        int encodingType = SmsConstants.ENCODING_UNKNOWN;
        String ret =null;
        // Look up the data encoding scheme
        if ((dataCodingScheme & 0x80) == 0) {
           // Bits 7..4 == 0xxx
            automaticDeletion = (0 != (dataCodingScheme & 0x40));
            userDataCompressed = (0 != (dataCodingScheme & 0x20));
            hasMessageClass = (0 != (dataCodingScheme & 0x10));
            if (userDataCompressed) {
                Log.w(LOG_TAG, "4 - Unsupported SMS data coding scheme "
                            + "(compression) " + (dataCodingScheme & 0xff));
            } else {
                switch ((dataCodingScheme >> 2) & 0x3) {
                    case 0: // GSM 7 bit default alphabet
                        encodingType = SmsConstants.ENCODING_7BIT;
                        break;
                    case 2: // UCS 2 (16bit)
                        encodingType = SmsConstants.ENCODING_16BIT;
                        break;
                    case 1: // 8 bit data
                    case 3: // reserved
                        Log.w(LOG_TAG, "1 - Unsupported SMS data coding scheme "
                                    + (dataCodingScheme & 0xff));
                        encodingType = SmsConstants.ENCODING_8BIT;
                        break;
                }
            }
        } else if ((dataCodingScheme & 0xf0) == 0xf0) {
            automaticDeletion = false;
            hasMessageClass = true;
            userDataCompressed = false;
            if (0 == (dataCodingScheme & 0x04)) {
                // GSM 7 bit default alphabet
                encodingType = SmsConstants.ENCODING_7BIT;
            } else {
                // 8 bit data
                encodingType = SmsConstants.ENCODING_8BIT;
            }
        } else if ((dataCodingScheme & 0xF0) == 0xC0
                    || (dataCodingScheme & 0xF0) == 0xD0
                    || (dataCodingScheme & 0xF0) == 0xE0) {
            // 3GPP TS 23.038 V7.0.0 (2006-03) section 4
            // 0xC0 == 7 bit, don't store
            // 0xD0 == 7 bit, store
            // 0xE0 == UCS-2, store
            if ((dataCodingScheme & 0xF0) == 0xE0) {
                encodingType = SmsConstants.ENCODING_16BIT;
            } else {
                encodingType = SmsConstants.ENCODING_7BIT;
            }
           userDataCompressed = false;
            // bit 0x04 reserved
            // VM - If TP-UDH is present, these values will be overwritten
        } else {
            Log.w(LOG_TAG, "3 - Unsupported SMS data coding scheme "
                        + (dataCodingScheme & 0xff));
        }
        int headerSeptets=0;
        int mUserDataSeptetPadding=0;
        //offset is start-point of user data(except user data header)
        int count=datalen-offset; 
        if (count < 0) {
            count = 0;
        }
        if (hasUserDataHeader) {
            Log.d(LOG_TAG,"GSM_DCS has UserData header");
            int headerBits = offset * 8;
            headerSeptets = headerBits / 7;
            headerSeptets += (headerBits % 7) > 0 ? 1 : 0;
            mUserDataSeptetPadding = (headerSeptets * 7) - headerBits;
            Log.d(LOG_TAG,"GSM_DCS has userData Header offset: " + offset);
            Log.d(LOG_TAG,"GSM_DCS has mUserDataSeptetPadding: " + mUserDataSeptetPadding);
            Log.d(LOG_TAG,"GSM_DCS has headerSeptets: " + headerSeptets);
        }
        switch (encodingType) {
            case SmsConstants.ENCODING_UNKNOWN:
            case SmsConstants.ENCODING_8BIT:
                ret = null;
                break;
            case SmsConstants.ENCODING_7BIT:
                count=datalen - headerSeptets;
                ret = GsmAlphabet.gsm7BitPackedToString(data, offset, (count<0 ? 0: count),
                        mUserDataSeptetPadding,
                        hasUserDataHeader ? userDataHeader.languageTable : 0,
                        hasUserDataHeader ? userDataHeader.languageShiftTable : 0);
                Log.i(LOG_TAG,"GSM_DCS 7bit userData Length: " + count);
                break;
            case SmsConstants.ENCODING_16BIT:
                try {
                    Log.d(LOG_TAG,"GSM_DCS 16bit bin"+count);
                    ret = new String(data, offset, count, "utf-16");
                } catch (UnsupportedEncodingException ex) {
                    ret = "";
                    Log.e(LOG_TAG, "implausible UnsupportedEncodingException", ex);
                }
                break;
        }
        return ret;
    }
}







