/*Test nfc driver owner permissions

	modified:   tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java

Change-Id:I90ac2c4389593fd387bae47c82aa74efb354cf37*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java b/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java
//Synthetic comment -- index 51f7e7e..a492e0b 100644

//Synthetic comment -- @@ -165,6 +165,14 @@
assertFalse(f.canRead());
assertFalse(f.canWrite());
assertFalse(f.canExecute());

        FileUtils.FileStatus status = new FileUtils.FileStatus();
        if (FileUtils.getFileStatus(f.getAbsolutePath(), status, true)) {
            assertEquals("nfc", FileUtils.getUserName(status.uid));
            assertEquals("nfc", FileUtils.getGroupName(status.gid));
        } else {
            assertFalse(f.exists());
        }
}

@MediumTest







