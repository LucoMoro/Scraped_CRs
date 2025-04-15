/*Fix a couple of NPEs in PackageManagerService.*/




//Synthetic comment -- diff --git a/services/java/com/android/server/PackageManagerService.java b/services/java/com/android/server/PackageManagerService.java
//Synthetic comment -- index 89f854e..2449777 100644

//Synthetic comment -- @@ -1871,7 +1871,7 @@
scanMode |= SCAN_FORWARD_LOCKED;
}
File resFile = destResourceFile;
        if (ps != null && (scanMode & SCAN_FORWARD_LOCKED) != 0) {
resFile = getFwdLockedResource(ps.name);
}
// Note that we invoke the following method only if we are about to unpack an application
//Synthetic comment -- @@ -1967,6 +1967,11 @@
File scanFile, File destCodeFile, File destResourceFile,
PackageParser.Package pkg, int parseFlags, int scanMode) {

        if (destResourceFile == null) {
            mLastScanError = PackageManager.INSTALL_FAILED_INVALID_APK;
            return null;
        }

mScanningPath = scanFile;
if (pkg == null) {
mLastScanError = PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
//Synthetic comment -- @@ -5521,7 +5526,7 @@
this.codePath = codePath;
this.codePathString = codePath.toString();
this.resourcePath = resourcePath;
            this.resourcePathString = resourcePath == null ? null : resourcePath.toString();
this.versionCode = pVersionCode;
}








