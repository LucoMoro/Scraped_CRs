/*Seperate Messages with/without callback.

Because Runnable is internally Message with Callback that what field is
'0', it can be conflicted with general Messages that 'what' field is
'0'.
To avoid it, seperate Messages and Runnables by checking Message's
callback field.

Change-Id:I6d3b5c5640d265e6cd3ef43267ddac60656283a1*/
//Synthetic comment -- diff --git a/core/java/android/os/MessageQueue.java b/core/java/android/os/MessageQueue.java
//Synthetic comment -- index 0090177..f37d29f 100644

//Synthetic comment -- @@ -232,7 +232,7 @@
boolean found = false;

// Remove all messages at front.
            while (p != null && p.target == h && p.what == what
&& (object == null || p.obj == object)) {
if (!doRemove) return true;
found = true;
//Synthetic comment -- @@ -246,7 +246,7 @@
while (p != null) {
Message n = p.next;
if (n != null) {
                    if (n.target == h && n.what == what
&& (object == null || n.obj == object)) {
if (!doRemove) return true;
found = true;







