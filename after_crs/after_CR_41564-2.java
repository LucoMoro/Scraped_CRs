/*[Browser] Video can not continue to play after wake up.

Save the time to make the player continue.
While playing, lock-screen will make the play paused, the time is not saved,
so it will play from beginning after unlock screen.

Change-Id:Idc57b994854bdc25c1d5a15594e3904a3f78ec26Author: Bin Xu <binx.xu@intel.com>
Signed-off-by: Bin Xu <binx.xu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 26444*/




//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5VideoView.java b/core/java/android/webkit/HTML5VideoView.java
//Synthetic comment -- index 371feea..67bfc26 100644

//Synthetic comment -- @@ -42,6 +42,10 @@
// video besides initial load.
protected int mSaveSeekTime;

    // Save the last save time. When from InLine model to FullScreen model after unlocking screen,
    // make it continue.
    protected int mLastSaveTime;

// This is used to find the VideoLayer on the native side.
protected int mVideoLayerId;

//Synthetic comment -- @@ -142,6 +146,14 @@
return mPauseDuringPreparing;
}

    public void setLastSaveTime (int position) {
        mLastSaveTime = position;
    }

    public int getLastSaveTime () {
        return mLastSaveTime;
    }

// Every time we start a new Video, we create a VideoView and a MediaPlayer
public void init(int videoLayerId, int position, boolean skipPrepare) {
if (mPlayer == null) {
//Synthetic comment -- @@ -158,6 +170,7 @@
mSaveSeekTime = position;
mTimer = null;
mPauseDuringPreparing = false;
        mLastSaveTime = 0;
}

protected HTML5VideoView() {








//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5VideoViewProxy.java b/core/java/android/webkit/HTML5VideoViewProxy.java
//Synthetic comment -- index ab884df..7fee958 100644

//Synthetic comment -- @@ -160,6 +160,9 @@
// save the current position.
if (layerId == mHTML5VideoView.getVideoLayerId()) {
savePosition = mHTML5VideoView.getCurrentPosition();
                        if (savePosition == 0) {
                            savePosition = mHTML5VideoView.getLastSaveTime();
                        }
canSkipPrepare = (playerState == HTML5VideoView.STATE_PREPARING
|| playerState == HTML5VideoView.STATE_PREPARED
|| playerState == HTML5VideoView.STATE_PLAYING)
//Synthetic comment -- @@ -286,13 +289,22 @@
public static void end() {
mHTML5VideoView.showControllerInFullScreen();
if (mCurrentProxy != null) {
                if (isVideoSelfEnded) {
mCurrentProxy.dispatchOnEnded();
                    if (mHTML5VideoView != null)
                        mHTML5VideoView.setLastSaveTime(0);
                }
else
mCurrentProxy.dispatchOnPaused();
}
isVideoSelfEnded = false;
}

        public static void setLastSaveTime (int time) {
            if (mHTML5VideoView != null) {
                mHTML5VideoView.setLastSaveTime(time);
            }
        }
}

// A bunch event listeners for our VideoView
//Synthetic comment -- @@ -637,6 +649,7 @@
private void sendTimeupdate() {
Message msg = Message.obtain(mWebCoreHandler, TIMEUPDATE);
msg.arg1 = VideoPlayer.getCurrentPosition();
        VideoPlayer.setLastSaveTime(msg.arg1);
mWebCoreHandler.sendMessage(msg);
}








