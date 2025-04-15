/*frameworks/base: Handle null from topRunningNonDelayedActivityLocked

startActivityUncheckedLocked tries to move the target task to front
when it is not at front. topRunningNonDelayedActivityLocked is used
to find the current task, however null value isn't handled. This
null causes an unhandled exception leading to the android framework
reboot.

Change-Id:I2a43cda50483e28a4456846d8b3ccb30d7cf110e*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 804af9c..ef35bf3 100644

//Synthetic comment -- @@ -3410,7 +3410,7 @@
// being started, which means not bringing it to the front
// if the caller is not itself in the front.
HistoryRecord curTop = topRunningNonDelayedActivityLocked(notTop);
                    if ((curTop != null) && (curTop.task != taskTop.task)) {
r.intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
boolean callerAtFront = sourceRecord == null
|| curTop.task == sourceRecord.task;







