/*SDK Manager: support for partial resume of downloads.

Change-Id:I248194b5764cf801e52ebd35c6b5963facf31a3e*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index 01a2078..0667b74 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdklib.internal.repository;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
//Synthetic comment -- @@ -49,7 +50,7 @@
/**
* A simple cache for the XML resources handled by the SDK Manager.
* <p/>
 * Callers should use {@link #openDirectUrl} to download "large files"
* that should not be cached (like actual installation packages which are several MBs big)
* and call {@link #openCachedUrl(String, ITaskMonitor)} to download small XML files.
* <p/>
//Synthetic comment -- @@ -284,35 +285,33 @@
* documentation.
*
* @param urlString the URL string to be opened.
     * @param headers An optional set of headers to pass when requesting the resource. Can be null.
* @param monitor {@link ITaskMonitor} which is related to this URL
*                 fetching.
     * @return Returns a pair with a {@link InputStream} and an {@link HttpResponse}.
     *              The pair is never null.
     *              The input stream can be null in case of error, although in general the
     *              method will probably throw an exception instead.
     *              The caller should look at the response code's status and only accept the
     *              input stream if it's the desired code (e.g. 200 or 206).
* @throws IOException Exception thrown when there are problems retrieving
*                 the URL or its content.
* @throws CanceledByUserException Exception thrown if the user cancels the
*              authentication dialog.
*/
    public Pair<InputStream, HttpResponse> openDirectUrl(
            @NonNull  String urlString,
            @Nullable Header[] headers,
            @NonNull  ITaskMonitor monitor)
                throws IOException, CanceledByUserException {
if (DEBUG) {
System.out.println(String.format("%s : Direct download", urlString)); //$NON-NLS-1$
}
        return UrlOpener.openUrl(
urlString,
false /*needsMarkResetSupport*/,
monitor,
                headers);
}

/**
//Synthetic comment -- @@ -321,8 +320,7 @@
* from the cache, potentially updated first or directly downloaded.
* <p/>
* For large downloads (e.g. installable archives) please do not invoke the
     * cache and instead use the {@link #openDirectUrl} method.
* <p/>
* For details on realm authentication and user/password handling,
* check the underlying {@link UrlOpener#openUrl(String, boolean, ITaskMonitor, Header[])}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index 72ec65f..baccc27 100644

//Synthetic comment -- @@ -69,10 +69,10 @@
* @see #openUrl(String, boolean, ITaskMonitor, Header[])
* <p/>
* Implementation detail: callers should use {@link DownloadCache} instead of this class.
 * {@link DownloadCache#openDirectUrl} is a direct pass-through to {@link UrlOpener} since
 * there's no caching. However from an implementation perspective it's still recommended
 * to pass down a {@link DownloadCache} instance, which will let us override the implementation
 * later on (for testing, for example.)
*/
class UrlOpener {









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java
//Synthetic comment -- index e7c10d4..635eaff 100755

//Synthetic comment -- @@ -32,9 +32,15 @@
import com.android.sdklib.util.GrabProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.IProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.Wait;
import com.android.utils.Pair;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;

import java.io.EOFException;
import java.io.File;
//Synthetic comment -- @@ -43,12 +49,13 @@
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
//Synthetic comment -- @@ -59,7 +66,8 @@
*/
public class ArchiveInstaller {

    private static final String PROP_STATUS_CODE = "StatusCode";                    //$NON-NLS-1$
    public static final String ENV_VAR_IGNORE_COMPAT = "ANDROID_SDK_IGNORE_COMPAT"; //$NON-NLS-1$

public static final int NUM_MONITOR_INC = 100;

//Synthetic comment -- @@ -108,7 +116,6 @@
Archive newArchive = archiveInfo.getNewArchive();
Package pkg = newArchive.getParentPackage();

String name = pkg.getShortDescription();

if (newArchive.isLocal()) {
//Synthetic comment -- @@ -129,13 +136,16 @@
return false;
}

        Pair<File, File> files = downloadFile(newArchive, osSdkRoot, cache, monitor, forceHttp);
        File tmpFile   = files == null ? null : files.getFirst();
        File propsFile = files == null ? null : files.getSecond();
        if (tmpFile != null) {
// Unarchive calls the pre/postInstallHook methods.
            if (unarchive(archiveInfo, osSdkRoot, tmpFile, sdkManager, monitor)) {
monitor.log("Installed %1$s", name);
// Delete the temp archive if it exists, only on success
                mFileOp.deleteFileOrFolder(tmpFile);
                mFileOp.deleteFileOrFolder(propsFile);
return true;
}
}
//Synthetic comment -- @@ -148,7 +158,7 @@
* Caller is responsible with deleting the temp file when done.
*/
@VisibleForTesting(visibility=Visibility.PRIVATE)
    protected Pair<File, File> downloadFile(Archive archive,
String osSdkRoot,
DownloadCache cache,
ITaskMonitor monitor,
//Synthetic comment -- @@ -204,6 +214,9 @@
}
File tmpFile = new File(tmpFolder, base);

