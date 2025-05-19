//<Beginning of snippet n. 0>
if (pkg.signatures != null && pkg.signatures.length > 0) {
    for (Signature signature : pkg.signatures) {
        if (isSignedWithPlatformKey(signature)) {
            return "Installation denied: The package is signed with the platform key due to security concerns.";
        }
    }
}

// Initialize package source and resource directories
File destCodeFile = new File(pkg.applicationInfo.sourceDir);
File destResourceFile = new File(pkg.applicationInfo.publicSourceDir);
//<End of snippet n. 0>