/*Removing unused code.

It was quite expensive threasure hunting.

This is the part 1 of refactoring BackupRecord class. Subsequent
commit(s) will follow.

Change-Id:I07b592f5dbe6247e3ba3e6d431d79450f1b85663*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 0e38e10..47b0c56 100755

//Synthetic comment -- @@ -9860,13 +9860,7 @@

synchronized(this) {
// !!! TODO: currently no check here that we're already bound
            BatteryStatsImpl.Uid.Pkg.Serv ss = null;
            BatteryStatsImpl stats = mBatteryStatsService.getActiveStatistics();
            synchronized (stats) {
                ss = stats.getServiceStatsLocked(app.uid, app.packageName, app.name);
            }

            BackupRecord r = new BackupRecord(ss, app, backupMode);
ComponentName hostingName = new ComponentName(app.packageName, app.backupAgentName);
// startProcessLocked() returns existing proc's record if it's already running
ProcessRecord proc = startProcessLocked(app.processName, app,








//Synthetic comment -- diff --git a/services/java/com/android/server/am/BackupRecord.java b/services/java/com/android/server/am/BackupRecord.java
//Synthetic comment -- index 6590b91..ab56923 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.server.am;

import com.android.internal.os.BatteryStatsImpl;

import android.content.pm.ApplicationInfo;

/** @hide */
//Synthetic comment -- @@ -27,7 +25,6 @@
public static final int BACKUP_FULL = 1;
public static final int RESTORE = 2;

    final BatteryStatsImpl.Uid.Pkg.Serv stats;
String stringName;                     // cached toString() output
final ApplicationInfo appInfo;         // information about BackupAgent's app
final int backupMode;                  // full backup / incremental / restore
//Synthetic comment -- @@ -35,9 +32,7 @@

// ----- Implementation -----

    BackupRecord(BatteryStatsImpl.Uid.Pkg.Serv _agentStats, ApplicationInfo _appInfo,
            int _backupMode) {
        stats = _agentStats;
appInfo = _appInfo;
backupMode = _backupMode;
}







