/*Use Rlog

Change-Id:Ie013f51215de8380b8de74161b6056b010711cfd*/
//Synthetic comment -- diff --git a/mockril/src/com/android/internal/telephony/mockril/MockRilController.java b/mockril/src/com/android/internal/telephony/mockril/MockRilController.java
//Synthetic comment -- index 0e75c72..aef9fb5 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.internal.telephony.mockril;

import android.os.Bundle;
import android.util.Log;

import com.android.internal.communication.MsgHeader;
import com.android.internal.communication.Msg;
//Synthetic comment -- @@ -62,7 +62,7 @@
try {
Msg.send(mRilChannel, cmd, token, status, pbData);
} catch (IOException e) {
            Log.v(TAG, "send command : %d failed: " + e.getStackTrace());
return false;
}
return true;
//Synthetic comment -- @@ -77,7 +77,7 @@
try {
response = Msg.recv(mRilChannel);
} catch (IOException e) {
            Log.v(TAG, "receive response for getRadioState() error: " + e.getStackTrace());
return null;
}
return response;
//Synthetic comment -- @@ -92,7 +92,7 @@
}
Msg response = getCtrlResponse();
if (response == null) {
            Log.v(TAG, "failed to get response");
return -1;
}
response.printHeader(TAG);
//Synthetic comment -- @@ -113,17 +113,17 @@
public boolean setRadioState(int state) {
RilCtrlCmds.CtrlReqRadioState req = new RilCtrlCmds.CtrlReqRadioState();
if (state < 0 || state > RilCmds.RADIOSTATE_NV_READY) {
            Log.v(TAG, "the give radio state is not valid.");
return false;
}
req.setState(state);
if (!sendCtrlCommand(RilCtrlCmds.CTRL_CMD_SET_RADIO_STATE, 0, 0, req)) {
            Log.v(TAG, "send set radio state request failed.");
return false;
}
Msg response = getCtrlResponse();
if (response == null) {
            Log.v(TAG, "failed to get response for setRadioState");
return false;
}
response.printHeader(TAG);
//Synthetic comment -- @@ -144,7 +144,7 @@

req.setPhoneNumber(phoneNumber);
if (!sendCtrlCommand(RilCtrlCmds.CTRL_CMD_SET_MT_CALL, 0, 0, req)) {
            Log.v(TAG, "send CMD_SET_MT_CALL request failed");
return false;
}
return true;
//Synthetic comment -- @@ -163,7 +163,7 @@
req.setCallFailCause(failCause);

if (!sendCtrlCommand(RilCtrlCmds.CTRL_CMD_HANGUP_CONN_REMOTE, 0, 0, req)) {
            Log.v(TAG, "send CTRL_CMD_HANGUP_CONN_REMOTE request failed");
return false;
}
return true;
//Synthetic comment -- @@ -183,7 +183,7 @@
req.setFlag(flag);

if (!sendCtrlCommand(RilCtrlCmds.CTRL_CMD_SET_CALL_TRANSITION_FLAG, 0, 0, req)) {
            Log.v(TAG, "send CTRL_CMD_SET_CALL_TRANSITION_FLAG request failed");
return false;
}
return true;
//Synthetic comment -- @@ -196,7 +196,7 @@
*/
public boolean setDialCallToAlert() {
if (!sendCtrlCommand(RilCtrlCmds.CTRL_CMD_SET_CALL_ALERT, 0, 0, null)) {
            Log.v(TAG, "send CTRL_CMD_SET_CALL_ALERT request failed");
return false;
}
return true;
//Synthetic comment -- @@ -209,7 +209,7 @@
*/
public boolean setAlertCallToActive() {
if (!sendCtrlCommand(RilCtrlCmds.CTRL_CMD_SET_CALL_ACTIVE, 0, 0, null)) {
            Log.v(TAG, "send CTRL_CMD_SET_CALL_ACTIVE request failed");
return false;
}
return true;








//Synthetic comment -- diff --git a/src/java/android/provider/Telephony.java b/src/java/android/provider/Telephony.java
//Synthetic comment -- index e932e2b..50f3203 100644

//Synthetic comment -- @@ -28,7 +28,7 @@
import android.os.Environment;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;


//Synthetic comment -- @@ -1293,7 +1293,7 @@
}

Uri uri = uriBuilder.build();
            //if (DEBUG) Log.v(TAG, "getOrCreateThreadId uri: " + uri);

Cursor cursor = SqliteWrapper.query(context, context.getContentResolver(),
uri, ID_PROJECTION, null, null, null);
//Synthetic comment -- @@ -1302,14 +1302,14 @@
if (cursor.moveToFirst()) {
return cursor.getLong(0);
} else {
                        Log.e(TAG, "getOrCreateThreadId returned no rows!");
}
} finally {
cursor.close();
}
}

            Log.e(TAG, "getOrCreateThreadId failed with uri " + uri.toString());
throw new IllegalArgumentException("Unable to find or allocate a thread ID.");
}
}








//Synthetic comment -- diff --git a/src/java/android/telephony/SmsMessage.java b/src/java/android/telephony/SmsMessage.java
//Synthetic comment -- index b94609e..c35f09e 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package android.telephony;

import android.os.Parcel;
import android.util.Log;

import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.GsmAlphabet.TextEncodingDetails;
//Synthetic comment -- @@ -158,7 +158,7 @@
} else if (SmsConstants.FORMAT_3GPP.equals(format)) {
wrappedMessage = com.android.internal.telephony.gsm.SmsMessage.createFromPdu(pdu);
} else {
            Log.e(LOG_TAG, "createFromPdu(): unsupported message format " + format);
return null;
}

//Synthetic comment -- @@ -337,7 +337,7 @@
nextPos = pos + Math.min(limit / 2, textLen - pos);
}
if ((nextPos <= pos) || (nextPos > textLen)) {
                Log.e(LOG_TAG, "fragmentText failed (" + pos + " >= " + nextPos + " or " +
nextPos + " >= " + textLen + ")");
break;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/AdnRecord.java b/src/java/com/android/internal/telephony/AdnRecord.java
//Synthetic comment -- index 1bf2d3c..6c51a22 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import android.os.Parcelable;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;

//Synthetic comment -- @@ -212,15 +212,15 @@
}

if (TextUtils.isEmpty(number)) {
            Log.w(LOG_TAG, "[buildAdnString] Empty dialing number");
return adnString;   // return the empty record (for delete)
} else if (number.length()
> (ADN_DIALING_NUMBER_END - ADN_DIALING_NUMBER_START + 1) * 2) {
            Log.w(LOG_TAG,
"[buildAdnString] Max length of dialing number is 20");
return null;
} else if (alphaTag != null && alphaTag.length() > footerOffset) {
            Log.w(LOG_TAG,
"[buildAdnString] Max length of tag is " + footerOffset);
return null;
} else {
//Synthetic comment -- @@ -271,7 +271,7 @@
// We don't support ext record chaining.

} catch (RuntimeException ex) {
            Log.w(LOG_TAG, "Error parsing AdnRecord ext record", ex);
}
}

//Synthetic comment -- @@ -312,7 +312,7 @@
emails = null;

} catch (RuntimeException ex) {
            Log.w(LOG_TAG, "Error parsing AdnRecord", ex);
number = "";
alphaTag = "";
emails = null;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/AdnRecordCache.java b/src/java/com/android/internal/telephony/AdnRecordCache.java
//Synthetic comment -- index db5f4da..d635076 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

import com.android.internal.telephony.gsm.UsimPhoneBookManager;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/AdnRecordLoader.java b/src/java/com/android/internal/telephony/AdnRecordLoader.java
//Synthetic comment -- index 084fae6..56d5793 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


public class AdnRecordLoader extends Handler {
//Synthetic comment -- @@ -186,7 +186,7 @@
}

if (false) {
                        Log.d(LOG_TAG,"ADN EF: 0x"
+ Integer.toHexString(ef)
+ ":" + recordNumber
+ "\n" + IccUtils.bytesToHexString(data));
//Synthetic comment -- @@ -217,7 +217,7 @@
throw new RuntimeException("load failed", ar.exception);
}

                    Log.d(LOG_TAG,"ADN extension EF: 0x"
+ Integer.toHexString(extensionEF)
+ ":" + adn.extRecord
+ "\n" + IccUtils.bytesToHexString(data));








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ApnContext.java b/src/java/com/android/internal/telephony/ApnContext.java
//Synthetic comment -- index 4817a7b..b6083ad 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony;

import android.util.Log;

import java.io.FileDescriptor;
import java.io.PrintWriter;
//Synthetic comment -- @@ -237,7 +237,7 @@
}

protected void log(String s) {
        Log.d(LOG_TAG, "[ApnContext:" + mApnType + "] " + s);
}

public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/BaseCommands.java b/src/java/com/android/internal/telephony/BaseCommands.java
//Synthetic comment -- index 1b54656..49d3c76 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
import android.os.AsyncResult;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
//Synthetic comment -- @@ -555,11 +555,11 @@
*/
@Override
public void registerForRilConnected(Handler h, int what, Object obj) {
        Log.d(LOG_TAG, "registerForRilConnected h=" + h + " w=" + what);
Registrant r = new Registrant (h, what, obj);
mRilConnectedRegistrants.add(r);
if (mRilVersion != -1) {
            Log.d(LOG_TAG, "Notifying: ril connected mRilVersion=" + mRilVersion);
r.notifyRegistrant(new AsyncResult(null, new Integer(mRilVersion), null));
}
}
//Synthetic comment -- @@ -592,7 +592,7 @@

synchronized (mStateMonitor) {
if (false) {
                Log.v(LOG_TAG, "setRadioState old: " + mState
+ " new " + newState);
}

//Synthetic comment -- @@ -607,25 +607,25 @@
mRadioStateChangedRegistrants.notifyRegistrants();

if (mState.isAvailable() && !oldState.isAvailable()) {
                Log.d(LOG_TAG,"Notifying: radio available");
mAvailRegistrants.notifyRegistrants();
onRadioAvailable();
}

if (!mState.isAvailable() && oldState.isAvailable()) {
                Log.d(LOG_TAG,"Notifying: radio not available");
mNotAvailRegistrants.notifyRegistrants();
}

if (mState.isOn() && !oldState.isOn()) {
                Log.d(LOG_TAG,"Notifying: Radio On");
mOnRegistrants.notifyRegistrants();
}

if ((!mState.isOn() || !mState.isAvailable())
&& !((!oldState.isOn() || !oldState.isAvailable()))
) {
                Log.d(LOG_TAG,"Notifying: radio off or not available");
mOffOrNotAvailRegistrants.notifyRegistrants();
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Call.java b/src/java/com/android/internal/telephony/Call.java
//Synthetic comment -- index 4967ab8..2127258 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import java.util.List;

import android.util.Log;

/**
* {@hide}
//Synthetic comment -- @@ -248,7 +248,7 @@
try {
hangup();
} catch (CallStateException ex) {
                Log.w(LOG_TAG, " hangupIfActive: caught " + ex);
}
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CallManager.java b/src/java/com/android/internal/telephony/CallManager.java
//Synthetic comment -- index b87ea50..7d696b4 100644

//Synthetic comment -- @@ -27,7 +27,7 @@
import android.os.Registrant;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
//Synthetic comment -- @@ -293,7 +293,7 @@
if (basePhone != null && !mPhones.contains(basePhone)) {

if (DBG) {
                Log.d(LOG_TAG, "registerPhone(" +
phone.getPhoneName() + " " + phone + ")");
}

//Synthetic comment -- @@ -320,7 +320,7 @@
if (basePhone != null && mPhones.contains(basePhone)) {

if (DBG) {
                Log.d(LOG_TAG, "unregisterPhone(" +
phone.getPhoneName() + " " + phone + ")");
}

//Synthetic comment -- @@ -380,7 +380,7 @@
if (audioManager.getMode() != AudioManager.MODE_RINGTONE) {
// only request audio focus if the ringtone is going to be heard
if (audioManager.getStreamVolume(AudioManager.STREAM_RING) > 0) {
                        if (VDBG) Log.d(LOG_TAG, "requestAudioFocus on STREAM_RING");
audioManager.requestAudioFocusForCall(AudioManager.STREAM_RING,
AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
}
//Synthetic comment -- @@ -402,7 +402,7 @@
}
if (audioManager.getMode() != newAudioMode) {
// request audio focus before setting the new mode
                    if (VDBG) Log.d(LOG_TAG, "requestAudioFocus on STREAM_VOICE_CALL");
audioManager.requestAudioFocusForCall(AudioManager.STREAM_VOICE_CALL,
AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
audioManager.setMode(newAudioMode);
//Synthetic comment -- @@ -411,7 +411,7 @@
case IDLE:
if (audioManager.getMode() != AudioManager.MODE_NORMAL) {
audioManager.setMode(AudioManager.MODE_NORMAL);
                    if (VDBG) Log.d(LOG_TAG, "abandonAudioFocus");
// abandon audio focus after the mode has been set back to normal
audioManager.abandonAudioFocusForCall();
}
//Synthetic comment -- @@ -507,8 +507,8 @@
Phone ringingPhone = ringingCall.getPhone();

if (VDBG) {
            Log.d(LOG_TAG, "acceptCall(" +ringingCall + " from " + ringingCall.getPhone() + ")");
            Log.d(LOG_TAG, this.toString());
}

if ( hasActiveFgCall() ) {
//Synthetic comment -- @@ -517,7 +517,7 @@
boolean sameChannel = (activePhone == ringingPhone);

if (VDBG) {
                Log.d(LOG_TAG, "hasBgCall: "+ hasBgCall + "sameChannel:" + sameChannel);
}

if (sameChannel && hasBgCall) {
//Synthetic comment -- @@ -532,8 +532,8 @@
ringingPhone.acceptCall();

if (VDBG) {
            Log.d(LOG_TAG, "End acceptCall(" +ringingCall + ")");
            Log.d(LOG_TAG, this.toString());
}
}

//Synthetic comment -- @@ -548,8 +548,8 @@
*/
public void rejectCall(Call ringingCall) throws CallStateException {
if (VDBG) {
            Log.d(LOG_TAG, "rejectCall(" +ringingCall + ")");
            Log.d(LOG_TAG, this.toString());
}

Phone ringingPhone = ringingCall.getPhone();
//Synthetic comment -- @@ -557,8 +557,8 @@
ringingPhone.rejectCall();

if (VDBG) {
            Log.d(LOG_TAG, "End rejectCall(" +ringingCall + ")");
            Log.d(LOG_TAG, this.toString());
}
}

//Synthetic comment -- @@ -585,8 +585,8 @@
Phone heldPhone = null;

if (VDBG) {
            Log.d(LOG_TAG, "switchHoldingAndActive(" +heldCall + ")");
            Log.d(LOG_TAG, this.toString());
}

if (hasActiveFgCall()) {
//Synthetic comment -- @@ -606,8 +606,8 @@
}

if (VDBG) {
            Log.d(LOG_TAG, "End switchHoldingAndActive(" +heldCall + ")");
            Log.d(LOG_TAG, this.toString());
}
}

//Synthetic comment -- @@ -624,8 +624,8 @@
Phone backgroundPhone = null;

if (VDBG) {
            Log.d(LOG_TAG, "hangupForegroundResumeBackground(" +heldCall + ")");
            Log.d(LOG_TAG, this.toString());
}

if (hasActiveFgCall()) {
//Synthetic comment -- @@ -643,8 +643,8 @@
}

if (VDBG) {
            Log.d(LOG_TAG, "End hangupForegroundResumeBackground(" +heldCall + ")");
            Log.d(LOG_TAG, this.toString());
}
}

//Synthetic comment -- @@ -680,8 +680,8 @@
public void conference(Call heldCall) throws CallStateException {

if (VDBG) {
            Log.d(LOG_TAG, "conference(" +heldCall + ")");
            Log.d(LOG_TAG, this.toString());
}


//Synthetic comment -- @@ -695,8 +695,8 @@
}

if (VDBG) {
            Log.d(LOG_TAG, "End conference(" +heldCall + ")");
            Log.d(LOG_TAG, this.toString());
}

}
//Synthetic comment -- @@ -716,8 +716,8 @@
Connection result;

if (VDBG) {
            Log.d(LOG_TAG, " dial(" + basePhone + ", "+ dialString + ")");
            Log.d(LOG_TAG, this.toString());
}

if (!canDial(phone)) {
//Synthetic comment -- @@ -729,15 +729,15 @@
boolean hasBgCall = !(activePhone.getBackgroundCall().isIdle());

if (DBG) {
                Log.d(LOG_TAG, "hasBgCall: "+ hasBgCall + " sameChannel:" + (activePhone == basePhone));
}

if (activePhone != basePhone) {
if (hasBgCall) {
                    Log.d(LOG_TAG, "Hangup");
getActiveFgCall().hangup();
} else {
                    Log.d(LOG_TAG, "Switch");
activePhone.switchHoldingAndActive();
}
}
//Synthetic comment -- @@ -746,8 +746,8 @@
result = basePhone.dial(dialString);

if (VDBG) {
            Log.d(LOG_TAG, "End dial(" + basePhone + ", "+ dialString + ")");
            Log.d(LOG_TAG, this.toString());
}

return result;
//Synthetic comment -- @@ -802,7 +802,7 @@
|| (fgCallState == Call.State.DISCONNECTED)));

if (result == false) {
            Log.d(LOG_TAG, "canDial serviceState=" + serviceState
+ " hasRingingCall=" + hasRingingCall
+ " hasActiveCall=" + hasActiveCall
+ " hasHoldingCall=" + hasHoldingCall
//Synthetic comment -- @@ -846,8 +846,8 @@
*/
public void explicitCallTransfer(Call heldCall) throws CallStateException {
if (VDBG) {
            Log.d(LOG_TAG, " explicitCallTransfer(" + heldCall + ")");
            Log.d(LOG_TAG, this.toString());
}

if (canTransfer(heldCall)) {
//Synthetic comment -- @@ -855,8 +855,8 @@
}

if (VDBG) {
            Log.d(LOG_TAG, "End explicitCallTransfer(" + heldCall + ")");
            Log.d(LOG_TAG, this.toString());
}

}
//Synthetic comment -- @@ -871,7 +871,7 @@
* @return null if phone doesn't have or support mmi code
*/
public List<? extends MmiCode> getPendingMmiCodes(Phone phone) {
        Log.e(LOG_TAG, "getPendingMmiCodes not implemented");
return null;
}

//Synthetic comment -- @@ -884,7 +884,7 @@
* @return false if phone doesn't support ussd service
*/
public boolean sendUssdResponse(Phone phone, String ussdMessge) {
        Log.e(LOG_TAG, "sendUssdResponse not implemented");
return false;
}

//Synthetic comment -- @@ -899,8 +899,8 @@

public void setMute(boolean muted) {
if (VDBG) {
            Log.d(LOG_TAG, " setMute(" + muted + ")");
            Log.d(LOG_TAG, this.toString());
}

if (hasActiveFgCall()) {
//Synthetic comment -- @@ -908,8 +908,8 @@
}

if (VDBG) {
            Log.d(LOG_TAG, "End setMute(" + muted + ")");
            Log.d(LOG_TAG, this.toString());
}
}

//Synthetic comment -- @@ -936,8 +936,8 @@
*/
public void setEchoSuppressionEnabled(boolean enabled) {
if (VDBG) {
            Log.d(LOG_TAG, " setEchoSuppression(" + enabled + ")");
            Log.d(LOG_TAG, this.toString());
}

if (hasActiveFgCall()) {
//Synthetic comment -- @@ -945,8 +945,8 @@
}

if (VDBG) {
            Log.d(LOG_TAG, "End setEchoSuppression(" + enabled + ")");
            Log.d(LOG_TAG, this.toString());
}
}

//Synthetic comment -- @@ -962,8 +962,8 @@
boolean result = false;

if (VDBG) {
            Log.d(LOG_TAG, " sendDtmf(" + c + ")");
            Log.d(LOG_TAG, this.toString());
}

if (hasActiveFgCall()) {
//Synthetic comment -- @@ -972,8 +972,8 @@
}

if (VDBG) {
            Log.d(LOG_TAG, "End sendDtmf(" + c + ")");
            Log.d(LOG_TAG, this.toString());
}
return result;
}
//Synthetic comment -- @@ -991,8 +991,8 @@
boolean result = false;

if (VDBG) {
            Log.d(LOG_TAG, " startDtmf(" + c + ")");
            Log.d(LOG_TAG, this.toString());
}

if (hasActiveFgCall()) {
//Synthetic comment -- @@ -1001,8 +1001,8 @@
}

if (VDBG) {
            Log.d(LOG_TAG, "End startDtmf(" + c + ")");
            Log.d(LOG_TAG, this.toString());
}

return result;
//Synthetic comment -- @@ -1014,15 +1014,15 @@
*/
public void stopDtmf() {
if (VDBG) {
            Log.d(LOG_TAG, " stopDtmf()" );
            Log.d(LOG_TAG, this.toString());
}

if (hasActiveFgCall()) getFgPhone().stopDtmf();

if (VDBG) {
            Log.d(LOG_TAG, "End stopDtmf()");
            Log.d(LOG_TAG, this.toString());
}
}

//Synthetic comment -- @@ -1714,98 +1714,98 @@

switch (msg.what) {
case EVENT_DISCONNECT:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_DISCONNECT)");
mDisconnectRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_PRECISE_CALL_STATE_CHANGED:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_PRECISE_CALL_STATE_CHANGED)");
mPreciseCallStateRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_NEW_RINGING_CONNECTION:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_NEW_RINGING_CONNECTION)");
if (getActiveFgCallState().isDialing() || hasMoreThanOneRingingCall()) {
Connection c = (Connection) ((AsyncResult) msg.obj).result;
try {
                            Log.d(LOG_TAG, "silently drop incoming call: " + c.getCall());
c.getCall().hangup();
} catch (CallStateException e) {
                            Log.w(LOG_TAG, "new ringing connection", e);
}
} else {
mNewRingingConnectionRegistrants.notifyRegistrants((AsyncResult) msg.obj);
}
break;
case EVENT_UNKNOWN_CONNECTION:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_UNKNOWN_CONNECTION)");
mUnknownConnectionRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_INCOMING_RING:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_INCOMING_RING)");
// The event may come from RIL who's not aware of an ongoing fg call
if (!hasActiveFgCall()) {
mIncomingRingRegistrants.notifyRegistrants((AsyncResult) msg.obj);
}
break;
case EVENT_RINGBACK_TONE:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_RINGBACK_TONE)");
mRingbackToneRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_IN_CALL_VOICE_PRIVACY_ON:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_IN_CALL_VOICE_PRIVACY_ON)");
mInCallVoicePrivacyOnRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_IN_CALL_VOICE_PRIVACY_OFF:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_IN_CALL_VOICE_PRIVACY_OFF)");
mInCallVoicePrivacyOffRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_CALL_WAITING:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_CALL_WAITING)");
mCallWaitingRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_DISPLAY_INFO:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_DISPLAY_INFO)");
mDisplayInfoRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_SIGNAL_INFO:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_SIGNAL_INFO)");
mSignalInfoRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_CDMA_OTA_STATUS_CHANGE:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_CDMA_OTA_STATUS_CHANGE)");
mCdmaOtaStatusChangeRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_RESEND_INCALL_MUTE:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_RESEND_INCALL_MUTE)");
mResendIncallMuteRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_MMI_INITIATE:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_MMI_INITIATE)");
mMmiInitiateRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_MMI_COMPLETE:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_MMI_COMPLETE)");
mMmiCompleteRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_ECM_TIMER_RESET:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_ECM_TIMER_RESET)");
mEcmTimerResetRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_SUBSCRIPTION_INFO_READY:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_SUBSCRIPTION_INFO_READY)");
mSubscriptionInfoReadyRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_SUPP_SERVICE_FAILED:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_SUPP_SERVICE_FAILED)");
mSuppServiceFailedRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_SERVICE_STATE_CHANGED:
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_SERVICE_STATE_CHANGED)");
mServiceStateChangedRegistrants.notifyRegistrants((AsyncResult) msg.obj);
break;
case EVENT_POST_DIAL_CHARACTER:
// we need send the character that is being processed in msg.arg1
// so can't use notifyRegistrants()
                    if (VDBG) Log.d(LOG_TAG, " handleMessage (EVENT_POST_DIAL_CHARACTER)");
