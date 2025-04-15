/*Playback in SoundRecorder interrupted by screen timeout.

The same wakeLock as is used for recording is also applied for playback
to be able to preview/play the entire recorded sound without the preview
being interrupted.

Change-Id:I17f9f89bc98cf0d8be8b0f07b8d67ee240af1409*/




//Synthetic comment -- diff --git a/src/com/android/soundrecorder/SoundRecorder.java b/src/com/android/soundrecorder/SoundRecorder.java
//Synthetic comment -- index 0fa48e7..f5f5716 100644

//Synthetic comment -- @@ -811,10 +811,7 @@
if (state == Recorder.PLAYING_STATE || state == Recorder.RECORDING_STATE) {
mSampleInterrupted = false;
mErrorUiMessage = null;
            mWakeLock.acquire(); // we don't want to go to sleep while recording or playing
} else {
if (mWakeLock.isHeld())
mWakeLock.release();







