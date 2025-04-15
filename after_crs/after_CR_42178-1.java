/*Add file permission tests for broadcom nfc drivers.

	modified:   tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java

Change-Id:If0db3d01cf324738c002425ed7c4d68d7d5719b8*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java b/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java
//Synthetic comment -- index 0ffbe3c..6a4765e 100644

//Synthetic comment -- @@ -166,15 +166,51 @@
assertFalse(f.canWrite());
assertFalse(f.canExecute());

        if (f.exists()) {
            assertTrue(isFileOwner(f, "nfc"));
            assertTrue(isFileGroup(f, "nfc"));
}
}

@MediumTest
    public void testBcm2079xSane() throws Exception {
        File f = new File("/dev/bcm2079x");
        assertFalse(f.canRead());
        assertFalse(f.canWrite());
        assertFalse(f.canExecute());

        if (f.exists()) {
            assertTrue(isFileOwner(f, "nfc"));
            assertTrue(isFileGroup(f, "nfc"));
        }
    }

    @MediumTest
    public void testBcm2079xi2cSane() throws Exception {
        File f = new File("/dev/bcm2079x-i2c");
        assertFalse(f.canRead());
        assertFalse(f.canWrite());
        assertFalse(f.canExecute());

        if (f.exists()) {
            assertTrue(isFileOwner(f, "nfc"));
            assertTrue(isFileGroup(f, "nfc"));
        }
    }

    private static boolean isFileOwner(File file, String expectedOwner) {
        FileUtils.FileStatus status = new FileUtils.FileStatus();
        return FileUtils.getFileStatus(file.getAbsolutePath(), status, true)
            && FileUtils.getUserName(status.uid).equals(expectedOwner);
    }

    private static boolean isFileGroup(File file, String expectedGroup) {
        FileUtils.FileStatus status = new FileUtils.FileStatus();
        return FileUtils.getFileStatus(file.getAbsolutePath(), status, true)
            && FileUtils.getGroupName(status.gid).equals(expectedGroup);
    }

    @MediumTest
public void testTtyO3Sane() throws Exception {
File f = new File("/dev/ttyO3");
assertFalse(f.canRead());







