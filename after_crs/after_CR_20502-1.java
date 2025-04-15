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
            List<IProject> libProjects = projectState.getFullLibraryProjects();

IProject project = projectState.getProject();
IJavaProject javaProject = JavaCore.create(project);
//Synthetic comment -- @@ -294,8 +294,8 @@
String outputFile = binFolder.getFile(outputName).getLocation().toOSString();

// get the list of referenced projects.
            List<IProject> javaRefs = ProjectHelper.getReferencedProjects(project);
            List<IJavaProject> referencedJavaProjects = BuildHelper.getJavaProjects(javaRefs);

helper.finalPackage(
new File(projectBinFolderPath, pkgName).getAbsolutePath(),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index e582a91..b75e831 100644

//Synthetic comment -- @@ -137,7 +137,7 @@
* @throws AaptExecException
* @throws AaptResultException
*/
    public void packageResources(IFile manifestFile, List<IProject> libProjects, String resFilter,
int versionCode, String outputFolder, String outputFilename)
throws AaptExecException, AaptResultException {
// need to figure out some path before we can execute aapt;
//Synthetic comment -- @@ -208,8 +208,8 @@
* @throws DuplicateFileException
*/
public void finalDebugPackage(String intermediateApk, String dex, String output,
            final IJavaProject javaProject, List<IProject> libProjects,
            List<IJavaProject> referencedJavaProjects, ResourceMarker resMarker)
throws ApkCreationException, KeytoolException, AndroidLocationException,
NativeLibInJarException, DuplicateFileException, CoreException {

//Synthetic comment -- @@ -262,8 +262,8 @@
* @throws DuplicateFileException
*/
public void finalPackage(String intermediateApk, String dex, String output,
            final IJavaProject javaProject, List<IProject> libProjects,
            List<IJavaProject> referencedJavaProjects, String abiFilter, PrivateKey key,
X509Certificate certificate, ResourceMarker resMarker)
throws NativeLibInJarException, ApkCreationException, DuplicateFileException,
CoreException {
//Synthetic comment -- @@ -358,14 +358,16 @@
IFolder outputFolder = BaseProjectHelper.getOutputFolder(mProject);

// get the list of referenced projects output to add
        List<IProject> javaProjects = ProjectHelper.getReferencedProjects(mProject);
        List<IJavaProject> referencedJavaProjects = BuildHelper.getJavaProjects(javaProjects);
        List<String> projectOutputs = getProjectOutputs(referencedJavaProjects);

        String[] outputs = new String[1 + projectOutputs.size()];

outputs[0] = outputFolder.getLocation().toOSString();

        String[] array = projectOutputs.toArray(new String[projectOutputs.size()]);
        System.arraycopy(array, 0, outputs, 1, projectOutputs.size());

return outputs;
}
//Synthetic comment -- @@ -721,7 +723,7 @@
* @throws CoreException
*/
private void writeStandardResources(ApkBuilder apkBuilder, IJavaProject javaProject,
            List<IJavaProject> referencedJavaProjects)
throws DuplicateFileException, ApkCreationException, SealedApkException,
CoreException  {
IWorkspace ws = ResourcesPlugin.getWorkspace();
//Synthetic comment -- @@ -834,10 +836,11 @@
* they are Android projects.
*
* @param referencedJavaProjects the java projects.
     * @return a new list object containing the output folder paths.
* @throws CoreException
*/
    private List<String> getProjectOutputs(List<IJavaProject> referencedJavaProjects)
            throws CoreException {
ArrayList<String> list = new ArrayList<String>();

IWorkspace ws = ResourcesPlugin.getWorkspace();
//Synthetic comment -- @@ -865,7 +868,7 @@
}
}

        return list;
}

/**
//Synthetic comment -- @@ -891,12 +894,12 @@
}

/**
     * Returns a list of {@link IJavaProject} matching the provided {@link IProject} objects.
* @param projects the IProject objects.
     * @return a new list object containing the IJavaProject object for the given IProject objects.
* @throws CoreException
*/
    public static List<IJavaProject> getJavaProjects(List<IProject> projects) throws CoreException {
ArrayList<IJavaProject> list = new ArrayList<IJavaProject>();

for (IProject p : projects) {
//Synthetic comment -- @@ -906,7 +909,7 @@
}
}

        return list;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 4fe237f..a4f5ebc 100644

//Synthetic comment -- @@ -58,6 +58,7 @@

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostCompilerBuilder extends BaseBuilder {
//Synthetic comment -- @@ -207,8 +208,7 @@
IProject project = getProject();

// list of referenced projects.
        List<IProject> libProjects = null;
IProject[] allRefProjects = null;

try {
//Synthetic comment -- @@ -228,20 +228,14 @@
abortOnBadSetup(javaProject);

// get the list of referenced projects.
            List<IProject> javaProjects = ProjectHelper.getReferencedProjects(project);
            List<IJavaProject> referencedJavaProjects = BuildHelper.getJavaProjects(
javaProjects);

// mix the java project and the library projects
            final int size = libProjects.size() + javaProjects.size();
            ArrayList<IProject> refList = new ArrayList<IProject>(size);
            allRefProjects = refList.toArray(new IProject[size]);

// get the output folder, this method returns the path with a trailing
// separator
//Synthetic comment -- @@ -283,7 +277,7 @@
// if the main resources didn't change, then we check for the library
// ones (will trigger resource repackaging too)
if ((mPackageResources == false || mBuildFinalPackage == false) &&
                        libProjects.size() > 0) {
for (IProject libProject : libProjects) {
delta = getDelta(libProject);
if (delta != null) {
//Synthetic comment -- @@ -302,9 +296,10 @@

// also go through the delta for all the referenced projects, until we are forced to
// compile anyway
                final int referencedCount = referencedJavaProjects.size();
                for (int i = 0 ; i < referencedCount &&
(mBuildFinalPackage == false || mConvertToDex == false); i++) {
                    IJavaProject referencedJavaProject = referencedJavaProjects.get(i);
delta = getDelta(referencedJavaProject.getProject());
if (delta != null) {
ReferencedProjectDeltaVisitor refProjectDv =








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index acec9b1..7be2ad0 100644

//Synthetic comment -- @@ -56,6 +56,7 @@

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Synthetic comment -- @@ -219,7 +220,7 @@
IProject project = getProject();

// list of referenced projects.
        List<IProject> libProjects = null;

try {
mDerivedProgressMonitor.reset();
//Synthetic comment -- @@ -292,7 +293,7 @@

// if the main resources didn't change, then we check for the library
// ones (will trigger resource recompilation too)
                    if (mMustCompileResources == false && libProjects.size() > 0) {
for (IProject libProject : libProjects) {
delta = getDelta(libProject);
if (delta != null) {
//Synthetic comment -- @@ -516,7 +517,7 @@
mGenFolder.refreshLocal(IResource.DEPTH_INFINITE, mDerivedProgressMonitor);
}

        return libProjects.toArray(new IProject[libProjects.size()]);
}

@Override
//Synthetic comment -- @@ -585,7 +586,7 @@
* @throws CoreException
*/
private void handleResources(IProject project, String javaPackage, IAndroidTarget projectTarget,
            IFile manifest, List<IProject> libProjects) throws CoreException {
// get the resource folder
IFolder resFolder = project.getFolder(AndroidConstants.WS_RESOURCES);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java
//Synthetic comment -- index ca58dce..4f44a28 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

import java.util.ArrayList;
import java.util.HashMap;
//Synthetic comment -- @@ -166,9 +167,46 @@
* This records the changes for each project, but does not notify listeners.
*/
public void fileChanged(IFile file, IMarkerDelta[] markerDeltas, int kind) {
            // get the file's project
IProject project = file.getProject();

            boolean hasAndroidNature = false;
            try {
                hasAndroidNature = project.hasNature(AndroidConstants.NATURE_DEFAULT);
            } catch (CoreException e) {
                // do nothing if the nature cannot be queried.
                return;
            }

            if (hasAndroidNature) {
                processFileChanged(file, project);
            } else {
                // check the project depending on it, if they are Android project, update them.
                IProject[] referencingProjects = project.getReferencingProjects();

                for (IProject p : referencingProjects) {
                    try {
                        hasAndroidNature = p.hasNature(AndroidConstants.NATURE_DEFAULT);
                    } catch (CoreException e) {
                        // do nothing if the nature cannot be queried.
                        continue;
                    }

                    if (hasAndroidNature) {
                        // the changed project is a dependency on an Android project,
                        // update the main project.
                        processFileChanged(file, p);
                    }
                }
            }
        }

        /**
         * Processes a file change for a given project which may or may not be the file's project.
         * @param file the changed file
         * @param project the project impacted by the file change.
         */
        private void processFileChanged(IFile file, IProject project) {
// if this project has already been marked as modified, we do nothing.
ChangeFlags changeFlags = mProjectFlags.get(project);
if (changeFlags != null && changeFlags.isAllTrue()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index e1c6584..e7cbaea 100644

//Synthetic comment -- @@ -53,6 +53,13 @@
private ProjectClassLoader mLoader = null;
private LayoutLog mLogger;

    /**
     * Creates a new {@link ProjectCallback} to be used with the layout lib.
     *
     * @param classLoader The class loader that was used to load layoutlib.jar
     * @param projectRes the {@link ProjectResources} for the project.
     * @param project the project.
     */
public ProjectCallback(ClassLoader classLoader, ProjectResources projectRes, IProject project) {
mParentClassLoader = classLoader;
mProjectRes = projectRes;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index 17fc1e2..ddb4321 100644

//Synthetic comment -- @@ -60,6 +60,7 @@
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

//Synthetic comment -- @@ -117,7 +118,7 @@

// get the list of library projects
ProjectState projectState = Sdk.getProjectState(project);
            List<IProject> libProjects = projectState.getFullLibraryProjects();

// Step 1. Package the resources.

//Synthetic comment -- @@ -196,8 +197,8 @@
}

IJavaProject javaProject = JavaCore.create(project);
            List<IProject> javaProjects = ProjectHelper.getReferencedProjects(project);
            List<IJavaProject> referencedJavaProjects = BuildHelper.getJavaProjects(
javaProjects);

helper.executeDx(javaProject, dxInput, dexFile.getAbsolutePath());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java
//Synthetic comment -- index 01479bf..91e2380 100644

//Synthetic comment -- @@ -535,7 +535,7 @@

// test the referenced projects if needed.
if (includeReferencedProjects) {
            List<IProject> projects = getReferencedProjects(project);

for (IProject p : projects) {
if (hasError(p, false)) {
//Synthetic comment -- @@ -651,10 +651,10 @@
/**
* Returns the list of referenced project that are opened and Java projects.
* @param project
     * @return a new list object containing the opened referenced java project.
* @throws CoreException
*/
    public static List<IProject> getReferencedProjects(IProject project) throws CoreException {
IProject[] projects = project.getReferencedProjects();

ArrayList<IProject> list = new ArrayList<IProject>();
//Synthetic comment -- @@ -665,7 +665,7 @@
}
}

        return list;
}










//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectClassLoader.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectClassLoader.java
//Synthetic comment -- index 5244618..db33f2a 100644

//Synthetic comment -- @@ -17,11 +17,14 @@
package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
//Synthetic comment -- @@ -35,6 +38,7 @@
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
* ClassLoader able to load class from output of an Eclipse project.
//Synthetic comment -- @@ -52,30 +56,71 @@

@Override
protected Class<?> findClass(String name) throws ClassNotFoundException {
        // if we are here through a child classloader, throw an exception.
        if (mInsideJarClassLoader) {
            throw new ClassNotFoundException(name);
        }

        // attempt to load the class from the main project
        Class<?> clazz = loadFromProject(mJavaProject, name);

        if (clazz != null) {
            return clazz;
        }

        // attempt to load the class from the jar dependencies
        clazz = loadClassFromJar(name);
        if (clazz != null) {
            return clazz;
        }

        // attempt to load the class from the referenced projects.
        try {
            List<IProject> javaProjects = ProjectHelper.getReferencedProjects(
                    mJavaProject.getProject());
            List<IJavaProject> referencedJavaProjects = BuildHelper.getJavaProjects(javaProjects);

            for (IJavaProject javaProject : referencedJavaProjects) {
                clazz = loadFromProject(javaProject, name);

                if (clazz != null) {
                    return clazz;
                }
            }
        } catch (CoreException e) {
            // log exception?
        }

        throw new ClassNotFoundException(name);
    }

    /**
     * Attempts to load a class from a project output folder.
     * @param project the project to load the class from
     * @param name the name of the class
     * @return a class object if found, null otherwise.
     */
    private Class<?> loadFromProject(IJavaProject project, String name) {
try {
// get the project output folder.
IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IPath outputLocation = project.getOutputLocation();
IResource outRes = root.findMember(outputLocation);
if (outRes == null) {
                return null;
}

File outFolder = new File(outRes.getLocation().toOSString());

// get the class name segments
String[] segments = name.split("\\."); //$NON-NLS-1$

            // try to load the class from the bin folder of the project.
File classFile = getFile(outFolder, segments, 0);
if (classFile == null) {
                return null;
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
            // log the exception?
}

        return null;
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
//Synthetic comment -- @@ -151,20 +196,20 @@
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
//Synthetic comment -- @@ -173,21 +218,23 @@
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
        } catch (ClassNotFoundException e) {
            return null;
} finally {
mInsideJarClassLoader = false;
}
}

/**
* Returns an array of external jar files used by the project.
* @return an array of OS-specific absolute file paths
//Synthetic comment -- @@ -195,7 +242,7 @@
private final URL[] getExternalJars() {
// get a java project from it
IJavaProject javaProject = JavaCore.create(mJavaProject.getProject());

IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();

ArrayList<URL> oslibraryList = new ArrayList<URL>();
//Synthetic comment -- @@ -206,7 +253,7 @@
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
                List<IProject> libraries = state.getFullLibraryProjects();

ResourceManager resMgr = ResourceManager.getInstance();

//Synthetic comment -- @@ -426,8 +426,8 @@
// one will have priority over the 2nd one. So it's better to loop in the inverse
// order and fill the map with resources that will be overwritten by higher
// priority resources
                for (int i = libraries.size() - 1 ; i >= 0 ; i--) {
                    IProject library = libraries.get(i);

ProjectResources libRes = resMgr.getProjectResources(library);
if (libRes != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/ProjectState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/ProjectState.java
//Synthetic comment -- index dd09355..0d58201 100644

//Synthetic comment -- @@ -165,7 +165,7 @@
private final ArrayList<LibraryState> mLibraries = new ArrayList<LibraryState>();
/** Cached list of all IProject instances representing the resolved libraries, including
* indirect dependencies. This must never be null. */
    private List<IProject> mLibraryProjects = Collections.emptyList();
/**
* List of parent projects. When this instance is a library ({@link #isLibrary()} returns
* <code>true</code>) then this is filled with projects that depends on this project.
//Synthetic comment -- @@ -330,13 +330,13 @@

/**
* Returns all the <strong>resolved</strong> library projects, including indirect dependencies.
     * The list is ordered to match the library priority order for resource processing with
* <code>aapt</code>.
* <p/>If some dependencies are not resolved (or their projects is not opened in Eclipse),
* they will not show up in this list.
     * @return the resolved projects as an unmodifiable list. May be an empty.
*/
    public List<IProject> getFullLibraryProjects() {
return mLibraryProjects;
}

//Synthetic comment -- @@ -608,7 +608,7 @@
buildFullLibraryDependencies(mLibraries, list);
}

        mLibraryProjects = Collections.unmodifiableList(list);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index c13cc73..a7e8647 100644

//Synthetic comment -- @@ -781,7 +781,7 @@
LibraryState libState = projectState.getLibrary(project);
if (libState != null) {
// get the current libraries.
                            List<IProject> oldLibraries = projectState.getFullLibraryProjects();

// the unlink below will work in the job, but we need to close
// the library right away.
//Synthetic comment -- @@ -924,7 +924,7 @@
projectState.getProject().getFullPath());

// get the current libraries
                            List<IProject> oldLibraries = projectState.getFullLibraryProjects();

// update the library for the main project.
LibraryState libState = projectState.updateLibrary(
//Synthetic comment -- @@ -977,7 +977,7 @@
boolean wasLibrary = state.isLibrary();

// get the current list of project dependencies
                    List<IProject> oldLibraries = state.getFullLibraryProjects();

LibraryDifference diff = state.reloadProperties();

//Synthetic comment -- @@ -1511,9 +1511,9 @@
*            {@link ProjectState#getFullLibraryProjects()} before the ProjectState is updated.
* @return null if there no action to take, or a {@link LinkUpdateBundle} object to run.
*/
    private LinkUpdateBundle getLinkBundle(ProjectState project, List<IProject> oldLibraries) {
// get the new full list of projects
        List<IProject> newLibraries = project.getFullLibraryProjects();

// and build the real difference. A list of new projects and a list of
// removed project.
//Synthetic comment -- @@ -1634,7 +1634,7 @@
// we add them to the list so that can be updated as well.
for (ProjectState projectState : sProjectStateMap.values()) {
// record the current library dependencies
                List<IProject> oldLibraries = projectState.getFullLibraryProjects();

boolean needLibraryDependenciesUpdated = false;
for (ProjectState library : libraries) {







