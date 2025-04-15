/*New library project mechanism.

- When libraries build, create a jar file containing
  all the classes (except the R classes) and the java
  resources. This will be used by the main project.

Change-Id:Ib909efbb20e30c6eeb1619a4bf3c70eeeb5e2a5d*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 545d0f9..b609dbc 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import com.android.sdklib.build.ApkBuilder.SigningInfo;
import com.android.sdklib.build.ApkCreationException;
import com.android.sdklib.build.DuplicateFileException;
import com.android.sdklib.build.SealedApkException;
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;
//Synthetic comment -- @@ -126,6 +127,11 @@
mVerbose = verbose;
}

/**
* Packages the resources of the projet into a .ap_ file.
* @param manifestFile the manifest of the project.
//Synthetic comment -- @@ -721,7 +727,7 @@
* Writes the standard resources of a project and its referenced projects
* into a {@link SignedJarBuilder}.
* Standard resources are non java/aidl files placed in the java package folders.
     * @param apkBuilder the {@link ApkBuilder}.
* @param javaProject the javaProject object.
* @param referencedJavaProjects the java projects that this project references.
* @throws ApkCreationException if an error occurred
//Synthetic comment -- @@ -730,25 +736,24 @@
*                                   at the same location inside the APK archive.
* @throws CoreException
*/
    private void writeStandardResources(ApkBuilder apkBuilder, IJavaProject javaProject,
List<IJavaProject> referencedJavaProjects)
throws DuplicateFileException, ApkCreationException, SealedApkException,
CoreException  {
IWorkspace ws = ResourcesPlugin.getWorkspace();
IWorkspaceRoot wsRoot = ws.getRoot();

        // create a list of path already put into the archive, in order to detect conflict
        ArrayList<String> list = new ArrayList<String>();

        writeStandardProjectResources(apkBuilder, javaProject, wsRoot, list);

        for (IJavaProject referencedJavaProject : referencedJavaProjects) {
            // only include output from non android referenced project
            // (This is to handle the case of reference Android projects in the context of
            // instrumentation projects that need to reference the projects to be tested).
            if (referencedJavaProject.getProject().hasNature(
                    AndroidConstants.NATURE_DEFAULT) == false) {
                writeStandardProjectResources(apkBuilder, referencedJavaProject, wsRoot, list);
}
}
}
//Synthetic comment -- @@ -759,15 +764,14 @@
* @param jarBuilder the {@link ApkBuilder}.
* @param javaProject the javaProject object.
* @param wsRoot the {@link IWorkspaceRoot}.
     * @param list a list of files already added to the archive, to detect conflicts.
* @throws ApkCreationException if an error occurred
* @throws SealedApkException if the APK is already sealed.
* @throws DuplicateFileException if a file conflicts with another already added to the APK
*                                   at the same location inside the APK archive.
* @throws CoreException
*/
    private void writeStandardProjectResources(ApkBuilder apkBuilder,
            IJavaProject javaProject, IWorkspaceRoot wsRoot, ArrayList<String> list)
