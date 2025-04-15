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

    public static int convertDipToPixels(Context context, int dip) {
      float density = context.getResources().getDisplayMetrics().density;
      return (int) (density * dip + 0.5f);
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
import android.widget.cts.WidgetTestUtils;

/**
* Test {@link ScrollingMovementMethod}. The class is an implementation of interface
//Synthetic comment -- @@ -117,9 +118,9 @@
mTextView.setText("hello world", BufferType.SPANNABLE);
mTextView.setSingleLine();
mSpannable = (Spannable) mTextView.getText();
                int width = WidgetTestUtils.convertDipToPixels(getActivity(), LITTLE_SPACE);
getActivity().setContentView(mTextView,
                        new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
}
});
assertNotNull(mTextView.getLayout());
//Synthetic comment -- @@ -340,9 +341,9 @@
*/
runActionOnUiThread(new Runnable() {
public void run() {
                int width = WidgetTestUtils.convertDipToPixels(getActivity(), LITTLE_SPACE);
getActivity().setContentView(mTextView,
                        new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
}
});
assertNotNull(mTextView.getLayout());
//Synthetic comment -- @@ -474,9 +475,9 @@
public void run() {
mTextView.setText("short");
mTextView.setSingleLine();
                int width = WidgetTestUtils.convertDipToPixels(getActivity(), LITTLE_SPACE);
getActivity().setContentView(mTextView,
                        new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
}
});
assertNotNull(mTextView.getLayout());
//Synthetic comment -- @@ -832,9 +833,9 @@

runActionOnUiThread(new Runnable() {
public void run() {
                int height = WidgetTestUtils.convertDipToPixels(getActivity(), LITTLE_SPACE);
getActivity().setContentView(mTextView,
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, height));
}
});
Layout layout = mTextView.getLayout();
//Synthetic comment -- @@ -889,12 +890,6 @@
args = {TextView.class, Spannable.class}
)
})
public void testHorizontalMovement() throws Throwable {
/*
* All these assertions depends on whether the TextView has a layout.The text view will not
//Synthetic comment -- @@ -909,9 +904,9 @@
public void run() {
mTextView.setText("short");
mTextView.setSingleLine();
                int width = WidgetTestUtils.convertDipToPixels(getActivity(), LITTLE_SPACE);
getActivity().setContentView(mTextView,
                        new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
}
});
assertNotNull(mTextView.getLayout());
//Synthetic comment -- @@ -948,34 +943,6 @@
}
}));
assertEquals(previousScrollX, mTextView.getScrollX());
}

@TestTargetNew(








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/HorizontalScrollViewTest.java b/tests/tests/widget/src/android/widget/cts/HorizontalScrollViewTest.java
//Synthetic comment -- index d26b217..31b0b7b 100644

//Synthetic comment -- @@ -669,10 +669,12 @@
mScrollView.setSmoothScrollingEnabled(true);
assertEquals(0, mScrollView.getScrollX());

        final int velocityX = WidgetTestUtils.convertDipToPixels(getActivity(), 2000);

// fling towards right
runTestOnUiThread(new Runnable() {
public void run() {
                mScrollView.fling(velocityX);
}
});
delayedCheckFling(0, true);
//Synthetic comment -- @@ -681,7 +683,7 @@
// fling towards left
runTestOnUiThread(new Runnable() {
public void run() {
                mScrollView.fling(-velocityX);
}
});
delayedCheckFling(currentX, false);
//Synthetic comment -- @@ -872,7 +874,7 @@
}
return mScrollView.getScrollX() < startPosition;
}
        }.run();

new DelayedCheck() {
private int mPreviousScrollX = mScrollView.getScrollX();







