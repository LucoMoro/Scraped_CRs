/*SDK Manager: support for partial resume of downloads.

Change-Id:I248194b5764cf801e52ebd35c6b5963facf31a3e*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index 01a2078..0667b74 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdklib.internal.repository;

import com.android.SdkConstants;
import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
//Synthetic comment -- @@ -49,7 +50,7 @@
/**
* A simple cache for the XML resources handled by the SDK Manager.
* <p/>
 * Callers should use {@link #openDirectUrl(String, ITaskMonitor)} to download "large files"
* that should not be cached (like actual installation packages which are several MBs big)
* and call {@link #openCachedUrl(String, ITaskMonitor)} to download small XML files.
* <p/>
//Synthetic comment -- @@ -284,35 +285,33 @@
* documentation.
*
* @param urlString the URL string to be opened.
* @param monitor {@link ITaskMonitor} which is related to this URL
*                 fetching.
     * @return Returns an {@link InputStream} holding the URL content, or null if
     *                 there's no content.
* @throws IOException Exception thrown when there are problems retrieving
*                 the URL or its content.
* @throws CanceledByUserException Exception thrown if the user cancels the
*              authentication dialog.
*/
    public InputStream openDirectUrl(String urlString, ITaskMonitor monitor)
            throws IOException, CanceledByUserException {
if (DEBUG) {
System.out.println(String.format("%s : Direct download", urlString)); //$NON-NLS-1$
}
        Pair<InputStream, HttpResponse> result = UrlOpener.openUrl(
urlString,
false /*needsMarkResetSupport*/,
monitor,
                null /*headers*/);
        InputStream is = result.getFirst();
        HttpResponse resp = result.getSecond();
        int status = resp.getStatusLine().getStatusCode();
        // We shouldn't be using the input stream if the response code isn't 200; this
        // shoouldn't happen normally.
        if (status != HttpStatus.SC_OK && is != null) {
            is.close();
            is = null;
        }
        return is;
}

/**
//Synthetic comment -- @@ -321,8 +320,7 @@
* from the cache, potentially updated first or directly downloaded.
* <p/>
* For large downloads (e.g. installable archives) please do not invoke the
     * cache and instead use the {@link #openDirectUrl(String, ITaskMonitor)}
     * method.
* <p/>
* For details on realm authentication and user/password handling,
* check the underlying {@link UrlOpener#openUrl(String, boolean, ITaskMonitor, Header[])}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index 72ec65f..baccc27 100644

//Synthetic comment -- @@ -69,10 +69,10 @@
* @see #openUrl(String, boolean, ITaskMonitor, Header[])
* <p/>
* Implementation detail: callers should use {@link DownloadCache} instead of this class.
 * {@link DownloadCache#openDirectUrl(String, ITaskMonitor)} is a direct pass-through to
 * {@link UrlOpener} since there's no caching. However from an implementation perspective
 * it's still recommended to pass down a {@link DownloadCache} instance, which will let us
 * override the implementation later on (for testing, for example.)
*/
class UrlOpener {









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java
//Synthetic comment -- index e7c10d4..75e8912 100755

//Synthetic comment -- @@ -32,9 +32,15 @@
import com.android.sdklib.util.GrabProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.IProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.Wait;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.EOFException;
import java.io.File;
//Synthetic comment -- @@ -43,12 +49,13 @@
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
//Synthetic comment -- @@ -59,7 +66,8 @@
*/
public class ArchiveInstaller {

    public static final String ENV_VAR_IGNORE_COMPAT = "ANDROID_SDK_IGNORE_COMPAT";

public static final int NUM_MONITOR_INC = 100;

//Synthetic comment -- @@ -108,7 +116,6 @@
Archive newArchive = archiveInfo.getNewArchive();
Package pkg = newArchive.getParentPackage();

        File archiveFile = null;
String name = pkg.getShortDescription();

if (newArchive.isLocal()) {
//Synthetic comment -- @@ -129,13 +136,16 @@
return false;
}

        archiveFile = downloadFile(newArchive, osSdkRoot, cache, monitor, forceHttp);
        if (archiveFile != null) {
// Unarchive calls the pre/postInstallHook methods.
            if (unarchive(archiveInfo, osSdkRoot, archiveFile, sdkManager, monitor)) {
monitor.log("Installed %1$s", name);
// Delete the temp archive if it exists, only on success
                mFileOp.deleteFileOrFolder(archiveFile);
return true;
}
}
//Synthetic comment -- @@ -148,7 +158,7 @@
* Caller is responsible with deleting the temp file when done.
*/
@VisibleForTesting(visibility=Visibility.PRIVATE)
    protected File downloadFile(Archive archive,
String osSdkRoot,
DownloadCache cache,
ITaskMonitor monitor,
//Synthetic comment -- @@ -204,6 +214,9 @@
}
File tmpFile = new File(tmpFolder, base);

// if the file exists, check its checksum & size. Use it if complete
if (mFileOp.exists(tmpFile)) {
if (mFileOp.length(tmpFile) == archive.getSize()) {
//Synthetic comment -- @@ -217,25 +230,76 @@
}
if (chksum.equalsIgnoreCase(archive.getChecksum())) {
// File is good, let's use it.
                    return tmpFile;
}
}

            // Existing file is either of different size or content.
            // TODO: continue download when we support continue mode.
            // Right now, let's simply remove the file and start over.
            mFileOp.deleteFileOrFolder(tmpFile);
}

        if (fetchUrl(archive, tmpFile, link, pkgName, cache, monitor)) {
// Fetching was successful, let's use this file.
            return tmpFile;
        } else {
            // Delete the temp file if we aborted the download
            // TODO: disable this when we want to support partial downloads.
            mFileOp.deleteFileOrFolder(tmpFile);
            return null;
}
}

