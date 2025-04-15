/*Fix RingertoneManagerTest#testStopPreviousRingtone

Bug 2893018

Use adjustStreamVolume to get the device out of vibrate mode,
so that isPlaying will return true when its supposed to.

Change-Id:Ie86b0781d5c39dfcbc6e15c79c07fc67109896bc*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/RingtoneManagerTest.java b/tests/tests/media/src/android/media/cts/RingtoneManagerTest.java
//Synthetic comment -- index 31aab6d..dcc45c3 100644

//Synthetic comment -- @@ -63,9 +63,7 @@
mOriginalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
mDefaultUri = RingtoneManager.getActualDefaultRingtoneUri(mContext,
RingtoneManager.TYPE_RINGTONE);
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE,
AudioManager.FLAG_ALLOW_RINGER_MODES);
}








