/*Further Activity Lifecycle Testing Improvements

Bug 2639189 2663328 2189784

The prior change ran the lifecycle checking activity and if it
failed to match ran the activity again with a different expected
lifecycle. However, running the activity again could cause the
activity to follow the original lifecycle that failed. Thus,
follow the expected lifecycle as long as possible and then switch
to other possible lifecycles when running into lifecycle mismatches.

Also do not check the lifecycle for onStop or onDestroy methods,
because those are not guaranteed to be executed at all. Delete tests
that checked onStop and onDestroy being called, because they will
probably hang in the future.

Change-Id:I6f721d70861cd9b22523b8aa9b883e7336daab22*/




//Synthetic comment -- diff --git a/tests/src/android/app/cts/ActivityTestsBase.java b/tests/src/android/app/cts/ActivityTestsBase.java
//Synthetic comment -- index faf74f0..426a8b8 100644

//Synthetic comment -- @@ -21,18 +21,12 @@
import android.content.Intent;
import android.test.AndroidTestCase;
import android.test.PerformanceTestCase;

public class ActivityTestsBase extends AndroidTestCase implements PerformanceTestCase,
LaunchpadActivity.CallingTest {
public static final String PERMISSION_GRANTED = "android.app.cts.permission.TEST_GRANTED";
public static final String PERMISSION_DENIED = "android.app.cts.permission.TEST_DENIED";

private static final int TIMEOUT_MS = 60 * 1000;

protected Intent mIntent;
//Synthetic comment -- @@ -197,44 +191,6 @@
return mResultCode;
}


public int getResultCode() {
return mResultCode;








//Synthetic comment -- diff --git a/tests/src/android/app/cts/LaunchpadActivity.java b/tests/src/android/app/cts/LaunchpadActivity.java
//Synthetic comment -- index aa6d9808..fa18ec5 100644

//Synthetic comment -- @@ -30,6 +30,12 @@
import android.os.Parcel;
import android.os.Parcelable;
import android.test.PerformanceTestCase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MyBadParcelable implements Parcelable {
public MyBadParcelable() {
//Synthetic comment -- @@ -84,12 +90,8 @@
public static final int FORWARDED_RESULT = 2;

public static final String LIFECYCLE_BASIC = "android.app.cts.activity.LIFECYCLE_BASIC";
    public static final String LIFECYCLE_SCREEN = "android.app.cts.activity.LIFECYCLE_SCREEN";
    public static final String LIFECYCLE_DIALOG = "android.app.cts.activity.LIFECYCLE_DIALOG";

public static final String BROADCAST_REGISTERED = "android.app.cts.activity.BROADCAST_REGISTERED";
public static final String BROADCAST_LOCAL = "android.app.cts.activity.BROADCAST_LOCAL";
//Synthetic comment -- @@ -121,13 +123,15 @@
public static final String ON_RESUME = "onResume";
public static final String ON_FREEZE = "onSaveInstanceState";
public static final String ON_PAUSE = "onPause";

    // ON_STOP and ON_DESTROY are not tested because they may not be called.

public static final String DO_FINISH = "finish";
public static final String DO_LOCAL_SCREEN = "local-screen";
public static final String DO_LOCAL_DIALOG = "local-dialog";

    private static final String TAG = "LaunchpadActivity";

private boolean mBadParcelable = false;

private boolean mStarted = false;
//Synthetic comment -- @@ -136,9 +140,18 @@
private Intent mData = new Intent().setAction("No result received");
private RuntimeException mResultStack = null;

    /** Index into the {@link #mNextLifecycle} array. */
private int mNextLifecycle;

    /** Current lifecycle expected to be followed. */
    private String[] mExpectedLifecycle;

    /** Other possible lifecycles. Never includes the current {@link #mExpectedLifecycle}. */
    private List<String[]> mOtherPossibleLifecycles = new ArrayList<String[]>(2);

    /** Map from lifecycle arrays to debugging log names. */
    private Map<String[], String> mLifecycleNames = new HashMap<String[], String>(2);

private String[] mExpectedReceivers = null;
private int mNextReceiver;

//Synthetic comment -- @@ -159,47 +172,82 @@
@Override
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);

        resetLifecycles();

        // ON_STOP and ON_DESTROY are not tested because they may not be called.

final String action = getIntent().getAction();
if (LIFECYCLE_BASIC.equals(action)) {
            addPossibleLifecycle(LIFECYCLE_BASIC, new String[] {
                    ON_START, ON_RESUME, DO_FINISH, ON_PAUSE
});
        } else if (LIFECYCLE_SCREEN.equals(action)) {
            addPossibleLifecycle(LIFECYCLE_SCREEN + "_RESTART", new String[] {
                    ON_START, ON_RESUME, DO_LOCAL_SCREEN, ON_FREEZE, ON_PAUSE,
                    ON_RESTART, ON_START, ON_RESUME, DO_FINISH, ON_PAUSE
});
            addPossibleLifecycle(LIFECYCLE_SCREEN + "_RESUME", new String[] {
                    ON_START, ON_RESUME, DO_LOCAL_SCREEN, ON_FREEZE, ON_PAUSE,
                    ON_RESUME, DO_FINISH, ON_PAUSE
});
        } else if (LIFECYCLE_DIALOG.equals(action)) {
            addPossibleLifecycle(LIFECYCLE_DIALOG + "_RESTART", new String[] {
                    ON_START, ON_RESUME, DO_LOCAL_DIALOG, ON_FREEZE, ON_PAUSE,
                    ON_RESTART, ON_START, ON_RESUME, DO_FINISH, ON_PAUSE
});
            addPossibleLifecycle(LIFECYCLE_DIALOG + "_RESUME", new String[] {
                    ON_START, ON_RESUME, DO_LOCAL_DIALOG, ON_FREEZE, ON_PAUSE,
                    ON_RESUME, DO_FINISH, ON_PAUSE
});
        }
    }

    private void resetLifecycles() {
        mNextLifecycle = 0;
        mExpectedLifecycle = null;
        mOtherPossibleLifecycles.clear();
        mLifecycleNames.clear();
    }

    /**
     * Add a potential lifecycle that this activity may follow, since there
     * are usually multiple valid lifecycles. For instance, sometimes onPause
     * will lead to onResume rather than onStop when another activity is
     * raised over the current one.
     *
     * @param debugName for the lifecycle shown in the logs
     * @param lifecycle array containing tokens indicating the expected lifecycle
     */
    private void addPossibleLifecycle(String debugName, String[] lifecycle) {
        mLifecycleNames.put(lifecycle, debugName);
        if (mExpectedLifecycle == null) {
            mExpectedLifecycle = lifecycle;
        } else {
            mOtherPossibleLifecycles.add(lifecycle);
        }
    }

    /**
     * Switch to the next possible lifecycle and return if switching was
     * successful. Call this method when mExpectedLifecycle doesn't match
     * the current lifecycle and you need to check another possible lifecycle.
     *
     * @return whether on not there was a lifecycle to switch to
     */
    private boolean switchToNextPossibleLifecycle() {
        if (!mOtherPossibleLifecycles.isEmpty()) {
            String[] newLifecycle = mOtherPossibleLifecycles.remove(0);
            Log.w(TAG, "Switching expected lifecycles from "
                    + mLifecycleNames.get(mExpectedLifecycle) + " to "
                    + mLifecycleNames.get(newLifecycle));
            mExpectedLifecycle = newLifecycle;
            return true;
        } else {
            Log.w(TAG, "No more lifecycles after "
                    + mLifecycleNames.get(mExpectedLifecycle));
            mExpectedLifecycle = null;
            return false;
}
}

//Synthetic comment -- @@ -364,12 +412,6 @@
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
switch (requestCode) {
case LAUNCHED_RESULT:
//Synthetic comment -- @@ -393,18 +435,6 @@
}
}

