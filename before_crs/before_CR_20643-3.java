/*frameworks/base: Cap the number of toasts that a package can post.

NotificationManagerService keeps track of requested toasts in a
queue. Any package can trigger a DoS by repeated enqueue of
toasts which eventually results in a leak of WeakReferences in
system_server and causes dalvik (hosting system_server) to
abort the same.

Change-Id:I5e23c1bf7e195b07344711d2c6719fa568f2dfaf*/
//Synthetic comment -- diff --git a/services/java/com/android/server/NotificationManagerService.java b/services/java/com/android/server/NotificationManagerService.java
//Synthetic comment -- index 540389e..f5caf7a 100755

//Synthetic comment -- @@ -519,6 +519,24 @@
record = mToastQueue.get(index);
record.update(duration);
} else {
record = new ToastRecord(callingPid, pkg, callback, duration);
mToastQueue.add(record);
index = mToastQueue.size() - 1;







