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
            final BackupRecord r = new BackupRecord(backupMode, app, proc);
if (proc == null) {
Slog.e(TAG, "Unable to start backup agent process " + r);
return false;








//Synthetic comment -- diff --git a/services/java/com/android/server/am/BackupRecord.java b/services/java/com/android/server/am/BackupRecord.java
//Synthetic comment -- index cca1143..e6f110a 100644

//Synthetic comment -- @@ -19,32 +19,63 @@
import android.app.IApplicationThread;
import android.content.pm.ApplicationInfo;

/**
 * @hide
 */
final class BackupRecord {

static final int BACKUP_INCREMENTAL = IApplicationThread.BACKUP_MODE_INCREMENTAL;
static final int BACKUP_FULL = IApplicationThread.BACKUP_MODE_FULL;
static final int RESTORE = IApplicationThread.BACKUP_MODE_RESTORE;

    /**
     * Information about BackupAgent's app.
     */
    final ApplicationInfo appInfo;

    /**
     * Backup mode: incremental, full, or restore.
     */
    final int backupMode;

    /**
     * Where this agent is running or {@code null}.
     */
    final ProcessRecord app;

    /**
     * Creates a new backup record.
     *
     * @param mode Backup mode.
     * @param appInfo Information about BackupAgent's app.
     * @param process Where this agent is running.
     *
     * @throws IllegalArgumentException if {@code backupMode} is not one
     * of {@code BACKUP_INCREMENTAL}, {@code BACKUP_FULL} and {@code RESTORE}.
     */
    BackupRecord(int mode, ApplicationInfo appInfo, ProcessRecord process) {
        if (!modeIsValid(mode)) {
            throw new IllegalArgumentException("Invalid backup mode: " + mode);
        }
        this.backupMode = mode;
        this.appInfo = appInfo;
        // TODO: THERE IS NO REASON TO ALLOW 'null' HERE, BUT..
        // .. review the usage of the constructor and refactor.
        this.app = process;
    }

    private static boolean modeIsValid(final int mode) {
        return mode == BACKUP_INCREMENTAL || mode == BACKUP_FULL || mode == RESTORE;
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







