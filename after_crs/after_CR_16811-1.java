/*Fix CameraTest#testAccessParameters Assertion

Issue 10800

Use a float assertEquals and swap expected and actual parameters
around.

Change-Id:I791947c78471d8fdb992843ca66e09333ba0ddec*/




//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index ba298c2..649b824 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package android.hardware.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -38,8 +37,8 @@
import android.os.Environment;
import android.os.Looper;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.SurfaceHolder;

//Synthetic comment -- @@ -633,7 +632,7 @@
int min = parameters.getMinExposureCompensation();
float step = parameters.getExposureCompensationStep();
if (max == 0 && min == 0) {
            assertEquals(0, step, 0.000001);
return;
}
assertTrue(step > 0);







