/*Test that MediaRecorder can use a FileDescriptor from a LocalSocket

LocalSocket can be used to generate a FileDecriptor. These tests ensure that this FileDescriptor
is compatible with MediaRecorder#setOutputFile(FileDescriptor fd).

Change-Id:I5d9d47ad4b165cf2e865565e419ea3918b6b02b2*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaRecorderTest.java b/tests/tests/media/src/android/media/cts/MediaRecorderTest.java
//Synthetic comment -- index 6dddaaa..d5839c4 100644

//Synthetic comment -- @@ -25,6 +25,9 @@
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Surface;
//Synthetic comment -- @@ -334,6 +337,170 @@
),
@TestTargetNew(
level = TestLevel.COMPLETE,
            method = "setOutputFile",
            args = {FileDescriptor.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setOutputFormat",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setAudioEncoder",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setAudioSource",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "start",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "stop",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setMaxFileSize",
            args = {long.class}
        )
    })
    public void testRecorderWithLocalSocketAudio() throws Exception {
       mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
       mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
       mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

       String namespace = "MediaRecorderTest#testRecorderWithLocalSocketAudio";
       LocalSocket source = new LocalSocket();
       LocalServerSocket srv = new LocalServerSocket(namespace);
       source.connect(new LocalSocketAddress(namespace));
       LocalSocket sink = srv.accept();
       FileDescriptor fd = sink.getFileDescriptor();
        mMediaRecorder.setOutputFile(fd);

        mMediaRecorder.setMaxFileSize(MAX_FILE_SIZE);
        mMediaRecorder.prepare();
        mMediaRecorder.start();
       // we're just testing if the MediaRecorder will start(),
       // so there's no need to record for any amount of time
       // (also, there's nothing reading from the other end)
        mMediaRecorder.stop();

       source.close();
       sink.close();
       srv.close();
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "MediaRecorder",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "prepare",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setOutputFile",
            args = {FileDescriptor.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setOutputFormat",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setPreviewDisplay",
            args = {Surface.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setVideoEncoder",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setVideoFrameRate",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setVideoSize",
            args = {int.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setVideoSource",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "start",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "stop",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setMaxFileSize",
            args = {long.class}
        )
    })
    public void testRecorderWithLocalSocketVideo() throws Exception {
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        mMediaRecorder.setPreviewDisplay(getActivity().getSurfaceHolder().getSurface());
        mMediaRecorder.setVideoFrameRate(FRAME_RATE);
        mMediaRecorder.setVideoSize(VIDEO_WIDTH, VIDEO_HEIGHT);

       String namespace = "MediaRecorderTest#testRecorderWithLocalSocketVideo";
       LocalSocket source = new LocalSocket();
       LocalServerSocket srv = new LocalServerSocket(namespace);
       source.connect(new LocalSocketAddress(namespace));
       LocalSocket sink = srv.accept();
       FileDescriptor fd = sink.getFileDescriptor();
        mMediaRecorder.setOutputFile(fd);

        mMediaRecorder.setMaxFileSize(MAX_FILE_SIZE);
        mMediaRecorder.prepare();
        mMediaRecorder.start();
       // we're just testing if the MediaRecorder will start(),
       // so there's no need to record for any amount of time
       // (also, there's nothing reading from the other end)
        mMediaRecorder.stop();

       source.close();
       sink.close();
       srv.close();
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "MediaRecorder",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "prepare",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
method = "setAudioEncoder",
args = {int.class}
),







