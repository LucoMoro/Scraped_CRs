/*Video can not continue to play after wake up.

Save the time to make the player continue.
While playing, lock-screen will make the play paused, the time is not saved,
so it will play from beginning after unlock screen.

Change-Id:I54d2e40521c5351a563a269ade087bae203f8098Author: Bin Xu <binx.xu@intel.com>
Signed-off-by: Bin Xu <binx.xu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 26444*/
//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5VideoView.java b/core/java/android/webkit/HTML5VideoView.java
//Synthetic comment -- index 0e8a5db..d2cda28 100644

//Synthetic comment -- @@ -55,6 +55,10 @@
// video besides initial load.
protected int mSaveSeekTime;

// This is used to find the VideoLayer on the native side.
protected int mVideoLayerId;

//Synthetic comment -- @@ -167,6 +171,14 @@
return mPauseDuringPreparing;
}

// Every time we start a new Video, we create a VideoView and a MediaPlayer
public void init(int videoLayerId, int position, boolean skipPrepare) {
if (mPlayer == null) {
//Synthetic comment -- @@ -183,6 +195,7 @@
mSaveSeekTime = position;
mTimer = null;
mPauseDuringPreparing = false;
}

protected HTML5VideoView() {








//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5VideoViewProxy.java b/core/java/android/webkit/HTML5VideoViewProxy.java
//Synthetic comment -- index a3d62ae..121724e 100644

//Synthetic comment -- @@ -155,6 +155,9 @@
// save the current position.
if (layerId == mHTML5VideoView.getVideoLayerId()) {
savePosition = mHTML5VideoView.getCurrentPosition();
canSkipPrepare = (playerState == HTML5VideoView.STATE_PREPARING
|| playerState == HTML5VideoView.STATE_PREPARED
|| playerState == HTML5VideoView.STATE_PLAYING)
//Synthetic comment -- @@ -291,13 +294,22 @@
public static void end() {
mHTML5VideoView.showControllerInFullScreen();
if (mCurrentProxy != null) {
                if (isVideoSelfEnded)
mCurrentProxy.dispatchOnEnded();
else
mCurrentProxy.dispatchOnPaused();
}
isVideoSelfEnded = false;
}
}

// A bunch event listeners for our VideoView
//Synthetic comment -- @@ -663,6 +675,7 @@
private void sendTimeupdate() {
Message msg = Message.obtain(mWebCoreHandler, TIMEUPDATE);
msg.arg1 = VideoPlayer.getCurrentPosition();
mWebCoreHandler.sendMessage(msg);
}








