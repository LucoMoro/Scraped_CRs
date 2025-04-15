/*Add all new source folders in a single pass in new project wizard.

Change-Id:Id54bc449a64373b4ad1cb63ec48b72076c387414*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java
//Synthetic comment -- index d30a812..1bfff9f 100644

//Synthetic comment -- @@ -645,9 +645,7 @@

// Setup class path: mark folders as source folders
IJavaProject javaProject = JavaCore.create(project);
        for (String sourceFolder : sourceFolders) {
            setupSourceFolder(javaProject, sourceFolder, monitor);
        }

// Mark the gen source folder as derived
IFolder genSrcFolder = project.getFolder(AndroidConstants.WS_ROOT + GEN_SRC_DIRECTORY);
//Synthetic comment -- @@ -1030,19 +1028,25 @@
* @param monitor An existing monitor.
* @throws JavaModelException if the classpath could not be set.
*/
    private void setupSourceFolder(IJavaProject javaProject, String sourceFolder,
IProgressMonitor monitor) throws JavaModelException {
IProject project = javaProject.getProject();

        // Add "src" to class path
        IFolder srcFolder = project.getFolder(sourceFolder);

IClasspathEntry[] entries = javaProject.getRawClasspath();
        entries = removeSourceClasspath(entries, srcFolder);
        entries = removeSourceClasspath(entries, srcFolder.getParent());

        entries = ProjectHelper.addEntryToClasspath(entries,
                JavaCore.newSourceEntry(srcFolder.getFullPath()));

javaProject.setRawClasspath(entries, new SubProgressMonitor(monitor, 10));
}







