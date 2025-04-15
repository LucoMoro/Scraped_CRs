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

private static Properties createProps(FullRevision revision, int minPlatformToolsRev) {
Properties props = FullRevisionPackageTest.createProps(revision);
props.setProperty(PkgProps.MIN_PLATFORM_TOOLS_REV,
Integer.toString((minPlatformToolsRev)));
return props;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java
//Synthetic comment -- index da71115..57b119c 100755

//Synthetic comment -- @@ -782,6 +782,7 @@
// This is the requirement to match.
FullRevision rev = pkg.getMinPlatformToolsRevision();
boolean findMax = false;
ArchiveInfo aiMax = null;
Archive aMax = null;

//Synthetic comment -- @@ -793,6 +794,9 @@
// So instead we parse all the existing and remote packages and try to find
// the max available revision and we'll use it.
findMax = true;
}

// First look in locally installed packages.
//Synthetic comment -- @@ -802,10 +806,10 @@
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r.compareTo(rev) > 0) {
rev = r;
aiMax = ai;
                    } else if (!findMax && r.compareTo(rev) >= 0) {
// We found one already installed.
return null;
}
//Synthetic comment -- @@ -813,6 +817,9 @@
}
}

// Look in archives already scheduled for install
for (ArchiveInfo ai : outArchives) {
Archive a = ai.getNewArchive();
//Synthetic comment -- @@ -820,43 +827,61 @@
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r.compareTo(rev) > 0) {
                        rev = r;
                        aiMax = ai;
                    } else if (!findMax && r.compareTo(rev) >= 0) {
                        // The dependency is already scheduled for install, nothing else to do.
                        return ai;
}
}
}
}

// Otherwise look in the selected archives.
if (selectedArchives != null) {
for (Archive a : selectedArchives) {
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r.compareTo(rev) > 0) {
                        rev = r;
                        aiMax = null;
                        aMax = a;
                    } else if (!findMax && r.compareTo(rev) >= 0) {
                        // It's not already in the list of things to install, so add it now
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

// Finally nothing matched, so let's look at all available remote packages
fetchRemotePackages(remotePkgs, remoteSources);
for (Package p : remotePkgs) {
if (p instanceof PlatformToolPackage) {
FullRevision r = ((PlatformToolPackage) p).getRevision();
//Synthetic comment -- @@ -864,26 +889,33 @@
// Make sure there's at least one valid archive here
for (Archive a : p.getArchives()) {
if (a.isCompatible()) {
                            if (findMax && r.compareTo(rev) > 0) {
                                rev = r;
                                aiMax = null;
                                aMax = a;
                            } else if (!findMax && r.compareTo(rev) >= 0) {
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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java
//Synthetic comment -- index 7918885..0c86ca9 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.MockAddonPackage;
import com.android.sdklib.internal.repository.packages.MockBrokenPackage;
import com.android.sdklib.internal.repository.packages.MockPlatformPackage;
//Synthetic comment -- @@ -363,4 +364,66 @@
"SDK Platform Android android-9, API 9, revision 1]",
Arrays.toString(selected.toArray()));
}
}







