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
        IProject[] libProjects = projectState.getFullLibraryProjects();

IProject project = projectState.getProject();
IJavaProject javaProject = JavaCore.create(project);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PostCompilerBuilder.java
//Synthetic comment -- index ffec6e7..15f960c 100644

//Synthetic comment -- @@ -206,7 +206,7 @@
}

// get the libraries
            libProjects = projectState.getFullLibraryProjects();

IJavaProject javaProject = JavaCore.create(project);

//Synthetic comment -- @@ -218,7 +218,7 @@
IJavaProject[] referencedJavaProjects = PostCompilerHelper.getJavaProjects(javaProjects);

// mix the java project and the library projects
            final int libCount = libProjects.length;
final int javaCount = javaProjects != null ? javaProjects.length : 0;
allRefProjects = new IProject[libCount + javaCount];
if (libCount > 0) {
//Synthetic comment -- @@ -268,7 +268,7 @@
// if the main resources didn't change, then we check for the library
// ones (will trigger resource repackaging too)
if ((mPackageResources == false || mBuildFinalPackage == false) &&
                        libProjects.length > 0) {
for (IProject libProject : libProjects) {
delta = getDelta(libProject);
if (delta != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerBuilder.java
//Synthetic comment -- index 868ecf7..e3a6715 100644

//Synthetic comment -- @@ -234,7 +234,7 @@
IAndroidTarget projectTarget = projectState.getTarget();

// get the libraries
            libProjects = projectState.getFullLibraryProjects();

IJavaProject javaProject = JavaCore.create(project);

//Synthetic comment -- @@ -287,8 +287,7 @@

// if the main resources didn't change, then we check for the library
// ones (will trigger resource recompilation too)
                    if (mMustCompileResources == false && libProjects.length > 0) {
for (IProject libProject : libProjects) {
delta = getDelta(libProject);
if (delta != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectState.java
//Synthetic comment -- index 93c77f9..5886692 100644

//Synthetic comment -- @@ -42,6 +42,18 @@
* <p>This gives raw access to the properties (from <code>default.properties</code>), as well
* as direct access to target, apksettings and library information.
*
 * This also gives access to library information.
 *
 * {@link #isLibrary()} indicates if the project is a library.
 * {@link #hasLibraries()} and {@link #getLibraries()} give access to the libraries through
 * instances of {@link LibraryState}. A {@link LibraryState} instance is a link between a main
 * project and its library. Theses instances are owned by the {@link ProjectState}.
 *
 * {@link #isMissingLibraries()} will indicate if the project has libraries that are not resolved.
 * Unresolved libraries are libraries that do not have any matching opened Eclipse project.
 * When there are missing libraries, the {@link LibraryState} instance for them will return null
 * for {@link LibraryState#getProjectState()}.
 *
*/
public final class ProjectState {

//Synthetic comment -- @@ -74,13 +86,14 @@
* Closes the library. This resets the IProject from this object ({@link #getProjectState()} will
* return <code>null</code>), and updates the main project data so that the library
* {@link IProject} object does not show up in the return value of
         * {@link ProjectState#getFullLibraryProjects()}.
*/
public void close() {
            mProjectState.removeParentProject(getMainProjectState());
mProjectState = null;
mPath = null;

            getMainProjectState().updateFullLibraryList();
}

private void setRelativePath(String relativePath) {
//Synthetic comment -- @@ -90,8 +103,9 @@
private void setProject(ProjectState project) {
mProjectState = project;
mPath = project.getProject().getLocation().toOSString();
            mProjectState.addParentProject(getMainProjectState());

            getMainProjectState().updateFullLibraryList();
}

/**
//Synthetic comment -- @@ -145,15 +159,22 @@

private final IProject mProject;
private final ProjectProperties mProperties;
    private IAndroidTarget mTarget;
    private ApkSettings mApkSettings;
/**
* list of libraries. Access to this list must be protected by
* <code>synchronized(mLibraries)</code>, but it is important that such code do not call
* out to other classes (especially those protected by {@link Sdk#getLock()}.)
*/
private final ArrayList<LibraryState> mLibraries = new ArrayList<LibraryState>();
    /** Cached list of all IProject instances representing the resolved libraries, including
     * indirect dependencies. This must never be null. */
    private IProject[] mLibraryProjects = new IProject[0];
    /**
     * List of parent projects. When this instance is a library ({@link #isLibrary()} returns
     * <code>true</code>) then this is filled with projects that depends on this project.
     */
    private final ArrayList<ProjectState> mParentProjects = new ArrayList<ProjectState>();

public ProjectState(IProject project, ProjectProperties properties) {
mProject = project;
//Synthetic comment -- @@ -281,7 +302,7 @@
diff.removed.addAll(oldLibraries);

// update the library with what IProjet are known at the time.
            updateFullLibraryList();
}

return diff;
//Synthetic comment -- @@ -305,13 +326,14 @@
}

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

//Synthetic comment -- @@ -369,6 +391,15 @@
return null;
}

    /**
     * Returns the {@link LibraryState} object for a given <var>name</var>.
     * </p>This can only return a non-null object if the link between the main project's
     * {@link IProject} and the library's {@link IProject} was done.
     *
     * @return the matching LibraryState or <code>null</code>
     *
     * @see #needs(IProject)
     */
public LibraryState getLibrary(String name) {
synchronized (mLibraries) {
for (LibraryState state : mLibraries) {
//Synthetic comment -- @@ -424,6 +455,26 @@
}

/**
     * Returns whether the project depends on a given <var>library</var>
     * @param library the library to check.
     * @return true if the project depends on the library. This is not affected by whether the link
     * was done through {@link #needs(ProjectState)}.
     */
    public boolean dependsOn(ProjectState library) {
        synchronized (mLibraries) {
            for (LibraryState state : mLibraries) {
                if (state != null && state.getProjectState() != null &&
                        library.getProject().equals(state.getProjectState().getProject())) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
* Updates a library with a new path.
* <p/>This method acts both as a check and an action. If the project does not depend on the
* given <var>oldRelativePath</var> then no action is done and <code>null</code> is returned.
//Synthetic comment -- @@ -491,6 +542,15 @@
return null;
}


    private void addParentProject(ProjectState parentState) {
        mParentProjects.add(parentState);
    }

    private void removeParentProject(ProjectState parentState) {
        mParentProjects.remove(parentState);
    }

/**
* Saves the default.properties file and refreshes it to make sure that it gets reloaded
* by Eclipse
//Synthetic comment -- @@ -543,20 +603,51 @@
return null;
}

    /**
     * Update the full library list, including indirect dependencies. The result is returned by
     * {@link #getFullLibraryProjects()}.
     */
    private void updateFullLibraryList() {
ArrayList<IProject> list = new ArrayList<IProject>();
synchronized (mLibraries) {
            buildFullLibraryDependencies(mLibraries, list);
}

mLibraryProjects = list.toArray(new IProject[list.size()]);
}

/**
     * Resolves a given list of libraries, finds out if they depend on other libraries, and
     * returns a full list of all the direct and indirect dependencies in the proper order (first
     * is higher priority when calling aapt).
     * @param inLibraries the libraries to resolve
     * @param outLibraries where to store all the libraries.
     */
    private void buildFullLibraryDependencies(List<LibraryState> inLibraries,
            ArrayList<IProject> outLibraries) {
        // loop in the inverse order to resolve dependencies on the libraries, so that if a library
        // is required by two higher level libraries it can be inserted in the correct place
        for (int i = inLibraries.size() - 1  ; i >= 0 ; i--) {
            LibraryState library = inLibraries.get(i);

            // get its libraries if possible
            ProjectState libProjectState = library.getProjectState();
            if (libProjectState != null) {
                List<LibraryState> dependencies = libProjectState.getLibraries();

                // build the dependencies for those libraries
                buildFullLibraryDependencies(dependencies, outLibraries);

                // and add the current library (if needed) in front (higher priority)
                if (outLibraries.contains(libProjectState.getProject()) == false) {
                    outLibraries.add(0, libProjectState.getProject());
                }
            }
        }
    }


    /**
* Converts a path containing only / by the proper platform separator.
*/
private String convertPath(String path) {
//Synthetic comment -- @@ -589,4 +680,9 @@
public int hashCode() {
return mProject.hashCode();
}

    @Override
    public String toString() {
        return mProject.getName();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index ef2e950..e46c964 100644

//Synthetic comment -- @@ -384,7 +384,7 @@
if (mProject != null) {
ProjectState state = Sdk.getProjectState(mProject);
if (state != null) {
                IProject[] libraries = state.getFullLibraryProjects();

ResourceManager resMgr = ResourceManager.getInstance();

//Synthetic comment -- @@ -392,8 +392,7 @@
// one will have priority over the 2nd one. So it's better to loop in the inverse
// order and fill the map with resources that will be overwritten by higher
// priority resources
                for (int i = libraries.length - 1 ; i >= 0 ; i--) {
IProject library = libraries[i];

ProjectResources libRes = resMgr.getProjectResources(library);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index c3dc894..aebf95e 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IProjectListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IResourceEventListener;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData.LayoutBridge;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.AndroidVersion;
//Synthetic comment -- @@ -48,20 +49,17 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import java.io.File;
import java.io.IOException;
//Synthetic comment -- @@ -71,6 +69,7 @@
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
//Synthetic comment -- @@ -605,6 +604,7 @@
GlobalProjectMonitor monitor = GlobalProjectMonitor.getMonitor();
monitor.addProjectListener(mProjectListener);
monitor.addFileListener(mFileListener, IResourceDelta.CHANGED | IResourceDelta.ADDED);
        monitor.addResourceEventListener(mResourceEventListener);

// pre-compute some paths
mDocBaseUrl = getDocumentationBaseUrl(mManager.getLocation() +
//Synthetic comment -- @@ -631,6 +631,7 @@
GlobalProjectMonitor monitor = GlobalProjectMonitor.getMonitor();
monitor.removeProjectListener(mProjectListener);
monitor.removeFileListener(mFileListener);
        monitor.removeResourceEventListener(mResourceEventListener);

// the IAndroidTarget objects are now obsolete so update the project states.
synchronized (sLock) {
//Synthetic comment -- @@ -736,6 +737,9 @@

// 2. if the project is a library, make sure to update the
// LibraryState for any main project using this.
                    // Also, recorded the updated projects that are libraries, to update
                    // projects that depend on them.
                    ArrayList<ProjectState> updatedLibraries = new ArrayList<ProjectState>();
for (ProjectState projectState : sProjectStateMap.values()) {
LibraryState libState = projectState.getLibrary(project);
if (libState != null) {
//Synthetic comment -- @@ -749,7 +753,11 @@

// edit the project to remove the linked source folder.
// this also calls LibraryState.close();
                            startActionBundle(new UnlinkLibraryBundle(projectState, project));

                            if (projectState.isLibrary()) {
                                updatedLibraries.add(projectState);
                            }
}
}

//Synthetic comment -- @@ -760,71 +768,79 @@

// now remove the project for the project map.
sProjectStateMap.remove(project);

                    // update the projects that depend on the updated project
                    updateProjectsWithNewLibraries(updatedLibraries);
}
}
}

public void projectOpened(IProject project) {
            onProjectOpened(project);
}

public void projectOpenedWithWorkspace(IProject project) {
// no need to force recompilation when projects are opened with the workspace.
            onProjectOpened(project);
}

        private void onProjectOpened(final IProject openedProject) {
ProjectState openedState = getProjectState(openedProject);
if (openedState != null) {
                if (openedState.hasLibraries()) {
                    // list of library to link to the opened project.
                    final ArrayList<IProject> libsToLink = new ArrayList<IProject>();

                    // Look for all other opened projects to see if any is a library for the opened
                    // project.
synchronized (sLock) {
for (ProjectState projectState : sProjectStateMap.values()) {
if (projectState != openedState) {
                                // ProjectState#needs() both checks if this is a missing library
                                // and updates LibraryState to contains the new values.
LibraryState libState = openedState.needs(projectState);

if (libState != null) {
                                    // we have a match! Add it the library to the list (if it was
                                    // not added through an indirect dependency before).
                                    IProject libProject = libState.getProjectState().getProject();
                                    if (libsToLink.contains(libProject) == false) {
                                        libsToLink.add(libProject);
                                    }

                                    // now find what this depends on, and add it too.
                                    // The order here doesn't matter
                                    // as it's just to add the linked source folder, so there's no
                                    // need to use ProjectState#getFullLibraryProjects() which
                                    // could return project that have already been added anyway.
                                    fillProjectDependenciesList(libState.getProjectState(),
                                            libsToLink);
}
}
}
}

                    if (libsToLink.size() > 0) {
                        // link the libraries to the opened project through the job by adding an
                        // action bundle to the queue.
                        LinkLibraryBundle bundle = new LinkLibraryBundle();
                        bundle.mProject = openedProject;
                        bundle.mLibraryProjects = libsToLink.toArray(
                                new IProject[libsToLink.size()]);
                        bundle.mPreviousLibraryPath = null;
                        bundle.mCleanupCPE = true;
                        startActionBundle(bundle);
}
}

                // if the project is a library, then add it to the list of projects being opened.
                // They will be processed in IResourceEventListener#resourceChangeEventEnd.
                // This is done so that we are sure to process all the projects being opened
                // first and only then process projects depending on the projects that were opened.
if (openedState.isLibrary()) {
setupLibraryProject(openedProject);

                    mOpenedLibraryProjects.add(openedState);
}
}
}
//Synthetic comment -- @@ -853,12 +869,12 @@
oldRelativePath.toString(), newRelativePath.toString(),
renamedState);
if (libState != null) {
                                LinkLibraryBundle bundle = new LinkLibraryBundle();
                                bundle.mProject = projectState.getProject();
                                bundle.mLibraryProjects = new IProject[] {
                                        libState.getProjectState().getProject() };
                                bundle.mCleanupCPE = false;
                                startActionBundle(bundle);
}
}
}
//Synthetic comment -- @@ -896,10 +912,11 @@
// reload the libraries if needed
if (diff.hasDiff()) {
for (LibraryState removedState : diff.removed) {
                                    ProjectState removedPState = removedState.getProjectState();
                                    if (removedPState != null) {
                                        startActionBundle(
                                                new UnlinkLibraryBundle(
                                                        state, removedPState.getProject()));
}
}

//Synthetic comment -- @@ -910,16 +927,20 @@
LibraryState libState = state.needs(projectState);

if (libState != null) {
                                                    IProject[] libArray = new IProject[] {
                                                            libState.getProjectState().getProject()
                                                    };
                                                    LinkLibraryBundle bundle = new LinkLibraryBundle();
                                                    bundle.mProject = iProject;
                                                    bundle.mLibraryProjects = libArray;
                                                    bundle.mPreviousLibraryPath = null;
                                                    bundle.mCleanupCPE = false;
                                                    startActionBundle(bundle);
}
}
}
}
}
}

// apply the new target if needed.
//Synthetic comment -- @@ -948,6 +969,202 @@
}
};

    /** List of opened project. This is filled in {@link IProjectListener#projectOpened(IProject)}
     * and {@link IProjectListener#projectOpenedWithWorkspace(IProject)}, and processed in
     * {@link IResourceEventListener#resourceChangeEventEnd()}.
     */
    private final ArrayList<ProjectState> mOpenedLibraryProjects = new ArrayList<ProjectState>();

    /**
     * Delegate listener for resource changes. This is called before and after any calls to the
     * project and file listeners (for a given resource change event).
     */
    private IResourceEventListener mResourceEventListener = new IResourceEventListener() {
        public void resourceChangeEventStart() {
            // pass
        }

        public void resourceChangeEventEnd() {
            updateProjectsWithNewLibraries(mOpenedLibraryProjects);
            mOpenedLibraryProjects.clear();
        }
    };

    /**
     * Action Bundle to be used with {@link Sdk#startActionBundle(ActionBundle)}.
     */
    private interface ActionBundle {
        enum BundleType { LINK_LIBRARY, UNLINK_LIBRARY };
        BundleType getType();
    };

    /**
     * Action bundle to link libraries to a project.
     *
     * @see Sdk#linkProjectAndLibrary(LinkLibraryBundle, IProgressMonitor)
     */
    private static class LinkLibraryBundle implements ActionBundle {

        /** The main project receiving the library links. */
        IProject mProject;
        /** The libraries to add to the main project. */
        IProject[] mLibraryProjects;
        /** an optional old library path that needs to be removed at the same time as the new
         * libraries are added. Can be <code>null</code> in which case no libraries are removed. */
        IPath mPreviousLibraryPath;
        /** Whether unknown IClasspathEntry (that were flagged as being added by ADT) are to be
         * removed. This is typically only set to <code>true</code> when the project is opened. */
        boolean mCleanupCPE;

        public BundleType getType() {
            return BundleType.LINK_LIBRARY;
        }

        @Override
        public String toString() {
            return String.format("LinkLibraryBundle: %1$s (%2$s) > %3$s", //$NON-NLS-1$
                    mProject.getName(),
                    mCleanupCPE,
                    Arrays.toString(mLibraryProjects));
        }
    }

    /**
     * Action bundle to unlink a library from a project.
     *
     * @see Sdk#unlinkLibrary(UnlinkLibraryBundle, IProgressMonitor)
     */
    private static class UnlinkLibraryBundle implements ActionBundle {
        /** the main project */
        final ProjectState mProject;
        /** the library to remove */
        final IProject mLibrary;

        UnlinkLibraryBundle(ProjectState project, IProject library) {
            mProject = project;
            mLibrary = library;
        }

        public BundleType getType() {
            return BundleType.UNLINK_LIBRARY;
        }
    }

    private final ArrayList<ActionBundle> mActionBundleQueue = new ArrayList<ActionBundle>();

    /**
     * Runs the given action bundle through a job queue.
     *
     * All action bundles are executed in a job in the exact order they are added.
     * This is convenient when several actions must be executed in a job consecutively (instead
     * of in parallel as it would happen if each started its own job) but it is impossible
     * to manully control the job that's running them (for instance each action is started from
     * different callbacks such as {@link IProjectListener#projectOpened(IProject)}.
     *
     * If the job is not yet started, or has terminated due to lack of action bundle, it is
     * restarted.
     *
     * @param bundle the action bundle to execute
     */
    private void startActionBundle(ActionBundle bundle) {
        boolean startJob = false;
        synchronized (mActionBundleQueue) {
            startJob = mActionBundleQueue.size() == 0;
            mActionBundleQueue.add(bundle);
        }

        if (startJob) {
            Job job = new Job("Android Library Job") { //$NON-NLS-1$
                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    // loop until there's no bundle to process
                    while (true) {
                        // get the bundle, but don't remove until we're done, or a new job could be
                        // started.
                        ActionBundle bundle = null;
                        synchronized (mActionBundleQueue) {
                            // there is always a bundle at this point, as they are only removed
                            // at the end of this method, and the job is only started after adding
                            // one
                            bundle = mActionBundleQueue.get(0);
                        }

                        // process the bundle.
                        try {
                            switch (bundle.getType()) {
                                case LINK_LIBRARY:
                                    linkProjectAndLibrary((LinkLibraryBundle)bundle, monitor);
                                    break;
                                case UNLINK_LIBRARY:
                                    unlinkLibrary((UnlinkLibraryBundle) bundle, monitor);
                                    break;
                            }
                        } catch (Exception e) {
                            AdtPlugin.log(e, "Failed to process bundle: %1$s", //$NON-NLS-1$
                                    bundle.toString());
                        }

                        // remove it from the list.
                        synchronized (mActionBundleQueue) {
                            mActionBundleQueue.remove(0);

                            // no more bundle to process? done.
                            if (mActionBundleQueue.size() == 0) {
                                return Status.OK_STATUS;
                            }
                        }
                    }
                }
            };
            job.setPriority(Job.BUILD);
            job.schedule();
        }
    }


    /**
     * Adds to a list the resolved {@link IProject} dependencies for a given {@link ProjectState}.
     * This recursively goes down to indirect dependencies.
     *
     * <strong>The list is filled in an order that is not valid for calling <code>aapt</code>
     * </strong>.
     * Use {@link ProjectState#getFullLibraryProjects()} for use with <code>aapt</code>.
     *
     * @param projectState the ProjectState of the project from which to add the libraries.
     * @param libraries the list of {@link IProject} to fill.
     */
    private void fillProjectDependenciesList(ProjectState projectState,
            ArrayList<IProject> libraries) {
        for (LibraryState libState : projectState.getLibraries()) {
            ProjectState libProjectState = libState.getProjectState();

            // only care if the LibraryState has a resolved ProjectState
            if (libProjectState != null) {
                // try not to add duplicate. This can happen if a project depends on 2 different
                // libraries that both depend on the same one.
                IProject libProject = libProjectState.getProject();
                if (libraries.contains(libProject) == false) {
                    libraries.add(libProject);
                }

                // process the libraries of this library too.
                fillProjectDependenciesList(libProjectState, libraries);
            }
        }
    }

    /**
     * Sets up a path variable for a given project.
     * The name of the variable is based on the name of the project. However some valid character
     * for project names can be invalid for variable paths.
     * {@link #getLibraryVariableName(String)} return the name of the variable based on the
     * project name.
     *
     * @param libProject the project
     *
     * @see IPathVariableManager
     * @see #getLibraryVariableName(String)
     */
private void setupLibraryProject(IProject libProject) {
// if needed add a path var for this library
IPathVariableManager pathVarMgr =
//Synthetic comment -- @@ -968,15 +1185,28 @@
}


    /**
     * Deletes the path variable that was setup for the given project.
     * @param project the project
     * @see #disposeLibraryProject(String)
     */
private void disposeLibraryProject(IProject project) {
disposeLibraryProject(project.getName());
}

    /**
     * Deletes the path variable that was setup for the given project name.
     * The name of the variable is based on the name of the project. However some valid character
     * for project names can be invalid for variable paths.
     * {@link #getLibraryVariableName(String)} return the name of the variable based on the
     * project name.
     * @param projectName the name of the project, unmodified.
     */
    private void disposeLibraryProject(String projectName) {
IPathVariableManager pathVarMgr =
ResourcesPlugin.getWorkspace().getPathVariableManager();

        final String varName = getLibraryVariableName(projectName);

// remove the value by setting the value to null.
try {
//Synthetic comment -- @@ -997,318 +1227,387 @@
}

/**
     * Links a project and a set of libraries so that the project can use the library code.
*
     * This does the follow:
     * - add the library projects to the main projects dynamic reference list. This is used by
     *   the builders to receive resource change deltas for library projects and figure out what
     *   needs to be recompiled/recreated.
     * - create new {@link IClasspathEntry} of type {@link IClasspathEntry#CPE_SOURCE} for each
     *   source folder for each library project. If there was a previous
     * - If {@link LinkLibraryBundle#mCleanupCPE} is set to true, all CPE created by ADT that cannot
     *   be resolved are removed. This should only be used when the project is opened.
     *
     * @param bundle The {@link LinkLibraryBundle} action bundle that contains all the parameters
     *               necessary to execute the action.
     * @param monitor an {@link IProgressMonitor}.
     * @return an {@link IStatus} with the status of the action.
*/
    private IStatus linkProjectAndLibrary(LinkLibraryBundle bundle, IProgressMonitor monitor) {
        try {
            // add the library to the list of dynamic references. This is necessary to receive
            // notifications that the library content changed in the builders.
            IProjectDescription projectDescription = bundle.mProject.getDescription();
            IProject[] refs = projectDescription.getDynamicReferences();

            if (refs.length > 0) {
                ArrayList<IProject> list = new ArrayList<IProject>(Arrays.asList(refs));

                // remove a previous library if needed (in case of a rename)
                if (bundle.mPreviousLibraryPath != null) {
                    final int count = list.size();
                    for (int i = 0 ; i < count ; i++) {
                        // since project basically have only one segment that matter,
                        // just check the names
                        if (list.get(i).getName().equals(
                                bundle.mPreviousLibraryPath.lastSegment())) {
                            list.remove(i);
                            break;
}
}

                }

                // add the new ones.
                list.addAll(Arrays.asList(bundle.mLibraryProjects));

                // set the changed list
                projectDescription.setDynamicReferences(
                        list.toArray(new IProject[list.size()]));
            } else {
                projectDescription.setDynamicReferences(bundle.mLibraryProjects);
            }

            // get the current classpath entries for the project to add the new source
            // folders.
            IJavaProject javaProject = JavaCore.create(bundle.mProject);
            IClasspathEntry[] entries = javaProject.getRawClasspath();
            ArrayList<IClasspathEntry> classpathEntries = new ArrayList<IClasspathEntry>(
                    Arrays.asList(entries));

            IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();

            // loop on the classpath entries and look for CPE_SOURCE entries that
            // are linked folders, then record them for comparison layer as we add the new
            // ones.
            ArrayList<IClasspathEntry> libCpeList = new ArrayList<IClasspathEntry>();
            if (bundle.mCleanupCPE) {
                for (IClasspathEntry classpathEntry : classpathEntries) {
                    if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                        IPath path = classpathEntry.getPath();
                        IResource linkedRes = wsRoot.findMember(path);
                        if (linkedRes != null && linkedRes.isLinked() &&
                                CREATOR_ADT.equals(ProjectHelper.loadStringProperty(
                                        linkedRes, PROP_CREATOR))) {
                            libCpeList.add(classpathEntry);
}
                    }
                }
            }

            // loop on the projects to add.
            for (IProject library : bundle.mLibraryProjects) {
                final String libName = library.getName();
                final String varName = getLibraryVariableName(libName);

                // get the list of source folders for the library.
                ArrayList<IPath> sourceFolderPaths = BaseProjectHelper.getSourceClasspaths(
                        library);

                // loop on all the source folder, ignoring FD_GEN and add them
                // as linked folder
                for (IPath sourceFolderPath : sourceFolderPaths) {
                    IResource sourceFolder = wsRoot.findMember(sourceFolderPath);
                    if (sourceFolder == null || sourceFolder.isLinked()) {
                        continue;
}

                    IPath relativePath = sourceFolder.getProjectRelativePath();
                    if (SdkConstants.FD_GEN_SOURCES.equals(relativePath.toString())) {
                        continue;
                    }

                    // create the linked path
                    IPath linkedPath = new Path(varName).append(relativePath);

                    // look for an existing linked path
                    IClasspathEntry match = findClasspathEntryMatch(libCpeList, linkedPath, null);

                    if (match == null) {

                        // no match, create one
// get a string version, to make up the linked folder name
String srcFolderName = relativePath.toString().replace("/", //$NON-NLS-1$
"_"); //$NON-NLS-1$

                        // folder name
                        String folderName = libName + "_" + srcFolderName; //$NON-NLS-1$

// create a linked resource for the library using the path var.
                        IFolder libSrc = bundle.mProject.getFolder(folderName);
                        IPath libSrcPath = libSrc.getFullPath();

                        // check if there's a CPE that would conflict, in which case it needs to
                        // be removed (this can happen for existing CPE that don't match an open
                        // project)
                        match = findClasspathEntryMatch(classpathEntries, null/*rawPath*/,
                                libSrcPath);
                        if (match != null) {
                            classpathEntries.remove(match);
}

                        // the path of the linked resource is based on the path variable
                        // representing the library project, followed by the source folder name.
                        libSrc.createLink(linkedPath,
                                IResource.REPLACE, monitor);

                        // set some persistent properties on it to know that it was
                        // created by ADT.
ProjectHelper.saveStringProperty(libSrc, PROP_CREATOR, CREATOR_ADT);
ProjectHelper.saveResourceProperty(libSrc, PROP_LIBRARY, library);

// add the source folder to the classpath entries
                        classpathEntries.add(JavaCore.newSourceEntry(libSrcPath));
                    } else {
                        // there's a valid match, do nothing, but remove the match from
                        // the list of previously existing CPE.
                        libCpeList.remove(match);
}
}
}

            if (bundle.mCleanupCPE) {
                // remove the remaining CPE as they could not be resolved.
                classpathEntries.removeAll(libCpeList);
            }

            // set the new list
            javaProject.setRawClasspath(
                    classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]),
                    monitor);

            if (bundle.mCleanupCPE) {
                // and delete the folders of the CPE that were removed (must be done after)
                for (IClasspathEntry cpe : libCpeList) {
                    IResource res = wsRoot.findMember(cpe.getPath());
                    res.delete(true, monitor);
}
            }

            return Status.OK_STATUS;
        } catch (CoreException e) {
            AdtPlugin.logAndPrintError(e, bundle.mProject.getName(),
                    "Failed to create library links: %1$s",
                    e.getMessage());
            return e.getStatus();
}
}

/**
     * Returns a {@link IClasspathEntry} from the given list whose linked path match the given path.
     * @param cpeList a list of {@link IClasspathEntry} of {@link IClasspathEntry#getEntryKind()}
     *                {@link IClasspathEntry#CPE_SOURCE} whose {@link IClasspathEntry#getPath()}
     *                points to a linked folder.
     * @param rawPath the raw path to compare to. Can be null if <var>path</var> is used instead.
     * @param path the path to compare to. Can be null if <var>rawPath</var> is used instead.
     * @return the matching IClasspathEntry or null.
     */
    private IClasspathEntry findClasspathEntryMatch(ArrayList<IClasspathEntry> cpeList,
            IPath rawPath, IPath path) {
        IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
        for (IClasspathEntry cpe : cpeList) {
            IPath cpePath = cpe.getPath();
            // test the normal path of the resource.
            if (path != null && path.equals(cpePath)) {
                return cpe;
            }

            IResource res = wsRoot.findMember(cpePath);
            // getRawLocation returns the path that the linked folder points to.
            if (rawPath != null && res.getRawLocation().equals(rawPath)) {
                return cpe;
            }

        }
        return null;
    }

    /**
* Unlinks a project and a library. This removes the linked folder from the main project, and
* removes it from the build path. Finally, this calls {@link LibraryState#close()}.
* <p/>This can be done in a job in case the workspace is not locked for resource
* modification. See <var>doInJob</var>.
*
     * @param bundle The {@link UnlinkLibraryBundle} action bundle that contains all the parameters
     *               necessary to execute the action.
     * @param monitor an {@link IProgressMonitor}.
     * @return an {@link IStatus} with the status of the action.
*/
    private IStatus unlinkLibrary(UnlinkLibraryBundle bundle, IProgressMonitor monitor) {
        try {
            IProject project = bundle.mProject.getProject();

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
                    if (list.get(i).equals(bundle.mLibrary)) {
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
                                originalLibrary.equals(bundle.mLibrary)) {
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
            AdtPlugin.log(e, "Failure to unlink %1$s from %2$s", //$NON-NLS-1$
                    bundle.mLibrary.getName(),
                    bundle.mProject.getProject().getName());
            return e.getStatus();
}
}

/**
     * Updates all existing projects with a given list of new/updated libraries.
     * This loops through all opened projects and check if they depend on any of the given
     * library project, and if they do, they are linked together.
     * @param libraries the list of new/updated library projects.
*/
    private void updateProjectsWithNewLibraries(List<ProjectState> libraries) {
        if (libraries.size() == 0) {
            return;
        }

        ArrayList<ProjectState> updatedLibraries = new ArrayList<ProjectState>();
        synchronized (sLock) {
            // for each projects, look for projects that depend on it, and update them.
            // Once they are updated (meaning ProjectState#needs() has been called on them),
            // we add them to the list so that can be updated as well.
            for (ProjectState projectState : sProjectStateMap.values()) {
                // list for all the new library dependencies we find.
                ArrayList<IProject> libsToLink = new ArrayList<IProject>();

                for (ProjectState library : libraries) {
                    // Normally we would only need to test if ProjectState#needs returns non null,
                    // meaning the link between the project and the library has not been
                    // done yet.
                    // However what matters here is that the library is a dependency,
                    // period. If the library project was updated, then we redo the link,
                    // with all indirect dependencies (which *have* changed, since this is
                    // what this method is all about.)
                    // We still need to call ProjectState#needs to make the link in case it's not
                    // been done yet (which can happen if the library project was just opened).
                    if (projectState != library) {
                        LibraryState libState = projectState.needs(library);
                        if (libState != null || projectState.dependsOn(library)) {
                            // we have a match. Add it.
                            IProject libProject = library.getProject();

                            // library could already be here if it was an indirect dependencies
                            // from a previously processed updated library.
                            if (libsToLink.contains(libProject) == false) {
                                libsToLink.add(libProject);
                            }

                            // now find what this depends on, and add it too.
                            // The order here doesn't matter
                            // as it's just to add the linked source folder, so there's no
                            // need to use ProjectState#getFullLibraryProjects() which
                            // could return project that have already been added anyway.
                            fillProjectDependenciesList(library, libsToLink);
                        }
                    }
                }

                if (libsToLink.size() > 0) {
                    // create an action bundle for this link
                    LinkLibraryBundle bundle = new LinkLibraryBundle();
                    bundle.mProject = projectState.getProject();
                    bundle.mLibraryProjects = libsToLink.toArray(
                            new IProject[libsToLink.size()]);
                    bundle.mPreviousLibraryPath = null;
                    bundle.mCleanupCPE = false;
                    startActionBundle(bundle);

                    // if this updated project is a library, add it to the list, so that
                    // projects depending on it get updated too.
                    if (projectState.isLibrary() &&
                            updatedLibraries.contains(projectState) == false) {
                        updatedLibraries.add(projectState);
                    }
}
}
        }

        // done, but there may be updated projects that were libraries, so we need to do the same
        // for this libraries, to update the project there were depending on.
        updateProjectsWithNewLibraries(updatedLibraries);
}

/**







