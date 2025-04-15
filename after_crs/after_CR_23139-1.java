/*Use sdklib.OsHelper in AddCompatibilityJarAction.

ADT's AddCompatibilityJarAction defined 2 new generic
useful methods, copyFile and isSameFile. SdkLib already
had an OsHelper.copyFile, so I moved that class to a
more generic sdklib.io package and added the new method
isSameFile.

Another suitable candidate would have been io.FileWrapper
in the common project.

Change-Id:If310e09af112c5f4d87a253b35e67e4f5adb34da*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AddCompatibilityJarAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AddCompatibilityJarAction.java
//Synthetic comment -- index 118e70a..2f4bd10 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.io.OsHelper;
import com.android.sdkuilib.internal.repository.AdtUpdateDialog;
import com.android.util.Pair;

//Synthetic comment -- @@ -50,11 +51,7 @@
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
//Synthetic comment -- @@ -222,8 +219,8 @@
File destPath = loc.toFile();

// Only modify the file if necessary so that we don't trigger unnecessary recompilations
        if (!destPath.isFile() || !OsHelper.isSameFile(jarPath, destPath)) {
            OsHelper.copyFile(jarPath, destPath);
// Make sure Eclipse discovers java.io file changes
resFolder.refreshLocal(1, new NullProgressMonitor());
}
//Synthetic comment -- @@ -232,106 +229,6 @@
}

/**
* @see IWorkbenchWindowActionDelegate#init
*/
public void init(IWorkbenchWindow window) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java
//Synthetic comment -- index 96d5cf5..4a21851 100755

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.io.OsHelper;

import java.io.File;
import java.security.MessageDigest;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java
//Synthetic comment -- index 5807ca3..552c7bb 100755

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.io.OsHelper;
import com.android.sdklib.repository.RepoConstants;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
//Synthetic comment -- @@ -854,7 +855,9 @@
if (file.isFile()) {
File f = new File(destFolder, file.getName());
destFiles.remove(f);
                try {
                    OsHelper.copyFile(file, f);
                } catch (IOException e) {
result = false;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/OsHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/OsHelper.java
similarity index 69%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/OsHelper.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/io/OsHelper.java
//Synthetic comment -- index c2338f3..1b36dc0 100755

//Synthetic comment -- @@ -14,22 +14,24 @@
* limitations under the License.
*/

package com.android.sdklib.io;

import com.android.sdklib.SdkConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;


/**
* Helper methods used when dealing with archives installation.
*/
public abstract class OsHelper {

/**
* Reflection method for File.setExecutable(boolean, boolean). Only present in Java 6.
//Synthetic comment -- @@ -52,7 +54,7 @@
* Files that cannot be deleted right away are marked for deletion on exit.
* The argument can be null.
*/
    public static void deleteFileOrFolder(File fileOrFolder) {
if (fileOrFolder != null) {
if (fileOrFolder.isDirectory()) {
// Must delete content recursively first
//Synthetic comment -- @@ -95,7 +97,7 @@
/**
* Sets the executable Unix permission (+x) on a file or folder.
* <p/>
     * This attempts to use File#setExecutable through reflection if
* it's available.
* If this is not available, this invokes a chmod exec instead,
* so there is no guarantee of it being fast.
//Synthetic comment -- @@ -105,7 +107,7 @@
* @param file The file to set permissions on.
* @throws IOException If an I/O error occurs
*/
    public static void setExecutablePermission(File file) throws IOException {
if (sFileReflectionDone == false) {
try {
sFileSetExecutable = File.class.getMethod("setExecutable", //$NON-NLS-1$
//Synthetic comment -- @@ -141,11 +143,12 @@
/**
* Copies a binary file.
*
     * @param source the source file to copy.
     * @param dest the destination file to write.
     * @throws FileNotFoundException if the source file doesn't exist.
     * @throws IOException if there's a problem reading or writing the file.
*/
    public static void copyFile(File source, File dest) throws IOException {
byte[] buffer = new byte[8192];

FileInputStream fis = null;
//Synthetic comment -- @@ -159,11 +162,6 @@
fos.write(buffer, 0, read);
}

} finally {
if (fis != null) {
try {
//Synthetic comment -- @@ -180,7 +178,67 @@
}
}
}
    }

    /**
     * Checks whether 2 binary files are the same.
     *
     * @param source the source file to copy
     * @param destination the destination file to write
     * @throws FileNotFoundException if the source files don't exist.
     * @throws IOException if there's a problem reading the files.
     */
    public static boolean isSameFile(File source, File destination) throws IOException {

        if (source.length() != destination.length()) {
            return false;
        }

        FileInputStream fis1 = null;
        FileInputStream fis2 = null;

        try {
            fis1 = new FileInputStream(source);
            fis2 = new FileInputStream(destination);

            byte[] buffer1 = new byte[8192];
            byte[] buffer2 = new byte[8192];

            int read1;
            while ((read1 = fis1.read(buffer1)) != -1) {
                int read2 = 0;
                while (read2 < read1) {
                    int n = fis2.read(buffer2, read2, read1 - read2);
                    if (n == -1) {
                        break;
                    }
                }

                if (read2 != read1) {
                    return false;
                }

                if (!Arrays.equals(buffer1, buffer2)) {
                    return false;
                }
            }
        } finally {
            if (fis2 != null) {
                try {
                    fis2.close();
                } catch (IOException e) {
                    // ignore
                }
            }
            if (fis1 != null) {
                try {
                    fis1.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

        return true;
}
}







