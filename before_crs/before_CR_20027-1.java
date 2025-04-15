/*Test only apk files of /system/app directory.
Implementer may put some file rather than apk in /system/app.
So it should check only apk files.

Change-Id:Ia5dea75f044c5f09010d864796e294c850fbd53c*/
//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/FileAccessPermissionTest.java b/tests/tests/os/src/android/os/cts/FileAccessPermissionTest.java
//Synthetic comment -- index ffed104..e3536a4 100644

//Synthetic comment -- @@ -21,6 +21,7 @@

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -65,7 +66,16 @@
file = new File("/system/app");
assertTrue(file.canRead());
assertFalse(file.canWrite());
        File[] apkFiles = file.listFiles();
for (File f : apkFiles) {
assertTrue(f.canRead());
assertFalse(f.canWrite());







