/*Support for KSC5601 on SIM.

Korean phones write to the ADN record of the SIM in a non-standard way.
When UCS2 is not used, the alphaTag will be written in the KSC5601
encoding. This contribution adds support for reading that format when
a Korean SIM card is used.

Change-Id:I81a4a6949359b4d23a937ac2d813bafed2b85ff6*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/AdnRecord.java b/telephony/java/com/android/internal/telephony/AdnRecord.java
//Synthetic comment -- index 1bf2d3c..a01b00d 100644

//Synthetic comment -- @@ -283,7 +283,7 @@
private void
parseRecord(byte[] record) {
try {
            alphaTag = IccUtils.adnStringFieldToStringKsc5601Support(
record, 0, record.length - FOOTER_SIZE_BYTES);

int footerOffset = record.length - FOOTER_SIZE_BYTES;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccUtils.java b/telephony/java/com/android/internal/telephony/IccUtils.java
//Synthetic comment -- index 1034bda..3e309ae 100644

//Synthetic comment -- @@ -150,6 +150,48 @@
*/
public static String
adnStringFieldToString(byte[] data, int offset, int length) {
        String s = adnStringFieldToStringUcs2Helper(data, offset, length);
        if(s == null){
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

        if(s == null){
            if (SimRegionCache.getRegion() == SimRegionCache.MCC_KOREAN) {
                try {
                    int len = offset;
                    byte stop = (byte)0xFF;
                    while(len < length && data[len] != stop){
                        len++;
                    }
                    return new String(data, offset, len, "KSC5601");
                } catch (UnsupportedEncodingException e) {
                    Log.e(LOG_TAG, "implausible UnsupportedEncodingException",
                            e);
                }
            }

            return adnStringFieldToStringGsm8BitHelper(data, offset, length);
        }
        return s;
    }

    private static String
    adnStringFieldToStringUcs2Helper(byte[] data, int offset, int length){
if (length >= 1) {
if (data[offset] == (byte) 0x80) {
int ucslen = (length - 1) / 2;
//Synthetic comment -- @@ -225,6 +267,11 @@
return ret.toString();
}

        return null;
    }

    private static String
    adnStringFieldToStringGsm8BitHelper(byte[] data, int offset, int length){
return GsmAlphabet.gsm8BitUnpackedToString(data, offset, length);
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SimRegionCache.java b/telephony/java/com/android/internal/telephony/SimRegionCache.java
new file mode 100644
//Synthetic comment -- index 0000000..2cf6d25

//Synthetic comment -- @@ -0,0 +1,51 @@
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








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 12c6b88..c8a4cbf 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.SimRegionCache;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsMessageBase.TextEncodingDetails;
//Synthetic comment -- @@ -48,6 +49,12 @@
public class SmsMessage extends SmsMessageBase{
static final String LOG_TAG = "GSM";

    /**
     * Used with the ENCODING_ constants from {@link android.telephony.SmsMessage}
     * Not a part of the public API, therefore not in order with those constants.
     */
    private static final int ENCODING_KSC5601 = 4000;

private MessageClass messageClass;

/**
//Synthetic comment -- @@ -781,6 +788,28 @@
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
//Synthetic comment -- @@ -1106,6 +1135,10 @@
} else {
Log.w(LOG_TAG, "3 - Unsupported SMS data coding scheme "
+ (dataCodingScheme & 0xff));
            if (SimRegionCache.getRegion() == SimRegionCache.MCC_KOREAN) {
                Log.w(LOG_TAG, "Korean SIM, using KSC5601 for decoding.");
                encodingType = ENCODING_KSC5601;
            }
}

// set both the user data and the user data header.
//Synthetic comment -- @@ -1127,6 +1160,10 @@
case ENCODING_16BIT:
messageBody = p.getUserDataUCS2(count);
break;

        case ENCODING_KSC5601:
            messageBody = p.getUserDataKSC5601(count);
            break;
}

if (Config.LOGV) Log.v(LOG_TAG, "SMS message body (raw): '" + messageBody + "'");








//Synthetic comment -- diff --git a/tests/CoreTests/com/android/internal/telephony/AdnRecordTest.java b/tests/CoreTests/com/android/internal/telephony/AdnRecordTest.java
//Synthetic comment -- index 8a4a285..5511c09c 100644

//Synthetic comment -- @@ -170,6 +170,18 @@
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








