/*Update track info in refresh in case the duration get by first time is not enough accurate.

There are some VBR files, which need parser great time to calculate the duration time.
So parser may calcultate a non-precise duration at first for fast start up playing back.
Then update the duration time to a more precise time at a later time.

Signed-off-by: guoyin.chen <guoyin.chen@freescale.com>
Change-Id:I0baf82d3a8623c4cee5010fbe28e3cf0433da4ec*/




//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackActivity.java b/src/com/android/music/MediaPlaybackActivity.java
//Synthetic comment -- index 021a199..c0f2c72 100644

//Synthetic comment -- @@ -374,6 +374,7 @@
long now = SystemClock.elapsedRealtime();
if ((now - mLastSeekEventTime) > 250) {
mLastSeekEventTime = now;
                updateTrackInfo();
mPosOverride = mDuration * progress / 1000;
try {
mService.seek(mPosOverride);
//Synthetic comment -- @@ -946,6 +947,7 @@
} else {
mService.play();
}
                updateTrackInfo();
refreshNow();
setPauseButtonImage();
}
//Synthetic comment -- @@ -1165,7 +1167,7 @@
try {
long pos = mPosOverride < 0 ? mService.position() : mPosOverride;
long remaining = 1000 - (pos % 1000);
            if ((pos >= 0) && (mService.duration() > 0)) {
mCurrentTime.setText(MusicUtils.makeTimeString(this, pos / 1000));

if (mService.isPlaying()) {
//Synthetic comment -- @@ -1200,6 +1202,7 @@
break;

case REFRESH:
                    updateTrackInfo();
long next = refreshNow();
queueNextRefresh(next);
break;







