/*Telephony: Add support to read 3GPP2 sms from CSIM/RUIM

Add parser to parse 3GPP2 format sms stored in CSIM/RUIM card

Change-Id:I4720ba7602fcc1b078de0d5fea34247541242c1c*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java b/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index f869dbd..30c8d95 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.internal.telephony.cdma.sms.CdmaSmsSubaddress;
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.HexDump;

import java.io.BufferedInputStream;
//Synthetic comment -- @@ -72,6 +73,16 @@
static final String LOG_TAG = "CDMA";
static private final String LOGGABLE_TAG = "CDMA:SMS";

/**
*  Status of a previously submitted SMS.
*  This field applies to SMS Delivery Acknowledge messages. 0 indicates success;
//Synthetic comment -- @@ -267,6 +278,7 @@
System.arraycopy(data, 2, pdu, 0, size);
// the message has to be parsed before it can be displayed
// see gsm.SmsMessage
return msg;
} catch (RuntimeException ex) {
Log.e(LOG_TAG, "SMS PDU parsing failed: ", ex);
//Synthetic comment -- @@ -539,6 +551,143 @@
}

/**
* Parses a SMS message from its BearerData stream. (mobile-terminated only)
*/
protected void parseSms() {







