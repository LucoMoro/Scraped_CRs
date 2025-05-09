/*Remove Broken NoActivityRelatedPermissionTest

Bug 1910487

Change-Id:I8dfa05222713e4d4f6a11aa42567510b06859a07*/
//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoActivityRelatedPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoActivityRelatedPermissionTest.java
//Synthetic comment -- index 39da4b6..2d11883 100644

//Synthetic comment -- @@ -16,19 +16,14 @@

package android.permission.cts;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestTargetClass;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.Suppress;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;

/**
* Verify the Activity related operations require specific permissions.
//Synthetic comment -- @@ -50,47 +45,6 @@
}

/**
     * Verify that adding window of different types in Window Manager requires permissions.
     * <p>Requires Permission:
     *   {@link android.Manifest.permission#SYSTEM_ALERT_WINDOW}.
     */
    @UiThreadTest
    @MediumTest
    @Suppress
    @BrokenTest("This test passes, but crashes the UI thread later on. See issues 1909470, 1910487")
    public void testSystemAlertWindow() {
        final int[] types = new int[] {
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.TYPE_PRIORITY_PHONE,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
            };

        AlertDialog dialog = (AlertDialog) (mActivity.getDialog());
        // Use normal window type will success
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION);
        dialog.show();

        // Test special window types which need to be check SYSTEM_ALERT_WINDOW
        // permission.
        for (int i = 0; i < types.length; i++) {
            dialog = (AlertDialog) (mActivity.getDialog());
            dialog.getWindow().setType(types[i]);
            try {
                dialog.show();
                // This throws an exception as expected, but only after already adding
                // a new view to the view hierarchy. This later results in a NullPointerException
                // when the activity gets destroyed. Since that crashes the UI thread and causes
                // test runs to abort, this test is currently excluded.
                fail("Add dialog to Window Manager did not throw BadTokenException as expected");
            } catch (BadTokenException e) {
                // Expected
            }
        }
    }

    /**
* Verify that setting Activity's persistent attribute requires permissions.
* <p>Requires Permission:
*   {@link android.Manifest.permission#PERSISTENT_ACTIVITY}.







