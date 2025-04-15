/*Telephony: CB duplicate detection

Detect and ignore duplicate CB.
3GPP TS 23.041  Section 2 General description states:
Each page of such CBS message will have the same message identifier
(indicating the source of the message), and the same serial number.
Using this information, the MS/UE is able to identify and ignore
re-broadcasts of already received messages.

Change-Id:Ifc5d83af48bda00ccacbf60ba35b711a84fe614a*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/SmsCbMessage.java b/telephony/java/android/telephony/SmsCbMessage.java
//Synthetic comment -- index 383e0f9..7a15d28 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.gsm.SmsCbHeader;

import java.io.UnsupportedEncodingException;

/**
//Synthetic comment -- @@ -456,4 +457,27 @@
return "SmsCbMessage{" + mHeader.toString() + ", language=" + mLanguage +
", body=\"" + mBody + "\"}";
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 8a75f51..e774956 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
import com.android.internal.telephony.SmsUsageMonitor;
import com.android.internal.telephony.TelephonyProperties;

import java.util.HashMap;
import java.util.Iterator;

//Synthetic comment -- @@ -50,6 +51,11 @@
public final class GsmSMSDispatcher extends SMSDispatcher {
private static final String TAG = "GSM";

/** Status report received */
private static final int EVENT_NEW_SMS_STATUS_REPORT = 100;

//Synthetic comment -- @@ -474,4 +480,39 @@
Log.e(TAG, "Error in decoding SMS CB pdu", e);
}
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsCbHeader.java b/telephony/java/com/android/internal/telephony/gsm/SmsCbHeader.java
//Synthetic comment -- index 8e6b79b..ed03d74 100644

//Synthetic comment -- @@ -83,9 +83,9 @@

if (pdu.length <= PDU_LENGTH_ETWS) {
format = FORMAT_ETWS_PRIMARY;
            geographicalScope = -1; //not applicable
            messageCode = -1;
            updateNumber = -1;
messageIdentifier = ((pdu[2] & 0xff) << 8) | (pdu[3] & 0xff);
dataCodingScheme = -1;
pageIndex = -1;







