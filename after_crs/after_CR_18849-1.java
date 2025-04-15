/*Without SD card the shutdown sequence was delayed

If the memory card is not inserted (or removed) from
the phone the shut down process is very long. It
takes almost 24 seconds. For the phone with memory
card the averige is 5-6 seconds

Make sure to send onShutDownComplete even if an SD
card is not mounted and no unmount is done.

Change-Id:I0e79b82e294a971f5e7144cdd3cc16b7ff414b9c*/




//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index cb55808..542c740 100644

//Synthetic comment -- @@ -958,6 +958,17 @@
// Post a unmount message.
ShutdownCallBack ucb = new ShutdownCallBack(path, observer);
mHandler.sendMessage(mHandler.obtainMessage(H_UNMOUNT_PM_UPDATE, ucb));
        } else if (observer != null) {
            /*
             * Observer is waiting for onShutDownComplete when we are done.
             * Since nothing will be done send notification directly so shutdown
             * sequence can continue.
             */
            try {
                observer.onShutDownComplete(StorageResultCode.OperationSucceeded);
            } catch (RemoteException e) {
                Slog.w(TAG, "RemoteException when shutting down");
            }
}
}








