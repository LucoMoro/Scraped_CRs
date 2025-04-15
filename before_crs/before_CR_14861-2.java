/*replaced deprecated setButton Methods
replaced deprecated String Constructor

Change-Id:I7031b8ddc80b9847af9933b05fe4ca96405f7605*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 0c11940..eca7747 100644

//Synthetic comment -- @@ -52,6 +52,7 @@
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IIntentReceiver;
//Synthetic comment -- @@ -148,7 +149,7 @@
static final boolean DEBUG_CONFIGURATION = localLOGV || false;
static final boolean VALIDATE_TOKENS = false;
static final boolean SHOW_ACTIVITY_START_TIME = true;
    
// Control over CPU and battery monitoring.
static final long BATTERY_STATS_TIME = 30*60*1000;      // write battery stats every 30 minutes.
static final boolean MONITOR_CPU_USAGE = true;
//Synthetic comment -- @@ -158,7 +159,7 @@

// The flags that are set for all calls we make to the package manager.
static final int STOCK_PM_FLAGS = PackageManager.GET_SHARED_LIBRARY_FILES;
    
private static final String SYSTEM_SECURE = "ro.secure";

// This is the maximum number of application processes we would like
//Synthetic comment -- @@ -180,11 +181,11 @@
// Amount of time after a call to stopAppSwitches() during which we will
// prevent further untrusted switches from happening.
static final long APP_SWITCH_DELAY_TIME = 5*1000;
    
// How long until we reset a task when the user returns to it.  Currently
// 30 minutes.
static final long ACTIVITY_INACTIVE_RESET_TIME = 1000*60*30;
    
// Set to true to disable the icon that is shown while a new activity
// is being started.
static final boolean SHOW_APP_STARTING_ICON = true;
//Synthetic comment -- @@ -216,7 +217,7 @@
// How long we wait until giving up on an activity telling us it has
// finished destroying itself.
static final int DESTROY_TIMEOUT = 10*1000;
    
// How long we allow a receiver to run before giving up on it.
static final int BROADCAST_TIMEOUT = 10*1000;

//Synthetic comment -- @@ -234,7 +235,7 @@
// Multiplying factor to increase restart duration time by, for each time
// a service is killed before it has run for SERVICE_RESET_RUN_DURATION.
static final int SERVICE_RESTART_DURATION_FACTOR = 4;
    
// The minimum amount of time between restarting services that we allow.
// That is, when multiple services are restarting, we won't allow each
// to restart less than this amount of time from the last one.
//Synthetic comment -- @@ -244,7 +245,7 @@
// we consider it non-essential and allow its process to go on the
// LRU background list.
static final int MAX_SERVICE_INACTIVITY = 30*60*1000;
    
// How long we wait until we timeout on key dispatching.
static final int KEY_DISPATCHING_TIMEOUT = 5*1000;

//Synthetic comment -- @@ -301,7 +302,7 @@

// Memory pages are 4K.
static final int PAGE_SIZE = 4*1024;
    
// Corresponding memory levels for above adjustments.
static final int EMPTY_APP_MEM;
static final int HIDDEN_APP_MEM;
//Synthetic comment -- @@ -314,20 +315,20 @@
// The minimum number of hidden apps we want to be able to keep around,
// without empty apps being able to push them out of memory.
static final int MIN_HIDDEN_APPS = 2;
    
// The maximum number of hidden processes we will keep around before
// killing them; this is just a control to not let us go too crazy with
// keeping around processes on devices with large amounts of RAM.
static final int MAX_HIDDEN_APPS = 15;
    
// We put empty content processes after any hidden processes that have
// been idle for less than 15 seconds.
static final long CONTENT_APP_IDLE_OFFSET = 15*1000;
    
// We put empty content processes after any hidden processes that have
// been idle for less than 120 seconds.
static final long EMPTY_APP_IDLE_OFFSET = 120*1000;
    
static {
// These values are set in system/rootdir/init.rc on startup.
FOREGROUND_APP_ADJ =
//Synthetic comment -- @@ -360,9 +361,9 @@
EMPTY_APP_MEM =
Integer.valueOf(SystemProperties.get("ro.EMPTY_APP_MEM"))*PAGE_SIZE;
}
    
static final int MY_PID = Process.myPid();
    
static final String[] EMPTY_STRING_ARRAY = new String[0];

enum ActivityState {
//Synthetic comment -- @@ -394,22 +395,22 @@
int grantedMode;
boolean onlyIfNeeded;
}
    
final ArrayList<PendingActivityLaunch> mPendingActivityLaunches
= new ArrayList<PendingActivityLaunch>();
    
/**
* List of people waiting to find out about the next launched activity.
*/
final ArrayList<IActivityManager.WaitResult> mWaitingActivityLaunched
= new ArrayList<IActivityManager.WaitResult>();
    
/**
* List of people waiting to find out about the next visible activity.
*/
final ArrayList<IActivityManager.WaitResult> mWaitingActivityVisible
= new ArrayList<IActivityManager.WaitResult>();
    
/**
* List of all active broadcasts that are to be executed immediately
* (without waiting for another broadcast to finish).  Currently this only
//Synthetic comment -- @@ -489,7 +490,7 @@
*/
final ArrayList<HistoryRecord> mNoAnimActivities
= new ArrayList<HistoryRecord>();
    
/**
* List of intents that were used to start the most recent tasks.
*/
//Synthetic comment -- @@ -548,7 +549,7 @@
}
final SparseArray<ForegroundToken> mForegroundProcesses
= new SparseArray<ForegroundToken>();
    
/**
* List of records for processes that someone had tried to start before the
* system was ready.  We don't start them at that point, but ensure they
//Synthetic comment -- @@ -599,7 +600,7 @@
* the "home" activity.
*/
private ProcessRecord mHomeProcess;
    
/**
* List of running activities, sorted by recent usage.
* The first entry in the list is the least recently used.
//Synthetic comment -- @@ -769,7 +770,7 @@
* any user id that can impact battery performance.
*/
final BatteryStatsService mBatteryStatsService;
    
/**
* information about component usage
*/
//Synthetic comment -- @@ -787,13 +788,13 @@
* configurations.
*/
int mConfigurationSeq = 0;
    
/**
* Set when we know we are going to be calling updateConfiguration()
* soon, so want to skip intermediate config checks.
*/
boolean mConfigWillChange;
    
/**
* Hardware-reported OpenGLES version.
*/
//Synthetic comment -- @@ -809,7 +810,7 @@
* Temporary to avoid allocations.  Protected by main lock.
*/
final StringBuilder mStringBuilder = new StringBuilder(256);
    
/**
* Used to control how we initialize the service.
*/
//Synthetic comment -- @@ -827,7 +828,7 @@
int mFactoryTest;

boolean mCheckedForSetup;
    
/**
* The time at which we will allow normal application switches again,
* after a call to {@link #stopAppSwitches()}.
//Synthetic comment -- @@ -839,7 +840,7 @@
* is set; any switches after that will clear the time.
*/
boolean mDidAppSwitch;
    
/**
* Set while we are wanting to sleep, to prevent any
* activities from being started/resumed.
//Synthetic comment -- @@ -850,7 +851,7 @@
* Set if we are shutting down the system, similar to sleeping.
*/
boolean mShuttingDown = false;
    
/**
* Set when the system is going to sleep, until we have
* successfully paused the current activity and released our wake lock.
//Synthetic comment -- @@ -883,7 +884,7 @@
* Current sequence id for process LRU updating.
*/
int mLruSeq = 0;
    
/**
* Set to true if the ANDROID_SIMPLE_PROCESS_MANAGEMENT envvar
* is set, indicating the user wants processes started in such a way
//Synthetic comment -- @@ -897,13 +898,13 @@
* N procs were started.
*/
int[] mProcDeaths = new int[20];
    
/**
* This is set if we had to do a delayed dexopt of an app before launching
* it, to increasing the ANR timeouts in that case.
*/
boolean mDidDexOpt;
    
String mDebugApp = null;
boolean mWaitForDebugger = false;
boolean mDebugTransient = false;
//Synthetic comment -- @@ -914,7 +915,7 @@

final RemoteCallbackList<IActivityWatcher> mWatchers
= new RemoteCallbackList<IActivityWatcher>();
    
/**
* Callback of last caller to {@link #requestPss}.
*/
//Synthetic comment -- @@ -926,13 +927,13 @@
*/
final ArrayList<ProcessRecord> mRequestPssList
= new ArrayList<ProcessRecord>();
    
/**
* Runtime statistics collection thread.  This object's lock is used to
* protect all related state.
*/
final Thread mProcessStatsThread;
    
/**
* Used to collect process stats when showing not responding dialog.
* Protected by mProcessStatsThread.
//Synthetic comment -- @@ -945,7 +946,7 @@
long mLastWriteTime = 0;

long mInitialStartTime = 0;
    
/**
* Set to true after the system has finished booting.
*/
//Synthetic comment -- @@ -1035,7 +1036,7 @@
res.set(0);
}
}
                
ensureBootCompleted();
} break;
case SHOW_NOT_RESPONDING_MSG: {
//Synthetic comment -- @@ -1046,7 +1047,7 @@
Slog.e(TAG, "App already has anr dialog: " + proc);
return;
}
                    
broadcastIntentLocked(null, null, new Intent("android.intent.action.ANR"),
null, null, 0, null, null, null,
false, false, MY_PID, Process.SYSTEM_UID);
//Synthetic comment -- @@ -1056,7 +1057,7 @@
d.show();
proc.anrDialog = d;
}
                
ensureBootCompleted();
} break;
case SHOW_FACTORY_ERROR_MSG: {
//Synthetic comment -- @@ -1176,7 +1177,7 @@
d.setCancelable(false);
d.setTitle("System UIDs Inconsistent");
d.setMessage("UIDs on the system are inconsistent, you need to wipe your data partition or your device will be unstable.");
                d.setButton("I'm Feeling Lucky",
mHandler.obtainMessage(IM_FEELING_LUCKY_MSG));
mUidAlert = d;
d.show();
//Synthetic comment -- @@ -1242,7 +1243,7 @@
public static void setSystemProcess() {
try {
ActivityManagerService m = mSelf;
            
ServiceManager.addService("activity", m);
ServiceManager.addService("meminfo", new MemBinder(m));
if (MONITOR_CPU_USAGE) {
//Synthetic comment -- @@ -1254,7 +1255,7 @@
mSelf.mContext.getPackageManager().getApplicationInfo(
"android", STOCK_PM_FLAGS);
mSystemThread.installSystemApplicationInfo(info);
       
synchronized (mSelf) {
ProcessRecord app = mSelf.newProcessRecordLocked(
mSystemThread.getApplicationThread(), info,
//Synthetic comment -- @@ -1303,24 +1304,24 @@
m.mGoingToSleep = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ActivityManager-Sleep");
m.mLaunchingActivity = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ActivityManager-Launch");
m.mLaunchingActivity.setReferenceCounted(false);
        
m.mBatteryStatsService.publish(context);
m.mUsageStatsService.publish(context);
        
synchronized (thr) {
thr.mReady = true;
thr.notifyAll();
}

m.startRunning(null, null, null, null);
        
return context;
}

public static ActivityManagerService self() {
return mSelf;
}
    
static class AThread extends Thread {
ActivityManagerService mService;
boolean mReady = false;
//Synthetic comment -- @@ -1373,7 +1374,7 @@
try {
pid = Integer.parseInt(args[0]);
} catch (NumberFormatException e) {
                        
}
for (int i=service.mLruProcesses.size()-1; i>=0; i--) {
ProcessRecord proc = service.mLruProcesses.get(i);
//Synthetic comment -- @@ -1420,7 +1421,7 @@
}

Slog.i(TAG, "Memory class: " + ActivityManager.staticGetMemoryClass());
        
File dataDir = Environment.getDataDirectory();
File systemDir = new File(dataDir, "system");
systemDir.mkdirs();
//Synthetic comment -- @@ -1428,7 +1429,7 @@
systemDir, "batterystats.bin").toString());
mBatteryStatsService.getActiveStatistics().readLocked();
mBatteryStatsService.getActiveStatistics().writeLocked();
        
mUsageStatsService = new UsageStatsService( new File(
systemDir, "usagestats").toString());

//Synthetic comment -- @@ -1438,7 +1439,7 @@
mConfiguration.setToDefaults();
mConfiguration.locale = Locale.getDefault();
mProcessStats.init();
        
// Add ourself to the Watchdog monitors.
Watchdog.getInstance().addMonitor(this);

//Synthetic comment -- @@ -1536,7 +1537,7 @@
(softIrq * 100) / total);
}
}
            
long[] cpuSpeedTimes = mProcessStats.getLastCpuSpeedTimes();
final BatteryStatsImpl bstats = mBatteryStatsService.getActiveStatistics();
synchronized(bstats) {
//Synthetic comment -- @@ -1572,7 +1573,7 @@
}
}
}
    
