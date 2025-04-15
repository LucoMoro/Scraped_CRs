/*For the device which has no camera,
testStressRecorder() and testStressRecordVideoAndPlayback() should be skipped.

I modified so that these test cases are skipped if the device has no camera.*/
//Synthetic comment -- diff --git a/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java b/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java
//Synthetic comment -- index 2891c4b..3677641 100644

//Synthetic comment -- @@ -226,9 +226,18 @@
runOnLooper(new Runnable() {
@Override
public void run() {
                    mRecorder = new MediaRecorder();
}
});
Log.v(TAG, "counter = " + i);
filename = OUTPUT_FILE + i + OUTPUT_FILE_EXT;
Log.v(TAG, filename);
//Synthetic comment -- @@ -368,9 +377,18 @@
runOnLooper(new Runnable() {
@Override
public void run() {
                    mRecorder = new MediaRecorder();
}
});
Log.v(TAG, "iterations : " + i);
Log.v(TAG, "videoEncoder : " + mVideoEncoder);
Log.v(TAG, "audioEncoder : " + mAudioEncoder);







