/*frameworks/base: Cap the number of toasts that a package can post.

NotificationManagerService keeps track of requested toasts in a
queue. Any package can trigger a DoS by repeated enqueue of
toasts which eventually results in a leak of WeakReferences in
system_server and causes dalvik (hosting system_server) to
abort the same.

Change-Id:I5e23c1bf7e195b07344711d2c6719fa568f2dfaf*/




//Synthetic comment -- diff --git a/services/java/com/android/server/NotificationManagerService.java b/services/java/com/android/server/NotificationManagerService.java
//Synthetic comment -- index 4da5eb2..2c03d9e 100755

//Synthetic comment -- @@ -509,6 +509,8 @@
return ;
}

        checkIncomingCall(pkg);

synchronized (mToastQueue) {
int callingPid = Binder.getCallingPid();
long callingId = Binder.clearCallingIdentity();
//Synthetic comment -- @@ -521,6 +523,24 @@
record = mToastQueue.get(index);
record.update(duration);
} else {
                    // Limit the number of toasts that any given package except the android
                    // package can enqueue.  Prevents DOS attacks and deals with leaks.
                    if (!"android".equals(pkg)) {
                        int count = 0;
                        final int N = mToastQueue.size();
                        for (int i=0; i<N; i++) {
                             final ToastRecord r = mToastQueue.get(i);
                             if (r.pkg.equals(pkg)) {
                                 count++;
                                 if (count >= MAX_PACKAGE_NOTIFICATIONS) {
                                     Slog.e(TAG, "Package has already posted " + count
                                            + " toasts. Not showing more. Package=" + pkg);
                                     return;
                                 }
                             }
                        }
                    }

record = new ToastRecord(callingPid, pkg, callback, duration);
mToastQueue.add(record);
index = mToastQueue.size() - 1;