        // property file were we'll keep partial/resume information for reuse.
        File propsFile = new File(tmpFolder, base + ".inf"); //$NON-NLS-1$

// if the file exists, check its checksum & size. Use it if complete
if (mFileOp.exists(tmpFile)) {
if (mFileOp.length(tmpFile) == archive.getSize()) {
//Synthetic comment -- @@ -217,25 +230,76 @@
}
if (chksum.equalsIgnoreCase(archive.getChecksum())) {
// File is good, let's use it.
                    return Pair.of(tmpFile, propsFile);
                } else {
                    // The file has the right size but the wrong content.
                    // Just remove it and this will trigger a full download below.
                    mFileOp.deleteFileOrFolder(tmpFile);
}
}
}

        Header[] resumeHeaders = preparePartialDownload(archive, tmpFile, propsFile);

        if (fetchUrl(archive, resumeHeaders, tmpFile, propsFile, link, pkgName, cache, monitor)) {
// Fetching was successful, let's use this file.
            return Pair.of(tmpFile, propsFile);
}
        return null;
    }

    /**
     * Prepares to do a partial/resume download.
     *
     * @param archive The archive we're trying to download.
     * @param tmpFile The destination file to download (e.g. something.zip)
     * @param propsFile A properties file generated by the last partial download (e.g. .zip.inf)
     * @return Null in case we should perform a full download, or a set of headers
     *      to resume a partial download.
     */
    private Header[] preparePartialDownload(Archive archive, File tmpFile, File propsFile) {
        // We need both the destination file and its properties to do a resume.
        if (mFileOp.isFile(tmpFile) && mFileOp.isFile(propsFile)) {
            // The caller already checked the case were the destination file has the
            // right size _and_ checksum, so we know at this point one of them is wrong
            // here.
            // We can obviously only resume a file if it's size is smaller than expected.
            if (mFileOp.length(tmpFile) < archive.getSize()) {
                Properties props = mFileOp.loadProperties(propsFile);

                List<Header> headers = new ArrayList<Header>(2);
                headers.add(new BasicHeader(HttpHeaders.RANGE,
                                            String.format("bytes=%d-", mFileOp.length(tmpFile))));

                // Don't use the properties if there's not at least a 200 or 206 code from
                // the last download.
                int status = 0;
                try {
                    status = Integer.parseInt(props.getProperty(PROP_STATUS_CODE));
                } catch (Exception ignore) {}

                if (status == HttpStatus.SC_OK || status == HttpStatus.SC_PARTIAL_CONTENT) {
                    // Do we have an ETag and/or a Last-Modified?
                    String etag = props.getProperty(HttpHeaders.ETAG);
                    String lastMod = props.getProperty(HttpHeaders.LAST_MODIFIED);

                    if (etag != null && etag.length() > 0) {
                        headers.add(new BasicHeader(HttpHeaders.IF_MATCH, etag));
                    } else if (lastMod != null && lastMod.length() > 0) {
                        headers.add(new BasicHeader(HttpHeaders.IF_MATCH, lastMod));
                    }

                    return headers.toArray(new Header[headers.size()]);
                }
            }
        }

        // Existing file is either of different size or content.
        // Remove the existing file and request a full download.
        mFileOp.deleteFileOrFolder(tmpFile);
        mFileOp.deleteFileOrFolder(propsFile);

        return null;
}

