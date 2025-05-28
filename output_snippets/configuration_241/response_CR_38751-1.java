//<Beginning of snippet n. 0>
if (mMediaController != null) {
    mMediaController.setEnabled(true);
}

if (mp != null) {
    mp.setOnPreparedListener(mediaPlayer -> {
        mVideoWidth = mp.getVideoWidth();
        mVideoHeight = mp.getVideoHeight();
        
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            // Proceed with layout update logic here if needed
        } else {
            // Log invalid dimensions or handle fallback
        }
    });
}

int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
if (seekToPosition != 0) {
//<End of snippet n. 0>