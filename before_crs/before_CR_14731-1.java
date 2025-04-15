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
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ActivityTestsBase extends AndroidTestCase implements PerformanceTestCase,
LaunchpadActivity.CallingTest {
public static final String PERMISSION_GRANTED = "android.app.cts.permission.TEST_GRANTED";
public static final String PERMISSION_DENIED = "android.app.cts.permission.TEST_DENIED";

    private static final String TAG = "ActivityTestsBase";

private static final int TIMEOUT_MS = 60 * 1000;

protected Intent mIntent;
//Synthetic comment -- @@ -197,44 +191,6 @@
return mResultCode;
}

    /**
     * Runs multiple launch pad activities until successfully finishing one or
     * exhausting them all and throwing an exception.
     *
     * @param testName to make it easier to debug failures
     * @param testClass to make it easier to debug failures
     * @param firstAction to run
     * @param moreActions to run in sequence if the first action fails
     */
    public void runMultipleLaunchpads(String testName, Class<?> testClass,
            String firstAction, String... moreActions) {
        List<String> actions = new ArrayList<String>();
        actions.add(firstAction);
        for (String action : moreActions) {
            actions.add(action);
        }

        RuntimeException lastException = null;
        String testIdentifier = testClass.getSimpleName() + "#" + testName + ":";

        for (String action : actions) {
            startLaunchpadActivity(action);
            try {
                int res = waitForResultOrThrow(TIMEOUT_MS);
                if (res == Activity.RESULT_OK) {
                    return;
                } else {
                    Log.w(TAG, testIdentifier + action + " returned result " + res);
                }
            } catch (RuntimeException e) {
                Log.w(TAG, testIdentifier + action + " threw exception", e);
            }
        }

        if (lastException != null) {
            throw lastException;
        }
    }

