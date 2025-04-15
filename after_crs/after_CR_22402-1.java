/*Removing duplicit constants definitions.

These definitions duplicated those from IApplicationThread, which
are widely used in framework. Now only one real constants definition
exists and future divergence is less probable.

Change-Id:Iccb23847eeb37196db64fc9ea3bf7513091db9d8*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/BackupRecord.java b/services/java/com/android/server/am/BackupRecord.java
//Synthetic comment -- index 6590b91..d658ff7 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.server.am;

import android.app.IApplicationThread;
import com.android.internal.os.BatteryStatsImpl;

import android.content.pm.ApplicationInfo;
//Synthetic comment -- @@ -23,9 +24,9 @@
/** @hide */
class BackupRecord {
// backup/restore modes
    public static final int BACKUP_NORMAL = IApplicationThread.BACKUP_MODE_INCREMENTAL;
    public static final int BACKUP_FULL = IApplicationThread.BACKUP_MODE_FULL;
    public static final int RESTORE = IApplicationThread.BACKUP_MODE_RESTORE;

final BatteryStatsImpl.Uid.Pkg.Serv stats;
String stringName;                     // cached toString() output







