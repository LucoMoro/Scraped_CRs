//<Beginning of snippet n. 0>


if (mMediaController != null) {
    mMediaController.setEnabled(true);
}

// Removed width/height assignment on prepare
// mVideoWidth = mp.getVideoWidth();
// mVideoHeight = mp.getVideoHeight();

int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
if (seekToPosition != 0) {
    // Add fallback mechanism to retain last valid dimensions or set default values
    if (mVideoWidth <= 0 || mVideoHeight <= 0) {
        mVideoWidth = DEFAULT_VIDEO_WIDTH; // example default value
        mVideoHeight = DEFAULT_VIDEO_HEIGHT; // example default value
    }
}

// Ensure layout is updated after dimension changes during screen on/off transitions
updateVideoViewDimensions();

//<End of snippet n. 0>