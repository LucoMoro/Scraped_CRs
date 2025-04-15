/*When possible use File.setExecutable instead of doing an exec of chmod.

Change-Id:I2b2f79bb07e277a1f2caa62b4100cbd8f7e8328d*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/OsHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/OsHelper.java
//Synthetic comment -- index dfd2587..c2338f3 100755

//Synthetic comment -- @@ -22,6 +22,8 @@
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
//Synthetic comment -- @@ -30,6 +32,21 @@
abstract class OsHelper {

/**
* Helper to delete a file or a directory.
* For a directory, recursively deletes all of its content.
* Files that cannot be deleted right away are marked for deletion on exit.
//Synthetic comment -- @@ -78,16 +95,47 @@
/**
* Sets the executable Unix permission (+x) on a file or folder.
* <p/>
     * This invokes a chmod exec, so there is no guarantee of it being fast.
* Caller must make sure to not invoke this under Windows.
*
* @param file The file to set permissions on.
* @throws IOException If an I/O error occurs
*/
static void setExecutablePermission(File file) throws IOException {
Runtime.getRuntime().exec(new String[] {
           "chmod", "+x", file.getAbsolutePath()
        });
}

/**







