//<Beginning of snippet n. 0>


// The private status of the view that created this player
private IsPrivateBrowsingEnabledGetter mIsPrivateBrowsingEnabledGetter;

private static int IDLE        =  0;
private static int INITIALIZED =  1;
private static int PREPARED    =  2;
private static int STARTED     =  4;
private static int COMPLETE    =  5;
private static int PAUSED      =  6;
private static int STOPPED     = -2;
private static int ERROR       = -1;
private static int AUDIOFOCUS_LOSS_TRANSIENT = 10; // Added state for transient audio focus loss

private int mState = IDLE;
private boolean mUserPaused = false; // Added state to track manual pause

// resume playback
if (mMediaPlayer == null) {
    resetMediaPlayer();
} else if (mState != ERROR && !mMediaPlayer.isPlaying()) {
    mMediaPlayer.start();
    mState = STARTED;
}
case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
    // Lost focus for a short time, but we have to check if paused by user
    if (mState != ERROR && mMediaPlayer.isPlaying()) {
        if (!mUserPaused) {
            pause();
        }
    }
    break;
}

private void pause() {
    if (mState == STARTED) {
        if (mTimer != null) {
            mTimer.purge();
        }
        mMediaPlayer.pause();
        mState = PAUSED;
        mUserPaused = true; // Set manual pause indicator
    }
}

// Method to unpause
public void unpause() {
    if (mUserPaused) {
        mMediaPlayer.start();
        mState = STARTED;
        mUserPaused = false; // Reset manual pause indicator
    }
}

// Additional handling for user intent when audio focus is lost
public void handleAudioFocusChange(int focusChange) {
    switch (focusChange) {
        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
            if (mState != ERROR && mMediaPlayer.isPlaying() && !mUserPaused) {
                pause();
            }
            break;
        case AudioManager.AUDIOFOCUS_LOSS:
            if (mState == STARTED) {
                pause();
            }
            break;
        case AudioManager.AUDIOFOCUS_GAIN:
            if (mState == PAUSED && !mUserPaused) {
                unpause();
            }
            break;
    }
}

//<End of snippet n. 0>