for(int i=0; i < mPostDialCharacterRegistrants.size(); i++) {
Message notifyMsg;
notifyMsg = ((Registrant)mPostDialCharacterRegistrants.get(i)).messageForRegistrant();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CallTracker.java b/src/java/com/android/internal/telephony/CallTracker.java
//Synthetic comment -- index 62caf01..efe53c9 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import android.os.Message;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.CommandException;









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CommandException.java b/src/java/com/android/internal/telephony/CommandException.java
//Synthetic comment -- index 94c544e..d1085f6 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.internal.telephony.RILConstants;

import android.util.Log;

/**
* {@hide}
//Synthetic comment -- @@ -84,7 +84,7 @@
case RILConstants.ILLEGAL_SIM_OR_ME:
return new CommandException(Error.ILLEGAL_SIM_OR_ME);
default:
                Log.e("GSM", "Unrecognized RIL errno " + ril_errno);
return new CommandException(Error.INVALID_RESPONSE);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CommandsInterface.java b/src/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index f7757b3..d1b39f6 100644

//Synthetic comment -- @@ -20,7 +20,7 @@

import android.os.Message;
import android.os.Handler;
import android.util.Log;

/**
* {@hide}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Connection.java b/src/java/com/android/internal/telephony/Connection.java
//Synthetic comment -- index 554d974..8e10af5 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.internal.telephony;

import android.util.Log;

/**
//Synthetic comment -- @@ -293,7 +294,7 @@
public String toString() {
StringBuilder str = new StringBuilder(128);

        if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
str.append("addr: " + getAddress())
.append(" pres.: " + getNumberPresentation())
.append(" dial: " + getOrigDialString())








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CsimFileHandler.java b/src/java/com/android/internal/telephony/CsimFileHandler.java
//Synthetic comment -- index 979d1d2..c9b23a4 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony;

import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccConstants;
//Synthetic comment -- @@ -61,11 +61,11 @@

@Override
protected void logd(String msg) {
        Log.d(LOG_TAG, msg);
}

@Override
protected void loge(String msg) {
        Log.e(LOG_TAG, msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataCallState.java b/src/java/com/android/internal/telephony/DataCallState.java
//Synthetic comment -- index efbf608..c3a3b1d 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import android.net.NetworkUtils;
import android.net.RouteInfo;
import android.os.SystemProperties;
import android.util.Log;

import com.android.internal.telephony.DataConnection.FailCause;

//Synthetic comment -- @@ -149,7 +149,7 @@
// Assume point to point
addrPrefixLen = (ia instanceof Inet4Address) ? 32 : 128;
}
                            if (DBG) Log.d(LOG_TAG, "addr/pl=" + addr + "/" + addrPrefixLen);
la = new LinkAddress(ia, addrPrefixLen);
linkProperties.addLinkAddress(la);
}
//Synthetic comment -- @@ -219,7 +219,7 @@

result = SetupResult.SUCCESS;
} catch (UnknownHostException e) {
                Log.d(LOG_TAG, "setLinkProperties: UnknownHostException " + e);
e.printStackTrace();
result = SetupResult.ERR_UnacceptableParameter;
}
//Synthetic comment -- @@ -234,7 +234,7 @@
// An error occurred so clear properties
if (result != SetupResult.SUCCESS) {
if(DBG) {
                Log.d(LOG_TAG, "setLinkProperties: error clearing LinkProperties " +
"status=" + status + " result=" + result);
}
linkProperties.clear();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionAc.java b/src/java/com/android/internal/telephony/DataConnectionAc.java
//Synthetic comment -- index a24414f..2cd64e1 100644

//Synthetic comment -- @@ -586,6 +586,6 @@
}

private void log(String s) {
        android.util.Log.d(mLogTag, "DataConnectionAc " + s);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionTracker.java b/src/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index a2980be..a01fd3a 100644

//Synthetic comment -- @@ -44,7 +44,7 @@
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;

import com.android.internal.R;
import com.android.internal.telephony.DataConnection.FailCause;
//Synthetic comment -- @@ -706,7 +706,7 @@
break;

default:
                Log.e("DATA", "Unidentified event msg=" + msg);
break;
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DebugService.java b/src/java/com/android/internal/telephony/DebugService.java
//Synthetic comment -- index 29fea6e..82543ae 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony;

import android.util.Log;

import java.io.FileDescriptor;
import java.io.PrintWriter;
//Synthetic comment -- @@ -103,6 +103,6 @@
}

private static void log(String s) {
        Log.d(TAG, "DebugService " + s);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DefaultPhoneNotifier.java b/src/java/com/android/internal/telephony/DefaultPhoneNotifier.java
//Synthetic comment -- index 157fee6..8f4ae84 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
import android.telephony.CellInfo;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephonyRegistry;

//Synthetic comment -- @@ -176,7 +176,7 @@
}

private void log(String s) {
        Log.d(LOG_TAG, "[PhoneNotifier] " + s);
}

/**








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DriverCall.java b/src/java/com/android/internal/telephony/DriverCall.java
//Synthetic comment -- index b1e63ae..c76be5f 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony;
//import com.android.internal.telephony.*;
import android.util.Log;
import java.lang.Comparable;
import android.telephony.PhoneNumberUtils;

//Synthetic comment -- @@ -90,7 +90,7 @@

}
} catch (ATParseEx ex) {
            Log.e(LOG_TAG,"Invalid CLCC line: '" + line + "'");
return null;
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardProxy.java b/src/java/com/android/internal/telephony/IccCardProxy.java
//Synthetic comment -- index cdd2fea..3c949d8 100644

//Synthetic comment -- @@ -27,7 +27,7 @@
import android.os.RegistrantList;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.Log;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

//Synthetic comment -- @@ -687,10 +687,10 @@
}

private void log(String s) {
        Log.d(LOG_TAG, s);
}

private void loge(String msg) {
        Log.e(LOG_TAG, msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccFileHandler.java b/src/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index fd4e3cc..b0784b6 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.internal.telephony;

import android.os.*;
import android.util.Log;
import java.util.ArrayList;

/**








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManagerProxy.java b/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManagerProxy.java
//Synthetic comment -- index 1c0fc52..2ba0ce1 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
import android.os.Message;
import android.os.ServiceManager;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccProvider.java b/src/java/com/android/internal/telephony/IccProvider.java
//Synthetic comment -- index a66e19d..4feffb8 100644

//Synthetic comment -- @@ -25,7 +25,7 @@
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

//Synthetic comment -- @@ -210,7 +210,7 @@
String[] pair = param.split("=");

if (pair.length != 2) {
                Log.e(TAG, "resolve: bad whereClause parameter: " + param);
continue;
}

//Synthetic comment -- @@ -312,7 +312,7 @@
return cursor;
} else {
// No results to load
            Log.w(TAG, "Cannot load ADN records");
return new MatrixCursor(ADDRESS_BOOK_COLUMN_NAMES);
}
}
//Synthetic comment -- @@ -425,7 +425,7 @@
}

private void log(String msg) {
        Log.d(TAG, "[IccProvider] " + msg);
}

}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccServiceTable.java b/src/java/com/android/internal/telephony/IccServiceTable.java
//Synthetic comment -- index ed74a11..51cfd83 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony;

import android.util.Log;

/**
* Wrapper class for an ICC EF containing a bit field of enabled services.
//Synthetic comment -- @@ -43,7 +43,7 @@
int offset = service / 8;
if (offset >= mServiceTable.length) {
// Note: Enums are zero-based, but the TS service numbering is one-based
            Log.e(getTag(), "isAvailable for service " + (service + 1) + " fails, max service is " +
(mServiceTable.length * 8));
return false;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccSmsInterfaceManager.java b/src/java/com/android/internal/telephony/IccSmsInterfaceManager.java
//Synthetic comment -- index 525bcd9..45972e7 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import com.android.internal.util.HexDump;
//Synthetic comment -- @@ -78,7 +79,7 @@
mPhone.getContext().enforceCallingPermission(
"android.permission.SEND_SMS",
"Sending SMS message");
        if (Log.isLoggable("SMS", Log.VERBOSE)) {
log("sendData: destAddr=" + destAddr + " scAddr=" + scAddr + " destPort=" +
destPort + " data='"+ HexDump.toHexString(data)  + "' sentIntent=" +
sentIntent + " deliveryIntent=" + deliveryIntent);
//Synthetic comment -- @@ -115,7 +116,7 @@
mPhone.getContext().enforceCallingPermission(
"android.permission.SEND_SMS",
"Sending SMS message");
        if (Log.isLoggable("SMS", Log.VERBOSE)) {
log("sendText: destAddr=" + destAddr + " scAddr=" + scAddr +
" text='"+ text + "' sentIntent=" +
sentIntent + " deliveryIntent=" + deliveryIntent);
//Synthetic comment -- @@ -153,7 +154,7 @@
mPhone.getContext().enforceCallingPermission(
"android.permission.SEND_SMS",
"Sending SMS message");
        if (Log.isLoggable("SMS", Log.VERBOSE)) {
int i = 0;
for (String part : parts) {
log("sendMultipartText: destAddr=" + destAddr + ", srAddr=" + scAddr +








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccUtils.java b/src/java/com/android/internal/telephony/IccUtils.java
//Synthetic comment -- index a966f76..795740c 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.android.internal.telephony.GsmAlphabet;
import java.io.UnsupportedEncodingException;
//Synthetic comment -- @@ -187,7 +187,7 @@
try {
ret = new String(data, offset + 1, ucslen * 2, "utf-16be");
} catch (UnsupportedEncodingException ex) {
                    Log.e(LOG_TAG, "implausible UnsupportedEncodingException",
ex);
}

//Synthetic comment -- @@ -360,7 +360,7 @@
offset + 1, length - 1, "utf-16");
} catch (UnsupportedEncodingException ex) {
ret = "";
                    Log.e(LOG_TAG,"implausible UnsupportedEncodingException", ex);
}
break;

//Synthetic comment -- @@ -409,7 +409,7 @@
};

if (pixelIndex != numOfPixels) {
            Log.e(LOG_TAG, "parse end and size error");
}
return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
}
//Synthetic comment -- @@ -461,7 +461,7 @@
private static int[] mapTo2OrderBitColor(byte[] data, int valueIndex,
int length, int[] colorArray, int bits) {
if (0 != (8 % bits)) {
            Log.e(LOG_TAG, "not event number of color");
return mapToNon2OrderBitColor(data, valueIndex, length, colorArray,
bits);
}
//Synthetic comment -- @@ -499,7 +499,7 @@
private static int[] mapToNon2OrderBitColor(byte[] data, int valueIndex,
int length, int[] colorArray, int bits) {
if (0 == (8 % bits)) {
            Log.e(LOG_TAG, "not odd number of color");
return mapTo2OrderBitColor(data, valueIndex, length, colorArray,
bits);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/MccTable.java b/src/java/com/android/internal/telephony/MccTable.java
//Synthetic comment -- index cb33521..7d5a4e7 100644

//Synthetic comment -- @@ -26,7 +26,7 @@
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
//Synthetic comment -- @@ -180,11 +180,11 @@
mcc = Integer.parseInt(mccmnc.substring(0,3));
mnc = Integer.parseInt(mccmnc.substring(3));
} catch (NumberFormatException e) {
                Log.e(LOG_TAG, "Error parsing IMSI");
return;
}

            Log.d(LOG_TAG, "updateMccMncConfiguration: mcc=" + mcc + ", mnc=" + mnc);

if (mcc != 0) {
setTimezoneFromMccIfNeeded(context, mcc);
//Synthetic comment -- @@ -201,7 +201,7 @@
}
ActivityManagerNative.getDefault().updateConfiguration(config);
} catch (RemoteException e) {
                Log.e(LOG_TAG, "Can't update configuration", e);
}
}
}
//Synthetic comment -- @@ -273,7 +273,7 @@
AlarmManager alarm =
(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
alarm.setTimeZone(zoneId);
                Log.d(LOG_TAG, "timezone set to "+zoneId);
}
}
}
//Synthetic comment -- @@ -292,7 +292,7 @@
String language = MccTable.defaultLanguageForMcc(mcc);
String country = MccTable.countryCodeForMcc(mcc);

        Log.d(LOG_TAG, "locale set to "+language+"_"+country);
setSystemLocale(context, language, country);
}

//Synthetic comment -- @@ -305,7 +305,7 @@
private static void setWifiCountryCodeFromMcc(Context context, int mcc) {
String country = MccTable.countryCodeForMcc(mcc);
if (!country.isEmpty()) {
            Log.d(LOG_TAG, "WIFI_COUNTRY_CODE set to " + country);
WifiManager wM = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//persist
wM.setCountryCode(country, true);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index f617e2b..e490d5b 100644

//Synthetic comment -- @@ -36,7 +36,7 @@
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.R;
import com.android.internal.telephony.IccCardApplicationStatus.AppState;
//Synthetic comment -- @@ -253,11 +253,11 @@
*/
mDoesRilSendMultipleCallRing = SystemProperties.getBoolean(
TelephonyProperties.PROPERTY_RIL_SENDS_MULTIPLE_CALL_RING, true);
        Log.d(LOG_TAG, "mDoesRilSendMultipleCallRing=" + mDoesRilSendMultipleCallRing);

mCallRingDelay = SystemProperties.getInt(
TelephonyProperties.PROPERTY_CALL_RING_DELAY, 3000);
        Log.d(LOG_TAG, "mCallRingDelay=" + mCallRingDelay);

// Initialize device storage and outgoing SMS usage monitors for SMSDispatchers.
mSmsStorageMonitor = new SmsStorageMonitor(this);
//Synthetic comment -- @@ -302,7 +302,7 @@

switch(msg.what) {
case EVENT_CALL_RING:
                Log.d(LOG_TAG, "Event EVENT_CALL_RING Received state=" + getState());
ar = (AsyncResult)msg.obj;
if (ar.exception == null) {
PhoneConstants.State state = getState();
//Synthetic comment -- @@ -318,7 +318,7 @@
break;

case EVENT_CALL_RING_CONTINUE:
                Log.d(LOG_TAG, "Event EVENT_CALL_RING_CONTINUE Received stat=" + getState());
if (getState() == PhoneConstants.State.RINGING) {
sendIncomingCallRingNotification(msg.arg1);
}
//Synthetic comment -- @@ -1118,12 +1118,12 @@
private void sendIncomingCallRingNotification(int token) {
if (mIsVoiceCapable && !mDoesRilSendMultipleCallRing &&
(token == mCallRingContinueToken)) {
            Log.d(LOG_TAG, "Sending notifyIncomingRing");
notifyIncomingRing();
sendMessageDelayed(
obtainMessage(EVENT_CALL_RING_CONTINUE, token, 0), mCallRingDelay);
} else {
            Log.d(LOG_TAG, "Ignoring ring notification request,"
+ " mDoesRilSendMultipleCallRing=" + mDoesRilSendMultipleCallRing
+ " token=" + token
+ " mCallRingContinueToken=" + mCallRingContinueToken
//Synthetic comment -- @@ -1139,12 +1139,12 @@
}

public IsimRecords getIsimRecords() {
        Log.e(LOG_TAG, "getIsimRecords() is only supported on LTE devices");
return null;
}

public void requestIsimAuthentication(String nonce, Message result) {
        Log.e(LOG_TAG, "requestIsimAuthentication() is only supported on LTE devices");
}

public String getMsisdn() {
//Synthetic comment -- @@ -1157,7 +1157,7 @@
*/
private static void logUnexpectedCdmaMethodCall(String name)
{
        Log.e(LOG_TAG, "Error! " + name + "() in PhoneBase should not be " +
"called, CDMAPhone inactive.");
}

//Synthetic comment -- @@ -1169,14 +1169,14 @@
* Common error logger method for unexpected calls to GSM/WCDMA-only methods.
*/
private static void logUnexpectedGsmMethodCall(String name) {
        Log.e(LOG_TAG, "Error! " + name + "() in PhoneBase should not be " +
"called, GSMPhone inactive.");
}

// Called by SimRecords which is constructed with a PhoneBase instead of a GSMPhone.
public void notifyCallForwardingIndicator() {
// This function should be overridden by the class GSMPhone. Not implemented in CDMAPhone.
        Log.e(LOG_TAG, "Error! This function should never be executed, inactive CDMAPhone.");
}

public void notifyDataConnectionFailed(String reason, String apnType) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneFactory.java b/src/java/com/android/internal/telephony/PhoneFactory.java
//Synthetic comment -- index 1892427..c0641d5 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.os.SystemProperties;

import com.android.internal.telephony.cdma.CDMAPhone;
//Synthetic comment -- @@ -108,7 +108,7 @@
}
int networkMode = Settings.Global.getInt(context.getContentResolver(),
Settings.Global.PREFERRED_NETWORK_MODE, preferredNetworkMode);
                Log.i(LOG_TAG, "Network Mode set to " + Integer.toString(networkMode));

// Get cdmaSubscription
// TODO: Change when the ril will provides a way to know at runtime
//Synthetic comment -- @@ -119,11 +119,11 @@
switch (lteOnCdma) {
case PhoneConstants.LTE_ON_CDMA_FALSE:
cdmaSubscription = CdmaSubscriptionSourceManager.SUBSCRIPTION_FROM_NV;
                        Log.i(LOG_TAG, "lteOnCdma is 0 use SUBSCRIPTION_FROM_NV");
break;
case PhoneConstants.LTE_ON_CDMA_TRUE:
cdmaSubscription = CdmaSubscriptionSourceManager.SUBSCRIPTION_FROM_RUIM;
                        Log.i(LOG_TAG, "lteOnCdma is 1 use SUBSCRIPTION_FROM_RUIM");
break;
case PhoneConstants.LTE_ON_CDMA_UNKNOWN:
default:
//Synthetic comment -- @@ -131,10 +131,10 @@
cdmaSubscription = Settings.Global.getInt(context.getContentResolver(),
Settings.Global.PREFERRED_CDMA_SUBSCRIPTION,
preferredCdmaSubscription);
                        Log.i(LOG_TAG, "lteOnCdma not set, using PREFERRED_CDMA_SUBSCRIPTION");
break;
}
                Log.i(LOG_TAG, "Cdma Subscription set to " + cdmaSubscription);

//reads the system properties and makes commandsinterface
sCommandsInterface = new RIL(context, networkMode, cdmaSubscription);
//Synthetic comment -- @@ -144,19 +144,19 @@

int phoneType = TelephonyManager.getPhoneType(networkMode);
if (phoneType == PhoneConstants.PHONE_TYPE_GSM) {
                    Log.i(LOG_TAG, "Creating GSMPhone");
sProxyPhone = new PhoneProxy(new GSMPhone(context,
sCommandsInterface, sPhoneNotifier));
} else if (phoneType == PhoneConstants.PHONE_TYPE_CDMA) {
switch (TelephonyManager.getLteOnCdmaModeStatic()) {
case PhoneConstants.LTE_ON_CDMA_TRUE:
                            Log.i(LOG_TAG, "Creating CDMALTEPhone");
sProxyPhone = new PhoneProxy(new CDMALTEPhone(context,
sCommandsInterface, sPhoneNotifier));
break;
case PhoneConstants.LTE_ON_CDMA_FALSE:
default:
                            Log.i(LOG_TAG, "Creating CDMAPhone");
sProxyPhone = new PhoneProxy(new CDMAPhone(context,
sCommandsInterface, sPhoneNotifier));
break;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneProxy.java b/src/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 1c4bdc5..75980fc 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.util.Log;

import com.android.internal.telephony.cdma.CDMAPhone;
import com.android.internal.telephony.gsm.GSMPhone;
//Synthetic comment -- @@ -131,15 +131,15 @@
}

private static void logd(String msg) {
        Log.d(LOG_TAG, "[PhoneProxy] " + msg);
}

private void logw(String msg) {
        Log.w(LOG_TAG, "[PhoneProxy] " + msg);
}

private void loge(String msg) {
        Log.e(LOG_TAG, "[PhoneProxy] " + msg);
}

private void updatePhoneObject(int newVoiceRadioTech) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneStateIntentReceiver.java b/src/java/com/android/internal/telephony/PhoneStateIntentReceiver.java
//Synthetic comment -- index 89084ac..40f0c34 100644

//Synthetic comment -- @@ -25,7 +25,7 @@
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
*
//Synthetic comment -- @@ -174,7 +174,7 @@
mTarget.sendMessage(message);
}
} else if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(action)) {
                if (DBG) Log.d(LOG_TAG, "onReceiveIntent: ACTION_PHONE_STATE_CHANGED, state="
+ intent.getStringExtra(PhoneConstants.STATE_KEY));
String phoneState = intent.getStringExtra(PhoneConstants.STATE_KEY);
mPhoneState = (PhoneConstants.State) Enum.valueOf(
//Synthetic comment -- @@ -195,7 +195,7 @@
}
}
} catch (Exception ex) {
            Log.e(LOG_TAG, "[PhoneStateIntentRecv] caught " + ex);
ex.printStackTrace();
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneSubInfo.java b/src/java/com/android/internal/telephony/PhoneSubInfo.java
//Synthetic comment -- index e8449ce..5a3e9b7 100755

//Synthetic comment -- @@ -22,7 +22,7 @@
import android.content.pm.PackageManager;
import android.os.Binder;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.android.internal.telephony.ims.IsimRecords;

//Synthetic comment -- @@ -50,9 +50,9 @@
try {
super.finalize();
} catch (Throwable throwable) {
            Log.e(LOG_TAG, "Error while finalizing:", throwable);
}
        Log.d(LOG_TAG, "PhoneSubInfo finalized");
}

/**
//Synthetic comment -- @@ -118,7 +118,7 @@
public String getVoiceMailNumber() {
mContext.enforceCallingOrSelfPermission(READ_PHONE_STATE, "Requires READ_PHONE_STATE");
String number = PhoneNumberUtils.extractNetworkPortion(mPhone.getVoiceMailNumber());
        Log.d(LOG_TAG, "VM: PhoneSubInfo.getVoiceMailNUmber: "); // + number);
return number;
}

//Synthetic comment -- @@ -131,7 +131,7 @@
mContext.enforceCallingOrSelfPermission(CALL_PRIVILEGED,
"Requires CALL_PRIVILEGED");
String number = mPhone.getVoiceMailNumber();
        Log.d(LOG_TAG, "VM: PhoneSubInfo.getCompleteVoiceMailNUmber: "); // + number);
return number;
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index f3ed594..ed936be 100644

//Synthetic comment -- @@ -47,7 +47,7 @@
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SuppServiceNotification;
//Synthetic comment -- @@ -179,7 +179,7 @@

ex = CommandException.fromRilErrno(error);

        if (RIL.RILJ_LOGD) Log.d(LOG_TAG, serialString() + "< "
+ RIL.requestToString(mRequest)
+ " error: " + ex);

//Synthetic comment -- @@ -269,7 +269,7 @@
} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
sendScreenState(false);
} else {
                Log.w(LOG_TAG, "RIL received unexpected Intent: " + intent.getAction());
}
}
};
//Synthetic comment -- @@ -343,12 +343,12 @@
dataLength[2] = (byte)((data.length >> 8) & 0xff);
dataLength[3] = (byte)((data.length) & 0xff);

                        //Log.v(LOG_TAG, "writing packet: " + data.length + " bytes");

s.getOutputStream().write(dataLength);
s.getOutputStream().write(data);
} catch (IOException ex) {
                        Log.e(LOG_TAG, "IOException", ex);
req = findAndRemoveRequestFromList(rr.mSerial);
// make sure this request has not already been handled,
// eg, if RILReceiver cleared the list.
//Synthetic comment -- @@ -357,7 +357,7 @@
rr.release();
}
} catch (RuntimeException exc) {
                        Log.e(LOG_TAG, "Uncaught exception ", exc);
req = findAndRemoveRequestFromList(rr.mSerial);
// make sure this request has not already been handled,
// eg, if RILReceiver cleared the list.
//Synthetic comment -- @@ -393,7 +393,7 @@
// Note: Keep mRequestList so that delayed response
// can still be handled when response finally comes.
if (mRequestMessagesWaiting != 0) {
                                Log.d(LOG_TAG, "NOTE: mReqWaiting is NOT 0 but"
+ mRequestMessagesWaiting + " at TIMEOUT, reset!"
+ " There still msg waitng for response");

//Synthetic comment -- @@ -402,12 +402,12 @@
if (RILJ_LOGD) {
synchronized (mRequestsList) {
int count = mRequestsList.size();
                                        Log.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
" mRequestList=" + count);

for (int i = 0; i < count; i++) {
rr = mRequestsList.get(i);
                                            Log.d(LOG_TAG, i + ": [" + rr.mSerial + "] "
+ requestToString(rr.mRequest));
}
}
//Synthetic comment -- @@ -421,7 +421,7 @@
// should already sent out (i.e.
// mRequestMessagesPending is 0 )while TIMEOUT occurs.
if (mRequestMessagesPending != 0) {
                                Log.e(LOG_TAG, "ERROR: mReqPending is NOT 0 but"
+ mRequestMessagesPending + " at TIMEOUT, reset!");
mRequestMessagesPending = 0;

//Synthetic comment -- @@ -461,7 +461,7 @@
countRead = is.read(buffer, offset, remaining);

if (countRead < 0 ) {
                Log.e(LOG_TAG, "Hit EOS reading message length");
return -1;
}

//Synthetic comment -- @@ -481,7 +481,7 @@
countRead = is.read(buffer, offset, remaining);

if (countRead < 0 ) {
                Log.e(LOG_TAG, "Hit EOS reading message.  messageLength=" + messageLength
+ " remaining=" + remaining);
return -1;
}
//Synthetic comment -- @@ -526,12 +526,12 @@
// or after the 8th time

if (retryCount == 8) {
                        Log.e (LOG_TAG,
"Couldn't find '" + SOCKET_NAME_RIL
+ "' socket after " + retryCount
+ " times, continuing to retry silently");
} else if (retryCount > 0 && retryCount < 8) {
                        Log.i (LOG_TAG,
"Couldn't find '" + SOCKET_NAME_RIL
+ "' socket; retrying after timeout");
}
//Synthetic comment -- @@ -548,7 +548,7 @@
retryCount = 0;

mSocket = s;
                Log.i(LOG_TAG, "Connected to '" + SOCKET_NAME_RIL + "' socket");

int length = 0;
try {
//Synthetic comment -- @@ -568,20 +568,20 @@
p.unmarshall(buffer, 0, length);
p.setDataPosition(0);

                        //Log.v(LOG_TAG, "Read packet: " + length + " bytes");

processResponse(p);
p.recycle();
}
} catch (java.io.IOException ex) {
                    Log.i(LOG_TAG, "'" + SOCKET_NAME_RIL + "' socket closed",
ex);
} catch (Throwable tr) {
                    Log.e(LOG_TAG, "Uncaught exception read length=" + length +
"Exception:" + tr.toString());
}

                Log.i(LOG_TAG, "Disconnected from '" + SOCKET_NAME_RIL
+ "' socket");

setRadioState (RadioState.RADIO_UNAVAILABLE);
//Synthetic comment -- @@ -597,7 +597,7 @@
// Clear request list on close
clearRequestsList(RADIO_NOT_AVAILABLE, false);
}} catch (Throwable tr) {
                Log.e(LOG_TAG,"Uncaught exception", tr);
}

/* We're disconnected so we don't know the ril version */
//Synthetic comment -- @@ -2151,7 +2151,7 @@
synchronized (mRequestsList) {
int count = mRequestsList.size();
if (RILJ_LOGD && loggable) {
                Log.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
" mReqPending=" + mRequestMessagesPending +
" mRequestList=" + count);
}
//Synthetic comment -- @@ -2159,7 +2159,7 @@
for (int i = 0; i < count ; i++) {
rr = mRequestsList.get(i);
if (RILJ_LOGD && loggable) {
                    Log.d(LOG_TAG, i + ": [" + rr.mSerial + "] " +
requestToString(rr.mRequest));
}
rr.onError(error, null);
//Synthetic comment -- @@ -2200,7 +2200,7 @@
rr = findAndRemoveRequestFromList(serial);

if (rr == null) {
            Log.w(LOG_TAG, "Unexpected solicited response! sn: "
+ serial + " error: " + error);
return;
}
//Synthetic comment -- @@ -2338,7 +2338,7 @@
}} catch (Throwable tr) {
// Exceptions here usually mean invalid RIL responses

                Log.w(LOG_TAG, rr.serialString() + "< "
+ requestToString(rr.mRequest)
+ " exception, possible invalid RIL response", tr);

//Synthetic comment -- @@ -2518,7 +2518,7 @@
throw new RuntimeException("Unrecognized unsol response: " + response);
//break; (implied)
}} catch (Throwable tr) {
            Log.e(LOG_TAG, "Exception processing unsol response: " + response +
"Exception:" + tr.toString());
return;
}
//Synthetic comment -- @@ -2789,7 +2789,7 @@
try {
listInfoRecs = (ArrayList<CdmaInformationRecords>)ret;
} catch (ClassCastException e) {
                    Log.e(LOG_TAG, "Unexpected exception casting to listInfoRecs", e);
break;
}

//Synthetic comment -- @@ -3659,11 +3659,11 @@
}

private void riljLog(String msg) {
        Log.d(LOG_TAG, msg);
}

private void riljLogv(String msg) {
        Log.v(LOG_TAG, msg);
}

private void unsljLog(int response) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RetryManager.java b/src/java/com/android/internal/telephony/RetryManager.java
//Synthetic comment -- index 250d99e..7d9dc9c 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony;

import android.util.Log;
import android.util.Pair;
import android.text.TextUtils;

//Synthetic comment -- @@ -208,7 +208,7 @@
mMaxRetryCount = value.second;
}
} else {
                        Log.e(LOG_TAG, "Unrecognized configuration name value pair: "
+ strArray[i]);
return false;
}
//Synthetic comment -- @@ -365,7 +365,7 @@
value = Integer.parseInt(stringValue);
retVal = new Pair<Boolean, Integer>(validateNonNegativeInt(name, value), value);
} catch (NumberFormatException e) {
            Log.e(LOG_TAG, name + " bad value: " + stringValue, e);
retVal = new Pair<Boolean, Integer>(false, 0);
}
if (VDBG) log("parseNonNetativeInt: " + name + ", " + stringValue + ", "
//Synthetic comment -- @@ -383,7 +383,7 @@
private boolean validateNonNegativeInt(String name, int value) {
boolean retVal;
if (value < 0) {
            Log.e(LOG_TAG, name + " bad value: is < 0");
retVal = false;
} else {
retVal = true;
//Synthetic comment -- @@ -405,6 +405,6 @@
}

private void log(String s) {
        Log.d(LOG_TAG, "[RM] " + s);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/SMSDispatcher.java b/src/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index 6740372..fedadc9 100644

//Synthetic comment -- @@ -51,7 +51,7 @@
import android.text.Html;
import android.text.Spanned;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//Synthetic comment -- @@ -225,7 +225,7 @@
TelephonyProperties.PROPERTY_SMS_RECEIVE, mSmsCapable);
mSmsSendDisabled = !SystemProperties.getBoolean(
TelephonyProperties.PROPERTY_SMS_SEND, mSmsCapable);
        Log.d(TAG, "SMSDispatcher: ctor mSmsCapable=" + mSmsCapable + " format=" + getFormat()
+ " mSmsReceiveDisabled=" + mSmsReceiveDisabled
+ " mSmsSendDisabled=" + mSmsSendDisabled);
}
//Synthetic comment -- @@ -271,7 +271,7 @@

@Override
protected void finalize() {
        Log.d(TAG, "SMSDispatcher finalized");
}


//Synthetic comment -- @@ -296,7 +296,7 @@
case EVENT_NEW_SMS:
// A new SMS has been received by the device
if (false) {
                Log.d(TAG, "New SMS Message Received");
}

SmsMessage sms;
//Synthetic comment -- @@ -304,7 +304,7 @@
ar = (AsyncResult) msg.obj;

if (ar.exception != null) {
                Log.e(TAG, "Exception processing incoming SMS. Exception:" + ar.exception);
return;
}

//Synthetic comment -- @@ -318,7 +318,7 @@
notifyAndAcknowledgeLastIncomingSms(handled, result, null);
}
} catch (RuntimeException ex) {
                Log.e(TAG, "Exception dispatching message", ex);
notifyAndAcknowledgeLastIncomingSms(false, Intents.RESULT_SMS_GENERIC_ERROR, null);
}

