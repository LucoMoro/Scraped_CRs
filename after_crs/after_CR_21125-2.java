/*SDK Manager: Update All doesn't pick highest revisions

SDK Bug 14128

Change-Id:I4a9d1a10c99e32226eb0c2a4bc6bdddedfa26f66*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index 77eb981..58bf314 100755

//Synthetic comment -- @@ -170,12 +170,22 @@
return mPackages;
}

    @VisibleForTesting(visibility=Visibility.PRIVATE)
    protected void setPackages(Package[] packages) {
        mPackages = packages;

        if (mPackages != null) {
            // Order the packages.
            Arrays.sort(mPackages, null);
        }
    }

/**
* Clear the internal packages list. After this call, {@link #getPackages()} will return
* null till load() is called.
*/
public void clearPackages() {
        setPackages(null);
}

/**
//Synthetic comment -- @@ -740,10 +750,7 @@
}
}

            setPackages(packages.toArray(new Package[packages.size()]));

return true;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index 824a89f..91afed6 100755

//Synthetic comment -- @@ -302,8 +302,26 @@
// Found a suitable update. Only accept the remote package
// if it provides at least one compatible archive

                    addArchives:
for (Archive a : remotePkg.getArchives()) {
if (a.isCompatible()) {

                            // If we're trying to add a package for revision N,
                            // make sure we don't also have a package for revision N-1.
                            for (int i = updates.size() - 1; i >= 0; i--) {
                                Package pkgFound = updates.get(i).getParentPackage();
                                if (pkgFound.canBeUpdatedBy(remotePkg) == UpdateInfo.UPDATE) {
                                    // This package can update one we selected earlier.
                                    // Remove the one that can be updated by this new one.
                                   updates.remove(i);
                                } else if (remotePkg.canBeUpdatedBy(pkgFound) ==
                                                UpdateInfo.UPDATE) {
                                    // There is a package in the list that is already better
                                    // than the one we want to add, so don't add it.
                                    break addArchives;
                                }
                            }

updates.add(a);
break;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java
//Synthetic comment -- index fb956f2..72229ff 100755

//Synthetic comment -- @@ -21,13 +21,17 @@
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.MockAddonPackage;
import com.android.sdklib.internal.repository.MockBrokenPackage;
import com.android.sdklib.internal.repository.MockPlatformPackage;
import com.android.sdklib.internal.repository.MockPlatformToolPackage;
import com.android.sdklib.internal.repository.MockToolPackage;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.SdkRepoSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

import org.eclipse.swt.widgets.Shell;
//Synthetic comment -- @@ -35,6 +39,7 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

//Synthetic comment -- @@ -266,4 +271,86 @@
assertEquals(1, out.size());
assertSame(t2_archive, out.get(0).getNewArchive());
}

    public void testComputeRevisionUpdate() {
        // Scenario:
        // - user has tools rev 7 installed + plat-tools rev 1 installed
        // - server has tools rev 8, depending on plat-tools rev 2
        // - server has tools rev 9, depending on plat-tools rev 3
        // - server has platform 9 that requires min-tools-rev 9
        //
        // If we do an update all, we want to the installer to pick up:
        // - the new platform 9
        // - the tools rev 9 (required by platform 9)
        // - the plat-tools rev 3 (required by tools rev 9)

        final MockPlatformToolPackage pt1 = new MockPlatformToolPackage(1);
        final MockPlatformToolPackage pt2 = new MockPlatformToolPackage(2);
        final MockPlatformToolPackage pt3 = new MockPlatformToolPackage(3);

        final MockToolPackage t7 = new MockToolPackage(7, 1 /*min-plat-tools*/);
        final MockToolPackage t8 = new MockToolPackage(8, 2 /*min-plat-tools*/);
        final MockToolPackage t9 = new MockToolPackage(9, 3 /*min-plat-tools*/);

        final MockPlatformPackage p9 = new MockPlatformPackage(9, 1, 9 /*min-tools*/);

        // Note: the mock updater logic gets the remotes packages from the array given
        // here and bypasses the source (to avoid fetching any actual URLs)
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(),
                new Package[] { t8, pt2, t9, pt3, p9 });

        SdkSources sources = new SdkSources();
        Package[] localPkgs = { t7, pt1 };

        List<ArchiveInfo> selected = mul.computeUpdates(
                null /*selectedArchives*/,
                sources,
                localPkgs,
                false /*includeObsoletes*/);

        assertEquals(
                "[Android SDK Platform-tools, revision 3, " +
                 "Android SDK Tools, revision 9]",
                Arrays.toString(selected.toArray()));

        mul.addNewPlatforms(
                selected,
                sources,
                localPkgs,
                false /*includeObsoletes*/);

        assertEquals(
                "[Android SDK Platform-tools, revision 3, " +
                 "Android SDK Tools, revision 9, " +
                 "SDK Platform Android android-9, API 9, revision 1]",
                Arrays.toString(selected.toArray()));

        // Now try again but reverse the order of the remote package list.

        mul = new MockUpdaterLogic(new NullUpdaterData(),
                new Package[] { p9, t9, pt3, t8, pt2 });

        selected = mul.computeUpdates(
                null /*selectedArchives*/,
                sources,
                localPkgs,
                false /*includeObsoletes*/);

        assertEquals(
                "[Android SDK Platform-tools, revision 3, " +
                 "Android SDK Tools, revision 9]",
                Arrays.toString(selected.toArray()));

        mul.addNewPlatforms(
                selected,
                sources,
                localPkgs,
                false /*includeObsoletes*/);

        assertEquals(
                "[Android SDK Platform-tools, revision 3, " +
                 "Android SDK Tools, revision 9, " +
                 "SDK Platform Android android-9, API 9, revision 1]",
                Arrays.toString(selected.toArray()));
    }
}