/**
* Initialize the application bind args. These are passed to each
* process when the bindApplication() IPC is sent to the process. They're
//Synthetic comment -- @@ -1603,12 +1604,12 @@
// put it on the LRU to keep track of when it should be exited.
int lrui = mLruProcesses.indexOf(app);
if (lrui >= 0) mLruProcesses.remove(lrui);
        
int i = mLruProcesses.size()-1;
int skipTop = 0;
        
app.lruSeq = mLruSeq;
        
// compute the new weight for this process.
if (updateActivityTime) {
app.lastActivityTime = SystemClock.uptimeMillis();
//Synthetic comment -- @@ -1631,7 +1632,7 @@
// Also don't let it kick out the first few "real" hidden processes.
skipTop = MIN_HIDDEN_APPS;
}
        
while (i >= 0) {
ProcessRecord p = mLruProcesses.get(i);
// If this app shouldn't be in front of the first N background
//Synthetic comment -- @@ -1648,7 +1649,7 @@
if (i < 0) {
mLruProcesses.add(0, app);
}
        
// If the app is currently using a content provider or service,
// bump those processes as well.
if (app.connections.size() > 0) {
//Synthetic comment -- @@ -1669,7 +1670,7 @@
}
}
}
        
//Slog.i(TAG, "Putting proc to front: " + app.processName);
if (oomAdj) {
updateOomAdjLocked();
//Synthetic comment -- @@ -1681,7 +1682,7 @@
mLruSeq++;
updateLruProcessInternalLocked(app, oomAdj, updateActivityTime, 0);
}
    
private final boolean updateLRUListLocked(HistoryRecord r) {
final boolean hadit = mLRUActivities.remove(r);
mLRUActivities.add(r);
//Synthetic comment -- @@ -1715,10 +1716,10 @@
/**
* This is a simplified version of topRunningActivityLocked that provides a number of
* optional skip-over modes.  It is intended for use with the ActivityController hook only.
     * 
* @param token If non-null, any history records matching this token will be skipped.
* @param taskId If non-zero, we'll attempt to skip over records with the same task ID.
     * 
* @return Returns the HistoryRecord of the next activity on the stack.
*/
private final HistoryRecord topRunningActivityLocked(IBinder token, int taskId) {
//Synthetic comment -- @@ -1757,14 +1758,14 @@
} catch (RemoteException e) {
}
}
    
private boolean isNextTransitionForward() {
int transit = mWindowManager.getPendingAppTransition();
return transit == WindowManagerPolicy.TRANSIT_ACTIVITY_OPEN
|| transit == WindowManagerPolicy.TRANSIT_TASK_OPEN
|| transit == WindowManagerPolicy.TRANSIT_TASK_TO_FRONT;
}
    
private final boolean realStartActivityLocked(HistoryRecord r,
ProcessRecord app, boolean andResume, boolean checkConfig)
throws RemoteException {
//Synthetic comment -- @@ -1857,7 +1858,7 @@
mResumedActivity = r;
r.task.touchActiveTime();
completeResumeLocked(r);
            pauseIfSleepingLocked();                
} else {
// This activity is not starting in the resumed state... which
// should look like we asked it to pause+stop (but remain visible),
//Synthetic comment -- @@ -1872,7 +1873,7 @@
// a chance to initialize itself while in the background, making the
// switch back to it faster and look better.
startSetupActivityLocked();
        
return true;
}

//Synthetic comment -- @@ -1881,7 +1882,7 @@
// Is this activity's application already running?
ProcessRecord app = getProcessRecordLocked(r.processName,
r.info.applicationInfo.uid);
        
if (r.startTime == 0) {
r.startTime = SystemClock.uptimeMillis();
if (mInitialStartTime == 0) {
//Synthetic comment -- @@ -1890,7 +1891,7 @@
} else if (mInitialStartTime == 0) {
mInitialStartTime = SystemClock.uptimeMillis();
}
        
if (app != null && app.thread != null) {
try {
realStartActivityLocked(r, app, andResume, checkConfig);
//Synthetic comment -- @@ -1936,7 +1937,7 @@

String hostingNameStr = hostingName != null
? hostingName.flattenToShortString() : null;
        
if ((intentFlags&Intent.FLAG_FROM_BACKGROUND) != 0) {
// If we are in the background, then check to see if this process
// is bad.  If so, we will just silently fail.
//Synthetic comment -- @@ -1958,7 +1959,7 @@
}
}
}
        
if (app == null) {
app = newProcessRecordLocked(null, info, processName);
mProcessNames.put(processName, info.uid, app);
//Synthetic comment -- @@ -1985,7 +1986,7 @@
boolean isAllowedWhileBooting(ApplicationInfo ai) {
return (ai.flags&ApplicationInfo.FLAG_PERSISTENT) != 0;
}
    
private final void startProcessLocked(ProcessRecord app,
String hostingType, String hostingNameStr) {
if (app.pid > 0 && app.pid != MY_PID) {
//Synthetic comment -- @@ -1999,10 +2000,10 @@
mProcessesOnHold.remove(app);

updateCpuStats();
        
System.arraycopy(mProcDeaths, 0, mProcDeaths, 1, mProcDeaths.length-1);
mProcDeaths[0] = 0;
        
try {
int uid = app.info.uid;
int[] gids = null;
//Synthetic comment -- @@ -2048,15 +2049,15 @@
app.batteryStats.incStartsLocked();
}
}
            
EventLog.writeEvent(EventLogTags.AM_PROC_START, pid, uid,
app.processName, hostingType,
hostingNameStr != null ? hostingNameStr : "");
            
if (app.persistent) {
Watchdog.getInstance().processStarted(app, app.processName, pid);
}
            
StringBuilder buf = mStringBuilder;
buf.setLength(0);
buf.append("Start proc ");
//Synthetic comment -- @@ -2130,7 +2131,7 @@
prev.task.touchActiveTime();

updateCpuStats();
        
if (prev.app != null && prev.app.thread != null) {
if (DEBUG_PAUSE) Slog.v(TAG, "Enqueueing pending pause: " + prev);
try {
//Synthetic comment -- @@ -2192,7 +2193,7 @@
private final void completePauseLocked() {
HistoryRecord prev = mPausingActivity;
if (DEBUG_PAUSE) Slog.v(TAG, "Complete pause: " + prev);
        
if (prev != null) {
if (prev.finishing) {
if (DEBUG_PAUSE) Slog.v(TAG, "Executing finish of activity: " + prev);
//Synthetic comment -- @@ -2242,7 +2243,7 @@
notifyAll();
}
}
        
if (prev != null) {
prev.resumeKeyDispatchingLocked();
}
//Synthetic comment -- @@ -2293,7 +2294,7 @@
}

reportResumedActivityLocked(next);
        
next.thumbnail = null;
setFocusedActivityLocked(next);
next.resumeKeyDispatchingLocked();
//Synthetic comment -- @@ -2340,16 +2341,16 @@
if (r.finishing) {
continue;
}
            
final boolean doThisProcess = onlyThisProcess == null
|| onlyThisProcess.equals(r.processName);
            
// First: if this is not the current activity being started, make
// sure it matches the current configuration.
if (r != starting && doThisProcess) {
ensureActivityConfigurationLocked(r, 0);
}
            
if (r.app == null || r.app.thread == null) {
if (onlyThisProcess == null
|| onlyThisProcess.equals(r.processName)) {
//Synthetic comment -- @@ -2466,7 +2467,7 @@
ensureActivitiesVisibleLocked(r, starting, null, configChanges);
}
}
    
private void updateUsageStats(HistoryRecord resumedComponent, boolean resumed) {
if (resumed) {
mUsageStatsService.noteResumeComponent(resumedComponent.realActivity);
//Synthetic comment -- @@ -2506,11 +2507,11 @@
null, null, 0, 0, 0, false, false);
}
}
        
        
return true;
}
    
/**
* Starts the "new version setup screen" if appropriate.
*/
//Synthetic comment -- @@ -2519,7 +2520,7 @@
if (mCheckedForSetup) {
return;
}
        
// We will show this screen if the current one is a different
// version than the last one shown, and we are not running in
// low-level factory test mode.
//Synthetic comment -- @@ -2528,12 +2529,12 @@
Settings.Secure.getInt(resolver,
Settings.Secure.DEVICE_PROVISIONED, 0) != 0) {
mCheckedForSetup = true;
            
// See if we should be showing the platform update setup UI.
Intent intent = new Intent(Intent.ACTION_UPGRADE_SETUP);
List<ResolveInfo> ris = mSelf.mContext.getPackageManager()
.queryIntentActivities(intent, PackageManager.GET_META_DATA);
            
// We don't allow third party apps to replace this.
ResolveInfo ri = null;
for (int i=0; ris != null && i<ris.size(); i++) {
//Synthetic comment -- @@ -2543,7 +2544,7 @@
break;
}
}
            
if (ri != null) {
String vers = ri.activityInfo.metaData != null
? ri.activityInfo.metaData.getString(Intent.METADATA_SETUP_VERSION)
//Synthetic comment -- @@ -2564,13 +2565,13 @@
}
}
}
    
private void reportResumedActivityLocked(HistoryRecord r) {
//Slog.i(TAG, "**** REPORT RESUME: " + r);
        
final int identHash = System.identityHashCode(r);
updateUsageStats(r, true);
        
int i = mWatchers.beginBroadcast();
while (i > 0) {
i--;
//Synthetic comment -- @@ -2584,7 +2585,7 @@
}
mWatchers.finishBroadcast();
}
    
