/*Fixes a issue that occured unexpected exception "pointerIndex out of range".

The findNewActiveIndex method may return -1.
So, the code should check case of -1 before event.getPointerId.

Change-Id:I4aae5c84e3db61d10b0bfcfa7bfa6b9115231a52*/
//Synthetic comment -- diff --git a/core/java/android/view/ScaleGestureDetector.java b/core/java/android/view/ScaleGestureDetector.java
//Synthetic comment -- index 5d2c1a7..b7220a0 100644

//Synthetic comment -- @@ -221,7 +221,7 @@
if (index0 < 0 || index0 == index1) {
// Probably someone sending us a broken event stream.
index0 = findNewActiveIndex(event, index0 == index1 ? -1 : mActiveId1, index0);
                    mActiveId0 = event.getPointerId(index0);
}
mActive0MostRecent = false;

//Synthetic comment -- @@ -383,7 +383,7 @@
"ScaleGestureDetector#onTouchEvent?");
index0 = findNewActiveIndex(event,
mActiveId0 == mActiveId1 ? -1 : mActiveId1, index0);
                        mActiveId0 = event.getPointerId(index0);
}

setContext(event);







