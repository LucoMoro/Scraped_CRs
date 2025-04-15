/*Phone: Enable audio & call related logs

Add logs when
-enabling/disabling MIC and Speaker
through AudioManager.
-for call related operations.
This would help in debugging call issues.

Change-Id:I48e09a8e68737444009a0deac3cb0550f8bd1ed8CRs-Fixed: 359921*/
//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 5845397..57b4206 100755

//Synthetic comment -- @@ -2357,13 +2357,13 @@
if (phoneType == PhoneConstants.PHONE_TYPE_CDMA) {
if (ringingCall.isRinging()) {
// Hangup the active call and then answer call waiting call.
                                if (VDBG) log("CHLD:1 Callwaiting Answer call");
PhoneUtils.hangupRingingAndActive(phone);
} else {
// If there is no Call waiting then just hangup
// the active call. In CDMA this mean that the complete
// call session would be ended
                                if (VDBG) log("CHLD:1 Hangup Call");
PhoneUtils.hangup(PhoneApp.getInstance().mCM);
}
return new AtCommandResult(AtCommandResult.OK);
//Synthetic comment -- @@ -2387,7 +2387,7 @@
// If the Phone state is already in CONF_CALL then we simply send
// a flash cmd by calling switchHoldingAndActive()
if (ringingCall.isRinging()) {
                                if (VDBG) log("CHLD:2 Callwaiting Answer call");
PhoneUtils.answerCall(ringingCall);
PhoneUtils.setMute(false);
// Setting the second callers state flag to TRUE (i.e. active)
//Synthetic comment -- @@ -2395,7 +2395,7 @@
} else if (PhoneApp.getInstance().cdmaPhoneCallState
.getCurrentCallState()
== CdmaPhoneCallState.PhoneCallState.CONF_CALL) {
                                if (VDBG) log("CHLD:2 Swap Calls");
PhoneUtils.switchHoldingAndActive(backgroundCall);
// Toggle the second callers active state flag
cdmaSwapSecondCallState();
//Synthetic comment -- @@ -2413,7 +2413,7 @@
PhoneApp.getInstance().cdmaPhoneCallState.getCurrentCallState();
// For CDMA, we need to check if the call is in THRWAY_ACTIVE state
if (state == CdmaPhoneCallState.PhoneCallState.THRWAY_ACTIVE) {
                                if (VDBG) log("CHLD:3 Merge Calls");
PhoneUtils.mergeCalls();
} else if (state == CdmaPhoneCallState.PhoneCallState.CONF_CALL) {
// State is CONF_CALL already and we are getting a merge call








//Synthetic comment -- diff --git a/src/com/android/phone/CallCard.java b/src/com/android/phone/CallCard.java
//Synthetic comment -- index 77702d1..efa9833 100644

//Synthetic comment -- @@ -445,7 +445,7 @@
* (ie. the stuff in the primaryCallInfo block) based on the specified Call.
*/
private void displayMainCallStatus(CallManager cm, Call call) {
        if (DBG) log("displayMainCallStatus(call " + call + ")...");

if (call == null) {
// There's no call to display, presumably because the phone is idle.
//Synthetic comment -- @@ -455,7 +455,7 @@
mPrimaryCallInfo.setVisibility(View.VISIBLE);

Call.State state = call.getState();
        if (DBG) log("  - call.state: " + call.getState());

switch (state) {
case ACTIVE:








//Synthetic comment -- diff --git a/src/com/android/phone/InCallControlState.java b/src/com/android/phone/InCallControlState.java
//Synthetic comment -- index e901cf1..268c212 100644

//Synthetic comment -- @@ -204,7 +204,7 @@
canHold = false;
}

        if (DBG) dumpState();
}

public void dumpState() {








//Synthetic comment -- diff --git a/src/com/android/phone/InCallScreen.java b/src/com/android/phone/InCallScreen.java
//Synthetic comment -- index 2cc22a0..8e100d1 100755

//Synthetic comment -- @@ -1165,7 +1165,7 @@
return;
}
String action = intent.getAction();
        if (DBG) log("internalResolveIntent: action=" + action);

// In gingerbread and earlier releases, the InCallScreen used to
// directly handle certain intent actions that could initiate phone
//Synthetic comment -- @@ -1709,8 +1709,8 @@
private void onDisconnect(AsyncResult r) {
Connection c = (Connection) r.result;
Connection.DisconnectCause cause = c.getDisconnectCause();
        if (DBG) log("onDisconnect: connection '" + c + "', cause = " + cause
                + ", showing screen: " + mApp.isShowingCallScreen());

boolean currentlyIdle = !phoneIsInUse();
int autoretrySetting = AUTO_RETRY_OFF;
//Synthetic comment -- @@ -2975,7 +2975,7 @@
* Handles button clicks from the InCallTouchUi widget.
*/
/* package */ void handleOnscreenButtonClick(int id) {
        if (DBG) log("handleOnscreenButtonClick(id " + id + ")...");

switch (id) {
// Actions while an incoming call is ringing:
//Synthetic comment -- @@ -3711,7 +3711,7 @@
* necessary.
*/
private void setInCallScreenMode(InCallScreenMode newMode) {
        if (DBG) log("setInCallScreenMode: " + newMode);
mApp.inCallUiState.inCallScreenMode = newMode;

switch (newMode) {








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneUtils.java b/src/com/android/phone/PhoneUtils.java
//Synthetic comment -- index 616bd40..909ca18 100644

//Synthetic comment -- @@ -1877,7 +1877,7 @@
* @param store True when the settings should be stored in the device.
*/
/* package */ static void turnOnSpeaker(Context context, boolean flag, boolean store) {
        if (DBG) log("turnOnSpeaker(flag=" + flag + ", store=" + store + ")...");
final PhoneApp app = PhoneApp.getInstance();

AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//Synthetic comment -- @@ -2012,10 +2012,10 @@
if (routeToAudioManager) {
AudioManager audioManager =
(AudioManager) phone.getContext().getSystemService(Context.AUDIO_SERVICE);
            if (DBG) log("setMuteInternal: using setMicrophoneMute(" + muted + ")...");
audioManager.setMicrophoneMute(muted);
} else {
            if (DBG) log("setMuteInternal: using phone.setMute(" + muted + ")...");
phone.setMute(muted);
}
app.notificationMgr.updateMuteNotification();







