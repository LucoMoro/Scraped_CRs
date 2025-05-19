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

private int mState = IDLE;
private boolean wasPausedByUser = false;

// resume playback
if (mMediaPlayer == null) {
    resetMediaPlayer();
} else if (mState != ERROR && !mMediaPlayer.isPlaying()) {
    mMediaPlayer.start();
    mState = STARTED;
}
case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
    // Lost focus for a short time, but we have to stop playback only if not manually paused.
    if (mState != ERROR && mMediaPlayer.isPlaying() && !wasPausedByUser) pause();
    break;

private void pause() {
    if (mState == STARTED) {
        if (mTimer != null) {
            mTimer.purge();
        }
        mMediaPlayer.pause();
        mState = PAUSED;
        wasPausedByUser = true; // Set only for user-initiated pauses.
    }
}

private void resumePlayback() {
    if (mState == PAUSED) {
        if (wasPausedByUser) {
            wasPausedByUser = false; // Reset during user-initiated resume.
        } else {
            return; // Prevent automatic resumes if paused by user.
        }
    }
    mMediaPlayer.start();
    mState = STARTED;
}

//<End of snippet n. 0>