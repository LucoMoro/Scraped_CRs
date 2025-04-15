/*Issue 4625: Lock screen silent mode selection does not enable vibration.

This patch set fixes the issue that the silent mode selection on
the lock screen does not consider the vibration settings. This patch
allows setting the phone on vibrate mode if the vibration settings
is enabled and the user sets the phone on silent on the lock screen.

Change-Id:I05da86e8a5e96e0bb2fb1bf59a5689e1fe6484fbSigned-off-by: David Sobreira Marques <dpsmarques@gmail.com>*/




//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/LockScreen.java b/phone/com/android/internal/policy/impl/LockScreen.java
//Synthetic comment -- index 08cdd3f..31a19a6 100644

//Synthetic comment -- @@ -224,7 +224,8 @@
}

private boolean isSilentMode() {
        int mode = mAudioManager.getRingerMode();
        return  mode == AudioManager.RINGER_MODE_SILENT || mode == AudioManager.RINGER_MODE_VIBRATE;
}

private void updateRightTabResources() {
//Synthetic comment -- @@ -269,9 +270,17 @@
} else if (whichHandle == SlidingTab.OnTriggerListener.RIGHT_HANDLE) {
// toggle silent mode
mSilentMode = !mSilentMode;

            int ringerMode  = AudioManager.RINGER_MODE_NORMAL;
            if (mSilentMode) {
                int vibrateMode = mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER);
                if (vibrateMode == AudioManager.VIBRATE_SETTING_ON) {
                    ringerMode = AudioManager.RINGER_MODE_VIBRATE;
                } else {
                    ringerMode = AudioManager.RINGER_MODE_SILENT;
                }
            }
            mAudioManager.setRingerMode(ringerMode);
updateRightTabResources();

String message = mSilentMode ?
//Synthetic comment -- @@ -599,7 +608,7 @@

/** {@inheritDoc} */
public void onRingerModeChanged(int state) {
        boolean silent = AudioManager.RINGER_MODE_SILENT == state || AudioManager.RINGER_MODE_VIBRATE == state;
if (silent != mSilentMode) {
mSilentMode = silent;
updateRightTabResources();







