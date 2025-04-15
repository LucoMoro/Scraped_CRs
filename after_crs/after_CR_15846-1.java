/*Improve library link/unlink again.

Change-Id:I808a8f1536171ebebca3a9bb5370e9dc0b6433d5*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/ProjectState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/ProjectState.java
//Synthetic comment -- index 05045a2..dd09355 100644

//Synthetic comment -- @@ -232,18 +232,25 @@
}

public static class LibraryDifference {
        public boolean removed = false;
public boolean added = false;

public boolean hasDiff() {
            return removed || added;
}
}

/**
* Reloads the content of the properties.
     * <p/>This also reset the reference to the target as it may have changed, therefore this
     * should be followed by a call to {@link Sdk#loadTarget(ProjectState)}.
     *
     * <p/>If the project libraries changes, they are updated to a certain extent.<br>
     * Removed libraries are removed from the state list, and added to the {@link LibraryDifference}
     * object that is returned so that they can be processed.<br>
     * Added libraries are added to the state (as new {@link LibraryState} objects), but their
     * IProject is not resolved. {@link ProjectState#needs(ProjectState)} should be called
     * afterwards to properly initialize the libraries.
*
* @return an instance of {@link LibraryDifference} describing the change in libraries.
*/
//Synthetic comment -- @@ -295,7 +302,7 @@
}

// whatever's left in oldLibraries is removed.
            diff.removed = oldLibraries.size() > 0;

