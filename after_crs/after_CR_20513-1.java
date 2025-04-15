/*Added fix for race condition in ringer notification on CDMA variant

In CDMA during a incoming call, RIL sends out a
UNSOL_RESPONSE_CALL_STATE_CHANGED message which triggers a
GET_CURRENT_CALL. Since we are already getting the current call.
RIL sends out UNSOL_CALL_RING right after.

This causes the CallNotifier to think that it is a repeat ring,
so it calls ringer.ring() before the custom ringtone query is
finished.

The RIL sequence is different in GSM, it doesn't send out a
CALL_STATE_CHANGE first. So this problem doesn't happen in GSM.
So set a variable to wait for the custom ringtone query to finish
or time out before processing a Repeat ring request in CallNotifier.

Change-Id:I245cffc66583138a33212d63f19db8ed027b1e1e*/




//Synthetic comment -- diff --git a/src/com/android/phone/CallNotifier.java b/src/com/android/phone/CallNotifier.java
//Synthetic comment -- index 90c1a59..f5523f5 100755

//Synthetic comment -- @@ -150,6 +150,7 @@
private BluetoothHandsfree mBluetoothHandsfree;
private CallLogAsync mCallLog;
private boolean mSilentRingerRequested;
    private boolean mIsQueryingCallerInfo;

// ToneGenerator instance for playing SignalInfo tones
private ToneGenerator mSignalInfoToneGenerator;
//Synthetic comment -- @@ -224,7 +225,8 @@
PhoneBase pb =  (PhoneBase)((AsyncResult)msg.obj).result;

if ((pb.getState() == Phone.State.RINGING)
                            && (mSilentRingerRequested == false)
                            && (mIsQueryingCallerInfo == false)) {
if (DBG) log("RINGING... (PHONE_INCOMING_RING event)");
mRinger.ring();
} else {
//Synthetic comment -- @@ -500,6 +502,7 @@
}
}
if (shouldStartQuery) {
            mIsQueryingCallerInfo = true;
// create a custom ringer using the default ringer first
mRinger.setCustomRingtoneUri(Settings.System.DEFAULT_RINGTONE_URI);

//Synthetic comment -- @@ -533,6 +536,7 @@
// showIncomingCall().
if (DBG) log("- showing incoming call (couldn't start query)...");
showIncomingCall();
            mIsQueryingCallerInfo = false;
}
}

//Synthetic comment -- @@ -890,6 +894,7 @@
onCustomRingQueryComplete();
}
}
        mIsQueryingCallerInfo = false;
}

private void onDisconnect(AsyncResult r) {







