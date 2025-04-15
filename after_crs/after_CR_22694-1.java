/*PackageManager: Restrict system packages to protected storage

Don't allow packages signed with the platform key to be
installed into user (or app) controlled storage

Change-Id:I37b72d982fd7536c327061788fc785f7cad3ce16*/




//Synthetic comment -- diff --git a/services/java/com/android/server/PackageManagerService.java b/services/java/com/android/server/PackageManagerService.java
//Synthetic comment -- index 8c74566..4c9ee90 100644

//Synthetic comment -- @@ -2874,6 +2874,20 @@
return null;
}

        if (!pkg.applicationInfo.sourceDir.startsWith(Environment.getRootDirectory().getPath()) &&
                !pkg.applicationInfo.sourceDir.startsWith("/vendor")) {
            Object obj = mSettings.getUserIdLP(1000);
            Signature[] s1 = null;
            if (obj instanceof SharedUserSetting) {
                s1 = ((SharedUserSetting)obj).signatures.mSignatures;
            }
            if ((checkSignaturesLP(pkg.mSignatures, s1) == PackageManager.SIGNATURE_MATCH)) {
                Slog.w(TAG, "Cannot install platform packages to user storage");
                mLastScanError = PackageManager.INSTALL_FAILED_INVALID_INSTALL_LOCATION;
                return null;
            }
        }

// Initialize package source and resource directories
File destCodeFile = new File(pkg.applicationInfo.sourceDir);
File destResourceFile = new File(pkg.applicationInfo.publicSourceDir);







