/*Issue 4625: Lock screen silent mode selection does not enable vibration.

This patch set fixes the issue that the silent mode selection on
the lock screen does not consider the vibration settings. This patch
allows setting the phone on vibrate mode if the vibration settings
is enabled and the user sets the phone on silent on the lock screen.
The same behavior is also fixed on the power off dialog silent mode
option.

Change-Id:I05da86e8a5e96e0bb2fb1bf59a5689e1fe6484fbSigned-off-by: David Sobreira Marques <dpsmarques@gmail.com>*/
//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/GlobalActions.java b/phone/com/android/internal/policy/impl/GlobalActions.java
//Synthetic comment -- index 2f1f024..d0cd6de 100644

//Synthetic comment -- @@ -126,8 +126,16 @@
R.string.global_action_silent_mode_off_status) {

void onToggle(boolean on) {
                mAudioManager.setRingerMode(on ? AudioManager.RINGER_MODE_SILENT
                        : AudioManager.RINGER_MODE_NORMAL);
}

public boolean showDuringKeyguard() {








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/LockScreen.java b/phone/com/android/internal/policy/impl/LockScreen.java
//Synthetic comment -- index 08cdd3f..31a19a6 100644

//Synthetic comment -- @@ -224,7 +224,8 @@
}

private boolean isSilentMode() {
        return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
}

private void updateRightTabResources() {
//Synthetic comment -- @@ -269,9 +270,17 @@
} else if (whichHandle == SlidingTab.OnTriggerListener.RIGHT_HANDLE) {
// toggle silent mode
mSilentMode = !mSilentMode;
            mAudioManager.setRingerMode(mSilentMode ? AudioManager.RINGER_MODE_SILENT
                        : AudioManager.RINGER_MODE_NORMAL);

updateRightTabResources();

String message = mSilentMode ?
//Synthetic comment -- @@ -599,7 +608,7 @@

/** {@inheritDoc} */
public void onRingerModeChanged(int state) {
        boolean silent = AudioManager.RINGER_MODE_SILENT == state;
if (silent != mSilentMode) {
mSilentMode = silent;
updateRightTabResources();







