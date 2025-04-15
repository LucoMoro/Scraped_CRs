/*Support for VirtualVoiceCall over SCO - Implementation of the 3rd party
API.

Review comments implemented

Change-Id:I320ab10f5ca6a0fcfd6e710e2514fea1004a722e*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 9d205e3..946c3f4 100644

//Synthetic comment -- @@ -154,6 +154,9 @@
private static final int BRSF_HF_ENHANCED_CALL_STATUS = 1 <<  5;
private static final int BRSF_HF_ENHANCED_CALL_CONTROL = 1 << 6;

    // VirtualCall - true if Virtual Call is active, false otherwise
    private boolean mVirtualCallStarted = false;

public static String typeToString(int type) {
switch (type) {
case TYPE_UNKNOWN:
//Synthetic comment -- @@ -208,6 +211,7 @@
mBackgroundCall = mPhone.getBackgroundCall();
mBluetoothPhoneState = new BluetoothPhoneState();
mUserWantsAudio = true;
        mVirtualCallStarted = false;
mPhonebook = new BluetoothAtPhonebook(mContext, this);
mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
cdmaSetSecondCallState(false);
//Synthetic comment -- @@ -275,6 +279,10 @@
mHeadset = null;
stopDebug();
resetAtState();
        // This cleans up and resets any virtual call
        // especially needed if the handsfree device is switched off
        // while in a virtual call
        terminateVirtualVoiceCall();
}

private void resetAtState() {
//Synthetic comment -- @@ -666,7 +674,11 @@
switch (mPhoneState) {
case IDLE:
mUserWantsAudio = true;  // out of call - reset state
                    // if VirtualCall is in progress, then the audioOff is done via
                    // the terminateVirtualCall, else audioOff is invoked from here
                    if (!isVirtualCallInProgress()) {
                        audioOff();
                    }
break;
default:
callStarted();
//Synthetic comment -- @@ -680,6 +692,8 @@
break;
case DIALING:
callsetup = 2;
                // if there is a VirtualCallSCO terminate it
                terminateVirtualVoiceCall();
mAudioPossible = true;
// We also need to send a Call started indication
// for cases where the 2nd MO was initiated was
//Synthetic comment -- @@ -705,6 +719,8 @@
case INCOMING:
case WAITING:
callsetup = 1;
                // if there is a VirtualCall, first terminate that
                terminateVirtualVoiceCall();
break;
}

//Synthetic comment -- @@ -898,7 +914,22 @@

private synchronized AtCommandResult toCindResult() {
AtCommandResult result = new AtCommandResult(AtCommandResult.OK);
            int call, call_setup;

            // Handsfree carkits expect that +CIND is properly responded to.
            // Hence we ensure that a proper response is sent for the virtual call too.
            if (isVirtualCallInProgress() && mOutgoingSco != null) {
                call = 0;
                call_setup = 1;
            } else if (isVirtualCallInProgress() && mConnectedSco != null) {
                call = 1;
                call_setup = 0;
            } else {
                // regular phone call
                call = mCall;
                call_setup = mCallsetup;
            }
            String status = "+CIND: " + mService + "," + call + "," + call_setup + "," +
mCallheld + "," + mSignal + "," + mRoam + "," + mBattchg;
result.addResponse(status);
return result;
//Synthetic comment -- @@ -947,8 +978,14 @@
Log.i(TAG, "Routing audio for incoming SCO connection");
mConnectedSco = (ScoSocket)msg.obj;
mAudioManager.setBluetoothScoOn(true);
                            // Looks like the call has been transferred back to the handsfree device
                            if (isCellularCallInProgress()) {
                                 broadcastAudioStateIntent(
                                     BluetoothHeadset.AUDIO_STATE_CONNECTED,
                                     mHeadset.getRemoteDevice());
                            } else if (isVirtualCallInProgress()) {
                                 broadcastVirtualCallStateIntent(BluetoothHeadset.VIRTUAL_CALL_STATE_CONNECTED);
                            }
} else {
Log.i(TAG, "Rejecting incoming SCO connection");
((ScoSocket)msg.obj).close();
//Synthetic comment -- @@ -963,8 +1000,14 @@
if (VDBG) log("Routing audio for outgoing SCO conection");
mConnectedSco = (ScoSocket)msg.obj;
mAudioManager.setBluetoothScoOn(true);

                        if (isCellularCallInProgress()) {
                            broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_CONNECTED,
                                                      mHeadset.getRemoteDevice());
                        } else if (isVirtualCallInProgress()) {
                            broadcastVirtualCallStateIntent(
                                   BluetoothHeadset.VIRTUAL_CALL_STATE_CONNECTED);
                        }
} else if (msg.arg1 == ScoSocket.STATE_CONNECTED) {
if (VDBG) log("Rejecting new connected outgoing SCO socket");
((ScoSocket)msg.obj).close();
//Synthetic comment -- @@ -977,8 +1020,14 @@
mConnectedSco.close();
mConnectedSco = null;
mAudioManager.setBluetoothScoOn(false);

                        if (isCellularCallInProgress()) {
                            broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_DISCONNECTED,
mHeadset.getRemoteDevice());
                        } else if (isVirtualCallInProgress()) {
                            broadcastVirtualCallStateIntent(
                               BluetoothHeadset.VIRTUAL_CALL_STATE_TRANSFERRED);
                        }
} else if (mOutgoingSco == (ScoSocket)msg.obj) {
mOutgoingSco.close();
mOutgoingSco = null;
//Synthetic comment -- @@ -1027,7 +1076,8 @@
if (VDBG) log("broadcastAudioStateIntent(" + state + ")");
Intent intent = new Intent(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
intent.putExtra(BluetoothHeadset.EXTRA_AUDIO_STATE, state);
        if( device != null )
            intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
}

//Synthetic comment -- @@ -1149,7 +1199,18 @@
mConnectedSco.close();
mConnectedSco = null;
mAudioManager.setBluetoothScoOn(false);
            if (isVirtualCallInProgress()) {
                broadcastVirtualCallStateIntent(BluetoothHeadset.VIRTUAL_CALL_STATE_DISCONNECTED);
            } else {
                broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_DISCONNECTED, device);
            }

        } else {
            /* this is possible if the handsfree device powered off while in the middle of a
            * virtual call */
            if (isVirtualCallInProgress()) {
               broadcastVirtualCallStateIntent(BluetoothHeadset.VIRTUAL_CALL_STATE_DISCONNECTED);
            }
}
if (mOutgoingSco != null) {
mOutgoingSco.close();
//Synthetic comment -- @@ -1187,6 +1248,9 @@
"outgoing calls found. Ignoring");
return new AtCommandResult(AtCommandResult.ERROR);
}
        // Outgoing call initiated by the handsfree device
        // Send terminateVirtualVoiceCall
        terminateVirtualVoiceCall();
