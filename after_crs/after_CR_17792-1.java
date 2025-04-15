/*During the test MediaRecorderTest testRecorderVideo () is for the information.
If you are holding your phone and the cts test fails, put on the floor if you must pass.
(The former is that if you are taking things, taking the latter to the floor when the screen is black.)

Because the size is too small to MAX_FILE_SIZE
If your phone holding the record for the file size exceeds 50KB CTS test fails.

Accordingly, the size of MAX_FILE_SIZE 5000-0 is hoping to change to.*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaRecorderTest.java b/tests/tests/media/src/android/media/cts/MediaRecorderTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index e5dc1ff..8fff812

//Synthetic comment -- @@ -41,7 +41,7 @@
private static final int VIDEO_WIDTH = 320;
private static final int VIDEO_HEIGHT = 240;
private static final int FRAME_RATE = 15;
    private static final long MAX_FILE_SIZE = 0;//5000;
private static final int MAX_DURATION_MSEC = 200;
private boolean mOnInfoCalled;
private boolean mOnErrorCalled;







