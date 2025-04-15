/*UnitTest for UpdaterLogic.findPlatformToolsDependency.

Change-Id:Ie49a141ad0d25021164166aeb4405877045719bf*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockPlatformToolPackage.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockPlatformToolPackage.java
new file mode 100755
//Synthetic comment -- index 0000000..f3a4259

//Synthetic comment -- @@ -0,0 +1,49 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
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

import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;

/**
 * A mock {@link PlatformToolPackage} for testing.
 *
 * By design, this package contains one and only one archive.
 */
public class MockPlatformToolPackage extends PlatformToolPackage {

    /**
     * Creates a {@link MockPlatformToolPackage} with the given revision and hardcoded defaults
     * for everything else.
     * <p/>
     * By design, this creates a package with one and only one archive.
     */
    public MockPlatformToolPackage(int revision) {
        super(
            null, // source,
            null, // props,
            revision,
            null, // license,
            "desc", // description,
            "url", // descUrl,
            Os.getCurrentOs(), // archiveOs,
            Arch.getCurrentArch(), // archiveArch,
            "foo" // archiveOsPath
            );
    }
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockToolPackage.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockToolPackage.java
//Synthetic comment -- index e1ce621..8ce704c 100755

//Synthetic comment -- @@ -19,6 +19,8 @@
import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;

import java.util.Properties;

/**
* A mock {@link ToolPackage} for testing.
*
//Synthetic comment -- @@ -32,10 +34,10 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    public MockToolPackage(int revision, int min_platform_tools_rev) {
super(
null, // source,
            createProps(min_platform_tools_rev), // props,
revision,
null, // license,
"desc", // description,
//Synthetic comment -- @@ -45,4 +47,11 @@
"foo" // archiveOsPath
);
}

    private static Properties createProps(int min_platform_tools_rev) {
        Properties props = new Properties();
        props.setProperty(ToolPackage.PROP_MIN_PLATFORM_TOOLS_REV,
                          Integer.toString((min_platform_tools_rev)));
        return props;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java
//Synthetic comment -- index 4c86c3e..f5e3c1f 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.MockAddonPackage;
import com.android.sdklib.internal.repository.MockPlatformPackage;
import com.android.sdklib.internal.repository.MockPlatformToolPackage;
import com.android.sdklib.internal.repository.MockToolPackage;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.RepoSource;
//Synthetic comment -- @@ -48,7 +49,12 @@
}
}

    /**
     * Addon packages depend on a base platform package.
     * This test checks that UpdaterLogic.findPlatformToolsDependency(...)
     * can find the base platform for a given addon.
     */
    public void testFindAddonDependency() {
MockUpdaterLogic mul = new MockUpdaterLogic(null);

MockPlatformPackage p1 = new MockPlatformPackage(1, 1);
//Synthetic comment -- @@ -68,7 +74,7 @@
RepoSource[] sources = null;

// a2 now depends on a "fake" archive info with no newArchive that wraps the missing
        // underlying platform.
ArchiveInfo fai = mul.findPlatformDependency(a2, out, selected, remote, sources, locals);
assertNotNull(fai);
assertNull(fai.getNewArchive());
//Synthetic comment -- @@ -85,11 +91,18 @@
assertSame(p2_archive, out.get(0).getNewArchive());
}

    /**
     * Platform packages depend on a tool package.
     * This tests checks that UpdaterLogic.findToolsDependency() can find a base
     * tool package for a given platform package.
     */
    public void testFindPlatformDependency() {
MockUpdaterLogic mul = new MockUpdaterLogic(null);

        MockPlatformToolPackage pt1 = new MockPlatformToolPackage(1);

        MockToolPackage t1 = new MockToolPackage(1, 1);
        MockToolPackage t2 = new MockToolPackage(2, 1);

MockPlatformPackage p2 = new MockPlatformPackage(2, 1, 2);

//Synthetic comment -- @@ -98,7 +111,7 @@
ArrayList<Package> remote = new ArrayList<Package>();

// p2 depends on t2, which is not locally installed
        Package[] localPkgs = { t1, pt1 };
ArchiveInfo[] locals = mul.createLocalArchives(localPkgs);

RepoSource[] sources = null;
//Synthetic comment -- @@ -120,4 +133,45 @@
assertEquals(1, out.size());
assertSame(t2_archive, out.get(0).getNewArchive());
}

    /**
     * Tool packages require a platform-tool package to be present or installed.
     * This tests checks that UpdaterLogic.findPlatformToolsDependency() can find a base
     * platform-tool package for a given tool package.
     */
    public void testFindPlatformToolDependency() {
        MockUpdaterLogic mul = new MockUpdaterLogic(null);

        MockPlatformToolPackage t1 = new MockPlatformToolPackage(1);
        MockPlatformToolPackage t2 = new MockPlatformToolPackage(2);

        MockToolPackage p2 = new MockToolPackage(2, 2);

        ArrayList<ArchiveInfo> out = new ArrayList<ArchiveInfo>();
        ArrayList<Archive> selected = new ArrayList<Archive>();
        ArrayList<Package> remote = new ArrayList<Package>();

        // p2 depends on t2, which is not locally installed
        Package[] localPkgs = { t1 };
        ArchiveInfo[] locals = mul.createLocalArchives(localPkgs);

        RepoSource[] sources = null;

        // p2 now depends on a "fake" archive info with no newArchive that wraps the missing
        // underlying tool
        ArchiveInfo fai = mul.findPlatformToolsDependency(p2, out, selected, remote, sources, locals);
        assertNotNull(fai);
        assertNull(fai.getNewArchive());
        assertTrue(fai.isRejected());
        assertEquals(0, out.size());

        // t2 is now selected and can be used as a dependency
        Archive t2_archive = t2.getArchives()[0];
        selected.add(t2_archive);
        ArchiveInfo ai2 = mul.findPlatformToolsDependency(p2, out, selected, remote, sources, locals);
        assertNotNull(ai2);
        assertSame(t2_archive, ai2.getNewArchive());
        assertEquals(1, out.size());
        assertSame(t2_archive, out.get(0).getNewArchive());
    }
}







