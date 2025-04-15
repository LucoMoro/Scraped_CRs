/*NPE in FolderDecorater

Change-Id:Ica70dedb0b86ab877337d001aac6a205b15a60c0*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/FolderDecorator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/FolderDecorator.java
//Synthetic comment -- index b7ba2fe..bc322d9 100644

//Synthetic comment -- @@ -49,6 +49,9 @@

// get the project and make sure this is an android project
IProject project = folder.getProject();

try {
if (project.hasNature(AndroidConstants.NATURE_DEFAULT)) {







