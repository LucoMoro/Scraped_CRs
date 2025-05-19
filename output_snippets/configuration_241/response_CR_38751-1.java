//<Beginning of snippet n. 0>
if (mMediaController != null) {
    mMediaController.setEnabled(true);
}

if (mp.isPlaying() || mp.isPrepared()) {
    fetchVideoDimensions();
}

int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
if (seekToPosition != 0) {
    // Your existing seek logic
}

private void fetchVideoDimensions() {
    if (mp.isPrepared()) {
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();

        if (videoWidth > 0 && videoHeight > 0) {
            mVideoWidth = videoWidth;
            mVideoHeight = videoHeight;
        } else {
            if (mVideoWidth <= 0 && mVideoHeight <= 0) {
                mVideoWidth = DEFAULT_VIDEO_WIDTH;
                mVideoHeight = DEFAULT_VIDEO_HEIGHT;
            } else {
                mVideoWidth = Math.max(mVideoWidth, DEFAULT_VIDEO_WIDTH);
                mVideoHeight = Math.max(mVideoHeight, DEFAULT_VIDEO_HEIGHT);
            }
            Log.e("VideoDimensions", "Invalid video dimensions retrieved. Fallback to defaults.");
        }
    }
}

@Override
protected void onResume() {
    super.onResume();
    handleScreenOn();
}

@Override
protected void onPause() {
    super.onPause();
    handleScreenOff();
}

private void handleScreenOn() {
    if (mp.isPrepared()) {
        fetchVideoDimensions();
    }
}

private void handleScreenOff() {
    // Logic to handle screen off event
    // Retaining last known valid dimensions instead of setting to zero
}
//<End of snippet n. 0>