//Synthetic comment -- @@ -364,7 +364,7 @@
try {
tracker.mSentIntent.send(RESULT_ERROR_LIMIT_EXCEEDED);
} catch (CanceledException ex) {
                    Log.e(TAG, "failed to send RESULT_ERROR_LIMIT_EXCEEDED");
}
}
mPendingTrackerCount--;
//Synthetic comment -- @@ -426,7 +426,7 @@

if (ar.exception == null) {
if (false) {
                Log.d(TAG, "SMS send complete. Broadcasting "
+ "intent: " + sentIntent);
}

//Synthetic comment -- @@ -454,7 +454,7 @@
}
} else {
if (false) {
                Log.d(TAG, "SMS send failed");
}

int ss = mPhone.getServiceState().getState();
//Synthetic comment -- @@ -609,12 +609,12 @@

// moveToNext() returns false if no duplicates were found
if (cursor.moveToNext()) {
                Log.w(TAG, "Discarding duplicate message segment from address=" + address
+ " refNumber=" + refNumber + " seqNumber=" + seqNumber);
String oldPduString = cursor.getString(PDU_COLUMN);
byte[] oldPdu = HexDump.hexStringToByteArray(oldPduString);
if (!Arrays.equals(oldPdu, pdu)) {
                    Log.e(TAG, "Warning: dup message segment PDU of length " + pdu.length
+ " is different from existing PDU of length " + oldPdu.length);
}
return Intents.RESULT_SMS_HANDLED;
//Synthetic comment -- @@ -672,7 +672,7 @@
// Remove the parts from the database
mResolver.delete(mRawUri, where, whereArgs);
} catch (SQLException e) {
            Log.e(TAG, "Can't access multipart SMS database", e);
return Intents.RESULT_SMS_GENERIC_ERROR;
} finally {
if (cursor != null) cursor.close();
//Synthetic comment -- @@ -938,7 +938,7 @@
sentIntent.send(RESULT_ERROR_NO_SERVICE);
} catch (CanceledException ex) {}
}
            Log.d(TAG, "Device does not support sending sms.");
return;
}

//Synthetic comment -- @@ -961,12 +961,12 @@

if (packageNames == null || packageNames.length == 0) {
// Refuse to send SMS if we can't get the calling package name.
            Log.e(TAG, "Can't get calling app package name: refusing to send SMS");
if (sentIntent != null) {
try {
sentIntent.send(RESULT_ERROR_GENERIC_FAILURE);
} catch (CanceledException ex) {
                    Log.e(TAG, "failed to send error result");
}
}
return;
//Synthetic comment -- @@ -978,12 +978,12 @@
// XXX this is lossy- apps can share a UID
appInfo = pm.getPackageInfo(packageNames[0], PackageManager.GET_SIGNATURES);
} catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Can't get calling app package info: refusing to send SMS");
if (sentIntent != null) {
try {
sentIntent.send(RESULT_ERROR_GENERIC_FAILURE);
} catch (CanceledException ex) {
                    Log.e(TAG, "failed to send error result");
}
}
return;
//Synthetic comment -- @@ -1031,7 +1031,7 @@
if (rule == PREMIUM_RULE_USE_SIM || rule == PREMIUM_RULE_USE_BOTH) {
String simCountryIso = mTelephonyManager.getSimCountryIso();
if (simCountryIso == null || simCountryIso.length() != 2) {
                    Log.e(TAG, "Can't get SIM country Iso: trying network country Iso");
simCountryIso = mTelephonyManager.getNetworkCountryIso();
}

//Synthetic comment -- @@ -1040,7 +1040,7 @@
if (rule == PREMIUM_RULE_USE_NETWORK || rule == PREMIUM_RULE_USE_BOTH) {
String networkCountryIso = mTelephonyManager.getNetworkCountryIso();
if (networkCountryIso == null || networkCountryIso.length() != 2) {
                    Log.e(TAG, "Can't get Network country Iso: trying SIM country Iso");
networkCountryIso = mTelephonyManager.getSimCountryIso();
}

//Synthetic comment -- @@ -1064,11 +1064,11 @@

switch (premiumSmsPermission) {
case SmsUsageMonitor.PREMIUM_SMS_PERMISSION_ALWAYS_ALLOW:
                    Log.d(TAG, "User approved this app to send to premium SMS");
return true;

case SmsUsageMonitor.PREMIUM_SMS_PERMISSION_NEVER_ALLOW:
                    Log.w(TAG, "User denied this app from sending to premium SMS");
sendMessage(obtainMessage(EVENT_STOP_SENDING, tracker));
return false;   // reject this message

//Synthetic comment -- @@ -1098,7 +1098,7 @@
try {
tracker.mSentIntent.send(RESULT_ERROR_LIMIT_EXCEEDED);
} catch (CanceledException ex) {
                Log.e(TAG, "failed to send back RESULT_ERROR_LIMIT_EXCEEDED");
}
return true;
}
//Synthetic comment -- @@ -1117,7 +1117,7 @@
ApplicationInfo appInfo = pm.getApplicationInfo(appPackage, 0);
return appInfo.loadLabel(pm);
} catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "PackageManager Name Not Found for package " + appPackage);
return appPackage;  // fall back to package name if we can't get app label
}
}
//Synthetic comment -- @@ -1373,7 +1373,7 @@
int newSmsPermission = SmsUsageMonitor.PREMIUM_SMS_PERMISSION_ASK_USER;

if (which == DialogInterface.BUTTON_POSITIVE) {
                Log.d(TAG, "CONFIRM sending SMS");
// XXX this is lossy- apps can have more than one signature
EventLog.writeEvent(EventLogTags.SMS_SENT_BY_USER,
mTracker.mAppInfo.signatures[0].toCharsString());
//Synthetic comment -- @@ -1382,7 +1382,7 @@
newSmsPermission = SmsUsageMonitor.PREMIUM_SMS_PERMISSION_ALWAYS_ALLOW;
}
} else if (which == DialogInterface.BUTTON_NEGATIVE) {
                Log.d(TAG, "DENY sending SMS");
// XXX this is lossy- apps can have more than one signature
EventLog.writeEvent(EventLogTags.SMS_DENIED_BY_USER,
mTracker.mAppInfo.signatures[0].toCharsString());
//Synthetic comment -- @@ -1396,13 +1396,13 @@

@Override
public void onCancel(DialogInterface dialog) {
            Log.d(TAG, "dialog dismissed: don't send SMS");
sendMessage(obtainMessage(EVENT_STOP_SENDING, mTracker));
}

@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(TAG, "remember this choice: " + isChecked);
mRememberChoice = isChecked;
if (isChecked) {
mPositiveButton.setText(R.string.sms_short_code_confirm_always_allow);
//Synthetic comment -- @@ -1442,12 +1442,12 @@
if (message.isEmergencyMessage()) {
Intent intent = new Intent(Intents.SMS_EMERGENCY_CB_RECEIVED_ACTION);
intent.putExtra("message", message);
            Log.d(TAG, "Dispatching emergency SMS CB");
dispatch(intent, RECEIVE_EMERGENCY_BROADCAST_PERMISSION);
} else {
Intent intent = new Intent(Intents.SMS_CB_RECEIVED_ACTION);
intent.putExtra("message", message);
            Log.d(TAG, "Dispatching SMS CB");
dispatch(intent, RECEIVE_SMS_PERMISSION);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/SmsStorageMonitor.java b/src/java/com/android/internal/telephony/SmsStorageMonitor.java
//Synthetic comment -- index 0c06ffc..6a9283f 100644

//Synthetic comment -- @@ -25,7 +25,7 @@
import android.os.Message;
import android.os.PowerManager;
import android.provider.Telephony.Sms.Intents;
import android.util.Log;

/**
* Monitors the device and ICC storage, and sends the appropriate events.
//Synthetic comment -- @@ -107,7 +107,7 @@
ar = (AsyncResult) msg.obj;
if (ar.exception != null) {
mReportMemoryStatusPending = true;
                    Log.v(TAG, "Memory status report to modem pending : mStorageAvailable = "
+ mStorageAvailable);
} else {
mReportMemoryStatusPending = false;
//Synthetic comment -- @@ -116,7 +116,7 @@

case EVENT_RADIO_ON:
if (mReportMemoryStatusPending) {
                    Log.v(TAG, "Sending pending memory status report : mStorageAvailable = "
+ mStorageAvailable);
mCm.reportSmsMemoryStatus(mStorageAvailable,
obtainMessage(EVENT_REPORT_MEMORY_STATUS_DONE));








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/SmsUsageMonitor.java b/src/java/com/android/internal/telephony/SmsUsageMonitor.java
//Synthetic comment -- index 0032881..98155fd 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.util.AtomicFile;
import android.util.Log;
import android.util.Xml;

import com.android.internal.util.FastXmlSerializer;
//Synthetic comment -- @@ -290,9 +290,9 @@
parser.setInput(patternReader);
return getPatternMatcherFromXmlParser(parser, country);
} catch (FileNotFoundException e) {
            Log.e(TAG, "Short Code Pattern File not found");
} catch (XmlPullParserException e) {
            Log.e(TAG, "XML parser exception reading short code pattern file", e);
} finally {
mPatternFileLastModified = mPatternFile.lastModified();
if (patternReader != null) {
//Synthetic comment -- @@ -324,13 +324,13 @@
XmlUtils.nextElement(parser);
String element = parser.getName();
if (element == null) {
                    Log.e(TAG, "Parsing pattern data found null");
break;
}

if (element.equals(TAG_SHORTCODE)) {
String currentCountry = parser.getAttributeValue(null, ATTR_COUNTRY);
                    if (VDBG) Log.d(TAG, "Found country " + currentCountry);
if (country.equals(currentCountry)) {
String pattern = parser.getAttributeValue(null, ATTR_PATTERN);
String premium = parser.getAttributeValue(null, ATTR_PREMIUM);
//Synthetic comment -- @@ -339,15 +339,15 @@
return new ShortCodePatternMatcher(pattern, premium, free, standard);
}
} else {
                    Log.e(TAG, "Error: skipping unknown XML tag " + element);
}
}
} catch (XmlPullParserException e) {
            Log.e(TAG, "XML parser exception reading short code patterns", e);
} catch (IOException e) {
            Log.e(TAG, "I/O exception reading short code patterns", e);
}
        if (DBG) Log.d(TAG, "Country (" + country + ") not found");
return null;    // country not found
}

//Synthetic comment -- @@ -398,12 +398,12 @@
synchronized (mSettingsObserverHandler) {
// always allow emergency numbers
if (PhoneNumberUtils.isEmergencyNumber(destAddress, countryIso)) {
                if (DBG) Log.d(TAG, "isEmergencyNumber");
return CATEGORY_NOT_SHORT_CODE;
}
// always allow if the feature is disabled
if (!mCheckEnabled.get()) {
                if (DBG) Log.e(TAG, "check disabled");
return CATEGORY_NOT_SHORT_CODE;
}

//Synthetic comment -- @@ -411,10 +411,10 @@
if (mCurrentCountry == null || !countryIso.equals(mCurrentCountry) ||
mPatternFile.lastModified() != mPatternFileLastModified) {
if (mPatternFile.exists()) {
                        if (DBG) Log.d(TAG, "Loading SMS Short Code patterns from file");
mCurrentPatternMatcher = getPatternMatcherFromFile(countryIso);
} else {
                        if (DBG) Log.d(TAG, "Loading SMS Short Code patterns from resource");
mCurrentPatternMatcher = getPatternMatcherFromResource(countryIso);
}
mCurrentCountry = countryIso;
//Synthetic comment -- @@ -425,7 +425,7 @@
return mCurrentPatternMatcher.getNumberCategory(destAddress);
} else {
// Generic rule: numbers of 5 digits or less are considered potential short codes
                Log.e(TAG, "No patterns for \"" + countryIso + "\": using generic short code rule");
if (destAddress.length() <= 5) {
return CATEGORY_POSSIBLE_PREMIUM_SHORT_CODE;
} else {
//Synthetic comment -- @@ -465,26 +465,26 @@
String packageName = parser.getAttributeValue(null, ATTR_PACKAGE_NAME);
String policy = parser.getAttributeValue(null, ATTR_PACKAGE_SMS_POLICY);
if (packageName == null) {
                                Log.e(TAG, "Error: missing package name attribute");
} else if (policy == null) {
                                Log.e(TAG, "Error: missing package policy attribute");
} else try {
mPremiumSmsPolicy.put(packageName, Integer.parseInt(policy));
} catch (NumberFormatException e) {
                                Log.e(TAG, "Error: non-numeric policy type " + policy);
}
} else {
                            Log.e(TAG, "Error: skipping unknown XML tag " + element);
}
}
} catch (FileNotFoundException e) {
// No data yet
} catch (IOException e) {
                    Log.e(TAG, "Unable to read premium SMS policy database", e);
} catch (NumberFormatException e) {
                    Log.e(TAG, "Unable to parse premium SMS policy database", e);
} catch (XmlPullParserException e) {
                    Log.e(TAG, "Unable to parse premium SMS policy database", e);
} finally {
if (infile != null) {
try {
//Synthetic comment -- @@ -526,7 +526,7 @@

mPolicyFile.finishWrite(outfile);
} catch (IOException e) {
                Log.e(TAG, "Unable to write premium SMS policy database", e);
if (outfile != null) {
mPolicyFile.failWrite(outfile);
}
//Synthetic comment -- @@ -649,6 +649,6 @@
}

private static void log(String msg) {
        Log.d(TAG, msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/TelephonyCapabilities.java b/src/java/com/android/internal/telephony/TelephonyCapabilities.java
//Synthetic comment -- index a9e9376..f374f41 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony;

import android.util.Log;

import com.android.internal.telephony.Phone;

//Synthetic comment -- @@ -104,7 +104,7 @@
} else if (phone.getPhoneType() == PhoneConstants.PHONE_TYPE_CDMA) {
return com.android.internal.R.string.meid;
} else {
            Log.w(LOG_TAG, "getDeviceIdLabel: no known label for phone "
+ phone.getPhoneName());
return 0;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCard.java b/src/java/com/android/internal/telephony/UiccCard.java
//Synthetic comment -- index 038a138..6c4199f 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
import android.os.PowerManager;
import android.os.Registrant;
import android.os.RegistrantList;
import android.util.Log;
import android.view.WindowManager;

import com.android.internal.telephony.PhoneBase;
//Synthetic comment -- @@ -349,10 +349,10 @@
}

private void log(String msg) {
        Log.d(LOG_TAG, msg);
}

private void loge(String msg) {
        Log.e(LOG_TAG, msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCardApplication.java b/src/java/com/android/internal/telephony/UiccCardApplication.java
//Synthetic comment -- index 2718af6..5edddc5 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.util.Log;

import com.android.internal.telephony.IccCardApplicationStatus.AppState;
import com.android.internal.telephony.IccCardApplicationStatus.AppType;
//Synthetic comment -- @@ -690,10 +690,10 @@
}

private void log(String msg) {
        Log.d(LOG_TAG, msg);
}

private void loge(String msg) {
        Log.e(LOG_TAG, msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UsimFileHandler.java b/src/java/com/android/internal/telephony/UsimFileHandler.java
//Synthetic comment -- index 5ef0333..d41844d 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony;

import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccConstants;
//Synthetic comment -- @@ -77,11 +77,11 @@

@Override
protected void logd(String msg) {
        Log.d(LOG_TAG, msg);
}

@Override
protected void loge(String msg) {
        Log.e(LOG_TAG, msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/WapPushOverSms.java b/src/java/com/android/internal/telephony/WapPushOverSms.java
//Synthetic comment -- index e2779dc..b04164b 100755

//Synthetic comment -- @@ -24,7 +24,7 @@
import android.content.ServiceConnection;
import android.provider.Telephony;
import android.provider.Telephony.Sms.Intents;
import android.util.Log;
import android.os.IBinder;
import android.os.RemoteException;

//Synthetic comment -- @@ -61,13 +61,13 @@

public void onServiceConnected(ComponentName name, IBinder service) {
mWapPushMan = IWapPushManager.Stub.asInterface(service);
            if (false) Log.v(LOG_TAG, "wappush manager connected to " +
mOwner.hashCode());
}

public void onServiceDisconnected(ComponentName name) {
mWapPushMan = null;
            if (false) Log.v(LOG_TAG, "wappush manager disconnected.");
// WapPushManager must be always attached.
rebindWapPushManager();
}
//Synthetic comment -- @@ -100,7 +100,7 @@
try {
Thread.sleep(BIND_RETRY_INTERVAL);
} catch (InterruptedException e) {
                            if (false) Log.v(LOG_TAG, "sleep interrupted.");
}
}
}
//Synthetic comment -- @@ -134,7 +134,7 @@
*/
public int dispatchWapPdu(byte[] pdu) {

        if (false) Log.d(LOG_TAG, "Rx: " + IccUtils.bytesToHexString(pdu));

int index = 0;
int transactionId = pdu[index++] & 0xFF;
//Synthetic comment -- @@ -143,7 +143,7 @@

if ((pduType != WspTypeDecoder.PDU_TYPE_PUSH) &&
(pduType != WspTypeDecoder.PDU_TYPE_CONFIRMED_PUSH)) {
            if (false) Log.w(LOG_TAG, "Received non-PUSH WAP PDU. Type = " + pduType);
return Intents.RESULT_SMS_HANDLED;
}

//Synthetic comment -- @@ -156,7 +156,7 @@
* So it will be encoded in no more than 5 octets.
*/
if (pduDecoder.decodeUintvarInteger(index) == false) {
            if (false) Log.w(LOG_TAG, "Received PDU. Header Length error.");
return Intents.RESULT_SMS_GENERIC_ERROR;
}
headerLength = (int)pduDecoder.getValue32();
//Synthetic comment -- @@ -177,7 +177,7 @@
* Length = Uintvar-integer
*/
if (pduDecoder.decodeContentType(index) == false) {
            if (false) Log.w(LOG_TAG, "Received PDU. Header Content-Type error.");
return Intents.RESULT_SMS_GENERIC_ERROR;
}

//Synthetic comment -- @@ -214,14 +214,14 @@

String contentType = ((mimeType == null) ?
Long.toString(binaryContentType) : mimeType);
            if (false) Log.v(LOG_TAG, "appid found: " + wapAppId + ":" + contentType);

try {
boolean processFurther = true;
IWapPushManager wapPushMan = mWapConn.getWapPushManager();

if (wapPushMan == null) {
                    if (false) Log.w(LOG_TAG, "wap push manager not found!");
} else {
Intent intent = new Intent();
intent.putExtra("transactionId", transactionId);
//Synthetic comment -- @@ -232,7 +232,7 @@
pduDecoder.getContentParameters());

int procRet = wapPushMan.processMessage(wapAppId, contentType, intent);
                    if (false) Log.v(LOG_TAG, "procRet:" + procRet);
if ((procRet & WapPushManagerParams.MESSAGE_HANDLED) > 0
&& (procRet & WapPushManagerParams.FURTHER_PROCESSING) == 0) {
processFurther = false;
//Synthetic comment -- @@ -242,13 +242,13 @@
return Intents.RESULT_SMS_HANDLED;
}
} catch (RemoteException e) {
                if (false) Log.w(LOG_TAG, "remote func failed...");
}
}
        if (false) Log.v(LOG_TAG, "fall back to existing handler");

if (mimeType == null) {
            if (false) Log.w(LOG_TAG, "Header Content-Type error.");
return Intents.RESULT_SMS_GENERIC_ERROR;
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatLog.java b/src/java/com/android/internal/telephony/cat/CatLog.java
//Synthetic comment -- index e19ff43..b2e641c 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony.cat;

import android.util.Log;

public abstract class CatLog {
static final boolean DEBUG = true;
//Synthetic comment -- @@ -27,7 +27,7 @@
}

String className = caller.getClass().getName();
        Log.d("CAT", className.substring(className.lastIndexOf('.') + 1) + ": "
+ msg);
}

//Synthetic comment -- @@ -36,6 +36,6 @@
return;
}

        Log.d("CAT", caller + ": " + msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/ComprehensionTlv.java b/src/java/com/android/internal/telephony/cat/ComprehensionTlv.java
//Synthetic comment -- index 22cd5a4..e2522a4 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony.cat;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
//Synthetic comment -- @@ -125,7 +125,7 @@
case 0:
case 0xff:
case 0x80:
                Log.d("CAT     ", "decode: unexpected first tag byte=" + Integer.toHexString(temp) +
", startIndex=" + startIndex + " curIndex=" + curIndex +
" endIndex=" + endIndex);
// Return null which will stop decoding, this has occurred








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/IconLoader.java b/src/java/com/android/internal/telephony/cat/IconLoader.java
//Synthetic comment -- index 2fa1811..8808152 100644

//Synthetic comment -- @@ -25,7 +25,7 @@
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMALTEPhone.java b/src/java/com/android/internal/telephony/cdma/CDMALTEPhone.java
//Synthetic comment -- index cc59b67..fc4d356 100644

//Synthetic comment -- @@ -25,7 +25,7 @@
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.OperatorInfo;
//Synthetic comment -- @@ -177,7 +177,7 @@
// look for our wrapper within the asyncresult, skip the rest if it
// is null.
if (!(ar.userObj instanceof NetworkSelectMessage)) {
            Log.e(LOG_TAG, "unexpected result from user object.");
return;
}

//Synthetic comment -- @@ -200,7 +200,7 @@

// commit and log the result.
if (! editor.commit()) {
            Log.e(LOG_TAG, "failed to commit network selection preference");
}

}
//Synthetic comment -- @@ -218,7 +218,7 @@
mContext.getContentResolver().insert(uri, map);
return true;
} catch (SQLException e) {
                Log.e(LOG_TAG, "[CDMALTEPhone] Can't store current operator ret false", e);
}
} else {
if (DBG) log("updateCurrentCarrierInProvider mIccRecords == null ret false");
//Synthetic comment -- @@ -302,7 +302,7 @@

@Override
protected void log(String s) {
            Log.d(LOG_TAG, "[CDMALTEPhone] " + s);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMAPhone.java b/src/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index c43888b..3eebc2e 100755

//Synthetic comment -- @@ -39,7 +39,7 @@
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
//Synthetic comment -- @@ -265,9 +265,9 @@

@Override
protected void finalize() {
        if(DBG) Log.d(LOG_TAG, "CDMAPhone finalized");
if (mWakeLock.isHeld()) {
            Log.e(LOG_TAG, "UNEXPECTED; mWakeLock is held when finalizing.");
mWakeLock.release();
}
}
//Synthetic comment -- @@ -297,7 +297,7 @@
}

public boolean canTransfer() {
        Log.e(LOG_TAG, "canTransfer: not possible in CDMA");
return false;
}

//Synthetic comment -- @@ -315,7 +315,7 @@

public void conference() throws CallStateException {
// three way calls in CDMA will be handled by feature codes
        Log.e(LOG_TAG, "conference: not possible in CDMA");
}

public void enableEnhancedVoicePrivacy(boolean enable, Message onComplete) {
//Synthetic comment -- @@ -379,7 +379,7 @@

public void registerForSuppServiceNotification(
Handler h, int what, Object obj) {
        Log.e(LOG_TAG, "method registerForSuppServiceNotification is NOT supported in CDMA!");
}

public CdmaCall getBackgroundCall() {
//Synthetic comment -- @@ -387,7 +387,7 @@
}

public boolean handleInCallMmiCommands(String dialString) {
        Log.e(LOG_TAG, "method handleInCallMmiCommands is NOT supported in CDMA!");
return false;
}

//Synthetic comment -- @@ -402,11 +402,11 @@

public void
setNetworkSelectionModeAutomatic(Message response) {
        Log.e(LOG_TAG, "method setNetworkSelectionModeAutomatic is NOT supported in CDMA!");
}

public void unregisterForSuppServiceNotification(Handler h) {
        Log.e(LOG_TAG, "method unregisterForSuppServiceNotification is NOT supported in CDMA!");
}

public void
//Synthetic comment -- @@ -461,14 +461,14 @@
public String getDeviceId() {
String id = getMeid();
if ((id == null) || id.matches("^0*$")) {
            Log.d(LOG_TAG, "getDeviceId(): MEID is not initialized use ESN");
id = getEsn();
}
return id;
}

public String getDeviceSvn() {
        Log.d(LOG_TAG, "getDeviceSvn(): return 0");
return "0";
}

//Synthetic comment -- @@ -477,12 +477,12 @@
}

public String getImei() {
        Log.e(LOG_TAG, "IMEI is not available in CDMA");
return null;
}

public boolean canConference() {
        Log.e(LOG_TAG, "canConference: not possible in CDMA");
return false;
}

//Synthetic comment -- @@ -497,7 +497,7 @@
public void
selectNetworkManually(OperatorInfo network,
Message response) {
        Log.e(LOG_TAG, "selectNetworkManually: not possible in CDMA");
}

public void setOnPostDialCharacter(Handler h, int what, Object obj) {
//Synthetic comment -- @@ -508,7 +508,7 @@
CdmaMmiCode mmi = CdmaMmiCode.newFromDialString(dialString, this);

if (mmi == null) {
            Log.e(LOG_TAG, "Mmi is NULL!");
return false;
} else if (mmi.isPukCommand()) {
mPendingMmis.add(mmi);
//Synthetic comment -- @@ -516,7 +516,7 @@
mmi.processCode();
return true;
}
        Log.e(LOG_TAG, "Unrecognized mmi!");
return false;
}

//Synthetic comment -- @@ -537,11 +537,11 @@
}

public void setLine1Number(String alphaTag, String number, Message onComplete) {
        Log.e(LOG_TAG, "setLine1Number: not possible in CDMA");
}

public void setCallWaiting(boolean enable, Message onComplete) {
        Log.e(LOG_TAG, "method setCallWaiting is NOT supported in CDMA!");
}

public void updateServiceLocation() {
//Synthetic comment -- @@ -647,12 +647,12 @@
}

public void sendUssdResponse(String ussdMessge) {
        Log.e(LOG_TAG, "sendUssdResponse: not possible in CDMA");
}

