//<Beginning of snippet n. 1>
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal/editors.layout.uimodel.UiViewElementNode;
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

return mNodeFactory;
}

public void dragSetData(DragSourceEvent e) {
    AdtPlugin.printToConsole("CanvasDND", "dragSetData");

    if (TextTransfer.getInstance().isSupportedType(e.dataType)) {
        e.data = getSelectionAsText();
        return;
    }

    if (SimpleXmlTransfer.getInstance().isSupportedType(e.dataType)) {
        AdtPlugin.printToConsole("CanvasDND", "=> SimpleXmlTransfer");
        e.data = mDragElements;
        return;
    }
}

public void dragFinished(DragSourceEvent e) {
    AdtPlugin.printToConsole("CanvasDND", "dragFinished");

    if (e.detail == DND.DROP_MOVE) {
        for (UiViewElementNode element : mDragElements) {
            if (removeElementFromSource(element)) {
                AdtPlugin.printToConsole("CanvasDND", "Element removed: " + element);
            } else {
                AdtPlugin.printToConsole("CanvasDND", "Failed to remove element: " + element);
            }
        }
        updateUIAfterRemoval();
    }

    GlobalCanvasDragInfo.getInstance().stopDrag();
}

private boolean removeElementFromSource(UiViewElementNode element) {
    boolean isSuccess = false;
    try {
        isSuccess = performRemovalAction(element);
        if (isSuccess) {
            AdtPlugin.printToConsole("CanvasDND", "Element successfully removed: " + element);
        }
    } catch (SpecificExceptionType ex) {
        AdtPlugin.printToConsole("CanvasDND", "Error while removing element: " + ex.getMessage(), ex);
    } catch (AnotherSpecificExceptionType ex) {
        AdtPlugin.printToConsole("CanvasDND", "Another error while removing element: " + ex.getMessage(), ex);
    }
    return isSuccess;
}

private boolean performRemovalAction(UiViewElementNode element) {
    // Complete logic to remove element from the data structure
    return dataStructure.remove(element);
}

private void updateUIAfterRemoval() {
    refreshCanvasUI();
    AdtPlugin.printToConsole("CanvasDND", "UI updated after removal.");
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
public CopyCutAction(AndroidEditor editor, Clipboard clipboard, ICommitXml xmlCommit,
            UiElementNode selected, boolean perform_cut) {
    this(editor, clipboard, xmlCommit, toList(selected), perform_cut);
}

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
        setDisabledImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
    }

    mUiNodes = selected;
    mPerformCut = perform_cut;
}
//<End of snippet n. 2>