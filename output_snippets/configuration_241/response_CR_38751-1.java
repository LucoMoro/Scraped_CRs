//<Beginning of snippet n. 0>
if (mp.getVideoWidth() > 0 && mp.getVideoHeight() > 0) {
    mVideoWidth = mp.getVideoWidth();
    mVideoHeight = mp.getVideoHeight();

    if (mMediaController != null) {
        mMediaController.setEnabled(true);
    }
} else {
    // Fallback approach to handle invalid dimensions
    mVideoWidth = 640; // Default width
    mVideoHeight = 480; // Default height
}

int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
if (seekToPosition != 0) {
//<End of snippet n. 0>