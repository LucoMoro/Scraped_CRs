/*Rename functions which ought to be named *Locked()

Several functions operate on variables to which access needs to be
synchronized.  However, it happens that the functions in question
are only ever called from places which have already synchronized.
Therefore, nothing is really wrong, but the functions ought to
have 'Locked' appended to their names, to indicate that it is the
caller's responsibility to synchronize before calling them.

Change-Id:I44e7dc0dff6da9436677cb10908dce41ffeba195*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 5f6356d..1359f66 100644

//Synthetic comment -- @@ -3566,7 +3566,7 @@
if (DEBUG_TASKS) Log.v(TAG, "Starting new activity " + r
+ " in new task " + r.task);
newTask = true;
            addRecentTaskLocked(r.task);

} else if (sourceRecord != null) {
if (!addingToTask &&
//Synthetic comment -- @@ -3855,7 +3855,7 @@
}
}

    private final void addRecentTaskLocked(TaskRecord task) {
// Remove any existing entries that are the same kind of task.
int N = mRecentTasks.size();
for (int i=0; i<N; i++) {
//Synthetic comment -- @@ -6887,7 +6887,7 @@
taskTopI = -1;
}
replyChainEnd = -1;
                        addRecentTaskLocked(target.task);
} else if (forceReset || finishOnTaskLaunch
|| clearWhenTaskReset) {
// If the activity should just be removed -- either
//Synthetic comment -- @@ -7109,7 +7109,7 @@
moved.add(0, r);
top--;
if (first) {
                    addRecentTaskLocked(r.task);
first = false;
}
}
//Synthetic comment -- @@ -7134,11 +7134,11 @@
mWindowManager.validateAppTokens(mHistory);
}

        finishTaskMoveLocked(task);
EventLog.writeEvent(LOG_TASK_TO_FRONT, task);
}

    private final void finishTaskMoveLocked(int task) {
resumeTopActivityLocked(null);
}

//Synthetic comment -- @@ -7256,7 +7256,7 @@
mWindowManager.validateAppTokens(mHistory);
}

        finishTaskMoveLocked(task);
return true;
}

//Synthetic comment -- @@ -11564,7 +11564,7 @@
// BROADCASTS
// =========================================================

    private final List getStickiesLocked(String action, IntentFilter filter,
List cur) {
final ContentResolver resolver = mContext.getContentResolver();
final ArrayList<Intent> list = mStickyBroadcasts.get(action);
//Synthetic comment -- @@ -11616,10 +11616,10 @@
if (actions != null) {
while (actions.hasNext()) {
String action = (String)actions.next();
                    allSticky = getStickiesLocked(action, filter, allSticky);
}
} else {
                allSticky = getStickiesLocked(null, filter, allSticky);
}

// The first sticky in the list is returned directly back to
//Synthetic comment -- @@ -13357,7 +13357,7 @@
/**
* Returns true if things are idle enough to perform GCs.
*/
    private final boolean canGcNowLocked() {
return mParallelBroadcasts.size() == 0
&& mOrderedBroadcasts.size() == 0
&& (mSleeping || (mResumedActivity != null &&
//Synthetic comment -- @@ -13373,7 +13373,7 @@
if (N <= 0) {
return;
}
        if (canGcNowLocked()) {
while (mProcessesToGc.size() > 0) {
ProcessRecord proc = mProcessesToGc.remove(0);
if (proc.curRawAdj > VISIBLE_APP_ADJ || proc.reportLowMemory) {
//Synthetic comment -- @@ -13401,7 +13401,7 @@
* If all looks good, perform GCs on all processes waiting for them.
*/
final void performAppGcsIfAppropriateLocked() {
        if (canGcNowLocked()) {
performAppGcsLocked();
return;
}







