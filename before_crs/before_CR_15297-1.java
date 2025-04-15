/*Refactoring of ApkBuilder to prepare for its integration into sdklib.

Change-Id:If75b9262126813769537435546be0ad4e4648ac2*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/ApkBuilderTask.java b/anttasks/src/com/android/ant/ApkBuilderTask.java
//Synthetic comment -- index f73739b..55e1ab3 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ant;

import com.android.apkbuilder.ApkBuilder.ApkCreationException;
import com.android.apkbuilder.internal.ApkBuilderImpl;
import com.android.apkbuilder.internal.ApkBuilderImpl.ApkFile;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
//Synthetic comment -- @@ -208,7 +208,7 @@
public void execute() throws BuildException {
Project antProject = getProject();

        ApkBuilderImpl apkBuilder = new ApkBuilderImpl();
apkBuilder.setVerbose(mVerbose);
apkBuilder.setSignedPackage(mSigned);
apkBuilder.setDebugMode(mDebug);
//Synthetic comment -- @@ -228,7 +228,7 @@
// now go through the list of file to directly add the to the list.
for (Path pathList : mFileList) {
for (String path : pathList.list()) {
                    mArchiveFiles.add(ApkBuilderImpl.getInputFile(path));
}
}

//Synthetic comment -- @@ -236,7 +236,7 @@
if (mHasCode) {
for (Path pathList : mDexList) {
for (String path : pathList.list()) {
                        mArchiveFiles.add(ApkBuilderImpl.getInputFile(path));
}
}
}
//Synthetic comment -- @@ -245,7 +245,7 @@
if (mHasCode) {
for (Path pathList : mSourceList) {
for (String path : pathList.list()) {
                        ApkBuilderImpl.processSourceFolderForResource(new File(path),
mJavaResources);
}
}
//Synthetic comment -- @@ -257,7 +257,7 @@
// it's ok if top level folders are missing
File folder = new File(path);
if (folder.isDirectory()) {
                        ApkBuilderImpl.processJar(folder, mResourcesJars);
}
}
}
//Synthetic comment -- @@ -265,7 +265,7 @@
// now go through the list of jar files.
for (Path pathList : mJarfileList) {
for (String path : pathList.list()) {
                    ApkBuilderImpl.processJar(new File(path), mResourcesJars);
}
}

//Synthetic comment -- @@ -275,7 +275,7 @@
// it's ok if top level folders are missing
File folder = new File(path);
if (folder.isDirectory()) {
                        ApkBuilderImpl.processNativeFolder(folder, mDebug,
mNativeLibraries, mVerbose, mAbiFilter);
}
}
//Synthetic comment -- @@ -324,7 +324,7 @@
* @throws FileNotFoundException
* @throws ApkCreationException
*/
    private void createApk(ApkBuilderImpl apkBuilder, File outputfile)
