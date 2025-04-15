/*Merge: SdkManager: suggest which platform to install to fix a broken addon.

The SDK Manager now has the notion of a "broken installed package".
The BrokenPackage can specify that:
- it requires a certain minimal platform to be installed,
and/or:
- it requires a specific exact platform to be installed.

The later constraint is expressed by IExactApiLevelDependency and
allows UpdaterLogic to find which platform would fix an addon which
is missing its base platform.

Change-Id:I0215900f499014038ba9470b5fcff4c60a24c536*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonPackage.java
//Synthetic comment -- index 8226a60..ab27561 100755

//Synthetic comment -- @@ -38,7 +38,7 @@
* Represents an add-on XML node in an SDK repository.
*/
public class AddonPackage extends Package
    implements IPackageVersion, IPlatformDependency, IExactApiLevelDependency {

private static final String PROP_NAME      = "Addon.Name";      //$NON-NLS-1$
private static final String PROP_VENDOR    = "Addon.Vendor";    //$NON-NLS-1$
//Synthetic comment -- @@ -157,15 +157,22 @@
shortDesc,
error);

        int apiLevel = IExactApiLevelDependency.API_LEVEL_INVALID;

try {
            apiLevel = Integer.parseInt(api);
} catch(NumberFormatException e) {
// ignore
}

        return new BrokenPackage(null/*props*/, shortDesc, longDesc,
                IMinApiLevelDependency.MIN_API_LEVEL_NOT_SPECIFIED,
                apiLevel,
                archiveOsPath);
    }

    public int getExactApiLevel() {
        return mVersion.getApiLevel();
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/BrokenPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/BrokenPackage.java
//Synthetic comment -- index 646172d..1629045 100755

//Synthetic comment -- @@ -27,14 +27,21 @@
* Represents an SDK repository package that is incomplete.
* It has a distinct icon and a specific error that is supposed to help the user on how to fix it.
*/
public class BrokenPackage extends Package
        implements IExactApiLevelDependency, IMinApiLevelDependency {

/**
     * The minimal API level required by this package, if > 0,
* or {@link #MIN_API_LEVEL_NOT_SPECIFIED} if there is no such requirement.
*/
private final int mMinApiLevel;

    /**
     * The exact API level required by this package, if > 0,
     * or {@link #API_LEVEL_INVALID} if there is no such requirement.
     */
    private final int mExactApiLevel;

private final String mShortDescription;

/**
//Synthetic comment -- @@ -48,6 +55,7 @@
String shortDescription,
String longDescription,
int minApiLevel,
            int exactApiLevel,
String archiveOsPath) {
super(  null,                                   //source
props,                                  //properties
//Synthetic comment -- @@ -61,6 +69,7 @@
);
mShortDescription = shortDescription;
mMinApiLevel = minApiLevel;
        mExactApiLevel = exactApiLevel;
}

/**
//Synthetic comment -- @@ -75,13 +84,21 @@
}

/**
     * Returns the minimal API level required by this package, if > 0,
* or {@link #MIN_API_LEVEL_NOT_SPECIFIED} if there is no such requirement.
*/
public int getMinApiLevel() {
return mMinApiLevel;
}

    /**
     * Returns the exact API level required by this package, if > 0,
     * or {@link #API_LEVEL_INVALID} if the value was missing.
     */
    public int getExactApiLevel() {
        return mExactApiLevel;
    }

/** Returns a short description for an {@link IDescription}. */
@Override
public String getShortDescription() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java
//Synthetic comment -- index cac622d..9c94800 100755

//Synthetic comment -- @@ -113,7 +113,9 @@
ep.getPath());

BrokenPackage ba = new BrokenPackage(props, shortDesc, longDesc,
                    ep.getMinApiLevel(),
                    IExactApiLevelDependency.API_LEVEL_INVALID,
                    archiveOsPath);
return ba;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IExactApiLevelDependency.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IExactApiLevelDependency.java
new file mode 100755
//Synthetic comment -- index 0000000..2a0130c

//Synthetic comment -- @@ -0,0 +1,49 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.internal.repository;

import com.android.sdklib.repository.RepoConstants;

/**
 * Interface used to decorate a {@link Package} that has a dependency
 * on a specific API level, e.g. which XML has a {@code <api-level>} element.
 * <p/>
 * For example an add-on package requires a platform with an exact API level to be installed
 * at the same time.
 * This is not the same as {@link IMinApiLevelDependency} which requests that a platform with at
 * least the requested API level be present or installed at the same time.
 * <p/>
 * Such package requires the {@code <api-level>} element. It is not an optional
 * property, however it can be invalid.
 */
public interface IExactApiLevelDependency {

    /**
     * The value of {@link #getExactApiLevel()} when the {@link RepoConstants#NODE_API_LEVEL}
     * was not specified in the XML source.
     */
    public static final int API_LEVEL_INVALID = 0;

    /**
     * Returns the exact API level required by this package, if > 0,
     * or {@link #API_LEVEL_INVALID} if the value was missing.
     * <p/>
     * This attribute is mandatory and should not be normally missing.
     * It can only happen when dealing with an invalid repository XML.
     */
    public abstract int getExactApiLevel();
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IMinApiLevelDependency.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IMinApiLevelDependency.java
//Synthetic comment -- index 14e6744..e23f3b6 100755

//Synthetic comment -- @@ -34,7 +34,7 @@
public static final int MIN_API_LEVEL_NOT_SPECIFIED = 0;

/**
     * Returns the minimal API level required by this package, if > 0,
* or {@link #MIN_API_LEVEL_NOT_SPECIFIED} if there is no such requirement.
*/
public abstract int getMinApiLevel();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformToolPackage.java
//Synthetic comment -- index 7b0494f..c31325f 100755

//Synthetic comment -- @@ -111,7 +111,9 @@
error);

BrokenPackage ba = new BrokenPackage(props, shortDesc, longDesc,
                    IMinApiLevelDependency.MIN_API_LEVEL_NOT_SPECIFIED,
                    IExactApiLevelDependency.API_LEVEL_INVALID,
                    archiveOsPath);
return ba;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/BrokenPackageTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/BrokenPackageTest.java
new file mode 100755
//Synthetic comment -- index 0000000..3e9ba8d

//Synthetic comment -- @@ -0,0 +1,54 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.internal.repository;

import junit.framework.TestCase;

public class BrokenPackageTest extends TestCase {

    private BrokenPackage m;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        m = new BrokenPackage(null /*props*/,
                "short description",
                "long description",
                12, // min api level
                13, // exact api level
                "os/path");

    }

    public final void testGetShortDescription() {
        assertEquals("short description", m.getShortDescription());
    }

    public final void testGetLongDescription() {
        assertEquals("long description", m.getLongDescription());
    }

    public final void testGetMinApiLevel() {
        assertEquals(12, m.getMinApiLevel());
    }

    public final void testGetExactApiLevel() {
        assertEquals(13, m.getExactApiLevel());
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockBrokenPackage.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockBrokenPackage.java
new file mode 100755
//Synthetic comment -- index 0000000..289305b

//Synthetic comment -- @@ -0,0 +1,43 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.internal.repository;


/**
 * A mock {@link BrokenPackage} for testing.
 * <p/>
 * By design, this package contains one and only one archive.
 */
public class MockBrokenPackage extends BrokenPackage {

    public MockBrokenPackage(int minApiLevel, int exactApiLevel) {
        this(null, null, minApiLevel, exactApiLevel);
    }

    public MockBrokenPackage(
            String shortDescription,
            String longDescription,
            int minApiLevel,
            int exactApiLevel) {
        super(null /*props*/,
                shortDescription,
                longDescription,
                minApiLevel,
                exactApiLevel,
                null /*osPath*/);
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index f57213c..505a613 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.DocPackage;
import com.android.sdklib.internal.repository.ExtraPackage;
import com.android.sdklib.internal.repository.IExactApiLevelDependency;
import com.android.sdklib.internal.repository.IMinApiLevelDependency;
import com.android.sdklib.internal.repository.IMinPlatformToolsDependency;
import com.android.sdklib.internal.repository.IMinToolsDependency;
//Synthetic comment -- @@ -32,9 +33,9 @@
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.PlatformPackage;
import com.android.sdklib.internal.repository.PlatformToolPackage;
import com.android.sdklib.internal.repository.SamplePackage;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdklib.internal.repository.ToolPackage;
import com.android.sdklib.internal.repository.Package.UpdateInfo;

//Synthetic comment -- @@ -501,6 +502,21 @@
}
}

        if (pkg instanceof IExactApiLevelDependency) {

            ArchiveInfo ai = findExactApiLevelDependency(
                    (IExactApiLevelDependency) pkg,
                    outArchives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives);

            if (ai != null) {
                list.add(ai);
            }
        }

if (list.size() > 0) {
return list.toArray(new ArchiveInfo[list.size()]);
}
//Synthetic comment -- @@ -864,7 +880,7 @@

int api = pkg.getMinApiLevel();

        if (api == IMinApiLevelDependency.MIN_API_LEVEL_NOT_SPECIFIED) {
return null;
}

//Synthetic comment -- @@ -963,6 +979,104 @@
}

/**
     * Resolves platform dependencies for add-ons.
     * An add-ons depends on having a platform with an exact specific API level.
     *
     * Finds the platform dependency. If found, add it to the list of things to install.
     * Returns the archive info dependency, if any.
     */
    protected ArchiveInfo findExactApiLevelDependency(
            IExactApiLevelDependency pkg,
            ArrayList<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            ArrayList<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {

        int api = pkg.getExactApiLevel();

        if (api == IExactApiLevelDependency.API_LEVEL_INVALID) {
            return null;
        }

        // Find a platform that would satisfy the requirement.

        // First look in locally installed packages.
        for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getVersion().equals(api)) {
                        // We found one already installed.
                        return null;
                    }
                }
            }
        }

        // Look in archives already scheduled for install

        for (ArchiveInfo ai : outArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getVersion().equals(api)) {
                        return ai;
                    }
                }
            }
        }

        // Otherwise look in the selected archives.
        if (selectedArchives != null) {
            for (Archive a : selectedArchives) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getVersion().equals(api)) {
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
            if (p instanceof PlatformPackage) {
                if (((PlatformPackage) p).getVersion().equals(api)) {
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
        // so that it can be impossible to install later on.
        return new MissingPlatformArchiveInfo(new AndroidVersion(api, null /*codename*/));
    }

    /**
* Fetch all remote packages only if really needed.
* <p/>
* This method takes a list of sources. Each source is only fetched once -- that is each








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java
//Synthetic comment -- index 94fac06..dc65e14 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.MockAddonPackage;
import com.android.sdklib.internal.repository.MockBrokenPackage;
import com.android.sdklib.internal.repository.MockPlatformPackage;
import com.android.sdklib.internal.repository.MockPlatformToolPackage;
import com.android.sdklib.internal.repository.MockToolPackage;
//Synthetic comment -- @@ -132,6 +133,54 @@
}

/**
     * Broken add-on packages require an exact platform package to be present or installed.
     * This tests checks that findExactApiLevelDependency() can find a base
     * platform package for a given broken add-on package.
     */
    public void testFindExactApiLevelDependency() {
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(), null);

        MockPlatformPackage p1 = new MockPlatformPackage(1, 1);
        MockPlatformPackage p2 = new MockPlatformPackage(2, 1);

        MockBrokenPackage a1 = new MockBrokenPackage(0, 1);
        MockBrokenPackage a2 = new MockBrokenPackage(0, 2);

        ArrayList<ArchiveInfo> out = new ArrayList<ArchiveInfo>();
        ArrayList<Archive> selected = new ArrayList<Archive>();
        ArrayList<Package> remote = new ArrayList<Package>();

        // a2 depends on p2, which is not in the locals
        Package[] localPkgs = { p1, a1 };
        ArchiveInfo[] locals = mul.createLocalArchives(localPkgs);

        SdkSource[] sources = null;

        // a1 depends on p1, which can be found in the locals. p1 is already "installed"
        // so we donn't need to suggest it as a dependency to solve any problem.
        ArchiveInfo found = mul.findExactApiLevelDependency(
                a1, out, selected, remote, sources, locals);
        assertNull(found);

        // a2 now depends on a "fake" archive info with no newArchive that wraps the missing
        // underlying platform.
        found = mul.findExactApiLevelDependency(a2, out, selected, remote, sources, locals);
        assertNotNull(found);
        assertNull(found.getNewArchive());
        assertTrue(found.isRejected());
        assertEquals(0, out.size());

        // p2 is now selected, and should be scheduled for install in out
        Archive p2_archive = p2.getArchives()[0];
        selected.add(p2_archive);
        found = mul.findExactApiLevelDependency(a2, out, selected, remote, sources, locals);
        assertNotNull(found);
        assertSame(p2_archive, found.getNewArchive());
        assertEquals(1, out.size());
        assertSame(p2_archive, out.get(0).getNewArchive());
    }

    /**
* Platform packages depend on a tool package.
* This tests checks that UpdaterLogic.findToolsDependency() can find a base
* tool package for a given platform package.







