/*Reattach header view after DPAD scroll.

A header view that was scrolled off screen using the DPAD would not be
reattached properly when scrolled back into view, due to the flag
recycledHeaderFooter. Solved this by using detachViewFromParent()
instead of removeViewInLayout(). Compare to
AbsListView.trackMotionScroll().

Change-Id:I0ac0ec0f9bf23bc62430c1f62ae7d1a8570b0a24*/




//Synthetic comment -- diff --git a/core/java/android/widget/ListView.java b/core/java/android/widget/ListView.java
//Synthetic comment -- index 7f7a3a7..b18032b 100644

//Synthetic comment -- @@ -2916,11 +2916,9 @@
while (first.getBottom() < listTop) {
AbsListView.LayoutParams layoutParams = (LayoutParams) first.getLayoutParams();
if (recycleBin.shouldRecycleViewType(layoutParams.viewType)) {
recycleBin.addScrapView(first, mFirstPosition);
}
                detachViewFromParent(first);
first = getChildAt(0);
mFirstPosition++;
}
//Synthetic comment -- @@ -2947,11 +2945,9 @@
while (last.getTop() > listBottom) {
AbsListView.LayoutParams layoutParams = (LayoutParams) last.getLayoutParams();
if (recycleBin.shouldRecycleViewType(layoutParams.viewType)) {
recycleBin.addScrapView(last, mFirstPosition+lastIndex);
}
                detachViewFromParent(last);
last = getChildAt(--lastIndex);
}
}








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/widget/listview/focus/ListWithEditTextHeaderTest.java b/core/tests/coretests/src/android/widget/listview/focus/ListWithEditTextHeaderTest.java
//Synthetic comment -- index b9051e9..532b9d1 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.listview.ListWithEditTextHeader;
//Synthetic comment -- @@ -63,4 +64,22 @@
assertTrue("header does not have focus", mListView.getChildAt(0).isFocused());
assertEquals("something is selected", AbsListView.INVALID_POSITION, mListView.getSelectedItemPosition());
}

    @LargeTest
    public void testScrollingDoesNotDetachHeaderViewFromWindow() {
        View header = mListView.getChildAt(0);
        assertNotNull("header is not attached to a window (?!)", header.getWindowToken());

        // Scroll header off the screen and back onto the screen
        int numItemsOnScreen = mListView.getChildCount();
        for (int i = 0; i < numItemsOnScreen; i++) {
            sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
        }
        for (int i = 0; i < numItemsOnScreen; i++) {
            sendKeys(KeyEvent.KEYCODE_DPAD_UP);
        }

        // Make sure the header was not accidentally left detached from its window
        assertNotNull("header has lost its window", header.getWindowToken());
    }
}







