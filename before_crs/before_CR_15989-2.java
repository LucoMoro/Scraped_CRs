/*ADT GLE2: paste element in empty layout.

Change-Id:I792947e39d4b0ef7828f2df394bac81c0da37970*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 5a4761d..0659d2d 100755

//Synthetic comment -- @@ -110,8 +110,6 @@
*
* TODO List:
* - display error icon
 * - finish palette (see palette's todo list)
 * - finish canvas (see canvas' todo list)
* - completly rethink the property panel
*/
public class GraphicalEditorPart extends EditorPart








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index e8a6dd4..ea10d78 100755

//Synthetic comment -- @@ -17,9 +17,11 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.editors.layout.gscripts.INode;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Point;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Rect;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
//Synthetic comment -- @@ -134,9 +136,7 @@
* TODO list:
* - gray on error, keep select but disable d'n'd.
* - context menu handling of layout + local props (via IViewRules)
 * - handle empty root:
 *    - Must also be able to copy/paste into an empty document (prolly need to bypass script, and deal with the xmlns)
 *    - We must be able to move/copy/cut the top root element (mostly between documents).
*/
class LayoutCanvas extends Canvas implements ISelectionProvider {

//Synthetic comment -- @@ -1471,7 +1471,6 @@
if (e.detail == DND.DROP_MOVE) {
// Remove from source. Since we know the selection, we'll simply
// create a cut operation on the existing drag selection.
                AdtPlugin.printToConsole("CanvasDND", "dragFinished => MOVE");

// Create an undo wrapper, which takes a runnable
mLayoutEditor.wrapUndoRecording(
//Synthetic comment -- @@ -2008,9 +2007,113 @@
getRulesEngine().callOnPaste(targetNode, pasted);
}

    private void pasteInEmptyDocument(SimpleElement simpleElement) {
        // TODO Auto-generated method stub

}

/**
//Synthetic comment -- @@ -2030,12 +2133,15 @@
// Need a valid empty document to create the new root
final UiDocumentNode uiDoc = mLayoutEditor.getUiRootNode();
if (uiDoc == null || uiDoc.getUiChildren().size() > 0) {
return;
}

// Find the view descriptor matching our FQCN
final ViewElementDescriptor viewDesc = mLayoutEditor.getFqcnViewDescritor(rootFqcn);
if (viewDesc == null) {
return;
}

//Synthetic comment -- @@ -2069,4 +2175,8 @@
}
});
}
}







