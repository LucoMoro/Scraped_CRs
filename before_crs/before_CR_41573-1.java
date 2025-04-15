/*Fix the issue the progress bar cursor forward/backward abnormally during music seek

Need to change the seekto() method to be invoked on right time

Change-Id:Ibebd2045a3f7315e21cc960e99bb3b32b5304024Author: b359 <b359@borqs.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 35232*/
//Synthetic comment -- diff --git a/src/com/android/music/AudioPreview.java b/src/com/android/music/AudioPreview.java
//Synthetic comment -- index 846ed14..44ab7f7 100644

//Synthetic comment -- @@ -330,13 +330,14 @@
mSeeking = true;
}
public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
return;
}
            mPlayer.seekTo(progress);
}
public void onStopTrackingTouch(SeekBar bar) {
mSeeking = false;
}
};









//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackActivity.java b/src/com/android/music/MediaPlaybackActivity.java
//Synthetic comment -- index 2f04978..9fcb56c 100644

//Synthetic comment -- @@ -369,25 +369,22 @@
}
public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
if (!fromuser || (mService == null)) return;
            long now = SystemClock.elapsedRealtime();
            if ((now - mLastSeekEventTime) > 250) {
                mLastSeekEventTime = now;
mPosOverride = mDuration * progress / 1000;
                try {
                    mService.seek(mPosOverride);
                } catch (RemoteException ex) {
                }

                // trackball event, allow progress updates
                if (!mFromTouch) {
                    refreshNow();
                    mPosOverride = -1;
                }
}
}
public void onStopTrackingTouch(SeekBar bar) {
mPosOverride = -1;
mFromTouch = false;
}
};