public int getResultCode() {
return mResultCode;








//Synthetic comment -- diff --git a/tests/src/android/app/cts/LaunchpadActivity.java b/tests/src/android/app/cts/LaunchpadActivity.java
//Synthetic comment -- index aa6d9808..fa18ec5 100644

//Synthetic comment -- @@ -30,6 +30,12 @@
import android.os.Parcel;
import android.os.Parcelable;
import android.test.PerformanceTestCase;

class MyBadParcelable implements Parcelable {
public MyBadParcelable() {
//Synthetic comment -- @@ -84,12 +90,8 @@
public static final int FORWARDED_RESULT = 2;

public static final String LIFECYCLE_BASIC = "android.app.cts.activity.LIFECYCLE_BASIC";
    public static final String LIFECYCLE_SCREEN_ON_STOP = "android.app.cts.activity.LIFECYCLE_SCREEN_ON_STOP";
    public static final String LIFECYCLE_SCREEN_ON_RESUME = "android.app.cts.activity.LIFECYCLE_SCREEN_ON_RESUME";
    public static final String LIFECYCLE_DIALOG_ON_STOP = "android.app.cts.activity.LIFECYCLE_DIALOG_ON_STOP";
    public static final String LIFECYCLE_DIALOG_ON_RESUME = "android.app.cts.activity.LIFECYCLE_DIALOG_ON_RESUME";
    public static final String LIFECYCLE_FINISH_CREATE = "android.app.cts.activity.LIFECYCLE_FINISH_CREATE";
    public static final String LIFECYCLE_FINISH_START = "android.app.cts.activity.LIFECYCLE_FINISH_START";

public static final String BROADCAST_REGISTERED = "android.app.cts.activity.BROADCAST_REGISTERED";
public static final String BROADCAST_LOCAL = "android.app.cts.activity.BROADCAST_LOCAL";
//Synthetic comment -- @@ -121,13 +123,15 @@
public static final String ON_RESUME = "onResume";
public static final String ON_FREEZE = "onSaveInstanceState";
public static final String ON_PAUSE = "onPause";
    public static final String ON_STOP = "onStop";
    public static final String ON_DESTROY = "onDestroy";

public static final String DO_FINISH = "finish";
public static final String DO_LOCAL_SCREEN = "local-screen";
public static final String DO_LOCAL_DIALOG = "local-dialog";

private boolean mBadParcelable = false;

private boolean mStarted = false;
//Synthetic comment -- @@ -136,9 +140,18 @@
private Intent mData = new Intent().setAction("No result received");
private RuntimeException mResultStack = null;

    private String[] mExpectedLifecycle = null;
private int mNextLifecycle;

private String[] mExpectedReceivers = null;
private int mNextReceiver;

//Synthetic comment -- @@ -159,47 +172,82 @@
@Override
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
final String action = getIntent().getAction();
if (LIFECYCLE_BASIC.equals(action)) {
            setExpectedLifecycle(new String[] {
                    ON_START, ON_RESUME, DO_FINISH, ON_PAUSE, ON_STOP, ON_DESTROY
});
        } else if (LIFECYCLE_SCREEN_ON_STOP.equals(action)) {
            setExpectedLifecycle(new String[] {
                    ON_START, ON_RESUME, DO_LOCAL_SCREEN, ON_FREEZE, ON_PAUSE, ON_STOP, ON_RESTART,
                    ON_START, ON_RESUME, DO_FINISH, ON_PAUSE, ON_STOP, ON_DESTROY
});
        } else if (LIFECYCLE_SCREEN_ON_RESUME.equals(action)) {
            setExpectedLifecycle(new String[] {
                    ON_START, ON_RESUME, DO_LOCAL_SCREEN, ON_FREEZE, ON_PAUSE, ON_RESUME, DO_FINISH,
                    ON_PAUSE, ON_STOP, ON_DESTROY
});
        } else if (LIFECYCLE_DIALOG_ON_RESUME.equals(action)) {
            setExpectedLifecycle(new String[] {
                    ON_START, ON_RESUME, DO_LOCAL_DIALOG, ON_FREEZE, ON_PAUSE, ON_RESUME,
                    DO_FINISH, ON_PAUSE, ON_STOP, ON_DESTROY
});
        } else if (LIFECYCLE_DIALOG_ON_STOP.equals(action)) {
            setExpectedLifecycle(new String[] {
                    ON_START, ON_RESUME, DO_LOCAL_DIALOG, ON_FREEZE, ON_PAUSE, ON_STOP, ON_RESTART,
                    ON_START, ON_RESUME, DO_FINISH, ON_PAUSE, ON_STOP, ON_DESTROY
});
        } else if (LIFECYCLE_FINISH_CREATE.equals(action)) {
            // This one behaves a little differently when running in a group.
            if (getParent() == null) {
                setExpectedLifecycle(new String[] {
                    ON_DESTROY
                });
            } else {
                setExpectedLifecycle(new String[] {
                        ON_START, ON_STOP, ON_DESTROY
                });
            }
            finish();
        } else if (LIFECYCLE_FINISH_START.equals(action)) {
            setExpectedLifecycle(new String[] {
                    ON_START, DO_FINISH, ON_STOP, ON_DESTROY
            });
}
}

//Synthetic comment -- @@ -364,12 +412,6 @@
}

@Override
    protected void onStop() {
        super.onStop();
        checkLifecycle(ON_STOP);
    }

    @Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
switch (requestCode) {
case LAUNCHED_RESULT:
//Synthetic comment -- @@ -393,18 +435,6 @@
}
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkLifecycle(ON_DESTROY);
        sCallingTest.activityFinished(mResultCode, mData, mResultStack);
    }

    private void setExpectedLifecycle(String[] lifecycle) {
        mExpectedLifecycle = lifecycle;
        mNextLifecycle = 0;
    }

private void checkLifecycle(String where) {
String action = getIntent().getAction();

//Synthetic comment -- @@ -418,28 +448,29 @@
mExpectedLifecycle = null;
return;
}
        if (!mExpectedLifecycle[mNextLifecycle].equals(where)) {
finishBad("Activity lifecycle for " + action + " incorrect: received " + where
+ " but expected " + mExpectedLifecycle[mNextLifecycle]
+ " at " + mNextLifecycle);
            mExpectedLifecycle = null;
return;
}

mNextLifecycle++;

if (mNextLifecycle >= mExpectedLifecycle.length) {
            setTestResult(RESULT_OK, null);
return;
}

final String next = mExpectedLifecycle[mNextLifecycle];
        if (where.equals(ON_DESTROY)) {
            finishBad("Activity lifecycle for " + action + " incorrect: received " + where
                    + " but expected more actions (next is " + next + ")");
            mExpectedLifecycle = null;
            return;
        } else if (next.equals(DO_FINISH)) {
mNextLifecycle++;
if (mNextLifecycle >= mExpectedLifecycle.length) {
setTestResult(RESULT_OK, null);
//Synthetic comment -- @@ -488,6 +519,9 @@
private void finishWithResult(int resultCode, Intent data) {
setTestResult(resultCode, data);
finish();
}

private void setTestResult(int resultCode, Intent data) {








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/ActivityGroupTest.java b/tests/tests/app/src/android/app/cts/ActivityGroupTest.java
//Synthetic comment -- index b0739f4..0cf7998 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package android.app.cts;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -167,12 +166,9 @@
args = {}
)
})
    @BrokenTest(value="bug 2189784, needs investigation")
