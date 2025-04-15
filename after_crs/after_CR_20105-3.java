/*Minor method rename in SdkManager UI.

Change-Id:I35447fe1ae9a21763207a07c0e205c1808f3b420*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalSdkAdapter.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalSdkAdapter.java
//Synthetic comment -- index 83aa0c9..f09e144 100755

//Synthetic comment -- @@ -93,7 +93,7 @@
*/
public Object[] getElements(Object inputElement) {
if (inputElement == LocalSdkAdapter.this) {
                Package[] packages = mUpdaterData.getInstalledPackages();

if (packages != null) {
return packages;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java
//Synthetic comment -- index e7fc74c..8722e02 100755

//Synthetic comment -- @@ -314,7 +314,7 @@
*/
private Package[] filterUpdateOnlyPackages(Package[] remotePackages) {
// get the installed packages
        Package[] installedPackages = mUpdaterData.getInstalledPackages();

ArrayList<Package> filteredList = new ArrayList<Package>();









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 0aa8a15..e611e15 100755

//Synthetic comment -- @@ -351,8 +351,11 @@

/**
* Returns the list of installed packages, parsing them if this has not yet been done.
     * <p/>
     * The package list is cached in the {@link LocalSdkParser} and will be reset when
     * {@link #reloadSdk()} is invoked.
*/
    public Package[] getInstalledPackages() {
LocalSdkParser parser = getLocalSdkParser();

Package[] packages = parser.getPackages();
//Synthetic comment -- @@ -391,7 +394,7 @@

// Mark all current local archives as already installed.
HashSet<Archive> installedArchives = new HashSet<Archive>();
                for (Package p : getInstalledPackages()) {
for (Archive a : p.getArchives()) {
installedArchives.add(a);
}







