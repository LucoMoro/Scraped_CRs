/*SDK Manager: handle platform-tools preview + final.

One issue is that when there was only one instance of platform-tools possible,
the computeUpdates() code would pick the first one. But now there can be 2 of
them (preview, non-preview) and thus we need to pick up the higher one even if
it's not the first choice.

Change-Id:I61db2881274dafa32c5c303c0b3569fc336b8e92*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockToolPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockToolPackage.java
//Synthetic comment -- index 32711d6..3c39194 100755

//Synthetic comment -- @@ -84,10 +84,40 @@
);
}

    /**
     * Creates a {@link MockToolPackage} with the given revision and hardcoded defaults
     * for everything else.
     * <p/>
     * By design, this creates a package with one and only one archive.
     */
    public MockToolPackage(
            SdkSource source,
            FullRevision revision,
            FullRevision minPlatformToolsRev) {
        super(
                source, // source,
                createProps(revision, minPlatformToolsRev), // props,
                revision.getMajor(),
                null, // license,
                "desc", // description,
                "url", // descUrl,
                Os.getCurrentOs(), // archiveOs,
                Arch.getCurrentArch(), // archiveArch,
                "foo" // archiveOsPath
                );
    }

private static Properties createProps(FullRevision revision, int minPlatformToolsRev) {
Properties props = FullRevisionPackageTest.createProps(revision);
props.setProperty(PkgProps.MIN_PLATFORM_TOOLS_REV,
Integer.toString((minPlatformToolsRev)));
return props;
}

    private static Properties createProps(FullRevision revision, FullRevision minPlatformToolsRev) {
        Properties props = FullRevisionPackageTest.createProps(revision);
        props.setProperty(PkgProps.MIN_PLATFORM_TOOLS_REV,
                          minPlatformToolsRev.toShortString());
        return props;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java
//Synthetic comment -- index da71115..57b119c 100755

//Synthetic comment -- @@ -782,6 +782,7 @@
// This is the requirement to match.
FullRevision rev = pkg.getMinPlatformToolsRevision();
boolean findMax = false;
        int compareThreshold = 0;
ArchiveInfo aiMax = null;
Archive aMax = null;

//Synthetic comment -- @@ -793,6 +794,9 @@
// So instead we parse all the existing and remote packages and try to find
// the max available revision and we'll use it.
findMax = true;
            // When findMax is false, we want r.compareTo(rev) >= 0.
            // When findMax is true, we want r.compareTo(rev) > 0 (so >= 1).
            compareThreshold = 1;
}

// First look in locally installed packages.
//Synthetic comment -- @@ -802,10 +806,10 @@
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r.compareTo(rev) > compareThreshold) {
rev = r;
aiMax = ai;
                    } else if (!findMax && r.compareTo(rev) >= compareThreshold) {
// We found one already installed.
return null;
}
//Synthetic comment -- @@ -813,6 +817,9 @@
}
}

        // Because of previews, we can have more than 1 choice, so get the local max.
        FullRevision localRev = rev;
        ArchiveInfo localAiMax = null;
// Look in archives already scheduled for install
for (ArchiveInfo ai : outArchives) {
Archive a = ai.getNewArchive();
//Synthetic comment -- @@ -820,43 +827,61 @@
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (r.compareTo(localRev) >= compareThreshold) {
                        localRev = r;
                        localAiMax = ai;
}
}
}
}
        if (localAiMax != null) {
            if (findMax) {
                rev = localRev;
                aiMax = localAiMax;
            } else {
                // The dependency is already scheduled for install, nothing else to do.
                return localAiMax;
            }
        }


// Otherwise look in the selected archives.
        localRev = rev;
        Archive localAMax = null;
if (selectedArchives != null) {
for (Archive a : selectedArchives) {
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (r.compareTo(localRev) >= compareThreshold) {
                        localRev = r;
                        localAiMax = null;
                        localAMax = a;
}
}
}
            if (localAMax != null) {
                if (findMax) {
                    rev = localRev;
                    aiMax = null;
                    aMax = localAMax;
                } else {
                    // It's not already in the list of things to install, so add it now
                    return insertArchive(localAMax,
                            outArchives,
                            selectedArchives,
                            remotePkgs,
                            remoteSources,
                            localArchives,
                            true /*automated*/);
                }
            }
}

