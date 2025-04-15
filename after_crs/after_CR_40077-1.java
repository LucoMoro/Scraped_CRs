/*AsyncChannel to support remote death notification and post a
disconnect message to the source handler.

Change-Id:I7368cb466d9e33f7da8095ef2de25fc70f13d47e*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/util/AsyncChannel.java b/core/java/com/android/internal/util/AsyncChannel.java
//Synthetic comment -- index 3973344..85a8ccc 100644

//Synthetic comment -- @@ -162,6 +162,9 @@
/** CMD_FULLY_CONNECTED refused because a connection already exists*/
public static final int STATUS_FULL_CONNECTION_REFUSED_ALREADY_CONNECTED = 3;

    /** Error indicating abnormal termination of destination messenger */
    public static final int STATUS_REMOTE_DISCONNECTION = 4;

/** Service connection */
private AsyncChannelConnection mConnection;

//Synthetic comment -- @@ -177,6 +180,9 @@
/** Messenger for destination */
private Messenger mDstMessenger;

    /** Death Monitor for destination messenger */
    private DeathMonitor mDeathMonitor;

/**
* AsyncChannel constructor
*/
//Synthetic comment -- @@ -416,6 +422,7 @@
mSrcHandler = null;
mSrcMessenger = null;
mDstMessenger = null;
        mDeathMonitor = null;
mConnection = null;
}

//Synthetic comment -- @@ -429,6 +436,10 @@
if (mSrcHandler != null) {
replyDisconnected(STATUS_SUCCESSFUL);
}
        // Unlink only when bindService isn't used
        if (mConnection == null && mDstMessenger != null && mDeathMonitor!= null) {
            mDstMessenger.getBinder().unlinkToDeath(mDeathMonitor, 0);
        }
}

/**
//Synthetic comment -- @@ -804,6 +815,21 @@
msg.arg1 = status;
msg.obj = this;
msg.replyTo = mDstMessenger;

        /*
         * Link to death only when bindService isn't used.
         */
        if (mConnection == null) {
            mDeathMonitor = new DeathMonitor();
            try {
                mDstMessenger.getBinder().linkToDeath(mDeathMonitor, 0);
            } catch (RemoteException e) {
                mDeathMonitor = null;
                // Override status to indicate failure
                msg.arg1 = STATUS_BINDING_UNSUCCESSFUL;
            }
        }

mSrcHandler.sendMessage(msg);
}

//Synthetic comment -- @@ -849,4 +875,15 @@
private static void log(String s) {
Slog.d(TAG, s);
}

    private final class DeathMonitor implements IBinder.DeathRecipient {

        DeathMonitor() {
        }

        public void binderDied() {
            replyDisconnected(STATUS_REMOTE_DISCONNECTION);
        }

    }
}







