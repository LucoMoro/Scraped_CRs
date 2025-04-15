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

import com.android.sdklib.build.ApkBuilder;
import com.android.sdklib.build.ApkBuilder.ApkCreationException;
import com.android.sdklib.build.ApkBuilder.DuplicateFileException;
import com.android.sdklib.build.ApkBuilder.SealedApkException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
//Synthetic comment -- @@ -26,12 +27,15 @@
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ApkBuilderTask extends Task {

    private final static Pattern PATTERN_JAR_EXT = Pattern.compile("^.+\\.jar$",
            Pattern.CASE_INSENSITIVE);

private String mOutFolder;
@Deprecated private String mBaseName;
private String mApkFilepath;
//Synthetic comment -- @@ -42,20 +46,15 @@
private boolean mHasCode = true;
private String mAbiFilter = null;

    private Path mDexPath;

private final ArrayList<Path> mZipList = new ArrayList<Path>();
private final ArrayList<Path> mFileList = new ArrayList<Path>();
private final ArrayList<Path> mSourceList = new ArrayList<Path>();
private final ArrayList<Path> mJarfolderList = new ArrayList<Path>();
private final ArrayList<Path> mJarfileList = new ArrayList<Path>();
private final ArrayList<Path> mNativeList = new ArrayList<Path>();

/**
* Sets the value of the "outfolder" attribute.
* @param outFolder the value.
//Synthetic comment -- @@ -154,15 +153,19 @@
* is <code>false</code> in which case it's ignored.
*/
public Object createDex() {
        if (mDexPath == null) {
            return mDexPath = new Path(getProject());
        } else {
            throw new BuildException("Only one <dex> inner element can be provided");
        }
}

/**
* Returns an object representing a nested <var>file</var> element.
*/
public Object createFile() {
        System.out.println("WARNNG: Using deprecated <file> inner element in ApkBuilderTask." +
        "Use <dex path=...> instead.");
Path path = new Path(getProject());
mFileList.add(path);
return path;
//Synthetic comment -- @@ -208,36 +211,69 @@
public void execute() throws BuildException {
Project antProject = getProject();

        // get the rules revision to figure out how to build the output file.
        String rulesRevStr = antProject.getProperty(TaskHelper.PROP_RULES_REV);
        int rulesRev = 1;
        try {
            rulesRev = Integer.parseInt(rulesRevStr);
        } catch (NumberFormatException e) {
            // this shouldn't happen since setup task is the one setting up every time.
        }

        File outputFile;
        if (mApkFilepath != null) {
            outputFile = new File(mApkFilepath);
        } else if (rulesRev == 2) {
            if (mSigned) {
                outputFile = new File(mOutFolder, mBaseName + "-debug-unaligned.apk");
            } else {
                outputFile = new File(mOutFolder, mBaseName + "-unsigned.apk");
            }
        } else {
            throw new BuildException("missing attribute 'apkFilepath'");
        }

        // check dexPath is only one file.
        File dexFile = null;
        if (mHasCode) {
            String[] dexFiles = mDexPath.list();
            if (dexFiles.length != 1) {
                throw new BuildException(String.format(
                        "Expected one dex file but path value resolve to %d files.", dexFiles.length));
            }
            dexFile = new File(dexFiles[0]);
        }

try {
            if (mSigned) {
                System.out.println(String.format(
                        "Creating %s and signing it with a debug key...", outputFile.getName()));
            } else {
                System.out.println(String.format(
                        "Creating %s for release...", outputFile.getName()));
            }

            ApkBuilder apkBuilder = new ApkBuilder(
                    outputFile,
                    new File(mOutFolder, mResourceFile),
                    dexFile,
                    mSigned ? ApkBuilder.getDebugKeystore() : null,
                    mVerbose ? System.out : null);
            apkBuilder.setDebugMode(mDebug);


            // add the content of the zip files.
for (Path pathList : mZipList) {
for (String path : pathList.list()) {
                    apkBuilder.addZipFile(new File(path));
}
}

            // add the files that go to the root of the archive (this is deprecated)
for (Path pathList : mFileList) {
for (String path : pathList.list()) {
                    File f = new File(path);
                    apkBuilder.addFile(f, f.getName());
}
}

//Synthetic comment -- @@ -245,8 +281,7 @@
if (mHasCode) {
for (Path pathList : mSourceList) {
for (String path : pathList.list()) {
                        apkBuilder.addSourceFolder(new File(path));
}
}
}
//Synthetic comment -- @@ -257,7 +292,15 @@
// it's ok if top level folders are missing
File folder = new File(path);
if (folder.isDirectory()) {
                        String[] filenames = folder.list(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                return PATTERN_JAR_EXT.matcher(name).matches();
                            }
                        });

                        for (String filename : filenames) {
                            apkBuilder.addResourcesFromJar(new File(folder, filename));
                        }
}
}
}
//Synthetic comment -- @@ -265,7 +308,7 @@
// now go through the list of jar files.
for (Path pathList : mJarfileList) {
for (String path : pathList.list()) {
                    apkBuilder.addResourcesFromJar(new File(path));
}
}

