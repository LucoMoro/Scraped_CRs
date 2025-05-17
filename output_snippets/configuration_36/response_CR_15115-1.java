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

private RulesEngine mRulesEngine;

mLayoutEditor = layoutEditor;
mRulesEngine = rulesEngine;

mHScale = new ScaleInfo(getHorizontalBar());
mVScale = new ScaleInfo(getVerticalBar());

mRulesEngine.dispose();
mRulesEngine = null;
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

}

public void dragSetData(DragSourceEvent e) {
if (TextTransfer.getInstance().isSupportedType(e.dataType)) {
e.data = getSelectionAsText();
return;
}

if (SimpleXmlTransfer.getInstance().isSupportedType(e.dataType)) {
e.data = mDragElements;
return;
}
public void dragFinished(DragSourceEvent e) {
if (e.detail == DND.DROP_MOVE) {
    List<UiElementNode> elementsToRemove = getSelectionAsElements();
    if (elementsToRemove != null && !elementsToRemove.isEmpty()) {
        for (UiElementNode element : elementsToRemove) {
            if (element != null) {
                removeElement(element); 
            }
        }
        updateUI(); 
    }
}

GlobalCanvasDragInfo.getInstance().stopDrag();
}

}
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

/**
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
setDisabledImageDescriptor(
}

mUiNodes = selected;
mPerformCut = perform_cut;
}

/**

//<End of snippet n. 2>