public void testTabScreen() throws Exception {
mIntent = mTabIntent;
        runMultipleLaunchpads("testTabScreen", getClass(),
                LaunchpadActivity.LIFECYCLE_SCREEN_ON_STOP,
                LaunchpadActivity.LIFECYCLE_SCREEN_ON_RESUME);
}

@TestTargets({
//Synthetic comment -- @@ -237,145 +233,8 @@
args = {}
)
})
    @BrokenTest(value="bug 2189784, needs investigation")
public void testTabDialog() throws Exception {
mIntent = mTabIntent;
        runMultipleLaunchpads("testTabDialog", getClass(),
                LaunchpadActivity.LIFECYCLE_DIALOG_ON_RESUME,
                LaunchpadActivity.LIFECYCLE_DIALOG_ON_STOP);
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "ActivityGroup",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "ActivityGroup",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "onCreate",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "onResume",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "onSaveInstanceState",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "onPause",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "onStop",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "onDestroy",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "getCurrentActivity",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "getLocalActivityManager",
            args = {}
        )
    })
     public void testTabFinishCreate() throws Exception {
        mIntent = mTabIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_FINISH_CREATE);
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "ActivityGroup",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "ActivityGroup",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "onCreate",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "onResume",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "onSaveInstanceState",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "onPause",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "onStop",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "onDestroy",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "getCurrentActivity",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "",
            method = "getLocalActivityManager",
            args = {}
        )
    })
    public void testTabFinishStart() throws Exception {
        mIntent = mTabIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_FINISH_START);
}
}








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/LifecycleTest.java b/tests/tests/app/src/android/app/cts/LifecycleTest.java
//Synthetic comment -- index 5f69306..aa28628 100644

//Synthetic comment -- @@ -17,6 +17,11 @@

package android.app.cts;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
//Synthetic comment -- @@ -40,10 +45,6 @@
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.WindowManager.LayoutParams;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

