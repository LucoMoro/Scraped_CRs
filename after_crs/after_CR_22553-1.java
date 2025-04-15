/*Fix for PackageManagerService.moveDexFilesLI(). Add condition mInstaller!=null to avoid NullPointerException.
Signed-off-by: Kai Wei <kai.wei.cn@gmail.com>

Change-Id:Idf31eccf4e291929cbf570b40e047852dc31674d*/




//Synthetic comment -- diff --git a/services/java/com/android/server/PackageManagerService.java b/services/java/com/android/server/PackageManagerService.java
//Synthetic comment -- index 8c74566..2241fe3 100644

//Synthetic comment -- @@ -5708,7 +5708,7 @@
// Utility method used to move dex files during install.
private int moveDexFilesLI(PackageParser.Package newPackage) {
int retCode;
        if ((newPackage.applicationInfo.flags&ApplicationInfo.FLAG_HAS_CODE) != 0 && mInstaller != null) {
retCode = mInstaller.movedex(newPackage.mScanPath, newPackage.mPath);
if (retCode != 0) {
if (mNoDexOpt) {







