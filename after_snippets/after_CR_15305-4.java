
//<Beginning of snippet n. 0>


private void
parseRecord(byte[] record) {
try {
            alphaTag = IccUtils.adnStringFieldToStringKsc5601Support(
record, 0, record.length - FOOTER_SIZE_BYTES);

int footerOffset = record.length - FOOTER_SIZE_BYTES;

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


*/
public static String
adnStringFieldToString(byte[] data, int offset, int length) {
        String s = adnStringFieldToStringUcs2Helper(data, offset, length);
        if (s == null) {
            s = adnStringFieldToStringGsm8BitHelper(data, offset, length);
        }
        return s;
    }

    /**
     * Almost identical to the method {@link #adnStringFieldToString}.
     *
     * Exception:
     * If the SIM is Korean (MCC equals "450"), KSC5601 encoding will be
     * assumed (instead of GSM8Bit). This could lead to unintended consequences,
     * if the ADN alphaTag was saved with GSM8Bit. This is considered an
     * acceptable risk.
     */
    public static String
    adnStringFieldToStringKsc5601Support(byte[] data, int offset, int length) {
        String s = adnStringFieldToStringUcs2Helper(data, offset, length);

        if (s == null) {
            if (SimRegionCache.getRegion() == SimRegionCache.MCC_KOREAN) {
                try {
                    int len = offset;
                    byte stop = (byte)0xFF;
                    while (len < length && data[len] != stop) {
                        len++;
                    }
                    return new String(data, offset, len, "KSC5601");
                } catch (UnsupportedEncodingException e) {
                    Log.e(LOG_TAG, "implausible UnsupportedEncodingException", e);
                }
            }

            return adnStringFieldToStringGsm8BitHelper(data, offset, length);
        }
        return s;
    }

    private static String
    adnStringFieldToStringUcs2Helper(byte[] data, int offset, int length) {
if (length >= 1) {
if (data[offset] == (byte) 0x80) {
int ucslen = (length - 1) / 2;
return ret.toString();
}

        return null;
    }

    private static String
    adnStringFieldToStringGsm8BitHelper(byte[] data, int offset, int length) {
return GsmAlphabet.gsm8BitUnpackedToString(data, offset, length);
}


//<End of snippet n. 1>










//<Beginning of snippet n. 2>

new file mode 100644

/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony;

import android.os.SystemProperties;

public class SimRegionCache {
    public static final int MCC_UNSET  = Integer.MIN_VALUE;
    public static final int MCC_KOREAN = 450;

    private static int regionFromMcc = MCC_UNSET;

    /**
     * Returns the region as read from the MCC of the SIM card.
     * If the property {@link TelephonyProperties#
     * PROPERTY_ICC_OPERATOR_NUMERIC}
     * returns null or an empty String, the value is {@link #MCC_UNSET}
     *
     * @return the cached region, if set.
     */
    public static int getRegion() {
        if (regionFromMcc == MCC_UNSET) {
            String plmn = SystemProperties.get(
                    TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC,
                    null);

            if (plmn != null && plmn.length() >= 3) {
                try {
                    regionFromMcc = Integer.parseInt(plmn.substring(0, 3));
                } catch(Exception e) {
                    // Nothing that can be done here.
                }
            }
        }
        return regionFromMcc;
    }
}

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.SimRegionCache;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsMessageBase.TextEncodingDetails;
public class SmsMessage extends SmsMessageBase{
static final String LOG_TAG = "GSM";

    /**
     * Used with the ENCODING_ constants from {@link android.telephony.SmsMessage}
     * Not a part of the public API, therefore not in order with those constants.
     */
    private static final int ENCODING_KSC5601 = 4000;

private MessageClass messageClass;

/**
return ret;
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
                Log.e(LOG_TAG, "implausible UnsupportedEncodingException", ex);
            }

            cur += byteCount;
            return ret;
        }

boolean moreDataPresent() {
return (pdu.length > cur);
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
case ENCODING_16BIT:
messageBody = p.getUserDataUCS2(count);
break;

        case ENCODING_KSC5601:
            messageBody = p.getUserDataKSC5601(count);
            break;
}

if (Config.LOGV) Log.v(LOG_TAG, "SMS message body (raw): '" + messageBody + "'");

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


assertEquals("Adgjm", adn.getAlphaTag());
assertEquals("+18885551212,12345678", adn.getNumber());
assertFalse(adn.isEmpty());

        //
        // Test that a ADN record with KSC5601 will get converted correctly
        // This test will only be run when using a Korean SIM
        //
        if (SimRegionCache.getRegion() == SimRegionCache.MCC_KOREAN) {
            adn = new AdnRecord(IccUtils.hexStringToBytes(
                  "3030312C20C8AB41B1E6FFFFFFFFFFFF07811010325476F8FFFFFFFFFFFF"));
            assertEquals("001, \uD64DA\uAE38", adn.getAlphaTag());
            assertEquals("01012345678", adn.getNumber());
            assertFalse(adn.isEmpty());
        }
}
}


//<End of snippet n. 4>










//<Beginning of snippet n. 5>


sms = SmsMessage.createFromEfRecord(1, data);
assertNotNull(sms.getMessageBody());
}

    @MediumTest
    public void testEfRecordKorean() throws Exception {
        if (SimRegionCache.getRegion() == SimRegionCache.MCC_KOREAN) {
            SmsMessage sms;

            String s = "01089128010099010259040ba11000000000f00095013091900563008c4142"
                     + "434445b0a1b3aab4d9b6f3b8b631323334354142434445b0a1b3aab4d9b6f3"
                     + "b8b631323334354142434445b0a1b3aab4d9b6f3b8b6313233343541424344"
                     + "45b0a1b3aab4d9b6f3b8b63132333435000000000000000000000000000000"
                     + "00000000000000000000000000000000000000000000000000000000000000"
                     + "0000000000000000000000000000ffffffffffffff";


           byte[] data = IccUtils.hexStringToBytes(s);

           sms = SmsMessage.createFromEfRecord(1, data);
           assertNotNull(sms.getMessageBody());
           assertTrue(sms.getMessageBody().startsWith("ABCDE\uAC00\uB098\uB2E4\uB77C\uB9C812345ABCDE"));
        }
    }
}

//<End of snippet n. 5>










//<Beginning of snippet n. 6>


data = IccUtils.hexStringToBytes("820505302D82d32d31");
// Example from 3GPP TS 11.11 V18.1.3.0 annex B
assertEquals("-\u0532\u0583-1", IccUtils.adnStringFieldToString(data, 0, data.length));

        /*
         * adnStringFieldToStringKsc5601Support()
         * Tests equal the ones above, and will only be run if the SIM is NOT korean.
         */

        if (SimRegionCache.getRegion() != SimRegionCache.MCC_KOREAN) {
            data = IccUtils.hexStringToBytes("00566f696365204d61696c07918150367742f3ffffffffffff");
            // Again, skip prepended 0
            // (this is an EF[ADN] record)
            assertEquals("Voice Mail", IccUtils.adnStringFieldToStringKsc5601Support(data, 1, data.length - 15));

            data = IccUtils.hexStringToBytes("809673539A5764002F004DFFFFFFFFFF");
            // (this is from an EF[ADN] record)
            assertEquals("\u9673\u539A\u5764/M", IccUtils.adnStringFieldToStringKsc5601Support(data, 0, data.length));

            data = IccUtils.hexStringToBytes("810A01566fec6365204de0696cFFFFFF");
            // (this is made up to test since I don't have a real one)
            assertEquals("Vo\u00ECce M\u00E0il", IccUtils.adnStringFieldToStringKsc5601Support(data, 0, data.length));

            data = IccUtils.hexStringToBytes("820505302D82d32d31");
            // Example from 3GPP TS 11.11 V18.1.3.0 annex B
            assertEquals("-\u0532\u0583-1", IccUtils.adnStringFieldToStringKsc5601Support(data, 0, data.length));
        }
}

}

//<End of snippet n. 6>