@TestTargetClass(Activity.class)
public class LifecycleTest extends ActivityTestsBase {
//Synthetic comment -- @@ -81,2735 +82,6 @@
),
@TestTargetNew(
level = TestLevel.COMPLETE,
            method = "finalize",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getInstanceCount",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "closeContextMenu",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getIntent",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setIntent",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getApplication",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "isChild",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getParent",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWindowManager",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWindow",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getCurrentFocus",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWallpaperDesiredMinimumWidth",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWallpaperDesiredMinimumHeight",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreate",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onRestoreInstanceState",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPostCreate",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onStart",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onRestart",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onResume",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPostResume",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onNewIntent",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onSaveInstanceState",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPause",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateThumbnail",
            args = {Bitmap.class, Canvas.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateDescription",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onStop",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onDestroy",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onConfigurationChanged",
            args = {Configuration.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getChangingConfigurations",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getLastNonConfigurationInstance",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onRetainNonConfigurationInstance",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onLowMemory",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "managedQuery",
            args = {Uri.class, String[].class, String.class, String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "managedQuery",
            args = {Uri.class, String[].class, String.class, String[].class, String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "managedCommitUpdates",
            args = {Cursor.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startManagingCursor",
            args = {Cursor.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "stopManagingCursor",
            args = {Cursor.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setPersistent",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "findViewById",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setContentView",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setContentView",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setContentView",
            args = {View.class, ViewGroup.LayoutParams.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "addContentView",
            args = {View.class, ViewGroup.LayoutParams.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setDefaultKeyMode",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onKeyDown",
            args = {int.class, KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onKeyUp",
            args = {int.class, KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onKeyMultiple",
            args = {int.class, int.class, KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onTouchEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onTrackballEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onWindowAttributesChanged",
            args = {LayoutParams.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onContentChanged",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onWindowFocusChanged",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "hasWindowFocus",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dispatchKeyEvent",
            args = {KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dispatchTouchEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dispatchTrackballEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreatePanelView",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreatePanelMenu",
            args = {int.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPreparePanel",
            args = {int.class, View.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
             method = "onMenuOpened",
            args = {int.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onMenuItemSelected",
            args = {int.class, MenuItem.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
             method = "onPanelClosed",
            args = {int.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateOptionsMenu",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPrepareOptionsMenu",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onOptionsItemSelected",
            args = {MenuItem.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onOptionsMenuClosed",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "openOptionsMenu",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "closeOptionsMenu",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateContextMenu",
            args = {ContextMenu.class, View.class, ContextMenuInfo.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "registerForContextMenu",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "unregisterForContextMenu",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "openContextMenu",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onContextItemSelected",
            args = {MenuItem.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onContextMenuClosed",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPrepareDialog",
            args = {int.class, Dialog.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "showDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dismissDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "removeDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onSearchRequested",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startSearch",
            args = {String.class, boolean.class, Bundle.class, boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "takeKeyEvents",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "requestWindowFeature",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawableResource",
            args = {int.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawableUri",
            args = {int.class, Uri.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawable",
            args = {int.class, Drawable.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawableAlpha",
            args = {int.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getLayoutInflater",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getMenuInflater",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onApplyThemeResource",
            args = {Theme.class, int.class, boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivityForResult",
            args = {Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivity",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivityIfNeeded",
            args = {Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startNextMatchingActivity",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivityFromChild",
            args = {Activity.class, Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setResult",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setResult",
            args = {int.class, Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getCallingPackage",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getCallingActivity",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "isFinishing",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finish",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finishFromChild",
            args = {Activity.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finishActivity",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finishActivityFromChild",
            args = {Activity.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onActivityResult",
            args = {int.class, int.class, Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "createPendingResult",
            args = {int.class, Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setRequestedOrientation",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getRequestedOrientation",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getTaskId",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "isTaskRoot",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "moveTaskToBack",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getLocalClassName",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getComponentName",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getPreferences",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getSystemService",
            args = {String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setTitle",
            args = {CharSequence.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setTitle",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setTitleColor",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getTitle",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getTitleColor",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onTitleChanged",
            args = {CharSequence.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onChildTitleChanged",
            args = {Activity.class, CharSequence.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgressBarVisibility",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgressBarIndeterminateVisibility",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgressBarIndeterminate",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgress",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setSecondaryProgress",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setVolumeControlStream",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getVolumeControlStream",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "runOnUiThread",
            args = {Runnable.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateView",
            args = {String.class, Context.class, AttributeSet.class}
        )
    })
    public void testTabFinishStart() {
        mIntent = mTabIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_FINISH_START);
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "Activity",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finalize",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onUserInteraction",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onUserLeaveHint",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setVisible",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "closeContextMenu",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getInstanceCount",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getIntent",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setIntent",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getApplication",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "isChild",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getParent",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWindowManager",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWindow",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getCurrentFocus",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWallpaperDesiredMinimumWidth",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWallpaperDesiredMinimumHeight",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreate",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onRestoreInstanceState",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPostCreate",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onStart",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onRestart",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onResume",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPostResume",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onNewIntent",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onSaveInstanceState",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPause",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateThumbnail",
            args = {Bitmap.class, Canvas.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateDescription",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onStop",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onDestroy",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onConfigurationChanged",
            args = {Configuration.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getChangingConfigurations",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getLastNonConfigurationInstance",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onRetainNonConfigurationInstance",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onLowMemory",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "managedQuery",
            args = {Uri.class, String[].class, String.class, String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "managedQuery",
            args = {Uri.class, String[].class, String.class, String[].class, String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "managedCommitUpdates",
            args = {Cursor.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startManagingCursor",
            args = {Cursor.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "stopManagingCursor",
            args = {Cursor.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setPersistent",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "findViewById",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setContentView",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setContentView",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setContentView",
            args = {View.class, ViewGroup.LayoutParams.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "addContentView",
            args = {View.class, ViewGroup.LayoutParams.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setDefaultKeyMode",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onKeyDown",
            args = {int.class, KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onKeyUp",
            args = {int.class, KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onKeyMultiple",
            args = {int.class, int.class, KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onTouchEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onTrackballEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onWindowAttributesChanged",
            args = {LayoutParams.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onContentChanged",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onWindowFocusChanged",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "hasWindowFocus",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dispatchKeyEvent",
            args = {KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dispatchTouchEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dispatchTrackballEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreatePanelView",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreatePanelMenu",
            args = {int.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPreparePanel",
            args = {int.class, View.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onMenuOpened",
            args = {int.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onMenuItemSelected",
            args = {int.class, MenuItem.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPanelClosed",
            args = {int.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateOptionsMenu",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPrepareOptionsMenu",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onOptionsItemSelected",
            args = {MenuItem.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onOptionsMenuClosed",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "openOptionsMenu",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "closeOptionsMenu",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateContextMenu",
            args = {ContextMenu.class, View.class, ContextMenuInfo.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "registerForContextMenu",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "unregisterForContextMenu",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "openContextMenu",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onContextItemSelected",
            args = {MenuItem.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onContextMenuClosed",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPrepareDialog",
            args = {int.class, Dialog.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "showDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dismissDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "removeDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onSearchRequested",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startSearch",
            args = {String.class, boolean.class, Bundle.class, boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "takeKeyEvents",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "requestWindowFeature",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawableResource",
            args = {int.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawableUri",
            args = {int.class, Uri.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawable",
            args = {int.class, Drawable.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawableAlpha",
            args = {int.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getLayoutInflater",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getMenuInflater",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onApplyThemeResource",
            args = {Theme.class, int.class, boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivityForResult",
            args = {Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivity",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivityIfNeeded",
            args = {Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startNextMatchingActivity",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivityFromChild",
            args = {Activity.class, Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setResult",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setResult",
            args = {int.class, Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getCallingPackage",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getCallingActivity",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "isFinishing",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finish",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finishFromChild",
            args = {Activity.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finishActivity",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finishActivityFromChild",
            args = {Activity.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onActivityResult",
            args = {int.class, int.class, Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "createPendingResult",
            args = {int.class, Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setRequestedOrientation",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getRequestedOrientation",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getTaskId",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "isTaskRoot",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "moveTaskToBack",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getLocalClassName",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getComponentName",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getPreferences",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getSystemService",
            args = {String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setTitle",
            args = {CharSequence.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setTitle",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setTitleColor",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getTitle",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getTitleColor",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onTitleChanged",
            args = {CharSequence.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onChildTitleChanged",
            args = {Activity.class, CharSequence.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgressBarVisibility",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgressBarIndeterminateVisibility",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgressBarIndeterminate",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgress",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setSecondaryProgress",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setVolumeControlStream",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getVolumeControlStream",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "runOnUiThread",
            args = {Runnable.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateView",
            args = {String.class, Context.class, AttributeSet.class}
        )
    })
    public void testFinishStart() {
        mIntent = mTopIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_FINISH_START);
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "Activity",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onUserInteraction",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onUserLeaveHint",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setVisible",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finalize",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "closeContextMenu",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getInstanceCount",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,

            method = "getIntent",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setIntent",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getApplication",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "isChild",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getParent",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWindowManager",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWindow",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getCurrentFocus",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWallpaperDesiredMinimumWidth",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWallpaperDesiredMinimumHeight",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreate",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onRestoreInstanceState",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPostCreate",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onStart",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onRestart",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onResume",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPostResume",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onNewIntent",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onSaveInstanceState",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPause",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateThumbnail",
            args = {Bitmap.class, Canvas.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateDescription",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onStop",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onDestroy",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onConfigurationChanged",
            args = {Configuration.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getChangingConfigurations",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getLastNonConfigurationInstance",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onRetainNonConfigurationInstance",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onLowMemory",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "managedQuery",
            args = {Uri.class, String[].class, String.class, String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "managedQuery",
            args = {Uri.class, String[].class, String.class, String[].class, String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "managedCommitUpdates",
            args = {Cursor.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startManagingCursor",
            args = {Cursor.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "stopManagingCursor",
            args = {Cursor.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setPersistent",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "findViewById",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setContentView",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setContentView",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setContentView",
            args = {View.class, ViewGroup.LayoutParams.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "addContentView",
            args = {View.class, ViewGroup.LayoutParams.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setDefaultKeyMode",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onKeyDown",
            args = {int.class, KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onKeyUp",
            args = {int.class, KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onKeyMultiple",
            args = {int.class, int.class, KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onTouchEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onTrackballEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onWindowAttributesChanged",
            args = {LayoutParams.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onContentChanged",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onWindowFocusChanged",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "hasWindowFocus",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dispatchKeyEvent",
            args = {KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dispatchTouchEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dispatchTrackballEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreatePanelView",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreatePanelMenu",
            args = {int.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPreparePanel",
            args = {int.class, View.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onMenuOpened",
            args = {int.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onMenuItemSelected",
            args = {int.class, MenuItem.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPanelClosed",
            args = {int.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateOptionsMenu",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPrepareOptionsMenu",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onOptionsItemSelected",
            args = {MenuItem.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onOptionsMenuClosed",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "openOptionsMenu",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "closeOptionsMenu",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateContextMenu",
            args = {ContextMenu.class, View.class, ContextMenuInfo.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "registerForContextMenu",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "unregisterForContextMenu",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "openContextMenu",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onContextItemSelected",
            args = {MenuItem.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onContextMenuClosed",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPrepareDialog",
            args = {int.class, Dialog.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "showDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dismissDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "removeDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onSearchRequested",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startSearch",
            args = {String.class, boolean.class, Bundle.class, boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "takeKeyEvents",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "requestWindowFeature",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawableResource",
            args = {int.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawableUri",
            args = {int.class, Uri.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawable",
            args = {int.class, Drawable.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawableAlpha",
            args = {int.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getLayoutInflater",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getMenuInflater",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onApplyThemeResource",
            args = {Theme.class, int.class, boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivityForResult",
            args = {Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivity",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivityIfNeeded",
            args = {Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startNextMatchingActivity",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivityFromChild",
            args = {Activity.class, Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setResult",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setResult",
            args = {int.class, Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getCallingPackage",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getCallingActivity",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "isFinishing",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finish",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finishFromChild",
            args = {Activity.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finishActivity",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finishActivityFromChild",
            args = {Activity.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onActivityResult",
            args = {int.class, int.class, Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "createPendingResult",
            args = {int.class, Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setRequestedOrientation",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getRequestedOrientation",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getTaskId",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "isTaskRoot",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "moveTaskToBack",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getLocalClassName",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getComponentName",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getPreferences",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getSystemService",
            args = {String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setTitle",
            args = {CharSequence.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setTitle",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setTitleColor",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getTitle",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getTitleColor",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onTitleChanged",
            args = {CharSequence.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onChildTitleChanged",
            args = {Activity.class, CharSequence.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgressBarVisibility",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgressBarIndeterminateVisibility",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgressBarIndeterminate",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgress",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setSecondaryProgress",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setVolumeControlStream",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getVolumeControlStream",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "runOnUiThread",
            args = {Runnable.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateView",
            args = {String.class, Context.class, AttributeSet.class}
        )
    })
    public void testTabFinishCreate() {
        mIntent = mTabIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_FINISH_CREATE);
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "Activity",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onUserInteraction",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onUserLeaveHint",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setVisible",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "closeContextMenu",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finalize",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getInstanceCount",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getIntent",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setIntent",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getApplication",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "isChild",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getParent",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWindowManager",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWindow",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getCurrentFocus",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWallpaperDesiredMinimumWidth",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getWallpaperDesiredMinimumHeight",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreate",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onRestoreInstanceState",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPostCreate",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onStart",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onRestart",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onResume",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPostResume",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onNewIntent",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onSaveInstanceState",
            args = {Bundle.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPause",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateThumbnail",
            args = {Bitmap.class, Canvas.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateDescription",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onStop",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onDestroy",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onConfigurationChanged",
            args = {Configuration.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getChangingConfigurations",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getLastNonConfigurationInstance",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onRetainNonConfigurationInstance",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onLowMemory",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "managedQuery",
            args = {Uri.class, String[].class, String.class, String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "managedQuery",
            args = {Uri.class, String[].class, String.class, String[].class, String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "managedCommitUpdates",
            args = {Cursor.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startManagingCursor",
            args = {Cursor.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "stopManagingCursor",
            args = {Cursor.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setPersistent",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "findViewById",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setContentView",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setContentView",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setContentView",
            args = {View.class, ViewGroup.LayoutParams.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "addContentView",
            args = {View.class, ViewGroup.LayoutParams.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setDefaultKeyMode",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onKeyDown",
            args = {int.class, KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onKeyUp",
            args = {int.class, KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onKeyMultiple",
            args = {int.class, int.class, KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onTouchEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onTrackballEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onWindowAttributesChanged",
            args = {LayoutParams.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onContentChanged",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onWindowFocusChanged",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "hasWindowFocus",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dispatchKeyEvent",
            args = {KeyEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dispatchTouchEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dispatchTrackballEvent",
            args = {MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreatePanelView",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreatePanelMenu",
            args = {int.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPreparePanel",
            args = {int.class, View.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onMenuOpened",
            args = {int.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onMenuItemSelected",
            args = {int.class, MenuItem.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPanelClosed",
            args = {int.class, Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateOptionsMenu",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPrepareOptionsMenu",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onOptionsItemSelected",
            args = {MenuItem.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onOptionsMenuClosed",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "openOptionsMenu",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "closeOptionsMenu",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateContextMenu",
            args = {ContextMenu.class, View.class, ContextMenuInfo.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "registerForContextMenu",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "unregisterForContextMenu",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "openContextMenu",
            args = {View.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onContextItemSelected",
            args = {MenuItem.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onContextMenuClosed",
            args = {Menu.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onPrepareDialog",
            args = {int.class, Dialog.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "showDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "dismissDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "removeDialog",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onSearchRequested",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startSearch",
            args = {String.class, boolean.class, Bundle.class, boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "takeKeyEvents",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "requestWindowFeature",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawableResource",
            args = {int.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawableUri",
            args = {int.class, Uri.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawable",
            args = {int.class, Drawable.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setFeatureDrawableAlpha",
            args = {int.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getLayoutInflater",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getMenuInflater",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onApplyThemeResource",
            args = {Theme.class, int.class, boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivityForResult",
            args = {Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivity",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivityIfNeeded",
            args = {Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startNextMatchingActivity",
            args = {Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startActivityFromChild",
            args = {Activity.class, Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setResult",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setResult",
            args = {int.class, Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getCallingPackage",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getCallingActivity",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "isFinishing",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finish",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finishFromChild",
            args = {Activity.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finishActivity",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "finishActivityFromChild",
            args = {Activity.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onActivityResult",
            args = {int.class, int.class, Intent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "createPendingResult",
            args = {int.class, Intent.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setRequestedOrientation",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getRequestedOrientation",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getTaskId",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "isTaskRoot",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "moveTaskToBack",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getLocalClassName",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getComponentName",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getPreferences",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getSystemService",
            args = {String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setTitle",
            args = {CharSequence.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setTitle",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setTitleColor",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getTitle",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getTitleColor",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onTitleChanged",
            args = {CharSequence.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onChildTitleChanged",
            args = {Activity.class, CharSequence.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgressBarVisibility",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgressBarIndeterminateVisibility",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgressBarIndeterminate",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setProgress",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setSecondaryProgress",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setVolumeControlStream",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getVolumeControlStream",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "runOnUiThread",
            args = {Runnable.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onCreateView",
            args = {String.class, Context.class, AttributeSet.class}
        )
    })
    public void testFinishCreate() {
        mIntent = mTopIntent;
        runLaunchpad(LaunchpadActivity.LIFECYCLE_FINISH_CREATE);
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "Activity",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onUserInteraction",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onUserLeaveHint",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setVisible",
            args = {boolean.class}
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
        runMultipleLaunchpads("testTabDialog", getClass(),
                LaunchpadActivity.LIFECYCLE_DIALOG_ON_RESUME,
                LaunchpadActivity.LIFECYCLE_DIALOG_ON_STOP);
}

@TestTargets({
//Synthetic comment -- @@ -4150,9 +1420,7 @@
})
public void testDialog() {
mIntent = mTopIntent;
        runMultipleLaunchpads("testDialog", getClass(),
                LaunchpadActivity.LIFECYCLE_DIALOG_ON_RESUME,
                LaunchpadActivity.LIFECYCLE_DIALOG_ON_STOP);
}

@TestTargets({
//Synthetic comment -- @@ -4834,9 +2102,7 @@
})
public void testTabScreen() {
mIntent = mTabIntent;
        runMultipleLaunchpads("testTabScreen", getClass(),
                LaunchpadActivity.LIFECYCLE_SCREEN_ON_RESUME,
                LaunchpadActivity.LIFECYCLE_SCREEN_ON_STOP);
}

@TestTargets({
//Synthetic comment -- @@ -5519,9 +2785,7 @@
})
public void testScreen() {
mIntent = mTopIntent;
        runMultipleLaunchpads("testScreen", getClass(),
                LaunchpadActivity.LIFECYCLE_SCREEN_ON_RESUME,
                LaunchpadActivity.LIFECYCLE_SCREEN_ON_STOP);
}

@TestTargets({







