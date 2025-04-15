/*Notification : some app vibrate with silent sound

In vibrate mode, some applications, like Email, vibrate even if
- vibrate option is never
- default sound is silent
To define silent sound, some applications use an empty uristring instead
a null one.

Change-Id:Idc9f75f3fb8ee2ef7966c152f158983725438b40*/




//Synthetic comment -- diff --git a/services/java/com/android/server/NotificationManagerService.java b/services/java/com/android/server/NotificationManagerService.java
//Synthetic comment -- index 70d37bf..46f236a 100644

//Synthetic comment -- @@ -1120,7 +1120,9 @@
// and no other vibration is specified, we apply the default vibration anyway
final boolean convertSoundToVibration =
!hasCustomVibrate
                        && (useDefaultSound
                            || ( (notification.sound != null)
                              && (! TextUtils.isEmpty(notification.sound.toString()))))
&& (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE);

// The DEFAULT_VIBRATE flag trumps any custom vibration.







