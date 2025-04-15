/*Framework/base: Added synchronization block to avoid race condition

Fix for the synchronization issue leading to access of an array
Index out of bounds. Issue occurs due to race condition between
removing the activities of a crashed process from history stack
and resuming a separate activity.

Change-Id:I14bb5834e778c15b674248e46fe93b0ce9f37967*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityStack.java b/services/java/com/android/server/am/ActivityStack.java
//Synthetic comment -- index 4546dc3..676722f 100644

//Synthetic comment -- @@ -3623,7 +3623,9 @@
}

if (activityRemoved) {
            resumeTopActivityLocked(null);
}

return res;







