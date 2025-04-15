/*Add support for national language tables in SMS alphabets

Implementation of the national languages feature for SMS.
3GPP 23.040 9.2.3.24.15 and 9.2.3.24.16 specifies how national
languages are implemented in SMS. 3GPP 23.038 specifies the
actual language tables.

The change also contains some tests for the national characters
support.*/




//Synthetic comment -- diff --git a/telephony/java/android/telephony/SmsMessage.java b/telephony/java/android/telephony/SmsMessage.java
//Synthetic comment -- index a284ea5..e03a1efb 100644

//Synthetic comment -- @@ -19,7 +19,8 @@
import android.os.Parcel;
import android.util.Log;

import com.android.internal.telephony.gsm.GsmAlphabet;
import com.android.internal.telephony.gsm.GsmAlphabet.GsmLanguage;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
//Synthetic comment -- @@ -254,23 +255,43 @@
*         space chars.  If false, and if the messageBody contains
*         non-7-bit encodable characters, length is calculated
*         using a 16-bit encoding.
     * @return an int[5] with int[0] being the number of SMS's
*         required, int[1] the number of code units used, and
*         int[2] is the number of code units remaining until the
*         next message. int[3] is an indicator of the encoding
*         code unit size (see the ENCODING_* definitions in this
     *         class). int[4] is the number of code units used in
     *         the last message.
*/
public static int[] calculateLength(CharSequence msgBody, boolean use7bitOnly) {
int activePhone = TelephonyManager.getDefault().getPhoneType();

TextEncodingDetails ted = (PHONE_TYPE_CDMA == activePhone) ?
com.android.internal.telephony.cdma.SmsMessage.calculateLength(msgBody, use7bitOnly) :
com.android.internal.telephony.gsm.SmsMessage.calculateLength(msgBody, use7bitOnly);

        int ret[] = new int[5];
ret[0] = ted.msgCount;
ret[1] = ted.codeUnitCount;
ret[2] = ted.codeUnitsRemaining;
ret[3] = ted.codeUnitSize;

        switch (ted.codeUnitSize) {
            case ENCODING_7BIT:
                ret[4] = (MAX_USER_DATA_BYTES - ted.headerLength) * 8 / 7;
                break;

            case ENCODING_16BIT:
                ret[4] = (MAX_USER_DATA_BYTES - ted.headerLength) / 2;
                break;

            case ENCODING_8BIT:
            default:
                ret[4] = MAX_USER_DATA_BYTES - ted.headerLength;
                break;
        }
        ret[4] -= ted.codeUnitsRemaining;

return ret;
}

