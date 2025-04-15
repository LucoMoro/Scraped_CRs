/*Fixing race in android.hardware.cts.CameraTest#testZoom

Remove the checking process that compares "given zoom value
from callback" and "gotten zoom value via getZoom()".

Because there is a race condition in that the callback happens
and before the call to getZoom the zoom has already been updated.

When "onZoomChange()" is executed during smooth zoom,
the return value of "getZoom()" in checking process
won't correspond to the argument "value"
in case that the zoom has been already updated before the
call to it.

Change-Id:Ie9c277c09efb1510d1314b26652815d04a79be4f*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index 4200e2b..45bc87e 100644

//Synthetic comment -- @@ -1176,7 +1176,6 @@

public void onZoomChange(int value, boolean stopped, Camera camera) {
mValues.add(value);
            assertEquals(value, camera.getParameters().getZoom());
assertFalse(mStopped);
mStopped = stopped;
if (stopped) {







