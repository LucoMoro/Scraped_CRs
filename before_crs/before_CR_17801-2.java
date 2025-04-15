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

/**
* The tool package executes tools/lib/post_tools_install[.bat|.sh]
* {@inheritDoc}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index d2999bd..56847bc 100755

//Synthetic comment -- @@ -69,6 +69,9 @@
// Create ArchiveInfos out of local (installed) packages.
ArchiveInfo[] localArchives = createLocalArchives(localPkgs);

if (selectedArchives == null) {
selectedArchives = findUpdates(
localArchives,
//Synthetic comment -- @@ -77,6 +80,10 @@
includeObsoletes);
}

for (Archive a : selectedArchives) {
insertArchive(a,
archives,
//Synthetic comment -- @@ -87,6 +94,16 @@
false /*automated*/);
}

return archives;
}

//Synthetic comment -- @@ -246,6 +263,11 @@

/**
* Find suitable updates to all current local packages.
*/
private Collection<Archive> findUpdates(
ArchiveInfo[] localArchives,
//Synthetic comment -- @@ -283,6 +305,46 @@
return updates;
}

private ArchiveInfo insertArchive(Archive archive,
ArrayList<ArchiveInfo> outArchives,
Collection<Archive> selectedArchives,
//Synthetic comment -- @@ -305,7 +367,7 @@
}
}

        // Find dependencies
ArchiveInfo[] deps = findDependency(p,
outArchives,
selectedArchives,
//Synthetic comment -- @@ -544,11 +606,18 @@
ArchiveInfo[] localArchives) {
// This is the requirement to match.
int rev = pkg.getMinPlatformToolsRevision();

if (rev == IMinPlatformToolsDependency.MIN_PLATFORM_TOOLS_REV_INVALID) {
            // The requirement is invalid, which is not supposed to happen.
            // We'll never find a matching package so don't even bother.
            return null;
}

// First look in locally installed packages.
//Synthetic comment -- @@ -557,7 +626,11 @@
if (a != null) {
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
                    if (((PlatformToolPackage) p).getRevision() >= rev) {
// We found one already installed.
return null;
}
//Synthetic comment -- @@ -571,7 +644,11 @@
if (a != null) {
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
                    if (((PlatformToolPackage) p).getRevision() >= rev) {
// The dependency is already scheduled for install, nothing else to do.
return ai;
}
//Synthetic comment -- @@ -584,7 +661,12 @@
for (Archive a : selectedArchives) {
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
                    if (((PlatformToolPackage) p).getRevision() >= rev) {
// It's not already in the list of things to install, so add it now
return insertArchive(a,
outArchives,
//Synthetic comment -- @@ -602,24 +684,46 @@
fetchRemotePackages(remotePkgs, remoteSources);
for (Package p : remotePkgs) {
if (p instanceof PlatformToolPackage) {
                if (((PlatformToolPackage) p).getRevision() >= rev) {
                    // It's not already in the list of things to install, so add the
                    // first compatible archive we can find.
for (Archive a : p.getArchives()) {
if (a.isCompatible()) {
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

// We end up here if nothing matches. We don't have a good platform to match.
// We need to indicate this package depends on a missing platform archive
// so that it can be impossible to install later on.
//Synthetic comment -- @@ -869,7 +973,7 @@
* "local" package/archive.
* <p/>
* In this case, the "new Archive" is still expected to be non null and the
     * "replaced Archive" isnull. Installed archives are always accepted and never
* rejected.
* <p/>
* Dependencies are not set.







