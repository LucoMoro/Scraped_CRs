/*ADT support for libraries depending on libraries.

Change-Id:Ifcab6c8d1c8e4c7e9370bfab76d7e6ad79707168*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index c3dc894..72d7b7f 100644

//Synthetic comment -- @@ -736,20 +736,22 @@

// 2. if the project is a library, make sure to update the
// LibraryState for any main project using this.
                    for (ProjectState projectState : sProjectStateMap.values()) {
                        LibraryState libState = projectState.getLibrary(project);
                        if (libState != null) {
                            // the unlink below will work in the job, but we need to close
                            // the library right away.
                            // This is because in case of a rename of a project, projectClosed and
                            // projectOpened will be called before any other job is run, so we
                            // need to make sure projectOpened is closed with the main project
                            // state up to date.
                            libState.close();

                            // edit the project to remove the linked source folder.
                            // this also calls LibraryState.close();
                            unlinkLibrary(projectState, project, true /*doInJob*/);
}
}

//Synthetic comment -- @@ -773,96 +775,134 @@
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

        public void projectRenamed(IProject project, IPath from) {
            // a project was renamed.
            // if the project is a library, look for any project that depended on it
            // and update it. (default.properties and linked source folder)
            ProjectState renamedState = getProjectState(project);
            if (renamedState.isLibrary()) {
                // remove the variable
                disposeLibraryProject(from.lastSegment());

                // update the project depending on the library
                synchronized (sLock) {
                    for (ProjectState projectState : sProjectStateMap.values()) {
                        if (projectState != renamedState && projectState.isMissingLibraries()) {
                            IPath oldRelativePath = makeRelativeTo(from,
                                    projectState.getProject().getFullPath());

                            IPath newRelativePath = makeRelativeTo(project.getFullPath(),
                                    projectState.getProject().getFullPath());

                            // update the library for the main project.
                            LibraryState libState = projectState.updateLibrary(
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
                }
}
}
};
//Synthetic comment -- @@ -911,7 +951,7 @@

if (libState != null) {
linkProjectAndLibrary(state, libState, null,
                                                            false /*doInJob*/);
}
}
}
//Synthetic comment -- @@ -997,6 +1037,67 @@
}

/**
* Links a project and a library so that the project can use the library code and resources.
* <p/>This can be done in a job in case the workspace is not locked for resource
* modification. See <var>doInJob</var>.
//Synthetic comment -- @@ -1005,169 +1106,107 @@
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








