/*Fix AP being downgrade after FOTA

Why: The AP doesn't exist in /system/app will ignore version check
     before change code path
How: Examine the version code before change code path whatever
Verify steps: N/A
Possible effects: NONE

Change-Id:If0dcf45344f0e5841a0208a3c7bad26cd10d49a7*/
//Synthetic comment -- diff --git a/services/java/com/android/server/pm/PackageManagerService.java b/services/java/com/android/server/pm/PackageManagerService.java
//Synthetic comment -- index 6b61c47..1459471 100644

//Synthetic comment -- @@ -2871,41 +2871,42 @@
updatedPkg = mSettings.mDisabledSysPackages.get(
ps != null ? ps.name : pkg.packageName);
}
// First check if this is a system package that may involve an update
if (updatedPkg != null && (parseFlags&PackageParser.PARSE_IS_SYSTEM) != 0) {
if (ps != null && !ps.codePath.equals(scanFile)) {
                // The path has changed from what was last scanned...  check the
                // version of the new path against what we have stored to determine
                // what to do.
                if (pkg.mVersionCode < ps.versionCode) {
                    // The system package has been updated and the code path does not match
                    // Ignore entry. Skip it.
                    Log.i(TAG, "Package " + ps.name + " at " + scanFile
                            + " ignored: updated version " + ps.versionCode
                            + " better than this " + pkg.mVersionCode);
                    mLastScanError = PackageManager.INSTALL_FAILED_DUPLICATE_PACKAGE;
                    return null;
                } else {
                    // The current app on the system partion is better than
                    // what we have updated to on the data partition; switch
                    // back to the system partition version.
                    // At this point, its safely assumed that package installation for
                    // apps in system partition will go through. If not there won't be a working
                    // version of the app
                    // writer
                    synchronized (mPackages) {
                        // Just remove the loaded entries from package lists.
                        mPackages.remove(ps.name);
                    }
                    Slog.w(TAG, "Package " + ps.name + " at " + scanFile
                            + "reverting from " + ps.codePathString
                            + ": new version " + pkg.mVersionCode
                            + " better than installed " + ps.versionCode);
                    InstallArgs args = new FileInstallArgs(ps.codePathString,
                            ps.resourcePathString, ps.nativeLibraryPathString);
                    args.cleanUpResourcesLI();
                    mSettings.enableSystemPackageLPw(ps.name);
}
}
}
if (updatedPkg != null) {







