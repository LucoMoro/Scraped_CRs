/*Look for classes in java projects when loading custom views.

Even if Java projects can't really contain custom views,
they can contain classes on which the custom views depend.
The ProjectClassLoader now handles these projects.

Changed the way the LayoutReloadMonitor deals with projects
to handle referenced projects.

Also fixed some API returning arrays to make them return lists.http://code.google.com/p/android/issues/detail?id=13010Change-Id:I7f9da9d5289a5f2735c2cf3638bedc1efbd0c71a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/MultiApkExportAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/MultiApkExportAction.java
//Synthetic comment -- index da57a4f..10ec15f 100644

//Synthetic comment -- @@ -254,7 +254,7 @@
throws CoreException {
try {
// get the libraries for this project
            IProject[] libProjects = projectState.getFullLibraryProjects();

IProject project = projectState.getProject();
IJavaProject javaProject = JavaCore.create(project);
//Synthetic comment -- @@ -294,8 +294,8 @@
String outputFile = binFolder.getFile(outputName).getLocation().toOSString();

// get the list of referenced projects.
            IProject[] javaRefs = ProjectHelper.getReferencedProjects(project);
            IJavaProject[] referencedJavaProjects = BuildHelper.getJavaProjects(javaRefs);

helper.finalPackage(
new File(projectBinFolderPath, pkgName).getAbsolutePath(),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index e582a91..b43571a 100644

//Synthetic comment -- @@ -137,7 +137,7 @@
* @throws AaptExecException
* @throws AaptResultException
*/
    public void packageResources(IFile manifestFile, IProject[] libProjects, String resFilter,
int versionCode, String outputFolder, String outputFilename)
throws AaptExecException, AaptResultException {
// need to figure out some path before we can execute aapt;
//Synthetic comment -- @@ -208,8 +208,8 @@
* @throws DuplicateFileException
*/
public void finalDebugPackage(String intermediateApk, String dex, String output,
            final IJavaProject javaProject, IProject[] libProjects,
            IJavaProject[] referencedJavaProjects, ResourceMarker resMarker)
throws ApkCreationException, KeytoolException, AndroidLocationException,
NativeLibInJarException, DuplicateFileException, CoreException {

//Synthetic comment -- @@ -262,8 +262,8 @@
* @throws DuplicateFileException
*/
public void finalPackage(String intermediateApk, String dex, String output,
            final IJavaProject javaProject, IProject[] libProjects,
            IJavaProject[] referencedJavaProjects, String abiFilter, PrivateKey key,
X509Certificate certificate, ResourceMarker resMarker)
throws NativeLibInJarException, ApkCreationException, DuplicateFileException,
CoreException {
//Synthetic comment -- @@ -358,16 +358,16 @@
IFolder outputFolder = BaseProjectHelper.getOutputFolder(mProject);

// get the list of referenced projects output to add
        IProject[] javaProjects = ProjectHelper.getReferencedProjects(mProject);
        IJavaProject[] referencedJavaProjects = BuildHelper.getJavaProjects(javaProjects);
        String[] projectOutputs = getProjectOutputs(referencedJavaProjects);

        String[] outputs = new String[1 + projectOutputs.length];

        outputs[0] = outputFolder.getLocation().toOSString();
        System.arraycopy(projectOutputs, 0, outputs, 1, projectOutputs.length);

        return outputs;
}

/**
//Synthetic comment -- @@ -721,7 +721,7 @@
* @throws CoreException
*/
private void writeStandardResources(ApkBuilder apkBuilder, IJavaProject javaProject,
            IJavaProject[] referencedJavaProjects)
throws DuplicateFileException, ApkCreationException, SealedApkException,
CoreException  {
IWorkspace ws = ResourcesPlugin.getWorkspace();
//Synthetic comment -- @@ -834,10 +834,11 @@
* they are Android projects.
*
* @param referencedJavaProjects the java projects.
     * @return an array, always. Can be empty.
* @throws CoreException
*/
    private String[] getProjectOutputs(IJavaProject[] referencedJavaProjects) throws CoreException {
ArrayList<String> list = new ArrayList<String>();

IWorkspace ws = ResourcesPlugin.getWorkspace();
//Synthetic comment -- @@ -865,7 +866,7 @@
}
}

        return list.toArray(new String[list.size()]);
}

/**
//Synthetic comment -- @@ -891,12 +892,12 @@
}

/**
     * Returns an array of {@link IJavaProject} matching the provided {@link IProject} objects.
* @param projects the IProject objects.
     * @return an array, always. Can be empty.
* @throws CoreException
*/
    public static IJavaProject[] getJavaProjects(IProject[] projects) throws CoreException {
ArrayList<IJavaProject> list = new ArrayList<IJavaProject>();

for (IProject p : projects) {
//Synthetic comment -- @@ -906,7 +907,7 @@
}
}

        return list.toArray(new IJavaProject[list.size()]);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 4fe237f..ac48739 100644

//Synthetic comment -- @@ -58,6 +58,7 @@

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class PostCompilerBuilder extends BaseBuilder {
//Synthetic comment -- @@ -206,9 +207,8 @@
// get a project object
IProject project = getProject();

        // list of referenced projects.
        IProject[] libProjects = null;
        IProject[] javaProjects = null;
IProject[] allRefProjects = null;

try {
//Synthetic comment -- @@ -220,7 +220,7 @@
}

// get the libraries
            libProjects = projectState.getFullLibraryProjects();

IJavaProject javaProject = JavaCore.create(project);

//Synthetic comment -- @@ -228,20 +228,16 @@
abortOnBadSetup(javaProject);

// get the list of referenced projects.
            javaProjects = ProjectHelper.getReferencedProjects(project);
            IJavaProject[] referencedJavaProjects = BuildHelper.getJavaProjects(
javaProjects);

// mix the java project and the library projects
            final int libCount = libProjects.length;
            final int javaCount = javaProjects != null ? javaProjects.length : 0;
            allRefProjects = new IProject[libCount + javaCount];
            if (libCount > 0) {
                System.arraycopy(libProjects, 0, allRefProjects, 0, libCount);
            }
            if (javaCount > 0) {
                System.arraycopy(javaProjects, 0, allRefProjects, libCount, javaCount);
            }

// get the output folder, this method returns the path with a trailing
// separator
//Synthetic comment -- @@ -283,7 +279,7 @@
// if the main resources didn't change, then we check for the library
// ones (will trigger resource repackaging too)
if ((mPackageResources == false || mBuildFinalPackage == false) &&
                        libProjects.length > 0) {
for (IProject libProject : libProjects) {
delta = getDelta(libProject);
if (delta != null) {
//Synthetic comment -- @@ -302,9 +298,10 @@

// also go through the delta for all the referenced projects, until we are forced to
// compile anyway
                for (int i = 0 ; i < referencedJavaProjects.length &&
(mBuildFinalPackage == false || mConvertToDex == false); i++) {
                    IJavaProject referencedJavaProject = referencedJavaProjects[i];
delta = getDelta(referencedJavaProject.getProject());
if (delta != null) {
ReferencedProjectDeltaVisitor refProjectDv =








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index acec9b1..dc3bf9e 100644

//Synthetic comment -- @@ -56,6 +56,7 @@

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Synthetic comment -- @@ -218,8 +219,9 @@
// get a project object
IProject project = getProject();

        // list of referenced projects.
        IProject[] libProjects = null;

try {
mDerivedProgressMonitor.reset();
//Synthetic comment -- @@ -292,7 +294,7 @@

// if the main resources didn't change, then we check for the library
// ones (will trigger resource recompilation too)
                    if (mMustCompileResources == false && libProjects.length > 0) {
for (IProject libProject : libProjects) {
delta = getDelta(libProject);
if (delta != null) {
//Synthetic comment -- @@ -516,7 +518,7 @@
mGenFolder.refreshLocal(IResource.DEPTH_INFINITE, mDerivedProgressMonitor);
}

        return libProjects;
}

@Override
//Synthetic comment -- @@ -585,7 +587,7 @@
* @throws CoreException
*/
private void handleResources(IProject project, String javaPackage, IAndroidTarget projectTarget,
            IFile manifest, IProject[] libProjects) throws CoreException {
// get the resource folder
IFolder resFolder = project.getFolder(AndroidConstants.WS_RESOURCES);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java
//Synthetic comment -- index ca58dce..48d6cb4 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;

import java.util.ArrayList;
import java.util.HashMap;
//Synthetic comment -- @@ -166,9 +167,50 @@
* This records the changes for each project, but does not notify listeners.
*/
public void fileChanged(IFile file, IMarkerDelta[] markerDeltas, int kind) {
            // get the file project
IProject project = file.getProject();

// if this project has already been marked as modified, we do nothing.
ChangeFlags changeFlags = mProjectFlags.get(project);
if (changeFlags != null && changeFlags.isAllTrue()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index e1c6584..e7cbaea 100644

//Synthetic comment -- @@ -53,6 +53,13 @@
private ProjectClassLoader mLoader = null;
private LayoutLog mLogger;

public ProjectCallback(ClassLoader classLoader, ProjectResources projectRes, IProject project) {
mParentClassLoader = classLoader;
mProjectRes = projectRes;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index 17fc1e2..ddb4321 100644

//Synthetic comment -- @@ -60,6 +60,7 @@
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

//Synthetic comment -- @@ -117,7 +118,7 @@

// get the list of library projects
ProjectState projectState = Sdk.getProjectState(project);
            IProject[] libProjects = projectState.getFullLibraryProjects();

// Step 1. Package the resources.

//Synthetic comment -- @@ -196,8 +197,8 @@
}

IJavaProject javaProject = JavaCore.create(project);
            IProject[] javaProjects = ProjectHelper.getReferencedProjects(project);
            IJavaProject[] referencedJavaProjects = BuildHelper.getJavaProjects(
javaProjects);

helper.executeDx(javaProject, dxInput, dexFile.getAbsolutePath());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java
//Synthetic comment -- index 01479bf..91e2380 100644

//Synthetic comment -- @@ -535,7 +535,7 @@

// test the referenced projects if needed.
if (includeReferencedProjects) {
            IProject[] projects = getReferencedProjects(project);

for (IProject p : projects) {
if (hasError(p, false)) {
//Synthetic comment -- @@ -651,10 +651,10 @@
/**
* Returns the list of referenced project that are opened and Java projects.
* @param project
     * @return list of opened referenced java project.
* @throws CoreException
*/
    public static IProject[] getReferencedProjects(IProject project) throws CoreException {
IProject[] projects = project.getReferencedProjects();

ArrayList<IProject> list = new ArrayList<IProject>();
//Synthetic comment -- @@ -665,7 +665,7 @@
}
}

        return list.toArray(new IProject[list.size()]);
}










//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectClassLoader.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectClassLoader.java
//Synthetic comment -- index 5244618..0deb89c 100644

//Synthetic comment -- @@ -17,11 +17,14 @@
package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.AndroidConstants;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
//Synthetic comment -- @@ -35,6 +38,7 @@
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

/**
* ClassLoader able to load class from output of an Eclipse project.
//Synthetic comment -- @@ -52,30 +56,71 @@

@Override
protected Class<?> findClass(String name) throws ClassNotFoundException {
try {
// get the project output folder.
IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IPath outputLocation = mJavaProject.getOutputLocation();
IResource outRes = root.findMember(outputLocation);
if (outRes == null) {
                throw new ClassNotFoundException(name);
}

File outFolder = new File(outRes.getLocation().toOSString());

// get the class name segments
String[] segments = name.split("\\."); //$NON-NLS-1$
            
File classFile = getFile(outFolder, segments, 0);
if (classFile == null) {
                if (mInsideJarClassLoader == false) {
                    // if no file matching the class name was found, look in the 3rd party jars
                    return loadClassFromJar(name);
                } else {
                    throw new ClassNotFoundException(name);
                }
}
            
// load the content of the file and create the class.
FileInputStream fis = new FileInputStream(classFile);
byte[] data = new byte[(int)classFile.length()];
//Synthetic comment -- @@ -86,7 +131,7 @@
data = null;
}
fis.close();
            
if (data != null) {
Class<?> clazz = defineClass(null, data, 0, read);
if (clazz != null) {
//Synthetic comment -- @@ -94,12 +139,12 @@
}
}
} catch (Exception e) {
            throw new ClassNotFoundException(e.getMessage());
}

        throw new ClassNotFoundException(name);
}
    
/**
* Returns the File matching the a certain path from a root {@link File}.
* <p/>The methods checks that the file ends in .class even though the last segment
//Synthetic comment -- @@ -121,7 +166,7 @@

// we're at the last segments. we look for a matching <file>.class
if (index == segments.length - 1) {
            toMatch = toMatch + ".class"; 

if (files != null) {
for (File file : files) {
//Synthetic comment -- @@ -130,13 +175,13 @@
}
}
}
            
// no match? abort.
throw new FileNotFoundException();
}
        
String innerClassName = null;
        
if (files != null) {
for (File file : files) {
if (file.isDirectory()) {
//Synthetic comment -- @@ -151,43 +196,47 @@
sb.append(segments[i]);
}
sb.append(".class");
                        
innerClassName = sb.toString();
}
                    
if (file.getName().equals(innerClassName)) {
return file;
}
}
}
}
        
return null;
}
    
/**
* Loads a class from the 3rd party jar present in the project
     * @throws ClassNotFoundException
*/
    private Class<?> loadClassFromJar(String name) throws ClassNotFoundException {
if (mJarClassLoader == null) {
// get the OS path to all the external jars
URL[] jars = getExternalJars();
            
mJarClassLoader = new URLClassLoader(jars, this /* parent */);
}
        
try {
// because a class loader always look in its parent loader first, we need to know
// that we are querying the jar classloader. This will let us know to not query
// it again for classes we don't find, or this would create an infinite loop.
mInsideJarClassLoader = true;
return mJarClassLoader.loadClass(name);
} finally {
mInsideJarClassLoader = false;
}
}
    
/**
* Returns an array of external jar files used by the project.
* @return an array of OS-specific absolute file paths
//Synthetic comment -- @@ -195,7 +244,7 @@
private final URL[] getExternalJars() {
// get a java project from it
IJavaProject javaProject = JavaCore.create(mJavaProject.getProject());
        
IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();

ArrayList<URL> oslibraryList = new ArrayList<URL>();
//Synthetic comment -- @@ -206,7 +255,7 @@
e.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
// if this is a classpath variable reference, we resolve it.
if (e.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
                        e = JavaCore.getResolvedClasspathEntry(e); 
}

// get the IPath








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index 1209eac..1580906 100644

//Synthetic comment -- @@ -418,7 +418,7 @@
if (mProject != null) {
ProjectState state = Sdk.getProjectState(mProject);
if (state != null) {
                IProject[] libraries = state.getFullLibraryProjects();

ResourceManager resMgr = ResourceManager.getInstance();

//Synthetic comment -- @@ -426,8 +426,8 @@
// one will have priority over the 2nd one. So it's better to loop in the inverse
// order and fill the map with resources that will be overwritten by higher
// priority resources
                for (int i = libraries.length - 1 ; i >= 0 ; i--) {
                    IProject library = libraries[i];

ProjectResources libRes = resMgr.getProjectResources(library);
if (libRes != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/ProjectState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/ProjectState.java
//Synthetic comment -- index dd09355..0d58201 100644

//Synthetic comment -- @@ -165,7 +165,7 @@
private final ArrayList<LibraryState> mLibraries = new ArrayList<LibraryState>();
/** Cached list of all IProject instances representing the resolved libraries, including
* indirect dependencies. This must never be null. */
    private IProject[] mLibraryProjects = new IProject[0];
/**
* List of parent projects. When this instance is a library ({@link #isLibrary()} returns
* <code>true</code>) then this is filled with projects that depends on this project.
//Synthetic comment -- @@ -330,13 +330,13 @@

/**
* Returns all the <strong>resolved</strong> library projects, including indirect dependencies.
     * The array is ordered to match the library priority order for resource processing with
* <code>aapt</code>.
* <p/>If some dependencies are not resolved (or their projects is not opened in Eclipse),
* they will not show up in this list.
     * @return the resolved projects. May be an empty list.
*/
    public IProject[] getFullLibraryProjects() {
return mLibraryProjects;
}

//Synthetic comment -- @@ -608,7 +608,7 @@
buildFullLibraryDependencies(mLibraries, list);
}

        mLibraryProjects = list.toArray(new IProject[list.size()]);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index c13cc73..a7e8647 100644

//Synthetic comment -- @@ -781,7 +781,7 @@
LibraryState libState = projectState.getLibrary(project);
if (libState != null) {
// get the current libraries.
                            IProject[] oldLibraries = projectState.getFullLibraryProjects();

// the unlink below will work in the job, but we need to close
// the library right away.
//Synthetic comment -- @@ -924,7 +924,7 @@
projectState.getProject().getFullPath());

// get the current libraries
                            IProject[] oldLibraries = projectState.getFullLibraryProjects();

// update the library for the main project.
LibraryState libState = projectState.updateLibrary(
//Synthetic comment -- @@ -977,7 +977,7 @@
boolean wasLibrary = state.isLibrary();

// get the current list of project dependencies
                    IProject[] oldLibraries = state.getFullLibraryProjects();

LibraryDifference diff = state.reloadProperties();

//Synthetic comment -- @@ -1511,9 +1511,9 @@
*            {@link ProjectState#getFullLibraryProjects()} before the ProjectState is updated.
* @return null if there no action to take, or a {@link LinkUpdateBundle} object to run.
*/
    private LinkUpdateBundle getLinkBundle(ProjectState project, IProject[] oldLibraries) {
// get the new full list of projects
        IProject[] newLibraries = project.getFullLibraryProjects();

// and build the real difference. A list of new projects and a list of
// removed project.
//Synthetic comment -- @@ -1634,7 +1634,7 @@
// we add them to the list so that can be updated as well.
for (ProjectState projectState : sProjectStateMap.values()) {
// record the current library dependencies
                IProject[] oldLibraries = projectState.getFullLibraryProjects();

boolean needLibraryDependenciesUpdated = false;
for (ProjectState library : libraries) {







