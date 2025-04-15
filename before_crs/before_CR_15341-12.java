/*New ApkBuilder class.

this is meant to replace the one previously in apkbuilder.jar and the
one in ADT, while being part of the public sdklib API so that other
tools can use it if needed (to deprecate the command line version)

Another changelist will rename the ApkBuilder classes inside
ADT to make things less confusing.

Change-Id:I13f2a09d8d507a85be33af3fe659d175819cb641*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/ApkBuilderTask.java b/anttasks/src/com/android/ant/ApkBuilderTask.java
//Synthetic comment -- index bec0417..f5a7f0b 100644

//Synthetic comment -- @@ -16,9 +16,10 @@

package com.android.ant;

import com.android.sdklib.internal.build.ApkBuilderHelper;
import com.android.sdklib.internal.build.ApkBuilderHelper.ApkCreationException;
import com.android.sdklib.internal.build.ApkBuilderHelper.ApkFile;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
//Synthetic comment -- @@ -26,12 +27,15 @@
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ApkBuilderTask extends Task {

private String mOutFolder;
@Deprecated private String mBaseName;
private String mApkFilepath;
//Synthetic comment -- @@ -42,20 +46,15 @@
private boolean mHasCode = true;
private String mAbiFilter = null;

private final ArrayList<Path> mZipList = new ArrayList<Path>();
    private final ArrayList<Path> mDexList = new ArrayList<Path>();
private final ArrayList<Path> mFileList = new ArrayList<Path>();
private final ArrayList<Path> mSourceList = new ArrayList<Path>();
private final ArrayList<Path> mJarfolderList = new ArrayList<Path>();
private final ArrayList<Path> mJarfileList = new ArrayList<Path>();
private final ArrayList<Path> mNativeList = new ArrayList<Path>();

    private final ArrayList<FileInputStream> mZipArchives = new ArrayList<FileInputStream>();
    private final ArrayList<File> mArchiveFiles = new ArrayList<File>();
    private final ArrayList<ApkFile> mJavaResources = new ArrayList<ApkFile>();
    private final ArrayList<FileInputStream> mResourcesJars = new ArrayList<FileInputStream>();
    private final ArrayList<ApkFile> mNativeLibraries = new ArrayList<ApkFile>();

/**
* Sets the value of the "outfolder" attribute.
* @param outFolder the value.
//Synthetic comment -- @@ -154,15 +153,19 @@
* is <code>false</code> in which case it's ignored.
*/
public Object createDex() {
        Path path = new Path(getProject());
        mDexList.add(path);
        return path;
}

