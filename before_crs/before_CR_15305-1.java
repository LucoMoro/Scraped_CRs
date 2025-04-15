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
            alphaTag = IccUtils.adnStringFieldToString(
record, 0, record.length - FOOTER_SIZE_BYTES);

int footerOffset = record.length - FOOTER_SIZE_BYTES;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccUtils.java b/telephony/java/com/android/internal/telephony/IccUtils.java
//Synthetic comment -- index 1034bda..3e309ae 100644

//Synthetic comment -- @@ -150,6 +150,48 @@
*/
public static String
adnStringFieldToString(byte[] data, int offset, int length) {
if (length >= 1) {
if (data[offset] == (byte) 0x80) {
int ucslen = (length - 1) / 2;
//Synthetic comment -- @@ -225,6 +267,11 @@
return ret.toString();
}

return GsmAlphabet.gsm8BitUnpackedToString(data, offset, length);
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SimRegionCache.java b/telephony/java/com/android/internal/telephony/SimRegionCache.java
new file mode 100644
//Synthetic comment -- index 0000000..2cf6d25

//Synthetic comment -- @@ -0,0 +1,51 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 12c6b88..c8a4cbf 100644

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
//Synthetic comment -- @@ -1106,6 +1135,10 @@
} else {
Log.w(LOG_TAG, "3 - Unsupported SMS data coding scheme "
+ (dataCodingScheme & 0xff));
}

// set both the user data and the user data header.
//Synthetic comment -- @@ -1127,6 +1160,10 @@
case ENCODING_16BIT:
messageBody = p.getUserDataUCS2(count);
break;
}

if (Config.LOGV) Log.v(LOG_TAG, "SMS message body (raw): '" + messageBody + "'");








//Synthetic comment -- diff --git a/tests/CoreTests/com/android/internal/telephony/AdnRecordTest.java b/tests/CoreTests/com/android/internal/telephony/AdnRecordTest.java
//Synthetic comment -- index 8a4a285..5511c09c 100644

//Synthetic comment -- @@ -170,6 +170,18 @@
assertEquals("Adgjm", adn.getAlphaTag());
assertEquals("+18885551212,12345678", adn.getNumber());
assertFalse(adn.isEmpty());
}
}








