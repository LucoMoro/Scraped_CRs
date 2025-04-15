/*Improve SystemFeaturesTest#testCameraFeatures

Incorporate suggestion on improving the reliability of the
auto focus feature check.

Bug 3166146

Change-Id:Ic513e69386be54cb4a8405c4a8f35d9fa6a6e902*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java b/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java
//Synthetic comment -- index c978919..ac3d03a 100644

//Synthetic comment -- @@ -113,7 +113,7 @@
assertAvailable(PackageManager.FEATURE_CAMERA);

Camera.Parameters params = camera.getParameters();
                if (!Parameters.FOCUS_MODE_FIXED.equals(params.getFocusMode())) {
assertAvailable(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
} else {
assertNotAvailable(PackageManager.FEATURE_CAMERA_AUTOFOCUS);







