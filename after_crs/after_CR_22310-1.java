/*Removing duplicit constants definitions.

These definitions duplicated those from IApplicationThread, which
are widely used in framework. Now only one real constants definition
exists and future divergence is less probable.

This is the part 4 of refactoring BackupRecord class. Subsequent
commit(s) will follow.

Change-Id:I4703f08d8fa7261f2d280d0b967f2bf12506367b*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/BackupRecord.java b/services/java/com/android/server/am/BackupRecord.java
//Synthetic comment -- index 106d922..cca1143 100644

//Synthetic comment -- @@ -16,14 +16,15 @@

package com.android.server.am;

import android.app.IApplicationThread;
import android.content.pm.ApplicationInfo;

/** @hide */
final class BackupRecord {

    static final int BACKUP_INCREMENTAL = IApplicationThread.BACKUP_MODE_INCREMENTAL;
    static final int BACKUP_FULL = IApplicationThread.BACKUP_MODE_FULL;
    static final int RESTORE = IApplicationThread.BACKUP_MODE_RESTORE;

final ApplicationInfo appInfo;         // information about BackupAgent's app
final int backupMode;                  // full backup / incremental / restore







