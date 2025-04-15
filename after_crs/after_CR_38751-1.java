/*differ VideoView's mechanism of updating video's width/height when onPrepare.

For NuPlayer, the video's width/height cannot be fetched on prepare.
when screen off/on, the videoView updates zero width/height,
and thus will lead full screen layout when suspend/resume.

Change-Id:I3f254cd96b5fd3cc1247b54cfee6e7cca2d64427Signed-off-by: xdyang <xdyang@marvell.com>*/




//Synthetic comment -- diff --git a/core/java/android/widget/VideoView.java b/core/java/android/widget/VideoView.java
//Synthetic comment -- index 8e438ff..0dd8665 100644

//Synthetic comment -- @@ -296,8 +296,14 @@
if (mMediaController != null) {
mMediaController.setEnabled(true);
}

            //nuplayer cannot get video w,h on prepare. keep the original value especially for suspend/resume.
            int videoWidth = mp.getVideoWidth();
            int videoHeight = mp.getVideoHeight();
            if (videoWidth != 0 && videoHeight != 0) {
                mVideoWidth = videoWidth;
                mVideoHeight = videoHeight;
            }

int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
if (seekToPosition != 0) {







