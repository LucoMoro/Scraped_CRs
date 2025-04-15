/*Fixed the implementaion of the SeekbarChangeListener Methods. This fixes the incorrect behavior of seekbar thumb's jump when it is dragged very fast. Now we ensure that there's only one REFRESH message queued and after the drag is over. The change is inline with the implementation in the MediaController class. Signed-off-by: Ritu Srivastava <rsrivast@sta.samsung.com>

Change-Id:I66cad9807f36d17248078b9a7f516d600419263e*/
//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackActivity.java b/src/com/android/music/MediaPlaybackActivity.java
//Synthetic comment -- index 1367c5a..71c6123 100644

//Synthetic comment -- @@ -362,32 +362,50 @@
return true;
}

private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
public void onStartTrackingTouch(SeekBar bar) {
mLastSeekEventTime = 0;
mFromTouch = true;
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

//Synthetic comment -- @@ -1158,7 +1176,7 @@
private static final int ALBUM_ART_DECODED = 4;

private void queueNextRefresh(long delay) {
        if (!paused) {
Message msg = mHandler.obtainMessage(REFRESH);
mHandler.removeMessages(REFRESH);
mHandler.sendMessageDelayed(msg, delay);
//Synthetic comment -- @@ -1178,8 +1196,14 @@
mCurrentTime.setVisibility(View.VISIBLE);
} else {
// blink the counter
                    int vis = mCurrentTime.getVisibility();
                    mCurrentTime.setVisibility(vis == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
remaining = 500;
}








