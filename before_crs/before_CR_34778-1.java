/*use front camera if rear isn't defined.

fix issue cts fails if a device has a front camera without rear camera.
-use front camera instead of rear when rear camera isn't declared
 in feature definitions.
-skip a test case for rear camera only if rear isn't declared.

Signed-off-by: fujita <fujita@brilliantservice.co.jp>*/
//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/CamcorderProfileTest.java b/tests/tests/media/src/android/media/cts/CamcorderProfileTest.java
//Synthetic comment -- index 990ab9b..08c3280 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.media.CamcorderProfile;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.List;

//Synthetic comment -- @@ -70,6 +71,10 @@
)
})
public void testGet() {
int nCamera = Camera.getNumberOfCameras();
if (nCamera != 0) {
CamcorderProfile lowProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);








//Synthetic comment -- diff --git a/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java b/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java
//Synthetic comment -- index fd4e3b3..82c6ccb 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package android.mediastress.cts;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
//Synthetic comment -- @@ -50,6 +52,7 @@
private final RecorderErrorCallback mRecorderErrorCallback = new RecorderErrorCallback();
private final static int WAIT_TIMEOUT = 10000;

private MediaRecorder mRecorder;
private Camera mCamera;
private Thread mLooperThread;
//Synthetic comment -- @@ -74,6 +77,7 @@
}

protected void setUp() throws Exception {
int cameraId = 0;
CamcorderProfile profile = CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_HIGH);
mVideoEncoder = profile.videoCodec;
//Synthetic comment -- @@ -166,11 +170,16 @@
Log.v(TAG, "Start preview");
output.write("No of loop: ");

for (int i = 0; i< NUMBER_OF_CAMERA_STRESS_LOOPS; i++) {
runOnLooper(new Runnable() {
@Override
public void run() {
                    mCamera = Camera.open();
}
});
mCamera.setErrorCallback(mCameraErrorCallback);
//Synthetic comment -- @@ -248,11 +257,16 @@

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







