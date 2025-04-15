/*Support devices with only front-facing camera.

If the device only supports a front-facing camera, the camera count will
be greater than zero, but Camera.open() will return null, causing this
test to crash.

Change-Id:I6e98181cc189930e2e2a1313b0d2a2d4c334349a*/




//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/Camera_SizeTest.java b/tests/tests/hardware/src/android/hardware/cts/Camera_SizeTest.java
//Synthetic comment -- index 028b819..e6b3780 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
)
public void testConstructor() {
Camera camera = Camera.open();
        if (camera == null) return;
Parameters parameters = camera.getParameters();

checkSize(parameters, WIDTH1, HEIGHT1);







