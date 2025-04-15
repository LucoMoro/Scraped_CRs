/*CTS: Fixes for android.hardware.cts.CameraTest#testCancelAutofocus

- setPreviewDisplay() was omitted after release().
release() disconnects and releases the Camera object resources.
If a camera uses an overlay for preview, the overlay object is
also destroyed when release() is used. At the same time,
the camera documentation states that a call to either
setPreviewDisplay() or setPreviewTexture() is necessary in
order to start the preview.

Change-Id:Ic77e8a5ea952414c610850241be9e09018b02b20Signed-off-by: Julian Shandorov <jshandorov@mm-sol.com>*/




//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index 351c858..9ed0d93 100644

//Synthetic comment -- @@ -1307,6 +1307,7 @@

// Ensure the camera can be opened if release is called right after AF.
mCamera = Camera.open(cameraId);
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
mCamera.startPreview();
mCamera.autoFocus(mAutoFocusCallback);
mCamera.release();







