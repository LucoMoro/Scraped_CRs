/*[HTML5Video:] Release MediaPlayer when WebView is paused.

When WebView is paused, Tablet could go suspend mode, but
MediaPlayer and all resources set isn't freed. To avoid
libstagefright crash and resume WebView correctly after
Tablet awaking and Ducati re-initialization, codecs buffers
should be reallocated and relinked. That could be done by
releasing of MediaPlayer and then AwesomePlayer and all
related resources thereby on WebView pausing. All related
context is restored on video restart.

Change-Id:Iaf0899c3460270e3743668d05feb997ee499f870Signed-off-by: Sergii Iegorov <x0155539@ti.com>*/




//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5VideoViewProxy.java b/core/java/android/webkit/HTML5VideoViewProxy.java
//Synthetic comment -- index d0237b5..4b61ac6 100644

//Synthetic comment -- @@ -137,6 +137,9 @@
// When switching out, clean the video content on the old page
// by telling the layer not readyToUseSurfTex.
setBaseLayer(mBaseLayer);
                if("true".equals(System.getProperty("omap.enhancement"))) {
                    mHTML5VideoView.release();
                }
}
}

//Synthetic comment -- @@ -171,20 +174,29 @@
WebChromeClient client, int videoLayerId) {
int currentVideoLayerId = -1;
boolean backFromFullScreenMode = false;
            boolean surfaceDeleted = true;
            int currentState = mHTML5VideoView.STATE_RELEASED;
if (mHTML5VideoView != null) {
currentVideoLayerId = mHTML5VideoView.getVideoLayerId();
backFromFullScreenMode = mHTML5VideoView.fullScreenExited();
                surfaceDeleted = mHTML5VideoView.surfaceTextureDeleted();
                currentState = mHTML5VideoView.getCurrentState();
}

if (backFromFullScreenMode
|| currentVideoLayerId != videoLayerId
                || surfaceDeleted == true
                || (currentState == mHTML5VideoView.STATE_RELEASED
                    && "true".equals(System.getProperty("omap.enhancement")))) {
// Here, we handle the case when switching to a new video,
// either inside a WebView or across WebViews
// For switching videos within a WebView or across the WebView,
// we need to pause the old one and re-create a new media player
// inside the HTML5VideoView.
                if ((mHTML5VideoView != null && !"true".equals(System.getProperty("omap.enhancement")))
                     || (mHTML5VideoView != null
                         && currentState != mHTML5VideoView.STATE_RELEASED
                         && "true".equals(System.getProperty("omap.enhancement")))) {
if (!backFromFullScreenMode) {
mHTML5VideoView.pauseAndDispatch(mCurrentProxy);
}







