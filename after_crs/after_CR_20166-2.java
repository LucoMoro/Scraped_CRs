/*CTS test is not considering the case when the devcie get into SILENT mode
by volume down key.
It needs to be raised twice to get back to NORMAL mode from the SILENT mode.

Change-Id:I094840860ae92256263331dd0e3d40fa26a3a6e1*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioManagerTest.java b/tests/tests/media/src/android/media/cts/AudioManagerTest.java
//Synthetic comment -- index 3558fa3..650c542 100644

//Synthetic comment -- @@ -584,6 +584,13 @@
assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_VIBRATE ||
mAudioManager.getRingerMode() == RINGER_MODE_SILENT);
mAudioManager.adjustStreamVolume(streams[i], ADJUST_RAISE, FLAG_ALLOW_RINGER_MODES);
                // There are two possible ways the device may work. It may have a silent/vibrate
                // mode or it may have distinct silent and vibrate modes.
        		assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_NORMAL ||
        		        mAudioManager.getRingerMode() == RINGER_MODE_VIBRATE);
        		// Increase the volume one more time to get out of the vibrate mode which may
        		// be separate from silent mode.
                mAudioManager.adjustStreamVolume(streams[i], ADJUST_RAISE, FLAG_ALLOW_RINGER_MODES);
assertEquals(RINGER_MODE_NORMAL, mAudioManager.getRingerMode());
}








