/*Support for KSC5601 on SIM.

Korean phones write to the ADN record of the SIM in a non-standard way.
When UCS2 is not used, the alphaTag will be written in the KSC5601
encoding. This contribution adds support for reading that format when
a Korean SIM card is used.

Also adds support for KSC5601 in SMS.

Change-Id:I81a4a6949359b4d23a937ac2d813bafed2b85ff6*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/AdnRecord.java b/telephony/java/com/android/internal/telephony/AdnRecord.java
//Synthetic comment -- index 1bf2d3c..a01b00d 100644

//Synthetic comment -- @@ -283,7 +283,7 @@
private void
parseRecord(byte[] record) {
try {
            alphaTag = IccUtils.adnStringFieldToString(
record, 0, record.length - FOOTER_SIZE_BYTES);

int footerOffset = record.length - FOOTER_SIZE_BYTES;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccUtils.java b/telephony/java/com/android/internal/telephony/IccUtils.java
//Synthetic comment -- index 957eddd..5bd7523 100644

//Synthetic comment -- @@ -150,6 +150,47 @@
*/
public static String
adnStringFieldToString(byte[] data, int offset, int length) {
if (length >= 1) {
if (data[offset] == (byte) 0x80) {
int ucslen = (length - 1) / 2;
//Synthetic comment -- @@ -225,6 +266,11 @@
return ret.toString();
}

return GsmAlphabet.gsm8BitUnpackedToString(data, offset, length);
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SimRegionCache.java b/telephony/java/com/android/internal/telephony/SimRegionCache.java
new file mode 100644
//Synthetic comment -- index 0000000..2cf6d25

//Synthetic comment -- @@ -0,0 +1,51 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 9a3c476..f4c5e6c 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsMessageBase.TextEncodingDetails;
//Synthetic comment -- @@ -48,6 +49,12 @@
public class SmsMessage extends SmsMessageBase{
static final String LOG_TAG = "GSM";

private MessageClass messageClass;

/**
//Synthetic comment -- @@ -781,6 +788,28 @@
return ret;
}

boolean moreDataPresent() {
return (pdu.length > cur);
}
//Synthetic comment -- @@ -1110,6 +1139,10 @@
} else {
Log.w(LOG_TAG, "3 - Unsupported SMS data coding scheme "
+ (dataCodingScheme & 0xff));
}

// set both the user data and the user data header.
//Synthetic comment -- @@ -1131,6 +1164,10 @@
case ENCODING_16BIT:
messageBody = p.getUserDataUCS2(count);
break;
}

if (Config.LOGV) Log.v(LOG_TAG, "SMS message body (raw): '" + messageBody + "'");








//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/com/android/internal/telephony/AdnRecordTest.java b/telephony/tests/telephonytests/src/com/android/internal/telephony/AdnRecordTest.java
//Synthetic comment -- index 8a4a285..5511c09c 100644

//Synthetic comment -- @@ -170,6 +170,18 @@
assertEquals("Adgjm", adn.getAlphaTag());
assertEquals("+18885551212,12345678", adn.getNumber());
assertFalse(adn.isEmpty());
}
}









//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/com/android/internal/telephony/SMSDispatcherTest.java b/telephony/tests/telephonytests/src/com/android/internal/telephony/SMSDispatcherTest.java
//Synthetic comment -- index 8a66614..f578a8d 100644

//Synthetic comment -- @@ -102,4 +102,25 @@
sms = SmsMessage.createFromEfRecord(1, data);
assertNotNull(sms.getMessageBody());
}
}








//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/com/android/internal/telephony/SimUtilsTest.java b/telephony/tests/telephonytests/src/com/android/internal/telephony/SimUtilsTest.java
//Synthetic comment -- index db38ede..0502636 100644

//Synthetic comment -- @@ -82,6 +82,30 @@
data = IccUtils.hexStringToBytes("820505302D82d32d31");
// Example from 3GPP TS 11.11 V18.1.3.0 annex B
assertEquals("-\u0532\u0583-1", IccUtils.adnStringFieldToString(data, 0, data.length));
}

}







