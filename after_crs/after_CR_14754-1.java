/*Removed Calls to depracted APIs, Added type Arguments for Type Safety

Change-Id:I28aa897f4343dda8d40a66a9c03c30304e75e6a3*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 9e9552a..d43a807 100644

//Synthetic comment -- @@ -43,11 +43,13 @@
import android.app.PendingIntent;
import android.app.ResultInfo;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.backup.IBackupManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IIntentReceiver;
//Synthetic comment -- @@ -376,7 +378,7 @@
* The back history of all previous (and possibly still
* running) activities.  It contains HistoryRecord objects.
*/
    final ArrayList<HistoryRecord> mHistory = new ArrayList<HistoryRecord>();

/**
* Description of a request to start a new activity, which has been held
//Synthetic comment -- @@ -456,7 +458,7 @@
* to become visible before completing whatever operation they are
* supposed to do.
*/
    final ArrayList<HistoryRecord> mWaitingVisibleActivities = new ArrayList<HistoryRecord>();

/**
* List of activities that are ready to be stopped, but waiting
//Synthetic comment -- @@ -484,7 +486,7 @@
* for the previous activity to settle down before doing so.  It contains
* HistoryRecord objects.
*/
    final ArrayList<HistoryRecord> mFinishingActivities = new ArrayList<HistoryRecord>();

/**
* All of the applications we currently have running organized by name.
//Synthetic comment -- @@ -588,7 +590,7 @@
* The first entry in the list is the least recently used.
* It contains HistoryRecord objects.
*/
    private final ArrayList<HistoryRecord> mLRUActivities = new ArrayList<HistoryRecord>();

/**
* Set of PendingResultRecord objects that are currently active.
//Synthetic comment -- @@ -615,7 +617,7 @@
* broadcasts.  Hash keys are the receiver IBinder, hash value is
* a ReceiverList.
*/
    final HashMap<IBinder, ReceiverList> mRegisteredReceivers = new HashMap<IBinder, ReceiverList>();

/**
* Resolver for broadcast intents to registered receivers.
//Synthetic comment -- @@ -694,13 +696,13 @@
* List of PendingThumbnailsRecord objects of clients who are still
* waiting to receive all of the thumbnails for a task.
*/
    final ArrayList<PendingThumbnailsRecord> mPendingThumbnails = new ArrayList<PendingThumbnailsRecord>();

/**
* List of HistoryRecord objects that have been finished and must
* still report back to a pending thumbnail receiver.
*/
    final ArrayList<HistoryRecord> mCancelledThumbnails = new ArrayList<HistoryRecord>();

/**
* All of the currently running global content providers.  Keys are a
//Synthetic comment -- @@ -709,21 +711,21 @@
* that a single provider may be published under multiple names, so
* there may be multiple entries here for a single one in mProvidersByClass.
*/
    final HashMap<String, ContentProviderRecord> mProvidersByName = new HashMap<String, ContentProviderRecord>();

/**
* All of the currently running global content providers.  Keys are a
* string containing the provider's implementation class and values are a
* ContentProviderRecord object containing the data about it.
*/
    final HashMap<String, ContentProviderRecord> mProvidersByClass = new HashMap<String, ContentProviderRecord>();

/**
* List of content providers who have clients waiting for them.  The
* application is currently being launched and the provider will be
* removed from this list once it is published.
*/
    final ArrayList<ContentProviderRecord> mLaunchingProviders = new ArrayList<ContentProviderRecord>();

