/*Type Zero Sms should not be displayed/stored/notified.

Type Zero messages indicated by TP_PID field set to value 0x40,
should not be displayed/stored/notified. They should only be
acknowledged.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 6ae316d..d720516 100644

//Synthetic comment -- @@ -94,6 +94,13 @@
SmsMessage sms = (SmsMessage) smsb;
boolean handled = false;

        if (sms.isTypeZero()) {
            // As per 3GPP TS 23.040 9.2.3.9, Type Zero messages should not be
            // Displayed/Stored/Notified. They should only be acknowledged.
            Log.d(TAG, "Received short message type 0, Dont display or store it. Send Ack");
            return Intents.RESULT_SMS_HANDLED;
        }

// Special case the message waiting indicator messages
if (sms.isMWISetMessage()) {
mGsmPhone.updateMessageWaitingIndicator(true);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index d627baf..12c6b88 100644

//Synthetic comment -- @@ -111,6 +111,14 @@
}

/**
     * 3GPP TS 23.040 9.2.3.9 specifies that Type Zero messages are indicated
     * by TP_PID field set to value 0x40
     */
    public boolean isTypeZero() {
        return (protocolIdentifier == 0x40);
    }

    /**
* TS 27.005 3.4.1 lines[0] and lines[1] are the two lines read from the
* +CMT unsolicited response (PDU mode, of course)
*  +CMT: [&lt;alpha>],<length><CR><LF><pdu>







