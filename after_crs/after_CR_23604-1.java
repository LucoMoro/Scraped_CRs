/*Telephony: Add support to send screen status to Stk App

Screen idle intent is sent from the ActivityManagerService
to Apps/Stk when an activity is finished or moved to back.
Screen busy intent is sent when an activity is started.

Change-Id:Ibdb4900717e3e8d909fbbc7958c577712ea73e58*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index d24ce7e..611060b 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006-2008 The Android Open Source Project
 * Copyright (c) 2010-2011, Code Aurora Forum. All rights reserved
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -17,6 +18,7 @@
package com.android.server.am;

import com.android.internal.R;
import com.android.internal.telephony.cat.AppInterface;
import com.android.internal.os.BatteryStatsImpl;
import com.android.server.AttributeCache;
import com.android.server.IntentResolver;
//Synthetic comment -- @@ -112,6 +114,7 @@
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManagerPolicy;
import android.content.BroadcastReceiver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
//Synthetic comment -- @@ -542,6 +545,9 @@
*/
ProcessRecord mHomeProcess;


    private boolean mScreenStatusRequest = false;

/**
* Set of PendingResultRecord objects that are currently active.
*/
//Synthetic comment -- @@ -2332,6 +2338,19 @@
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
//Synthetic comment -- @@ -2343,6 +2362,11 @@
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
//Synthetic comment -- @@ -5000,6 +5024,10 @@
moveTaskBackwardsLocked(task);
Binder.restoreCallingIdentity(origId);
}
        // When an activity is moved to back check if the top is home activity.
        if (mScreenStatusRequest) {
            checkScreenIdle();
        }
}

private final void moveTaskBackwardsLocked(int task) {
//Synthetic comment -- @@ -6213,9 +6241,13 @@
mProcessesReady = true;
}


Slog.i(TAG, "System now ready");
EventLog.writeEvent(EventLogTags.BOOT_PROGRESS_AMS_READY,
SystemClock.uptimeMillis());
        IntentFilter bootFilter = new IntentFilter(AppInterface.CHECK_SCREEN_IDLE_ACTION);
        mContext.registerReceiver(new ScreenStatusReceiver(),bootFilter);


synchronized(this) {
// Make sure we have no pre-ready processes sitting around.
//Synthetic comment -- @@ -6295,6 +6327,33 @@
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








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/AppInterface.java b/telephony/java/com/android/internal/telephony/cat/AppInterface.java
//Synthetic comment -- index 2eb6ccb..5be6724 100644

//Synthetic comment -- @@ -31,6 +31,18 @@
"android.intent.action.stk.command";
public static final String CAT_SESSION_END_ACTION =
"android.intent.action.stk.session_end";
    /*
     * This is broadcast from the ActivityManagerService when the screen
     * switches to idle or busy state
     */
    public static final String CAT_IDLE_SCREEN_ACTION =
        "android.intent.action.stk.idle_screen";
    /*
     * This is broadcast from the Stk Apps to ActivityManagerService
     * when the screen status is requested.
     */
    public static final String CHECK_SCREEN_IDLE_ACTION =
        "android.intent.action.stk.check_screen_idle";

/*
* Callback function from app to telephony to pass a result code and user's







