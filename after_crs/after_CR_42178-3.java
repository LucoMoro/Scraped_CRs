/*Add file permission tests for broadcom nfc drivers.

	modified:   tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java

Change-Id:If0db3d01cf324738c002425ed7c4d68d7d5719b8*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java b/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java
//Synthetic comment -- index 0ffbe3c..5e7b540 100644

//Synthetic comment -- @@ -166,11 +166,69 @@
assertFalse(f.canWrite());
assertFalse(f.canExecute());

        assertFileOwnedBy(f, "nfc");
        assertFileOwnedByGroup(f, "nfc");
    }

    @MediumTest
    public void testBcm2079xSane() throws Exception {
        File f = new File("/dev/bcm2079x");
        assertFalse(f.canRead());
        assertFalse(f.canWrite());
        assertFalse(f.canExecute());

        assertFileOwnedBy(f, "nfc");
        assertFileOwnedByGroup(f, "nfc");
    }

    @MediumTest
    public void testBcm2079xi2cSane() throws Exception {
        File f = new File("/dev/bcm2079x-i2c");
        assertFalse(f.canRead());
        assertFalse(f.canWrite());
        assertFalse(f.canExecute());

        assertFileOwnedBy(f, "nfc");
        assertFileOwnedByGroup(f, "nfc");
    }

    /**
     * Assert that a file is owned by a specific owner. This is a noop if the
     * file does not exist.
     *
     * @param file The file to check.
     * @param expectedOwner The owner of the file.
     */
    private static void assertFileOwnedBy(File file, String expectedOwner) {
FileUtils.FileStatus status = new FileUtils.FileStatus();
        String path = file.getAbsolutePath();
        if (file.exists() && FileUtils.getFileStatus(path, status, true)) {
            String actualOwner = FileUtils.getUserName(status.uid);
            if (!expectedOwner.equals(actualOwner)) {
                String msg = String.format("Wrong owner. Expected '%s', but found '%s' for %s.",
                        expectedOwner, actualOwner, path);
                fail(msg);
            }
        }
    }

    /**
     * Assert that a file is owned by a specific group. This is a noop if the
     * file does not exist.
     *
     * @param file The file to check.
     * @param expectedGroup The owner group of the file.
     */
    private static void assertFileOwnedByGroup(File file, String expectedGroup) {
        FileUtils.FileStatus status = new FileUtils.FileStatus();
        String path = file.getAbsolutePath();
        if (file.exists() && FileUtils.getFileStatus(path, status, true)) {
            String actualGroup = FileUtils.getGroupName(status.gid);
            if (!expectedGroup.equals(actualGroup)) {
                String msg = String.format("Wrong group. Expected '%s', but found '%s' for %s.",
                        expectedGroup, actualGroup, path);
                fail(msg);
            }
}
}








