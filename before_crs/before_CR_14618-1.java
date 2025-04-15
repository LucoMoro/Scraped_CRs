/*Support Multiple Lifecycles in Tests

Bug 2639189 2663328

Many of the lifecycle tests expected onStop to be called. However,
there is no guarantee that onStop will be called, since that app
may be killed in that situation. Enhance the tests to support
multiple lifecycles by adding a runMultipleLaunchpads method.

Change-Id:I87d031b2ab0bc92554d8208e0c3f000eac1f6cf9*/
//Synthetic comment -- diff --git a/tests/src/android/app/cts/ActivityTestsBase.java b/tests/src/android/app/cts/ActivityTestsBase.java
//Synthetic comment -- index 7bbedd8..faf74f0 100644

//Synthetic comment -- @@ -21,12 +21,20 @@
import android.content.Intent;
import android.test.AndroidTestCase;
import android.test.PerformanceTestCase;

public class ActivityTestsBase extends AndroidTestCase implements PerformanceTestCase,
LaunchpadActivity.CallingTest {
public static final String PERMISSION_GRANTED = "android.app.cts.permission.TEST_GRANTED";
public static final String PERMISSION_DENIED = "android.app.cts.permission.TEST_DENIED";

protected Intent mIntent;

private PerformanceTestCase.Intermediates mIntermediates;
//Synthetic comment -- @@ -125,6 +133,11 @@
}

public int runLaunchpad(String action) {
LaunchpadActivity.setCallingTest(this);

synchronized (this) {
//Synthetic comment -- @@ -133,8 +146,6 @@
mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
mContext.startActivity(mIntent);
}

        return waitForResultOrThrow(60 * 1000);
}

public int waitForResultOrThrow(int timeoutMs) {
//Synthetic comment -- @@ -186,6 +197,45 @@
return mResultCode;
}

public int getResultCode() {
return mResultCode;
}








//Synthetic comment -- diff --git a/tests/src/android/app/cts/LaunchpadActivity.java b/tests/src/android/app/cts/LaunchpadActivity.java
//Synthetic comment -- index 566e7b7..aa6d9808 100644

//Synthetic comment -- @@ -84,8 +84,10 @@
public static final int FORWARDED_RESULT = 2;

public static final String LIFECYCLE_BASIC = "android.app.cts.activity.LIFECYCLE_BASIC";
    public static final String LIFECYCLE_SCREEN = "android.app.cts.activity.LIFECYCLE_SCREEN";
    public static final String LIFECYCLE_DIALOG = "android.app.cts.activity.LIFECYCLE_DIALOG";
public static final String LIFECYCLE_FINISH_CREATE = "android.app.cts.activity.LIFECYCLE_FINISH_CREATE";
public static final String LIFECYCLE_FINISH_START = "android.app.cts.activity.LIFECYCLE_FINISH_START";

//Synthetic comment -- @@ -162,16 +164,26 @@
setExpectedLifecycle(new String[] {
ON_START, ON_RESUME, DO_FINISH, ON_PAUSE, ON_STOP, ON_DESTROY
});
        } else if (LIFECYCLE_SCREEN.equals(action)) {
setExpectedLifecycle(new String[] {
ON_START, ON_RESUME, DO_LOCAL_SCREEN, ON_FREEZE, ON_PAUSE, ON_STOP, ON_RESTART,
ON_START, ON_RESUME, DO_FINISH, ON_PAUSE, ON_STOP, ON_DESTROY
});
        } else if (LIFECYCLE_DIALOG.equals(action)) {
setExpectedLifecycle(new String[] {
ON_START, ON_RESUME, DO_LOCAL_DIALOG, ON_FREEZE, ON_PAUSE, ON_RESUME,
DO_FINISH, ON_PAUSE, ON_STOP, ON_DESTROY
});
} else if (LIFECYCLE_FINISH_CREATE.equals(action)) {
// This one behaves a little differently when running in a group.
if (getParent() == null) {
//Synthetic comment -- @@ -394,19 +406,22 @@
}

private void checkLifecycle(String where) {
if (mExpectedLifecycle == null) {
return;
}

if (mNextLifecycle >= mExpectedLifecycle.length) {
            finishBad("Activity lifecycle incorrect: received " + where
+ " but don't expect any more calls");
mExpectedLifecycle = null;
return;
}
if (!mExpectedLifecycle[mNextLifecycle].equals(where)) {
            finishBad("Activity lifecycle incorrect: received " + where + " but expected "
                    + mExpectedLifecycle[mNextLifecycle] + " at " + mNextLifecycle);
mExpectedLifecycle = null;
return;
}
//Synthetic comment -- @@ -420,7 +435,7 @@

final String next = mExpectedLifecycle[mNextLifecycle];
if (where.equals(ON_DESTROY)) {
            finishBad("Activity lifecycle incorrect: received " + where
+ " but expected more actions (next is " + next + ")");
mExpectedLifecycle = null;
return;








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/ActivityGroupTest.java b/tests/tests/app/src/android/app/cts/ActivityGroupTest.java
//Synthetic comment -- index 05287cd..b0739f4 100644

//Synthetic comment -- @@ -170,7 +170,9 @@
@BrokenTest(value="bug 2189784, needs investigation")
public void testTabScreen() throws Exception {
mIntent = mTabIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_SCREEN);
}

@TestTargets({
//Synthetic comment -- @@ -238,7 +240,9 @@
@BrokenTest(value="bug 2189784, needs investigation")
public void testTabDialog() throws Exception {
mIntent = mTabIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_DIALOG);
}

@TestTargets({








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/LifecycleTest.java b/tests/tests/app/src/android/app/cts/LifecycleTest.java
//Synthetic comment -- index 56517ae..5f69306 100644

//Synthetic comment -- @@ -3466,7 +3466,9 @@
})
public void testTabDialog() {
mIntent = mTabIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_DIALOG);
}

@TestTargets({
//Synthetic comment -- @@ -4148,7 +4150,9 @@
})
public void testDialog() {
mIntent = mTopIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_DIALOG);
}

@TestTargets({
//Synthetic comment -- @@ -4830,7 +4834,9 @@
})
public void testTabScreen() {
mIntent = mTabIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_SCREEN);
}

@TestTargets({
//Synthetic comment -- @@ -5513,7 +5519,9 @@
})
public void testScreen() {
mIntent = mTopIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_SCREEN);
}

@TestTargets({







