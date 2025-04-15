/*Corrected update of HistoryRecord member 'visible' in ActivityManager

Corrected the update of the HistoryRecord member 'visible' in
the method ensureActivitiesVisibleLocked in the ActivityManager.
Depending on other conditions, the member 'visible' would
sometimes be set without performing a corresponding call to the
WindowManager method setAppVisibility, causing the
ActivityManager to erroneously believe that the activity already
was visible.

Change-Id:I1f3d346458a78e0b9f1ec94664cb47cd959b5372*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 804af9c..d9e888c 100644

//Synthetic comment -- @@ -2382,12 +2382,12 @@
} else if (onlyThisProcess == null) {
// This activity is not currently visible, but is running.
// Tell it to become visible.
if (r.state != ActivityState.RESUMED && r != starting) {
// If this activity is paused, tell it
// to now show its window.
if (DEBUG_VISBILITY) Slog.v(
TAG, "Making visible and scheduling visibility: " + r);
                    r.visible = true;
try {
mWindowManager.setAppVisibility(r, true);
r.app.thread.scheduleWindowVisibility(r, true);







