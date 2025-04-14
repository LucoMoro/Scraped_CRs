Extend ANDROID with CDMA mobile technology support - With review comments of Base Class Rename Fixed.

This project has the goal to extend the Android telephony layers with CDMA
mobile technology support.
The current release 1 of Android supports GSM/WCDMA as mobile communication
standards.
Our contribution will contain changes in the phone related applications, the
application framework telephony packages and in the RIL daemon library space.
The implementation of the CDMA support requires architectural changes in the
telephony package and extensions of the RIL interface.
The application interface (SDK interface) will be extended to provide
CDMA specific features/information to the phone related application and other
applications.
Where ever possible the actual used radio technology is transparent for the
application using mobile connections.

Each increment of the contribution will provide a pre-tested set of use case
implementations.
The final contribution will support CDMA functionality for Android phones
supporting
either CDMA mobile technology only or a world mode including GSM/WCDMA and CDMA.
The following CDMA technologies are considered: IS-95, CDMA2000 1xRTT, CDMA2000
1x EVDO.

This contribution implements the following use cases:
UC Startup-Phone
UC Initialize Phone
UC Access SIM/RUIM
UC Network Indications
UC Mobile Originated Call
UC Mobile Terminated Call
UC Network / Phone Settings
UC Supplementary Services (partly)

With these use cases the phone will
- start up,
- access the CDMA subscription and other information from memory of from the card (either SIM, USIM or RUIM),
- register to the network,
- provides registration status to the application for displaying
- be able to handle incoming and outgoing voice calls,
- provide phone and call settings in the settings application
- provide supplementary services in the settings application

Various review comments are also fixed with this contribution.

Approved By :- Aravind Mahishi , aravind.mahishi@teleca.com
           Wolfgang Schmidt, wolfgang.schmidt@teleca.com




diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
index 341cc7b..e39ce32 100644

@@ -39,8 +39,8 @@
import android.provider.CallLog.Calls;
import android.provider.Contacts.Phones;
import android.provider.Contacts.PhonesColumns;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.TelephonyIntents;
import android.telephony.PhoneNumberUtils;
@@ -74,9 +74,9 @@
private ScoSocket mOutgoingSco;
private ScoSocket mConnectedSco;

    private Call mForegroundCall;
    private Call mBackgroundCall;
    private Call mRingingCall;

private AudioManager mAudioManager;
private PowerManager mPowerManager;
@@ -242,9 +242,9 @@
/* returns true if there is some kind of in-call audio we may wish to route
* bluetooth to */
private boolean isIncallAudio() {
        Call.State state = mForegroundCall.getState();

        return (state == Call.State.ACTIVE || state == Call.State.ALERTING);
}

