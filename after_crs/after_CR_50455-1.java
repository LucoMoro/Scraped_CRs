/*PhoneApp: Initialize ToneGenerator when needed

Whenever there is a radio tech change, mSignalInfoToneGenerator
is released and never created. This leads to phone app crash later
when it is accessed for toneplay. This is fixed by creating the
instance (if null) before calling the APIs.

Change-Id:I2f2513624b66892018dc3191daf3e48c63f744a1CRs-fixed: 438290*/




//Synthetic comment -- diff --git a/src/com/android/phone/CallNotifier.java b/src/com/android/phone/CallNotifier.java
//Synthetic comment -- index 99636df..6e4a33f 100755

//Synthetic comment -- @@ -208,18 +208,7 @@

registerForNotifications();

        createSignalInfoToneGenerator();

mRinger = ringer;
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//Synthetic comment -- @@ -236,6 +225,26 @@
| PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR);
}

    private void createSignalInfoToneGenerator() {
        // Instantiate the ToneGenerator for SignalInfo and CallWaiting
        // TODO: We probably don't need the mSignalInfoToneGenerator instance
        // around forever. Need to change it so as to create a ToneGenerator instance only
        // when a tone is being played and releases it after its done playing.
        if (mSignalInfoToneGenerator == null) {
            try {
                mSignalInfoToneGenerator = new ToneGenerator(AudioManager.STREAM_VOICE_CALL,
                        TONE_RELATIVE_VOLUME_SIGNALINFO);
                Log.d(LOG_TAG, "CallNotifier: mSignalInfoToneGenerator created when toneplay");
            } catch (RuntimeException e) {
                Log.w(LOG_TAG, "CallNotifier: Exception caught while creating " +
                        "mSignalInfoToneGenerator: " + e);
                mSignalInfoToneGenerator = null;
            }
        } else {
            Log.d(LOG_TAG, "mSignalInfoToneGenerator created already, hence skipping");
        }
    }

@Override
public void handleMessage(Message msg) {
switch (msg.what) {
//Synthetic comment -- @@ -872,6 +881,7 @@
// Release the ToneGenerator used for playing SignalInfo and CallWaiting
if (mSignalInfoToneGenerator != null) {
mSignalInfoToneGenerator.release();
            mSignalInfoToneGenerator = null;
}

// Clear ringback tone player
//Synthetic comment -- @@ -883,6 +893,9 @@
mCM.unregisterForInCallVoicePrivacyOn(this);
mCM.unregisterForInCallVoicePrivacyOff(this);

        // Instantiate mSignalInfoToneGenerator
        createSignalInfoToneGenerator();

// Register all events new to the new active phone
registerForNotifications();
}







