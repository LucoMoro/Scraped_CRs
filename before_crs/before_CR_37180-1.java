/*Fix LayoutEditorMatchingStrategy to handle non-layout files

If you have both res/menu/main.xml and res/layout/main.xml, and the
menu file is open, then double clicking on the layout file would *not*
open up the layout. There was a bug in LayoutEditorMatchingStrategy
which meant that it only considered the project and base names of the
files, not whether the existing editor represented a layout file.

Change-Id:Idaf7c1b145b60510f4913896d7d0b2162744a399*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorMatchingStrategy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorMatchingStrategy.java
//Synthetic comment -- index 6a6b99c..4ea49f9 100644

//Synthetic comment -- @@ -42,7 +42,8 @@

// get the IFile object and check it's in one of the layout folders.
IFile iFile = fileInput.getFile();
            ResourceFolder resFolder = ResourceManager.getInstance().getResourceFolder(iFile);

// Per the IEditorMatchingStrategy documentation, editorRef.getEditorInput()
// is expensive so try exclude files that definitely don't match, such
//Synthetic comment -- @@ -61,6 +62,12 @@
FileEditorInput editorFileInput = (FileEditorInput)editorInput;
IFile editorIFile = editorFileInput.getFile();

return editorIFile.getProject().equals(iFile.getProject())
&& editorIFile.getName().equals(iFile.getName());
}







