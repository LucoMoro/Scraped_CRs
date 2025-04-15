/*Reattach header view after DPAD scroll.

A header view that was scrolled off screen using the DPAD would not be
reattached properly when scrolled back into view, due to the flag
recycledHeaderFooter. Solved this by using detachViewFromParent()
instead of removeViewInLayout(). Compare to
AbsListView.trackMotionScroll().

Change-Id:I0ac0ec0f9bf23bc62430c1f62ae7d1a8570b0a24*/
//Synthetic comment -- diff --git a/core/java/android/widget/ListView.java b/core/java/android/widget/ListView.java
//Synthetic comment -- index e1a1894..4aa3b18 100644

//Synthetic comment -- @@ -2859,11 +2859,9 @@
while (first.getBottom() < listTop) {
AbsListView.LayoutParams layoutParams = (LayoutParams) first.getLayoutParams();
if (recycleBin.shouldRecycleViewType(layoutParams.viewType)) {
                    detachViewFromParent(first);
recycleBin.addScrapView(first);
                } else {
                    removeViewInLayout(first);
}
first = getChildAt(0);
mFirstPosition++;
}
//Synthetic comment -- @@ -2890,11 +2888,9 @@
while (last.getTop() > listBottom) {
AbsListView.LayoutParams layoutParams = (LayoutParams) last.getLayoutParams();
if (recycleBin.shouldRecycleViewType(layoutParams.viewType)) {
                    detachViewFromParent(last);
recycleBin.addScrapView(last);
                } else {
                    removeViewInLayout(last);
}
last = getChildAt(--lastIndex);
}
}








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/widget/listview/focus/ListWithEditTextHeaderTest.java b/core/tests/coretests/src/android/widget/listview/focus/ListWithEditTextHeaderTest.java
//Synthetic comment -- index b9051e9..532b9d1 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.listview.ListWithEditTextHeader;
//Synthetic comment -- @@ -63,4 +64,22 @@
assertTrue("header does not have focus", mListView.getChildAt(0).isFocused());
assertEquals("something is selected", AbsListView.INVALID_POSITION, mListView.getSelectedItemPosition());
}
}







