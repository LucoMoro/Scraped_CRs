/*Merge: SDK Manager: Rework install logic.

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

Change-Id:I4fd862147cf7e0813eeef77a8332c0ce4f97a836*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AdbWrapper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AdbWrapper.java
similarity index 98%
rename from sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AdbWrapper.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AdbWrapper.java
//Synthetic comment -- index 241f30f..3fab9ce 100755

//Synthetic comment -- @@ -14,10 +14,9 @@
* limitations under the License.
*/

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.ITaskMonitor;

import java.io.BufferedReader;
import java.io.File;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonPackage.java
//Synthetic comment -- index 39750d4..2a58a89 100755

//Synthetic comment -- @@ -242,13 +242,11 @@
* has this add-ons installed, we'll use that one.
*
* @param osSdkRoot The OS path of the SDK root folder.
     * @param suggestedDir A suggestion for the installation folder name, based on the root
     *                     folder used in the zip archive.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
@Override
    public File getInstallFolder(String osSdkRoot, String suggestedDir, SdkManager sdkManager) {
File addons = new File(osSdkRoot, SdkConstants.FD_ADDONS);

// First find if this add-on is already installed. If so, reuse the same directory.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java
//Synthetic comment -- index 7739f8c..ce3372c 100755

//Synthetic comment -- @@ -364,7 +364,7 @@
*/
public void deleteLocal() {
if (isLocal()) {
            new ArchiveInstaller().deleteFileOrFolder(new File(getLocalOsPath()));
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java
//Synthetic comment -- index b98f731..f3fd347 100755

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -33,7 +33,9 @@
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Properties;


/**
//Synthetic comment -- @@ -88,7 +90,7 @@
if (unarchive(archive, osSdkRoot, archiveFile, sdkManager, monitor)) {
monitor.setResult("Installed %1$s", name);
// Delete the temp archive if it exists, only on success
                deleteFileOrFolder(archiveFile);
return true;
}
}
//Synthetic comment -- @@ -147,7 +149,7 @@
File tmpFolder = getTempFolder(osSdkRoot);
if (!tmpFolder.isDirectory()) {
if (tmpFolder.isFile()) {
                deleteFileOrFolder(tmpFolder);
}
if (!tmpFolder.mkdirs()) {
monitor.setResult("Failed to create directory %1$s", tmpFolder.getPath());
//Synthetic comment -- @@ -176,7 +178,7 @@
// Existing file is either of different size or content.
// TODO: continue download when we support continue mode.
// Right now, let's simply remove the file and start over.
            deleteFileOrFolder(tmpFile);
}

if (fetchUrl(archive, tmpFile, link, desc, monitor)) {
//Synthetic comment -- @@ -184,8 +186,8 @@
return tmpFile;
} else {
// Delete the temp file if we aborted the download
            // TODO: disable this when we want to support partial downloads!
            deleteFileOrFolder(tmpFile);
return null;
}
}
//Synthetic comment -- @@ -391,45 +393,42 @@
monitor.setDescription(pkgDesc);
monitor.setResult(pkgDesc);

        // We always unzip in a temp folder which name depends on the package type
        // (e.g. addon, tools, etc.) and then move the folder to the destination folder.
// If the destination folder exists, it will be renamed and deleted at the very
        // end if everything succeeded.

String pkgKind = pkg.getClass().getSimpleName();

File destFolder = null;
        File unzipDestFolder = null;
File oldDestFolder = null;

try {
            // Find a new temp folder that doesn't exist yet
            unzipDestFolder = createTempFolder(osSdkRoot, pkgKind, "new");  //$NON-NLS-1$

            if (unzipDestFolder == null) {
                // this should not seriously happen.
                monitor.setResult("Failed to find a temp directory in %1$s.", osSdkRoot);
                return false;
            }

            if (!unzipDestFolder.mkdirs()) {
                monitor.setResult("Failed to create directory %1$s", unzipDestFolder.getPath());
                return false;
            }

            String[] zipRootFolder = new String[] { null };
            if (!unzipFolder(archiveFile, archive.getSize(),
                    unzipDestFolder, pkgDesc,
                    zipRootFolder, monitor)) {
                return false;
            }

            if (!generateSourceProperties(archive, unzipDestFolder)) {
                return false;
            }

            // Compute destination directory
            destFolder = pkg.getInstallFolder(osSdkRoot, zipRootFolder[0], sdkManager);

if (destFolder == null) {
// this should not seriously happen.
//Synthetic comment -- @@ -442,105 +441,142 @@
return false;
}

            // Swap the old folder by the new one.
            // We have 2 "folder rename" (aka moves) to do.
            // They must both succeed in the right order.
            boolean move1done = false;
            boolean move2done = false;
            while (!move1done || !move2done) {
                File renameFailedForDir = null;

                // Case where the dest dir already exists
                if (!move1done) {
                    if (destFolder.isDirectory()) {
                        // Create a new temp/old dir
                        if (oldDestFolder == null) {
                            oldDestFolder = createTempFolder(osSdkRoot, pkgKind, "old");  //$NON-NLS-1$
                        }
                        if (oldDestFolder == null) {
                            // this should not seriously happen.
                            monitor.setResult("Failed to find a temp directory in %1$s.", osSdkRoot);
                            return false;
                        }

                        // try to move the current dest dir to the temp/old one
                        if (!destFolder.renameTo(oldDestFolder)) {
                            monitor.setResult("Failed to rename directory %1$s to %2$s.",
                                    destFolder.getPath(), oldDestFolder.getPath());
                            renameFailedForDir = destFolder;
                        }
                    }

                    move1done = (renameFailedForDir == null);
}

                // Case where there's no dest dir or we successfully moved it to temp/old
                // We now try to move the temp/unzip to the dest dir
                if (move1done && !move2done) {
                    if (renameFailedForDir == null && !unzipDestFolder.renameTo(destFolder)) {
                        monitor.setResult("Failed to rename directory %1$s to %2$s",
                                unzipDestFolder.getPath(), destFolder.getPath());
                        renameFailedForDir = unzipDestFolder;
                    }

                    move2done = (renameFailedForDir == null);
                }

                if (renameFailedForDir != null) {
                    if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) {

                        String msg = String.format(
                                "-= Warning ! =-\n" +
                                "A folder failed to be renamed or moved. On Windows this " +
                                "typically means that a program is using that folder (for example " +
                                "Windows Explorer or your anti-virus software.)\n" +
                                "Please momentarily deactivate your anti-virus software.\n" +
                                "Please also close any running programs that may be accessing " +
                                "the directory '%1$s'.\n" +
                                "When ready, press YES to try again.",
                                renameFailedForDir.getPath());

                        if (monitor.displayPrompt("SDK Manager: failed to install", msg)) {
                            // loop, trying to rename the temp dir into the destination
                            continue;
                        }

                    }
return false;
}
                break;
}

            unzipDestFolder = null;
success = true;
pkg.postInstallHook(archive, monitor, destFolder);
return true;

} finally {
            // Cleanup if the unzip folder is still set.
            deleteFileOrFolder(oldDestFolder);
            deleteFileOrFolder(unzipDestFolder);

            // In case of failure, we call the postInstallHool with a null directory
if (!success) {
pkg.postInstallHook(archive, monitor, null /*installDir*/);
}
}
}

