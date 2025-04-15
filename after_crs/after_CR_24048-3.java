/*add test case to test the Intent starting the camera.

Change-Id:I1c8e1f0947ff17bb76dbb5d0a70b2315a27b7e4f*/




//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java b/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java
//Synthetic comment -- index bd8c2604..a877415 100644

//Synthetic comment -- @@ -171,4 +171,30 @@
assertCanBeHandled(intent);
}
}

    /**
     * Test start camera by intent INTENT_ACTION_STILL_IMAGE_CAMERA
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "Intent",
        args = {}
    )
    public void testCamera() {
        PackageManager packageManager = mContext.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) || 
            packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            assertCanBeHandled(intent);

            intent.setAction(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
            assertCanBeHandled(intent);

            intent.setAction(android.provider.MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            assertCanBeHandled(intent);

            intent.setAction(android.provider.MediaStore.INTENT_ACTION_VIDEO_CAMERA);
            assertCanBeHandled(intent);
        }
    }
}







