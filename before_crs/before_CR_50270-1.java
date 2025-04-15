/*load BackupAgents with shared libraries

When loading BackupAgents, add shared libraries to the class path.

For example, let's suppose browser contains "A" library using <uses-library>.
During backup or after backup, if access browser, ClassNotFoundException
occurs on and off. Because "A" library is not linked, unexpectedly.
To fix it, add shared libraries to the class path as loading backup agent.

Change-Id:I1a51e485af2748c57cb6a29ed50a0246bb820af6*/
//Synthetic comment -- diff --git a/services/java/com/android/server/BackupManagerService.java b/services/java/com/android/server/BackupManagerService.java
//Synthetic comment -- index 7ac314b..3d87328 100644

//Synthetic comment -- @@ -1976,7 +1976,7 @@
// package's backup agent.
try {
mCurrentPackage = mPackageManager.getPackageInfo(request.packageName,
                        PackageManager.GET_SIGNATURES);
if (mCurrentPackage.applicationInfo.backupAgentName == null) {
// The manifest has changed but we had a stale backup request pending.
// This won't happen again because the app won't be requesting further







