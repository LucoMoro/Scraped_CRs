/*Support for VirtualVoiceCall over SCO - Implementation of the 3rd party
API

Change-Id:I7d354115532376e0e4dfdb814b151b6241d0876d*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 9d205e3..7dd4381 100644

//Synthetic comment -- @@ -57,9 +57,9 @@
*/
public class BluetoothHandsfree {
private static final String TAG = "BT HS/HF";
    private static final boolean DBG = true; //(PhoneApp.DBG_LEVEL >= 1)
            //&& (SystemProperties.getInt("ro.debuggable", 0) == 1);
    private static final boolean VDBG = true; //(PhoneApp.DBG_LEVEL >= 2);  // even more logging

public static final int TYPE_UNKNOWN           = 0;
public static final int TYPE_HEADSET           = 1;
//Synthetic comment -- @@ -154,6 +154,21 @@
private static final int BRSF_HF_ENHANCED_CALL_STATUS = 1 <<  5;
private static final int BRSF_HF_ENHANCED_CALL_CONTROL = 1 << 6;

    // VirtualCall support
    // VirtualCall can be enabled/disabled via this property
    private static final String PROPERTY_VIRTUALCALL_ENABLE = "service.bt.virtualcall";
	// Default VirtualCall is enabled unless disabled otherwise via the above property
    private static boolean mVirtualCallEnabled = true;
    // BluetoothHandsfree SCO states - both cellular call SCO  and VirtualCall SCO
    private static final int HF_STATE_SCO_IDLE = 1;
    private static final int HF_STATE_SCO_CALL_SETUP = 2;
    private static final int HF_STATE_SCO_CALL_ACTIVE = 3;
    private static final int HF_STATE_SCO_CALL_TRANSFERRED = 4;
    private static final int HF_STATE_SCO_VIRTUALCALL_SETUP = 5;
    private static final int HF_STATE_SCO_VIRTUALCALL_ACTIVE = 6;
    private static final int HF_STATE_SCO_VIRTUALCALL_TRANSFERRED = 7;
    private int mHFScoState;

public static String typeToString(int type) {
switch (type) {
case TYPE_UNKNOWN:
//Synthetic comment -- @@ -208,6 +223,7 @@
mBackgroundCall = mPhone.getBackgroundCall();
mBluetoothPhoneState = new BluetoothPhoneState();
mUserWantsAudio = true;
        mHFScoState = HF_STATE_SCO_IDLE;
mPhonebook = new BluetoothAtPhonebook(mContext, this);
mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
cdmaSetSecondCallState(false);
//Synthetic comment -- @@ -275,6 +291,10 @@
mHeadset = null;
stopDebug();
resetAtState();
        // this cleans up and resets any virtual call
        // especially needed if the handsfree device is switched
        // while in a virtual call
        terminateVirtualVoiceCall();
}

private void resetAtState() {
//Synthetic comment -- @@ -396,6 +416,10 @@
filter.addAction(TelephonyIntents.ACTION_SIGNAL_STRENGTH_CHANGED);
filter.addAction(BluetoothA2dp.ACTION_SINK_STATE_CHANGED);
mContext.registerReceiver(mStateReceiver, filter);

            // Intent Filter action for VirtualCall SCO
            // reset value of VirtualCallSCo from the property
            setVirtualCallStatusFromProperty();
}

private void updateBtPhoneStateAfterRadioTechnologyChange() {
//Synthetic comment -- @@ -666,7 +690,14 @@
switch (mPhoneState) {
case IDLE:
mUserWantsAudio = true;  // out of call - reset state
                    // if VirtualCallSco is not enabled, then we do perform the actions for the cellular call
                    // without looking into the state
                    // if VirtualCallSco is enabled, then the audioOff is done via the terminateVirtualCall
                    if ((isVirtualCallEnabled() == false) ||
                        (mHFScoState == HF_STATE_SCO_CALL_ACTIVE)) {
                        audioOff();
                        mHFScoState = HF_STATE_SCO_IDLE;
                    }
break;
default:
callStarted();
//Synthetic comment -- @@ -680,6 +711,8 @@
break;
case DIALING:
callsetup = 2;
                // if there is a VirtualCallSCO terminate it
                terminateVirtualVoiceCall();
mAudioPossible = true;
// We also need to send a Call started indication
// for cases where the 2nd MO was initiated was
//Synthetic comment -- @@ -690,6 +723,7 @@
if (mPhone.getPhoneType() == Phone.PHONE_TYPE_GSM) {
callStarted();
}
                mHFScoState = HF_STATE_SCO_CALL_SETUP;
break;
case ALERTING:
callsetup = 3;
//Synthetic comment -- @@ -705,6 +739,9 @@
case INCOMING:
case WAITING:
callsetup = 1;
                // if there is a VirtualCall, first terminate that
                terminateVirtualVoiceCall();
                mHFScoState = HF_STATE_SCO_CALL_SETUP;
break;
}

//Synthetic comment -- @@ -724,6 +761,7 @@
// This means that a call has transitioned from NOT ACTIVE to ACTIVE.
// Switch on audio.
audioOn();
                    mHFScoState = HF_STATE_SCO_CALL_ACTIVE;
}
mCall = call;
if (sendUpdate) {
//Synthetic comment -- @@ -898,7 +936,22 @@

private synchronized AtCommandResult toCindResult() {
AtCommandResult result = new AtCommandResult(AtCommandResult.OK);
            int call, call_setup;

            // Handsfree carkits expect that +CIND is properly responded to.
            // Hence we ensure that a proper response is sent for the virtual call too.
            if (mHFScoState == HF_STATE_SCO_VIRTUALCALL_SETUP) {
                call = 0;
                call_setup = 1;
            } else if (mHFScoState == HF_STATE_SCO_VIRTUALCALL_ACTIVE) {
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
//Synthetic comment -- @@ -947,8 +1000,15 @@
Log.i(TAG, "Routing audio for incoming SCO connection");
mConnectedSco = (ScoSocket)msg.obj;
mAudioManager.setBluetoothScoOn(true);
                            // Looks like the call has been transferred to the handsfree device
                            if (mHFScoState == HF_STATE_SCO_CALL_TRANSFERRED) {
                                 mHFScoState = HF_STATE_SCO_CALL_ACTIVE;
                                 broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_CONNECTED,
										                   mHeadset.getRemoteDevice());
                            } else if (mHFScoState == HF_STATE_SCO_VIRTUALCALL_TRANSFERRED) {
                                 mHFScoState = HF_STATE_SCO_VIRTUALCALL_ACTIVE;
                                 broadcastVirtualCallStateIntent(BluetoothHeadset.VIRTUALCALL_STATE_CONNECTED);
                            }
} else {
Log.i(TAG, "Rejecting incoming SCO connection");
((ScoSocket)msg.obj).close();
//Synthetic comment -- @@ -963,22 +1023,44 @@
if (VDBG) log("Routing audio for outgoing SCO conection");
mConnectedSco = (ScoSocket)msg.obj;
mAudioManager.setBluetoothScoOn(true);

                        if ((mHFScoState == HF_STATE_SCO_CALL_SETUP) ||
                            (mHFScoState == HF_STATE_SCO_CALL_TRANSFERRED)) {
                            mHFScoState = HF_STATE_SCO_CALL_ACTIVE;
							broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_CONNECTED,
									                  mHeadset.getRemoteDevice());
                        } else if ((mHFScoState == HF_STATE_SCO_VIRTUALCALL_SETUP) ||
                                   (mHFScoState == HF_STATE_SCO_VIRTUALCALL_TRANSFERRED)) {
                            mHFScoState = HF_STATE_SCO_VIRTUALCALL_ACTIVE;
							broadcastVirtualCallStateIntent(BluetoothHeadset.VIRTUALCALL_STATE_CONNECTED);
                        }
                        if (DBG) log("mHandler: Updated mHFScoState:"+ mHFScoState);
} else if (msg.arg1 == ScoSocket.STATE_CONNECTED) {
if (VDBG) log("Rejecting new connected outgoing SCO socket");
((ScoSocket)msg.obj).close();
mOutgoingSco.close();
                        mHFScoState = HF_STATE_SCO_IDLE;
                        // TODO: VC_AOSP Take a look at this            
                        if (DBG) log("mHandler: Updated mHFScoState:"+ mHFScoState);
}
mOutgoingSco = null;
break;
case SCO_CLOSED:
                    if(msg.obj == null)   // Ensure null pointer check is done before typecasting
                        break;
if (mConnectedSco == (ScoSocket)msg.obj) {
mConnectedSco.close();
mConnectedSco = null;
mAudioManager.setBluetoothScoOn(false);

                        if (mHFScoState == HF_STATE_SCO_CALL_ACTIVE) {
                            mHFScoState = HF_STATE_SCO_CALL_TRANSFERRED;
                            broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_DISCONNECTED,
mHeadset.getRemoteDevice());
                        } else if (mHFScoState == HF_STATE_SCO_VIRTUALCALL_ACTIVE) {
                            mHFScoState = HF_STATE_SCO_VIRTUALCALL_TRANSFERRED;
                            broadcastVirtualCallStateIntent(BluetoothHeadset.VIRTUALCALL_STATE_TRANSFERRED);
                        }
} else if (mOutgoingSco == (ScoSocket)msg.obj) {
mOutgoingSco.close();
mOutgoingSco = null;
//Synthetic comment -- @@ -1027,7 +1109,8 @@
if (VDBG) log("broadcastAudioStateIntent(" + state + ")");
Intent intent = new Intent(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
intent.putExtra(BluetoothHeadset.EXTRA_AUDIO_STATE, state);
        if( device != null )
            intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
}

//Synthetic comment -- @@ -1110,6 +1193,8 @@
/* package */ synchronized void userWantsAudioOn() {
mUserWantsAudio = true;
audioOn();
        // Unlike userWantsAudioOff, no need to act on call transfer
        // as the SCO_CONNECTED would be received.
}
/** Used to indicate the user requested BT audio off.
*  This will prevent us from establishing BT audio again during this call
//Synthetic comment -- @@ -1118,6 +1203,16 @@
/* package */ synchronized void userWantsAudioOff() {
mUserWantsAudio = false;
audioOff();

        // User has transferred the call from the handsfree to the phone
        // SCO_CLOSED shall not be received. So Act now.
        // 1. Set the state to HF_STATE_SCO_TRANSFERRED
        if (mHFScoState == HF_STATE_SCO_CALL_ACTIVE) {
            mHFScoState = HF_STATE_SCO_CALL_TRANSFERRED;
        } else if (mHFScoState == HF_STATE_SCO_VIRTUALCALL_ACTIVE) {
            mHFScoState = HF_STATE_SCO_VIRTUALCALL_TRANSFERRED;
            broadcastVirtualCallStateIntent(BluetoothHeadset.VIRTUALCALL_STATE_TRANSFERRED);
        }
}

/** Request to disconnect SCO (audio) connection to bluetooth
//Synthetic comment -- @@ -1149,7 +1244,19 @@
mConnectedSco.close();
mConnectedSco = null;
mAudioManager.setBluetoothScoOn(false);
            if ((mHFScoState == HF_STATE_SCO_VIRTUALCALL_ACTIVE) ||
                (mHFScoState == HF_STATE_SCO_VIRTUALCALL_SETUP) ||
                (mHFScoState == HF_STATE_SCO_VIRTUALCALL_TRANSFERRED)) {
                broadcastVirtualCallStateIntent(BluetoothHeadset.VIRTUALCALL_STATE_DISCONNECTED);
            } else {
                broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_DISCONNECTED, device);
            }

        } else {
           /* this is possible if the handsfree device powered off while in the middle of a virtual call */
            if (mHFScoState == HF_STATE_SCO_VIRTUALCALL_TRANSFERRED) {
               broadcastVirtualCallStateIntent(BluetoothHeadset.VIRTUALCALL_STATE_DISCONNECTED);
            }
}
if (mOutgoingSco != null) {
mOutgoingSco.close();
//Synthetic comment -- @@ -1187,6 +1294,9 @@
"outgoing calls found. Ignoring");
return new AtCommandResult(AtCommandResult.ERROR);
}
        // Outgoing call initiated by the handsfree device
        // Send terminateVirtualVoiceCall
        terminateVirtualVoiceCall();
