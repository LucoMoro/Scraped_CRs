
//<Beginning of snippet n. 0>


private void
parseRecord(byte[] record) {
try {
            alphaTag = IccUtils.adnStringFieldToString(
record, 0, record.length - FOOTER_SIZE_BYTES);

int footerOffset = record.length - FOOTER_SIZE_BYTES;

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


*/
public static String
adnStringFieldToString(byte[] data, int offset, int length) {
if (length >= 1) {
if (data[offset] == (byte) 0x80) {
int ucslen = (length - 1) / 2;
return ret.toString();
}

return GsmAlphabet.gsm8BitUnpackedToString(data, offset, length);
}


//<End of snippet n. 1>










//<Beginning of snippet n. 2>

new file mode 100644


//<End of snippet n. 2>










//<Beginning of snippet n. 3>


import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsMessageBase.TextEncodingDetails;
public class SmsMessage extends SmsMessageBase{
static final String LOG_TAG = "GSM";

private MessageClass messageClass;

/**
return ret;
}

boolean moreDataPresent() {
return (pdu.length > cur);
}
} else {
Log.w(LOG_TAG, "3 - Unsupported SMS data coding scheme "
+ (dataCodingScheme & 0xff));
}

// set both the user data and the user data header.
case ENCODING_16BIT:
messageBody = p.getUserDataUCS2(count);
break;
}

if (Config.LOGV) Log.v(LOG_TAG, "SMS message body (raw): '" + messageBody + "'");

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


assertEquals("Adgjm", adn.getAlphaTag());
assertEquals("+18885551212,12345678", adn.getNumber());
assertFalse(adn.isEmpty());
}
}


//<End of snippet n. 4>










//<Beginning of snippet n. 5>


sms = SmsMessage.createFromEfRecord(1, data);
assertNotNull(sms.getMessageBody());
}
}

//<End of snippet n. 5>










//<Beginning of snippet n. 6>


data = IccUtils.hexStringToBytes("820505302D82d32d31");
// Example from 3GPP TS 11.11 V18.1.3.0 annex B
assertEquals("-\u0532\u0583-1", IccUtils.adnStringFieldToString(data, 0, data.length));
}

}

//<End of snippet n. 6>








