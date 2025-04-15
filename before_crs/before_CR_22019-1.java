/*CTS: Fix fps calculation for android.hardware.cts.CameraTest#testPreviewFpsRangeByCamera

- FPS should be calculated as a moving average to avoid inaccuracies

- When variable framerate is active and depending on the light
  conditions, the framerate can get very low. This should be
  taken in to account during the process of discarding the
  measurements and leave just enough data. This way the checks
  and calculations that follow can proceed without any issues.

Change-Id:I464785bb768af76f4975b16be4cbaa17a562b95aSigned-off-by: Sundar Raman <sunds@ti.com>*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index 9ed0d93..e784d3e 100644

//Synthetic comment -- @@ -87,6 +87,9 @@
private final ConditionVariable mFocusDone = new ConditionVariable();
private final ConditionVariable mSnapshotDone = new ConditionVariable();

Camera mCamera;

public CameraTest() {
//Synthetic comment -- @@ -248,7 +251,7 @@
boolean result = mFocusDone.block(WAIT_FOR_FOCUS_TO_COMPLETE);
if (!result) {
// timeout could be expected or unexpected. The caller will decide.
            Log.v(TAG, "waitForFocusDone: timeout");
}
mFocusDone.close();
return result;
//Synthetic comment -- @@ -1605,7 +1608,7 @@

// Start the test after one second.
if (arrivalTime - firstFrameArrivalTime > 1000) {
                assertTrue(mFrames.size() >= 2);

// Check the frame interval and fps. The interval check
// considers the time variance passing frames from the camera
//Synthetic comment -- @@ -1630,12 +1633,21 @@
double avgInterval = (double)(arrivalTime - mFrames.get(0))
/ mFrames.size();
double fps = 1000.0 / avgInterval;
                assertTrue("Actual fps (" + fps + ") should be larger than " +
"min fps (" + mMinFps + ")",
                           fps >= mMinFps * (1 - fpsMargin));
                assertTrue("Actual fps (" + fps + ") should be smaller than " +
"max fps (" + mMaxFps + ")",
                           fps <= mMaxFps * (1 + fpsMargin));
}
// Add the arrival time of this frame to the list.
mFrames.add(arrivalTime);