/**
* Returns an object representing a nested <var>file</var> element.
*/
public Object createFile() {
Path path = new Path(getProject());
mFileList.add(path);
return path;
//Synthetic comment -- @@ -208,36 +211,69 @@
public void execute() throws BuildException {
Project antProject = getProject();

        ApkBuilderHelper apkBuilder = new ApkBuilderHelper();
        apkBuilder.setVerbose(mVerbose);
        apkBuilder.setSignedPackage(mSigned);
        apkBuilder.setDebugMode(mDebug);

try {
            // setup the list of everything that needs to go in the archive.

            // go through the list of zip files to add. This will not include
            // the resource package, which is handled separaly for each apk to create.
for (Path pathList : mZipList) {
for (String path : pathList.list()) {
                    FileInputStream input = new FileInputStream(path);
                    mZipArchives.add(input);
}
}

            // now go through the list of file to directly add the to the list.
for (Path pathList : mFileList) {
for (String path : pathList.list()) {
                    mArchiveFiles.add(ApkBuilderHelper.getInputFile(path));
                }
            }

            // only attempt to add Dex files if hasCode is true.
            if (mHasCode) {
                for (Path pathList : mDexList) {
                    for (String path : pathList.list()) {
                        mArchiveFiles.add(ApkBuilderHelper.getInputFile(path));
                    }
}
}

//Synthetic comment -- @@ -245,8 +281,7 @@
if (mHasCode) {
for (Path pathList : mSourceList) {
for (String path : pathList.list()) {
                        ApkBuilderHelper.processSourceFolderForResource(new File(path),
                                mJavaResources);
}
}
}
//Synthetic comment -- @@ -257,7 +292,15 @@
// it's ok if top level folders are missing
File folder = new File(path);
if (folder.isDirectory()) {
                        ApkBuilderHelper.processJar(folder, mResourcesJars);
}
}
}
//Synthetic comment -- @@ -265,7 +308,7 @@
// now go through the list of jar files.
for (Path pathList : mJarfileList) {
for (String path : pathList.list()) {
                    ApkBuilderHelper.processJar(new File(path), mResourcesJars);
}
}

//Synthetic comment -- @@ -275,77 +318,26 @@
// it's ok if top level folders are missing
File folder = new File(path);
if (folder.isDirectory()) {
                        ApkBuilderHelper.processNativeFolder(folder, mDebug,
                                mNativeLibraries, mVerbose, mAbiFilter);
}
}
}

            // get the rules revision
            String rulesRevStr = antProject.getProperty(TaskHelper.PROP_RULES_REV);
            int rulesRev = 1;
            try {
                rulesRev = Integer.parseInt(rulesRevStr);
            } catch (NumberFormatException e) {
                // this shouldn't happen since setup task is the one setting up every time.
            }


            File file;
            if (mApkFilepath != null) {
                file = new File(mApkFilepath);
            } else if (rulesRev == 2) {
                if (mSigned) {
                    file = new File(mOutFolder, mBaseName + "-debug-unaligned.apk");
                } else {
                    file = new File(mOutFolder, mBaseName + "-unsigned.apk");
                }
            } else {
                throw new BuildException("missing attribute 'apkFilepath'");
            }

            // create the package.
            createApk(apkBuilder, file);

        } catch (FileNotFoundException e) {
throw new BuildException(e);
} catch (IllegalArgumentException e) {
throw new BuildException(e);
        } catch (ApkCreationException e) {
            e.printStackTrace();
            throw new BuildException(e);
}
}

    /**
     * Creates an application package.
     * @param apkBuilder
     * @param outputfile the file to generate
     * @throws FileNotFoundException
     * @throws ApkCreationException
     */
    private void createApk(ApkBuilderHelper apkBuilder, File outputfile)
            throws FileNotFoundException, ApkCreationException {

        // add the resource pack file as a zip archive input.
        FileInputStream resoucePackageZipFile = new FileInputStream(
                new File(mOutFolder, mResourceFile));
        mZipArchives.add(resoucePackageZipFile);

        if (mSigned) {
            System.out.println(String.format(
                    "Creating %s and signing it with a debug key...", outputfile.getName()));
        } else {
            System.out.println(String.format(
                    "Creating %s for release...", outputfile.getName()));
        }

        // and generate the apk
        apkBuilder.createPackage(outputfile.getAbsoluteFile(), mZipArchives,
                mArchiveFiles, mJavaResources, mResourcesJars, mNativeLibraries);

        // we are done. We need to remove the resource package from the list of zip archives
        // in case we have another apk to generate.
        mZipArchives.remove(resoucePackageZipFile);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java
//Synthetic comment -- index d5d2185..73f6fcb 100644

//Synthetic comment -- @@ -101,8 +101,6 @@
public final static String FN_COMPILED_RESOURCE_CLASS = FN_RESOURCE_BASE + DOT_CLASS;
/** Manifest java class filename, i.e. "Manifest.java" */
public final static String FN_MANIFEST_CLASS = "Manifest.java"; //$NON-NLS-1$
    /** Dex conversion output filname, i.e. "classes.dex" */
    public final static String FN_CLASSES_DEX = "classes.dex"; //$NON-NLS-1$
/** Temporary packaged resources file name, i.e. "resources.ap_" */
public final static String FN_RESOURCES_AP_ = "resources.ap_"; //$NON-NLS-1$
/** Temporary packaged resources file name for a specific set of configuration */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/MultiApkExportAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/MultiApkExportAction.java
//Synthetic comment -- index fe05b1d..aa4b700 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.ide.eclipse.adt.internal.actions;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.build.ApkBuilderHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectState;
//Synthetic comment -- @@ -282,17 +281,23 @@
apk.setOutputName(softVariant != null ? softVariant.getKey() : null, outputName);

// do the final export.
        IFile dexFile = projectBinFolder.getFile(AndroidConstants.FN_CLASSES_DEX);
String outputFile = binFolder.getFile(outputName).getLocation().toOSString();

// get the list of referenced projects.
IProject[] javaRefs = ProjectHelper.getReferencedProjects(project);
IJavaProject[] referencedJavaProjects = ApkBuilderHelper.getJavaProjects(javaRefs);

        helper.finalPackage(new File(projectBinFolderPath, pkgName).getAbsolutePath(),
dexFile.getLocation().toOSString(),
                outputFile, javaProject, libProjects, referencedJavaProjects,
                apk.getAbi(), false /*debuggable*/);

}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilder.java
//Synthetic comment -- index 9cd3036..8360feb 100644

//Synthetic comment -- @@ -350,7 +350,7 @@

