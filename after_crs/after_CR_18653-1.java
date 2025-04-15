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
public class Archive implements IDescription, Comparable<Archive> {

public static final int NUM_MONITOR_INC = 100;
private static final String PROP_OS   = "Archive.Os";       //$NON-NLS-1$
//Synthetic comment -- @@ -1099,4 +1099,16 @@
"chmod", "777", file.getAbsolutePath()
});
}

    /**
     * Archives are compared using their {@link Package} ordering.
     *
     * @see Package#compareTo(Package)
     */
    public int compareTo(Archive rhs) {
        if (mPackage != null && rhs != null) {
            return mPackage.compareTo(rhs.getParentPackage());
        }
        return 0;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index a6c28a9..438b07b 100755

//Synthetic comment -- @@ -503,18 +503,18 @@


/**
     * Returns an ordering like this: <br/>
     * - Tools <br/>
     * - Platform-Tools <br/>
     * - Docs. <br/>
     * - Platform n preview <br/>
     * - Platform n <br/>
     * - Platform n-1 <br/>
     * - Samples packages <br/>
     * - Add-on based on n preview <br/>
     * - Add-on based on n <br/>
     * - Add-on based on n-1 <br/>
     * - Extra packages <br/>
*/
public int compareTo(Package other) {
int s1 = this.sortingScore();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java
//Synthetic comment -- index 15560b7..d01570d 100755

//Synthetic comment -- @@ -42,7 +42,7 @@
*
* @see ArchiveInfo#ArchiveInfo(Archive, Archive, ArchiveInfo[])
*/
class ArchiveInfo implements IDescription, Comparable<ArchiveInfo> {

private final Archive mNewArchive;
private final Archive mReplaced;
//Synthetic comment -- @@ -205,4 +205,17 @@
}
return super.toString();
}

    /**
     * ArchiveInfos are compared using ther "new archive" ordering.
     *
     * @see Archive#compareTo(Archive)
     */
    public int compareTo(ArchiveInfo rhs) {
        if (mNewArchive != null && rhs != null) {
            return mNewArchive.compareTo(rhs.getNewArchive());
        }
        return 0;
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index fe51f19..a1c089b 100755

//Synthetic comment -- @@ -51,6 +51,7 @@
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
//Synthetic comment -- @@ -639,6 +640,8 @@
// TODO if selectedArchives is null and archives.len==0, find if there are
// any new platform we can suggest to install instead.

        Collections.sort(archives);

UpdateChooserDialog dialog = new UpdateChooserDialog(getWindowShell(), this, archives);
dialog.open();

//Synthetic comment -- @@ -680,6 +683,8 @@
getLocalSdkParser().getPackages(),
includeObsoletes);

        Collections.sort(archives);

// Filter the selected archives to only keep the ones matching the filter
if (pkgFilter != null && pkgFilter.size() > 0 && archives != null && archives.size() > 0) {
// Map filter types to an SdkRepository Package type.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index 4380ca8..6c25ae4 100755

//Synthetic comment -- @@ -962,7 +962,24 @@
return new MissingPlatformArchiveInfo(new AndroidVersion(api, null /*codename*/));
}

    /**
     * Fetch all remote packages only if really needed.
     * <p/>
     * This methods takes a list of sources. Each source is only fetched once -- that is each
     * source keeps the list of packages that we fetched from the remove XML file. If the list
     * is null, it means this source has never been fetched so we'll do it onc ehere. Otherwise
     * we rely on the cached list of packages from this source.
     * <p/>
     * This methods also takes a remote package list as input, which it will fill out.
     * If a source has already been fetched, we'll add its packages to the remote package list
     * if they are not already present. Otherwise, the source will be fetched and the packages
     * added to the list.
     *
     * @param remotePkgs An in-out list of packages available from remote sources.
     *                  This list must not be null.
     *                  It can be empty or already contains some packages.
     * @param remoteSources A list of available remote sources to fetch from.
     */
protected void fetchRemotePackages(
final ArrayList<Package> remotePkgs,
final SdkSource[] remoteSources) {
//Synthetic comment -- @@ -975,9 +992,27 @@
// necessary.
boolean needsFetch = false;
for (final SdkSource remoteSrc : remoteSources) {
            Package[] pkgs = remoteSrc.getPackages();
            if (pkgs == null) {
                // This source has never been fetched. We'll do it below.
                needsFetch = true;
            } else {
                // This source has already been fetched and we know its package list.
                // We still need to make sure all of its packages are present in the
                // remotePkgs list.

                nextPackage: for (Package pkg : pkgs) {
                    for (Archive a : pkg.getArchives()) {
                        // Only add a package if it contains at least one compatible archive
                        // and is not already in the remote package list.
                        if (a.isCompatible()) {
                            if (!remotePkgs.contains(pkg)) {
                                remotePkgs.add(pkg);
                                continue nextPackage;
                            }
                        }
                    }
                }
}
}

//Synthetic comment -- @@ -1000,11 +1035,13 @@
if (pkgs != null) {
nextPackage: for (Package pkg : pkgs) {
for (Archive a : pkg.getArchives()) {
                                // Only add a package if it contains at least one compatible archive
                                // and is not already in the remote package list.
if (a.isCompatible()) {
                                    if (!remotePkgs.contains(pkg)) {
                                        remotePkgs.add(pkg);
                                        continue nextPackage;
                                    }
}
}
}







