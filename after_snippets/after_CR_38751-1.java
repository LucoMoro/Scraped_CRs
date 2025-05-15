
//<Beginning of snippet n. 0>


if (mMediaController != null) {
mMediaController.setEnabled(true);
}

            //nuplayer cannot get video w,h on prepare. keep the original value especially for suspend/resume.
            int videoWidth = mp.getVideoWidth();
            int videoHeight = mp.getVideoHeight();
            if (videoWidth != 0 && videoHeight != 0) {
                mVideoWidth = videoWidth;
                mVideoHeight = videoHeight;
            }

int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
if (seekToPosition != 0) {

//<End of snippet n. 0>








