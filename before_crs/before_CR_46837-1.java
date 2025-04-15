/*Merge "SDK Manager: handle platform-tools preview + final."

One issue is that when there was only one instance of platform-tools possible,
the computeUpdates() code would pick the first one. But now there can be 2 of
them (preview, non-preview) and thus we need to pick up the higher one even if
it's not the first choice.

Same issue with tools: if a platform depends on tools and there are none
installed, we need to pick the highest version of the available preview
or final package that satisfies the dependency.

Note that in both cases the issue does not arise if there's already
a tools or platform-tools installed.

(cherry picked from commit ce38196f8e47fa3b48805b36eaca55716c0af79c)

Change-Id:Iec36d68378ac1235107183a2f403e76e419042dc*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockPlatformPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockPlatformPackage.java
//Synthetic comment -- index 1d70ba9..dd744ec 100755

//Synthetic comment -- @@ -47,7 +47,7 @@
* Creates a {@link MockPlatformTarget} with the requested API and revision
* and then a {@link MockPlatformPackage} wrapping it.
*
     * Also sets the min-tools-rev of the platform.
*
* By design, this package contains one and only one archive.
*/
//Synthetic comment -- @@ -57,6 +57,20 @@
createProps(min_tools_rev));
}

public MockPlatformPackage(SdkSource source, int apiLevel, int revision, int min_tools_rev) {
this(source, new MockPlatformTarget(apiLevel, revision), createProps(min_tools_rev));
}
//Synthetic comment -- @@ -73,6 +87,12 @@
return props;
}

public IAndroidTarget getTarget() {
return mTarget;
}








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
//Synthetic comment -- index da71115..25e6553 100755

//Synthetic comment -- @@ -738,25 +738,34 @@

// Finally nothing matched, so let's look at all available remote packages
fetchRemotePackages(remotePkgs, remoteSources);
for (Package p : remotePkgs) {
if (p instanceof ToolPackage) {
                if (((ToolPackage) p).getRevision().compareTo(rev) >= 0) {
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
// We need to indicate this extra depends on a missing platform archive
//Synthetic comment -- @@ -782,6 +791,7 @@
// This is the requirement to match.
FullRevision rev = pkg.getMinPlatformToolsRevision();
boolean findMax = false;
ArchiveInfo aiMax = null;
Archive aMax = null;

//Synthetic comment -- @@ -793,6 +803,9 @@
// So instead we parse all the existing and remote packages and try to find
// the max available revision and we'll use it.
findMax = true;
}

// First look in locally installed packages.
//Synthetic comment -- @@ -802,10 +815,10 @@
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
//Synthetic comment -- @@ -813,6 +826,9 @@
}
}

// Look in archives already scheduled for install
for (ArchiveInfo ai : outArchives) {
Archive a = ai.getNewArchive();
//Synthetic comment -- @@ -820,43 +836,61 @@
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
//Synthetic comment -- @@ -864,26 +898,34 @@
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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/SdkUpdaterLogicTest.java
similarity index 65%
rename from sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java
rename to sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/SdkUpdaterLogicTest.java
//Synthetic comment -- index 7918885..6a93914 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.MockAddonPackage;
import com.android.sdklib.internal.repository.packages.MockBrokenPackage;
import com.android.sdklib.internal.repository.packages.MockPlatformPackage;
//Synthetic comment -- @@ -41,7 +42,7 @@

import junit.framework.TestCase;

public class UpdaterLogicTest extends TestCase {

private static class NullUpdaterData implements IUpdaterData {

//Synthetic comment -- @@ -87,10 +88,10 @@

}

    private static class MockUpdaterLogic extends SdkUpdaterLogic {
private final Package[] mRemotePackages;

        public MockUpdaterLogic(IUpdaterData updaterData, Package[] remotePackages) {
super(updaterData);
mRemotePackages = remotePackages;
}
//Synthetic comment -- @@ -112,7 +113,7 @@
* can find the base platform for a given addon.
*/
public void testFindAddonDependency() {
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(), null);

MockPlatformPackage p1 = new MockPlatformPackage(1, 1);
MockPlatformPackage p2 = new MockPlatformPackage(2, 1);
//Synthetic comment -- @@ -154,7 +155,7 @@
* platform package for a given broken add-on package.
*/
public void testFindExactApiLevelDependency() {
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(), null);

MockPlatformPackage p1 = new MockPlatformPackage(1, 1);
MockPlatformPackage p2 = new MockPlatformPackage(2, 1);
//Synthetic comment -- @@ -202,7 +203,7 @@
* tool package for a given platform package.
*/
public void testFindPlatformDependency() {
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(), null);

MockPlatformToolPackage pt1 = new MockPlatformToolPackage(1);

//Synthetic comment -- @@ -245,7 +246,7 @@
* platform-tool package for a given tool package.
*/
public void testFindPlatformToolDependency() {
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(), null);

MockPlatformToolPackage t1 = new MockPlatformToolPackage(1);
MockPlatformToolPackage t2 = new MockPlatformToolPackage(2);
//Synthetic comment -- @@ -306,7 +307,7 @@

// Note: the mock updater logic gets the remotes packages from the array given
// here and bypasses the source (to avoid fetching any actual URLs)
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(),
new Package[] { t8, pt2, t9, pt3, p9 });

SdkSources sources = new SdkSources();
//Synthetic comment -- @@ -337,7 +338,7 @@

// Now try again but reverse the order of the remote package list.

        mul = new MockUpdaterLogic(new NullUpdaterData(),
new Package[] { p9, t9, pt3, t8, pt2 });

selected = mul.computeUpdates(
//Synthetic comment -- @@ -363,4 +364,132 @@
"SDK Platform Android android-9, API 9, revision 1]",
Arrays.toString(selected.toArray()));
}
}







