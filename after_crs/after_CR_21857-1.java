/*Remove Broken TouchDelegateTest#testOn

Bug 3188260

Change-Id:Ibcc74111be172b7afe668bbb016f2fce9745e4dc*/




//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/TouchDelegateTest.java b/tests/tests/view/src/android/view/cts/TouchDelegateTest.java
//Synthetic comment -- index f82b903..0a78d1f 100644

//Synthetic comment -- @@ -16,6 +16,11 @@

package android.view.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.cts.MockActivity;
//Synthetic comment -- @@ -25,45 +30,22 @@
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

@TestTargetClass(TouchDelegate.class)
public class TouchDelegateTest extends ActivityInstrumentationTestCase2<MockActivity> {
private static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
private static final int ACTION_DOWN = MotionEvent.ACTION_DOWN;

private Activity mActivity;
private Instrumentation mInstrumentation;
private Button mButton;
private Rect mRect;

private int mXInside;
private int mYInside;

private Exception mException;

//Synthetic comment -- @@ -76,7 +58,6 @@
super.setUp();
mActivity = getActivity();
mInstrumentation = getInstrumentation();

mButton = new Button(mActivity);
mActivity.runOnUiThread(new Runnable() {
//Synthetic comment -- @@ -99,38 +80,11 @@
int bottom = mButton.getBottom();
mXInside = (mButton.getLeft() + right) / 3;
mYInside = (mButton.getTop() + bottom) / 3;

mRect = new Rect();
mButton.getHitRect(mRect);
}

@TestTargets({
@TestTargetNew(
level = TestLevel.COMPLETE,
//Synthetic comment -- @@ -154,59 +108,6 @@
assertFalse(touchDelegate.mOnTouchEventCalled);
view.onTouchEvent(MotionEvent.obtain(0, 0, ACTION_DOWN, mXInside, mYInside, 0));
assertTrue(touchDelegate.mOnTouchEventCalled);
}

class MockTouchDelegate extends TouchDelegate {







