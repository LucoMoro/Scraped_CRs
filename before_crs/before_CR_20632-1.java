/*Fix Bad Merge

My merging of Froyo changes reintroduced a test in
NoActivityRelatedPermissionTest that had already been
removed.

Change-Id:I103e615be7b32196a6eab0ad07016002fa25207c*/
//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoActivityRelatedPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoActivityRelatedPermissionTest.java
//Synthetic comment -- index 2d11883..a6454242 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import android.app.ActivityManager;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;

/**
//Synthetic comment -- @@ -45,22 +44,6 @@
}

/**
     * Verify that setting Activity's persistent attribute requires permissions.
     * <p>Requires Permission:
     *   {@link android.Manifest.permission#PERSISTENT_ACTIVITY}.
     */
    @UiThreadTest
    @MediumTest
    public void testSetPersistent() {
        try {
            mActivity.setPersistent(true);
            fail("Activity.setPersistent() did not throw SecurityException as expected");
        } catch (SecurityException e) {
            // Expected
        }
    }

    /**
* Verify that get task requires permissions.
* <p>Requires Permission:
*   {@link android.Manifest.permission#GET_TASKS}







