/*CTS test is not considering the case when the devcie get into SILENT mode
by volume down key.
It needs to be raised twice to get back to NORMAL mode from the SILENT mode.

Change-Id:I094840860ae92256263331dd0e3d40fa26a3a6e1*/
//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioManagerTest.java b/tests/tests/media/src/android/media/cts/AudioManagerTest.java
//Synthetic comment -- index 3558fa3..3de8a51 100644

//Synthetic comment -- @@ -27,9 +27,6 @@
import static android.media.AudioManager.RINGER_MODE_NORMAL;
import static android.media.AudioManager.RINGER_MODE_SILENT;
import static android.media.AudioManager.RINGER_MODE_VIBRATE;
import static android.media.AudioManager.ROUTE_BLUETOOTH_SCO;
import static android.media.AudioManager.ROUTE_EARPIECE;
import static android.media.AudioManager.ROUTE_SPEAKER;
import static android.media.AudioManager.STREAM_MUSIC;
import static android.media.AudioManager.USE_DEFAULT_STREAM_TYPE;
import static android.media.AudioManager.VIBRATE_SETTING_OFF;
//Synthetic comment -- @@ -584,6 +581,13 @@
assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_VIBRATE ||
mAudioManager.getRingerMode() == RINGER_MODE_SILENT);
mAudioManager.adjustStreamVolume(streams[i], ADJUST_RAISE, FLAG_ALLOW_RINGER_MODES);
assertEquals(RINGER_MODE_NORMAL, mAudioManager.getRingerMode());
}








