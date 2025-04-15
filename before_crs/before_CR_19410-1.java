/*Play ringtone on PHF/BT headset, even in silent mode

People were missing phone calls when their phones were in silent
mode and they were listening to music on their PHF/BT headsets.

Change-Id:Id9011b0456057e2eecd0723549652721df48133d*/
//Synthetic comment -- diff --git a/media/java/android/media/AudioService.java b/media/java/android/media/AudioService.java
//Synthetic comment -- index 5c278d9..a4987d23 100644

//Synthetic comment -- @@ -568,6 +568,7 @@

private void setRingerModeInt(int ringerMode, boolean persist) {
mRingerMode = ringerMode;

// Mute stream if not previously muted by ringer mode and ringer mode
// is not RINGER_MODE_NORMAL and stream is affected by ringer mode.
//Synthetic comment -- @@ -577,16 +578,22 @@
for (int streamType = numStreamTypes - 1; streamType >= 0; streamType--) {
if (isStreamMutedByRingerMode(streamType)) {
if (!isStreamAffectedByRingerMode(streamType) ||
                    mRingerMode == AudioManager.RINGER_MODE_NORMAL) {
mStreamStates[streamType].mute(null, false);
mRingerModeMutedStreams &= ~(1 << streamType);
}
} else {
if (isStreamAffectedByRingerMode(streamType) &&
                    mRingerMode != AudioManager.RINGER_MODE_NORMAL) {
                   mStreamStates[streamType].mute(null, true);
                   mRingerModeMutedStreams |= (1 << streamType);
               }
}
}

//Synthetic comment -- @@ -1147,6 +1154,8 @@
* take care of changing the index.
*/
adjustVolumeIndex = false;
}

return adjustVolumeIndex;
//Synthetic comment -- @@ -1918,6 +1927,7 @@
}
}
}
}
}








