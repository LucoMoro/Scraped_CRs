/*Fix setRequestedOrientation Calls for Increased Stability

Bug 2639189

Tens of random failures in DialogTest and InstrumentationTest seemed
to occur. Commenting out the setRequestedOrientation calls in
ApplicationTest seemed to remove the problem. It seems if the orientation
is not restored properly, then Froyo will attempt to change the
orientation on subsequent activities causing the test activities to
restart and making the references to the prior activity null!

Create a method that check what the current orientation is in order to
switch to a new orientation and then restore the original orientation.

Change-Id:I3218cb2770e70168f4785eb49800bf85293b7ad4*/
//Synthetic comment -- diff --git a/tests/src/android/app/cts/OrientationTestUtils.java b/tests/src/android/app/cts/OrientationTestUtils.java
new file mode 100644
//Synthetic comment -- index 0000000..7b2b568

//Synthetic comment -- @@ -0,0 +1,59 @@








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/ApplicationTest.java b/tests/tests/app/src/android/app/cts/ApplicationTest.java
//Synthetic comment -- index c0c777c..2dfb97d 100644

//Synthetic comment -- @@ -16,17 +16,17 @@

package android.app.cts;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.test.InstrumentationTestCase;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

/**
* Test {@link Application}.
//Synthetic comment -- @@ -78,13 +78,10 @@

runTestOnUiThread(new Runnable() {
public void run() {
                // make sure the configuration has been changed.
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
}
});
        getInstrumentation().waitForIdleSync();

assertTrue(mockApp.isOnConfigurationChangedCalled);
}









//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/DatePickerDialogTest.java b/tests/tests/app/src/android/app/cts/DatePickerDialogTest.java
//Synthetic comment -- index 8b64a56..140ccaa 100644

//Synthetic comment -- @@ -16,29 +16,28 @@

package android.app.cts;

import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.widget.DatePicker;
import android.widget.TextView;
import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

@TestTargetClass(DatePickerDialog.class)
public class DatePickerDialogTest extends ActivityInstrumentationTestCase2<DialogStubActivity> {

private Instrumentation mInstrumentation;
private DialogStubActivity mActivity;
    private int mOrientation;

public DatePickerDialogTest() {
super("com.android.cts.stub", DialogStubActivity.class);
//Synthetic comment -- @@ -49,13 +48,11 @@
super.setUp();
mInstrumentation = getInstrumentation();
mActivity = getActivity();
        mOrientation = mActivity.getRequestedOrientation();
}

@Override
protected void tearDown() throws Exception {
if (mActivity != null) {
            mActivity.setRequestedOrientation(mOrientation);
mActivity.finish();
}
super.tearDown();
//Synthetic comment -- @@ -174,10 +171,9 @@
assertEquals(mActivity.updatedDay, mActivity.INITIAL_DAY_OF_MONTH);
assertTrue(DialogStubActivity.onDateChangedCalled);

        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mInstrumentation.waitForIdleSync();
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mInstrumentation.waitForIdleSync();
assertTrue(mActivity.onSaveInstanceStateCalled);
assertTrue(DialogStubActivity.onRestoreInstanceStateCalled);
}








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/DialogTest.java b/tests/tests/app/src/android/app/cts/DialogTest.java
//Synthetic comment -- index ea7deca..4d02896 100644

//Synthetic comment -- @@ -326,10 +326,12 @@
public void testOnSaveInstanceState() {
popDialog(DialogStubActivity.TEST_ONSTART_AND_ONSTOP);
final TestDialog d = (TestDialog) mActivity.getDialog();
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mInstrumentation.waitForIdleSync();
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mInstrumentation.waitForIdleSync();
assertTrue(d.isOnSaveInstanceStateCalled);
assertTrue(TestDialog.isOnRestoreInstanceStateCalled);
}








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/ListActivityTest.java b/tests/tests/app/src/android/app/cts/ListActivityTest.java
//Synthetic comment -- index 43ebd05..d3a43f9 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import dalvik.annotation.TestTargets;

import android.app.ListActivity;
import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
//Synthetic comment -- @@ -34,7 +33,7 @@
@TestTargetClass(ListActivity.class)
public class ListActivityTest extends ActivityInstrumentationTestCase2<ListActivityTestHelper> {
private ListActivityTestHelper mStubListActivity;
    private int mScreenOrientation;
public ListActivityTest() {
super("com.android.cts.stub", ListActivityTestHelper.class);
}
//Synthetic comment -- @@ -44,7 +43,6 @@
super.setUp();
mStubListActivity = getActivity();
assertNotNull(mStubListActivity);
        mScreenOrientation = mStubListActivity.getRequestedOrientation();
}

protected void waitForAction() throws InterruptedException {
//Synthetic comment -- @@ -167,20 +165,7 @@
assertEquals(arrayAdapter, mStubListActivity.getListView().getAdapter());
assertTrue(mStubListActivity.isOnContentChangedCalled);
assertFalse(ListActivityTestHelper.isOnRestoreInstanceStateCalled);
        mStubListActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getInstrumentation().waitForIdleSync();
        mStubListActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getInstrumentation().waitForIdleSync();
assertTrue(ListActivityTestHelper.isOnRestoreInstanceStateCalled);
}

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (mStubListActivity != null) {
            mStubListActivity.setRequestedOrientation(mScreenOrientation);
            getInstrumentation().waitForIdleSync();
        }
    }

}








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/TabActivityTest.java b/tests/tests/app/src/android/app/cts/TabActivityTest.java
//Synthetic comment -- index a4595a5b..649a56d 100644

//Synthetic comment -- @@ -145,10 +145,7 @@
sendKeys(KeyEvent.KEYCODE_BACK);
mInstrumentation.waitForIdleSync();
assertFalse(MockTabActivity.isOnRestoreInstanceStateCalled);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mInstrumentation.waitForIdleSync();
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mInstrumentation.waitForIdleSync();
assertTrue(MockTabActivity.isOnRestoreInstanceStateCalled);
}








