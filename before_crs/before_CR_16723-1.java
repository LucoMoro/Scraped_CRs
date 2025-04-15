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
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Surface;
//Synthetic comment -- @@ -334,6 +337,170 @@
),
@TestTargetNew(
level = TestLevel.COMPLETE,
method = "setAudioEncoder",
args = {int.class}
),







