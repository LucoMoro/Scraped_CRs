/*SdkManager: exclude obsolete packages from automatic update

Fixes for SDK Manager:
- Fix --filter flag for "update sdk --no-ui"
- Filter out obsolete packages when doing automatic sdk update.
- Respect the "Display Update Only" checkbox, which internally has
  always been to display non-obsolete updates, so again filter
  obsolete packages correctly when using the UI.
- Use the same code to filter obsolete packages in the No-UI mode.

Change-Id:I767c151370a09c18c2adcc0a11e267e973622399*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepository.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepository.java
//Synthetic comment -- index d29ec6f..723688c 100755

//Synthetic comment -- @@ -67,7 +67,7 @@
* List of possible nodes in a repository XML. Used to populate options automatically
* in the no-GUI mode.
* <p/>
     * **IMPORTANT**: if you edit this list, please also update the package-to-class map
* com.android.sdkuilib.internal.repository.UpdaterData.updateOrInstallAll_NoGUI().
*/
public static final String[] NODES = {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java
//Synthetic comment -- index f8a23bb..d701ea5 100755

//Synthetic comment -- @@ -256,7 +256,9 @@
}

private void onUpdateSelected() {
        mUpdaterData.updateOrInstallAll_WithGUI(
                null /*selectedArchives*/,
                false /* includeObsoletes */);
}

private void onDeleteSelected() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java
//Synthetic comment -- index defcd77..fe2734d 100755

//Synthetic comment -- @@ -139,7 +139,7 @@
mUpdateOnlyCheckBox = new Button(parent, SWT.CHECK);
mUpdateOnlyCheckBox.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
mUpdateOnlyCheckBox.setText("Display updates only");
        mUpdateOnlyCheckBox.setToolTipText("When selected, only compatible non-obsolete update packages are shown in the list above.");
mUpdateOnlyCheckBox.setSelection(mUpdaterData.getSettingsController().getShowUpdateOnly());
mUpdateOnlyCheckBox.addSelectionListener(new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -320,7 +320,9 @@
}

