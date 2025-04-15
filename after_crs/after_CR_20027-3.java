/*Test only apk files of /system/app directory.

Implementer may put some file rather than apk in /system/app.
So it should check only apk files.

Change-Id:Ia5dea75f044c5f09010d864796e294c850fbd53c*/




//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/FileAccessPermissionTest.java b/tests/tests/os/src/android/os/cts/FileAccessPermissionTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index ffed104..67a15bb

//Synthetic comment -- @@ -21,6 +21,7 @@

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -65,12 +66,28 @@
file = new File("/system/app");
assertTrue(file.canRead());
assertFalse(file.canWrite());

        // Test un-rewritable for all files
        File[] allFiles = file.listFiles();
        for (File f : allFiles) {
assertFalse(f.canWrite());
assertFalse(f.delete());
}

        // Test readable for only apk files
        File[] apkFiles = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".apk")) {
                    // retrieve only apk files
                    return true;
                }
                return false;
            }
        });
        for (File f : apkFiles) {
            assertTrue(f.canRead());
        }
}

/**







