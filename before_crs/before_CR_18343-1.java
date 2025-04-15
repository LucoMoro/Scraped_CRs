/*Support for ME De-Personalization.

Added support for all ME De-Personalizations as per specs
3GPP2 C.S0068-0 and 3GPP TS 22.022.

Change-Id:I0ff0a8ff3c183a548c7b136033c2c3bb94434b9d*/
//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardUpdateMonitor.java b/phone/com/android/internal/policy/impl/KeyguardUpdateMonitor.java
//Synthetic comment -- index b225e56..5f87d64 100644

//Synthetic comment -- @@ -123,11 +123,11 @@
this.simState = IccCard.State.PIN_REQUIRED;
} else if (IccCard.INTENT_VALUE_LOCKED_ON_PUK.equals(lockedReason)) {
this.simState = IccCard.State.PUK_REQUIRED;
} else {
this.simState = IccCard.State.UNKNOWN;
}
            } else if (IccCard.INTENT_VALUE_LOCKED_NETWORK.equals(stateExtra)) {
                this.simState = IccCard.State.NETWORK_LOCKED;
} else {
this.simState = IccCard.State.UNKNOWN;
}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/LockScreen.java b/phone/com/android/internal/policy/impl/LockScreen.java
//Synthetic comment -- index a5ef1fa..e775d49 100644

//Synthetic comment -- @@ -103,9 +103,9 @@
Normal(true),

/**
         * The sim card is 'network locked'.
*/
        NetworkLocked(true),

/**
* The sim card is missing.
//Synthetic comment -- @@ -481,8 +481,8 @@
switch (simState) {
case ABSENT:
return Status.SimMissing;
            case NETWORK_LOCKED:
                return Status.SimMissingLocked;
case NOT_READY:
return Status.SimMissing;
case PIN_REQUIRED:
//Synthetic comment -- @@ -522,13 +522,13 @@
mSelector.setVisibility(View.VISIBLE);
mEmergencyCallText.setVisibility(View.GONE);
break;
            case NetworkLocked:
// The carrier string shows both sim card status (i.e. No Sim Card) and
// carrier's name and/or "Emergency Calls Only" status
mCarrier.setText(
getCarrierString(
mUpdateMonitor.getTelephonyPlmn(),
                                getContext().getText(R.string.lockscreen_network_locked_message)));
mScreenLocked.setText(R.string.lockscreen_instructions_when_pattern_disabled);

// layout







