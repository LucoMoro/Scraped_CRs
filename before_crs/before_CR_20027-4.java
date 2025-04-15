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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -65,15 +66,33 @@
file = new File("/system/app");
assertTrue(file.canRead());
assertFalse(file.canWrite());
        File[] apkFiles = file.listFiles();
        for (File f : apkFiles) {
            assertTrue(f.canRead());
assertFalse(f.canWrite());
assertFalse(f.delete());
}
}

/**
* Test dir which app can and cannot access.
*/
public void testAccessAppDataDir() {







