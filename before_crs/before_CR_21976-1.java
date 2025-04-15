/*Avoid Runnables and Messages's conflict.
Because Runnables internally are same as Messages that what field is 0,
it can be conflicted with Messages that field is 0.
For that, makes all Runnables use what field as -2^32.

Change-Id:I6d3b5c5640d265e6cd3ef43267ddac60656283a1*/
//Synthetic comment -- diff --git a/core/java/android/os/Handler.java b/core/java/android/os/Handler.java
//Synthetic comment -- index 3b2bf1e..a12e663 100644

//Synthetic comment -- @@ -71,6 +71,12 @@
private static final String TAG = "Handler";

/**
* Callback interface you can use when instantiating a Handler to avoid
* having to implement your own subclass of Handler.
*/
//Synthetic comment -- @@ -572,12 +578,14 @@

private final Message getPostMessage(Runnable r) {
Message m = Message.obtain();
m.callback = r;
return m;
}

private final Message getPostMessage(Runnable r, Object token) {
Message m = Message.obtain();
m.obj = token;
m.callback = r;
return m;







