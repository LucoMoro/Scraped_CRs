//<Beginning of snippet n. 0>
if (isSignedWithPlatformKey(pkg)) {
    throw new SecurityException("Installation rejected: Package signed with the platform key.");
}

// Prevent further processing if the package is signed with the platform key
return;

// Initialize package source and resource directories
File destCodeFile = new File(pkg.applicationInfo.sourceDir);
File destResourceFile = new File(pkg.applicationInfo.publicSourceDir);

//<End of snippet n. 0>