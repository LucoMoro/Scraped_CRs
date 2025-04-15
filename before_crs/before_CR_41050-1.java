/*Added check of ActivityInfo launch mode when starting activity.

When starting a singleTop activity from a singleInstance activity,
the onNewIntent() callback was not called when expected. The
reason for this was that only the launchMode specified on the
intent was checked and not the launchMode specified in the manifest.

Fixes issue 17137.

Change-Id:I1a9bc1007d6f5145bf93a6161534732bf5214b7a*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityStack.java b/services/java/com/android/server/am/ActivityStack.java
//Synthetic comment -- index 9171e47..5199030 100755

//Synthetic comment -- @@ -2762,7 +2762,8 @@
// If the top activity in the task is the root
// activity, deliver this new intent to it if it
// desires.
                        if ((launchFlags&Intent.FLAG_ACTIVITY_SINGLE_TOP) != 0
&& taskTop.realActivity.equals(r.realActivity)) {
logStartActivity(EventLogTags.AM_NEW_INTENT, r, taskTop.task);
if (taskTop.frontOfTask) {







