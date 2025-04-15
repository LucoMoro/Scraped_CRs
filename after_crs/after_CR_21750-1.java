/*Added support for handling priority of displaytext.

Requires that the CatApp has permission for GET_TASKS

Change-Id:I20db34705099aec8945351c5257ce1d9befa2d97*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 1e23e34..fcb3591 100644

//Synthetic comment -- @@ -1,5 +1,4 @@
/*
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -16,8 +15,13 @@

package com.android.internal.telephony.cat;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
//Synthetic comment -- @@ -30,9 +34,11 @@
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;

import android.telephony.TelephonyManager;
import android.util.Config;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
* Enumeration for representing the tag value of COMPREHENSION-TLV objects. If
//Synthetic comment -- @@ -255,7 +261,17 @@
sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
break;
case DISPLAY_TEXT:
                // when screen is busy based on the priority of the cmd, send
                // response with screen busy
                if (!canDisplayText(cmdMsg.geTextMessage().isHighPriority)) {
                    sendTerminalResponse(cmdParams.cmdDet,
                            ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS, true, 0x01,
                            null);
                    return;
                }

                // when application is not required to respond, send an
                // immediate response.
if (!cmdMsg.geTextMessage().responseNeeded) {
sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
}
//Synthetic comment -- @@ -690,4 +706,78 @@
sendTerminalResponse(cmdDet, resMsg.resCode, false, 0, resp);
mCurrntCmd = null;
}

    /*
     * Used to determine if the screen can show displaytext depending on the
     * priority and the terminals current state.
     *
     * Refs ETSI TS 102 223
     *
     * @param isHighPriority true if high priority.
     */
    private boolean canDisplayText(boolean isHighPriority) {
        if (isHighPriority) {
            // Check for conflicting high priority events, the resolution of high
            // priority is up to the terminal.
            TelephonyManager tm =
                (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm.getCallState() == TelephonyManager.CALL_STATE_RINGING) {
                CatLog.d(sInstance, "Cant show DISPLAY_TEXT with HIGH priority, "
                        +"conflict with incomming call");
                return false;
            }
            return true;
        } else {
            // Display text with normal priority shall only be shown when the
            // terminal screen shows the default/standby screen according to
            // spec. For Android that is the home screen activity.

            // Get list of installed home applications
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            PackageManager pm = mContext.getPackageManager();
            List<ResolveInfo> homeApps =
                pm.queryIntentActivities(homeIntent, PackageManager.GET_ACTIVITIES);

            // Get list of running applications
            List<RunningTaskInfo> runningTasks;
            try {
                ActivityManager am =
                    (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
                runningTasks = am.getRunningTasks(3);
            } catch (SecurityException e) {
                CatLog.d(sInstance, "Cant show DISPLAY_TEXT with NORMAL priority, "
                        +"missing permission android.permission.GET_TASKS");
                return false;
            }
            for (RunningTaskInfo task : runningTasks) {
                if (task != null && task.numRunning > 0) {
                    ComponentName cn = task.topActivity;
                    if (cn == null) {
                        continue;
                    }

                    String pkg = cn.getPackageName();
                    if (pkg == null) {
                        continue;
                    }

                    // Check if running app is home app
                    for (ResolveInfo app : homeApps) {
                        if (app.activityInfo.packageName != null) {
                            if (pkg.startsWith(app.activityInfo.packageName)) {
                                return true;
                            }
                        }
                    }
                    CatLog.d(sInstance, "Cant show DISPLAY_TEXT with NORMAL priority, "
                            +"homescreen not visible");
                    return false;
                }
            }
            CatLog.d(sInstance, "Cant show DISPLAY_TEXT with NORMAL priority, "
                    +"cant determine if homescreen is visible");
            return false;
        }
    }
}