// Finally nothing matched, so let's look at all available remote packages
fetchRemotePackages(remotePkgs, remoteSources);
        localRev = rev;
        localAMax = null;
for (Package p : remotePkgs) {
if (p instanceof PlatformToolPackage) {
FullRevision r = ((PlatformToolPackage) p).getRevision();
//Synthetic comment -- @@ -864,26 +889,33 @@
// Make sure there's at least one valid archive here
for (Archive a : p.getArchives()) {
if (a.isCompatible()) {
                            if (r.compareTo(localRev) >= compareThreshold) {
                                localRev = r;
                                localAiMax = null;
                                localAMax = a;
}
}
}
}
}
}
        if (localAMax != null) {
            if (findMax) {
                rev = localRev;
                aiMax = null;
                aMax = localAMax;
            } else {
                // It's not already in the list of things to install, so add the
                // first compatible archive we can find.
                return insertArchive(localAMax,
                        outArchives,
                        selectedArchives,
                        remotePkgs,
                        remoteSources,
                        localArchives,
                        true /*automated*/);
            }
        }

if (findMax) {
if (aMax != null) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java
//Synthetic comment -- index 7918885..0c86ca9 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.packages.MockAddonPackage;
import com.android.sdklib.internal.repository.packages.MockBrokenPackage;
import com.android.sdklib.internal.repository.packages.MockPlatformPackage;
//Synthetic comment -- @@ -363,4 +364,66 @@
"SDK Platform Android android-9, API 9, revision 1]",
Arrays.toString(selected.toArray()));
}

    public void testComputeRevisionUpdate2() {
        // Scenario:
        // - user has tools rev 2 installed and NO platform-tools
        // - server has platform tools 1 rc 1 (a preview) and 2.
        // - server has platform 2 that requires min-tools 2 that requires min-plat-tools 1rc1.
        //
        // One issue is that when there was only one instance of platform-tools possible,
        // the computeUpdates() code would pick the first one. But now there can be 2 of
        // them (preview, non-preview) and thus we need to pick up the higher one even if
        // it's not the first choice.

        final MockPlatformToolPackage pt1rc = new MockPlatformToolPackage(
                                                    null,
                                                    new FullRevision(1, 0, 0, 1));
        final MockPlatformToolPackage pt2 = new MockPlatformToolPackage(2);

        // Tools rev 2 requires at least plat-tools 1rc1
        final MockToolPackage t2 = new MockToolPackage(null,
                                                       new FullRevision(2),           // tools rev
                                                       new FullRevision(1, 0, 0, 1)); // min-pt-rev

        final MockPlatformPackage p2 = new MockPlatformPackage(2, 1, 2 /*min-tools*/);

        // Note: the mock updater logic gets the remotes packages from the array given
        // here and bypasses the source (to avoid fetching any actual URLs)
        // Remote available packages include both plat-tools 1rc1 and 2.
        //
        // Order DOES matter: the issue is that computeUpdates was selecting the first platform
        // tools (so 1rc1) and ignoring the newer revision 2 because originally there could be
        // only one platform-tool definition. Now with previews we can have 2 and we need to
        // select the higher one even if it's not the first choice.
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(),
                new Package[] { t2, pt1rc, pt2, p2 });

        // Local packages only have tools 2.
        SdkSources sources = new SdkSources();
        Package[] localPkgs = { t2 };
        List<Archive> selectedArchives = Arrays.asList( p2.getArchives() );

        List<ArchiveInfo> selected = mul.computeUpdates(
                selectedArchives,
                sources,
                localPkgs,
                false /*includeObsoletes*/);

        assertEquals(
                "[SDK Platform Android android-2, API 2, revision 1, " +
                 "Android SDK Platform-tools, revision 2]",
                Arrays.toString(selected.toArray()));

        mul.addNewPlatforms(
                selected,
                sources,
                localPkgs,
                false /*includeObsoletes*/);

        assertEquals(
                "[SDK Platform Android android-2, API 2, revision 1, " +
                 "Android SDK Platform-tools, revision 2]",
                Arrays.toString(selected.toArray()));
    }
}







