/*Don't die in a loop when OpenCORE isn't there.*/




//Synthetic comment -- diff --git a/src/com/android/phone/PhoneApp.java b/src/com/android/phone/PhoneApp.java
//Synthetic comment -- index 7d6ee62..2076453 100644

//Synthetic comment -- @@ -354,22 +354,26 @@
// Make sure the audio mode (along with some
// audio-mode-related state of our own) is initialized
// correctly, given the current state of the phone.
            try {
                switch (phone.getState()) {
                    case IDLE:
                        if (DBG) Log.d(LOG_TAG, "Resetting audio state/mode: IDLE");
                        PhoneUtils.setAudioControlState(PhoneUtils.AUDIO_IDLE);
                        PhoneUtils.setAudioMode(this, AudioManager.MODE_NORMAL);
                        break;
                    case RINGING:
                        if (DBG) Log.d(LOG_TAG, "Resetting audio state/mode: RINGING");
                        PhoneUtils.setAudioControlState(PhoneUtils.AUDIO_RINGING);
                        PhoneUtils.setAudioMode(this, AudioManager.MODE_RINGTONE);
                        break;
                    case OFFHOOK:
                        if (DBG) Log.d(LOG_TAG, "Resetting audio state/mode: OFFHOOK");
                        PhoneUtils.setAudioControlState(PhoneUtils.AUDIO_OFFHOOK);
                        PhoneUtils.setAudioMode(this, AudioManager.MODE_IN_CALL);
                        break;
                }
            } catch (NullPointerException npe) {
                Log.e(LOG_TAG, "ignoring NPE", npe);
}
}