//Synthetic comment -- @@ -286,21 +307,14 @@
*/
public static ArrayList<String> fragmentText(String text) {
int activePhone = TelephonyManager.getDefault().getPhoneType();

TextEncodingDetails ted = (PHONE_TYPE_CDMA == activePhone) ?
com.android.internal.telephony.cdma.SmsMessage.calculateLength(text, false) :
com.android.internal.telephony.gsm.SmsMessage.calculateLength(text, false);

        int limit = MAX_USER_DATA_BYTES - ted.headerLength;
        if (ted.codeUnitSize == ENCODING_7BIT) {
            limit = limit * 8 / 7;
}

int pos = 0;  // Index in code units.
//Synthetic comment -- @@ -314,7 +328,9 @@
nextPos = pos + Math.min(limit, textLen - pos);
} else {
// For multi-segment messages, CDMA 7bit equals GSM 7bit encoding (EMS mode).
                    GsmLanguage extensionLanguage = GsmAlphabet.getExtensionLanguage(text);
                    nextPos = GsmAlphabet.getAlphabet(GsmLanguage.DEFAULT, extensionLanguage)
                            .findGsmSeptetLimitIndex(text, pos, limit);
}
} else {  // Assume unicode.
nextPos = pos + Math.min(limit / 2, textLen - pos);
//Synthetic comment -- @@ -380,14 +396,14 @@
*/
public static SubmitPdu getSubmitPdu(String scAddress,
String destinationAddress, String message,
            boolean statusReportRequested, SmsHeader header) {
SubmitPduBase spb;
int activePhone = TelephonyManager.getDefault().getPhoneType();

if (PHONE_TYPE_CDMA == activePhone) {
spb = com.android.internal.telephony.cdma.SmsMessage.getSubmitPdu(scAddress,
destinationAddress, message, statusReportRequested,
                    header);
} else {
spb = com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(scAddress,
destinationAddress, message, statusReportRequested, header);








//Synthetic comment -- diff --git a/telephony/java/android/telephony/gsm/SmsMessage.java b/telephony/java/android/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 37ef912..59dee8b 100644

//Synthetic comment -- @@ -375,7 +375,8 @@
SmsHeader.fromByteArray(header));
} else {
spb = com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(scAddress,
                    destinationAddress, message, statusReportRequested, SmsHeader
                            .fromByteArray(header));
}

return new SubmitPdu(spb);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SmsHeader.java b/telephony/java/com/android/internal/telephony/SmsHeader.java
//Synthetic comment -- index 7872eec..4ad8a22 100644

//Synthetic comment -- @@ -18,17 +18,21 @@

import android.telephony.SmsMessage;

import com.android.internal.telephony.gsm.GsmAlphabet;
import com.android.internal.telephony.gsm.GsmAlphabet.GsmLanguage;
import com.android.internal.util.HexDump;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.util.ArrayList;
import android.util.Log;

/**
* SMS user data header, as specified in TS 23.040 9.2.3.24.
*/
public class SmsHeader {
    static final String LOG_TAG = "GSM";

// TODO(cleanup): this datastructure is generally referred to as
// the 'user data header' or UDH, and so the class name should
//Synthetic comment -- @@ -66,6 +70,8 @@
public static final int ELT_ID_HYPERLINK_FORMAT_ELEMENT           = 0x21;
public static final int ELT_ID_REPLY_ADDRESS_ELEMENT              = 0x22;
public static final int ELT_ID_ENHANCED_VOICE_MAIL_INFORMATION    = 0x23;
    public static final int ELT_ID_NATIONAL_LANGUAGE_SINGLE_SHIFT     = 0x24;
    public static final int ELT_ID_NATIONAL_LANGUAGE_LOCKING_SHIFT    = 0x25;

public static final int PORT_WAP_PUSH = 2948;
public static final int PORT_WAP_WSP  = 9200;
//Synthetic comment -- @@ -95,6 +101,8 @@
public PortAddrs portAddrs;
public ConcatRef concatRef;
public ArrayList<MiscElt> miscEltList = new ArrayList<MiscElt>();
    public GsmLanguage singleShiftLanguage = GsmLanguage.DEFAULT;
    public GsmLanguage lockingShiftLanguage = GsmLanguage.DEFAULT;

public SmsHeader() {}

//Synthetic comment -- @@ -157,6 +165,12 @@
portAddrs.areEightBits = false;
smsHeader.portAddrs = portAddrs;
break;
            case ELT_ID_NATIONAL_LANGUAGE_SINGLE_SHIFT:
                smsHeader.singleShiftLanguage = GsmLanguage.toGsmLanguage(inStream.read());
                break;
            case ELT_ID_NATIONAL_LANGUAGE_LOCKING_SHIFT:
                smsHeader.lockingShiftLanguage = GsmLanguage.toGsmLanguage(inStream.read());
                break;
default:
MiscElt miscElt = new MiscElt();
miscElt.id = id;
//Synthetic comment -- @@ -174,9 +188,13 @@
* @return Byte array representing the SmsHeader
*/
public static byte[] toByteArray(SmsHeader smsHeader) {
        if (smsHeader == null ||
            (smsHeader.portAddrs == null &&
            smsHeader.concatRef == null &&
            smsHeader.miscEltList.size() == 0 &&
            smsHeader.singleShiftLanguage == GsmLanguage.DEFAULT &&
            smsHeader.lockingShiftLanguage == GsmLanguage.DEFAULT)) {

return null;
}

//Synthetic comment -- @@ -196,6 +214,7 @@
outStream.write(concatRef.msgCount);
outStream.write(concatRef.seqNumber);
}

PortAddrs portAddrs = smsHeader.portAddrs;
if (portAddrs != null) {
if (portAddrs.areEightBits) {
//Synthetic comment -- @@ -212,14 +231,61 @@
outStream.write(portAddrs.origPort & 0x00FF);
}
}

        if (smsHeader.singleShiftLanguage != GsmLanguage.DEFAULT) {
            outStream.write(ELT_ID_NATIONAL_LANGUAGE_SINGLE_SHIFT);
            outStream.write(1);
            outStream.write(smsHeader.singleShiftLanguage.getLanguageCode());
        }

        if (smsHeader.lockingShiftLanguage != GsmLanguage.DEFAULT) {
            outStream.write(ELT_ID_NATIONAL_LANGUAGE_LOCKING_SHIFT);
            outStream.write(1);
            outStream.write(smsHeader.lockingShiftLanguage.getLanguageCode());
        }

for (MiscElt miscElt : smsHeader.miscEltList) {
outStream.write(miscElt.id);
outStream.write(miscElt.data.length);
outStream.write(miscElt.data, 0, miscElt.data.length);
}

return outStream.toByteArray();
}

    /**
     * Returns the length of the header, _including_ the UDHL byte
     * @return the length of the header, _including_ the UDHL byte
     */
    public static int getLengthWithUDHL(SmsHeader smsHeader) {
        int length = 0;

        if (smsHeader != null) {
            if (smsHeader.concatRef != null) {
                length += smsHeader.concatRef.isEightBits ? 5 : 6;
            }

            if (smsHeader.portAddrs != null) {
                length += smsHeader.portAddrs.areEightBits ? 4 : 6;
            }

            if (smsHeader.singleShiftLanguage != GsmLanguage.DEFAULT) {
                length += 3;
            }

            if (smsHeader.lockingShiftLanguage != GsmLanguage.DEFAULT) {
                length += 3;
            }

            for (MiscElt miscElt : smsHeader.miscEltList) {
                length += 2 + miscElt.data.length;
            }
        }

        // Add one byte for UDHL if there actually is a header
        return length > 0 ? length + 1 : length;
    }

@Override
public String toString() {
StringBuilder builder = new StringBuilder();
//Synthetic comment -- @@ -243,6 +309,8 @@
builder.append(", areEightBits=" + portAddrs.areEightBits);
builder.append(" }");
}
        builder.append(", SingleShift language: " + singleShiftLanguage.getLanguageCode());
        builder.append(", LockingShift language: " + lockingShiftLanguage.getLanguageCode());
for (MiscElt miscElt : miscEltList) {
builder.append(", MiscElt ");
builder.append("{ id=" + miscElt.id);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SmsMessageBase.java b/telephony/java/com/android/internal/telephony/SmsMessageBase.java
//Synthetic comment -- index af6c5f8..2fab312 100644

//Synthetic comment -- @@ -18,6 +18,9 @@

import android.util.Log;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.gsm.GsmAlphabet;
import com.android.internal.telephony.gsm.GsmAlphabet.GsmLanguage;

import java.util.Arrays;

import static android.telephony.SmsMessage.MessageClass;
//Synthetic comment -- @@ -118,6 +121,23 @@
*/
public int codeUnitSize;

        /**
         * The alphabet to use for encoding (i.e. locking shift)
         * com.android.internal.telephony.GsmAlphabet GSM_NATIONAL_LANGUAGE_*).
         */
        public GsmLanguage alphabet = GsmLanguage.DEFAULT;

        /**
         * The extension table to use for encoding (i.e. single shift)
         * com.android.internal.telephony.GsmAlphabet GSM_NATIONAL_LANGUAGE_*).
         */
        public GsmLanguage extensionTable = GsmLanguage.DEFAULT;

        /**
         * The length of the header needed to encode the text, including UDHL
         */
        public int headerLength = 0;

@Override
public String toString() {
return "TextEncodingDetails " +
//Synthetic comment -- @@ -125,6 +145,9 @@
", codeUnitCount=" + codeUnitCount +
", codeUnitsRemaining=" + codeUnitsRemaining +
", codeUnitSize=" + codeUnitSize +
                    ", alphabet=" + alphabet.getLanguageCode() +
                    ", extensionTable=" + extensionTable.getLanguageCode() +
                    ", headerLength=" + headerLength +
" }";
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmAlphabet.java b/telephony/java/com/android/internal/telephony/gsm/GsmAlphabet.java
new file mode 100644
//Synthetic comment -- index 0000000..7fbb6ab

//Synthetic comment -- @@ -0,0 +1,748 @@
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

package com.android.internal.telephony.gsm;

import com.android.internal.telephony.EncodeException;

import android.util.Log;
import android.util.Pair;
import android.util.SparseIntArray;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class implements the character set mapping between the GSM SMS 7-bit
 * alphabet specified in TS 23.038 6.2.1 and UTF-16 with regards to the national
 * language properties provided in the constructor
 *
 * {@hide}
 */
public class GsmAlphabet {

    private static final String LOG_TAG = "GSM";

    public static enum GsmLanguage {
        /*
         * Note that it is important that the languages appear in the same
         * order as they appear in 3GPP 23.038 to make ordinal match the
         * language code.
         * We do not want to use a constructor since we want to exploit
         * ordinal/length in code.
         */
        DEFAULT, TURKISH, SPANISH, PORTUGUESE, BENGALI, GUJARATI, HINDI,
        KANNADA, MALAYALAM, ORIYA, PUNJABI, TAMIL, TELUGU, URDU;

        /**
         * Factory method to Returns a GsmLanguage based on the languageCode
         * provided
         *
         * @return a GsmLanguage object based on the languageCode provided
         */
        public static GsmLanguage toGsmLanguage(int languageCode) {
            if (languageCode >= 0 && languageCode < values().length) {
                return values()[languageCode];
            } else {
                return null;
            }
        }

        public int getLanguageCode() {
            return ordinal();
        }
    }

    public static final byte GSM_EXTENDED_ESCAPE = 0x1B;

    // Conversion tables for all supported languages
    private static final Map<GsmLanguage, SparseIntArray> sCharToGsmTables =
        new HashMap<GsmLanguage, SparseIntArray>();

    private static final Map<GsmLanguage, SparseIntArray> sGsmToCharTables =
        new HashMap<GsmLanguage, SparseIntArray>();

    private static final Map<GsmLanguage, SparseIntArray> sCharToGsmExtendedTables =
        new HashMap<GsmLanguage, SparseIntArray>();

    private static final Map<GsmLanguage, SparseIntArray> sGsmExtendedToCharTables =
        new HashMap<GsmLanguage, SparseIntArray>();

    // A cache array to keep the alphabets which uses the default GSM alphabet
    // and an extension table
    private static final GsmAlphabet[] sExtendedDefaultAlphabets =
        new GsmAlphabet[GsmLanguage.values().length];

    // A cache map to keep the alphabets which uses a locking shift alphabet
    private static final int MAX_NBR_CACHED_ALPHABETS = 10;

    private static Map<Pair<GsmLanguage, GsmLanguage>, GsmAlphabet> sAlphabetCache =
        new HashMap<Pair<GsmLanguage, GsmLanguage>, GsmAlphabet>(MAX_NBR_CACHED_ALPHABETS);

    // The default gsm7bit alphabet
    private static final SparseIntArray sGsm7bitAlphabet;

    // We keep the data from getExtensionLanguage to speed up analysis
    private static List<GsmLanguage> sLastCandidateAlphabets;

    private static String sLastAnalyzedText = null;

    // Static initializer to setup static members
    static {
        // Get all available tables
        GsmAlphabetTables.init(sCharToGsmTables, sGsmToCharTables, sCharToGsmExtendedTables,
                sGsmExtendedToCharTables);

        // Setup the default alphabets for fast retreival
        for (GsmLanguage language : GsmLanguage.values()) {
            sExtendedDefaultAlphabets[language.getLanguageCode()] = new GsmAlphabet(
                    GsmLanguage.DEFAULT, language);
        }

        sGsm7bitAlphabet = sCharToGsmTables.get(GsmLanguage.DEFAULT);

        sLastCandidateAlphabets = getNewCandidateAlphabets();
    }

    private final int mGsmSpaceChar;

    private final SparseIntArray mCharToGsmTable;

    private final SparseIntArray mGsmToCharTable;

    private final SparseIntArray mCharToGsmExtendedTable;

    private final SparseIntArray mGsmExtendedToCharTable;

    /**
     * This escapes extended characters, and when present indicates that the
     * following character should be looked up in the "extended" table
     *
     * gsmToChar(GSM_EXTENDED_ESCAPE) returns 0xffff
     */

    private GsmAlphabet(GsmLanguage lockingshift, GsmLanguage singleshift) {
        mCharToGsmTable = sCharToGsmTables.get(lockingshift);
        mGsmToCharTable = sGsmToCharTables.get(lockingshift);
        mCharToGsmExtendedTable = sCharToGsmExtendedTables.get(singleshift);
        mGsmExtendedToCharTable = sGsmExtendedToCharTables.get(singleshift);
        mGsmSpaceChar = mCharToGsmTable.get(' ');
    }

    /**
     * Returns an alphabet based on the parameters provided
     *
     * @param alphabet The language used for locking shift
     * @param extension The language used for single shift
     * @return an NationalGsmAlphabet instance
     */
    public static synchronized GsmAlphabet getAlphabet(GsmLanguage alphabet,
            GsmLanguage extension) {
        if (alphabet == GsmLanguage.DEFAULT) {
            // This is a default alphabet with extension table
            return sExtendedDefaultAlphabets[extension.getLanguageCode()];
        } else {
            // This is a locking shift alphabet
            Pair<GsmLanguage, GsmLanguage> languagePair = new Pair<GsmLanguage, GsmLanguage>(
                    alphabet, extension);
            if (sAlphabetCache.containsKey(languagePair)) {
                // This is a cached alphabet, return it
                return sAlphabetCache.get(languagePair);
            } else {
                GsmAlphabet nationalAlphabet = new GsmAlphabet(alphabet, extension);
                sAlphabetCache.put(languagePair, nationalAlphabet);
                return nationalAlphabet;
            }
        }
    }

    /**
     * Calculates what gsm7bit single shift extension table needed to encode a
     * text, using default gsm7bit alphabet for locking shift
     *
     * @param text The text to scan
     * @return the calculated gsm7bit encoding language
     */
    public static synchronized GsmLanguage getExtensionLanguage(CharSequence text) {
        List<GsmLanguage> candidateAlphabets = null;
        CharSequence unanalyzedText = text;
        int unanalyzedTextLength = unanalyzedText.length();

        if (sLastAnalyzedText != null) {
            // This is an optimization that covers the case where a text is
            // growing, i.e. characters are added to the end of a previously
            // analyzed text. In this case we dont have to re-evaluate alphabets
            // that have already been ruled out as candidates for the existing
            // text

            int lastTextLength = sLastAnalyzedText.length();
            if (unanalyzedTextLength > lastTextLength
                    && text.subSequence(0, lastTextLength).equals(sLastAnalyzedText)) {
                // This is the last analyzed text with an addition at the end,
                // we only
                // have to analyze the added text. We can re-use the remaining
                // alphabet
                // candidates from the last run
                unanalyzedText = text.subSequence(lastTextLength, unanalyzedTextLength);
                unanalyzedTextLength = unanalyzedText.length();
                candidateAlphabets = sLastCandidateAlphabets;
            }
        }

        if (candidateAlphabets == null) {
            // We cannot use any information from previous runs
            candidateAlphabets = getNewCandidateAlphabets();
        }

        // Now analyze (parts of the) the text
        for (int i = 0; i < unanalyzedTextLength && candidateAlphabets.size() > 0; i++) {
            int c = unanalyzedText.charAt(i);
            if (sGsm7bitAlphabet.indexOfKey(c) < 0) {
                // The character is not in the gsm7bitAlphabet, loop through all
                // remaining extension tables to see if it is any of them
                Iterator<GsmLanguage> it = candidateAlphabets.iterator();
                while (it.hasNext()) {
                    GsmLanguage language = it.next();
                    if (sCharToGsmExtendedTables.get(language).indexOfKey(c) < 0) {
                        // We found a language that does not contain the
                        // character, remove it from the candidate alphabets
                        it.remove();
                    }
                }
            }
        }

        sLastCandidateAlphabets = candidateAlphabets;
        sLastAnalyzedText = text.toString();

        if (candidateAlphabets.size() > 0) {
            // We have (at least) one alphabet that contains all characters
            return candidateAlphabets.get(0);
        } else {
            // We need unicode for this
            return null;
        }
    }

    private static List<GsmLanguage> getNewCandidateAlphabets() {
        List<GsmLanguage> list = new LinkedList<GsmLanguage>();
        for (GsmLanguage language : GsmLanguage.values()) {
            list.add(language);
        }
        return list;
    }

    /**
     * char to GSM alphabet char
     *
     * @param c the character to convert
     * @return Returns ' ' in GSM alphabet if there's no possible match
     *         Returns GSM_EXTENDED_ESCAPE if this character is in the extended
     *         table. In this case, you must call charToGsmExtended() for the
     *         value that should follow GSM_EXTENDED_ESCAPE in the GSM alphabet
     *         string
     */
    public int charToGsm(char c) {
        try {
            return charToGsm(c, false);
        } catch (EncodeException ex) {
            // this should never happen
            return mGsmSpaceChar;
        }
    }

    /**
     * char to GSM alphabet char
     *
     * @param throwException If true, throws EncodeException on invalid char. If
     *        false, returns GSM alphabet ' ' char.
     * @return Returns GSM_EXTENDED_ESCAPE if this character is in the extended
     *         table. In this case, you must call charToGsmExtended() for the
     *         value that should follow GSM_EXTENDED_ESCAPE in the GSM alphabet
     *         string
     */

    public int charToGsm(char c, boolean throwException) throws EncodeException {
        int ret;

        ret = mCharToGsmTable.get(c, -1);

        if (ret == -1) {
            ret = mCharToGsmExtendedTable.get(c, -1);

            if (ret == -1) {
                if (throwException) {
                    throw new EncodeException(c);
                } else {
                    return mGsmSpaceChar;
                }
            } else {
                return GSM_EXTENDED_ESCAPE;
            }
        }

        return ret;

    }

    /**
     * char to extended GSM alphabet char
     * Extended chars should be escaped with GSM_EXTENDED_ESCAPE
     *
     * @param c the character to convert
     * @return Returns ' ' in GSM alphabet if there's no possible match
     *
     */
    public int charToGsmExtended(char c) {
        int ret;

        ret = mCharToGsmExtendedTable.get(c, -1);

        if (ret == -1) {
            return mGsmSpaceChar;
        }

        return ret;
    }

    /**
     * Converts a character in the GSM alphabet into a char
     *
     * @param gsmChar the character to convert
     * @return if GSM_EXTENDED_ESCAPE is passed, 0xffff is returned. In this
     *         case, the following character in the stream should be decoded
     *         with gsmExtendedToChar(). If an unmappable value is passed
     *         (one greater than 127), ' ' is returned
     */

    public char gsmToChar(int gsmChar) {
        return (char)mGsmToCharTable.get(gsmChar, ' ');
    }

    /**
     * Converts a character in the extended GSM alphabet into a char
     *
     * @param gsmChar the character to convert
     * @return if GSM_EXTENDED_ESCAPE is passed, ' ' is returned since no second
     *         extension page has yet been defined (see Note 1 in Table 6.2.1.1
     *         of TS 23.038 v7.00)
     */

    public char gsmExtendedToChar(int gsmChar) {
        int ret;

        ret = mGsmExtendedToCharTable.get(gsmChar, -1);

        if (ret == -1) {
            // A miss in the extension table should fall back to the default
            // table
            return gsmToChar(gsmChar);
        }
        return (char)ret;
    }

    /**
     * Converts a String into a byte array containing the 7-bit packed GSM
     * Alphabet representation of the string. If a header is provided, this is
     * included in the returned byte array and padded to a septet boundary.
     *
     * Unencodable chars are encoded as spaces.
     *
     * Byte 0 in the returned byte array is the count of septets used, including
     * the header and header padding. The returned byte array is the minimum
     * size required to store the packed septets. The returned array cannot
     * contain more than 255 septets.
     *
     * @param data The text string to encode.
     * @param header Optional header (including length byte) that precedes the
     *        encoded data, padded to septet boundary.
     * @return Byte array containing header and encoded data.
     */
    public byte[] stringToGsm7BitPackedWithHeader(String data, byte[] header)
            throws EncodeException {
        if (header == null || header.length == 0) {
            return stringToGsm7BitPacked(data);
        }

        int headerBits = (header.length + 1) * 8;
        int headerSeptets = (headerBits + 6) / 7;

        byte[] ret = stringToGsm7BitPacked(data, headerSeptets, true);

        // Paste in the header
        ret[1] = (byte)header.length;
        System.arraycopy(header, 0, ret, 2, header.length);
        return ret;
    }

    /**
     * Converts a String into a byte array containing the 7-bit packed GSM
     * Alphabet representation of the string.
     *
     * Unencodable chars are encoded as spaces.
     *
     * Byte 0 in the returned byte array is the count of septets used The
     * returned byte array is the minimum size required to store the packed
     * septets. The returned array cannot contain more than 255 septets.
     *
     * @param data the data string to encode
     * @throws EncodeException if string is too large to encode
     */
    public byte[] stringToGsm7BitPacked(String data) throws EncodeException {
        return stringToGsm7BitPacked(data, 0, true);
    }

    /**
     * Converts a String into a byte array containing the 7-bit packed GSM
     * Alphabet representation of the string.
     *
     * @param data the text to convert to septets
     * @param startingSeptetOffset the number of padding septets to put before
     *        the character data at the beginning of the array
     * @param throwException If true, throws EncodeException on invalid char. If
     *        false, replaces unencodable char with GSM alphabet space char.
     * @return Byte 0 in the returned byte array is the count of septets used.
     *         The returned byte array is the minimum size required to store the
     *         packed septets. The returned array cannot contain more than 255
     *         septets.
     * @throws EncodeException if string is too large to encode
     */
    public byte[] stringToGsm7BitPacked(String data, int startingSeptetOffset,
            boolean throwException) throws EncodeException {
        int dataLen = data.length();
        int septetCount = countGsmSeptets(data, throwException) + startingSeptetOffset;
        if (septetCount > 255) {
            throw new EncodeException("Payload cannot exceed 255 septets");
        }
        int byteCount = ((septetCount * 7) + 7) / 8;
        byte[] ret = new byte[byteCount + 1]; // Include space for one byte length prefix.
        for (int i = 0, septets = startingSeptetOffset, bitOffset = startingSeptetOffset * 7;
                i < dataLen && septets < septetCount; i++, bitOffset += 7) {
            char c = data.charAt(i);
            int v = charToGsm(c, throwException);
            if (v == GSM_EXTENDED_ESCAPE) {
                v = charToGsmExtended(c); // Lookup the extended char.
                packSmsChar(ret, bitOffset, GSM_EXTENDED_ESCAPE);
                bitOffset += 7;
                septets++;
            }
            packSmsChar(ret, bitOffset, v);
            septets++;
        }
        ret[0] = (byte)(septetCount); // Validated by check above.

        return ret;
    }

    /**
     * Pack a 7-bit char into its appropriate place in a byte array
     *
     * @param bitOffset the bit offset that the septet should be packed at
     *        (septet index * 7)
     */
    private void packSmsChar(byte[] packedChars, int bitOffset, int value) {
        int byteOffset = bitOffset / 8;
        int shift = bitOffset % 8;

        packedChars[++byteOffset] |= value << shift;

        if (shift > 1) {
            packedChars[++byteOffset] = (byte)(value >> (8 - shift));
        }
    }

    /**
     * Convert a GSM alphabet 7 bit packed string (SMS string) into a
     * {@link java.lang.String}.
     *
     * See TS 23.038 6.1.2.1 for SMS Character Packing
     *
     * @param pdu the raw data from the pdu
     * @param offset the byte offset of
     * @param lengthSeptets string length in septets, not bytes
     * @return string representation or null on decoding exception
     */
    public String gsm7BitPackedToString(byte[] pdu, int offset, int lengthSeptets) {
        return gsm7BitPackedToString(pdu, offset, lengthSeptets, 0);
    }

    /**
     * Convert a GSM alphabet 7 bit packed string (SMS string) into a
     * {@link java.lang.String}.
     *
     * See TS 23.038 6.1.2.1 for SMS Character Packing
     *
     * @param pdu the raw data from the pdu
     * @param offset the byte offset
     * @param lengthSeptets string length in septets, not bytes
     * @param numPaddingBits the number of padding bits before the start of the
     *        string in the first byte
     * @return string representation or null on decoding exception
     */
    public String gsm7BitPackedToString(byte[] pdu, int offset, int lengthSeptets,
            int numPaddingBits) {
        StringBuilder ret = new StringBuilder(lengthSeptets);
        boolean prevCharWasEscape;

        try {
            prevCharWasEscape = false;

            for (int i = 0; i < lengthSeptets; i++) {
                int bitOffset = (7 * i) + numPaddingBits;

                int byteOffset = bitOffset / 8;
                int shift = bitOffset % 8;
                int gsmVal;

                gsmVal = (0x7f & (pdu[offset + byteOffset] >> shift));

                // if it crosses a byte boundary
                if (shift > 1) {
                    // set MSB bits to 0
                    gsmVal &= 0x7f >> (shift - 1);

                    gsmVal |= 0x7f & (pdu[offset + byteOffset + 1] << (8 - shift));
                }

                if (prevCharWasEscape) {
                    ret.append(gsmExtendedToChar(gsmVal));
                    prevCharWasEscape = false;
                } else if (gsmVal == GSM_EXTENDED_ESCAPE) {
                    prevCharWasEscape = true;
                } else {
                    ret.append(gsmToChar(gsmVal));
                }
            }
        } catch (RuntimeException ex) {
            Log.e(LOG_TAG, "Error GSM 7 bit packed: ", ex);
            return null;
        }

        return ret.toString();
    }

    /**
     * Convert a GSM alphabet string that's stored in 8-bit unpacked format (as
     * it often appears in SIM records) into a string
     *
     * Field may be padded with trailing 0xff's. The decode stops at the first
     * 0xff encountered.
     */
    public String gsm8BitUnpackedToString(byte[] data, int offset, int length) {
        boolean prevWasEscape;
        StringBuilder ret = new StringBuilder(length);

        prevWasEscape = false;
        for (int i = offset; i < offset + length; i++) {
            // Never underestimate the pain that can be caused
            // by signed bytes
            int c = data[i] & 0xff;

            if (c == 0xff) {
                break;
            } else if (c == GSM_EXTENDED_ESCAPE) {
                if (prevWasEscape) {
                    // Two escape chars in a row
                    // We treat this as a space
                    // See Note 1 in Table 6.2.1.1 of TS 23.038 v7.00
                    ret.append(' ');
                    prevWasEscape = false;
                } else {
                    prevWasEscape = true;
                }
            } else {
                if (prevWasEscape) {
                    ret.append((char)mGsmExtendedToCharTable.get(c, ' '));
                } else {
                    ret.append((char)mGsmToCharTable.get(c, ' '));
                }
                prevWasEscape = false;
            }
        }

        return ret.toString();
    }

    /**
     * Convert a string into an 8-bit unpacked GSM alphabet byte array
     */
    public byte[] stringToGsm8BitPacked(String s) {
        byte[] ret;

        int septets = 0;

        septets = countGsmSeptets(s);

        // Enough for all the septets and the length byte prefix
        ret = new byte[septets];

        stringToGsm8BitUnpackedField(s, ret, 0, ret.length);

        return ret;
    }

    /**
     * Write a String into a GSM 8-bit unpacked field <code>dest</code> of size
     * <code>length</code> at given <code>offset</code>. Field is padded with
     * 0xff's, string is truncated if necessary.
     *
     * @param length size of field
     * @param offset offset into field
     * @param dest field
     */

    public void stringToGsm8BitUnpackedField(String s, byte dest[], int offset, int length) {
        int outByteIndex = offset;

        // Septets are stored in byte-aligned octets
        for (int i = 0, sz = s.length(); i < sz && (outByteIndex - offset) < length; i++) {
            char c = s.charAt(i);

            int v = charToGsm(c);

            if (v == GSM_EXTENDED_ESCAPE) {
                // make sure we can fit an escaped char
                if (!(outByteIndex + 1 - offset < length)) {
                    break;
                }

                dest[outByteIndex++] = GSM_EXTENDED_ESCAPE;

                v = charToGsmExtended(c);
            }

            dest[outByteIndex++] = (byte)v;
        }

        // pad with 0xff's
        while ((outByteIndex - offset) < length) {
            dest[outByteIndex++] = (byte)0xff;
        }
    }

    /**
     * Returns the count of 7-bit GSM alphabet characters needed to represent
     * this character. Counts unencodable char as 1 septet.
     *
     * @param c the character to count
     * @return count of 7-bit GSM alphabet characters needed to represent
     *         this character
     */
    public int countGsmSeptets(char c) {
        try {
            return countGsmSeptets(c, false);
        } catch (EncodeException ex) {
            // This should never happen.
            return 0;
        }
    }

    /**
     * Returns the count of 7-bit GSM alphabet characters needed to represent
     * this character
     *
     * @param c the character to count
     * @param throwsException if true, throws EncodeException if unencodable
     *        char. Otherwise, counts invalid char as 1 septet.
     * @return count of 7-bit GSM alphabet characters needed to represent
     *         this character
     */
    public int countGsmSeptets(char c, boolean throwsException) throws EncodeException {
        if (mCharToGsmTable.get(c, -1) != -1) {
            return 1;
        }

        if (mCharToGsmExtendedTable.get(c, -1) != -1) {
            return 2;
        }

        if (throwsException) {
            throw new EncodeException(c);
        } else {
            // count as a space char
            return 1;
        }
    }

    /**
     * Returns the count of 7-bit GSM alphabet characters needed to represent
     * this string. Counts unencodable char as 1 septet.
     *
     * @param s the character sequence to count
     * @return count of 7-bit GSM alphabet characters needed to represent
     *         this character sequence
     */
    public int countGsmSeptets(CharSequence s) {
        try {
            return countGsmSeptets(s, false);
        } catch (EncodeException ex) {
            // this should never happen
            return 0;
        }
    }

    /**
     * Returns the count of 7-bit GSM alphabet characters needed to represent
     * this string.
     *
     * @param s the character sequence to count
     * @param throwsException if true, throws EncodeException if unencodable
     *        char. Otherwise, counts invalid char as 1 septet.
     * @return count of 7-bit GSM alphabet characters needed to represent
     *         this character sequence
     */
    public int countGsmSeptets(CharSequence s, boolean throwsException) throws EncodeException {
        int charIndex = 0;
        int sz = s.length();
        int count = 0;

        while (charIndex < sz) {
            count += countGsmSeptets(s.charAt(charIndex), throwsException);
            charIndex++;
        }

        return count;
    }

    /**
     * Returns the index into <code>s</code> of the first character after
     * <code>limit</code> septets have been reached, starting at index
     * <code>start</code>. This is used when dividing messages into units within
     * the SMS message size limit.
     *
     * @param s source string
     * @param start index of where to start counting septets
     * @param limit maximum septets to include, e.g.
     *        <code>MAX_USER_DATA_SEPTETS</code>
     * @return index of first character that won't fit, or the length of the
     *         entire string if everything fits
     */
    public int findGsmSeptetLimitIndex(String s, int start, int limit) {
        int accumulator = 0;
        int size = s.length();

        for (int i = start; i < size; i++) {
            accumulator += countGsmSeptets(s.charAt(i));
            if (accumulator > limit) {
                return i;
            }
        }
        return size;
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmAlphabetTables.java b/telephony/java/com/android/internal/telephony/gsm/GsmAlphabetTables.java
new file mode 100644
//Synthetic comment -- index 0000000..0b3e6b9

//Synthetic comment -- @@ -0,0 +1,1939 @@
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

package com.android.internal.telephony.gsm;

import com.android.internal.telephony.gsm.GsmAlphabet.GsmLanguage;

import android.util.SparseIntArray;

import java.util.Map;

/**
 * This class contain the tables used for the GSM SMS 7-bit alphabet specified
 * in TS 23.038 6.2.1
 *
 * {@hide}
 */
public class GsmAlphabetTables {
    static final String LOG_TAG = "GSM";

    static void init(Map<GsmLanguage, SparseIntArray> charToGsmTables,
            Map<GsmLanguage, SparseIntArray> gsmToCharTables,
            Map<GsmLanguage, SparseIntArray> charToGsmExtendedTables,
            Map<GsmLanguage, SparseIntArray> gsmExtendedToCharTables) {

        // Create the table for the standard GSM SMS 7-bit alphabet
        SparseIntArray table = new SparseIntArray();
        gsmToCharTables.put(GsmLanguage.DEFAULT, table);

        int i = 0;
        table.put(i++, '@');
        table.put(i++, '\u00a3');
        table.put(i++, '$');
        table.put(i++, '\u00a5');
        table.put(i++, '\u00e8');
        table.put(i++, '\u00e9');
        table.put(i++, '\u00f9');
        table.put(i++, '\u00ec');
        table.put(i++, '\u00f2');
        table.put(i++, '\u00c7');
        table.put(i++, '\n');
        table.put(i++, '\u00d8');
        table.put(i++, '\u00f8');
        table.put(i++, '\r');
        table.put(i++, '\u00c5');
        table.put(i++, '\u00e5');

        table.put(i++, '\u0394');
        table.put(i++, '_');
        table.put(i++, '\u03a6');
        table.put(i++, '\u0393');
        table.put(i++, '\u039b');
        table.put(i++, '\u03a9');
        table.put(i++, '\u03a0');
        table.put(i++, '\u03a8');
        table.put(i++, '\u03a3');
        table.put(i++, '\u0398');
        table.put(i++, '\u039e');
        table.put(i++, '\uffff');
        table.put(i++, '\u00c6');
        table.put(i++, '\u00e6');
        table.put(i++, '\u00df');
        table.put(i++, '\u00c9');

        table.put(i++, ' ');
        table.put(i++, '!');
        table.put(i++, '"');
        table.put(i++, '#');
        table.put(i++, '\u00a4');
        table.put(i++, '%');
        table.put(i++, '&');
        table.put(i++, '\'');
        table.put(i++, '(');
        table.put(i++, ')');
        table.put(i++, '*');
        table.put(i++, '+');
        table.put(i++, ',');
        table.put(i++, '-');
        table.put(i++, '.');
        table.put(i++, '/');

        table.put(i++, '0');
        table.put(i++, '1');
        table.put(i++, '2');
        table.put(i++, '3');
        table.put(i++, '4');
        table.put(i++, '5');
        table.put(i++, '6');
        table.put(i++, '7');
        table.put(i++, '8');
        table.put(i++, '9');
        table.put(i++, ':');
        table.put(i++, ';');
        table.put(i++, '<');
        table.put(i++, '=');
        table.put(i++, '>');
        table.put(i++, '?');

        table.put(i++, '\u00a1');
        table.put(i++, 'A');
        table.put(i++, 'B');
        table.put(i++, 'C');
        table.put(i++, 'D');
        table.put(i++, 'E');
        table.put(i++, 'F');
        table.put(i++, 'G');
        table.put(i++, 'H');
        table.put(i++, 'I');
        table.put(i++, 'J');
        table.put(i++, 'K');
        table.put(i++, 'L');
        table.put(i++, 'M');
        table.put(i++, 'N');
        table.put(i++, 'O');

        table.put(i++, 'P');
        table.put(i++, 'Q');
        table.put(i++, 'R');
        table.put(i++, 'S');
        table.put(i++, 'T');
        table.put(i++, 'U');
        table.put(i++, 'V');
        table.put(i++, 'W');
        table.put(i++, 'X');
        table.put(i++, 'Y');
        table.put(i++, 'Z');
        table.put(i++, '\u00c4');
        table.put(i++, '\u00d6');
        table.put(i++, '\u00d1');
        table.put(i++, '\u00dc');
        table.put(i++, '\u00a7');

        table.put(i++, '\u00bf');
        table.put(i++, 'a');
        table.put(i++, 'b');
        table.put(i++, 'c');
        table.put(i++, 'd');
        table.put(i++, 'e');
        table.put(i++, 'f');
        table.put(i++, 'g');
        table.put(i++, 'h');
        table.put(i++, 'i');
        table.put(i++, 'j');
        table.put(i++, 'k');
        table.put(i++, 'l');
        table.put(i++, 'm');
        table.put(i++, 'n');
        table.put(i++, 'o');

        table.put(i++, 'p');
        table.put(i++, 'q');
        table.put(i++, 'r');
        table.put(i++, 's');
        table.put(i++, 't');
        table.put(i++, 'u');
        table.put(i++, 'v');
        table.put(i++, 'w');
        table.put(i++, 'x');
        table.put(i++, 'y');
        table.put(i++, 'z');
        table.put(i++, '\u00e4');
        table.put(i++, '\u00f6');
        table.put(i++, '\u00f1');
        table.put(i++, '\u00fc');
        table.put(i++, '\u00e0');

        // Create the locking shift table for turkish national language
        table = copyTable(gsmToCharTables.get(GsmLanguage.DEFAULT));
        gsmToCharTables.put(GsmLanguage.TURKISH, table);

        table.put(0x04, '\u20ac');
        table.put(0x07, '\u0131');
        table.put(0x0b, '\u011e');
        table.put(0x0c, '\u011f');

        table.put(0x1c, '\u015e');
        table.put(0x1d, '\u015f');

        table.put(0x40, '\u0130');

        table.put(0x60, '\u00e7');

        // Create the locking shift table for spanish national language
        // Not defined - fall back to GSM 7 bit default
        table = copyTable(gsmToCharTables.get(GsmLanguage.DEFAULT));
        gsmToCharTables.put(GsmLanguage.SPANISH, table);

        // Create the locking shift table for portuguese national language
        table = copyTable(gsmToCharTables.get(GsmLanguage.DEFAULT));
        gsmToCharTables.put(GsmLanguage.PORTUGUESE, table);
        table.put(0x04, '\u00ea');
        table.put(0x06, '\u00fa');
        table.put(0x07, '\u00ed');
        table.put(0x08, '\u00f3');
        table.put(0x09, '\u00e7');
        table.put(0x0b, '\u00d4');
        table.put(0x0c, '\u00f4');
        table.put(0x0e, '\u00c1');
        table.put(0x0f, '\u00e1');

        table.put(0x12, '\u00aa');
        table.put(0x13, '\u00c7');
        table.put(0x14, '\u00c0');
        table.put(0x15, '\u22e1');
        table.put(0x16, '\u005e');
        table.put(0x17, '\\');
        table.put(0x18, '\u20ac');
        table.put(0x19, '\u00d3');
        table.put(0x1a, '\u007c');
        table.put(0x1c, '\u00c2');
        table.put(0x1d, '\u00e2');
        table.put(0x1e, '\u00ca');

        table.put(0x24, '\u00b0');

        table.put(0x40, '\u00cd');

        table.put(0x5b, '\u00c3');
        table.put(0x5c, '\u005d');
        table.put(0x5d, '\u00da');

        table.put(0x60, '\u007e');

        table.put(0x7b, '\u00e3');
        table.put(0x7c, '\u00f5');
        table.put(0x7d, '\u0060');

        // Create the locking shift table for bengali national language
        table = copyTable(gsmToCharTables.get(GsmLanguage.DEFAULT));
        gsmToCharTables.put(GsmLanguage.BENGALI, table);
        table.put(0x00, '\u0981');
        table.put(0x01, '\u0982');
        table.put(0x02, '\u0983');
        table.put(0x03, '\u0985');
        table.put(0x04, '\u0986');
        table.put(0x05, '\u0987');
        table.put(0x06, '\u0988');
        table.put(0x07, '\u0989');
        table.put(0x08, '\u098a');
        table.put(0x09, '\u098b');
        table.put(0x0b, '\u098c');
        table.put(0x0f, '\u098f');

        table.put(0x10, '\u0990');
        table.put(0x13, '\u0993');
        table.put(0x14, '\u0994');
        table.put(0x15, '\u0995');
        table.put(0x16, '\u0996');
        table.put(0x17, '\u0997');
        table.put(0x18, '\u0998');
        table.put(0x19, '\u0999');
        table.put(0x1a, '\u099a');
        table.put(0x1c, '\u099b');
        table.put(0x1d, '\u099c');
        table.put(0x1e, '\u099d');
        table.put(0x1f, '\u099e');

        table.put(0x22, '\u099f');
        table.put(0x23, '\u09a0');
        table.put(0x24, '\u09a1');
        table.put(0x25, '\u09a2');
        table.put(0x26, '\u09a3');
        table.put(0x27, '\u09a4');
        table.put(0x2a, '\u09a5');
        table.put(0x2b, '\u09a6');
        table.put(0x2d, '\u09a7');
        table.put(0x2f, '\u09a8');

        table.put(0x3d, '\u09aa');
        table.put(0x3e, '\u09ab');

        table.put(0x40, '\u09ac');
        table.put(0x41, '\u09ad');
        table.put(0x42, '\u09ae');
        table.put(0x43, '\u09af');
        table.put(0x44, '\u09b0');
        table.put(0x46, '\u09b2');
        table.put(0x4a, '\u09b6');
        table.put(0x4b, '\u09b7');
        table.put(0x4c, '\u09b8');
        table.put(0x4d, '\u09b9');
        table.put(0x4e, '\u09bc');
        table.put(0x4f, '\u09bd');

        table.put(0x50, '\u09be');
        table.put(0x51, '\u09bf');
        table.put(0x52, '\u09c0');
        table.put(0x53, '\u09c1');
        table.put(0x54, '\u09c2');
        table.put(0x55, '\u09c3');
        table.put(0x56, '\u09c4');
        table.put(0x59, '\u09c7');
        table.put(0x5a, '\u09c8');
        table.put(0x5d, '\u09cb');
        table.put(0x5e, '\u09cc');
        table.put(0x5f, '\u09cd');

        table.put(0x60, '\u09ce');

        table.put(0x7b, '\u09d7');
        table.put(0x7c, '\u09dc');
        table.put(0x7d, '\u09dd');
        table.put(0x7e, '\u09f0');
        table.put(0x7f, '\u09f1');

        // Create the locking shift table for gujariti national language
        table = copyTable(gsmToCharTables.get(GsmLanguage.DEFAULT));
        gsmToCharTables.put(GsmLanguage.GUJARATI, table);
        table.put(0x00, '\u0a81');
        table.put(0x01, '\u0a82');
        table.put(0x02, '\u0a83');
        table.put(0x03, '\u0a85');
        table.put(0x04, '\u0a86');
        table.put(0x05, '\u0a87');
        table.put(0x06, '\u0a88');
        table.put(0x07, '\u0a89');
        table.put(0x08, '\u0a8a');
        table.put(0x09, '\u0a8b');
        table.put(0x0b, '\u0a8c');
        table.put(0x0c, '\u0a8d');
        table.put(0x0f, '\u0a8f');

        table.put(0x10, '\u0a90');
        table.put(0x11, '\u0a91');
        table.put(0x13, '\u0a93');
        table.put(0x14, '\u0a94');
        table.put(0x15, '\u0a95');
        table.put(0x16, '\u0a96');
        table.put(0x17, '\u0a97');
        table.put(0x18, '\u0a98');
        table.put(0x19, '\u0a99');
        table.put(0x1a, '\u0a9a');
        table.put(0x1c, '\u0a9b');
        table.put(0x1d, '\u0a9c');
        table.put(0x1e, '\u0a9d');
        table.put(0x1f, '\u0a9e');

        table.put(0x22, '\u0a9f');
        table.put(0x23, '\u0aa0');
        table.put(0x24, '\u0aa1');
        table.put(0x25, '\u0aa2');
        table.put(0x26, '\u0aa3');
        table.put(0x27, '\u0aa4');
        table.put(0x2a, '\u0aa5');
        table.put(0x2b, '\u0aa6');
        table.put(0x2d, '\u0aa7');
        table.put(0x2f, '\u0aa8');

        table.put(0x3d, '\u0aaa');
        table.put(0x3e, '\u0aab');

        table.put(0x40, '\u0aac');
        table.put(0x41, '\u0aad');
        table.put(0x42, '\u0aae');
        table.put(0x43, '\u0aaf');
        table.put(0x44, '\u0ab0');
        table.put(0x46, '\u0ab2');
        table.put(0x47, '\u0ab3');
        table.put(0x49, '\u0ab5');
        table.put(0x4a, '\u0ab6');
        table.put(0x4b, '\u0ab7');
        table.put(0x4c, '\u0ab8');
        table.put(0x4d, '\u0ab9');
        table.put(0x4e, '\u0abc');
        table.put(0x4f, '\u0abd');

        table.put(0x50, '\u0abe');
        table.put(0x51, '\u0abf');
        table.put(0x52, '\u0ac0');
        table.put(0x53, '\u0ac1');
        table.put(0x54, '\u0ac2');
        table.put(0x55, '\u0ac3');
        table.put(0x56, '\u0ac4');
        table.put(0x57, '\u0ac5');
        table.put(0x59, '\u0ac7');
        table.put(0x5a, '\u0ac8');
        table.put(0x5b, '\u0ac9');
        table.put(0x5d, '\u0acb');
        table.put(0x5e, '\u0acc');
        table.put(0x5f, '\u0acd');

        table.put(0x60, '\u0ad0');

        table.put(0x7b, '\u0ae0');
        table.put(0x7c, '\u0ae1');
        table.put(0x7d, '\u0ae2');
        table.put(0x7e, '\u0ae3');
        table.put(0x7f, '\u0af1');

        // Create the locking shift table for hindi national language
        table = copyTable(gsmToCharTables.get(GsmLanguage.DEFAULT));
        gsmToCharTables.put(GsmLanguage.HINDI, table);
        table.put(0x00, '\u0901');
        table.put(0x01, '\u0902');
        table.put(0x02, '\u0903');
        table.put(0x03, '\u0905');
        table.put(0x04, '\u0906');
        table.put(0x05, '\u0907');
        table.put(0x06, '\u0908');
        table.put(0x07, '\u0909');
        table.put(0x08, '\u090a');
        table.put(0x09, '\u090b');
        table.put(0x0b, '\u090c');
        table.put(0x0c, '\u090c');
        table.put(0x0e, '\u090e');
        table.put(0x0f, '\u090f');

        table.put(0x10, '\u0910');
        table.put(0x11, '\u0911');
        table.put(0x12, '\u0912');
        table.put(0x13, '\u0913');
        table.put(0x14, '\u0914');
        table.put(0x15, '\u0915');
        table.put(0x16, '\u0916');
        table.put(0x17, '\u0917');
        table.put(0x18, '\u0918');
        table.put(0x19, '\u0919');
        table.put(0x1a, '\u091a');
        table.put(0x1c, '\u091b');
        table.put(0x1d, '\u091c');
        table.put(0x1e, '\u091d');
        table.put(0x1f, '\u091e');

        table.put(0x22, '\u091f');
        table.put(0x23, '\u0920');
        table.put(0x24, '\u0921');
        table.put(0x25, '\u0922');
        table.put(0x26, '\u0923');
        table.put(0x27, '\u0924');
        table.put(0x2a, '\u0925');
        table.put(0x2b, '\u0926');
        table.put(0x2d, '\u0927');
        table.put(0x2f, '\u0928');

        table.put(0x3c, '\u0929');
        table.put(0x3d, '\u092a');
        table.put(0x3e, '\u092b');

        table.put(0x40, '\u092c');
        table.put(0x41, '\u092d');
        table.put(0x42, '\u092e');
        table.put(0x43, '\u092f');
        table.put(0x44, '\u0930');
        table.put(0x45, '\u0931');
        table.put(0x46, '\u0932');
        table.put(0x47, '\u0933');
        table.put(0x48, '\u0934');
        table.put(0x49, '\u0935');
        table.put(0x4a, '\u0936');
        table.put(0x4c, '\u0937');
        table.put(0x4d, '\u0938');
        table.put(0x4e, '\u0939');
        table.put(0x4f, '\u093c');
        table.put(0x4f, '\u093d');

        table.put(0x50, '\u093e');
        table.put(0x51, '\u093f');
        table.put(0x52, '\u0940');
        table.put(0x53, '\u0941');
        table.put(0x54, '\u0942');
        table.put(0x55, '\u0943');
        table.put(0x56, '\u0944');
        table.put(0x57, '\u0945');
        table.put(0x58, '\u0946');
        table.put(0x59, '\u0947');
        table.put(0x5a, '\u0948');
        table.put(0x5c, '\u0949');
        table.put(0x5d, '\u094a');
        table.put(0x5e, '\u094b');
        table.put(0x5f, '\u094c');
        table.put(0x5f, '\u094d');

        table.put(0x60, '\u0950');

        table.put(0x7b, '\u0972');
        table.put(0x7c, '\u097b');
        table.put(0x7d, '\u097c');
        table.put(0x7e, '\u097e');
        table.put(0x7f, '\u097f');

        // Create the locking shift table for kannada national language
        table = copyTable(gsmToCharTables.get(GsmLanguage.DEFAULT));
        gsmToCharTables.put(GsmLanguage.KANNADA, table);
        table.put(0x01, '\u0c82');
        table.put(0x02, '\u0c83');
        table.put(0x03, '\u0c85');
        table.put(0x04, '\u0c86');
        table.put(0x05, '\u0c87');
        table.put(0x06, '\u0c88');
        table.put(0x07, '\u0c89');
        table.put(0x08, '\u0c8a');
        table.put(0x09, '\u0c8b');
        table.put(0x0b, '\u0c8c');
        table.put(0x0e, '\u0c8e');
        table.put(0x0f, '\u0c8f');

        table.put(0x10, '\u0c90');
        table.put(0x12, '\u0c92');
        table.put(0x13, '\u0c93');
        table.put(0x14, '\u0c94');
        table.put(0x15, '\u0c95');
        table.put(0x16, '\u0c96');
        table.put(0x17, '\u0c97');
        table.put(0x18, '\u0c98');
        table.put(0x19, '\u0c99');
        table.put(0x1a, '\u0c9a');
        table.put(0x1c, '\u0c9b');
        table.put(0x1d, '\u0c9c');
        table.put(0x1e, '\u0c9d');
        table.put(0x1f, '\u0c9e');

        table.put(0x22, '\u0c9f');
        table.put(0x23, '\u0ca0');
        table.put(0x24, '\u0caa');
        table.put(0x25, '\u0ca2');
        table.put(0x26, '\u0ca3');
        table.put(0x27, '\u0ca4');
        table.put(0x2a, '\u0ca5');
        table.put(0x2b, '\u0ca6');
        table.put(0x2d, '\u0ca7');
        table.put(0x2f, '\u0ca8');

        table.put(0x3d, '\u0caa');
        table.put(0x3e, '\u0cab');

        table.put(0x40, '\u0cac');
        table.put(0x41, '\u0cad');
        table.put(0x42, '\u0cae');
        table.put(0x43, '\u0caf');
        table.put(0x44, '\u0cb0');
        table.put(0x45, '\u0cb1');
        table.put(0x46, '\u0cb2');
        table.put(0x47, '\u0cb3');
        table.put(0x49, '\u0cb5');
        table.put(0x4a, '\u0cb6');
        table.put(0x4b, '\u0cb7');
        table.put(0x4c, '\u0cb8');
        table.put(0x4d, '\u0cb9');
        table.put(0x4e, '\u0cbc');
        table.put(0x4f, '\u0cbd');

        table.put(0x50, '\u0cbe');
        table.put(0x51, '\u0cbf');
        table.put(0x52, '\u0cc0');
        table.put(0x53, '\u0cc1');
        table.put(0x54, '\u0cc2');
        table.put(0x55, '\u0cc3');
        table.put(0x56, '\u0cc4');
        table.put(0x58, '\u0cc6');
        table.put(0x59, '\u0cc7');
        table.put(0x5a, '\u0cc8');
        table.put(0x5c, '\u0cca');
        table.put(0x5d, '\u0ccb');
        table.put(0x5e, '\u0ccc');
        table.put(0x5f, '\u0ccd');

        table.put(0x7b, '\u0cd6');
        table.put(0x7c, '\u0ce0');
        table.put(0x7d, '\u0ce1');
        table.put(0x7e, '\u0ce2');
        table.put(0x7f, '\u0ce3');

        // Create the locking shift table for malayalam national language
        table = copyTable(gsmToCharTables.get(GsmLanguage.DEFAULT));
        gsmToCharTables.put(GsmLanguage.MALAYALAM, table);
        table.put(0x01, '\u0d02');
        table.put(0x02, '\u0d03');
        table.put(0x03, '\u0d05');
        table.put(0x04, '\u0d06');
        table.put(0x05, '\u0d07');
        table.put(0x06, '\u0d08');
        table.put(0x07, '\u0d09');
        table.put(0x08, '\u0d0a');
        table.put(0x09, '\u0d0b');
        table.put(0x0b, '\u0d0c');
        table.put(0x0e, '\u0d0e');
        table.put(0x0f, '\u0c0f');

        table.put(0x10, '\u0d10');
        table.put(0x12, '\u0d12');
        table.put(0x13, '\u0d13');
        table.put(0x14, '\u0d14');
        table.put(0x15, '\u0d15');
        table.put(0x16, '\u0d16');
        table.put(0x17, '\u0d17');
        table.put(0x18, '\u0d18');
        table.put(0x19, '\u0d19');
        table.put(0x1a, '\u0d1a');
        table.put(0x1c, '\u0d1b');
        table.put(0x1d, '\u0d1c');
        table.put(0x1e, '\u0d1d');
        table.put(0x1f, '\u0d1e');

        table.put(0x22, '\u0d1f');
        table.put(0x23, '\u0d20');
        table.put(0x24, '\u0d21');
        table.put(0x25, '\u0d22');
        table.put(0x26, '\u0d23');
        table.put(0x27, '\u0d24');
        table.put(0x2a, '\u0d25');
        table.put(0x2b, '\u0d26');
        table.put(0x2d, '\u0d27');
        table.put(0x2f, '\u0d28');

        table.put(0x3d, '\u0d2a');
        table.put(0x3e, '\u0d2b');

        table.put(0x40, '\u0d2c');
        table.put(0x41, '\u0d2d');
        table.put(0x42, '\u0d2e');
        table.put(0x43, '\u0d2f');
        table.put(0x44, '\u0d30');
        table.put(0x45, '\u0d31');
        table.put(0x46, '\u0d32');
        table.put(0x47, '\u0d33');
        table.put(0x48, '\u0d34');
        table.put(0x49, '\u0d35');
        table.put(0x4a, '\u0d36');
        table.put(0x4b, '\u0d37');
        table.put(0x4c, '\u0d38');
        table.put(0x4d, '\u0d39');
        table.put(0x4f, '\u0d3d');

        table.put(0x50, '\u0d3e');
        table.put(0x51, '\u0d3f');
        table.put(0x52, '\u0d40');
        table.put(0x53, '\u0d41');
        table.put(0x54, '\u0d42');
        table.put(0x55, '\u0d43');
        table.put(0x56, '\u0d44');
        table.put(0x58, '\u0d46');
        table.put(0x59, '\u0d47');
        table.put(0x5a, '\u0d48');
        table.put(0x5c, '\u0d4a');
        table.put(0x5d, '\u0d4b');
        table.put(0x5e, '\u0d4c');
        table.put(0x5f, '\u0d4d');

        table.put(0x60, '\u0d57');

        table.put(0x7b, '\u0d60');
        table.put(0x7c, '\u0d61');
        table.put(0x7d, '\u0d62');
        table.put(0x7e, '\u0d63');
        table.put(0x7f, '\u0d79');

        // Create the locking shift table for oriya national language
        table = copyTable(gsmToCharTables.get(GsmLanguage.DEFAULT));
        gsmToCharTables.put(GsmLanguage.ORIYA, table);
        table.put(0x00, '\u0b01');
        table.put(0x01, '\u0b02');
        table.put(0x02, '\u0b03');
        table.put(0x03, '\u0b05');
        table.put(0x04, '\u0b06');
        table.put(0x05, '\u0b07');
        table.put(0x06, '\u0b08');
        table.put(0x07, '\u0b09');
        table.put(0x08, '\u0b0a');
        table.put(0x09, '\u0b0b');
        table.put(0x0b, '\u0b0c');
        table.put(0x0f, '\u0b0f');

        table.put(0x00, '\u0b10');
        table.put(0x13, '\u0b13');
        table.put(0x14, '\u0b14');
        table.put(0x15, '\u0b15');
        table.put(0x16, '\u0b16');
        table.put(0x17, '\u0b17');
        table.put(0x18, '\u0b18');
        table.put(0x19, '\u0b19');
        table.put(0x1a, '\u0b1a');
        table.put(0x1c, '\u0b1b');
        table.put(0x1d, '\u0b1c');
        table.put(0x1e, '\u0b1d');
        table.put(0x1f, '\u0b1e');

        table.put(0x22, '\u0b1f');
        table.put(0x23, '\u0b20');
        table.put(0x24, '\u0b21');
        table.put(0x25, '\u0b22');
        table.put(0x26, '\u0b23');
        table.put(0x27, '\u0b24');
        table.put(0x2a, '\u0b25');
        table.put(0x2b, '\u0b26');
        table.put(0x2d, '\u0b27');
        table.put(0x2f, '\u0b28');

        table.put(0x3d, '\u0b2a');
        table.put(0x3e, '\u0b2b');

        table.put(0x40, '\u0b2c');
        table.put(0x41, '\u0b2d');
        table.put(0x42, '\u0b2e');
        table.put(0x43, '\u0b2f');
        table.put(0x44, '\u0b30');
        table.put(0x46, '\u0b32');
        table.put(0x47, '\u0b33');
        table.put(0x49, '\u0b35');
        table.put(0x4a, '\u0b36');
        table.put(0x4b, '\u0b37');
        table.put(0x4c, '\u0b38');
        table.put(0x4d, '\u0b39');
        table.put(0x4f, '\u0b3d');

        table.put(0x50, '\u0b3e');
        table.put(0x51, '\u0b3f');
        table.put(0x52, '\u0b40');
        table.put(0x53, '\u0b41');
        table.put(0x54, '\u0b42');
        table.put(0x55, '\u0b43');
        table.put(0x56, '\u0b44');
        table.put(0x59, '\u0b47');
        table.put(0x5a, '\u0b48');
        table.put(0x5d, '\u0b4b');
        table.put(0x5e, '\u0b4c');
        table.put(0x5f, '\u0b4d');

        table.put(0x60, '\u0b56');

        table.put(0x7b, '\u0b57');
        table.put(0x7c, '\u0b60');
        table.put(0x7d, '\u0b61');
        table.put(0x7e, '\u0b62');
        table.put(0x7f, '\u0b63');

        // Create the locking shift table for punjabi national language
        table = copyTable(gsmToCharTables.get(GsmLanguage.DEFAULT));
        gsmToCharTables.put(GsmLanguage.PUNJABI, table);
        table.put(0x00, '\u0a01');
        table.put(0x01, '\u0a02');
        table.put(0x02, '\u0a03');
        table.put(0x03, '\u0a05');
        table.put(0x04, '\u0a06');
        table.put(0x05, '\u0a07');
        table.put(0x06, '\u0a08');
        table.put(0x07, '\u0a09');
        table.put(0x08, '\u0a0a');
        table.put(0x0f, '\u0a0f');

        table.put(0x10, '\u0a10');
        table.put(0x13, '\u0a13');
        table.put(0x14, '\u0a14');
        table.put(0x15, '\u0a15');
        table.put(0x16, '\u0a16');
        table.put(0x17, '\u0a17');
        table.put(0x18, '\u0a18');
        table.put(0x19, '\u0a19');
        table.put(0x1a, '\u0a1a');
        table.put(0x1c, '\u0a1b');
        table.put(0x1d, '\u0a1c');
        table.put(0x1e, '\u0a1d');
        table.put(0x1f, '\u0a1e');

        table.put(0x22, '\u0a1f');
        table.put(0x23, '\u0a20');
        table.put(0x24, '\u0a21');
        table.put(0x25, '\u0a22');
        table.put(0x26, '\u0a23');
        table.put(0x27, '\u0a24');
        table.put(0x2a, '\u0a25');
        table.put(0x2b, '\u0a26');
        table.put(0x2d, '\u0a27');
        table.put(0x2f, '\u0a28');

        table.put(0x3d, '\u0a2a');
        table.put(0x3e, '\u0a2b');

        table.put(0x40, '\u0a2c');
        table.put(0x41, '\u0a2d');
        table.put(0x42, '\u0a2e');
        table.put(0x43, '\u0a2f');
        table.put(0x44, '\u0a30');
        table.put(0x46, '\u0a32');
        table.put(0x47, '\u0a33');
        table.put(0x49, '\u0a35');
        table.put(0x4a, '\u0a36');
        table.put(0x4c, '\u0a38');
        table.put(0x4d, '\u0a39');
        table.put(0x4e, '\u0a3c');

        table.put(0x50, '\u0a3e');
        table.put(0x51, '\u0a3f');
        table.put(0x52, '\u0a40');
        table.put(0x53, '\u0a41');
        table.put(0x54, '\u0a42');
        table.put(0x59, '\u0a47');
        table.put(0x5a, '\u0a48');
        table.put(0x5d, '\u0a4b');
        table.put(0x5e, '\u0a4c');
        table.put(0x5f, '\u0a4d');

        table.put(0x60, '\u0a51');

        table.put(0x7b, '\u0a70');
        table.put(0x7c, '\u0a71');
        table.put(0x7d, '\u0a72');
        table.put(0x7e, '\u0a73');
        table.put(0x7f, '\u0a74');

        // Create the locking shift table for tamil national language
        table = copyTable(gsmToCharTables.get(GsmLanguage.DEFAULT));
        gsmToCharTables.put(GsmLanguage.TAMIL, table);
        table.put(0x01, '\u0b82');
        table.put(0x02, '\u0b83');
        table.put(0x03, '\u0b85');
        table.put(0x04, '\u0b86');
        table.put(0x05, '\u0b87');
        table.put(0x06, '\u0b88');
        table.put(0x07, '\u0b89');
        table.put(0x08, '\u0b8a');
        table.put(0x0e, '\u0b8e');
        table.put(0x0f, '\u0b8f');

        table.put(0x10, '\u0b90');
        table.put(0x12, '\u0b92');
        table.put(0x13, '\u0b93');
        table.put(0x14, '\u0b94');
        table.put(0x15, '\u0b95');
        table.put(0x19, '\u0b99');
        table.put(0x1a, '\u0b9a');
        table.put(0x1d, '\u0b9c');
        table.put(0x1f, '\u0b9e');

        table.put(0x22, '\u0b9f');
        table.put(0x26, '\u0ba3');
        table.put(0x27, '\u0ba4');
        table.put(0x2f, '\u0ba8');

        table.put(0x3c, '\u0ba9');
        table.put(0x3d, '\u0baa');

        table.put(0x42, '\u0bae');
        table.put(0x43, '\u0baf');
        table.put(0x44, '\u0bb0');
        table.put(0x45, '\u0bb1');
        table.put(0x46, '\u0bb2');
        table.put(0x47, '\u0bb3');
        table.put(0x48, '\u0bb4');
        table.put(0x49, '\u0bb5');
        table.put(0x4a, '\u0bb6');
        table.put(0x4b, '\u0bb7');
        table.put(0x4c, '\u0bb8');
        table.put(0x4d, '\u0bb9');

        table.put(0x50, '\u0bbe');
        table.put(0x51, '\u0bbf');
        table.put(0x52, '\u0bc0');
        table.put(0x53, '\u0bc1');
        table.put(0x54, '\u0bc2');
        table.put(0x58, '\u0bc6');
        table.put(0x59, '\u0bc7');
        table.put(0x5a, '\u0bc8');
        table.put(0x5c, '\u0bca');
        table.put(0x5d, '\u0bcb');
        table.put(0x5e, '\u0bcc');
        table.put(0x5f, '\u0bcd');

        table.put(0x60, '\u0bd0');

        table.put(0x7b, '\u0bd7');
        table.put(0x7c, '\u0bf0');
        table.put(0x7d, '\u0bf1');
        table.put(0x7e, '\u0bf2');
        table.put(0x7f, '\u0bf9');

        // Create the locking shift table for telugu national language
        table = copyTable(gsmToCharTables.get(GsmLanguage.DEFAULT));
        gsmToCharTables.put(GsmLanguage.TELUGU, table);
        table.put(0x00, '\u0c01');
        table.put(0x01, '\u0c02');
        table.put(0x02, '\u0c03');
        table.put(0x03, '\u0c05');
        table.put(0x04, '\u0c06');
        table.put(0x05, '\u0c07');
        table.put(0x06, '\u0c08');
        table.put(0x07, '\u0c09');
        table.put(0x08, '\u0c0a');
        table.put(0x09, '\u0c0b');
        table.put(0x0b, '\u0c0c');
        table.put(0x0e, '\u0c0e');
        table.put(0x0f, '\u0c0f');

        table.put(0x10, '\u0c10');
        table.put(0x12, '\u0c12');
        table.put(0x13, '\u0c13');
        table.put(0x14, '\u0c14');
        table.put(0x15, '\u0c15');
        table.put(0x16, '\u0c16');
        table.put(0x17, '\u0c17');
        table.put(0x18, '\u0c18');
        table.put(0x19, '\u0c19');
        table.put(0x1a, '\u0c1a');
        table.put(0x1c, '\u0c1b');
        table.put(0x1d, '\u0c1c');
        table.put(0x1e, '\u0c1d');
        table.put(0x1f, '\u0c1e');

        table.put(0x22, '\u0c1f');
        table.put(0x23, '\u0c20');
        table.put(0x24, '\u0c21');
        table.put(0x25, '\u0c22');
        table.put(0x26, '\u0c23');
        table.put(0x27, '\u0c24');
        table.put(0x2a, '\u0c25');
        table.put(0x2b, '\u0c26');
        table.put(0x2d, '\u0c27');
        table.put(0x2f, '\u0c28');

        table.put(0x3d, '\u0c2a');
        table.put(0x3e, '\u0c2b');

        table.put(0x40, '\u0c2c');
        table.put(0x41, '\u0c2d');
        table.put(0x42, '\u0c2e');
        table.put(0x43, '\u0c2f');
        table.put(0x44, '\u0c30');
        table.put(0x45, '\u0d31');
        table.put(0x46, '\u0c32');
        table.put(0x47, '\u0c33');
        table.put(0x49, '\u0c35');
        table.put(0x4a, '\u0c36');
        table.put(0x4b, '\u0c37');
        table.put(0x4c, '\u0c38');
        table.put(0x4d, '\u0c39');
        table.put(0x4f, '\u0c3d');

        table.put(0x50, '\u0c3e');
        table.put(0x51, '\u0c3f');
        table.put(0x52, '\u0c40');
        table.put(0x53, '\u0c41');
        table.put(0x54, '\u0c42');
        table.put(0x55, '\u0c43');
        table.put(0x56, '\u0c44');
        table.put(0x58, '\u0c46');
        table.put(0x59, '\u0c47');
        table.put(0x5a, '\u0c48');
        table.put(0x5c, '\u0c4a');
        table.put(0x5d, '\u0c4b');
        table.put(0x5e, '\u0c4c');
        table.put(0x5f, '\u0c4d');

        table.put(0x60, '\u0c55');

        table.put(0x7b, '\u0c56');
        table.put(0x7c, '\u0c60');
        table.put(0x7d, '\u0c61');
        table.put(0x7e, '\u0c62');
        table.put(0x7f, '\u0c63');

        // Create the locking shift table for urdu national language
        table = copyTable(gsmToCharTables.get(GsmLanguage.DEFAULT));
        gsmToCharTables.put(GsmLanguage.URDU, table);
        table.put(0x00, '\u0627');
        table.put(0x01, '\u0622');
        table.put(0x02, '\u0628');
        table.put(0x03, '\u067b');
        table.put(0x04, '\u0680');
        table.put(0x05, '\u067e');
        table.put(0x06, '\u06a6');
        table.put(0x07, '\u062a');
        table.put(0x08, '\u06c2');
        table.put(0x09, '\u067f');
        table.put(0x0b, '\u0679');
        table.put(0x0c, '\u067d');
        table.put(0x0e, '\u067a');
        table.put(0x0f, '\u067c');

        table.put(0x10, '\u062b');
        table.put(0x11, '\u062c');
        table.put(0x12, '\u0681');
        table.put(0x13, '\u0684');
        table.put(0x14, '\u0683');
        table.put(0x15, '\u0685');
        table.put(0x16, '\u0686');
        table.put(0x17, '\u0687');
        table.put(0x18, '\u062d');
        table.put(0x19, '\u062e');
        table.put(0x1a, '\u062f');
        table.put(0x1c, '\u068c');
        table.put(0x1d, '\u0688');
        table.put(0x1e, '\u0689');
        table.put(0x1f, '\u068a');

        table.put(0x22, '\u068f');
        table.put(0x23, '\u068d');
        table.put(0x24, '\u0630');
        table.put(0x25, '\u0631');
        table.put(0x26, '\u0691');
        table.put(0x27, '\u0693');
        table.put(0x2a, '\u0699');
        table.put(0x2b, '\u0632');
        table.put(0x2d, '\u0696');
        table.put(0x2f, '\u0698');

        table.put(0x3c, '\u069a');
        table.put(0x3d, '\u0633');
        table.put(0x3e, '\u0634');

        table.put(0x40, '\u0635');
        table.put(0x41, '\u0636');
        table.put(0x42, '\u0637');
        table.put(0x43, '\u0638');
        table.put(0x44, '\u0639');
        table.put(0x45, '\u0641');
        table.put(0x46, '\u0642');
        table.put(0x47, '\u06a9');
        table.put(0x48, '\u06aa');
        table.put(0x49, '\u06ab');
        table.put(0x4a, '\u06af');
        table.put(0x4b, '\u06b3');
        table.put(0x4c, '\u06b1');
        table.put(0x4d, '\u0644');
        table.put(0x4e, '\u0645');
        table.put(0x4f, '\u0646');

        table.put(0x50, '\u06ba');
        table.put(0x51, '\u06bb');
        table.put(0x52, '\u06bc');
        table.put(0x53, '\u0648');
        table.put(0x54, '\u06c4');
        table.put(0x55, '\u06d5');
        table.put(0x56, '\u06c1');
        table.put(0x57, '\u06be');
        table.put(0x58, '\u0621');
        table.put(0x59, '\u06cc');
        table.put(0x5a, '\u06d0');
        table.put(0x5b, '\u06d2');
        table.put(0x5c, '\u064d');
        table.put(0x5d, '\u0650');
        table.put(0x5e, '\u064f');
        table.put(0x5f, '\u0657');

        table.put(0x60, '\u0654');

        table.put(0x7b, '\u0655');
        table.put(0x7c, '\u0651');
        table.put(0x7d, '\u0653');
        table.put(0x7e, '\u0656');
        table.put(0x7f, '\u0670');

        // Create the table for the extended GSM SMS 7-bit alphabet
        table = new SparseIntArray();
        gsmExtendedToCharTables.put(GsmLanguage.DEFAULT, table);
        table.put(10, '\f');
        table.put(20, '^');
        table.put(40, '{');
        table.put(41, '}');
        table.put(47, '\\');
        table.put(60, '[');
        table.put(61, '~');
        table.put(62, ']');
        table.put(64, '|');
        table.put(101, '\u20ac');

        // Create the single shift table for the turkish national language table
        table = copyTable(gsmExtendedToCharTables.get(GsmLanguage.DEFAULT));
        gsmExtendedToCharTables.put(GsmLanguage.TURKISH, table);
        table.put(0x47, '\u011e');
        table.put(0x49, '\u0130');
        table.put(0x53, '\u015e');
        table.put(0x63, '\u00e7');
        table.put(0x67, '\u011f');
        table.put(0x69, '\u0131');
        table.put(0x73, '\u015f');

        // Create the single shift table for the spanish national language table
        table = copyTable(gsmExtendedToCharTables.get(GsmLanguage.DEFAULT));
        gsmExtendedToCharTables.put(GsmLanguage.SPANISH, table);
        table.put(0x09, '\u00e7');
        table.put(0x41, '\u00c1');
        table.put(0x49, '\u00cd');
        table.put(0x4f, '\u00d3');
        table.put(0x55, '\u00da');
        table.put(0x61, '\u00e1');
        table.put(0x69, '\u00ed');
        table.put(0x6f, '\u00f3');
        table.put(0x75, '\u00fa');

        // Create the single shift table for the portuguese national language
        // table
        table = copyTable(gsmExtendedToCharTables.get(GsmLanguage.DEFAULT));
        gsmExtendedToCharTables.put(GsmLanguage.PORTUGUESE, table);
        table.put(0x05, '\u00ea');
        table.put(0x09, '\u00e7');
        table.put(0x0b, '\u00d4');
        table.put(0x0c, '\u00f4');
        table.put(0x0e, '\u00c1');
        table.put(0x0f, '\u00e1');

        table.put(0x12, '\u03a6');
        table.put(0x13, '\u0393');
        table.put(0x15, '\u03a9');
        table.put(0x16, '\u03a0');
        table.put(0x17, '\u03a8');
        table.put(0x18, '\u03a3');
        table.put(0x19, '\u0398');
        table.put(0x1f, '\u00ca');

        table.put(0x41, '\u00c0');
        table.put(0x49, '\u00cd');
        table.put(0x4f, '\u00d3');

        table.put(0x55, '\u00da');
        table.put(0x5b, '\u00c3');
        table.put(0x5c, '\u00d5');
        table.put(0x5c, '\u00d5');

        table.put(0x61, '\u00c2');
        table.put(0x69, '\u00ed');
        table.put(0x6f, '\u00f3');

        table.put(0x75, '\u00fa');
        table.put(0x7b, '\u00e3');
        table.put(0x7c, '\u00f5');
        table.put(0x7f, '\u00e2');

        // Create the single shift table for the bengali national language table
        table = copyTable(gsmExtendedToCharTables.get(GsmLanguage.DEFAULT));
        gsmExtendedToCharTables.put(GsmLanguage.BENGALI, table);
        table.put(0x00, '\u0040');
        table.put(0x01, '\u00a3');
        table.put(0x02, '\u0024');
        table.put(0x03, '\u00a5');
        table.put(0x04, '\u00bf');
        table.put(0x05, '\u0022');
        table.put(0x06, '\u00a4');
        table.put(0x07, '\u0025');
        table.put(0x08, '\u0026');
        table.put(0x09, '\'');
        table.put(0x0b, '\u002a');
        table.put(0x0c, '\u002b');
        table.put(0x0e, '\u002d');
        table.put(0x0f, '\u002f');

        table.put(0x10, '\u003c');
        table.put(0x11, '\u003d');
        table.put(0x12, '\u003e');
        table.put(0x13, '\u00a1');
        table.put(0x15, '\u00a1');
        table.put(0x16, '\u005f');
        table.put(0x17, '\u0023');
        table.put(0x18, '\u002a');
        table.put(0x19, '\u09e6');
        table.put(0x1a, '\u09e7');
        table.put(0x1c, '\u09e8');
        table.put(0x1d, '\u09e9');
        table.put(0x1e, '\u09ea');
        table.put(0x1f, '\u09eb');

        table.put(0x20, '\u09ec');
        table.put(0x21, '\u09ed');
        table.put(0x22, '\u09ee');
        table.put(0x23, '\u09ef');
        table.put(0x24, '\u09df');
        table.put(0x25, '\u09e0');
        table.put(0x26, '\u09e1');
        table.put(0x27, '\u09e2');
        table.put(0x2a, '\u09e3');
        table.put(0x2b, '\u09f2');
        table.put(0x2c, '\u09f3');
        table.put(0x2d, '\u09f4');
        table.put(0x2e, '\u09f5');

        table.put(0x30, '\u09f6');
        table.put(0x31, '\u09f7');
        table.put(0x32, '\u09f8');
        table.put(0x33, '\u09f9');
        table.put(0x34, '\u09fa');

        table.put(0x41, '\u0041');
        table.put(0x42, '\u0042');
        table.put(0x43, '\u0043');
        table.put(0x44, '\u0044');
        table.put(0x45, '\u0045');
        table.put(0x46, '\u0046');
        table.put(0x47, '\u0047');
        table.put(0x48, '\u0048');
        table.put(0x49, '\u0049');
        table.put(0x4a, '\u004a');
        table.put(0x4b, '\u004b');
        table.put(0x4c, '\u004c');
        table.put(0x4d, '\u004d');
        table.put(0x4e, '\u004e');
        table.put(0x4f, '\u004f');

        table.put(0x50, '\u0050');
        table.put(0x51, '\u0051');
        table.put(0x52, '\u0052');
        table.put(0x53, '\u0053');
        table.put(0x54, '\u0054');
        table.put(0x55, '\u0055');
        table.put(0x56, '\u0056');
        table.put(0x57, '\u0057');
        table.put(0x58, '\u0058');
        table.put(0x59, '\u0059');
        table.put(0x5a, '\u005a');

        // Create the single shift table for the gujarati national language
        // table
        table = copyTable(gsmExtendedToCharTables.get(GsmLanguage.DEFAULT));
        gsmExtendedToCharTables.put(GsmLanguage.GUJARATI, table);
        table.put(0x00, '\u0040');
        table.put(0x01, '\u00a3');
        table.put(0x02, '\u0024');
        table.put(0x03, '\u00a5');
        table.put(0x04, '\u00bf');
        table.put(0x05, '\u0022');
        table.put(0x06, '\u00a4');
        table.put(0x07, '\u0025');
        table.put(0x08, '\u0026');
        table.put(0x09, '\'');
        table.put(0x0b, '\u002a');
        table.put(0x0c, '\u002b');
        table.put(0x0e, '\u002d');
        table.put(0x0f, '\u002f');

        table.put(0x10, '\u003c');
        table.put(0x11, '\u003d');
        table.put(0x12, '\u003e');
        table.put(0x13, '\u00a1');
        table.put(0x15, '\u00a1');
        table.put(0x16, '\u005f');
        table.put(0x17, '\u0023');
        table.put(0x18, '\u002a');
        table.put(0x19, '\u0964');
        table.put(0x1a, '\u0965');
        table.put(0x1c, '\u0ae6');
        table.put(0x1d, '\u0ae7');
        table.put(0x1e, '\u0ae8');
        table.put(0x1f, '\u0ae9');

        table.put(0x20, '\u0aea');
        table.put(0x21, '\u0aeb');
        table.put(0x22, '\u0aec');
        table.put(0x23, '\u0aed');
        table.put(0x24, '\u0aee');
        table.put(0x25, '\u0aef');

        table.put(0x41, '\u0041');
        table.put(0x42, '\u0042');
        table.put(0x43, '\u0043');
        table.put(0x44, '\u0044');
        table.put(0x45, '\u0045');
        table.put(0x46, '\u0046');
        table.put(0x47, '\u0047');
        table.put(0x48, '\u0048');
        table.put(0x49, '\u0049');
        table.put(0x4a, '\u004a');
        table.put(0x4b, '\u004b');
        table.put(0x4c, '\u004c');
        table.put(0x4d, '\u004d');
        table.put(0x4e, '\u004e');
        table.put(0x4f, '\u004f');

        table.put(0x50, '\u0050');
        table.put(0x51, '\u0051');
        table.put(0x52, '\u0052');
        table.put(0x53, '\u0053');
        table.put(0x54, '\u0054');
        table.put(0x55, '\u0055');
        table.put(0x56, '\u0056');
        table.put(0x57, '\u0057');
        table.put(0x58, '\u0058');
        table.put(0x59, '\u0059');
        table.put(0x5a, '\u005a');

        // Create the single shift table for the hindi national language table
        table = copyTable(gsmExtendedToCharTables.get(GsmLanguage.DEFAULT));
        gsmExtendedToCharTables.put(GsmLanguage.HINDI, table);
        table.put(0x00, '\u0040');
        table.put(0x01, '\u00a3');
        table.put(0x02, '\u0024');
        table.put(0x03, '\u00a5');
        table.put(0x04, '\u00bf');
        table.put(0x05, '\u0022');
        table.put(0x06, '\u00a4');
        table.put(0x07, '\u0025');
        table.put(0x08, '\u0026');
        table.put(0x09, '\'');
        table.put(0x0b, '\u002a');
        table.put(0x0c, '\u002b');
        table.put(0x0e, '\u002d');
        table.put(0x0f, '\u002f');

        table.put(0x10, '\u003c');
        table.put(0x11, '\u003d');
        table.put(0x12, '\u003e');
        table.put(0x13, '\u00a1');
        table.put(0x15, '\u00a1');
        table.put(0x16, '\u005f');
        table.put(0x17, '\u0023');
        table.put(0x18, '\u002a');
        table.put(0x19, '\u0964');
        table.put(0x1a, '\u0965');
        table.put(0x1c, '\u0966');
        table.put(0x1d, '\u0967');
        table.put(0x1e, '\u0968');
        table.put(0x1f, '\u0969');

        table.put(0x20, '\u096a');
        table.put(0x21, '\u096b');
        table.put(0x22, '\u096c');
        table.put(0x23, '\u096d');
        table.put(0x24, '\u096e');
        table.put(0x25, '\u096f');
        table.put(0x26, '\u0951');
        table.put(0x27, '\u0952');
        table.put(0x2a, '\u0953');
        table.put(0x2b, '\u0954');
        table.put(0x2c, '\u0958');
        table.put(0x2d, '\u0959');
        table.put(0x2e, '\u095a');

        table.put(0x30, '\u095b');
        table.put(0x31, '\u095c');
        table.put(0x32, '\u095d');
        table.put(0x33, '\u095e');
        table.put(0x34, '\u095f');
        table.put(0x35, '\u0960');
        table.put(0x36, '\u0961');
        table.put(0x37, '\u0962');
        table.put(0x38, '\u0963');
        table.put(0x39, '\u0970');
        table.put(0x3a, '\u0971');

        table.put(0x41, '\u0041');
        table.put(0x42, '\u0042');
        table.put(0x43, '\u0043');
        table.put(0x44, '\u0044');
        table.put(0x45, '\u0045');
        table.put(0x46, '\u0046');
        table.put(0x47, '\u0047');
        table.put(0x48, '\u0048');
        table.put(0x49, '\u0049');
        table.put(0x4a, '\u004a');
        table.put(0x4b, '\u004b');
        table.put(0x4c, '\u004c');
        table.put(0x4d, '\u004d');
        table.put(0x4e, '\u004e');
        table.put(0x4f, '\u004f');

        table.put(0x50, '\u0050');
        table.put(0x51, '\u0051');
        table.put(0x52, '\u0052');
        table.put(0x53, '\u0053');
        table.put(0x54, '\u0054');
        table.put(0x55, '\u0055');
        table.put(0x56, '\u0056');
        table.put(0x57, '\u0057');
        table.put(0x58, '\u0058');
        table.put(0x59, '\u0059');
        table.put(0x5a, '\u005a');

        // Create the single shift table for the kannada national language table
        table = copyTable(gsmExtendedToCharTables.get(GsmLanguage.DEFAULT));
        gsmExtendedToCharTables.put(GsmLanguage.KANNADA, table);
        table.put(0x00, '\u0040');
        table.put(0x01, '\u00a3');
        table.put(0x02, '\u0024');
        table.put(0x03, '\u00a5');
        table.put(0x04, '\u00bf');
        table.put(0x05, '\u0022');
        table.put(0x06, '\u00a4');
        table.put(0x07, '\u0025');
        table.put(0x08, '\u0026');
        table.put(0x09, '\'');
        table.put(0x0b, '\u002a');
        table.put(0x0c, '\u002b');
        table.put(0x0e, '\u002d');
        table.put(0x0f, '\u002f');

        table.put(0x10, '\u003c');
        table.put(0x11, '\u003d');
        table.put(0x12, '\u003e');
        table.put(0x13, '\u00a1');
        table.put(0x15, '\u00a1');
        table.put(0x16, '\u005f');
        table.put(0x17, '\u0023');
        table.put(0x18, '\u002a');
        table.put(0x19, '\u0964');
        table.put(0x1a, '\u0965');
        table.put(0x1c, '\u0ce6');
        table.put(0x1d, '\u0ce7');
        table.put(0x1e, '\u0ce8');
        table.put(0x1f, '\u0ce9');

        table.put(0x20, '\u0cea');
        table.put(0x21, '\u0ceb');
        table.put(0x22, '\u0cec');
        table.put(0x23, '\u0ced');
        table.put(0x24, '\u0cee');
        table.put(0x25, '\u0cef');
        table.put(0x26, '\u0cde');
        table.put(0x27, '\u0cf1');
        table.put(0x2a, '\u0cf2');

        table.put(0x41, '\u0041');
        table.put(0x42, '\u0042');
        table.put(0x43, '\u0043');
        table.put(0x44, '\u0044');
        table.put(0x45, '\u0045');
        table.put(0x46, '\u0046');
        table.put(0x47, '\u0047');
        table.put(0x48, '\u0048');
        table.put(0x49, '\u0049');
        table.put(0x4a, '\u004a');
        table.put(0x4b, '\u004b');
        table.put(0x4c, '\u004c');
        table.put(0x4d, '\u004d');
        table.put(0x4e, '\u004e');
        table.put(0x4f, '\u004f');

        table.put(0x50, '\u0050');
        table.put(0x51, '\u0051');
        table.put(0x52, '\u0052');
        table.put(0x53, '\u0053');
        table.put(0x54, '\u0054');
        table.put(0x55, '\u0055');
        table.put(0x56, '\u0056');
        table.put(0x57, '\u0057');
        table.put(0x58, '\u0058');
        table.put(0x59, '\u0059');
        table.put(0x5a, '\u005a');

        // Create the single shift table for the malayalam national language
        // table
        table = copyTable(gsmExtendedToCharTables.get(GsmLanguage.DEFAULT));
        gsmExtendedToCharTables.put(GsmLanguage.MALAYALAM, table);
        table.put(0x00, '\u0040');
        table.put(0x01, '\u00a3');
        table.put(0x02, '\u0024');
        table.put(0x03, '\u00a5');
        table.put(0x04, '\u00bf');
        table.put(0x05, '\u0022');
        table.put(0x06, '\u00a4');
        table.put(0x07, '\u0025');
        table.put(0x08, '\u0026');
        table.put(0x09, '\'');
        table.put(0x0b, '\u002a');
        table.put(0x0c, '\u002b');
        table.put(0x0e, '\u002d');
        table.put(0x0f, '\u002f');

        table.put(0x10, '\u003c');
        table.put(0x11, '\u003d');
        table.put(0x12, '\u003e');
        table.put(0x13, '\u00a1');
        table.put(0x15, '\u00a1');
        table.put(0x16, '\u005f');
        table.put(0x17, '\u0023');
        table.put(0x18, '\u002a');
        table.put(0x19, '\u0964');
        table.put(0x1a, '\u0965');
        table.put(0x1c, '\u0d66');
        table.put(0x1d, '\u0d67');
        table.put(0x1e, '\u0d68');
        table.put(0x1f, '\u0d69');

        table.put(0x20, '\u0d6a');
        table.put(0x21, '\u0d6b');
        table.put(0x22, '\u0d6c');
        table.put(0x23, '\u0d6d');
        table.put(0x24, '\u0d6e');
        table.put(0x25, '\u0d6f');
        table.put(0x26, '\u0d70');
        table.put(0x27, '\u0d71');
        table.put(0x2a, '\u0d72');
        table.put(0x2b, '\u0d73');
        table.put(0x2c, '\u0d74');
        table.put(0x2d, '\u0d75');
        table.put(0x2e, '\u0d7a');

        table.put(0x30, '\u0d7b');
        table.put(0x31, '\u0d7c');
        table.put(0x32, '\u0d7d');
        table.put(0x33, '\u0d7e');
        table.put(0x34, '\u0d7f');

        table.put(0x41, '\u0041');
        table.put(0x42, '\u0042');
        table.put(0x43, '\u0043');
        table.put(0x44, '\u0044');
        table.put(0x45, '\u0045');
        table.put(0x46, '\u0046');
        table.put(0x47, '\u0047');
        table.put(0x48, '\u0048');
        table.put(0x49, '\u0049');
        table.put(0x4a, '\u004a');
        table.put(0x4b, '\u004b');
        table.put(0x4c, '\u004c');
        table.put(0x4d, '\u004d');
        table.put(0x4e, '\u004e');
        table.put(0x4f, '\u004f');

        table.put(0x50, '\u0050');
        table.put(0x51, '\u0051');
        table.put(0x52, '\u0052');
        table.put(0x53, '\u0053');
        table.put(0x54, '\u0054');
        table.put(0x55, '\u0055');
        table.put(0x56, '\u0056');
        table.put(0x57, '\u0057');
        table.put(0x58, '\u0058');
        table.put(0x59, '\u0059');
        table.put(0x5a, '\u005a');

        // Create the single shift table for the oriya national language table
        table = copyTable(gsmExtendedToCharTables.get(GsmLanguage.DEFAULT));
        gsmExtendedToCharTables.put(GsmLanguage.ORIYA, table);
        table.put(0x00, '\u0040');
        table.put(0x01, '\u00a3');
        table.put(0x02, '\u0024');
        table.put(0x03, '\u00a5');
        table.put(0x04, '\u00bf');
        table.put(0x05, '\u0022');
        table.put(0x06, '\u00a4');
        table.put(0x07, '\u0025');
        table.put(0x08, '\u0026');
        table.put(0x09, '\'');
        table.put(0x0b, '\u002a');
        table.put(0x0c, '\u002b');
        table.put(0x0e, '\u002d');
        table.put(0x0f, '\u002f');

        table.put(0x10, '\u003c');
        table.put(0x11, '\u003d');
        table.put(0x12, '\u003e');
        table.put(0x13, '\u00a1');
        table.put(0x15, '\u00a1');
        table.put(0x16, '\u005f');
        table.put(0x17, '\u0023');
        table.put(0x18, '\u002a');
        table.put(0x19, '\u0964');
        table.put(0x1a, '\u0965');
        table.put(0x1c, '\u0b66');
        table.put(0x1d, '\u0b67');
        table.put(0x1e, '\u0b68');
        table.put(0x1f, '\u0b69');

        table.put(0x20, '\u0b6a');
        table.put(0x21, '\u0b6b');
        table.put(0x22, '\u0b6c');
        table.put(0x23, '\u0b6d');
        table.put(0x24, '\u0b6e');
        table.put(0x25, '\u0b6f');
        table.put(0x26, '\u0b5c');
        table.put(0x27, '\u0b5d');
        table.put(0x2a, '\u0b5f');
        table.put(0x2b, '\u0b70');
        table.put(0x2c, '\u0b71');

        table.put(0x41, '\u0041');
        table.put(0x42, '\u0042');
        table.put(0x43, '\u0043');
        table.put(0x44, '\u0044');
        table.put(0x45, '\u0045');
        table.put(0x46, '\u0046');
        table.put(0x47, '\u0047');
        table.put(0x48, '\u0048');
        table.put(0x49, '\u0049');
        table.put(0x4a, '\u004a');
        table.put(0x4b, '\u004b');
        table.put(0x4c, '\u004c');
        table.put(0x4d, '\u004d');
        table.put(0x4e, '\u004e');
        table.put(0x4f, '\u004f');

        table.put(0x50, '\u0050');
        table.put(0x51, '\u0051');
        table.put(0x52, '\u0052');
        table.put(0x53, '\u0053');
        table.put(0x54, '\u0054');
        table.put(0x55, '\u0055');
        table.put(0x56, '\u0056');
        table.put(0x57, '\u0057');
        table.put(0x58, '\u0058');
        table.put(0x59, '\u0059');
        table.put(0x5a, '\u005a');

        // Create the single shift table for the punjabi national language table
        table = copyTable(gsmExtendedToCharTables.get(GsmLanguage.DEFAULT));
        gsmExtendedToCharTables.put(GsmLanguage.PUNJABI, table);
        table.put(0x00, '\u0040');
        table.put(0x01, '\u00a3');
        table.put(0x02, '\u0024');
        table.put(0x03, '\u00a5');
        table.put(0x04, '\u00bf');
        table.put(0x05, '\u0022');
        table.put(0x06, '\u00a4');
        table.put(0x07, '\u0025');
        table.put(0x08, '\u0026');
        table.put(0x09, '\'');
        table.put(0x0b, '\u002a');
        table.put(0x0c, '\u002b');
        table.put(0x0e, '\u002d');
        table.put(0x0f, '\u002f');

        table.put(0x10, '\u003c');
        table.put(0x11, '\u003d');
        table.put(0x12, '\u003e');
        table.put(0x13, '\u00a1');
        table.put(0x15, '\u00a1');
        table.put(0x16, '\u005f');
        table.put(0x17, '\u0023');
        table.put(0x18, '\u002a');
        table.put(0x19, '\u0964');
        table.put(0x1a, '\u0965');
        table.put(0x1c, '\u0a66');
        table.put(0x1d, '\u0a67');
        table.put(0x1e, '\u0a68');
        table.put(0x1f, '\u0a69');

        table.put(0x20, '\u0a6a');
        table.put(0x21, '\u0a6b');
        table.put(0x22, '\u0a6c');
        table.put(0x23, '\u0a6d');
        table.put(0x24, '\u0a6e');
        table.put(0x25, '\u0a6f');
        table.put(0x26, '\u0a59');
        table.put(0x27, '\u0a5a');
        table.put(0x2a, '\u0a5b');
        table.put(0x2b, '\u0a5c');
        table.put(0x2c, '\u0a5e');
        table.put(0x2d, '\u0a75');

        table.put(0x41, '\u0041');
        table.put(0x42, '\u0042');
        table.put(0x43, '\u0043');
        table.put(0x44, '\u0044');
        table.put(0x45, '\u0045');
        table.put(0x46, '\u0046');
        table.put(0x47, '\u0047');
        table.put(0x48, '\u0048');
        table.put(0x49, '\u0049');
        table.put(0x4a, '\u004a');
        table.put(0x4b, '\u004b');
        table.put(0x4c, '\u004c');
        table.put(0x4d, '\u004d');
        table.put(0x4e, '\u004e');
        table.put(0x4f, '\u004f');

        table.put(0x50, '\u0050');
        table.put(0x51, '\u0051');
        table.put(0x52, '\u0052');
        table.put(0x53, '\u0053');
        table.put(0x54, '\u0054');
        table.put(0x55, '\u0055');
        table.put(0x56, '\u0056');
        table.put(0x57, '\u0057');
        table.put(0x58, '\u0058');
        table.put(0x59, '\u0059');
        table.put(0x5a, '\u005a');

        // Create the single shift table for the tamil national language table
        table = copyTable(gsmExtendedToCharTables.get(GsmLanguage.DEFAULT));
        gsmExtendedToCharTables.put(GsmLanguage.TAMIL, table);
        table.put(0x00, '\u0040');
        table.put(0x01, '\u00a3');
        table.put(0x02, '\u0024');
        table.put(0x03, '\u00a5');
        table.put(0x04, '\u00bf');
        table.put(0x05, '\u0022');
        table.put(0x06, '\u00a4');
        table.put(0x07, '\u0025');
        table.put(0x08, '\u0026');
        table.put(0x09, '\'');
        table.put(0x0b, '\u002a');
        table.put(0x0c, '\u002b');
        table.put(0x0e, '\u002d');
        table.put(0x0f, '\u002f');

        table.put(0x10, '\u003c');
        table.put(0x11, '\u003d');
        table.put(0x12, '\u003e');
        table.put(0x13, '\u00a1');
        table.put(0x15, '\u00a1');
        table.put(0x16, '\u005f');
        table.put(0x17, '\u0023');
        table.put(0x18, '\u002a');
        table.put(0x19, '\u0964');
        table.put(0x1a, '\u0965');
        table.put(0x1c, '\u0be6');
        table.put(0x1d, '\u0be7');
        table.put(0x1e, '\u0be8');
        table.put(0x1f, '\u0be9');

        table.put(0x20, '\u0bea');
        table.put(0x21, '\u0beb');
        table.put(0x22, '\u0bec');
        table.put(0x23, '\u0bed');
        table.put(0x24, '\u0bee');
        table.put(0x25, '\u0bef');
        table.put(0x26, '\u0bf3');
        table.put(0x27, '\u0bf4');
        table.put(0x2a, '\u0bf5');
        table.put(0x2b, '\u0bf6');
        table.put(0x2c, '\u0bf7');
        table.put(0x2d, '\u0bf8');
        table.put(0x2e, '\u0bfa');

        table.put(0x41, '\u0041');
        table.put(0x42, '\u0042');
        table.put(0x43, '\u0043');
        table.put(0x44, '\u0044');
        table.put(0x45, '\u0045');
        table.put(0x46, '\u0046');
        table.put(0x47, '\u0047');
        table.put(0x48, '\u0048');
        table.put(0x49, '\u0049');
        table.put(0x4a, '\u004a');
        table.put(0x4b, '\u004b');
        table.put(0x4c, '\u004c');
        table.put(0x4d, '\u004d');
        table.put(0x4e, '\u004e');
        table.put(0x4f, '\u004f');

        table.put(0x50, '\u0050');
        table.put(0x51, '\u0051');
        table.put(0x52, '\u0052');
        table.put(0x53, '\u0053');
        table.put(0x54, '\u0054');
        table.put(0x55, '\u0055');
        table.put(0x56, '\u0056');
        table.put(0x57, '\u0057');
        table.put(0x58, '\u0058');
        table.put(0x59, '\u0059');
        table.put(0x5a, '\u005a');

        // Create the single shift table for the telugu national language table
        table = copyTable(gsmExtendedToCharTables.get(GsmLanguage.DEFAULT));
        gsmExtendedToCharTables.put(GsmLanguage.TELUGU, table);
        table.put(0x00, '\u0040');
        table.put(0x01, '\u00a3');
        table.put(0x02, '\u0024');
        table.put(0x03, '\u00a5');
        table.put(0x04, '\u00bf');
        table.put(0x05, '\u0022');
        table.put(0x06, '\u00a4');
        table.put(0x07, '\u0025');
        table.put(0x08, '\u0026');
        table.put(0x09, '\'');
        table.put(0x0b, '\u002a');
        table.put(0x0c, '\u002b');
        table.put(0x0e, '\u002d');
        table.put(0x0f, '\u002f');

        table.put(0x10, '\u003c');
        table.put(0x11, '\u003d');
        table.put(0x12, '\u003e');
        table.put(0x13, '\u00a1');
        table.put(0x15, '\u00a1');
        table.put(0x16, '\u005f');
        table.put(0x17, '\u0023');
        table.put(0x18, '\u002a');
        table.put(0x1c, '\u0c66');
        table.put(0x1d, '\u0c67');
        table.put(0x1e, '\u0c68');
        table.put(0x1f, '\u0c69');

        table.put(0x20, '\u0c6a');
        table.put(0x21, '\u0c6b');
        table.put(0x22, '\u06cc');
        table.put(0x23, '\u06cd');
        table.put(0x24, '\u0c6e');
        table.put(0x25, '\u0c6f');
        table.put(0x26, '\u0c58');
        table.put(0x27, '\u0c59');
        table.put(0x2a, '\u0c78');
        table.put(0x2b, '\u0c79');
        table.put(0x2c, '\u0c7a');
        table.put(0x2d, '\u0c7b');
        table.put(0x2e, '\u0c7c');

        table.put(0x30, '\u0c7d');
        table.put(0x31, '\u0c7e');
        table.put(0x32, '\u0c7f');

        table.put(0x41, '\u0041');
        table.put(0x42, '\u0042');
        table.put(0x43, '\u0043');
        table.put(0x44, '\u0044');
        table.put(0x45, '\u0045');
        table.put(0x46, '\u0046');
        table.put(0x47, '\u0047');
        table.put(0x48, '\u0048');
        table.put(0x49, '\u0049');
        table.put(0x4a, '\u004a');
        table.put(0x4b, '\u004b');
        table.put(0x4c, '\u004c');
        table.put(0x4d, '\u004d');
        table.put(0x4e, '\u004e');
        table.put(0x4f, '\u004f');

        table.put(0x50, '\u0050');
        table.put(0x51, '\u0051');
        table.put(0x52, '\u0052');
        table.put(0x53, '\u0053');
        table.put(0x54, '\u0054');
        table.put(0x55, '\u0055');
        table.put(0x56, '\u0056');
        table.put(0x57, '\u0057');
        table.put(0x58, '\u0058');
        table.put(0x59, '\u0059');
        table.put(0x5a, '\u005a');

        // Create the single shift table for the urdu national language table
        table = copyTable(gsmExtendedToCharTables.get(GsmLanguage.DEFAULT));
        gsmExtendedToCharTables.put(GsmLanguage.URDU, table);
        table.put(0x00, '\u0040');
        table.put(0x01, '\u00a3');
        table.put(0x02, '\u0024');
        table.put(0x03, '\u00a5');
        table.put(0x04, '\u00bf');
        table.put(0x05, '\u0022');
        table.put(0x06, '\u00a4');
        table.put(0x07, '\u0025');
        table.put(0x08, '\u0026');
        table.put(0x09, '\'');
        table.put(0x0b, '\u002a');
        table.put(0x0c, '\u002b');
        table.put(0x0e, '\u002d');
        table.put(0x0f, '\u002f');

        table.put(0x10, '\u003c');
        table.put(0x11, '\u003d');
        table.put(0x12, '\u003e');
        table.put(0x13, '\u00a1');
        table.put(0x15, '\u00a1');
        table.put(0x16, '\u005f');
        table.put(0x17, '\u0023');
        table.put(0x18, '\u002a');
        table.put(0x19, '\u0600');
        table.put(0x1a, '\u0601');
        table.put(0x1c, '\u06f0');
        table.put(0x1d, '\u06f1');
        table.put(0x1e, '\u06f2');
        table.put(0x1f, '\u06f3');

        table.put(0x20, '\u06f4');
        table.put(0x21, '\u06f5');
        table.put(0x22, '\u06f6');
        table.put(0x23, '\u06f7');
        table.put(0x24, '\u06f8');
        table.put(0x25, '\u06f9');
        table.put(0x26, '\u060c');
        table.put(0x27, '\u060d');
        table.put(0x2a, '\u060e');
        table.put(0x2b, '\u060f');
        table.put(0x2c, '\u0610');
        table.put(0x2d, '\u0611');
        table.put(0x2e, '\u0612');

        table.put(0x30, '\u0613');
        table.put(0x31, '\u0614');
        table.put(0x32, '\u061b');
        table.put(0x33, '\u061f');
        table.put(0x34, '\u0640');
        table.put(0x35, '\u0652');
        table.put(0x36, '\u0658');
        table.put(0x37, '\u066b');
        table.put(0x38, '\u066c');
        table.put(0x39, '\u0672');
        table.put(0x3a, '\u0673');
        table.put(0x3b, '\u06cd');
        table.put(0x3f, '\u06d4');

        table.put(0x41, '\u0041');
        table.put(0x42, '\u0042');
        table.put(0x43, '\u0043');
        table.put(0x44, '\u0044');
        table.put(0x45, '\u0045');
        table.put(0x46, '\u0046');
        table.put(0x47, '\u0047');
        table.put(0x48, '\u0048');
        table.put(0x49, '\u0049');
        table.put(0x4a, '\u004a');
        table.put(0x4b, '\u004b');
        table.put(0x4c, '\u004c');
        table.put(0x4d, '\u004d');
        table.put(0x4e, '\u004e');
        table.put(0x4f, '\u004f');

        table.put(0x50, '\u0050');
        table.put(0x51, '\u0051');
        table.put(0x52, '\u0052');
        table.put(0x53, '\u0053');
        table.put(0x54, '\u0054');
        table.put(0x55, '\u0055');
        table.put(0x56, '\u0056');
        table.put(0x57, '\u0057');
        table.put(0x58, '\u0058');
        table.put(0x59, '\u0059');
        table.put(0x5a, '\u005a');

        // Create the reverse lookup table for gsm characters
        for (GsmLanguage language : GsmLanguage.values()) {
            table = gsmToCharTables.get(language);
            SparseIntArray revTable = new SparseIntArray();
            charToGsmTables.put(language, revTable);
            int size = table.size();
            for (i = 0; i < size; i++) {
                revTable.put(table.valueAt(i), table.keyAt(i));
            }
        }

        // Create the reverse lookup tables for extended gsm characters
        for (GsmLanguage language : GsmLanguage.values()) {
            table = gsmExtendedToCharTables.get(language);
            SparseIntArray revTable = new SparseIntArray();
            charToGsmExtendedTables.put(language, revTable);
            int size = table.size();
            for (i = 0; i < size; i++) {
                revTable.put(table.valueAt(i), table.keyAt(i));
            }
        }
    }

    private static SparseIntArray copyTable(SparseIntArray src) {
        SparseIntArray dest = new SparseIntArray();
        int size = src.size();
        for (int i = 0; i < size; i++) {
            dest.put(src.keyAt(i), src.valueAt(i));
        }
        return dest;
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index d720516..385dbdc 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.SmsMessageBase.TextEncodingDetails;
import com.android.internal.telephony.gsm.SmsMessage;
import com.android.internal.telephony.gsm.GsmAlphabet.GsmLanguage;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsHeader;
//Synthetic comment -- @@ -162,8 +163,20 @@
/** {@inheritDoc} */
protected void sendText(String destAddr, String scAddr, String text,
PendingIntent sentIntent, PendingIntent deliveryIntent) {

        SmsHeader smsHeader = null;
        GsmLanguage extensionLanguage = GsmAlphabet.getExtensionLanguage(text);

        if (extensionLanguage != null && extensionLanguage != GsmLanguage.DEFAULT) {
            // We will code this with a national single shift table
            smsHeader = new SmsHeader();
            smsHeader.lockingShiftLanguage = GsmLanguage.DEFAULT;
            smsHeader.singleShiftLanguage = extensionLanguage;
        }

SmsMessage.SubmitPdu pdu = SmsMessage.getSubmitPdu(
                scAddr, destAddr, text, (deliveryIntent != null), smsHeader);

sendRawPdu(pdu.encodedScAddress, pdu.encodedMessage, sentIntent, deliveryIntent);
}

//Synthetic comment -- @@ -172,17 +185,25 @@
ArrayList<String> parts, ArrayList<PendingIntent> sentIntents,
ArrayList<PendingIntent> deliveryIntents) {

        SmsHeader smsHeader = new SmsHeader();

int refNumber = getNextConcatenatedRef() & 0x00FF;
int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

        // We want the same encoding of all parts.
        // Check if we can manage with gsm7bit (with extensions) or if we need
        // unicode for this message.
        StringBuilder sb = new StringBuilder();
for (int i = 0; i < msgCount; i++) {
            sb.append(parts.get(i));
        }
        GsmLanguage extensionLanguage = GsmAlphabet.getExtensionLanguage(sb.toString());
        if (extensionLanguage != null) {
            smsHeader.singleShiftLanguage = extensionLanguage;
            encoding = android.telephony.SmsMessage.ENCODING_7BIT;
        } else {
            encoding = android.telephony.SmsMessage.ENCODING_16BIT;
}

for (int i = 0; i < msgCount; i++) {
//Synthetic comment -- @@ -197,7 +218,6 @@
// Note:  It's not sufficient to just flip this bit to true; it will have
// ripple effects (several calculations assume 8-bit ref).
concatRef.isEightBits = true;
smsHeader.concatRef = concatRef;

PendingIntent sentIntent = null;
//Synthetic comment -- @@ -211,8 +231,7 @@
}

SmsMessage.SubmitPdu pdus = SmsMessage.getSubmitPdu(scAddress, destinationAddress,
                    parts.get(i), deliveryIntent != null, smsHeader, encoding);

sendRawPdu(pdus.encodedScAddress, pdus.encodedMessage, sentIntent, deliveryIntent);
}
//Synthetic comment -- @@ -261,17 +280,24 @@
return;
}

        SmsHeader smsHeader = new SmsHeader();

int refNumber = getNextConcatenatedRef() & 0x00FF;
int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

        // We want the same encoding of all parts.
        // Check if we can manage with gsm7bit (with extensions) or if we need
        // unicode for this message.
        StringBuilder sb = new StringBuilder();
for (int i = 0; i < msgCount; i++) {
            sb.append(parts.get(i));
        }
        GsmLanguage extensionLanguage = GsmAlphabet.getExtensionLanguage(sb.toString());
        if (extensionLanguage != null) {
            smsHeader.singleShiftLanguage = extensionLanguage;
        } else {
            encoding = android.telephony.SmsMessage.ENCODING_16BIT;
}

for (int i = 0; i < msgCount; i++) {
//Synthetic comment -- @@ -279,8 +305,7 @@
concatRef.refNumber = refNumber;
concatRef.seqNumber = i + 1;  // 1-based sequence
concatRef.msgCount = msgCount;
            concatRef.isEightBits = true;
smsHeader.concatRef = concatRef;

PendingIntent sentIntent = null;
//Synthetic comment -- @@ -294,8 +319,7 @@
}

SmsMessage.SubmitPdu pdus = SmsMessage.getSubmitPdu(scAddress, destinationAddress,
                    parts.get(i), deliveryIntent != null, smsHeader, encoding);

HashMap<String, Object> map = new HashMap<String, Object>();
map.put("smsc", pdus.encodedScAddress);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 4fd62fb..4d10abb 100644

//Synthetic comment -- @@ -23,10 +23,10 @@
import android.util.Log;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsHeader.ConcatRef;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.gsm.GsmAlphabet.GsmLanguage;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
//Synthetic comment -- @@ -38,7 +38,6 @@
import static android.telephony.SmsMessage.MAX_USER_DATA_BYTES;
import static android.telephony.SmsMessage.MAX_USER_DATA_BYTES_WITH_HEADER;
import static android.telephony.SmsMessage.MAX_USER_DATA_SEPTETS;
import static android.telephony.SmsMessage.MessageClass;

/**
//Synthetic comment -- @@ -232,7 +231,7 @@
*/
public static SubmitPdu getSubmitPdu(String scAddress,
String destinationAddress, String message,
            boolean statusReportRequested, SmsHeader header) {
return getSubmitPdu(scAddress, destinationAddress, message, statusReportRequested, header,
ENCODING_UNKNOWN);
}
//Synthetic comment -- @@ -251,7 +250,7 @@
*/
public static SubmitPdu getSubmitPdu(String scAddress,
String destinationAddress, String message,
            boolean statusReportRequested, SmsHeader header, int encoding) {

// Perform null parameter checks.
if (message == null || destinationAddress == null) {
//Synthetic comment -- @@ -264,6 +263,7 @@
ByteArrayOutputStream bo = getSubmitPduHead(
scAddress, destinationAddress, mtiByte,
statusReportRequested, ret);

// User Data (and length)
byte[] userData;
if (encoding == ENCODING_UNKNOWN) {
//Synthetic comment -- @@ -272,10 +272,18 @@
}
try {
if (encoding == ENCODING_7BIT) {
                GsmLanguage alphabet = GsmLanguage.DEFAULT;
                GsmLanguage extensionTable = GsmLanguage.DEFAULT;
                if (header != null) {
                    alphabet = header.lockingShiftLanguage;
                    extensionTable = header.singleShiftLanguage;
                }

                userData = GsmAlphabet.getAlphabet(alphabet, extensionTable)
                        .stringToGsm7BitPackedWithHeader(message, SmsHeader.toByteArray(header));
} else { //assume UCS-2
try {
                    userData = encodeUCS2(message, SmsHeader.toByteArray(header));
} catch(UnsupportedEncodingException uex) {
Log.e(LOG_TAG,
"Implausible UnsupportedEncodingException ",
//Synthetic comment -- @@ -287,7 +295,7 @@
// Encoding to the 7-bit alphabet failed. Let's see if we can
// send it as a UCS-2 encoded message
try {
                userData = encodeUCS2(message, SmsHeader.toByteArray(header));
encoding = ENCODING_16BIT;
} catch(UnsupportedEncodingException uex) {
Log.e(LOG_TAG,
//Synthetic comment -- @@ -751,9 +759,15 @@
*/
String getUserDataGSM7Bit(int septetCount) {
String ret;
            GsmLanguage alphabet = GsmLanguage.DEFAULT;
            GsmLanguage extensionTable = GsmLanguage.DEFAULT;
            if (userDataHeader != null) {
                alphabet = userDataHeader.lockingShiftLanguage;
                extensionTable = userDataHeader.singleShiftLanguage;
            }

            ret = GsmAlphabet.getAlphabet(alphabet, extensionTable).gsm7BitPackedToString(pdu, cur,
                    septetCount, mUserDataSeptetPadding);

cur += (septetCount * 7) / 8;

//Synthetic comment -- @@ -795,26 +809,57 @@
*/
public static TextEncodingDetails calculateLength(CharSequence msgBody,
boolean use7bitOnly) {

        TextEncodingDetails ted = null;
try {
            ted = new TextEncodingDetails();

            ted.extensionTable = GsmAlphabet.getExtensionLanguage(msgBody);

            if (ted.extensionTable == null) {
                // This is not a very pretty solution, but we use it to stick to
                // the legacy concept of throw-catch-ing into UCS2 coding.
                // Since we now know from the TextEncodingDetails that we need
                // UCS2, refactoring should be considered.
                throw new EncodeException();
            }

            ted.codeUnitCount = GsmAlphabet.getAlphabet(ted.alphabet, ted.extensionTable)
                    .countGsmSeptets(msgBody, !use7bitOnly);

            SmsHeader header = new SmsHeader();
            header.lockingShiftLanguage = ted.alphabet;
            header.singleShiftLanguage = ted.extensionTable;

            ted.headerLength = SmsHeader.getLengthWithUDHL(header);

            int userdataSeptets = (MAX_USER_DATA_BYTES - ted.headerLength) * 8 / 7;

            if (ted.codeUnitCount > userdataSeptets) {
                // This message needs to be concatenated, recalculate
                header.concatRef = new ConcatRef();
                header.concatRef.isEightBits = true;
                ted.headerLength = SmsHeader.getLengthWithUDHL(header);
                userdataSeptets = (MAX_USER_DATA_BYTES - ted.headerLength) * 8 / 7;
                ted.msgCount = (ted.codeUnitCount + userdataSeptets - 1) / userdataSeptets;
                ted.codeUnitsRemaining = (ted.msgCount * userdataSeptets) - ted.codeUnitCount;
} else {
ted.msgCount = 1;
                ted.codeUnitsRemaining = userdataSeptets - ted.codeUnitCount;
}
ted.codeUnitSize = ENCODING_7BIT;
} catch (EncodeException ex) {
            SmsHeader header = new SmsHeader();
            ted = new TextEncodingDetails(); // To reset old values
int octets = msgBody.length() * 2;
ted.codeUnitCount = msgBody.length();
if (octets > MAX_USER_DATA_BYTES) {
                // This message needs to be concatenated, recalculate
                header.concatRef = new ConcatRef();
                header.concatRef.isEightBits = true;
                ted.headerLength = SmsHeader.getLengthWithUDHL(header);
                ted.msgCount = (octets + MAX_USER_DATA_BYTES_WITH_HEADER - 1) / MAX_USER_DATA_BYTES_WITH_HEADER;
                ted.codeUnitsRemaining = ((ted.msgCount * MAX_USER_DATA_BYTES_WITH_HEADER) - octets) / 2;
} else {
ted.msgCount = 1;
ted.codeUnitsRemaining = (MAX_USER_DATA_BYTES - octets)/2;








//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/com/android/internal/telephony/SmsNationalCharactersTest.java b/telephony/tests/telephonytests/src/com/android/internal/telephony/SmsNationalCharactersTest.java
new file mode 100644
//Synthetic comment -- index 0000000..6f0eb8e

//Synthetic comment -- @@ -0,0 +1,355 @@
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

package com.android.unit_tests;

import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.gsm.GsmAlphabet;
import com.android.internal.telephony.gsm.GsmAlphabet.GsmLanguage;
import com.android.internal.util.HexDump;

import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.SmsMessage.SubmitPdu;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import java.util.ArrayList;

public class SmsNationalCharactersTest extends AndroidTestCase {

    private static final String gsm7bit_default_short =
        "0123456789";

    private static final String gsm7bit_default_160_chars =
        "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
        + "0123456789012345678901234567890123456789012345678901234567890123456789";

    private static final String ucs2_short =
        "\ubabe123456789";

    private static final String ucs2_70_chars =
        "\ubabe123456789012345678901234567890123456789012345678901234567890123456789";

    private static final String gsm7bit_turkish_short =
        "\u011e\u0130\u015e\u00e7\u011f\u0131\u015f";

    private static final String gsm7bit_spanish_short =
        "\u00e7\u00c1\u00cd\u00d3\u00da\u00e1\u00ed\u00f3\u00fa";

    private static final String gsm7bit_portuguese_short =
        "\u00ea\u00e7\u00d4\u00f4\u00c1\u00e1\u03a6\u0393\u03a9\u03a0\u03a8\u03a3\u0398\u00ca\u00c0"
        + "\u00cd\u00d3\u00da\u00c3\u00d5\u00c2\u00ed\u00f3\u00fa\u00e3\u00f5\u00e2";

    private static final String arbitrary_deliver_pdu_head =
        "00440680214365000080114080545340";

    private final SmsManager smsManager = SmsManager.getDefault();

    @SmallTest
    public void testDivideShortGsm7bitOnly() throws Exception {
        String text = gsm7bit_default_short;

        ArrayList<String> parts = smsManager.divideMessage(text);

        assertNotNull("Message divided into no parts: null", parts);
        assertEquals("Message divided into more than one part", 1, parts.size());
        assertTrue("Message corrupted during division", comparePartsToString(parts, text));
    }

    @SmallTest
    public void testDivideLongGsm7bitOnly() throws Exception {
        String text = gsm7bit_default_160_chars + gsm7bit_default_160_chars;

        ArrayList<String> parts = smsManager.divideMessage(text);

        assertNotNull("Message divided into no parts: null", parts);
        assertEquals("Message not divided into three parts", 3, parts.size());
        assertTrue("Message corrupted during division", comparePartsToString(parts, text));
    }

    @SmallTest
    public void testDivideShortUSC2() throws Exception {
        String text = ucs2_short;

        ArrayList<String> parts = smsManager.divideMessage(text);

        assertNotNull("Message divided into no parts: null", parts);
        assertEquals("Message divided into more than one part", 1, parts.size());
        assertTrue("Message corrupted during division", comparePartsToString(parts, text));
    }

    @SmallTest
    public void testDivideLongUSC2() throws Exception {
        String text = ucs2_70_chars + ucs2_70_chars;

        ArrayList<String> parts = smsManager.divideMessage(text);

        assertNotNull("Message divided into no parts: null", parts);
        assertEquals("Message not divided into three parts", 3, parts.size());
        assertTrue("Message corrupted during division", comparePartsToString(parts, text));
    }

    @SmallTest
    public void testDivideShortTurkish() throws Exception {
        String text = gsm7bit_turkish_short;

        ArrayList<String> parts = smsManager.divideMessage(text);

        assertNotNull("Message divided into no parts: null", parts);
        assertEquals("Message divided into more than one part", 1, parts.size());
        assertTrue("Message corrupted during division", comparePartsToString(parts, text));
    }

    @SmallTest
    public void testDivideLongTurkish() throws Exception {
        String text = gsm7bit_turkish_short + gsm7bit_default_160_chars + gsm7bit_turkish_short;

        ArrayList<String> parts = smsManager.divideMessage(text);

        assertNotNull("Message divided into no parts: null", parts);
        assertEquals("Message not divided into two parts", 2, parts.size());
        assertTrue("Message corrupted during division", comparePartsToString(parts, text));
    }

    @SmallTest
    public void testDivideShortSpanish() throws Exception {
        String text = gsm7bit_spanish_short;

        ArrayList<String> parts = smsManager.divideMessage(text);

        assertNotNull("Message divided into no parts: null", parts);
        assertEquals("Message divided into more than one part", 1, parts.size());
        assertTrue("Message corrupted during division", comparePartsToString(parts, text));
    }

    @SmallTest
    public void testDivideLongSpanish() throws Exception {
        String text = gsm7bit_spanish_short + gsm7bit_default_160_chars + gsm7bit_spanish_short;

        ArrayList<String> parts = smsManager.divideMessage(text);

        assertNotNull("Message divided into no parts: null", parts);
        assertEquals("Message not divided into two parts", 2, parts.size());
        assertTrue("Message corrupted during division", comparePartsToString(parts, text));
    }

    @SmallTest
    public void testDivideShortPortuguese() throws Exception {
        String text = gsm7bit_portuguese_short;

        ArrayList<String> parts = smsManager.divideMessage(text);

        assertNotNull("Message divided into no parts: null", parts);
        assertEquals("Message divided into more than one part", 1, parts.size());
        assertTrue("Message corrupted during division", comparePartsToString(parts, text));
    }

    @SmallTest
    public void testDivideLongPortuguese() throws Exception {
        String text = gsm7bit_portuguese_short + gsm7bit_default_160_chars
                + gsm7bit_portuguese_short;

        ArrayList<String> parts = smsManager.divideMessage(text);

        assertNotNull("Message divided into no parts: null", parts);
        assertEquals("Message not divided into two parts", 2, parts.size());
        assertTrue("Message corrupted during division", comparePartsToString(parts, text));
    }

    @SmallTest
    public void testEncodeAndDecodeTurkishSingleshift() throws Exception {
        String text = gsm7bit_turkish_short;

        GsmLanguage extensionLanguage = GsmAlphabet.getExtensionLanguage(text);

        assertEquals("Wrong extension table", GsmLanguage.TURKISH, extensionLanguage);

        SmsHeader smsHeader = new SmsHeader();
        smsHeader.singleShiftLanguage = extensionLanguage;

        SubmitPdu pdu = SmsMessage.getSubmitPdu(null, "123456", text, false, smsHeader);
        assertNotNull("getSubmitPdu returned null", pdu);

        String submitPdu = HexDump.toHexString(pdu.encodedMessage);
        assertEquals("Message not correctly coded",
                "4100068121436500001303240101D81C37C9CD7433DE9C37E9CD1C", submitPdu);

        SmsMessage msg = SmsMessage.newFromCDS(submitToDeliver(submitPdu));
        assertNotNull("Could not create message", msg);

        String body = msg.getDisplayMessageBody();
        assertNotNull("Body null", body);
        assertEquals("Pdu not correctly decoded", text, body);
    }

    @SmallTest
    public void testEncodeAndDecodeSpanishSingleshift() throws Exception {
        String text = gsm7bit_spanish_short;

        GsmLanguage extensionLanguage = GsmAlphabet.getExtensionLanguage(text);

        assertEquals("Wrong extension table", GsmLanguage.SPANISH, extensionLanguage);

        SmsHeader smsHeader = new SmsHeader();
        smsHeader.singleShiftLanguage = extensionLanguage;

        SubmitPdu pdu = SmsMessage.getSubmitPdu(null, "123456", text, false, smsHeader);
        assertNotNull("getSubmitPdu returned null", pdu);

        String submitPdu = HexDump.toHexString(pdu.encodedMessage);
        assertEquals("Message not correctly coded",
                "4100068121436500001703240102D82436C14D72F3DC5437E14D7AF3DED401", submitPdu);

        SmsMessage msg = SmsMessage.newFromCDS(submitToDeliver(submitPdu));
        assertNotNull("Could not create message", msg);

        String body = msg.getDisplayMessageBody();
        assertNotNull("Body null", body);
        assertEquals("Pdu not correctly decoded", text, body);
    }

    @SmallTest
    public void testEncodeAndDecodePortugueseSingleshift() throws Exception {
        String text = gsm7bit_portuguese_short;

        GsmLanguage extensionLanguage = GsmAlphabet.getExtensionLanguage(text);

        assertEquals("Wrong extension table", GsmLanguage.PORTUGUESE, extensionLanguage);

        SmsHeader smsHeader = new SmsHeader();
        smsHeader.singleShiftLanguage = extensionLanguage;

        SubmitPdu pdu = SmsMessage.getSubmitPdu(null, "123456", text, false, smsHeader);
        assertNotNull("getSubmitPdu returned null", pdu);

        String submitPdu = HexDump.toHexString(pdu.encodedMessage);
        assertEquals(
                "Message not correctly coded",
                "4100068121436500003403240103D8143689CD62C3D838360FC9A462B960329BCF26B8496E9E9BEA66"
                + "BBE16EC29BF4E6BDA96FF61BFEE60F",
                submitPdu);

        SmsMessage msg = SmsMessage.newFromCDS(submitToDeliver(submitPdu));
        assertNotNull("Could not create message", msg);

        String body = msg.getDisplayMessageBody();
        assertNotNull("Body null", body);
        assertEquals("Pdu not correctly decoded", text, body);
    }

    @SmallTest
    public void testEncodeAndDecodeSingleshift() throws Exception {

        for (GsmLanguage language : GsmLanguage.values()) {

            if (language != GsmLanguage.DEFAULT) {
                GsmAlphabet alphabet = GsmAlphabet.getAlphabet(GsmLanguage.DEFAULT, language);

                String text = getAllExtendedCharacters(alphabet);

                GsmLanguage extensionLanguage = GsmAlphabet.getExtensionLanguage(text);

                assertEquals("Wrong extension table (language: " + language + ")", language,
                        extensionLanguage);

                SmsHeader smsHeader = new SmsHeader();
                smsHeader.singleShiftLanguage = extensionLanguage;

                SubmitPdu submitPdu = SmsMessage.getSubmitPdu(null, "123456", text, false,
                        smsHeader);
                assertNotNull("getSubmitPdu returned null (language: " + language + ")", submitPdu);

                SmsMessage msg = SmsMessage.newFromCDS(submitToDeliver(HexDump
                        .toHexString(submitPdu.encodedMessage)));
                assertNotNull("Could not create message (language: " + language + ")", msg);

                String body = msg.getDisplayMessageBody();
                assertNotNull("Body null (language: " + language + ")", body);
                assertEquals("Pdu not correctly decoded (language: " + language + ")", text, body);
            }
        }
    }

    @SmallTest
    public void testDecodeLockingShift() throws Exception {

        for (GsmLanguage language : GsmLanguage.values()) {

            GsmAlphabet alphabet = GsmAlphabet.getAlphabet(language, GsmLanguage.DEFAULT);

            String text = getAllDefaultCharacters(alphabet);
            byte[] header = {
                    0x25, 0x01, (byte)language.getLanguageCode()
            };

            byte[] userdata = alphabet.stringToGsm7BitPackedWithHeader(text, header);
            assertNotNull("Could not create userdata (language: " + language + ")", userdata);

            String deliverPdu = arbitrary_deliver_pdu_head + HexDump.toHexString(userdata);

            SmsMessage msg = SmsMessage.newFromCDS(deliverPdu);
            assertNotNull("Could not create message from: " + deliverPdu + " (language: "
                    + language + ")", msg);

            String body = msg.getDisplayMessageBody();
            assertNotNull("Could not get body from message, pdu: " + deliverPdu + " (language: "
                    + language + ")", body);
            assertEquals("Pdu not correctly decoded" + " (language: " + language + ")", text, body);
        }
    }

    private static boolean comparePartsToString(ArrayList<String> parts, String text) {
        if (parts != null && text != null) {
            StringBuilder sb = new StringBuilder();
            for (String str : parts) {
                sb.append(str);
            }
            if (sb.toString().equals(text)) {
                return true;
            }
        }
        return false;
    }

    private static String submitToDeliver(String submitPdu) {
        return arbitrary_deliver_pdu_head + submitPdu.substring(18);
    }

    private static String getAllDefaultCharacters(GsmAlphabet alphabet) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 0x80; i++) {
            if (i != GsmAlphabet.GSM_EXTENDED_ESCAPE) {
                sb.append(alphabet.gsmToChar(i));
            }
        }
        return sb.toString();
    }

    private String getAllExtendedCharacters(GsmAlphabet alphabet) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 0x80; i++) {
            if (i != 0x0a && i != 0x0d && i != GsmAlphabet.GSM_EXTENDED_ESCAPE) {
                char c = alphabet.gsmExtendedToChar(i);
                if (c != alphabet.gsmToChar(i)) {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

}