if (mUpdaterData != null) {
            mUpdaterData.updateOrInstallAll_WithGUI(
                    archives,
                    mUpdateOnlyCheckBox.getSelection() /* includeObsoletes */);
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java
//Synthetic comment -- index 199d75d..625cf3a 100755

//Synthetic comment -- @@ -202,7 +202,7 @@

// filter out only the packages that are new/upgrade.
if (packages != null && mUpdaterData.getSettingsController().getShowUpdateOnly()) {
                packages = filterUpdateOnlyPackages(packages);
}
if (packages != null && packages.length == 0) {
packages = null;
//Synthetic comment -- @@ -279,11 +279,12 @@

/**
* Filters out a list of remote packages to only keep the ones that are either new or
     * updates of existing package. This also removes obsolete packages.
     *
* @param remotePackages the list of packages to filter.
* @return a non null (but maybe empty) list of new or update packages.
*/
    private Package[] filterUpdateOnlyPackages(Package[] remotePackages) {
// get the installed packages
Package[] installedPackages = mUpdaterData.getInstalledPackage();

//Synthetic comment -- @@ -295,7 +296,7 @@
for (Package remotePkg : remotePackages) {
boolean newPkg = true;

            // Obsolete packages are not offered as updates.
if (remotePkg.isObsolete()) {
continue;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index e2e503e..4d10b54 100755

//Synthetic comment -- @@ -54,6 +54,7 @@

/**
* Returns the value of the {@link ISettingsPage#KEY_FORCE_HTTP} setting.
     *
* @see ISettingsPage#KEY_FORCE_HTTP
*/
public boolean getForceHttp() {
//Synthetic comment -- @@ -62,6 +63,7 @@

/**
* Returns the value of the {@link ISettingsPage#KEY_ASK_ADB_RESTART} setting.
     *
* @see ISettingsPage#KEY_ASK_ADB_RESTART
*/
public boolean getAskBeforeAdbRestart() {
//Synthetic comment -- @@ -74,6 +76,7 @@

/**
* Returns the value of the {@link ISettingsPage#KEY_SHOW_UPDATE_ONLY} setting.
     *
* @see ISettingsPage#KEY_SHOW_UPDATE_ONLY
*/
public boolean getShowUpdateOnly() {
//Synthetic comment -- @@ -86,7 +89,8 @@

/**
* Sets the value of the {@link ISettingsPage#KEY_SHOW_UPDATE_ONLY} setting.
     *
     * @param enabled True if only compatible non-obsolete update items should be shown.
* @see ISettingsPage#KEY_SHOW_UPDATE_ONLY
*/
public void setShowUpdateOnly(boolean enabled) {
//Synthetic comment -- @@ -112,6 +116,7 @@

/**
* Sets the value of the {@link ISettingsPage#KEY_MONITOR_DENSITY} setting.
     *
* @param density the density of the monitor
* @see ISettingsPage#KEY_MONITOR_DENSITY
*/








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index fdba9c8..af81977 100755

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.sdklib.internal.repository.LocalSdkParser;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.PlatformPackage;
import com.android.sdklib.internal.repository.PlatformToolPackage;
import com.android.sdklib.internal.repository.RepoSource;
import com.android.sdklib.internal.repository.RepoSources;
import com.android.sdklib.internal.repository.SamplePackage;
//Synthetic comment -- @@ -593,7 +594,9 @@
*  This can be null, in which case a list of remote archive is fetched from all
*  available sources.
*/
    public void updateOrInstallAll_WithGUI(
            Collection<Archive> selectedArchives,
            boolean includeObsoletes) {
if (selectedArchives == null) {
refreshSources(true);
}
//Synthetic comment -- @@ -602,10 +605,15 @@
ArrayList<ArchiveInfo> archives = ul.computeUpdates(
selectedArchives,
getSources(),
                getLocalSdkParser().getPackages(),
                includeObsoletes);

if (selectedArchives == null) {
            ul.addNewPlatforms(
                    archives,
                    getSources(),
                    getLocalSdkParser().getPackages(),
                    includeObsoletes);
}

// TODO if selectedArchives is null and archives.len==0, find if there are
//Synthetic comment -- @@ -642,21 +650,27 @@
ArrayList<ArchiveInfo> archives = ul.computeUpdates(
null /*selectedArchives*/,
getSources(),
                getLocalSdkParser().getPackages(),
                includeObsoletes);

        ul.addNewPlatforms(
                archives,
                getSources(),
                getLocalSdkParser().getPackages(),
                includeObsoletes);

// Filter the selected archives to only keep the ones matching the filter
if (pkgFilter != null && pkgFilter.size() > 0 && archives != null && archives.size() > 0) {
// Map filter types to an SdkRepository Package type.
HashMap<String, Class<? extends Package>> pkgMap =
new HashMap<String, Class<? extends Package>>();
            pkgMap.put(SdkRepository.NODE_PLATFORM,         PlatformPackage.class);
            pkgMap.put(SdkRepository.NODE_ADD_ON,           AddonPackage.class);
            pkgMap.put(SdkRepository.NODE_TOOL,             ToolPackage.class);
            pkgMap.put(SdkRepository.NODE_PLATFORM_TOOL,    PlatformToolPackage.class);
            pkgMap.put(SdkRepository.NODE_DOC,              DocPackage.class);
            pkgMap.put(SdkRepository.NODE_SAMPLE,           SamplePackage.class);
            pkgMap.put(SdkRepository.NODE_EXTRA,            ExtraPackage.class);

if (SdkRepository.NODES.length != pkgMap.size()) {
// Sanity check in case we forget to update this package map.
//Synthetic comment -- @@ -698,40 +712,12 @@
}

if (archives.size() == 0) {
                mSdkLog.printf("The package filter removed all packages. There is nothing to install.\n" +
                        "Please consider trying updating again without a package filter.\n");
return;
}
}

if (archives != null && archives.size() > 0) {
if (dryMode) {
mSdkLog.printf("Packages selected for install:\n");








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index 85f727f..81a11a2 100755

//Synthetic comment -- @@ -59,7 +59,8 @@
public ArrayList<ArchiveInfo> computeUpdates(
Collection<Archive> selectedArchives,
RepoSources sources,
            Package[] localPkgs,
            boolean includeObsoletes) {

ArrayList<ArchiveInfo> archives = new ArrayList<ArchiveInfo>();
ArrayList<Package> remotePkgs = new ArrayList<Package>();
//Synthetic comment -- @@ -69,7 +70,11 @@
ArchiveInfo[] localArchives = createLocalArchives(localPkgs);

if (selectedArchives == null) {
            selectedArchives = findUpdates(
                    localArchives,
                    remotePkgs,
                    remoteSources,
                    includeObsoletes);
}

for (Archive a : selectedArchives) {
//Synthetic comment -- @@ -89,9 +94,11 @@
* Finds new packages that the user does not have in his/her local SDK
* and adds them to the list of archives to install.
*/
    public void addNewPlatforms(
            ArrayList<ArchiveInfo> archives,
RepoSources sources,
            Package[] localPkgs,
            boolean includeObsoletes) {

// Create ArchiveInfos out of local (installed) packages.
ArchiveInfo[] localArchives = createLocalArchives(localPkgs);
//Synthetic comment -- @@ -137,6 +144,11 @@
Package suggestedDoc = null;

for (Package p : remotePkgs) {
            // Skip obsolete packages unless requested to include them.
            if (p.isObsolete() && !includeObsoletes) {
                continue;
            }

int rev = p.getRevision();
int api = 0;
boolean isPreview = false;
//Synthetic comment -- @@ -233,9 +245,11 @@
/**
* Find suitable updates to all current local packages.
*/
    private Collection<Archive> findUpdates(
            ArchiveInfo[] localArchives,
ArrayList<Package> remotePkgs,
            RepoSource[] remoteSources,
            boolean includeObsoletes) {
ArrayList<Archive> updates = new ArrayList<Archive>();

fetchRemotePackages(remotePkgs, remoteSources);
//Synthetic comment -- @@ -248,9 +262,11 @@
Package localPkg = na.getParentPackage();

for (Package remotePkg : remotePkgs) {
                // Only look for non-obsolete updates unless requested to include them
                if ((includeObsoletes || !remotePkg.isObsolete()) &&
                        localPkg.canBeUpdatedBy(remotePkg) == UpdateInfo.UPDATE) {
// Found a suitable update. Only accept the remote package
                    // if it provides at least one compatible archive

for (Archive a : remotePkg.getArchives()) {
if (a.isCompatible()) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index f7c9a96..574357f 100755

//Synthetic comment -- @@ -292,7 +292,9 @@
mUpdaterData.notifyListeners(true /*init*/);

if (mRequestAutoUpdate) {
            mUpdaterData.updateOrInstallAll_WithGUI(
                    null /*selectedArchives*/,
                    false /* includeObsoletes */);
}

return true;







