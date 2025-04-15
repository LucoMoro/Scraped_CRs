/*SDK Manager: fix extra package update detection.

(cherry picked from commit fcb38f8f3ee58bbac65340c4878f8ab66431ddb6)

Change-Id:I44eeeb06dd406beb8ab53288da7b1eca3f74edad*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java
//Synthetic comment -- index 49236dc..3472544 100755

//Synthetic comment -- @@ -394,19 +394,33 @@
ExtraPackage ep = (ExtraPackage) pkg;

// To be backward compatible, we need to support the old vendor-path form
            if (ep.mPath != null && (ep.mVendor == null || ep.mVendor.length() == 0) &&
                    mPath != null && mVendor != null) {
                if (ep.mPath.equals(mVendor + "-" + mPath)) {  //$NON-NLS-1$
return true;
}
}

if (!mPath.equals(ep.mPath)) {
return false;
}
if ((mVendor == null && ep.mVendor == null) ||
                (mVendor != null && !mVendor.equals(ep.mVendor))) {
                return false;
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java
//Synthetic comment -- index 8722e02..54bc068 100755

//Synthetic comment -- @@ -316,13 +316,13 @@
// get the installed packages
Package[] installedPackages = mUpdaterData.getInstalledPackages();

ArrayList<Package> filteredList = new ArrayList<Package>();

// for each remote packages, we look for an existing version.
// If no existing version -> add to the list
// if existing version but with older revision -> add it to the list
for (Package remotePkg : remotePackages) {
            boolean newPkg = true;

// Obsolete packages are not offered as updates.
if (remotePkg.isObsolete()) {
//Synthetic comment -- @@ -332,20 +332,27 @@
// For all potential packages, we also make sure that there's an archive for
// the current platform, or we simply skip them.
if (remotePkg.hasCompatibleArchive()) {
                for (Package installedPkg : installedPackages) {
UpdateInfo info = installedPkg.canBeUpdatedBy(remotePkg);
                    if (info == UpdateInfo.UPDATE) {
                        filteredList.add(remotePkg);
                        newPkg = false;
                        break; // there shouldn't be 2 revisions of the same package
                    } else if (info != UpdateInfo.INCOMPATIBLE) {
                        newPkg = false;
                        break; // there shouldn't be 2 revisions of the same package
}
}

                // if we have not found the same package, then we add it (it's a new package)
                if (newPkg) {
filteredList.add(remotePkg);
}
}







