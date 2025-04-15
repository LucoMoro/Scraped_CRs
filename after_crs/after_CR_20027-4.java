/*Add a new test testApksAlwaysReadable()

Now testSystemDirAccess() will test not writable/deletable
for all files in /system/app.

Change-Id:Ia5dea75f044c5f09010d864796e294c850fbd53c*/




//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/FileAccessPermissionTest.java b/tests/tests/os/src/android/os/cts/FileAccessPermissionTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index ffed104..cd89e3d

//Synthetic comment -- @@ -21,6 +21,7 @@

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -65,15 +66,33 @@
file = new File("/system/app");
assertTrue(file.canRead());
assertFalse(file.canWrite());

        // Test not writable / deletable for all files
        File[] allFiles = file.listFiles();
        for (File f : allFiles) {
assertFalse(f.canWrite());
assertFalse(f.delete());
}
}

/**
     * Test apks in /system/app.
     */
    public void testApksAlwaysReadable() {
        File file = new File("/system/app");

        // Test readable for only apk files
        File[] apkFiles = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".apk");
            }
        });
        for (File f : apkFiles) {
            assertTrue(f.canRead());
        }
    }

    /**
* Test dir which app can and cannot access.
*/
public void testAccessAppDataDir() {







