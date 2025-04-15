/*Added sleep in recordMedia
In this test case, testRecorderVideo is executed after testRecorderCamera.
However, there are cases where "record.out" is not closed within testRecorderCarmera.
So for the solution, I added sleep in recordMedia within testRecorderVideo to surely close "record.out" before calling prepare().
I also added sleep after prepare() so that it prepare() is completely closed.*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaRecorderTest.java b/tests/tests/media/src/android/media/cts/MediaRecorderTest.java
//Synthetic comment -- index e5dc1ff..d5d95dc 100644

//Synthetic comment -- @@ -438,11 +438,13 @@

private void recordMedia(long maxFileSize) throws Exception {
mMediaRecorder.setMaxFileSize(maxFileSize);
        Thread.sleep(1000);
mMediaRecorder.prepare();
mMediaRecorder.start();
Thread.sleep(RECORD_TIME);
mMediaRecorder.stop();
assertTrue(mOutFile.exists());
        Thread.sleep(1000);
// The max file size is always guaranteed.
// We just make sure that the margin is not too big
assertTrue(mOutFile.length() < 1.1 * maxFileSize);







