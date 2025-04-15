/*Prevent key dispatch ANR on rapid change of orientation.

Rapidly opening and closing the keyboard slider while playing a
track on the music player and touching the screen to start the key
dispatch timer, an ANR could occur because the 500ms timeout occured
with mWasFrozen true, causing waitedFor to be set to zero and mWasFrozen
to be set to false. If the 500ms timeout then occured again before the
mWasFrozen frozen flag was set to true again, the value of waitedFor
was recalculated and if this indicated that the keyDispatchingTimeout
had elapsed then a "key dispatching" ANR was raised. The intention was
to restart the timeout checks if we were frozen during configuration
change, and this is properly achieved by setting startTime to the
current time so that the subsequent recalculation of waitedFor is from
this new point.

Change-Id:I6626c4bfff82ebe3d3401fed15263d09fe2b69ad*/




//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index 68787cd..8c05ca2 100644

//Synthetic comment -- @@ -5789,6 +5789,8 @@
// timeout checks from now; otherwise look at whether we timed
// out before awakening.
if (mWasFrozen) {
                    // Set startTime to the current time in addition to setting waitedFor to zero.
                    startTime = SystemClock.uptimeMillis();
waitedFor = 0;
mWasFrozen = false;
} else {







