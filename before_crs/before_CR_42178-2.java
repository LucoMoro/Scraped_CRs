/*Add file permission tests for broadcom nfc drivers.

	modified:   tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java

Change-Id:If0db3d01cf324738c002425ed7c4d68d7d5719b8*/
//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java b/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java
//Synthetic comment -- index 0ffbe3c..3f26edc 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import android.content.pm.PackageManager;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.LargeTest;

//Synthetic comment -- @@ -166,11 +167,75 @@
assertFalse(f.canWrite());
assertFalse(f.canExecute());

FileUtils.FileStatus status = new FileUtils.FileStatus();
        if (f.exists()
                && FileUtils.getFileStatus(f.getAbsolutePath(), status, true)) {
            assertEquals("nfc", FileUtils.getUserName(status.uid));
            assertEquals("nfc", FileUtils.getGroupName(status.gid));
}
}








