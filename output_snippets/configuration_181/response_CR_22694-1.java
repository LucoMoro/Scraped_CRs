//<Beginning of snippet n. 0>

if (!isPackageSignedWithPlatformKey(pkg)) {
    throw new SecurityException("Package not signed with platform key, installation denied.");
}

// Initialize package source and resource directories
File destCodeFile = new File(pkg.applicationInfo.sourceDir);
File destResourceFile = new File(pkg.applicationInfo.publicSourceDir);

// Error handling
if (!canInstallInUserStorage(pkg)) {
    throw new SecurityException("Installation in user/app-controlled storage is not allowed for this package.");
}

//<End of snippet n. 0>