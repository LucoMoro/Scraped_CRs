//<Beginning of snippet n. 1>
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.layoutlib.api.ILayoutResult;

import org.eclipse.jface.text.BadLocationException;
/** The Groovy Rules Engine, associated with the current project. */
private RulesEngine mRulesEngine;

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

* {@inheritDoc}
*/
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
* On a successful move, remove the originating elements.
*/
public void dragFinished(DragSourceEvent e) {
            // TODO for debugging. remove later.
            AdtPlugin.printToConsole("CanvasDND", "dragFinished");

if (e.detail == DND.DROP_MOVE) {
                // remove from source
                for (UiElementNode element : mDragElements) {
                    if (element.getParent() != null) {
                        element.getParent().removeChild(element);
                    }
                }
                AdtPlugin.printToConsole("CanvasDND", "dragFinished => MOVE");
}

// Clear the selection
GlobalCanvasDragInfo.getInstance().stopDrag();
}

}
//<End of snippet n. 1>