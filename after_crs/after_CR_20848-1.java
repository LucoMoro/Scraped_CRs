/*Fix Broken MediaRecorderTest#testSetCamera

Bug 3188260

The test needs to unlock the camera before passing it to the
MediaRecorder. This is apparently normal as seen in
VideoCamera.java in the Camera project.

Change-Id:Iecc55a2e39929c3637ff5eed805a75fcf5568ca1*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaRecorderTest.java b/tests/tests/media/src/android/media/cts/MediaRecorderTest.java
//Synthetic comment -- index db9c5be..a9e1c33 100644

//Synthetic comment -- @@ -15,7 +15,6 @@
*/
package android.media.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -27,6 +26,7 @@
import android.media.MediaRecorder.OnInfoListener;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.Surface;

import java.io.File;
//Synthetic comment -- @@ -89,6 +89,7 @@
}
if (mCamera != null)  {
mCamera.release();
            mCamera = null;
}
super.tearDown();
}
//Synthetic comment -- @@ -159,10 +160,10 @@
method = "setCamera",
args = {Camera.class}
)
    @UiThreadTest
public void testSetCamera() throws Exception {
mCamera = Camera.open();
        mCamera.unlock();
mMediaRecorder.setCamera(mCamera);
mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);







