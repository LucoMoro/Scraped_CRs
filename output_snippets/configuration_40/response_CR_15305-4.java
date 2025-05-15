//<Beginning of snippet n. 0>
private void parseRecord(byte[] record) {
    try {
        alphaTag = IccUtils.adnStringFieldToString(record, 0, record.length - FOOTER_SIZE_BYTES);
        int footerOffset = record.length - FOOTER_SIZE_BYTES;

        // Identify Korean SIM cards and handle KSC5601 encoding
        if (isKoreanSimCard(record)) {
            alphaTag = IccUtils.adnStringFieldToString(record, 0, record.length - FOOTER_SIZE_BYTES);
        }

    } catch (Exception e) {
        Log.e("ParseRecord", "Error parsing record", e);
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public static String adnStringFieldToString(byte[] data, int offset, int length) {
    if (length >= 1) {
        if (data[offset] == (byte) 0x80) { // UCS2 encoding
            int ucslen = (length - 1) / 2;
            StringBuilder ret = new StringBuilder();
            for (int i = 0; i < ucslen; i++) {
                char c = (char) ((data[offset + 2 * i + 1] << 8) | (data[offset + 2 * i] & 0xFF));
                ret.append(c);
            }
            return ret.toString();
        }

        // Check for KSC5601 encoding
        if (isKSC5601(data, offset, length)) {
            return convertKSC5601ToString(data, offset, length);
        }

        return GsmAlphabet.gsm8BitUnpackedToString(data, offset, length);
    }
    return "";
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

    // SMS handling adjustments for KSC5601 encoding
    public void handleSmsData(byte[] pdu) {
        if (pdu.length > 0) {
            int dataCodingScheme = pdu[0];
            String messageBody = null;

            switch (dataCodingScheme) {
                case ENCODING_16BIT:
                    messageBody = p.getUserDataUCS2(count);
                    break;
                case ENCODING_KSC5601:
                    messageBody = convertKSC5601ToString(pdu, 0, pdu.length);
                    break;
                default:
                    Log.w(LOG_TAG, "Unsupported SMS data coding scheme " + (dataCodingScheme & 0xff));
                    break;
            }

            if (Config.LOGV) Log.v(LOG_TAG, "SMS message body (raw): '" + messageBody + "'");
        }
    }
}
//<End of snippet n. 3>

//<Beginning of snippet n. 6>
data = IccUtils.hexStringToBytes("820505302D82d32d31");
// Example from 3GPP TS 11.11 V18.1.3.0 annex B
assertEquals("-\u0532\u0583-1", IccUtils.adnStringFieldToString(data, 0, data.length));
}
}
//<End of snippet n. 6>

// Helper methods for KSC5601 detection and conversion
private boolean isKoreanSimCard(byte[] record) {
    // Logic to identify Korean SIM cards
    return (record[0] & 0xFF) == 0x11; // Example condition
}

private boolean isKSC5601(byte[] data, int offset, int length) {
    // Logic to detect KSC5601 encoding
    return (data[offset] == (byte) 0x81 || data[offset] == (byte) 0x82);
}

private String convertKSC5601ToString(byte[] data, int offset, int length) {
    // Conversion logic from KSC5601 to String
    StringBuilder ret = new StringBuilder();
    for (int i = 0; i < length; i++) {
        ret.append((char) data[offset + i]); // Simplified conversion
    }
    return ret.toString();
}