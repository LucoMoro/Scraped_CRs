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
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.app.cts;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.pm.ActivityInfo;

public class OrientationTestUtils {

    /**
     * Change the activity's orientation to something different and then switch back. This is used
     * to trigger {@link Activity#onConfigurationChanged(android.content.res.Configuration)}.
     *
     * @param activity whose orientation will be changed and restored
     */
    public static void toggleOrientation(Activity activity) {
        toggleOrientationSync(activity, null);
    }

    /**
     * Same as {@link #toggleOrientation(Activity)} except {@link Instrumentation#waitForIdleSync()}
     * is called after each orientation change.
     *
     * @param activity whose orientation will be changed and restored
     * @param instrumentation use for idle syncing
     */
    public static void toggleOrientationSync(final Activity activity,
            final Instrumentation instrumentation) {
        final int originalOrientation = activity.getResources().getConfiguration().orientation;
        final int newOrientation = originalOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        changeOrientation(activity, instrumentation, newOrientation);
        changeOrientation(activity, instrumentation, originalOrientation);
    }

    private static void changeOrientation(final Activity activity,
            Instrumentation instrumentation, final int orientation) {
        activity.setRequestedOrientation(orientation);
        if (instrumentation != null) {
            instrumentation.waitForIdleSync();
        }
    }
}








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/ApplicationTest.java b/tests/tests/app/src/android/app/cts/ApplicationTest.java
//Synthetic comment -- index c0c777c..2dfb97d 100644

//Synthetic comment -- @@ -16,17 +16,17 @@

package android.app.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.InstrumentationTestCase;

/**
* Test {@link Application}.
//Synthetic comment -- @@ -78,13 +78,10 @@

runTestOnUiThread(new Runnable() {
public void run() {
               OrientationTestUtils.toggleOrientation(activity);
}
});
        instrumentation.waitForIdleSync();
assertTrue(mockApp.isOnConfigurationChangedCalled);
}









//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/DatePickerDialogTest.java b/tests/tests/app/src/android/app/cts/DatePickerDialogTest.java
//Synthetic comment -- index 8b64a56..140ccaa 100644

//Synthetic comment -- @@ -16,29 +16,28 @@

package android.app.cts;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.widget.DatePicker;
import android.widget.TextView;

@TestTargetClass(DatePickerDialog.class)
public class DatePickerDialogTest extends ActivityInstrumentationTestCase2<DialogStubActivity> {

private Instrumentation mInstrumentation;
private DialogStubActivity mActivity;

public DatePickerDialogTest() {
super("com.android.cts.stub", DialogStubActivity.class);
//Synthetic comment -- @@ -49,13 +48,11 @@
super.setUp();
mInstrumentation = getInstrumentation();
mActivity = getActivity();
}

@Override
protected void tearDown() throws Exception {
if (mActivity != null) {
mActivity.finish();
}
super.tearDown();
//Synthetic comment -- @@ -174,10 +171,9 @@
assertEquals(mActivity.updatedDay, mActivity.INITIAL_DAY_OF_MONTH);
assertTrue(DialogStubActivity.onDateChangedCalled);

        assertFalse(mActivity.onSaveInstanceStateCalled);
        assertFalse(DialogStubActivity.onRestoreInstanceStateCalled);
        OrientationTestUtils.toggleOrientationSync(mActivity, mInstrumentation);
assertTrue(mActivity.onSaveInstanceStateCalled);
assertTrue(DialogStubActivity.onRestoreInstanceStateCalled);
}








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/DialogTest.java b/tests/tests/app/src/android/app/cts/DialogTest.java
//Synthetic comment -- index ea7deca..4d02896 100644

//Synthetic comment -- @@ -326,10 +326,12 @@
public void testOnSaveInstanceState() {
popDialog(DialogStubActivity.TEST_ONSTART_AND_ONSTOP);
final TestDialog d = (TestDialog) mActivity.getDialog();

        assertFalse(d.isOnSaveInstanceStateCalled);
        assertFalse(TestDialog.isOnRestoreInstanceStateCalled);

        OrientationTestUtils.toggleOrientationSync(mActivity, mInstrumentation);

assertTrue(d.isOnSaveInstanceStateCalled);
assertTrue(TestDialog.isOnRestoreInstanceStateCalled);
}








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/ListActivityTest.java b/tests/tests/app/src/android/app/cts/ListActivityTest.java
//Synthetic comment -- index 43ebd05..d3a43f9 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import dalvik.annotation.TestTargets;

import android.app.ListActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
//Synthetic comment -- @@ -34,7 +33,7 @@
@TestTargetClass(ListActivity.class)
public class ListActivityTest extends ActivityInstrumentationTestCase2<ListActivityTestHelper> {
private ListActivityTestHelper mStubListActivity;

public ListActivityTest() {
super("com.android.cts.stub", ListActivityTestHelper.class);
}
//Synthetic comment -- @@ -44,7 +43,6 @@
super.setUp();
mStubListActivity = getActivity();
assertNotNull(mStubListActivity);
}

protected void waitForAction() throws InterruptedException {
//Synthetic comment -- @@ -167,20 +165,7 @@
assertEquals(arrayAdapter, mStubListActivity.getListView().getAdapter());
assertTrue(mStubListActivity.isOnContentChangedCalled);
assertFalse(ListActivityTestHelper.isOnRestoreInstanceStateCalled);
        OrientationTestUtils.toggleOrientationSync(mStubListActivity, getInstrumentation());
assertTrue(ListActivityTestHelper.isOnRestoreInstanceStateCalled);
}
}








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/TabActivityTest.java b/tests/tests/app/src/android/app/cts/TabActivityTest.java
//Synthetic comment -- index a4595a5b..649a56d 100644

//Synthetic comment -- @@ -145,10 +145,7 @@
sendKeys(KeyEvent.KEYCODE_BACK);
mInstrumentation.waitForIdleSync();
assertFalse(MockTabActivity.isOnRestoreInstanceStateCalled);
        OrientationTestUtils.toggleOrientationSync(mActivity, mInstrumentation);
assertTrue(MockTabActivity.isOnRestoreInstanceStateCalled);
}








