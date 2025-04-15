/*Fix a bug where we cleaned an apps external data when upgrading it. :(

Change-Id:I0eee1e7062d334c66d6daa3c43e11a292263aada*/




//Synthetic comment -- diff --git a/services/java/com/android/server/PackageManagerService.java b/services/java/com/android/server/PackageManagerService.java
//Synthetic comment -- index 49d2a76..0b84c8d 100644

//Synthetic comment -- @@ -6222,11 +6222,10 @@
File dataDir = new File(pkg.applicationInfo.dataDir);
dataDir.delete();
}
            schedulePackageCleaning(packageName);
}
synchronized (mPackages) {
if (deletedPs != null) {
if ((flags&PackageManager.DONT_DELETE_DATA) == 0) {
if (outInfo != null) {
outInfo.removedUid = mSettings.removePackageLP(packageName);







