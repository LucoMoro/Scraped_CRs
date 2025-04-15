/*Telephony: Fix for first pause DTMF digit

As per 3GPP requirement first pause sequence also should maintain 3 sec
delay. Added fix to wait for 3 sec time before sending DTMF digits to
RIL.

Change-Id:I6041614b544cc9c24521bee9ad505439b810ff86CRs-Fixed: 211036*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmConnection.java b/src/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 59aa12a..9df8b66 100644

//Synthetic comment -- @@ -565,24 +565,19 @@
owner.cm.sendDtmf(c, h.obtainMessage(EVENT_DTMF_DONE));
} else if (c == PhoneNumberUtils.PAUSE) {
// From TS 22.101:
            // It continues...
            // Upon the called party answering the UE shall send the DTMF digits
            // automatically to the network after a delay of 3 seconds(± 20 %).
            // The digits shall be sent according to the procedures and timing
            // specified in 3GPP TS 24.008 [13]. The first occurrence of the
            // "DTMF Control Digits Separator" shall be used by the ME to
            // distinguish between the addressing digits (i.e. the phone number)
            // and the DTMF digits. Upon subsequent occurrences of the
            // separator,
            // the UE shall pause again for 3 seconds (± 20 %) before sending
            // any further DTMF digits.
            h.sendMessageDelayed(h.obtainMessage(EVENT_PAUSE_DONE),
                    PAUSE_DELAY_MILLIS);
} else if (c == PhoneNumberUtils.WAIT) {
setPostDialState(PostDialState.WAIT);
} else if (c == PhoneNumberUtils.WILD) {







