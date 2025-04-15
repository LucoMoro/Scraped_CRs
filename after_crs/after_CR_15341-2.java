/*New ApkBuilder class.

this is meant to replace the one previously in apkbuilder.jar and the
one in ADT, while being part of the public sdklib API so that other
tools can use it if needed (to deprecate the command line version)

Change-Id:I13f2a09d8d507a85be33af3fe659d175819cb641*/




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
//Synthetic comment -- index 36afc5b..8af5561 100644

//Synthetic comment -- @@ -132,6 +132,11 @@
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
//Synthetic comment -- index 0000000..0571e52

//Synthetic comment -- @@ -0,0 +1,409 @@
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

import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.sdklib.internal.build.JavaResourceFilter;
import com.android.sdklib.internal.build.SignedJarBuilder;
import com.android.sdklib.internal.build.ApkBuilderHelper.ApkCreationException;
import com.android.sdklib.internal.build.ApkBuilderHelper.ApkFile;
import com.android.sdklib.internal.build.DebugKeyProvider.IKeyGenOutput;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;
import com.android.sdklib.internal.build.SignedJarBuilder.IZipEntryFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class ApkBuilder {

    private final static Pattern PATTERN_JAR_EXT = Pattern.compile("^.+\\.jar$",
            Pattern.CASE_INSENSITIVE);
    private final static Pattern PATTERN_NATIVELIB_EXT = Pattern.compile("^.+\\.so$",
            Pattern.CASE_INSENSITIVE);

    /**
     * Custom {@link IZipEntryFilter} to filter out everything that is not a standard java
     * resources, and also record whether the zip file contains native libraries.
     * <p/>Used in {@link SignedJarBuilder#writeZip(java.io.InputStream, IZipEntryFilter)} when
     * we only want the java resources from external jars.
     */
    private final static class JavaAndNativeResourceFilter extends JavaResourceFilter {
        private final List<String> mNativeLibs = new ArrayList<String>();
        private boolean mNativeLibInteference = false;

        @Override
        public boolean checkEntry(String name) {
            boolean value = super.checkEntry(name);

            // only do additional checks if the file passes the default checks.
            if (value) {
                if (name.endsWith(".so")) {
                    mNativeLibs.add(name);

                    // only .so located in lib/ will interfer with the installation
                    if (name.startsWith("lib/")) {
                        mNativeLibInteference = true;
                    }
                } else if (name.endsWith(".jnilib")) {
                    mNativeLibs.add(name);
                }
            }

            return value;
        }

        List<String> getNativeLibs() {
            return mNativeLibs;
        }

        boolean getNativeLibInterefence() {
            return mNativeLibInteference;
        }

        void clear() {
            mNativeLibs.clear();
            mNativeLibInteference = false;
        }
    }

    private final JavaAndNativeResourceFilter mResourceFilter = new JavaAndNativeResourceFilter();
    private final File mApkFile;
    private final File mResFile;
    private final File mDexFile;
    private boolean mDebugMode = false;
    private String mStoreOsPath;
    private PrintStream mVerboseStream;
    private boolean mIsSealed = false;
    private SignedJarBuilder mBuilder;

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
        mStoreOsPath = storeOsPath;
        mVerboseStream = verboseStream;

        // get the debug key
        try {
            File storeFile = null;
            if (storeOsPath != null) {
                storeFile = new File(storeOsPath);
                checkInputFile(storeFile);
            }

            if (storeFile != null) {
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
            verbosePrintln("=> %s", mResFile);
            mBuilder.writeZip(new FileInputStream(mResFile), null /* filter */);

            // add the class dex file at the root of the apk
            verbosePrintln("%1$s => %2$s", mDexFile, mDexFile.getName());
            mBuilder.writeFile(mDexFile, mDexFile.getName());

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
        } catch (AndroidLocationException e) {
            throw new ApkCreationException(e);
        } catch (Exception e) {
            throw new ApkCreationException(e);
        }
    }

	/**
	 * Sets the debug mode. In debug mode, when native libraries are present, the packaging
	 * will also include one or more copies of gdb_server in the final APK file.
	 *
	 * These are used for debugging native code, to ensure that gdb_server is accessible to the
	 * application.
	 *
	 * There will be one version of gdb_server for each ABI supported by the application.
	 *
	 * the gbd_server files are placed in the libs/abi/ folders automatically by the NDK.
	 *
	 * @param debugMode the debug mode flag.
	 */
	public void setDebugMode(boolean debugMode) {
        mDebugMode = debugMode;
	}

	/**
	 * Adds the resources from a jar file.
	 * @param jarFile the jar File.
	 * @throws ApkCreationException if an error occurred
	 * @throws SealedApkException if the APK is already sealed.
	 */
	public void addJar(File jarFile) throws ApkCreationException, SealedApkException {
        if (mIsSealed) {
            throw new SealedApkException("APK is already sealed");
        }
	    // TODO
	}

	/**
	 * Adds the resources from a source folder.
	 * @param sourceFolder the source folder.
     * @throws ApkCreationException if an error occurred
     * @throws SealedApkException if the APK is already sealed.
	 */
	public void addSourceFolder(File sourceFolder) throws ApkCreationException, SealedApkException {
        if (mIsSealed) {
            throw new SealedApkException("APK is already sealed");
        }
	    // TODO
	}

    /**
     * Adds the native libraries from the top native folder.
     * The content of this folder must be the various ABI folders.
     * @param nativeFolder the native folder.
     * @throws ApkCreationException if an error occurred
     * @throws SealedApkException if the APK is already sealed.
     */
	public void addNativeLibraries(File nativeFolder)
	        throws ApkCreationException, SealedApkException {
        if (mIsSealed) {
            throw new SealedApkException("APK is already sealed");
        }
	    // TODO
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

	private void verbosePrintln(String format, Object... args) {
	    if (mVerboseStream != null) {
	        mVerboseStream.println(String.format(format, args));
	    }
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
            throw new ApkCreationException(file.getAbsolutePath() + " is a directory!");
        }

        if (file.exists()) { // will be a file in this case.
            if (file.canWrite() == false) {
                throw new ApkCreationException("Cannot write " + file.getAbsolutePath() );
            }
        } else {
            try {
                if (file.createNewFile() == false) {
                    throw new ApkCreationException("Failed to create " + file.getAbsolutePath() );
                }
            } catch (IOException e) {
                throw new ApkCreationException(
                        "Failed to create '" + file.getAbsolutePath()  + "' : " + e.getMessage());
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
            throw new ApkCreationException(file.getAbsolutePath() + " is a directory!");
        }

        if (file.exists()) {
            if (file.canRead() == false) {
                throw new ApkCreationException("Cannot read " + file.getAbsolutePath());
            }
        } else {
            throw new ApkCreationException(file.getAbsolutePath() + " does not exists!");
        }
    }

}







