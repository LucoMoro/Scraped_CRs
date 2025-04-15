/*AsyncChannel to support remote death notification and post a
disconnect message to the source handler.

Change-Id:I7368cb466d9e33f7da8095ef2de25fc70f13d47e*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/util/AsyncChannel.java b/core/java/com/android/internal/util/AsyncChannel.java
//Synthetic comment -- index 3973344..85a8ccc 100644

//Synthetic comment -- @@ -162,6 +162,9 @@
/** CMD_FULLY_CONNECTED refused because a connection already exists*/
public static final int STATUS_FULL_CONNECTION_REFUSED_ALREADY_CONNECTED = 3;

/** Service connection */
private AsyncChannelConnection mConnection;

//Synthetic comment -- @@ -177,6 +180,9 @@
/** Messenger for destination */
private Messenger mDstMessenger;

/**
* AsyncChannel constructor
*/
//Synthetic comment -- @@ -416,6 +422,7 @@
mSrcHandler = null;
mSrcMessenger = null;
mDstMessenger = null;
mConnection = null;
}

//Synthetic comment -- @@ -429,6 +436,10 @@
if (mSrcHandler != null) {
replyDisconnected(STATUS_SUCCESSFUL);
}
}

/**
//Synthetic comment -- @@ -804,6 +815,21 @@
msg.arg1 = status;
msg.obj = this;
msg.replyTo = mDstMessenger;
mSrcHandler.sendMessage(msg);
}

//Synthetic comment -- @@ -849,4 +875,15 @@
private static void log(String s) {
Slog.d(TAG, s);
}
}