//Synthetic comment -- @@ -275,77 +318,26 @@
// it's ok if top level folders are missing
File folder = new File(path);
if (folder.isDirectory()) {
                        apkBuilder.addNativeLibraries(folder, mAbiFilter);
}
}
}


            // close the archive
            apkBuilder.sealApk();

        } catch (DuplicateFileException e) {
            System.err.println(String.format(
                    "Found duplicate file for APK: %1$s\nOrigin 1: %2$s\nOrigin 2: %3$s",
                    e.getArchivePath(), e.getFile1(), e.getFile2()));
            throw new BuildException(e);
        } catch (ApkCreationException e) {
            throw new BuildException(e);
        } catch (SealedApkException e) {
throw new BuildException(e);
} catch (IllegalArgumentException e) {
throw new BuildException(e);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java
//Synthetic comment -- index d5d2185..73f6fcb 100644

//Synthetic comment -- @@ -101,8 +101,6 @@
public final static String FN_COMPILED_RESOURCE_CLASS = FN_RESOURCE_BASE + DOT_CLASS;
/** Manifest java class filename, i.e. "Manifest.java" */
public final static String FN_MANIFEST_CLASS = "Manifest.java"; //$NON-NLS-1$
/** Temporary packaged resources file name, i.e. "resources.ap_" */
public final static String FN_RESOURCES_AP_ = "resources.ap_"; //$NON-NLS-1$
/** Temporary packaged resources file name for a specific set of configuration */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/MultiApkExportAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/MultiApkExportAction.java
//Synthetic comment -- index fe05b1d..aa4b700 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.ide.eclipse.adt.internal.actions;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.ApkBuilderHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectState;
//Synthetic comment -- @@ -282,17 +281,23 @@
apk.setOutputName(softVariant != null ? softVariant.getKey() : null, outputName);

// do the final export.
        IFile dexFile = projectBinFolder.getFile(SdkConstants.FN_APK_CLASSES_DEX);
String outputFile = binFolder.getFile(outputName).getLocation().toOSString();

// get the list of referenced projects.
IProject[] javaRefs = ProjectHelper.getReferencedProjects(project);
IJavaProject[] referencedJavaProjects = ApkBuilderHelper.getJavaProjects(javaRefs);

        helper.finalPackage(
                new File(projectBinFolderPath, pkgName).getAbsolutePath(),
dexFile.getLocation().toOSString(),
                outputFile,
                false /*debugSign */,
                javaProject,
                libProjects,
                referencedJavaProjects,
                apk.getAbi(),
                false /*debuggable*/);

}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilder.java
//Synthetic comment -- index 9cd3036..8360feb 100644

//Synthetic comment -- @@ -350,7 +350,7 @@

