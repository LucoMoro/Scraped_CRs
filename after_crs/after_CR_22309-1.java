/*Removing toString() return value caching.

The value must not be cached, just because information
used for it are not immutable. If anybody wants to
improve performance here, he/she must simply not use
objects of BackupRecord "just for logging".

This is the part 3 of refactoring BackupRecord class. Subsequent
commit(s) will follow.

Change-Id:Ie79c71a99a4587854d8ad66518b4920647e4650f*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/BackupRecord.java b/services/java/com/android/server/am/BackupRecord.java
//Synthetic comment -- index 08dd27c..106d922 100644

//Synthetic comment -- @@ -25,7 +25,6 @@
public static final int BACKUP_FULL = 1;
public static final int RESTORE = 2;

final ApplicationInfo appInfo;         // information about BackupAgent's app
final int backupMode;                  // full backup / incremental / restore
final ProcessRecord app;                     // where this agent is running or null
//Synthetic comment -- @@ -39,15 +38,12 @@
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







