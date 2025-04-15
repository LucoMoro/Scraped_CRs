/*Allow some time for recording in testVideoSnapshot

- Stagefright design is to return an error if it hasn't received sufficient
encoded frames when stop is called.  Sometimes, the calls in testVideoSnapshot
go faster than the startTimeOffsetUs resulting in no frames captured and
a failed test case.
- The sleep is added in consistent to other media recording test cases.

Change-Id:Ic15518a47ff04a886c0c51974b53b8b8e1081b09*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index 939a6c2..f79230e 100755

//Synthetic comment -- @@ -2747,6 +2747,7 @@
subtestTakePictureByCamera();
testJpegExifByCamera(true);
testJpegThumbnailSizeByCamera(true);
recorder.stop();
} finally {
recorder.release();