// check classes.dex is present. If not we force to recreate it.
if (mConvertToDex == false) {
                tmp = outputFolder.findMember(AndroidConstants.FN_CLASSES_DEX);
if (tmp == null || tmp.exists() == false) {
mConvertToDex = true;
mBuildFinalPackage = true;
//Synthetic comment -- @@ -445,7 +445,7 @@
// then we check if we need to package the .class into classes.dex
if (mConvertToDex) {
if (helper.executeDx(javaProject, osBinPath, osBinPath + File.separator +
                            AndroidConstants.FN_CLASSES_DEX, referencedJavaProjects) == false) {
// dx failed, we return
return allRefProjects;
}
//Synthetic comment -- @@ -477,10 +477,11 @@
// This is the default package with all the resources.

String classesDexPath = osBinPath + File.separator +
                        AndroidConstants.FN_CLASSES_DEX;
if (helper.finalPackage(
osBinPath + File.separator + AndroidConstants.FN_RESOURCES_AP_,
                        classesDexPath, osFinalPackagePath, javaProject, libProjects,
referencedJavaProjects, null /*abiFilter*/, debuggable) == false) {
return allRefProjects;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilderHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilderHelper.java
//Synthetic comment -- index 04acf44..3c2d86a 100644

//Synthetic comment -- @@ -27,12 +27,14 @@
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.sdklib.internal.build.JavaResourceFilter;
import com.android.sdklib.internal.build.SignedJarBuilder;
import com.android.sdklib.internal.build.DebugKeyProvider.IKeyGenOutput;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;
import com.android.sdklib.internal.build.SignedJarBuilder.IZipEntryFilter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -45,7 +47,6 @@
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
//Synthetic comment -- @@ -54,73 +55,30 @@
import org.eclipse.jface.preference.IPreferenceStore;

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

public class ApkBuilderHelper {

    final static String GDBSERVER_NAME = "gdbserver"; //$NON-NLS-1$

private final IProject mProject;
private final PrintStream mOutStream;
private final PrintStream mErrStream;

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

public ApkBuilderHelper(IProject project, PrintStream outStream, PrintStream errStream) {
mProject = project;
mOutStream = outStream;
//Synthetic comment -- @@ -190,7 +148,6 @@
}

return true;

}

/**
//Synthetic comment -- @@ -199,6 +156,7 @@
* @param intermediateApk The path to the temporary resource file.
* @param dex The path to the dex file.
* @param output The path to the final package file to create.
* @param javaProject the java project being compiled
* @param libProjects an optional list of library projects (can be null)
* @param referencedJavaProjects referenced projects.
//Synthetic comment -- @@ -209,128 +167,96 @@
* @return true if success, false otherwise.
*/
public boolean finalPackage(String intermediateApk, String dex, String output,
            final IJavaProject javaProject, IProject[] libProjects,
IJavaProject[] referencedJavaProjects, String abiFilter, boolean debuggable) {

        FileOutputStream fos = null;
        try {
IPreferenceStore store = AdtPlugin.getDefault().getPreferenceStore();
            String osKeyPath = store.getString(AdtPrefs.PREFS_CUSTOM_DEBUG_KEYSTORE);
            if (osKeyPath == null || new File(osKeyPath).exists() == false) {
                osKeyPath = DebugKeyProvider.getDefaultKeyStoreOsPath();
                AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, mProject,
                        Messages.ApkBuilder_Using_Default_Key);
} else {
AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, mProject,
                        String.format(Messages.ApkBuilder_Using_s_To_Sign, osKeyPath));
}

            // TODO: get the store type from somewhere else.
            DebugKeyProvider provider = new DebugKeyProvider(osKeyPath, null /* storeType */,
                    new IKeyGenOutput() {
                        public void err(String message) {
                            AdtPlugin.printErrorToConsole(mProject,
                                    Messages.ApkBuilder_Signing_Key_Creation_s + message);
                        }

                        public void out(String message) {
                            AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE,
                                    mProject,
                                    Messages.ApkBuilder_Signing_Key_Creation_s + message);
                        }
            });
            PrivateKey key = provider.getDebugKey();
            X509Certificate certificate = (X509Certificate)provider.getCertificate();

            if (key == null) {
                String msg = String.format(Messages.Final_Archive_Error_s,
                        Messages.ApkBuilder_Unable_To_Gey_Key);
                AdtPlugin.printErrorToConsole(mProject, msg);
                BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING, msg,
                        IMarker.SEVERITY_ERROR);
                return false;
            }

            // compare the certificate expiration date
            if (certificate != null && certificate.getNotAfter().compareTo(new Date()) < 0) {
                // TODO, regenerate a new one.
                String msg = String.format(Messages.Final_Archive_Error_s,
                    String.format(Messages.ApkBuilder_Certificate_Expired_on_s,
                            DateFormat.getInstance().format(certificate.getNotAfter())));
                AdtPlugin.printErrorToConsole(mProject, msg);
                BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING, msg,
                        IMarker.SEVERITY_ERROR);
                return false;
            }

            // create the jar builder.
            fos = new FileOutputStream(output);
            SignedJarBuilder builder = new SignedJarBuilder(fos, key, certificate);

            // add the intermediate file containing the compiled resources.
            AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, mProject,
                    String.format(Messages.ApkBuilder_Packaging_s, intermediateApk));
            FileInputStream fis = new FileInputStream(intermediateApk);
            try {
                builder.writeZip(fis, null /* filter */);
            } finally {
                fis.close();
            }

            // Now we add the new file to the zip archive for the classes.dex file.
            AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, mProject,
                    String.format(Messages.ApkBuilder_Packaging_s,
                            AndroidConstants.FN_CLASSES_DEX));
            File entryFile = new File(dex);
            builder.writeFile(entryFile, AndroidConstants.FN_CLASSES_DEX);

// Now we write the standard resources from the project and the referenced projects.
            writeStandardResources(builder, javaProject, referencedJavaProjects);

            // Now we write the standard resources from the external libraries
