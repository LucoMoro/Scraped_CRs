/*Remove unnecessary reference in ActivityManagerService.

Remove referencing mHander in itself. Make message codes simpler.

Change-Id:I996f2805e99297bbc11d8c156560a01017e1ac3c*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index db64a9a..6559532 100644

//Synthetic comment -- @@ -1047,9 +1047,9 @@
case SERVICE_TIMEOUT_MSG: {
if (mDidDexOpt) {
mDidDexOpt = false;
                    Message nmsg = obtainMessage(SERVICE_TIMEOUT_MSG);
nmsg.obj = msg.obj;
                    sendMessageDelayed(nmsg, ActiveServices.SERVICE_TIMEOUT);
return;
}
mServices.serviceTimeout((ProcessRecord)msg.obj);
//Synthetic comment -- @@ -1119,7 +1119,7 @@
d.setTitle(title);
d.setMessage(text);
d.setButton(DialogInterface.BUTTON_POSITIVE, "I'm Feeling Lucky",
                            obtainMessage(IM_FEELING_LUCKY_MSG));
mUidAlert = d;
d.show();
}
//Synthetic comment -- @@ -1133,9 +1133,9 @@
case PROC_START_TIMEOUT_MSG: {
if (mDidDexOpt) {
mDidDexOpt = false;
                    Message nmsg = obtainMessage(PROC_START_TIMEOUT_MSG);
nmsg.obj = msg.obj;
                    sendMessageDelayed(nmsg, PROC_START_TIMEOUT);
return;
}
ProcessRecord app = (ProcessRecord)msg.obj;
//Synthetic comment -- @@ -4406,8 +4406,8 @@

if (mFactoryTest != SystemServer.FACTORY_TEST_LOW_LEVEL) {
// Start looking for apps that are abusing wake locks.
                mHandler.sendEmptyMessageDelayed(CHECK_EXCESSIVE_WAKE_LOCKS_MSG,
                        POWER_CHECK_DELAY);
// Tell anyone interested that we are done booting!
SystemProperties.set("sys.boot_completed", "1");
SystemProperties.set("dev.bootcomplete", "1");
//Synthetic comment -- @@ -7089,8 +7089,8 @@
// Initialize the wake times of all processes.
checkExcessivePowerUsageLocked(false);
mHandler.removeMessages(CHECK_EXCESSIVE_WAKE_LOCKS_MSG);
                mHandler.sendEmptyMessageDelayed(CHECK_EXCESSIVE_WAKE_LOCKS_MSG,
                        POWER_CHECK_DELAY);
}
}
}
//Synthetic comment -- @@ -7204,8 +7204,8 @@
+ APP_SWITCH_DELAY_TIME;
mDidAppSwitch = false;
mHandler.removeMessages(DO_PENDING_ACTIVITY_LAUNCHES_MSG);
            mHandler.sendEmptyMessageDelayed(DO_PENDING_ACTIVITY_LAUNCHES_MSG,
                    APP_SWITCH_DELAY_TIME);
}
}

//Synthetic comment -- @@ -7895,9 +7895,7 @@

try {
if (AppGlobals.getPackageManager().hasSystemUidErrors()) {
                    mHandler.sendEmptyMessage(SHOW_UID_ERROR_MSG);
}
} catch (RemoteException e) {
}
//Synthetic comment -- @@ -13201,7 +13199,7 @@
if (mPendingProcessChanges.size() == 0) {
if (DEBUG_PROCESS_OBSERVERS) Slog.i(TAG,
"*** Enqueueing dispatch processes changed!");
                    mHandler.sendEmptyMessage(DISPATCH_PROCESSES_CHANGED);
}
mPendingProcessChanges.add(item);
}
//Synthetic comment -- @@ -13308,14 +13306,13 @@
if (mProcessesToGc.size() > 0) {
// Schedule a GC for the time to the next process.
ProcessRecord proc = mProcessesToGc.get(0);

long when = proc.lastRequestedGc + GC_MIN_INTERVAL;
long now = SystemClock.uptimeMillis();
if (when < (now+GC_TIMEOUT)) {
when = now + GC_TIMEOUT;
}
            mHandler.sendEmptyMessageAtTime(GC_BACKGROUND_PROCESSES_MSG, when);
}
}









//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityStack.java b/services/java/com/android/server/am/ActivityStack.java
//Synthetic comment -- index 4546dc3..5265bb79 100644

//Synthetic comment -- @@ -339,9 +339,9 @@
case IDLE_TIMEOUT_MSG: {
if (mService.mDidDexOpt) {
mService.mDidDexOpt = false;
                        Message nmsg = obtainMessage(IDLE_TIMEOUT_MSG);
nmsg.obj = msg.obj;
                        sendMessageDelayed(nmsg, IDLE_TIMEOUT);
return;
}
// We don't at this point know if the activity is fullscreen,
//Synthetic comment -- @@ -373,8 +373,7 @@
case LAUNCH_TIMEOUT_MSG: {
if (mService.mDidDexOpt) {
mService.mDidDexOpt = false;
                        sendEmptyMessageDelayed(LAUNCH_TIMEOUT_MSG, LAUNCH_TIMEOUT);
return;
}
synchronized (mService) {
//Synthetic comment -- @@ -825,8 +824,7 @@
}
}
mHandler.removeMessages(SLEEP_TIMEOUT_MSG);
            mHandler.sendEmptyMessageDelayed(SLEEP_TIMEOUT_MSG, SLEEP_TIMEOUT);
checkReadyForSleepLocked();
}
}
//Synthetic comment -- @@ -981,8 +979,7 @@
mLaunchingActivity.acquire();
if (!mHandler.hasMessages(LAUNCH_TIMEOUT_MSG)) {
// To be safe, don't allow the wake lock to be held for too long.
                mHandler.sendEmptyMessageDelayed(LAUNCH_TIMEOUT_MSG, LAUNCH_TIMEOUT);
}
}








