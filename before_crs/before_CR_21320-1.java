/*Adding a sanity test in the while loop in readHistory()

Adding a sanity test in the while loop in readHistory() which
is useful in case the file read (batterystats.bin), is corrupt.
The file can get corrupt (missing end of file marker -1) if
writeHistory fails for some reason. Without this change
readHistory will continue looping untill an OutOfMemory is
thrown.

Change-Id:I3418d258e7cb99aa2f9d934c9449a19806499ce4*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/os/BatteryStatsImpl.java b/core/java/com/android/internal/os/BatteryStatsImpl.java
//Synthetic comment -- index fb4dff8..5b9c78c 100644

//Synthetic comment -- @@ -4563,7 +4563,7 @@
mHistory = mHistoryEnd = mHistoryCache = null;
mHistoryBaseTime = 0;
long time;
        while ((time=in.readLong()) >= 0) {
HistoryItem rec = new HistoryItem(time, in);
addHistoryRecordLocked(rec);
if (rec.time > mHistoryBaseTime) {







