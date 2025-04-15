/*Corrected visibility reporting error in WindowManager

Correction for the error:
When a window is done animating and about to be destroyed,
it is still checked for visibility in the method
updateReportedVisibilityLocked in the WindowManager.
In the case where other windows are ready to be reported as
visible, this, not yet destroyed window which has the state
not visible, will prevent the visibility report message from
being sent.

Change-Id:I9e307a678a204f0883ccd78c2645c373d3f81066*/




//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index 11b6310..dd4aeab 100644

//Synthetic comment -- @@ -8760,7 +8760,8 @@
WindowState win = allAppWindows.get(i);
if (win == startingWindow || win.mAppFreezing
|| win.mViewVisibility != View.VISIBLE
                        || win.mAttrs.type == TYPE_APPLICATION_STARTING
                        || win.mDestroying) {
continue;
}
if (DEBUG_VISIBILITY) {