/**
//Synthetic comment -- @@ -306,9 +370,23 @@
* SHA1 as expected. Returns true on success or false if any of those checks fail.
* <p/>
* Increments the monitor by {@link #NUM_MONITOR_INC}.
     *
     * @param archive The archive we're trying to download.
     * @param resumeHeaders The headers to use for a partial resume, or null when fetching
     *          a whole new file.
     * @param tmpFile The destination file to download (e.g. something.zip)
     * @param propsFile A properties file generated by the last partial download (e.g. .zip.inf)
     * @param urlString The URL as a string
     * @param pkgName The archive's package name, used for progress output.
     * @param cache The {@link DownloadCache} instance to use.
     * @param monitor The monitor to output the progress and errors.
     * @return True if we fetched the file successfully.
     *         False if the download failed or was aborted.
*/
private boolean fetchUrl(Archive archive,
            Header[] resumeHeaders,
File tmpFile,
            File propsFile,
String urlString,
String pkgName,
DownloadCache cache,
//Synthetic comment -- @@ -318,20 +396,85 @@
InputStream is = null;
int inc_remain = NUM_MONITOR_INC;
try {
            Pair<InputStream, HttpResponse> result = cache.openDirectUrl(urlString, resumeHeaders, monitor);

            is = result.getFirst();
            HttpResponse resp = result.getSecond();
            int status = resp.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_NOT_FOUND) {
                throw new Exception("URL not found.");
            }
            if (is == null) {
                throw new Exception("No content.");
            }


            Properties props = new Properties();
            props.setProperty(PROP_STATUS_CODE, Integer.toString(status));
            if (resp.containsHeader(HttpHeaders.ETAG)) {
                props.setProperty(HttpHeaders.ETAG,
                                  resp.getFirstHeader(HttpHeaders.ETAG).getValue());
            }
            if (resp.containsHeader(HttpHeaders.LAST_MODIFIED)) {
                props.setProperty(HttpHeaders.LAST_MODIFIED,
                                  resp.getFirstHeader(HttpHeaders.LAST_MODIFIED).getValue());
            }

            mFileOp.saveProperties(propsFile, props, "## Android SDK Download.");  //$NON-NLS-1$

            // On success, status can be:
            // - 206 (Partial content), if resumeHeaders is not null (we asked for a partial
            //   download, and we get partial content for that download => we'll need to append
            //   to the existing file.)
            // - 200 (OK) meaning we're getting whole new content from scratch. This can happen
            //   even if resumeHeaders is not null (typically means the server has a new version
            //   of the file to server.) In this case we reset the file and write from scratch.

            boolean append = status == HttpStatus.SC_PARTIAL_CONTENT;
            if (status != HttpStatus.SC_OK && !(append && resumeHeaders != null)) {
                throw new Exception(String.format("Unexpected HTTP Status %1$d", status));
            }
MessageDigest digester = archive.getChecksumType().getMessageDigest();

            if (append) {
                // Seed the digest with the existing content.
                InputStream temp = null;
                try {
                    temp = new FileInputStream(tmpFile);

                    byte[] buf = new byte[65536];
                    int n;

                    while ((n = temp.read(buf)) >= 0) {
                        if (n > 0) {
                            digester.update(buf, 0, n);
                        }
                    }
                } catch (Exception ignore) {
                } finally {
                    if (temp != null) {
                        try {
                            temp.close();
                        } catch (IOException ignore) {}
                    }
                }
            }

            // Open the output stream in append for a resume, or reset for a full download.
            os = new FileOutputStream(tmpFile, append);

byte[] buf = new byte[65536];
int n;

long total = 0;
long size = archive.getSize();
            if (append) {
                long len = mFileOp.length(tmpFile);
                int percent = (int) (len * 100 / size);
                size -= len;
                monitor.logVerbose(
                        "Resuming %1$s download at %2$d (%3$d%%)", pkgName, len, percent);
            }
long inc = size / NUM_MONITOR_INC;
long next_inc = inc;

//Synthetic comment -- @@ -412,10 +555,10 @@

} catch (FileNotFoundException e) {
// The FNF message is just the URL. Make it a bit more useful.
            monitor.logError("URL not found: %1$s", e.getMessage());

} catch (Exception e) {
            monitor.logError("Download interrupted: %1$s", e.getMessage());   //$NON-NLS-1$

} finally {
if (os != null) {
//Synthetic comment -- @@ -932,27 +1075,10 @@
pkg.saveProperties(props);
}

        return mFileOp.saveProperties(
                new File(unzipDestFolder, SdkConstants.FN_SOURCE_PROP),
                props,
                "## Android Tool: Source of this archive.");  //$NON-NLS-1$
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FileOp.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FileOp.java
//Synthetic comment -- index 7f7a1f2..0585e22 100755

//Synthetic comment -- @@ -27,6 +27,7 @@
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;


/**
//Synthetic comment -- @@ -89,6 +90,7 @@
* Helper to delete a file or a directory.
* For a directory, recursively deletes all of its content.
* Files that cannot be deleted right away are marked for deletion on exit.
     * It's ok for the file or folder to not exist at all.
* The argument can be null.
*/
@Override
//Synthetic comment -- @@ -104,6 +106,11 @@
}
}

            // Don't try to delete it if it doesn't exist.
            if (!exists(fileOrFolder)) {
                return;
            }

