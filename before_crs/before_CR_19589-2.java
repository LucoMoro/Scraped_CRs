/*Closing cursor in finalizer to avoid GREF and fd leak in acore

The finalize() call did not clean up completely, this eventually
caused the android.process.acore to crash since it ran out of fds
and GREF to increased above 2000 if an application forgot to close
its cursor objects. A warning was also added when this happens so
that application developers can correct their mistake. The
included test case tries to verify that the finalizer works as
expected by creating a bunch of Cursor objects without closing
them (without this fix the acore process crashes after about 400
iterations and the test case ends with "Process crashed").

Change-Id:I11e485cef1ac02e718b2742108aa88793666c31d*/
//Synthetic comment -- diff --git a/core/java/android/content/ContentResolver.java b/core/java/android/content/ContentResolver.java
//Synthetic comment -- index 1f3426e..d16b3d8 100644

//Synthetic comment -- @@ -1398,9 +1398,11 @@

@Override
protected void finalize() throws Throwable {
try {
if(!mCloseFlag) {
                    ContentResolver.this.releaseProvider(mContentProvider);
}
} finally {
super.finalize();








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/content/ContentResolverTest.java b/core/tests/coretests/src/android/content/ContentResolverTest.java
new file mode 100644
//Synthetic comment -- index 0000000..2b6dee8b

//Synthetic comment -- @@ -0,0 +1,41 @@







