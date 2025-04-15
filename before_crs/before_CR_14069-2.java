/*Type Zero Sms should not be displayed/stored/notified.

Type Zero messages indicated by TP_PID field set to value 0x40,
should not be displayed/stored/notified. They should only be
acknowledged.*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 6ae316d..d720516 100644

//Synthetic comment -- @@ -94,6 +94,13 @@
SmsMessage sms = (SmsMessage) smsb;
boolean handled = false;

// Special case the message waiting indicator messages
if (sms.isMWISetMessage()) {
mGsmPhone.updateMessageWaitingIndicator(true);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index d627baf..12c6b88 100644

//Synthetic comment -- @@ -111,6 +111,14 @@
}

/**
* TS 27.005 3.4.1 lines[0] and lines[1] are the two lines read from the
* +CMT unsolicited response (PDU mode, of course)
*  +CMT: [&lt;alpha>],<length><CR><LF><pdu>







