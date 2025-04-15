/*Telephony: Fix radio state printing

Decode new radio state before printing its value

Change-Id:Ib6d47fd412e85a7799fe56a49d29af7531de8674*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 569ac5c..a9a4be2 100644

//Synthetic comment -- @@ -1961,26 +1961,30 @@
sendScreenState(true);
}

    private void setRadioStateFromRILInt(int state) {
        RadioState newState;

/* RIL_RadioState ril.h */
        switch(state) {
            case 0: newState = RadioState.RADIO_OFF; break;
            case 1: newState = RadioState.RADIO_UNAVAILABLE; break;
            case 2: newState = RadioState.SIM_NOT_READY; break;
            case 3: newState = RadioState.SIM_LOCKED_OR_ABSENT; break;
            case 4: newState = RadioState.SIM_READY; break;
            case 5: newState = RadioState.RUIM_NOT_READY; break;
            case 6: newState = RadioState.RUIM_READY; break;
            case 7: newState = RadioState.RUIM_LOCKED_OR_ABSENT; break;
            case 8: newState = RadioState.NV_NOT_READY; break;
            case 9: newState = RadioState.NV_READY; break;

default:
throw new RuntimeException(
                            "Unrecognized RIL_RadioState: " +state);
}

if (mInitialRadioStateChange) {
if (newState.isOn()) {
//Synthetic comment -- @@ -2361,9 +2365,10 @@
switch(response) {
case RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED:
/* has bonus radio state int */
                setRadioStateFromRILInt(p.readInt());

                if (RILJ_LOGD) unsljLogMore(response, mState.toString());
break;
case RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED:
if (RILJ_LOGD) unsljLog(response);