if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) {
// Trying to delete a resource on windows might fail if there's a file
// indexer locking the resource. Generally retrying will be enough to
//Synthetic comment -- @@ -337,4 +344,43 @@
public OutputStream newFileOutputStream(File file) throws FileNotFoundException {
return new FileOutputStream(file);
}

    @Override
    public Properties loadProperties(File file) {
        Properties props = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            props.load(fis);
        } catch (IOException ignore) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ignore) {}
            }
        }
        return props;
    }

    @Override
    public boolean saveProperties(File file, Properties props, String comments) {
        OutputStream fos = null;
        try {
            fos = newFileOutputStream(file);

            props.store(fos, comments);
            return true;
        } catch (IOException ignore) {
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
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/IFileOp.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/IFileOp.java
//Synthetic comment -- index b0d442b..5b131d5 100755

//Synthetic comment -- @@ -16,11 +16,14 @@

package com.android.sdklib.io;

import com.android.annotations.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;


/**
//Synthetic comment -- @@ -34,6 +37,7 @@
* Helper to delete a file or a directory.
* For a directory, recursively deletes all of its content.
* Files that cannot be deleted right away are marked for deletion on exit.
     * It's ok for the file or folder to not exist at all.
* The argument can be null.
*/
public abstract void deleteFileOrFolder(File fileOrFolder);
//Synthetic comment -- @@ -111,4 +115,25 @@
/** Creates a new {@link FileOutputStream} for the given {@code file}. */
public abstract OutputStream newFileOutputStream(File file) throws FileNotFoundException;

    /**
     * Load {@link Properties} from a file. Returns an empty property set on error.
     *
     * @param file A non-null file to load from. File may not exist.
     * @return A new {@link Properties} with the properties loaded from the file,
     *          or an empty property set in case of error.
     */
    public @NonNull Properties loadProperties(@NonNull File file);

    /**
     * Saves (write, store) the given {@link Properties} into the given {@link File}.
     *
     * @param file A non-null file to write to.
     * @param props The properties to write.
     * @param comments A non-null description of the properly list, written in the file.
     * @return True if the properties could be saved, false otherwise.
     */
    public boolean saveProperties(
            @NonNull File file,
            @NonNull Properties props,
            @NonNull String comments);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/archives/ArchiveInstallerTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/archives/ArchiveInstallerTest.java
//Synthetic comment -- index 609481b..b0b3f03 100755

//Synthetic comment -- @@ -30,6 +30,7 @@
import com.android.sdklib.io.IFileOp;
import com.android.sdklib.io.MockFileOp;
import com.android.sdklib.repository.PkgProps;
import com.android.utils.Pair;

import java.io.File;
import java.util.Arrays;
//Synthetic comment -- @@ -64,7 +65,7 @@
}

@Override
        protected Pair<File, File> downloadFile(
Archive archive,
String osSdkRoot,
DownloadCache cache,
//Synthetic comment -- @@ -73,7 +74,7 @@
File file = mDownloadMap.get(archive);
// register the file as "created"
ArchiveInstallerTest.this.mFile.recordExistingFile(file);
            return Pair.of(file, null);
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/io/MockFileOp.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/io/MockFileOp.java
//Synthetic comment -- index 2af4742..1d46c18 100755

//Synthetic comment -- @@ -20,6 +20,7 @@

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
//Synthetic comment -- @@ -27,6 +28,7 @@
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
//Synthetic comment -- @@ -165,17 +167,9 @@
}

/**
     * {@inheritDoc}
* <p/>
     * <em>Note: this mock version does nothing.</em>
*/
@Override
public void setExecutablePermission(File file) throws IOException {
//Synthetic comment -- @@ -184,6 +178,8 @@

/**
* {@inheritDoc}
     * <p/>
     * <em>Note: this mock version does nothing.</em>
*/
@Override
public void setReadOnly(File file) {
//Synthetic comment -- @@ -191,12 +187,9 @@
}

/**
     * {@inheritDoc}
     * <p/>
     * <em>Note: this mock version does nothing.</em>
*/
@Override
public void copyFile(File source, File dest) throws IOException {
//Synthetic comment -- @@ -369,6 +362,56 @@
}

/**
     * {@inheritDoc}
     * <p/>
     * <em>TODO: we might want to overload this to read mock properties instead of a real file.</em>
     */
    @Override
    public Properties loadProperties(File file) {
        Properties props = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            props.load(fis);
        } catch (IOException ignore) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ignore) {}
            }
        }
        return props;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <em>Note that this uses the mock version of {@link #newFileOutputStream(File)} and thus
     * records the write rather than actually performing it.</em>
     */
    @Override
    public boolean saveProperties(File file, Properties props, String comments) {
        OutputStream fos = null;
        try {
            fos = newFileOutputStream(file);

            props.store(fos, comments);
            return true;
        } catch (IOException ignore) {
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
* Returns an OutputStream that will capture the bytes written and associate
* them with the given file.
*/