// update the library with what IProjet are known at the time.
updateFullLibraryList();
//Synthetic comment -- @@ -595,7 +602,7 @@
* Update the full library list, including indirect dependencies. The result is returned by
* {@link #getFullLibraryProjects()}.
*/
    void updateFullLibraryList() {
ArrayList<IProject> list = new ArrayList<IProject>();
synchronized (mLibraries) {
buildFullLibraryDependencies(mLibraries, list);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 02b538e..4f12dc0 100644

//Synthetic comment -- @@ -86,6 +86,7 @@
*/
public final class Sdk  {
private static final String PROP_LIBRARY = "_library"; //$NON-NLS-1$
    private static final String PROP_LIBRARY_NAME = "_library_name"; //$NON-NLS-1$
public static final String CREATOR_ADT = "ADT";        //$NON-NLS-1$
public static final String PROP_CREATOR = "_creator";  //$NON-NLS-1$
private final static Object sLock = new Object();
//Synthetic comment -- @@ -744,6 +745,9 @@
for (ProjectState projectState : sProjectStateMap.values()) {
LibraryState libState = projectState.getLibrary(project);
if (libState != null) {
                            // get the current libraries.
                            IProject[] oldLibraries = projectState.getFullLibraryProjects();

// the unlink below will work in the job, but we need to close
// the library right away.
// This is because in case of a rename of a project, projectClosed and
//Synthetic comment -- @@ -752,11 +756,16 @@
// state up to date.
libState.close();


// edit the project to remove the linked source folder.
// this also calls LibraryState.close();
                            LinkLibraryBundle bundle = getLinkBundle(projectState, oldLibraries);
                            if (bundle != null) {
                                startActionBundle(bundle);
                            }

if (projectState.isLibrary()) {
                                System.out.println("QUEUE: " + projectState.getProject().getName() + " < closed (" + project.getName() + ")");
updatedLibraries.add(projectState);
}
}
//Synthetic comment -- @@ -826,9 +835,8 @@
// action bundle to the queue.
LinkLibraryBundle bundle = new LinkLibraryBundle();
bundle.mProject = openedProject;
                        bundle.mNewLibraryProjects = libsToLink.toArray(
new IProject[libsToLink.size()]);
bundle.mCleanupCPE = true;
startActionBundle(bundle);
}
//Synthetic comment -- @@ -841,6 +849,7 @@
if (openedState.isLibrary()) {
setupLibraryProject(openedProject);

                    System.out.println("QUEUE: " + openedState.getProject().getName() + " < opened");
mOpenedLibraryProjects.add(openedState);
}
}
//Synthetic comment -- @@ -872,7 +881,7 @@
if (libState != null) {
LinkLibraryBundle bundle = new LinkLibraryBundle();
bundle.mProject = projectState.getProject();
                                bundle.mNewLibraryProjects = new IProject[] {
libState.getProjectState().getProject() };
bundle.mCleanupCPE = false;
startActionBundle(bundle);
//Synthetic comment -- @@ -903,6 +912,9 @@
// get the current library flag
boolean wasLibrary = state.isLibrary();

                    // get the current list of project dependencies
                    IProject[] oldLibraries = state.getFullLibraryProjects();

LibraryDifference diff = state.reloadProperties();

// load the (possibly new) target.
//Synthetic comment -- @@ -915,45 +927,29 @@

// reload the libraries if needed
if (diff.hasDiff()) {
if (diff.added) {
synchronized (sLock) {
for (ProjectState projectState : sProjectStateMap.values()) {
if (projectState != state) {
                                        // need to call needs to do the libraryState link,
                                        // but no need to look at the result, as we'll compare
                                        // the result of getFullLibraryProjects()
                                        // this is easier to due to indirect dependencies.
                                        state.needs(projectState);
}
}
}
                        }

                        // and build the real difference. A list of new projects and a list of
                        // removed project.
                        // This is not the same as the added/removed libraries because libraries
                        // could be indirect dependencies through several different direct
                        // dependencies so it's easier to compare the full lists before and after
                        // the reload.
                        LinkLibraryBundle bundle = getLinkBundle(state, oldLibraries);
                        if (bundle != null) {
                            startActionBundle(bundle);
}
}

//Synthetic comment -- @@ -1015,15 +1011,17 @@
private static class LinkLibraryBundle implements ActionBundle {

/** The main project receiving the library links. */
        IProject mProject = null;
/** The libraries to add to the main project. */
        IProject[] mNewLibraryProjects = null;
/** an optional old library path that needs to be removed at the same time as the new
* libraries are added. Can be <code>null</code> in which case no libraries are removed. */
        IPath mDeletedLibraryPath = null;
        /** a list (possibly null/empty) of projects that should be unlinked */
        IProject[] mRemovedLibraryProjects = null;
/** Whether unknown IClasspathEntry (that were flagged as being added by ADT) are to be
* removed. This is typically only set to <code>true</code> when the project is opened. */
        boolean mCleanupCPE = false;

public BundleType getType() {
return BundleType.LINK_LIBRARY;
//Synthetic comment -- @@ -1038,7 +1036,7 @@
return String.format("LinkLibraryBundle: %1$s (%2$s) > %3$s", //$NON-NLS-1$
mProject.getName(),
mCleanupCPE,
                    Arrays.toString(mNewLibraryProjects));
}
}

//Synthetic comment -- @@ -1047,13 +1045,13 @@
*
* @see Sdk#unlinkLibrary(UnlinkLibraryBundle, IProgressMonitor)
*/
    private static class UnlinkLibraryBundle2 implements ActionBundle {
/** the main project */
final ProjectState mProject;
/** the library to remove */
final IProject mLibrary;

        UnlinkLibraryBundle2(ProjectState project, IProject library) {
mProject = project;
mLibrary = library;
}
//Synthetic comment -- @@ -1113,19 +1111,22 @@
linkProjectAndLibrary((LinkLibraryBundle)bundle, monitor);
break;
case UNLINK_LIBRARY:
                                    unlinkLibrary((UnlinkLibraryBundle2) bundle, monitor);
break;
}
} catch (Exception e) {
AdtPlugin.log(e, "Failed to process bundle: %1$s", //$NON-NLS-1$
bundle.toString());
}

                        try {
                            // force a recompile
                            bundle.getProject().build(
                                    IncrementalProjectBuilder.FULL_BUILD, monitor);
                        } catch (Exception e) {
                            // no need to log those.
                        }

// remove it from the list.
synchronized (mActionBundleQueue) {
mActionBundleQueue.remove(0);
//Synthetic comment -- @@ -1279,28 +1280,34 @@
ArrayList<IProject> list = new ArrayList<IProject>(Arrays.asList(refs));

// remove a previous library if needed (in case of a rename)
                if (bundle.mDeletedLibraryPath != null) {
                    // since project basically have only one segment that matter,
                    // just check the names
                    removeFromList(list, bundle.mDeletedLibraryPath.lastSegment());
}

                if (bundle.mRemovedLibraryProjects != null) {
                    for (IProject removedProject : bundle.mRemovedLibraryProjects) {
                        removeFromList(list, removedProject.getName());
                    }
                }

                // add the new ones if they don't exist
                if (bundle.mNewLibraryProjects != null) {
                    for (IProject newProject : bundle.mNewLibraryProjects) {
                        if (list.contains(newProject) == false) {
                            list.add(newProject);
                        }
                    }
                }

// set the changed list
projectDescription.setDynamicReferences(
list.toArray(new IProject[list.size()]));
} else {
                if (bundle.mNewLibraryProjects != null) {
                    projectDescription.setDynamicReferences(bundle.mNewLibraryProjects);
                }
}

// get the current classpath entries for the project to add the new source
//Synthetic comment -- @@ -1313,116 +1320,125 @@
IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();

// loop on the classpath entries and look for CPE_SOURCE entries that
            // are linked folders, then record them for comparison later as we add the new
// ones.
            ArrayList<IClasspathEntry> cpeToRemove = new ArrayList<IClasspathEntry>();
            for (IClasspathEntry classpathEntry : classpathEntries) {
                if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                    IPath path = classpathEntry.getPath();
                    IResource linkedRes = wsRoot.findMember(path);
                    if (linkedRes != null && linkedRes.isLinked() &&
                            CREATOR_ADT.equals(ProjectHelper.loadStringProperty(
                                    linkedRes, PROP_CREATOR))) {

                        // add always to list if we're doing clean-up
                        if (bundle.mCleanupCPE) {
                            cpeToRemove.add(classpathEntry);
                        } else {
                            String libName = ProjectHelper.loadStringProperty(linkedRes,
                                    PROP_LIBRARY_NAME);
                            if (libName != null && isRemovedLibrary(bundle, libName)) {
                                cpeToRemove.add(classpathEntry);
                            }
}
}
}
}

// loop on the projects to add.
            if (bundle.mNewLibraryProjects != null) {
                for (IProject library : bundle.mNewLibraryProjects) {
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

                        // look for an existing CPE that has the same linked path and that was
                        // going to be removed.
                        IClasspathEntry match = findClasspathEntryMatch(cpeToRemove, linkedPath,
                                null);

                        if (match == null) {
                            // no match, create one
                            // get a string version, to make up the linked folder name
                            String srcFolderName = relativePath.toString().replace(
                                    "/",  //$NON-NLS-1$
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
                            libSrc.createLink(linkedPath, IResource.REPLACE, monitor);

                            // mark it as derived so that Team plug-in ignore this
                            libSrc.setDerived(true);

                            // set some persistent properties on it to know that it was
                            // created by ADT.
                            ProjectHelper.saveStringProperty(libSrc, PROP_CREATOR, CREATOR_ADT);
                            ProjectHelper.saveResourceProperty(libSrc, PROP_LIBRARY, library);
                            ProjectHelper.saveStringProperty(libSrc, PROP_LIBRARY_NAME,
                                    library.getName());

                            // add the source folder to the classpath entries
                            classpathEntries.add(JavaCore.newSourceEntry(libSrcPath));
                        } else {
                            // there's a valid match, do nothing, but remove the match from
                            // the list of previously existing CPE.
                            cpeToRemove.remove(match);
                        }
}
}
}

            // remove the CPE that should be removed.
            classpathEntries.removeAll(cpeToRemove);

// set the new list
javaProject.setRawClasspath(
classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]),
monitor);

            // and delete the folders of the CPE that were removed (must be done after)
            for (IClasspathEntry cpe : cpeToRemove) {
                IResource res = wsRoot.findMember(cpe.getPath());
                res.delete(true, monitor);
}