// check classes.dex is present. If not we force to recreate it.
if (mConvertToDex == false) {
                tmp = outputFolder.findMember(SdkConstants.FN_APK_CLASSES_DEX);
if (tmp == null || tmp.exists() == false) {
mConvertToDex = true;
mBuildFinalPackage = true;
//Synthetic comment -- @@ -445,7 +445,7 @@
// then we check if we need to package the .class into classes.dex
if (mConvertToDex) {
if (helper.executeDx(javaProject, osBinPath, osBinPath + File.separator +
                            SdkConstants.FN_APK_CLASSES_DEX, referencedJavaProjects) == false) {
// dx failed, we return
return allRefProjects;
}
//Synthetic comment -- @@ -477,10 +477,11 @@
// This is the default package with all the resources.

String classesDexPath = osBinPath + File.separator +
                        SdkConstants.FN_APK_CLASSES_DEX;
if (helper.finalPackage(
osBinPath + File.separator + AndroidConstants.FN_RESOURCES_AP_,
                        classesDexPath, osFinalPackagePath, true /*debugSign*/,
                        javaProject, libProjects,
referencedJavaProjects, null /*abiFilter*/, debuggable) == false) {
return allRefProjects;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilderHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilderHelper.java
//Synthetic comment -- index 04acf44..3c2d86a 100644

//Synthetic comment -- @@ -27,12 +27,14 @@
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.build.ApkBuilder;
import com.android.sdklib.build.ApkBuilder.ApkCreationException;
import com.android.sdklib.build.ApkBuilder.DuplicateFileException;
import com.android.sdklib.build.ApkBuilder.JarStatus;
import com.android.sdklib.build.ApkBuilder.SealedApkException;
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.sdklib.internal.build.SignedJarBuilder;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -45,7 +47,6 @@
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
//Synthetic comment -- @@ -54,73 +55,30 @@
import org.eclipse.jface.preference.IPreferenceStore;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper with methods for the last 3 steps of the generation of an APK.
 *
 * {@link #packageResources(IFile, IProject[], String, int, String, String)} packages the
 * application resources using aapt into a zip file that is ready to be integrated into the apk.
 *
 * {@link #executeDx(IJavaProject, String, String, IJavaProject[])} will convert the Java byte
 * code into the Dalvik bytecode.
 *
 * {@link #finalPackage(String, String, String, boolean, IJavaProject, IProject[], IJavaProject[], String, boolean)}
 * will make the apk from all the previous components.
 *
 */
public class ApkBuilderHelper {

private final IProject mProject;
private final PrintStream mOutStream;
private final PrintStream mErrStream;

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
     * @param debugSign whether the apk must be signed with the debug key.
* @param javaProject the java project being compiled
* @param libProjects an optional list of library projects (can be null)
* @param referencedJavaProjects referenced projects.
//Synthetic comment -- @@ -209,128 +167,96 @@
* @return true if success, false otherwise.
*/
public boolean finalPackage(String intermediateApk, String dex, String output,
            boolean debugSign, final IJavaProject javaProject, IProject[] libProjects,
IJavaProject[] referencedJavaProjects, String abiFilter, boolean debuggable) {

        IProject project = javaProject.getProject();

        String keystoreOsPath = null;
        if (debugSign) {
IPreferenceStore store = AdtPlugin.getDefault().getPreferenceStore();
            keystoreOsPath = store.getString(AdtPrefs.PREFS_CUSTOM_DEBUG_KEYSTORE);
            if (keystoreOsPath == null || new File(keystoreOsPath).isFile() == false) {
                try {
                    keystoreOsPath = DebugKeyProvider.getDefaultKeyStoreOsPath();
                    AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, mProject,
                            Messages.ApkBuilder_Using_Default_Key);
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

                    return false;
                } catch (AndroidLocationException e) {
                    String eMessage = e.getMessage();

                    // mark the project with the standard message
                    String msg = String.format(Messages.Final_Archive_Error_s, eMessage);
                    BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING, msg,
                            IMarker.SEVERITY_ERROR);

                    return false;
                }
} else {
AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, mProject,
                        String.format(Messages.ApkBuilder_Using_s_To_Sign, keystoreOsPath));
}
        }


        try {
            ApkBuilder apkBuilder = new ApkBuilder(output, intermediateApk, dex, keystoreOsPath,
                    AdtPrefs.getPrefs().getBuildVerbosity() == BuildVerbosity.VERBOSE ?
                            AdtPlugin.getOutPrintStream(project, null): null);
            apkBuilder.setDebugMode(debuggable);

// Now we write the standard resources from the project and the referenced projects.
            writeStandardResources(apkBuilder, javaProject, referencedJavaProjects);

            // Now we write the standard resources from the external jars
for (String libraryOsPath : getExternalJars()) {
                JarStatus status = apkBuilder.addResourcesFromJar(new File(libraryOsPath));

                // check if we found native libraries in the external library. This
                // constitutes an error or warning depending on if they are in lib/
                if (status.getNativeLibs().size() > 0) {
                    String libName = new File(libraryOsPath).getName();
                    String msg = String.format(
                            "Native libraries detected in '%1$s'. See console for more information.",
                            libName);

                    BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING,
                            msg,
                            status.hasNativeLibsConflicts() ||
                                    AdtPrefs.getPrefs().getBuildForceErrorOnNativeLibInJar() ?
                                    IMarker.SEVERITY_ERROR : IMarker.SEVERITY_WARNING);

                    ArrayList<String> consoleMsgs = new ArrayList<String>();
                    consoleMsgs.add(String.format(
                            "The library '%1$s' contains native libraries that will not run on the device.",
                            libName));
                    if (status.hasNativeLibsConflicts()) {
                        consoleMsgs.add("Additionally some of those libraries will interfer with the installation of the application because of their location in lib/");
                        consoleMsgs.add("lib/ is reserved for NDK libraries.");
}
                    consoleMsgs.add("The following libraries were found:");
                    for (String lib : status.getNativeLibs()) {
                        consoleMsgs.add(" - " + lib);
                    }
                    AdtPlugin.printErrorToConsole(mProject,
                            consoleMsgs.toArray());

                    return false;
}
}

//Synthetic comment -- @@ -339,8 +265,8 @@
IResource libFolder = mProject.findMember(SdkConstants.FD_NATIVE_LIBS);
if (libFolder != null && libFolder.exists() &&
libFolder.getType() == IResource.FOLDER) {
                // get a File for the folder.
                apkBuilder.addNativeLibraries(libFolder.getLocation().toFile(), abiFilter);
}

// write the native libraries for the library projects.
//Synthetic comment -- @@ -349,59 +275,36 @@
libFolder = lib.findMember(SdkConstants.FD_NATIVE_LIBS);
if (libFolder != null && libFolder.exists() &&
libFolder.getType() == IResource.FOLDER) {
                        apkBuilder.addNativeLibraries(libFolder.getLocation().toFile(), abiFilter);
}
}
}

            // seal the APK.
            apkBuilder.sealApk();
            return true;
} catch (CoreException e) {
// mark project and return
String msg = String.format(Messages.Final_Archive_Error_s, e.getMessage());
AdtPlugin.printErrorToConsole(mProject, msg);
BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING, msg,
IMarker.SEVERITY_ERROR);
        } catch (ApkCreationException e) {
            // mark project and return
            String msg = String.format(Messages.Final_Archive_Error_s, e.getMessage());
            AdtPlugin.printErrorToConsole(mProject, msg);
            BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING, msg,
                    IMarker.SEVERITY_ERROR);
        } catch (DuplicateFileException e) {
            String msg1 = String.format(
                    "Found duplicate file for APK: %1$s\nOrigin 1: %2$s\nOrigin 2: %3$s",
                    e.getArchivePath(), e.getFile1(), e.getFile2());
            String msg2 = String.format(Messages.Final_Archive_Error_s, msg1);
            AdtPlugin.printErrorToConsole(mProject, msg2);
            BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING, msg2,
                    IMarker.SEVERITY_ERROR);
        } catch (SealedApkException e) {
            // this won't happen as we control when the apk is sealed.
} catch (Exception e) {
// try to catch other exception to actually display an error. This will be useful
// if we get an NPE or something so that we can at least notify the user that something
//Synthetic comment -- @@ -416,18 +319,9 @@
AdtPlugin.printErrorToConsole(mProject, msg);
BaseProjectHelper.markResource(mProject, AndroidConstants.MARKER_PACKAGING, msg,
IMarker.SEVERITY_ERROR);
}

        return false;
}