/**
* Unzips a zip file into the given destination directory.
*
     * The archive file MUST have a unique "root" folder. This root folder is skipped when
     * unarchiving. However we return that root folder name to the caller, as it can be used
     * as a template to know what destination directory to use in the Add-on case.
*/
@SuppressWarnings("unchecked")
private boolean unzipFolder(File archiveFile,
long compressedSize,
File unzipDestFolder,
String description,
            String[] outZipRootFolder,
ITaskMonitor monitor) {

description += " (%1$d%%)";
//Synthetic comment -- @@ -549,8 +585,9 @@
try {
zipFile = new ZipFile(archiveFile);

            // figure if we'll need to set the unix permission
            boolean usingUnixPerm = SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_DARWIN ||
SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_LINUX;

// To advance the percent and the progress bar, we don't know the number of
//Synthetic comment -- @@ -581,9 +618,6 @@
if (pos < 0 || pos == name.length() - 1) {
continue;
} else {
                    if (outZipRootFolder[0] == null && pos > 0) {
                        outZipRootFolder[0] = name.substring(0, pos);
                    }
name = name.substring(pos + 1);
}

//Synthetic comment -- @@ -632,7 +666,7 @@
// get the mode and test if it contains the executable bit
int mode = entry.getUnixMode();
if ((mode & 0111) != 0) {
                        setExecutablePermission(destFile);
}
}

//Synthetic comment -- @@ -671,7 +705,10 @@
}