Intent intent = new Intent(Intent.ACTION_CALL_PRIVILEGED,
Uri.fromParts("tel", number, null));
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//Synthetic comment -- @@ -1530,6 +1640,8 @@
}
return redial();
} else {
                        // Send terminateVirtualVoiceCall
                        terminateVirtualVoiceCall();
// Remove trailing ';'
if (args.charAt(args.length() - 1) == ';') {
args = args.substring(0, args.length() - 1);
//Synthetic comment -- @@ -1552,12 +1664,18 @@
@Override
public AtCommandResult handleActionCommand() {
sendURC("OK");
                if ((isVirtualCallEnabled() == true) &&
                    ((mHFScoState == HF_STATE_SCO_VIRTUALCALL_ACTIVE) ||
                    (mHFScoState == HF_STATE_SCO_VIRTUALCALL_TRANSFERRED))) {
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
//Synthetic comment -- @@ -1773,6 +1891,24 @@
@Override
public AtCommandResult handleActionCommand() {
int phoneType = mPhone.getPhoneType();
                // Handsfree carkits expect that +CLCC is properly responded to.
                // Hence we ensure that a proper response is sent for the virtual call too.
                if ((mHFScoState == HF_STATE_SCO_VIRTUALCALL_SETUP) ||
                    (mHFScoState == HF_STATE_SCO_VIRTUALCALL_ACTIVE)) {
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
//Synthetic comment -- @@ -2049,6 +2185,8 @@
if (!BluetoothHeadset.isBluetoothVoiceDialingEnabled(mContext)) {
return new AtCommandResult(AtCommandResult.ERROR);
}
                // Send terminateVirtualVoiceCall
                terminateVirtualVoiceCall();
if (args.length >= 1 && args[0].equals(1)) {
synchronized (BluetoothHandsfree.this) {
if (!mWaitingForVoiceRecognition) {
//Synthetic comment -- @@ -2228,6 +2366,162 @@
}
}

    // VirtualCall SCO support
    // This API should be called once and thereafter status is read via isVirtualCallEnabled()
    // Property overrides the default status
    private void setVirtualCallStatusFromProperty() {
        String prop = SystemProperties.get(PROPERTY_VIRTUALCALL_ENABLE);
        if (prop != null && prop.equals("0")) {
            mVirtualCallEnabled = false;
        }
        else {
            mVirtualCallEnabled = true;
        }
        log("setVirtualCallStatusFromProperty: mVirtualCallEnabled:" + mVirtualCallEnabled);
    }
    private boolean isVirtualCallEnabled() {
        return mVirtualCallEnabled;
    }

/* VC_AOSP
      private void updateAndBroadcastHFScoState(int state, int reason) {
        mHFScoState = state;
        broadcastHFScoStateIntent(reason);
    }
    private void broadcastHFScoStateIntent(int reason) {
	  if (isVirtualCallScoEnabled() == false) return;
      if (VDBG) log("broadcastHFScoStateIntent(" + mHFScoState + ")");
      String action;
      Intent intent;	  
	  action = BluetoothHeadset.VIRTUALCALL_STATE_CHANGED;
	  intent = new Intent(action);
      switch (mHFScoState) {
        case HF_STATE_SCO_IDLE:
			intent.putExtra(BluetoothHeadset.VIRTUALCALL_STATE,
				            BluetoothHeadset.VIRTUALCALL_STATE_DISCONNECTED);
			break;
        case HF_STATE_SCO_CALL_TRANSFERRED:
        case HF_STATE_SCO_VIRTUALCALL_TRANSFERRED:
            intent.putExtra(ScoIntent.SCO_CLOSED_REASON, reason);
            break;
        case HF_STATE_SCO_CALL_ACTIVE:
            action = ScoIntent.SCO_CALL_ACTIVE;
            intent = new Intent(action);
            break;
        case HF_STATE_SCO_VIRTUALCALL_ACTIVE:
            action = ScoIntent.SCO_VIRTUALCALL_ACTIVE;
            intent = new Intent(action);
            break;                  
        default:
            return;
      }
      mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
      if (DBG) Log.d(TAG, "broadcasHFScoStateIntent: Sent intent:" + action);     
    }
 */
    private void broadcastVirtualCallStateIntent(int state) {
	   if (VDBG) log("broadcastVirtualCallStateIntent(" + state + ")");
	   Intent intent = new Intent(BluetoothHeadset.ACTION_VIRTUALCALL_STATE_CHANGED);
	   intent.putExtra(BluetoothHeadset.EXTRA_VIRTUALCALL_STATE, state);
	   mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
    }

    //NOTE: Currently the VirtualCall API does not allow the application to initiate a call transfer.
    // Call transfer may be initiated from the handsfree device and this is handled by the VirtualCall API
    synchronized boolean initiateVirtualVoiceCall() {
        if (DBG) log("initiateVirtualVoiceCall: Received");
        // 1. Check if the SCO state is idle
        if ((isVirtualCallEnabled() == false) ||
            (mHFScoState != HF_STATE_SCO_IDLE)) {
            Log.e(TAG, "initiateVirtualVoiceCall: NotPossible:" + mHFScoState);
            return false;
        }

        // 1.5. Set the state to HF_STATE_VIRTUALCALL_SCO_SETUP
        mHFScoState = HF_STATE_SCO_VIRTUALCALL_SETUP;

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

        //VC_AOSP This is done from ScoConnected mHandler
        // 5. Broadcast the intent  
        //broadcastHFScoStateIntent(0);

        // Done
        if (DBG) log("initiateVirtualVoiceCall: Done");
        return true;
    }

    synchronized boolean terminateVirtualVoiceCall() {
        if (DBG) log("terminateVirtualVoiceCall: Received");
        if (isVirtualCallEnabled() == false) {
            return false;
        }

        // 1. Check if the SCO state is VirtualCall_active or setup
        if ((mHFScoState != HF_STATE_SCO_VIRTUALCALL_ACTIVE) &&
            (mHFScoState != HF_STATE_SCO_VIRTUALCALL_SETUP)  &&
            (mHFScoState != HF_STATE_SCO_VIRTUALCALL_TRANSFERRED)) {
            log("terminateVirtualVoiceCall: Nothing to terminate :" + mHFScoState);
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

        // 5. Set the state to HF_STATE_IDLE
        mHFScoState = HF_STATE_SCO_IDLE;

        // 4. terminate call-setup
        if (mBluetoothPhoneState.sendUpdate()) {
            AtCommandResult result = new AtCommandResult(AtCommandResult.UNSOLICITED);
            // outgoing call
            result.addResponse("+CIEV: 2,0");
            sendURC(result.toString());
            if (DBG) log("terminateVirtualVoiceCall: Sent Call-setup procedure");
        }
        mAudioPossible = false;

        // 6. Broadcast the intent
        // VC_AOSP This is done from audioOff
        //broadcastHFScoStateIntent(ScoIntent.SCO_CLOSED_REASON_CALL_ENDED);
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
//Synthetic comment -- index aab89ff..813ef39 100755

//Synthetic comment -- @@ -67,7 +67,7 @@
*/
public class PhoneUtils {
private static final String LOG_TAG = "PhoneUtils";
    private static final boolean DBG = true; //(PhoneApp.DBG_LEVEL >= 2);

/** Control stack trace for Audio Mode settings */
private static final boolean DBG_SETAUDIOMODE_STACK = false;
//Synthetic comment -- @@ -528,7 +528,14 @@
}
} else {
PhoneApp app = PhoneApp.getInstance();
                BluetoothHandsfree bthf = null;

                bthf = app.getBluetoothHandsfree();
                if (bthf != null) {
                    // This would be needed if the user initiates an outgoing cellular call from the phone while
                    // in a virtual voice call
                    bthf.terminateVirtualVoiceCall();
                }
if (phoneType == Phone.PHONE_TYPE_CDMA) {
updateCdmaCallStateOnNewOutgoingCall(app);
}







