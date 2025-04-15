/*SDK Manager: refuse to not install dependencies.

In the case the SDK Manager found a package that would fix
a broken dependency of the currently installed packages
(e.g. user has Tools but not Platform-Tools), it will refuse
to not install the package that would fix that dependency.

Bug: 3092907
Change-Id:Iea21c69d726b7c859bfbbffc74ba61b690c4afe0*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java
//Synthetic comment -- index 038b86a..904928d 100755

//Synthetic comment -- @@ -108,10 +108,12 @@
* Adds an {@link ArchiveInfo} for which <em>this</em> package is a dependency.
* This means the package added here depends on this package.
*/
    public void addDependencyFor(ArchiveInfo dependencyFor) {
if (!mDependencyFor.contains(dependencyFor)) {
mDependencyFor.add(dependencyFor);
}
}

public Collection<ArchiveInfo> getDependenciesFor() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateChooserDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateChooserDialog.java
//Synthetic comment -- index 2f64edd..114e5e8 100755

//Synthetic comment -- @@ -519,10 +519,12 @@
}
}

            // If there's no selection, just find the first missing dependency of any accepted
            // package.
for (ArchiveInfo ai2 : mArchives) {
if (ai2.isAccepted()) {
ArchiveInfo[] adeps = ai2.getDependsOn();
if (adeps != null) {
for (ArchiveInfo adep : adeps) {
//Synthetic comment -- @@ -534,6 +536,20 @@
}
}
}
}
}
} finally {
//Synthetic comment -- @@ -676,6 +692,7 @@
// update state
mLicenseAcceptAll = false;
mTableViewPackage.refresh(ai);
updateLicenceRadios(ai);
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 8d6a253..dde2f40 100755

//Synthetic comment -- @@ -613,9 +613,6 @@
public void updateOrInstallAll_WithGUI(
Collection<Archive> selectedArchives,
boolean includeObsoletes) {
        if (selectedArchives == null) {
            refreshSources(true);
        }

UpdaterLogic ul = new UpdaterLogic(this);
ArrayList<ArchiveInfo> archives = ul.computeUpdates(








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index db5c99c..3ccc01b 100755

//Synthetic comment -- @@ -344,12 +344,21 @@

// find dependencies for the local archive and add them as needed
// to the outArchives collection.
            findDependency(p,
outArchives,
selectedArchives,
remotePkgs,
remoteSources,
localArchives);
}
}








