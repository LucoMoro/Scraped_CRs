/*Fixes an issue that occured unexpected exception "pointerIndex out of range".

The findNewActiveIndex method may return -1.
So, the code should check case of -1 before event.getPointerId
and if index0 is -1, gesture should be ended immediately.

Change-Id:I4aae5c84e3db61d10b0bfcfa7bfa6b9115231a52*/
//Synthetic comment -- diff --git a/core/java/android/view/ScaleGestureDetector.java b/core/java/android/view/ScaleGestureDetector.java
//Synthetic comment -- index 5d2c1a7..bbb5ade 100644

//Synthetic comment -- @@ -220,8 +220,10 @@
mActiveId1 = event.getPointerId(index1);
if (index0 < 0 || index0 == index1) {
// Probably someone sending us a broken event stream.
                    index0 = findNewActiveIndex(event, index0 == index1 ? -1 : mActiveId1, index0);
                    mActiveId0 = event.getPointerId(index0);
}
mActive0MostRecent = false;

//Synthetic comment -- @@ -377,13 +379,10 @@
int index0 = event.findPointerIndex(mActiveId0);
if (index0 < 0 || mActiveId0 == mActiveId1) {
// Probably someone sending us a broken event stream.
                        Log.e(TAG, "Got " + MotionEvent.actionToString(action) +
                                " with bad state while a gesture was in progress. " +
                                "Did you forget to pass an event to " +
                                "ScaleGestureDetector#onTouchEvent?");
                        index0 = findNewActiveIndex(event,
                                mActiveId0 == mActiveId1 ? -1 : mActiveId1, index0);
                        mActiveId0 = event.getPointerId(index0);
}

setContext(event);
//Synthetic comment -- @@ -483,6 +482,27 @@
return handled;
}

private int findNewActiveIndex(MotionEvent ev, int otherActiveId, int oldIndex) {
final int pointerCount = ev.getPointerCount();








