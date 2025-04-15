/*Adds setting to suppress warnins in gen folder.http://code.google.com/p/android/issues/detail?id=43from Eclipse JUNO, each source folder has settings "Ignore optional compiler problems".
if set YES, warnings from java compiler are suppressed. This patch sets that flag
YES on project creation.

In some situations, warnings in gen folder is inevitable.
For example, if .aidl file contains Map as a return type, generated code
is warned because of rawtype(.aidl does not accept type parameters).
And we can't write @SuppressWarnings in generated code nor .aidl.

So we should simply suppress warnings in gen folder.

Change-Id:Ic41a16fb0ce779c0c45c669d378720afaba32fedSigned-off-by: Makoto Yamazaki <makoto1975@gmail.com>*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java
//Synthetic comment -- index 19a7101..0601bc5 100644

//Synthetic comment -- @@ -210,6 +210,8 @@
private static final String LAYOUT_TEMPLATE = "layout.template";            //$NON-NLS-1$
private static final String MAIN_LAYOUT_XML = "main.xml";                   //$NON-NLS-1$

private final NewProjectWizardState mValues;
private final IRunnableContext mRunnableContext;

//Synthetic comment -- @@ -719,7 +721,8 @@

// Setup class path: mark folders as source folders
IJavaProject javaProject = JavaCore.create(project);
        setupSourceFolders(javaProject, sourceFolders, monitor);

if (((Boolean) parameters.get(PARAM_IS_NEW_PROJECT)).booleanValue()) {
// Create files in the project if they don't already exist
//Synthetic comment -- @@ -1326,12 +1329,39 @@
* Adds the given folder to the project's class path.
*
* @param javaProject The Java Project to update.
     * @param sourceFolders Template Parameters.
* @param monitor An existing monitor.
* @throws JavaModelException if the classpath could not be set.
*/
    private void setupSourceFolders(IJavaProject javaProject, String[] sourceFolders,
IProgressMonitor monitor) throws JavaModelException {
IProject project = javaProject.getProject();

// get the list of entries.
//Synthetic comment -- @@ -1340,15 +1370,23 @@
// remove the project as a source folder (This is the default)
entries = removeSourceClasspath(entries, project);

        // add the source folders.
        for (String sourceFolder : sourceFolders) {
            IFolder srcFolder = project.getFolder(sourceFolder);

            // remove it first in case.
            entries = removeSourceClasspath(entries, srcFolder);
            entries = ProjectHelper.addEntryToClasspath(entries,
                    JavaCore.newSourceEntry(srcFolder.getFullPath()));
}

javaProject.setRawClasspath(entries, new SubProgressMonitor(monitor, 10));
}







