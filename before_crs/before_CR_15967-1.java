/*ADT GLE2: Properly handle empty documents, including drag'n'drop.

This makes it possible to drop into empty documents.

Change-Id:I512d8ff5e8747e4d3f5e27900308157e75cacd57*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index e245558..fd5198f 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IUnknownDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.CustomViewDescriptorService;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle1.GraphicalLayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle1.UiContentOutlinePage;
//Synthetic comment -- @@ -58,6 +59,8 @@
import org.w3c.dom.Document;

import java.util.HashMap;

/**
* Multi-page form editor for /res/layout XML files.
//Synthetic comment -- @@ -108,6 +111,10 @@
return mUiRootNode;
}

// ---- Base Class Overrides ----

@Override
//Synthetic comment -- @@ -307,7 +314,7 @@
/**
* Returns the custom IContentOutlinePage or IPropertySheetPage when asked for it.
*/
    @SuppressWarnings("rawtypes")
@Override
public Object getAdapter(Class adapter) {
// for the outline, force it to come from the Graphical Editor.
//Synthetic comment -- @@ -572,7 +579,63 @@
}
}

    public void setNewFileOnConfigChange(boolean state) {
        mNewFileOnConfigChange = state;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasDropListener.java
//Synthetic comment -- index ee0b802..3ac0be7 100755

//Synthetic comment -- @@ -235,12 +235,6 @@
public void drop(DropTargetEvent event) {
AdtPlugin.printErrorToConsole("DEBUG", "dropped");

        if (mTargetNode == null) {
            // DEBUG
            AdtPlugin.printErrorToConsole("DEBUG", "dropped on null targetNode");
            return;
        }

SimpleElement[] elements = null;

SimpleXmlTransfer sxt = SimpleXmlTransfer.getInstance();
//Synthetic comment -- @@ -260,6 +254,17 @@
elements = mCurrentDragElements;
}

Point where = mCanvas.displayToCanvasPoint(event.x, event.y);

updateDropFeedback(mFeedback, event);
//Synthetic comment -- @@ -490,4 +495,14 @@
mCanvas.redraw();
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index ea3171f..5a4761d 100755

//Synthetic comment -- @@ -83,6 +83,7 @@
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -945,7 +946,33 @@
UiDocumentNode model = getModel();

if (model.getUiChildren().size() == 0) {
                    displayError("No Xml content. Go to the Outline view and add nodes.");
return;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 6a946bd..d01aa71 100755

//Synthetic comment -- @@ -22,13 +22,19 @@
import com.android.ide.eclipse.adt.editors.layout.gscripts.Rect;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.ILayoutResult;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.Action;
//Synthetic comment -- @@ -83,10 +89,11 @@
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.TextActionHandler;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
//Synthetic comment -- @@ -143,28 +150,25 @@
/** SWT clipboard instance. */
private Clipboard mClipboard;

    /*
     * The last valid ILayoutResult passed to {@link #setResult(ILayoutResult)}.
     * This can be null.
     * When non null, {@link #mLastValidViewInfoRoot} is guaranteed to be non-null too.
    */
    private ILayoutResult mLastValidResult;

/**
     * The CanvasViewInfo root created for the last update of {@link #mLastValidResult}.
     * This is null when {@link #mLastValidResult} is null.
     * When non null, {@link #mLastValidResult} is guaranteed to be non-null too.
*/
private CanvasViewInfo mLastValidViewInfoRoot;

/**
     * True when the last {@link #setResult(ILayoutResult)} provided a valid {@link ILayoutResult}
     * in which case it is also available in {@link #mLastValidResult}.
* When false this means the canvas is displaying an out-dated result image & bounds and some
* features should be disabled accordingly such a drag'n'drop.
* <p/>
     * When this is false, {@link #mLastValidResult} can be non-null and points to an older
     * layout result.
*/
private boolean mIsResultValid;

//Synthetic comment -- @@ -371,17 +375,25 @@

/**
* Returns true when the last {@link #setResult(ILayoutResult)} provided a valid
     * {@link ILayoutResult} in which case it is also available in {@link #mLastValidResult}.
* When false this means the canvas is displaying an out-dated result image & bounds and some
* features should be disabled accordingly such a drag'n'drop.
* <p/>
     * When this is false, {@link #mLastValidResult} can be non-null and points to an older
     * layout result.
     */
/* package */ boolean isResultValid() {
return mIsResultValid;
}

/** Returns the Groovy Rules Engine, associated with the current project. */
/* package */ RulesEngine getRulesEngine() {
return mRulesEngine;
//Synthetic comment -- @@ -433,8 +445,12 @@
mIsResultValid = (result != null && result.getSuccess() == ILayoutResult.SUCCESS);

if (mIsResultValid && result != null) {
            mLastValidResult = result;
            mLastValidViewInfoRoot = new CanvasViewInfo(result.getRootView());
setImage(result.getImage());

updateNodeProxies(mLastValidViewInfoRoot);
//Synthetic comment -- @@ -462,8 +478,10 @@
// remove the current alternate selection views
mAltSelection = null;

            mHScale.setSize(mImage.getImageData().width, getClientArea().width);
            mVScale.setSize(mImage.getImageData().height, getClientArea().height);

// Pre-load the android.view.View rule in the Rules Engine. Doing it here means
// it will be done after the first rendering is finished. Successive calls are
//Synthetic comment -- @@ -787,7 +805,6 @@
* view info.
*/
private void updateNodeProxies(CanvasViewInfo vi) {

if (vi == null) {
return;
}
//Synthetic comment -- @@ -806,26 +823,34 @@
/**
* Sets the image of the last *successful* rendering.
* Converts the AWT image into an SWT image.
*/
private void setImage(BufferedImage awtImage) {
        int width = awtImage.getWidth();
        int height = awtImage.getHeight();

        Raster raster = awtImage.getData(new java.awt.Rectangle(width, height));
        int[] imageDataBuffer = ((DataBufferInt)raster.getDataBuffer()).getData();

        ImageData imageData = new ImageData(width, height, 32,
                new PaletteData(0x00FF0000, 0x0000FF00, 0x000000FF));

        imageData.setPixels(0, 0, imageDataBuffer.length, imageDataBuffer, 0);

        mImage = new Image(getDisplay(), imageData);
}

/**
* Sets the alpha for the given GC.
* <p/>
     * Alpha may not work on all platforms and may fail with an exception.
*
* @param gc the GC to change
* @param alpha the new alpha, 0 for transparent, 255 for opaque.
//Synthetic comment -- @@ -845,7 +870,8 @@
/**
* Sets the non-text antialias flag for the given GC.
* <p/>
     * Antialias may not work on all platforms and may fail with an exception.
*
* @param gc the GC to change
* @param alias One of {@link SWT#DEFAULT}, {@link SWT#ON}, {@link SWT#OFF}.
//Synthetic comment -- @@ -969,37 +995,35 @@
* Hover on top of a known child.
*/
private void onMouseMove(MouseEvent e) {
        if (mLastValidResult != null) {
            CanvasViewInfo root = mLastValidViewInfoRoot;

            int x = mHScale.inverseTranslate(e.x);
            int y = mVScale.inverseTranslate(e.y);

            CanvasViewInfo vi = findViewInfoAt(x, y);

            // We don't hover on the root since it's not a widget per see and it is always there.
            if (vi == root) {
                vi = null;
            }

            boolean needsUpdate = vi != mHoverViewInfo;
            mHoverViewInfo = vi;

            if (vi == null) {
                mHoverRect = null;
            } else {
                Rectangle r = vi.getSelectionRect();
                mHoverRect = new Rectangle(r.x, r.y, r.width, r.height);
            }

            if (needsUpdate) {
                redraw();
            }
}
}

private void onMouseDown(MouseEvent e) {
        // pass, not used yet.
}

/**
//Synthetic comment -- @@ -1010,81 +1034,78 @@
* pointed at (i.e. click on an object then alt-click to cycle).
*/
private void onMouseUp(MouseEvent e) {
        if (mLastValidResult != null) {

            boolean isShift = (e.stateMask & SWT.SHIFT) != 0;
            boolean isAlt   = (e.stateMask & SWT.ALT)   != 0;

            int x = mHScale.inverseTranslate(e.x);
            int y = mVScale.inverseTranslate(e.y);

            CanvasViewInfo vi = findViewInfoAt(x, y);

            if (isShift && !isAlt) {
                // Case where shift is pressed: pointed object is toggled.

                // reset alternate selection if any
                mAltSelection = null;

                // If nothing has been found at the cursor, assume it might be a user error
                // and avoid clearing the existing selection.

                if (vi != null) {
                    // toggle this selection on-off: remove it if already selected
                    if (deselect(vi)) {
                        redraw();
                        return;
                    }

                    // otherwise add it.
                    mSelections.add(new CanvasSelection(vi, mRulesEngine, mNodeFactory));
                    fireSelectionChanged();
redraw();
}

            } else if (isAlt) {
                // Case where alt is pressed: select or cycle the object pointed at.

                // Note: if shift and alt are pressed, shift is ignored. The alternate selection
                // mechanism does not reset the current multiple selection unless they intersect.

                // We need to remember the "origin" of the alternate selection, to be
                // able to continue cycling through it later. If there's no alternate selection,
                // create one. If there's one but not for the same origin object, create a new
                // one too.
                if (mAltSelection == null || mAltSelection.getOriginatingView() != vi) {
                    mAltSelection = new CanvasAlternateSelection(vi, findAltViewInfoAt(
                                                    x, y, mLastValidViewInfoRoot, null));

                    // deselect them all, in case they were partially selected
                    deselectAll(mAltSelection.getAltViews());

                    // select the current one
                    CanvasViewInfo vi2 = mAltSelection.getCurrent();
                    if (vi2 != null) {
                        mSelections.addFirst(new CanvasSelection(vi2, mRulesEngine, mNodeFactory));
                        fireSelectionChanged();
                    }
                } else {
                    // We're trying to cycle through the current alternate selection.
                    // First remove the current object.
                    CanvasViewInfo vi2 = mAltSelection.getCurrent();
                    deselect(vi2);

                    // Now select the next one.
                    vi2 = mAltSelection.getNext();
                    if (vi2 != null) {
                        mSelections.addFirst(new CanvasSelection(vi2, mRulesEngine, mNodeFactory));
                        fireSelectionChanged();
                    }
                }
redraw();

            } else {
                // Case where no modifier is pressed: either select or reset the selection.

                selectSingle(vi);
}
}
}

//Synthetic comment -- @@ -1157,6 +1178,9 @@
* Returns null if not found.
*/
private CanvasViewInfo findViewInfoKey(Object viewKey, CanvasViewInfo canvasViewInfo) {
if (canvasViewInfo.getUiViewKey() == viewKey) {
return canvasViewInfo;
}
//Synthetic comment -- @@ -1174,33 +1198,38 @@


/**
     * Tries to find the inner most child matching the given x,y coordinates in the view
     * info sub-tree, starting at the last know view info root.
* This uses the potentially-expanded selection bounds.
     *
     * Returns null if not found or if there's view info root.
*/
/* package */ CanvasViewInfo findViewInfoAt(int x, int y) {
if (mLastValidViewInfoRoot == null) {
return null;
} else {
            return findViewInfoAt(x, y, mLastValidViewInfoRoot);
}
}

/**
* Tries to find the inner most child matching the given x,y coordinates in the view
* info sub-tree. This uses the potentially-expanded selection bounds.
*
* Returns null if not found.
*/
    private CanvasViewInfo findViewInfoAt(int x, int y, CanvasViewInfo canvasViewInfo) {
Rectangle r = canvasViewInfo.getSelectionRect();
if (r.contains(x, y)) {

// try to find a matching child first
for (CanvasViewInfo child : canvasViewInfo.getChildren()) {
                CanvasViewInfo v = findViewInfoAt(x, y, child);
if (v != null) {
return v;
}
//Synthetic comment -- @@ -1213,21 +1242,36 @@
return null;
}

    private ArrayList<CanvasViewInfo> findAltViewInfoAt(
            int x, int y, CanvasViewInfo parent, ArrayList<CanvasViewInfo> outList) {
Rectangle r;

if (outList == null) {
outList = new ArrayList<CanvasViewInfo>();

            // add the parent root only once
            r = parent.getSelectionRect();
            if (r.contains(x, y)) {
                outList.add(parent);
}
}

        if (!parent.getChildren().isEmpty()) {
// then add all children that match the position
for (CanvasViewInfo child : parent.getChildren()) {
r = child.getSelectionRect();
//Synthetic comment -- @@ -1240,7 +1284,7 @@
for (CanvasViewInfo child : parent.getChildren()) {
r = child.getSelectionRect();
if (r.contains(x, y)) {
                    findAltViewInfoAt(x, y, child, outList);
}
}
}
//Synthetic comment -- @@ -1255,9 +1299,11 @@
*                 selection list.
*/
private void selectAllViewInfos(CanvasViewInfo canvasViewInfo) {
        mSelections.add(new CanvasSelection(canvasViewInfo, mRulesEngine, mNodeFactory));
        for (CanvasViewInfo vi : canvasViewInfo.getChildren()) {
            selectAllViewInfos(vi);
}
}

//Synthetic comment -- @@ -1799,7 +1845,7 @@
mAltSelection = null;

// Now select everything if there's a valid layout
        if (mIsResultValid && mLastValidResult != null) {
selectAllViewInfos(mLastValidViewInfoRoot);
redraw();
}
//Synthetic comment -- @@ -1940,4 +1986,62 @@
//   Must also be able to copy/paste into an empty document (prolly need to bypass script, and deal with the xmlns)
AdtPlugin.displayWarning("Canvas Paste", "Not implemented yet");
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java
//Synthetic comment -- index 052e2ab..31e42b8 100755

//Synthetic comment -- @@ -23,17 +23,13 @@
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SimpleAttribute;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;

import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.NamedNodeMap;
//Synthetic comment -- @@ -42,9 +38,7 @@
import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
*
//Synthetic comment -- @@ -349,58 +343,8 @@
*/
private ViewElementDescriptor getFqcnViewDescritor(String fqcn) {
AndroidXmlEditor editor = mNode.getEditor();
        if (editor != null) {
            AndroidTargetData data = editor.getTargetData();
            if (data != null) {
                LayoutDescriptors layoutDesc = data.getLayoutDescriptors();
                if (layoutDesc != null) {
                    DocumentDescriptor docDesc = layoutDesc.getDescriptor();
                    if (docDesc != null) {
                        return internalFindFqcnViewDescritor(fqcn, docDesc.getChildren(), null);
                    }
                }
            }
        }

        return null;
    }

    /**
     * Internal helper to recursively search for a {@link ViewElementDescriptor} that matches
     * the requested FQCN.
     *
     * @param fqcn The target View FQCN to find.
     * @param descriptors A list of cildren descriptors to iterate through.
     * @param visited A set we use to remember which descriptors have already been visited,
     *  necessary since the view descriptor hierarchy is cyclic.
     * @return Either a matching {@link ViewElementDescriptor} or null.
     */
    private ViewElementDescriptor internalFindFqcnViewDescritor(String fqcn,
            ElementDescriptor[] descriptors,
            Set<ElementDescriptor> visited) {
        if (visited == null) {
            visited = new HashSet<ElementDescriptor>();
        }

        if (descriptors != null) {
            for (ElementDescriptor desc : descriptors) {
                if (visited.add(desc)) {
                    // Set.add() returns true if this a new element that was added to the set.
                    // That means we haven't visited this descriptor yet.
                    // We want a ViewElementDescriptor with a matching FQCN.
                    if (desc instanceof ViewElementDescriptor &&
                            fqcn.equals(((ViewElementDescriptor) desc).getFullClassName())) {
                        return (ViewElementDescriptor) desc;
                    }

                    // Visit its children
                    ViewElementDescriptor vd =
                        internalFindFqcnViewDescritor(fqcn, desc.getChildren(), visited);
                    if (vd != null) {
                        return vd;
                    }
                }
            }
}

return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 280f518..9d0927a 100644

//Synthetic comment -- @@ -1190,6 +1190,7 @@
new AttributeInfo(xmlAttrLocalName, new Format[] { Format.STRING } )
);
UiAttributeNode uiAttr = desc.createUiNode(this);
mUnknownUiAttributes.add(uiAttr);
return uiAttr;
}







