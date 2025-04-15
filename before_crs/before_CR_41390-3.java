/*Fix the concatenated messages parse error

The "intent" has format of last msg and parse all msg with same format type.
But throw ParseException
when the received concatenated msg has GSM/CDMA format irregularly.
Therefore when every Message is received,
the msg should be parsed from GSM and CDMA Format both.
Signed-off-by: kyunga kim <kyunga1.kim@lge.com>
Change-Id:I3f5c4fefe151326efc1b5d62b585f8aefe60054a*/
//Synthetic comment -- diff --git a/src/java/android/provider/Telephony.java b/src/java/android/provider/Telephony.java
//Synthetic comment -- index dd8be66..700306d 100644

//Synthetic comment -- @@ -697,7 +697,7 @@
SmsMessage[] msgs = new SmsMessage[pduCount];
for (int i = 0; i < pduCount; i++) {
pdus[i] = pduObjs[i];
                    msgs[i] = SmsMessage.createFromPdu(pdus[i], format);
}
return msgs;
}








//Synthetic comment -- diff --git a/src/java/android/telephony/SmsMessage.java b/src/java/android/telephony/SmsMessage.java
//Synthetic comment -- index b94609e..a68b013 100644

//Synthetic comment -- @@ -134,10 +134,18 @@
* such as dual-mode GSM/CDMA and CDMA/LTE phones.
*/
public static SmsMessage createFromPdu(byte[] pdu) {
int activePhone = TelephonyManager.getDefault().getCurrentPhoneType();
String format = (PHONE_TYPE_CDMA == activePhone) ?
SmsConstants.FORMAT_3GPP2 : SmsConstants.FORMAT_3GPP;
        return createFromPdu(pdu, format);
}

/**
//Synthetic comment -- @@ -151,7 +159,7 @@
* @hide pending API council approval
*/
public static SmsMessage createFromPdu(byte[] pdu, String format) {
        SmsMessageBase wrappedMessage;

if (SmsConstants.FORMAT_3GPP2.equals(format)) {
wrappedMessage = com.android.internal.telephony.cdma.SmsMessage.createFromPdu(pdu);
//Synthetic comment -- @@ -159,9 +167,11 @@
wrappedMessage = com.android.internal.telephony.gsm.SmsMessage.createFromPdu(pdu);
} else {
Log.e(LOG_TAG, "createFromPdu(): unsupported message format " + format);
return null;
}

return new SmsMessage(wrappedMessage);
}








