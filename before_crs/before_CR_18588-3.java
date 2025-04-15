/*3s Wait time introduced before sending first DTMF digit

Added fix to wait for 3 sec interval before sending
the first DTMF digit to RIL as per TS 22.101 A.20

Change-Id:I711f0b72fafebad06dffc8369d12a7c2d4aec85d*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java b/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 7dc2504..b785b56 100644

//Synthetic comment -- @@ -86,7 +86,6 @@
static final int EVENT_WAKE_LOCK_TIMEOUT = 4;

//***** Constants
    static final int PAUSE_DELAY_FIRST_MILLIS = 100;
static final int PAUSE_DELAY_MILLIS = 3 * 1000;
static final int WAKE_LOCK_TIMEOUT_MILLIS = 60*1000;

//Synthetic comment -- @@ -537,25 +536,19 @@
if (PhoneNumberUtils.is12Key(c)) {
owner.cm.sendDtmf(c, h.obtainMessage(EVENT_DTMF_DONE));
} else if (c == PhoneNumberUtils.PAUSE) {
            // From TS 22.101:

            // "The first occurrence of the "DTMF Control Digits Separator"
            //  shall be used by the ME to distinguish between the addressing
            //  digits (i.e. the phone number) and the DTMF digits...."

            if (nextPostDialChar == 1) {
                // The first occurrence.
                // We don't need to pause here, but wait for just a bit anyway
                h.sendMessageDelayed(h.obtainMessage(EVENT_PAUSE_DONE),
                                            PAUSE_DELAY_FIRST_MILLIS);
            } else {
                // It continues...
                // "Upon subsequent occurrences of the separator, the UE shall
                //  pause again for 3 seconds (\u00B1 20 %) before sending any
                //  further DTMF digits."
                h.sendMessageDelayed(h.obtainMessage(EVENT_PAUSE_DONE),
                                            PAUSE_DELAY_MILLIS);
            }
} else if (c == PhoneNumberUtils.WAIT) {
setPostDialState(PostDialState.WAIT);
} else if (c == PhoneNumberUtils.WILD) {