public void sendDtmf(char c) {
if (!PhoneNumberUtils.is12Key(c)) {
            Log.e(LOG_TAG,
"sendDtmf called with invalid character '" + c + "'");
} else {
if (mCT.state ==  PhoneConstants.State.OFFHOOK) {
//Synthetic comment -- @@ -663,7 +663,7 @@

public void startDtmf(char c) {
if (!PhoneNumberUtils.is12Key(c)) {
            Log.e(LOG_TAG,
"startDtmf called with invalid character '" + c + "'");
} else {
mCM.startDtmf(c, null);
//Synthetic comment -- @@ -678,7 +678,7 @@
boolean check = true;
for (int itr = 0;itr < dtmfString.length(); itr++) {
if (!PhoneNumberUtils.is12Key(dtmfString.charAt(itr))) {
                Log.e(LOG_TAG,
"sendDtmf called with invalid character '" + dtmfString.charAt(itr)+ "'");
check = false;
break;
//Synthetic comment -- @@ -690,11 +690,11 @@
}

public void getAvailableNetworks(Message response) {
        Log.e(LOG_TAG, "getAvailableNetworks: not possible in CDMA");
}

public void setOutgoingCallerIdDisplay(int commandInterfaceCLIRMode, Message onComplete) {
        Log.e(LOG_TAG, "setOutgoingCallerIdDisplay: not possible in CDMA");
}

public void enableLocationUpdates() {
//Synthetic comment -- @@ -771,7 +771,7 @@
}

public void getCallForwardingOption(int commandInterfaceCFReason, Message onComplete) {
        Log.e(LOG_TAG, "getCallForwardingOption: not possible in CDMA");
}

public void setCallForwardingOption(int commandInterfaceCFAction,
//Synthetic comment -- @@ -779,26 +779,26 @@
String dialingNumber,
int timerSeconds,
Message onComplete) {
        Log.e(LOG_TAG, "setCallForwardingOption: not possible in CDMA");
}

public void
getOutgoingCallerIdDisplay(Message onComplete) {
        Log.e(LOG_TAG, "getOutgoingCallerIdDisplay: not possible in CDMA");
}

public boolean
getCallForwardingIndicator() {
        Log.e(LOG_TAG, "getCallForwardingIndicator: not possible in CDMA");
return false;
}

public void explicitCallTransfer() {
        Log.e(LOG_TAG, "explicitCallTransfer: not possible in CDMA");
}

public String getLine1AlphaTag() {
        Log.e(LOG_TAG, "getLine1AlphaTag: not possible in CDMA");
return null;
}

//Synthetic comment -- @@ -852,7 +852,7 @@
Intent intent = new Intent(TelephonyIntents.ACTION_EMERGENCY_CALLBACK_MODE_CHANGED);
intent.putExtra(PhoneConstants.PHONE_IN_ECM_STATE, mIsPhoneInEcmState);
ActivityManagerNative.broadcastStickyIntent(intent,null,UserHandle.USER_ALL);
        if (DBG) Log.d(LOG_TAG, "sendEmergencyCallbackModeChange");
}

@Override
//Synthetic comment -- @@ -866,7 +866,7 @@

private void handleEnterEmergencyCallbackMode(Message msg) {
if (DBG) {
            Log.d(LOG_TAG, "handleEnterEmergencyCallbackMode,mIsPhoneInEcmState= "
+ mIsPhoneInEcmState);
}
// if phone is not in Ecm mode, and it's changed to Ecm mode
//Synthetic comment -- @@ -889,7 +889,7 @@
private void handleExitEmergencyCallbackMode(Message msg) {
AsyncResult ar = (AsyncResult)msg.obj;
if (DBG) {
            Log.d(LOG_TAG, "handleExitEmergencyCallbackMode,ar.exception , mIsPhoneInEcmState "
+ ar.exception + mIsPhoneInEcmState);
}
// Remove pending exit Ecm runnable, if any
//Synthetic comment -- @@ -929,7 +929,7 @@
mEcmTimerResetRegistrants.notifyResult(Boolean.FALSE);
break;
default:
            Log.e(LOG_TAG, "handleTimerInEmergencyCallbackMode, unsupported action " + action);
}
}

//Synthetic comment -- @@ -967,7 +967,7 @@
break;
}

                if (DBG) Log.d(LOG_TAG, "Baseband version: " + ar.result);
setSystemProperty(TelephonyProperties.PROPERTY_BASEBAND_VERSION, (String)ar.result);
}
break;
//Synthetic comment -- @@ -1002,40 +1002,40 @@
break;

case EVENT_RUIM_RECORDS_LOADED:{
                Log.d(LOG_TAG, "Event EVENT_RUIM_RECORDS_LOADED Received");
updateCurrentCarrierInProvider();
}
break;

case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:{
                Log.d(LOG_TAG, "Event EVENT_RADIO_OFF_OR_NOT_AVAILABLE Received");
}
break;

case EVENT_RADIO_ON:{
                Log.d(LOG_TAG, "Event EVENT_RADIO_ON Received");
handleCdmaSubscriptionSource(mCdmaSSM.getCdmaSubscriptionSource());
}
break;

case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:{
                Log.d(LOG_TAG, "EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED");
handleCdmaSubscriptionSource(mCdmaSSM.getCdmaSubscriptionSource());
}
break;

case EVENT_SSN:{
                Log.d(LOG_TAG, "Event EVENT_SSN Received");
}
break;

case EVENT_REGISTERED_TO_NETWORK:{
                Log.d(LOG_TAG, "Event EVENT_REGISTERED_TO_NETWORK Received");
}
break;

case EVENT_NV_READY:{
                Log.d(LOG_TAG, "Event EVENT_NV_READY Received");
prepareEri();
}
break;
//Synthetic comment -- @@ -1097,7 +1097,7 @@
break;

default:
                Log.e(LOG_TAG,"Unknown icc records event code " + eventCode);
break;
}
}
//Synthetic comment -- @@ -1162,7 +1162,7 @@
* @param response Callback message is empty on completion
*/
public void activateCellBroadcastSms(int activate, Message response) {
        Log.e(LOG_TAG, "[CDMAPhone] activateCellBroadcastSms() is obsolete; use SmsManager");
response.sendToTarget();
}

//Synthetic comment -- @@ -1172,7 +1172,7 @@
* @param response Callback message is empty on completion
*/
public void getCellBroadcastSmsConfig(Message response) {
        Log.e(LOG_TAG, "[CDMAPhone] getCellBroadcastSmsConfig() is obsolete; use SmsManager");
response.sendToTarget();
}

//Synthetic comment -- @@ -1182,7 +1182,7 @@
* @param response Callback message is empty on completion
*/
public void setCellBroadcastSmsConfig(int[] configValuesArray, Message response) {
        Log.e(LOG_TAG, "[CDMAPhone] setCellBroadcastSmsConfig() is obsolete; use SmsManager");
response.sendToTarget();
}

//Synthetic comment -- @@ -1254,7 +1254,7 @@
dialStr.substring (IS683A_FEATURE_CODE_NUM_DIGITS,
IS683A_FEATURE_CODE_NUM_DIGITS + IS683A_SYS_SEL_CODE_NUM_DIGITS));
}
        if (DBG) Log.d(LOG_TAG, "extractSelCodeFromOtaSpNum " + sysSelCodeInt);
return sysSelCodeInt;
}

//Synthetic comment -- @@ -1284,7 +1284,7 @@
} catch (NumberFormatException ex) {
// If the carrier ota sp number schema is not correct, we still allow dial
// and only log the error:
            Log.e(LOG_TAG, "checkOtaSpNumBasedOnSysSelCode, error", ex);
}
return isOtaSpNum;
}
//Synthetic comment -- @@ -1321,7 +1321,7 @@
if (!TextUtils.isEmpty(mCarrierOtaSpNumSchema)) {
Matcher m = pOtaSpNumSchema.matcher(mCarrierOtaSpNumSchema);
if (DBG) {
                Log.d(LOG_TAG, "isCarrierOtaSpNum,schema" + mCarrierOtaSpNumSchema);
}

if (m.find()) {
//Synthetic comment -- @@ -1332,7 +1332,7 @@
isOtaSpNum=checkOtaSpNumBasedOnSysSelCode(sysSelCodeInt,sch);
} else {
if (DBG) {
                            Log.d(LOG_TAG, "isCarrierOtaSpNum,sysSelCodeInt is invalid");
}
}
} else if (!TextUtils.isEmpty(sch[0]) && sch[0].equals("FC")) {
//Synthetic comment -- @@ -1341,21 +1341,21 @@
if (dialStr.regionMatches(0,fc,0,fcLen)) {
isOtaSpNum = true;
} else {
                        if (DBG) Log.d(LOG_TAG, "isCarrierOtaSpNum,not otasp number");
}
} else {
if (DBG) {
                        Log.d(LOG_TAG, "isCarrierOtaSpNum,ota schema not supported" + sch[0]);
}
}
} else {
if (DBG) {
                    Log.d(LOG_TAG, "isCarrierOtaSpNum,ota schema pattern not right" +
mCarrierOtaSpNumSchema);
}
}
} else {
            if (DBG) Log.d(LOG_TAG, "isCarrierOtaSpNum,ota schema pattern empty");
}
return isOtaSpNum;
}
//Synthetic comment -- @@ -1377,7 +1377,7 @@
isOtaSpNum = isCarrierOtaSpNum(dialableStr);
}
}
        if (DBG) Log.d(LOG_TAG, "isOtaSpNumber " + isOtaSpNum);
return isOtaSpNum;
}

//Synthetic comment -- @@ -1430,9 +1430,9 @@
iso = MccTable.countryCodeForMcc(Integer.parseInt(
operatorNumeric.substring(0,3)));
} catch (NumberFormatException ex) {
                Log.w(LOG_TAG, "countryCodeForMcc error" + ex);
} catch (StringIndexOutOfBoundsException ex) {
                Log.w(LOG_TAG, "countryCodeForMcc error" + ex);
}

setSystemProperty(PROPERTY_ICC_OPERATOR_ISO_COUNTRY, iso);
//Synthetic comment -- @@ -1459,7 +1459,7 @@

return true;
} catch (SQLException e) {
                Log.e(LOG_TAG, "Can't store current operator", e);
}
}
return false;
//Synthetic comment -- @@ -1508,7 +1508,7 @@

protected void log(String s) {
if (DBG)
            Log.d(LOG_TAG, "[CDMAPhone] " + s);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaCallTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaCallTracker.java
//Synthetic comment -- index a7d5d0a..b6a9b18 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
import android.os.RegistrantList;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.util.Log;
import android.os.SystemProperties;

import com.android.internal.telephony.CallStateException;
//Synthetic comment -- @@ -112,14 +112,14 @@
try {
if(c != null) hangup(c);
} catch (CallStateException ex) {
                Log.e(LOG_TAG, "unexpected error on hangup during dispose");
}
}

try {
if(pendingMO != null) hangup(pendingMO);
} catch (CallStateException ex) {
            Log.e(LOG_TAG, "unexpected error on hangup during dispose");
}

clearDisconnected();
//Synthetic comment -- @@ -128,7 +128,7 @@

@Override
protected void finalize() {
        Log.d(LOG_TAG, "CdmaCallTracker finalized");
}

//***** Instance Methods
//Synthetic comment -- @@ -274,7 +274,7 @@
void
acceptCall() throws CallStateException {
if (ringingCall.getState() == CdmaCall.State.INCOMING) {
            Log.i("phone", "acceptCall: incoming...");
// Always unmute when answering a new call
setMute(false);
cm.acceptCall(obtainCompleteMessage());
//Synthetic comment -- @@ -386,7 +386,7 @@

boolean
canTransfer() {
        Log.e(LOG_TAG, "canTransfer: not possible in CDMA");
return false;
}

//Synthetic comment -- @@ -436,7 +436,7 @@
cm.getCurrentCalls(lastRelevantPoll);
} else if (pendingOperations < 0) {
// this should never happen
            Log.e(LOG_TAG,"CdmaCallTracker.pendingOperations < 0");
pendingOperations = 0;
}
}
//Synthetic comment -- @@ -540,7 +540,7 @@
"poll: hangupPendingMO, hangup conn " + i);
hangup(connections[i]);
} catch (CallStateException ex) {
                            Log.e(LOG_TAG, "unexpected error on hangup");
}

// Do not continue processing this poll
//Synthetic comment -- @@ -608,7 +608,7 @@
// We should follow the rule of MT calls taking precedence over MO calls
// when there is conflict, so here we drop the call info from dc and
// continue to use the call info from conn, and only take a log.
                        Log.e(LOG_TAG,"Error in RIL, Phantom call appeared " + dc);
}
} else {
boolean changed;
//Synthetic comment -- @@ -641,7 +641,7 @@
// We expect the pending call to appear in the list
// If it does not, we land here
if (pendingMO != null) {
            Log.d(LOG_TAG,"Pending MO dropped before poll fg state:"
+ foregroundCall.getState());

droppedDuringPoll.add(pendingMO);
//Synthetic comment -- @@ -756,7 +756,7 @@
} catch (CallStateException ex) {
// Ignore "connection not found"
// Call may have hung up already
                Log.w(LOG_TAG,"CdmaCallTracker WARN: hangup() on absent connection "
+ conn);
}
}
//Synthetic comment -- @@ -776,7 +776,7 @@
} catch (CallStateException ex) {
// Ignore "connection not found"
// Call may have hung up already
            Log.w(LOG_TAG,"CdmaCallTracker WARN: separate() on absent connection "
+ conn);
}
}
//Synthetic comment -- @@ -867,7 +867,7 @@
cm.hangupConnection(cn.getCDMAIndex(), obtainCompleteMessage());
}
} catch (CallStateException ex) {
            Log.e(LOG_TAG, "hangupConnectionByIndex caught " + ex);
}
}

//Synthetic comment -- @@ -949,7 +949,7 @@

switch (msg.what) {
case EVENT_POLL_CALLS_RESULT:{
                Log.d(LOG_TAG, "Event EVENT_POLL_CALLS_RESULT Received");
ar = (AsyncResult)msg.obj;

if(msg == lastRelevantPoll) {
//Synthetic comment -- @@ -982,7 +982,7 @@
// An exception occurred...just treat the disconnect
// cause as "normal"
causeCode = CallFailCause.NORMAL_CLEARING;
                    Log.i(LOG_TAG,
"Exception during getLastCallFailCause, assuming normal disconnect");
} else {
causeCode = ((int[])ar.result)[0];
//Synthetic comment -- @@ -1028,7 +1028,7 @@
ar = (AsyncResult)msg.obj;
if (ar.exception == null) {
handleCallWaitingInfo((CdmaCallWaitingNotification)ar.result);
                   Log.d(LOG_TAG, "Event EVENT_CALL_WAITING_INFO_CDMA Received");
}
break;

//Synthetic comment -- @@ -1056,7 +1056,7 @@
case CDMAPhone.CANCEL_ECM_TIMER: mIsEcmTimerCanceled = true; break;
case CDMAPhone.RESTART_ECM_TIMER: mIsEcmTimerCanceled = false; break;
default:
            Log.e(LOG_TAG, "handleEcmTimer, unsupported action " + action);
}
}

//Synthetic comment -- @@ -1106,7 +1106,7 @@
// Something strange happened: a call which is neither
// a ringing call nor the one we created. It could be the
// call collision result from RIL
            Log.e(LOG_TAG,"Phantom call appeared " + dc);
// If it's a connected call, set the connect time so that
// it's non-zero.  It may not be accurate, but at least
// it won't appear as a Missed Call.
//Synthetic comment -- @@ -1129,7 +1129,7 @@
}

