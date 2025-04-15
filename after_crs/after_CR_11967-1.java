/*Don't clear activity stack when returning to a singleTask task.

The if-statement on line 3333 causes the activity stack to be cleared if the launch mode is singleTask,
regardless of the state of FLAG_ACTIVITY_CLEAR_TOP. As the documentation says, the stack should be
preserved on a singleTask activity. (Unless FLAG_ACTIVITY_CLEAR_TOP is set)http://developer.android.com/guide/topics/fundamentals.html"a singleTask activity may or may not have other activities above it in the stack. If it does, it is not in
position to handle the intent, and the intent is dropped. (Even though the intent is dropped, its arrival would
have caused the task to come to the foreground, where it would remain.)"*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index a473db27..c88096b 100644

//Synthetic comment -- @@ -3331,7 +3331,6 @@
return START_RETURN_INTENT_TO_CALLER;
}
if ((launchFlags&Intent.FLAG_ACTIVITY_CLEAR_TOP) != 0
|| r.launchMode == ActivityInfo.LAUNCH_SINGLE_INSTANCE) {
// In this situation we want to remove all activities
// from the task up to the one being started.  In most







