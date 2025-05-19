//<Beginning of snippet n. 0>
private void parseRecord(byte[] record) {
    try {
        if (isKSC5601(record)) {
            alphaTag = IccUtils.adnStringFieldToString(record, 0, record.length - FOOTER_SIZE_BYTES);
        }
        int footerOffset = record.length - FOOTER_SIZE_BYTES;
    } catch (ArrayIndexOutOfBoundsException e) {
        Log.e(LOG_TAG, "Error parsing record: Array index out of bounds - " + e.getMessage());
    } catch (Exception e) {
        Log.e(LOG_TAG, "Error parsing record with KSC5601 handling: " + e.getMessage());
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public static String adnStringFieldToString(byte[] data, int offset, int length) {
    if (length >= 1) {
        if (isKSC5601(data, offset, length)) {
            try {
                return new String(data, offset + 1, length - 1, "EUC-KR");
            } catch (UnsupportedEncodingException e) {
                Log.e(LOG_TAG, "Unsupported encoding for KSC5601: " + e.getMessage());
            }
            return null;
        }
        return GsmAlphabet.gsm8BitUnpackedToString(data, offset, length);
    }
    return null;
}

private static boolean isKSC5601(byte[] data, int offset, int length) {
    for (int i = 0; i < length; i++) {
        if ((data[offset + i] & 0xFF) >= 0x80 && (data[offset + i] & 0xFF) <= 0xFE) {
            return true;
        }
    }
    return false;
}

private static boolean isKSC5601(byte[] record) {
    return record != null && record.length > 0 && isKSC5601(record, 0, record.length);
}
//<End of snippet n. 1>

//<Beginning of snippet n. 3>
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsMessageBase.TextEncodingDetails;

public class SmsMessage extends SmsMessageBase {
    static final String LOG_TAG = "GSM";

    private MessageClass messageClass;

    boolean moreDataPresent() {
        return (pdu.length > cur);
    }

    private void parseSmsData(byte[] pdu) {
        int dataCodingScheme = pdu[0] & 0xff;
        String messageBody = null;

        if (isKSC5601(pdu)) {
            messageBody = new String(pdu, 1, pdu.length - 1, "EUC-KR");
        } else {
            switch (dataCodingScheme) {
                case ENCODING_16BIT:
                    messageBody = p.getUserDataUCS2(count);
                    break;
                default:
                    Log.w(LOG_TAG, "Unsupported SMS data coding scheme " + dataCodingScheme);
                    break;
            }
        }

        if (Config.LOGV) Log.v(LOG_TAG, "SMS message body (raw): '" + messageBody + "'");
    }

    private boolean isKSC5601(byte[] pdu) {
        return isKSC5601(pdu, 0, pdu.length);
    }
}
//<End of snippet n. 3>

//<Beginning of snippet n. 6>
data = IccUtils.hexStringToBytes("820505302D82d32d31");
// Example from 3GPP TS 11.11 V18.1.3.0 annex B
assertEquals("-\u0532\u0583-1", IccUtils.adnStringFieldToString(data, 0, data.length));
//<End of snippet n. 6>