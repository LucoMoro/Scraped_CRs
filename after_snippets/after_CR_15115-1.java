
//<Beginning of snippet n. 0>


sameCanvas);

clearDropInfo();
        mCanvas.redraw();
}

/**

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.ui.tree.CopyCutAction;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.ILayoutResult;

import org.eclipse.jface.text.BadLocationException;
/** The Groovy Rules Engine, associated with the current project. */
private RulesEngine mRulesEngine;

    /** SWT clipboard instance. */
    private Clipboard mClipboard;

/*
* The last valid ILayoutResult passed to {@link #setResult(ILayoutResult)}.
* This can be null.
mLayoutEditor = layoutEditor;
mRulesEngine = rulesEngine;

        mClipboard = new Clipboard(parent.getDisplay());

mHScale = new ScaleInfo(getHorizontalBar());
mVScale = new ScaleInfo(getVerticalBar());

mRulesEngine.dispose();
mRulesEngine = null;
}

        if (mClipboard != null) {
            mClipboard.dispose();
            mClipboard = null;
        }
}

/**
return mNodeFactory;
}

    /** Returns the shared SWT keyboard. */
    public Clipboard getClipboard() {
        return mClipboard;
    }

/**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
if (e.doit) {
mDragElements = getSelectionAsElements();
GlobalCanvasDragInfo.getInstance().startDrag(mDragElements, LayoutCanvas.this);
}
}

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

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


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
}

mUiNodes = selected;
        mPerformCut = performCut;
}

/**

//<End of snippet n. 2>








