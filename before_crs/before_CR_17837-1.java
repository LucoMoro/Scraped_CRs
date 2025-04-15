/*Verify preinstalled apps don't have debuggable set.

Fix bug 3065312.  It is a security hole for applications to
ship with debuggable set.  Make sure we warn people about this.

Change-Id:Ie91d2479aa4512804ca2a9c4847c1abd40566f03*/
//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/DebuggableTest.java b/tests/tests/permission/src/android/permission/cts/DebuggableTest.java
new file mode 100644
//Synthetic comment -- index 0000000..fe4ed57

//Synthetic comment -- @@ -0,0 +1,42 @@







