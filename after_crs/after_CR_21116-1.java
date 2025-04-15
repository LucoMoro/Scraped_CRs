/*Added checks for the FEATURE_VIBRATE support for the Audio Manager Tests

Change-Id:Ieb869ce64e017cf6ec934014cf774c037acbf697*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioManagerTest.java b/tests/tests/media/src/android/media/cts/AudioManagerTest.java
//Synthetic comment -- index 3558fa3..18df32b 100644

//Synthetic comment -- @@ -51,6 +51,7 @@
import android.app.cts.CTSResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.Settings;
//Synthetic comment -- @@ -63,6 +64,7 @@
private final static int MP3_TO_PLAY = R.raw.testmp3;
private final static long TIME_TO_PLAY = 2000;
private AudioManager mAudioManager;
    private PackageManager packageManager;
private int mResultCode;
private Sync mSync = new Sync();

//Synthetic comment -- @@ -85,6 +87,7 @@
protected void setUp() throws Exception {
super.setUp();
mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        packageManager = mContext.getPackageManager();
}

@TestTargets({
//Synthetic comment -- @@ -577,12 +580,20 @@
// API quirk: volume must be decremented from 1 to get ringer mode change
mAudioManager.setStreamVolume(streams[i], 1, FLAG_SHOW_UI);
mAudioManager.adjustStreamVolume(streams[i], ADJUST_LOWER, FLAG_ALLOW_RINGER_MODES);
                // if device support vibrate, test this function
                if (packageManager.hasSystemFeature(PackageManager.FEATURE_VIBRATE) {
                    // lowering the volume should have changed the ringer mode
                    assertEquals(RINGER_MODE_VIBRATE, mAudioManager.getRingerMode());
                }
mAudioManager.adjustStreamVolume(streams[i], ADJUST_LOWER, FLAG_ALLOW_RINGER_MODES);
                // if device support vibrate, test this function
                if (packageManager.hasSystemFeature(PackageManager.FEATURE_VIBRATE) {
                    // adjusting the volume to zero should result in either silent or vibrate mode
                    assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_VIBRATE || 
                            mAudioManager.getRingerMode() == RINGER_MODE_SILENT);
                } else {
                    assertTrue(mAudioManager.getRingerMode() == RINGER_MODE_SILENT);
                }
mAudioManager.adjustStreamVolume(streams[i], ADJUST_RAISE, FLAG_ALLOW_RINGER_MODES);
assertEquals(RINGER_MODE_NORMAL, mAudioManager.getRingerMode());
}







