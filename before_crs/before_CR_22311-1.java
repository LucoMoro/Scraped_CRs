/*Final cleanup of BackupRecord class.

- Changing constructor signature: checked attributes go first.
- Adding proper JavaDoc documentation.
- Checking value of backup mode and refusing invalid values.
- Adding TODO item for outstanding issue.

This is the last commit of this series.

Change-Id:I45ae35c169ec9623c9ece912e90fc482149df399*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index e2ce2d5..0c05284 100755

//Synthetic comment -- @@ -9864,7 +9864,7 @@
// startProcessLocked() returns existing proc's record if it's already running
ProcessRecord proc = startProcessLocked(app.processName, app,
false, 0, "backup", hostingName, false);
            final BackupRecord r = new BackupRecord(app, backupMode, proc);
if (proc == null) {
Slog.e(TAG, "Unable to start backup agent process " + r);
return false;








//Synthetic comment -- diff --git a/services/java/com/android/server/am/BackupRecord.java b/services/java/com/android/server/am/BackupRecord.java
//Synthetic comment -- index cca1143..e6f110a 100644

//Synthetic comment -- @@ -19,32 +19,63 @@
import android.app.IApplicationThread;
import android.content.pm.ApplicationInfo;

/** @hide */
final class BackupRecord {

static final int BACKUP_INCREMENTAL = IApplicationThread.BACKUP_MODE_INCREMENTAL;
static final int BACKUP_FULL = IApplicationThread.BACKUP_MODE_FULL;
static final int RESTORE = IApplicationThread.BACKUP_MODE_RESTORE;
    
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
final StringBuilder sb = new StringBuilder(128);
sb.append("BackupRecord{")
            .append(Integer.toHexString(System.identityHashCode(this)))
            .append(' ').append(appInfo.packageName)
            .append(' ').append(appInfo.name)
            .append(' ').append(appInfo.backupAgentName).append('}');
return sb.toString();
}
}







