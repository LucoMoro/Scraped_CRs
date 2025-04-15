/*Checking physical cameras exist on the device for graphics.TextureView test.

Added handlings to pass testTextureViewActivity
with no camera devices.

Change-Id:Id5bd4ad105fd255e04d1dc820e8f3538259fe944*/




//Synthetic comment -- diff --git a/tests/tests/graphics2/src/android/graphics2/cts/TextureViewTest.java b/tests/tests/graphics2/src/android/graphics2/cts/TextureViewTest.java
//Synthetic comment -- index 932a5d5..c84919a 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package android.graphics2.cts;

import android.graphics2.cts.TextureViewCameraActivity;
import android.hardware.Camera;
import android.test.ActivityInstrumentationTestCase2;


//Synthetic comment -- @@ -30,10 +31,16 @@
@Override
protected void setUp() throws Exception {
super.setUp();
        if (Camera.getNumberOfCameras() < 1) {
            return;
        }
mActivity = getActivity();
}

public void testTextureViewActivity() throws InterruptedException {
        if (Camera.getNumberOfCameras() < 1) {
            return;
        }
assertTrue(mActivity.waitForCompletion(WAIT_TIMEOUT_IN_SECS));
}








