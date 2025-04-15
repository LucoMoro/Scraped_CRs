/*Remove the window from being a target for motion events when it is destroyed.

If window was destroyed when touch was pressed this destroyed
window was still considered as a target for motion events.
In some cases this could result with hang up inside event dispatch procedure.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index 24caf1f..29dfbc7 100644

//Synthetic comment -- @@ -2067,6 +2067,7 @@
private void removeWindowInnerLocked(Session session, WindowState win) {
mKeyWaiter.finishedKey(session, win.mClient, true,
KeyWaiter.RETURN_NOTHING);
mKeyWaiter.releasePendingPointerLocked(win.mSession);
mKeyWaiter.releasePendingTrackballLocked(win.mSession);

//Synthetic comment -- @@ -5971,6 +5972,12 @@
}
}

MotionEvent finishedKey(Session session, IWindow client, boolean force,
int returnWhat) {
if (DEBUG_INPUT) Log.v(