for (String libraryOsPath : getExternalJars()) {
                AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, mProject,
                        String.format(Messages.ApkBuilder_Packaging_s, libraryOsPath));
                try {
                    fis = new FileInputStream(libraryOsPath);
                    mResourceFilter.clear();
                    builder.writeZip(fis, mResourceFilter);

                    // check if we found native libraries in the external library. This
                    // constitutes an error or warning depending on if they are in lib/
                    List<String> nativeLibs = mResourceFilter.getNativeLibs();
                    boolean nativeInterference = mResourceFilter.getNativeLibInterefence();
                    if (nativeLibs.size() > 0) {
                        String libName = new File(libraryOsPath).getName();
                        String msg = String.format("Native libraries detected in '%1$s'. See console for more information.",
                                libName);


                        BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING,
                                msg,
                                nativeInterference ||
                                        AdtPrefs.getPrefs().getBuildForceErrorOnNativeLibInJar() ?
                                        IMarker.SEVERITY_ERROR : IMarker.SEVERITY_WARNING);

                        ArrayList<String> consoleMsgs = new ArrayList<String>();
                        consoleMsgs.add(String.format(
                                "The library '%1$s' contains native libraries that will not run on the device.",
                                libName));
                        if (nativeInterference) {
                            consoleMsgs.add("Additionally some of those libraries will interfer with the installation of the application because of their location in lib/");
                            consoleMsgs.add("lib/ is reserved for NDK libraries.");
                        }
                        consoleMsgs.add("The following libraries were found:");
                        for (String lib : nativeLibs) {
                            consoleMsgs.add(" - " + lib);
                        }
                        AdtPlugin.printErrorToConsole(mProject,
                                consoleMsgs.toArray());

                        return false;
}
                } finally {
                    fis.close();
}
}

//Synthetic comment -- @@ -339,8 +265,8 @@
IResource libFolder = mProject.findMember(SdkConstants.FD_NATIVE_LIBS);
if (libFolder != null && libFolder.exists() &&
libFolder.getType() == IResource.FOLDER) {
                // look inside and put .so in lib/* by keeping the relative folder path.
                writeNativeLibraries((IFolder) libFolder, builder, abiFilter, debuggable);
}

// write the native libraries for the library projects.
//Synthetic comment -- @@ -349,59 +275,36 @@
libFolder = lib.findMember(SdkConstants.FD_NATIVE_LIBS);
if (libFolder != null && libFolder.exists() &&
libFolder.getType() == IResource.FOLDER) {
                        // look inside and put .so in lib/* by keeping the relative folder path.
                        writeNativeLibraries((IFolder) libFolder, builder, abiFilter, debuggable);
}
}
}

            // close the jar file and write the manifest and sign it.
            builder.close();
        } catch (GeneralSecurityException e1) {
            // mark project and return
            String msg = String.format(Messages.Final_Archive_Error_s, e1.getMessage());
            AdtPlugin.printErrorToConsole(mProject, msg);
            BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING, msg,
                    IMarker.SEVERITY_ERROR);
            return false;
        } catch (IOException e1) {
            // mark project and return
            String msg = String.format(Messages.Final_Archive_Error_s, e1.getMessage());
            AdtPlugin.printErrorToConsole(mProject, msg);
            BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING, msg,
                    IMarker.SEVERITY_ERROR);
            return false;
        } catch (KeytoolException e) {
            String eMessage = e.getMessage();

            // mark the project with the standard message
            String msg = String.format(Messages.Final_Archive_Error_s, eMessage);
            BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING, msg,
                    IMarker.SEVERITY_ERROR);

            // output more info in the console
            AdtPlugin.printErrorToConsole(mProject,
                    msg,
                    String.format(Messages.ApkBuilder_JAVA_HOME_is_s, e.getJavaHome()),
                    Messages.ApkBuilder_Update_or_Execute_manually_s,
                    e.getCommandLine());
        } catch (AndroidLocationException e) {
            String eMessage = e.getMessage();

            // mark the project with the standard message
            String msg = String.format(Messages.Final_Archive_Error_s, eMessage);
            BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING, msg,
                    IMarker.SEVERITY_ERROR);

            // and also output it in the console
            AdtPlugin.printErrorToConsole(mProject, msg);
} catch (CoreException e) {
// mark project and return
String msg = String.format(Messages.Final_Archive_Error_s, e.getMessage());
AdtPlugin.printErrorToConsole(mProject, msg);
BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING, msg,
IMarker.SEVERITY_ERROR);
            return false;
} catch (Exception e) {
// try to catch other exception to actually display an error. This will be useful
// if we get an NPE or something so that we can at least notify the user that something
//Synthetic comment -- @@ -416,18 +319,9 @@
AdtPlugin.printErrorToConsole(mProject, msg);
BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING, msg,
IMarker.SEVERITY_ERROR);
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // pass.
                }
            }
}

        return true;
}

