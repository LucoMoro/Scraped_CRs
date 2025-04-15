/*add test case to test the Intent starting the camera.

Change-Id:I804a6583f3ee36f11e5caf2b3fdcaac36ce7cd66*/




//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java b/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java
//Synthetic comment -- index bd8c2604..412551c 100644

//Synthetic comment -- @@ -171,4 +171,18 @@
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
        Intent intent = new Intent();
        intent.setAction(android.provider.MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        assertCanBeHandled(intent);
    }
}







