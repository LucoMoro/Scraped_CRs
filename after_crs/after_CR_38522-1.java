/*Fix CTS cases ApplicationTest and DialogTest.

The device which supports a fixed-orientation screen like a set-top box doesn't need to change the screen orientation.
The test case should be skipped if the device doesn't support both of portrait and landscape orientation screens.

Change-Id:I13f452548b37724353b8663fa71919ecea77ca7f*/




//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/ApplicationTest.java b/tests/tests/app/src/android/app/cts/ApplicationTest.java
//Synthetic comment -- index 2dfb97d..35a5c74 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.test.InstrumentationTestCase;

/**
//Synthetic comment -- @@ -76,6 +77,12 @@
assertTrue(mockApp.isConstructorCalled);
assertTrue(mockApp.isOnCreateCalled);

        //skip if the device doesn't support both of portrait and landscape orientation screens.
        final PackageManager pm = targetContext.getPackageManager();
        if(!(pm.hasSystemFeature(PackageManager.FEATURE_SCREEN_LANDSCAPE)
                && pm.hasSystemFeature(PackageManager.FEATURE_SCREEN_PORTRAIT)))
            return;
			
runTestOnUiThread(new Runnable() {
public void run() {
OrientationTestUtils.toggleOrientation(activity);








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/DialogTest.java b/tests/tests/app/src/android/app/cts/DialogTest.java
//Synthetic comment -- index 37ded56..aece697 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
//Synthetic comment -- @@ -313,6 +314,12 @@
assertFalse(d.isOnSaveInstanceStateCalled);
assertFalse(TestDialog.isOnRestoreInstanceStateCalled);

        //skip if the device doesn't support both of portrait and landscape orientation screens.
        final PackageManager pm = mContext.getPackageManager();
        if(!(pm.hasSystemFeature(PackageManager.FEATURE_SCREEN_LANDSCAPE)
                && pm.hasSystemFeature(PackageManager.FEATURE_SCREEN_PORTRAIT)))
	    return;

OrientationTestUtils.toggleOrientationSync(mActivity, mInstrumentation);

assertTrue(d.isOnSaveInstanceStateCalled);







