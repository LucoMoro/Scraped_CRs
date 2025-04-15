/*Remove Broken TouchDelegateTest#testOn

Bug 3188260

Change-Id:Ibcc74111be172b7afe668bbb016f2fce9745e4dc*/
//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/TouchDelegateTest.java b/tests/tests/view/src/android/view/cts/TouchDelegateTest.java
//Synthetic comment -- index f82b903..0a78d1f 100644

//Synthetic comment -- @@ -16,6 +16,11 @@

package android.view.cts;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.cts.MockActivity;
//Synthetic comment -- @@ -25,45 +30,22 @@
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

@TestTargetClass(TouchDelegate.class)
public class TouchDelegateTest extends ActivityInstrumentationTestCase2<MockActivity> {
private static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
private static final int ACTION_DOWN = MotionEvent.ACTION_DOWN;
    private static final int ACTION_UP = MotionEvent.ACTION_UP;
    private static final int ACTION_CANCEL = MotionEvent.ACTION_CANCEL;
    private static final int ACTION_MOVE = MotionEvent.ACTION_MOVE;

    private ViewConfiguration mViewConfig;
private Activity mActivity;
private Instrumentation mInstrumentation;
    private TouchDelegate mTouchDelegate;
private Button mButton;
private Rect mRect;

private int mXInside;
private int mYInside;
    private int mXOutside;
    private int mYOutside;
    private int mScaledTouchSlop;

    private MotionEvent mActionDownInside;
    private MotionEvent mActionDownOutside;
    private MotionEvent mActionUpInside;
    private MotionEvent mActionUpOutside;
    private MotionEvent mActionMoveInside;
    private MotionEvent mActionMoveOutside;
    private MotionEvent mActionCancelInside;
    private MotionEvent mActionCancelOutside;

private Exception mException;

//Synthetic comment -- @@ -76,7 +58,6 @@
super.setUp();
mActivity = getActivity();
mInstrumentation = getInstrumentation();
        mViewConfig = ViewConfiguration.get(mActivity);

mButton = new Button(mActivity);
mActivity.runOnUiThread(new Runnable() {
//Synthetic comment -- @@ -99,38 +80,11 @@
int bottom = mButton.getBottom();
mXInside = (mButton.getLeft() + right) / 3;
mYInside = (mButton.getTop() + bottom) / 3;
        mScaledTouchSlop = mViewConfig.getScaledTouchSlop() << 1;
        mXOutside = right + mScaledTouchSlop;
        mYOutside = bottom + mScaledTouchSlop;

mRect = new Rect();
mButton.getHitRect(mRect);
}

    private void init() {
        mTouchDelegate = new TouchDelegate(mRect, mButton);

        mActionDownInside = MotionEvent.obtain(0, 0, ACTION_DOWN, mXInside, mYInside, 0);
        mActionDownOutside = MotionEvent.obtain(0, 0, ACTION_DOWN, mXOutside, mYOutside, 0);
        mActionUpInside = MotionEvent.obtain(0, 0, ACTION_UP, mXInside, mYInside, 0);
        mActionUpOutside = MotionEvent.obtain(0, 0, ACTION_UP, mXOutside, mYOutside, 0);
        mActionMoveInside = MotionEvent.obtain(0, 0, ACTION_MOVE, mXInside, mYInside, 0);
        mActionMoveOutside = MotionEvent.obtain(0, 0, ACTION_MOVE, mXOutside, mYOutside, 0);
        mActionCancelInside = MotionEvent.obtain(0, 0, ACTION_CANCEL, mXInside, mYInside, 0);
        mActionCancelOutside = MotionEvent.obtain(0, 0, ACTION_CANCEL, mXOutside, mYOutside, 0);
    }

    private void clear() {
        mActionDownInside.recycle();
        mActionDownOutside.recycle();
        mActionUpInside.recycle();
        mActionUpOutside.recycle();
        mActionMoveInside.recycle();
        mActionMoveOutside.recycle();
        mActionCancelInside.recycle();
        mActionCancelOutside.recycle();
    }

@TestTargets({
@TestTargetNew(
level = TestLevel.COMPLETE,
//Synthetic comment -- @@ -154,59 +108,6 @@
assertFalse(touchDelegate.mOnTouchEventCalled);
view.onTouchEvent(MotionEvent.obtain(0, 0, ACTION_DOWN, mXInside, mYInside, 0));
assertTrue(touchDelegate.mOnTouchEventCalled);

    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "onTouchEvent",
        args = {MotionEvent.class}
    )
    @UiThreadTest
    @BrokenTest("Will fail in batch mode but can pass if only run this TestCase")
    public void testOn() {

        init();
        assertTrue(mTouchDelegate.onTouchEvent(mActionDownInside));
        clear();

        init();
        assertFalse(mTouchDelegate.onTouchEvent(mActionDownOutside));
        clear();

        init();
        assertTrue(mTouchDelegate.onTouchEvent(mActionDownInside));
        assertTrue(mTouchDelegate.onTouchEvent(mActionUpInside));
        clear();

        init();
        assertFalse(mTouchDelegate.onTouchEvent(mActionUpInside));
        clear();

        init();
        assertTrue(mTouchDelegate.onTouchEvent(mActionDownInside));
        assertTrue(mTouchDelegate.onTouchEvent(mActionUpOutside));
        clear();

        init();
        assertFalse(mTouchDelegate.onTouchEvent(mActionMoveInside));
        clear();

        init();
        assertTrue(mTouchDelegate.onTouchEvent(mActionDownInside));
        assertTrue(mTouchDelegate.onTouchEvent(mActionMoveInside));
        clear();

        init();
        assertTrue(mTouchDelegate.onTouchEvent(mActionDownInside));
        assertTrue(mTouchDelegate.onTouchEvent(mActionMoveOutside));
        clear();

        init();
        assertTrue(mTouchDelegate.onTouchEvent(mActionDownInside));
        assertTrue(mTouchDelegate.onTouchEvent(mActionCancelInside));
        assertFalse(mTouchDelegate.onTouchEvent(mActionUpInside));
        clear();
}

class MockTouchDelegate extends TouchDelegate {