/**
//Synthetic comment -- @@ -639,80 +533,29 @@


/**
     * Writes native libraries into a {@link SignedJarBuilder}.
     * <p/>The native libraries must be located in a given main folder. Under this folder, it is
     * expected that the libraries are under a sub-folder that represents the ABI of the library.
     *
     * The path in the archive is based on the ABI folder name, and located under a main
     * folder called "lib".
     *
     * This method also packages any "gdbserver" executable it finds in the ABI folders, if
     * <var>debuggable</var> is set to true.
     *
     * @param rootFolder The folder containing the native libraries.
     * @param jarBuilder the {@link SignedJarBuilder} used to create the archive.
     * @param abiFilter an optional filter. If not null, then only the matching ABI is included in
     * the final archive
     * @param debuggable whether the application is debuggable. If <code>true</code> then gdbserver
     * executables will be packaged as well.
     * @throws CoreException
     * @throws IOException
     */
    private void writeNativeLibraries(IFolder rootFolder, SignedJarBuilder jarBuilder,
            String abiFilter, boolean debuggable) throws CoreException, IOException {
        // the native files must be under a single sub-folder under the main root folder.
        // the sub-folder represents the abi for the native libs
        IResource[] abis = rootFolder.members();
        for (IResource abi : abis) {
            if (abi.getType() == IResource.FOLDER) { // ignore non folders.

                // check the abi filter and reject all other ABIs
                if (abiFilter != null && abiFilter.equals(abi.getName()) == false) {
                    continue;
                }

                IResource[] libs = ((IFolder)abi).members();

                for (IResource lib : libs) {
                    if (lib.getType() == IResource.FILE) { // ignore non files.
                        IPath path = lib.getFullPath();

                        // check the extension.
                        String ext = path.getFileExtension();
                        if (AndroidConstants.EXT_NATIVE_LIB.equalsIgnoreCase(ext) ||
                                (debuggable && GDBSERVER_NAME.equals(lib.getName()))) {
                            // compute the path inside the archive.
                            IPath apkPath = new Path(SdkConstants.FD_APK_NATIVE_LIBS);
                            apkPath = apkPath.append(abi.getName()).append(lib.getName());

                            // writes the file in the apk.
                            jarBuilder.writeFile(lib.getLocation().toFile(), apkPath.toString());
                        }
                    }
                }
            }
        }
    }

    /**
* Writes the standard resources of a project and its referenced projects
* into a {@link SignedJarBuilder}.
* Standard resources are non java/aidl files placed in the java package folders.
     * @param jarBuilder the {@link SignedJarBuilder}.
* @param javaProject the javaProject object.
* @param referencedJavaProjects the java projects that this project references.
     * @throws IOException
* @throws CoreException
*/
    private void writeStandardResources(SignedJarBuilder jarBuilder, IJavaProject javaProject,
            IJavaProject[] referencedJavaProjects) throws IOException, CoreException {
IWorkspace ws = ResourcesPlugin.getWorkspace();
IWorkspaceRoot wsRoot = ws.getRoot();

// create a list of path already put into the archive, in order to detect conflict
ArrayList<String> list = new ArrayList<String>();

        writeStandardProjectResources(jarBuilder, javaProject, wsRoot, list);

for (IJavaProject referencedJavaProject : referencedJavaProjects) {
// only include output from non android referenced project
//Synthetic comment -- @@ -720,7 +563,7 @@
// instrumentation projects that need to reference the projects to be tested).
if (referencedJavaProject.getProject().hasNature(
AndroidConstants.NATURE_DEFAULT) == false) {
                writeStandardProjectResources(jarBuilder, referencedJavaProject, wsRoot, list);
}
}
}
//Synthetic comment -- @@ -728,15 +571,18 @@
/**
* Writes the standard resources of a {@link IJavaProject} into a {@link SignedJarBuilder}.
* Standard resources are non java/aidl files placed in the java package folders.
     * @param jarBuilder the {@link SignedJarBuilder}.
* @param javaProject the javaProject object.
* @param wsRoot the {@link IWorkspaceRoot}.
* @param list a list of files already added to the archive, to detect conflicts.
     * @throws IOException
*/
    private void writeStandardProjectResources(SignedJarBuilder jarBuilder,
IJavaProject javaProject, IWorkspaceRoot wsRoot, ArrayList<String> list)
            throws IOException {
// get the source pathes
ArrayList<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(javaProject);

//Synthetic comment -- @@ -744,75 +590,13 @@
for (IPath sourcePath : sourceFolders) {
IResource sourceResource = wsRoot.findMember(sourcePath);
if (sourceResource != null && sourceResource.getType() == IResource.FOLDER) {
                writeStandardSourceFolderResources(jarBuilder, sourcePath, (IFolder)sourceResource,
                        list);
}
}
}

/**
     * Recursively writes the standard resources of a source folder into a {@link SignedJarBuilder}.
     * Standard resources are non java/aidl files placed in the java package folders.
     * @param jarBuilder the {@link SignedJarBuilder}.
     * @param sourceFolder the {@link IPath} of the source folder.
     * @param currentFolder The current folder we're recursively processing.
     * @param list a list of files already added to the archive, to detect conflicts.
     * @throws IOException
     */
    private void writeStandardSourceFolderResources(SignedJarBuilder jarBuilder, IPath sourceFolder,
            IFolder currentFolder, ArrayList<String> list) throws IOException {
        try {
            IResource[] members = currentFolder.members();

            for (IResource member : members) {
                int type = member.getType();
                if (type == IResource.FILE && member.exists()) {
                    if (checkFileForPackaging((IFile)member)) {
                        // this files must be added to the archive.
                        IPath fullPath = member.getFullPath();

                        // We need to create its path inside the archive.
                        // This path is relative to the source folder.
                        IPath relativePath = fullPath.removeFirstSegments(
                                sourceFolder.segmentCount());
                        String zipPath = relativePath.toString();

                        // lets check it's not already in the list of path added to the archive
                        if (list.indexOf(zipPath) != -1) {
                            AdtPlugin.printErrorToConsole(mProject,
                                    String.format(
                                            Messages.ApkBuilder_s_Conflict_with_file_s,
                                            fullPath, zipPath));
                        } else {
                            // get the File object
                            File entryFile = member.getLocation().toFile();

                            AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, mProject,
                                    String.format(
                                            Messages.ApkBuilder_Packaging_s_into_s,
                                            fullPath, zipPath));

                            // write it in the zip archive
                            jarBuilder.writeFile(entryFile, zipPath);

                            // and add it to the list of entries
                            list.add(zipPath);
                        }
                    }
                } else if (type == IResource.FOLDER) {
                    if (checkFolderForPackaging((IFolder)member)) {
                        writeStandardSourceFolderResources(jarBuilder, sourceFolder,
                                (IFolder)member, list);
                    }
                }
            }
        } catch (CoreException e) {
            // if we can't get the members of the folder, we just don't do anything.
        }
    }


    /**
* Returns an array of external jar files used by the project.
* @return an array of OS-specific absolute file paths
*/
//Synthetic comment -- @@ -838,15 +622,11 @@

