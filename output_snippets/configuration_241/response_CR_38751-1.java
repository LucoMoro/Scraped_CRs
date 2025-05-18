//<Beginning of snippet n. 0>
if (mMediaController != null) {
    mMediaController.setEnabled(true);
}

if (mp != null) {
    mp.setOnPreparedListener(mediaPlayer -> {
        retrieveVideoDimensions();

        if (mVideoWidth > 0 && mVideoHeight > 0) {
            // Proceed with layout update logic here if needed
        } else {
            // Log invalid dimensions or handle fallback
            Log.e("VideoPlayer", "Invalid video dimensions: width=" + mVideoWidth + ", height=" + mVideoHeight);
            revertToDefaultLayout();
        }
    });
}

int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
if (seekToPosition != 0) {
    // Seek logic here
}

private void retrieveVideoDimensions() {
    mVideoWidth = mp.getVideoWidth();
    mVideoHeight = mp.getVideoHeight();
}

private void revertToDefaultLayout() {
    // Logic to revert to a default layout
}

// Handle screen on/off lifecycle events to reassess video dimensions if needed
@Override
protected void onResume() {
    super.onResume();
    if (mp != null) {
        retrieveVideoDimensions();
    }
}
//<End of snippet n. 0>