/*Fix bug.
Parsing error is ocuured when irregularly  concatenated msgs with GSM/CDMA Format.
so, We should consider following cases and fix them.
	1. When receive irregularly concatenated msgs with GSM/CDMA Format,
	 The device can't parse normally. because intent has format of last msg and parse all msg with same format type.
	2. Try to parse the received Message  GSM/ CDMA both, when every Message is received.
Signed-off-by: kyunga kim <kyunga1.kim@lge.com>

Change-Id:Icc024913dcc52176fa495d4b729e92611162531f(cherry picked from commit 855acb5cc741ba39f6f95713889f0e5cf97e9828)*/
//Synthetic comment -- diff --git a/src/java/android/provider/Telephony.java b/src/java/android/provider/Telephony.java
//Synthetic comment -- index dd8be66..bc1835f 100644

//Synthetic comment -- @@ -697,7 +697,11 @@
SmsMessage[] msgs = new SmsMessage[pduCount];
for (int i = 0; i < pduCount; i++) {
pdus[i] = pduObjs[i];
                    msgs[i] = SmsMessage.createFromPdu(pdus[i], format);
}
return msgs;
}








//Synthetic comment -- diff --git a/src/java/android/telephony/SmsMessage.java b/src/java/android/telephony/SmsMessage.java
//Synthetic comment -- index b94609e..cbdb86e 100644

//Synthetic comment -- @@ -134,10 +134,20 @@
* such as dual-mode GSM/CDMA and CDMA/LTE phones.
*/
public static SmsMessage createFromPdu(byte[] pdu) {
int activePhone = TelephonyManager.getDefault().getCurrentPhoneType();
String format = (PHONE_TYPE_CDMA == activePhone) ?
SmsConstants.FORMAT_3GPP2 : SmsConstants.FORMAT_3GPP;
        return createFromPdu(pdu, format);
}

/**
//Synthetic comment -- @@ -151,7 +161,8 @@
* @hide pending API council approval
*/
public static SmsMessage createFromPdu(byte[] pdu, String format) {
        SmsMessageBase wrappedMessage;

if (SmsConstants.FORMAT_3GPP2.equals(format)) {
wrappedMessage = com.android.internal.telephony.cdma.SmsMessage.createFromPdu(pdu);
//Synthetic comment -- @@ -159,6 +170,11 @@
wrappedMessage = com.android.internal.telephony.gsm.SmsMessage.createFromPdu(pdu);
} else {
Log.e(LOG_TAG, "createFromPdu(): unsupported message format " + format);
return null;
}








