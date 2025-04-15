/*Ringtone is not routed to headset when silent mode is activated

People were missing phone calls when their phones were in silent
mode and they were listening to music on their headsets.

The notification should be routed to headset/headphone even when
the phone is in silent/vibrate mode.

This is also part of the japanese 'manner mode'.

Change-Id:Id9011b0456057e2eecd0723549652721df48133d*/
//Synthetic comment -- diff --git a/services/java/com/android/server/NotificationManagerService.java b/services/java/com/android/server/NotificationManagerService.java
//Synthetic comment -- index f6d3b608..d2fadac 100755

//Synthetic comment -- @@ -1038,7 +1038,8 @@
mSoundNotification = r;
// do not play notifications if stream volume is 0
// (typically because ringer mode is silent).
                    if (audioManager.getStreamVolume(audioStreamType) != 0) {
final long identity = Binder.clearCallingIdentity();
try {
final IRingtonePlayer player = mAudioService.getRingtonePlayer();







