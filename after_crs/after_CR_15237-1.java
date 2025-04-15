/*When ICC Card is not used broadcast SIM state changed intent accordingly.

If NV is used in CDMA, ICC card is not used. In global mode phones,
when user enables and disables airplane mode, SIM card could be shutdown which
might cause "NO SIM/NO RUIM" message to pop up on the screen even though the
phone is in CDMA using NV. Hence SIM_STATE_CHANGED message should be ignored
when phone is using NV.

During ICC card status change, if the radio state is NV_READY then
ACTION_SIM_STATE_CHANGED intent will be broadcasted with INTENT_VALUE_ICC_UNUSED
value.

Change-Id:I85dacb17b52badcee1249301793f0d8cef44e00c*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCard.java b/telephony/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index 0f76633..5bf19b8 100644

//Synthetic comment -- @@ -72,6 +72,8 @@
static public final String INTENT_VALUE_LOCKED_ON_PUK = "PUK";
/* NETWORK means ICC is locked on NETWORK PERSONALIZATION */
static public final String INTENT_VALUE_LOCKED_NETWORK = "NETWORK";
    /* UNUSED means the ICC state not used (eg, nv ready) */
    static public final String INTENT_VALUE_ICC_UNUSED = "UNUSED";

protected static final int EVENT_ICC_LOCKED_OR_ABSENT = 1;
private static final int EVENT_GET_ICC_STATUS_DONE = 2;
//Synthetic comment -- @@ -469,6 +471,9 @@
public void broadcastIccStateChangedIntent(String value, String reason) {
Intent intent = new Intent(TelephonyIntents.ACTION_SIM_STATE_CHANGED);
intent.putExtra(Phone.PHONE_NAME_KEY, mPhone.getPhoneName());
        if (mPhone.mCM.getRadioState() == RadioState.NV_READY) {
            value = INTENT_VALUE_ICC_UNUSED;
        }
intent.putExtra(INTENT_KEY_ICC_STATE, value);
intent.putExtra(INTENT_KEY_LOCKED_REASON, reason);
if(mDbg) log("Broadcasting intent ACTION_SIM_STATE_CHANGED " +  value