/**
* Ensure that the top activity in the stack is resumed.
*
//Synthetic comment -- @@ -2610,7 +2611,7 @@
}

next.delayedResume = false;
        
// If the top activity is the resumed one, nothing to do.
if (mResumedActivity == next && next.state == ActivityState.RESUMED) {
// Make sure we have executed any pending transitions, since there
//Synthetic comment -- @@ -2630,7 +2631,7 @@
mNoAnimActivities.clear();
return false;
}
        
// The activity may be waiting for stop, but that is no longer
// appropriate for it.
mStoppingActivities.remove(next);
//Synthetic comment -- @@ -2734,7 +2735,7 @@
ActivityState lastState = next.state;

updateCpuStats();
            
next.state = ActivityState.RESUMED;
mResumedActivity = next;
next.task.touchActiveTime();
//Synthetic comment -- @@ -2773,7 +2774,7 @@
mNoAnimActivities.clear();
return true;
}
            
try {
// Deliver all pending results.
ArrayList a = next.results;
//Synthetic comment -- @@ -2794,10 +2795,10 @@
EventLog.writeEvent(EventLogTags.AM_RESUME_ACTIVITY,
System.identityHashCode(next),
next.task.taskId, next.shortComponentName);
                
next.app.thread.scheduleResumeActivity(next,
isNextTransitionForward());
                
pauseIfSleepingLocked();

} catch (Exception e) {
//Synthetic comment -- @@ -2862,7 +2863,7 @@
final int NH = mHistory.size();

int addPos = -1;
        
if (!newTask) {
// If starting in an existing task, find where that is...
HistoryRecord next = null;
//Synthetic comment -- @@ -2902,7 +2903,7 @@
if (addPos < 0) {
addPos = mHistory.size();
}
        
// If we are not placing the new activity frontmost, we do not want
// to deliver the onUserLeaving callback to the actual frontmost
// activity
//Synthetic comment -- @@ -2910,7 +2911,7 @@
mUserLeaving = false;
if (DEBUG_USER_LEAVING) Slog.v(TAG, "startActivity() behind front, mUserLeaving=false");
}
        
// Slot the activity into the history stack and proceed
mHistory.add(addPos, r);
r.inHistory = true;
//Synthetic comment -- @@ -3003,7 +3004,7 @@
private final HistoryRecord performClearTaskLocked(int taskId,
HistoryRecord newR, int launchFlags, boolean doClear) {
int i = mHistory.size();
        
// First find the requested task.
while (i > 0) {
i--;
//Synthetic comment -- @@ -3013,7 +3014,7 @@
break;
}
}
        
// Now clear it.
while (i > 0) {
i--;
//Synthetic comment -- @@ -3040,7 +3041,7 @@
}
}
}
                
// Finally, if this is a normal launch mode (that is, not
// expecting onNewIntent()), then we will finish the current
// instance of the activity so a new fresh one can be started.
//Synthetic comment -- @@ -3055,7 +3056,7 @@
return null;
}
}
                
return ret;
}
}
//Synthetic comment -- @@ -3264,7 +3265,7 @@
return START_SWITCHES_CANCELED;
}
}
        
if (mDidAppSwitch) {
// This is the second allowed switch since we stopped switches,
// so now just generally allow switches.  Use case: user presses
//Synthetic comment -- @@ -3275,13 +3276,13 @@
} else {
mDidAppSwitch = true;
}
     
doPendingActivityLaunchesLocked(false);
        
return startActivityUncheckedLocked(r, sourceRecord,
grantedUriPermissions, grantedMode, onlyIfNeeded, true);
}
  
private final void doPendingActivityLaunchesLocked(boolean doResume) {
final int N = mPendingActivityLaunches.size();
if (N <= 0) {
//Synthetic comment -- @@ -3295,28 +3296,28 @@
}
mPendingActivityLaunches.clear();
}
    
private final int startActivityUncheckedLocked(HistoryRecord r,
HistoryRecord sourceRecord, Uri[] grantedUriPermissions,
int grantedMode, boolean onlyIfNeeded, boolean doResume) {
final Intent intent = r.intent;
final int callingUid = r.launchedFromUid;
        
int launchFlags = intent.getFlags();
        
// We'll invoke onUserLeaving before onPause only if the launching
// activity did not explicitly state that this is an automated launch.
mUserLeaving = (launchFlags&Intent.FLAG_ACTIVITY_NO_USER_ACTION) == 0;
if (DEBUG_USER_LEAVING) Slog.v(TAG,
"startActivity() => mUserLeaving=" + mUserLeaving);
        
// If the caller has asked not to resume at this point, we make note
// of this in the record so that we can skip it when trying to find
// the top running activity.
if (!doResume) {
r.delayedResume = true;
}
        
HistoryRecord notTop = (launchFlags&Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
!= 0 ? r : null;

//Synthetic comment -- @@ -3577,7 +3578,7 @@
+ " in new task " + r.task);
newTask = true;
addRecentTaskLocked(r.task);
            
} else if (sourceRecord != null) {
if (!addingToTask &&
(launchFlags&Intent.FLAG_ACTIVITY_CLEAR_TOP) != 0) {
//Synthetic comment -- @@ -3654,7 +3655,7 @@
}
notify();
}
    
void reportActivityVisibleLocked(HistoryRecord r) {
for (int i=mWaitingActivityVisible.size()-1; i>=0; i--) {
WaitResult w = mWaitingActivityVisible.get(i);
//Synthetic comment -- @@ -3667,7 +3668,7 @@
}
notify();
}
    
private final int startActivityMayWait(IApplicationThread caller,
Intent intent, String resolvedType, Uri[] grantedUriPermissions,
int grantedMode, IBinder resultTo,
//Synthetic comment -- @@ -3679,7 +3680,7 @@
}

final boolean componentSpecified = intent.getComponent() != null;
        
// Don't modify the client's object!
intent = new Intent(intent);

//Synthetic comment -- @@ -3721,18 +3722,18 @@
} else {
callingPid = callingUid = -1;
}
            
mConfigWillChange = config != null && mConfiguration.diff(config) != 0;
if (DEBUG_CONFIGURATION) Slog.v(TAG,
"Starting activity when config will change = " + mConfigWillChange);
            
final long origId = Binder.clearCallingIdentity();
            
int res = startActivityLocked(caller, intent, resolvedType,
grantedUriPermissions, grantedMode, aInfo,
resultTo, resultWho, requestCode, callingPid, callingUid,
onlyIfNeeded, componentSpecified);
            
if (mConfigWillChange) {
// If the caller also wants to switch to a new configuration,
// do so now.  This allows a clean switch, as we are waiting
//Synthetic comment -- @@ -3745,9 +3746,9 @@
"Updating to new configuration after starting activity.");
updateConfigurationLocked(config, null);
}
            
Binder.restoreCallingIdentity(origId);
            
if (outResult != null) {
outResult.result = res;
if (res == IActivityManager.START_SUCCESS) {
//Synthetic comment -- @@ -3777,7 +3778,7 @@
}
}
}
            
return res;
}
}
//Synthetic comment -- @@ -3803,7 +3804,7 @@
requestCode, onlyIfNeeded, debug, res, null);
return res;
}
    
public final int startActivityWithConfig(IApplicationThread caller,
Intent intent, String resolvedType, Uri[] grantedUriPermissions,
int grantedMode, IBinder resultTo,
//Synthetic comment -- @@ -3822,14 +3823,14 @@
if (fillInIntent != null && fillInIntent.hasFileDescriptors()) {
throw new IllegalArgumentException("File descriptors passed in Intent");
}
        
IIntentSender sender = intent.getTarget();
if (!(sender instanceof PendingIntentRecord)) {
throw new IllegalArgumentException("Bad PendingIntent object");
}
        
PendingIntentRecord pir = (PendingIntentRecord)sender;
        
synchronized (this) {
// If this is coming from the currently resumed activity, it is
// effectively saying that app switches are allowed at this point.
//Synthetic comment -- @@ -3839,11 +3840,11 @@
mAppSwitchesAllowedTime = 0;
}
}
        
return pir.sendInner(0, fillInIntent, resolvedType,
null, resultTo, resultWho, requestCode, flagsMask, flagsValues);
}
    
public boolean startNextMatchingActivity(IBinder callingActivity,
Intent intent) {
// Refuse possible leaked file descriptors
//Synthetic comment -- @@ -3941,7 +3942,7 @@
public final int startActivityInPackage(int uid,
Intent intent, String resolvedType, IBinder resultTo,
String resultWho, int requestCode, boolean onlyIfNeeded) {
        
// This is so super not safe, that only the system (or okay root)
// can do it.
final int callingUid = Binder.getCallingUid();
//Synthetic comment -- @@ -3949,9 +3950,9 @@
throw new SecurityException(
"startActivityInPackage only available to the system");
}
        
final boolean componentSpecified = intent.getComponent() != null;
        
// Don't modify the client's object!
intent = new Intent(intent);

//Synthetic comment -- @@ -4102,7 +4103,7 @@
break;
}
}
        
// If this is the last activity, but it is the home activity, then
// just don't finish it.
if (lastActivity) {
//Synthetic comment -- @@ -4110,7 +4111,7 @@
return false;
}
}
        
finishActivityLocked(r, index, resultCode, resultData, reason);
return true;
}
//Synthetic comment -- @@ -4175,7 +4176,7 @@
r.pendingResults = null;
r.newIntents = null;
r.icicle = null;
        
if (mPendingThumbnails.size() > 0) {
// There are clients waiting to receive thumbnails so, in case
// this is an activity that someone is waiting for, add it
//Synthetic comment -- @@ -4191,10 +4192,10 @@
mWindowManager.prepareAppTransition(endTask
? WindowManagerPolicy.TRANSIT_TASK_CLOSE
: WindowManagerPolicy.TRANSIT_ACTIVITY_CLOSE);
    
// Tell window manager to prepare for this one to be removed.
mWindowManager.setAppVisibility(r, false);
                
if (mPausingActivity == null) {
if (DEBUG_PAUSE) Slog.v(TAG, "Finish needs to pause: " + r);
if (DEBUG_USER_LEAVING) Slog.v(TAG, "finish() => pause with userLeaving=false");
//Synthetic comment -- @@ -4277,11 +4278,11 @@

/**
* This is the internal entry point for handling Activity.finish().
     * 
* @param token The Binder token referencing the Activity we want to finish.
* @param resultCode Result code, if any, from this Activity.
* @param resultData Result data (Intent), if any, from this Activity.
     * 
* @return Returns true if the activity successfully finished, or false if it is still running.
*/
public final boolean finishActivity(IBinder token, int resultCode, Intent resultData) {
//Synthetic comment -- @@ -4302,7 +4303,7 @@
} catch (RemoteException e) {
mController = null;
}
    
if (!resumeOK) {
return false;
}
//Synthetic comment -- @@ -4384,7 +4385,7 @@
return true;
}
}
    
public void overridePendingTransition(IBinder token, String packageName,
int enterAnim, int exitAnim) {
synchronized(this) {
//Synthetic comment -- @@ -4395,17 +4396,17 @@
HistoryRecord self = (HistoryRecord)mHistory.get(index);

final long origId = Binder.clearCallingIdentity();
            
if (self.state == ActivityState.RESUMED
|| self.state == ActivityState.PAUSING) {
mWindowManager.overridePendingAppTransition(packageName,
enterAnim, exitAnim);
}
            
Binder.restoreCallingIdentity(origId);
}
}
    
/**
* Perform clean-up of service connections in an activity record.
*/
//Synthetic comment -- @@ -4420,7 +4421,7 @@
r.connections = null;
}
}
    
