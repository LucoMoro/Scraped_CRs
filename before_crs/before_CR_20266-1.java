/*Merge: Minor method rename in SdkManager UI.

Change-Id:I723a97aaff7f67507007806391b494998d99f058*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalSdkAdapter.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalSdkAdapter.java
//Synthetic comment -- index 444e998..f430381 100755

//Synthetic comment -- @@ -94,7 +94,7 @@
*/
public Object[] getElements(Object inputElement) {
if (inputElement == LocalSdkAdapter.this) {
                Package[] packages = mUpdaterData.getInstalledPackage();

if (packages != null) {
return packages;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java
//Synthetic comment -- index 05d0d43..64fb7c7 100755

//Synthetic comment -- @@ -314,7 +314,7 @@
*/
private Package[] filterUpdateOnlyPackages(Package[] remotePackages) {
// get the installed packages
        Package[] installedPackages = mUpdaterData.getInstalledPackage();

ArrayList<Package> filteredList = new ArrayList<Package>();









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index e569eba..33d756a 100755

//Synthetic comment -- @@ -353,8 +353,11 @@

/**
* Returns the list of installed packages, parsing them if this has not yet been done.
*/
    public Package[] getInstalledPackage() {
LocalSdkParser parser = getLocalSdkParser();

Package[] packages = parser.getPackages();
//Synthetic comment -- @@ -416,7 +419,7 @@

// Mark all current local archives as already installed.
HashSet<Archive> installedArchives = new HashSet<Archive>();
                for (Package p : getInstalledPackage()) {
for (Archive a : p.getArchives()) {
installedArchives.add(a);
}







