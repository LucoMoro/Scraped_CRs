/*Fix for deadlock between StatusBarService and NotificationManagerService

A ServerThread holding a lock on mQueue in StatusBarService invoked a
callback in NotificationManagerService which required a lock on
mNotificationList. At  the same time, a BinderThread holding a lock on
mNotificationList was attempting to post a message to StatusBarService
which requires lock on mQueue. The fix is to release the lock on mQueue
in handleMessage() before running the actions at the end of the method.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/status/StatusBarService.java b/services/java/com/android/server/status/StatusBarService.java
//Synthetic comment -- index 34921d6..9ccd38b 100644

//Synthetic comment -- @@ -552,15 +552,17 @@
doRevealAnimation();
return;
}
synchronized (mQueue) {
boolean wasExpanded = mExpanded;

// for each one in the queue, find all of the ones with the same key
// and collapse that down into a final op and/or call to setVisibility, etc
                boolean expand = wasExpanded;
                boolean doExpand = false;
                boolean doDisable = false;
                int disableWhat = 0;
int N = mQueue.size();
while (N > 0) {
PendingOp op = mQueue.get(0);
//Synthetic comment -- @@ -634,17 +636,20 @@
if (mQueue.size() != 0) {
throw new RuntimeException("Assertion failed: mQueue.size=" + mQueue.size());
}
                if (doExpand) {
                    // this is last so that we capture all of the pending changes before doing it
                    if (expand) {
                        animateExpand();
                    } else {
                        animateCollapse();
                    }
}
                if (doDisable) {
                    performDisableActions(disableWhat);
                }
}
}
}







