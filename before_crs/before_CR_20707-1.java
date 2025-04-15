/*CTS test is not considering the case when the devcie get into SILENT mode by volume down key.

When volume gets down to SILENT mode, added the condition to go to VIBRATE mode or SILENT mode instead of SILENT mode only.

Change-Id:I50e4d45035975c418d244e15d892d37435119d72*/
//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioManagerTest.java b/tests/tests/media/src/android/media/cts/AudioManagerTest.java
//Synthetic comment -- index 3de8a51..cfa9293 100644

//Synthetic comment -- @@ -575,7 +575,8 @@
mAudioManager.setStreamVolume(streams[i], 1, FLAG_SHOW_UI);
mAudioManager.adjustStreamVolume(streams[i], ADJUST_LOWER, FLAG_ALLOW_RINGER_MODES);
// lowering the volume should have changed the ringer mode
                assertEquals(RINGER_MODE_VIBRATE, mAudioManager.getRingerMode());
mAudioManager.adjustStreamVolume(streams[i], ADJUST_LOWER, FLAG_ALLOW_RINGER_MODES);
// adjusting the volume to zero should result in either silent or vibrate mode
assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_VIBRATE ||







