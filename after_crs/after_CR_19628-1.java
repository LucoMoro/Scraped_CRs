/*CTS: 2 test cases fail in android.media related to camera

Solve problems with handsets that does not support the hard
coded preview resolution used in the tests.
The tests that fails are testRecorderCamera and testRecorderVideo.
This fix updates the test to ask the camera for the
lowest supported preview resolution and use this for the test.

Change-Id:Ie3efa4f5e579f90abe6a960aef509f338711c90e*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaRecorderTest.java b/tests/tests/media/src/android/media/cts/MediaRecorderTest.java
//Synthetic comment -- index db9c5be..09daa68 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import dalvik.annotation.TestTargets;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
//Synthetic comment -- @@ -32,6 +34,7 @@
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.util.List;

@TestTargetClass(MediaRecorder.class)
public class MediaRecorderTest extends ActivityInstrumentationTestCase2<MediaStubActivity> {
//Synthetic comment -- @@ -141,11 +144,15 @@
)
})
public void testRecorderCamera() throws Exception {
        Camera camera = Camera.open();
        Size size = getMinSupportedPreviewSize(camera);
        camera.release();

mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
mMediaRecorder.setVideoFrameRate(FRAME_RATE);
        mMediaRecorder.setVideoSize(size.width, size.height);
mMediaRecorder.setPreviewDisplay(getActivity().getSurfaceHolder().getSurface());
mMediaRecorder.prepare();
mMediaRecorder.start();
//Synthetic comment -- @@ -248,12 +255,16 @@
)
})
public void testRecorderVideo() throws Exception {
        Camera camera = Camera.open();
        Size size = getMinSupportedPreviewSize(camera);
        camera.release();

mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
mMediaRecorder.setPreviewDisplay(getActivity().getSurfaceHolder().getSurface());
mMediaRecorder.setVideoFrameRate(FRAME_RATE);
        mMediaRecorder.setVideoSize(size.width, size.height);
FileOutputStream fos = new FileOutputStream(OUTPUT_PATH);
FileDescriptor fd = fos.getFD();
mMediaRecorder.setOutputFile(fd);
//Synthetic comment -- @@ -436,6 +447,21 @@
assertFalse(mOnErrorCalled);
}

    private Size getMinSupportedPreviewSize(Camera camera) {
        List<Size> sizes = camera.getParameters().getSupportedPreviewSizes();
        if (sizes.size() == 0) {
            throw new IllegalArgumentException(
                "camera.getParameters().getSupportedPreviewSizes() returned no data");
        }
        Size result = sizes.get(0);
        for (Size s: sizes) {
            if (s.width < result.width) {
                result = s;
            }
        }
        return result;
    }

private void recordMedia(long maxFileSize) throws Exception {
mMediaRecorder.setMaxFileSize(maxFileSize);
mMediaRecorder.prepare();