// check the name ends with .jar
if (AndroidConstants.EXT_JAR.equalsIgnoreCase(path.getFileExtension())) {
                        boolean local = false;
IResource resource = wsRoot.findMember(path);
if (resource != null && resource.exists() &&
resource.getType() == IResource.FILE) {
                            local = true;
oslibraryList.add(resource.getLocation().toOSString());
                        }

                        if (local == false) {
// if the jar path doesn't match a workspace resource,
// then we get an OSString and check if this links to a valid file.
String osFullPath = path.toOSString();
//Synthetic comment -- @@ -922,7 +702,7 @@
String name = file.getName();

String ext = file.getFileExtension();
        return JavaResourceFilter.checkFileForPackaging(name, ext);
}

/**
//Synthetic comment -- @@ -932,7 +712,7 @@
*/
static boolean checkFolderForPackaging(IFolder folder) {
String name = folder.getName();
        return JavaResourceFilter.checkFolderForPackaging(name);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkDeltaVisitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkDeltaVisitor.java
//Synthetic comment -- index 643fee5..81f06c1 100644

//Synthetic comment -- @@ -193,7 +193,7 @@
if (mOutputPath.equals(parentPath)) {
String resourceName = resource.getName();
// check if classes.dex was removed
                        if (resourceName.equalsIgnoreCase(AndroidConstants.FN_CLASSES_DEX)) {
mConvertToDex = true;
mMakeFinalPackage = true;
} else if (resourceName.equalsIgnoreCase(
//Synthetic comment -- @@ -237,7 +237,7 @@
// inside the native library folder. Test if the changed resource is a .so file.
if (type == IResource.FILE &&
(AndroidConstants.EXT_NATIVE_LIB.equalsIgnoreCase(path.getFileExtension())
                            || ApkBuilderHelper.GDBSERVER_NAME.equals(resource.getName()))) {
mMakeFinalPackage = true;
return false; // return false for file.
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index 36afc5b..824719a 100644

//Synthetic comment -- @@ -48,6 +48,9 @@

/** An SDK Project's AndroidManifest.xml file */
public static final String FN_ANDROID_MANIFEST_XML= "AndroidManifest.xml";
/** An SDK Project's build.xml file */
public final static String FN_BUILD_XML = "build.xml";

//Synthetic comment -- @@ -132,6 +135,11 @@
/** properties file for the SDK */
public final static String FN_SDK_PROP = "sdk.properties"; //$NON-NLS-1$

/* Folder Names for Android Projects . */

/** Resources folder name, i.e. "res". */








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java
new file mode 100644
//Synthetic comment -- index 0000000..cc514a1

//Synthetic comment -- @@ -0,0 +1,831 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilderMain.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilderMain.java
//Synthetic comment -- index fb6ee6a..c5f6ccb 100644

//Synthetic comment -- @@ -16,14 +16,14 @@

package com.android.sdklib.build;

import com.android.sdklib.internal.build.ApkBuilderHelper;
import com.android.sdklib.internal.build.ApkBuilderHelper.ApkCreationException;
import com.android.sdklib.internal.build.ApkBuilderHelper.ApkFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
//Synthetic comment -- @@ -31,13 +31,8 @@
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
//Synthetic comment -- @@ -50,97 +45,143 @@
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
System.err.println("\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.err.println("THIS TOOL IS DEPRECATED!\n");
System.err.println("If you wish to use apkbuilder for a custom build system, please look at the");
System.err.println("com.android.sdklib.build.ApkBuilder which provides support for");
System.err.println("recent build improvements including library projects.");








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/ApkBuilderHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/ApkBuilderHelper.java
deleted file mode 100644
//Synthetic comment -- index ba1c878..0000000

//Synthetic comment -- @@ -1,433 +0,0 @@
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

package com.android.sdklib.internal.build;

import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Command line APK builder with signing support.
 */
public final class ApkBuilderHelper {

    private final static Pattern PATTERN_JAR_EXT = Pattern.compile("^.+\\.jar$",
            Pattern.CASE_INSENSITIVE);
    private final static Pattern PATTERN_NATIVELIB_EXT = Pattern.compile("^.+\\.so$",
            Pattern.CASE_INSENSITIVE);

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
     */
    public final static class ApkFile {
        String archivePath;
        File file;

        ApkFile(File file, String path) {
            this.file = file;
            this.archivePath = path;
        }
    }

    private JavaResourceFilter mResourceFilter = new JavaResourceFilter();
    private boolean mVerbose = false;
    private boolean mSignedPackage = true;
    private boolean mDebugMode = false;
    /** the optional type of the debug keystore. If <code>null</code>, the default */
    private String mStoreType = null;

    public void setVerbose(boolean verbose) {
        mVerbose = verbose;
    }

    public boolean isVerbose() {
        return mVerbose;
    }

    public void setSignedPackage(boolean signedPackage) {
        mSignedPackage = signedPackage;
    }

    public void setDebugMode(boolean debugMode) {
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
            throw new ApkCreationException(filepath + " is a directory!");
        }

        if (f.exists()) { // will be a file in this case.
            if (f.canWrite() == false) {
                throw new ApkCreationException("Cannot write " + filepath);
            }
        } else {
            try {
                if (f.createNewFile() == false) {
                    throw new ApkCreationException("Failed to create " + filepath);
                }
            } catch (IOException e) {
                throw new ApkCreationException(
                        "Failed to create '" + filepath + "' : " + e.getMessage());
            }
        }

        return f;
    }

    /**
     * Returns a {@link File} representing a given file path. The path must represent
     * an actual existing file (not a directory). The path may be relative.
     * @param filepath the path to a file.
     * @return the File representing the path.
     * @throws ApkCreationException if the path represents a directory or if the file does not
     * exist, or cannot be read.
     */
    public static File getInputFile(String filepath) throws ApkCreationException {
        File f = new File(filepath);

        if (f.isDirectory()) {
            throw new ApkCreationException(filepath + " is a directory!");
        }

        if (f.exists()) {
            if (f.canRead() == false) {
                throw new ApkCreationException("Cannot read " + filepath);
            }
        } else {
            throw new ApkCreationException(filepath + " does not exists!");
        }

        return f;
    }

    /**
     * Processes a source folder and adds its java resources to a given list of {@link ApkFile}.
     * @param folder the folder representing the source folder.
     * @param javaResources the list of {@link ApkFile} to fill.
     * @throws ApkCreationException
     */
    public static void processSourceFolderForResource(File folder,
            ArrayList<ApkFile> javaResources) throws ApkCreationException {
        if (folder.isDirectory()) {
            // file is a directory, process its content.
            File[] files = folder.listFiles();
            for (File file : files) {
                processFileForResource(file, null, javaResources);
            }
        } else {
            // not a directory? output error and quit.
            if (folder.exists()) {
                throw new ApkCreationException(folder.getAbsolutePath() + " is not a folder!");
            } else {
                throw new ApkCreationException(folder.getAbsolutePath() + " does not exist!");
            }
        }
    }

    /**
     * Process a jar file or a jar folder
     * @param file the {@link File} to process
     * @param resourcesJars the collection of FileInputStream to fill up with jar files.
     * @throws FileNotFoundException
     * @throws ApkCreationException
     */
    public static void processJar(File file, Collection<FileInputStream> resourcesJars)
            throws FileNotFoundException, ApkCreationException {
        if (file.isDirectory()) {
            String[] filenames = file.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return PATTERN_JAR_EXT.matcher(name).matches();
                }
            });

            for (String filename : filenames) {
                File f = new File(file, filename);
                processJarFile(f, resourcesJars);
            }
        } else if (file.isFile()) {
            processJarFile(file, resourcesJars);
        } else {
            throw new ApkCreationException(file.getAbsolutePath()+ " does not exist!");
        }
    }

    public static void processJarFile(File file, Collection<FileInputStream> resourcesJars)
            throws FileNotFoundException {
        FileInputStream input = new FileInputStream(file);
        resourcesJars.add(input);
    }

    /**
     * Processes a {@link File} that could be a {@link ApkFile}, or a folder containing
     * java resources.
     * @param file the {@link File} to process.
     * @param path the relative path of this file to the source folder. Can be <code>null</code> to
     * identify a root file.
     * @param javaResources the Collection of {@link ApkFile} object to fill.
     */
    private static void processFileForResource(File file, String path,
            Collection<ApkFile> javaResources) {
        if (file.isDirectory()) {
            // a directory? we check it
            if (JavaResourceFilter.checkFolderForPackaging(file.getName())) {
                // if it's valid, we append its name to the current path.
                if (path == null) {
                    path = file.getName();
                } else {
                    path = path + "/" + file.getName();
                }

                // and process its content.
                File[] files = file.listFiles();
                for (File contentFile : files) {
                    processFileForResource(contentFile, path, javaResources);
                }
            }
        } else {
            // a file? we check it
            if (JavaResourceFilter.checkFileForPackaging(file.getName())) {
                // we append its name to the current path
                if (path == null) {
                    path = file.getName();
                } else {
                    path = path + "/" + file.getName();
                }

                // and add it to the list.
                javaResources.add(new ApkFile(file, path));
            }
        }
    }

    /**
     * Process a {@link File} for native library inclusion.
     * <p/>The root folder must include folders that include .so files.
     * @param root the native root folder.
     * @param nativeLibraries the collection to add native libraries to.
     * @param verbose verbose mode.
     * @param abiFilter optional ABI filter. If non-null only the given ABI is included.
     * @throws ApkCreationException
     */
    public static void processNativeFolder(File root, boolean debugMode,
            Collection<ApkFile> nativeLibraries, boolean verbose, String abiFilter)
            throws ApkCreationException {
        if (root.isDirectory() == false) {
            throw new ApkCreationException(root.getAbsolutePath() + " is not a folder!");
        }

        File[] abiList = root.listFiles();

        if (verbose) {
            System.out.println("Processing native folder: " + root.getAbsolutePath());
            if (abiFilter != null) {
                System.out.println("ABI Filter: " + abiFilter);
            }
        }

        if (abiList != null) {
            for (File abi : abiList) {
                if (abi.isDirectory()) { // ignore files

                    // check the abi filter and reject all other ABIs
                    if (abiFilter != null && abiFilter.equals(abi.getName()) == false) {
                        if (verbose) {
                            System.out.println("Rejecting ABI " + abi.getName());
                        }
                        continue;
                    }

                    File[] libs = abi.listFiles();
                    if (libs != null) {
                        for (File lib : libs) {
                            // only consider files that are .so or, if in debug mode, that
                            // are gdbserver executables
                            if (lib.isFile() &&
                                    (PATTERN_NATIVELIB_EXT.matcher(lib.getName()).matches() ||
                                            (debugMode && GDBSERVER_NAME.equals(lib.getName())))) {
                                String path =
                                    NATIVE_LIB_ROOT + abi.getName() + "/" + lib.getName();

                                nativeLibraries.add(new ApkFile(lib, path));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates the application package
     * @param outFile the package file to create
     * @param zipArchives the list of zip archive
     * @param files the list of files to include in the archive
     * @param javaResources the list of java resources from the source folders.
     * @param resourcesJars the list of jar files from which to take java resources
     * @throws ApkCreationException
     */
    public void createPackage(File outFile, Iterable<? extends FileInputStream> zipArchives,
            Iterable<? extends File> files, Iterable<? extends ApkFile> javaResources,
            Iterable<? extends FileInputStream> resourcesJars,
            Iterable<? extends ApkFile> nativeLibraries) throws ApkCreationException {

        // get the debug key
        try {
            SignedJarBuilder builder;

            if (mSignedPackage) {
                System.err.println(String.format("Using keystore: %s",
                        DebugKeyProvider.getDefaultKeyStoreOsPath()));


                DebugKeyProvider keyProvider = new DebugKeyProvider(
                        null /* osKeyPath: use default */,
                        mStoreType, null /* IKeyGenOutput */);
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

                builder = new SignedJarBuilder(
                        new FileOutputStream(outFile.getAbsolutePath(), false /* append */), key,
                        certificate);
            } else {
                builder = new SignedJarBuilder(
                        new FileOutputStream(outFile.getAbsolutePath(), false /* append */),
                        null /* key */, null /* certificate */);
            }

            // add the archives
            for (FileInputStream input : zipArchives) {
                builder.writeZip(input, null /* filter */);
            }

            // add the single files
            for (File input : files) {
                // always put the file at the root of the archive in this case
                builder.writeFile(input, input.getName());
                if (mVerbose) {
                    System.err.println(String.format("%1$s => %2$s", input.getAbsolutePath(),
                            input.getName()));
                }
            }

            // add the java resource from the source folders.
            for (ApkFile resource : javaResources) {
                builder.writeFile(resource.file, resource.archivePath);
                if (mVerbose) {
                    System.err.println(String.format("%1$s => %2$s",
                            resource.file.getAbsolutePath(), resource.archivePath));
                }
            }

            // add the java resource from jar files.
            for (FileInputStream input : resourcesJars) {
                builder.writeZip(input, mResourceFilter);
            }

            // add the native files
            for (ApkFile file : nativeLibraries) {
                builder.writeFile(file.file, file.archivePath);
                if (mVerbose) {
                    System.err.println(String.format("%1$s => %2$s", file.file.getAbsolutePath(),
                            file.archivePath));
                }
            }

            // close and sign the application package.
            builder.close();
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
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/JavaResourceFilter.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/JavaResourceFilter.java
deleted file mode 100644
//Synthetic comment -- index aac1e55..0000000

//Synthetic comment -- @@ -1,103 +0,0 @@
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

package com.android.sdklib.internal.build;

import com.android.sdklib.internal.build.SignedJarBuilder.IZipEntryFilter;

/**
 * A basic implementation of {@link IZipEntryFilter} to filter out anything that is not a
 * java resource.
 */
public class JavaResourceFilter implements IZipEntryFilter {

    public boolean checkEntry(String name) {
        // split the path into segments.
        String[] segments = name.split("/");

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

        return checkFileForPackaging(fileName);
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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SignedJarBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SignedJarBuilder.java
//Synthetic comment -- index 79e4be2..81131bc 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.internal.build;

import sun.misc.BASE64Encoder;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
//Synthetic comment -- @@ -100,14 +102,42 @@
* be added to a Jar file.
*/
public interface IZipEntryFilter {
/**
* Checks a file for inclusion in a Jar archive.
         * @param name the archive file path of the entry
* @return <code>true</code> if the file should be included.
*/
        public boolean checkEntry(String name);
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
*/
    public void writeZip(InputStream input, IZipEntryFilter filter) throws IOException {
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







