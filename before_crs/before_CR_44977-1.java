/*Telephony: Enable audio and call logs

Add logs for audio mode, call operations.
This would help in debugging above issues.

Change-Id:I0f370cdd5fbdc3ad7748bfc8f64f7994457f2d8a*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CallManager.java b/src/java/com/android/internal/telephony/CallManager.java
//Synthetic comment -- index b87ea50..4797b51 100644

//Synthetic comment -- @@ -398,13 +398,17 @@
int newAudioMode = AudioManager.MODE_IN_CALL;
if (offhookPhone instanceof SipPhone) {
// enable IN_COMMUNICATION audio mode instead for sipPhone
newAudioMode = AudioManager.MODE_IN_COMMUNICATION;
}
                if (audioManager.getMode() != newAudioMode) {
// request audio focus before setting the new mode
if (VDBG) Log.d(LOG_TAG, "requestAudioFocus on STREAM_VOICE_CALL");
audioManager.requestAudioFocusForCall(AudioManager.STREAM_VOICE_CALL,
AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
audioManager.setMode(newAudioMode);
}
break;







