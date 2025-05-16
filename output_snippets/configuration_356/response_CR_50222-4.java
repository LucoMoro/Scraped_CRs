//<Beginning of snippet n. 0>


// The private status of the view that created this player
private IsPrivateBrowsingEnabledGetter mIsPrivateBrowsingEnabledGetter;

private static int IDLE        =  0;
private static int INITIALIZED =  1;
private static int PREPARED    =  2;
private static int STARTED     =  4;
private static int COMPLETE    =  5;
private static int PAUSED      =  6;
private static int USER_PAUSED  =  7; // New state for user-initiated pause
private static int STOPPED     = -2;
private static int ERROR       = -1;

private int mState = IDLE;

// resume playback
if (mMediaPlayer == null) {
    resetMediaPlayer();
} else if (mState != ERROR && !mMediaPlayer.isPlaying()) {
    mMediaPlayer.start();
    mState = STARTED;
}
case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
    // Lost focus for a short time, but we can continue if paused by the user
    if (mState != ERROR && mMediaPlayer.isPlaying() && mState != USER_PAUSED) {
        pause();
    }
    break;
}
}

private void pause() {
    if (mState == STARTED) {
        if (mTimer != null) {
            mTimer.purge();
        }
        mMediaPlayer.pause();
        mState = USER_PAUSED; // Set state to USER_PAUSED for user-initiated pause
    }
}

private void resumePlayback() {
    if (mState == USER_PAUSED) {
        mMediaPlayer.start();
        mState = STARTED; // Resume state to STARTED
    }
}

//<End of snippet n. 0>