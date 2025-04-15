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
        mQueue.removeMessages(this, what, null, null, true, false, true);
}

/**
//Synthetic comment -- @@ -505,7 +505,7 @@
* 'object' that are in the message queue.
*/
public final void removeMessages(int what, Object object) {
        mQueue.removeMessages(this, what, null, object, true, false, true);
}

/**
//Synthetic comment -- @@ -521,7 +521,7 @@
* the message queue.
*/
public final boolean hasMessages(int what) {
        return mQueue.removeMessages(this, what, null, null, true, false, false);
}

/**
//Synthetic comment -- @@ -529,7 +529,7 @@
* whose obj is 'object' in the message queue.
*/
public final boolean hasMessages(int what, Object object) {
        return mQueue.removeMessages(this, what, null, object, false, true, false);
}

// if we can get rid of this method, the handler need not remember its loop








//Synthetic comment -- diff --git a/core/java/android/os/MessageQueue.java b/core/java/android/os/MessageQueue.java
//Synthetic comment -- index 0090177..913ce27 100644

//Synthetic comment -- @@ -227,13 +227,31 @@

final boolean removeMessages(Handler h, int what, Object object,
boolean doRemove) {
        return removeMessages(h, what, null, object, true, false, doRemove);
    }

    final void removeMessages(Handler h, Runnable r, Object object) {
        removeMessages(h, null, r, object, false, true, true);
    }

    final boolean removeMessages(Handler h, int what, Runnable r, Object object,
            boolean checkWhat, boolean checkRunnable, boolean doRemove) {
        if ((checkRunnable && r == null) || (!checkRunnable && !checkWhat)) {
            return false;
        }

synchronized (this) {
Message p = mMessages;
boolean found = false;

// Remove all messages at front.
            while (p != null && p.target == h && (object == null || p.obj == object)) {
                if (checkWhat && p.what != what) {
                    break;
                }
                if (checkRunnable && p.callback != r) {
                    break;
                }
if (!doRemove) return true;
found = true;
Message n = p.next;
//Synthetic comment -- @@ -246,8 +264,13 @@
while (p != null) {
Message n = p.next;
if (n != null) {
                    if (n.target == h && (object == null || n.obj == object)) {
                        if (checkWhat && n.what != what) {
                            continue;
                        }
                        if (checkRunnable && n.callback != r) {
                            continue;
                        }
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

final void removeCallbacksAndMessages(Handler h, Object object) {
synchronized (this) {
Message p = mMessages;