throws DuplicateFileException, ApkCreationException, SealedApkException, CoreException {
// get the source pathes
List<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(javaProject);
//Synthetic comment -- @@ -776,14 +780,14 @@
for (IPath sourcePath : sourceFolders) {
IResource sourceResource = wsRoot.findMember(sourcePath);
if (sourceResource != null && sourceResource.getType() == IResource.FOLDER) {
                writeFolderResources(apkBuilder, javaProject, (IFolder) sourceResource);
}
}
}

    private void writeFolderResources(ApkBuilder apkBuilder, final IJavaProject javaProject,
            IFolder root) throws CoreException, ApkCreationException,
            SealedApkException, DuplicateFileException {
final List<IPath> pathsToPackage = new ArrayList<IPath>();
root.accept(new IResourceProxyVisitor() {
public boolean visit(IResourceProxy proxy) throws CoreException {
//Synthetic comment -- @@ -809,7 +813,7 @@
IPath rootLocation = root.getLocation();
for (IPath path : pathsToPackage) {
IPath archivePath = path.makeRelativeTo(rootLocation);
            apkBuilder.addFile(path.toFile(), archivePath.toString());
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 1d3d13b..4574088 100644

//Synthetic comment -- @@ -38,6 +38,8 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.build.ApkCreationException;
import com.android.sdklib.build.DuplicateFileException;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;

import org.eclipse.core.resources.IContainer;
//Synthetic comment -- @@ -58,9 +60,18 @@
import org.eclipse.jdt.core.JavaModelException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostCompilerBuilder extends BaseBuilder {

//Synthetic comment -- @@ -215,11 +226,14 @@
try {
// get the project info
ProjectState projectState = Sdk.getProjectState(project);
            if (projectState == null || projectState.isLibrary()) {
                // library project do not need to be dexified or packaged.
return null;
}

// get the libraries
List<IProject> libProjects = projectState.getFullLibraryProjects();

//Synthetic comment -- @@ -244,6 +258,22 @@
// separator
IFolder outputFolder = BaseProjectHelper.getOutputFolder(project);

// now we need to get the classpath list
List<IPath> sourceList = BaseProjectHelper.getSourceClasspaths(javaProject);

//Synthetic comment -- @@ -254,6 +284,7 @@
AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, project,
Messages.Start_Full_Apk_Build);

mPackageResources = true;
mConvertToDex = true;
mBuildFinalPackage = true;
//Synthetic comment -- @@ -264,6 +295,7 @@
// go through the resources and see if something changed.
IResourceDelta delta = getDelta(project);
if (delta == null) {
mPackageResources = true;
mConvertToDex = true;
mBuildFinalPackage = true;
//Synthetic comment -- @@ -624,6 +656,126 @@
return allRefProjects;
}

@Override
protected void startupOnInitialize() {
super.startupOnInitialize();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index fd3a07d..c101109 100644

//Synthetic comment -- @@ -490,7 +490,8 @@
// generate resources.
boolean compiledTheResources = mMustCompileResources;
if (mMustCompileResources) {
                handleResources(project, javaPackage, projectTarget, manifestFile, libProjects);
saveProjectBooleanProperty(PROPERTY_COMPILE_RESOURCES , false);
}

//Synthetic comment -- @@ -570,11 +571,13 @@
* @param projectTarget the target of the main project
* @param manifest the {@link IFile} representing the project manifest
* @param libProjects the library dependencies
* @throws CoreException
* @throws AbortBuildException
*/
private void handleResources(IProject project, String javaPackage, IAndroidTarget projectTarget,
            IFile manifest, List<IProject> libProjects) throws CoreException, AbortBuildException {
// get the resource folder
IFolder resFolder = project.getFolder(AndroidConstants.WS_RESOURCES);

//Synthetic comment -- @@ -624,7 +627,7 @@
}

execAapt(project, projectTarget, osOutputPath, osResPath, osManifestPath,
                    mainPackageFolder, libResFolders, null /* custom java package */);

final int count = libOutputFolders.size();
if (count > 0) {
//Synthetic comment -- @@ -632,7 +635,7 @@
IFolder libFolder = libOutputFolders.get(i);
String libJavaPackage = libJavaPackages.get(i);
execAapt(project, projectTarget, osOutputPath, osResPath, osManifestPath,
                            libFolder, libResFolders, libJavaPackage);
}
}
}
//Synthetic comment -- @@ -653,11 +656,13 @@
* @param libResFolders the list of res folders for the library.
* @param customJavaPackage an optional javapackage to replace the main project java package.
* can be null.
* @throws AbortBuildException
*/
private void execAapt(IProject project, IAndroidTarget projectTarget, String osOutputPath,
String osResPath, String osManifestPath, IFolder packageFolder,
            ArrayList<IFolder> libResFolders, String customJavaPackage) throws AbortBuildException {
// We actually need to delete the manifest.java as it may become empty and
// in this case aapt doesn't generate an empty one, but instead doesn't
// touch it.
//Synthetic comment -- @@ -673,6 +678,10 @@
array.add("-v"); //$NON-NLS-1$
}

if (libResFolders.size() > 0) {
array.add("--auto-add-overlay"); //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java
//Synthetic comment -- index c066dff..3438f4b 100644

//Synthetic comment -- @@ -47,7 +47,7 @@
* - Native libraries from the project or its library.
*
*/
public final class ApkBuilder {

private final static Pattern PATTERN_NATIVELIB_EXT = Pattern.compile("^.+\\.so$",
Pattern.CASE_INSENSITIVE);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/IArchiveBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/IArchiveBuilder.java
new file mode 100644
//Synthetic comment -- index 0000000..e2230e9

//Synthetic comment -- @@ -0,0 +1,35 @@







