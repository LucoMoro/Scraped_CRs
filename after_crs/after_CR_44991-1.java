/*Frameworks: Usat Phase 2 feature and Screen status support

Added changes to support Icon, Idle screen, Language Selection.

This also includes change to support Idle screen status information.

Change-Id:I3f66a4491291b043b803dce00ac992002301309d*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 5d74cf3..6074834c 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import com.android.internal.R;
import com.android.internal.telephony.cat.AppInterface;
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


    private boolean mScreenStatusRequest = false;

/**
* This is the process holding the activity the user last visited that
* is in a different process from the one they are currently in.
//Synthetic comment -- @@ -2625,6 +2630,19 @@
}
}

    /* Checks for the last activity.If it was home then send an intent to stk */
    private void checkScreenIdle() {
        int top = mMainStack.mHistory.size() - 1;
        if (top >= 0) {
            ActivityRecord p = (ActivityRecord)mMainStack.mHistory.get(top - 1);
            if (p.intent.hasCategory(Intent.CATEGORY_HOME)) {
                Intent StkIntent = new Intent(AppInterface.CAT_IDLE_SCREEN_ACTION);
                StkIntent.putExtra("SCREEN_IDLE", true);
                mContext.sendBroadcast(StkIntent);
            }
        }
    }

/**
* This is the internal entry point for handling Activity.finish().
* 
//Synthetic comment -- @@ -2636,6 +2654,11 @@
*/
public final boolean finishActivity(IBinder token, int resultCode, Intent resultData) {
// Refuse possible leaked file descriptors
        // When an activity ends check if the top is home activity.
        if (mScreenStatusRequest) {
            checkScreenIdle();
        }

if (resultData != null && resultData.hasFileDescriptors() == true) {
throw new IllegalArgumentException("File descriptors passed in Intent");
}
//Synthetic comment -- @@ -5932,6 +5955,10 @@
moveTaskBackwardsLocked(task);
Binder.restoreCallingIdentity(origId);
}
        // When an activity is moved to back check if the top is home activity.
        if (mScreenStatusRequest) {
            checkScreenIdle();
        }
}

private final void moveTaskBackwardsLocked(int task) {
//Synthetic comment -- @@ -7586,6 +7613,9 @@
Slog.i(TAG, "System now ready");
EventLog.writeEvent(EventLogTags.BOOT_PROGRESS_AMS_READY,
SystemClock.uptimeMillis());
        IntentFilter bootFilter = new IntentFilter(AppInterface.CHECK_SCREEN_IDLE_ACTION);
        mContext.registerReceiver(new ScreenStatusReceiver(),bootFilter);


synchronized(this) {
// Make sure we have no pre-ready processes sitting around.
//Synthetic comment -- @@ -7665,6 +7695,33 @@
}
}

    class ScreenStatusReceiver extends BroadcastReceiver {

        @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(AppInterface.CHECK_SCREEN_IDLE_ACTION)) {
                    mScreenStatusRequest  = intent.getBooleanExtra("SCREEN_STATUS_REQUEST",false);
                    if (mScreenStatusRequest) {
                        Slog.i(ActivityManagerService.TAG, "Screen Status request is ON");
                        int top = mMainStack.mHistory.size() - 1;
                        if (top >= 0) {
                            Intent StkIntent = new Intent(AppInterface.CAT_IDLE_SCREEN_ACTION);
                            ActivityRecord p = (ActivityRecord)mMainStack.mHistory.get(top);
                            if (p.intent.hasCategory(Intent.CATEGORY_HOME)) {
                                StkIntent.putExtra("SCREEN_IDLE",true);
                            } else {
                                StkIntent.putExtra("SCREEN_IDLE",false);
                            }
                            mContext.sendBroadcast(StkIntent);
                        }
                    } else {
                        Slog.i(ActivityManagerService.TAG, "Screen Status request is OFF");
                    }
                }
            }
    }


private boolean makeAppCrashingLocked(ProcessRecord app,
String shortMsg, String longMsg, String stackTrace) {
app.crashing = true;







