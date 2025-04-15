/*CTS test is not considering the case when the devcie get into SILENT mode by volume down key.

When volume gets down to SILENT mode, added the condition to go to VIBRATE mode or SILENT mode instead of SILENT mode only.

Change-Id:I4d47c9678f2582bb9dc2ad5c01830d24428bbc19*/
//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioManagerTest.java b/tests/tests/media/src/android/media/cts/AudioManagerTest.java
//Synthetic comment -- index 3558fa3..f64a539 100644

//Synthetic comment -- @@ -578,7 +578,8 @@
mAudioManager.setStreamVolume(streams[i], 1, FLAG_SHOW_UI);
mAudioManager.adjustStreamVolume(streams[i], ADJUST_LOWER, FLAG_ALLOW_RINGER_MODES);
// lowering the volume should have changed the ringer mode
                assertEquals(RINGER_MODE_VIBRATE, mAudioManager.getRingerMode());
mAudioManager.adjustStreamVolume(streams[i], ADJUST_LOWER, FLAG_ALLOW_RINGER_MODES);
// adjusting the volume to zero should result in either silent or vibrate mode
assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_VIBRATE ||







