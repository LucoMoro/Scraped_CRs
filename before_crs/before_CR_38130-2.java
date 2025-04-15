/*resolve the issue that the incoming call ring UI disappear after going to SMS and back repeatedly.

[ROOT Cause]
	original Google source code do not show the incoming call UI some times even if the phone is still in the RINGING state and Dialogs is canced.
[Solution description]
	to resolve the issue,force show the incoming call UI again when the phone is still in the RINGING state and the Dialogs is canced.

Change-Id:Ic62d4b53f0cebac77b3bb9000e519d9f4a2f7a6bSigned-off-by: Robin Hu <bin.hu@ck-telecom.com>*/
//Synthetic comment -- diff --git a/src/com/android/phone/InCallScreen.java b/src/com/android/phone/InCallScreen.java
//Synthetic comment -- index 26733ed..b772da6 100755

//Synthetic comment -- @@ -156,8 +156,9 @@
private static final int EVENT_PAUSE_DIALOG_COMPLETE = 120;
private static final int EVENT_HIDE_PROVIDER_OVERLAY = 121;  // Time to remove the overlay.
private static final int REQUEST_UPDATE_SCREEN = 122;
    private static final int PHONE_INCOMING_RING = 123;
    private static final int PHONE_NEW_RINGING_CONNECTION = 124;

// When InCallScreenMode is UNDEFINED set the default action
// to ACTION_UNDEFINED so if we are resumed the activity will
//Synthetic comment -- @@ -417,6 +418,9 @@
updateScreen();
break;

case PHONE_INCOMING_RING:
onIncomingRing();
break;
//Synthetic comment -- @@ -2172,7 +2176,21 @@

mWildPromptText.requestFocus();
}

/**
* Updates the state of the in-call UI based on the current state of
* the Phone.  This call has no effect if we're not currently the
//Synthetic comment -- @@ -2184,7 +2202,7 @@
* to be updated (rather than doing it synchronously), call
* requestUpdateScreen() instead.
*/
    private void updateScreen() {
if (DBG) log("updateScreen()...");
final InCallScreenMode inCallScreenMode = mApp.inCallUiState.inCallScreenMode;
if (VDBG) {
//Synthetic comment -- @@ -2245,7 +2263,7 @@
// Note we update the InCallTouchUi widget before the CallCard,
// since the CallCard adjusts its size based on how much vertical
// space the InCallTouchUi widget needs.
        updateInCallTouchUi();
mCallCard.updateState(mCM);
updateDialpadVisibility();
updateProviderOverlay();
//Synthetic comment -- @@ -3925,13 +3943,18 @@
mRespondViaSmsManager = new RespondViaSmsManager();
mRespondViaSmsManager.setInCallScreenInstance(this);
}

/**
* Updates the state of the in-call touch UI.
*/
    private void updateInCallTouchUi() {
if (mInCallTouchUi != null) {
            mInCallTouchUi.updateState(mCM);
}
}

//Synthetic comment -- @@ -3941,7 +3964,6 @@
/* package */ InCallTouchUi getInCallTouchUi() {
return mInCallTouchUi;
}

/**
* Posts a handler message telling the InCallScreen to refresh the
* onscreen in-call UI.
//Synthetic comment -- @@ -3958,7 +3980,23 @@
mHandler.removeMessages(REQUEST_UPDATE_SCREEN);
mHandler.sendEmptyMessage(REQUEST_UPDATE_SCREEN);
}

/**
* @return true if it's OK to display the in-call touch UI, given the
* current state of the InCallScreen.








//Synthetic comment -- diff --git a/src/com/android/phone/InCallTouchUi.java b/src/com/android/phone/InCallTouchUi.java
old mode 100644
new mode 100755
//Synthetic comment -- index f8ddc8f..8435f15

//Synthetic comment -- @@ -226,12 +226,20 @@
View.OnTouchListener smallerHitTargetTouchListener = new SmallerHitTargetTouchListener();
mEndButton.setOnTouchListener(smallerHitTargetTouchListener);
}

/**
* Updates the visibility and/or state of our UI elements, based on
* the current state of the phone.
*/
    void updateState(CallManager cm) {
if (mInCallScreen == null) {
log("- updateState: mInCallScreen has been destroyed; bailing out...");
return;
//Synthetic comment -- @@ -270,7 +278,7 @@
// within the last 500 msec, *don't* show the incoming call
// UI even if the phone is still in the RINGING state.
long now = SystemClock.uptimeMillis();
            if (now < mLastIncomingCallActionTime + 500) {
log("updateState: Too soon after last action; not drawing!");
showIncomingCallControls = false;
}








//Synthetic comment -- diff --git a/src/com/android/phone/RespondViaSmsManager.java b/src/com/android/phone/RespondViaSmsManager.java
old mode 100644
new mode 100755
//Synthetic comment -- index 451198f..31a6010

//Synthetic comment -- @@ -281,7 +281,7 @@
// update itself based on the current telephony state.
// (Assuming the incoming call is still ringing, this will
// cause the incoming call widget to reappear.)
            mInCallScreen.requestUpdateScreen();
}
}








