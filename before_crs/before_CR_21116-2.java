/*Fixes for testing the low/min volume of the ringer for the Audio Manager Tests

Change-Id:Ieb869ce64e017cf6ec934014cf774c037acbf697*/
//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioManagerTest.java b/tests/tests/media/src/android/media/cts/AudioManagerTest.java
//Synthetic comment -- index 3558fa3..1a0ac01 100644

//Synthetic comment -- @@ -577,12 +577,20 @@
// API quirk: volume must be decremented from 1 to get ringer mode change
mAudioManager.setStreamVolume(streams[i], 1, FLAG_SHOW_UI);
mAudioManager.adjustStreamVolume(streams[i], ADJUST_LOWER, FLAG_ALLOW_RINGER_MODES);
                // lowering the volume should have changed the ringer mode
                assertEquals(RINGER_MODE_VIBRATE, mAudioManager.getRingerMode());
mAudioManager.adjustStreamVolume(streams[i], ADJUST_LOWER, FLAG_ALLOW_RINGER_MODES);
                // adjusting the volume to zero should result in either silent or vibrate mode
                assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_VIBRATE ||
                        mAudioManager.getRingerMode() == RINGER_MODE_SILENT);
mAudioManager.adjustStreamVolume(streams[i], ADJUST_RAISE, FLAG_ALLOW_RINGER_MODES);
assertEquals(RINGER_MODE_NORMAL, mAudioManager.getRingerMode());
}