/**
* Perform the common clean-up of an activity record.  This is called both
* as part of destroyActivityLocked() (when destroying the client-side
//Synthetic comment -- @@ -4444,7 +4445,7 @@
// down to the max limit while they are still waiting to finish.
mFinishingActivities.remove(r);
mWaitingVisibleActivities.remove(r);
        
// Remove any pending results.
if (r.finishing && r.pendingResults != null) {
for (WeakReference<PendingIntentRecord> apr : r.pendingResults) {
//Synthetic comment -- @@ -4457,7 +4458,7 @@
}

if (cleanServices) {
            cleanUpActivityServicesLocked(r);            
}

if (mPendingThumbnails.size() > 0) {
//Synthetic comment -- @@ -4485,7 +4486,7 @@
removeActivityUriPermissionsLocked(r);
}
}
    
/**
* Destroy the current CLIENT SIDE instance of an activity.  This may be
* called both when actually finishing an activity, or when performing
//Synthetic comment -- @@ -4502,11 +4503,11 @@
r.task.taskId, r.shortComponentName);

boolean removedFromHistory = false;
        
cleanUpActivityLocked(r, false);

final boolean hadApp = r.app != null;
        
if (hadApp) {
if (removeFromApp) {
int idx = r.app.activities.indexOf(r);
//Synthetic comment -- @@ -4524,7 +4525,7 @@
}

boolean skipDestroy = false;
            
try {
if (DEBUG_SWITCH) Slog.i(TAG, "Destroying: " + r);
r.app.thread.scheduleDestroyActivity(r, r.finishing,
//Synthetic comment -- @@ -4543,7 +4544,7 @@

r.app = null;
r.nowVisible = false;
            
if (r.finishing && !skipDestroy) {
r.state = ActivityState.DESTROYING;
Message msg = mHandler.obtainMessage(DESTROY_TIMEOUT_MSG);
//Synthetic comment -- @@ -4563,11 +4564,11 @@
}

r.configChangeFlags = 0;
        
if (!mLRUActivities.remove(r) && hadApp) {
Slog.w(TAG, "Activity " + r + " being finished, but not in LRU list");
}
        
return removedFromHistory;
}

//Synthetic comment -- @@ -4663,7 +4664,7 @@
}

app.activities.clear();
        
if (app.instrumentationClass != null) {
Slog.w(TAG, "Crash of app " + app.processName
+ " running instrumentation " + app.instrumentationClass);
//Synthetic comment -- @@ -4713,7 +4714,7 @@
IApplicationThread thread) {

mProcDeaths[0]++;
        
// Clean up already done if the process has been re-started.
if (app.pid == pid && app.thread != null &&
app.thread.asBinder() == thread.asBinder()) {
//Synthetic comment -- @@ -4740,7 +4741,7 @@
break;
}
}
                
if (!haveBg) {
Slog.i(TAG, "Low Memory: No more background processes.");
EventLog.writeEvent(EventLogTags.AM_LOW_MEMORY, mLruProcesses.size());
//Synthetic comment -- @@ -4832,7 +4833,7 @@
final void appNotResponding(ProcessRecord app, HistoryRecord activity,
HistoryRecord parent, final String annotation) {
ArrayList<Integer> pids = new ArrayList<Integer>(20);
        
synchronized (this) {
// PowerManager.reboot() can block for a long time, so ignore ANRs while shutting down.
if (mShuttingDown) {
//Synthetic comment -- @@ -4845,7 +4846,7 @@
Slog.i(TAG, "Crashing app skipping ANR: " + app + " " + annotation);
return;
}
            
// In case we come through here for the same app before completing
// this one, mark as anring now so we will bail out.
app.notResponding = true;
//Synthetic comment -- @@ -4856,11 +4857,11 @@

// Dump thread traces as quickly as we can, starting with "interesting" processes.
pids.add(app.pid);
    
int parentPid = app.pid;
if (parent != null && parent.app != null && parent.app.pid > 0) parentPid = parent.app.pid;
if (parentPid != app.pid) pids.add(parentPid);
    
if (MY_PID != app.pid && MY_PID != parentPid) pids.add(MY_PID);

for (int i = mLruProcesses.size() - 1; i >= 0; i--) {
//Synthetic comment -- @@ -4922,19 +4923,19 @@
// Unless configured otherwise, swallow ANRs in background processes & kill the process.
boolean showBackground = Settings.Secure.getInt(mContext.getContentResolver(),
Settings.Secure.ANR_SHOW_BACKGROUND, 0) != 0;
        
synchronized (this) {
if (!showBackground && !app.isInterestingToUserLocked() && app.pid != MY_PID) {
Process.killProcess(app.pid);
return;
}
    
// Set the app's notResponding state, and look up the errorReportReceiver
makeAppNotRespondingLocked(app,
activity != null ? activity.shortComponentName : null,
annotation != null ? "ANR " + annotation : "ANR",
info.toString());
    
// Bring up the infamous App Not Responding dialog
Message msg = Message.obtain();
HashMap map = new HashMap();
//Synthetic comment -- @@ -4944,7 +4945,7 @@
if (activity != null) {
map.put("activity", activity);
}
    
mHandler.sendMessage(msg);
}
}
//Synthetic comment -- @@ -5027,7 +5028,7 @@
}
}
}
    
public boolean clearApplicationUserData(final String packageName,
final IPackageDataObserver observer) {
int uid = Binder.getCallingUid();
//Synthetic comment -- @@ -5056,7 +5057,7 @@
"for process:"+packageName);
}
}
            
try {
//clear application user data
pm.clearApplicationUserData(packageName, observer);
//Synthetic comment -- @@ -5088,7 +5089,7 @@
Slog.w(TAG, msg);
throw new SecurityException(msg);
}
        
long callingId = Binder.clearCallingIdentity();
try {
IPackageManager pm = ActivityThread.getPackageManager();
//Synthetic comment -- @@ -5120,7 +5121,7 @@
Slog.w(TAG, msg);
throw new SecurityException(msg);
}
        
long callingId = Binder.clearCallingIdentity();
try {
IPackageManager pm = ActivityThread.getPackageManager();
//Synthetic comment -- @@ -5174,7 +5175,7 @@
if (reason != null) {
intent.putExtra("reason", reason);
}
        
final int uid = Binder.getCallingUid();
final long origId = Binder.clearCallingIdentity();
synchronized (this) {
//Synthetic comment -- @@ -5190,9 +5191,9 @@
}
}
mWatchers.finishBroadcast();
            
mWindowManager.closeSystemDialogs(reason);
            
for (i=mHistory.size()-1; i>=0; i--) {
HistoryRecord r = (HistoryRecord)mHistory.get(i);
if ((r.info.flags&ActivityInfo.FLAG_FINISH_ON_CLOSE_SYSTEM_DIALOGS) != 0) {
//Synthetic comment -- @@ -5200,13 +5201,13 @@
Activity.RESULT_CANCELED, null, "close-sys");
}
}
            
broadcastIntentLocked(null, null, intent, null,
null, 0, null, null, null, false, false, -1, uid);
}
Binder.restoreCallingIdentity(origId);
}
    
public Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids)
throws RemoteException {
Debug.MemoryInfo[] infos = new Debug.MemoryInfo[pids.length];
//Synthetic comment -- @@ -5253,7 +5254,7 @@
null, null, 0, null, null, null,
false, false, MY_PID, Process.SYSTEM_UID);
}
    
private final boolean killPackageProcessesLocked(String packageName, int uid,
int minOomAdj, boolean callerWillRestart, boolean doit) {
ArrayList<ProcessRecord> procs = new ArrayList<ProcessRecord>();
//Synthetic comment -- @@ -5283,7 +5284,7 @@
}
}
}
        
int N = procs.size();
for (int i=0; i<N; i++) {
removeProcessLocked(procs.get(i), callerWillRestart);
//Synthetic comment -- @@ -5313,10 +5314,10 @@
}
}
}
        
boolean didSomething = killPackageProcessesLocked(name, uid, -100,
callerWillRestart, doit);
        
for (i=mHistory.size()-1; i>=0; i--) {
HistoryRecord r = (HistoryRecord)mHistory.get(i);
if (r.packageName.equals(name)) {
//Synthetic comment -- @@ -5353,7 +5354,7 @@
for (i=0; i<N; i++) {
bringDownServiceLocked(services.get(i), true);
}
        
if (doit) {
if (purgeCache) {
AttributeCache ac = AttributeCache.instance();
//Synthetic comment -- @@ -5363,7 +5364,7 @@
}
resumeTopActivityLocked(null);
}
        
return didSomething;
}

//Synthetic comment -- @@ -5385,7 +5386,7 @@
handleAppDiedLocked(app, true);
mLruProcesses.remove(app);
Process.killProcess(pid);
            
if (app.persistent) {
if (!callerWillRestart) {
addAppLocked(app.info);
//Synthetic comment -- @@ -5396,7 +5397,7 @@
} else {
mRemovedProcesses.add(app);
}
        
return needRestart;
}

//Synthetic comment -- @@ -5408,9 +5409,9 @@
if (knownApp != null && knownApp.thread == null) {
mPidsSelfLocked.remove(pid);
gone = true;
            }        
}
        
if (gone) {
Slog.w(TAG, "Process " + app + " failed to attach");
EventLog.writeEvent(EventLogTags.AM_PROCESS_START_TIMEOUT, pid, app.info.uid,
//Synthetic comment -- @@ -5506,7 +5507,7 @@
}

EventLog.writeEvent(EventLogTags.AM_PROC_BOUND, app.pid, app.processName);
        
app.thread = thread;
app.curAdj = app.setAdj = -100;
app.curSchedGroup = Process.THREAD_GROUP_DEFAULT;
//Synthetic comment -- @@ -5523,7 +5524,7 @@
if (!normalMode) {
Slog.i(TAG, "Launching preboot mode app: " + app);
}
        
if (localLOGV) Slog.v(
TAG, "New app record " + app
+ " thread=" + thread.asBinder() + " pid=" + pid);
//Synthetic comment -- @@ -5539,14 +5540,14 @@
mWaitForDebugger = mOrigWaitForDebugger;
}
}
            
// If the app is being launched for restore or full backup, set it up specially
boolean isRestrictedBackupMode = false;
if (mBackupTarget != null && mBackupAppName.equals(processName)) {
isRestrictedBackupMode = (mBackupTarget.backupMode == BackupRecord.RESTORE)
|| (mBackupTarget.backupMode == BackupRecord.BACKUP_FULL);
}
            
ensurePackageDexOpt(app.instrumentationInfo != null
? app.instrumentationInfo.packageName
: app.info.packageName);
//Synthetic comment -- @@ -5558,7 +5559,7 @@
thread.bindApplication(processName, app.instrumentationInfo != null
? app.instrumentationInfo : app.info, providers,
app.instrumentationClass, app.instrumentationProfileFile,
                    app.instrumentationArguments, app.instrumentationWatcher, testMode, 
isRestrictedBackupMode || !normalMode,
mConfiguration, getCommonServicesLocked());
updateLruProcessLocked(app, false, true);
//Synthetic comment -- @@ -5760,7 +5761,7 @@
if (fromTimeout) {
reportActivityLaunchedLocked(fromTimeout, r, -1, -1);
}
                
// This is a hack to semi-deal with a race condition
// in the client where it can be constructed with a
// newer configuration from when we asked it to launch.
//Synthetic comment -- @@ -5769,7 +5770,7 @@
if (config != null) {
r.configuration = config;
}
                
// No longer need to keep the device awake.
if (mResumedActivity == r && mLaunchingActivity.isHeld()) {
mHandler.removeMessages(LAUNCH_TIMEOUT_MSG);
//Synthetic comment -- @@ -5795,7 +5796,7 @@
mBooted = true;
enableScreen = true;
}
                
} else if (fromTimeout) {
reportActivityLaunchedLocked(fromTimeout, null, -1, -1);
}
//Synthetic comment -- @@ -5887,7 +5888,7 @@
}
}
}, pkgFilter);
        
synchronized (this) {
// Ensure that any processes we had put on hold are now started
// up.
//Synthetic comment -- @@ -5899,7 +5900,7 @@
this.startProcessLocked(procs.get(ip), "on-hold", null);
}
}
            
if (mFactoryTest != SystemServer.FACTORY_TEST_LOW_LEVEL) {
// Tell anyone interested that we are done booting!
broadcastIntentLocked(null, null,
//Synthetic comment -- @@ -5910,7 +5911,7 @@
}
}
}
    
final void ensureBootCompleted() {
boolean booting;
boolean enableScreen;
//Synthetic comment -- @@ -5920,7 +5921,7 @@
enableScreen = !mBooted;
mBooted = true;
}
        
if (booting) {
finishBooting();
}
//Synthetic comment -- @@ -5929,7 +5930,7 @@
enableScreenAfterBoot();
}
}
    
public final void activityPaused(IBinder token, Bundle icicle) {
// Refuse possible leaked file descriptors
if (icicle != null && icicle.hasFileDescriptors()) {
//Synthetic comment -- @@ -5961,10 +5962,10 @@
r.state = ActivityState.PAUSED;
completePauseLocked();
} else {
                	EventLog.writeEvent(EventLogTags.AM_FAILED_TO_PAUSE,
                	        System.identityHashCode(r), r.shortComponentName, 
                			mPausingActivity != null
                			    ? mPausingActivity.shortComponentName : "(none)");
}
}
}
//Synthetic comment -- @@ -6009,7 +6010,7 @@
if (DEBUG_SWITCH) Slog.v(TAG, "ACTIVITY DESTROYED: " + token);
synchronized (this) {
mHandler.removeMessages(DESTROY_TIMEOUT_MSG, token);
            
int index = indexOfTokenLocked(token);
if (index >= 0) {
HistoryRecord r = (HistoryRecord)mHistory.get(index);
//Synthetic comment -- @@ -6021,7 +6022,7 @@
}
}
}
    
public String getCallingPackage(IBinder token) {
synchronized (this) {
HistoryRecord r = getCallingRecordLocked(token);
//Synthetic comment -- @@ -6083,7 +6084,7 @@
"Can't use FLAG_RECEIVER_BOOT_UPGRADE here");
}
}
        
synchronized(this) {
int callingUid = Binder.getCallingUid();
try {
//Synthetic comment -- @@ -6228,13 +6229,13 @@
updateOomAdjLocked();
}
}
    
public void setProcessForeground(IBinder token, int pid, boolean isForeground) {
enforceCallingPermission(android.Manifest.permission.SET_PROCESS_LIMIT,
"setProcessForeground()");
synchronized(this) {
boolean changed = false;
            
synchronized (mPidsSelfLocked) {
ProcessRecord pr = mPidsSelfLocked.get(pid);
if (pr == null) {
//Synthetic comment -- @@ -6267,13 +6268,13 @@
}
}
}
            
if (changed) {
updateOomAdjLocked();
}
}
}
    
// =========================================================
// PERMISSIONS
// =========================================================
//Synthetic comment -- @@ -6335,7 +6336,7 @@
* permission is automatically denied.  (Internally a null permission
* string is used when calling {@link #checkComponentPermission} in cases
* when only uid-based security is needed.)
     * 
* This can be called with or without the global lock held.
*/
public int checkPermission(String permission, int pid, int uid) {
//Synthetic comment -- @@ -6436,14 +6437,14 @@
return;
}

        if (DEBUG_URI_PERMISSION) Slog.v(TAG, 
"Requested grant " + targetPkg + " permission to " + uri);
        
final IPackageManager pm = ActivityThread.getPackageManager();

