/*Fix phone crash in BT MT call disconnect, enable mute/hold in GSM.

When phone is in global mode and acquires CDMA signal first, it will start an
OTA call. OTA variables get initiazed which are only valid in CDMA. On switching
to GSM/UMTS these variables are not cleared. MUTE/HOLD options in InCallMenu are
enabled/disabled based on these variables and hence MUTE/HOLD options are
disabled for normal GSM calls. Similarly while disconnecting the MT call in BT,
the OTA vars are checked and some processing is done which leads to phone crash.

To fix these issues, phone type is checked for CDMA before using OTA data. OTA
variables are cleaned up and call screen is cleaned up when there is a
radio technology change.*/




//Synthetic comment -- diff --git a/src/com/android/phone/InCallMenu.java b/src/com/android/phone/InCallMenu.java
//Synthetic comment -- index ff5ee12..41cd12b 100755

//Synthetic comment -- @@ -275,7 +275,8 @@
final boolean hasHoldingCall = !phone.getBackgroundCall().isIdle();

// For OTA call, only show dialpad, endcall, speaker, and mute menu items
        if (hasActiveCall && (phone.getPhoneType() == Phone.PHONE_TYPE_CDMA) &&
                (PhoneApp.getInstance().isOtaCallInActiveState())) {
mAnswerAndHold.setVisible(false);
mAnswerAndHold.setEnabled(false);
mAnswerAndEnd.setVisible(false);








//Synthetic comment -- diff --git a/src/com/android/phone/InCallScreen.java b/src/com/android/phone/InCallScreen.java
//Synthetic comment -- index 1ff2f30..2e6d629 100755

//Synthetic comment -- @@ -1106,6 +1106,11 @@

/* package */ void updateAfterRadioTechnologyChange() {
if (DBG) Log.d(LOG_TAG, "updateAfterRadioTechnologyChange()...");

        // Reset the call screen since the calls can be transferred
        // across radio technologies.
        resetInCallScreenMode();

// Unregister for all events from the old obsolete phone
unregisterForPhoneStates();









//Synthetic comment -- diff --git a/src/com/android/phone/PhoneApp.java b/src/com/android/phone/PhoneApp.java
//Synthetic comment -- index dde9b84..a00f5ff 100755

//Synthetic comment -- @@ -694,11 +694,12 @@
*/
void dismissCallScreen() {
if (mInCallScreen != null) {
            if ((phone.getPhoneType() == Phone.PHONE_TYPE_CDMA) &&
                    (mInCallScreen.isOtaCallInActiveState()
|| mInCallScreen.isOtaCallInEndState()
|| ((cdmaOtaScreenState != null)
&& (cdmaOtaScreenState.otaScreenState
                            != CdmaOtaScreenState.OtaScreenState.OTA_STATUS_UNDEFINED)))) {
// TODO(Moto): During OTA Call, display should not become dark to
// allow user to see OTA UI update. Phone app needs to hold a SCREEN_DIM_WAKE_LOCK
// wake lock during the entire OTA call.
//Synthetic comment -- @@ -1245,6 +1246,12 @@
if (cdmaOtaScreenState == null) {
cdmaOtaScreenState = new OtaUtils.CdmaOtaScreenState();
}
            if (cdmaOtaInCallScreenUiState == null) {
                cdmaOtaInCallScreenUiState = new OtaUtils.CdmaOtaInCallScreenUiState();
            }
        } else {
            //Clean up OTA data in GSM/UMTS. It is valid only for CDMA
            clearOtaState();
}

ringer.updateRingerContextAfterRadioTechnologyChange(this.phone);







