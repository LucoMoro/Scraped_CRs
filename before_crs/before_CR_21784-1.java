/*CTS: Fixes for android.hardware.cts.CameraTest#testPreviewFpsRangeByCamera

- When variable framerate is active and depending on the light
  conditions, the framerate can get very low. This should be
  taken in to account during the process of discarding the
  measurements and leave just enough data. This way the checks
  and calculations that follow can proceed without any issues.

Change-Id:I156760d893778f7db0b876d943b67acb99754bbcSigned-off-by: Emilian Peev <epeev@mm-sol.com>*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index ef8490d..351c858 100644

//Synthetic comment -- @@ -1595,7 +1595,7 @@
Iterator<Long> it = mFrames.iterator();
while(it.hasNext()) {
long time = it.next();
                if (arrivalTime - time > 1000 && mFrames.size() > 1) {
it.remove();
} else {
break;







