/*When setVibrateSetting is called to change vibrate settings for VIBRATE_TYPE_RINGER, added calls for VIBRATE_TYPE_RINGER. Some market applications are changing individual settings and users are expecting the vibrate setting to affect notifications as well. Without this, it is impossible to fix notification's vibrate settings if they are not in sync with those for VIBRATE_TYPE_RINGER.

Fixes issue 4964.

Change-Id:I706eccdc1f5a6ad908458313c53f0548be80cd82*/
//Synthetic comment -- diff --git a/src/com/android/settings/SoundSettings.java b/src/com/android/settings/SoundSettings.java
//Synthetic comment -- index a735268..836535c 100644

//Synthetic comment -- @@ -185,6 +185,10 @@
mAudioManager.setVibrateSetting(
AudioManager.VIBRATE_TYPE_RINGER,
AudioManager.VIBRATE_SETTING_ONLY_SILENT);
}
if (callsVibrateSetting == AudioManager.VIBRATE_SETTING_ON) {
return VALUE_VIBRATE_ALWAYS;
//Synthetic comment -- @@ -197,6 +201,10 @@
mAudioManager.setVibrateSetting(
AudioManager.VIBRATE_TYPE_RINGER,
AudioManager.VIBRATE_SETTING_OFF);
}
if (callsVibrateSetting == AudioManager.VIBRATE_SETTING_ON) {
return VALUE_VIBRATE_UNLESS_SILENT;
//Synthetic comment -- @@ -239,6 +247,9 @@
mAudioManager.setVibrateSetting(
AudioManager.VIBRATE_TYPE_RINGER,
callsVibrateSetting);
}

// updateState in fact updates the UI to reflect the system state







