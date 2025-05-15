//<Beginning of snippet n. 0>

if (!isPackageSignedWithPlatformKey(pkg)) {
    // Initialize package source and resource directories
    File destCodeFile = new File(pkg.applicationInfo.sourceDir);
    File destResourceFile = new File(pkg.applicationInfo.publicSourceDir);
} else {
    // Log or throw exception for attempting to install a platform-key-signed package in user-controlled storage
    logError("Attempted installation of package signed with platform key, redirecting to protected storage.");
    redirectToProtectedStorage(pkg);
}

return null;

//<End of snippet n. 0>