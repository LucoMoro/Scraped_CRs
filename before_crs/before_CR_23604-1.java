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
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -17,6 +18,7 @@
package com.android.server.am;

import com.android.internal.R;
import com.android.internal.os.BatteryStatsImpl;
import com.android.server.AttributeCache;
import com.android.server.IntentResolver;
//Synthetic comment -- @@ -112,6 +114,7 @@
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManagerPolicy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
//Synthetic comment -- @@ -542,6 +545,9 @@
*/
ProcessRecord mHomeProcess;

/**
* Set of PendingResultRecord objects that are currently active.
*/
//Synthetic comment -- @@ -2332,6 +2338,19 @@
}
}

/**
* This is the internal entry point for handling Activity.finish().
* 
//Synthetic comment -- @@ -2343,6 +2362,11 @@
*/
public final boolean finishActivity(IBinder token, int resultCode, Intent resultData) {
// Refuse possible leaked file descriptors
if (resultData != null && resultData.hasFileDescriptors() == true) {
throw new IllegalArgumentException("File descriptors passed in Intent");
}
//Synthetic comment -- @@ -5000,6 +5024,10 @@
moveTaskBackwardsLocked(task);
Binder.restoreCallingIdentity(origId);
}
}

private final void moveTaskBackwardsLocked(int task) {
//Synthetic comment -- @@ -6213,9 +6241,13 @@
mProcessesReady = true;
}

Slog.i(TAG, "System now ready");
EventLog.writeEvent(EventLogTags.BOOT_PROGRESS_AMS_READY,
SystemClock.uptimeMillis());

synchronized(this) {
// Make sure we have no pre-ready processes sitting around.
//Synthetic comment -- @@ -6295,6 +6327,33 @@
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
* Callback function from app to telephony to pass a result code and user's







