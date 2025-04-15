/*Consider one front camera case

Some devices only have one front-facing camera. Using open() directly
will cause NPE. If there is only one camera available, always use the
first one, not the back-facing one.

Change-Id:Idfe06a30c69ae0547551f902da3d60906c057022*/
//Synthetic comment -- diff --git a/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java b/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java
//Synthetic comment -- index fd4e3b3..8aa8568 100644

//Synthetic comment -- @@ -69,6 +69,8 @@
private boolean mRemoveVideo = true;
private int mRecordDuration = 5000;

public MediaRecorderStressTest() {
super(MediaFrameworkTest.class);
}
//Synthetic comment -- @@ -82,6 +84,7 @@
mVideoWidth = profile.videoFrameWidth;
mVideoHeight = profile.videoFrameHeight;
mBitRate = profile.videoBitRate;

final Semaphore sem = new Semaphore(0);
mLooperThread = new Thread() {
//Synthetic comment -- @@ -170,7 +173,10 @@
runOnLooper(new Runnable() {
@Override
public void run() {
                    mCamera = Camera.open();
}
});
mCamera.setErrorCallback(mCameraErrorCallback);
//Synthetic comment -- @@ -248,11 +254,15 @@

Log.v(TAG, "Start preview");
output.write("No of loop: ");
for (int i = 0; i < NUMBER_OF_SWTICHING_LOOPS_BW_CAMERA_AND_RECORDER; i++) {
runOnLooper(new Runnable() {
@Override
public void run() {
                    mCamera = Camera.open();
}
});
mCamera.setErrorCallback(mCameraErrorCallback);







