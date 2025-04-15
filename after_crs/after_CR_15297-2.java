/*Refactoring of ApkBuilder to prepare for its integration into sdklib.

Change-Id:If75b9262126813769537435546be0ad4e4648ac2*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/ApkBuilderTask.java b/anttasks/src/com/android/ant/ApkBuilderTask.java
//Synthetic comment -- index f73739b..55e1ab3 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ant;

import com.android.apkbuilder.internal.ApkBuilderHelper;
import com.android.apkbuilder.internal.ApkBuilderHelper.ApkCreationException;
import com.android.apkbuilder.internal.ApkBuilderHelper.ApkFile;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
//Synthetic comment -- @@ -208,7 +208,7 @@
public void execute() throws BuildException {
Project antProject = getProject();

        ApkBuilderHelper apkBuilder = new ApkBuilderHelper();
apkBuilder.setVerbose(mVerbose);
apkBuilder.setSignedPackage(mSigned);
apkBuilder.setDebugMode(mDebug);
//Synthetic comment -- @@ -228,7 +228,7 @@
// now go through the list of file to directly add the to the list.
for (Path pathList : mFileList) {
for (String path : pathList.list()) {
                    mArchiveFiles.add(ApkBuilderHelper.getInputFile(path));
}
}

//Synthetic comment -- @@ -236,7 +236,7 @@
if (mHasCode) {
for (Path pathList : mDexList) {
for (String path : pathList.list()) {
                        mArchiveFiles.add(ApkBuilderHelper.getInputFile(path));
}
}
}
//Synthetic comment -- @@ -245,7 +245,7 @@
if (mHasCode) {
for (Path pathList : mSourceList) {
for (String path : pathList.list()) {
                        ApkBuilderHelper.processSourceFolderForResource(new File(path),
mJavaResources);
}
}
//Synthetic comment -- @@ -257,7 +257,7 @@
// it's ok if top level folders are missing
File folder = new File(path);
if (folder.isDirectory()) {
                        ApkBuilderHelper.processJar(folder, mResourcesJars);
}
}
}
//Synthetic comment -- @@ -265,7 +265,7 @@
// now go through the list of jar files.
for (Path pathList : mJarfileList) {
for (String path : pathList.list()) {
                    ApkBuilderHelper.processJar(new File(path), mResourcesJars);
}
}

//Synthetic comment -- @@ -275,7 +275,7 @@
// it's ok if top level folders are missing
File folder = new File(path);
if (folder.isDirectory()) {
                        ApkBuilderHelper.processNativeFolder(folder, mDebug,
mNativeLibraries, mVerbose, mAbiFilter);
}
}
//Synthetic comment -- @@ -324,7 +324,7 @@
* @throws FileNotFoundException
* @throws ApkCreationException
*/
    private void createApk(ApkBuilderHelper apkBuilder, File outputfile)