/**
//Synthetic comment -- @@ -306,9 +370,23 @@
* SHA1 as expected. Returns true on success or false if any of those checks fail.
* <p/>
* Increments the monitor by {@link #NUM_MONITOR_INC}.
*/
private boolean fetchUrl(Archive archive,
File tmpFile,
String urlString,
String pkgName,
DownloadCache cache,
//Synthetic comment -- @@ -318,20 +396,86 @@
InputStream is = null;
int inc_remain = NUM_MONITOR_INC;
try {
            is = cache.openDirectUrl(urlString, monitor);
            if (is == null) {
                monitor.logError("Download failed: no content.");
                return false;
            }
            os = new FileOutputStream(tmpFile);

MessageDigest digester = archive.getChecksumType().getMessageDigest();

byte[] buf = new byte[65536];
int n;

long total = 0;
long size = archive.getSize();
long inc = size / NUM_MONITOR_INC;
long next_inc = inc;

//Synthetic comment -- @@ -412,10 +556,10 @@

} catch (FileNotFoundException e) {
// The FNF message is just the URL. Make it a bit more useful.
            monitor.logError("File not found: %1$s", e.getMessage());

} catch (Exception e) {
            monitor.logError("Download failed: %1$s", e.getMessage());   //$NON-NLS-1$

} finally {
if (os != null) {
//Synthetic comment -- @@ -932,27 +1076,10 @@
pkg.saveProperties(props);
}

        OutputStream fos = null;
        try {
            File f = new File(unzipDestFolder, SdkConstants.FN_SOURCE_PROP);

            fos = mFileOp.newFileOutputStream(f);

            props.store(fos, "## Android Tool: Source of this archive.");  //$NON-NLS-1$

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }

        return false;
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FileOp.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FileOp.java
//Synthetic comment -- index 7f7a1f2..0585e22 100755

//Synthetic comment -- @@ -27,6 +27,7 @@
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;


/**
//Synthetic comment -- @@ -89,6 +90,7 @@
* Helper to delete a file or a directory.
* For a directory, recursively deletes all of its content.
* Files that cannot be deleted right away are marked for deletion on exit.
* The argument can be null.
*/
@Override
//Synthetic comment -- @@ -104,6 +106,11 @@
}
}

if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) {
// Trying to delete a resource on windows might fail if there's a file
// indexer locking the resource. Generally retrying will be enough to
//Synthetic comment -- @@ -337,4 +344,43 @@
public OutputStream newFileOutputStream(File file) throws FileNotFoundException {
return new FileOutputStream(file);
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/IFileOp.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/IFileOp.java
//Synthetic comment -- index b0d442b..5b131d5 100755

//Synthetic comment -- @@ -16,11 +16,14 @@

package com.android.sdklib.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
//Synthetic comment -- @@ -34,6 +37,7 @@
* Helper to delete a file or a directory.
* For a directory, recursively deletes all of its content.
* Files that cannot be deleted right away are marked for deletion on exit.
* The argument can be null.
*/
public abstract void deleteFileOrFolder(File fileOrFolder);
//Synthetic comment -- @@ -111,4 +115,25 @@
/** Creates a new {@link FileOutputStream} for the given {@code file}. */
public abstract OutputStream newFileOutputStream(File file) throws FileNotFoundException;

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/archives/ArchiveInstallerTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/archives/ArchiveInstallerTest.java
//Synthetic comment -- index 609481b..b0b3f03 100755

//Synthetic comment -- @@ -30,6 +30,7 @@
import com.android.sdklib.io.IFileOp;
import com.android.sdklib.io.MockFileOp;
import com.android.sdklib.repository.PkgProps;

import java.io.File;
import java.util.Arrays;
//Synthetic comment -- @@ -64,7 +65,7 @@
}

@Override
        protected File downloadFile(
Archive archive,
String osSdkRoot,
DownloadCache cache,
//Synthetic comment -- @@ -73,7 +74,7 @@
File file = mDownloadMap.get(archive);
// register the file as "created"
ArchiveInstallerTest.this.mFile.recordExistingFile(file);
            return file;
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/io/MockFileOp.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/io/MockFileOp.java
//Synthetic comment -- index 2af4742..1d46c18 100755

//Synthetic comment -- @@ -20,6 +20,7 @@

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
//Synthetic comment -- @@ -27,6 +28,7 @@
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
//Synthetic comment -- @@ -165,17 +167,9 @@
}

/**
     * Sets the executable Unix permission (+x) on a file or folder.
* <p/>
     * This attempts to use File#setExecutable through reflection if
     * it's available.
     * If this is not available, this invokes a chmod exec instead,
     * so there is no guarantee of it being fast.
     * <p/>
     * Caller must make sure to not invoke this under Windows.
     *
     * @param file The file to set permissions on.
     * @throws IOException If an I/O error occurs
*/
@Override
public void setExecutablePermission(File file) throws IOException {
//Synthetic comment -- @@ -184,6 +178,8 @@

/**
* {@inheritDoc}
*/
@Override
public void setReadOnly(File file) {
//Synthetic comment -- @@ -191,12 +187,9 @@
}

/**
     * Copies a binary file.
     *
     * @param source the source file to copy.
     * @param dest the destination file to write.
     * @throws FileNotFoundException if the source file doesn't exist.
     * @throws IOException if there's a problem reading or writing the file.
*/
@Override
public void copyFile(File source, File dest) throws IOException {
//Synthetic comment -- @@ -369,6 +362,56 @@
}

/**
* Returns an OutputStream that will capture the bytes written and associate
* them with the given file.
*/







