/*setRingerMode set Vibrate mode when setVolume to zero.

[DESC] setRingerMode has problem about mode.
    when we set silent mode and setStreamVolume to zero.
    then mode change from silent to vibrate.

Change-Id:I760e20fd5c88fd292629e34c91f095161d58b819*/
//Synthetic comment -- diff --git a/media/java/android/media/AudioService.java b/media/java/android/media/AudioService.java
//Synthetic comment -- index 22f699f..da01c75 100644

//Synthetic comment -- @@ -939,6 +939,11 @@
if (index == 0) {
newRingerMode = mHasVibrator ? AudioManager.RINGER_MODE_VIBRATE
: AudioManager.RINGER_MODE_SILENT;
setStreamVolumeInt(mStreamVolumeAlias[streamType],
index,
device,







