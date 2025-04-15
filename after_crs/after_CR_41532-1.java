/*Press button on Wired Stereo Headset while httplive audio playback in black screen, music playback automatically.

The key event is handled by media controller,
If media controller is hidden, the event can't be handled.
Try to make vedio view handle the event when media controller hidden.

We only handle media control key events when in fullscreen,
otherwise the Back or Menu key will be handled by MediaController.

Change-Id:Iccfaf6f47a135d0ace32ec253c400fd82ed877d1Author: Weiwei Ji <weiweix.ji@intel.com>
Signed-off-by: Weiwei Ji <weiweix.ji@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 29426, 46540*/




//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5VideoFullScreen.java b/core/java/android/webkit/HTML5VideoFullScreen.java
//Synthetic comment -- index 33eaad6..e23f5c3 100644

//Synthetic comment -- @@ -13,14 +13,14 @@
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.view.KeyEvent;

/**
* @hide This is only used by the browser
*/
public class HTML5VideoFullScreen extends HTML5VideoView
implements MediaPlayerControl, MediaPlayer.OnPreparedListener,
    View.OnTouchListener, View.OnKeyListener {

// Add this sub-class to handle the resizing when rotating screen.
private class VideoSurfaceView extends SurfaceView {
//Synthetic comment -- @@ -180,6 +180,7 @@
super.onPrepared(mp);

mVideoSurfaceView.setOnTouchListener(this);
        mVideoSurfaceView.setOnKeyListener(this);
// Get the capabilities of the player for this stream
Metadata data = mp.getMetadata(MediaPlayer.METADATA_ALL,
MediaPlayer.BYPASS_METADATA_FILTER);
//Synthetic comment -- @@ -349,6 +350,22 @@
}

@Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // Media control key should be dispatched to MediaController to handle.
        if (mFullScreenMode >= FULLSCREEN_SURFACECREATED
                && mMediaController != null
                && (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY
                || keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE)) {
            return mMediaController.dispatchKeyEvent(event);
        }
        return false;
    }

    @Override
protected void switchProgressView(boolean playerBuffering) {
if (mProgressView != null) {
if (playerBuffering) {







