/*Select element surrounding caret when switching from editor

If you edit the XML, and then switch back to the visual editor, this
changeset will cause the element surrounding the caret (if any) to be
selected in the visual editor.

Change-Id:I1f03856b3b3946fe23d6e654773ee4318d0d56ed*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index 5a749b1..a4a133e 100644

//Synthetic comment -- @@ -48,9 +48,6 @@
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

//Synthetic comment -- @@ -743,23 +740,8 @@
/**
* Returns the XML DOM node corresponding to the given offset of the given document.
*/
    public static Node getNode(ITextViewer viewer, int offset) {
        return AndroidXmlEditor.getNode(viewer.getDocument(), offset);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index a1b9d13..117138e 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
//Synthetic comment -- @@ -95,7 +96,7 @@
public static final int TEXT_WIDTH_HINT = 50;

/** Page index of the text editor (always the last page) */
    protected int mTextPageIndex;
/** The text editor */
private StructuredTextEditor mTextEditor;
/** Listener for the XML model from the StructuredEditor */
//Synthetic comment -- @@ -621,6 +622,39 @@
}

/**
     * Returns the XML DOM node corresponding to the given offset of the given
     * document.
     *
     * @param document The document to look in
     * @param offset The offset to look up the node for
     * @return The node containing the offset, or null
     */
    @SuppressWarnings("restriction") // No replacement for restricted XML model yet
    public static Node getNode(IDocument document, int offset) {
        Node node = null;
        IModelManager modelManager = StructuredModelManager.getModelManager();
        if (modelManager == null) {
            return null;
        }
        try {
            IStructuredModel model = modelManager.getExistingModelForRead(document);
            if (model != null) {
                try {
                    for (; offset >= 0 && node == null; --offset) {
                        node = (Node) model.getIndexedRegion(offset);
                    }
                } finally {
                    model.releaseFromRead();
                }
            }
        } catch (Exception e) {
            // Ignore exceptions.
        }

        return node;
    }

    /**
* Returns a version of the model that has been shared for read.
* <p/>
* Callers <em>must</em> call model.releaseFromRead() when done, typically








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index ff176de..3875896 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
//Synthetic comment -- @@ -51,6 +52,7 @@
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.HashSet;
//Synthetic comment -- @@ -307,6 +309,21 @@

@Override
protected void pageChange(int newPageIndex) {
        if (getCurrentPage() == mTextPageIndex &&
                newPageIndex == mGraphicalEditorIndex) {
            // You're switching from the XML editor to the WYSIWYG editor;
            // look at the caret position and figure out which node it corresponds to
            // (if any) and if found, select the corresponding visual element.
            ISourceViewer textViewer = getStructuredSourceViewer();
            int caretOffset = textViewer.getTextWidget().getCaretOffset();
            if (caretOffset >= 0) {
                Node node = AndroidXmlEditor.getNode(textViewer.getDocument(), caretOffset);
                if (node != null && mGraphicalEditor instanceof GraphicalEditorPart) {
                    ((GraphicalEditorPart)mGraphicalEditor).select(node);
                }
            }
        }

super.pageChange(newPageIndex);

if (mGraphicalEditor != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index f9b916f..253ceed 100755

//Synthetic comment -- @@ -98,6 +98,7 @@
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.w3c.dom.Node;

import java.awt.image.BufferedImage;
import java.io.File;
//Synthetic comment -- @@ -457,6 +458,14 @@
}

/**
     * Select the visual element corresponding to the given XML node
     * @param xmlNode The Node whose element we want to select
     */
    public void select(Node xmlNode) {
        mCanvasViewer.getCanvas().select(xmlNode);
    }

    /**
* Listens to changes from the Configuration UI banner and triggers layout rendering when
* changed. Also provide the Configuration UI with the list of resources/layout to display.
*/








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 29094b7..ebb053a 100755

//Synthetic comment -- @@ -1294,6 +1294,25 @@
}

/**
     * Select the visual element corresponding to the given XML node
     * @param xmlNode The Node whose element we want to select
     */
    public void select(Node xmlNode) {
        CanvasViewInfo vi = findViewInfoFor(xmlNode);
        if (vi != null) {
            // Select the visual element -- unless it's the root.
            // The root element is the one whose GRAND parent
            // is null (because the parent will be a -document-
            // node).
            UiViewElementNode key = vi.getUiViewKey();
            if (key != null && key.getUiParent() != null &&
                    key.getUiParent().getUiParent() != null) {
                selectSingle(vi);
            }
        }
    }

    /**
* Deselects a view info.
* Returns true if the object was actually selected.
* Callers are responsible for calling redraw() and updateOulineSelection() after.
//Synthetic comment -- @@ -1369,6 +1388,46 @@
return null;
}

    /**
     * Locates and returns the {@link CanvasViewInfo} corresponding to the given
     * node, or null if it cannot be found.
     *
     * @param node The node we want to find a corresponding
     *            {@link CanvasViewInfo} for.
     * @return The {@link CanvasViewInfo} corresponding to the given node, or
     *         null if no match was found.
     */
    /* package */ CanvasViewInfo findViewInfoFor(Node node) {
        if (mLastValidViewInfoRoot != null) {
            return findViewInfoForNode(node, mLastValidViewInfoRoot);
        } else {
            return null;
        }
    }

    /**
     * Tries to find a child with the same view XML node in the view info sub-tree.
     * Returns null if not found.
     */
    private CanvasViewInfo findViewInfoForNode(Node xmlNode, CanvasViewInfo canvasViewInfo) {
        if (canvasViewInfo == null) {
            return null;
        }
        if (canvasViewInfo.getXmlNode() == xmlNode) {
            return canvasViewInfo;
        }

        // Try to find a matching child
        for (CanvasViewInfo child : canvasViewInfo.getChildren()) {
            CanvasViewInfo v = findViewInfoForNode(xmlNode, child);
            if (v != null) {
                return v;
            }
        }

        return null;
    }


/**
* Tries to find the inner most child matching the given x,y coordinates







