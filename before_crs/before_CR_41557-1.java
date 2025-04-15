/*JAVACRASH on com.android.browser

Fixes an issue that occured unexpected exception "pointerIndex out of range".
The findNewActiveIndex method may return -1.So, the code should check case of -1
before event.getPointerId and if index0 is -1, gesture should be ended immediately.

original aosp patch: d8a3663afc1f4db38a28f9eb93c8596c43ccb699

Change-Id:I45771509116cbad860f9f34af39e63b044b64248Author: Keiji Ariyama <keiji_ariyama@c-lis.co.jp>
Signed-off-by: Bin Xu <binx.xu@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 34985*/
//Synthetic comment -- diff --git a/core/java/android/view/ScaleGestureDetector.java b/core/java/android/view/ScaleGestureDetector.java
//Synthetic comment -- index 73f94bc..a62fc18 100644

//Synthetic comment -- @@ -208,8 +208,10 @@
mActiveId1 = event.getPointerId(index1);
if (index0 < 0 || index0 == index1) {
// Probably someone sending us a broken event stream.
                        index0 = findNewActiveIndex(event, mActiveId1, -1);
                        mActiveId0 = event.getPointerId(index0);
}
mActive0MostRecent = false;

//Synthetic comment -- @@ -237,12 +239,10 @@
int index0 = event.findPointerIndex(mActiveId0);
if (index0 < 0 || mActiveId0 == mActiveId1) {
// Probably someone sending us a broken event stream.
                        Log.e(TAG, "Got " + MotionEvent.actionToString(action) +
                                " with bad state while a gesture was in progress. " +
                                "Did you forget to pass an event to " +
                                "ScaleGestureDetector#onTouchEvent?");
                        index0 = findNewActiveIndex(event, mActiveId1, -1);
                        mActiveId0 = event.getPointerId(index0);
}

setContext(event);
//Synthetic comment -- @@ -342,6 +342,27 @@
return handled;
}

private int findNewActiveIndex(MotionEvent ev, int otherActiveId, int removedPointerIndex) {
final int pointerCount = ev.getPointerCount();








