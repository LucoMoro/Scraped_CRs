/*HandlerCaller: Fix to remove references in recycled args

Change-Id:I316980b80ff84f87e1089288e78260ea6346dfb6*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/os/HandlerCaller.java b/core/java/com/android/internal/os/HandlerCaller.java
//Synthetic comment -- index a94fb1e..62342b5 100644

//Synthetic comment -- @@ -45,6 +45,13 @@
public int argi4;
public int argi5;
public int argi6;
}

static final int ARGS_POOL_MAX_SIZE = 10;
//Synthetic comment -- @@ -96,6 +103,7 @@
public void recycleArgs(SomeArgs args) {
synchronized (mH) {
if (mArgsPoolSize < ARGS_POOL_MAX_SIZE) {
args.next = mArgsPool;
mArgsPool = args;
mArgsPoolSize++;







