/*Issue 4625: Lock screen silent mode selection does not enable vibration.

This patch set fixes the issue that the silent mode selection on
the lock screen does not consider the vibration settings. This patch
allows setting the phone on vibrate mode if the vibration settings
is enabled and the user sets the phone on silent on the lock screen.

Change-Id:I84c153d6aa73e1fd6a4377e5983f1804123091f9Signed-off-by: David Sobreira Marques <dpsmarques@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/settings/SoundAndDisplaySettings.java b/src/com/android/settings/SoundAndDisplaySettings.java
//Synthetic comment -- index 29eb878..93c4ed6 100644

//Synthetic comment -- @@ -268,10 +268,10 @@
AudioManager.RINGER_MODE_SILENT);
} else {
mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
}
        mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                vibrate ? AudioManager.VIBRATE_SETTING_ON
                        : AudioManager.VIBRATE_SETTING_OFF);
}

@Override







