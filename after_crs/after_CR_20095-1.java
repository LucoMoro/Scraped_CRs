/*SDK Manager: Rework install logic.

This should get rid of the annoying behavior on Windows
that prevent the "folder swap" operation due to the folders
being locked. Cf public issue 4410.

High level summary of the issue and the fix: the old behavior
was to unzip in a temp folder, then rename the old folder to
another temp file and finally rename the new folder at the
desired location. This fails typically when there is a file
indexer (e.g. anti-virus) scanning the new folder so we can't
move that folder.
The new logic is to try to move the old folder first into a
temp folder. If the fail move, we have a lock on the old folder
and ask the user to fix it manually. They probably have a file
opened and it's a legit issue to report. Once that succeeded
we can directly unzip the archive into the final destination
without using a temp unzip location, thus avoiding the common
"indexer in progress" issue.
In case the unzip operation fails, we try to copy (not move) the
old folder back.

Change-Id:I5ed67ff626532fe7cc48a45e87d1dbaf6954f28a*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AdbWrapper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AdbWrapper.java
similarity index 98%
rename from sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AdbWrapper.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AdbWrapper.java
//Synthetic comment -- index 241f30f..3fab9ce 100755

//Synthetic comment -- @@ -14,10 +14,9 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository;

import com.android.sdklib.SdkConstants;

import java.io.BufferedReader;
import java.io.File;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonPackage.java
//Synthetic comment -- index 39750d4..2a58a89 100755

//Synthetic comment -- @@ -242,13 +242,11 @@
* has this add-ons installed, we'll use that one.
*
* @param osSdkRoot The OS path of the SDK root folder.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
@Override
    public File getInstallFolder(String osSdkRoot, SdkManager sdkManager) {
File addons = new File(osSdkRoot, SdkConstants.FD_ADDONS);

// First find if this add-on is already installed. If so, reuse the same directory.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java
//Synthetic comment -- index 7739f8c..ce3372c 100755

//Synthetic comment -- @@ -364,7 +364,7 @@
*/
public void deleteLocal() {
if (isLocal()) {
            OsHelper.deleteFileOrFolder(new File(getLocalOsPath()));
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java
//Synthetic comment -- index b98f731..a88b5f4 100755

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -33,7 +33,9 @@
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;


/**
//Synthetic comment -- @@ -88,7 +90,7 @@
if (unarchive(archive, osSdkRoot, archiveFile, sdkManager, monitor)) {
monitor.setResult("Installed %1$s", name);
// Delete the temp archive if it exists, only on success
                OsHelper.deleteFileOrFolder(archiveFile);
return true;
}
}
//Synthetic comment -- @@ -147,7 +149,7 @@
File tmpFolder = getTempFolder(osSdkRoot);
if (!tmpFolder.isDirectory()) {
if (tmpFolder.isFile()) {
                OsHelper.deleteFileOrFolder(tmpFolder);
}
if (!tmpFolder.mkdirs()) {
monitor.setResult("Failed to create directory %1$s", tmpFolder.getPath());
//Synthetic comment -- @@ -176,7 +178,7 @@
// Existing file is either of different size or content.
// TODO: continue download when we support continue mode.
// Right now, let's simply remove the file and start over.
            OsHelper.deleteFileOrFolder(tmpFile);
}

if (fetchUrl(archive, tmpFile, link, desc, monitor)) {
//Synthetic comment -- @@ -184,8 +186,8 @@
return tmpFile;
} else {
// Delete the temp file if we aborted the download
            // TODO: disable this when we want to support partial downloads.
            OsHelper.deleteFileOrFolder(tmpFile);
return null;
}
}
//Synthetic comment -- @@ -391,45 +393,42 @@
monitor.setDescription(pkgDesc);
monitor.setResult(pkgDesc);

        // Ideally we want to always unzip in a temp folder which name depends on the package
        // type (e.g. addon, tools, etc.) and then move the folder to the destination folder.
// If the destination folder exists, it will be renamed and deleted at the very
        // end if everything succeeded. This provides a nice atomic swap and should leave the
        // original folder untouched in case something wrong (e.g. program crash) in the
        // middle of the unzip operation.
        //
        // However that doesn't work on Windows, we always end up not being able to move the
        // new folder. There are actually 2 cases:
        // A- A process such as a the explorer is locking the *old* folder or a file inside
        //    (e.g. adb.exe)
        //    In this case we really shouldn't be tried to work around it and we need to let
        //    the user know and let it close apps that access that folder.
        // B- A process is locking the *new* folder. Very often this turns to be a file indexer
        //    or an anti-virus that is busy scanning the new folder that we just unzipped.
        //
        // So we're going to change the strategy:
        // 1- Try to move the old folder to a temp/old folder. This might fail in case of issue A.
        //    Note: for platform-tools, we can try killing adb first.
        //    If it still fails, we do nothing and ask the user to terminate apps that can be
        //    locking that folder.
        // 2- Once the old folder is out of the way, we unzip the archive directly into the
        //    optimal new location. We no longer unzip it in a temp folder and move it since we
        //    know that's what fails in most of the cases.
        // 3- If the unzip fails, remove everything and try to restore the old folder by doing
        //    a *copy* in place and not a folder move (which will likely fail too).

String pkgKind = pkg.getClass().getSimpleName();

File destFolder = null;
File oldDestFolder = null;

try {
            // -0- Compute destination directory and check install pre-conditions

            destFolder = pkg.getInstallFolder(osSdkRoot, sdkManager);

if (destFolder == null) {
// this should not seriously happen.
//Synthetic comment -- @@ -442,105 +441,140 @@
return false;
}

            // -1- move old folder.

            if (destFolder.exists()) {
                // Create a new temp/old dir
                if (oldDestFolder == null) {
                    oldDestFolder = getNewTempFolder(osSdkRoot, pkgKind, "old");  //$NON-NLS-1$
}
                if (oldDestFolder == null) {
                    // this should not seriously happen.
                    monitor.setResult("Failed to find a temp directory in %1$s.", osSdkRoot);
return false;
}

                // Try to move the current dest dir to the temp/old one. Tell the user if it failed.
                while(true) {
                    if (!moveFolder(destFolder, oldDestFolder)) {
                        monitor.setResult("Failed to rename directory %1$s to %2$s.",
                                destFolder.getPath(), oldDestFolder.getPath());

                        if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) {
                            String msg = String.format(
                                    "-= Warning ! =-\n" +
                                    "A folder failed to be moved. On Windows this " +
                                    "typically means that a program is using that folder (for " +
                                    "example Windows Explorer or your anti-virus software.)\n" +
                                    "Please momentarily deactivate your anti-virus software or " +
                                    "close any running programs that may be accessing the " +
                                    "directory '%1$s'.\n" +
                                    "When ready, press YES to try again.",
                                    destFolder.getPath());

                            if (monitor.displayPrompt("SDK Manager: failed to install", msg)) {
                                // loop, trying to rename the temp dir into the destination
                                continue;
                            }
                        }
                    }
                    break;
                }
}

            assert !destFolder.exists();

            // -2- Unzip new content directly in place.

            if (!destFolder.mkdirs()) {
                monitor.setResult("Failed to create directory %1$s", destFolder.getPath());
                return false;
            }

            if (!unzipFolder(archiveFile, archive.getSize(), destFolder, pkgDesc, monitor)) {
                return false;
            }

            if (!generateSourceProperties(archive, destFolder)) {
                monitor.setResult("Failed to generate source.properties in directory %1$s",
                        destFolder.getPath());
                return false;
            }

success = true;
pkg.postInstallHook(archive, monitor, destFolder);
return true;

} finally {
if (!success) {
                // In case of failure, we try to restore the old folder content.
                if (oldDestFolder != null) {
                    restoreFolder(oldDestFolder, destFolder);
                }

                // We also call the postInstallHool with a null directory to give a chance
                // to the archive to cleanup after preInstallHook.
pkg.postInstallHook(archive, monitor, null /*installDir*/);
}

            // Cleanup if the unzip folder is still set.
            OsHelper.deleteFileOrFolder(oldDestFolder);
}
}

