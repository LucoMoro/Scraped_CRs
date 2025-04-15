/*Audio continue to played even if paused manually

Currently, audio will resume playing after a phone call
or notification even if the user had manually paused playback.

This patch addresses that by introducing a new player state
to distinguish the user pausing playback and the browser
losing audio focus for some other reason. Audio will only
 resume if the browser temporarily lost audio focus and
not when the user has manually paused playback.

Change-Id:I9e8beaedb0fcc5afe920068297ed9c387eab2ac8Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5Audio.java b/core/java/android/webkit/HTML5Audio.java
//Synthetic comment -- index fc5df2d..684ec07 100644

//Synthetic comment -- @@ -54,14 +54,15 @@
// The private status of the view that created this player
private IsPrivateBrowsingEnabledGetter mIsPrivateBrowsingEnabledGetter;

    private static int IDLE                =  0;
    private static int INITIALIZED         =  1;
    private static int PREPARED            =  2;
    private static int STARTED             =  4;
    private static int COMPLETE            =  5;
    private static int PAUSED              =  6;
    private static int PAUSED_TRANSITORILY =  7;
    private static int STOPPED             = -2;
    private static int ERROR               = -1;

private int mState = IDLE;

//Synthetic comment -- @@ -247,7 +248,7 @@
// resume playback
if (mMediaPlayer == null) {
resetMediaPlayer();
            } else if (mState == PAUSED_TRANSITORILY && !mMediaPlayer.isPlaying()) {
mMediaPlayer.start();
mState = STARTED;
}
//Synthetic comment -- @@ -265,7 +266,9 @@
case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
// Lost focus for a short time, but we have to stop
// playback.
            if (mState != ERROR && mMediaPlayer.isPlaying()) {
                pause(PAUSED_TRANSITORILY);
            }
break;
}
}
//Synthetic comment -- @@ -298,12 +301,16 @@
}

private void pause() {
        pause(PAUSED);
    }

    private void pause(int state) {
if (mState == STARTED) {
if (mTimer != null) {
mTimer.purge();
}
mMediaPlayer.pause();
            mState = state;
}
}








