/*New ApkBuilder class.

this is meant to replace the one previously in apkbuilder.jar and the
one in ADT, while being part of the public sdklib API so that other
tools can use it if needed (to deprecate the command line version)

Change-Id:I13f2a09d8d507a85be33af3fe659d175819cb641*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java
//Synthetic comment -- index d5d2185..73f6fcb 100644

//Synthetic comment -- @@ -101,8 +101,6 @@
public final static String FN_COMPILED_RESOURCE_CLASS = FN_RESOURCE_BASE + DOT_CLASS;
/** Manifest java class filename, i.e. "Manifest.java" */
public final static String FN_MANIFEST_CLASS = "Manifest.java"; //$NON-NLS-1$
/** Temporary packaged resources file name, i.e. "resources.ap_" */
public final static String FN_RESOURCES_AP_ = "resources.ap_"; //$NON-NLS-1$
/** Temporary packaged resources file name for a specific set of configuration */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilderHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilderHelper.java
//Synthetic comment -- index 04acf44..fdb9942 100644

//Synthetic comment -- @@ -838,15 +838,11 @@

// check the name ends with .jar
if (AndroidConstants.EXT_JAR.equalsIgnoreCase(path.getFileExtension())) {
IResource resource = wsRoot.findMember(path);
if (resource != null && resource.exists() &&
resource.getType() == IResource.FILE) {
oslibraryList.add(resource.getLocation().toOSString());
                        } else {
// if the jar path doesn't match a workspace resource,
// then we get an OSString and check if this links to a valid file.
String osFullPath = path.toOSString();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index 36afc5b..824719a 100644

//Synthetic comment -- @@ -48,6 +48,9 @@

/** An SDK Project's AndroidManifest.xml file */
public static final String FN_ANDROID_MANIFEST_XML= "AndroidManifest.xml";
    /** Dex filename inside the APK. i.e. "classes.dex" */
    public final static String FN_APK_CLASSES_DEX = "classes.dex"; //$NON-NLS-1$

/** An SDK Project's build.xml file */
public final static String FN_BUILD_XML = "build.xml";

//Synthetic comment -- @@ -132,6 +135,11 @@
/** properties file for the SDK */
public final static String FN_SDK_PROP = "sdk.properties"; //$NON-NLS-1$

    /**
     * filename for gdbserver.
     */
    public final static String FN_GDBSERVER = "gdbserver";

/* Folder Names for Android Projects . */

/** Resources folder name, i.e. "res". */








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java
new file mode 100644
//Synthetic comment -- index 0000000..4358435

//Synthetic comment -- @@ -0,0 +1,809 @@
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

package com.android.sdklib.build;

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.sdklib.internal.build.SignedJarBuilder;
import com.android.sdklib.internal.build.DebugKeyProvider.IKeyGenOutput;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;
import com.android.sdklib.internal.build.SignedJarBuilder.IZipEntryFilter;
import com.android.sdklib.internal.build.SignedJarBuilder.IZipEntryFilter.ZipAbortException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class making the final apk packaging.
 * The inputs are:
 * - packaged resources (output of aapt)
 * - code file (ouput of dx)
 * - Java resources coming from the project, its libraries, and its jar files
 * - Native libraries from the project or its library.
 *
 */
public final class ApkBuilder {

    private final static Pattern PATTERN_NATIVELIB_EXT = Pattern.compile("^.+\\.so$",
            Pattern.CASE_INSENSITIVE);

    /**
     * A No-op zip filter. It's used to detect conflicts.
     *
     */
    private final class NullZipFilter implements IZipEntryFilter {
        private File mInputFile;

        void reset(File inputFile) {
            mInputFile = inputFile;
        }

        public boolean checkEntry(String archivePath) throws ZipAbortException {
            verbosePrintln("=> %s", archivePath);

            File duplicate = checkFileForDuplicate(archivePath);
            if (duplicate != null) {
                throw new DuplicateApkFileException(archivePath, duplicate, mInputFile);
            } else {
                mAddedFiles.put(archivePath, mInputFile);
            }

            return true;
        }
    }

    /**
     * Custom {@link IZipEntryFilter} to filter out everything that is not a standard java
     * resources, and also record whether the zip file contains native libraries.
     * <p/>Used in {@link SignedJarBuilder#writeZip(java.io.InputStream, IZipEntryFilter)} when
     * we only want the java resources from external jars.
     */
    private final class JavaAndNativeResourceFilter implements IZipEntryFilter {
        private final List<String> mNativeLibs = new ArrayList<String>();
        private boolean mNativeLibsConflict = false;
        private File mInputFile;

        public boolean checkEntry(String archivePath) throws ZipAbortException {
            // split the path into segments.
            String[] segments = archivePath.split("/");

            // empty path? skip to next entry.
            if (segments.length == 0) {
                return false;
            }

            // Check each folders to make sure they should be included.
            // Folders like CVS, .svn, etc.. should already have been excluded from the
            // jar file, but we need to exclude some other folder (like /META-INF) so
            // we check anyway.
            for (int i = 0 ; i < segments.length - 1; i++) {
                if (checkFolderForPackaging(segments[i]) == false) {
                    return false;
                }
            }

            // get the file name from the path
            String fileName = segments[segments.length-1];

            boolean check = checkFileForPackaging(fileName);

            // only do additional checks if the file passes the default checks.
            if (check) {
                verbosePrintln("=> %s", archivePath);

                File duplicate = checkFileForDuplicate(archivePath);
                if (duplicate != null) {
                    throw new DuplicateApkFileException(archivePath, duplicate, mInputFile);
                } else {
                    mAddedFiles.put(archivePath, mInputFile);
                }

                if (archivePath.endsWith(".so")) {
                    mNativeLibs.add(archivePath);

                    // only .so located in lib/ will interfere with the installation
                    if (archivePath.startsWith(SdkConstants.FD_APK_NATIVE_LIBS + "/")) {
                        mNativeLibsConflict = true;
                    }
                } else if (archivePath.endsWith(".jnilib")) {
                    mNativeLibs.add(archivePath);
                }
            }

            return check;
        }

        List<String> getNativeLibs() {
            return mNativeLibs;
        }

        boolean getNativeLibsConflict() {
            return mNativeLibsConflict;
        }

        void reset(File inputFile) {
            mInputFile = inputFile;
            mNativeLibs.clear();
            mNativeLibsConflict = false;
        }
    }

    private final File mApkFile;
    private final File mResFile;
    private final File mDexFile;
    private final PrintStream mVerboseStream;
    private final SignedJarBuilder mBuilder;
    private boolean mDebugMode = false;
    private boolean mIsSealed = false;

    private final NullZipFilter mNullFilter = new NullZipFilter();
    private final JavaAndNativeResourceFilter mFilter = new JavaAndNativeResourceFilter();
    private final HashMap<String, File> mAddedFiles = new HashMap<String, File>();

    /**
     * An exception thrown during packaging of an APK file.
     */
    public final static class ApkCreationException extends Exception {
        private static final long serialVersionUID = 1L;

        public ApkCreationException(String format, Object... args) {
            super(String.format(format, args));
        }

        public ApkCreationException(Throwable cause, String format, Object... args) {
            super(String.format(format, args), cause);
        }

        public ApkCreationException(Throwable cause) {
            super(cause);
        }
    }

    /**
     * An exception thrown during packaging of an APK file.
     */
    public final static class DuplicateApkFileException extends ZipAbortException {
        private static final long serialVersionUID = 1L;
        private final String mArchivePath;
        private final File mFile1;
        private final File mFile2;

        public DuplicateApkFileException(String archivePath, File file1, File file2) {
            super();
            mArchivePath = archivePath;
            mFile1 = file1;
            mFile2 = file2;
        }

        public String getArchivePath() {
            return mArchivePath;
        }

        public File getFile1() {
            return mFile1;
        }

        public File getFile2() {
            return mFile2;
        }
    }

    /**
     * An exception thrown when trying to add files to a sealed APK.
     */
    public final static class SealedApkException extends Exception {
        private static final long serialVersionUID = 1L;

        public SealedApkException(String format, Object... args) {
            super(String.format(format, args));
        }

        public SealedApkException(Throwable cause, String format, Object... args) {
            super(String.format(format, args), cause);
        }

        public SealedApkException(Throwable cause) {
            super(cause);
        }
    }

    /**
     * Status for the addition of a jar file resources into the APK.
     * This indicates possible issues with native library inside the jar file.
     */
    public interface JarStatus {
        /**
         * Returns the list of native libraries found in the jar file.
         */
        List<String> getNativeLibs();

        /**
         * Returns whether some of those libraries were located in the location that Android
         * expects its native libraries.
         */
        boolean hasNativeLibsConflicts();

    }

    /** Internal implementation of {@link JarStatus}. */
    public final static class JarStatusImpl implements JarStatus {
        public final List<String> mLibs;
        public final boolean mNativeLibsConflict;

        public JarStatusImpl(List<String> libs, boolean nativeLibsConflict) {
            mLibs = libs;
            mNativeLibsConflict = nativeLibsConflict;
        }

        public List<String> getNativeLibs() {
            return mLibs;
        }

        public boolean hasNativeLibsConflicts() {
            return mNativeLibsConflict;
        }
    }

    /**
     * Creates a new instance.
     * @param apkOsPath the OS path of the file to create.
     * @param resOsPath the OS path of the packaged resource file.
     * @param dexOsPath the OS path of the dex file.
     * @throws ApkCreationException
     */
    public ApkBuilder(String apkOsPath, String resOsPath, String dexOsPath, String storeOsPath,
            PrintStream verboseStream) throws ApkCreationException {
        this(new File(apkOsPath), new File(resOsPath), new File(dexOsPath), storeOsPath,
                verboseStream);
    }

    /**
     * Creates a new instance.
     *
     * This creates a new builder that will create the specified output file, using the two
     * mandatory given input files.
     *
     * An optional debug keystore can be provided. If set, it is expected that the store password
     * is 'android' and the key alias and password are 'androiddebugkey' and 'android'
     *
     * An option {@link PrintStream} can also be provided for verbose output. If null, there will
     * be no output.
     *
     * @param apkFile the file to create
     * @param resFile the file representing the packaged resource file.
     * @param dexFile the file representing the dex file.
     * @param storeOsPath the OS path to the debug keystore, if needed or null.
     * @param verboseStream the stream to which verbose output should go. If null, verbose mode
     *                      is not enabled.
     * @throws ApkCreationException
     */
    public ApkBuilder(File apkFile, File resFile, File dexFile, String storeOsPath,
            PrintStream verboseStream) throws ApkCreationException {
        checkOutputFile(mApkFile = apkFile);
        checkInputFile(mResFile = resFile);
        checkInputFile(mDexFile = dexFile);
        mVerboseStream = verboseStream;

        try {
            File storeFile = null;
            if (storeOsPath != null) {
                storeFile = new File(storeOsPath);
                checkInputFile(storeFile);
            }

            if (storeFile != null) {
                // get the debug key
                verbosePrintln("Using keystore: %s", storeOsPath);

                IKeyGenOutput keygenOutput = null;
                if (mVerboseStream != null) {
                    keygenOutput = new IKeyGenOutput() {
                        public void out(String message) {
                            mVerboseStream.println(message);
                        }

                        public void err(String message) {
                            mVerboseStream.println(message);
                        }
                    };
                }

                DebugKeyProvider keyProvider = new DebugKeyProvider(
                        storeOsPath, null /*store type*/, keygenOutput);

                PrivateKey key = keyProvider.getDebugKey();
                X509Certificate certificate = (X509Certificate)keyProvider.getCertificate();

                if (key == null) {
                    throw new ApkCreationException("Unable to get debug signature key");
                }

                // compare the certificate expiration date
                if (certificate != null && certificate.getNotAfter().compareTo(new Date()) < 0) {
                    // TODO, regenerate a new one.
                    throw new ApkCreationException("Debug Certificate expired on " +
                            DateFormat.getInstance().format(certificate.getNotAfter()));
                }

                mBuilder = new SignedJarBuilder(
                        new FileOutputStream(mApkFile, false /* append */), key,
                        certificate);
            } else {
                mBuilder = new SignedJarBuilder(
                        new FileOutputStream(mApkFile, false /* append */),
                        null /* key */, null /* certificate */);
            }

            verbosePrintln("Packaging %s", mApkFile.getName());

            // add the resources
            addZipFile(mResFile);

            // add the class dex file at the root of the apk
            addFile(mDexFile, SdkConstants.FN_APK_CLASSES_DEX);

        } catch (KeytoolException e) {
            if (e.getJavaHome() == null) {
                throw new ApkCreationException(e.getMessage() +
                        "\nJAVA_HOME seems undefined, setting it will help locating keytool automatically\n" +
                        "You can also manually execute the following command\n:" +
                        e.getCommandLine());
            } else {
                throw new ApkCreationException(e.getMessage() +
                        "\nJAVA_HOME is set to: " + e.getJavaHome() +
                        "\nUpdate it if necessary, or manually execute the following command:\n" +
                        e.getCommandLine());
            }
        } catch (Exception e) {
            throw new ApkCreationException(e);
        }
    }

    /**
     * Sets the debug mode. In debug mode, when native libraries are present, the packaging
     * will also include one or more copies of gdbserver in the final APK file.
     *
     * These are used for debugging native code, to ensure that gdbserver is accessible to the
     * application.
     *
     * There will be one version of gdb_server for each ABI supported by the application.
     *
     * the gbdserver files are placed in the libs/abi/ folders automatically by the NDK.
     *
     * @param debugMode the debug mode flag.
     */
    public void setDebugMode(boolean debugMode) {
        mDebugMode = debugMode;
    }

    /**
     * Adds a file to the APK at a given path
     * @param file the file to add
     * @param archivePath the path of the file inside the APK archive.
     * @throws ApkCreationException if an error occurred
     * @throws SealedApkException if the APK is already sealed.
     * @throws DuplicateApkFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
     */
    public void addFile(File file, String archivePath) throws ApkCreationException,
            SealedApkException, DuplicateApkFileException {
        if (mIsSealed) {
            throw new SealedApkException("APK is already sealed");
        }

        try {
            doAddFile(file, archivePath);
        } catch (DuplicateApkFileException e) {
            throw e;
        } catch (Exception e) {
            throw new ApkCreationException(e, "Failed to add %s", file);
        }
    }

    /**
     * Adds the content from a zip file.
     * All file keep the same path inside the archive.
     * @param zipFile the zip File.
     * @throws ApkCreationException if an error occurred
     * @throws SealedApkException if the APK is already sealed.
     * @throws DuplicateApkFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
     */
    public void addZipFile(File zipFile) throws ApkCreationException, SealedApkException,
            DuplicateApkFileException {
        if (mIsSealed) {
            throw new SealedApkException("APK is already sealed");
        }

        try {
            verbosePrintln("%s:", zipFile);

            // reset the filter with this input.
            mNullFilter.reset(zipFile);

            // ask the builder to add the content of the file.
            FileInputStream fis = new FileInputStream(zipFile);
            mBuilder.writeZip(fis, mNullFilter);
        } catch (DuplicateApkFileException e) {
            throw e;
        } catch (Exception e) {
            throw new ApkCreationException(e, "Failed to add %s", zipFile);
        }
    }

    /**
     * Adds the resources from a jar file.
     * @param jarFile the jar File.
     * @return a {@link JarStatus} object indicating if native libraries where found in
     *         the jar file.
     * @throws ApkCreationException if an error occurred
     * @throws SealedApkException if the APK is already sealed.
     * @throws DuplicateApkFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
     */
    public JarStatus addResourcesFromJar(File jarFile) throws ApkCreationException,
            SealedApkException, DuplicateApkFileException {
        if (mIsSealed) {
            throw new SealedApkException("APK is already sealed");
        }

        try {
            verbosePrintln("%s:", jarFile);

            // reset the filter with this input.
            mFilter.reset(jarFile);

            // ask the builder to add the content of the file, filtered to only let through
            // the java resources.
            FileInputStream fis = new FileInputStream(jarFile);
            mBuilder.writeZip(fis, mFilter);

            // check if native libraries were found in the external library. This should
            // constitutes an error or warning depending on if they are in lib/
            return new JarStatusImpl(mFilter.getNativeLibs(), mFilter.getNativeLibsConflict());
        } catch (DuplicateApkFileException e) {
            throw e;
        } catch (Exception e) {
            throw new ApkCreationException(e, "Failed to add %s", jarFile);
        }
    }

    /**
     * Adds the resources from a source folder.
     * @param sourceFolder the source folder.
     * @throws ApkCreationException if an error occurred
     * @throws SealedApkException if the APK is already sealed.
     * @throws DuplicateApkFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
     */
    public void addSourceFolder(File sourceFolder) throws ApkCreationException, SealedApkException,
            DuplicateApkFileException {
        if (mIsSealed) {
            throw new SealedApkException("APK is already sealed");
        }

        if (sourceFolder.isDirectory()) {
            try {
                // file is a directory, process its content.
                File[] files = sourceFolder.listFiles();
                for (File file : files) {
                    processFileForResource(file, null);
                }
            } catch (DuplicateApkFileException e) {
                throw e;
            } catch (Exception e) {
                throw new ApkCreationException(e, "Failed to add %s", sourceFolder);
            }
        } else {
            // not a directory? check if it's a file or doesn't exist
            if (sourceFolder.exists()) {
                throw new ApkCreationException("%s is not a folder", sourceFolder);
            } else {
                throw new ApkCreationException("%s does not exist", sourceFolder);
            }
        }
    }

    /**
     * Adds the native libraries from the top native folder.
     * The content of this folder must be the various ABI folders.
     *
     * This may or may not copy gdbserver into the apk based on whether the debug mode is set.
     *
     * @param nativeFolder the native folder.
     * @param abiFilter an optional filter. If not null, then only the matching ABI is included in
     * the final archive
     * @throws ApkCreationException if an error occurred
     * @throws SealedApkException if the APK is already sealed.
     * @throws DuplicateApkFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
     *
     * @see #setDebugMode(boolean)
     */
    public void addNativeLibraries(File nativeFolder, String abiFilter)
            throws ApkCreationException, SealedApkException, DuplicateApkFileException {
        if (mIsSealed) {
            throw new SealedApkException("APK is already sealed");
        }

        if (nativeFolder.isDirectory() == false) {
            // not a directory? check if it's a file or doesn't exist
            if (nativeFolder.exists()) {
                throw new ApkCreationException("%s is not a folder", nativeFolder);
            } else {
                throw new ApkCreationException("%s does not exist", nativeFolder);
            }
        }

        File[] abiList = nativeFolder.listFiles();

        if (abiFilter != null) {
            verbosePrintln("Native folder: %1$s with filter %2$ss", nativeFolder, abiFilter);
        } else {
            verbosePrintln("Native folder: %s", nativeFolder);
        }

        if (abiList != null) {
            for (File abi : abiList) {
                if (abi.isDirectory()) { // ignore files

                    // check the abi filter and reject all other ABIs
                    if (abiFilter != null && abiFilter.equals(abi.getName()) == false) {
                        continue;
                    }

                    File[] libs = abi.listFiles();
                    if (libs != null) {
                        for (File lib : libs) {
                            // only consider files that are .so or, if in debug mode, that
                            // are gdbserver executables
                            if (lib.isFile() &&
                                    (PATTERN_NATIVELIB_EXT.matcher(lib.getName()).matches() ||
                                            (mDebugMode &&
                                                    SdkConstants.FN_GDBSERVER.equals(
                                                            lib.getName())))) {
                                String path =
                                    SdkConstants.FD_APK_NATIVE_LIBS + "/" +
                                    abi.getName() + "/" + lib.getName();

                                try {
                                    doAddFile(lib, path);
                                } catch (IOException e) {
                                    throw new ApkCreationException(e, "Failed to add %s", lib);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Seals the APK, and signs it if necessary.
     * @throws ApkCreationException
     * @throws ApkCreationException if an error occurred
     * @throws SealedApkException if the APK is already sealed.
     */
    public void sealApk() throws ApkCreationException, SealedApkException {
        if (mIsSealed) {
            throw new SealedApkException("APK is already sealed");
        }

        // close and sign the application package.
        try {
            mBuilder.close();
            mIsSealed = true;
        } catch (Exception e) {
            throw new ApkCreationException(e, "Failed to seal APK");
        }
    }

    /**
     * Output a given message if the verbose mode is enabled.
     * @param format the format string for {@link String#format(String, Object...)}
     * @param args the string arguments
     */
    private void verbosePrintln(String format, Object... args) {
        if (mVerboseStream != null) {
            mVerboseStream.println(String.format(format, args));
        }
    }

    private void doAddFile(File file, String archivePath) throws DuplicateApkFileException,
            IOException {
        verbosePrintln("%1$s => %2$s", file, archivePath);

        File duplicate = checkFileForDuplicate(archivePath);
        if (duplicate != null) {
            throw new DuplicateApkFileException(archivePath, duplicate, file);
        }

        mAddedFiles.put(archivePath, file);
        mBuilder.writeFile(file, archivePath);
    }

    /**
     * Processes a {@link File} that could be a {@link ApkFile}, or a folder containing
     * java resources.
     * @param file the {@link File} to process.
     * @param path the relative path of this file to the source folder. Can be <code>null</code> to
     * identify a root file.
     * @throws IOException
     * @throws DuplicateApkFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
     */
    private void processFileForResource(File file, String path)
            throws IOException, DuplicateApkFileException {
        if (file.isDirectory()) {
            // a directory? we check it
            if (checkFolderForPackaging(file.getName())) {
                // if it's valid, we append its name to the current path.
                if (path == null) {
                    path = file.getName();
                } else {
                    path = path + "/" + file.getName();
                }

                // and process its content.
                File[] files = file.listFiles();
                for (File contentFile : files) {
                    processFileForResource(contentFile, path);
                }
            }
        } else {
            // a file? we check it to make sure it should be added
            if (checkFileForPackaging(file.getName())) {
                // we append its name to the current path
                if (path == null) {
                    path = file.getName();
                } else {
                    path = path + "/" + file.getName();
                }

                // and add it to the apk
                doAddFile(file, path);
            }
        }
    }

    /**
     * Checks if the given path in the APK archive has not already been used and if it has been,
     * then returns a {@link File} object for the source of the duplicate
     * @param archivePath the archive path to test.
     * @return A File object of either a file at the same location or an archive that contains a
     * file that was put at the same location.
     */
    private File checkFileForDuplicate(String archivePath) {
        return mAddedFiles.get(archivePath);
    }

    /**
     * Checks an output {@link File} object.
     * This checks the following:
     * - the file is not an existing directory.
     * - if the file exists, that it can be modified.
     * - if it doesn't exists, that a new file can be created.
     * @param file the File to check
     * @throws ApkCreationException If the check fails
     */
    private void checkOutputFile(File file) throws ApkCreationException {
        if (file.isDirectory()) {
            throw new ApkCreationException("%s is a directory!", file);
        }

        if (file.exists()) { // will be a file in this case.
            if (file.canWrite() == false) {
                throw new ApkCreationException("Cannot write %s", file);
            }
        } else {
            try {
                if (file.createNewFile() == false) {
                    throw new ApkCreationException("Failed to create %s", file);
                }
            } catch (IOException e) {
                throw new ApkCreationException(
                        "Failed to create '%1$ss': %2$s", file, e.getMessage());
            }
        }
    }

    /**
     * Checks an input {@link File} object.
     * This checks the following:
     * - the file is not an existing directory.
     * - that the file exists and can be read.
     * @param file the File to check
     * @throws ApkCreationException If the check fails
     */
    private void checkInputFile(File file) throws ApkCreationException {
        if (file.isDirectory()) {
            throw new ApkCreationException("%s is a directory!", file);
        }

        if (file.exists()) {
            if (file.canRead() == false) {
                throw new ApkCreationException("Cannot read %s", file);
            }
        } else {
            throw new ApkCreationException("%s does not exist", file);
        }
    }

    /**
     * Checks whether a folder and its content is valid for packaging into the .apk as
     * standard Java resource.
     * @param folderName the name of the folder.
     */
    public static boolean checkFolderForPackaging(String folderName) {
        return folderName.equals("CVS") == false &&
            folderName.equals(".svn") == false &&
            folderName.equals("SCCS") == false &&
            folderName.equals("META-INF") == false &&
            folderName.startsWith("_") == false;
    }

    /**
     * Checks a file to make sure it should be packaged as standard resources.
     * @param fileName the name of the file (including extension)
     * @return true if the file should be packaged as standard java resources.
     */
    public static boolean checkFileForPackaging(String fileName) {
        String[] fileSegments = fileName.split("\\.");
        String fileExt = "";
        if (fileSegments.length > 1) {
            fileExt = fileSegments[fileSegments.length-1];
        }

        return checkFileForPackaging(fileName, fileExt);
    }

    /**
     * Checks a file to make sure it should be packaged as standard resources.
     * @param fileName the name of the file (including extension)
     * @param extension the extension of the file (excluding '.')
     * @return true if the file should be packaged as standard java resources.
     */
    public  static boolean checkFileForPackaging(String fileName, String extension) {
        // Note: this method is used by com.android.ide.eclipse.adt.internal.build.ApkBuilder
        if (fileName.charAt(0) == '.') { // ignore hidden files.
            return false;
        }

        return "aidl".equalsIgnoreCase(extension) == false &&       // Aidl files
            "java".equalsIgnoreCase(extension) == false &&          // Java files
            "class".equalsIgnoreCase(extension) == false &&         // Java class files
            "scc".equalsIgnoreCase(extension) == false &&           // VisualSourceSafe
            "swp".equalsIgnoreCase(extension) == false &&           // vi swap file
            "package.html".equalsIgnoreCase(fileName) == false &&   // Javadoc
            "overview.html".equalsIgnoreCase(fileName) == false &&  // Javadoc
            ".cvsignore".equalsIgnoreCase(fileName) == false &&     // CVS
            ".DS_Store".equals(fileName) == false &&                // Mac resources
            fileName.charAt(fileName.length()-1) != '~';            // Backup files
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/ApkBuilderHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/ApkBuilderHelper.java
//Synthetic comment -- index ba1c878..85439c4 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdklib.internal.build;

import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.build.ApkBuilder;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;

import java.io.File;
//Synthetic comment -- @@ -73,7 +74,6 @@
}
}

private boolean mVerbose = false;
private boolean mSignedPackage = true;
private boolean mDebugMode = false;
//Synthetic comment -- @@ -236,7 +236,7 @@
Collection<ApkFile> javaResources) {
if (file.isDirectory()) {
// a directory? we check it
            if (ApkBuilder.checkFolderForPackaging(file.getName())) {
// if it's valid, we append its name to the current path.
if (path == null) {
path = file.getName();
//Synthetic comment -- @@ -252,7 +252,7 @@
}
} else {
// a file? we check it
            if (ApkBuilder.checkFileForPackaging(file.getName())) {
// we append its name to the current path
if (path == null) {
path = file.getName();
//Synthetic comment -- @@ -398,7 +398,7 @@

// add the java resource from jar files.
for (FileInputStream input : resourcesJars) {
                builder.writeZip(input, null);
}

// add the native files








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/JavaResourceFilter.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/JavaResourceFilter.java
deleted file mode 100644
//Synthetic comment -- index aac1e55..0000000

//Synthetic comment -- @@ -1,103 +0,0 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SignedJarBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SignedJarBuilder.java
//Synthetic comment -- index 79e4be2..81131bc 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.internal.build;

import com.android.sdklib.internal.build.SignedJarBuilder.IZipEntryFilter.ZipAbortException;

import sun.misc.BASE64Encoder;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
//Synthetic comment -- @@ -100,14 +102,42 @@
* be added to a Jar file.
*/
public interface IZipEntryFilter {

        /**
         * An exception thrown during packaging of a zip file into APK file.
         * This is typically thrown by implementations of
         * {@link IZipEntryFilter#checkEntry(String)}.
         */
        public static class ZipAbortException extends Exception {
            private static final long serialVersionUID = 1L;

            public ZipAbortException() {
                super();
            }

            public ZipAbortException(String format, Object... args) {
                super(String.format(format, args));
            }

            public ZipAbortException(Throwable cause, String format, Object... args) {
                super(String.format(format, args), cause);
            }

            public ZipAbortException(Throwable cause) {
                super(cause);
            }
        }


/**
* Checks a file for inclusion in a Jar archive.
         * @param archivePath the archive file path of the entry
* @return <code>true</code> if the file should be included.
         * @throws ZipAbortException if writing the file should be aborted.
*/
        public boolean checkEntry(String archivePath) throws ZipAbortException;
}

/**
* Creates a {@link SignedJarBuilder} with a given output stream, and signing information.
* <p/>If either <code>key</code> or <code>certificate</code> is <code>null</code> then
//Synthetic comment -- @@ -125,18 +155,18 @@
mOutputJar.setLevel(9);
mKey = key;
mCertificate = certificate;

if (mKey != null && mCertificate != null) {
mManifest = new Manifest();
Attributes main = mManifest.getMainAttributes();
main.putValue("Manifest-Version", "1.0");
main.putValue("Created-By", "1.0 (Android)");

mBase64Encoder = new BASE64Encoder();
mMessageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
}
}

/**
* Writes a new {@link File} into the archive.
* @param inputFile the {@link File} to write.
//Synthetic comment -- @@ -147,7 +177,7 @@
// Get an input stream on the file.
FileInputStream fis = new FileInputStream(inputFile);
try {

// create the zip entry
JarEntry entry = new JarEntry(jarPath);
entry.setTime(inputFile.lastModified());
//Synthetic comment -- @@ -166,8 +196,11 @@
* @param input the {@link InputStream} for the Jar/Zip to copy.
* @param filter the filter or <code>null</code>
* @throws IOException
     * @throws ZipAbortException if the {@link IZipEntryFilter} filter indicated that the write
     *                           must be aborted.
*/
    public void writeZip(InputStream input, IZipEntryFilter filter)
            throws IOException, ZipAbortException {
ZipInputStream zis = new ZipInputStream(input);

try {
//Synthetic comment -- @@ -175,19 +208,19 @@
ZipEntry entry;
while ((entry = zis.getNextEntry()) != null) {
String name = entry.getName();

// do not take directories or anything inside a potential META-INF folder.
if (entry.isDirectory() || name.startsWith("META-INF/")) {
continue;
}

// if we have a filter, we check the entry against it
if (filter != null && filter.checkEntry(name) == false) {
continue;
}

JarEntry newEntry;

// Preserve the STORED method of the input entry.
if (entry.getMethod() == JarEntry.STORED) {
newEntry = new JarEntry(entry);
//Synthetic comment -- @@ -195,9 +228,9 @@
// Create a new entry so that the compressed len is recomputed.
newEntry = new JarEntry(name);
}

writeEntry(zis, newEntry);

zis.closeEntry();
}
} finally {
//Synthetic comment -- @@ -206,7 +239,7 @@
}

/**
     * Closes the Jar archive by creating the manifest, and signing the archive.
* @throws IOException
* @throws GeneralSecurityException
*/
//Synthetic comment -- @@ -215,21 +248,21 @@
// write the manifest to the jar file
mOutputJar.putNextEntry(new JarEntry(JarFile.MANIFEST_NAME));
mManifest.write(mOutputJar);

// CERT.SF
Signature signature = Signature.getInstance("SHA1with" + mKey.getAlgorithm());
signature.initSign(mKey);
mOutputJar.putNextEntry(new JarEntry("META-INF/CERT.SF"));
writeSignatureFile(new SignatureOutputStream(mOutputJar, signature));

// CERT.*
mOutputJar.putNextEntry(new JarEntry("META-INF/CERT." + mKey.getAlgorithm()));
writeSignatureBlock(signature, mCertificate, mKey);
}

mOutputJar.close();
}

/**
* Adds an entry to the output jar, and write its content from the {@link InputStream}
* @param input The input stream from where to write the entry content.
//Synthetic comment -- @@ -241,10 +274,10 @@
mOutputJar.putNextEntry(entry);

// read the content of the entry from the input stream, and write it into the archive.
        int count;
while ((count = input.read(mBuffer)) != -1) {
mOutputJar.write(mBuffer, 0, count);

// update the digest
if (mMessageDigest != null) {
mMessageDigest.update(mBuffer, 0, count);
//Synthetic comment -- @@ -253,7 +286,7 @@

// close the entry for this file
mOutputJar.closeEntry();

if (mManifest != null) {
// update the manifest for this entry.
Attributes attr = mManifest.getAttributes(entry.getName());
//Synthetic comment -- @@ -264,7 +297,7 @@
attr.putValue(DIGEST_ATTR, mBase64Encoder.encode(mMessageDigest.digest()));
}
}

/** Writes a .SF file with a digest to the manifest. */
private void writeSignatureFile(OutputStream out)
throws IOException, GeneralSecurityException {







