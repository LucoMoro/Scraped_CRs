/*add a possible volume behavior.
Some device will be silent mode after the first volume up from vibrate mode.

Change-Id:Ibf8c323a160ca1e3f2611c0b746976ac9b1942e7*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioManagerTest.java b/tests/tests/media/src/android/media/cts/AudioManagerTest.java
//Synthetic comment -- index b4053cf..fafac9b 100644

//Synthetic comment -- @@ -553,11 +553,17 @@
// adjusting the volume to zero should result in either silent or vibrate mode
assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_VIBRATE ||
mAudioManager.getRingerMode() == RINGER_MODE_SILENT);
                int lastRingerMode = mAudioManager.getRingerMode();
mAudioManager.adjustStreamVolume(streams[i], ADJUST_RAISE, FLAG_ALLOW_RINGER_MODES);
// There are two possible ways the device may work. It may have a silent/vibrate
// mode or it may have distinct silent and vibrate modes.
                if(lastRingerMode == RINGER_MODE_VIBRATE){
                    assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_NORMAL ||
                        	mAudioManager.getRingerMode() == RINGER_MODE_SILENT);	
                }else if (lastRingerMode == RINGER_MODE_SILENT){
                    assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_NORMAL ||
                        	mAudioManager.getRingerMode() == RINGER_MODE_VIBRATE);
                }
// Increase the volume one more time to get out of the vibrate mode which may
// be separate from silent mode.
mAudioManager.adjustStreamVolume(streams[i], ADJUST_RAISE, FLAG_ALLOW_RINGER_MODES);







