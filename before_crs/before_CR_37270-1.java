/*Fix cut & paste in XML editors for Eclipse 4.x

Change-Id:Ie30c362c71eff0d62e177e06687c362802d416b1*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java
//Synthetic comment -- index 8d7b02e..f904f6e 100644

//Synthetic comment -- @@ -687,6 +687,16 @@
public void delegatePostPageChange(int newPageIndex) {
super.delegatePostPageChange(newPageIndex);

IFormPage page = getEditor().getActivePageInstance();
updateOutline(page);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 2154179..371852c 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.Margins;
import com.android.ide.common.api.Point;
//Synthetic comment -- @@ -1213,16 +1214,30 @@
*
* @param bars the action bar for this canvas
*/
    public void updateGlobalActions(IActionBars bars) {
updateMenuActionState();
        assert bars != null;
        bars.setGlobalActionHandler(ActionFactory.CUT.getId(), mCutAction);
        bars.setGlobalActionHandler(ActionFactory.COPY.getId(), mCopyAction);
        bars.setGlobalActionHandler(ActionFactory.PASTE.getId(), mPasteAction);
        bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), mDeleteAction);
        bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), mSelectAllAction);

ITextEditor editor = mEditorDelegate.getEditor().getStructuredTextEditor();
IAction undoAction = editor.getAction(ActionFactory.UNDO.getId());
bars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);
IAction redoAction = editor.getAction(ActionFactory.REDO.getId());







