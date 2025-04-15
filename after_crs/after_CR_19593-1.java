/*Remove Broken NoActivityRelatedPermissionTest

Bug 1910487

Change-Id:I8dfa05222713e4d4f6a11aa42567510b06859a07*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoActivityRelatedPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoActivityRelatedPermissionTest.java
//Synthetic comment -- index 39da4b6..2d11883 100644

//Synthetic comment -- @@ -16,19 +16,14 @@

package android.permission.cts;

import dalvik.annotation.TestTargetClass;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;

/**
* Verify the Activity related operations require specific permissions.
//Synthetic comment -- @@ -50,47 +45,6 @@
}

/**
* Verify that setting Activity's persistent attribute requires permissions.
* <p>Requires Permission:
*   {@link android.Manifest.permission#PERSISTENT_ACTIVITY}.