// If this is not a content: uri, we can't do anything with it.
if (!ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            if (DEBUG_URI_PERMISSION) Slog.v(TAG, 
"Can't grant URI permission for non-content URI: " + uri);
return;
}
//Synthetic comment -- @@ -6470,7 +6471,7 @@
try {
targetUid = pm.getPackageUid(targetPkg);
if (targetUid < 0) {
                if (DEBUG_URI_PERMISSION) Slog.v(TAG, 
"Can't grant URI permission no uid for: " + targetPkg);
return;
}
//Synthetic comment -- @@ -6481,7 +6482,7 @@
// First...  does the target actually need this permission?
if (checkHoldingPermissionsLocked(pm, pi, targetUid, modeFlags)) {
// No need to grant the target this permission.
            if (DEBUG_URI_PERMISSION) Slog.v(TAG, 
"Target " + targetPkg + " already has full permission to " + uri);
return;
}
//Synthetic comment -- @@ -6524,9 +6525,9 @@
// to the uri, and the target doesn't.  Let's now give this to
// the target.

        if (DEBUG_URI_PERMISSION) Slog.v(TAG, 
"Granting " + targetPkg + " permission to " + uri);
        
HashMap<Uri, UriPermission> targetUris
= mGrantedUriPermissions.get(targetUid);
if (targetUris == null) {
//Synthetic comment -- @@ -6600,7 +6601,7 @@
HashMap<Uri, UriPermission> perms
= mGrantedUriPermissions.get(perm.uid);
if (perms != null) {
                if (DEBUG_URI_PERMISSION) Slog.v(TAG, 
"Removing " + perm.uid + " permission to " + perm.uri);
perms.remove(perm.uri);
if (perms.size() == 0) {
//Synthetic comment -- @@ -6641,9 +6642,9 @@
return;
}

        if (DEBUG_URI_PERMISSION) Slog.v(TAG, 
"Revoking all granted permissions to " + uri);
        
final IPackageManager pm = ActivityThread.getPackageManager();

final String authority = uri.getAuthority();
//Synthetic comment -- @@ -6702,7 +6703,7 @@
continue toploop;
}
}
                    if (DEBUG_URI_PERMISSION) Slog.v(TAG, 
"Revoking " + perm.uid + " permission to " + perm.uri);
perm.clearModes(modeFlags);
if (perm.modeFlags == 0) {
//Synthetic comment -- @@ -6783,7 +6784,7 @@
outInfo.lowMemory = outInfo.availMem <
(HOME_APP_MEM + ((HIDDEN_APP_MEM-HOME_APP_MEM)/2));
}
    
// =========================================================
// TASK MANAGEMENT
// =========================================================
//Synthetic comment -- @@ -6933,7 +6934,7 @@
"getRecentTasks()");

IPackageManager pm = ActivityThread.getPackageManager();
            
final int N = mRecentTasks.size();
ArrayList<ActivityManager.RecentTaskInfo> res
= new ArrayList<ActivityManager.RecentTaskInfo>(
//Synthetic comment -- @@ -6950,7 +6951,7 @@
rti.baseIntent = new Intent(
tr.intent != null ? tr.intent : tr.affinityIntent);
rti.origActivity = tr.origActivity;
                    
if ((flags&ActivityManager.RECENT_IGNORE_UNAVAILABLE) != 0) {
// Check whether this activity is currently available.
try {
//Synthetic comment -- @@ -6968,7 +6969,7 @@
// Will never happen.
}
}
                    
res.add(rti);
maxNum--;
}
//Synthetic comment -- @@ -6979,9 +6980,9 @@

private final int findAffinityTaskTopLocked(int startIndex, String affinity) {
int j;
        TaskRecord startTask = ((HistoryRecord)mHistory.get(startIndex)).task; 
TaskRecord jt = startTask;
        
// First look backwards
for (j=startIndex-1; j>=0; j--) {
HistoryRecord r = (HistoryRecord)mHistory.get(j);
//Synthetic comment -- @@ -6992,7 +6993,7 @@
}
}
}
        
// Now look forwards
final int N = mHistory.size();
jt = startTask;
//Synthetic comment -- @@ -7005,15 +7006,15 @@
jt = r.task;
}
}
        
// Might it be at the top?
if (affinity.equals(((HistoryRecord)mHistory.get(N-1)).task.affinity)) {
return N-1;
}
        
return -1;
}
    
/**
* Perform a reset of the given task, if needed as part of launching it.
* Returns the new HistoryRecord at the top of the task.
//Synthetic comment -- @@ -7028,9 +7029,9 @@
forceReset = true;
}
}
        
final TaskRecord task = taskTop.task;
        
// We are going to move through the history list so that we can look
// at each activity 'target' with 'below' either the interesting
// activity immediately below it in the stack or null.
//Synthetic comment -- @@ -7041,7 +7042,7 @@
int lastReparentPos = -1;
for (int i=mHistory.size()-1; i>=-1; i--) {
HistoryRecord below = i >= 0 ? (HistoryRecord)mHistory.get(i) : null;
            
if (below != null && below.finishing) {
continue;
}
//Synthetic comment -- @@ -7054,14 +7055,14 @@
replyChainEnd = -1;
continue;
}
        
final int flags = target.info.flags;
            
final boolean finishOnTaskLaunch =
(flags&ActivityInfo.FLAG_FINISH_ON_TASK_LAUNCH) != 0;
final boolean allowTaskReparenting =
(flags&ActivityInfo.FLAG_ALLOW_TASK_REPARENTING) != 0;
            
if (target.task == task) {
// We are inside of the task being reset...  we'll either
// finish this activity, push it out for another task,
//Synthetic comment -- @@ -7197,7 +7198,7 @@
// should be left as-is.
replyChainEnd = -1;
}
                
} else if (target.resultTo != null) {
// If this activity is sending a reply to a previous
// activity, we can't do anything with it now until
//Synthetic comment -- @@ -7271,7 +7272,7 @@
}
}
replyChainEnd = -1;
                    
// Now we've moved it in to place...  but what if this is
// a singleTop activity and we have put it on top of another
// instance of the same activity?  Then we drop the instance
//Synthetic comment -- @@ -7293,14 +7294,14 @@
}
}
}
            
target = below;
targetI = i;
}
        
return taskTop;
}
    
/**
* TODO: Add mController hook
*/
//Synthetic comment -- @@ -7387,7 +7388,7 @@
} else {
mWindowManager.prepareAppTransition(WindowManagerPolicy.TRANSIT_TASK_TO_FRONT);
}
        
mWindowManager.moveAppTokensToTop(moved);
if (VALIDATE_TOKENS) {
mWindowManager.validateAppTokens(mHistory);
//Synthetic comment -- @@ -7421,7 +7422,7 @@
/**
* Moves an activity, and all of the other activities within the same task, to the bottom
* of the history stack.  The activity's order within the task is unchanged.
     * 
* @param token A reference to the activity we wish to move
* @param nonRoot If false then this only works if the activity is the root
*                of a task; if true it will work for any activity in a task.
//Synthetic comment -- @@ -7440,19 +7441,19 @@
}

/**
     * Worker method for rearranging history stack.  Implements the function of moving all 
     * activities for a specific task (gathering them if disjoint) into a single group at the 
* bottom of the stack.
     * 
* If a watcher is installed, the action is preflighted and the watcher has an opportunity
* to premeptively cancel the move.
     * 
* @param task The taskId to collect and move to the bottom.
* @return Returns true if the move completed, false if not.
*/
private final boolean moveTaskToBackLocked(int task, HistoryRecord reason) {
Slog.i(TAG, "moveTaskToBack: " + task);
        
// If we have a watcher, preflight the move before committing to it.  First check
// for *other* available tasks, but if none are available, then try again allowing the
// current task to be selected.
//Synthetic comment -- @@ -7479,7 +7480,7 @@

if (DEBUG_TRANSITION) Slog.v(TAG,
"Prepare to back transition: task=" + task);
        
final int N = mHistory.size();
int bottom = 0;
int pos = 0;
//Synthetic comment -- @@ -7781,7 +7782,7 @@
== PackageManager.PERMISSION_GRANTED) {
return null;
}
        
PathPermission[] pps = cpi.pathPermissions;
if (pps != null) {
int i = pps.length;
//Synthetic comment -- @@ -7801,7 +7802,7 @@
}
}
}
        
String msg = "Permission Denial: opening provider " + cpi.name
+ " from " + (r != null ? r : "(null)") + " (pid=" + callingPid
+ ", uid=" + callingUid + ") requires "
//Synthetic comment -- @@ -7908,7 +7909,7 @@
throw new IllegalArgumentException(
"Attempt to launch content provider before system ready");
}
                
cpr = (ContentProviderRecord)mProvidersByClass.get(cpi.name);
final boolean firstClass = cpr == null;
if (firstClass) {
//Synthetic comment -- @@ -8098,7 +8099,7 @@
updateOomAdjLocked();
}
}
    
public final void publishContentProviders(IApplicationThread caller,
List<ContentProviderHolder> providers) {
if (providers == null) {
//Synthetic comment -- @@ -8278,9 +8279,9 @@
throw new SecurityException("Requires permission "
+ android.Manifest.permission.SHUTDOWN);
}
        
boolean timedout = false;
        
synchronized(this) {
mShuttingDown = true;
mWindowManager.setEventDispatching(false);
//Synthetic comment -- @@ -8302,13 +8303,13 @@
}
}
}
        
mUsageStatsService.shutdown();
mBatteryStatsService.shutdown();
        
return timedout;
}
    
void pauseIfSleepingLocked() {
if (mSleeping || mShuttingDown) {
if (!mGoingToSleep.isHeld()) {
//Synthetic comment -- @@ -8329,7 +8330,7 @@
}
}
}
    
public void wakingUp() {
synchronized(this) {
if (mGoingToSleep.isHeld()) {
//Synthetic comment -- @@ -8347,7 +8348,7 @@
throw new SecurityException("Requires permission "
+ android.Manifest.permission.STOP_APP_SWITCHES);
}
        
synchronized(this) {
mAppSwitchesAllowedTime = SystemClock.uptimeMillis()
+ APP_SWITCH_DELAY_TIME;
//Synthetic comment -- @@ -8357,14 +8358,14 @@
mHandler.sendMessageDelayed(msg, APP_SWITCH_DELAY_TIME);
}
}
    
public void resumeAppSwitches() {
if (checkCallingPermission(android.Manifest.permission.STOP_APP_SWITCHES)
!= PackageManager.PERMISSION_GRANTED) {
throw new SecurityException("Requires permission "
+ android.Manifest.permission.STOP_APP_SWITCHES);
}
        
synchronized(this) {
// Note that we don't execute any pending app switches... we will
// let those wait until either the timeout, or the next start
//Synthetic comment -- @@ -8372,24 +8373,24 @@
mAppSwitchesAllowedTime = 0;
}
}
    
boolean checkAppSwitchAllowedLocked(int callingPid, int callingUid,
String name) {
if (mAppSwitchesAllowedTime < SystemClock.uptimeMillis()) {
return true;
}
            
final int perm = checkComponentPermission(
android.Manifest.permission.STOP_APP_SWITCHES, callingPid,
callingUid, -1);
if (perm == PackageManager.PERMISSION_GRANTED) {
return true;
}
        
Slog.w(TAG, name + " request from " + callingUid + " stopped");
return false;
}
    
public void setDebugApp(String packageName, boolean waitForDebugger,
boolean persistent) {
enforceCallingPermission(android.Manifest.permission.SET_DEBUG_APP,
//Synthetic comment -- @@ -8431,7 +8432,7 @@
Settings.System.putInt(
mContext.getContentResolver(),
Settings.System.ALWAYS_FINISH_ACTIVITIES, enabled ? 1 : 0);
        
synchronized (this) {
mAlwaysFinishActivities = enabled;
}
//Synthetic comment -- @@ -8452,7 +8453,7 @@
return mController != null;
}
}
    
public void registerActivityWatcher(IActivityWatcher watcher) {
synchronized (this) {
mWatchers.register(watcher);
//Synthetic comment -- @@ -8516,7 +8517,7 @@
String reason = (pReason == null) ? "Unknown" : pReason;
// XXX Note: don't acquire main activity lock here, because the window
// manager calls in with its locks held.
        
boolean killed = false;
synchronized (mPidsSelfLocked) {
int[] types = new int[pids.length];
//Synthetic comment -- @@ -8531,7 +8532,7 @@
}
}
}
            
// If the worse oom_adj is somewhere in the hidden proc LRU range,
// then constrain it so we will kill all hidden procs.
if (worstType < EMPTY_APP_ADJ && worstType > HIDDEN_APP_MIN_ADJ) {
//Synthetic comment -- @@ -8556,7 +8557,7 @@
}
return killed;
}
    
public void reportPss(IApplicationThread caller, int pss) {
Watchdog.PssRequestor req;
String name;
//Synthetic comment -- @@ -8578,7 +8579,7 @@
removeRequestedPss(callerApp);
}
}
    
public void requestPss(Runnable completeCallback) {
ArrayList<ProcessRecord> procs;
synchronized (this) {
//Synthetic comment -- @@ -8592,8 +8593,8 @@
}
procs = new ArrayList<ProcessRecord>(mRequestPssList);
}
        
        int oldPri = Process.getThreadPriority(Process.myTid()); 
Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
for (int i=procs.size()-1; i>=0; i--) {
ProcessRecord proc = procs.get(i);
//Synthetic comment -- @@ -8602,7 +8603,7 @@
}
Process.setThreadPriority(oldPri);
}
    
void removeRequestedPss(ProcessRecord proc) {
Runnable callback = null;
synchronized (this) {
//Synthetic comment -- @@ -8613,12 +8614,12 @@
}
}
}
        
if (callback != null) {
callback.run();
}
}
    
