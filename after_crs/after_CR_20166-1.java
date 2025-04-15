/*CTS test is not considering the case when the device get into SILENT mode by VOLUME down key*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioManagerTest.java b/tests/tests/media/src/android/media/cts/AudioManagerTest.java
//Synthetic comment -- index 331f0e1..9c39b08 100644

//Synthetic comment -- @@ -589,8 +589,10 @@
		// so need to raise one mre
		assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_NORMAL ||
mAudioManager.getRingerMode() == RINGER_MODE_VIBRATE);
                // 
		if(mAudioManager.getRingerMode() != RINGER_MODE_NORMAL){	
		mAudioManager.adjustStreamVolume(streams[i], ADJUST_RAISE, FLAG_ALLOW_RINGER_MODES);
                }                
assertEquals(RINGER_MODE_NORMAL, mAudioManager.getRingerMode());
}








