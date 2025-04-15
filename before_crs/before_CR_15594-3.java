/*Support for VirtualVoiceCall over SCO - Implementation of the 3rd party
API.

Change-Id:I320ab10f5ca6a0fcfd6e710e2514fea1004a722e*/
//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 9d205e3..ac5ea28 100644

//Synthetic comment -- @@ -154,6 +154,22 @@
private static final int BRSF_HF_ENHANCED_CALL_STATUS = 1 <<  5;
private static final int BRSF_HF_ENHANCED_CALL_CONTROL = 1 << 6;

public static String typeToString(int type) {
switch (type) {
case TYPE_UNKNOWN:
//Synthetic comment -- @@ -208,6 +224,7 @@
mBackgroundCall = mPhone.getBackgroundCall();
mBluetoothPhoneState = new BluetoothPhoneState();
mUserWantsAudio = true;
mPhonebook = new BluetoothAtPhonebook(mContext, this);
mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
cdmaSetSecondCallState(false);
//Synthetic comment -- @@ -275,6 +292,10 @@
mHeadset = null;
stopDebug();
resetAtState();
}

private void resetAtState() {
//Synthetic comment -- @@ -396,6 +417,9 @@
filter.addAction(TelephonyIntents.ACTION_SIGNAL_STRENGTH_CHANGED);
filter.addAction(BluetoothA2dp.ACTION_SINK_STATE_CHANGED);
mContext.registerReceiver(mStateReceiver, filter);
}

private void updateBtPhoneStateAfterRadioTechnologyChange() {
//Synthetic comment -- @@ -666,7 +690,15 @@
switch (mPhoneState) {
case IDLE:
mUserWantsAudio = true;  // out of call - reset state
                    audioOff();
break;
default:
callStarted();
//Synthetic comment -- @@ -680,6 +712,8 @@
break;
case DIALING:
callsetup = 2;
mAudioPossible = true;
// We also need to send a Call started indication
// for cases where the 2nd MO was initiated was
//Synthetic comment -- @@ -690,6 +724,7 @@
if (mPhone.getPhoneType() == Phone.PHONE_TYPE_GSM) {
callStarted();
}
break;
case ALERTING:
callsetup = 3;
//Synthetic comment -- @@ -705,6 +740,9 @@
case INCOMING:
case WAITING:
callsetup = 1;
break;
}

//Synthetic comment -- @@ -724,6 +762,7 @@
// This means that a call has transitioned from NOT ACTIVE to ACTIVE.
// Switch on audio.
audioOn();
}
mCall = call;
if (sendUpdate) {
//Synthetic comment -- @@ -898,7 +937,22 @@

private synchronized AtCommandResult toCindResult() {
AtCommandResult result = new AtCommandResult(AtCommandResult.OK);
            String status = "+CIND: " + mService + "," + mCall + "," + mCallsetup + "," +
mCallheld + "," + mSignal + "," + mRoam + "," + mBattchg;
result.addResponse(status);
return result;
//Synthetic comment -- @@ -947,8 +1001,16 @@
Log.i(TAG, "Routing audio for incoming SCO connection");
mConnectedSco = (ScoSocket)msg.obj;
mAudioManager.setBluetoothScoOn(true);
                            broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_CONNECTED,
                                    mHeadset.getRemoteDevice());
} else {
Log.i(TAG, "Rejecting incoming SCO connection");
((ScoSocket)msg.obj).close();
//Synthetic comment -- @@ -963,22 +1025,45 @@
if (VDBG) log("Routing audio for outgoing SCO conection");
mConnectedSco = (ScoSocket)msg.obj;
mAudioManager.setBluetoothScoOn(true);
                        broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_CONNECTED,
                                mHeadset.getRemoteDevice());
} else if (msg.arg1 == ScoSocket.STATE_CONNECTED) {
if (VDBG) log("Rejecting new connected outgoing SCO socket");
((ScoSocket)msg.obj).close();
mOutgoingSco.close();
}
mOutgoingSco = null;
break;
case SCO_CLOSED:
if (mConnectedSco == (ScoSocket)msg.obj) {
mConnectedSco.close();
mConnectedSco = null;
mAudioManager.setBluetoothScoOn(false);
                        broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_DISCONNECTED,
mHeadset.getRemoteDevice());
} else if (mOutgoingSco == (ScoSocket)msg.obj) {
mOutgoingSco.close();
mOutgoingSco = null;
//Synthetic comment -- @@ -1027,7 +1112,8 @@
if (VDBG) log("broadcastAudioStateIntent(" + state + ")");
Intent intent = new Intent(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
intent.putExtra(BluetoothHeadset.EXTRA_AUDIO_STATE, state);
        intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
}

//Synthetic comment -- @@ -1110,6 +1196,8 @@
/* package */ synchronized void userWantsAudioOn() {
mUserWantsAudio = true;
audioOn();
}
/** Used to indicate the user requested BT audio off.
*  This will prevent us from establishing BT audio again during this call
//Synthetic comment -- @@ -1118,6 +1206,16 @@
/* package */ synchronized void userWantsAudioOff() {
mUserWantsAudio = false;
audioOff();
}

/** Request to disconnect SCO (audio) connection to bluetooth
//Synthetic comment -- @@ -1149,7 +1247,21 @@
mConnectedSco.close();
mConnectedSco = null;
mAudioManager.setBluetoothScoOn(false);
            broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_DISCONNECTED, device);
}
if (mOutgoingSco != null) {
mOutgoingSco.close();
//Synthetic comment -- @@ -1187,6 +1299,9 @@
"outgoing calls found. Ignoring");
return new AtCommandResult(AtCommandResult.ERROR);
}
Intent intent = new Intent(Intent.ACTION_CALL_PRIVILEGED,
Uri.fromParts("tel", number, null));
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//Synthetic comment -- @@ -1530,6 +1645,8 @@
}
return redial();
} else {
// Remove trailing ';'
if (args.charAt(args.length() - 1) == ';') {
args = args.substring(0, args.length() - 1);
//Synthetic comment -- @@ -1552,12 +1669,18 @@
@Override
public AtCommandResult handleActionCommand() {
sendURC("OK");
                if (!mForegroundCall.isIdle()) {
                    PhoneUtils.hangupActiveCall(mPhone);
                } else if (!mRingingCall.isIdle()) {
                    PhoneUtils.hangupRingingCall(mPhone);
                } else if (!mBackgroundCall.isIdle()) {
                    PhoneUtils.hangupHoldingCall(mPhone);
}
return new AtCommandResult(AtCommandResult.UNSOLICITED);
}
//Synthetic comment -- @@ -1773,6 +1896,24 @@
@Override
public AtCommandResult handleActionCommand() {
int phoneType = mPhone.getPhoneType();
if (phoneType == Phone.PHONE_TYPE_CDMA) {
return cdmaGetClccResult();
} else if (phoneType == Phone.PHONE_TYPE_GSM) {
//Synthetic comment -- @@ -2049,6 +2190,8 @@
if (!BluetoothHeadset.isBluetoothVoiceDialingEnabled(mContext)) {
return new AtCommandResult(AtCommandResult.ERROR);
}
if (args.length >= 1 && args[0].equals(1)) {
synchronized (BluetoothHandsfree.this) {
if (!mWaitingForVoiceRecognition) {
//Synthetic comment -- @@ -2228,6 +2371,120 @@
}
}

/** Debug thread to read debug properties - runs when debug.bt.hfp is true
*  at the time a bluetooth handsfree device is connected. Debug properties
*  are polled and mock updates sent every 1 second */








//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHeadsetService.java b/src/com/android/phone/BluetoothHeadsetService.java
//Synthetic comment -- index 513caad..bff7aba 100644

//Synthetic comment -- @@ -677,6 +677,24 @@

return HeadsetBase.getAtInputCount();
}
};

@Override








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneUtils.java b/src/com/android/phone/PhoneUtils.java
//Synthetic comment -- index aab89ff..f07a50a 100755

//Synthetic comment -- @@ -528,7 +528,14 @@
}
} else {
PhoneApp app = PhoneApp.getInstance();

if (phoneType == Phone.PHONE_TYPE_CDMA) {
updateCdmaCallStateOnNewOutgoingCall(app);
}







