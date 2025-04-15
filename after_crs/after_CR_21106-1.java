/*Adjust AudioManagerTest#testVolume to check for new silent vs vibrate behavior.

Since System.VIBRATE_IN_SILENT is not part of public API, test has been changed
to check that either vibrate or silent mode is active when volume is adjusted
to zero.

Related framework changes:I14cf91b0Change-Id:I4e61705c7177601c6251a1a13198ce0f4f19bf69*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioManagerTest.java b/tests/tests/media/src/android/media/cts/AudioManagerTest.java
//Synthetic comment -- index aa3831f..01ca1d2 100644

//Synthetic comment -- @@ -560,12 +560,9 @@
if (streams[i] == AudioManager.STREAM_RING) {
mAudioManager.adjustStreamVolume(streams[i], ADJUST_LOWER, FLAG_SHOW_UI);
assertEquals(0, mAudioManager.getStreamVolume(streams[i]));
                // adjusting the volume to zero should result in either silent or vibrate mode
                assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_VIBRATE ||
                        mAudioManager.getRingerMode() == RINGER_MODE_SILENT);
mAudioManager.adjustStreamVolume(streams[i], ADJUST_RAISE, FLAG_SHOW_UI);
assertEquals(RINGER_MODE_NORMAL, mAudioManager.getRingerMode());
} else {
//Synthetic comment -- @@ -579,9 +576,9 @@
// lowering the volume should have changed the ringer mode
assertEquals(RINGER_MODE_VIBRATE, mAudioManager.getRingerMode());
mAudioManager.adjustStreamVolume(streams[i], ADJUST_LOWER, FLAG_ALLOW_RINGER_MODES);
                // adjusting the volume to zero should result in either silent or vibrate mode
                assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_VIBRATE ||
                        mAudioManager.getRingerMode() == RINGER_MODE_SILENT);
mAudioManager.adjustStreamVolume(streams[i], ADJUST_RAISE, FLAG_ALLOW_RINGER_MODES);
assertEquals(RINGER_MODE_NORMAL, mAudioManager.getRingerMode());
}







