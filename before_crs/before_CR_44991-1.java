/*Frameworks: Usat Phase 2 feature and Screen status support

Added changes to support Icon, Idle screen, Language Selection.

This also includes change to support Idle screen status information.

Change-Id:I3f66a4491291b043b803dce00ac992002301309d*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 5d74cf3..6074834c 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import com.android.internal.R;
import com.android.internal.os.BatteryStatsImpl;
import com.android.internal.os.ProcessStats;
import com.android.server.AttributeCache;
//Synthetic comment -- @@ -128,6 +129,7 @@
import android.view.WindowManager;
import android.view.WindowManagerPolicy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
//Synthetic comment -- @@ -442,6 +444,9 @@
*/
ProcessRecord mHomeProcess;

/**
* This is the process holding the activity the user last visited that
* is in a different process from the one they are currently in.
//Synthetic comment -- @@ -2625,6 +2630,19 @@
}
}

/**
* This is the internal entry point for handling Activity.finish().
* 
//Synthetic comment -- @@ -2636,6 +2654,11 @@
*/
public final boolean finishActivity(IBinder token, int resultCode, Intent resultData) {
// Refuse possible leaked file descriptors
if (resultData != null && resultData.hasFileDescriptors() == true) {
throw new IllegalArgumentException("File descriptors passed in Intent");
}
//Synthetic comment -- @@ -5932,6 +5955,10 @@
moveTaskBackwardsLocked(task);
Binder.restoreCallingIdentity(origId);
}
}

private final void moveTaskBackwardsLocked(int task) {
//Synthetic comment -- @@ -7586,6 +7613,9 @@
Slog.i(TAG, "System now ready");
EventLog.writeEvent(EventLogTags.BOOT_PROGRESS_AMS_READY,
SystemClock.uptimeMillis());

synchronized(this) {
// Make sure we have no pre-ready processes sitting around.
//Synthetic comment -- @@ -7665,6 +7695,33 @@
}
}

private boolean makeAppCrashingLocked(ProcessRecord app,
String shortMsg, String longMsg, String stackTrace) {
app.crashing = true;







