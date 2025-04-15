/*Make the error text participate in select all and copy

When there are rendering errors, the error messages are listed in a
StyledText widget which opens up at the bottom of the layout editor.

This changeset makes it possible to copy & paste the error message. It
does this by:

- Making the global Select All action operate on the error label when
  the error label has focus

- Make the global Copy action operate on the error label if the error
  label has selected text (which could have been made selected either
  by the Select All action above, or by a manual mouse selection)

Change-Id:Ifa58dcd22d86d2b78dfe121ea4676a35da1f88ca*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ClipboardSupport.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ClipboardSupport.java
//Synthetic comment -- index 540aedb..89b9f8e 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.sdklib.SdkConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
//Synthetic comment -- @@ -78,6 +79,8 @@
* <p/>
* This sanitizes the selection, so it must be a copy. It then inserts the
* selection both as text and as {@link SimpleElement}s in the clipboard.
     * (If there is selected text in the error label, then the error is used
     * as the text portion of the transferable.)
*
* @param selection A list of selection items to add to the clipboard;
*            <b>this should be a copy already - this method will not make a
//Synthetic comment -- @@ -86,13 +89,29 @@
public void copySelectionToClipboard(List<SelectionItem> selection) {
SelectionManager.sanitize(selection);

        // The error message area shares the copy action with the canvas. Invoking the
        // copy action when there are errors visible *AND* the user has selected text there,
        // should include the error message as the text transferable.
        String message = null;
        GraphicalEditorPart graphicalEditor = mCanvas.getLayoutEditor().getGraphicalEditor();
        StyledText errorLabel = graphicalEditor.getErrorLabel();
        if (errorLabel.getSelectionCount() > 0) {
            message = errorLabel.getSelectionText();
        }

if (selection.isEmpty()) {
            if (message != null) {
                mClipboard.setContents(
                        new Object[] { message },
                        new Transfer[] { TextTransfer.getInstance() }
                );
            }
return;
}

Object[] data = new Object[] {
SelectionItem.getAsElements(selection),
                message != null ? message : SelectionItem.getAsText(mCanvas, selection)
};

Transfer[] types = new Transfer[] {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 7fbf782..21e21ca 100644

//Synthetic comment -- @@ -1937,6 +1937,16 @@
private static class ClassLinkStyleRange extends StyleRange {}

/**
     * Returns the error label for the graphical editor (which may not be visible
     * or showing errors)
     *
     * @return the error label, never null
     */
    StyledText getErrorLabel() {
        return mErrorLabel;
    }

    /**
* Monitor clicks on the error label.
* If the click happens on a style range created by
* {@link GraphicalEditorPart#addClassLink(StyledText, String)}, we assume it's about
//Synthetic comment -- @@ -1969,6 +1979,9 @@
String link = mErrorLabel.getText(r.start, r.start + r.length - 1);
createNewClass(link);
}

            LayoutCanvas canvas = getCanvasControl();
            canvas.updateMenuActionState(canvas.getSelectionManager().getSelections().isEmpty());
}

private void createNewClass(String fqcn) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 17ade8a..a97aedd 100755

//Synthetic comment -- @@ -54,6 +54,7 @@
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
//Synthetic comment -- @@ -963,6 +964,13 @@
mSelectAllAction = new Action() {
@Override
public void run() {
                GraphicalEditorPart graphicalEditor = getLayoutEditor().getGraphicalEditor();
                StyledText errorLabel = graphicalEditor.getErrorLabel();
                if (errorLabel.isFocusControl()) {
                    errorLabel.selectAll();
                    return;
                }

mSelectionManager.selectAll();
}
};
//Synthetic comment -- @@ -985,10 +993,11 @@
* @param hasSelection True iff we have a non-empty selection
*/
/* package */ void updateMenuActionState(boolean hasSelection) {
        StyledText errorLabel = mLayoutEditor.getGraphicalEditor().getErrorLabel();
mCutAction.setEnabled(hasSelection);
        mCopyAction.setEnabled(hasSelection || errorLabel.getSelectionCount() > 0);
mDeleteAction.setEnabled(hasSelection);
        mSelectAllAction.setEnabled(hasSelection || errorLabel.isFocusControl());

// The paste operation is only available if we can paste our custom type.
// We do not currently support pasting random text (e.g. XML). Maybe later.







