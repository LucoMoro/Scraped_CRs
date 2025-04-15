/*Fix CTS cases ApplicationTest and DialogTest.

The device which supports a fixed-orientation screen like a set-top box doesn't need to change the screen orientation.
The test case should be skipped if the device doesn't support both of portrait and landscape orientation screens.

Change-Id:I13f452548b37724353b8663fa71919ecea77ca7f*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/ApplicationTest.java b/tests/tests/app/src/android/app/cts/ApplicationTest.java
//Synthetic comment -- index 2dfb97d..41a926c 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.InstrumentationTestCase;

/**
//Synthetic comment -- @@ -76,6 +77,13 @@
assertTrue(mockApp.isConstructorCalled);
assertTrue(mockApp.isOnCreateCalled);

runTestOnUiThread(new Runnable() {
public void run() {
OrientationTestUtils.toggleOrientation(activity);








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/DialogTest.java b/tests/tests/app/src/android/app/cts/DialogTest.java
//Synthetic comment -- index 37ded56..6c560f3 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
//Synthetic comment -- @@ -313,6 +314,13 @@
assertFalse(d.isOnSaveInstanceStateCalled);
assertFalse(TestDialog.isOnRestoreInstanceStateCalled);

OrientationTestUtils.toggleOrientationSync(mActivity, mInstrumentation);

assertTrue(d.isOnSaveInstanceStateCalled);







