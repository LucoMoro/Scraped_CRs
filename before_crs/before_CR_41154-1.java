/*AudioPreview app betterments could save ~20mW for MP3 playback played through filemanager like apps

When music is played from other apps than the music player like File manager,
progress bar refresh happens even when screen is OFF. Changes are done in
AudioPreview.java to wake-up only if Display=ON

Change-Id:Ia9a3e7c4b3341a894d27b0aed7854d137a7c8ac2Author: sjayanti <satya.charitardha.jayanti@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 14933*/
//Synthetic comment -- diff --git a/src/com/android/music/AudioPreview.java b/src/com/android/music/AudioPreview.java
//Synthetic comment -- index 846ed14..4a5602a 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
//Synthetic comment -- @@ -31,6 +32,7 @@
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
//Synthetic comment -- @@ -196,6 +198,20 @@
}

@Override
public Object onRetainNonConfigurationInstance() {
PreviewPlayer player = mPlayer;
mPlayer = null;
//Synthetic comment -- @@ -309,7 +325,11 @@
mSeekBar.setProgress(mPlayer.getCurrentPosition());
}
mProgressRefresher.removeCallbacksAndMessages(null);
            mProgressRefresher.postDelayed(new ProgressRefresher(), 200);
}
}








