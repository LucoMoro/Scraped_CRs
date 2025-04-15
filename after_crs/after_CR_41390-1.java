/*Fix bugs irregularly concatenated messages.

When receive irregularly concatenated msgs with GSM/CDMA Format, the device can't parse normally.
Because intent has format of last msg and parse all msg with same format type.
Try to parse the received Message  GSM/ CDMA both, when every Message is received.
Signed-off-by: kyunga kim <kyunga1.kim@lge.com>

Change-Id:I3f5c4fefe151326efc1b5d62b585f8aefe60054a*/




//Synthetic comment -- diff --git a/src/java/android/provider/Telephony.java b/src/java/android/provider/Telephony.java
//Synthetic comment -- index dd8be66..bc1835f 100644

//Synthetic comment -- @@ -697,7 +697,11 @@
SmsMessage[] msgs = new SmsMessage[pduCount];
for (int i = 0; i < pduCount; i++) {
pdus[i] = pduObjs[i];
		// change the function for GSM/CDMA FORMAT interworking
		// When receive irregularly concatenated msgs with GSM/CDMA Format, 
		// The device can't parse normally. because intent has format of last msg and parse all msg with same format type.
	        //msgs[i] = SmsMessage.createFromPdu(pdus[i], format);
	        msgs[i] = SmsMessage.createFromPdu(pdus[i]);
}
return msgs;
}








//Synthetic comment -- diff --git a/src/java/android/telephony/SmsMessage.java b/src/java/android/telephony/SmsMessage.java
//Synthetic comment -- index b94609e..cbdb86e 100644

//Synthetic comment -- @@ -134,10 +134,20 @@
* such as dual-mode GSM/CDMA and CDMA/LTE phones.
*/
public static SmsMessage createFromPdu(byte[] pdu) {
	// need initialization
        SmsMessage message = null;
        // cdma vs gsm encoding info was not given, guess from active voice phone type
int activePhone = TelephonyManager.getDefault().getCurrentPhoneType();
String format = (PHONE_TYPE_CDMA == activePhone) ?
SmsConstants.FORMAT_3GPP2 : SmsConstants.FORMAT_3GPP;
        message = createFromPdu(pdu, format);
        if (null == message) {
            // decoding pdu failed based on activePhone type, must be other encoding
	        format = (PHONE_TYPE_CDMA == activePhone) ?
                SmsConstants.FORMAT_3GPP2 : SmsConstants.FORMAT_3GPP;
            message = createFromPdu(pdu, format);
        }
        return message;
}

/**
//Synthetic comment -- @@ -151,7 +161,8 @@
* @hide pending API council approval
*/
public static SmsMessage createFromPdu(byte[] pdu, String format) {
	// need initialization
        SmsMessageBase wrappedMessage = null;

if (SmsConstants.FORMAT_3GPP2.equals(format)) {
wrappedMessage = com.android.internal.telephony.cdma.SmsMessage.createFromPdu(pdu);
//Synthetic comment -- @@ -159,6 +170,11 @@
wrappedMessage = com.android.internal.telephony.gsm.SmsMessage.createFromPdu(pdu);
} else {
Log.e(LOG_TAG, "createFromPdu(): unsupported message format " + format);
        }
	// handle exception
	if(wrappedMessage == null)
	{
            Log.e(LOG_TAG, "createFromPdu(): wrappedMessage is null");
return null;
}








