/*SDK Manager: fix to suggest platform-tools install.

The way the updater currently works, it will only
generate the source.props of a new package based on the
attributes it knows from that package. That means mean
Tools r7 is updating tools, it will not add the proper
min-platform-tools-rev to the new Tools r8 package.

When "Update All" is selected, we try to do 2 new things:
- make sure to lool at local *existing* packages for
  potential missing dependencies, and suggest them for
  install.
- if a package doesn't have a min-platform-tools-rev set,
  suggest the higest revision available.

Change-Id:I76cdbc2818133429b2726d3127eedd7e65579a7e*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java
//Synthetic comment -- index 24316e4..c8f1e66 100755

//Synthetic comment -- @@ -182,6 +182,16 @@
return pkg instanceof ToolPackage;
}

    @Override
    void saveProperties(Properties props) {
        super.saveProperties(props);

        if (getMinPlatformToolsRevision() != MIN_PLATFORM_TOOLS_REV_INVALID) {
            props.setProperty(PROP_MIN_PLATFORM_TOOLS_REV,
                              Integer.toString(getMinPlatformToolsRevision()));
        }
    }

/**
* The tool package executes tools/lib/post_tools_install[.bat|.sh]
* {@inheritDoc}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index d2999bd..56847bc 100755

//Synthetic comment -- @@ -69,6 +69,9 @@
// Create ArchiveInfos out of local (installed) packages.
ArchiveInfo[] localArchives = createLocalArchives(localPkgs);

        // If we do not have a specific list of archives to install (that is the user
        // selected "update all" rather than request specific packages), then we try to
        // find updates based on the *existing* packages.
if (selectedArchives == null) {
selectedArchives = findUpdates(
localArchives,
//Synthetic comment -- @@ -77,6 +80,10 @@
includeObsoletes);
}

        // Once we have a list of packages to install, we try to solve all their
        // dependencies by automatically adding them to the list of things to install.
        // This works on the list provided either by the user directly or the list
        // computed from potential updates.
for (Archive a : selectedArchives) {
insertArchive(a,
archives,
//Synthetic comment -- @@ -87,6 +94,16 @@
false /*automated*/);
}

        // Finally we need to look at *existing* packages which are not being updated
        // and check if they have any missing dependencies and suggest how to fix
        // these dependencies.
        fixMissingLocalDependencies(
                archives,
                selectedArchives,
                remotePkgs,
                remoteSources,
                localArchives);

return archives;
}

//Synthetic comment -- @@ -246,6 +263,11 @@

/**
* Find suitable updates to all current local packages.
     * <p/>
     * Returns a list of potential updates for *existing* packages. This does NOT solve
     * dependencies for the new packages.
     * <p/>
     * Always returns a non-null collection, which can be empty.
*/
private Collection<Archive> findUpdates(
ArchiveInfo[] localArchives,
//Synthetic comment -- @@ -283,6 +305,46 @@
return updates;
}

    /**
     * Check all local archives which are NOT being updated and see if they
     * miss any dependency. If they do, try to fix that dependency by selecting
     * an appropriate package.
     */
    private void fixMissingLocalDependencies(
            ArrayList<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            ArrayList<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {

        nextLocalArchive: for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            Package p = a == null ? null : a.getParentPackage();
            if (p == null) {
                continue;
            }

            // Is this local archive being updated?
            for (ArchiveInfo ai2 : outArchives) {
                if (ai2.getReplaced() == a) {
                    // this new archive will replace the current local one,
                    // so we don't have to care about fixing dependencies (since the
                    // new archive should already have had its dependencies resolved)
                    continue nextLocalArchive;
                }
            }

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

private ArchiveInfo insertArchive(Archive archive,
ArrayList<ArchiveInfo> outArchives,
Collection<Archive> selectedArchives,
//Synthetic comment -- @@ -305,7 +367,7 @@
}
}

        // Find dependencies and adds them as needed to outArchives
ArchiveInfo[] deps = findDependency(p,
outArchives,
selectedArchives,
//Synthetic comment -- @@ -544,11 +606,18 @@
ArchiveInfo[] localArchives) {
// This is the requirement to match.
int rev = pkg.getMinPlatformToolsRevision();
        boolean findMax = false;
        ArchiveInfo aiMax = null;
        Archive aMax = null;

if (rev == IMinPlatformToolsDependency.MIN_PLATFORM_TOOLS_REV_INVALID) {
            // The requirement is invalid, which is not supposed to happen since this
            // property is mandatory. However in a typical upgrade scenario we can end
            // up with the previous updater managing a new package and not dealing
            // correctly with the new unknown property.
            // So instead we parse all the existing and remote packages and try to find
            // the max available revision and we'll use it.
            findMax = true;
}

// First look in locally installed packages.
//Synthetic comment -- @@ -557,7 +626,11 @@
if (a != null) {
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
                    int r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r > rev) {
                        rev = r;
                        aiMax = ai;
                    } else if (!findMax && r >= rev) {
// We found one already installed.
return null;
}
//Synthetic comment -- @@ -571,7 +644,11 @@
if (a != null) {
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
                    int r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r > rev) {
                        rev = r;
                        aiMax = ai;
                    } else if (!findMax && r >= rev) {
// The dependency is already scheduled for install, nothing else to do.
return ai;
}
//Synthetic comment -- @@ -584,7 +661,12 @@
for (Archive a : selectedArchives) {
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
                    int r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r > rev) {
                        rev = r;
                        aiMax = null;
                        aMax = a;
                    } else if (!findMax && r >= rev) {
// It's not already in the list of things to install, so add it now
return insertArchive(a,
outArchives,
//Synthetic comment -- @@ -602,24 +684,46 @@
fetchRemotePackages(remotePkgs, remoteSources);
for (Package p : remotePkgs) {
if (p instanceof PlatformToolPackage) {
                int r = ((PlatformToolPackage) p).getRevision();
                if (r >= rev) {
                    // Make sure there's at least one valid archive here
for (Archive a : p.getArchives()) {
if (a.isCompatible()) {
                            if (findMax && r > rev) {
                                rev = r;
                                aiMax = null;
                                aMax = a;
                            } else if (!findMax && r >= rev) {
                                // It's not already in the list of things to install, so add the
                                // first compatible archive we can find.
                                return insertArchive(a,
                                        outArchives,
                                        selectedArchives,
                                        remotePkgs,
                                        remoteSources,
                                        localArchives,
                                        true /*automated*/);
                            }
}
}
}
}
}

        if (findMax) {
            if (aMax != null) {
                return insertArchive(aMax,
                        outArchives,
                        selectedArchives,
                        remotePkgs,
                        remoteSources,
                        localArchives,
                        true /*automated*/);
            } else if (aiMax != null) {
                return aiMax;
            }
        }

// We end up here if nothing matches. We don't have a good platform to match.
// We need to indicate this package depends on a missing platform archive
// so that it can be impossible to install later on.
//Synthetic comment -- @@ -869,7 +973,7 @@
* "local" package/archive.
* <p/>
* In this case, the "new Archive" is still expected to be non null and the
     * "replaced Archive" is null. Installed archives are always accepted and never
* rejected.
* <p/>
* Dependencies are not set.







