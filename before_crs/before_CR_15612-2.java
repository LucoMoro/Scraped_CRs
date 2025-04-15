/*ADT support for libraries depending on libraries.

Major change in the workflow linking projects and library together.
This is now done through a single job with a queue of action to do.
This ensure that each new opened project is processed one at a time
and not in parallel which would generate problems.

Change-Id:I2a05ada293a21faba65bb639092f77ff7e5ffb2d*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/MultiApkExportAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/MultiApkExportAction.java
//Synthetic comment -- index cce8ac8..f5876e9 100644

//Synthetic comment -- @@ -244,7 +244,7 @@
int versionCode, ApkData apk, Entry<String, String> softVariant, IFolder binFolder)
throws CoreException {
// get the libraries for this project
        IProject[] libProjects = projectState.getLibraryProjects();

IProject project = projectState.getProject();
IJavaProject javaProject = JavaCore.create(project);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PostCompilerBuilder.java
//Synthetic comment -- index ffec6e7..15f960c 100644

//Synthetic comment -- @@ -206,7 +206,7 @@
}

// get the libraries
            libProjects = projectState.getLibraryProjects();

IJavaProject javaProject = JavaCore.create(project);

//Synthetic comment -- @@ -218,7 +218,7 @@
IJavaProject[] referencedJavaProjects = PostCompilerHelper.getJavaProjects(javaProjects);

// mix the java project and the library projects
            final int libCount = libProjects != null ? libProjects.length : 0;
final int javaCount = javaProjects != null ? javaProjects.length : 0;
allRefProjects = new IProject[libCount + javaCount];
if (libCount > 0) {
//Synthetic comment -- @@ -268,7 +268,7 @@
// if the main resources didn't change, then we check for the library
// ones (will trigger resource repackaging too)
if ((mPackageResources == false || mBuildFinalPackage == false) &&
                        libProjects != null && libProjects.length > 0) {
for (IProject libProject : libProjects) {
delta = getDelta(libProject);
if (delta != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerBuilder.java
//Synthetic comment -- index 868ecf7..e3a6715 100644

//Synthetic comment -- @@ -234,7 +234,7 @@
IAndroidTarget projectTarget = projectState.getTarget();

// get the libraries
            libProjects = projectState.getLibraryProjects();

IJavaProject javaProject = JavaCore.create(project);

//Synthetic comment -- @@ -287,8 +287,7 @@

// if the main resources didn't change, then we check for the library
// ones (will trigger resource recompilation too)
                    if (mMustCompileResources == false && libProjects != null &&
                            libProjects.length > 0) {
for (IProject libProject : libProjects) {
delta = getDelta(libProject);
if (delta != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectState.java
//Synthetic comment -- index 93c77f9..5886692 100644

//Synthetic comment -- @@ -42,6 +42,18 @@
* <p>This gives raw access to the properties (from <code>default.properties</code>), as well
* as direct access to target, apksettings and library information.
*
*/
public final class ProjectState {

//Synthetic comment -- @@ -74,13 +86,14 @@
* Closes the library. This resets the IProject from this object ({@link #getProjectState()} will
* return <code>null</code>), and updates the main project data so that the library
* {@link IProject} object does not show up in the return value of
         * {@link ProjectState#getLibraryProjects()}.
*/
public void close() {
mProjectState = null;
mPath = null;

            updateLibraries();
}

private void setRelativePath(String relativePath) {
//Synthetic comment -- @@ -90,8 +103,9 @@
private void setProject(ProjectState project) {
mProjectState = project;
mPath = project.getProject().getLocation().toOSString();

            updateLibraries();
}

/**
//Synthetic comment -- @@ -145,15 +159,22 @@

private final IProject mProject;
private final ProjectProperties mProperties;
/**
* list of libraries. Access to this list must be protected by
* <code>synchronized(mLibraries)</code>, but it is important that such code do not call
* out to other classes (especially those protected by {@link Sdk#getLock()}.)
*/
private final ArrayList<LibraryState> mLibraries = new ArrayList<LibraryState>();
    private IAndroidTarget mTarget;
    private ApkSettings mApkSettings;
    private IProject[] mLibraryProjects;

public ProjectState(IProject project, ProjectProperties properties) {
mProject = project;
//Synthetic comment -- @@ -281,7 +302,7 @@
diff.removed.addAll(oldLibraries);

// update the library with what IProjet are known at the time.
            updateLibraries();
}

return diff;
//Synthetic comment -- @@ -305,13 +326,14 @@
}

/**
     * Convenience method returning all the IProject objects for the resolved libraries.
* <p/>If some dependencies are not resolved (or their projects is not opened in Eclipse),
* they will not show up in this list.
     * @return the resolved projects or null if there are no project (either no resolved or no
     * dependencies)
*/
    public IProject[] getLibraryProjects() {
return mLibraryProjects;
}

//Synthetic comment -- @@ -369,6 +391,15 @@
return null;
}

public LibraryState getLibrary(String name) {
synchronized (mLibraries) {
for (LibraryState state : mLibraries) {
//Synthetic comment -- @@ -424,6 +455,26 @@
}

/**
* Updates a library with a new path.
* <p/>This method acts both as a check and an action. If the project does not depend on the
* given <var>oldRelativePath</var> then no action is done and <code>null</code> is returned.
//Synthetic comment -- @@ -491,6 +542,15 @@
return null;
}

/**
* Saves the default.properties file and refreshes it to make sure that it gets reloaded
* by Eclipse
//Synthetic comment -- @@ -543,20 +603,51 @@
return null;
}

    private void updateLibraries() {
ArrayList<IProject> list = new ArrayList<IProject>();
synchronized (mLibraries) {
            for (LibraryState state : mLibraries) {
                if (state.getProjectState() != null) {
                    list.add(state.getProjectState().getProject());
                }
            }
}

mLibraryProjects = list.toArray(new IProject[list.size()]);
}

/**
* Converts a path containing only / by the proper platform separator.
*/
private String convertPath(String path) {
//Synthetic comment -- @@ -589,4 +680,9 @@
public int hashCode() {
return mProject.hashCode();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index ef2e950..e46c964 100644

//Synthetic comment -- @@ -384,7 +384,7 @@
if (mProject != null) {
ProjectState state = Sdk.getProjectState(mProject);
if (state != null) {
                IProject[] libraries = state.getLibraryProjects();

ResourceManager resMgr = ResourceManager.getInstance();

//Synthetic comment -- @@ -392,8 +392,7 @@
// one will have priority over the 2nd one. So it's better to loop in the inverse
// order and fill the map with resources that will be overwritten by higher
// priority resources
                final int libCount = libraries != null ? libraries.length : 0;
                for (int i = libCount - 1 ; i >= 0 ; i--) {
IProject library = libraries[i];

ProjectResources libRes = resMgr.getProjectResources(library);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index c3dc894..aebf95e 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IProjectListener;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData.LayoutBridge;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.AndroidVersion;
//Synthetic comment -- @@ -48,20 +49,17 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.progress.IJobRunnable;

import java.io.File;
import java.io.IOException;
//Synthetic comment -- @@ -71,6 +69,7 @@
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
//Synthetic comment -- @@ -605,6 +604,7 @@
GlobalProjectMonitor monitor = GlobalProjectMonitor.getMonitor();
monitor.addProjectListener(mProjectListener);
monitor.addFileListener(mFileListener, IResourceDelta.CHANGED | IResourceDelta.ADDED);

// pre-compute some paths
mDocBaseUrl = getDocumentationBaseUrl(mManager.getLocation() +
//Synthetic comment -- @@ -631,6 +631,7 @@
GlobalProjectMonitor monitor = GlobalProjectMonitor.getMonitor();
monitor.removeProjectListener(mProjectListener);
monitor.removeFileListener(mFileListener);

// the IAndroidTarget objects are now obsolete so update the project states.
synchronized (sLock) {
//Synthetic comment -- @@ -736,6 +737,9 @@

// 2. if the project is a library, make sure to update the
// LibraryState for any main project using this.
for (ProjectState projectState : sProjectStateMap.values()) {
LibraryState libState = projectState.getLibrary(project);
if (libState != null) {
//Synthetic comment -- @@ -749,7 +753,11 @@

// edit the project to remove the linked source folder.
// this also calls LibraryState.close();
                            unlinkLibrary(projectState, project, true /*doInJob*/);
}
}

//Synthetic comment -- @@ -760,71 +768,79 @@

// now remove the project for the project map.
sProjectStateMap.remove(project);
}
}
}

public void projectOpened(IProject project) {
            onProjectOpened(project, true /*recompile*/);
}

public void projectOpenedWithWorkspace(IProject project) {
// no need to force recompilation when projects are opened with the workspace.
            onProjectOpened(project, false /*recompile*/);
}

        private void onProjectOpened(IProject openedProject, boolean recompile) {
ProjectState openedState = getProjectState(openedProject);
if (openedState != null) {
                // find dependencies, if any
                if (openedState.isMissingLibraries()) {
                    // the opened project depends on some libraries.
                    // Look for all other opened projects to see if any is a library for it.
                    // if they are, fix its LibraryState to get the IProject reference and
                    // link the two projects with the linked source folder.
                    boolean foundLibrary = false;
synchronized (sLock) {
for (ProjectState projectState : sProjectStateMap.values()) {
if (projectState != openedState) {
LibraryState libState = openedState.needs(projectState);

if (libState != null) {
                                    foundLibrary = true;
                                    linkProjectAndLibrary(openedState, libState, null,
                                            true /*doInJob*/);
}
}
}
}

                    // force a recompile of the main project through a job
                    // (tree is locked)
                    if (recompile && foundLibrary) {
                        recompile(openedState.getProject());
}
}

                // if the project is a library, then try to see if it's required by other projects.
if (openedState.isLibrary()) {
setupLibraryProject(openedProject);

                    synchronized (sLock) {
                        for (ProjectState projectState : sProjectStateMap.values()) {
                            if (projectState != openedState && projectState.isMissingLibraries()) {
                                LibraryState libState = projectState.needs(openedState);
                                if (libState != null) {
                                    linkProjectAndLibrary(projectState, libState, null,
                                            true /*doInJob*/);

                                    // force a recompile of the main project through a job
                                    // (tree is locked)
                                    if (recompile) {
                                        recompile(projectState.getProject());
                                    }
                                }
                            }
                        }
                    }
}
}
}
//Synthetic comment -- @@ -853,12 +869,12 @@
oldRelativePath.toString(), newRelativePath.toString(),
renamedState);
if (libState != null) {
                                linkProjectAndLibrary(projectState, libState, from,
                                        true /*doInJob*/);

                                // force a recompile of the main project through a job
                                // (tree is locked)
                                recompile(projectState.getProject());
}
}
}
//Synthetic comment -- @@ -896,10 +912,11 @@
// reload the libraries if needed
if (diff.hasDiff()) {
for (LibraryState removedState : diff.removed) {
                                    ProjectState removePState = removedState.getProjectState();
                                    if (removePState != null) {
                                        unlinkLibrary(state, removePState.getProject(),
                                                false /*doInJob*/);
}
}

//Synthetic comment -- @@ -910,16 +927,20 @@
LibraryState libState = state.needs(projectState);

if (libState != null) {
                                                    linkProjectAndLibrary(state, libState, null,
                                                            false /*doInJob*/);
}
}
}
}
}

                                // need to force a full recompile.
                                iProject.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
}

// apply the new target if needed.
//Synthetic comment -- @@ -948,6 +969,202 @@
}
};

