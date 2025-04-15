/*ADT GLE2: paste element in empty layout.

Change-Id:I792947e39d4b0ef7828f2df394bac81c0da37970*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 5a4761d..0659d2d 100755

//Synthetic comment -- @@ -110,8 +110,6 @@
*
* TODO List:
* - display error icon
* - completly rethink the property panel
*/
public class GraphicalEditorPart extends EditorPart








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index e8a6dd4..c724207 100755

//Synthetic comment -- @@ -17,9 +17,11 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IDragElement;
import com.android.ide.eclipse.adt.editors.layout.gscripts.INode;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Point;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Rect;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IDragElement.IDragAttribute;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
//Synthetic comment -- @@ -134,9 +136,7 @@
* TODO list:
* - gray on error, keep select but disable d'n'd.
* - context menu handling of layout + local props (via IViewRules)
 * - properly handle custom views
*/
class LayoutCanvas extends Canvas implements ISelectionProvider {

//Synthetic comment -- @@ -1471,7 +1471,6 @@
if (e.detail == DND.DROP_MOVE) {
// Remove from source. Since we know the selection, we'll simply
// create a cut operation on the existing drag selection.

// Create an undo wrapper, which takes a runnable
mLayoutEditor.wrapUndoRecording(
//Synthetic comment -- @@ -2008,9 +2007,114 @@
getRulesEngine().callOnPaste(targetNode, pasted);
}

    /**
     * Paste a new root into an empty XML layout.
     * <p/>
     * In case of error (unknown FQCN, document not empty), silently do nothing.
     * In case of success, the new element will have some default attributes set (xmlns:android,
     * layout_width and height). The edit is wrapped in a proper undo.
     * <p/>
     * Implementation is similar to {@link #createDocumentRoot(String)} except we also
     * copy all the attributes and inner elements recursively.
     */
    private void pasteInEmptyDocument(final IDragElement pastedElement) {
        String rootFqcn = pastedElement.getFqcn();

        // Need a valid empty document to create the new root
        final UiDocumentNode uiDoc = mLayoutEditor.getUiRootNode();
        if (uiDoc == null || uiDoc.getUiChildren().size() > 0) {
            debugPrintf("Failed to paste document root for %1$s: document is not empty", rootFqcn);
            return;
        }

        // Find the view descriptor matching our FQCN
        final ViewElementDescriptor viewDesc = mLayoutEditor.getFqcnViewDescritor(rootFqcn);
        if (viewDesc == null) {
            // TODO this could happen if pasting a custom view not known in this project
            debugPrintf("Failed to paste document root, unknown FQCN %1$s", rootFqcn);
            return;
        }

        // Get the last segment of the FQCN for the undo title
        String title = rootFqcn;
        int pos = title.lastIndexOf('.');
        if (pos > 0 && pos < title.length() - 1) {
            title = title.substring(pos + 1);
        }
        title = String.format("Paste root %1$s in document", title);

        mLayoutEditor.wrapUndoRecording(title, new Runnable() {
            public void run() {
                mLayoutEditor.editXmlModel(new Runnable() {
                    public void run() {
                        UiElementNode uiNew = uiDoc.appendNewUiChild(viewDesc);

                        // A root node requires the Android XMLNS
                        uiNew.setAttributeValue(
                                "android",
                                XmlnsAttributeDescriptor.XMLNS_URI,
                                SdkConstants.NS_RESOURCES,
                                true /*override*/);

                        // Copy all the attributes from the pasted element
                        for (IDragAttribute attr : pastedElement.getAttributes()) {
                            uiNew.setAttributeValue(
                                    attr.getName(),
                                    attr.getUri(),
                                    attr.getValue(),
                                    true /*override*/);
                        }

                        // Adjust the attributes, adding the default layout_width/height
                        // only if they are not present (the original element should have
                        // them though.)
                        DescriptorsUtils.setDefaultLayoutAttributes(uiNew, false /*updateLayout*/);

                        uiNew.createXmlNode();

                        // Now process all children
                        for (IDragElement childElement : pastedElement.getInnerElements()) {
                            addChild(uiNew, childElement);
                        }
                    }

                    private void addChild(UiElementNode uiParent, IDragElement childElement) {
                        String childFqcn = childElement.getFqcn();
                        final ViewElementDescriptor childDesc =
                            mLayoutEditor.getFqcnViewDescritor(childFqcn);
                        if (childDesc == null) {
                            // TODO this could happen if pasting a custom view
                            debugPrintf("Failed to paste element, unknown FQCN %1$s", childFqcn);
                            return;
                        }

                        UiElementNode uiChild = uiParent.appendNewUiChild(childDesc);

                        // Copy all the attributes from the pasted element
                        for (IDragAttribute attr : childElement.getAttributes()) {
                            uiChild.setAttributeValue(
                                    attr.getName(),
                                    attr.getUri(),
                                    attr.getValue(),
                                    true /*override*/);
                        }

                        // Adjust the attributes, adding the default layout_width/height
                        // only if they are not present (the original element should have
                        // them though.)
                        DescriptorsUtils.setDefaultLayoutAttributes(
                                uiChild, false /*updateLayout*/);

                        uiChild.createXmlNode();

                        // Now process all grand children
                        for (IDragElement grandChildElement : childElement.getInnerElements()) {
                            addChild(uiChild, grandChildElement);
                        }
                    }
                });
            }
        });
}

/**
//Synthetic comment -- @@ -2030,12 +2134,15 @@
// Need a valid empty document to create the new root
final UiDocumentNode uiDoc = mLayoutEditor.getUiRootNode();
if (uiDoc == null || uiDoc.getUiChildren().size() > 0) {
            debugPrintf("Failed to create document root for %1$s: document is not empty", rootFqcn);
return;
}

// Find the view descriptor matching our FQCN
final ViewElementDescriptor viewDesc = mLayoutEditor.getFqcnViewDescritor(rootFqcn);
if (viewDesc == null) {
            // TODO this could happen if dropping a custom view not known in this project
            debugPrintf("Failed to add document root, unknown FQCN %1$s", rootFqcn);
return;
}

//Synthetic comment -- @@ -2069,4 +2176,8 @@
}
});
}

    private void debugPrintf(String message, Object... params) {
        AdtPlugin.printToConsole("Canvas", String.format(message, params));
    }
}







