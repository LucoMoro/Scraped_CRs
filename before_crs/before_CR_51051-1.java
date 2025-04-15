/*Set foreground priority for shutdown receivers.

The broadcast of ACTION_SHUTDOWN has a timeout of 10 seconds. To
reduce the risk of hitting the timeout and speeding up the
shutdown sequence in ShutdownThread we set the priority of the
BroadcastReceivers to FLAG_RECEIVER_FOREGROUND.

We have seen that this change reduces the shutdown time when
the system is under heavy load.

Change-Id:I22cbf6af8cf6fc4bdefaa1c3da8a7eed7e7b7674*/
//Synthetic comment -- diff --git a/services/java/com/android/server/power/ShutdownThread.java b/services/java/com/android/server/power/ShutdownThread.java
//Synthetic comment -- index c7f7390..c084666 100644

//Synthetic comment -- @@ -297,7 +297,9 @@

// First send the high-level shut down broadcast.
mActionDone = false;
        mContext.sendOrderedBroadcastAsUser(new Intent(Intent.ACTION_SHUTDOWN),
UserHandle.ALL, null, br, mHandler, 0, null, null);

final long endTime = SystemClock.elapsedRealtime() + MAX_BROADCAST_TIME;