protected void log(String msg) {
        Log.d(LOG_TAG, "[CdmaCallTracker] " + msg);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaCallWaitingNotification.java b/src/java/com/android/internal/telephony/cdma/CdmaCallWaitingNotification.java
//Synthetic comment -- index 0a9bdb73..1d50c8b 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony.cdma;

import android.util.Log;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.PhoneConstants;

//Synthetic comment -- @@ -62,7 +62,7 @@
case 2: return PhoneConstants.PRESENTATION_UNKNOWN;
default:
// This shouldn't happen, just log an error and treat as Unknown
                Log.d(LOG_TAG, "Unexpected presentation " + cli);
return PhoneConstants.PRESENTATION_UNKNOWN;
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaConnection.java
//Synthetic comment -- index 17eecbd..4fe1dc0 100755

//Synthetic comment -- @@ -26,7 +26,7 @@
import android.os.Registrant;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.Log;
import android.text.TextUtils;

import android.telephony.PhoneNumberUtils;
//Synthetic comment -- @@ -151,9 +151,9 @@
h = new MyHandler(owner.getLooper());

this.dialString = dialString;
        Log.d(LOG_TAG, "[CDMAConn] CdmaConnection: dialString=" + dialString);
dialString = formatDialString(dialString);
        Log.d(LOG_TAG, "[CDMAConn] CdmaConnection:formated dialString=" + dialString);

this.address = PhoneNumberUtils.extractNetworkPortionAlt(dialString);
this.postDialString = PhoneNumberUtils.extractPostDialPortion(dialString);
//Synthetic comment -- @@ -304,7 +304,7 @@

public void proceedAfterWaitChar() {
if (postDialState != PostDialState.WAIT) {
            Log.w(LOG_TAG, "CdmaConnection.proceedAfterWaitChar(): Expected "
+ "getPostDialState() to be WAIT but was " + postDialState);
return;
}
//Synthetic comment -- @@ -316,7 +316,7 @@

public void proceedAfterWildChar(String str) {
if (postDialState != PostDialState.WILD) {
            Log.w(LOG_TAG, "CdmaConnection.proceedAfterWaitChar(): Expected "
+ "getPostDialState() to be WILD but was " + postDialState);
return;
}
//Synthetic comment -- @@ -451,7 +451,7 @@

if (!disconnected) {
doDisconnect();
            if (false) Log.d(LOG_TAG,
"[CDMAConn] onDisconnect: cause=" + cause);

owner.phone.notifyDisconnect(this);
//Synthetic comment -- @@ -468,7 +468,7 @@
onLocalDisconnect() {
if (!disconnected) {
doDisconnect();
            if (false) Log.d(LOG_TAG,
"[CDMAConn] onLoalDisconnect" );

if (parent != null) {
//Synthetic comment -- @@ -679,7 +679,7 @@
* and or onConnectedInOrOut.
*/
if (mPartialWakeLock.isHeld()) {
            Log.e(LOG_TAG, "[CdmaConn] UNEXPECTED; mPartialWakeLock is held when finalizing.");
}
releaseWakeLock();
}
//Synthetic comment -- @@ -690,7 +690,7 @@

if (postDialState == PostDialState.CANCELLED) {
releaseWakeLock();
            //Log.v("CDMA", "##### processNextPostDialChar: postDialState == CANCELLED, bail");
return;
}

//Synthetic comment -- @@ -716,7 +716,7 @@
// Will call processNextPostDialChar
h.obtainMessage(EVENT_NEXT_POST_DIAL).sendToTarget();
// Don't notify application
                Log.e("CDMA", "processNextPostDialChar: c=" + c + " isn't valid!");
return;
}
}
//Synthetic comment -- @@ -936,7 +936,7 @@
}

private void log(String msg) {
        Log.d(LOG_TAG, "[CDMAConn] " + msg);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java
//Synthetic comment -- index 8761828..a041974 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.internal.telephony.cdma;

import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionTracker;
//Synthetic comment -- @@ -113,7 +113,7 @@

@Override
protected void log(String s) {
        Log.d(LOG_TAG, "[" + getName() + "] " + s);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 2acc5f9..35e41c7 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
import android.telephony.cdma.CdmaCellLocation;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;

import com.android.internal.telephony.ApnSetting;
import com.android.internal.telephony.CommandsInterface;
//Synthetic comment -- @@ -951,12 +951,12 @@

@Override
protected void log(String s) {
        Log.d(LOG_TAG, "[CdmaDCT] " + s);
}

@Override
protected void loge(String s) {
        Log.e(LOG_TAG, "[CdmaDCT] " + s);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java
//Synthetic comment -- index 07bc6ea..84d15fb 100644

//Synthetic comment -- @@ -37,7 +37,7 @@
import android.os.SystemProperties;

import android.text.TextUtils;
import android.util.Log;
import android.util.EventLog;

import com.android.internal.telephony.IccCardApplicationStatus.AppState;
//Synthetic comment -- @@ -611,12 +611,12 @@

@Override
protected void log(String s) {
        Log.d(LOG_TAG, "[CdmaLteSST] " + s);
}

@Override
protected void loge(String s) {
        Log.e(LOG_TAG, "[CdmaLteSST] " + s);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaMmiCode.java b/src/java/com/android/internal/telephony/cdma/CdmaMmiCode.java
//Synthetic comment -- index 8dd8c2e..eaf5d01 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
//Synthetic comment -- @@ -183,7 +183,7 @@
}

public boolean isUssdRequest() {
        Log.w(LOG_TAG, "isUssdRequest is not implemented in CdmaMmiCode");
return false;
}

//Synthetic comment -- @@ -239,7 +239,7 @@
ar = (AsyncResult) (msg.obj);
onSetComplete(ar);
} else {
            Log.e(LOG_TAG, "Unexpected reply");
}
}
// Private instance methods








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaSMSDispatcher.java b/src/java/com/android/internal/telephony/cdma/CdmaSMSDispatcher.java
//Synthetic comment -- index 1a49db9..8473113 100755

//Synthetic comment -- @@ -39,7 +39,7 @@
import android.telephony.SmsManager;
import android.telephony.cdma.CdmaSmsCbProgramData;
import android.telephony.cdma.CdmaSmsCbProgramResults;
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.GsmAlphabet;
//Synthetic comment -- @@ -116,7 +116,7 @@
private void handleServiceCategoryProgramData(SmsMessage sms) {
ArrayList<CdmaSmsCbProgramData> programDataList = sms.getSmsCbProgramData();
if (programDataList == null) {
            Log.e(TAG, "handleServiceCategoryProgramData: program data list is null!");
return;
}

//Synthetic comment -- @@ -132,7 +132,7 @@

// If sms is null, means there was a parsing error.
if (smsb == null) {
            Log.e(TAG, "dispatchMessage: message is null");
return Intents.RESULT_SMS_GENERIC_ERROR;
}

//Synthetic comment -- @@ -143,7 +143,7 @@

if (mSmsReceiveDisabled) {
// Device doesn't support receiving SMS,
            Log.d(TAG, "Received short message on device which doesn't support "
+ "receiving SMS. Ignored.");
return Intents.RESULT_SMS_HANDLED;
}
//Synthetic comment -- @@ -152,7 +152,7 @@

// Handle CMAS emergency broadcast messages.
if (SmsEnvelope.MESSAGE_TYPE_BROADCAST == sms.getMessageType()) {
            Log.d(TAG, "Broadcast type message");
SmsCbMessage message = sms.parseBroadcastSms();
if (message != null) {
dispatchBroadcastMessage(message);
//Synthetic comment -- @@ -175,7 +175,7 @@
(SmsEnvelope.TELESERVICE_MWI == teleService)) {
// handling Voicemail
int voicemailCount = sms.getNumOfVoicemails();
            Log.d(TAG, "Voicemail count=" + voicemailCount);
// Store the voicemail count in preferences.
SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(
mContext);
//Synthetic comment -- @@ -194,7 +194,7 @@
handled = true;
} else if ((sms.getUserData() == null)) {
if (false) {
                Log.d(TAG, "Received SMS without user data");
}
handled = true;
}
//Synthetic comment -- @@ -243,14 +243,14 @@

int msgType = (0xFF & pdu[index++]);
if (msgType != 0) {
            Log.w(TAG, "Received a WAP SMS which is not WDP. Discard.");
return Intents.RESULT_SMS_HANDLED;
}
int totalSegments = (0xFF & pdu[index++]);   // >= 1
int segment = (0xFF & pdu[index++]);         // >= 0

if (segment >= totalSegments) {
            Log.e(TAG, "WDP bad segment #" + segment + " expecting 0-" + (totalSegments - 1));
return Intents.RESULT_SMS_HANDLED;
}

//Synthetic comment -- @@ -273,7 +273,7 @@
}

// Lookup all other related parts
        Log.i(TAG, "Received WAP PDU. Type = " + msgType + ", originator = " + address
+ ", src-port = " + sourcePort + ", dst-port = " + destinationPort
+ ", ID = " + referenceNumber + ", segment# = " + segment + '/' + totalSegments);

//Synthetic comment -- @@ -344,7 +344,7 @@
} catch (CanceledException ex) {}
}
if (false) {
                Log.d(TAG, "Block SMS in Emergency Callback mode");
}
return;
}
//Synthetic comment -- @@ -443,23 +443,23 @@
int rc = getResultCode();
boolean success = (rc == Activity.RESULT_OK) || (rc == Intents.RESULT_SMS_HANDLED);
if (!success) {
                Log.e(TAG, "SCP results error: result code = " + rc);
return;
}
Bundle extras = getResultExtras(false);
if (extras == null) {
                Log.e(TAG, "SCP results error: missing extras");
return;
}
String sender = extras.getString("sender");
if (sender == null) {
                Log.e(TAG, "SCP results error: missing sender extra.");
return;
}
ArrayList<CdmaSmsCbProgramResults> results
= extras.getParcelableArrayList("results");
if (results == null) {
                Log.e(TAG, "SCP results error: missing results extra.");
return;
}

//Synthetic comment -- @@ -492,7 +492,7 @@
// Ignore the RIL response. TODO: implement retry if SMS send fails.
mCm.sendCdmaSms(baos.toByteArray(), null);
} catch (IOException e) {
                Log.e(TAG, "exception creating SCP results PDU", e);
} finally {
try {
dos.close();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 2554691..8cd9817 100755

//Synthetic comment -- @@ -57,7 +57,7 @@
import android.telephony.cdma.CdmaCellLocation;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.TimeUtils;

import java.io.FileDescriptor;
//Synthetic comment -- @@ -1709,12 +1709,12 @@

@Override
protected void log(String s) {
        Log.d(LOG_TAG, "[CdmaSST] " + s);
}

@Override
protected void loge(String s) {
        Log.e(LOG_TAG, "[CdmaSST] " + s);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaSubscriptionSourceManager.java b/src/java/com/android/internal/telephony/cdma/CdmaSubscriptionSourceManager.java
//Synthetic comment -- index 6d82f3d..972d3c3 100644

//Synthetic comment -- @@ -28,7 +28,7 @@
import android.os.Registrant;
import android.os.RegistrantList;
import android.provider.Settings;
import android.util.Log;

/**
* Class that handles the CDMA subscription source changed events from RIL
//Synthetic comment -- @@ -182,15 +182,15 @@
}

private void log(String s) {
        Log.d(LOG_TAG, "[CdmaSSM] " + s);
}

private void loge(String s) {
        Log.e(LOG_TAG, "[CdmaSSM] " + s);
}

private void logw(String s) {
        Log.w(LOG_TAG, "[CdmaSSM] " + s);
}

}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/EriManager.java b/src/java/com/android/internal/telephony/cdma/EriManager.java
//Synthetic comment -- index 1bcc90a..bb1707c 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.android.internal.telephony.Phone;
//Synthetic comment -- @@ -159,22 +159,22 @@
Resources r = mContext.getResources();

try {
            if (DBG) Log.d(LOG_TAG, "loadEriFileFromXml: check for alternate file");
stream = new FileInputStream(
r.getString(com.android.internal.R.string.alternate_eri_file));
parser = Xml.newPullParser();
parser.setInput(stream, null);
            if (DBG) Log.d(LOG_TAG, "loadEriFileFromXml: opened alternate file");
} catch (FileNotFoundException e) {
            if (DBG) Log.d(LOG_TAG, "loadEriFileFromXml: no alternate file");
parser = null;
} catch (XmlPullParserException e) {
            if (DBG) Log.d(LOG_TAG, "loadEriFileFromXml: no parser for alternate file");
parser = null;
}

if (parser == null) {
            if (DBG) Log.d(LOG_TAG, "loadEriFileFromXml: open normal file");
parser = r.getXml(com.android.internal.R.xml.eri);
}

//Synthetic comment -- @@ -193,7 +193,7 @@
String name = parser.getName();
if (name == null) {
if (parsedEriEntries != mEriFile.mNumberOfEriEntries)
                        Log.e(LOG_TAG, "Error Parsing ERI file: " +  mEriFile.mNumberOfEriEntries
+ " defined, " + parsedEriEntries + " parsed!");
break;
} else if (name.equals("CallPromptId")) {
//Synthetic comment -- @@ -202,7 +202,7 @@
if (id >= 0 && id <= 2) {
mEriFile.mCallPromptId[id] = text;
} else {
                        Log.e(LOG_TAG, "Error Parsing ERI file: found" + id + " CallPromptId");
}

} else if (name.equals("EriInfo")) {
//Synthetic comment -- @@ -220,11 +220,11 @@
}
}

            if (DBG) Log.d(LOG_TAG, "loadEriFileFromXml: eri parsing successful, file loaded");
isEriFileLoaded = true;

} catch (Exception e) {
            Log.e(LOG_TAG, "Got exception while loading ERI file.", e);
} finally {
if (parser instanceof XmlResourceParser) {
((XmlResourceParser)parser).close();
//Synthetic comment -- @@ -290,7 +290,7 @@
if (isEriFileLoaded) {
EriInfo eriInfo = getEriInfo(roamInd);
if (eriInfo != null) {
                if (VDBG) Log.v(LOG_TAG, "ERI roamInd " + roamInd + " found in ERI file");
ret = new EriDisplayInformation(
eriInfo.mIconIndex,
eriInfo.mIconMode,
//Synthetic comment -- @@ -398,16 +398,16 @@
default:
if (!isEriFileLoaded) {
// ERI file NOT loaded
                if (DBG) Log.d(LOG_TAG, "ERI File not loaded");
if(defRoamInd > 2) {
                    if (VDBG) Log.v(LOG_TAG, "ERI defRoamInd > 2 ...flashing");
ret = new EriDisplayInformation(
EriInfo.ROAMING_INDICATOR_FLASH,
EriInfo.ROAMING_ICON_MODE_FLASH,
mContext.getText(com.android.internal
.R.string.roamingText2).toString());
} else {
                    if (VDBG) Log.v(LOG_TAG, "ERI defRoamInd <= 2");
switch (defRoamInd) {
case EriInfo.ROAMING_INDICATOR_ON:
ret = new EriDisplayInformation(
//Synthetic comment -- @@ -443,11 +443,11 @@
EriInfo defEriInfo = getEriInfo(defRoamInd);
if (eriInfo == null) {
if (VDBG) {
                        Log.v(LOG_TAG, "ERI roamInd " + roamInd
+ " not found in ERI file ...using defRoamInd " + defRoamInd);
}
if(defEriInfo == null) {
                        Log.e(LOG_TAG, "ERI defRoamInd " + defRoamInd
+ " not found in ERI file ...on");
ret = new EriDisplayInformation(
EriInfo.ROAMING_INDICATOR_ON,
//Synthetic comment -- @@ -457,7 +457,7 @@

} else {
if (VDBG) {
                            Log.v(LOG_TAG, "ERI defRoamInd " + defRoamInd + " found in ERI file");
}
ret = new EriDisplayInformation(
defEriInfo.mIconIndex,
//Synthetic comment -- @@ -465,7 +465,7 @@
defEriInfo.mEriText);
}
} else {
                    if (VDBG) Log.v(LOG_TAG, "ERI roamInd " + roamInd + " found in ERI file");
ret = new EriDisplayInformation(
eriInfo.mIconIndex,
eriInfo.mIconMode,
//Synthetic comment -- @@ -474,7 +474,7 @@
}
break;
}
        if (VDBG) Log.v(LOG_TAG, "Displaying ERI " + ret.toString());
return ret;
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimFileHandler.java b/src/java/com/android/internal/telephony/cdma/RuimFileHandler.java
//Synthetic comment -- index 4c271f9..7d8a893 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.internal.telephony.cdma;

import android.os.*;
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccFileHandler;
//Synthetic comment -- @@ -67,12 +67,12 @@

@Override
protected void logd(String msg) {
        Log.d(LOG_TAG, "[RuimFileHandler] " + msg);
}

@Override
protected void loge(String msg) {
        Log.e(LOG_TAG, "[RuimFileHandler] " + msg);
}

}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimPhoneBookInterfaceManager.java b/src/java/com/android/internal/telephony/cdma/RuimPhoneBookInterfaceManager.java
//Synthetic comment -- index e919245..c844a0f 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import java.util.concurrent.atomic.AtomicBoolean;

import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
//Synthetic comment -- @@ -46,9 +46,9 @@
try {
super.finalize();
} catch (Throwable throwable) {
            Log.e(LOG_TAG, "Error while finalizing:", throwable);
}
        if(DBG) Log.d(LOG_TAG, "RuimPhoneBookInterfaceManager finalized");
}

public int[] getAdnRecordsSize(int efid) {
//Synthetic comment -- @@ -73,11 +73,11 @@
}

protected void logd(String msg) {
        Log.d(LOG_TAG, "[RuimPbInterfaceManager] " + msg);
}

protected void loge(String msg) {
        Log.e(LOG_TAG, "[RuimPbInterfaceManager] " + msg);
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimRecords.java b/src/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index e8cd8f3..3702f3f 100755

//Synthetic comment -- @@ -31,7 +31,7 @@
import android.os.Message;
import android.os.Registrant;
import android.os.SystemProperties;
import android.util.Log;

import com.android.internal.telephony.AdnRecord;
import com.android.internal.telephony.AdnRecordCache;
//Synthetic comment -- @@ -506,7 +506,7 @@
case EVENT_UPDATE_DONE:
ar = (AsyncResult)msg.obj;
if (ar.exception != null) {
                    Log.i(LOG_TAG, "RuimRecords update failed", ar.exception);
}
break;

//Synthetic comment -- @@ -514,7 +514,7 @@
case EVENT_MARK_SMS_READ_DONE:
case EVENT_SMS_ON_RUIM:
case EVENT_GET_SMS_DONE:
                Log.w(LOG_TAG, "Event not supported: " + msg.what);
break;

// TODO: probably EF_CST should be read instead
//Synthetic comment -- @@ -535,7 +535,7 @@

}}catch (RuntimeException exc) {
// I don't want these exceptions to be fatal
            Log.w(LOG_TAG, "Exception parsing RUIM record", exc);
} finally {
// Count up record load responses even if they are fails
if (isRecordLoadResponse) {
//Synthetic comment -- @@ -799,11 +799,11 @@
}
@Override
protected void log(String s) {
        Log.d(LOG_TAG, "[RuimRecords] " + s);
}

@Override
protected void loge(String s) {
        Log.e(LOG_TAG, "[RuimRecords] " + s);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimSmsInterfaceManager.java b/src/java/com/android/internal/telephony/cdma/RuimSmsInterfaceManager.java
//Synthetic comment -- index 9cd059d..f170b94 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccSmsInterfaceManager;
//Synthetic comment -- @@ -93,9 +93,9 @@
try {
super.finalize();
} catch (Throwable throwable) {
            Log.e(LOG_TAG, "Error while finalizing:", throwable);
}
        if(DBG) Log.d(LOG_TAG, "RuimSmsInterfaceManager finalized");
}

/**
//Synthetic comment -- @@ -193,30 +193,30 @@

public boolean enableCellBroadcast(int messageIdentifier) {
// Not implemented
        Log.e(LOG_TAG, "Error! Not implemented for CDMA.");
return false;
}

public boolean disableCellBroadcast(int messageIdentifier) {
// Not implemented
        Log.e(LOG_TAG, "Error! Not implemented for CDMA.");
return false;
}

public boolean enableCellBroadcastRange(int startMessageId, int endMessageId) {
// Not implemented
        Log.e(LOG_TAG, "Error! Not implemented for CDMA.");
return false;
}

public boolean disableCellBroadcastRange(int startMessageId, int endMessageId) {
// Not implemented
        Log.e(LOG_TAG, "Error! Not implemented for CDMA.");
return false;
}

protected void log(String msg) {
        Log.d(LOG_TAG, "[RuimSmsInterfaceManager] " + msg);
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/SignalToneUtil.java b/src/java/com/android/internal/telephony/cdma/SignalToneUtil.java
//Synthetic comment -- index a149e72..5fedc52 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import java.util.HashMap;
import java.util.HashSet;
import android.util.Log;
import android.media.ToneGenerator;

public class SignalToneUtil {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/SmsMessage.java b/src/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index 617a328..f536a60 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.telephony.SmsCbLocation;
import android.telephony.SmsCbMessage;
import android.telephony.cdma.CdmaSmsCbProgramData;
import android.util.Log;

import com.android.internal.telephony.GsmAlphabet.TextEncodingDetails;
//Synthetic comment -- @@ -110,7 +111,7 @@
msg.parsePdu(pdu);
return msg;
} catch (RuntimeException ex) {
            Log.e(LOG_TAG, "SMS PDU parsing failed: ", ex);
return null;
}
}
//Synthetic comment -- @@ -237,7 +238,7 @@
// or STORED_UNSENT
// See 3GPP2 C.S0023 3.4.27
if ((data[0] & 1) == 0) {
                Log.w(LOG_TAG, "SMS parsing failed: Trying to parse a free record");
return null;
} else {
msg.statusOnIcc = data[0] & 0x07;
//Synthetic comment -- @@ -256,7 +257,7 @@
msg.parsePduFromEfRecord(pdu);
return msg;
} catch (RuntimeException ex) {
            Log.e(LOG_TAG, "SMS PDU parsing failed: ", ex);
return null;
}

//Synthetic comment -- @@ -266,7 +267,7 @@
* Note: This function is a GSM specific functionality which is not supported in CDMA mode.
*/
public static int getTPLayerLengthForPDU(String pdu) {
        Log.w(LOG_TAG, "getTPLayerLengthForPDU: is not supported in CDMA mode.");
return 0;
}

//Synthetic comment -- @@ -369,7 +370,7 @@
* Note: This function is a GSM specific functionality which is not supported in CDMA mode.
*/
public int getProtocolIdentifier() {
        Log.w(LOG_TAG, "getProtocolIdentifier: is not supported in CDMA mode.");
// (3GPP TS 23.040): "no interworking, but SME to SME protocol":
return 0;
}
//Synthetic comment -- @@ -378,7 +379,7 @@
* Note: This function is a GSM specific functionality which is not supported in CDMA mode.
*/
public boolean isReplace() {
        Log.w(LOG_TAG, "isReplace: is not supported in CDMA mode.");
return false;
}

//Synthetic comment -- @@ -387,7 +388,7 @@
* Note: This function is a GSM specific functionality which is not supported in CDMA mode.
*/
public boolean isCphsMwiMessage() {
        Log.w(LOG_TAG, "isCphsMwiMessage: is not supported in CDMA mode.");
return false;
}

//Synthetic comment -- @@ -432,7 +433,7 @@
* Note: This function is a GSM specific functionality which is not supported in CDMA mode.
*/
public boolean isReplyPathPresent() {
        Log.w(LOG_TAG, "isReplyPathPresent: is not supported in CDMA mode.");
return false;
}

//Synthetic comment -- @@ -519,7 +520,7 @@
dis.read(env.bearerData, 0, bearerDataLength);
dis.close();
} catch (Exception ex) {
            Log.e(LOG_TAG, "createFromPdu: conversion from byte array to object failed: " + ex);
}

// link the filled objects to this SMS
//Synthetic comment -- @@ -557,7 +558,7 @@
* this message
*/
env.teleService = dis.readUnsignedShort();
                        Log.i(LOG_TAG, "teleservice = " + env.teleService);
break;
case SERVICE_CATEGORY:
/*
//Synthetic comment -- @@ -603,18 +604,18 @@

} else if (addr.numberMode == CdmaSmsAddress.NUMBER_MODE_DATA_NETWORK) {
if (numberType == 2)
                                    Log.e(LOG_TAG, "TODO: Originating Addr is email id");
else
                                    Log.e(LOG_TAG,
"TODO: Originating Addr is data network address");
} else {
                                Log.e(LOG_TAG, "Originating Addr is of incorrect type");
}
} else {
                            Log.e(LOG_TAG, "Incorrect Digit mode");
}
addr.origBytes = data;
                        Log.i(LOG_TAG, "Originating Addr=" + addr.toString());
break;
case ORIGINATING_SUB_ADDRESS:
case DESTINATION_SUB_ADDRESS:
//Synthetic comment -- @@ -655,7 +656,7 @@
bais.close();
dis.close();
} catch (Exception ex) {
            Log.e(LOG_TAG, "parsePduFromEfRecord: conversion from pdu to SmsMessage failed" + ex);
}

// link the filled objects to this SMS
//Synthetic comment -- @@ -680,16 +681,16 @@
mBearerData.numberOfMessages = 0x000000FF & mEnvelope.bearerData[0];
}
if (false) {
                Log.d(LOG_TAG, "parseSms: get MWI " +
Integer.toString(mBearerData.numberOfMessages));
}
return;
}
mBearerData = BearerData.decode(mEnvelope.bearerData);
        if (Log.isLoggable(LOGGABLE_TAG, Log.VERBOSE)) {
            Log.d(LOG_TAG, "MT raw BearerData = '" +
HexDump.toHexString(mEnvelope.bearerData) + "'");
            Log.d(LOG_TAG, "MT (decoded) BearerData = " + mBearerData);
}
messageRef = mBearerData.messageId;
if (mBearerData.userData != null) {
//Synthetic comment -- @@ -700,7 +701,7 @@

if (originatingAddress != null) {
originatingAddress.address = new String(originatingAddress.origBytes);
            if (false) Log.v(LOG_TAG, "SMS originating address: "
+ originatingAddress.address);
}

//Synthetic comment -- @@ -708,7 +709,7 @@
scTimeMillis = mBearerData.msgCenterTimeStamp.toMillis(true);
}

        if (false) Log.d(LOG_TAG, "SMS SC timestamp: " + scTimeMillis);

// Message Type (See 3GPP2 C.S0015-B, v2, 4.5.1)
if (mBearerData.messageType == BearerData.MESSAGE_TYPE_DELIVERY_ACK) {
//Synthetic comment -- @@ -720,7 +721,7 @@
// message without this subparameter is assumed to
// indicate successful delivery (status == 0).
if (! mBearerData.messageStatusSet) {
                Log.d(LOG_TAG, "DELIVERY_ACK message without msgStatus (" +
(userData == null ? "also missing" : "does have") +
" userData).");
status = 0;
//Synthetic comment -- @@ -733,10 +734,10 @@
}

if (messageBody != null) {
            if (false) Log.v(LOG_TAG, "SMS message body: '" + messageBody + "'");
parseMessageBody();
} else if ((userData != null) && (false)) {
            Log.v(LOG_TAG, "SMS payload: '" + IccUtils.bytesToHexString(userData) + "'");
}
}

//Synthetic comment -- @@ -746,12 +747,12 @@
SmsCbMessage parseBroadcastSms() {
BearerData bData = BearerData.decode(mEnvelope.bearerData, mEnvelope.serviceCategory);
if (bData == null) {
            Log.w(LOG_TAG, "BearerData.decode() returned null");
return null;
}

        if (Log.isLoggable(LOGGABLE_TAG, Log.VERBOSE)) {
            Log.d(LOG_TAG, "MT raw BearerData = " + HexDump.toHexString(mEnvelope.bearerData));
}

String plmn = SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_NUMERIC);
//Synthetic comment -- @@ -791,9 +792,9 @@
int msgId = SystemProperties.getInt(TelephonyProperties.PROPERTY_CDMA_MSG_ID, 1);
String nextMsgId = Integer.toString((msgId % 0xFFFF) + 1);
SystemProperties.set(TelephonyProperties.PROPERTY_CDMA_MSG_ID, nextMsgId);
        if (Log.isLoggable(LOGGABLE_TAG, Log.VERBOSE)) {
            Log.d(LOG_TAG, "next " + TelephonyProperties.PROPERTY_CDMA_MSG_ID + " = " + nextMsgId);
            Log.d(LOG_TAG, "readback gets " +
SystemProperties.get(TelephonyProperties.PROPERTY_CDMA_MSG_ID));
}
return msgId;
//Synthetic comment -- @@ -837,9 +838,9 @@
bearerData.userData = userData;

byte[] encodedBearerData = BearerData.encode(bearerData);
        if (Log.isLoggable(LOGGABLE_TAG, Log.VERBOSE)) {
            Log.d(LOG_TAG, "MO (encoded) BearerData = " + bearerData);
            Log.d(LOG_TAG, "MO raw BearerData = '" + HexDump.toHexString(encodedBearerData) + "'");
}
if (encodedBearerData == null) return null;

//Synthetic comment -- @@ -887,7 +888,7 @@
pdu.encodedScAddress = null;
return pdu;
} catch(IOException ex) {
            Log.e(LOG_TAG, "creating SubmitPdu failed: " + ex);
}
return null;
}
//Synthetic comment -- @@ -934,7 +935,7 @@

mPdu = baos.toByteArray();
} catch (IOException ex) {
            Log.e(LOG_TAG, "createPdu: conversion from object to byte array failed: " + ex);
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/sms/BearerData.java b/src/java/com/android/internal/telephony/cdma/sms/BearerData.java
//Synthetic comment -- index d40242c..35538f6 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import android.telephony.cdma.CdmaSmsCbProgramData;
import android.telephony.cdma.CdmaSmsCbProgramResults;
import android.text.format.Time;
import android.util.Log;

import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.IccUtils;
//Synthetic comment -- @@ -627,7 +627,7 @@
throws CodingException
{
if ((uData.payloadStr == null) && (uData.msgEncoding != UserData.ENCODING_OCTET)) {
            Log.e(LOG_TAG, "user data with null payloadStr");
uData.payloadStr = "";
}

//Synthetic comment -- @@ -639,7 +639,7 @@
if (uData.msgEncodingSet) {
if (uData.msgEncoding == UserData.ENCODING_OCTET) {
if (uData.payload == null) {
                    Log.e(LOG_TAG, "user data with octet encoding but null payload");
uData.payload = new byte[0];
uData.numFields = 0;
} else {
//Synthetic comment -- @@ -647,7 +647,7 @@
}
} else {
if (uData.payloadStr == null) {
                    Log.e(LOG_TAG, "non-octet user data with null payloadStr");
uData.payloadStr = "";
}
if (uData.msgEncoding == UserData.ENCODING_GSM_7BIT_ALPHABET) {
//Synthetic comment -- @@ -937,9 +937,9 @@
}
return outStream.toByteArray();
} catch (BitwiseOutputStream.AccessException ex) {
            Log.e(LOG_TAG, "BearerData encode failed: " + ex);
} catch (CodingException ex) {
            Log.e(LOG_TAG, "BearerData encode failed: " + ex);
}
return null;
}
//Synthetic comment -- @@ -960,7 +960,7 @@
inStream.skip(3);
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "MESSAGE_IDENTIFIER decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1014,7 +1014,7 @@
if (maxNumFields < 0) {
throw new CodingException(charset + " decode failed: offset out of range");
}
            Log.e(LOG_TAG, charset + " decode error: offset = " + offset + " numFields = "
+ numFields + " data.length = " + data.length + " maxNumFields = "
+ maxNumFields);
numFields = maxNumFields;
//Synthetic comment -- @@ -1271,7 +1271,7 @@
inStream.skip(4);
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "REPLY_OPTION decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1291,7 +1291,7 @@
bData.numberOfMessages = IccUtils.cdmaBcdByteToInt((byte)inStream.read(8));
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "NUMBER_OF_MESSAGES decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1311,7 +1311,7 @@
bData.depositIndex = (inStream.read(8) << 8) | inStream.read(8);
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "MESSAGE_DEPOSIT_INDEX decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1394,7 +1394,7 @@
bData.messageStatus = inStream.read(6);
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "MESSAGE_STATUS decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1415,7 +1415,7 @@
bData.msgCenterTimeStamp = TimeStamp.fromByteArray(inStream.readByteArray(6 * 8));
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "MESSAGE_CENTER_TIME_STAMP decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1435,7 +1435,7 @@
bData.validityPeriodAbsolute = TimeStamp.fromByteArray(inStream.readByteArray(6 * 8));
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "VALIDITY_PERIOD_ABSOLUTE decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1456,7 +1456,7 @@
inStream.readByteArray(6 * 8));
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "DEFERRED_DELIVERY_TIME_ABSOLUTE decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1476,7 +1476,7 @@
bData.deferredDeliveryTimeRelative = inStream.read(8);
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "VALIDITY_PERIOD_RELATIVE decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1497,7 +1497,7 @@
bData.validityPeriodRelative = inStream.read(8);
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "DEFERRED_DELIVERY_TIME_RELATIVE decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1519,7 +1519,7 @@
inStream.skip(6);
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "PRIVACY_INDICATOR decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1540,7 +1540,7 @@
bData.language = inStream.read(8);
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "LANGUAGE_INDICATOR decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1562,7 +1562,7 @@
inStream.skip(6);
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "DISPLAY_MODE decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1584,7 +1584,7 @@
inStream.skip(6);
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "PRIORITY_INDICATOR decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1606,7 +1606,7 @@
inStream.skip(6);
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "ALERT_ON_MESSAGE_DELIVERY decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1627,7 +1627,7 @@
bData.userResponseCode = inStream.read(8);
}
if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "USER_RESPONSE_CODE decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ")");
}
//Synthetic comment -- @@ -1689,7 +1689,7 @@
}

if ((! decodeSuccess) || (paramBits > 0)) {
            Log.d(LOG_TAG, "SERVICE_CATEGORY_PROGRAM_DATA decode " +
(decodeSuccess ? "succeeded" : "failed") +
" (extra bits = " + paramBits + ')');
}
//Synthetic comment -- @@ -1823,7 +1823,7 @@
break;

default:
                    Log.w(LOG_TAG, "skipping unsupported CMAS record type " + recordType);
inStream.skip(recordLen * 8);
break;
}
//Synthetic comment -- @@ -1945,7 +1945,7 @@
(1 << SUBPARAM_MESSAGE_IDENTIFIER) ^
(1 << SUBPARAM_USER_DATA))
!= 0) {
                        Log.e(LOG_TAG, "IS-91 must occur without extra subparams (" +
foundSubparamMask + ")");
}
decodeIs91(bData);
//Synthetic comment -- @@ -1955,9 +1955,9 @@
}
return bData;
} catch (BitwiseInputStream.AccessException ex) {
            Log.e(LOG_TAG, "BearerData decode failed: " + ex);
} catch (CodingException ex) {
            Log.e(LOG_TAG, "BearerData decode failed: " + ex);
}
return null;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 393d101..faa0b83 100644

//Synthetic comment -- @@ -35,7 +35,7 @@
import android.telephony.SignalStrength;
import com.android.internal.telephony.CallTracker;
import android.text.TextUtils;
import android.util.Log;

import static com.android.internal.telephony.CommandsInterface.CF_ACTION_DISABLE;
import static com.android.internal.telephony.CommandsInterface.CF_ACTION_ENABLE;
//Synthetic comment -- @@ -176,11 +176,11 @@
try {
Socket sock;
sock = debugSocket.accept();
                                        Log.i(LOG_TAG, "New connection; resetting radio");
mCM.resetRadio(null);
sock.close();
} catch (IOException ex) {
                                        Log.w(LOG_TAG,
"Exception accepting socket", ex);
}
}
//Synthetic comment -- @@ -191,7 +191,7 @@
debugPortThread.start();

} catch (IOException ex) {
                Log.w(LOG_TAG, "Failure to open com.android.internal.telephony.debug socket", ex);
}
}

//Synthetic comment -- @@ -228,7 +228,7 @@

@Override
public void removeReferences() {
        Log.d(LOG_TAG, "removeReferences");
mSimulatedRadioControl = null;
mSimPhoneBookIntManager = null;
mSimSmsIntManager = null;
//Synthetic comment -- @@ -239,7 +239,7 @@
}

protected void finalize() {
        if(LOCAL_DEBUG) Log.d(LOG_TAG, "GSMPhone finalized");
}


//Synthetic comment -- @@ -481,16 +481,16 @@
}

if (getRingingCall().getState() != GsmCall.State.IDLE) {
            if (LOCAL_DEBUG) Log.d(LOG_TAG, "MmiCode 0: rejectCall");
try {
mCT.rejectCall();
} catch (CallStateException e) {
                if (LOCAL_DEBUG) Log.d(LOG_TAG,
"reject failed", e);
notifySuppServiceFailed(Phone.SuppService.REJECT);
}
} else if (getBackgroundCall().getState() != GsmCall.State.IDLE) {
            if (LOCAL_DEBUG) Log.d(LOG_TAG,
"MmiCode 0: hangupWaitingOrBackground");
mCT.hangupWaitingOrBackground();
}
//Synthetic comment -- @@ -514,25 +514,25 @@
int callIndex = ch - '0';

if (callIndex >= 1 && callIndex <= GsmCallTracker.MAX_CONNECTIONS) {
                    if (LOCAL_DEBUG) Log.d(LOG_TAG,
"MmiCode 1: hangupConnectionByIndex " +
callIndex);
mCT.hangupConnectionByIndex(call, callIndex);
}
} else {
if (call.getState() != GsmCall.State.IDLE) {
                    if (LOCAL_DEBUG) Log.d(LOG_TAG,
"MmiCode 1: hangup foreground");
//mCT.hangupForegroundResumeBackground();
mCT.hangup(call);
} else {
                    if (LOCAL_DEBUG) Log.d(LOG_TAG,
"MmiCode 1: switchWaitingOrHoldingAndActive");
mCT.switchWaitingOrHoldingAndActive();
}
}
} catch (CallStateException e) {
            if (LOCAL_DEBUG) Log.d(LOG_TAG,
"hangup failed", e);
notifySuppServiceFailed(Phone.SuppService.HANGUP);
}
//Synthetic comment -- @@ -558,32 +558,32 @@

// gsm index starts at 1, up to 5 connections in a call,
if (conn != null && callIndex >= 1 && callIndex <= GsmCallTracker.MAX_CONNECTIONS) {
                    if (LOCAL_DEBUG) Log.d(LOG_TAG, "MmiCode 2: separate call "+
callIndex);
mCT.separate(conn);
} else {
                    if (LOCAL_DEBUG) Log.d(LOG_TAG, "separate: invalid call index "+
callIndex);
notifySuppServiceFailed(Phone.SuppService.SEPARATE);
}
} catch (CallStateException e) {
                if (LOCAL_DEBUG) Log.d(LOG_TAG,
"separate failed", e);
notifySuppServiceFailed(Phone.SuppService.SEPARATE);
}
} else {
try {
if (getRingingCall().getState() != GsmCall.State.IDLE) {
                    if (LOCAL_DEBUG) Log.d(LOG_TAG,
"MmiCode 2: accept ringing call");
mCT.acceptCall();
} else {
                    if (LOCAL_DEBUG) Log.d(LOG_TAG,
"MmiCode 2: switchWaitingOrHoldingAndActive");
mCT.switchWaitingOrHoldingAndActive();
}
} catch (CallStateException e) {
                if (LOCAL_DEBUG) Log.d(LOG_TAG,
"switch failed", e);
notifySuppServiceFailed(Phone.SuppService.SWITCH);
}
//Synthetic comment -- @@ -598,11 +598,11 @@
return false;
}

        if (LOCAL_DEBUG) Log.d(LOG_TAG, "MmiCode 3: merge calls");
try {
conference();
} catch (CallStateException e) {
            if (LOCAL_DEBUG) Log.d(LOG_TAG,
"conference failed", e);
notifySuppServiceFailed(Phone.SuppService.CONFERENCE);
}
//Synthetic comment -- @@ -618,11 +618,11 @@
return false;
}

        if (LOCAL_DEBUG) Log.d(LOG_TAG, "MmiCode 4: explicit call transfer");
try {
explicitCallTransfer();
} catch (CallStateException e) {
            if (LOCAL_DEBUG) Log.d(LOG_TAG,
"transfer failed", e);
notifySuppServiceFailed(Phone.SuppService.TRANSFER);
}
//Synthetic comment -- @@ -635,7 +635,7 @@
return false;
}

        Log.i(LOG_TAG, "MmiCode 5: CCBS not supported!");
// Treat it as an "unknown" service.
notifySuppServiceFailed(Phone.SuppService.UNKNOWN);
return true;
//Synthetic comment -- @@ -710,7 +710,7 @@
String networkPortion = PhoneNumberUtils.extractNetworkPortionAlt(newDialString);
GsmMmiCode mmi =
GsmMmiCode.newFromDialString(networkPortion, this, mUiccApplication.get());
        if (LOCAL_DEBUG) Log.d(LOG_TAG,
"dialing w/ mmi '" + mmi + "'...");

if (mmi == null) {
//Synthetic comment -- @@ -750,7 +750,7 @@
public void
sendDtmf(char c) {
if (!PhoneNumberUtils.is12Key(c)) {
            Log.e(LOG_TAG,
"sendDtmf called with invalid character '" + c + "'");
} else {
if (mCT.state ==  PhoneConstants.State.OFFHOOK) {
//Synthetic comment -- @@ -762,7 +762,7 @@
public void
startDtmf(char c) {
if (!PhoneNumberUtils.is12Key(c)) {
            Log.e(LOG_TAG,
"startDtmf called with invalid character '" + c + "'");
} else {
mCM.startDtmf(c, null);
//Synthetic comment -- @@ -776,7 +776,7 @@

public void
sendBurstDtmf(String dtmfString) {
        Log.e(LOG_TAG, "[GSMPhone] sendBurstDtmf() is a CDMA method");
}

public void
//Synthetic comment -- @@ -842,12 +842,12 @@
}

public String getEsn() {
        Log.e(LOG_TAG, "[GSMPhone] getEsn() is a CDMA method");
return "0";
}

public String getMeid() {
        Log.e(LOG_TAG, "[GSMPhone] getMeid() is a CDMA method");
return "0";
}

//Synthetic comment -- @@ -924,7 +924,7 @@

public void getCallForwardingOption(int commandInterfaceCFReason, Message onComplete) {
if (isValidCommandInterfaceCFReason(commandInterfaceCFReason)) {
            if (LOCAL_DEBUG) Log.d(LOG_TAG, "requesting call forwarding query.");
Message resp;
if (commandInterfaceCFReason == CF_REASON_UNCONDITIONAL) {
resp = obtainMessage(EVENT_GET_CALL_FORWARD_DONE, onComplete);
//Synthetic comment -- @@ -1009,7 +1009,7 @@
// get the message
Message msg = obtainMessage(EVENT_SET_NETWORK_AUTOMATIC_COMPLETE, nsm);
if (LOCAL_DEBUG)
            Log.d(LOG_TAG, "wrapping and sending message to connect automatically");

mCM.setNetworkSelectionModeAutomatic(msg);
}
//Synthetic comment -- @@ -1199,7 +1199,7 @@
break;
}

                if (LOCAL_DEBUG) Log.d(LOG_TAG, "Baseband version: " + ar.result);
setSystemProperty(PROPERTY_BASEBAND_VERSION, (String)ar.result);
break;

//Synthetic comment -- @@ -1232,7 +1232,7 @@
try {
onIncomingUSSD(Integer.parseInt(ussdResult[0]), ussdResult[1]);
} catch (NumberFormatException e) {
                        Log.w(LOG_TAG, "error parsing USSD");
}
}
break;
//Synthetic comment -- @@ -1389,7 +1389,7 @@
mContext.getContentResolver().insert(uri, map);
return true;
} catch (SQLException e) {
                Log.e(LOG_TAG, "Can't store current operator", e);
}
}
return false;
//Synthetic comment -- @@ -1402,7 +1402,7 @@
// look for our wrapper within the asyncresult, skip the rest if it
// is null.
if (!(ar.userObj instanceof NetworkSelectMessage)) {
            if (LOCAL_DEBUG) Log.d(LOG_TAG, "unexpected result from user object.");
return;
}

//Synthetic comment -- @@ -1411,7 +1411,7 @@
// found the object, now we send off the message we had originally
// attached to the request.
if (nsm.message != null) {
            if (LOCAL_DEBUG) Log.d(LOG_TAG, "sending original message to recipient");
AsyncResult.forMessage(nsm.message, ar.result, ar.exception);
nsm.message.sendToTarget();
}
//Synthetic comment -- @@ -1425,7 +1425,7 @@

// commit and log the result.
if (! editor.commit()) {
            Log.e(LOG_TAG, "failed to commit network selection preference");
}

}
//Synthetic comment -- @@ -1442,7 +1442,7 @@

// commit and log the result.
if (! editor.commit()) {
            Log.e(LOG_TAG, "failed to commit CLIR preference");
}
}

//Synthetic comment -- @@ -1493,7 +1493,7 @@
* @param response Callback message is empty on completion
*/
public void activateCellBroadcastSms(int activate, Message response) {
        Log.e(LOG_TAG, "[GSMPhone] activateCellBroadcastSms() is obsolete; use SmsManager");
response.sendToTarget();
}

//Synthetic comment -- @@ -1503,7 +1503,7 @@
* @param response Callback message is empty on completion
*/
public void getCellBroadcastSmsConfig(Message response) {
        Log.e(LOG_TAG, "[GSMPhone] getCellBroadcastSmsConfig() is obsolete; use SmsManager");
response.sendToTarget();
}

//Synthetic comment -- @@ -1513,7 +1513,7 @@
* @param response Callback message is empty on completion
*/
public void setCellBroadcastSmsConfig(int[] configValuesArray, Message response) {
        Log.e(LOG_TAG, "[GSMPhone] setCellBroadcastSmsConfig() is obsolete; use SmsManager");
response.sendToTarget();
}

//Synthetic comment -- @@ -1561,6 +1561,6 @@
}

protected void log(String s) {
        Log.d(LOG_TAG, "[GSMPhone] " + s);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index fc7fe8a..d8a2fce 100644

//Synthetic comment -- @@ -27,7 +27,7 @@
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.EventLog;
import android.util.Log;

import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CallTracker;
//Synthetic comment -- @@ -114,21 +114,21 @@
try {
if(c != null) hangup(c);
} catch (CallStateException ex) {
                Log.e(LOG_TAG, "unexpected error on hangup during dispose");
}
}

try {
if(pendingMO != null) hangup(pendingMO);
} catch (CallStateException ex) {
            Log.e(LOG_TAG, "unexpected error on hangup during dispose");
}

clearDisconnected();
}

protected void finalize() {
        Log.d(LOG_TAG, "GsmCallTracker finalized");
}

//***** Instance Methods
//Synthetic comment -- @@ -249,7 +249,7 @@
// is no longer call waiting

if (ringingCall.getState() == GsmCall.State.INCOMING) {
            Log.i("phone", "acceptCall: incoming...");
// Always unmute when answering a new call
setMute(false);
cm.acceptCall(obtainCompleteMessage());
//Synthetic comment -- @@ -378,7 +378,7 @@
cm.getCurrentCalls(lastRelevantPoll);
} else if (pendingOperations < 0) {
// this should never happen
            Log.e(LOG_TAG,"GsmCallTracker.pendingOperations < 0");
pendingOperations = 0;
}
}
//Synthetic comment -- @@ -471,7 +471,7 @@
"poll: hangupPendingMO, hangup conn " + i);
hangup(connections[i]);
} catch (CallStateException ex) {
                            Log.e(LOG_TAG, "unexpected error on hangup");
}

