/*NsdService: Fix to set source messenger info in replyTo

replyToMessage helper API uses mReplyChannel to send a reply
to destination messenger, however the source messenger isn't
set in mReplyChannel. Fix is to provide an overloaded constructor
in AsyncChannel to accept source context and source handler.

Change-Id:Iecf8af34834ce014f9159190d67f383d17262d06*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/util/AsyncChannel.java b/core/java/com/android/internal/util/AsyncChannel.java
//Synthetic comment -- index 5093b4d..d41e3a6 100644

//Synthetic comment -- @@ -202,6 +202,14 @@
}

/**
* Connect handler to named package/class synchronously.
*
* @param srcContext is the context of the source








//Synthetic comment -- diff --git a/services/java/com/android/server/NsdService.java b/services/java/com/android/server/NsdService.java
//Synthetic comment -- index 87843d9..7fc464a 100644

//Synthetic comment -- @@ -77,8 +77,6 @@
/* A map from unique id to client info */
private SparseArray<ClientInfo> mIdToClientInfoMap= new SparseArray<ClientInfo>();

    private AsyncChannel mReplyChannel = new AsyncChannel();

private int INVALID_ID = 0;
private int mUniqueId = 1;

//Synthetic comment -- @@ -109,6 +107,8 @@
private final DisabledState mDisabledState = new DisabledState();
private final EnabledState mEnabledState = new EnabledState();

@Override
protected String getWhatToString(int what) {
return cmdToString(what);
//Synthetic comment -- @@ -146,6 +146,38 @@
}
setLogRecSize(25);
registerForNsdSetting();
}

class DefaultState extends State {
//Synthetic comment -- @@ -765,37 +797,6 @@
mNsdStateMachine.dump(fd, pw, args);
}

    /* arg2 on the source message has an id that needs to be retained in replies
     * see NsdManager for details */
    private Message obtainMessage(Message srcMsg) {
        Message msg = Message.obtain();
        msg.arg2 = srcMsg.arg2;
        return msg;
    }

    private void replyToMessage(Message msg, int what) {
        if (msg.replyTo == null) return;
        Message dstMsg = obtainMessage(msg);
        dstMsg.what = what;
        mReplyChannel.replyToMessage(msg, dstMsg);
    }

    private void replyToMessage(Message msg, int what, int arg1) {
        if (msg.replyTo == null) return;
        Message dstMsg = obtainMessage(msg);
        dstMsg.what = what;
        dstMsg.arg1 = arg1;
        mReplyChannel.replyToMessage(msg, dstMsg);
    }

    private void replyToMessage(Message msg, int what, Object obj) {
        if (msg.replyTo == null) return;
        Message dstMsg = obtainMessage(msg);
        dstMsg.what = what;
        dstMsg.obj = obj;
        mReplyChannel.replyToMessage(msg, dstMsg);
    }

/* Information tracked per client */
private class ClientInfo {