public void collectPss(Watchdog.PssStats stats) {
stats.mEmptyPss = 0;
stats.mEmptyCount = 0;
//Synthetic comment -- @@ -8644,7 +8645,7 @@
stats.mProcDeaths[i] = 0;
i++;
}
            
for (i=mLruProcesses.size()-1; i>=0; i--) {
ProcessRecord proc = mLruProcesses.get(i);
if (proc.persistent) {
//Synthetic comment -- @@ -8673,7 +8674,7 @@
}
}
}
    
public final void startRunning(String pkg, String cls, String action,
String data) {
synchronized(this) {
//Synthetic comment -- @@ -8721,7 +8722,7 @@
// no need to synchronize(this) just to read & return the value
return mSystemReady;
}
    
public void systemReady(final Runnable goingCallback) {
// In the simulator, startRunning will never have been called, which
// normally sets a few crucial variables. Do it here instead.
//Synthetic comment -- @@ -8735,7 +8736,7 @@
if (goingCallback != null) goingCallback.run();
return;
}
            
// Check to see if there are any update receivers to run.
if (!mDidUpdate) {
if (mWaitingUpdate) {
//Synthetic comment -- @@ -8786,7 +8787,7 @@
}
mDidUpdate = true;
}
            
mSystemReady = true;
if (!mStartRunning) {
return;
//Synthetic comment -- @@ -8805,7 +8806,7 @@
}
}
}
        
if (procsToKill != null) {
synchronized(this) {
for (int i=procsToKill.size()-1; i>=0; i--) {
//Synthetic comment -- @@ -8815,14 +8816,14 @@
}
}
}
        
Slog.i(TAG, "System now ready");
EventLog.writeEvent(EventLogTags.BOOT_PROGRESS_AMS_READY,
SystemClock.uptimeMillis());

synchronized(this) {
// Make sure we have no pre-ready processes sitting around.
            
if (mFactoryTest == SystemServer.FACTORY_TEST_LOW_LEVEL) {
ResolveInfo ri = mContext.getPackageManager()
.resolveActivity(new Intent(Intent.ACTION_FACTORY_TEST),
//Synthetic comment -- @@ -8859,7 +8860,7 @@
retrieveSettings();

if (goingCallback != null) goingCallback.run();
        
synchronized (this) {
if (mFactoryTest != SystemServer.FACTORY_TEST_LOW_LEVEL) {
try {
//Synthetic comment -- @@ -8884,7 +8885,7 @@

// Start up initial activity.
mBooting = true;
            
try {
if (ActivityThread.getPackageManager().hasSystemUidErrors()) {
Message msg = Message.obtain();
//Synthetic comment -- @@ -8917,12 +8918,12 @@
startAppProblemLocked(app);
app.stopFreezingAllLocked();
}
    
/**
* Generate a process error record, suitable for attachment to a ProcessRecord.
     * 
* @param app The ProcessRecord in which the error occurred.
     * @param condition Crashing, Application Not Responding, etc.  Values are defined in 
*                      ActivityManager.AppErrorStateInfo
* @param activity The activity associated with the crash, if known.
* @param shortMsg Short message describing the crash.
//Synthetic comment -- @@ -8931,7 +8932,7 @@
*
* @return Returns a fully-formed AppErrorStateInfo record.
*/
    private ActivityManager.ProcessErrorStateInfo generateProcessError(ProcessRecord app, 
int condition, String activity, String shortMsg, String longMsg, String stackTrace) {
ActivityManager.ProcessErrorStateInfo report = new ActivityManager.ProcessErrorStateInfo();

//Synthetic comment -- @@ -9044,7 +9045,7 @@
sr.crashCount++;
}
}
        
mProcessCrashTimes.put(app.info.processName, app.info.uid, now);
return true;
}
//Synthetic comment -- @@ -9441,14 +9442,14 @@
} else if (app.notResponding) {
report = app.notRespondingReport;
}
                    
if (report != null) {
if (errList == null) {
errList = new ArrayList<ActivityManager.ProcessErrorStateInfo>(1);
}
errList.add(report);
} else {
                        Slog.w(TAG, "Missing app error report, app = " + app.processName + 
" crashing = " + app.crashing +
" notResponding = " + app.notResponding);
}
//Synthetic comment -- @@ -9458,7 +9459,7 @@

return errList;
}
    
public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() {
// Lazy instantiation of list
List<ActivityManager.RunningAppProcessInfo> runList = null;
//Synthetic comment -- @@ -9468,7 +9469,7 @@
ProcessRecord app = mLruProcesses.get(i);
if ((app.thread != null) && (!app.crashing && !app.notResponding)) {
// Generate process state info for running application
                    ActivityManager.RunningAppProcessInfo currApp = 
new ActivityManager.RunningAppProcessInfo(app.processName,
app.pid, app.getPackageList());
currApp.uid = app.info.uid;
//Synthetic comment -- @@ -9547,9 +9548,9 @@
+ android.Manifest.permission.DUMP);
return;
}
        
boolean dumpAll = false;
        
int opti = 0;
while (opti < args.length) {
String opt = args[opti];
//Synthetic comment -- @@ -9575,7 +9576,7 @@
pw.println("Unknown argument: " + opt + "; use -h for help");
}
}
        
// Is the caller requesting to dump a particular piece of data?
if (opti < args.length) {
String cmd = args[opti];
//Synthetic comment -- @@ -9615,7 +9616,7 @@
return;
}
}
        
// No piece of data specified, dump everything.
synchronized (this) {
boolean needSep;
//Synthetic comment -- @@ -9665,7 +9666,7 @@
dumpProcessesLocked(fd, pw, args, opti, dumpAll);
}
}
    
boolean dumpActivitiesLocked(FileDescriptor fd, PrintWriter pw, String[] args,
int opti, boolean dumpAll, boolean needHeader) {
if (needHeader) {
//Synthetic comment -- @@ -9709,13 +9710,13 @@
mRecentTasks.get(i).dump(pw, "    ");
}
}
        
pw.println(" ");
pw.println("  mCurTask: " + mCurTask);
        
return true;
}
        
boolean dumpProcessesLocked(FileDescriptor fd, PrintWriter pw, String[] args,
int opti, boolean dumpAll) {
boolean needSep = false;
//Synthetic comment -- @@ -9740,7 +9741,7 @@
}
}
}
        
if (mLruProcesses.size() > 0) {
if (needSep) pw.println(" ");
needSep = true;
//Synthetic comment -- @@ -9761,7 +9762,7 @@
}
}
}
        
if (mForegroundProcesses.size() > 0) {
if (needSep) pw.println(" ");
needSep = true;
//Synthetic comment -- @@ -9771,7 +9772,7 @@
pw.print(": "); pw.println(mForegroundProcesses.valueAt(i));
}
}
        
if (mPersistentStartingProcesses.size() > 0) {
if (needSep) pw.println(" ");
needSep = true;
//Synthetic comment -- @@ -9795,7 +9796,7 @@
dumpProcessList(pw, this, mRemovedProcesses, "    ",
"Removed Norm", "Removed PERS", false);
}
        
if (mProcessesOnHold.size() > 0) {
if (needSep) pw.println(" ");
needSep = true;
//Synthetic comment -- @@ -9818,10 +9819,10 @@
pw.print(" ms ago, last lowMem=");
pw.print(now-proc.lastLowMemory);
pw.println(" ms ago");
                
}
}
        
if (mProcessCrashTimes.getMap().size() > 0) {
if (needSep) pw.println(" ");
needSep = true;
//Synthetic comment -- @@ -9884,7 +9885,7 @@
pw.println("  mLaunchingActivity=" + mLaunchingActivity);
pw.println("  mAdjSeq=" + mAdjSeq + " mLruSeq=" + mLruSeq);
}
        
return true;
}

//Synthetic comment -- @@ -9948,7 +9949,7 @@
boolean dumpBroadcastsLocked(FileDescriptor fd, PrintWriter pw, String[] args,
int opti, boolean dumpAll) {
boolean needSep = false;
        
if (dumpAll) {
if (mRegisteredReceivers.size() > 0) {
pw.println(" ");
//Synthetic comment -- @@ -9960,13 +9961,13 @@
r.dump(pw, "    ");
}
}
    
pw.println(" ");
pw.println("Receiver Resolver Table:");
mReceiverResolver.dump(pw, null, "  ", null);
needSep = true;
}
        
if (mParallelBroadcasts.size() > 0 || mOrderedBroadcasts.size() > 0
|| mPendingBroadcast != null) {
if (mParallelBroadcasts.size() > 0) {
//Synthetic comment -- @@ -10008,7 +10009,7 @@
}
needSep = true;
}
        
if (mStickyBroadcasts != null) {
pw.println(" ");
pw.println("  Sticky broadcasts:");
//Synthetic comment -- @@ -10033,7 +10034,7 @@
}
needSep = true;
}
        
if (dumpAll) {
pw.println(" ");
pw.println("  mBroadcastsScheduled=" + mBroadcastsScheduled);
//Synthetic comment -- @@ -10041,7 +10042,7 @@
mHandler.dump(new PrintWriterPrinter(pw), "    ");
needSep = true;
}
        
return needSep;
}

//Synthetic comment -- @@ -10109,7 +10110,7 @@
needSep = true;
}
}
        
return needSep;
}

//Synthetic comment -- @@ -10130,7 +10131,7 @@
}
needSep = true;
}
    
if (mProvidersByName.size() > 0) {
pw.println(" ");
pw.println("  Authority to provider mappings:");
//Synthetic comment -- @@ -10171,14 +10172,14 @@
}
needSep = true;
}
        
return needSep;
}

boolean dumpPendingIntentsLocked(FileDescriptor fd, PrintWriter pw, String[] args,
int opti, boolean dumpAll) {
boolean needSep = false;
        
if (dumpAll) {
if (this.mIntentSenderRecords.size() > 0) {
Iterator<WeakReference<PendingIntentRecord>> it
//Synthetic comment -- @@ -10196,7 +10197,7 @@
}
}
}
        
return needSep;
}

//Synthetic comment -- @@ -10231,7 +10232,7 @@
}
return prefix + "+" + Integer.toString(val-base);
}
    
private static final int dumpProcessList(PrintWriter pw,
ActivityManagerService service, List list,
String prefix, String normalLabel, String persistentLabel,
//Synthetic comment -- @@ -10302,7 +10303,7 @@
final boolean isCheckinRequest = scanArgs(args, "--checkin");
long uptime = SystemClock.uptimeMillis();
long realtime = SystemClock.elapsedRealtime();
        
if (isCheckinRequest) {
// short checkin version
pw.println(uptime + "," + realtime);
//Synthetic comment -- @@ -10416,7 +10417,7 @@
sr.app = null;
sr.executeNesting = 0;
mStoppingServices.remove(sr);
                
boolean hasClients = sr.bindings.size() > 0;
if (hasClients) {
Iterator<IntentBindRecord> bindings
//Synthetic comment -- @@ -10440,7 +10441,7 @@
bringDownServiceLocked(sr, true);
} else {
boolean canceled = scheduleServiceRestartLocked(sr, true);
                    
// Should the service remain running?  Note that in the
// extreme case of so many attempts to deliver a command
// that it failed, that we also will stop it here.
//Synthetic comment -- @@ -10470,7 +10471,7 @@
mStoppingServices.remove(i);
}
}
        
app.executingServices.clear();
}

//Synthetic comment -- @@ -10480,13 +10481,13 @@
cpr.launchingApp = null;
cpr.notifyAll();
}
        
mProvidersByClass.remove(cpr.info.name);
String names[] = cpr.info.authority.split(";");
for (int j = 0; j < names.length; j++) {
mProvidersByName.remove(names[j]);
}
        
Iterator<ProcessRecord> cit = cpr.clients.iterator();
while (cit.hasNext()) {
ProcessRecord capp = cit.next();
//Synthetic comment -- @@ -10501,13 +10502,13 @@
Process.killProcess(capp.pid);
}
}
        
mLaunchingProviders.remove(cpr);
}
    
/**
* Main code for cleaning up a process when it has gone away.  This is
     * called both as a result of the process dying, or directly when stopping 
* a process when running in single process mode.
*/
private final void cleanUpApplicationRecordLocked(ProcessRecord app,
//Synthetic comment -- @@ -10517,7 +10518,7 @@
}

mProcessesToGc.remove(app);
        
// Dismiss any open dialogs.
if (app.crashDialog != null) {
app.crashDialog.dismiss();
//Synthetic comment -- @@ -10534,7 +10535,7 @@

app.crashing = false;
app.notResponding = false;
        
app.resetPackageList();
app.thread = null;
app.forcingToForeground = null;
//Synthetic comment -- @@ -10545,7 +10546,7 @@
boolean restart = false;

int NL = mLaunchingProviders.size();
        
// Remove published content providers.
if (!app.pubProviders.isEmpty()) {
Iterator it = app.pubProviders.values().iterator();
//Synthetic comment -- @@ -10575,12 +10576,12 @@
}
app.pubProviders.clear();
}
        
