/*setVibrateSetting for notifications in SoundSettings.

Some applications on the market change ringer and notification vibration settings independently. If a user removes this application, changing vibration settings only fixes their ringer setting. This patch keeps notification and ringer vibrate settings in sync. Otherwise, there is no way to fix notification vibration settings -- users expect vibration settings to carry over. Fixes issue 4964.

Change-Id:I706eccdc1f5a6ad908458313c53f0548be80cd82*/




//Synthetic comment -- diff --git a/src/com/android/settings/SoundSettings.java b/src/com/android/settings/SoundSettings.java
//Synthetic comment -- index a735268..836535c 100644

//Synthetic comment -- @@ -185,6 +185,10 @@
mAudioManager.setVibrateSetting(
AudioManager.VIBRATE_TYPE_RINGER,
AudioManager.VIBRATE_SETTING_ONLY_SILENT);
                mAudioManager.setVibrateSetting(
                    AudioManager.VIBRATE_TYPE_NOTIFICATION,
                    AudioManager.VIBRATE_SETTING_ONLY_SILENT);
                    
}
if (callsVibrateSetting == AudioManager.VIBRATE_SETTING_ON) {
return VALUE_VIBRATE_ALWAYS;
//Synthetic comment -- @@ -197,6 +201,10 @@
mAudioManager.setVibrateSetting(
AudioManager.VIBRATE_TYPE_RINGER,
AudioManager.VIBRATE_SETTING_OFF);
                mAudioManager.setVibrateSetting(
                    AudioManager.VIBRATE_TYPE_NOTIFICATION,
                    AudioManager.VIBRATE_SETTING_OFF);
                    
}
if (callsVibrateSetting == AudioManager.VIBRATE_SETTING_ON) {
return VALUE_VIBRATE_UNLESS_SILENT;
//Synthetic comment -- @@ -239,6 +247,9 @@
mAudioManager.setVibrateSetting(
AudioManager.VIBRATE_TYPE_RINGER,
callsVibrateSetting);
        mAudioManager.setVibrateSetting(
            AudioManager.VIBRATE_TYPE_NOTIFICATION,
            callsVibrateSetting);
}

// updateState in fact updates the UI to reflect the system state







