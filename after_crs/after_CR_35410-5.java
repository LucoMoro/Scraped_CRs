/*Issue: Foreground activity performs [Resume] and [Pause] when any process died in sleep mode.

Step to Reproduce
1)	Turn off device’s screen. (Sleep mode)
2)	Kill any process.
A.	Engineer Version: kill [PID]
B.	User Version: am force-stop [Package Name]
3)	Foreground activity proceed [Resume] and [Pause] consecutively.

Reason: Since ICS version, activity goes to stopped status when screen turns off.
stopIfSleepingLocked( ) makes activity to stopped status but, pauseIfSleepingLocked( ) was used in GB
and, activity keep paused status and, this problem did not occur.
This change give effect to resuming activity when any process was killed.
Because, resume is proceed without exception for activity status.
The exception only filtered for [ActivityState.PAUSED] in sleep or shutdown mode.
and, resume complete flow when activity status was [ActivityState.STOPPED].

Solution for this issue:
We think that exception’s condition have to change if stopped activity status is intended in sleep mode.
According to activity life cycle, activity can not resume from stop status.

Also check [ActivityState.STOPPING]. :)

Change-Id:Icca3366ac30ffa3b18f6e2393e4d7309089ef26a*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityStack.java b/services/java/com/android/server/am/ActivityStack.java
old mode 100644
new mode 100755
//Synthetic comment -- index 351dbb8..86d3a1a

//Synthetic comment -- @@ -1314,7 +1314,10 @@
// If we are sleeping, and there is no resumed activity, and the top
// activity is paused, well that is the state we want.
if ((mService.mSleeping || mService.mShuttingDown)
                && mLastPausedActivity == next
                && (next.state == ActivityState.PAUSED
                    || next.state == ActivityState.STOPPED
                    || next.state == ActivityState.STOPPING)) {
// Make sure we have executed any pending transitions, since there
// should be nothing left to do at this point.
mService.mWindowManager.executeAppTransition();







