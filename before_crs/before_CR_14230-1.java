/*Use Dips in Some Tests

Issue 7124 6953

Use dips rather than pixels so that the tests scale across
different dpi devices. Using dips in HorizontalScrollviewTest
so that less pixels are required to fling on ldpi devices.
ScrollingMovementMethodTest needs to use dips, so that the
view will only be scrolled twice before hitting the edge.

Change-Id:I6cec524d780692ee3193be0a5a7b9718edcb17c1*/
//Synthetic comment -- diff --git a/tests/src/android/widget/cts/WidgetTestUtils.java b/tests/src/android/widget/cts/WidgetTestUtils.java
//Synthetic comment -- index b6e0140..bf2169f 100644

//Synthetic comment -- @@ -103,6 +103,11 @@
actual, 3);
}

/**
* Retrieve a bitmap that can be used for comparison on any density
* @param resources








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/ScrollingMovementMethodTest.java b/tests/tests/text/src/android/text/method/cts/ScrollingMovementMethodTest.java
//Synthetic comment -- index 8e6ddde..fc83d32 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/**
* Test {@link ScrollingMovementMethod}. The class is an implementation of interface
//Synthetic comment -- @@ -117,9 +118,9 @@
mTextView.setText("hello world", BufferType.SPANNABLE);
mTextView.setSingleLine();
mSpannable = (Spannable) mTextView.getText();
getActivity().setContentView(mTextView,
                        new ViewGroup.LayoutParams(LITTLE_SPACE,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
}
});
assertNotNull(mTextView.getLayout());
//Synthetic comment -- @@ -340,9 +341,9 @@
*/
runActionOnUiThread(new Runnable() {
public void run() {
getActivity().setContentView(mTextView,
                        new ViewGroup.LayoutParams(LITTLE_SPACE,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
}
});
assertNotNull(mTextView.getLayout());
//Synthetic comment -- @@ -474,9 +475,9 @@
public void run() {
mTextView.setText("short");
mTextView.setSingleLine();
getActivity().setContentView(mTextView,
                        new ViewGroup.LayoutParams(LITTLE_SPACE,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
}
});
assertNotNull(mTextView.getLayout());
//Synthetic comment -- @@ -832,9 +833,9 @@

runActionOnUiThread(new Runnable() {
public void run() {
getActivity().setContentView(mTextView,
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                LITTLE_SPACE));
}
});
Layout layout = mTextView.getLayout();
//Synthetic comment -- @@ -889,12 +890,6 @@
args = {TextView.class, Spannable.class}
)
})
    @ToBeFixed(bug = "1695243", explanation = "Android API javadocs are incomplete. @throws clause "
            + "should be added into javadoc of "
            + "ScrollingMovementMethod#left(TextView, Spannable) and "
            + "ScrollingMovementMethod#right(TextView, Spannable)"
            + "when the param widget or buffer is null")
    @KnownFailure(value="bug 2323405, needs investigation")
public void testHorizontalMovement() throws Throwable {
/*
* All these assertions depends on whether the TextView has a layout.The text view will not
//Synthetic comment -- @@ -909,9 +904,9 @@
public void run() {
mTextView.setText("short");
mTextView.setSingleLine();
getActivity().setContentView(mTextView,
                        new ViewGroup.LayoutParams(LITTLE_SPACE,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
}
});
assertNotNull(mTextView.getLayout());
//Synthetic comment -- @@ -948,34 +943,6 @@
}
}));
assertEquals(previousScrollX, mTextView.getScrollX());

        runActionOnUiThread(new Runnable() {
            public void run() {
                try {
                    method.right(null, mSpannable);
                } catch (NullPointerException e) {
                    // NPE is acceptable
                }

                try {
                    method.right(mTextView, null);
                } catch (NullPointerException e) {
                    // NPE is acceptable
                }

                try {
                    method.left(null, mSpannable);
                } catch (NullPointerException e) {
                    // NPE is acceptable
                }

                try {
                    method.left(mTextView, null);
                } catch (NullPointerException e) {
                    // NPE is acceptable
                }
            }
        });
}

@TestTargetNew(








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/HorizontalScrollViewTest.java b/tests/tests/widget/src/android/widget/cts/HorizontalScrollViewTest.java
//Synthetic comment -- index d26b217..31b0b7b 100644

//Synthetic comment -- @@ -669,10 +669,12 @@
mScrollView.setSmoothScrollingEnabled(true);
assertEquals(0, mScrollView.getScrollX());

// fling towards right
runTestOnUiThread(new Runnable() {
public void run() {
                mScrollView.fling(2000);
}
});
delayedCheckFling(0, true);
//Synthetic comment -- @@ -681,7 +683,7 @@
// fling towards left
runTestOnUiThread(new Runnable() {
public void run() {
                mScrollView.fling(-2000);
}
});
delayedCheckFling(currentX, false);
//Synthetic comment -- @@ -872,7 +874,7 @@
}
return mScrollView.getScrollX() < startPosition;
}
        };

new DelayedCheck() {
private int mPreviousScrollX = mScrollView.getScrollX();







