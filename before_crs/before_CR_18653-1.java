/*SDK Manager: fix suggestions of new platforms.

I accidentally broke the way the SDK Manager was suggesting
new platforms when I "optimized" the way it was fetching new
sources when resolving dependencies. The fix is that even if
we don't need to refresh or fetch a source, we still need to
report the packages we know it contains.

Also made the Archive and ArchiveInfo implement Comparable
and defer their comparison to the one of Package. This way
we can sort the archives in the install window.

Change-Id:Ic3b39e49e8143541b19b00de09468c1b3f01b0d7*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java
//Synthetic comment -- index 8b61637..10e6424 100755

//Synthetic comment -- @@ -44,7 +44,7 @@
* <p/>
* Packages are offered by a {@link SdkSource} (a download site).
*/
public class Archive implements IDescription {

public static final int NUM_MONITOR_INC = 100;
private static final String PROP_OS   = "Archive.Os";       //$NON-NLS-1$
//Synthetic comment -- @@ -1099,4 +1099,16 @@
"chmod", "777", file.getAbsolutePath()
});
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index a6c28a9..438b07b 100755

//Synthetic comment -- @@ -503,18 +503,18 @@


/**
     * Returns an ordering like this:
     * - Tools.
     * - Platform-Tools.
     * - Docs.
     * - Platform n preview
     * - Platform n
     * - Platform n-1
     * - Samples packages.
     * - Add-on based on n preview
     * - Add-on based on n
     * - Add-on based on n-1
     * - Extra packages.
*/
public int compareTo(Package other) {
int s1 = this.sortingScore();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java
//Synthetic comment -- index 15560b7..d01570d 100755

//Synthetic comment -- @@ -42,7 +42,7 @@
*
* @see ArchiveInfo#ArchiveInfo(Archive, Archive, ArchiveInfo[])
*/
class ArchiveInfo implements IDescription {

private final Archive mNewArchive;
private final Archive mReplaced;
//Synthetic comment -- @@ -205,4 +205,17 @@
}
return super.toString();
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index fe51f19..a1c089b 100755

//Synthetic comment -- @@ -51,6 +51,7 @@
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
//Synthetic comment -- @@ -639,6 +640,8 @@
// TODO if selectedArchives is null and archives.len==0, find if there are
// any new platform we can suggest to install instead.

UpdateChooserDialog dialog = new UpdateChooserDialog(getWindowShell(), this, archives);
dialog.open();

//Synthetic comment -- @@ -680,6 +683,8 @@
getLocalSdkParser().getPackages(),
includeObsoletes);

// Filter the selected archives to only keep the ones matching the filter
if (pkgFilter != null && pkgFilter.size() > 0 && archives != null && archives.size() > 0) {
// Map filter types to an SdkRepository Package type.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index 4380ca8..6c25ae4 100755

//Synthetic comment -- @@ -962,7 +962,24 @@
return new MissingPlatformArchiveInfo(new AndroidVersion(api, null /*codename*/));
}

    /** Fetch all remote packages only if really needed. */
protected void fetchRemotePackages(
final ArrayList<Package> remotePkgs,
final SdkSource[] remoteSources) {
//Synthetic comment -- @@ -975,9 +992,27 @@
// necessary.
boolean needsFetch = false;
for (final SdkSource remoteSrc : remoteSources) {
            needsFetch = remoteSrc.getPackages() == null;
            if (needsFetch) {
                break;
}
}

//Synthetic comment -- @@ -1000,11 +1035,13 @@
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







