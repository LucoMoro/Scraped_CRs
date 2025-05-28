
//<Beginning of snippet n. 0>


private static final int BRSF_HF_ENHANCED_CALL_STATUS = 1 <<  5;
private static final int BRSF_HF_ENHANCED_CALL_CONTROL = 1 << 6;

public static String typeToString(int type) {
switch (type) {
case TYPE_UNKNOWN:
mBackgroundCall = mPhone.getBackgroundCall();
mBluetoothPhoneState = new BluetoothPhoneState();
mUserWantsAudio = true;
mPhonebook = new BluetoothAtPhonebook(mContext, this);
mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
cdmaSetSecondCallState(false);
mHeadset = null;
stopDebug();
resetAtState();
}

private void resetAtState() {
switch (mPhoneState) {
case IDLE:
mUserWantsAudio = true;  // out of call - reset state
                    audioOff();
break;
default:
callStarted();
break;
case DIALING:
callsetup = 2;
mAudioPossible = true;
// We also need to send a Call started indication
// for cases where the 2nd MO was initiated was
case INCOMING:
case WAITING:
callsetup = 1;
break;
}


private synchronized AtCommandResult toCindResult() {
AtCommandResult result = new AtCommandResult(AtCommandResult.OK);
            String status = "+CIND: " + mService + "," + mCall + "," + mCallsetup + "," +
mCallheld + "," + mSignal + "," + mRoam + "," + mBattchg;
result.addResponse(status);
return result;
Log.i(TAG, "Routing audio for incoming SCO connection");
mConnectedSco = (ScoSocket)msg.obj;
mAudioManager.setBluetoothScoOn(true);
                            broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_CONNECTED,
                                    mHeadset.getRemoteDevice());
} else {
Log.i(TAG, "Rejecting incoming SCO connection");
((ScoSocket)msg.obj).close();
if (VDBG) log("Routing audio for outgoing SCO conection");
mConnectedSco = (ScoSocket)msg.obj;
mAudioManager.setBluetoothScoOn(true);
                        broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_CONNECTED,
                                mHeadset.getRemoteDevice());
} else if (msg.arg1 == ScoSocket.STATE_CONNECTED) {
if (VDBG) log("Rejecting new connected outgoing SCO socket");
((ScoSocket)msg.obj).close();
mConnectedSco.close();
mConnectedSco = null;
mAudioManager.setBluetoothScoOn(false);
                        broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_DISCONNECTED,
mHeadset.getRemoteDevice());
} else if (mOutgoingSco == (ScoSocket)msg.obj) {
mOutgoingSco.close();
mOutgoingSco = null;
if (VDBG) log("broadcastAudioStateIntent(" + state + ")");
Intent intent = new Intent(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
intent.putExtra(BluetoothHeadset.EXTRA_AUDIO_STATE, state);
        intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
}

mConnectedSco.close();
mConnectedSco = null;
mAudioManager.setBluetoothScoOn(false);
            broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_DISCONNECTED, device);
}
if (mOutgoingSco != null) {
mOutgoingSco.close();
"outgoing calls found. Ignoring");
return new AtCommandResult(AtCommandResult.ERROR);
}
Intent intent = new Intent(Intent.ACTION_CALL_PRIVILEGED,
Uri.fromParts("tel", number, null));
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
}
return redial();
} else {
// Remove trailing ';'
if (args.charAt(args.length() - 1) == ';') {
args = args.substring(0, args.length() - 1);
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
@Override
public AtCommandResult handleActionCommand() {
int phoneType = mPhone.getPhoneType();
if (phoneType == Phone.PHONE_TYPE_CDMA) {
return cdmaGetClccResult();
} else if (phoneType == Phone.PHONE_TYPE_GSM) {
if (!BluetoothHeadset.isBluetoothVoiceDialingEnabled(mContext)) {
return new AtCommandResult(AtCommandResult.ERROR);
}
if (args.length >= 1 && args[0].equals(1)) {
synchronized (BluetoothHandsfree.this) {
if (!mWaitingForVoiceRecognition) {
}
}

/** Debug thread to read debug properties - runs when debug.bt.hfp is true
*  at the time a bluetooth handsfree device is connected. Debug properties
*  are polled and mock updates sent every 1 second */

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



return HeadsetBase.getAtInputCount();
}
};

@Override

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


}
} else {
PhoneApp app = PhoneApp.getInstance();

if (phoneType == Phone.PHONE_TYPE_CDMA) {
updateCdmaCallStateOnNewOutgoingCall(app);
}

//<End of snippet n. 2>