/* package */ void disconnectHeadset() {
@@ -349,9 +349,9 @@
updateServiceState(sendUpdate(), state);
break;
case PHONE_STATE_CHANGED:
                    Connection connection = null;
                    if (((AsyncResult) msg.obj).result instanceof Connection) {
                        connection = (Connection) ((AsyncResult) msg.obj).result;
}
updatePhoneState(sendUpdate(), connection);
break;
@@ -487,7 +487,7 @@
sendURC(result.toString());
}

        private synchronized void updatePhoneState(boolean sendUpdate, Connection connection) {
int call = 0;
int callsetup = 0;
int callheld = 0;
@@ -943,9 +943,9 @@
private synchronized AtCommandResult getClccResult() {
// Collect all known connections
// indexed by CLCC index
        Connection[] clccConnections = new Connection[MAX_CONNECTIONS];
        LinkedList<Connection> newConnections = new LinkedList<Connection>();
        LinkedList<Connection> connections = new LinkedList<Connection>();
if (mRingingCall.getState().isAlive()) {
connections.addAll(mRingingCall.getConnections());
}
@@ -962,7 +962,7 @@
clccUsed[i] = mClccUsed[i];
mClccUsed[i] = false;
}
        for (Connection c : connections) {
boolean found = false;
long timestamp = c.getCreateTime();
for (int i = 0; i < MAX_CONNECTIONS; i++) {
@@ -985,7 +985,7 @@
while (mClccUsed[i]) i++;
// Find earliest connection
long earliestTimestamp = newConnections.get(0).getCreateTime();
            Connection earliestConnection = newConnections.get(0);
for (int j = 0; j < newConnections.size(); j++) {
long timestamp = newConnections.get(j).getCreateTime();
if (timestamp < earliestTimestamp) {
@@ -1016,7 +1016,7 @@
}

/** Convert a Connection object into a single +CLCC result */
    private String connectionToClccEntry(int index, Connection c) {
int state;
switch (c.getState()) {
case ACTIVE:
@@ -1042,7 +1042,7 @@
}

int mpty = 0;
        Call call = c.getCall();
if (call != null) {
mpty = call.isMultiparty() ? 1 : 0;
}








diff --git a/src/com/android/phone/BluetoothHeadsetService.java b/src/com/android/phone/BluetoothHeadsetService.java
index 2f0c14e..3dc43ca 100644

@@ -39,7 +39,7 @@
import android.os.PowerManager;
import android.os.SystemProperties;
import android.os.SystemService;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import android.util.Log;
@@ -70,8 +70,8 @@
private String mHeadsetAddress;
private IBluetoothHeadsetCallback mConnectHeadsetCallback;
private String mLastHeadsetAddress;
    private Call mForegroundCall;
    private Call mRingingCall;
private Phone mPhone;

public BluetoothHeadsetService() {








diff --git a/src/com/android/phone/CallCard.java b/src/com/android/phone/CallCard.java
index ade0476..c3e3854 100644

@@ -16,10 +16,10 @@

package com.android.phone;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallerInfo;
import com.android.internal.telephony.CallerInfoAsyncQuery;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;

import android.content.ContentUris;
@@ -219,8 +219,8 @@
private void updateForegroundCall(Phone phone) {
if (DBG) log("updateForegroundCall()...");

        Call fgCall = phone.getForegroundCall();
        Call bgCall = phone.getBackgroundCall();

if (fgCall.isIdle() && !fgCall.hasConnections()) {
if (DBG) log("updateForegroundCall: no active call, show holding call");
@@ -244,9 +244,9 @@
private void updateRingingCall(Phone phone) {
if (DBG) log("updateRingingCall()...");

        Call ringingCall = phone.getRingingCall();
        Call fgCall = phone.getForegroundCall();
        Call bgCall = phone.getBackgroundCall();

displayMainCallStatus(phone, ringingCall);
displayOnHoldCallStatus(phone, bgCall);
@@ -257,11 +257,11 @@
* Updates the main block of caller info on the CallCard
* (ie. the stuff in the mainCallCard block) based on the specified Call.
*/
    private void displayMainCallStatus(Phone phone, Call call) {
if (DBG) log("displayMainCallStatus(phone " + phone
+ ", call " + call + ", state" + call.getState() + ")...");

        Call.State state = call.getState();
int callCardBackgroundResid = 0;

// Background frame resources are different between portrait/landscape:
@@ -352,7 +352,7 @@
} else {
// Update onscreen info for a regular call (which presumably
// has only one connection.)
            Connection conn = call.getEarliestConnection();

boolean isPrivateNumber = false; // TODO: need isPrivate() API

@@ -425,12 +425,12 @@
public void onQueryComplete(int token, Object cookie, CallerInfo ci) {
if (DBG) log("onQueryComplete: token " + token + ", cookie " + cookie + ", ci " + ci);

        if (cookie instanceof Call) {
// grab the call object and update the display for an individual call,
// as well as the successive call to update image via call state.
// If the object is a textview instead, we update it as we need to.
if (DBG) log("callerinfo query complete, updating ui from displayMainCallStatus()");
            Call call = (Call) cookie;
updateDisplayForPerson(ci, false, false, call);
updatePhotoForCallState(call);

@@ -447,16 +447,16 @@
public void onImageLoadComplete(int token, Object cookie, ImageView iView,
boolean imagePresent){
if (cookie != null) {
            updatePhotoForCallState((Call) cookie);
}
}

/**
* Updates the "upper" and "lower" titles based on the current state of this call.
*/
    private void updateCardTitleWidgets(Phone phone, Call call) {
if (DBG) log("updateCardTitleWidgets(call " + call + ")...");
        Call.State state = call.getState();

// TODO: Still need clearer spec on exactly how title *and* status get
// set in all states.  (Then, given that info, refactor the code
@@ -470,7 +470,7 @@

// We display *either* the "upper title" or the "lower title", but
// never both.
        if (state == Call.State.ACTIVE) {
// Use the "lower title" (in green).
mLowerTitleViewGroup.setVisibility(View.VISIBLE);
mLowerTitleIcon.setImageResource(R.drawable.ic_incall_ongoing);
@@ -478,7 +478,7 @@
mLowerTitle.setTextColor(mTextColorConnected);
mElapsedTime.setTextColor(mTextColorConnected);
mUpperTitle.setText("");
        } else if (state == Call.State.DISCONNECTED) {
// Use the "lower title" (in red).
// TODO: We may not *always* want to use the lower title for
// the DISCONNECTED state.  "Error" states like BUSY or
@@ -502,7 +502,7 @@
// the "Call ended" state.  (In that case, don't touch the
// mElapsedTime widget, so we continue to see the elapsed time of
// the call that just ended.)
        if (call.getState() == Call.State.DISCONNECTED) {
// "Call ended" state -- don't touch the onscreen elapsed time.
} else {
long duration = CallTime.getCallDuration(call);  // msec
@@ -531,9 +531,9 @@
* "Dialing" or "In call" or "On hold".  A null return value means that
* there's no title string for this state.
*/
    private String getTitleForCallCard(Call call) {
String retVal = null;
        Call.State state = call.getState();
Context context = getContext();
int resId;

@@ -581,14 +581,14 @@
* Or, clear out the "on hold" box if the specified call
* is null or idle.
*/
    private void displayOnHoldCallStatus(Phone phone, Call call) {
if (DBG) log("displayOnHoldCallStatus(call =" + call + ")...");
if (call == null) {
mOtherCallOnHoldInfoArea.setVisibility(View.GONE);
return;
}

        Call.State state = call.getState();
switch (state) {
case HOLDING:
// Ok, there actually is a background call on hold.
@@ -637,14 +637,14 @@
* Or, clear out the "ongoing call" box if the specified call
* is null or idle.
*/
    private void displayOngoingCallStatus(Phone phone, Call call) {
if (DBG) log("displayOngoingCallStatus(call =" + call + ")...");
if (call == null) {
mOtherCallOngoingInfoArea.setVisibility(View.GONE);
return;
}

        Call.State state = call.getState();
switch (state) {
case ACTIVE:
case DIALING:
@@ -687,9 +687,9 @@
}


    private String getCallFailedString(Call call) {
Phone phone = PhoneApp.getInstance().phone;
        Connection c = call.getEarliestConnection();
int resID;

if (c == null) {
@@ -699,7 +699,7 @@
resID = R.string.card_title_call_ended;
} else {

            Connection.DisconnectCause cause = c.getDisconnectCause();

// TODO: The card *title* should probably be "Call ended" in all
// cases, but if the DisconnectCause was an error condition we should
@@ -771,7 +771,7 @@
*  updateImageViewWithContactPhotoAsync call will need to use it.
*/

    private void updateDisplayForPerson(CallerInfo info, boolean isPrivateNumber, Call call) {
updateDisplayForPerson(info, isPrivateNumber, false, call);
}

@@ -785,7 +785,7 @@
private void updateDisplayForPerson(CallerInfo info,
boolean isPrivateNumber,
boolean isTemporary,
                                        Call call) {
if (DBG) log("updateDisplayForPerson(" + info + ")...");

// inform the state machine that we are displaying a photo.
@@ -919,25 +919,25 @@
* the generic "picture_unknown" image, or the "conference call"
* image.)
*/
    private void updatePhotoForCallState(Call call) {
if (DBG) log("updatePhotoForCallState(" + call + ")...");
int photoImageResource = 0;

// Check for the (relatively few) telephony states that need a
// special image in the "photo" slot.
        Call.State state = call.getState();
switch (state) {
case DISCONNECTED:
// Display the special "busy" photo for BUSY or CONGESTION.
// Otherwise (presumably the normal "call ended" state)
// leave the photo alone.
                Connection c = call.getEarliestConnection();
// if the connection is null, we assume the default case,
// otherwise update the image resource normally.
if (c != null) {
                    Connection.DisconnectCause cause = c.getDisconnectCause();
                    if ((cause == Connection.DisconnectCause.BUSY)
                        || (cause == Connection.DisconnectCause.CONGESTION)) {
photoImageResource = R.drawable.picture_busy;
}
} else if (DBG) {
@@ -970,7 +970,7 @@

// look for the photoResource if it is available.
CallerInfo ci = null; {
                    Connection conn = call.getEarliestConnection();
if (conn != null) {
Object o = conn.getUserData();
if (o instanceof CallerInfo) {








diff --git a/src/com/android/phone/CallNotifier.java b/src/com/android/phone/CallNotifier.java
index db31218..30caa85 100644

@@ -16,10 +16,10 @@

package com.android.phone;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallerInfo;
import com.android.internal.telephony.CallerInfoAsyncQuery;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.gsm.GSMPhone;

@@ -179,7 +179,7 @@
};

private void onNewRingingConnection(AsyncResult r) {
        Connection c = (Connection) r.result;
if (DBG) log("onNewRingingConnection()... connection: " + c);
PhoneApp app = PhoneApp.getInstance();

@@ -203,7 +203,7 @@
}

if (c != null && c.isRinging()) {
            Call.State state = c.getState();
// State will be either INCOMING or WAITING.
if (DBG) log("- connection is ringing!  state = " + state);
// if (DBG) PhoneUtils.dumpCallState(mPhone);
@@ -231,7 +231,7 @@

// - don't ring for call waiting connections
// - do this before showing the incoming call panel
            if (state == Call.State.INCOMING) {
PhoneUtils.setAudioControlState(PhoneUtils.AUDIO_RINGING);
startIncomingCallQuery(c);
} else {
@@ -263,7 +263,7 @@
/**
* Helper method to manage the start of incoming call queries
*/
    private void startIncomingCallQuery(Connection c) {
// TODO: cache the custom ringer object so that subsequent
// calls will not need to do this query work.  We can keep
// the MRU ringtones in memory.  We'll still need to hit
@@ -448,7 +448,7 @@
PhoneUtils.setAudioControlState(PhoneUtils.AUDIO_IDLE);
}

        Connection c = (Connection) r.result;
if (DBG && c != null) {
log("- cause = " + c.getDisconnectCause()
+ ", incoming = " + c.isIncoming()
@@ -470,11 +470,11 @@

// The "Busy" or "Congestion" tone is the highest priority:
if (c != null) {
            Connection.DisconnectCause cause = c.getDisconnectCause();
            if (cause == Connection.DisconnectCause.BUSY) {
if (DBG) log("- need to play BUSY tone!");
toneToPlay = InCallTonePlayer.TONE_BUSY;
            } else if (cause == Connection.DisconnectCause.CONGESTION) {
if (DBG) log("- need to play CONGESTION tone!");
toneToPlay = InCallTonePlayer.TONE_CONGESTION;
}
@@ -490,9 +490,9 @@
if ((toneToPlay == InCallTonePlayer.TONE_NONE)
&& (mPhone.getState() == Phone.State.IDLE)
&& (c != null)) {
            Connection.DisconnectCause cause = c.getDisconnectCause();
            if ((cause == Connection.DisconnectCause.NORMAL)  // remote hangup
                || (cause == Connection.DisconnectCause.LOCAL)) {  // local hangup
if (DBG) log("- need to play CALL_ENDED tone!");
toneToPlay = InCallTonePlayer.TONE_CALL_ENDED;
}
@@ -521,12 +521,12 @@
boolean isPrivateNumber = false; // TODO: need API for isPrivate()
long date = c.getCreateTime();
long duration = c.getDurationMillis();
            Connection.DisconnectCause cause = c.getDisconnectCause();

// Set the "type" to be displayed in the call log (see constants in CallLog.Calls)
int callLogType;
if (c.isIncoming()) {
                callLogType = (cause == Connection.DisconnectCause.INCOMING_MISSED ?
CallLog.Calls.MISSED_TYPE :
CallLog.Calls.INCOMING_TYPE);
} else {








diff --git a/src/com/android/phone/CallTime.java b/src/com/android/phone/CallTime.java
index 489043b..39a69af 100644

@@ -20,8 +20,8 @@
import android.os.Debug;
import android.os.Handler;
import android.os.SystemClock;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Connection;
import android.util.Log;

import java.io.File;
@@ -42,7 +42,7 @@

private static int sProfileState = PROFILE_STATE_NONE;

    private Call mCall;
private long mLastReportedTime;
private boolean mTimerRunning;
private long mInterval;
@@ -66,7 +66,7 @@
* After calling this you should also call reset() and
* periodicUpdateTimer() to get the timer started.
*/
    /* package */ void setActiveCallMode(Call call) {
if (DBG) log("setActiveCallMode(" + call + ")...");
mCall = call;

@@ -95,9 +95,9 @@
mLastReportedTime = nextReport;

if (mCall != null) {
                Call.State state = mCall.getState();

                if (state == Call.State.ACTIVE) {
updateElapsedTime(mCall);
}
}
@@ -116,7 +116,7 @@
mTimerRunning = false;
}

    private void updateElapsedTime(Call call) {
if (mListener != null) {
long duration = getCallDuration(call);
mListener.onTickForCallTimeElapsed(duration / 1000);
@@ -127,20 +127,20 @@
* Returns a "call duration" value for the specified Call, in msec,
* suitable for display in the UI.
*/
    /* package */ static long getCallDuration(Call call) {
long duration = 0;
List connections = call.getConnections();
int count = connections.size();
        Connection c;

if (count == 1) {
            c = (Connection) connections.get(0);
//duration = (state == Call.State.ACTIVE
//            ? c.getDurationMillis() : c.getHoldDurationMillis());
duration = c.getDurationMillis();
} else {
for (int i = 0; i < count; i++) {
                c = (Connection) connections.get(i);
//long t = (state == Call.State.ACTIVE
//          ? c.getDurationMillis() : c.getHoldDurationMillis());
long t = c.getDurationMillis();








diff --git a/src/com/android/phone/DTMFTwelveKeyDialer.java b/src/com/android/phone/DTMFTwelveKeyDialer.java
index f7b1bf0..15dbe8e 100644

@@ -17,10 +17,10 @@
package com.android.phone;


import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallerInfo;
import com.android.internal.telephony.CallerInfoAsyncQuery;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.widget.SlidingDrawer;

@@ -474,9 +474,9 @@
android.R.drawable.stat_sys_phone_call;

// get the current connected call.
        Call currentCall = hasActiveCall ? mPhone.getForegroundCall()
: mPhone.getBackgroundCall();
        Connection currentConn = currentCall.getEarliestConnection();

// update the information about the current connection (chronometer)
// only if the current connection exists.








diff --git a/src/com/android/phone/InCallMenu.java b/src/com/android/phone/InCallMenu.java
index 3a50433..dbdec04 100644

@@ -17,7 +17,7 @@
package com.android.phone;

import android.content.Context;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Phone;
import android.util.Log;
import android.view.ContextThemeWrapper;
@@ -237,7 +237,7 @@

final boolean hasRingingCall = !phone.getRingingCall().isIdle();
final boolean hasActiveCall = !phone.getForegroundCall().isIdle();
        final Call.State fgCallState = phone.getForegroundCall().getState();
final boolean hasHoldingCall = !phone.getBackgroundCall().isIdle();

// Special cases when an incoming call is ringing.
@@ -325,7 +325,7 @@
// (It's meaningless while on hold, or while DIALING/ALERTING.)
mMute.setVisible(true);
boolean muteOn = PhoneUtils.getMute(phone);
        boolean canMute = (fgCallState == Call.State.ACTIVE);
mMute.setIndicatorState(muteOn);
mMute.setEnabled(canMute);

@@ -337,7 +337,7 @@
mHold.setVisible(true);
boolean onHold = hasHoldingCall && !hasActiveCall;
boolean canHold = !((hasActiveCall && hasHoldingCall)
                            || (hasActiveCall && (fgCallState != Call.State.ACTIVE)));
mHold.setIndicatorState(onHold);
mHold.setEnabled(canHold);









diff --git a/src/com/android/phone/InCallScreen.java b/src/com/android/phone/InCallScreen.java
index 3c5ec6e..b126a79 100644

@@ -16,10 +16,10 @@

package com.android.phone;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallerInfo;
import com.android.internal.telephony.CallerInfoAsyncQuery;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;
import com.android.internal.widget.SlidingDrawer;
@@ -179,9 +179,9 @@
private boolean mRegisteredForPhoneStates;

private Phone mPhone;
    private Call mForegroundCall;
    private Call mBackgroundCall;
    private Call mRingingCall;

private BluetoothHandsfree mBluetoothHandsfree;
private BluetoothHeadset mBluetoothHeadset;  // valid only between onResume and onPause
@@ -1176,17 +1176,17 @@
* @param r r.result contains the connection that just ended
*/
private void onDisconnect(AsyncResult r) {
        Connection c = (Connection) r.result;
        Connection.DisconnectCause cause = c.getDisconnectCause();
if (DBG) log("onDisconnect: " + c + ", cause=" + cause);

// Under certain call disconnected states, we want to alert the user
// with a dialog instead of going through the normal disconnect
// routine.
        if (cause == Connection.DisconnectCause.CALL_BARRED) {
showGenericErrorDialog(R.string.callFailed_cb_enabled, false);
return;
        } else if (cause == Connection.DisconnectCause.FDN_BLOCKED) {
showGenericErrorDialog(R.string.callFailed_fdn_only, false);
return;
}
@@ -1203,7 +1203,7 @@
// conference call there's no "call ended" state at all; in that
// case we blow away any DISCONNECTED connections right now to make sure
// the UI updates instantly to reflect the current state.]
        Call call = c.getCall();
if (call != null) {
// We only care about situation of a single caller
// disconnecting from a conference call.  In that case, the
@@ -1213,10 +1213,10 @@
// has *no* ACTIVE connections, that means that the entire
// conference call just ended, so we *do* want to show the
// "Call ended" state.)
            List<Connection> connections = call.getConnections();
if (connections != null && connections.size() > 1) {
                for (Connection conn : connections) {
                    if (conn.getState() == Call.State.ACTIVE) {
// This call still has at least one ACTIVE connection!
// So blow away any DISCONNECTED connections
// (including, presumably, the one that just
@@ -1253,9 +1253,9 @@
// are waiting for the radio to finish powering up for an
// emergency call:
boolean bailOutImmediately =
                ((cause == Connection.DisconnectCause.INCOMING_MISSED)
                 || (cause == Connection.DisconnectCause.INCOMING_REJECTED)
                 || ((cause == Connection.DisconnectCause.OUT_OF_SERVICE)
&& (emergencyCallRetryCount > 0)))
&& currentlyIdle;

@@ -1269,7 +1269,7 @@

// Retry the call, by resending the intent to the emergency
// call handler activity.
            if ((cause == Connection.DisconnectCause.OUT_OF_SERVICE)
&& (emergencyCallRetryCount > 0)) {
startActivity(getIntent()
.setClassName(this, EmergencyCallHandler.class.getName()));
@@ -1318,7 +1318,7 @@
// "call ended" state.)  At that point, if the
// Phone is idle, we'll finish() out of this activity.
int callEndedDisplayDelay =
                    (cause == Connection.DisconnectCause.LOCAL)
? CALL_ENDED_SHORT_DELAY : CALL_ENDED_LONG_DELAY;
mHandler.removeMessages(DELAYED_CLEANUP_AFTER_DISCONNECT);
Message message = Message.obtain(mHandler, DELAYED_CLEANUP_AFTER_DISCONNECT);
@@ -1390,11 +1390,11 @@
* Dialer to handle POST_ON_DIAL_CHARS too.
*/
private void handlePostOnDialChars(AsyncResult r, char ch) {
        Connection c = (Connection) r.result;

if (c != null) {
            Connection.PostDialState state =
                    (Connection.PostDialState) r.userObj;

if (DBG) log("handlePostOnDialChar: state = " +
state + ", ch = " + ch);
@@ -1428,7 +1428,7 @@
}
}

    private void showWaitPromptDialog(final Connection c, String postDialStr) {
Resources r = getResources();
StringBuilder buf = new StringBuilder();
buf.append(r.getText(R.string.wait_prompt_str));
@@ -1494,7 +1494,7 @@
return result;
}

    private void showWildPromptDialog(final Connection c) {
View v = createWildPromptView();

if (mWildPromptDialog != null) {
@@ -2474,7 +2474,7 @@
setInCallScreenMode(InCallScreenMode.NORMAL);
return;
}
                List<Connection> connections = mForegroundCall.getConnections();
// There almost certainly will be > 1 connection,
// since isConferenceCall() just returned true.
if ((connections == null) || (connections.size() <= 1)) {
@@ -2553,7 +2553,7 @@
*        the current foreground call; size must be greater than 1
*        (or it wouldn't be a conference call in the first place.)
*/
    private void updateManageConferencePanel(List<Connection> connections) {
mNumCallersInConference = connections.size();
if (DBG) log("updateManageConferencePanel()... num connections in conference = "
+ mNumCallersInConference);
@@ -2567,7 +2567,7 @@
for (int i = 0; i < MAX_CALLERS_IN_CONFERENCE; i++) {
if (i < mNumCallersInConference) {
// Fill in the row in the UI for this caller.
                Connection connection = (Connection) connections.get(i);
updateManageConferenceRow(i, connection, canSeparate);
} else {
// Blank out this row in the UI
@@ -2585,7 +2585,7 @@
private void updateManageConferencePanelIfNecessary() {
if (DBG) log("updateManageConferencePanel: mForegroundCall " + mForegroundCall + "...");

        List<Connection> connections = mForegroundCall.getConnections();
if (connections == null) {
if (DBG) log("==> no connections on foreground call!");
// Hide the Manage Conference panel, return to NORMAL mode.
@@ -2637,7 +2637,7 @@
*        on this row in the UI.
*/
private void updateManageConferenceRow(final int i,
                                           final Connection connection,
boolean canSeparate) {
if (DBG) log("updateManageConferenceRow(" + i + ")...  connection = " + connection);

@@ -2756,7 +2756,7 @@
* user clicks the "End" button on a specific row in the Manage
* conference UI.
*/
    private void endConferenceConnection(int i, Connection connection) {
if (DBG) log("===> ENDING conference connection " + i
+ ": Connection " + connection);
// The actual work of ending the connection:
@@ -2772,7 +2772,7 @@
* when the user clicks the "Separate" (i.e. "Private") button on a
* specific row in the Manage conference UI.
*/
    private void separateConferenceConnection(int i, Connection connection) {
if (DBG) log("===> SEPARATING conference connection " + i
+ ": Connection " + connection);

@@ -2810,7 +2810,7 @@
*/
private boolean okToDialDTMFTones() {
final boolean hasRingingCall = !mRingingCall.isIdle();
        final Call.State fgCallState = mForegroundCall.getState();

// We're allowed to send DTMF tones when there's an ACTIVE
// foreground call, and not when an incoming call is ringing
@@ -2822,7 +2822,7 @@
// some connections that never update to an ACTIVE state (no
// indication from the network).
boolean canDial =
            (fgCallState == Call.State.ACTIVE || fgCallState == Call.State.ALERTING)
&& !hasRingingCall
&& (mInCallScreenMode != InCallScreenMode.MANAGE_CONFERENCE);









diff --git a/src/com/android/phone/NotificationMgr.java b/src/com/android/phone/NotificationMgr.java
index 0c67bc7..8f51863 100644

@@ -40,10 +40,10 @@
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallerInfo;
import com.android.internal.telephony.CallerInfoAsyncQuery;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.cdma.CDMAPhone;
@@ -547,9 +547,9 @@
// different calls.  So if there's only one call, use that, but if
// both lines are in use we display the caller-id info from the
// foreground call and totally ignore the background call.
        Call currentCall = hasActiveCall ? mPhone.getForegroundCall()
: mPhone.getBackgroundCall();
        Connection currentConn = currentCall.getEarliestConnection();

// When expanded, the "Ongoing call" notification is (visually)
// different from most other Notifications, so we need to use a








diff --git a/src/com/android/phone/PhoneUtils.java b/src/com/android/phone/PhoneUtils.java
index cdb1c24..a84addb 100644

@@ -16,11 +16,11 @@

package com.android.phone;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CallerInfo;
import com.android.internal.telephony.CallerInfoAsyncQuery;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;

@@ -81,8 +81,8 @@
private static boolean sIsSpeakerEnabled = false;

/** Hash table to store mute (Boolean) values based upon the connection.*/
    private static Hashtable<Connection, Boolean> sConnectionMuteTable =
        new Hashtable<Connection, Boolean>();

/** Static handler for the connection/mute tracking */
private static ConnectionHandler mConnectionHandler;
@@ -104,16 +104,16 @@
Phone phone = (Phone) ar.userObj;

// update the foreground connections, if there are new connections.
                    List<Connection> fgConnections = phone.getForegroundCall().getConnections();
                    for (Connection cn : fgConnections) {
if (sConnectionMuteTable.get(cn) == null) {
sConnectionMuteTable.put(cn, Boolean.FALSE);
}
}

// update the background connections, if there are new connections.
                    List<Connection> bgConnections = phone.getBackgroundCall().getConnections();
                    for (Connection cn : bgConnections) {
if (sConnectionMuteTable.get(cn) == null) {
sConnectionMuteTable.put(cn, Boolean.FALSE);
}
@@ -122,8 +122,8 @@
// Check to see if there are any lingering connections here
// (disconnected connections), use old-school iterators to avoid
// concurrent modification exceptions.
                    Connection cn;
                    for (Iterator<Connection> cnlist = sConnectionMuteTable.keySet().iterator();
cnlist.hasNext();) {
cn = cnlist.next();
if (!fgConnections.contains(cn) && !bgConnections.contains(cn)) {
@@ -188,7 +188,7 @@
PhoneUtils.setAudioControlState(PhoneUtils.AUDIO_OFFHOOK);

boolean answered = false;
        Call call = phone.getRingingCall();

if (call != null && call.isRinging()) {
if (DBG) log("answerCall: call state = " + call.getState());
@@ -218,9 +218,9 @@
*/
static boolean hangup(Phone phone) {
boolean hungup = false;
        Call ringing = phone.getRingingCall();
        Call fg = phone.getForegroundCall();
        Call bg = phone.getBackgroundCall();

if (!ringing.isIdle()) {
if (DBG) log("HANGUP ringing call");
@@ -253,7 +253,7 @@
return hangup(phone.getBackgroundCall());
}

    static boolean hangup(Call call) {
try {
call.hangup();
return true;
@@ -264,7 +264,7 @@
return false;
}

    static void hangup(Connection c) {
try {
if (c != null) {
c.hangup();
@@ -311,7 +311,7 @@
try {
if (DBG) log("placeCall: '" + number + "'...");

            Connection cn = phone.dial(number);
if (DBG) log("===> phone.dial() returned: " + cn);

// Presently, null is returned for MMI codes
@@ -365,7 +365,7 @@
*/
static Boolean restoreMuteState(Phone phone) {
//get the earliest connection
        Connection c = phone.getForegroundCall().getEarliestConnection();

// only do this if connection is not null.
if (c != null) {
@@ -394,7 +394,7 @@
}
}

    static void separateCall(Connection c) {
try {
if (DBG) log("separateCall: " + c.getAddress());
c.separate();
@@ -754,7 +754,7 @@
* NOTE: This API should be avoided, with preference given to the
* asynchronous startGetCallerInfo API.
*/
    static CallerInfo getCallerInfo(Context context, Connection c) {
CallerInfo info = null;

if (c != null) {
@@ -810,9 +810,9 @@
/**
* Start a CallerInfo Query based on the earliest connection in the call.
*/
    static CallerInfoToken startGetCallerInfo(Context context, Call call,
CallerInfoAsyncQuery.OnQueryCompleteListener listener, Object cookie) {
        Connection conn = call.getEarliestConnection();
return startGetCallerInfo(context, conn, listener, cookie);
}

@@ -820,7 +820,7 @@
* place a temporary callerinfo object in the hands of the caller and notify
* caller when the actual query is done.
*/
    static CallerInfoToken startGetCallerInfo(Context context, Connection c,
CallerInfoAsyncQuery.OnQueryCompleteListener listener, Object cookie) {
CallerInfoToken cit;

@@ -943,7 +943,7 @@
public void onQueryComplete(int token, Object cookie, CallerInfo ci){
if (DBG) log("query complete, updating connection.userdata");

                ((Connection) cookie).setUserData(ci);
}
};

@@ -963,7 +963,7 @@
* asynchronous startGetCallerInfo API, used in conjunction with
* getCompactNameFromCallerInfo().
*/
    static String getCompactName(Context context, Connection conn) {
CallerInfo info = getCallerInfo(context, conn);
if (DBG) log("getCompactName: info = " + info);

@@ -992,11 +992,11 @@
* asynchronous startGetCallerInfo API, used in conjunction with
* getCompactNameFromCallerInfo().
*/
    static String getCompactName(Context context, Call call) {
if (isConferenceCall(call)) {
return context.getString(R.string.confCall);
}
        Connection conn = call.getEarliestConnection();  // may be null
return getCompactName(context, conn);  // OK if conn is null
}

@@ -1034,8 +1034,8 @@
*
* @return true if the specified call has more than one connection (in any state.)
*/
    static boolean isConferenceCall(Call call) {
        List<Connection> connections = call.getConnections();
if (connections != null && connections.size() > 1) {
return true;
}
@@ -1175,7 +1175,7 @@

// update the foreground connections to match.  This includes
// all the connections on conference calls.
        for (Connection cn : phone.getForegroundCall().getConnections()) {
if (sConnectionMuteTable.get(cn) == null) {
if (DBG) log("problem retrieving mute value for this connection.");
}
@@ -1334,11 +1334,11 @@
*
* @return true if we find a connection that is disconnected, and
* pending removal via
     * {@link com.android.internal.telephony.gsm.GsmCall#clearDisconnected()}.
*/
    private static final boolean hasDisconnectedConnections(Call call) {
// look through all connections for non-active ones.
        for (Connection c : call.getConnections()) {
if (!c.isAlive()) {
return true;
}
@@ -1361,8 +1361,8 @@
// is in the HOLDING state, since you *can't* actually swap calls
// when the foreground call is DIALING or ALERTING.)
return phone.getRingingCall().isIdle()
                && (phone.getForegroundCall().getState() == Call.State.ACTIVE)
                && (phone.getBackgroundCall().getState() == Call.State.HOLDING);
}

/**
@@ -1391,13 +1391,13 @@
final boolean hasActiveCall = !phone.getForegroundCall().isIdle();
final boolean hasHoldingCall = !phone.getBackgroundCall().isIdle();
final boolean allLinesTaken = hasActiveCall && hasHoldingCall;
        final Call.State fgCallState = phone.getForegroundCall().getState();

return !hasRingingCall
&& !allLinesTaken
                && ((fgCallState == Call.State.ACTIVE)
                    || (fgCallState == Call.State.IDLE)
                    || (fgCallState == Call.State.DISCONNECTED));
}


@@ -1412,7 +1412,7 @@
Log.d(LOG_TAG, "--- Overall Phone state: " + phone.getState());
Log.d(LOG_TAG, "---");

        Call fgCall = phone.getForegroundCall();
Log.d(LOG_TAG, "--- FG call: " + fgCall);
Log.d(LOG_TAG, "--- FG call state: " + fgCall.getState());
Log.d(LOG_TAG, "--- FG call isAlive(): " + fgCall.getState().isAlive());
@@ -1422,7 +1422,7 @@
Log.d(LOG_TAG, "--- FG call hasConnections: " + fgCall.hasConnections());
Log.d(LOG_TAG, "---");

        Call bgCall = phone.getBackgroundCall();
Log.d(LOG_TAG, "--- BG call: " + bgCall);
Log.d(LOG_TAG, "--- BG call state: " + bgCall.getState());
Log.d(LOG_TAG, "--- BG call isAlive(): " + bgCall.getState().isAlive());
@@ -1432,7 +1432,7 @@
Log.d(LOG_TAG, "--- BG call hasConnections: " + bgCall.hasConnections());
Log.d(LOG_TAG, "---");

        Call ringingCall = phone.getRingingCall();
Log.d(LOG_TAG, "--- RINGING call: " + ringingCall);
Log.d(LOG_TAG, "--- RINGING call state: " + ringingCall.getState());
Log.d(LOG_TAG, "--- RINGING call isAlive(): " + ringingCall.getState().isAlive());