/**
     * Tries to rename/move a folder.
     * <p/>
     * Contract:
     * <ul>
     * <li> When we start, oldDir must exist and be a directory. newDir must not exist. </li>
     * <li> On successful completion, oldDir must not exists.
     *      newDir must exist and have the same content. </li>
     * <li> On failure completion, oldDir must have the same content as before.
     *      newDir must not exist. </li>
     * </ul>
     * <p/>
     * The simple "rename" operation on a folder can typically fail on Windows for a variety
     * of reason, in fact as soon as a single process holds a reference on a directory. The
     * most common case are the Explorer, the system's file indexer, Tortoise SVN cache or
     * an anti-virus that are busy indexing a new directory having been created.
     *
     * @param oldDir The old location to move. It must exist and be a directory.
     * @param newDir The new location where to move. It must not exist.
     * @return True if the move succeeded. On failure, we try hard to not have touched the old
     *  directory in order not to loose its content.
     */
    private boolean moveFolder(File oldDir, File newDir) {
        // This is a simple folder rename that works on Linux/Mac all the time.
        //
        // On Windows this might fail if an indexer is busy looking at a new directory
        // (e.g. right after we unzip our archive), so it fails let's be nice and give
        // it a bit of time to succeed.
        for (int i = 0; i < 5; i++) {
            if (oldDir.renameTo(newDir)) {
                return true;
            }
            try {
                Thread.sleep(500 /*ms*/);
            } catch (InterruptedException e) {
                // ignore
            }
        }

        return false;
    }

    /**
* Unzips a zip file into the given destination directory.
*
     * The archive file MUST have a unique "root" folder.
     * This root folder is skipped when unarchiving.
*/
@SuppressWarnings("unchecked")
private boolean unzipFolder(File archiveFile,
long compressedSize,
File unzipDestFolder,
String description,
ITaskMonitor monitor) {

description += " (%1$d%%)";
//Synthetic comment -- @@ -549,8 +583,9 @@
try {
zipFile = new ZipFile(archiveFile);

            // figure if we'll need to set the unix permissions
            boolean usingUnixPerm =
                    SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_DARWIN ||
SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_LINUX;

// To advance the percent and the progress bar, we don't know the number of
//Synthetic comment -- @@ -581,9 +616,6 @@
if (pos < 0 || pos == name.length() - 1) {
continue;
} else {
name = name.substring(pos + 1);
}

//Synthetic comment -- @@ -632,7 +664,7 @@
// get the mode and test if it contains the executable bit
int mode = entry.getUnixMode();
if ((mode & 0111) != 0) {
                        OsHelper.setExecutablePermission(destFile);
}
}

//Synthetic comment -- @@ -671,7 +703,10 @@
}

