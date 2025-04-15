/*add test on the Intent to start Camera.

Change-Id:I95f29dd1f6c6c9b1ef4e103fcfd599bf31a8cb55*/




//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java b/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java
//Synthetic comment -- index bd8c2604..45aff4c 100644

//Synthetic comment -- @@ -171,4 +171,30 @@
assertCanBeHandled(intent);
}
}

    /**
     * Test start camera by intent
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "Intent",
        args = {java.lang.String.class}
    )
    public void testCamera() {
        PackageManager packageManager = mContext.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                || packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
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