/**
* Global set of specific Uri permissions that have been granted.
//Synthetic comment -- @@ -1145,7 +1147,7 @@
d.setCancelable(false);
d.setTitle("System UIDs Inconsistent");
d.setMessage("UIDs on the system are inconsistent, you need to wipe your data partition or your device will be unstable.");
                d.setButton(DialogInterface.BUTTON_POSITIVE, "I'm Feeling Lucky",
mHandler.obtainMessage(IM_FEELING_LUCKY_MSG));
mUidAlert = d;
d.show();
//Synthetic comment -- @@ -1462,7 +1464,7 @@
GL_ES_VERSION = SystemProperties.getInt("ro.opengles.version",
ConfigurationInfo.GL_ES_VERSION_UNDEFINED);

        mConfiguration.setToDefaults();
mProcessStats.init();

// Add ourself to the Watchdog monitors.
//Synthetic comment -- @@ -1674,7 +1676,7 @@
private final HistoryRecord topRunningActivityLocked(HistoryRecord notTop) {
int i = mHistory.size()-1;
while (i >= 0) {
            HistoryRecord r = mHistory.get(i);
if (!r.finishing && r != notTop) {
return r;
}
//Synthetic comment -- @@ -1686,7 +1688,7 @@
private final HistoryRecord topRunningNonDelayedActivityLocked(HistoryRecord notTop) {
int i = mHistory.size()-1;
while (i >= 0) {
            HistoryRecord r = mHistory.get(i);
if (!r.finishing && !r.delayedResume && r != notTop) {
return r;
}
//Synthetic comment -- @@ -1707,7 +1709,7 @@
private final HistoryRecord topRunningActivityLocked(IBinder token, int taskId) {
int i = mHistory.size()-1;
while (i >= 0) {
            HistoryRecord r = mHistory.get(i);
// Note: the taskId check depends on real taskId fields being non-zero
if (!r.finishing && (token != r) && (taskId != r.task.taskId)) {
return r;
//Synthetic comment -- @@ -2308,7 +2310,7 @@
HistoryRecord r;
boolean behindFullscreen = false;
for (; i>=0; i--) {
            r = mHistory.get(i);
if (DEBUG_VISBILITY) Log.v(
TAG, "Make visible? " + r + " finishing=" + r.finishing
+ " state=" + r.state);
//Synthetic comment -- @@ -2391,7 +2393,7 @@
// Now for any activities that aren't visible to the user, make
// sure they no longer are keeping the screen frozen.
while (i >= 0) {
            r = mHistory.get(i);
if (DEBUG_VISBILITY) Log.v(
TAG, "Make invisible? " + r + " finishing=" + r.finishing
+ " state=" + r.state
//Synthetic comment -- @@ -2765,7 +2767,7 @@

try {
// Deliver all pending results.
                ArrayList<ResultInfo> a = next.results;
if (a != null) {
final int N = a.size();
if (!next.finishing && N > 0) {
//Synthetic comment -- @@ -2858,7 +2860,7 @@
HistoryRecord next = null;
boolean startIt = true;
for (int i = NH-1; i >= 0; i--) {
                HistoryRecord p = mHistory.get(i);
if (p.finishing) {
continue;
}
//Synthetic comment -- @@ -2997,7 +2999,7 @@
// First find the requested task.
while (i > 0) {
i--;
            HistoryRecord r = mHistory.get(i);
if (r.task.taskId == taskId) {
i++;
break;
//Synthetic comment -- @@ -3007,7 +3009,7 @@
// Now clear it.
while (i > 0) {
i--;
            HistoryRecord r = mHistory.get(i);
if (r.finishing) {
continue;
}
//Synthetic comment -- @@ -3020,7 +3022,7 @@
if (doClear) {
while (i < (mHistory.size()-1)) {
i++;
                        r = mHistory.get(i);
if (r.finishing) {
continue;
}
//Synthetic comment -- @@ -3061,7 +3063,7 @@
int i = mHistory.size();
while (i > 0) {
i--;
            HistoryRecord candidate = mHistory.get(i);
if (candidate.task.taskId != task) {
break;
}
//Synthetic comment -- @@ -3078,9 +3080,9 @@
* brought to the front.
*/
private final HistoryRecord moveActivityToFrontLocked(int where) {
        HistoryRecord newTop = mHistory.remove(where);
int top = mHistory.size();
        HistoryRecord oldTop = mHistory.get(top-1);
mHistory.add(top, newTop);
oldTop.frontOfTask = false;
newTop.frontOfTask = true;
//Synthetic comment -- @@ -3134,7 +3136,7 @@
if (DEBUG_RESULTS) Log.v(
TAG, "Sending result to " + resultTo + " (index " + index + ")");
if (index >= 0) {
                sourceRecord = mHistory.get(index);
if (requestCode >= 0 && !sourceRecord.finishing) {
resultRecord = sourceRecord;
}
//Synthetic comment -- @@ -3615,7 +3617,7 @@
// this case should never happen.
final int N = mHistory.size();
HistoryRecord prev =
                N > 0 ? mHistory.get(N-1) : null;
r.task = prev != null
? prev.task
: new TaskRecord(mCurTask, r.info, intent,
//Synthetic comment -- @@ -3728,7 +3730,7 @@
if (index < 0) {
return false;
}
            HistoryRecord r = mHistory.get(index);
if (r.app == null || r.app.thread == null) {
// The caller is not running...  d'oh!
return false;
//Synthetic comment -- @@ -3885,7 +3887,7 @@
if (index < 0) {
return;
}
            HistoryRecord r = mHistory.get(index);
final long origId = Binder.clearCallingIdentity();
mWindowManager.setAppOrientation(r, requestedOrientation);
Configuration config = mWindowManager.updateOrientationFromAppTokens(
//Synthetic comment -- @@ -3907,7 +3909,7 @@
if (index < 0) {
return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
}
            HistoryRecord r = mHistory.get(index);
return mWindowManager.getAppOrientation(r);
}
}
//Synthetic comment -- @@ -3963,12 +3965,12 @@
if (index < 0) {
return false;
}
        HistoryRecord r = mHistory.get(index);

// Is this the last activity left?
boolean lastActivity = true;
for (int i=mHistory.size()-1; i>=0; i--) {
            HistoryRecord p = mHistory.get(i);
if (!p.finishing && p != r) {
lastActivity = false;
break;
//Synthetic comment -- @@ -4004,7 +4006,7 @@
r.task.taskId, r.shortComponentName, reason);
r.task.numActivities--;
if (r.frontOfTask && index < (mHistory.size()-1)) {
            HistoryRecord next = mHistory.get(index+1);
if (next.task == r.task) {
next.frontOfTask = true;
}
//Synthetic comment -- @@ -4048,7 +4050,7 @@

if (mResumedActivity == r) {
boolean endTask = index <= 0
                    || (mHistory.get(index-1)).task != r.task;
if (DEBUG_TRANSITION) Log.v(TAG,
"Prepare close transition: finishing " + r);
mWindowManager.prepareAppTransition(endTask
//Synthetic comment -- @@ -4212,13 +4214,13 @@
if (index < 0) {
return;
}
            HistoryRecord self = mHistory.get(index);

final long origId = Binder.clearCallingIdentity();

int i;
for (i=mHistory.size()-1; i>=0; i--) {
                HistoryRecord r = mHistory.get(i);
if (r.resultTo == self && r.requestCode == requestCode) {
if ((r.resultWho == null && resultWho == null) ||
(r.resultWho != null && r.resultWho.equals(resultWho))) {
//Synthetic comment -- @@ -4239,7 +4241,7 @@
if (index < 0) {
return;
}
            HistoryRecord self = mHistory.get(index);

final long origId = Binder.clearCallingIdentity();

//Synthetic comment -- @@ -4411,7 +4413,7 @@
return removedFromHistory;
}

    private static void removeHistoryRecordsForAppLocked(ArrayList<HistoryRecord> list,
ProcessRecord app)
{
int i = list.size();
//Synthetic comment -- @@ -4420,7 +4422,7 @@
+ " with " + i + " entries");
while (i > 0) {
i--;
            HistoryRecord r = list.get(i);
if (localLOGV) Log.v(
TAG, "Record #" + i + " " + r + ": app=" + r.app);
if (r.app == app) {
//Synthetic comment -- @@ -4466,7 +4468,7 @@
TAG, "Removing app " + app + " from history with " + i + " entries");
while (i > 0) {
i--;
            HistoryRecord r = mHistory.get(i);
if (localLOGV) Log.v(
TAG, "Record #" + i + " " + r + ": app=" + r.app);
if (r.app == app) {
//Synthetic comment -- @@ -4627,7 +4629,7 @@
byte[] inp = new byte[8192];
int size = fs.read(inp);
fs.close();
            return new String(inp, 0, size);
} catch (java.io.IOException e) {
}
return "";
//Synthetic comment -- @@ -4850,7 +4852,7 @@
if (index < 0) {
return;
}
            HistoryRecord r = mHistory.get(index);
ProcessRecord app = r.app;

if (localLOGV) Log.v(
//Synthetic comment -- @@ -5028,7 +5030,7 @@
mWindowManager.closeSystemDialogs(reason);

for (i=mHistory.size()-1; i>=0; i--) {
                HistoryRecord r = mHistory.get(i);
if ((r.info.flags&ActivityInfo.FLAG_FINISH_ON_CLOSE_SYSTEM_DIALOGS) != 0) {
finishActivityLocked(r, i,
Activity.RESULT_CANCELED, null, "close-sys");
//Synthetic comment -- @@ -5136,7 +5138,7 @@
}

for (i=mHistory.size()-1; i>=0; i--) {
            HistoryRecord r = mHistory.get(i);
if (r.packageName.equals(name)) {
if (Config.LOGD) Log.d(
TAG, "  Force finishing activity "
//Synthetic comment -- @@ -5318,7 +5320,7 @@
mHandler.removeMessages(PROC_START_TIMEOUT_MSG, app);

boolean normalMode = mSystemReady || isAllowedWhileBooting(app.info);
        List<ProviderInfo> providers = normalMode ? generateApplicationProvidersLocked(app) : null;

if (!normalMode) {
Log.i(TAG, "Launching preboot mode app: " + app);
//Synthetic comment -- @@ -5555,7 +5557,7 @@
// Get the activity record.
int index = indexOfTokenLocked(token);
if (index >= 0) {
                HistoryRecord r = mHistory.get(index);

// This is a hack to semi-deal with a race condition
// in the client where it can be constructed with a
//Synthetic comment -- @@ -5624,7 +5626,7 @@
// Stop any activities that are scheduled to do so but have been
// waiting for the next one to start.
for (i=0; i<NS; i++) {
            HistoryRecord r = stops.get(i);
synchronized (this) {
if (r.finishing) {
finishCurrentActivityLocked(r, FINISH_IMMEDIATELY);
//Synthetic comment -- @@ -5637,7 +5639,7 @@
// Finish any activities that are scheduled to do so but have been
// waiting for the next one to start.
for (i=0; i<NF; i++) {
            HistoryRecord r = finishes.get(i);
synchronized (this) {
destroyActivityLocked(r, true);
}
//Synthetic comment -- @@ -5645,7 +5647,7 @@

// Report back to any thumbnail receivers.
for (i=0; i<NT; i++) {
            HistoryRecord r = thumbnails.get(i);
sendPendingThumbnail(r, null, null, null, true);
}

//Synthetic comment -- @@ -5727,7 +5729,7 @@
synchronized (this) {
int index = indexOfTokenLocked(token);
if (index >= 0) {
                r = mHistory.get(index);
if (!timeout) {
r.icicle = icicle;
r.haveState = true;
//Synthetic comment -- @@ -5758,7 +5760,7 @@
synchronized (this) {
int index = indexOfTokenLocked(token);
if (index >= 0) {
                r = mHistory.get(index);
r.thumbnail = thumbnail;
r.description = description;
r.stopped = true;
//Synthetic comment -- @@ -5788,7 +5790,7 @@

int index = indexOfTokenLocked(token);
if (index >= 0) {
                HistoryRecord r = mHistory.get(index);
if (r.state == ActivityState.DESTROYING) {
final long origId = Binder.clearCallingIdentity();
removeActivityFromHistoryLocked(r);
//Synthetic comment -- @@ -5815,7 +5817,7 @@
private HistoryRecord getCallingRecordLocked(IBinder token) {
int index = indexOfTokenLocked(token);
if (index >= 0) {
            HistoryRecord r = mHistory.get(index);
if (r != null) {
return r.resultTo;
}
//Synthetic comment -- @@ -5827,7 +5829,7 @@
synchronized(this) {
int index = indexOfTokenLocked(token);
if (index >= 0) {
                HistoryRecord r = mHistory.get(index);
return r.intent.getComponent();
}
return null;
//Synthetic comment -- @@ -5838,7 +5840,7 @@
synchronized(this) {
int index = indexOfTokenLocked(token);
if (index >= 0) {
                HistoryRecord r = mHistory.get(index);
return r.packageName;
}
return null;
//Synthetic comment -- @@ -5886,7 +5888,7 @@
if (index < 0) {
return null;
}
                activity = mHistory.get(index);
if (activity.finishing) {
return null;
}
//Synthetic comment -- @@ -6223,8 +6225,7 @@

String name = uri.getAuthority();
ProviderInfo pi = null;
        ContentProviderRecord cpr = mProvidersByName.get(name);
if (cpr != null) {
pi = cpr.info;
} else {
//Synthetic comment -- @@ -6416,8 +6417,7 @@

final String authority = uri.getAuthority();
ProviderInfo pi = null;
        ContentProviderRecord cpr = mProvidersByName.get(authority);
if (cpr != null) {
pi = cpr.info;
} else {
//Synthetic comment -- @@ -6509,8 +6509,7 @@

final String authority = uri.getAuthority();
ProviderInfo pi = null;
            ContentProviderRecord cpr = mProvidersByName.get(authority);
if (cpr != null) {
pi = cpr.info;
} else {
//Synthetic comment -- @@ -6554,9 +6553,9 @@
// TASK MANAGEMENT
// =========================================================

    public List<RunningTaskInfo> getTasks(int maxNum, int flags,
IThumbnailReceiver receiver) {
        ArrayList<RunningTaskInfo> list = new ArrayList<RunningTaskInfo>();

PendingThumbnailsRecord pending = null;
IApplicationThread topThumbnail = null;
//Synthetic comment -- @@ -6587,7 +6586,7 @@

int pos = mHistory.size()-1;
HistoryRecord next =
                pos >= 0 ? mHistory.get(pos) : null;
HistoryRecord top = null;
CharSequence topDescription = null;
TaskRecord curTask = null;
//Synthetic comment -- @@ -6596,7 +6595,7 @@
while (pos >= 0 && maxNum > 0) {
final HistoryRecord r = next;
pos--;
                next = pos >= 0 ? mHistory.get(pos) : null;

// Initialize state for next task if needed.
if (top == null ||
//Synthetic comment -- @@ -6624,8 +6623,7 @@
// If the next one is a different task, generate a new
// TaskInfo entry for what we have.
if (next == null || next.task != curTask) {
                    RunningTaskInfo ci = new RunningTaskInfo();
ci.id = curTask.taskId;
ci.baseActivity = r.intent.getComponent();
ci.topActivity = top.intent.getComponent();
//Synthetic comment -- @@ -6724,12 +6722,12 @@

private final int findAffinityTaskTopLocked(int startIndex, String affinity) {
int j;
        TaskRecord startTask = (mHistory.get(startIndex)).task; 
TaskRecord jt = startTask;

// First look backwards
for (j=startIndex-1; j>=0; j--) {
            HistoryRecord r = mHistory.get(j);
if (r.task != jt) {
jt = r.task;
if (affinity.equals(jt.affinity)) {
//Synthetic comment -- @@ -6742,7 +6740,7 @@
final int N = mHistory.size();
jt = startTask;
for (j=startIndex+1; j<N; j++) {
            HistoryRecord r = mHistory.get(j);
if (r.task != jt) {
if (affinity.equals(jt.affinity)) {
return j;
//Synthetic comment -- @@ -6752,7 +6750,7 @@
}

// Might it be at the top?
        if (affinity.equals((mHistory.get(N-1)).task.affinity)) {
return N-1;
}

//Synthetic comment -- @@ -6785,7 +6783,7 @@
int replyChainEnd = -1;
int lastReparentPos = -1;
for (int i=mHistory.size()-1; i>=-1; i--) {
            HistoryRecord below = i >= 0 ? mHistory.get(i) : null;

if (below != null && below.finishing) {
continue;
//Synthetic comment -- @@ -6839,7 +6837,7 @@
// bottom of the activity stack.  This also keeps it
// correctly ordered with any activities we previously
// moved.
                        HistoryRecord p = mHistory.get(0);
if (target.taskAffinity != null
&& target.taskAffinity.equals(p.task.affinity)) {
// If the activity currently at the bottom has the
//Synthetic comment -- @@ -6865,7 +6863,7 @@
}
int dstPos = 0;
for (int srcPos=targetI; srcPos<=replyChainEnd; srcPos++) {
                            p = mHistory.get(srcPos);
if (p.finishing) {
continue;
}
//Synthetic comment -- @@ -6904,7 +6902,7 @@
// like these are all in the reply chain.
replyChainEnd = targetI+1;
while (replyChainEnd < mHistory.size() &&
                                    (mHistory.get(
replyChainEnd)).task == task) {
replyChainEnd++;
}
//Synthetic comment -- @@ -6914,7 +6912,7 @@
}
HistoryRecord p = null;
for (int srcPos=targetI; srcPos<=replyChainEnd; srcPos++) {
                            p = mHistory.get(srcPos);
if (p.finishing) {
continue;
}
//Synthetic comment -- @@ -6974,7 +6972,7 @@
}
HistoryRecord p = null;
for (int srcPos=targetI; srcPos<=replyChainEnd; srcPos++) {
                        p = mHistory.get(srcPos);
if (p.finishing) {
continue;
}
//Synthetic comment -- @@ -6992,7 +6990,7 @@
replyChainEnd = targetI;
}
for (int srcPos=replyChainEnd; srcPos>=targetI; srcPos--) {
                        HistoryRecord p = mHistory.get(srcPos);
if (p.finishing) {
continue;
}
//Synthetic comment -- @@ -7023,7 +7021,7 @@
// below so it remains singleTop.
if (target.info.launchMode == ActivityInfo.LAUNCH_SINGLE_TOP) {
for (int j=lastReparentPos-1; j>=0; j--) {
                            HistoryRecord p = mHistory.get(j);
if (p.finishing) {
continue;
}
//Synthetic comment -- @@ -7069,7 +7067,7 @@
}
}
for (int i=mHistory.size()-1; i>=0; i--) {
                    HistoryRecord hr = mHistory.get(i);
if (hr.task.taskId == task) {
moveTaskToFrontLocked(hr.task, null);
return;
//Synthetic comment -- @@ -7087,12 +7085,12 @@
final int task = tr.taskId;
int top = mHistory.size()-1;

        if (top < 0 || (mHistory.get(top)).task.taskId == task) {
// nothing to do!
return;
}

        ArrayList<IBinder> moved = new ArrayList<IBinder>();

// Applying the affinities may have removed entries from the history,
// so get the size again.
//Synthetic comment -- @@ -7102,7 +7100,7 @@
// Shift all activities with this task up to the top
// of the stack, keeping them in the same internal order.
while (pos >= 0) {
            HistoryRecord r = mHistory.get(pos);
if (localLOGV) Log.v(
TAG, "At " + pos + " ckp " + r.task + ": " + r);
boolean first = true;
//Synthetic comment -- @@ -7220,7 +7218,7 @@
}
}

        ArrayList<IBinder> moved = new ArrayList<IBinder>();

if (DEBUG_TRANSITION) Log.v(TAG,
"Prepare to back transition: task=" + task);
//Synthetic comment -- @@ -7232,7 +7230,7 @@
// Shift all activities with this task down to the bottom
// of the stack, keeping them in the same internal order.
while (pos < N) {
            HistoryRecord r = mHistory.get(pos);
if (localLOGV) Log.v(
TAG, "At " + pos + " ckp " + r.task + ": " + r);
if (r.task.taskId == task) {
//Synthetic comment -- @@ -7293,7 +7291,7 @@
final int N = mHistory.size();
TaskRecord lastTask = null;
for (int i=0; i<N; i++) {
            HistoryRecord r = mHistory.get(i);
if (r == token) {
if (!onlyRoot || lastTask != r.task) {
return r.task.taskId;
//Synthetic comment -- @@ -7320,7 +7318,7 @@

final int N = mHistory.size();
for (int i=(N-1); i>=0; i--) {
            HistoryRecord r = mHistory.get(i);
if (!r.finishing && r.task != cp
&& r.launchMode != ActivityInfo.LAUNCH_SINGLE_INSTANCE) {
cp = r.task;
//Synthetic comment -- @@ -7364,7 +7362,7 @@

final int N = mHistory.size();
for (int i=(N-1); i>=0; i--) {
            HistoryRecord r = mHistory.get(i);
if (!r.finishing) {
if (r.intent.getComponent().equals(cls)) {
//Log.i(TAG, "Found matching class!");
//Synthetic comment -- @@ -7385,7 +7383,7 @@
int N = mHistory.size();
TaskRecord lastTask = null;
for (int i=0; i<N; i++) {
                HistoryRecord r = mHistory.get(i);
if (r.realActivity.equals(className)
&& r != token && lastTask != r.task) {
if (finishActivityLocked(r, i, Activity.RESULT_CANCELED,
//Synthetic comment -- @@ -7416,7 +7414,7 @@
final void sendPendingThumbnail(HistoryRecord r, IBinder token,
Bitmap thumbnail, CharSequence description, boolean always) {
TaskRecord task = null;
        ArrayList<PendingThumbnailsRecord> receivers = null;

//System.out.println("Send pending thumbnail: " + r);

//Synthetic comment -- @@ -7426,7 +7424,7 @@
if (index < 0) {
return;
}
                r = mHistory.get(index);
}
if (thumbnail == null) {
thumbnail = r.thumbnail;
//Synthetic comment -- @@ -7448,7 +7446,7 @@
//System.out.println("Looking in " + pr.pendingRecords);
if (pr.pendingRecords.remove(r)) {
if (receivers == null) {
                        receivers = new ArrayList<PendingThumbnailsRecord>();
}
receivers.add(pr);
if (pr.pendingRecords.size() == 0) {
//Synthetic comment -- @@ -7484,8 +7482,8 @@
// CONTENT PROVIDERS
// =========================================================

    private final List<ProviderInfo> generateApplicationProvidersLocked(ProcessRecord app) {
        List<ProviderInfo> providers = null;
try {
providers = ActivityThread.getPackageManager().
queryContentProviders(app.processName, app.info.uid,
//Synthetic comment -- @@ -7498,7 +7496,7 @@
ProviderInfo cpi =
(ProviderInfo)providers.get(i);
ContentProviderRecord cpr =
                    mProvidersByClass.get(cpi.name);
if (cpr == null) {
cpr = new ContentProviderRecord(cpi, app.info);
mProvidersByClass.put(cpi.name, cpr);
//Synthetic comment -- @@ -7573,7 +7571,7 @@
}

// First check if this content provider has been published...
            cpr = mProvidersByName.get(name);
if (cpr != null) {
cpi = cpr.info;
if (checkContentProviderPermissionLocked(cpi, r, -1) != null) {
//Synthetic comment -- @@ -7638,7 +7636,7 @@
? cpi.readPermission : cpi.writePermission);
}

                cpr = mProvidersByClass.get(cpi.name);
final boolean firstClass = cpr == null;
if (firstClass) {
try {
//Synthetic comment -- @@ -7772,7 +7770,7 @@
*/
public void removeContentProvider(IApplicationThread caller, String name) {
synchronized (this) {
            ContentProviderRecord cpr = mProvidersByName.get(name);
if(cpr == null) {
// remove from mProvidersByClass
if (DEBUG_PROVIDER) Log.v(TAG, name +
//Synthetic comment -- @@ -7786,8 +7784,7 @@
" when removing content provider " + name);
}
//update content provider record entry info
            ContentProviderRecord localCpr = mProvidersByClass.get(cpr.info.name);
if (DEBUG_PROVIDER) Log.v(TAG, "Removing provider requested by "
+ r.info.processName + " from process "
+ localCpr.appInfo.processName);
//Synthetic comment -- @@ -7811,7 +7808,7 @@

private void removeContentProviderExternal(String name) {
synchronized (this) {
            ContentProviderRecord cpr = mProvidersByName.get(name);
if(cpr == null) {
//remove from mProvidersByClass
if(localLOGV) Log.v(TAG, name+" content provider not found in providers list");
//Synthetic comment -- @@ -7819,7 +7816,7 @@
}

//update content provider record entry info
            ContentProviderRecord localCpr = mProvidersByClass.get(cpr.info.name);
localCpr.externals--;
if (localCpr.externals < 0) {
Log.e(TAG, "Externals < 0 for content provider " + localCpr);
//Synthetic comment -- @@ -7852,7 +7849,7 @@
continue;
}
ContentProviderRecord dst =
                    r.pubProviders.get(src.info.name);
if (dst != null) {
mProvidersByClass.put(dst.info.name, dst);
String names[] = dst.info.authority.split(";");
//Synthetic comment -- @@ -7883,7 +7880,7 @@
}

public static final void installSystemProviders() {
        List<ProviderInfo> providers = null;
synchronized (mSelf) {
ProcessRecord app = mSelf.mProcessNames.get("system", Process.SYSTEM_UID);
providers = mSelf.generateApplicationProvidersLocked(app);
//Synthetic comment -- @@ -7940,7 +7937,7 @@
TAG, "Performing unhandledBack(): stack size = " + count);
if (count > 1) {
final long origId = Binder.clearCallingIdentity();
                finishActivityLocked(mHistory.get(count-1),
count-1, Activity.RESULT_CANCELED, null, "unhandled-back");
Binder.restoreCallingIdentity(origId);
}
//Synthetic comment -- @@ -8574,7 +8571,7 @@
synchronized (this) {
if (mFactoryTest != SystemServer.FACTORY_TEST_LOW_LEVEL) {
try {
                    List<ApplicationInfo> apps = ActivityThread.getPackageManager().
getPersistentApplications(STOCK_PM_FLAGS);
if (apps != null) {
int N = apps.size();
//Synthetic comment -- @@ -8764,7 +8761,7 @@
app.info.processName, app.info.uid);
killServicesLocked(app, false);
for (int i=mHistory.size()-1; i>=0; i--) {
                HistoryRecord r = mHistory.get(i);
if (r.app == app) {
if (Config.LOGD) Log.d(
TAG, "  Force finishing activity "
//Synthetic comment -- @@ -8792,7 +8789,7 @@
if (app.services.size() != 0) {
// Any services running in the application need to be placed
// back in the pending list.
            Iterator<ServiceRecord> it = app.services.iterator();
while (it.hasNext()) {
ServiceRecord sr = (ServiceRecord)it.next();
sr.crashCount++;
//Synthetic comment -- @@ -9403,7 +9400,7 @@
if (mRegisteredReceivers.size() > 0) {
pw.println(" ");
pw.println("  Registered Receivers:");
                Iterator<ReceiverList> it = mRegisteredReceivers.values().iterator();
while (it.hasNext()) {
ReceiverList r = (ReceiverList)it.next();
pw.print("  * "); pw.println(r);
//Synthetic comment -- @@ -9577,9 +9574,9 @@
if (mProvidersByClass.size() > 0) {
if (needSep) pw.println(" ");
pw.println("  Published content providers (by class):");
                Iterator<Map.Entry<String, ContentProviderRecord>> it = mProvidersByClass.entrySet().iterator();
while (it.hasNext()) {
                    Map.Entry<String, ContentProviderRecord> e = it.next();
ContentProviderRecord r = (ContentProviderRecord)e.getValue();
pw.print("  * "); pw.println(r);
r.dump(pw, "    ");
//Synthetic comment -- @@ -9590,9 +9587,9 @@
if (mProvidersByName.size() > 0) {
pw.println(" ");
pw.println("  Authority to provider mappings:");
                Iterator<Map.Entry<String, ContentProviderRecord>> it = mProvidersByName.entrySet().iterator();
while (it.hasNext()) {
                    Map.Entry<String, ContentProviderRecord> e = it.next();
ContentProviderRecord r = (ContentProviderRecord)e.getValue();
pw.print("  "); pw.print(e.getKey()); pw.print(": ");
pw.println(r);
//Synthetic comment -- @@ -9659,11 +9656,11 @@
}
}

    private static final void dumpHistoryList(PrintWriter pw, List<HistoryRecord> list,
String prefix, String label, boolean complete) {
TaskRecord lastTask = null;
for (int i=list.size()-1; i>=0; i--) {
            HistoryRecord r = list.get(i);
final boolean full = complete || !r.inHistory;
if (lastTask != r.task) {
lastTask = r.task;
//Synthetic comment -- @@ -9714,7 +9711,7 @@
}

private static final void dumpApplicationMemoryUsage(FileDescriptor fd,
            PrintWriter pw, List<ProcessRecord> list, String prefix, String[] args) {
final boolean isCheckinRequest = scanArgs(args, "--checkin");
long uptime = SystemClock.uptimeMillis();
long realtime = SystemClock.elapsedRealtime();
//Synthetic comment -- @@ -9728,7 +9725,7 @@
pw.println("Uptime: " + uptime + " Realtime: " + realtime);
}
for (int i = list.size() - 1 ; i >= 0 ; i--) {
            ProcessRecord r = list.get(i);
if (r.thread != null) {
if (!isCheckinRequest) {
pw.println("\n** MEMINFO in pid " + r.pid + " [" + r.processName + "] **");
//Synthetic comment -- @@ -9825,7 +9822,7 @@
if (app.services.size() != 0) {
// Any services running in the application need to be placed
// back in the pending list.
            Iterator<ServiceRecord> it = app.services.iterator();
while (it.hasNext()) {
ServiceRecord sr = (ServiceRecord)it.next();
synchronized (sr.stats.getBatteryStats()) {
//Synthetic comment -- @@ -9965,7 +9962,7 @@

// Remove published content providers.
if (!app.pubProviders.isEmpty()) {
            Iterator<ContentProviderRecord> it = app.pubProviders.values().iterator();
while (it.hasNext()) {
ContentProviderRecord cpr = (ContentProviderRecord)it.next();
cpr.provider = null;
//Synthetic comment -- @@ -10000,7 +9997,7 @@

// Unregister from connected content providers.
if (!app.conProviders.isEmpty()) {
            Iterator<ContentProviderRecord> it = app.conProviders.keySet().iterator();
while (it.hasNext()) {
ContentProviderRecord cpr = (ContentProviderRecord)it.next();
cpr.clients.remove(app);
//Synthetic comment -- @@ -11063,7 +11060,7 @@
Log.w(TAG, "Binding with unknown activity: " + token);
return 0;
}
                activity = mHistory.get(aindex);
}

int clientLabel = 0;
//Synthetic comment -- @@ -11401,7 +11398,8 @@
serviceDoneExecutingLocked(r, inStopping);
Binder.restoreCallingIdentity(origId);
} else {
                String name = (r != null) ? r.name.toString() : "unknown Service";
                Log.w(TAG, "Done executing unknown service " + name
+ " with token " + token);
}
}
//Synthetic comment -- @@ -11577,8 +11575,8 @@
// BROADCASTS
// =========================================================

    private final List<Intent> getStickiesLocked(String action, IntentFilter filter,
            List<Intent> cur) {
final ContentResolver resolver = mContext.getContentResolver();
final ArrayList<Intent> list = mStickyBroadcasts.get(action);
if (list == null) {
//Synthetic comment -- @@ -11622,10 +11620,10 @@
}
}

            List<Intent> allSticky = null;

// Look for any matching sticky broadcasts...
            Iterator<String> actions = filter.actionsIterator();
if (actions != null) {
while (actions.hasNext()) {
String action = (String)actions.next();
//Synthetic comment -- @@ -11646,8 +11644,7 @@
return sticky;
}

            ReceiverList rl = mRegisteredReceivers.get(receiver.asBinder());
if (rl == null) {
rl = new ReceiverList(this, callerApp,
Binder.getCallingPid(),
//Synthetic comment -- @@ -11700,8 +11697,7 @@
boolean doNext = false;

synchronized(this) {
            ReceiverList rl = mRegisteredReceivers.get(receiver.asBinder());
if (rl != null) {
if (rl.curBroadcast != null) {
BroadcastRecord r = rl.curBroadcast;
//Synthetic comment -- @@ -11756,10 +11752,10 @@
// Handle special intents: if this broadcast is from the package
// manager about a package being removed, we need to remove all of
// its activities from the history stack.
        final boolean uidRemoved = Intent.ACTION_UID_REMOVED.equals(
intent.getAction());
        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())
                || Intent.ACTION_PACKAGE_CHANGED.equals(intent.getAction())
|| uidRemoved) {
if (checkComponentPermission(
android.Manifest.permission.BROADCAST_PACKAGE_REMOVED,
//Synthetic comment -- @@ -11805,7 +11801,7 @@
* of all currently running processes. This message will get queued up before the broadcast
* happens.
*/
        if (Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
mHandler.sendEmptyMessage(UPDATE_TIME_ZONE);
}

//Synthetic comment -- @@ -11927,11 +11923,11 @@
// broadcast or such for apps, but we'd like to deliberately make
// this decision.
boolean skip = false;
            if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
skip = true;
            } else if (Intent.ACTION_PACKAGE_RESTARTED.equals(intent.getAction())) {
skip = true;
            } else if (Intent.ACTION_PACKAGE_DATA_CLEARED.equals(intent.getAction())) {
skip = true;
}
String skipPackage = (skip && intent.getData() != null)
//Synthetic comment -- @@ -12112,8 +12108,8 @@
return false;
}
int state = r.state;
        r.state = BroadcastRecord.IDLE;
        if (state == BroadcastRecord.IDLE) {
if (explicit) {
Log.w(TAG, "finishReceiver called but state is IDLE");
}
//Synthetic comment -- @@ -13201,7 +13197,7 @@
final long now = SystemClock.uptimeMillis();
// This process is more important if the top activity is
// bound to the service.
            Iterator<ServiceRecord> jt = app.services.iterator();
while (jt.hasNext() && adj > FOREGROUND_APP_ADJ) {
ServiceRecord s = (ServiceRecord)jt.next();
if (s.startRequested) {
//Synthetic comment -- @@ -13278,7 +13274,7 @@
}

if (app.pubProviders.size() != 0 && adj > FOREGROUND_APP_ADJ) {
            Iterator<ContentProviderRecord> jt = app.pubProviders.values().iterator();
while (jt.hasNext() && adj > FOREGROUND_APP_ADJ) {
ContentProviderRecord cpr = (ContentProviderRecord)jt.next();
if (cpr.clients.size() != 0) {








//Synthetic comment -- diff --git a/services/java/com/android/server/am/AppWaitingForDebuggerDialog.java b/services/java/com/android/server/am/AppWaitingForDebuggerDialog.java
//Synthetic comment -- index 0992d4d..3e60c55 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.server.am;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

//Synthetic comment -- @@ -49,7 +50,7 @@
text.append(" is waiting for the debugger to attach.");

setMessage(text.toString());
        setButton(DialogInterface.BUTTON_POSITIVE, "Force Close", mHandler.obtainMessage(1, app));
setTitle("Waiting For Debugger");
getWindow().setTitle("Waiting For Debugger: " + app.info.processName);
}








//Synthetic comment -- diff --git a/services/java/com/android/server/am/FactoryErrorDialog.java b/services/java/com/android/server/am/FactoryErrorDialog.java
//Synthetic comment -- index 2e25474..21d1a3f 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.server.am;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

//Synthetic comment -- @@ -26,7 +27,7 @@
setCancelable(false);
setTitle(context.getText(com.android.internal.R.string.factorytest_failed));
setMessage(msg);
        setButton(DialogInterface.BUTTON_POSITIVE, context.getText(com.android.internal.R.string.factorytest_reboot),
mHandler.obtainMessage(0));
getWindow().setTitle("Factory Error");
}








//Synthetic comment -- diff --git a/services/java/com/android/server/am/HistoryRecord.java b/services/java/com/android/server/am/HistoryRecord.java
//Synthetic comment -- index 84ded22..64132e0 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.server.am.ActivityManagerService.ActivityState;

import android.app.Activity;
import android.app.ResultInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
//Synthetic comment -- @@ -71,9 +72,9 @@
HistoryRecord resultTo; // who started this entry, so will get our reply
final String resultWho; // additional identifier for use by resultTo.
final int requestCode;  // code given by requester (resultTo)
    ArrayList<ResultInfo> results;      // pending ActivityResult objs we have received
HashSet<WeakReference<PendingIntentRecord>> pendingResults; // all pending intents for this act
    ArrayList<Intent> newIntents;   // any pending new intents for single-top mode
HashSet<ConnectionRecord> connections; // All ConnectionRecord we hold
HashSet<UriPermission> readUriPermissions; // special access to reading uris.
HashSet<UriPermission> writeUriPermissions; // special access to writing uris.
//Synthetic comment -- @@ -298,7 +299,7 @@
ActivityResult r = new ActivityResult(from, resultWho,
		requestCode, resultCode, resultData);
if (results == null) {
            results = new ArrayList<ResultInfo>();
}
results.add(r);
}
//Synthetic comment -- @@ -323,7 +324,7 @@

void addNewIntentLocked(Intent intent) {
if (newIntents == null) {
            newIntents = new ArrayList<Intent>();
}
newIntents.add(intent);
}








//Synthetic comment -- diff --git a/services/java/com/android/server/am/PendingThumbnailsRecord.java b/services/java/com/android/server/am/PendingThumbnailsRecord.java
//Synthetic comment -- index ed478c9..7ccea56 100644

//Synthetic comment -- @@ -27,13 +27,13 @@
class PendingThumbnailsRecord
{
final IThumbnailReceiver receiver;   // who is waiting.
    HashSet<HistoryRecord> pendingRecords; // HistoryRecord objects we still wait for.
boolean finished;       // Is pendingRecords empty?

PendingThumbnailsRecord(IThumbnailReceiver _receiver)
{
receiver = _receiver;
        pendingRecords = new HashSet<HistoryRecord>();
finished = false;
}
}








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ProcessRecord.java b/services/java/com/android/server/am/ProcessRecord.java
//Synthetic comment -- index 6202257..94d7099 100644

//Synthetic comment -- @@ -44,7 +44,7 @@
final ApplicationInfo info; // all about the first app in the process
final String processName;   // name of the process
// List of packages running in the process
    final HashSet<String> pkgList = new HashSet<String>();
IApplicationThread thread;  // the actual proc...  may be null only if
// 'persistent' is true (in which case we
// are in the process of launching the app)
//Synthetic comment -- @@ -80,9 +80,9 @@
Object adjTarget;           // Debugging: target component impacting oom_adj.

// contains HistoryRecord objects
    final ArrayList<HistoryRecord> activities = new ArrayList<HistoryRecord>();
// all ServiceRecord running in this process
    final HashSet<ServiceRecord> services = new HashSet<ServiceRecord>();
// services that are currently executing code (need to remain foreground).
final HashSet<ServiceRecord> executingServices
= new HashSet<ServiceRecord>();
//Synthetic comment -- @@ -92,7 +92,7 @@
// all IIntentReceivers that are registered from this process.
final HashSet<ReceiverList> receivers = new HashSet<ReceiverList>();
// class (String) -> ContentProviderRecord
    final HashMap<String, ContentProviderRecord> pubProviders = new HashMap<String, ContentProviderRecord>(); 
// All ContentProviderRecord process is using
final HashMap<ContentProviderRecord, Integer> conProviders
= new HashMap<ContentProviderRecord, Integer>(); 







