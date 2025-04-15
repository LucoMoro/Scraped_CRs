/*Improve library link/unlink again.

Change-Id:I808a8f1536171ebebca3a9bb5370e9dc0b6433d5*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/ProjectState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/ProjectState.java
//Synthetic comment -- index 05045a2..dd09355 100644

//Synthetic comment -- @@ -232,18 +232,25 @@
}

public static class LibraryDifference {
        public List<LibraryState> removed = new ArrayList<LibraryState>();
public boolean added = false;

public boolean hasDiff() {
            return removed.size() > 0 || added;
}
}

/**
* Reloads the content of the properties.
     * <p/>This also reset the reference to the target as it may have changed.
     * <p/>This should be followed by a call to {@link Sdk#loadTarget(ProjectState)}.
*
* @return an instance of {@link LibraryDifference} describing the change in libraries.
*/
//Synthetic comment -- @@ -295,7 +302,7 @@
}

// whatever's left in oldLibraries is removed.
            diff.removed.addAll(oldLibraries);

// update the library with what IProjet are known at the time.
updateFullLibraryList();
//Synthetic comment -- @@ -595,7 +602,7 @@
* Update the full library list, including indirect dependencies. The result is returned by
* {@link #getFullLibraryProjects()}.
*/
    private void updateFullLibraryList() {
ArrayList<IProject> list = new ArrayList<IProject>();
synchronized (mLibraries) {
buildFullLibraryDependencies(mLibraries, list);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 02b538e..61109a7 100644

//Synthetic comment -- @@ -86,6 +86,7 @@
*/
public final class Sdk  {
private static final String PROP_LIBRARY = "_library"; //$NON-NLS-1$
public static final String CREATOR_ADT = "ADT";        //$NON-NLS-1$
public static final String PROP_CREATOR = "_creator";  //$NON-NLS-1$
private final static Object sLock = new Object();
//Synthetic comment -- @@ -716,6 +717,7 @@
}

private void onProjectRemoved(IProject project, boolean deleted) {
// get the target project
synchronized (sLock) {
// Don't use getProject() as it could create the ProjectState if it's not
//Synthetic comment -- @@ -744,6 +746,9 @@
for (ProjectState projectState : sProjectStateMap.values()) {
LibraryState libState = projectState.getLibrary(project);
if (libState != null) {
// the unlink below will work in the job, but we need to close
// the library right away.
// This is because in case of a rename of a project, projectClosed and
//Synthetic comment -- @@ -752,9 +757,13 @@
// state up to date.
libState.close();

// edit the project to remove the linked source folder.
// this also calls LibraryState.close();
                            startActionBundle(new UnlinkLibraryBundle(projectState, project));

if (projectState.isLibrary()) {
updatedLibraries.add(projectState);
//Synthetic comment -- @@ -786,6 +795,7 @@
}

private void onProjectOpened(final IProject openedProject) {
ProjectState openedState = getProjectState(openedProject);
if (openedState != null) {
if (openedState.hasLibraries()) {
//Synthetic comment -- @@ -821,17 +831,14 @@
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
//Synthetic comment -- @@ -847,6 +854,7 @@
}

public void projectRenamed(IProject project, IPath from) {
// a project was renamed.
// if the project is a library, look for any project that depended on it
// and update it. (default.properties and linked source folder)
//Synthetic comment -- @@ -865,17 +873,27 @@
IPath newRelativePath = makeRelativeTo(project.getFullPath(),
projectState.getProject().getFullPath());

// update the library for the main project.
LibraryState libState = projectState.updateLibrary(
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
//Synthetic comment -- @@ -903,6 +921,9 @@
// get the current library flag
boolean wasLibrary = state.isLibrary();

LibraryDifference diff = state.reloadProperties();

// load the (possibly new) target.
//Synthetic comment -- @@ -915,45 +936,29 @@

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

if (diff.added) {
                            ArrayList<IProject> libsToLink = new ArrayList<IProject>();
synchronized (sLock) {
for (ProjectState projectState : sProjectStateMap.values()) {
if (projectState != state) {
                                        LibraryState libState = state.needs(projectState);

                                        if (libState != null) {
                                            IProject p = libState.getProjectState().getProject();
                                            if (libsToLink.contains(p) == false) {
                                                libsToLink.add(p);
                                            }

                                            // now find the dependencies of the library itself.
                                            fillProjectDependenciesList(
                                                    libState.getMainProjectState(), libsToLink);
                                        }
}
}
}

                            if (libsToLink.size() > 0) {
                                LinkLibraryBundle bundle = new LinkLibraryBundle();
                                bundle.mProject = iProject;
                                bundle.mLibraryProjects =
                                        libsToLink.toArray(new IProject[libsToLink.size()]);
                                bundle.mPreviousLibraryPath = null;
                                bundle.mCleanupCPE = false;
                                startActionBundle(bundle);
                            }
}
}

//Synthetic comment -- @@ -999,78 +1004,39 @@
};

/**
     * Action Bundle to be used with {@link Sdk#startActionBundle(ActionBundle)}.
     */
    private interface ActionBundle {
        enum BundleType { LINK_LIBRARY, UNLINK_LIBRARY };
        BundleType getType();
        IProject getProject();
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

        public IProject getProject() {
            return mProject;
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

        public IProject getProject() {
            return mProject.getProject();
        }
    }

    private final ArrayList<ActionBundle> mActionBundleQueue = new ArrayList<ActionBundle>();

/**
     * Runs the given action bundle through a job queue.
*
* All action bundles are executed in a job in the exact order they are added.
* This is convenient when several actions must be executed in a job consecutively (instead
//Synthetic comment -- @@ -1083,55 +1049,50 @@
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

                            // force a recompile
                            bundle.getProject().build(
                                    IncrementalProjectBuilder.FULL_BUILD, monitor);

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
//Synthetic comment -- @@ -1249,24 +1210,27 @@
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
if (bundle.mProject.isOpen() == false) {
return Status.OK_STATUS;
}
try {
//Synthetic comment -- @@ -1279,28 +1243,34 @@
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
//Synthetic comment -- @@ -1313,116 +1283,125 @@
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
                if (library.isOpen() == false) {
                    continue;
                }
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

                        // mark it as derived so that Team plug-in ignore this
                        libSrc.setDerived(true);

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
//Synthetic comment -- @@ -1434,6 +1413,107 @@
}
}

/**
* Returns a {@link IClasspathEntry} from the given list whose linked path match the given path.
* @param cpeList a list of {@link IClasspathEntry} of {@link IClasspathEntry#getEntryKind()}
//Synthetic comment -- @@ -1464,107 +1544,6 @@
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
//Synthetic comment -- @@ -1581,9 +1560,10 @@
// Once they are updated (meaning ProjectState#needs() has been called on them),
// we add them to the list so that can be updated as well.
for (ProjectState projectState : sProjectStateMap.values()) {
                // list for all the new library dependencies we find.
                ArrayList<IProject> libsToLink = new ArrayList<IProject>();

for (ProjectState library : libraries) {
// Normally we would only need to test if ProjectState#needs returns non null,
// meaning the link between the project and the library has not been
//Synthetic comment -- @@ -1595,36 +1575,25 @@
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







