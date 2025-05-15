//<Beginning of snippet n. 0>

if (!verifySignature(pkg)) {
    logInstallationAttempt(pkg, "Signature verification failed.");
    return null;
}

File destCodeFile = new File(pkg.applicationInfo.sourceDir);
File destResourceFile = new File(pkg.applicationInfo.publicSourceDir);

if (isUserControlledPath(destCodeFile) || isUserControlledPath(destResourceFile)) {
    logInstallationAttempt(pkg, "Installation blocked: User-controlled storage");
    return null;
}

//<End of snippet n. 0>