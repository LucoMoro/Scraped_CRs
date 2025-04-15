/*SDK Manager: refuse to not install dependencies.

In the case the SDK Manager found a package that would fix
a broken dependency of the currently installed packages
(e.g. user has Tools but not Platform-Tools), it will refuse
to not install the package that would fix that dependency.

Bug: 3092907

Change-Id:Iea21c69d726b7c859bfbbffc74ba61b690c4afe0*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java
//Synthetic comment -- index 038b86a..15560b7 100755

//Synthetic comment -- @@ -90,6 +90,7 @@
* archive depends upon. In other words, we can only install this archive if the
* dependency has been successfully installed. It also means we need to install the
* dependency first.
     * <p/>
* This array can be null or empty. It can't contain nulls though.
*/
public ArchiveInfo[] getDependsOn() {
//Synthetic comment -- @@ -108,12 +109,21 @@
* Adds an {@link ArchiveInfo} for which <em>this</em> package is a dependency.
* This means the package added here depends on this package.
*/
    public ArchiveInfo addDependencyFor(ArchiveInfo dependencyFor) {
if (!mDependencyFor.contains(dependencyFor)) {
mDependencyFor.add(dependencyFor);
}

        return this;
}

    /**
     * Returns the list of {@link ArchiveInfo} for which <em>this</em> package is a dependency.
     * This means the packages listed here depend on this package.
     * <p/>
     * Implementation detail: this is the internal mutable list. Callers should not modify it.
     * This list can be empty but is never null.
     */
public Collection<ArchiveInfo> getDependenciesFor() {
return mDependencyFor;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateChooserDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateChooserDialog.java
//Synthetic comment -- index 2f64edd..277a9c2 100755

//Synthetic comment -- @@ -484,27 +484,17 @@
}

/**
     * Computes and displays missing dependencies.
     *
* If there's a selected package, check the dependency for that one.
     * Otherwise display the first missing dependency of any other package.
*/
private void displayMissingDependency(ArchiveInfo ai) {
String error = null;

try {
if (ai != null) {
                if (ai.isAccepted()) {
// Case where this package is accepted but blocked by another non-accepted one
ArchiveInfo[] adeps = ai.getDependsOn();
if (adeps != null) {
//Synthetic comment -- @@ -516,13 +506,29 @@
}
}
}
                } else {
                    // Case where this package blocks another one when not accepted
                    for (ArchiveInfo adep : ai.getDependenciesFor()) {
                        // It only matters if the blocked one is accepted
                        if (adep.isAccepted()) {
                            error = String.format("Package '%1$s' depends on this one.",
                                    adep.getShortDescription());
                            return;
                        }
                    }
}
}

            // If there is no missing dependency on the current selection,
            // just find the first missing dependency of any other package.
for (ArchiveInfo ai2 : mArchives) {
                if (ai2 == ai) {
                    // We already processed that one above.
                    continue;
                }
if (ai2.isAccepted()) {
                    // The user requested to install this package.
                    // Check if all its dependencies are met.
ArchiveInfo[] adeps = ai2.getDependsOn();
if (adeps != null) {
for (ArchiveInfo adep : adeps) {
//Synthetic comment -- @@ -534,6 +540,20 @@
}
}
}
                } else {
                    // The user did not request to install this package.
                    // Check whether this package blocks another one when not accepted.
                    for (ArchiveInfo adep : ai2.getDependenciesFor()) {
                        // It only matters if the blocked one is accepted
                        // or if it's a local archive that is already installed (these
                        // are marked as implicitly accepted, so it's the same test.)
                        if (adep.isAccepted()) {
                            error = String.format("Package '%1$s' depends on '%2$s'",
                                    adep.getShortDescription(),
                                    ai2.getShortDescription());
                            return;
                        }
                    }
}
}
} finally {
//Synthetic comment -- @@ -676,6 +696,7 @@
// update state
mLicenseAcceptAll = false;
mTableViewPackage.refresh(ai);
        displayMissingDependency(ai);
updateLicenceRadios(ai);
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 8d6a253..c9d482a 100755

//Synthetic comment -- @@ -613,9 +613,10 @@
public void updateOrInstallAll_WithGUI(
Collection<Archive> selectedArchives,
boolean includeObsoletes) {

        // Note: we not longer call refreshSources(true) here. This will be done
        // automatically by computeUpdates() iif it needs to access sources to
        // resolve missing dependencies.

UpdaterLogic ul = new UpdaterLogic(this);
ArrayList<ArchiveInfo> archives = ul.computeUpdates(








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index db5c99c..4380ca8 100755

//Synthetic comment -- @@ -344,12 +344,21 @@

// find dependencies for the local archive and add them as needed
// to the outArchives collection.
            ArchiveInfo[] deps = findDependency(p,
outArchives,
selectedArchives,
remotePkgs,
remoteSources,
localArchives);

            if (deps != null) {
                // The already installed archive has a missing dependency, which we
                // just selected for install. Make sure we remember the dependency
                // so that we can enforce it later in the UI.
                for (ArchiveInfo aid : deps) {
                    aid.addDependencyFor(ai);
                }
            }
}
}

//Synthetic comment -- @@ -954,38 +963,55 @@
}

/** Fetch all remote packages only if really needed. */
    protected void fetchRemotePackages(
            final ArrayList<Package> remotePkgs,
            final SdkSource[] remoteSources) {
if (remotePkgs.size() > 0) {
return;
}

        // First check if there's any remote source we need to fetch.
        // This will bring the task window, so we rather not display it unless
        // necessary.
        boolean needsFetch = false;
for (final SdkSource remoteSrc : remoteSources) {
            needsFetch = remoteSrc.getPackages() == null;
            if (needsFetch) {
                break;
}
        }

        if (!needsFetch) {
            return;
        }

        final boolean forceHttp = mUpdaterData.getSettingsController().getForceHttp();

        mUpdaterData.getTaskFactory().start("Refresh Sources", new ITask() {
            public void run(ITaskMonitor monitor) {
                for (SdkSource remoteSrc : remoteSources) {
                    Package[] pkgs = remoteSrc.getPackages();

                    if (pkgs == null) {
                        remoteSrc.load(monitor, forceHttp);
                        pkgs = remoteSrc.getPackages();
                    }

                    if (pkgs != null) {
                        nextPackage: for (Package pkg : pkgs) {
                            for (Archive a : pkg.getArchives()) {
                                // Only add a package if it contains at least
                                // one compatible archive
                                if (a.isCompatible()) {
                                    remotePkgs.add(pkg);
                                    continue nextPackage;
                                }
                            }
}
}
}
}
        });
}