throws FileNotFoundException, ApkCreationException {

// add the resource pack file as a zip archive input.








//Synthetic comment -- diff --git a/apkbuilder/src/com/android/apkbuilder/ApkBuilder.java b/apkbuilder/src/com/android/apkbuilder/ApkBuilder.java
deleted file mode 100644
//Synthetic comment -- index dd6da8d..0000000

//Synthetic comment -- @@ -1,118 +0,0 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.android.apkbuilder;

import com.android.apkbuilder.internal.ApkBuilderImpl;

import java.io.FileNotFoundException;


/**
 * Command line APK builder with signing support.
 */
public final class ApkBuilder {

    public final static class WrongOptionException extends Exception {
        private static final long serialVersionUID = 1L;

        public WrongOptionException(String message) {
            super(message);
        }
    }

    public final static class ApkCreationException extends Exception {
        private static final long serialVersionUID = 1L;

        public ApkCreationException(String message) {
            super(message);
        }

        public ApkCreationException(Throwable throwable) {
            super(throwable);
        }
    }

    /**
     * Main method. This is meant to be called from the command line through an exec.
     * <p/>WARNING: this will call {@link System#exit(int)} if anything goes wrong.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        try {
            new ApkBuilderImpl().run(args);
        } catch (WrongOptionException e) {
            printUsageAndQuit();
        } catch (FileNotFoundException e) {
            printAndExit(e.getMessage());
        } catch (ApkCreationException e) {
            printAndExit(e.getMessage());
        }
    }

    /**
     * API entry point similar to the {@link #main(String[])} method.
     * <p/>Unlike {@link #main(String[])}, this will not call {@link System#exit(int)} and instead
     * will throw exceptions.
     * @param args command line arguments.
     * @throws WrongOptionException if the command line arguments are incorrect.
     * @throws FileNotFoundException if a required file was not found.
     * @throws ApkCreationException if an error happened during the creation of the APK.
     */
    public static void createApk(String[] args) throws FileNotFoundException, WrongOptionException,
            ApkCreationException {
        new ApkBuilderImpl().run(args);
    }

    private static void printUsageAndQuit() {
        // 80 cols marker:  01234567890123456789012345678901234567890123456789012345678901234567890123456789
        System.err.println("A command line tool to package an Android application from various sources.");
        System.err.println("Usage: apkbuilder <out archive> [-v][-u][-storetype STORE_TYPE] [-z inputzip]");
        System.err.println("            [-f inputfile] [-rf input-folder] [-rj -input-path]");
        System.err.println("");
        System.err.println("    -v      Verbose.");
        System.err.println("    -d      Debug Mode: Includes debug files in the APK file.");
        System.err.println("    -u      Creates an unsigned package.");
        System.err.println("    -storetype Forces the KeyStore type. If ommited the default is used.");
        System.err.println("");
        System.err.println("    -z      Followed by the path to a zip archive.");
        System.err.println("            Adds the content of the application package.");
        System.err.println("");
        System.err.println("    -f      Followed by the path to a file.");
        System.err.println("            Adds the file to the application package.");
        System.err.println("");
        System.err.println("    -rf     Followed by the path to a source folder.");
        System.err.println("            Adds the java resources found in that folder to the application");
        System.err.println("            package, while keeping their path relative to the source folder.");
        System.err.println("");
        System.err.println("    -rj     Followed by the path to a jar file or a folder containing");
        System.err.println("            jar files.");
        System.err.println("            Adds the java resources found in the jar file(s) to the application");
        System.err.println("            package.");
        System.err.println("");
        System.err.println("    -nf     Followed by the root folder containing native libraries to");
        System.err.println("            include in the application package.");

        System.exit(1);
    }

    private static void printAndExit(String... messages) {
        for (String message : messages) {
            System.err.println(message);
        }
        System.exit(1);
    }
}








//Synthetic comment -- diff --git a/apkbuilder/src/com/android/apkbuilder/ApkBuilderMain.java b/apkbuilder/src/com/android/apkbuilder/ApkBuilderMain.java
new file mode 100644
//Synthetic comment -- index 0000000..08124ad

//Synthetic comment -- @@ -0,0 +1,178 @@








//Synthetic comment -- diff --git a/apkbuilder/src/com/android/apkbuilder/internal/ApkBuilderImpl.java b/apkbuilder/src/com/android/apkbuilder/internal/ApkBuilderHelper.java
similarity index 79%
rename from apkbuilder/src/com/android/apkbuilder/internal/ApkBuilderImpl.java
rename to apkbuilder/src/com/android/apkbuilder/internal/ApkBuilderHelper.java
//Synthetic comment -- index 781bc5a..1da7568 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.apkbuilder.internal;

import com.android.apkbuilder.ApkBuilder.WrongOptionException;
import com.android.apkbuilder.ApkBuilder.ApkCreationException;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.sdklib.internal.build.JavaResourceFilter;
//Synthetic comment -- @@ -41,7 +39,7 @@
/**
* Command line APK builder with signing support.
*/
public final class ApkBuilderImpl {

private final static Pattern PATTERN_JAR_EXT = Pattern.compile("^.+\\.jar$",
Pattern.CASE_INSENSITIVE);
//Synthetic comment -- @@ -51,6 +49,19 @@
private final static String NATIVE_LIB_ROOT = "lib/";
private final static String GDBSERVER_NAME = "gdbserver";

/**
* A File to be added to the APK archive.
* <p/>This includes the {@link File} representing the file and its path in the archive.
//Synthetic comment -- @@ -76,6 +87,10 @@
mVerbose = verbose;
}

public void setSignedPackage(boolean signedPackage) {
mSignedPackage = signedPackage;
}
//Synthetic comment -- @@ -84,89 +99,27 @@
mDebugMode = debugMode;
}

    public void run(String[] args) throws WrongOptionException, FileNotFoundException,
            ApkCreationException {
        if (args.length < 1) {
            throw new WrongOptionException("No options specified");
        }

        // read the first args that should be a file path
        File outFile = getOutFile(args[0]);

        ArrayList<FileInputStream> zipArchives = new ArrayList<FileInputStream>();
        ArrayList<File> archiveFiles = new ArrayList<File>();
        ArrayList<ApkFile> javaResources = new ArrayList<ApkFile>();
        ArrayList<FileInputStream> resourcesJars = new ArrayList<FileInputStream>();
        ArrayList<ApkFile> nativeLibraries = new ArrayList<ApkFile>();

        int index = 1;
        do {
            String argument = args[index++];

            if ("-v".equals(argument)) {
                mVerbose = true;
            } else if ("-d".equals(argument)) {
                mDebugMode = true;
            } else if ("-u".equals(argument)) {
                mSignedPackage = false;
            } else if ("-z".equals(argument)) {
                // quick check on the next argument.
                if (index == args.length)  {
                    throw new WrongOptionException("Missing value for -z");
                }

                try {
                    FileInputStream input = new FileInputStream(args[index++]);
                    zipArchives.add(input);
                } catch (FileNotFoundException e) {
                    throw new ApkCreationException("-z file is not found");
                }
            } else if ("-f". equals(argument)) {
                // quick check on the next argument.
                if (index == args.length) {
                    throw new WrongOptionException("Missing value for -f");
                }

                archiveFiles.add(getInputFile(args[index++]));
            } else if ("-rf". equals(argument)) {
                // quick check on the next argument.
                if (index == args.length) {
                    throw new WrongOptionException("Missing value for -rf");
                }

                processSourceFolderForResource(new File(args[index++]), javaResources);
            } else if ("-rj". equals(argument)) {
                // quick check on the next argument.
                if (index == args.length) {
                    throw new WrongOptionException("Missing value for -rj");
                }

                processJar(new File(args[index++]), resourcesJars);
            } else if ("-nf".equals(argument)) {
                // quick check on the next argument.
                if (index == args.length) {
                    throw new WrongOptionException("Missing value for -nf");
                }

                processNativeFolder(new File(args[index++]), mDebugMode, nativeLibraries,
                        mVerbose, null /*abiFilter*/);
            } else if ("-storetype".equals(argument)) {
                // quick check on the next argument.
                if (index == args.length) {
                    throw new WrongOptionException("Missing value for -storetype");
                }

                mStoreType  = args[index++];
            } else {
                throw new WrongOptionException("Unknown argument: " + argument);
            }
        } while (index < args.length);

        createPackage(outFile, zipArchives, archiveFiles, javaResources, resourcesJars,
                nativeLibraries);
}

    private File getOutFile(String filepath) throws ApkCreationException {
File f = new File(filepath);

if (f.isDirectory()) {
//Synthetic comment -- @@ -398,7 +351,7 @@

DebugKeyProvider keyProvider = new DebugKeyProvider(
null /* osKeyPath: use default */,
                        mStoreType, null /* IKeyGenOutput */);
PrivateKey key = keyProvider.getDebugKey();
X509Certificate certificate = (X509Certificate)keyProvider.getCertificate();

//Synthetic comment -- @@ -431,7 +384,7 @@
for (File input : files) {
// always put the file at the root of the archive in this case
builder.writeFile(input, input.getName());
                if (mVerbose) {
System.err.println(String.format("%1$s => %2$s", input.getAbsolutePath(),
input.getName()));
}
//Synthetic comment -- @@ -440,7 +393,7 @@
// add the java resource from the source folders.
for (ApkFile resource : javaResources) {
builder.writeFile(resource.file, resource.archivePath);
                if (mVerbose) {
System.err.println(String.format("%1$s => %2$s",
resource.file.getAbsolutePath(), resource.archivePath));
}
//Synthetic comment -- @@ -454,7 +407,7 @@
// add the native files
for (ApkFile file : nativeLibraries) {
builder.writeFile(file.file, file.archivePath);
                if (mVerbose) {
System.err.println(String.format("%1$s => %2$s", file.file.getAbsolutePath(),
file.archivePath));
}







