/*Seperate Messages with/without callback.

Because Runnable is internally Message with Callback that what field is
'0', it can be conflicted with general Messages that 'what' field is
'0'.
To avoid it, seperate Messages and Runnables by adding one more method
that checking Message's what field and callback field and use it.

Change-Id:I6d3b5c5640d265e6cd3ef43267ddac60656283a1*/
//Synthetic comment -- diff --git a/core/java/android/os/Handler.java b/core/java/android/os/Handler.java
//Synthetic comment -- index 3b2bf1e..0c6831b 100644

//Synthetic comment -- @@ -497,7 +497,7 @@
* message queue.
*/
public final void removeMessages(int what) {
        mQueue.removeMessages(this, what, null, true);
}

/**
//Synthetic comment -- @@ -505,7 +505,7 @@
* 'object' that are in the message queue.
*/
public final void removeMessages(int what, Object object) {
        mQueue.removeMessages(this, what, object, true);
}

/**
//Synthetic comment -- @@ -521,7 +521,7 @@
* the message queue.
*/
public final boolean hasMessages(int what) {
        return mQueue.removeMessages(this, what, null, false);
}

/**
//Synthetic comment -- @@ -529,7 +529,7 @@
* whose obj is 'object' in the message queue.
*/
public final boolean hasMessages(int what, Object object) {
        return mQueue.removeMessages(this, what, object, false);
}

// if we can get rid of this method, the handler need not remember its loop








//Synthetic comment -- diff --git a/core/java/android/os/MessageQueue.java b/core/java/android/os/MessageQueue.java
//Synthetic comment -- index 0090177..2f4583b 100644

//Synthetic comment -- @@ -297,6 +297,44 @@
}
}

final void removeCallbacksAndMessages(Handler h, Object object) {
synchronized (this) {
Message p = mMessages;







