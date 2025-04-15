/*make camera optional

Change-Id:I7610c5a82f176e7176bf815ceba5d13ca33de48d*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaRecorderTest.java b/tests/tests/media/src/android/media/cts/MediaRecorderTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index ead3d62..f9c0b41

//Synthetic comment -- @@ -20,7 +20,10 @@
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
//Synthetic comment -- @@ -150,17 +153,23 @@
)
})
public void testRecorderCamera() throws Exception {
        
        boolean hasCamera =getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
        
        if(hasCamera){
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            mMediaRecorder.setVideoFrameRate(FRAME_RATE);
            mMediaRecorder.setVideoSize(VIDEO_WIDTH, VIDEO_HEIGHT);
            mMediaRecorder.setPreviewDisplay(getActivity().getSurfaceHolder().getSurface());
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            Thread.sleep(1000);
            mMediaRecorder.stop();
            checkOutputExist();
        }
}

@TestTargetNew(
//Synthetic comment -- @@ -257,18 +266,23 @@
)
})
public void testRecorderVideo() throws Exception {
        boolean hasCamera =getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
        
        if(hasCamera){
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mMediaRecorder.setOutputFile(OUTPUT_PATH2);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            mMediaRecorder.setPreviewDisplay(getActivity().getSurfaceHolder().getSurface());
            mMediaRecorder.setVideoFrameRate(FRAME_RATE);
            mMediaRecorder.setVideoSize(VIDEO_WIDTH, VIDEO_HEIGHT);
            FileOutputStream fos = new FileOutputStream(OUTPUT_PATH2);
            FileDescriptor fd = fos.getFD();
            mMediaRecorder.setOutputFile(fd);
            long maxFileSize = MAX_FILE_SIZE * 10;
            recordMedia(maxFileSize, mOutFile2);
        }
}

@TestTargets({







