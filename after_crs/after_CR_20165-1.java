/*CTS test is not considering the case when the devcie get into SILENT mode
by volume down key.
It needs to be raised twice to get back to NORMAL mode from the SILENT mode.*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioManagerTest.java b/tests/tests/media/src/android/media/cts/AudioManagerTest.java
//Synthetic comment -- index 3558fa3..331f0e1 100644

//Synthetic comment -- @@ -583,7 +583,14 @@
// adjusting the volume to zero should result in either silent or vibrate mode
assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_VIBRATE ||
mAudioManager.getRingerMode() == RINGER_MODE_SILENT);
                mAudioManager.adjustStreamVolume(streams[i], ADJUST_RAISE, FLAG_ALLOW_RINGER_MODES)
		// add one more step for adding SILENT MODE by volume key		
		// if the mode was SILENT previously, now is the VIBRATE mode
		// so need to raise one mre
		assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_NORMAL ||
                        mAudioManager.getRingerMode() == RINGER_MODE_VIBRATE);
                mAudioManager.adjustStreamVolume(streams[i], ADJUST_RAISE, FLAG_ALLOW_RINGER_MODES)

assertEquals(RINGER_MODE_NORMAL, mAudioManager.getRingerMode());
}








