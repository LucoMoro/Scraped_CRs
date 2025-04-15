/*Fixed the implementaion of the SeekbarChangeListener Methods. This fixes the incorrect behavior of seekbar thumb's jump when it is dragged very fast. Now we ensure that there's only one REFRESH message queued and after the drag is over. The change is inline with the implementation in the MediaController class. Signed-off-by: Ritu Srivastava <rsrivast@sta.samsung.com>

Change-Id:I66cad9807f36d17248078b9a7f516d600419263e*/




//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackActivity.java b/src/com/android/music/MediaPlaybackActivity.java
//Synthetic comment -- index 1367c5a..eb35dbf 100644

//Synthetic comment -- @@ -362,32 +362,50 @@
return true;
}

    // There are two scenarios that can trigger the seekbar listener to trigger:
    //
    // The first is the user using the touchpad to adjust the posititon of the
    // seekbar's thumb. In this case onStartTrackingTouch is called followed by
    // a number of onProgressChanged notifications, concluded by onStopTrackingTouch.
    // We're setting the field "mFromTouch" to true for the duration of the dragging
    // session to avoid jumps in the position in case of ongoing playback.
    //
    // The second scenario involves the user operating the scroll ball, in this
    // case there WON'T BE onStartTrackingTouch/onStopTrackingTouch notifications,
    // we will simply apply the updated position without suspending regular updates.

private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
public void onStartTrackingTouch(SeekBar bar) {
mLastSeekEventTime = 0;
mFromTouch = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            mHandler.removeMessages(REFRESH);
}
public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
if (!fromuser || (mService == null)) return;
            mPosOverride = mDuration * progress / 1000;
            try {
                mService.seek(mPosOverride);
            } catch (RemoteException ex) {
            }

            refreshNow();
            // trackball event, allow progress updates
            if (!mFromTouch) {
                refreshNow();
                mPosOverride = -1;
}
}
public void onStopTrackingTouch(SeekBar bar) {
mPosOverride = -1;
mFromTouch = false;
            // Ensure that progress is properly updated in the future,
            mHandler.sendEmptyMessage(REFRESH);
}
};

//Synthetic comment -- @@ -1158,7 +1176,7 @@
private static final int ALBUM_ART_DECODED = 4;

private void queueNextRefresh(long delay) {
        if (!paused && !mFromTouch) {
Message msg = mHandler.obtainMessage(REFRESH);
mHandler.removeMessages(REFRESH);
mHandler.sendMessageDelayed(msg, delay);
//Synthetic comment -- @@ -1178,8 +1196,14 @@
mCurrentTime.setVisibility(View.VISIBLE);
} else {
// blink the counter
                    // If the progress bar is still been dragged, then we do not want to blink the 
                    // currentTime. It would cause flickering due to change in the visibility.
                    if (mFromTouch) {
                        mCurrentTime.setVisibility(View.VISIBLE); 
                    } else {
                        int vis = mCurrentTime.getVisibility();
                        mCurrentTime.setVisibility(vis == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
                    }
remaining = 500;
}








