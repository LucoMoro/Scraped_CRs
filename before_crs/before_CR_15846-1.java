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
//Synthetic comment -- index 02b538e..4f12dc0 100644

//Synthetic comment -- @@ -86,6 +86,7 @@
*/
public final class Sdk  {
private static final String PROP_LIBRARY = "_library"; //$NON-NLS-1$
public static final String CREATOR_ADT = "ADT";        //$NON-NLS-1$
public static final String PROP_CREATOR = "_creator";  //$NON-NLS-1$
private final static Object sLock = new Object();
//Synthetic comment -- @@ -744,6 +745,9 @@
for (ProjectState projectState : sProjectStateMap.values()) {
LibraryState libState = projectState.getLibrary(project);
if (libState != null) {
// the unlink below will work in the job, but we need to close
// the library right away.
// This is because in case of a rename of a project, projectClosed and
//Synthetic comment -- @@ -752,11 +756,16 @@
// state up to date.
libState.close();

// edit the project to remove the linked source folder.
// this also calls LibraryState.close();
                            startActionBundle(new UnlinkLibraryBundle(projectState, project));

if (projectState.isLibrary()) {
updatedLibraries.add(projectState);
}
}
//Synthetic comment -- @@ -826,9 +835,8 @@
// action bundle to the queue.
LinkLibraryBundle bundle = new LinkLibraryBundle();
bundle.mProject = openedProject;
                        bundle.mLibraryProjects = libsToLink.toArray(
new IProject[libsToLink.size()]);
                        bundle.mPreviousLibraryPath = null;
bundle.mCleanupCPE = true;
startActionBundle(bundle);
}
//Synthetic comment -- @@ -841,6 +849,7 @@
if (openedState.isLibrary()) {
setupLibraryProject(openedProject);

mOpenedLibraryProjects.add(openedState);
}
}
//Synthetic comment -- @@ -872,7 +881,7 @@
if (libState != null) {
LinkLibraryBundle bundle = new LinkLibraryBundle();
bundle.mProject = projectState.getProject();
                                bundle.mLibraryProjects = new IProject[] {
libState.getProjectState().getProject() };
bundle.mCleanupCPE = false;
startActionBundle(bundle);
//Synthetic comment -- @@ -903,6 +912,9 @@
// get the current library flag
boolean wasLibrary = state.isLibrary();

LibraryDifference diff = state.reloadProperties();

// load the (possibly new) target.
//Synthetic comment -- @@ -915,45 +927,29 @@

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

//Synthetic comment -- @@ -1015,15 +1011,17 @@
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
//Synthetic comment -- @@ -1038,7 +1036,7 @@
return String.format("LinkLibraryBundle: %1$s (%2$s) > %3$s", //$NON-NLS-1$
mProject.getName(),
mCleanupCPE,
                    Arrays.toString(mLibraryProjects));
}
}

//Synthetic comment -- @@ -1047,13 +1045,13 @@
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
//Synthetic comment -- @@ -1113,19 +1111,22 @@
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
//Synthetic comment -- @@ -1279,28 +1280,34 @@
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
//Synthetic comment -- @@ -1313,116 +1320,125 @@
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
//Synthetic comment -- @@ -1434,6 +1450,102 @@
}
}

/**
* Returns a {@link IClasspathEntry} from the given list whose linked path match the given path.
* @param cpeList a list of {@link IClasspathEntry} of {@link IClasspathEntry#getEntryKind()}
//Synthetic comment -- @@ -1474,7 +1586,7 @@
* @param monitor an {@link IProgressMonitor}.
* @return an {@link IStatus} with the status of the action.
*/
    private IStatus unlinkLibrary(UnlinkLibraryBundle bundle, IProgressMonitor monitor) {
try {
IProject project = bundle.mProject.getProject();

//Synthetic comment -- @@ -1581,9 +1693,10 @@
// Once they are updated (meaning ProjectState#needs() has been called on them),
// we add them to the list so that can be updated as well.
for (ProjectState projectState : sProjectStateMap.values()) {
                // list for all the new library dependencies we find.
                ArrayList<IProject> libsToLink = new ArrayList<IProject>();

for (ProjectState library : libraries) {
// Normally we would only need to test if ProjectState#needs returns non null,
// meaning the link between the project and the library has not been
//Synthetic comment -- @@ -1595,41 +1708,33 @@
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