// Do not continue processing this poll
//Synthetic comment -- @@ -490,7 +490,7 @@
// Either we've crashed and re-attached to an existing
// call, or something else (eg, SIM) initiated the call.

                        Log.i(LOG_TAG,"Phantom call appeared " + dc);

// If it's a connected call, set the connect time so that
// it's non-zero.  It may not be accurate, but at least
//Synthetic comment -- @@ -552,7 +552,7 @@
// We expect the pending call to appear in the list
// If it does not, we land here
if (pendingMO != null) {
            Log.d(LOG_TAG,"Pending MO dropped before poll fg state:"
+ foregroundCall.getState());

droppedDuringPoll.add(pendingMO);
//Synthetic comment -- @@ -640,27 +640,27 @@
dumpState() {
List l;

        Log.i(LOG_TAG,"Phone State:" + state);

        Log.i(LOG_TAG,"Ringing call: " + ringingCall.toString());

l = ringingCall.getConnections();
for (int i = 0, s = l.size(); i < s; i++) {
            Log.i(LOG_TAG,l.get(i).toString());
}

        Log.i(LOG_TAG,"Foreground call: " + foregroundCall.toString());

l = foregroundCall.getConnections();
for (int i = 0, s = l.size(); i < s; i++) {
            Log.i(LOG_TAG,l.get(i).toString());
}

        Log.i(LOG_TAG,"Background call: " + backgroundCall.toString());

l = backgroundCall.getConnections();
for (int i = 0, s = l.size(); i < s; i++) {
            Log.i(LOG_TAG,l.get(i).toString());
}

}
//Synthetic comment -- @@ -686,7 +686,7 @@
} catch (CallStateException ex) {
// Ignore "connection not found"
// Call may have hung up already
                Log.w(LOG_TAG,"GsmCallTracker WARN: hangup() on absent connection "
+ conn);
}
}
//Synthetic comment -- @@ -706,7 +706,7 @@
} catch (CallStateException ex) {
// Ignore "connection not found"
// Call may have hung up already
            Log.w(LOG_TAG,"GsmCallTracker WARN: separate() on absent connection "
+ conn);
}
}
//Synthetic comment -- @@ -797,7 +797,7 @@
cm.hangupConnection(cn.getGSMIndex(), obtainCompleteMessage());
}
} catch (CallStateException ex) {
            Log.e(LOG_TAG, "hangupConnectionByIndex caught " + ex);
}
}

//Synthetic comment -- @@ -874,7 +874,7 @@
// An exception occurred...just treat the disconnect
// cause as "normal"
causeCode = CallFailCause.NORMAL_CLEARING;
                    Log.i(LOG_TAG,
"Exception during getLastCallFailCause, assuming normal disconnect");
} else {
causeCode = ((int[])ar.result)[0];
//Synthetic comment -- @@ -923,7 +923,7 @@
}

protected void log(String msg) {
        Log.d(LOG_TAG, "[GsmCallTracker] " + msg);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmConnection.java b/src/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 59aa12a..5499f2a 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
import android.os.PowerManager;
import android.os.Registrant;
import android.os.SystemClock;
import android.util.Log;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.text.TextUtils;
//Synthetic comment -- @@ -266,7 +266,7 @@

public void proceedAfterWaitChar() {
if (postDialState != PostDialState.WAIT) {
            Log.w(LOG_TAG, "GsmConnection.proceedAfterWaitChar(): Expected "
+ "getPostDialState() to be WAIT but was " + postDialState);
return;
}
//Synthetic comment -- @@ -278,7 +278,7 @@

public void proceedAfterWildChar(String str) {
if (postDialState != PostDialState.WILD) {
            Log.w(LOG_TAG, "GsmConnection.proceedAfterWaitChar(): Expected "
+ "getPostDialState() to be WILD but was " + postDialState);
return;
}
//Synthetic comment -- @@ -422,7 +422,7 @@
duration = SystemClock.elapsedRealtime() - connectTimeReal;
disconnected = true;

            if (false) Log.d(LOG_TAG,
"[GSMConn] onDisconnect: cause=" + cause);

owner.phone.notifyDisconnect(this);
//Synthetic comment -- @@ -617,7 +617,7 @@
* and or onConnectedInOrOut.
*/
if (mPartialWakeLock.isHeld()) {
            Log.e(LOG_TAG, "[GSMConn] UNEXPECTED; mPartialWakeLock is held when finalizing.");
}
releaseWakeLock();
}
//Synthetic comment -- @@ -628,7 +628,7 @@
Registrant postDialHandler;

if (postDialState == PostDialState.CANCELLED) {
            //Log.v("GSM", "##### processNextPostDialChar: postDialState == CANCELLED, bail");
return;
}

//Synthetic comment -- @@ -651,7 +651,7 @@
// Will call processNextPostDialChar
h.obtainMessage(EVENT_NEXT_POST_DIAL).sendToTarget();
// Don't notify application
                Log.e("GSM", "processNextPostDialChar: c=" + c + " isn't valid!");
return;
}
}
//Synthetic comment -- @@ -671,7 +671,7 @@
// arg1 is the character that was/is being processed
notifyMessage.arg1 = c;

            //Log.v("GSM", "##### processNextPostDialChar: send msg to postDialHandler, arg1=" + c);
notifyMessage.sendToTarget();
}
}
//Synthetic comment -- @@ -753,7 +753,7 @@
}

private void log(String msg) {
        Log.d(LOG_TAG, "[GSMConn] " + msg);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnection.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnection.java
//Synthetic comment -- index 156574d..af36f8e 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.internal.telephony.gsm;

import android.os.Message;
import android.util.Log;
import android.util.Patterns;
import android.text.TextUtils;

//Synthetic comment -- @@ -147,7 +147,7 @@

@Override
protected void log(String s) {
        Log.d(LOG_TAG, "[" + getName() + "] " + s);
}

private boolean isIpAddress(String address) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 4e68450..8a7ea45 100644

//Synthetic comment -- @@ -49,7 +49,7 @@
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;

import com.android.internal.telephony.ApnContext;
import com.android.internal.telephony.ApnSetting;
//Synthetic comment -- @@ -2413,12 +2413,12 @@

@Override
protected void log(String s) {
        Log.d(LOG_TAG, "[GsmDCT] "+ s);
}

@Override
protected void loge(String s) {
        Log.e(LOG_TAG, "[GsmDCT] " + s);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index 10efdc4..08a8b85 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
import android.telephony.PhoneNumberUtils;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;

import static com.android.internal.telephony.CommandsInterface.*;

//Synthetic comment -- @@ -476,7 +476,7 @@

static private boolean
isTwoDigitShortCode(Context context, String dialString) {
        Log.d(LOG_TAG, "isTwoDigitShortCode");

if (dialString == null || dialString.length() != 2) return false;

//Synthetic comment -- @@ -486,13 +486,13 @@
}

for (String dialnumber : sTwoDigitNumberPattern) {
            Log.d(LOG_TAG, "Two Digit Number Pattern " + dialnumber);
if (dialString.equals(dialnumber)) {
                Log.d(LOG_TAG, "Two Digit Number Pattern -true");
return true;
}
}
        Log.d(LOG_TAG, "Two Digit Number Pattern -false");
return false;
}

//Synthetic comment -- @@ -637,14 +637,14 @@
processCode () {
try {
if (isShortCode()) {
                Log.d(LOG_TAG, "isShortCode");
// These just get treated as USSD.
sendUssd(dialingNumber);
} else if (dialingNumber != null) {
// We should have no dialing numbers here
throw new RuntimeException ("Invalid or Unsupported MMI Code");
} else if (sc != null && sc.equals(SC_CLIP)) {
                Log.d(LOG_TAG, "is CLIP");
if (isInterrogate()) {
phone.mCM.queryCLIP(
obtainMessage(EVENT_QUERY_COMPLETE, this));
//Synthetic comment -- @@ -652,7 +652,7 @@
throw new RuntimeException ("Invalid or Unsupported MMI Code");
}
} else if (sc != null && sc.equals(SC_CLIR)) {
                Log.d(LOG_TAG, "is CLIR");
if (isActivate()) {
phone.mCM.setCLIR(CommandsInterface.CLIR_INVOCATION,
obtainMessage(EVENT_SET_COMPLETE, this));
//Synthetic comment -- @@ -666,7 +666,7 @@
throw new RuntimeException ("Invalid or Unsupported MMI Code");
}
} else if (isServiceCodeCallForwarding(sc)) {
                Log.d(LOG_TAG, "is CF");

String dialingNumber = sia;
int serviceClass = siToServiceClass(sib);
//Synthetic comment -- @@ -702,7 +702,7 @@
((cfAction == CommandsInterface.CF_ACTION_ENABLE) ||
(cfAction == CommandsInterface.CF_ACTION_REGISTRATION)) ? 1 : 0;

                    Log.d(LOG_TAG, "is CF setCallForward");
phone.mCM.setCallForward(cfAction, reason, serviceClass,
dialingNumber, time, obtainMessage(
EVENT_SET_CFF_COMPLETE,
//Synthetic comment -- @@ -957,7 +957,7 @@
if (ar.exception instanceof CommandException) {
CommandException.Error err = ((CommandException)(ar.exception)).getCommandError();
if (err == CommandException.Error.FDN_CHECK_FAILURE) {
                Log.i(LOG_TAG, "FDN_CHECK_FAILURE");
return context.getText(com.android.internal.R.string.mmiFdnError);
}
}
//Synthetic comment -- @@ -1018,7 +1018,7 @@
sb.append(context.getText(
com.android.internal.R.string.needPuk2));
} else if (err == CommandException.Error.FDN_CHECK_FAILURE) {
                    Log.i(LOG_TAG, "FDN_CHECK_FAILURE");
sb.append(context.getText(com.android.internal.R.string.mmiFdnError));
} else {
sb.append(context.getText(








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/src/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 9295773..16cff84 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
import android.telephony.SmsCbMessage;
import android.telephony.SmsManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.GsmAlphabet;
//Synthetic comment -- @@ -103,10 +103,10 @@
case EVENT_WRITE_SMS_COMPLETE:
AsyncResult ar = (AsyncResult) msg.obj;
if (ar.exception == null) {
                Log.d(TAG, "Successfully wrote SMS-PP message to UICC");
mCm.acknowledgeLastIncomingGsmSms(true, 0, null);
} else {
                Log.d(TAG, "Failed to write SMS-PP message to UICC", ar.exception);
mCm.acknowledgeLastIncomingGsmSms(false,
CommandsInterface.GSM_SMS_FAIL_CAUSE_UNSPECIFIED_ERROR, null);
}
//Synthetic comment -- @@ -160,7 +160,7 @@

// If sms is null, means there was a parsing error.
if (smsb == null) {
            Log.e(TAG, "dispatchMessage: message is null");
return Intents.RESULT_SMS_GENERIC_ERROR;
}

//Synthetic comment -- @@ -169,7 +169,7 @@
if (sms.isTypeZero()) {
// As per 3GPP TS 23.040 9.2.3.9, Type Zero messages should not be
// Displayed/Stored/Notified. They should only be acknowledged.
            Log.d(TAG, "Received short message type 0, Don't display or store it. Send Ack");
return Intents.RESULT_SMS_HANDLED;
}

//Synthetic comment -- @@ -182,10 +182,10 @@
// records have been loaded, after the USIM service table has been loaded.
if (ust != null && ust.isAvailable(
UsimServiceTable.UsimService.DATA_DL_VIA_SMS_PP)) {
                Log.d(TAG, "Received SMS-PP data download, sending to UICC.");
return mDataDownloadHandler.startDataDownload(sms);
} else {
                Log.d(TAG, "DATA_DL_VIA_SMS_PP service not available, storing message to UICC.");
String smsc = IccUtils.bytesToHexString(
PhoneNumberUtils.networkPortionToCalledPartyBCDWithLength(
sms.getServiceCenterAddress()));
//Synthetic comment -- @@ -198,7 +198,7 @@

if (mSmsReceiveDisabled) {
// Device doesn't support SMS service,
            Log.d(TAG, "Received short message on device which doesn't support "
+ "SMS service. Ignored.");
return Intents.RESULT_SMS_HANDLED;
}
//Synthetic comment -- @@ -209,13 +209,13 @@
mPhone.setVoiceMessageWaiting(1, -1);  // line 1: unknown number of msgs waiting
handled = sms.isMwiDontStore();
if (false) {
                Log.d(TAG, "Received voice mail indicator set SMS shouldStore=" + !handled);
}
} else if (sms.isMWIClearMessage()) {
mPhone.setVoiceMessageWaiting(1, 0);   // line 1: no msgs waiting
handled = sms.isMwiDontStore();
if (false) {
                Log.d(TAG, "Received voice mail indicator clear SMS shouldStore=" + !handled);
}
}

//Synthetic comment -- @@ -243,7 +243,7 @@
sendRawPdu(pdu.encodedScAddress, pdu.encodedMessage, sentIntent, deliveryIntent,
destAddr);
} else {
            Log.e(TAG, "GsmSMSDispatcher.sendData(): getSubmitPdu() returned null");
}
}

//Synthetic comment -- @@ -257,7 +257,7 @@
sendRawPdu(pdu.encodedScAddress, pdu.encodedMessage, sentIntent, deliveryIntent,
destAddr);
} else {
            Log.e(TAG, "GsmSMSDispatcher.sendText(): getSubmitPdu() returned null");
}
}

//Synthetic comment -- @@ -280,7 +280,7 @@
sendRawPdu(pdu.encodedScAddress, pdu.encodedMessage, sentIntent, deliveryIntent,
destinationAddress);
} else {
            Log.e(TAG, "GsmSMSDispatcher.sendNewSubmitPdu(): getSubmitPdu() returned null");
}
}

//Synthetic comment -- @@ -387,7 +387,7 @@
}
sb.append(Integer.toHexString(b)).append(' ');
}
                    Log.d(TAG, sb.toString());
}
}

//Synthetic comment -- @@ -473,7 +473,7 @@
}
}
} catch (RuntimeException e) {
            Log.e(TAG, "Error in decoding SMS CB pdu", e);
}
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 6110bd1..b5ff551 100644

//Synthetic comment -- @@ -65,7 +65,7 @@
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.TimeUtils;

import java.io.FileDescriptor;
//Synthetic comment -- @@ -183,7 +183,7 @@
private ContentObserver mAutoTimeObserver = new ContentObserver(new Handler()) {
@Override
public void onChange(boolean selfChange) {
            Log.i("GsmServiceStateTracker", "Auto time state changed");
revertToNitzTime();
}
};
//Synthetic comment -- @@ -191,7 +191,7 @@
private ContentObserver mAutoTimeZoneObserver = new ContentObserver(new Handler()) {
@Override
public void onChange(boolean selfChange) {
            Log.i("GsmServiceStateTracker", "Auto time zone state changed");
revertToNitzTimeZone();
}
};
//Synthetic comment -- @@ -272,7 +272,7 @@
Message message;

if (!phone.mIsTheCurrentActivePhone) {
            Log.e(LOG_TAG, "Received message " + msg +
"[" + msg.what + "] while being destroyed. Ignoring.");
return;
}
//Synthetic comment -- @@ -339,7 +339,7 @@
cid = Integer.parseInt(states[2], 16);
}
} catch (NumberFormatException ex) {
                            Log.w(LOG_TAG, "error parsing location: " + ex);
}
}
cellLoc.setLacAndCid(lac, cid);
//Synthetic comment -- @@ -1503,7 +1503,7 @@
}

setAndBroadcastNetworkSetTime(c.getTimeInMillis());
                    Log.i(LOG_TAG, "NITZ: after Setting time of day");
}
SystemProperties.set("gsm.nitz.time", String.valueOf(c.getTimeInMillis()));
saveNitzTime(c.getTimeInMillis());
//Synthetic comment -- @@ -1702,16 +1702,16 @@
}
@Override
protected void log(String s) {
        Log.d(LOG_TAG, "[GsmSST] " + s);
}

@Override
protected void loge(String s) {
        Log.e(LOG_TAG, "[GsmSST] " + s);
}

private static void sloge(String s) {
        Log.e(LOG_TAG, "[GsmSST] " + s);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index 0387a70..5296df2 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.internal.telephony.gsm;

import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccConstants;
//Synthetic comment -- @@ -71,18 +71,18 @@
}
String path = getCommonIccEFPath(efid);
if (path == null) {
            Log.e(LOG_TAG, "Error: EF Path being returned in null");
}
return path;
}

@Override
protected void logd(String msg) {
        Log.d(LOG_TAG, "[SIMFileHandler] " + msg);
}

