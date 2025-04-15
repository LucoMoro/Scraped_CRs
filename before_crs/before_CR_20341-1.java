/*Add a new test testApksAlwaysReadable()

Now testSystemDirAccess() will test not writable/deletable
for all files in /system/app.

Change-Id:I4ad2b32ff00f54ccee43228c8bdf0c136bc297f7*/
//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/FileAccessPermissionTest.java b/tests/tests/os/src/android/os/cts/FileAccessPermissionTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index cb1f0ab..c5418bc

//Synthetic comment -- @@ -21,6 +21,7 @@

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -63,15 +64,33 @@
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







