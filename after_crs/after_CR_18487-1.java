/*Gen folder should not be derived.

This fix the issue where gen is not present after project
creation (it was actually created then deleted as part of a clean
up phase)

Change-Id:I25cd67c076ed401b482d97292640c08a2a7a5830*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java
//Synthetic comment -- index 2a9f231..6258268 100644

//Synthetic comment -- @@ -652,12 +652,6 @@
IJavaProject javaProject = JavaCore.create(project);
setupSourceFolders(javaProject, sourceFolders, monitor);

if (((Boolean) parameters.get(PARAM_IS_NEW_PROJECT)).booleanValue()) {
// Create files in the project if they don't already exist
addManifest(project, parameters, dictionary, monitor);







