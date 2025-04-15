/*Make VideoView and MediaController listen to more key events

Make VideoView and MediaController listen to more key
events, i.e: "FastForward", "Rewind", "Next" and "Previous".

Change-Id:I522061a3fae2ab349d0ca8dbb82f9ba22e490cf7*/




//Synthetic comment -- diff --git a/core/java/android/widget/MediaController.java b/core/java/android/widget/MediaController.java
//Synthetic comment -- index f76ab2b..4182c9f 100644

//Synthetic comment -- @@ -97,6 +97,9 @@
private ImageButton         mNextButton;
private ImageButton         mPrevButton;

    private static final int    FAST_FORWARD_TIME = 15000;
    private static final int    REWIND_TIME = 5000;

public MediaController(Context context, AttributeSet attrs) {
super(context, attrs);
mRoot = this;
//Synthetic comment -- @@ -486,6 +489,30 @@
hide();
}
return true;
        } else if (event.isDown() && keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
            int pos = mPlayer.getCurrentPosition() + FAST_FORWARD_TIME;
            mPlayer.seekTo(pos);
            setProgress();
            show(sDefaultTimeout);
            return true;
        } else if (event.isDown() && keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
            int pos = mPlayer.getCurrentPosition() - REWIND_TIME;
            mPlayer.seekTo(pos);
            setProgress();
            show(sDefaultTimeout);
            return true;
        } else if (event.isDown() && keyCode == KeyEvent.KEYCODE_MEDIA_NEXT) {
            int pos = mPlayer.getDuration();
            mPlayer.seekTo(pos);
            setProgress();
            show(sDefaultTimeout);
            return true;
        } else if (event.isDown() && keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
            int pos = 0;
            mPlayer.seekTo(pos);
            setProgress();
            show(sDefaultTimeout);
            return true;
}

show(sDefaultTimeout);








//Synthetic comment -- diff --git a/core/java/android/widget/VideoView.java b/core/java/android/widget/VideoView.java
//Synthetic comment -- index 7c8196d..fe9a2bf 100644

//Synthetic comment -- @@ -91,6 +91,9 @@
private boolean     mCanSeekBack;
private boolean     mCanSeekForward;

    private static final int    FAST_FORWARD_TIME = 15000;
    private static final int    REWIND_TIME = 5000;

public VideoView(Context context) {
super(context);
initVideoView();
//Synthetic comment -- @@ -566,6 +569,22 @@
mMediaController.show();
}
return true;
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
                int pos = getCurrentPosition() + FAST_FORWARD_TIME;
                seekTo(pos);
                mMediaController.show();
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
                int pos = getCurrentPosition() - REWIND_TIME;
                seekTo(pos);
                mMediaController.show();
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_NEXT) {
                int pos = getDuration();
                seekTo(pos);
                mMediaController.show();
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
                int pos = 0;
                seekTo(pos);
                mMediaController.show();
} else {
toggleMediaControlsVisiblity();
}