private void setupLibraryProject(IProject libProject) {
// if needed add a path var for this library
IPathVariableManager pathVarMgr =
//Synthetic comment -- @@ -968,15 +1185,28 @@
}


private void disposeLibraryProject(IProject project) {
disposeLibraryProject(project.getName());
}

    private void disposeLibraryProject(String libName) {
IPathVariableManager pathVarMgr =
ResourcesPlugin.getWorkspace().getPathVariableManager();

        final String varName = getLibraryVariableName(libName);

// remove the value by setting the value to null.
try {
//Synthetic comment -- @@ -997,318 +1227,387 @@
}

/**
     * Links a project and a library so that the project can use the library code and resources.
     * <p/>This can be done in a job in case the workspace is not locked for resource
     * modification. See <var>doInJob</var>.
*
     * @param projectState the {@link ProjectState} for the main project
     * @param libraryState the {@link LibraryState} for the library project.
     * @param previousLibraryPath an optional old library path that needs to be removed at the
     * same time. Can be <code>null</code> in which case no libraries are removed.
     * @param doInJob whether the action must be done in a new {@link Job}.
*/
    private void linkProjectAndLibrary(
            final ProjectState projectState,
            final LibraryState libraryState,
            final IPath previousLibraryPath,
            boolean doInJob) {
        final IJobRunnable jobRunnable = new IJobRunnable() {
            public IStatus run(final IProgressMonitor monitor) {
                try {
                    IProject project = projectState.getProject();
                    IProject library = libraryState.getProjectState().getProject();

                    // add the library to the list of dynamic references
                    IProjectDescription projectDescription = project.getDescription();
                    IProject[] refs = projectDescription.getDynamicReferences();

                    if (refs.length > 0) {
                        ArrayList<IProject> list = new ArrayList<IProject>(Arrays.asList(refs));

                        // remove a previous library if needed (in case of a rename)
                        if (previousLibraryPath != null) {
                            final int count = list.size();
                            for (int i = 0 ; i < count ; i++) {
                                // since project basically have only one segment that matter,
                                // just check the names
                                if (list.get(i).getName().equals(
                                        previousLibraryPath.lastSegment())) {
                                    list.remove(i);
                                    break;
                                }
                            }

}

                        // add the new one.
                        list.add(library);

                        // set the changed list
                        projectDescription.setDynamicReferences(
                                list.toArray(new IProject[list.size()]));
                    } else {
                        projectDescription.setDynamicReferences(new IProject[] { library });
}

                    // add a linked resource for the source of the library and add it to the project
                    final String libName = library.getName();
                    final String varName = getLibraryVariableName(libName);

                    // get the current classpath entries for the project to add the new source
                    // folders.
                    IJavaProject javaProject = JavaCore.create(project);
                    IClasspathEntry[] entries = javaProject.getRawClasspath();
                    ArrayList<IClasspathEntry> classpathEntries = new ArrayList<IClasspathEntry>(
                            Arrays.asList(entries));

                    IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();

                    // list to hold the source folder to delete, as they can't be delete before
                    // they have been removed from the classpath entries
                    final ArrayList<IResource> toDelete = new ArrayList<IResource>();

                    // loop on the classpath entries and look for CPE_SOURCE entries that
                    // are linked folders. If they are created by us for the given library, then
                    // we remove them as they'll be created again later (it's easier than trying
                    // to keep old one--if they link to the same resource)
                    for (int i = 0 ; i < classpathEntries.size();) {
                        IClasspathEntry classpathEntry = classpathEntries.get(i);
                        if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                            IPath path = classpathEntry.getPath();
                            IResource linkedRes = wsRoot.findMember(path);
                            if (linkedRes != null && linkedRes.isLinked() && CREATOR_ADT.equals(
                                    ProjectHelper.loadStringProperty(linkedRes, PROP_CREATOR))) {
                                IResource originalLibrary = ProjectHelper.loadResourceProperty(
                                        linkedRes, PROP_LIBRARY);

                                // if the library is missing, or if the library is the one being
                                // added or the same path as the one being removed:
                                // remove the classpath entry and delete the linked folder.
                                if (originalLibrary == null || originalLibrary.equals(library) ||
                                        originalLibrary.getFullPath().equals(previousLibraryPath)) {
                                    classpathEntries.remove(i);
                                    toDelete.add(linkedRes);
                                    continue; // don't increment i
                                }
                            }
}

                        i++;
}

                    // get the list of source folders for the library.
                    ArrayList<IPath> sourceFolderPaths = BaseProjectHelper.getSourceClasspaths(
                            library);

                    // loop on all the source folder, ignoring FD_GEN and add them as linked folder
                    for (IPath sourceFolderPath : sourceFolderPaths) {
                        IResource sourceFolder = wsRoot.findMember(sourceFolderPath);
                        if (sourceFolder == null) {
                            continue;
                        }

                        IPath relativePath = sourceFolder.getProjectRelativePath();
                        if (SdkConstants.FD_GEN_SOURCES.equals(relativePath.toString())) {
                            continue;
                        }

// get a string version, to make up the linked folder name
String srcFolderName = relativePath.toString().replace("/", //$NON-NLS-1$
"_"); //$NON-NLS-1$

// create a linked resource for the library using the path var.
                        IFolder libSrc = project.getFolder(libName + "_" + srcFolderName);  //$NON-NLS-1$

                        libSrc.createLink(new Path(varName).append(relativePath),
                                IResource.REPLACE, monitor);

                        // if we were deleting something called exactly the same (which could
                        // have linked to a different folder), we remove it from the list
                        // of items to delete
                        if (toDelete.contains(libSrc)) {
                            toDelete.remove(libSrc);
}

                        // set some persistent properties on it to know that it was created by ADT.
ProjectHelper.saveStringProperty(libSrc, PROP_CREATOR, CREATOR_ADT);
ProjectHelper.saveResourceProperty(libSrc, PROP_LIBRARY, library);

// add the source folder to the classpath entries
                        classpathEntries.add(JavaCore.newSourceEntry(libSrc.getFullPath()));
}

                    // set the new list
                    javaProject.setRawClasspath(
                            classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]),
                            monitor);

                    for (IResource res : toDelete) {
                        res.delete(true, monitor);
                    }

                    return Status.OK_STATUS;
                } catch (CoreException e) {
                    AdtPlugin.logAndPrintError(e, "Library Project", "Failed to create link between library %1$s and project %2$s: %3$s",
                            libraryState.getProjectState().getProject().getName(),
                            projectState.getProject().getName(),
                            e.getMessage());
                    return e.getStatus();
}
}
        };

        if (doInJob) {
            Job job = new Job("Android Library link creation") { //$NON-NLS-1$
                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    return jobRunnable.run(monitor);
}
            };
            job.setPriority(Job.BUILD);
            job.schedule();
        } else {
            jobRunnable.run(new NullProgressMonitor());
}
}

