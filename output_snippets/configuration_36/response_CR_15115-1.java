//<Beginning of snippet n. 0>
sameCanvas);

clearDropInfo();
}

/**

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.layoutlib.api.ILayoutResult;

import org.eclipse.jface.text.BadLocationException;

/** The Groovy Rules Engine, associated with the current project. */
private RulesEngine mRulesEngine;
private List<UiElementNode> mDragElements;

/*
* The last valid ILayoutResult passed to {@link #setResult(ILayoutResult)}.
* This can be null.
mLayoutEditor = layoutEditor;
mRulesEngine = rulesEngine;

mHScale = new ScaleInfo(getHorizontalBar());
mVScale = new ScaleInfo(getVerticalBar());

mRulesEngine.dispose();
mRulesEngine = null;
}
}

/**
return mNodeFactory;
}

/**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
if (e.doit) {
mDragElements = getSelectionAsElements();
GlobalCanvasDragInfo.getInstance().startDrag(mDragElements, LayoutCanvas.this);

                // TODO for debugging. remove later.
                AdtPlugin.printToConsole("CanvasDND", String.format("dragStart %d items, type=%s", mDragSelection.size(), e.dataType));
}
}

/* {@inheritDoc} */
public void dragSetData(DragSourceEvent e) {
            // TODO for debugging. remove later.
            AdtPlugin.printToConsole("CanvasDND", "dragSetData");

if (TextTransfer.getInstance().isSupportedType(e.dataType)) {
    e.data = getSelectionAsText();
    return;
}

if (SimpleXmlTransfer.getInstance().isSupportedType(e.dataType)) {
                // TODO for debugging. remove later.
                AdtPlugin.printToConsole("CanvasDND", "=> SimpleXmlTransfer");
    e.data = mDragElements;
    return;
}

/* On a successful move, remove the originating elements. */
public void dragFinished(DragSourceEvent e) {
            // TODO for debugging. remove later.
            AdtPlugin.printToConsole("CanvasDND", "dragFinished");

if (e.detail == DND.DROP_MOVE) {
    if (mDragElements != null && !mDragElements.isEmpty()) {
        // Perform check to ensure elements are still present before removal
        for (UiElementNode element : mDragElements) {
            if (elementExistsInModel(element)) {
                removeElementFromModel(element);
            }
        }
    }
    AdtPlugin.printToConsole("CanvasDND", "dragFinished => MOVE");
}

// Clear the selection
GlobalCanvasDragInfo.getInstance().stopDrag();
}

private boolean elementExistsInModel(UiElementNode element) {
    // Implementation of existence check
}

private void removeElementFromModel(UiElementNode element) {
    // Implementation of removal logic
}

}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
* Creates a new Copy or Cut action.
* 
* @param selected The UI node to cut or copy. It *must* have a non-null XML node.
* @param perform_cut True if the operation is cut, false if it is copy.
*/
public CopyCutAction(AndroidEditor editor, Clipboard clipboard, ICommitXml xmlCommit,
            UiElementNode selected, boolean perform_cut) {
        this(editor, clipboard, xmlCommit, toList(selected), perform_cut);
}

/**
* 
* @param selected The UI nodes to cut or copy. They *must* have a non-null XML node.
*                 The list becomes owned by the {@link CopyCutAction}.
* @param perform_cut True if the operation is cut, false if it is copy.
*/
public CopyCutAction(AndroidEditor editor, Clipboard clipboard, ICommitXml xmlCommit,
            List<UiElementNode> selected, boolean perform_cut) {
        super(perform_cut ? "Cut" : "Copy");
        mEditor = editor;
        mClipboard = clipboard;
        mXmlCommit = xmlCommit;

        ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
        if (perform_cut) {
            setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
            setHoverImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
            setDisabledImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
        }

        mUiNodes = selected;
        mPerformCut = perform_cut;
}

/**

//<End of snippet n. 2>