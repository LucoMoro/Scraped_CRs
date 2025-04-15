/** comment : request for modification CTS API(testCameraFeatures()) in SystemFeaturesTest.java file*/




//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java b/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java
//Synthetic comment -- index 052a6ff..efcfa92 100755

//Synthetic comment -- @@ -80,8 +80,8 @@
assertNotAvailable(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
}

                if ((params.getFlashMode() != null)
                    && (!Parameters.FLASH_MODE_OFF.equals(params.getFlashMode()))) {
assertAvailable(PackageManager.FEATURE_CAMERA_FLASH);
} else {
assertNotAvailable(PackageManager.FEATURE_CAMERA_FLASH);







