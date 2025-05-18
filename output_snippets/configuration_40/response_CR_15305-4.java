//<Beginning of snippet n. 0>
private void parseRecord(byte[] record) {
    try {
        alphaTag = IccUtils.adnStringFieldToString(record, 0, record.length - FOOTER_SIZE_BYTES);

        int footerOffset = record.length - FOOTER_SIZE_BYTES;
        if (alphaTag.isEmpty() && isKSC5601Encoded(record, footerOffset)) {
            alphaTag = decodeKSC5601(record, 0, footerOffset);
        }
    } catch (EncodeException e) {
        Log.e(LOG_TAG, "Error parsing record: " + e.getMessage());
    } catch (UnsupportedEncodingException e) {
        Log.e(LOG_TAG, "Unsupported encoding error: " + e.getMessage());
    } catch (Exception e) {
        Log.e(LOG_TAG, "Unexpected error: " + e.getMessage());
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public static String adnStringFieldToString(byte[] data, int offset, int length) {
    if (length >= 1) {
        if (data[offset] == (byte) 0x80) {
            int ucslen = (length - 1) / 2;
            StringBuilder ret = new StringBuilder();
            // Logic to process UCS2 remains unchanged
            return ret.toString();
        } else if (isKSC5601Encoded(data, offset)) {
            return decodeKSC5601(data, offset, length);
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
import java.nio.charset.StandardCharsets;

public class SmsMessage extends SmsMessageBase {
    static final String LOG_TAG = "GSM";
    private MessageClass messageClass;

    boolean moreDataPresent() {
        return (pdu.length > cur);
    }

    String parseMessageBody(byte[] pdu, int dataCodingScheme, int count) {
        String messageBody = "";
        switch (dataCodingScheme) {
            // existing cases...
            case ENCODING_16BIT:
                messageBody = p.getUserDataUCS2(count);
                break;
            case ENCODING_KSC5601:
                messageBody = decodeKSC5601(pdu, 0, pdu.length);
                break;
            default:
                Log.w(LOG_TAG, "Unsupported SMS data coding scheme: " + (dataCodingScheme & 0xff));
        }
        if (Config.LOGV) {
            Log.v(LOG_TAG, "SMS message body (raw): '" + messageBody + "'");
        }
        return messageBody;
    }

    private String decodeKSC5601(byte[] data, int offset, int length) {
        // Implement KSC5601 decoding logic here
        // Placeholder currently; should be replaced with actual decoding algorithm
        return new String(data, offset, length, StandardCharsets.UTF_16); 
    }

    private boolean isKSC5601Encoded(byte[] data, int offset) {
        return (data[offset] >= (byte) 0xA1 && data[offset] <= (byte) 0xFE);
    }
}
//<End of snippet n. 3>