return Status.OK_STATUS;
//Synthetic comment -- @@ -1434,6 +1450,102 @@
}
}

    private boolean isRemovedLibrary(LinkLibraryBundle bundle, String libName) {
        if (bundle.mDeletedLibraryPath != null &&
                libName.equals(bundle.mDeletedLibraryPath.lastSegment())) {
            return true;
        }

        if (bundle.mRemovedLibraryProjects != null) {
            for (IProject removedProject : bundle.mRemovedLibraryProjects) {
                if (libName.equals(removedProject.getName())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Computes the library difference based on a previous list and a current state, and create
     * a {@link LinkLibraryBundle} action to update the given project.
     * @param project The current project state
     * @param oldLibraries the list of old libraries. Typically the result of
     *            {@link ProjectState#getFullLibraryProjects()} before the ProjectState is updated.
     * @return null if there no action to take, or a {@link LinkLibraryBundle} object to run.
     */
    private LinkLibraryBundle getLinkBundle(ProjectState project, IProject[] oldLibraries) {
        // get the new full list of projects
        IProject[] newLibraries = project.getFullLibraryProjects();

        // and build the real difference. A list of new projects and a list of
        // removed project.
        // This is not the same as the added/removed libraries because libraries
        // could be indirect dependencies through several different direct
        // dependencies so it's easier to compare the full lists before and after
        // the reload.

        List<IProject> addedLibs = new ArrayList<IProject>();
        List<IProject> removedLibs = new ArrayList<IProject>();

        // first get the list of new projects.
        for (IProject newLibrary : newLibraries) {
            boolean found = false;
            for (IProject oldLibrary : oldLibraries) {
                if (newLibrary.equals(oldLibrary)) {
                    found = true;
                    break;
                }
            }

            // if it was not found in the old libraries, it's really new
            if (found == false) {
                addedLibs.add(newLibrary);
            }
        }

        // now the list of removed projects.
        for (IProject oldLibrary : oldLibraries) {
            boolean found = false;
            for (IProject newLibrary : newLibraries) {
                if (newLibrary.equals(oldLibrary)) {
                    found = true;
                    break;
                }
            }

            // if it was not found in the new libraries, it's really been removed
            if (found == false) {
                removedLibs.add(oldLibrary);
            }
        }

        if (addedLibs.size() > 0 || removedLibs.size() > 0) {
            LinkLibraryBundle bundle = new LinkLibraryBundle();
            bundle.mProject = project.getProject();
            bundle.mNewLibraryProjects =
                addedLibs.toArray(new IProject[addedLibs.size()]);
            bundle.mRemovedLibraryProjects =
                removedLibs.toArray(new IProject[removedLibs.size()]);
            return bundle;
        }

        return null;
    }

    private void removeFromList(List<IProject> projects, String name) {
        final int count = projects.size();
        for (int i = 0 ; i < count ; i++) {
            // since project basically have only one segment that matter,
            // just check the names
            if (projects.get(i).getName().equals(name)) {
                projects.remove(i);
                return;
            }
        }
    }

/**
* Returns a {@link IClasspathEntry} from the given list whose linked path match the given path.
* @param cpeList a list of {@link IClasspathEntry} of {@link IClasspathEntry#getEntryKind()}
//Synthetic comment -- @@ -1474,7 +1586,7 @@
* @param monitor an {@link IProgressMonitor}.
* @return an {@link IStatus} with the status of the action.
*/
    private IStatus unlinkLibrary(UnlinkLibraryBundle2 bundle, IProgressMonitor monitor) {
try {
IProject project = bundle.mProject.getProject();

//Synthetic comment -- @@ -1581,9 +1693,10 @@
// Once they are updated (meaning ProjectState#needs() has been called on them),
// we add them to the list so that can be updated as well.
for (ProjectState projectState : sProjectStateMap.values()) {
                // record the current library dependencies
                IProject[] oldLibraries = projectState.getFullLibraryProjects();

                boolean needLibraryDependenciesUpdated = false;
for (ProjectState library : libraries) {
// Normally we would only need to test if ProjectState#needs returns non null,
// meaning the link between the project and the library has not been
//Synthetic comment -- @@ -1595,41 +1708,33 @@
// We still need to call ProjectState#needs to make the link in case it's not
// been done yet (which can happen if the library project was just opened).
if (projectState != library) {
                        // call needs in case this new library was just opened, and the link needs
                        // to be done
LibraryState libState = projectState.needs(library);
                        if (libState == null && projectState.dependsOn(library)) {
                            // ProjectState.needs only returns true if the library was needed.
                            // but we also need to check the case where the project depends on
                            // the library but the link was already done.
                            needLibraryDependenciesUpdated = true;
}
}
}

                if (needLibraryDependenciesUpdated) {
                    projectState.updateFullLibraryList();
                }

                LinkLibraryBundle bundle = getLinkBundle(projectState, oldLibraries);
                if (bundle != null) {
startActionBundle(bundle);

// if this updated project is a library, add it to the list, so that
// projects depending on it get updated too.
if (projectState.isLibrary() &&
updatedLibraries.contains(projectState) == false) {
                        System.out.println("QUEUE: " + projectState.getProject().getName() +
                                " < updated (added: " + Arrays.toString(bundle.mNewLibraryProjects) +
                                " / removed" + Arrays.toString(bundle.mRemovedLibraryProjects) + ") > ");
updatedLibraries.add(projectState);
}
}







