/*The CTS test case, android.media.cts.CamcorderProfileTest#testGet, should consider the device without back-facing camera.

The standard API Camera.open() returns null if the device does not have a back-facing camera.
Add the protection to avoid the null point exception.
- If camera is null, return out the test.
- Make sure camera is not null before get parameters from camera.

Change-Id:Ia5f9684482de3d4453feb3dbc265a7221e2e9839*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/CamcorderProfileTest.java b/tests/tests/media/src/android/media/cts/CamcorderProfileTest.java
//Synthetic comment -- index 0587f88..b28733a 100644

//Synthetic comment -- @@ -195,6 +195,11 @@

final List<Size> videoSizes = getSupportedVideoSizes(cameraId);

        if (videoSizes == null && cameraId == -1) {
            Log.v(TAG, "The device does not have a back-facing camera");
            return;
        }

CamcorderProfile lowProfile =
getWithOptionalId(CamcorderProfile.QUALITY_LOW, cameraId);
CamcorderProfile highProfile =
//Synthetic comment -- @@ -256,6 +261,12 @@

private List<Size> getSupportedVideoSizes(int cameraId) {
Camera camera = (cameraId == -1)? Camera.open(): Camera.open(cameraId);

        if (camera == null && cameraId == -1) {
            return null;
        }
        assertNotNull(camera);

Parameters parameters = camera.getParameters();
List<Size> videoSizes = parameters.getSupportedVideoSizes();
if (videoSizes == null) {