/**
//Synthetic comment -- @@ -639,80 +533,29 @@


/**
* Writes the standard resources of a project and its referenced projects
* into a {@link SignedJarBuilder}.
* Standard resources are non java/aidl files placed in the java package folders.
     * @param apkBuilder the {@link ApkBuilder}.
* @param javaProject the javaProject object.
* @param referencedJavaProjects the java projects that this project references.
     * @throws ApkCreationException if an error occurred
     * @throws SealedApkException if the APK is already sealed.
     * @throws DuplicateFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
* @throws CoreException
*/
    private void writeStandardResources(ApkBuilder apkBuilder, IJavaProject javaProject,
            IJavaProject[] referencedJavaProjects)
            throws DuplicateFileException, ApkCreationException, SealedApkException,
            CoreException  {
IWorkspace ws = ResourcesPlugin.getWorkspace();
IWorkspaceRoot wsRoot = ws.getRoot();

// create a list of path already put into the archive, in order to detect conflict
ArrayList<String> list = new ArrayList<String>();

        writeStandardProjectResources(apkBuilder, javaProject, wsRoot, list);

for (IJavaProject referencedJavaProject : referencedJavaProjects) {
// only include output from non android referenced project
//Synthetic comment -- @@ -720,7 +563,7 @@
// instrumentation projects that need to reference the projects to be tested).
if (referencedJavaProject.getProject().hasNature(
AndroidConstants.NATURE_DEFAULT) == false) {
                writeStandardProjectResources(apkBuilder, referencedJavaProject, wsRoot, list);
}
}
}
//Synthetic comment -- @@ -728,15 +571,18 @@
/**
* Writes the standard resources of a {@link IJavaProject} into a {@link SignedJarBuilder}.
* Standard resources are non java/aidl files placed in the java package folders.
     * @param jarBuilder the {@link ApkBuilder}.
* @param javaProject the javaProject object.
* @param wsRoot the {@link IWorkspaceRoot}.
* @param list a list of files already added to the archive, to detect conflicts.
     * @throws ApkCreationException if an error occurred
     * @throws SealedApkException if the APK is already sealed.
     * @throws DuplicateFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
*/
    private void writeStandardProjectResources(ApkBuilder apkBuilder,
IJavaProject javaProject, IWorkspaceRoot wsRoot, ArrayList<String> list)
            throws DuplicateFileException, ApkCreationException, SealedApkException {
// get the source pathes
ArrayList<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(javaProject);

//Synthetic comment -- @@ -744,75 +590,13 @@
for (IPath sourcePath : sourceFolders) {
IResource sourceResource = wsRoot.findMember(sourcePath);
if (sourceResource != null && sourceResource.getType() == IResource.FOLDER) {
                // get a File from the IResource
                apkBuilder.addSourceFolder(sourceResource.getLocation().toFile());
}
}
}