/**
* Unlinks a project and a library. This removes the linked folder from the main project, and
* removes it from the build path. Finally, this calls {@link LibraryState#close()}.
* <p/>This can be done in a job in case the workspace is not locked for resource
* modification. See <var>doInJob</var>.
*
     * @param projectState the {@link ProjectState} for the main project
     * @param libraryProject the library project that needs to be removed
     * @param doInJob whether the action must be done in a new {@link Job}.
*/
    private void unlinkLibrary(final ProjectState projectState, final IProject libraryProject,
            boolean doInJob) {
        final IJobRunnable jobRunnable = new IJobRunnable() {
            public IStatus run(IProgressMonitor monitor) {
                try {
                    IProject project = projectState.getProject();

                    // if the library and the main project are closed at the same time, this
                    // is likely to return false since this is run in a new job.
                    if (project.isOpen() == false) {
                        // cannot change the description of closed projects.
                        return Status.OK_STATUS;
                    }

                    // remove the library to the list of dynamic references
                    IProjectDescription projectDescription = project.getDescription();
                    IProject[] refs = projectDescription.getDynamicReferences();

                    if (refs.length > 0) {
                        ArrayList<IProject> list = new ArrayList<IProject>(Arrays.asList(refs));

                        // remove a previous library if needed (in case of a rename)
                        final int count = list.size();
                        for (int i = 0 ; i < count ; i++) {
                            // since project basically have only one segment that matter,
                            // just check the names
                            if (list.get(i).equals(libraryProject)) {
                                list.remove(i);
                                break;
                            }
                        }

                        // set the changed list
                        projectDescription.setDynamicReferences(
                                list.toArray(new IProject[list.size()]));
                    }

                    // edit the list of source folders.
                    IJavaProject javaProject = JavaCore.create(project);
                    IClasspathEntry[] entries = javaProject.getRawClasspath();
                    ArrayList<IClasspathEntry> classpathEntries = new ArrayList<IClasspathEntry>(
                            Arrays.asList(entries));

                    IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();

                    // list to hold the source folder to delete, as they can't be delete before
                    // they have been removed from the classpath entries
                    ArrayList<IResource> toDelete = new ArrayList<IResource>();

                    for (int i = 0 ; i < classpathEntries.size();) {
                        IClasspathEntry classpathEntry = classpathEntries.get(i);
                        if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                            IPath path = classpathEntry.getPath();
                            IResource linkedRes = wsRoot.findMember(path);
                            if (linkedRes != null && linkedRes.isLinked() && CREATOR_ADT.equals(
                                    ProjectHelper.loadStringProperty(linkedRes, PROP_CREATOR))) {
                                IResource originalLibrary = ProjectHelper.loadResourceProperty(
                                        linkedRes, PROP_LIBRARY);

                                // if the library is missing, or if the library is the one being
                                // unlinked:
                                // remove the classpath entry and delete the linked folder.
                                if (originalLibrary == null ||
                                        originalLibrary.equals(libraryProject)) {
                                    classpathEntries.remove(i);
                                    toDelete.add(linkedRes);
                                    continue; // don't increment i
                                }
                            }
                        }

                        i++;
                    }

                    // set the new list
                    javaProject.setRawClasspath(
                            classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]),
                            monitor);

                    // delete the resources that need deleting
                    for (IResource res : toDelete) {
                        res.delete(true, monitor);
                    }

                    return Status.OK_STATUS;
                } catch (CoreException e) {
                    AdtPlugin.log(e, "Failure to unlink %1$s from %2$s", libraryProject.getName(),
                            projectState.getProject().getName());
                    return e.getStatus();
                }
}
        };

        if (doInJob) {
            Job job = new Job("Android Library unlinking") { //$NON-NLS-1$
                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    return jobRunnable.run(monitor);
}
            };

            job.setPriority(Job.BUILD);
            job.schedule();
        } else {
            jobRunnable.run(new NullProgressMonitor());
}
}

/**
     * Triggers a project recompilation in a new {@link Job}. This is useful when the
     * tree is locked and the {@link IProject#build(int, IProgressMonitor)} call would failed.
     * @param project the project to recompile.
*/
    private void recompile(final IProject project) {
        Job job = new Job("Project recompilation trigger") { //$NON-NLS-1$
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                try {
                    project.build( IncrementalProjectBuilder.FULL_BUILD, null);
                    return Status.OK_STATUS;
                } catch (CoreException e) {
                    return e.getStatus();
}
}
        };

        job.setPriority(Job.BUILD);
        job.schedule();
}

/**







