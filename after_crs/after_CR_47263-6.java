/*Set audio mode after accepting the call

Set the audio mode to incall immediately after accepting the call and not wait
till call is reported as ACTIVE by RIL. This is done to speed up time taken for audio
to set up the voice path

Bug: 7612431
Change-Id:I8008edc64928016ab94aa6d770de248b5d85dc93*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CallManager.java b/src/java/com/android/internal/telephony/CallManager.java
//Synthetic comment -- index 7d696b4..1c1799f 100644

//Synthetic comment -- @@ -100,6 +100,8 @@
// default phone as the first phone registered, which is PhoneBase obj
private Phone mDefaultPhone;

    private boolean mSpeedUpAudioForMtCall = false;

// state registrants
protected final RegistrantList mPreciseCallStateRegistrants
= new RegistrantList();
//Synthetic comment -- @@ -377,14 +379,21 @@
// but only on audio mode transitions
switch (getState()) {
case RINGING:
                int curAudioMode = audioManager.getMode();
                if (curAudioMode != AudioManager.MODE_RINGTONE) {
// only request audio focus if the ringtone is going to be heard
if (audioManager.getStreamVolume(AudioManager.STREAM_RING) > 0) {
if (VDBG) Rlog.d(LOG_TAG, "requestAudioFocus on STREAM_RING");
audioManager.requestAudioFocusForCall(AudioManager.STREAM_RING,
AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
}
                    if(!mSpeedUpAudioForMtCall) {
                        audioManager.setMode(AudioManager.MODE_RINGTONE);
                    }
                }

                if (mSpeedUpAudioForMtCall && (curAudioMode != AudioManager.MODE_IN_CALL)) {
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
}
break;
case OFFHOOK:
//Synthetic comment -- @@ -400,13 +409,14 @@
// enable IN_COMMUNICATION audio mode instead for sipPhone
newAudioMode = AudioManager.MODE_IN_COMMUNICATION;
}
                if (audioManager.getMode() != newAudioMode || mSpeedUpAudioForMtCall) {
// request audio focus before setting the new mode
if (VDBG) Rlog.d(LOG_TAG, "requestAudioFocus on STREAM_VOICE_CALL");
audioManager.requestAudioFocusForCall(AudioManager.STREAM_VOICE_CALL,
AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
audioManager.setMode(newAudioMode);
}
                mSpeedUpAudioForMtCall = false;
break;
case IDLE:
if (audioManager.getMode() != AudioManager.MODE_NORMAL) {
//Synthetic comment -- @@ -415,6 +425,7 @@
// abandon audio focus after the mode has been set back to normal
audioManager.abandonAudioFocusForCall();
}
                mSpeedUpAudioForMtCall = false;
break;
}
}
//Synthetic comment -- @@ -529,6 +540,23 @@
}
}

        Context context = getContext();
        if (context == null) {
            Rlog.d(LOG_TAG, "Speedup Audio Path enhancement: Context is null");
        } else if (context.getResources().getBoolean(
                com.android.internal.R.bool.config_speed_up_audio_on_mt_calls)) {
            Rlog.d(LOG_TAG, "Speedup Audio Path enhancement");
            AudioManager audioManager = (AudioManager)
                    context.getSystemService(Context.AUDIO_SERVICE);
            int currMode = audioManager.getMode();
            if ((currMode != AudioManager.MODE_IN_CALL) && !(ringingPhone instanceof SipPhone)) {
                Rlog.d(LOG_TAG, "setAudioMode Setting audio mode from " +
                                currMode + " to " + AudioManager.MODE_IN_CALL);
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                mSpeedUpAudioForMtCall = true;
            }
        }

ringingPhone.acceptCall();

if (VDBG) {