/**
     * Returns an unused temp folder path in the form of osBasePath/temp/prefix.suffixNNN.
     * <p/>
     * This does not actually <em>create</em> the folder. It just scan the base path for
     * a free folder name to use and returns the file to use to reference it.
* <p/>
* This operation is not atomic so there's no guarantee the folder can't get
* created in between. This is however unlikely and the caller can assume the
//Synthetic comment -- @@ -680,12 +715,12 @@
* Returns null if no such folder can be found (e.g. if all candidates exist,
* which is rather unlikely) or if the base temp folder cannot be created.
*/
    private File getNewTempFolder(String osBasePath, String prefix, String suffix) {
File baseTempFolder = getTempFolder(osBasePath);

if (!baseTempFolder.isDirectory()) {
if (baseTempFolder.isFile()) {
                OsHelper.deleteFileOrFolder(baseTempFolder);
}
if (!baseTempFolder.mkdirs()) {
return null;
//Synthetic comment -- @@ -703,8 +738,10 @@
}

/**
     * Returns the single fixed "temp" folder used by the SDK Manager.
* This folder is always at osBasePath/temp.
     * <p/>
     * This does not actually <em>create</em> the folder.
*/
private File getTempFolder(String osBasePath) {
File baseTempFolder = new File(osBasePath, RepoConstants.FD_TEMP);
//Synthetic comment -- @@ -712,25 +749,6 @@
}

/**
* Generates a source.properties in the destination folder that contains all the infos
* relevant to this archive, this package and the source so that we can reload them
* locally later.
//Synthetic comment -- @@ -769,13 +787,81 @@
}

/**
     * Recursively restore srcFolder into destFolder by performing a copy of the file
     * content rather than rename/moves.
     *
     * @param srcFolder The source folder to restore.
     * @param destFolder The destination folder where to restore.
     * @return True if the folder was successfully restored, false if it was not at all or
     *         only partially restored.
*/
    private boolean restoreFolder(File srcFolder, File destFolder) {
        boolean result = true;

        // Process sub-folders first
        File[] srcFiles = srcFolder.listFiles();
        if (srcFiles == null) {
            // Source does not exist. That is quite odd.
            return false;
        }

        if (destFolder.isFile()) {
            if (!destFolder.delete()) {
                // There's already a file in there where we want a directory and
                // we can't delete it. This is rather unexpected. Just give up on
                // that folder.
                return false;
            }
        } else if (!destFolder.isDirectory()) {
            destFolder.mkdirs();
        }

        // Get all the files and dirs of the current destination. We are not going
        // to clean up the destination first. Instead we'll copy over and just remove
        // any remaining files or directories.
        File[] files = destFolder.listFiles();
        Set<File> destDirs = new HashSet<File>();
        Set<File> destFiles = new HashSet<File>();
        for (File f : files) {
            if (f.isDirectory()) {
                destDirs.add(f);
            } else {
                destFiles.add(f);
            }
        }

        // First restore all source directories.
        for (File dir : srcFiles) {
            if (dir.isDirectory()) {
                File d = new File(destFolder, dir.getName());
                destDirs.remove(d);
                if (!restoreFolder(dir, d)) {
                    result = false;
                }
            }
        }

        // Remove any remaining directories not processed above.
        for (File dir : destDirs) {
            OsHelper.deleteFileOrFolder(dir);
        }

        // Copy any source files over to the destination.
        for (File file : srcFiles) {
            if (file.isFile()) {
                File f = new File(destFolder, file.getName());
                destFiles.remove(f);
                if (!OsHelper.copyFile(file, f)) {
                    result = false;
                }
            }
        }

        // Remove any remaining files not processed above.
        for (File file : destFiles) {
            OsHelper.deleteFileOrFolder(file);
        }

        return result;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DocPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DocPackage.java
//Synthetic comment -- index 3bfd639..5e0a767 100755

//Synthetic comment -- @@ -153,13 +153,11 @@
* A "doc" package should always be located in SDK/docs.
*
* @param osSdkRoot The OS path of the SDK root folder.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
@Override
    public File getInstallFolder(String osSdkRoot, SdkManager sdkManager) {
return new File(osSdkRoot, SdkConstants.FD_DOCS);
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java
//Synthetic comment -- index d3f3a9b..d06b08d 100755

//Synthetic comment -- @@ -257,13 +257,11 @@
* A "tool" package should always be located in SDK/tools.
*
* @param osSdkRoot The OS path of the SDK root folder.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
@Override
    public File getInstallFolder(String osSdkRoot, SdkManager sdkManager) {
return new File(osSdkRoot, getPath());
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/OsHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/OsHelper.java
new file mode 100755
//Synthetic comment -- index 0000000..02cebe1

//Synthetic comment -- @@ -0,0 +1,138 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.internal.repository;

import com.android.sdklib.SdkConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Helper methods used when dealing with archives installation.
 */
abstract class OsHelper {

    /**
     * Helper to delete a file or a directory.
     * For a directory, recursively deletes all of its content.
     * Files that cannot be deleted right away are marked for deletion on exit.
     * The argument can be null.
     */
    static void deleteFileOrFolder(File fileOrFolder) {
        if (fileOrFolder != null) {
            if (fileOrFolder.isDirectory()) {
                // Must delete content recursively first
                for (File item : fileOrFolder.listFiles()) {
                    deleteFileOrFolder(item);
                }
            }

            if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) {
                // Trying to delete a resource on windows might fail if there's a file
                // indexer locking the resource. Generally retrying will be enough to
                // make it work.
                //
                // Try for half a second before giving up.

                for (int i = 0; i < 5; i++) {
                    if (fileOrFolder.delete()) {
                        return;
                    }

                    try {
                        Thread.sleep(100 /*ms*/);
                    } catch (InterruptedException e) {
                        // Ignore.
                    }
                }

                fileOrFolder.deleteOnExit();

            } else {
                // On Linux or Mac, just straight deleting it should just work.

                if (!fileOrFolder.delete()) {
                    fileOrFolder.deleteOnExit();
                }
            }
        }
    }

    /**
     * Sets the executable Unix permission (0777) on a file or folder.
     * <p/>
     * This invokes a chmod exec, so there is no guarantee of it being fast.
     * Caller must make sure to not invoke this under Windows.
     *
     * @param file The file to set permissions on.
     * @throws IOException If an I/O error occurs
     */
    static void setExecutablePermission(File file) throws IOException {
        Runtime.getRuntime().exec(new String[] {
           "chmod", "777", file.getAbsolutePath()
        });
    }

    /**
     * Copies a binary file.
     *
     * @param source the source file to copy
     * @param dest the destination file to write
     * @return True if the file was successfully copied. False otherwise.
     */
    static boolean copyFile(File source, File dest) {
        byte[] buffer = new byte[8192];

        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(dest);

            int read;
            while ((read = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }

            return true;

        } catch (Exception e) {
            // Ignore. Simply return false below.

        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
        }

        return false;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index 438b07b..12ca0e9 100755

//Synthetic comment -- @@ -415,13 +415,10 @@
* existing or new folder depending on the current content of the SDK.
*
* @param osSdkRoot The OS path of the SDK root folder.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
    public abstract File getInstallFolder(String osSdkRoot, SdkManager sdkManager);

/**
* Hook called right before an archive is installed. The archive has already
//Synthetic comment -- @@ -454,7 +451,8 @@
* @param archive The archive that has been installed.
* @param monitor The {@link ITaskMonitor} to display errors.
* @param installFolder The folder where the archive was successfully installed.
     *                      Null if the installation failed, in case the archive needs to
     *                      do some cleanup after <code>preInstallHook</code>.
*/
public void postInstallHook(Archive archive, ITaskMonitor monitor, File installFolder) {
// Nothing to do in base class.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformPackage.java
//Synthetic comment -- index 1032e03..1e5e391 100755

//Synthetic comment -- @@ -166,13 +166,11 @@
* has this platform version installed, we'll use that one.
*
* @param osSdkRoot The OS path of the SDK root folder.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
@Override
    public File getInstallFolder(String osSdkRoot, SdkManager sdkManager) {

// First find if this platform is already installed. If so, reuse the same directory.
for (IAndroidTarget target : sdkManager.getTargets()) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformToolPackage.java
//Synthetic comment -- index cfd88e9..717948e 100755

//Synthetic comment -- @@ -107,13 +107,11 @@
* A "tool" package should always be located in SDK/tools.
*
* @param osSdkRoot The OS path of the SDK root folder.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
@Override
    public File getInstallFolder(String osSdkRoot, SdkManager sdkManager) {
return new File(osSdkRoot, SdkConstants.FD_PLATFORM_TOOLS);
}

//Synthetic comment -- @@ -123,4 +121,25 @@
return pkg instanceof PlatformToolPackage;
}

    /**
     * Hook called right before an archive is installed.
     * This is used here to stop ADB before trying to replace the platform-tool package.
     *
     * @param archive The archive that will be installed
     * @param monitor The {@link ITaskMonitor} to display errors.
     * @param osSdkRoot The OS path of the SDK root folder.
     * @param installFolder The folder where the archive will be installed. Note that this
     *                      is <em>not</em> the folder where the archive was temporary
     *                      unzipped. The installFolder, if it exists, contains the old
     *                      archive that will soon be replaced by the new one.
     * @return True if installing this archive shall continue, false if it should be skipped.
     */
    @Override
    public boolean preInstallHook(Archive archive, ITaskMonitor monitor,
            String osSdkRoot, File installFolder) {
        AdbWrapper aw = new AdbWrapper(osSdkRoot, monitor);
        aw.stopAdb();
        return super.preInstallHook(archive, monitor, osSdkRoot, installFolder);
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SamplePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SamplePackage.java
//Synthetic comment -- index d60cbaf..920a7e0 100755

//Synthetic comment -- @@ -209,13 +209,11 @@
* version installed, we'll use that one.
*
* @param osSdkRoot The OS path of the SDK root folder.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
@Override
    public File getInstallFolder(String osSdkRoot, SdkManager sdkManager) {

// The /samples dir at the root of the SDK
File samplesRoot = new File(osSdkRoot, SdkConstants.FD_SAMPLES);
//Synthetic comment -- @@ -326,12 +324,10 @@
public void postInstallHook(Archive archive, ITaskMonitor monitor, File installFolder) {
super.postInstallHook(archive, monitor, installFolder);

        if (installFolder != null) {
            String h = computeContentHash(installFolder);
            saveContentHash(installFolder, h);
}
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java
//Synthetic comment -- index c8f1e66..c76de30 100755

//Synthetic comment -- @@ -166,13 +166,11 @@
* A "tool" package should always be located in SDK/tools.
*
* @param osSdkRoot The OS path of the SDK root folder.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
@Override
    public File getInstallFolder(String osSdkRoot, SdkManager sdkManager) {
return new File(osSdkRoot, SdkConstants.FD_TOOLS);
}

//Synthetic comment -- @@ -210,9 +208,9 @@
}

String scriptName = "post_tools_install";   //$NON-NLS-1$
        String shell = "";                          //$NON-NLS-1$
if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_WINDOWS) {
            shell = "cmd.exe /c ";                  //$NON-NLS-1$
scriptName += ".bat";                   //$NON-NLS-1$
} else {
scriptName += ".sh";                    //$NON-NLS-1$








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 76ff7ba..e569eba 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.AdbWrapper;
import com.android.sdklib.internal.repository.AddonPackage;
import com.android.sdklib.internal.repository.AddonsListFetcher;
import com.android.sdklib.internal.repository.Archive;







