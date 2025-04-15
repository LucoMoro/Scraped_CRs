/*[Audio]Press button on wsHS while httplive audio playback in black screen, music playback automatically.

The key event is handled by media controller,
If media controller is hiddeni, the event can't be handled.
Try to make vedio view handle the event when media controller hidden.

Change-Id:I8c6aca2553eb82612df3849aab1dbf91da89f9b5Author: Weiwei Ji <weiweix.ji@intel.com>
Signed-off-by: Weiwei Ji <weiweix.ji@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 29426*/




//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5VideoFullScreen.java b/core/java/android/webkit/HTML5VideoFullScreen.java
//Synthetic comment -- index 9b93805..46a1d35 100644

//Synthetic comment -- @@ -28,14 +28,14 @@
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
//Synthetic comment -- @@ -197,6 +197,7 @@
super.onPrepared(mp);

mVideoSurfaceView.setOnTouchListener(this);
        mVideoSurfaceView.setOnKeyListener(this);
// Get the capabilities of the player for this stream
Metadata data = mp.getMetadata(MediaPlayer.METADATA_ALL,
MediaPlayer.BYPASS_METADATA_FILTER);
//Synthetic comment -- @@ -366,6 +367,15 @@
}

@Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (mFullScreenMode >= FULLSCREEN_SURFACECREATED
                && mMediaController != null) {
            return mMediaController.dispatchKeyEvent(event);
        }
        return false;
    }

    @Override
protected void switchProgressView(boolean playerBuffering) {
if (mProgressView != null) {
if (playerBuffering) {