// Take care of any launching providers waiting for this process.
if (checkAppInLaunchingProvidersLocked(app, false)) {
restart = true;
}
        
// Unregister from connected content providers.
if (!app.conProviders.isEmpty()) {
Iterator it = app.conProviders.keySet().iterator();
//Synthetic comment -- @@ -10608,7 +10609,7 @@
}
}
}
        
skipCurrentReceiverLocked(app);

// Unregister any receivers.
//Synthetic comment -- @@ -10619,7 +10620,7 @@
}
app.receivers.clear();
}
        
// If the app is undergoing backup, tell the backup manager about it
if (mBackupTarget != null && app.pid == mBackupTarget.app.pid) {
if (DEBUG_BACKUP) Slog.d(TAG, "App " + mBackupTarget.appInfo + " died during backup");
//Synthetic comment -- @@ -10659,7 +10660,7 @@
if (app == mHomeProcess) {
mHomeProcess = null;
}
        
if (restart) {
// We have components that still need to be running in the
// process, so re-launch it.
//Synthetic comment -- @@ -10696,7 +10697,7 @@
}
return restart;
}
    
// =========================================================
// SERVICES
// =========================================================
//Synthetic comment -- @@ -10737,13 +10738,13 @@
}
return info;
}
    
public List<ActivityManager.RunningServiceInfo> getServices(int maxNum,
int flags) {
synchronized (this) {
ArrayList<ActivityManager.RunningServiceInfo> res
= new ArrayList<ActivityManager.RunningServiceInfo>();
            
if (mServices.size() > 0) {
Iterator<ServiceRecord> it = mServices.values().iterator();
while (it.hasNext() && res.size() < maxNum) {
//Synthetic comment -- @@ -10758,7 +10759,7 @@
info.restarting = r.nextRestartTime;
res.add(info);
}
            
return res;
}
}
//Synthetic comment -- @@ -10776,7 +10777,7 @@
}
return null;
}
    
private final ServiceRecord findServiceLocked(ComponentName name,
IBinder token) {
ServiceRecord r = mServices.get(name);
//Synthetic comment -- @@ -10891,7 +10892,7 @@
res.setService(r);
mServices.put(name, r);
mServicesByIntent.put(filter, r);
                    
// Make sure this component isn't in the pending list.
int N = mPendingServices.size();
for (int i=0; i<N; i++) {
//Synthetic comment -- @@ -11062,7 +11063,7 @@
}

requestServiceBindingsLocked(r);
        
// If the service is in the started state, and there are no
// pending arguments, then fake up one so its onStartCommand() will
// be called.
//Synthetic comment -- @@ -11073,18 +11074,18 @@
}
r.pendingStarts.add(new ServiceRecord.StartItem(r.lastStartId, null));
}
        
sendServiceArgsLocked(r, true);
}

private final boolean scheduleServiceRestartLocked(ServiceRecord r,
boolean allowCancel) {
boolean canceled = false;
        
final long now = SystemClock.uptimeMillis();
long minDuration = SERVICE_RESTART_DURATION;
long resetTime = SERVICE_RESET_RUN_DURATION;
        
// Any delivered but not yet finished starts should be put back
// on the pending list.
final int N = r.deliveredStarts.size();
//Synthetic comment -- @@ -11108,7 +11109,7 @@
}
r.deliveredStarts.clear();
}
        
r.totalRestartCount++;
if (r.restartDelay == 0) {
r.restartCount++;
//Synthetic comment -- @@ -11129,9 +11130,9 @@
}
}
}
        
r.nextRestartTime = now + r.restartDelay;
        
// Make sure that we don't end up restarting a bunch of services
// all at the same time.
boolean repeat;
//Synthetic comment -- @@ -11150,13 +11151,13 @@
}
}
} while (repeat);
        
if (!mRestartingServices.contains(r)) {
mRestartingServices.add(r);
}
        
r.cancelNotification();
        
mHandler.removeCallbacks(r.restarter);
mHandler.postAtTime(r.restarter, r.nextRestartTime);
r.nextRestartTime = SystemClock.uptimeMillis() + r.restartDelay;
//Synthetic comment -- @@ -11206,7 +11207,7 @@
// We are now bringing the service up, so no longer in the
// restarting state.
mRestartingServices.remove(r);
        
final String appName = r.processName;
ProcessRecord app = getProcessRecordLocked(appName, r.appInfo.uid);
if (app != null && app.thread != null) {
//Synthetic comment -- @@ -11232,11 +11233,11 @@
bringDownServiceLocked(r, true);
return false;
}
        
if (!mPendingServices.contains(r)) {
mPendingServices.add(r);
}
        
return true;
}

//Synthetic comment -- @@ -11328,11 +11329,11 @@
r.isForeground = false;
r.foregroundId = 0;
r.foregroundNoti = null;
        
// Clear start entries.
r.deliveredStarts.clear();
r.pendingStarts.clear();
        
if (r.app != null) {
synchronized (r.stats.getBatteryStats()) {
r.stats.stopLaunchedLocked();
//Synthetic comment -- @@ -11491,7 +11492,7 @@

synchronized(this) {
ServiceLookupResult r = findServiceLocked(service, resolvedType);
            
if (r != null) {
// r.record is null if findServiceLocked() failed the caller permission check
if (r.record == null) {
//Synthetic comment -- @@ -11510,7 +11511,7 @@

return ret;
}
    
public boolean stopServiceToken(ComponentName className, IBinder token,
int startId) {
synchronized(this) {
//Synthetic comment -- @@ -11530,18 +11531,18 @@
}
}
}
                    
if (r.lastStartId != startId) {
return false;
}
                    
if (r.deliveredStarts.size() > 0) {
Slog.w(TAG, "stopServiceToken startId " + startId
+ " is last, but have " + r.deliveredStarts.size()
+ " remaining args");
}
}
                
synchronized (r.stats.getBatteryStats()) {
r.stats.stopRunningLocked();
r.startRequested = false;
//Synthetic comment -- @@ -11614,7 +11615,7 @@
}
}
}
    
public int bindService(IApplicationThread caller, IBinder token,
Intent service, String resolvedType,
IServiceConnection connection, int flags) {
//Synthetic comment -- @@ -11647,7 +11648,7 @@

int clientLabel = 0;
PendingIntent clientIntent = null;
            
if (callerApp.info.uid == Process.SYSTEM_UID) {
// Hacky kind of thing -- allow system stuff to tell us
// what they are, so we can report this elsewhere for
//Synthetic comment -- @@ -11667,7 +11668,7 @@
}
}
}
            
ServiceLookupResult res =
retrieveServiceLocked(service, resolvedType,
Binder.getCallingPid(), Binder.getCallingUid());
//Synthetic comment -- @@ -11975,7 +11976,7 @@
r.callStart = false;
}
}
                
final long origId = Binder.clearCallingIdentity();
serviceDoneExecutingLocked(r, inStopping);
Binder.restoreCallingIdentity(origId);
//Synthetic comment -- @@ -12002,7 +12003,7 @@

void serviceTimeout(ProcessRecord proc) {
String anrMessage = null;
        
synchronized(this) {
if (proc.executingServices.size() == 0 || proc.thread == null) {
return;
//Synthetic comment -- @@ -12030,16 +12031,16 @@
mHandler.sendMessageAtTime(msg, nextTime+SERVICE_TIMEOUT);
}
}
        
if (anrMessage != null) {
appNotResponding(proc, null, null, anrMessage);
}
}
    
// =========================================================
// BACKUP AND RESTORE
// =========================================================
    
// Cause the target app to be launched if necessary and its backup agent
// instantiated.  The backup agent will invoke backupAgentCreated() on the
// activity manager to announce its creation.
//Synthetic comment -- @@ -12089,11 +12090,11 @@
// mBackupAppName describe the app, so that when it binds back to the AM we
// know that it's scheduled for a backup-agent operation.
}
        
return true;
}

    // A backup agent has just come up                    
public void backupAgentCreated(String agentPackageName, IBinder agent) {
if (DEBUG_BACKUP) Slog.v(TAG, "backupAgentCreated: " + agentPackageName
+ " = " + agent);
//Synthetic comment -- @@ -12308,7 +12309,7 @@
if (!doNext) {
return;
}
        
final long origId = Binder.clearCallingIdentity();
processNextBroadcast(false);
trimApplications();
//Synthetic comment -- @@ -12322,7 +12323,7 @@
mReceiverResolver.removeFilter(rl.get(i));
}
}
    
private final void sendPackageBroadcastLocked(int cmd, String[] packages) {
for (int i = mLruProcesses.size() - 1 ; i >= 0 ; i--) {
ProcessRecord r = mLruProcesses.get(i);
//Synthetic comment -- @@ -12334,7 +12335,7 @@
}
}
}
    
private final int broadcastIntentLocked(ProcessRecord callerApp,
String callerPackage, Intent intent, String resolvedType,
IIntentReceiver resultTo, int resultCode, String resultData,
//Synthetic comment -- @@ -12348,7 +12349,7 @@
if ((resultTo != null) && !ordered) {
Slog.w(TAG, "Broadcast " + intent + " not ordered but result callback requested!");
}
        
// Handle special intents: if this broadcast is from the package
// manager about a package being removed, we need to remove all of
// its activities from the history stack.
//Synthetic comment -- @@ -12441,7 +12442,7 @@
return BROADCAST_SUCCESS;
}
}
        
// Add to the sticky list if requested.
if (sticky) {
if (checkPermission(android.Manifest.permission.BROADCAST_STICKY,
//Synthetic comment -- @@ -12511,10 +12512,10 @@

final boolean replacePending =
(intent.getFlags()&Intent.FLAG_RECEIVER_REPLACE_PENDING) != 0;
        
if (DEBUG_BROADCAST) Slog.v(TAG, "Enqueing broadcast: " + intent.getAction()
+ " replacePending=" + replacePending);
        
int NR = registeredReceivers != null ? registeredReceivers.size() : 0;
if (!ordered && NR > 0) {
// If we are not serializing this broadcast, then send the
//Synthetic comment -- @@ -12664,7 +12665,7 @@

synchronized(this) {
int flags = intent.getFlags();
            
if (!mSystemReady) {
// if the caller really truly claims to know what they're doing, go
// ahead and allow the broadcast without launching any receivers
//Synthetic comment -- @@ -12677,12 +12678,12 @@
throw new IllegalStateException("Cannot broadcast before boot completed");
}
}
            
if ((flags&Intent.FLAG_RECEIVER_BOOT_UPGRADE) != 0) {
throw new IllegalArgumentException(
"Can't use FLAG_RECEIVER_BOOT_UPGRADE here");
}
            
final ProcessRecord callerApp = getRecordForAppLocked(caller);
final int callingPid = Binder.getCallingPid();
final int callingUid = Binder.getCallingUid();
//Synthetic comment -- @@ -12893,7 +12894,7 @@
} else {
app = r.curApp;
}
            
if (app != null) {
anrMessage = "Broadcast of " + r.intent.toString();
}
//Synthetic comment -- @@ -12907,7 +12908,7 @@
r.resultExtras, r.resultAbort, true);
scheduleBroadcastsLocked();
}
        
if (anrMessage != null) {
appNotResponding(app, null, null, anrMessage);
}
//Synthetic comment -- @@ -12957,7 +12958,7 @@
receiver.performReceive(intent, resultCode, data, extras, ordered, sticky);
}
}
    
private final void deliverToRegisteredReceiver(BroadcastRecord r,
BroadcastFilter filter, boolean ordered) {
boolean skip = false;
//Synthetic comment -- @@ -13046,7 +13047,7 @@
r.finishTime = SystemClock.uptimeMillis();
mBroadcastHistory[0] = r;
}
    
private final void processNextBroadcast(boolean fromMsg) {
synchronized(this) {
BroadcastRecord r;
//Synthetic comment -- @@ -13056,7 +13057,7 @@
+ mOrderedBroadcasts.size() + " ordered broadcasts");

updateCpuStats();
            
if (fromMsg) {
mBroadcastsScheduled = false;
}
//Synthetic comment -- @@ -13106,7 +13107,7 @@
}

boolean looped = false;
            
do {
if (mOrderedBroadcasts.size() == 0) {
// No more broadcasts pending, so all done!
//Synthetic comment -- @@ -13174,13 +13175,13 @@
Slog.w(TAG, "Failure sending broadcast result of " + r.intent, e);
}
}
                    
