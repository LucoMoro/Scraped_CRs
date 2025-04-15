/*Use aapt output to feed proguard's keep list in ADT.

Change-Id:I3ba053055c302747082f8a5d6720172cefefb9bc*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtConstants.java
//Synthetic comment -- index cb5faba..9c1040e 100644

//Synthetic comment -- @@ -142,6 +142,9 @@
/** Temporary packaged resources file name, i.e. "resources.ap_" */
public final static String FN_RESOURCES_AP_ = "resources.ap_"; //$NON-NLS-1$

    /** aapt's proguard output */
    public final static String FN_AAPT_PROGUARD = "proguard.txt"; //$NON-NLS-1$

public final static String FN_TRACEVIEW =
(SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) ?
"traceview.bat" : "traceview"; //$NON-NLS-1$ //$NON-NLS-2$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 0347af6..8a9364e 100644

//Synthetic comment -- @@ -46,6 +46,7 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.build.BuildConfigGenerator;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.io.FileOp;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.ManifestData;
//Synthetic comment -- @@ -648,8 +649,14 @@
if (DEBUG_LOG) {
AdtPlugin.log(IStatus.INFO, "%s compiling resources!", project.getName());
}

                IFile proguardFile = null;
                if (projectState.getProperty(ProjectProperties.PROPERTY_PROGUARD_CONFIG) != null) {
                    proguardFile = androidOutputFolder.getFile(AdtConstants.FN_AAPT_PROGUARD);
                }

handleResources(project, javaPackage, projectTarget, manifestFile, libProjects,
                        projectState.isLibrary(), proguardFile);
}

if (processorStatus == SourceProcessor.COMPILE_STATUS_NONE &&
//Synthetic comment -- @@ -882,7 +889,7 @@
* @throws AbortBuildException
*/
private void handleResources(IProject project, String javaPackage, IAndroidTarget projectTarget,
            IFile manifest, List<IProject> libProjects, boolean isLibrary, IFile proguardFile)
throws CoreException, AbortBuildException {
// get the resource folder
IFolder resFolder = project.getFolder(AdtConstants.WS_RESOURCES);
//Synthetic comment -- @@ -941,8 +948,11 @@

}

            String proguardFilePath = proguardFile != null ?
                    proguardFile.getLocation().toOSString(): null;

execAapt(project, projectTarget, osOutputPath, osResPath, osManifestPath,
                    mainPackageFolder, libResFolders, libPackages, isLibrary, proguardFilePath);
}
}

//Synthetic comment -- @@ -959,14 +969,16 @@
* If <var>customJavaPackage</var> is not null, this must match the new destination triggered
* by its value.
* @param libResFolders the list of res folders for the library.
     * @param libraryPackages an optional list of javapackages to replace the main project java
     * package. can be null.
* @param isLibrary if the project is a library project
     * @param proguardFile an optional path to store proguard information
* @throws AbortBuildException
*/
private void execAapt(IProject project, IAndroidTarget projectTarget, String osOutputPath,
String osResPath, String osManifestPath, IFolder packageFolder,
            ArrayList<IFolder> libResFolders, String libraryPackages, boolean isLibrary,
            String proguardFile)
throws AbortBuildException {

// We actually need to delete the manifest.java as it may become empty and
//Synthetic comment -- @@ -1016,6 +1028,12 @@
array.add("-I"); //$NON-NLS-1$
array.add(projectTarget.getPath(IAndroidTarget.ANDROID_JAR));

        // use the proguard file
        if (proguardFile != null && proguardFile.length() > 0) {
            array.add("-G");
            array.add(proguardFile);
        }

if (AdtPrefs.getPrefs().getBuildVerbosity() == BuildVerbosity.VERBOSE) {
StringBuilder sb = new StringBuilder();
for (String c : array) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index 86c9b22..c391b1c 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import com.android.sdklib.xml.AndroidManifest;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
//Synthetic comment -- @@ -200,6 +201,13 @@
+ " does not exist or is not a regular file", null));
}
}

                // get the proguard file output by aapt
                if (proguardConfigFiles != null) {
                    IFolder androidOutputFolder = BaseProjectHelper.getAndroidOutputFolder(project);
                    IFile proguardFile = androidOutputFolder.getFile(AdtConstants.FN_AAPT_PROGUARD);
                    proguardConfigFiles.add(proguardFile.getLocation().toFile());
                }
}

Collection<String> dxInput;







