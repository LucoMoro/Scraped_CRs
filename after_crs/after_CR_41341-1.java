/*Music player progress bar updation is not required when the DUT display is OFF

We are getting one wake every 500ms from the application even when display is
off and user stays in music-application when song is playing. For low power
optimization, code is now changed that when the display is off this refresh
should not occur thereby helping in reducing 2 wakes/second

Change-Id:Ic14984b7f24e5642d58017d0acd21cda80233ab7Author: Charitardha <satya.charitardha.jayanti@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 27743*/




//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackActivity.java b/src/com/android/music/MediaPlaybackActivity.java
//Synthetic comment -- index 2f04978..957ba81 100644

//Synthetic comment -- @@ -45,6 +45,7 @@
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.TextUtils.TruncateAt;
//Synthetic comment -- @@ -482,6 +483,7 @@
IntentFilter f = new IntentFilter();
f.addAction(MediaPlaybackService.PLAYSTATE_CHANGED);
f.addAction(MediaPlaybackService.META_CHANGED);
        f.addAction(Intent.ACTION_SCREEN_ON);
registerReceiver(mStatusListener, new IntentFilter(f));
updateTrackInfo();
long next = refreshNow();
//Synthetic comment -- @@ -1161,7 +1163,11 @@
if (!paused) {
Message msg = mHandler.obtainMessage(REFRESH);
mHandler.removeMessages(REFRESH);
            //Post the refresh messgae only when the Screen is ON
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (pm.isScreenOn()) {
                mHandler.sendMessageDelayed(msg, delay);
            }
}
}

//Synthetic comment -- @@ -1254,6 +1260,9 @@
} else if (action.equals(MediaPlaybackService.PLAYSTATE_CHANGED)) {
setPauseButtonImage();
}
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                queueNextRefresh(1);
            }
}
};








