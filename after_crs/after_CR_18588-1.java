/*3s Wait time introduced before sending first DTMF digit

Added fix to wait for 3 sec interval before sending
the first DTMF digit to RIL as per TS 22.101 A.20

Change-Id:I711f0b72fafebad06dffc8369d12a7c2d4aec85d*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java b/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 7dc2504..7bfa2ae 100644

//Synthetic comment -- @@ -537,25 +537,19 @@
if (PhoneNumberUtils.is12Key(c)) {
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
	    // and the DTMF digits. Upon subsequent occurrences of the separator,
	    // the UE shall pause again for 3 seconds (± 20 %) before sending
	    // any further DTMF digits.
	    h.sendMessageDelayed(h.obtainMessage(EVENT_PAUSE_DONE),
PAUSE_DELAY_MILLIS);
} else if (c == PhoneNumberUtils.WAIT) {
setPostDialState(PostDialState.WAIT);
} else if (c == PhoneNumberUtils.WILD) {







