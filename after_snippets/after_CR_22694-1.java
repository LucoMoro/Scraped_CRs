
//<Beginning of snippet n. 0>


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

//<End of snippet n. 0>