throws FileNotFoundException, ApkCreationException {

// add the resource pack file as a zip archive input.








//Synthetic comment -- diff --git a/apkbuilder/src/com/android/apkbuilder/ApkBuilder.java b/apkbuilder/src/com/android/apkbuilder/ApkBuilder.java
deleted file mode 100644
//Synthetic comment -- index dd6da8d..0000000

//Synthetic comment -- @@ -1,118 +0,0 @@








//Synthetic comment -- diff --git a/apkbuilder/src/com/android/apkbuilder/ApkBuilderMain.java b/apkbuilder/src/com/android/apkbuilder/ApkBuilderMain.java
new file mode 100644
//Synthetic comment -- index 0000000..08124ad

//Synthetic comment -- @@ -0,0 +1,178 @@
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

import com.android.apkbuilder.internal.ApkBuilderHelper;
import com.android.apkbuilder.internal.ApkBuilderHelper.ApkCreationException;
import com.android.apkbuilder.internal.ApkBuilderHelper.ApkFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * Command line APK builder with signing support.
 */
public final class ApkBuilderMain {

    public final static class WrongOptionException extends Exception {
        private static final long serialVersionUID = 1L;

        public WrongOptionException(String message) {
            super(message);
        }
    }

    /**
     * Main method. This is meant to be called from the command line through an exec.
     * <p/>WARNING: this will call {@link System#exit(int)} if anything goes wrong.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            printUsageAndQuit();
        }

        try {
            ApkBuilderHelper helper = new ApkBuilderHelper();


            // read the first args that should be a file path
            File outFile = helper.getOutFile(args[0]);

            ArrayList<FileInputStream> zipArchives = new ArrayList<FileInputStream>();
            ArrayList<File> archiveFiles = new ArrayList<File>();
            ArrayList<ApkFile> javaResources = new ArrayList<ApkFile>();
            ArrayList<FileInputStream> resourcesJars = new ArrayList<FileInputStream>();
            ArrayList<ApkFile> nativeLibraries = new ArrayList<ApkFile>();

            int index = 1;
            do {
                String argument = args[index++];

                if ("-v".equals(argument)) {
                    helper.setVerbose(true);
                } else if ("-d".equals(argument)) {
                    helper.setDebugMode(true);
                } else if ("-u".equals(argument)) {
                    helper.setSignedPackage(false);
                } else if ("-z".equals(argument)) {
                    // quick check on the next argument.
                    if (index == args.length)  {
                        printAndExit("Missing value for -z");
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
                        printAndExit("Missing value for -f");
                    }

                    archiveFiles.add(ApkBuilderHelper.getInputFile(args[index++]));
                } else if ("-rf". equals(argument)) {
                    // quick check on the next argument.
                    if (index == args.length) {
                        printAndExit("Missing value for -rf");
                    }

                    ApkBuilderHelper.processSourceFolderForResource(
                            new File(args[index++]), javaResources);
                } else if ("-rj". equals(argument)) {
                    // quick check on the next argument.
                    if (index == args.length) {
                        printAndExit("Missing value for -rj");
                    }

                    ApkBuilderHelper.processJar(new File(args[index++]), resourcesJars);
                } else if ("-nf".equals(argument)) {
                    // quick check on the next argument.
                    if (index == args.length) {
                        printAndExit("Missing value for -nf");
                    }

                    ApkBuilderHelper.processNativeFolder(new File(args[index++]),
                            helper.getDebugMode(), nativeLibraries,
                            helper.isVerbose(), null /*abiFilter*/);
                } else if ("-storetype".equals(argument)) {
                    // quick check on the next argument.
                    if (index == args.length) {
                        printAndExit("Missing value for -storetype");
                    }

                    helper.setStoreType(args[index++]);
                } else {
                    printAndExit("Unknown argument: " + argument);
                }
            } while (index < args.length);

            helper.createPackage(outFile, zipArchives, archiveFiles, javaResources, resourcesJars,
                    nativeLibraries);

        } catch (FileNotFoundException e) {
            printAndExit(e.getMessage());
        } catch (ApkCreationException e) {
            printAndExit(e.getMessage());
        }
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








//Synthetic comment -- diff --git a/apkbuilder/src/com/android/apkbuilder/internal/ApkBuilderImpl.java b/apkbuilder/src/com/android/apkbuilder/internal/ApkBuilderHelper.java
similarity index 80%
rename from apkbuilder/src/com/android/apkbuilder/internal/ApkBuilderImpl.java
rename to apkbuilder/src/com/android/apkbuilder/internal/ApkBuilderHelper.java
//Synthetic comment -- index 781bc5a..3046942 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.apkbuilder.internal;

import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.sdklib.internal.build.JavaResourceFilter;
//Synthetic comment -- @@ -41,7 +39,7 @@
/**
* Command line APK builder with signing support.
*/
public final class ApkBuilderHelper {

private final static Pattern PATTERN_JAR_EXT = Pattern.compile("^.+\\.jar$",
Pattern.CASE_INSENSITIVE);
//Synthetic comment -- @@ -51,6 +49,19 @@
private final static String NATIVE_LIB_ROOT = "lib/";
private final static String GDBSERVER_NAME = "gdbserver";

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
* A File to be added to the APK archive.
* <p/>This includes the {@link File} representing the file and its path in the archive.
//Synthetic comment -- @@ -76,6 +87,10 @@
mVerbose = verbose;
}

    public boolean isVerbose() {
        return mVerbose;
    }

public void setSignedPackage(boolean signedPackage) {
mSignedPackage = signedPackage;
}
//Synthetic comment -- @@ -84,89 +99,27 @@
mDebugMode = debugMode;
}

    public boolean getDebugMode() {
        return mDebugMode;
}

    /**
     * Set the type of the keystore. <code>null</code> means the default.
     * @param storeType
     */
    public void setStoreType(String storeType) {
        mStoreType = storeType;
    }

    /**
     * Returns the store type. <code>null</code> means default.
     * @return
     */
    public String getStoreType() {
        return mStoreType;
    }

    public File getOutFile(String filepath) throws ApkCreationException {
File f = new File(filepath);

if (f.isDirectory()) {







