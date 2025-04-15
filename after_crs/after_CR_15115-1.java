/*ADT GLE2: remove source elements after a drag'n'drop MOVE operation.

Change-Id:Ic0560466a3ccfb3a5eeb077b1eae508828870629*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasDropListener.java
//Synthetic comment -- index 4a9ae72..619ea7e 100755

//Synthetic comment -- @@ -265,6 +265,7 @@
sameCanvas);

clearDropInfo();
        mCanvas.redraw();
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 10318a9..69c1d99 100755

//Synthetic comment -- @@ -26,7 +26,9 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.ui.tree.CopyCutAction;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.ILayoutResult;

import org.eclipse.jface.text.BadLocationException;
//Synthetic comment -- @@ -102,6 +104,9 @@
/** The Groovy Rules Engine, associated with the current project. */
private RulesEngine mRulesEngine;

    /** SWT clipboard instance. */
    private Clipboard mClipboard;

/*
* The last valid ILayoutResult passed to {@link #setResult(ILayoutResult)}.
* This can be null.
//Synthetic comment -- @@ -189,6 +194,8 @@
mLayoutEditor = layoutEditor;
mRulesEngine = rulesEngine;

        mClipboard = new Clipboard(parent.getDisplay());

mHScale = new ScaleInfo(getHorizontalBar());
mVScale = new ScaleInfo(getVerticalBar());

//Synthetic comment -- @@ -269,6 +276,11 @@
mRulesEngine.dispose();
mRulesEngine = null;
}

        if (mClipboard != null) {
            mClipboard.dispose();
            mClipboard = null;
        }
}

/**
//Synthetic comment -- @@ -302,6 +314,11 @@
return mNodeFactory;
}

    /** Returns the shared SWT keyboard. */
    public Clipboard getClipboard() {
        return mClipboard;
    }

/**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
//Synthetic comment -- @@ -1152,9 +1169,6 @@
if (e.doit) {
mDragElements = getSelectionAsElements();
GlobalCanvasDragInfo.getInstance().startDrag(mDragElements, LayoutCanvas.this);
}
}

//Synthetic comment -- @@ -1166,17 +1180,12 @@
* {@inheritDoc}
*/
public void dragSetData(DragSourceEvent e) {
if (TextTransfer.getInstance().isSupportedType(e.dataType)) {
e.data = getSelectionAsText();
return;
}

if (SimpleXmlTransfer.getInstance().isSupportedType(e.dataType)) {
e.data = mDragElements;
return;
}
//Synthetic comment -- @@ -1281,12 +1290,24 @@
* On a successful move, remove the originating elements.
*/
public void dragFinished(DragSourceEvent e) {
if (e.detail == DND.DROP_MOVE) {
                // Remove from source. Since we know the selection, we'll simply
                // create a cut operation on the existing drag selection.
AdtPlugin.printToConsole("CanvasDND", "dragFinished => MOVE");

                // Create an undo wrapper, which takes a runnable
                mLayoutEditor.wrapUndoRecording(
                        "Remove drag'n'drop source elements",
                        new Runnable() {
                            public void run() {
                                // Create an edit-XML wrapper, which takes a runnable
                                mLayoutEditor.editXmlModel(new Runnable() {
                                    public void run() {
                                        cutDragSelection();
                                    }
                                });
                            }
                        });
}

// Clear the selection
//Synthetic comment -- @@ -1295,5 +1316,22 @@
GlobalCanvasDragInfo.getInstance().stopDrag();
}

        private void cutDragSelection() {
            List<UiElementNode> selected = new ArrayList<UiElementNode>();

            for (CanvasSelection cs : mDragSelection) {
                selected.add(cs.getViewInfo().getUiViewKey());
            }

            CopyCutAction action = new CopyCutAction(
                    mLayoutEditor,
                    getClipboard(),
                    null, /* xml commit callback */
                    selected,
                    true /* cut */);

            action.run();
        }

}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/CopyCutAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/CopyCutAction.java
//Synthetic comment -- index e04256c..9eebfe7 100644

//Synthetic comment -- @@ -57,11 +57,11 @@
* Creates a new Copy or Cut action.
* 
* @param selected The UI node to cut or copy. It *must* have a non-null XML node.
     * @param performCut True if the operation is cut, false if it is copy.
*/
public CopyCutAction(AndroidEditor editor, Clipboard clipboard, ICommitXml xmlCommit,
            UiElementNode selected, boolean performCut) {
        this(editor, clipboard, xmlCommit, toList(selected), performCut);
}

/**
//Synthetic comment -- @@ -69,17 +69,17 @@
* 
* @param selected The UI nodes to cut or copy. They *must* have a non-null XML node.
*                 The list becomes owned by the {@link CopyCutAction}.
     * @param performCut True if the operation is cut, false if it is copy.
*/
public CopyCutAction(AndroidEditor editor, Clipboard clipboard, ICommitXml xmlCommit,
            List<UiElementNode> selected, boolean performCut) {
        super(performCut ? "Cut" : "Copy");
mEditor = editor;
mClipboard = clipboard;
mXmlCommit = xmlCommit;

ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
        if (performCut) {
setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
setHoverImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
setDisabledImageDescriptor(
//Synthetic comment -- @@ -92,7 +92,7 @@
}

mUiNodes = selected;
        mPerformCut = performCut;
}

/**







