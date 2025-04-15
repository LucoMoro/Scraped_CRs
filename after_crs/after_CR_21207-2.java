/*Using different filenames for testRecorderVideo and testRecorderCamera.

There are some cases where these failures will be caused due to same filename being used in these test cases.

To avoid this, I changed so that different filenames will be used for testRecorderVideo and testRecorderCamera.*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaRecorderTest.java b/tests/tests/media/src/android/media/cts/MediaRecorderTest.java
//Synthetic comment -- index e5dc1ff..eeeab75 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
public class MediaRecorderTest extends ActivityInstrumentationTestCase2<MediaStubActivity> {

private final String OUTPUT_PATH;
    private final String OUTPUT_PATH2;
private static final int RECORD_TIME = 3000;
private static final int VIDEO_WIDTH = 320;
private static final int VIDEO_HEIGHT = 240;
//Synthetic comment -- @@ -46,6 +47,7 @@
private boolean mOnInfoCalled;
private boolean mOnErrorCalled;
private File mOutFile;
    private File mOutFile2;
private Camera mCamera;

/*
//Synthetic comment -- @@ -61,11 +63,14 @@
super("com.android.cts.stub", MediaStubActivity.class);
OUTPUT_PATH = new File(Environment.getExternalStorageDirectory(),
"record.out").getAbsolutePath();
        OUTPUT_PATH2 = new File(Environment.getExternalStorageDirectory(),
                "record2.out").getAbsolutePath();
}

@Override
protected void setUp() throws Exception {
mOutFile = new File(OUTPUT_PATH);
        mOutFile2 = new File(OUTPUT_PATH2);
mMediaRecorder.reset();
mMediaRecorder.setOutputFile(OUTPUT_PATH);
mMediaRecorder.setOnInfoListener(new OnInfoListener() {
//Synthetic comment -- @@ -87,6 +92,9 @@
if (mOutFile != null && mOutFile.exists()) {
mOutFile.delete();
}
        if (mOutFile2 != null && mOutFile2.exists()) {
            mOutFile2.delete();
        }
if (mCamera != null)  {
mCamera.release();
}
//Synthetic comment -- @@ -250,15 +258,16 @@
public void testRecorderVideo() throws Exception {
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
        recordMedia2(maxFileSize);
}

@TestTargets({
//Synthetic comment -- @@ -317,6 +326,7 @@
mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
assertEquals(0, mMediaRecorder.getMaxAmplitude());
mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setOutputFile(OUTPUT_PATH);
mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
recordMedia(MAX_FILE_SIZE);
}
//Synthetic comment -- @@ -449,4 +459,17 @@
assertTrue(mOutFile.length() > 0);
}

    private void recordMedia2(long maxFileSize) throws Exception {
        mMediaRecorder.setMaxFileSize(maxFileSize);
        mMediaRecorder.prepare();
        mMediaRecorder.start();
        Thread.sleep(RECORD_TIME);
        mMediaRecorder.stop();
        assertTrue(mOutFile2.exists());
        // The max file size is always guaranteed.
        // We just make sure that the margin is not too big
        assertTrue(mOutFile2.length() < 1.1 * maxFileSize);
        assertTrue(mOutFile2.length() > 0);
    }

}







