/*Avoid crash in system server when mounting container

A race condition when mounting a container in PackageHelper may cause
the system_server to crash (uncaught exception). Calling methods are
prepared to handle null, so return null instead.

Change-Id:I852ee21a2d847e37d81c1b900c27ddf94ef24fcb*/
//Synthetic comment -- diff --git a/core/tests/coretests/src/android/content/pm/PackageHelperTests.java b/core/tests/coretests/src/android/content/pm/PackageHelperTests.java
new file mode 100644
//Synthetic comment -- index 0000000..27112a6

//Synthetic comment -- @@ -0,0 +1,131 @@








//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index e6e3e5f..4d934b6 100644

//Synthetic comment -- @@ -1509,7 +1509,8 @@
} catch (NativeDaemonConnectorException e) {
int code = e.getCode();
if (code == VoldResponseCode.OpFailedStorageNotFound) {
                throw new IllegalArgumentException(String.format("Container '%s' not found", id));
} else {
throw new IllegalStateException(String.format("Unexpected response code %d", code));
}