/**
     * Creates a temp folder in the form of osBasePath/temp/prefix.suffixNNN.
* <p/>
* This operation is not atomic so there's no guarantee the folder can't get
* created in between. This is however unlikely and the caller can assume the
//Synthetic comment -- @@ -680,12 +717,12 @@
* Returns null if no such folder can be found (e.g. if all candidates exist,
* which is rather unlikely) or if the base temp folder cannot be created.
*/
    private File createTempFolder(String osBasePath, String prefix, String suffix) {
File baseTempFolder = getTempFolder(osBasePath);

if (!baseTempFolder.isDirectory()) {
if (baseTempFolder.isFile()) {
                deleteFileOrFolder(baseTempFolder);
}
if (!baseTempFolder.mkdirs()) {
return null;
//Synthetic comment -- @@ -703,8 +740,10 @@
}

/**
     * Returns the temp folder used by the SDK Manager.
* This folder is always at osBasePath/temp.
*/
private File getTempFolder(String osBasePath) {
File baseTempFolder = new File(osBasePath, RepoConstants.FD_TEMP);
//Synthetic comment -- @@ -712,25 +751,6 @@
}

/**
     * Deletes a file or a directory.
     * Directories are deleted recursively.
     * The argument can be null.
     */
    /*package*/ void deleteFileOrFolder(File fileOrFolder) {
        if (fileOrFolder != null) {
            if (fileOrFolder.isDirectory()) {
                // Must delete content recursively first
                for (File item : fileOrFolder.listFiles()) {
                    deleteFileOrFolder(item);
                }
            }
            if (!fileOrFolder.delete()) {
                fileOrFolder.deleteOnExit();
            }
        }
    }

    /**
* Generates a source.properties in the destination folder that contains all the infos
* relevant to this archive, this package and the source so that we can reload them
* locally later.
//Synthetic comment -- @@ -769,13 +789,81 @@
}

/**
     * Sets the executable Unix permission (0777) on a file or folder.
     * @param file The file to set permissions on.
     * @throws IOException If an I/O error occurs
*/
    private void setExecutablePermission(File file) throws IOException {
        Runtime.getRuntime().exec(new String[] {
           "chmod", "777", file.getAbsolutePath()
        });
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DocPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DocPackage.java
//Synthetic comment -- index 3bfd639..5e0a767 100755

//Synthetic comment -- @@ -153,13 +153,11 @@
* A "doc" package should always be located in SDK/docs.
*
* @param osSdkRoot The OS path of the SDK root folder.
     * @param suggestedDir A suggestion for the installation folder name, based on the root
     *                     folder used in the zip archive.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
@Override
    public File getInstallFolder(String osSdkRoot, String suggestedDir, SdkManager sdkManager) {
return new File(osSdkRoot, SdkConstants.FD_DOCS);
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java
//Synthetic comment -- index d3f3a9b..d06b08d 100755

//Synthetic comment -- @@ -257,13 +257,11 @@
* A "tool" package should always be located in SDK/tools.
*
* @param osSdkRoot The OS path of the SDK root folder.
     * @param suggestedDir A suggestion for the installation folder name, based on the root
     *                     folder used in the zip archive.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
@Override
    public File getInstallFolder(String osSdkRoot, String suggestedDir, SdkManager sdkManager) {
return new File(osSdkRoot, getPath());
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/OsHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/OsHelper.java
new file mode 100755
//Synthetic comment -- index 0000000..02cebe1

//Synthetic comment -- @@ -0,0 +1,138 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index 438b07b..12ca0e9 100755

//Synthetic comment -- @@ -415,13 +415,10 @@
* existing or new folder depending on the current content of the SDK.
*
* @param osSdkRoot The OS path of the SDK root folder.
     * @param suggestedDir A suggestion for the installation folder name, based on the root
     *                     folder used in the zip archive.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
    public abstract File getInstallFolder(
            String osSdkRoot, String suggestedDir, SdkManager sdkManager);

/**
* Hook called right before an archive is installed. The archive has already
//Synthetic comment -- @@ -454,7 +451,8 @@
* @param archive The archive that has been installed.
* @param monitor The {@link ITaskMonitor} to display errors.
* @param installFolder The folder where the archive was successfully installed.
     *                      Null if the installation failed.
*/
public void postInstallHook(Archive archive, ITaskMonitor monitor, File installFolder) {
// Nothing to do in base class.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformPackage.java
//Synthetic comment -- index 1032e03..1e5e391 100755

//Synthetic comment -- @@ -166,13 +166,11 @@
* has this platform version installed, we'll use that one.
*
* @param osSdkRoot The OS path of the SDK root folder.
     * @param suggestedDir A suggestion for the installation folder name, based on the root
     *                     folder used in the zip archive.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
@Override
    public File getInstallFolder(String osSdkRoot, String suggestedDir, SdkManager sdkManager) {

// First find if this platform is already installed. If so, reuse the same directory.
for (IAndroidTarget target : sdkManager.getTargets()) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformToolPackage.java
//Synthetic comment -- index cfd88e9..717948e 100755

//Synthetic comment -- @@ -107,13 +107,11 @@
* A "tool" package should always be located in SDK/tools.
*
* @param osSdkRoot The OS path of the SDK root folder.
     * @param suggestedDir A suggestion for the installation folder name, based on the root
     *                     folder used in the zip archive.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
@Override
    public File getInstallFolder(String osSdkRoot, String suggestedDir, SdkManager sdkManager) {
return new File(osSdkRoot, SdkConstants.FD_PLATFORM_TOOLS);
}

//Synthetic comment -- @@ -123,4 +121,25 @@
return pkg instanceof PlatformToolPackage;
}

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SamplePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SamplePackage.java
//Synthetic comment -- index d60cbaf..920a7e0 100755

//Synthetic comment -- @@ -209,13 +209,11 @@
* version installed, we'll use that one.
*
* @param osSdkRoot The OS path of the SDK root folder.
     * @param suggestedDir A suggestion for the installation folder name, based on the root
     *                     folder used in the zip archive.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
@Override
    public File getInstallFolder(String osSdkRoot, String suggestedDir, SdkManager sdkManager) {

// The /samples dir at the root of the SDK
File samplesRoot = new File(osSdkRoot, SdkConstants.FD_SAMPLES);
//Synthetic comment -- @@ -326,12 +324,10 @@
public void postInstallHook(Archive archive, ITaskMonitor monitor, File installFolder) {
super.postInstallHook(archive, monitor, installFolder);

        if (installFolder == null) {
            return;
}

        String h = computeContentHash(installFolder);
        saveContentHash(installFolder, h);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java
//Synthetic comment -- index c8f1e66..c76de30 100755

//Synthetic comment -- @@ -166,13 +166,11 @@
* A "tool" package should always be located in SDK/tools.
*
* @param osSdkRoot The OS path of the SDK root folder.
     * @param suggestedDir A suggestion for the installation folder name, based on the root
     *                     folder used in the zip archive.
* @param sdkManager An existing SDK manager to list current platforms and addons.
* @return A new {@link File} corresponding to the directory to use to install this package.
*/
@Override
    public File getInstallFolder(String osSdkRoot, String suggestedDir, SdkManager sdkManager) {
return new File(osSdkRoot, SdkConstants.FD_TOOLS);
}

//Synthetic comment -- @@ -210,9 +208,9 @@
}

String scriptName = "post_tools_install";   //$NON-NLS-1$
        String shell = "";
if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_WINDOWS) {
            shell = "cmd.exe /c ";
scriptName += ".bat";                   //$NON-NLS-1$
} else {
scriptName += ".sh";                    //$NON-NLS-1$








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 76ff7ba..e569eba 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.AddonPackage;
import com.android.sdklib.internal.repository.AddonsListFetcher;
import com.android.sdklib.internal.repository.Archive;







