/*Fix regression in ListView click handling

Regression caused by patch a607223f34

If an item click handler results in the ListView becoming detached
from the window, the performClick runnable could be executed twice.
Null out the mTouchModeReset runnable before running the click handler
so that it is not run by the detach handler.

Change-Id:I2fab60232027bb4876f9912088e0ffb9b17d308c*/
//Synthetic comment -- diff --git a/core/java/android/widget/AbsListView.java b/core/java/android/widget/AbsListView.java
//Synthetic comment -- index a6b7dba..57bf0d3 100644

//Synthetic comment -- @@ -3416,13 +3416,13 @@
mTouchModeReset = new Runnable() {
@Override
public void run() {
mTouchMode = TOUCH_MODE_REST;
child.setPressed(false);
setPressed(false);
if (!mDataChanged) {
performClick.run();
}
                                    mTouchModeReset = null;
}
};
postDelayed(mTouchModeReset,







