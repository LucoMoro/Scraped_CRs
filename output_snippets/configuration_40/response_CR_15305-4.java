//<Beginning of snippet n. 0>
private void parseRecord(byte[] record) {
    try {
        alphaTag = IccUtils.adnStringFieldToString(record, 0, record.length - FOOTER_SIZE_BYTES);

        int footerOffset = record.length - FOOTER_SIZE_BYTES;

        // Check for Korean SIM Card
        if (isKoreanSimCard()) {
            alphaTag = IccUtils.adnStringFieldToKSC5601String(record, 0, record.length - FOOTER_SIZE_BYTES);
        }
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public static String adnStringFieldToString(byte[] data, int offset, int length) {
    if (length >= 1) {
        if (data[offset] == (byte) 0x80) {
            int ucslen = (length - 1) / 2;
            StringBuilder ret = new StringBuilder();
            for (int i = 0; i < ucslen; i++) {
                ret.append((char) ((data[offset + 1 + i * 2] & 0xFF) << 8 | (data[offset + 1 + i * 2 + 1] & 0xFF)));
            }
            return ret.toString();
        }
        // Handle KSC5601 encoding
        if (isKSC5601Encoded(data, offset)) {
            return Ksc5601Decoder.decode(data, offset, length);
        }
        return GsmAlphabet.gsm8BitUnpackedToString(data, offset, length);
    }
    return "";
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
// No changes made
//<End of snippet n. 2>

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

    // ... existing methods ...

    public String getMessageBody() {
        // Handle KSC5601 with SMS
        if (isKoreanSimCard() && isKSC5601Encoded(pdu, 0)) {
            return Ksc5601Decoder.decode(pdu, 0, pdu.length);
        }

        // Existing logic for SMS parsing remains the same
        switch (dataCodingScheme) {
            case ENCODING_16BIT:
                messageBody = p.getUserDataUCS2(count);
                break;
            // existing SMS case statements
        }

        if (Config.LOGV) Log.v(LOG_TAG, "SMS message body (raw): '" + messageBody + "'");
        return messageBody;
    }

    boolean moreDataPresent() {
        return (pdu.length > cur);
    }
}
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
assertEquals("Adgjm", adn.getAlphaTag());
assertEquals("+18885551212,12345678", adn.getNumber());
assertFalse(adn.isEmpty());
//<End of snippet n. 4>

//<Beginning of snippet n. 5>
sms = SmsMessage.createFromEfRecord(1, data);
assertNotNull(sms.getMessageBody());
//<End of snippet n. 5>

//<Beginning of snippet n. 6>
data = IccUtils.hexStringToBytes("820505302D82d32d31");
assertEquals("-\u0532\u0583-1", IccUtils.adnStringFieldToString(data, 0, data.length));
//<End of snippet n. 6>