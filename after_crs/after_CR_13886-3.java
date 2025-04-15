/*Remove the window from being a target for motion events when it is destroyed.

If window was destroyed when touch was pressed this destroyed
window was still considered as a target for motion events.
In some cases this could result with hang up inside event dispatch procedure.

Change-Id:I2ede17a40b14b2f509ab3d2560abb65292324f45*/




//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index b90b03b..758984e 100644

//Synthetic comment -- @@ -2067,6 +2067,7 @@
private void removeWindowInnerLocked(Session session, WindowState win) {
mKeyWaiter.finishedKey(session, win.mClient, true,
KeyWaiter.RETURN_NOTHING);
        mKeyWaiter.releaseMotionTarget(win);
mKeyWaiter.releasePendingPointerLocked(win.mSession);
mKeyWaiter.releasePendingTrackballLocked(win.mSession);

//Synthetic comment -- @@ -5971,6 +5972,12 @@
}
}

        void releaseMotionTarget(WindowState win) {
            if (mMotionTarget == win) {
                mMotionTarget = null;
            }
        }

MotionEvent finishedKey(Session session, IWindow client, boolean force,
int returnWhat) {
if (DEBUG_INPUT) Log.v(