Intent intent = new Intent(Intent.ACTION_CALL_PRIVILEGED,
Uri.fromParts("tel", number, null));
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//Synthetic comment -- @@ -1530,6 +1594,8 @@
}
return redial();
} else {
                        // Send terminateVirtualVoiceCall
                        terminateVirtualVoiceCall();
// Remove trailing ';'
if (args.charAt(args.length() - 1) == ';') {
args = args.substring(0, args.length() - 1);
//Synthetic comment -- @@ -1552,12 +1618,16 @@
@Override
public AtCommandResult handleActionCommand() {
sendURC("OK");
                if (isVirtualCallInProgress()) {
                    terminateVirtualVoiceCall();
                } else {
                    if (!mRingingCall.isIdle()) {
                        PhoneUtils.hangupRingingCall(mPhone);
                    } else if (!mForegroundCall.isIdle()) {
                        PhoneUtils.hangupActiveCall(mPhone);
                    } else if (!mBackgroundCall.isIdle()) {
                        PhoneUtils.hangupHoldingCall(mPhone);
                    }
}
return new AtCommandResult(AtCommandResult.UNSOLICITED);
}
//Synthetic comment -- @@ -1773,6 +1843,23 @@
@Override
public AtCommandResult handleActionCommand() {
int phoneType = mPhone.getPhoneType();
                // Handsfree carkits expect that +CLCC is properly responded to.
                // Hence we ensure that a proper response is sent for the virtual call too.
                if (isVirtualCallInProgress()) {
                    String number = mPhone.getLine1Number();
                    AtCommandResult result = new AtCommandResult(AtCommandResult.OK);
                    String args;
                    if (number == null) {
                        args = "+CLCC: 1,0,0,0,0,\"\",0";
                    }
                    else
                    {
                        args = "+CLCC: 1,0,0,0,0,\"" + number + "\"," +
                                  PhoneNumberUtils.toaFromString(number);
                    }
                    result.addResponse(args);
                    return result;
                }
if (phoneType == Phone.PHONE_TYPE_CDMA) {
return cdmaGetClccResult();
} else if (phoneType == Phone.PHONE_TYPE_GSM) {
//Synthetic comment -- @@ -2049,6 +2136,8 @@
if (!BluetoothHeadset.isBluetoothVoiceDialingEnabled(mContext)) {
return new AtCommandResult(AtCommandResult.ERROR);
}
                // Send terminateVirtualVoiceCall
                terminateVirtualVoiceCall();
if (args.length >= 1 && args[0].equals(1)) {
synchronized (BluetoothHandsfree.this) {
if (!mWaitingForVoiceRecognition) {
//Synthetic comment -- @@ -2228,6 +2317,119 @@
}
}

    // VirtualCall SCO support
    //
    // Cellular call in progress
    private boolean isCellularCallInProgress() {
        Call.State fgcallState = mForegroundCall.getState();
        Call.State ringcallState = mRingingCall.getState();

        if ((fgcallState == Call.State.ACTIVE) ||
             (fgcallState == Call.State.ALERTING) ||
             (fgcallState == Call.State.DIALING) ||
             (ringcallState == Call.State.INCOMING) ||
             (mRingingCall.isRinging())) {
             return true;
        } else {
             return false;
        }
    }
    // Virtual Call in Progress
    private boolean isVirtualCallInProgress() {
        return mVirtualCallStarted;
    }

    private void broadcastVirtualCallStateIntent(int state) {
	   if (VDBG) log("broadcastVirtualCallStateIntent(" + state + ")");
	   Intent intent = new Intent(BluetoothHeadset.ACTION_VIRTUAL_CALL_STATE_CHANGED);
	   intent.putExtra(BluetoothHeadset.EXTRA_VIRTUAL_CALL_STATE, state);
	   mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
    }

    //NOTE: Currently the VirtualCall API does not allow the application to initiate a call
    // transfer. Call transfer may be initiated from the handsfree device and this is handled by
    // the VirtualCall API
    synchronized boolean initiateVirtualVoiceCall() {
        if (DBG) log("initiateVirtualVoiceCall: Received");
        // 1. Check if the SCO state is idle
        if  ((isCellularCallInProgress()) ||
            (isVirtualCallInProgress())) {
            Log.e(TAG, "initiateVirtualVoiceCall: Call in progress");
            return false;
        }

        // 1.5. Set mVirtualCallStarted to true
        mVirtualCallStarted = true;

        // 2. Perform outgoing call setup procedure
        if (mBluetoothPhoneState.sendUpdate()) {
            AtCommandResult result = new AtCommandResult(AtCommandResult.UNSOLICITED);
            // outgoing call
            result.addResponse("+CIEV: 3,2");
            result.addResponse("+CIEV: 2,1");
            result.addResponse("+CIEV: 3,0");
            sendURC(result.toString());
            if (DBG) Log.d(TAG, "initiateVirtualVoiceCall: Sent Call-setup procedure");
        }
        // 3. Open the Audio Connection
        if (audioOn() == false) {
            log("initiateVirtualVoiceCall: audioON failed");
            terminateVirtualVoiceCall();
            return false;
        }
        // 3.5 Set the AudioManager state
        AudioManager audioManager =
                (AudioManager) mPhone.getContext().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getMode() != AudioManager.MODE_IN_CALL) {
            PhoneUtils.setAudioControlState(PhoneUtils.AUDIO_OFFHOOK);
            PhoneUtils.setAudioMode(mPhone.getContext(), AudioManager.MODE_IN_CALL);
            audioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
        }

        mAudioPossible = true;

        // Done
        if (DBG) log("initiateVirtualVoiceCall: Done");
        return true;
    }

    synchronized boolean terminateVirtualVoiceCall() {
        if (DBG) log("terminateVirtualVoiceCall: Received");
        // 1. Check if a virtual call is in progress
        if (!isVirtualCallInProgress()) {
            log("terminateVirtualVoiceCall: VirtualCall is not in progress");
            return false;
        }

        // 2 Set the AudioManager state
        AudioManager audioManager =
           (AudioManager) mPhone.getContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, true);
        PhoneUtils.setAudioControlState(PhoneUtils.AUDIO_IDLE);
        PhoneUtils.setAudioMode(mPhone.getContext(), AudioManager.MODE_NORMAL);

        // 3. Release audio connection
        audioOff();

        // 5. Reset mVirtualCallStarted to false
        mVirtualCallStarted = false;

        // 4. terminate call-setup
        if (mBluetoothPhoneState.sendUpdate()) {
            AtCommandResult result = new AtCommandResult(AtCommandResult.UNSOLICITED);
            // outgoing call
            result.addResponse("+CIEV: 2,0");
            sendURC(result.toString());
            if (DBG) log("terminateVirtualVoiceCall: Sent Call-setup procedure");
        }
        mAudioPossible = false;

        // Done
        if (DBG) log("terminateVirtualVoiceCall: Done");
        return true;
    }


/** Debug thread to read debug properties - runs when debug.bt.hfp is true
*  at the time a bluetooth handsfree device is connected. Debug properties
*  are polled and mock updates sent every 1 second */








//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHeadsetService.java b/src/com/android/phone/BluetoothHeadsetService.java
//Synthetic comment -- index 513caad..bff7aba 100644

//Synthetic comment -- @@ -677,6 +677,24 @@

return HeadsetBase.getAtInputCount();
}
        public boolean startVirtualVoiceCall() {
            enforceCallingOrSelfPermission(BLUETOOTH_PERM, "Need BLUETOOTH permission");
            synchronized (BluetoothHeadsetService.this) {
                if (mState != BluetoothHeadset.STATE_CONNECTED) {
                    return false;
                }
                return mBtHandsfree.initiateVirtualVoiceCall();
            }
        }
        public boolean stopVirtualVoiceCall() {
            enforceCallingOrSelfPermission(BLUETOOTH_PERM, "Need BLUETOOTH permission");
            synchronized (BluetoothHeadsetService.this) {
                if (mState != BluetoothHeadset.STATE_CONNECTED) {
                    return false;
                }
                return mBtHandsfree.terminateVirtualVoiceCall();
            }
        }
};

@Override








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneUtils.java b/src/com/android/phone/PhoneUtils.java
//Synthetic comment -- index aab89ff..f07a50a 100755

//Synthetic comment -- @@ -528,7 +528,14 @@
}
} else {
PhoneApp app = PhoneApp.getInstance();
                BluetoothHandsfree bthf = null;

                bthf = app.getBluetoothHandsfree();
                if (bthf != null) {
                    // This would be needed if the user initiates an outgoing cellular
                    // call from the phone while in a virtual voice call
                    bthf.terminateVirtualVoiceCall();
                }
if (phoneType == Phone.PHONE_TYPE_CDMA) {
updateCdmaCallStateOnNewOutgoingCall(app);
}







