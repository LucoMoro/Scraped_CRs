/*The final story for BackupRecord class.

Field 'app' is now immutable, which is easier for programmers
minds as well as for compiler, VM and JIT.

Class is final now. No reason for subclassing.

This is the part 2 of refactoring BackupRecord class. Subsequent
commit(s) will follow.

Change-Id:I478923855f6921dde8ef4a93392e81671fde9325*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 47b0c56..e2ce2d5 100755

//Synthetic comment -- @@ -9860,17 +9860,16 @@

synchronized(this) {
// !!! TODO: currently no check here that we're already bound
ComponentName hostingName = new ComponentName(app.packageName, app.backupAgentName);
// startProcessLocked() returns existing proc's record if it's already running
ProcessRecord proc = startProcessLocked(app.processName, app,
false, 0, "backup", hostingName, false);
            final BackupRecord r = new BackupRecord(app, backupMode, proc);
if (proc == null) {
Slog.e(TAG, "Unable to start backup agent process " + r);
return false;
}

mBackupTarget = r;
mBackupAppName = app.packageName;









//Synthetic comment -- diff --git a/services/java/com/android/server/am/BackupRecord.java b/services/java/com/android/server/am/BackupRecord.java
//Synthetic comment -- index ab56923..08dd27c 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import android.content.pm.ApplicationInfo;

/** @hide */
final class BackupRecord {
// backup/restore modes
public static final int BACKUP_NORMAL = 0;
public static final int BACKUP_FULL = 1;
//Synthetic comment -- @@ -28,13 +28,14 @@
String stringName;                     // cached toString() output
final ApplicationInfo appInfo;         // information about BackupAgent's app
final int backupMode;                  // full backup / incremental / restore
    final ProcessRecord app;                     // where this agent is running or null

// ----- Implementation -----

    BackupRecord(ApplicationInfo _appInfo, int _backupMode, ProcessRecord app) {
appInfo = _appInfo;
backupMode = _backupMode;
        this.app = app;
}

public String toString() {







