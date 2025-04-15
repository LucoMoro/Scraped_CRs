/*add test case to test the Intent starting the camera.
Signed-off-by: gzhhong <gzhhong@gmail.com>

Change-Id:I8f6570391dd1bf7e3a18631df4d59e6402c370da*/




//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java b/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java
//Synthetic comment -- index bd8c2604..7a6e16e 100644

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