private void checkLifecycle(String where) {
String action = getIntent().getAction();

//Synthetic comment -- @@ -418,28 +448,29 @@
mExpectedLifecycle = null;
return;
}

        do {
            if (mExpectedLifecycle[mNextLifecycle].equals(where)) {
                break;
            }
        } while (switchToNextPossibleLifecycle());

        if (mExpectedLifecycle == null) {
finishBad("Activity lifecycle for " + action + " incorrect: received " + where
+ " but expected " + mExpectedLifecycle[mNextLifecycle]
+ " at " + mNextLifecycle);
return;
}

mNextLifecycle++;

if (mNextLifecycle >= mExpectedLifecycle.length) {
            finishGood();
return;
}

final String next = mExpectedLifecycle[mNextLifecycle];
        if (next.equals(DO_FINISH)) {
mNextLifecycle++;
if (mNextLifecycle >= mExpectedLifecycle.length) {
setTestResult(RESULT_OK, null);
//Synthetic comment -- @@ -488,6 +519,9 @@
private void finishWithResult(int resultCode, Intent data) {
setTestResult(resultCode, data);
finish();

        // Member fields set by calling setTestResult above...
        sCallingTest.activityFinished(mResultCode, mData, mResultStack);
}

private void setTestResult(int resultCode, Intent data) {








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/ActivityGroupTest.java b/tests/tests/app/src/android/app/cts/ActivityGroupTest.java
//Synthetic comment -- index b0739f4..0cf7998 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package android.app.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -167,12 +166,9 @@
args = {}
)
})
public void testTabScreen() throws Exception {
mIntent = mTabIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_SCREEN);
}

@TestTargets({
//Synthetic comment -- @@ -237,145 +233,8 @@
args = {}
)
})
public void testTabDialog() throws Exception {
mIntent = mTabIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_DIALOG);
}
}








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/LifecycleTest.java b/tests/tests/app/src/android/app/cts/LifecycleTest.java
//Synthetic comment -- index 5f69306..aa28628 100644

//Synthetic comment -- @@ -17,6 +17,11 @@

package android.app.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
//Synthetic comment -- @@ -40,10 +45,6 @@
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.WindowManager.LayoutParams;

@TestTargetClass(Activity.class)
public class LifecycleTest extends ActivityTestsBase {
//Synthetic comment -- @@ -81,2735 +82,6 @@
),
@TestTargetNew(
level = TestLevel.COMPLETE,
method = "closeContextMenu",
args = {}
),
//Synthetic comment -- @@ -3466,9 +738,7 @@
})
public void testTabDialog() {
mIntent = mTabIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_DIALOG);
}

@TestTargets({
//Synthetic comment -- @@ -4150,9 +1420,7 @@
})
public void testDialog() {
mIntent = mTopIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_DIALOG);
}

@TestTargets({
//Synthetic comment -- @@ -4834,9 +2102,7 @@
})
public void testTabScreen() {
mIntent = mTabIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_SCREEN);
}

@TestTargets({
//Synthetic comment -- @@ -5519,9 +2785,7 @@
})
public void testScreen() {
mIntent = mTopIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_SCREEN);
}

@TestTargets({







