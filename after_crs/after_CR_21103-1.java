/*Camera: Fix unstable test result on testpreviewcallback

The callback is called at unexpected timing.
To avoid this issue, we need to wait for a while before going
to the next test.

Change-Id:I1d17cbd4c6678eca399829828119c85ba805b243*/




//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index a261914..4200e2b 100644

//Synthetic comment -- @@ -409,6 +409,13 @@
waitForPreviewDone();
assertTrue(mPreviewCallbackResult);
mCamera.stopPreview();
            try {
                // Wait for a while to throw away the remaining preview frames.
                Thread.sleep(1000);
            } catch(Exception e) {
                // ignore
            }
            mPreviewDone.close();
}
terminateMessageLooper();
}







