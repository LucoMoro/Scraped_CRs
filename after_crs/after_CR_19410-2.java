/*Ringtone is not routed to headset when silent mode is activated

People were missing phone calls when their phones were in silent
mode and they were listening to music on their headsets.

This is also important for 'manner mode'

Change-Id:Id9011b0456057e2eecd0723549652721df48133d*/




//Synthetic comment -- diff --git a/media/java/android/media/AudioService.java b/media/java/android/media/AudioService.java
//Synthetic comment -- index 37aacab..ff1a2c4 100644

//Synthetic comment -- @@ -638,7 +638,9 @@
VolumeStreamState streamState = mStreamStates[streamType];

// If stream is muted, set last audible index only
        if (streamState.muteCount() != 0
                || (mRingerMode != AudioManager.RINGER_MODE_NORMAL
                    && isStreamRoutedToWiredHeadset(streamType))) {
// Do not allow last audible index to be 0
if (index != 0) {
streamState.setLastAudibleIndex(index);
//Synthetic comment -- @@ -674,12 +676,23 @@

/** get stream mute state. */
public boolean isStreamMute(int streamType) {
        if (mRingerMode != AudioManager.RINGER_MODE_NORMAL
                && isStreamRoutedToWiredHeadset(streamType)) {
            return true;
        }

return (mStreamStates[streamType].muteCount() != 0);
}

/** @see AudioManager#getStreamVolume(int) */
public int getStreamVolume(int streamType) {
ensureValidStreamType(streamType);

        if (mRingerMode != AudioManager.RINGER_MODE_NORMAL
                && isStreamRoutedToWiredHeadset(streamType)) {
            return 0;
        }

return (mStreamStates[streamType].mIndex + 5) / 10;
}

//Synthetic comment -- @@ -714,16 +727,19 @@

private void setRingerModeInt(int ringerMode, boolean persist) {
mRingerMode = ringerMode;
        AudioSystem.setRingerMode(ringerMode, 0);

// Mute stream if not previously muted by ringer mode and ringer mode
        // is not RINGER_MODE_NORMAL and stream is affected by ringer mode, unless
        // stream is routed to a wired headset.
// Unmute stream if previously muted by ringer mode and ringer mode
// is RINGER_MODE_NORMAL or stream is not affected by ringer mode.
int numStreamTypes = AudioSystem.getNumStreamTypes();
for (int streamType = numStreamTypes - 1; streamType >= 0; streamType--) {
if (isStreamMutedByRingerMode(streamType)) {
                if (!isStreamAffectedByRingerMode(streamType)
                        || mRingerMode == AudioManager.RINGER_MODE_NORMAL
                        || isStreamRoutedToWiredHeadset(streamType)) {
// ring and notifications volume should never be 0 when not silenced
// on voice capable devices
if (mVoiceCapable &&
//Synthetic comment -- @@ -735,11 +751,20 @@
mRingerModeMutedStreams &= ~(1 << streamType);
}
} else {
                if (isStreamAffectedByRingerMode(streamType)
                        && mRingerMode != AudioManager.RINGER_MODE_NORMAL
                        && !isStreamRoutedToWiredHeadset(streamType)) {
                    mStreamStates[streamType].mute(null, true);
                    mRingerModeMutedStreams |= (1 << streamType);
                } else if (mVoiceCapable
                        && mRingerMode == AudioManager.RINGER_MODE_NORMAL
                        && mStreamStates[streamType].mLastAudibleIndex == 0
                        && isStreamRoutedToWiredHeadset(streamType)) {
                    // ring and notifications volume should never be 0 when not silenced
                    // on voice capable devices
                    mStreamStates[streamType].mLastAudibleIndex = 10;
                    mStreamStates[streamType].mIndex = 10;
                }
}
}

//Synthetic comment -- @@ -1798,6 +1823,12 @@
}
}

    private boolean isStreamRoutedToWiredHeadset(int streamType) {
        return STREAM_VOLUME_ALIAS[streamType] == AudioSystem.STREAM_RING
            && (mConnectedDevices.containsKey(AudioSystem.DEVICE_OUT_WIRED_HEADSET)
                || mConnectedDevices.containsKey(AudioSystem.DEVICE_OUT_WIRED_HEADPHONE));
    }

private void broadcastRingerMode() {
// Send sticky broadcast
Intent broadcast = new Intent(AudioManager.RINGER_MODE_CHANGED_ACTION);
//Synthetic comment -- @@ -2002,7 +2033,9 @@
if (muteCount() == 0) {
// If the stream is not muted any more, restore it's volume if
// ringer mode allows it
                                    if (!isStreamAffectedByRingerMode(mStreamType)
                                            || mRingerMode == AudioManager.RINGER_MODE_NORMAL
                                            || isStreamRoutedToWiredHeadset(mStreamType)) {
setIndex(mLastAudibleIndex, false);
sendMsg(mAudioHandler, MSG_SET_SYSTEM_VOLUME, mStreamType, SENDMSG_NOOP, 0, 0,
VolumeStreamState.this, 0);
//Synthetic comment -- @@ -2699,6 +2732,7 @@
} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
AudioSystem.setParameters("screen_state=off");
}
            setRingerModeInt(getRingerMode(), false);
}
}









//Synthetic comment -- diff --git a/media/java/android/media/Ringtone.java b/media/java/android/media/Ringtone.java
//Synthetic comment -- index f16ba36..1a6c8bc 100644

//Synthetic comment -- @@ -222,7 +222,9 @@
if (mAudio != null) {
// do not ringtones if stream volume is 0
// (typically because ringer mode is silent).
            // or if wireless headset is not connected.
            if (mAudioManager.getStreamVolume(mStreamType) != 0
                    || mAudioManager.isWiredHeadsetOn()) {
mAudio.start();
}
}







