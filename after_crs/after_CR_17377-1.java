/*DO NOT MERGE FileSystemPermissionTest Froyo Backport

Bug 2749525

Change-Id:Ib276a471c04de6299592989807bae42ed7530178*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java b/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java
new file mode 100644
//Synthetic comment -- index 0000000..3dfcad4

//Synthetic comment -- @@ -0,0 +1,130 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

package android.permission.cts;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Verify certain permissions on the filesystem
 */
@MediumTest
public class FileSystemPermissionTest extends AndroidTestCase {

    public void testCreateFileHasSanePermissions() throws Exception {
        File myFile = new File(getContext().getFilesDir(), "hello");
        FileOutputStream stream = new FileOutputStream(myFile);
        stream.write("hello world".getBytes());
        stream.close();
        try {
            FileUtils.FileStatus status = new FileUtils.FileStatus();
            FileUtils.getFileStatus(myFile.getAbsolutePath(), status, false);
            int expectedPerms = FileUtils.S_IFREG
                    | FileUtils.S_IWUSR
                    | FileUtils.S_IRUSR;
            assertEquals(
                    "Newly created files should have 0600 permissions",
                    Integer.toOctalString(expectedPerms),
                    Integer.toOctalString(status.mode));
        } finally {
            assertTrue(myFile.delete());
        }
    }

    public void testCreateDirectoryHasSanePermissions() throws Exception {
        File myDir = new File(getContext().getFilesDir(), "helloDirectory");
        assertTrue(myDir.mkdir());
        try {
            FileUtils.FileStatus status = new FileUtils.FileStatus();
            FileUtils.getFileStatus(myDir.getAbsolutePath(), status, false);
            int expectedPerms = FileUtils.S_IFDIR
                    | FileUtils.S_IWUSR
                    | FileUtils.S_IRUSR
                    | FileUtils.S_IXUSR;
            assertEquals(
                    "Newly created directories should have 0700 permissions",
                    Integer.toOctalString(expectedPerms),
                    Integer.toOctalString(status.mode));

        } finally {
            assertTrue(myDir.delete());
        }
    }

    public void testOtherApplicationDirectoriesAreNotWritable() throws Exception {
        List<ApplicationInfo> apps = getContext()
                .getPackageManager()
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        String myAppDirectory = getContext().getApplicationInfo().dataDir;
        for (ApplicationInfo app : apps) {
            if (!myAppDirectory.equals(app.dataDir)) {
                assertDirectoryNotWritable(new File(app.dataDir));
            }
        }
    }

    public void testApplicationParentDirectoryNotWritable() throws Exception {
        String myDataDir = getContext().getApplicationInfo().dataDir;
        File parentDir = new File(myDataDir).getParentFile();
        assertDirectoryNotWritable(parentDir);
    }

    public void testDataDirectoryNotWritable() throws Exception {
        assertDirectoryNotWritable(Environment.getDataDirectory());
    }

    public void testAndroidRootDirectoryNotWritable() throws Exception {
        assertDirectoryNotWritable(Environment.getRootDirectory());
    }

    public void testDownloadCacheDirectoryNotWritable() throws Exception {
        assertDirectoryNotWritable(Environment.getDownloadCacheDirectory());
    }

    public void testRootDirectoryNotWritable() throws Exception {
        assertDirectoryNotWritable(new File("/"));
    }

    public void testDevDirectoryNotWritable() throws Exception {
        assertDirectoryNotWritable(new File("/dev"));
    }

    public void testProcDirectoryNotWritable() throws Exception {
        assertDirectoryNotWritable(new File("/proc"));
    }

    private static void assertDirectoryNotWritable(File directory) throws Exception {
        File toCreate = new File(directory, "hello");
        try {
            toCreate.createNewFile();
            fail("Expected \"java.io.IOException: Permission denied\""
                 + " when trying to create " + toCreate.getAbsolutePath());
        } catch (IOException e) {
            // It's expected we'll get a "Permission denied" exception.
        } finally {
            toCreate.delete();
        }
    }
}







