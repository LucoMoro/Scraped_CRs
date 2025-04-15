/*Seperate Messages with/without callback.

Because Runnable is internally Message with Callback that what field is
'0', it can be conflicted with general Messages that 'what' field is
'0'.
To avoid it, seperate Messages and Runnables by adding one more method
that checking Message's what field and callback field optionally and use it.

Change-Id:I6d3b5c5640d265e6cd3ef43267ddac60656283a1*/
//Synthetic comment -- diff --git a/core/java/android/os/Handler.java b/core/java/android/os/Handler.java
//Synthetic comment -- index 3b2bf1e..126b16f 100644

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
//Synthetic comment -- index 0090177..913ce27 100644

//Synthetic comment -- @@ -227,13 +227,31 @@

final boolean removeMessages(Handler h, int what, Object object,
boolean doRemove) {
synchronized (this) {
Message p = mMessages;
boolean found = false;

// Remove all messages at front.
            while (p != null && p.target == h && p.what == what
                   && (object == null || p.obj == object)) {
if (!doRemove) return true;
found = true;
Message n = p.next;
//Synthetic comment -- @@ -246,8 +264,13 @@
while (p != null) {
Message n = p.next;
if (n != null) {
                    if (n.target == h && n.what == what
                        && (object == null || n.obj == object)) {
if (!doRemove) return true;
found = true;
Message nn = n.next;
//Synthetic comment -- @@ -258,45 +281,11 @@
}
p = n;
}
            
return found;
}
}

    final void removeMessages(Handler h, Runnable r, Object object) {
        if (r == null) {
            return;
        }

        synchronized (this) {
            Message p = mMessages;

            // Remove all messages at front.
            while (p != null && p.target == h && p.callback == r
                   && (object == null || p.obj == object)) {
                Message n = p.next;
                mMessages = n;
                p.recycle();
                p = n;
            }

            // Remove all messages after front.
            while (p != null) {
                Message n = p.next;
                if (n != null) {
                    if (n.target == h && n.callback == r
                        && (object == null || n.obj == object)) {
                        Message nn = n.next;
                        n.recycle();
                        p.next = nn;
                        continue;
                    }
                }
                p = n;
            }
        }
    }

final void removeCallbacksAndMessages(Handler h, Object object) {
synchronized (this) {
Message p = mMessages;







