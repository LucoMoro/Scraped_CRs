/*Make ActivityManagerService.systemReady thread safe

During boot of the framework, two threads execute code in
ActivityManagerService and ActivityStack, respectively. At one point
in time, the latter thread checks the internal state of the first
thread, but if for some reason that thread has stalled for an
unexpected amount of time, the state has not been updated (yet).

An example of the above is when one of the services started from
SystemServer (eg AppWidgetService) takes unexpectedly long to start.
This causes the framework to boot without broadcasting
BOOT_COMPLETED, which in turn has ramifications for the entire
system.

Change-Id:I14b5f6fcb320be01596a2e04db3f724a43f4fbe1*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index cffb391..ad8c071 100644

//Synthetic comment -- @@ -85,6 +85,7 @@
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.DropBoxManager;
import android.os.Environment;
//Synthetic comment -- @@ -674,6 +675,7 @@
boolean mProcessesReady = false;
boolean mSystemReady = false;
boolean mBooting = false;
boolean mWaitingUpdate = false;
boolean mDidUpdate = false;
boolean mOnBattery = false;
//Synthetic comment -- @@ -7083,6 +7085,7 @@

// Start up initial activity.
mBooting = true;

try {
if (AppGlobals.getPackageManager().hasSystemUidErrors()) {








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityStack.java b/services/java/com/android/server/am/ActivityStack.java
//Synthetic comment -- index 351dbb8..71f42c5 100644

//Synthetic comment -- @@ -3310,6 +3310,10 @@
mService.mCancelledThumbnails.clear();
}

if (mMainStack) {
booting = mService.mBooting;
mService.mBooting = false;







