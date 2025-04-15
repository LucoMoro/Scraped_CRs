/*Debug database locks from the main thread

Added debug functionality for printing stack traces when locking the
database from the main thread. This is very useful for detecting
potential ANR problems and increasing UI reponsiveness.

Change-Id:I88d63de43299b98fdffe37ad8cd7ca054406a820*/
//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteDatabase.java b/core/java/android/database/sqlite/SQLiteDatabase.java
//Synthetic comment -- index d4f9b20..b38de4c 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.database.SQLException;
import android.database.sqlite.SQLiteDebug.DbStats;
import android.os.Debug;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
//Synthetic comment -- @@ -373,6 +374,11 @@
/* package */ void lock() {
if (!mLockingEnabled) return;
mLock.lock();
if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING) {
if (mLock.getHoldCount() == 1) {
// Use elapsed real-time since the CPU may sleep when waiting for IO








//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteDebug.java b/core/java/android/database/sqlite/SQLiteDebug.java
//Synthetic comment -- index 89c3f96..4b6bc7c 100644

//Synthetic comment -- @@ -65,6 +65,12 @@
Log.isLoggable("SQLiteLockStackTrace", Log.VERBOSE);

/**
* Contains statistics about the active pagers in the current process.
*
* @see #getPagerStats(PagerStats)







