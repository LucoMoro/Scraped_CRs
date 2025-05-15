//<Beginning of snippet n. 0>


private static final int BRSF_HF_ENHANCED_CALL_STATUS = 1 <<  5;
private static final int BRSF_HF_ENHANCED_CALL_CONTROL = 1 << 6;

public static String typeToString(int type) {
    switch (type) {
        case TYPE_UNKNOWN:
            if (mPhone != null) {
                mBackgroundCall = mPhone.getBackgroundCall();
            }
            mBluetoothPhoneState = new BluetoothPhoneState();
            mUserWantsAudio = true;
            mPhonebook = new BluetoothAtPhonebook(mContext, this);
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            cdmaSetSecondCallState(false);
            mHeadset = null;
            stopDebug();
            resetAtState();
            break;
        case DIALING:
            callsetup = 2;
            mAudioPossible = true;
            // We also need to send a Call started indication
        case INCOMING:
        case WAITING:
            callsetup = 1;
            break;
        default:
            callStarted();
            break;
    }
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
    }
}

private synchronized AtCommandResult toCindResult() {
    AtCommandResult result = new AtCommandResult(AtCommandResult.OK);
    String status = "+CIND: " + mService + "," + mCall + "," + mCallsetup + "," +
                    mCallheld + "," + mSignal + "," + mRoam + "," + mBattchg;
    result.addResponse(status);
    Log.i(TAG, "Routing audio for incoming SCO connection");
    
    if (mConnectedSco != null) {
        mAudioManager.setBluetoothScoOn(true);
        broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_CONNECTED,
                mHeadset.getRemoteDevice());
    } else {
        Log.i(TAG, "Rejecting incoming SCO connection");
        if (msg.obj instanceof ScoSocket) {
            ((ScoSocket) msg.obj).close();
        }
    }
    
    if (msg.arg1 == ScoSocket.STATE_CONNECTED) {
        if (mConnectedSco != null) {
            Log.i(TAG, "Rejecting new connected outgoing SCO socket");
            mConnectedSco.close();
        }
        mConnectedSco = null;
        mAudioManager.setBluetoothScoOn(false);
        broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_DISCONNECTED,
                mHeadset.getRemoteDevice());
    } else if (mOutgoingSco == (ScoSocket) msg.obj) {
        mOutgoingSco.close();
        mOutgoingSco = null;
        if (VDBG) log("broadcastAudioStateIntent(" + state + ")");
        Intent intent = new Intent(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
        intent.putExtra(BluetoothHeadset.EXTRA_AUDIO_STATE, state);
        intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
        mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
    }

    if (mConnectedSco != null) {
        mConnectedSco.close();
        mConnectedSco = null;
        mAudioManager.setBluetoothScoOn(false);
        broadcastAudioStateIntent(BluetoothHeadset.AUDIO_STATE_DISCONNECTED, device);
    }

    if (mOutgoingSco != null) {
        mOutgoingSco.close();
        Log.i(TAG, "outgoing calls found. Ignoring");
        return new AtCommandResult(AtCommandResult.ERROR);
    }
    Intent intent = new Intent(Intent.ACTION_CALL_PRIVILEGED,
            Uri.fromParts("tel", number, null));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    return redial();
}

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
                    // Potentially handle recognition logic here
                }
            }
        }
    }
}

//<End of snippet n. 0>