if (DEBUG_BROADCAST) Slog.v(TAG, "Cancelling BROADCAST_TIMEOUT_MSG");
mHandler.removeMessages(BROADCAST_TIMEOUT_MSG);

if (DEBUG_BROADCAST_LIGHT) Slog.v(TAG, "Finished with ordered broadcast "
+ r);
                    
// ... and on to the next...
addBroadcastToHistoryLocked(r);
mOrderedBroadcasts.remove(0);
//Synthetic comment -- @@ -13390,17 +13391,17 @@

return true;
}
    
/**
     * Report errors that occur while attempting to start Instrumentation.  Always writes the 
* error to the logs, but if somebody is watching, send the report there too.  This enables
* the "am" command to report errors with more information.
     * 
* @param watcher The IInstrumentationWatcher.  Null if there isn't one.
* @param cn The component name of the instrumentation.
* @param report The error report.
*/
    private void reportStartInstrumentationFailure(IInstrumentationWatcher watcher, 
ComponentName cn, String report) {
Slog.w(TAG, report);
try {
//Synthetic comment -- @@ -13457,7 +13458,7 @@
// =========================================================
// CONFIGURATION
// =========================================================
    
public ConfigurationInfo getDeviceConfigurationInfo() {
ConfigurationInfo config = new ConfigurationInfo();
synchronized (this) {
//Synthetic comment -- @@ -13494,7 +13495,7 @@
// sentinel: fetch the current configuration from the window manager
values = mWindowManager.computeNewConfiguration();
}
            
final long origId = Binder.clearCallingIdentity();
updateConfigurationLocked(values, null);
Binder.restoreCallingIdentity(origId);
//Synthetic comment -- @@ -13511,9 +13512,9 @@
public boolean updateConfigurationLocked(Configuration values,
HistoryRecord starting) {
int changes = 0;
        
boolean kept = true;
        
if (values != null) {
Configuration newConfig = new Configuration(mConfiguration);
changes = newConfig.updateFrom(values);
//Synthetic comment -- @@ -13521,11 +13522,11 @@
if (DEBUG_SWITCH || DEBUG_CONFIGURATION) {
Slog.i(TAG, "Updating configuration to: " + values);
}
                
EventLog.writeEvent(EventLogTags.CONFIGURATION_CHANGED, changes);

if (values.locale != null) {
                    saveLocaleLocked(values.locale, 
!values.locale.equals(mConfiguration.locale),
values.userSetLocale);
}
//Synthetic comment -- @@ -13537,7 +13538,7 @@
newConfig.seq = mConfigurationSeq;
mConfiguration = newConfig;
Slog.i(TAG, "Config changed: " + newConfig);
                
AttributeCache ac = AttributeCache.instance();
if (ac != null) {
ac.updateConfiguration(mConfiguration);
//Synthetic comment -- @@ -13548,7 +13549,7 @@
msg.obj = new Configuration(mConfiguration);
mHandler.sendMessage(msg);
}
        
for (int i=mLruProcesses.size()-1; i>=0; i--) {
ProcessRecord app = mLruProcesses.get(i);
try {
//Synthetic comment -- @@ -13573,14 +13574,14 @@
}
}
}
        
if (changes != 0 && starting == null) {
// If the configuration changed, and the caller is not already
// in the process of starting an activity, then find the top
// activity to check if its configuration needs to change.
starting = topRunningActivityLocked(null);
}
        
if (starting != null) {
kept = ensureActivityConfigurationLocked(starting, changes);
if (kept) {
//Synthetic comment -- @@ -13592,11 +13593,11 @@
ensureActivitiesVisibleLocked(starting, changes);
}
}
        
if (values != null && mWindowManager != null) {
mWindowManager.setNewConfiguration(mConfiguration);
}
        
return kept;
}

//Synthetic comment -- @@ -13614,9 +13615,9 @@
EventLog.writeEvent(andResume ? EventLogTags.AM_RELAUNCH_RESUME_ACTIVITY
: EventLogTags.AM_RELAUNCH_ACTIVITY, System.identityHashCode(r),
r.task.taskId, r.shortComponentName);
        
r.startFreezingScreenLocked(r.app, 0);
        
try {
if (DEBUG_SWITCH) Slog.i(TAG, "Switch is restarting resumed " + r);
r.app.thread.scheduleRelaunchActivity(r, results, newIntents,
//Synthetic comment -- @@ -13651,10 +13652,10 @@
"Skipping config check (will change): " + r);
return true;
}
        
if (DEBUG_SWITCH || DEBUG_CONFIGURATION) Slog.v(TAG,
"Ensuring correct configuration: " + r);
        
// Short circuit: if the two configurations are the exact same
// object (the common case), then there is nothing to do.
Configuration newConfig = mConfiguration;
//Synthetic comment -- @@ -13663,7 +13664,7 @@
"Configuration unchanged in " + r);
return true;
}
        
// We don't worry about activities that are finishing.
if (r.finishing) {
if (DEBUG_SWITCH || DEBUG_CONFIGURATION) Slog.v(TAG,
//Synthetic comment -- @@ -13671,12 +13672,12 @@
r.stopFreezingScreenLocked(false);
return true;
}
        
// Okay we now are going to make this activity have the new config.
// But then we need to figure out how it needs to deal with that.
Configuration oldConfig = r.configuration;
r.configuration = newConfig;
        
// If the activity isn't currently running, just leave the new
// configuration and it will pick that up next time it starts.
if (r.app == null || r.app.thread == null) {
//Synthetic comment -- @@ -13685,7 +13686,7 @@
r.stopFreezingScreenLocked(false);
return true;
}
        
// If the activity isn't persistent, there is a chance we will
// need to restart it.
if (!r.persistent) {
//Synthetic comment -- @@ -13729,13 +13730,13 @@
relaunchActivityLocked(r, r.configChangeFlags, false);
r.configChangeFlags = 0;
}
                
// All done...  tell the caller we weren't able to keep this
// activity around.
return false;
}
}
        
// Default case: the activity can handle this new configuration, so
// hand it over.  Note that we don't need to give it the new
// configuration, since we always send configuration changes to all
//Synthetic comment -- @@ -13750,10 +13751,10 @@
}
}
r.stopFreezingScreenLocked(false);
        
return true;
}
    
/**
* Save the locale.  You must be inside a synchronized (this) block.
*/
//Synthetic comment -- @@ -13761,7 +13762,7 @@
if(isDiff) {
SystemProperties.set("user.language", l.getLanguage());
SystemProperties.set("user.region", l.getCountry());
        } 

if(isPersist) {
SystemProperties.set("persist.sys.language", l.getLanguage());
//Synthetic comment -- @@ -13802,7 +13803,7 @@
app.curSchedGroup = Process.THREAD_GROUP_DEFAULT;
return (app.curAdj=app.maxAdj);
}
        
app.adjTypeCode = ActivityManager.RunningAppProcessInfo.REASON_UNKNOWN;
app.adjSource = null;
app.adjTarget = null;
//Synthetic comment -- @@ -13887,7 +13888,7 @@
}

//Slog.i(TAG, "OOM " + app + ": initial adj=" + adj);
        
// By default, we use the computed adjustment.  It may be changed if
// there are applications dependent on our services or providers, but
// this gives us a baseline and makes sure we don't get into an
//Synthetic comment -- @@ -13992,7 +13993,7 @@
}
}
}
            
// Finally, f this process has active services running in it, we
// would like to avoid killing it unless it would prevent the current
// application from running.  By default we put the process in
//Synthetic comment -- @@ -14062,7 +14063,7 @@
}

app.curRawAdj = adj;
        
//Slog.i(TAG, "OOM ADJ " + app + ": pid=" + app.pid +
//      " adj=" + adj + " curAdj=" + app.curAdj + " maxAdj=" + app.maxAdj);
if (adj > app.maxAdj) {
//Synthetic comment -- @@ -14074,7 +14075,7 @@

app.curAdj = adj;
app.curSchedGroup = schedGroup;
        
return adj;
}

//Synthetic comment -- @@ -14096,7 +14097,7 @@
// whatever.
}
}
    
/**
* Returns true if things are idle enough to perform GCs.
*/
//Synthetic comment -- @@ -14106,7 +14107,7 @@
&& (mSleeping || (mResumedActivity != null &&
mResumedActivity.idle));
}
    
/**
* Perform GCs on all processes that are waiting for it, but only
* if things are idle.
//Synthetic comment -- @@ -14135,11 +14136,11 @@
}
}
}
            
scheduleAppGcsLocked();
}
}
    
/**
* If all looks good, perform GCs on all processes waiting for them.
*/
//Synthetic comment -- @@ -14157,12 +14158,12 @@
*/
final void scheduleAppGcsLocked() {
mHandler.removeMessages(GC_BACKGROUND_PROCESSES_MSG);
        
if (mProcessesToGc.size() > 0) {
// Schedule a GC for the time to the next process.
ProcessRecord proc = mProcessesToGc.get(0);
Message msg = mHandler.obtainMessage(GC_BACKGROUND_PROCESSES_MSG);
            
long when = mProcessesToGc.get(0).lastRequestedGc + GC_MIN_INTERVAL;
long now = SystemClock.uptimeMillis();
if (when < (now+GC_TIMEOUT)) {
//Synthetic comment -- @@ -14171,7 +14172,7 @@
mHandler.sendMessageAtTime(msg, when);
}
}
    
/**
* Add a process to the array of processes waiting to be GCed.  Keeps the
* list in sorted order by the last GC time.  The process can't already be
//Synthetic comment -- @@ -14191,7 +14192,7 @@
mProcessesToGc.add(0, proc);
}
}
    
/**
* Set up to ask a process to GC itself.  This will either do it
* immediately, or put it on the list of processes to gc the next
//Synthetic comment -- @@ -14329,7 +14330,7 @@
if (factor < 1) factor = 1;
int step = 0;
int numHidden = 0;
        
// First try updating the OOM adjustment for each of the
// application processes based on their current state.
int i = mLruProcesses.size();
//Synthetic comment -- @@ -14397,7 +14398,7 @@
}
cleanUpApplicationRecordLocked(app, false, -1);
mRemovedProcesses.remove(i);
                    
if (app.persistent) {
if (app.persistent) {
addAppLocked(app.info);
//Synthetic comment -- @@ -14600,11 +14601,11 @@
throw new SecurityException("Requires permission "
+ android.Manifest.permission.SET_ACTIVITY_WATCHER);
}
                
if (start && fd == null) {
throw new IllegalArgumentException("null fd");
}
                
ProcessRecord proc = null;
try {
int pid = Integer.parseInt(process);
//Synthetic comment -- @@ -14613,7 +14614,7 @@
}
} catch (NumberFormatException e) {
}
                
if (proc == null) {
HashMap<String, SparseArray<ProcessRecord>> all
= mProcessNames.getMap();
//Synthetic comment -- @@ -14622,18 +14623,18 @@
proc = procs.valueAt(0);
}
}
                
if (proc == null || proc.thread == null) {
throw new IllegalArgumentException("Unknown process: " + process);
}
                
boolean isSecure = "1".equals(SystemProperties.get(SYSTEM_SECURE, "0"));
if (isSecure) {
if ((proc.info.flags&ApplicationInfo.FLAG_DEBUGGABLE) == 0) {
throw new SecurityException("Process not debuggable: " + proc);
}
}
            
proc.thread.profilerControl(start, path, fd);
fd = null;
return true;
//Synthetic comment -- @@ -14649,7 +14650,7 @@
}
}
}
    
/** In this method we try to acquire our lock to make sure that we have not deadlocked */
public void monitor() {
synchronized (this) { }








//Synthetic comment -- diff --git a/services/java/com/android/server/am/AppWaitingForDebuggerDialog.java b/services/java/com/android/server/am/AppWaitingForDebuggerDialog.java
//Synthetic comment -- index 8e9818d..9fb48b3 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.server.am;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

//Synthetic comment -- @@ -49,7 +50,7 @@
text.append(" is waiting for the debugger to attach.");

setMessage(text.toString());
        setButton("Force Close", mHandler.obtainMessage(1, app));
setTitle("Waiting For Debugger");
getWindow().setTitle("Waiting For Debugger: " + app.info.processName);
}








//Synthetic comment -- diff --git a/services/java/com/android/server/am/FactoryErrorDialog.java b/services/java/com/android/server/am/FactoryErrorDialog.java
//Synthetic comment -- index 2e25474..b19bb5ca 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.server.am;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

//Synthetic comment -- @@ -26,7 +27,8 @@
setCancelable(false);
setTitle(context.getText(com.android.internal.R.string.factorytest_failed));
setMessage(msg);
        setButton(context.getText(com.android.internal.R.string.factorytest_reboot),
mHandler.obtainMessage(0));
getWindow().setTitle("Factory Error");
}







