/*Make sure files opened from XML wizard go to the WYSIWYG editor

Change-Id:I51a0d96564b5fa896eb4daef50f193df7cadfa20*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 6018d17..43fd823 100644

//Synthetic comment -- @@ -1810,7 +1810,8 @@
}

/**
     * Opens the given file and shows the given (optional) region in the editor (or
     * if no region is specified, opens the editor tab.)
*
* @param file the file to be opened
* @param region an optional region which if set will be selected and shown to the
//Synthetic comment -- @@ -1818,6 +1819,20 @@
* @throws PartInitException if something goes wrong
*/
public static void openFile(IFile file, IRegion region) throws PartInitException {
        openFile(file, region, true);
    }

    /**
     * Opens the given file and shows the given (optional) region
     *
     * @param file the file to be opened
     * @param region an optional region which if set will be selected and shown to the
     *            user
     * @param showEditorTab if true, front the editor tab after opening the file
     * @throws PartInitException if something goes wrong
     */
    public static void openFile(IFile file, IRegion region, boolean showEditorTab)
            throws PartInitException {
IWorkbench workbench = PlatformUI.getWorkbench();
if (workbench == null) {
return;
//Synthetic comment -- @@ -1835,7 +1850,7 @@
AndroidXmlEditor editor = (AndroidXmlEditor) targetEditor;
if (region != null) {
editor.show(region.getOffset(), region.getLength());
            } else if (showEditorTab) {
editor.setActivePage(AndroidXmlEditor.TEXT_EDITOR_ID);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java
//Synthetic comment -- index 9c0d31c..fd6bca8 100644

//Synthetic comment -- @@ -111,11 +111,10 @@
return false;
} else {
IFile file = created.getFirst();

            // Open the file
try {
                AdtPlugin.openFile(file, null, false /* showEditorTab */);
} catch (PartInitException e) {
AdtPlugin.log(e, "Failed to create %1$s: missing type",  //$NON-NLS-1$
file.getFullPath().toString());







