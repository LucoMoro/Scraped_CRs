/*More gracefully handle restarting IDE with deleted files

Change-Id:I024103c1456a88f230db9b9d5aaf0fe2126a315e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java
//Synthetic comment -- index a07e55e..1015d7d 100644

//Synthetic comment -- @@ -261,6 +261,9 @@
if (input instanceof FileEditorInput) {
FileEditorInput fileInput = (FileEditorInput)input;
editedFile = fileInput.getFile();
                if (!editedFile.isAccessible()) {
                    return;
                }
} else {
AdtPlugin.log(IStatus.ERROR,
"Input is not of type FileEditorInput: %1$s",  //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutWindowCoordinator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutWindowCoordinator.java
//Synthetic comment -- index d40af29..56b86aa 100644

//Synthetic comment -- @@ -232,7 +232,10 @@
*
* @param editor the editor to sync
*/
    private void sync(@Nullable GraphicalEditorPart editor) {
        if (editor == null) {
            return;
        }
if (mEditorMaximized) {
editor.showStructureViews(true /*outline*/, true /*properties*/, true /*layout*/);
} else if (mOutlineOpen) {







