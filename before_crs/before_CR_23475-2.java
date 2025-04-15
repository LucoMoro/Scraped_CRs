/*Telephony: Mark SIM SMS as read after importing it.

The GCF testcase 8.2.2 checks if the status byte of the SMS
is marked as read after reading the SMS from SIM. The status
byte is marked as read after the SMS is imported from the SIM
if it is not already marked

Change-Id:Ida0036760d2248b797134ec90c870f065ec52501*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SimSmsInterfaceManager.java b/telephony/java/com/android/internal/telephony/gsm/SimSmsInterfaceManager.java
//Synthetic comment -- index 92bf390..9d3ad02 100644

//Synthetic comment -- @@ -39,6 +39,8 @@
import java.util.Set;

import static android.telephony.SmsManager.STATUS_ON_ICC_FREE;

/**
* SimSmsInterfaceManager to provide an inter-process communication to
//Synthetic comment -- @@ -82,6 +84,8 @@
synchronized (mLock) {
if (ar.exception == null) {
mSms  = buildValidRawData((ArrayList<byte[]>) ar.result);
} else {
if(DBG) log("Cannot load Sms records");
if (mSms != null)
//Synthetic comment -- @@ -102,6 +106,26 @@
}
};

public SimSmsInterfaceManager(GSMPhone phone, SMSDispatcher dispatcher) {
super(phone);
mDispatcher = dispatcher;







