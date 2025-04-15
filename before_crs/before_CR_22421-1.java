/*Adding test case of VideoSize in MediaRecorderTest.

This test case it only tests image which is in QCIF,

so HWs which does not support QCIF will return error.

I modified to add multiple sizes,

and if designated size is supported in HW it will not return error.*/
//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaRecorderTest.java b/tests/tests/media/src/android/media/cts/MediaRecorderTest.java
//Synthetic comment -- index ead3d62..dd973cb 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import dalvik.annotation.TestTargets;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
//Synthetic comment -- @@ -32,6 +33,7 @@
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;

@TestTargetClass(MediaRecorder.class)
public class MediaRecorderTest extends ActivityInstrumentationTestCase2<MediaStubActivity> {
//Synthetic comment -- @@ -50,6 +52,11 @@
private File mOutFile2;
private Camera mCamera;

/*
* InstrumentationTestRunner.onStart() calls Looper.prepare(), which creates a looper
* for the current thread. However, since we don't actually call loop() in the test,
//Synthetic comment -- @@ -154,7 +161,10 @@
mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
mMediaRecorder.setVideoFrameRate(FRAME_RATE);
        mMediaRecorder.setVideoSize(VIDEO_WIDTH, VIDEO_HEIGHT);
mMediaRecorder.setPreviewDisplay(getActivity().getSurfaceHolder().getSurface());
mMediaRecorder.prepare();
mMediaRecorder.start();
//Synthetic comment -- @@ -263,7 +273,10 @@
mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
mMediaRecorder.setPreviewDisplay(getActivity().getSurfaceHolder().getSurface());
mMediaRecorder.setVideoFrameRate(FRAME_RATE);
        mMediaRecorder.setVideoSize(VIDEO_WIDTH, VIDEO_HEIGHT);
FileOutputStream fos = new FileOutputStream(OUTPUT_PATH2);
FileDescriptor fd = fos.getFD();
mMediaRecorder.setOutputFile(fd);
//Synthetic comment -- @@ -459,4 +472,21 @@
assertTrue(outFile.length() < 1.1 * maxFileSize);
assertTrue(outFile.length() > 0);
}
}







