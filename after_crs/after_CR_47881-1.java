/*Ensure running mTouchModeReset when AbsListView is detached

mTouchModeReset should be executed when AbsListView is detached from window.
If not, when the AbsListView is re-attaced to window,
the child can retain a pressed state.

You can see this problem easily when you double-touch an item of
option menu very quickly.

Change-Id:I5aaa1fd5b95847efb2f5f1b5ec7cabe8eb85b237Signed-off-by: Sangkyu Lee <sk82.lee@lge.com>*/




//Synthetic comment -- diff --git a/core/java/android/widget/AbsListView.java b/core/java/android/widget/AbsListView.java
//Synthetic comment -- index 33a8531..a6b7dba 100644

//Synthetic comment -- @@ -2637,7 +2637,7 @@

if (mTouchModeReset != null) {
removeCallbacks(mTouchModeReset);
            mTouchModeReset.run();
}
mIsAttached = false;
}
//Synthetic comment -- @@ -3422,6 +3422,7 @@
if (!mDataChanged) {
performClick.run();
}
                                    mTouchModeReset = null;
}
};
postDelayed(mTouchModeReset,







