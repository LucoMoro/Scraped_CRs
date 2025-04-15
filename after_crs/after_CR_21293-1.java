/*SDK Manager: fix extra package update detection.

Change-Id:Ie512a8cc75075987d1eb88dafd079e52083e2a3c*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java
//Synthetic comment -- index 49236dc..3472544 100755

//Synthetic comment -- @@ -394,19 +394,33 @@
ExtraPackage ep = (ExtraPackage) pkg;

// To be backward compatible, we need to support the old vendor-path form
            // in either the current or the remote package.
            //
            // The vendor test below needs to account for an old installed package
            // (e.g. with an install path of vendor-name) that has then beeen updated
            // in-place and thus when reloaded contains the vendor name in both the
            // path and the vendor attributes.
            if (ep.mPath != null && mPath != null && mVendor != null) {
                if (ep.mPath.equals(mVendor + "-" + mPath) &&  //$NON-NLS-1$
                        (ep.mVendor == null || ep.mVendor.length() == 0
                                || ep.mVendor.equals(mVendor))) {
return true;
}
}
            if (mPath != null && ep.mPath != null && ep.mVendor != null) {
                if (mPath.equals(ep.mVendor + "-" + ep.mPath) &&  //$NON-NLS-1$
                        (mVendor == null || mVendor.length() == 0 || mVendor.equals(ep.mVendor))) {
                    return true;
                }
            }


if (!mPath.equals(ep.mPath)) {
return false;
}
if ((mVendor == null && ep.mVendor == null) ||
                (mVendor != null && mVendor.equals(ep.mVendor))) {
                return true;
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java
//Synthetic comment -- index 8722e02..54bc068 100755

//Synthetic comment -- @@ -316,13 +316,13 @@
// get the installed packages
Package[] installedPackages = mUpdaterData.getInstalledPackages();

        // we'll populate this package list with either upgrades or new packages.
ArrayList<Package> filteredList = new ArrayList<Package>();

// for each remote packages, we look for an existing version.
// If no existing version -> add to the list
// if existing version but with older revision -> add it to the list
for (Package remotePkg : remotePackages) {

// Obsolete packages are not offered as updates.
if (remotePkg.isObsolete()) {
//Synthetic comment -- @@ -332,20 +332,27 @@
// For all potential packages, we also make sure that there's an archive for
// the current platform, or we simply skip them.
if (remotePkg.hasCompatibleArchive()) {
                boolean keepPkg = true;

                nextPkg: for (Package installedPkg : installedPackages) {
UpdateInfo info = installedPkg.canBeUpdatedBy(remotePkg);
                    switch(info) {
                    case UPDATE:
                        // The remote package is an update to an existing one.
                        // We're done looking.
                        keepPkg = true;
                        break nextPkg;
                    case NOT_UPDATE:
                        // The remote package is the same as one that is already installed.
                        keepPkg = false;
                        break;
                    case INCOMPATIBLE:
                        // We can't compare and decide on incompatible things.
                        break;
}
}

                if (keepPkg) {
filteredList.add(remotePkg);
}
}







