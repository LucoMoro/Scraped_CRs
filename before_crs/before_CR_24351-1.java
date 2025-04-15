/*add broadcast receiver RingModeReceiver to test the notification of ringer mode changes from AudioManager
Signed-off-by: Zhihong GUO <zhihong.guo@orange-ftgroup.com>

Change-Id:Ie5fb053bfd4969f3673e4f354069209ba59b794d*/
//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioManagerTest.java b/tests/tests/media/src/android/media/cts/AudioManagerTest.java
//Synthetic comment -- index b4053cf..b1daa7b 100644

//Synthetic comment -- @@ -45,7 +45,10 @@
import dalvik.annotation.ToBeFixed;

import android.app.cts.CTSResult;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.Settings;
//Synthetic comment -- @@ -61,6 +64,11 @@
private int mResultCode;
private Sync mSync = new Sync();

private static class Sync {
private boolean notified;

//Synthetic comment -- @@ -621,4 +629,83 @@
mSync.notifyResult();
mResultCode = resultCode;
}
}







