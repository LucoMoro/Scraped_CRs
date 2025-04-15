/*UnitTest for UpdaterLogic.findPlatformToolsDependency.

Change-Id:Ie49a141ad0d25021164166aeb4405877045719bf*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockPlatformToolPackage.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockPlatformToolPackage.java
new file mode 100755
//Synthetic comment -- index 0000000..f3a4259

//Synthetic comment -- @@ -0,0 +1,49 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockToolPackage.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockToolPackage.java
//Synthetic comment -- index e1ce621..8ce704c 100755

//Synthetic comment -- @@ -19,6 +19,8 @@
import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;

/**
* A mock {@link ToolPackage} for testing.
*
//Synthetic comment -- @@ -32,10 +34,10 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    public MockToolPackage(int revision) {
super(
null, // source,
            null, // props,
revision,
null, // license,
"desc", // description,
//Synthetic comment -- @@ -45,4 +47,11 @@
"foo" // archiveOsPath
);
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java
//Synthetic comment -- index 4c86c3e..f5e3c1f 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.MockAddonPackage;
import com.android.sdklib.internal.repository.MockPlatformPackage;
import com.android.sdklib.internal.repository.MockToolPackage;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.RepoSource;
//Synthetic comment -- @@ -48,7 +49,12 @@
}
}

    public void testFindAddonDependency() throws Exception {
MockUpdaterLogic mul = new MockUpdaterLogic(null);

MockPlatformPackage p1 = new MockPlatformPackage(1, 1);
//Synthetic comment -- @@ -68,7 +74,7 @@
RepoSource[] sources = null;

// a2 now depends on a "fake" archive info with no newArchive that wraps the missing
        // underlying platform
ArchiveInfo fai = mul.findPlatformDependency(a2, out, selected, remote, sources, locals);
assertNotNull(fai);
assertNull(fai.getNewArchive());
//Synthetic comment -- @@ -85,11 +91,18 @@
assertSame(p2_archive, out.get(0).getNewArchive());
}

    public void testFindPlatformDependency() throws Exception {
MockUpdaterLogic mul = new MockUpdaterLogic(null);

        MockToolPackage t1 = new MockToolPackage(1);
        MockToolPackage t2 = new MockToolPackage(2);

MockPlatformPackage p2 = new MockPlatformPackage(2, 1, 2);

//Synthetic comment -- @@ -98,7 +111,7 @@
ArrayList<Package> remote = new ArrayList<Package>();

// p2 depends on t2, which is not locally installed
        Package[] localPkgs = { t1 };
ArchiveInfo[] locals = mul.createLocalArchives(localPkgs);

RepoSource[] sources = null;
//Synthetic comment -- @@ -120,4 +133,45 @@
assertEquals(1, out.size());
assertSame(t2_archive, out.get(0).getNewArchive());
}
}