@Override
protected void loge(String msg) {
        Log.e(LOG_TAG, "[SIMFileHandler] " + msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index e866757..dc2c477 100755

//Synthetic comment -- @@ -25,7 +25,7 @@
import android.os.Message;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.AdnRecord;
import com.android.internal.telephony.AdnRecordCache;
//Synthetic comment -- @@ -944,7 +944,7 @@
break;

case EVENT_MARK_SMS_READ_DONE:
                Log.i("ENF", "marked read: sms " + msg.arg1);
break;


//Synthetic comment -- @@ -1205,7 +1205,7 @@

private void handleSms(byte[] ba) {
if (ba[0] != 0)
            Log.d("ENF", "status : " + ba[0]);

// 3GPP TS 51.011 v5.0.0 (20011-12)  10.5.3
// 3 == "received by MS from network; message to be read"
//Synthetic comment -- @@ -1230,7 +1230,7 @@
byte[] ba = (byte[]) messages.get(i);

if (ba[0] != 0)
                Log.i("ENF", "status " + i + ": " + ba[0]);

// 3GPP TS 51.011 v5.0.0 (20011-12)  10.5.3
// 3 == "received by MS from network; message to be read"
//Synthetic comment -- @@ -1618,19 +1618,19 @@
}

protected void log(String s) {
        Log.d(LOG_TAG, "[SIMRecords] " + s);
}

protected void loge(String s) {
        Log.e(LOG_TAG, "[SIMRecords] " + s);
}

protected void logw(String s, Throwable tr) {
        Log.w(LOG_TAG, "[SIMRecords] " + s, tr);
}

protected void logv(String s) {
        Log.v(LOG_TAG, "[SIMRecords] " + s);
}

/**








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SimPhoneBookInterfaceManager.java b/src/java/com/android/internal/telephony/gsm/SimPhoneBookInterfaceManager.java
//Synthetic comment -- index 37f9a4f..509a0df 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import java.util.concurrent.atomic.AtomicBoolean;

import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
//Synthetic comment -- @@ -46,9 +46,9 @@
try {
super.finalize();
} catch (Throwable throwable) {
            Log.e(LOG_TAG, "Error while finalizing:", throwable);
}
        if(DBG) Log.d(LOG_TAG, "SimPhoneBookInterfaceManager finalized");
}

public int[] getAdnRecordsSize(int efid) {
//Synthetic comment -- @@ -72,11 +72,11 @@
}

protected void logd(String msg) {
        Log.d(LOG_TAG, "[SimPbInterfaceManager] " + msg);
}

protected void loge(String msg) {
        Log.e(LOG_TAG, "[SimPbInterfaceManager] " + msg);
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SimSmsInterfaceManager.java b/src/java/com/android/internal/telephony/gsm/SimSmsInterfaceManager.java
//Synthetic comment -- index 92bf390..d5f7140 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccSmsInterfaceManager;
//Synthetic comment -- @@ -115,9 +115,9 @@
try {
super.finalize();
} catch (Throwable throwable) {
            Log.e(LOG_TAG, "Error while finalizing:", throwable);
}
        if(DBG) Log.d(LOG_TAG, "SimSmsInterfaceManager finalized");
}

/**
//Synthetic comment -- @@ -360,6 +360,6 @@

@Override
protected void log(String msg) {
        Log.d(LOG_TAG, "[SimSmsInterfaceManager] " + msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SmsMessage.java b/src/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 9421dd7..fd4d8c1 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import android.telephony.PhoneNumberUtils;
import android.text.format.Time;
import android.util.Log;

import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
//Synthetic comment -- @@ -105,7 +105,7 @@
msg.parsePdu(pdu);
return msg;
} catch (RuntimeException ex) {
            Log.e(LOG_TAG, "SMS PDU parsing failed: ", ex);
return null;
}
}
//Synthetic comment -- @@ -133,7 +133,7 @@
msg.parsePdu(IccUtils.hexStringToBytes(lines[1]));
return msg;
} catch (RuntimeException ex) {
            Log.e(LOG_TAG, "SMS PDU parsing failed: ", ex);
return null;
}
}
//Synthetic comment -- @@ -145,7 +145,7 @@
msg.parsePdu(IccUtils.hexStringToBytes(line));
return msg;
} catch (RuntimeException ex) {
            Log.e(LOG_TAG, "CDS SMS PDU parsing failed: ", ex);
return null;
}
}
//Synthetic comment -- @@ -170,7 +170,7 @@
// or STORED_UNSENT
// See TS 51.011 10.5.3
if ((data[0] & 1) == 0) {
                Log.w(LOG_TAG,
"SMS parsing failed: Trying to parse a free record");
return null;
} else {
//Synthetic comment -- @@ -186,7 +186,7 @@
msg.parsePdu(pdu);
return msg;
} catch (RuntimeException ex) {
            Log.e(LOG_TAG, "SMS PDU parsing failed: ", ex);
return null;
}
}
//Synthetic comment -- @@ -256,7 +256,7 @@
SmsHeader smsHeader = SmsHeader.fromByteArray(header);
if (smsHeader.languageTable != languageTable
|| smsHeader.languageShiftTable != languageShiftTable) {
                        Log.w(LOG_TAG, "Updating language table in SMS header: "
+ smsHeader.languageTable + " -> " + languageTable + ", "
+ smsHeader.languageShiftTable + " -> " + languageShiftTable);
smsHeader.languageTable = languageTable;
//Synthetic comment -- @@ -289,7 +289,7 @@
try {
userData = encodeUCS2(message, header);
} catch(UnsupportedEncodingException uex) {
                    Log.e(LOG_TAG,
"Implausible UnsupportedEncodingException ",
uex);
return null;
//Synthetic comment -- @@ -302,7 +302,7 @@
userData = encodeUCS2(message, header);
encoding = ENCODING_16BIT;
} catch(UnsupportedEncodingException uex) {
                Log.e(LOG_TAG,
"Implausible UnsupportedEncodingException ",
uex);
return null;
//Synthetic comment -- @@ -312,7 +312,7 @@
if (encoding == ENCODING_7BIT) {
if ((0xff & userData[0]) > MAX_USER_DATA_SEPTETS) {
// Message too long
                Log.e(LOG_TAG, "Message too long (" + (0xff & userData[0]) + " septets)");
return null;
}
// TP-Data-Coding-Scheme
//Synthetic comment -- @@ -327,7 +327,7 @@
} else { // assume UCS-2
if ((0xff & userData[0]) > MAX_USER_DATA_BYTES) {
// Message too long
                Log.e(LOG_TAG, "Message too long (" + (0xff & userData[0]) + " bytes)");
return null;
}
// TP-Data-Coding-Scheme
//Synthetic comment -- @@ -411,7 +411,7 @@
byte[] smsHeaderData = SmsHeader.toByteArray(smsHeader);

if ((data.length + smsHeaderData.length + 1) > MAX_USER_DATA_BYTES) {
            Log.e(LOG_TAG, "SMS data message may only contain "
+ (MAX_USER_DATA_BYTES - smsHeaderData.length - 1) + " bytes");
return null;
}
//Synthetic comment -- @@ -472,7 +472,7 @@
if (statusReportRequested) {
// Set TP-Status-Report-Request bit.
mtiByte |= 0x20;
            if (false) Log.d(LOG_TAG, "SMS status report requested");
}
bo.write(mtiByte);

//Synthetic comment -- @@ -530,7 +530,7 @@
ret = PhoneNumberUtils
.calledPartyBCDToString(pdu, cur, len);
} catch (RuntimeException tr) {
                    Log.d(LOG_TAG, "invalid SC address: ", tr);
ret = null;
}
}
//Synthetic comment -- @@ -564,7 +564,7 @@
try {
ret = new GsmSmsAddress(pdu, cur, lengthBytes);
} catch (ParseException e) {
                Log.e(LOG_TAG, e.getMessage());
ret = null;
}

//Synthetic comment -- @@ -738,7 +738,7 @@
ret = new String(pdu, cur, byteCount, "utf-16");
} catch (UnsupportedEncodingException ex) {
ret = "";
                Log.e(LOG_TAG, "implausible UnsupportedEncodingException", ex);
}

cur += byteCount;
//Synthetic comment -- @@ -759,7 +759,7 @@
ret = new String(pdu, cur, byteCount, "KSC5601");
} catch (UnsupportedEncodingException ex) {
ret = "";
                Log.e(LOG_TAG, "implausible UnsupportedEncodingException", ex);
}

cur += byteCount;
//Synthetic comment -- @@ -898,15 +898,15 @@
*/
private void parsePdu(byte[] pdu) {
mPdu = pdu;
        // Log.d(LOG_TAG, "raw sms message:");
        // Log.d(LOG_TAG, s);

PduParser p = new PduParser(pdu);

scAddress = p.getSCAddress();

if (scAddress != null) {
            if (false) Log.d(LOG_TAG, "SMS SC address: " + scAddress);
}

// TODO(mkf) support reply path, user data header indicator
//Synthetic comment -- @@ -992,7 +992,7 @@
originatingAddress = p.getAddress();

if (originatingAddress != null) {
            if (false) Log.v(LOG_TAG, "SMS originating address: "
+ originatingAddress.address);
}

//Synthetic comment -- @@ -1005,13 +1005,13 @@
dataCodingScheme = p.getByte();

if (false) {
            Log.v(LOG_TAG, "SMS TP-PID:" + protocolIdentifier
+ " data coding scheme: " + dataCodingScheme);
}

scTimeMillis = p.getSCTimestampMillis();

        if (false) Log.d(LOG_TAG, "SMS SC timestamp: " + scTimeMillis);

boolean hasUserDataHeader = (firstByte & 0x40) == 0x40;

//Synthetic comment -- @@ -1039,7 +1039,7 @@
hasMessageClass = (0 != (dataCodingScheme & 0x10));

if (userDataCompressed) {
                Log.w(LOG_TAG, "4 - Unsupported SMS data coding scheme "
+ "(compression) " + (dataCodingScheme & 0xff));
} else {
switch ((dataCodingScheme >> 2) & 0x3) {
//Synthetic comment -- @@ -1053,7 +1053,7 @@

case 1: // 8 bit data
case 3: // reserved
                    Log.w(LOG_TAG, "1 - Unsupported SMS data coding scheme "
+ (dataCodingScheme & 0xff));
encodingType = ENCODING_8BIT;
break;
//Synthetic comment -- @@ -1098,7 +1098,7 @@
} else {
isMwi = false;

                Log.w(LOG_TAG, "MWI for fax, email, or other "
+ (dataCodingScheme & 0xff));
}
} else if ((dataCodingScheme & 0xC0) == 0x80) {
//Synthetic comment -- @@ -1108,11 +1108,11 @@
// This value used for KSC5601 by carriers in Korea.
encodingType = ENCODING_KSC5601;
} else {
                Log.w(LOG_TAG, "5 - Unsupported SMS data coding scheme "
+ (dataCodingScheme & 0xff));
}
} else {
            Log.w(LOG_TAG, "3 - Unsupported SMS data coding scheme "
+ (dataCodingScheme & 0xff));
}

//Synthetic comment -- @@ -1143,7 +1143,7 @@
break;
}

        if (false) Log.v(LOG_TAG, "SMS message body (raw): '" + messageBody + "'");

if (messageBody != null) {
parseMessageBody();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SpnOverride.java b/src/java/com/android/internal/telephony/gsm/SpnOverride.java
//Synthetic comment -- index 918c2d2..575754e 100644

//Synthetic comment -- @@ -26,7 +26,7 @@
import org.xmlpull.v1.XmlPullParserException;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.android.internal.util.XmlUtils;
//Synthetic comment -- @@ -60,7 +60,7 @@
try {
spnReader = new FileReader(spnFile);
} catch (FileNotFoundException e) {
            Log.w(LOG_TAG, "Can't open " +
Environment.getRootDirectory() + "/" + PARTNER_SPN_OVERRIDE_PATH);
return;
}
//Synthetic comment -- @@ -85,9 +85,9 @@
CarrierSpnMap.put(numeric, data);
}
} catch (XmlPullParserException e) {
            Log.w(LOG_TAG, "Exception in spn-conf parser " + e);
} catch (IOException e) {
            Log.w(LOG_TAG, "Exception in spn-conf parser " + e);
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/UsimDataDownloadHandler.java b/src/java/com/android/internal/telephony/gsm/UsimDataDownloadHandler.java
//Synthetic comment -- index f47ff1b..4d7fc9b 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony.Sms.Intents;
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccIoResult;
//Synthetic comment -- @@ -67,7 +67,7 @@
if (sendMessage(obtainMessage(EVENT_START_DATA_DOWNLOAD, smsMessage))) {
return Activity.RESULT_OK;  // we will send SMS ACK/ERROR based on UICC response
} else {
            Log.e(TAG, "startDataDownload failed to send message to start data download.");
return Intents.RESULT_SMS_GENERIC_ERROR;
}
}
//Synthetic comment -- @@ -122,7 +122,7 @@

// Verify that we calculated the payload size correctly.
if (index != envelope.length) {
            Log.e(TAG, "startDataDownload() calculated incorrect envelope length, aborting.");
acknowledgeSmsWithError(CommandsInterface.GSM_SMS_FAIL_CAUSE_UNSPECIFIED_ERROR);
return;
}
//Synthetic comment -- @@ -164,17 +164,17 @@

boolean success;
if ((sw1 == 0x90 && sw2 == 0x00) || sw1 == 0x91) {
            Log.d(TAG, "USIM data download succeeded: " + response.toString());
success = true;
} else if (sw1 == 0x93 && sw2 == 0x00) {
            Log.e(TAG, "USIM data download failed: Toolkit busy");
acknowledgeSmsWithError(CommandsInterface.GSM_SMS_FAIL_CAUSE_USIM_APP_TOOLKIT_BUSY);
return;
} else if (sw1 == 0x62 || sw1 == 0x63) {
            Log.e(TAG, "USIM data download failed: " + response.toString());
success = false;
} else {
            Log.e(TAG, "Unexpected SW1/SW2 response from UICC: " + response.toString());
success = false;
}

//Synthetic comment -- @@ -250,7 +250,7 @@
AsyncResult ar = (AsyncResult) msg.obj;

if (ar.exception != null) {
                    Log.e(TAG, "UICC Send Envelope failure, exception: " + ar.exception);
acknowledgeSmsWithError(
CommandsInterface.GSM_SMS_FAIL_CAUSE_USIM_DATA_DOWNLOAD_ERROR);
return;
//Synthetic comment -- @@ -261,7 +261,7 @@
break;

default:
                Log.e(TAG, "Ignoring unexpected message, what=" + msg.what);
}
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java b/src/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java
//Synthetic comment -- index 8f5a420..c59aa34 100755

//Synthetic comment -- @@ -19,7 +19,7 @@
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.AdnRecord;
import com.android.internal.telephony.AdnRecordCache;
//Synthetic comment -- @@ -143,7 +143,7 @@
try {
mLock.wait();
} catch (InterruptedException e) {
            Log.e(LOG_TAG, "Interrupted Exception in readAdnFileAndWait");
}
}

//Synthetic comment -- @@ -161,7 +161,7 @@
if (mEmailPresentInIap) {
readIapFileAndWait(fileIds.get(USIM_EFIAP_TAG));
if (mIapFileRecord == null) {
                    Log.e(LOG_TAG, "Error: IAP file is empty");
return;
}
}
//Synthetic comment -- @@ -171,11 +171,11 @@
try {
mLock.wait();
} catch (InterruptedException e) {
                Log.e(LOG_TAG, "Interrupted Exception in readEmailFileAndWait");
}

if (mEmailFileRecord == null) {
                Log.e(LOG_TAG, "Error: Email file is empty");
return;
}
updatePhoneAdnRecord();
//Synthetic comment -- @@ -188,7 +188,7 @@
try {
mLock.wait();
} catch (InterruptedException e) {
            Log.e(LOG_TAG, "Interrupted Exception in readIapFileAndWait");
}
}

//Synthetic comment -- @@ -206,7 +206,7 @@
try {
record = mIapFileRecord.get(i);
} catch (IndexOutOfBoundsException e) {
                    Log.e(LOG_TAG, "Error: Improper ICC card: No IAP record for ADN, continuing");
break;
}
int recNum = record[mEmailTagNumberInIap];
//Synthetic comment -- @@ -262,7 +262,7 @@
try {
emailRec = mEmailFileRecord.get(i);
} catch (IndexOutOfBoundsException e) {
                Log.e(LOG_TAG, "Error: Improper ICC card: No email record for ADN, continuing");
break;
}
int adnRecNum = emailRec[emailRec.length - 1];
//Synthetic comment -- @@ -318,7 +318,7 @@
try {
mLock.wait();
} catch (InterruptedException e) {
            Log.e(LOG_TAG, "Interrupted Exception in readAdnFileAndWait");
}
}

//Synthetic comment -- @@ -448,6 +448,6 @@
}

private void log(String msg) {
        if(DBG) Log.d(LOG_TAG, msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/VoiceMailConstants.java b/src/java/com/android/internal/telephony/gsm/VoiceMailConstants.java
//Synthetic comment -- index d2665cb..4b29480 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import android.os.Environment;
import android.util.Xml;
import android.util.Log;

import java.util.HashMap;
import java.io.FileReader;
//Synthetic comment -- @@ -79,7 +79,7 @@
try {
vmReader = new FileReader(vmFile);
} catch (FileNotFoundException e) {
            Log.w(LOG_TAG, "Can't open " +
Environment.getRootDirectory() + "/" + PARTNER_VOICEMAIL_PATH);
return;
}
//Synthetic comment -- @@ -107,9 +107,9 @@
CarrierVmMap.put(numeric, data);
}
} catch (XmlPullParserException e) {
            Log.w(LOG_TAG, "Exception in Voicemail parser " + e);
} catch (IOException e) {
            Log.w(LOG_TAG, "Exception in Voicemail parser " + e);
} finally {
try {
if (vmReader != null) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ims/IsimFileHandler.java b/src/java/com/android/internal/telephony/ims/IsimFileHandler.java
//Synthetic comment -- index 2e00c19..41ba844 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony.ims;

import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccConstants;
//Synthetic comment -- @@ -48,11 +48,11 @@

@Override
protected void logd(String msg) {
        Log.d(LOG_TAG, msg);
}

@Override
protected void loge(String msg) {
        Log.e(LOG_TAG, msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ims/IsimUiccRecords.java b/src/java/com/android/internal/telephony/ims/IsimUiccRecords.java
//Synthetic comment -- index 2a658bf..45b198d 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.AdnRecord;
import com.android.internal.telephony.AdnRecordCache;
//Synthetic comment -- @@ -80,7 +80,7 @@
// ***** Overridden from Handler
public void handleMessage(Message msg) {
if (mDestroyed.get()) {
            Log.e(LOG_TAG, "Received message " + msg +
"[" + msg.what + "] while being destroyed. Ignoring.");
return;
}
//Synthetic comment -- @@ -97,7 +97,7 @@
}
} catch (RuntimeException exc) {
// I don't want these exceptions to be fatal
            Log.w(LOG_TAG, "Exception parsing SIM record", exc);
}
}

//Synthetic comment -- @@ -179,7 +179,7 @@
}
} while (tlv.nextObject());

        Log.e(LOG_TAG, "[ISIM] can't find TLV tag in ISIM record, returning null");
return null;
}

//Synthetic comment -- @@ -262,11 +262,11 @@

@Override
protected void log(String s) {
        if (DBG) Log.d(LOG_TAG, "[ISIM] " + s);
}

