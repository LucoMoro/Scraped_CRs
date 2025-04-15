/*Adding the delay time of 1000ms within testOpenGlEsVersion

What is tested in this test case is to check whether OpenGL version acquried from system.prop is same as the version acquired from OpenGL library.

This test initializes the OpenGL within the thread when creating activity.
When this test is performed before initialization is finished as this initialization is done by eglInitialize() in native level from setup(), OpenGL version can not be recognized properly, resulting in fail.

To avoid this, I added sleep of 1000ms before starting OpenGL version check so that it finishes initialization completely.*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/opengl/cts/OpenGlEsVersionTest.java b/tests/tests/graphics/src/android/opengl/cts/OpenGlEsVersionTest.java
//Synthetic comment -- index f1acd87..2f6e273 100644

//Synthetic comment -- @@ -55,6 +55,7 @@
}

public void testOpenGlEsVersion() throws InterruptedException {
int detectedVersion = getDetectedVersion();
int reportedVersion = getVersionFromActivityManager(mActivity);








