/*Adding null check in handleAppCrashLocked()

Adding null check in handleAppCrashLocked when retrieving the top
running activity to avoid crash of system server process. This
has been observed very rarely in internal crash reports.

Change-Id:Ibfb9bde270a689fde9881427d2c4cbd08a3aa53c*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 0e38e10..958c460 100755

//Synthetic comment -- @@ -6403,7 +6403,7 @@
}
} else {
ActivityRecord r = mMainStack.topRunningActivityLocked(null);
            if (r.app == app) {
// If the top running activity is from this crashing
// process, then terminate it to avoid getting in a loop.
Slog.w(TAG, "  Force finishing activity "







