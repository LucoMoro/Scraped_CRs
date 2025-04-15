/*Avoid crash in system server when mounting container

A race condition when mounting a container in PackageHelper may cause
the system_server to crash (uncaught exception). Calling methods are
prepared to handle null, so return null instead.

Change-Id:I852ee21a2d847e37d81c1b900c27ddf94ef24fcb*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/content/PackageHelper.java b/core/java/com/android/internal/content/PackageHelper.java
//Synthetic comment -- index d6c43f9..41e5c15 100644

//Synthetic comment -- @@ -96,6 +96,8 @@
return getMountService().getSecureContainerPath(cid);
} catch (RemoteException e) {
Log.e(TAG, "MountService running?");
}
return null;
}








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/content/pm/PackageHelperTests.java b/core/tests/coretests/src/android/content/pm/PackageHelperTests.java
new file mode 100644
//Synthetic comment -- index 0000000..27112a6

//Synthetic comment -- @@ -0,0 +1,131 @@







