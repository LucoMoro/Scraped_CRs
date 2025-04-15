/*Test nfc driver owner permissions

	modified:   tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java

Change-Id:I90ac2c4389593fd387bae47c82aa74efb354cf37*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java b/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java
//Synthetic comment -- index 51f7e7e..bc049f2 100644

//Synthetic comment -- @@ -165,6 +165,13 @@
assertFalse(f.canRead());
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







