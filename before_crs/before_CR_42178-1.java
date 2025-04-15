/*Add file permission tests for broadcom nfc drivers.

	modified:   tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java

Change-Id:If0db3d01cf324738c002425ed7c4d68d7d5719b8*/
//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java b/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java
//Synthetic comment -- index 0ffbe3c..6a4765e 100644

//Synthetic comment -- @@ -166,15 +166,51 @@
assertFalse(f.canWrite());
assertFalse(f.canExecute());

        FileUtils.FileStatus status = new FileUtils.FileStatus();
        if (f.exists()
                && FileUtils.getFileStatus(f.getAbsolutePath(), status, true)) {
            assertEquals("nfc", FileUtils.getUserName(status.uid));
            assertEquals("nfc", FileUtils.getGroupName(status.gid));
}
}

@MediumTest
public void testTtyO3Sane() throws Exception {
File f = new File("/dev/ttyO3");
assertFalse(f.canRead());