/**
* Returns an array of external jar files used by the project.
* @return an array of OS-specific absolute file paths
*/
//Synthetic comment -- @@ -838,15 +622,11 @@

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
//Synthetic comment -- @@ -922,7 +702,7 @@
String name = file.getName();

String ext = file.getFileExtension();
        return ApkBuilder.checkFileForPackaging(name, ext);
}

/**
//Synthetic comment -- @@ -932,7 +712,7 @@
*/
static boolean checkFolderForPackaging(IFolder folder) {
String name = folder.getName();
        return ApkBuilder.checkFolderForPackaging(name);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkDeltaVisitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkDeltaVisitor.java
//Synthetic comment -- index 643fee5..81f06c1 100644

//Synthetic comment -- @@ -193,7 +193,7 @@
if (mOutputPath.equals(parentPath)) {
String resourceName = resource.getName();
// check if classes.dex was removed
                        if (resourceName.equalsIgnoreCase(SdkConstants.FN_APK_CLASSES_DEX)) {
mConvertToDex = true;
mMakeFinalPackage = true;
} else if (resourceName.equalsIgnoreCase(
//Synthetic comment -- @@ -237,7 +237,7 @@
// inside the native library folder. Test if the changed resource is a .so file.
if (type == IResource.FILE &&
(AndroidConstants.EXT_NATIVE_LIB.equalsIgnoreCase(path.getFileExtension())
                            || SdkConstants.FN_GDBSERVER.equals(resource.getName()))) {
mMakeFinalPackage = true;
return false; // return false for file.
}








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
//Synthetic comment -- index 0000000..cc514a1

//Synthetic comment -- @@ -0,0 +1,831 @@
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
                throw new DuplicateFileException(archivePath, duplicate, mInputFile);
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
                    throw new DuplicateFileException(archivePath, duplicate, mInputFile);
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
    public final static class DuplicateFileException extends ZipAbortException {
        private static final long serialVersionUID = 1L;
        private final String mArchivePath;
        private final File mFile1;
        private final File mFile2;

        public DuplicateFileException(String archivePath, File file1, File file2) {
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

        @Override
        public String getMessage() {
            return "Duplicate files at the same path inside the APK";
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
     * @param dexOsPath the OS path of the dex file. This can be null for apk with no code.
     * @throws ApkCreationException
     */
    public ApkBuilder(String apkOsPath, String resOsPath, String dexOsPath, String storeOsPath,
            PrintStream verboseStream) throws ApkCreationException {
        this(new File(apkOsPath),
             new File(resOsPath),
             dexOsPath != null ? new File(dexOsPath) : null,
             storeOsPath,
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
     * @param dexFile the file representing the dex file. This can be null for apk with no code.
     * @param storeOsPath the OS path to the debug keystore, if needed or null.
     * @param verboseStream the stream to which verbose output should go. If null, verbose mode
     *                      is not enabled.
     * @throws ApkCreationException
     */
    public ApkBuilder(File apkFile, File resFile, File dexFile, String storeOsPath,
            PrintStream verboseStream) throws ApkCreationException {
        checkOutputFile(mApkFile = apkFile);
        checkInputFile(mResFile = resFile);
        if (dexFile != null) {
            checkInputFile(mDexFile = dexFile);
        } else {
            mDexFile = null;
        }
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
            if (mDexFile != null) {
                addFile(mDexFile, SdkConstants.FN_APK_CLASSES_DEX);
            }

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
     * @throws DuplicateFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
     */
    public void addFile(File file, String archivePath) throws ApkCreationException,
            SealedApkException, DuplicateFileException {
        if (mIsSealed) {
            throw new SealedApkException("APK is already sealed");
        }

        try {
            doAddFile(file, archivePath);
        } catch (DuplicateFileException e) {
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
     * @throws DuplicateFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
     */
    public void addZipFile(File zipFile) throws ApkCreationException, SealedApkException,
            DuplicateFileException {
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
        } catch (DuplicateFileException e) {
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
     * @throws DuplicateFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
     */
    public JarStatus addResourcesFromJar(File jarFile) throws ApkCreationException,
            SealedApkException, DuplicateFileException {
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
        } catch (DuplicateFileException e) {
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
     * @throws DuplicateFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
     */
    public void addSourceFolder(File sourceFolder) throws ApkCreationException, SealedApkException,
            DuplicateFileException {
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
            } catch (DuplicateFileException e) {
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
     * @throws DuplicateFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
     *
     * @see #setDebugMode(boolean)
     */
    public void addNativeLibraries(File nativeFolder, String abiFilter)
            throws ApkCreationException, SealedApkException, DuplicateFileException {
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

    private void doAddFile(File file, String archivePath) throws DuplicateFileException,
            IOException {
        verbosePrintln("%1$s => %2$s", file, archivePath);

        File duplicate = checkFileForDuplicate(archivePath);
        if (duplicate != null) {
            throw new DuplicateFileException(archivePath, duplicate, file);
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
     * @throws DuplicateFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
     */
    private void processFileForResource(File file, String path)
            throws IOException, DuplicateFileException {
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

    public static String getDebugKeystore() throws ApkCreationException {
        try {
            return DebugKeyProvider.getDefaultKeyStoreOsPath();
        } catch (Exception e) {
            throw new ApkCreationException(e, e.getMessage());
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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilderMain.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilderMain.java
//Synthetic comment -- index fb6ee6a..c5f6ccb 100644

//Synthetic comment -- @@ -16,14 +16,14 @@

package com.android.sdklib.build;

import com.android.sdklib.build.ApkBuilder.ApkCreationException;
import com.android.sdklib.build.ApkBuilder.DuplicateFileException;
import com.android.sdklib.build.ApkBuilder.SealedApkException;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.regex.Pattern;


/**
//Synthetic comment -- @@ -31,13 +31,8 @@
*/
public final class ApkBuilderMain {

    private final static Pattern PATTERN_JAR_EXT = Pattern.compile("^.+\\.jar$",
            Pattern.CASE_INSENSITIVE);

/**
* Main method. This is meant to be called from the command line through an exec.
//Synthetic comment -- @@ -50,97 +45,143 @@
}

try {
            File outApk = new File(args[0]);

            File dexFile = null;
            ArrayList<File> zipArchives = new ArrayList<File>();
            ArrayList<File> sourceFolders = new ArrayList<File>();
            ArrayList<File> jarFiles = new ArrayList<File>();
            ArrayList<File> nativeFolders = new ArrayList<File>();

            boolean verbose = false;
            boolean signed = true;
            boolean debug = false;

int index = 1;
do {
String argument = args[index++];

if ("-v".equals(argument)) {
                    verbose = true;

} else if ("-d".equals(argument)) {
                    debug = true;

} else if ("-u".equals(argument)) {
                    signed = false;

} else if ("-z".equals(argument)) {
// quick check on the next argument.
if (index == args.length)  {
printAndExit("Missing value for -z");
}

                    zipArchives.add(new File(args[index++]));
} else if ("-f". equals(argument)) {
                    if (dexFile != null) {
                        // can't have more than one dex file.
                        printAndExit("Can't have more than one dex file (-f)");
                    }
// quick check on the next argument.
if (index == args.length) {
printAndExit("Missing value for -f");
}

                    dexFile = new File(args[index++]);
} else if ("-rf". equals(argument)) {
// quick check on the next argument.
if (index == args.length) {
printAndExit("Missing value for -rf");
}

                    sourceFolders.add(new File(args[index++]));
} else if ("-rj". equals(argument)) {
// quick check on the next argument.
if (index == args.length) {
printAndExit("Missing value for -rj");
}

                    jarFiles.add(new File(args[index++]));
} else if ("-nf".equals(argument)) {
// quick check on the next argument.
if (index == args.length) {
printAndExit("Missing value for -nf");
}

                    nativeFolders.add(new File(args[index++]));
} else if ("-storetype".equals(argument)) {
// quick check on the next argument.
if (index == args.length) {
printAndExit("Missing value for -storetype");
}

                    // FIXME
} else {
printAndExit("Unknown argument: " + argument);
}
} while (index < args.length);

            if (zipArchives.size() == 0) {
                printAndExit("No zip archive, there must be one for the resources");
            }

            // create the builder with the basic files.
            ApkBuilder builder = new ApkBuilder(outApk, zipArchives.get(0), dexFile,
                    signed ? ApkBuilder.getDebugKeystore() : null,
                    verbose ? System.out : null);
            builder.setDebugMode(debug);

            // add the rest of the files.
            // first zip Archive was used in the constructor.
            for (int i = 1 ; i < zipArchives.size() ; i++) {
                builder.addZipFile(zipArchives.get(i));
            }

            for (File sourceFolder : sourceFolders) {
                builder.addSourceFolder(sourceFolder);
            }

            for (File jarFile : jarFiles) {
                if (jarFile.isDirectory()) {
                    String[] filenames = jarFile.list(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            return PATTERN_JAR_EXT.matcher(name).matches();
                        }
                    });

                    for (String filename : filenames) {
                        builder.addResourcesFromJar(new File(jarFile, filename));
                    }
                } else {
                    builder.addResourcesFromJar(jarFile);
                }
            }

            for (File nativeFolder : nativeFolders) {
                builder.addNativeLibraries(nativeFolder, null /*abiFilter*/);
            }

            // seal the apk
            builder.sealApk();


} catch (ApkCreationException e) {
printAndExit(e.getMessage());
        } catch (DuplicateFileException e) {
            printAndExit(String.format(
                    "Found duplicate file for APK: %1$s\nOrigin 1: %2$s\nOrigin 2: %3$s",
                    e.getArchivePath(), e.getFile1(), e.getFile2()));
        } catch (SealedApkException e) {
            printAndExit(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
}
}

private static void printUsageAndQuit() {
// 80 cols marker:  01234567890123456789012345678901234567890123456789012345678901234567890123456789
System.err.println("\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.err.println("THIS TOOL IS DEPRECATED and may stop working at any time!\n");
System.err.println("If you wish to use apkbuilder for a custom build system, please look at the");
System.err.println("com.android.sdklib.build.ApkBuilder which provides support for");
System.err.println("recent build improvements including library projects.");








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/ApkBuilderHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/ApkBuilderHelper.java
deleted file mode 100644
//Synthetic comment -- index ba1c878..0000000

//Synthetic comment -- @@ -1,433 +0,0 @@








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