@Override
protected void loge(String s) {
        if (DBG) Log.e(LOG_TAG, "[ISIM] " + s);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/sip/SipConnectionBase.java b/src/java/com/android/internal/telephony/sip/SipConnectionBase.java
//Synthetic comment -- index eaba2c4..eca4a50 100644

//Synthetic comment -- @@ -24,7 +24,7 @@

import android.net.sip.SipAudioCall;
import android.os.SystemClock;
import android.util.Log;
import android.telephony.PhoneNumberUtils;

abstract class SipConnectionBase extends Connection {
//Synthetic comment -- @@ -165,7 +165,7 @@
}

private void log(String msg) {
        Log.d(LOG_TAG, "[SipConn] " + msg);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/sip/SipPhone.java b/src/java/com/android/internal/telephony/sip/SipPhone.java
//Synthetic comment -- index 346b126..24651df 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
//Synthetic comment -- @@ -64,7 +64,7 @@
SipPhone (Context context, PhoneNotifier notifier, SipProfile profile) {
super(context, notifier);

        if (DEBUG) Log.d(LOG_TAG, "new SipPhone: " + profile.getUriString());
ringingCall = new SipCall();
foregroundCall = new SipCall();
backgroundCall = new SipCall();
//Synthetic comment -- @@ -106,7 +106,7 @@

try {
SipAudioCall sipAudioCall = (SipAudioCall) incomingCall;
                if (DEBUG) Log.d(LOG_TAG, "+++ taking call from: "
+ sipAudioCall.getPeerProfile().getUriString());
String localUri = sipAudioCall.getLocalProfile().getUriString();
if (localUri.equals(mProfile.getUriString())) {
//Synthetic comment -- @@ -115,7 +115,7 @@
if (sipAudioCall.getState()
!= SipSession.State.INCOMING_CALL) {
// Peer cancelled the call!
                        if (DEBUG) Log.d(LOG_TAG, "    call cancelled !!");
ringingCall.reset();
}
return true;
//Synthetic comment -- @@ -134,7 +134,7 @@
synchronized (SipPhone.class) {
if ((ringingCall.getState() == Call.State.INCOMING) ||
(ringingCall.getState() == Call.State.WAITING)) {
                if (DEBUG) Log.d(LOG_TAG, "acceptCall");
// Always unmute when answering a new call
ringingCall.setMute(false);
ringingCall.acceptCall();
//Synthetic comment -- @@ -147,7 +147,7 @@
public void rejectCall() throws CallStateException {
synchronized (SipPhone.class) {
if (ringingCall.getState().isRinging()) {
                if (DEBUG) Log.d(LOG_TAG, "rejectCall");
ringingCall.rejectCall();
} else {
throw new CallStateException("phone not ringing");
//Synthetic comment -- @@ -181,13 +181,13 @@
Connection c = foregroundCall.dial(dialString);
return c;
} catch (SipException e) {
            Log.e(LOG_TAG, "dial()", e);
throw new CallStateException("dial error: " + e);
}
}

public void switchHoldingAndActive() throws CallStateException {
        if (DEBUG) Log.d(LOG_TAG, " ~~~~~~  switch fg and bg");
synchronized (SipPhone.class) {
foregroundCall.switchWith(backgroundCall);
if (backgroundCall.getState().isAlive()) backgroundCall.hold();
//Synthetic comment -- @@ -242,7 +242,7 @@

public void sendDtmf(char c) {
if (!PhoneNumberUtils.is12Key(c)) {
            Log.e(LOG_TAG,
"sendDtmf called with invalid character '" + c + "'");
} else if (foregroundCall.getState().isAlive()) {
synchronized (SipPhone.class) {
//Synthetic comment -- @@ -253,7 +253,7 @@

public void startDtmf(char c) {
if (!PhoneNumberUtils.is12Key(c)) {
            Log.e(LOG_TAG,
"startDtmf called with invalid character '" + c + "'");
} else {
sendDtmf(c);
//Synthetic comment -- @@ -265,7 +265,7 @@
}

public void sendBurstDtmf(String dtmfString) {
        Log.e(LOG_TAG, "[SipPhone] sendBurstDtmf() is a CDMA method");
}

public void getOutgoingCallerIdDisplay(Message onComplete) {
//Synthetic comment -- @@ -289,7 +289,7 @@

public void setCallWaiting(boolean enable, Message onComplete) {
// FIXME: what to reply?
        Log.e(LOG_TAG, "call waiting not supported");
}

@Override
//Synthetic comment -- @@ -410,7 +410,7 @@
public void hangup() throws CallStateException {
synchronized (SipPhone.class) {
if (state.isAlive()) {
                    if (DEBUG) Log.d(LOG_TAG, "hang up call: " + getState()
+ ": " + this + " on phone " + getPhone());
setState(State.DISCONNECTING);
CallStateException excp = null;
//Synthetic comment -- @@ -423,7 +423,7 @@
}
if (excp != null) throw excp;
} else {
                    if (DEBUG) Log.d(LOG_TAG, "hang up dead call: " + getState()
+ ": " + this + " on phone " + getPhone());
}
}
//Synthetic comment -- @@ -473,7 +473,7 @@
} else {
audioGroup.setMode(AudioGroup.MODE_NORMAL);
}
            if (DEBUG) Log.d(LOG_TAG, String.format(
"audioGroup mode change: %d --> %d", mode,
audioGroup.getMode()));
}
//Synthetic comment -- @@ -558,7 +558,7 @@
@Override
protected void setState(State newState) {
if (state != newState) {
                if (DEBUG) Log.v(LOG_TAG, "+***+ call state changed: " + state
+ " --> " + newState + ": " + this + ": on phone "
+ getPhone() + " " + connections.size());

//Synthetic comment -- @@ -585,10 +585,10 @@
// set state to DISCONNECTED only when all conns are disconnected
if (state != State.DISCONNECTED) {
boolean allConnectionsDisconnected = true;
                if (DEBUG) Log.d(LOG_TAG, "---check connections: "
+ connections.size());
for (Connection c : connections) {
                    if (DEBUG) Log.d(LOG_TAG, "   state=" + c.getState() + ": "
+ c);
if (c.getState() != State.DISCONNECTED) {
allConnectionsDisconnected = false;
//Synthetic comment -- @@ -627,7 +627,7 @@
String sessionState = (sipAudioCall == null)
? ""
: (sipAudioCall.getState() + ", ");
                    if (DEBUG) Log.d(LOG_TAG, "--- connection ended: "
+ mPeer.getUriString() + ": " + sessionState
+ "cause: " + getDisconnectCause() + ", on phone "
+ getPhone());
//Synthetic comment -- @@ -674,7 +674,7 @@
setState(newState);
}
mOwner.onConnectionStateChanged(SipConnection.this);
                    if (DEBUG) Log.v(LOG_TAG, "+***+ connection state changed: "
+ mPeer.getUriString() + ": " + mState
+ " on phone " + getPhone());
}
//Synthetic comment -- @@ -682,7 +682,7 @@

@Override
protected void onError(DisconnectCause cause) {
                if (DEBUG) Log.d(LOG_TAG, "SIP error: " + cause);
onCallEnded(cause);
}
};
//Synthetic comment -- @@ -810,7 +810,7 @@
@Override
public void hangup() throws CallStateException {
synchronized (SipPhone.class) {
                if (DEBUG) Log.d(LOG_TAG, "hangup conn: " + mPeer.getUriString()
+ ": " + mState + ": on phone "
+ getPhone().getPhoneName());
if (!mState.isAlive()) return;
//Synthetic comment -- @@ -842,7 +842,7 @@
"cannot put conn back to a call in non-idle state: "
+ call.getState());
}
                if (DEBUG) Log.d(LOG_TAG, "separate conn: "
+ mPeer.getUriString() + " from " + mOwner + " back to "
+ call);

//Synthetic comment -- @@ -877,7 +877,7 @@
case SipSession.State.OUTGOING_CALL_CANCELING:  return Call.State.DISCONNECTING;
case SipSession.State.IN_CALL:                  return Call.State.ACTIVE;
default:
                Log.w(LOG_TAG, "illegal connection state: " + sessionState);
return Call.State.DISCONNECTED;
}
}
//Synthetic comment -- @@ -930,7 +930,7 @@
case SipErrorCode.SOCKET_ERROR:
case SipErrorCode.CLIENT_ERROR:
default:
                    Log.w(LOG_TAG, "error: " + SipErrorCode.toString(errorCode)
+ ": " + errorMessage);
onError(Connection.DisconnectCause.ERROR_UNSPECIFIED);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/sip/SipPhoneBase.java b/src/java/com/android/internal/telephony/sip/SipPhoneBase.java
//Synthetic comment -- index 43b0de3..9c0c266 100755

//Synthetic comment -- @@ -28,7 +28,7 @@
import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.util.Log;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
//Synthetic comment -- @@ -211,17 +211,17 @@

public boolean canDial() {
int serviceState = getServiceState().getState();
        Log.v(LOG_TAG, "canDial(): serviceState = " + serviceState);
if (serviceState == ServiceState.STATE_POWER_OFF) return false;

String disableCall = SystemProperties.get(
TelephonyProperties.PROPERTY_DISABLE_CALL, "false");
        Log.v(LOG_TAG, "canDial(): disableCall = " + disableCall);
if (disableCall.equals("true")) return false;

        Log.v(LOG_TAG, "canDial(): ringingCall: " + getRingingCall().getState());
        Log.v(LOG_TAG, "canDial(): foregndCall: " + getForegroundCall().getState());
        Log.v(LOG_TAG, "canDial(): backgndCall: " + getBackgroundCall().getState());
return !getRingingCall().isRinging()
&& (!getForegroundCall().getState().isAlive()
|| !getBackgroundCall().getState().isAlive());
//Synthetic comment -- @@ -279,12 +279,12 @@
}

public String getEsn() {
        Log.e(LOG_TAG, "[SipPhone] getEsn() is a CDMA method");
return "0";
}

public String getMeid() {
        Log.e(LOG_TAG, "[SipPhone] getMeid() is a CDMA method");
return "0";
}

//Synthetic comment -- @@ -344,7 +344,7 @@
}

public void setCallWaiting(boolean enable, Message onComplete) {
        Log.e(LOG_TAG, "call waiting not supported");
}

public boolean getIccRecordsLoaded() {
//Synthetic comment -- @@ -431,15 +431,15 @@
}

public void activateCellBroadcastSms(int activate, Message response) {
        Log.e(LOG_TAG, "Error! This functionality is not implemented for SIP.");
}

public void getCellBroadcastSmsConfig(Message response) {
        Log.e(LOG_TAG, "Error! This functionality is not implemented for SIP.");
}

public void setCellBroadcastSmsConfig(int[] configValuesArray, Message response){
        Log.e(LOG_TAG, "Error! This functionality is not implemented for SIP.");
}

//@Override
//Synthetic comment -- @@ -467,7 +467,7 @@
}

if (state != oldState) {
            Log.d(LOG_TAG, " ^^^ new phone state: " + state);
notifyPhoneStateChanged();
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/sip/SipPhoneFactory.java b/src/java/com/android/internal/telephony/sip/SipPhoneFactory.java
//Synthetic comment -- index 611e3ea..3383bed 100644

//Synthetic comment -- @@ -20,7 +20,7 @@

import android.content.Context;
import android.net.sip.SipProfile;
import android.util.Log;

import java.text.ParseException;

//Synthetic comment -- @@ -42,7 +42,7 @@
SipProfile profile = new SipProfile.Builder(sipUri).build();
return new SipPhone(context, phoneNotifier, profile);
} catch (ParseException e) {
            Log.w("SipPhoneFactory", "makePhone", e);
return null;
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/test/ModelInterpreter.java b/src/java/com/android/internal/telephony/test/ModelInterpreter.java
//Synthetic comment -- index b116c35..20aca89 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
//Synthetic comment -- @@ -204,7 +204,7 @@
try {
s = ss.accept();
} catch (java.io.IOException ex) {
                    Log.w(LOG_TAG,
"IOException on socket.accept(); stopping", ex);
return;
}
//Synthetic comment -- @@ -213,12 +213,12 @@
in = s.getInputStream();
out = s.getOutputStream();
} catch (java.io.IOException ex) {
                    Log.w(LOG_TAG,
"IOException on accepted socket(); re-listening", ex);
continue;
}

                Log.i(LOG_TAG, "New connection accepted");
}


//Synthetic comment -- @@ -260,7 +260,7 @@
}
}

            Log.i(LOG_TAG, "Disconnected");

if (ss == null) {
// no reconnect in this case








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/test/SimulatedCommands.java b/src/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index 4f61509..1672044 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.BaseCommands;
import com.android.internal.telephony.CommandException;
//Synthetic comment -- @@ -109,7 +109,7 @@

public void supplyIccPin(String pin, Message result)  {
if (mSimLockedState != SimLockState.REQUIRE_PIN) {
            Log.i(LOG_TAG, "[SimCmd] supplyIccPin: wrong state, state=" +
mSimLockedState);
CommandException ex = new CommandException(
CommandException.Error.PASSWORD_INCORRECT);
//Synthetic comment -- @@ -119,7 +119,7 @@
}

if (pin != null && pin.equals(mPinCode)) {
            Log.i(LOG_TAG, "[SimCmd] supplyIccPin: success!");
mPinUnlockAttempts = 0;
mSimLockedState = SimLockState.NONE;
mIccStatusChangedRegistrants.notifyRegistrants();
//Synthetic comment -- @@ -135,10 +135,10 @@
if (result != null) {
mPinUnlockAttempts ++;

            Log.i(LOG_TAG, "[SimCmd] supplyIccPin: failed! attempt=" +
mPinUnlockAttempts);
if (mPinUnlockAttempts >= 3) {
                Log.i(LOG_TAG, "[SimCmd] supplyIccPin: set state to REQUIRE_PUK");
mSimLockedState = SimLockState.REQUIRE_PUK;
}

//Synthetic comment -- @@ -151,7 +151,7 @@

public void supplyIccPuk(String puk, String newPin, Message result)  {
if (mSimLockedState != SimLockState.REQUIRE_PUK) {
            Log.i(LOG_TAG, "[SimCmd] supplyIccPuk: wrong state, state=" +
mSimLockedState);
CommandException ex = new CommandException(
CommandException.Error.PASSWORD_INCORRECT);
//Synthetic comment -- @@ -161,7 +161,7 @@
}

if (puk != null && puk.equals(SIM_PUK_CODE)) {
            Log.i(LOG_TAG, "[SimCmd] supplyIccPuk: success!");
mSimLockedState = SimLockState.NONE;
mPukUnlockAttempts = 0;
mIccStatusChangedRegistrants.notifyRegistrants();
//Synthetic comment -- @@ -177,10 +177,10 @@
if (result != null) {
mPukUnlockAttempts ++;

            Log.i(LOG_TAG, "[SimCmd] supplyIccPuk: failed! attempt=" +
mPukUnlockAttempts);
if (mPukUnlockAttempts >= 10) {
                Log.i(LOG_TAG, "[SimCmd] supplyIccPuk: set state to SIM_PERM_LOCKED");
mSimLockedState = SimLockState.SIM_PERM_LOCKED;
}

//Synthetic comment -- @@ -193,7 +193,7 @@

public void supplyIccPin2(String pin2, Message result)  {
if (mSimFdnEnabledState != SimFdnState.REQUIRE_PIN2) {
            Log.i(LOG_TAG, "[SimCmd] supplyIccPin2: wrong state, state=" +
mSimFdnEnabledState);
CommandException ex = new CommandException(
CommandException.Error.PASSWORD_INCORRECT);
//Synthetic comment -- @@ -203,7 +203,7 @@
}

if (pin2 != null && pin2.equals(mPin2Code)) {
            Log.i(LOG_TAG, "[SimCmd] supplyIccPin2: success!");
mPin2UnlockAttempts = 0;
mSimFdnEnabledState = SimFdnState.NONE;

//Synthetic comment -- @@ -218,10 +218,10 @@
if (result != null) {
mPin2UnlockAttempts ++;

            Log.i(LOG_TAG, "[SimCmd] supplyIccPin2: failed! attempt=" +
mPin2UnlockAttempts);
if (mPin2UnlockAttempts >= 3) {
                Log.i(LOG_TAG, "[SimCmd] supplyIccPin2: set state to REQUIRE_PUK2");
mSimFdnEnabledState = SimFdnState.REQUIRE_PUK2;
}

//Synthetic comment -- @@ -234,7 +234,7 @@

public void supplyIccPuk2(String puk2, String newPin2, Message result)  {
if (mSimFdnEnabledState != SimFdnState.REQUIRE_PUK2) {
            Log.i(LOG_TAG, "[SimCmd] supplyIccPuk2: wrong state, state=" +
mSimLockedState);
CommandException ex = new CommandException(
CommandException.Error.PASSWORD_INCORRECT);
//Synthetic comment -- @@ -244,7 +244,7 @@
}

if (puk2 != null && puk2.equals(SIM_PUK2_CODE)) {
            Log.i(LOG_TAG, "[SimCmd] supplyIccPuk2: success!");
mSimFdnEnabledState = SimFdnState.NONE;
mPuk2UnlockAttempts = 0;

//Synthetic comment -- @@ -259,10 +259,10 @@
if (result != null) {
mPuk2UnlockAttempts ++;

            Log.i(LOG_TAG, "[SimCmd] supplyIccPuk2: failed! attempt=" +
mPuk2UnlockAttempts);
if (mPuk2UnlockAttempts >= 10) {
                Log.i(LOG_TAG, "[SimCmd] supplyIccPuk2: set state to SIM_PERM_LOCKED");
mSimFdnEnabledState = SimFdnState.SIM_PERM_LOCKED;
}

//Synthetic comment -- @@ -285,7 +285,7 @@
}

if (result != null) {
            Log.i(LOG_TAG, "[SimCmd] changeIccPin: pin failed!");

CommandException ex = new CommandException(
CommandException.Error.PASSWORD_INCORRECT);
//Synthetic comment -- @@ -306,7 +306,7 @@
}

if (result != null) {
            Log.i(LOG_TAG, "[SimCmd] changeIccPin2: pin2 failed!");

CommandException ex = new CommandException(
CommandException.Error.PASSWORD_INCORRECT);
//Synthetic comment -- @@ -325,7 +325,7 @@
resultSuccess(result, null);

if (enable && mSsnNotifyOn) {
            Log.w(LOG_TAG, "Supp Service Notifications already enabled!");
}

mSsnNotifyOn = enable;
//Synthetic comment -- @@ -344,7 +344,7 @@
if (result != null) {
int[] r = new int[1];
r[0] = (mSimLockEnabled ? 1 : 0);
                Log.i(LOG_TAG, "[SimCmd] queryFacilityLock: SIM is "
+ (r[0] == 0 ? "unlocked" : "locked"));
AsyncResult.forMessage(result, r, null);
result.sendToTarget();
//Synthetic comment -- @@ -354,7 +354,7 @@
if (result != null) {
int[] r = new int[1];
r[0] = (mSimFdnEnabled ? 1 : 0);
                Log.i(LOG_TAG, "[SimCmd] queryFacilityLock: FDN is "
+ (r[0] == 0 ? "disabled" : "enabled"));
AsyncResult.forMessage(result, r, null);
result.sendToTarget();
//Synthetic comment -- @@ -378,7 +378,7 @@
if (facility != null &&
facility.equals(CommandsInterface.CB_FACILITY_BA_SIM)) {
if (pin != null && pin.equals(mPinCode)) {
                Log.i(LOG_TAG, "[SimCmd] setFacilityLock: pin is valid");
mSimLockEnabled = lockEnabled;

if (result != null) {
//Synthetic comment -- @@ -390,7 +390,7 @@
}

if (result != null) {
                Log.i(LOG_TAG, "[SimCmd] setFacilityLock: pin failed!");

CommandException ex = new CommandException(
CommandException.Error.GENERIC_FAILURE);
//Synthetic comment -- @@ -402,7 +402,7 @@
}  else if (facility != null &&
facility.equals(CommandsInterface.CB_FACILITY_BA_FD)) {
if (pin != null && pin.equals(mPin2Code)) {
                Log.i(LOG_TAG, "[SimCmd] setFacilityLock: pin2 is valid");
mSimFdnEnabled = lockEnabled;

if (result != null) {
//Synthetic comment -- @@ -414,7 +414,7 @@
}

if (result != null) {
                Log.i(LOG_TAG, "[SimCmd] setFacilityLock: pin2 failed!");

CommandException ex = new CommandException(
CommandException.Error.GENERIC_FAILURE);
//Synthetic comment -- @@ -442,10 +442,10 @@
*/
public void getCurrentCalls (Message result) {
if ((mState == RadioState.RADIO_ON) && !isSimLocked()) {
            //Log.i("GSM", "[SimCmds] getCurrentCalls");
resultSuccess(result, simulatedCallState.getDriverCalls());
} else {
            //Log.i("GSM", "[SimCmds] getCurrentCalls: RADIO_OFF or SIM not ready!");
resultFail(result,
new CommandException(
CommandException.Error.RADIO_NOT_AVAILABLE));
//Synthetic comment -- @@ -557,10 +557,10 @@
success = simulatedCallState.onChld('1', (char)('0'+gsmIndex));

if (!success){
            Log.i("GSM", "[SimCmd] hangupConnection: resultFail");
resultFail(result, new RuntimeException("Hangup Error"));
} else {
            Log.i("GSM", "[SimCmd] hangupConnection: resultSuccess");
resultSuccess(result, null);
}
}
//Synthetic comment -- @@ -942,22 +942,22 @@
public void sendSMS (String smscPDU, String pdu, Message result) {unimplemented(result);}

public void deleteSmsOnSim(int index, Message response) {
        Log.d(LOG_TAG, "Delete message at index " + index);
unimplemented(response);
}

public void deleteSmsOnRuim(int index, Message response) {
        Log.d(LOG_TAG, "Delete RUIM message at index " + index);
unimplemented(response);
}

public void writeSmsToSim(int status, String smsc, String pdu, Message response) {
        Log.d(LOG_TAG, "Write SMS to SIM with status " + status);
unimplemented(response);
}

public void writeSmsToRuim(int status, String pdu, Message response) {
        Log.d(LOG_TAG, "Write SMS to RUIM with status " + status);
unimplemented(response);
}

//Synthetic comment -- @@ -1321,7 +1321,7 @@
}
pausedResponses.clear();
} else {
            Log.e("GSM", "SimulatedCommands.resumeResponses < 0");
}
}

//Synthetic comment -- @@ -1365,44 +1365,44 @@
// ***** Methods for CDMA support
public void
getDeviceIdentity(Message response) {
        Log.w(LOG_TAG, "CDMA not implemented in SimulatedCommands");
unimplemented(response);
}

public void
getCDMASubscription(Message response) {
        Log.w(LOG_TAG, "CDMA not implemented in SimulatedCommands");
unimplemented(response);
}

public void
setCdmaSubscriptionSource(int cdmaSubscriptionType, Message response) {
        Log.w(LOG_TAG, "CDMA not implemented in SimulatedCommands");
unimplemented(response);
}

public void queryCdmaRoamingPreference(Message response) {
        Log.w(LOG_TAG, "CDMA not implemented in SimulatedCommands");
unimplemented(response);
}

public void setCdmaRoamingPreference(int cdmaRoamingType, Message response) {
        Log.w(LOG_TAG, "CDMA not implemented in SimulatedCommands");
unimplemented(response);
}

public void
setPhoneType(int phoneType) {
        Log.w(LOG_TAG, "CDMA not implemented in SimulatedCommands");
}

public void getPreferredVoicePrivacy(Message result) {
        Log.w(LOG_TAG, "CDMA not implemented in SimulatedCommands");
unimplemented(result);
}

public void setPreferredVoicePrivacy(boolean enable, Message result) {
        Log.w(LOG_TAG, "CDMA not implemented in SimulatedCommands");
unimplemented(result);
}

//Synthetic comment -- @@ -1417,7 +1417,7 @@
* @param response is callback message
*/
public void setTTYMode(int ttyMode, Message response) {
        Log.w(LOG_TAG, "Not implemented in SimulatedCommands");
unimplemented(response);
}

//Synthetic comment -- @@ -1432,7 +1432,7 @@
* @param response is callback message
*/
public void queryTTYMode(Message response) {
        Log.w(LOG_TAG, "CDMA not implemented in SimulatedCommands");
unimplemented(response);
}

//Synthetic comment -- @@ -1440,7 +1440,7 @@
* {@inheritDoc}
*/
public void sendCDMAFeatureCode(String FeatureCode, Message response) {
        Log.w(LOG_TAG, "CDMA not implemented in SimulatedCommands");
unimplemented(response);
}

//Synthetic comment -- @@ -1448,7 +1448,7 @@
* {@inheritDoc}
*/
public void sendCdmaSms(byte[] pdu, Message response){
       Log.w(LOG_TAG, "CDMA not implemented in SimulatedCommands");
}

public void setCdmaBroadcastActivation(boolean activate, Message response) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/test/SimulatedGsmCallState.java b/src/java/com/android/internal/telephony/test/SimulatedGsmCallState.java
//Synthetic comment -- index c6c301d..6b0a346 100644

//Synthetic comment -- @@ -25,7 +25,7 @@
import java.util.List;
import java.util.ArrayList;

import android.util.Log;

class CallInfo {
enum State {
//Synthetic comment -- @@ -189,7 +189,7 @@
&& (call.state == CallInfo.State.INCOMING
|| call.state == CallInfo.State.WAITING)
) {
                    Log.w("ModelInterpreter",
"triggerRing failed; phone already ringing");
return false;
} else if (call != null) {
//Synthetic comment -- @@ -198,7 +198,7 @@
}

if (empty < 0 ) {
                Log.w("ModelInterpreter", "triggerRing failed; all full");
return false;
}

//Synthetic comment -- @@ -658,25 +658,25 @@
CallInfo call;
int freeSlot = -1;

        Log.d("GSM", "SC> dial '" + address + "'");

if (nextDialFailImmediately) {
nextDialFailImmediately = false;

            Log.d("GSM", "SC< dial fail (per request)");
return false;
}

String phNum = PhoneNumberUtils.extractNetworkPortion(address);

if (phNum.length() == 0) {
            Log.d("GSM", "SC< dial fail (invalid ph num)");
return false;
}

// Ignore setting up GPRS
if (phNum.startsWith("*99") && phNum.endsWith("#")) {
            Log.d("GSM", "SC< dial ignored (gprs)");
return true;
}

//Synthetic comment -- @@ -684,11 +684,11 @@
// a new call
try {
if (countActiveLines() > 1) {
                Log.d("GSM", "SC< dial fail (invalid call state)");
return false;
}
} catch (InvalidStateEx ex) {
            Log.d("GSM", "SC< dial fail (invalid call state)");
return false;
}

//Synthetic comment -- @@ -700,7 +700,7 @@
if (calls[i] != null && !calls[i].isActiveOrHeld()) {
// Can't make outgoing calls when there is a ringing or
// connecting outgoing call
                Log.d("GSM", "SC< dial fail (invalid call state)");
return false;
} else if (calls[i] != null && calls[i].state == CallInfo.State.ACTIVE) {
// All active calls behome held
//Synthetic comment -- @@ -709,7 +709,7 @@
}

if (freeSlot < 0) {
            Log.d("GSM", "SC< dial fail (invalid call state)");
return false;
}

//Synthetic comment -- @@ -721,7 +721,7 @@
CONNECTING_PAUSE_MSEC);
}

        Log.d("GSM", "SC< dial (slot = " + freeSlot + ")");

return true;
}
//Synthetic comment -- @@ -741,7 +741,7 @@
}
}

        Log.d("GSM", "SC< getDriverCalls " + ret);

return ret;
}
//Synthetic comment -- @@ -779,12 +779,12 @@
} else if (call.isMpty && mptyIsHeld
&& call.state == CallInfo.State.ACTIVE
) {
                    Log.e("ModelInterpreter", "Invalid state");
throw new InvalidStateEx();
} else if (!call.isMpty && hasMpty && mptyIsHeld
&& call.state == CallInfo.State.HOLDING
) {
                    Log.e("ModelInterpreter", "Invalid state");
throw new InvalidStateEx();
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccController.java b/src/java/com/android/internal/telephony/uicc/UiccController.java
//Synthetic comment -- index 8d0868e..d0ebb3d 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCardStatus;
//Synthetic comment -- @@ -185,7 +185,7 @@
onGetIccCardStatusDone(ar);
break;
default:
                    Log.e(LOG_TAG, " Unknown Event " + msg.what);
}
}
}
//Synthetic comment -- @@ -201,7 +201,7 @@

private synchronized void onGetIccCardStatusDone(AsyncResult ar) {
if (ar.exception != null) {
            Log.e(LOG_TAG,"Error getting ICC status. "
+ "RIL_REQUEST_GET_ICC_STATUS should "
+ "never return an error", ar.exception);
return;
//Synthetic comment -- @@ -222,6 +222,6 @@
}

private void log(String string) {
        Log.d(LOG_TAG, string);
}
}








//Synthetic comment -- diff --git a/tests/telephonymockriltests/src/com/android/telephonymockriltests/TelephonyMockTestRunner.java b/tests/telephonymockriltests/src/com/android/telephonymockriltests/TelephonyMockTestRunner.java
//Synthetic comment -- index 78ee738..b6bcd27 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;
import com.android.internal.telephony.mockril.MockRilController;
import android.util.Log;

import com.android.telephonymockriltests.functional.SimpleTestUsingMockRil;









//Synthetic comment -- diff --git a/tests/telephonymockriltests/src/com/android/telephonymockriltests/functional/SimpleTestUsingMockRil.java b/tests/telephonymockriltests/src/com/android/telephonymockriltests/functional/SimpleTestUsingMockRil.java
//Synthetic comment -- index 3ea1cf2..df79b8b 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.internal.telephony.mockril.MockRilController;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.android.telephonymockriltests.TelephonyMockTestRunner;

//Synthetic comment -- @@ -43,7 +43,7 @@
*/
public void testGetRadioState() {
int state = mMockRilCtrl.getRadioState();
        Log.v(TAG, "testGetRadioState: " + state);
assertTrue(state >= 0 && state <= 9);
}

//Synthetic comment -- @@ -53,7 +53,7 @@
*/
public void testSetRadioState() {
for (int state = 0; state <= 9; state++) {
            Log.v(TAG, "set radio state to be " + state);
assertTrue("set radio state: " + state + " failed.",
mMockRilCtrl.setRadioState(state));
}








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/frameworks/telephonytests/TelephonyMockRilTestRunner.java b/tests/telephonytests/src/com/android/frameworks/telephonytests/TelephonyMockRilTestRunner.java
//Synthetic comment -- index 9192f57..25d9f4d 100644

//Synthetic comment -- @@ -20,7 +20,7 @@

import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;
import android.util.Log;

import java.io.IOException;

//Synthetic comment -- @@ -88,6 +88,6 @@
}

private void log(String s) {
        Log.e("TelephonyMockRilTestRunner", s);
}
}








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/CallerInfoTest.java b/tests/telephonytests/src/com/android/internal/telephony/CallerInfoTest.java
//Synthetic comment -- index 1e5dafb..5155ccfc 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
import android.content.res.Resources;
import com.android.internal.telephony.CallerInfo;
import com.android.internal.telephony.CallerInfoAsyncQuery;
import android.util.Log;
import android.os.Looper;
import android.test.ActivityInstrumentationTestCase;
import android.util.StringBuilderPrinter;








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/MccTableTest.java b/tests/telephonytests/src/com/android/internal/telephony/MccTableTest.java
//Synthetic comment -- index 868c76d..e6dee8b 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import android.util.Log;

public class MccTableTest extends AndroidTestCase {
private final static String LOG_TAG = "GSM";








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/SmsMessageBodyTest.java b/tests/telephonytests/src/com/android/internal/telephony/SmsMessageBodyTest.java
//Synthetic comment -- index b848657..2069696 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.android.internal.telephony.SmsConstants;

//Synthetic comment -- @@ -480,9 +480,9 @@
}

void printStats() {
            Log.d(TAG, "Unicode selection count: " + mUnicodeCounter);
for (int i = 0; i < 12; i++) {
                Log.d(TAG, "Language pair index " + i + " count: " + mStatsCounters[i]);
}
}
}
//Synthetic comment -- @@ -517,7 +517,7 @@
ch.addChar(charClass);

//                if (i % 20 == 0) {
//                    Log.d(TAG, "test string: " + sb);
//                }

// Test string against all combinations of enabled languages
//Synthetic comment -- @@ -537,13 +537,13 @@
// after 10 iterations with a Unicode-only string, skip to next test string
// so we can spend more time testing strings that do encode into 7 bits.
if (unicodeOnly && ++unicodeOnlyCount == 10) {
//                    Log.d(TAG, "Unicode only: skipping to next test string");
break;
}
}
}
ch.printStats();
        Log.d(TAG, "Completed in " + (System.currentTimeMillis() - startTime) + " ms");
GsmAlphabet.setEnabledLockingShiftTables(origLockingShiftTables);
GsmAlphabet.setEnabledSingleShiftTables(origSingleShiftTables);
}








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/cdma/CdmaSmsCbTest.java b/tests/telephonytests/src/com/android/internal/telephony/cdma/CdmaSmsCbTest.java
//Synthetic comment -- index a95f60c..f74400c 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import android.telephony.SmsCbMessage;
import android.telephony.cdma.CdmaSmsCbProgramData;
import android.test.AndroidTestCase;
import android.util.Log;

import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.IccUtils;
//Synthetic comment -- @@ -525,7 +525,7 @@
for (int i = 0; i < len; i++) {
data[i] = (byte) r.nextInt(256);
}
            // Log.d("CdmaSmsCbTest", "trying random bearer data run " + run + " length " + len);
try {
int category = 0x0ff0 + r.nextInt(32);  // half CMAS, half non-CMAS
Parcel p = createBroadcastParcel(category);
//Synthetic comment -- @@ -533,10 +533,10 @@
SmsCbMessage cbMessage = msg.parseBroadcastSms();
// with random input, cbMessage will almost always be null (log when it isn't)
if (cbMessage != null) {
                    Log.d("CdmaSmsCbTest", "success: " + cbMessage);
}
} catch (Exception e) {
                Log.d("CdmaSmsCbTest", "exception thrown", e);
fail("Exception in decoder at run " + run + " length " + len + ": " + e);
}
}
//Synthetic comment -- @@ -549,7 +549,7 @@
int category = 0x0ff0 + r.nextInt(32);  // half CMAS, half non-CMAS
Parcel p = createBroadcastParcel(category);
int len = r.nextInt(140);
            // Log.d("CdmaSmsCbTest", "trying random user data run " + run + " length " + len);

try {
BitwiseOutputStream bos = createBearerDataStream(r.nextInt(65536), r.nextInt(4),
//Synthetic comment -- @@ -565,7 +565,7 @@
SmsMessage msg = createMessageFromParcel(p, bos.toByteArray());
SmsCbMessage cbMessage = msg.parseBroadcastSms();
} catch (Exception e) {
                Log.d("CdmaSmsCbTest", "exception thrown", e);
fail("Exception in decoder at run " + run + " length " + len + ": " + e);
}
}








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/gsm/GSMTestHandler.java b/tests/telephonytests/src/com/android/internal/telephony/gsm/GSMTestHandler.java
//Synthetic comment -- index fb8a5d9..aa94e0e 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.gsm.GSMPhone;
import com.android.internal.telephony.test.SimulatedCommands;








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/gsm/GsmSmsCbTest.java b/tests/telephonytests/src/com/android/internal/telephony/gsm/GsmSmsCbTest.java
//Synthetic comment -- index 82c6944..8aa718a 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import android.telephony.SmsCbLocation;
import android.telephony.SmsCbMessage;
import android.test.AndroidTestCase;
import android.util.Log;

import com.android.internal.telephony.IccUtils;

//Synthetic comment -- @@ -709,7 +709,7 @@

public void testEtwsMessageNormal() {
SmsCbMessage msg = createFromPdu(etwsMessageNormal);
        Log.d(TAG, msg.toString());
assertEquals("GS mismatch", 0, msg.getGeographicalScope());
assertEquals("serial number mismatch", 0, msg.getSerialNumber());
assertEquals("message ID mismatch", 0x1100, msg.getServiceCategory());
//Synthetic comment -- @@ -719,7 +719,7 @@

public void testEtwsMessageCancel() {
SmsCbMessage msg = createFromPdu(etwsMessageCancel);
        Log.d(TAG, msg.toString());
assertEquals("GS mismatch", 0, msg.getGeographicalScope());
assertEquals("serial number mismatch", 0, msg.getSerialNumber());
assertEquals("message ID mismatch", 0x1100, msg.getServiceCategory());
//Synthetic comment -- @@ -729,7 +729,7 @@

public void testEtwsMessageTest() {
SmsCbMessage msg = createFromPdu(etwsMessageTest);
        Log.d(TAG, msg.toString());
assertEquals("GS mismatch", 0, msg.getGeographicalScope());
assertEquals("serial number mismatch", 0, msg.getSerialNumber());
assertEquals("message ID mismatch", 0x1103, msg.getServiceCategory());
//Synthetic comment -- @@ -750,7 +750,7 @@
// this should return a SmsCbMessage object or null for invalid data
SmsCbMessage msg = createFromPdu(data);
} catch (Exception e) {
                Log.d(TAG, "exception thrown", e);
fail("Exception in decoder at run " + run + " length " + len + ": " + e);
}
}








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/gsm/UsimDataDownloadCommands.java b/tests/telephonytests/src/com/android/internal/telephony/gsm/UsimDataDownloadCommands.java
//Synthetic comment -- index ea6836d..a858806 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import android.os.AsyncResult;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.android.internal.telephony.BaseCommands;
import com.android.internal.telephony.IccIoResult;
//Synthetic comment -- @@ -105,7 +105,7 @@
@Override
public synchronized void acknowledgeLastIncomingGsmSms(boolean success, int cause,
Message response) {
        Log.d(TAG, "acknowledgeLastIncomingGsmSms: success=" + success + ", cause=" + cause);
Assert.assertTrue("unexpected call to acknowledge SMS", mExpectingAcknowledgeGsmSms);
Assert.assertEquals(mExpectingAcknowledgeGsmSmsSuccess, success);
Assert.assertEquals(mExpectingAcknowledgeGsmSmsFailureCause, cause);
//Synthetic comment -- @@ -120,7 +120,7 @@
@Override
public synchronized void acknowledgeIncomingGsmSmsWithPdu(boolean success, String ackPdu,
Message response) {
        Log.d(TAG, "acknowledgeLastIncomingGsmSmsWithPdu: success=" + success
+ ", ackPDU= " + ackPdu);
Assert.assertTrue("unexpected call to acknowledge SMS", mExpectingAcknowledgeGsmSms);
Assert.assertEquals(mExpectingAcknowledgeGsmSmsSuccess, success);
//Synthetic comment -- @@ -140,7 +140,7 @@
for (int i = 0; i < contents.length(); i += 2) {
builder.append(contents.charAt(i)).append(contents.charAt(i+1)).append(' ');
}
        Log.d(TAG, "sendEnvelopeWithStatus: " + builder.toString());

Assert.assertTrue("unexpected call to send envelope", mExpectingSendEnvelope);
Assert.assertEquals(mExpectingSendEnvelopeContents, contents);








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/gsm/UsimDataDownloadTest.java b/tests/telephonytests/src/com/android/internal/telephony/gsm/UsimDataDownloadTest.java
//Synthetic comment -- index 6c8ba5e..7f0282a 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import android.os.HandlerThread;
import android.test.AndroidTestCase;
import android.util.Log;

import java.nio.charset.Charset;

//Synthetic comment -- @@ -67,7 +67,7 @@
mHandlerThread = new TestHandlerThread();
mHandlerThread.start();
mHandler = mHandlerThread.getHandler();
        Log.d(TAG, "mHandler is constructed");
}

@Override








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/mockril/MockRilTest.java b/tests/telephonytests/src/com/android/internal/telephony/mockril/MockRilTest.java
//Synthetic comment -- index 3149ee1..598d9fe 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony.mockril;

import android.util.Log;
import android.test.InstrumentationTestCase;

import java.io.IOException;
//Synthetic comment -- @@ -51,7 +51,7 @@
}

static void log(String s) {
        Log.v(TAG, s);
}

/**







