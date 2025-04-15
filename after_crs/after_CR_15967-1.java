/*ADT GLE2: Properly handle empty documents, including drag'n'drop.

This makes it possible to drop into empty documents.

Change-Id:I512d8ff5e8747e4d3f5e27900308157e75cacd57*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index e245558..fd5198f 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IUnknownDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.CustomViewDescriptorService;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle1.GraphicalLayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle1.UiContentOutlinePage;
//Synthetic comment -- @@ -58,6 +59,8 @@
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
* Multi-page form editor for /res/layout XML files.
//Synthetic comment -- @@ -108,6 +111,10 @@
return mUiRootNode;
}

    public void setNewFileOnConfigChange(boolean state) {
        mNewFileOnConfigChange = state;
    }

// ---- Base Class Overrides ----

@Override
//Synthetic comment -- @@ -307,7 +314,7 @@
/**
* Returns the custom IContentOutlinePage or IPropertySheetPage when asked for it.
*/
    @SuppressWarnings({"rawtypes", "unchecked"})
@Override
public Object getAdapter(Class adapter) {
// for the outline, force it to come from the Graphical Editor.
//Synthetic comment -- @@ -572,7 +579,63 @@
}
}

    /**
     * Helper method that returns a {@link ViewElementDescriptor} for the requested FQCN.
     * Will return null if we can't find that FQCN or we lack the editor/data/descriptors info.
     */
    public ViewElementDescriptor getFqcnViewDescritor(String fqcn) {
        AndroidTargetData data = getTargetData();
        if (data != null) {
            LayoutDescriptors layoutDesc = data.getLayoutDescriptors();
            if (layoutDesc != null) {
                DocumentDescriptor docDesc = layoutDesc.getDescriptor();
                if (docDesc != null) {
                    return internalFindFqcnViewDescritor(fqcn, docDesc.getChildren(), null);
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
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasDropListener.java
//Synthetic comment -- index ee0b802..3ac0be7 100755

//Synthetic comment -- @@ -235,12 +235,6 @@
public void drop(DropTargetEvent event) {
AdtPlugin.printErrorToConsole("DEBUG", "dropped");

SimpleElement[] elements = null;

SimpleXmlTransfer sxt = SimpleXmlTransfer.getInstance();
//Synthetic comment -- @@ -260,6 +254,17 @@
elements = mCurrentDragElements;
}

        if (mTargetNode == null) {
            if (mCanvas.isResultValid() && mCanvas.isEmptyDocument()) {
                // There is no target node because the drop happens on an empty document.
                // Attempt to create a root node accordingly.
                createDocumentRoot(elements);
            } else {
                AdtPlugin.printErrorToConsole("DEBUG", "dropped on null targetNode");
            }
            return;
        }

Point where = mCanvas.displayToCanvasPoint(event.x, event.y);

updateDropFeedback(mFeedback, event);
//Synthetic comment -- @@ -490,4 +495,14 @@
mCanvas.redraw();
}

    private void createDocumentRoot(SimpleElement[] elements) {
        if (elements == null || elements.length < 1 || elements[0] == null) {
            return;
        }

        String rootFqcn = elements[0].getFqcn();

        mCanvas.createDocumentRoot(rootFqcn);
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index ea3171f..5a4761d 100755

//Synthetic comment -- @@ -83,6 +83,7 @@
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -945,7 +946,33 @@
UiDocumentNode model = getModel();

if (model.getUiChildren().size() == 0) {
                    displayError("No XML content. Please add a root view or layout to your document.");

                    // Although we display an error, we still treat an empty document as a
                    // successful layout result so that we can drop new elements in it.
                    //
                    // For that purpose, create a special ILayoutResult that has no image,
                    // no root view yet indicates success and then update the canvas with it.

                    ILayoutResult result = new ILayoutResult() {
                        public String getErrorMessage() {
                            return null;
                        }

                        public BufferedImage getImage() {
                            return null;
                        }

                        public ILayoutViewInfo getRootView() {
                            return null;
                        }

                        public int getSuccess() {
                            return ILayoutResult.SUCCESS;
                        }
                    };

                    mCanvasViewer.getCanvas().setResult(result);
return;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 6a946bd..d01aa71 100755

//Synthetic comment -- @@ -22,13 +22,19 @@
import com.android.ide.eclipse.adt.editors.layout.gscripts.Rect;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.ILayoutResult;
import com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.Action;
//Synthetic comment -- @@ -83,10 +89,11 @@
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.TextActionHandler;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.registry.ViewDescriptor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
//Synthetic comment -- @@ -143,28 +150,25 @@
/** SWT clipboard instance. */
private Clipboard mClipboard;

/**
     * The CanvasViewInfo root created by the last call to {@link #setResult(ILayoutResult)}
     * with a valid layout.
     * <p/>
     * This <em>can</em> be null to indicate we're dealing with an empty document with
     * no root node. Null here does not mean the result was invalid, merely that the XML
     * had no content to display -- we need to treat an empty document as valid so that
     * we can drop new items in it.
*/
private CanvasViewInfo mLastValidViewInfoRoot;

/**
     * True when the last {@link #setResult(ILayoutResult)} provided a valid {@link ILayoutResult}.
     * <p/>
* When false this means the canvas is displaying an out-dated result image & bounds and some
* features should be disabled accordingly such a drag'n'drop.
* <p/>
     * Note that an empty document (with a null {@link #mLastValidViewInfoRoot}) is considered
     * valid since it is an acceptable drop target.
*/
private boolean mIsResultValid;

//Synthetic comment -- @@ -371,17 +375,25 @@

/**
* Returns true when the last {@link #setResult(ILayoutResult)} provided a valid
     * {@link ILayoutResult}.
     * <p/>
* When false this means the canvas is displaying an out-dated result image & bounds and some
* features should be disabled accordingly such a drag'n'drop.
* <p/>
     * Note that an empty document (with a null {@link #mLastValidViewInfoRoot}) is considered
     * valid since it is an acceptable drop target.
    */
/* package */ boolean isResultValid() {
return mIsResultValid;
}

    /**
     * Returns true if the last valid content of the canvas represents an empty document.
     */
    /* package */ boolean isEmptyDocument() {
        return mLastValidViewInfoRoot == null;
    }

/** Returns the Groovy Rules Engine, associated with the current project. */
/* package */ RulesEngine getRulesEngine() {
return mRulesEngine;
//Synthetic comment -- @@ -433,8 +445,12 @@
mIsResultValid = (result != null && result.getSuccess() == ILayoutResult.SUCCESS);

if (mIsResultValid && result != null) {
            ILayoutViewInfo root = result.getRootView();
            if (root == null) {
                mLastValidViewInfoRoot = null;
            } else {
                mLastValidViewInfoRoot = new CanvasViewInfo(result.getRootView());
            }
setImage(result.getImage());

updateNodeProxies(mLastValidViewInfoRoot);
//Synthetic comment -- @@ -462,8 +478,10 @@
// remove the current alternate selection views
mAltSelection = null;

            if (mImage != null) {
                mHScale.setSize(mImage.getImageData().width, getClientArea().width);
                mVScale.setSize(mImage.getImageData().height, getClientArea().height);
            }

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
     * <p/>
     * The image *can* be null, which is the case when we are dealing with an empty document.
*/
private void setImage(BufferedImage awtImage) {
        if (awtImage == null) {
            mImage = null;

        } else {
            int width = awtImage.getWidth();
            int height = awtImage.getHeight();

            Raster raster = awtImage.getData(new java.awt.Rectangle(width, height));
            int[] imageDataBuffer = ((DataBufferInt)raster.getDataBuffer()).getData();

            ImageData imageData = new ImageData(width, height, 32,
                    new PaletteData(0x00FF0000, 0x0000FF00, 0x000000FF));

            imageData.setPixels(0, 0, imageDataBuffer.length, imageDataBuffer, 0);

            mImage = new Image(getDisplay(), imageData);
        }
}

/**
* Sets the alpha for the given GC.
* <p/>
     * Alpha may not work on all platforms and may fail with an exception, which is
     * hidden here (false is returned in that case).
*
* @param gc the GC to change
* @param alpha the new alpha, 0 for transparent, 255 for opaque.
//Synthetic comment -- @@ -845,7 +870,8 @@
/**
* Sets the non-text antialias flag for the given GC.
* <p/>
     * Antialias may not work on all platforms and may fail with an exception, which is
     * hidden here (-2 is returned in that case).
*
* @param gc the GC to change
* @param alias One of {@link SWT#DEFAULT}, {@link SWT#ON}, {@link SWT#OFF}.
//Synthetic comment -- @@ -969,37 +995,35 @@
* Hover on top of a known child.
*/
private void onMouseMove(MouseEvent e) {
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

private void onMouseDown(MouseEvent e) {
        // Pass, not used yet. We do everything on mouse up.
}

/**
//Synthetic comment -- @@ -1010,81 +1034,78 @@
* pointed at (i.e. click on an object then alt-click to cycle).
*/
private void onMouseUp(MouseEvent e) {
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
                mAltSelection = new CanvasAlternateSelection(
                        vi, findAltViewInfoAt(x, y, mLastValidViewInfoRoot));

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

//Synthetic comment -- @@ -1157,6 +1178,9 @@
* Returns null if not found.
*/
private CanvasViewInfo findViewInfoKey(Object viewKey, CanvasViewInfo canvasViewInfo) {
        if (canvasViewInfo == null) {
            return null;
        }
if (canvasViewInfo.getUiViewKey() == viewKey) {
return canvasViewInfo;
}
//Synthetic comment -- @@ -1174,33 +1198,38 @@


/**
     * Tries to find the inner most child matching the given x,y coordinates
     * in the view info sub-tree, starting at the last know view info root.
* This uses the potentially-expanded selection bounds.
     * <p/>
     * Returns null if not found or if there's no view info root.
*/
/* package */ CanvasViewInfo findViewInfoAt(int x, int y) {
if (mLastValidViewInfoRoot == null) {
return null;
} else {
            return findViewInfoAt_Recursive(x, y, mLastValidViewInfoRoot);
}
}

/**
     * Recursive internal version of {@link #findViewInfoAt(int, int)}. Please don't use directly.
     * <p/>
* Tries to find the inner most child matching the given x,y coordinates in the view
* info sub-tree. This uses the potentially-expanded selection bounds.
*
* Returns null if not found.
*/
    private CanvasViewInfo findViewInfoAt_Recursive(int x, int y, CanvasViewInfo canvasViewInfo) {
        if (canvasViewInfo == null) {
            return null;
        }
Rectangle r = canvasViewInfo.getSelectionRect();
if (r.contains(x, y)) {

// try to find a matching child first
for (CanvasViewInfo child : canvasViewInfo.getChildren()) {
                CanvasViewInfo v = findViewInfoAt_Recursive(x, y, child);
if (v != null) {
return v;
}
//Synthetic comment -- @@ -1213,21 +1242,36 @@
return null;
}

    /**
     * Returns a list of all the possible alternatives for a given view at the given
     * position. This is used to build and manage the "alternate" selection that cycles
     * around the parents or children of the currently selected element.
     */
    private List<CanvasViewInfo> findAltViewInfoAt(int x, int y, CanvasViewInfo parent) {
        return findAltViewInfoAt_Recursive(x, y, parent, null);
    }

    /**
     * Internal recursive version of {@link #findAltViewInfoAt(int, int, CanvasViewInfo)}.
     * Please don't use directly.
     */
    private List<CanvasViewInfo> findAltViewInfoAt_Recursive(
            int x, int y, CanvasViewInfo parent, List<CanvasViewInfo> outList) {
Rectangle r;

if (outList == null) {
outList = new ArrayList<CanvasViewInfo>();

            if (parent != null) {
                // add the parent root only once
                r = parent.getSelectionRect();
                if (r.contains(x, y)) {
                    outList.add(parent);
                }
}
}

        if (parent != null && !parent.getChildren().isEmpty()) {
// then add all children that match the position
for (CanvasViewInfo child : parent.getChildren()) {
r = child.getSelectionRect();
//Synthetic comment -- @@ -1240,7 +1284,7 @@
for (CanvasViewInfo child : parent.getChildren()) {
r = child.getSelectionRect();
if (r.contains(x, y)) {
                    findAltViewInfoAt_Recursive(x, y, child, outList);
}
}
}
//Synthetic comment -- @@ -1255,9 +1299,11 @@
*                 selection list.
*/
private void selectAllViewInfos(CanvasViewInfo canvasViewInfo) {
        if (canvasViewInfo != null) {
            mSelections.add(new CanvasSelection(canvasViewInfo, mRulesEngine, mNodeFactory));
            for (CanvasViewInfo vi : canvasViewInfo.getChildren()) {
                selectAllViewInfos(vi);
            }
}
}

//Synthetic comment -- @@ -1799,7 +1845,7 @@
mAltSelection = null;

// Now select everything if there's a valid layout
        if (mIsResultValid && mLastValidViewInfoRoot != null) {
selectAllViewInfos(mLastValidViewInfoRoot);
redraw();
}
//Synthetic comment -- @@ -1940,4 +1986,62 @@
//   Must also be able to copy/paste into an empty document (prolly need to bypass script, and deal with the xmlns)
AdtPlugin.displayWarning("Canvas Paste", "Not implemented yet");
}

    /**
     * Add new root in an existing empty XML layout.
     * <p/>
     * In case of error (unknown FQCN, document not empty), silently do nothing.
     * In case of success, the new element will have some default attributes set (xmlns:android,
     * layout_width and height). The edit is wrapped in a proper undo.
     * <p/>
     * This is invoked by {@link #onPaste()} or
     * by {@link CanvasDropListener#drop(org.eclipse.swt.dnd.DropTargetEvent)}.
     *
     * @param rootFqcn A non-null non-empty FQCN that must match an existing {@link ViewDescriptor}
     *   to add as root to the current empty XML document.
     */
    /* package */ void createDocumentRoot(String rootFqcn) {

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

        // Get the last segment of the FQCN for the undo title
        String title = rootFqcn;
        int pos = title.lastIndexOf('.');
        if (pos > 0 && pos < title.length() - 1) {
            title = title.substring(pos + 1);
        }
        title = String.format("Create root %1$s in document", title);

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

                        // Adjust the attributes
                        DescriptorsUtils.setDefaultLayoutAttributes(uiNew, false /*updateLayout*/);

                        uiNew.createXmlNode();
                    }
                });
            }
        });
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java
//Synthetic comment -- index 052e2ab..31e42b8 100755

//Synthetic comment -- @@ -23,17 +23,13 @@
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SimpleAttribute;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.NamedNodeMap;
//Synthetic comment -- @@ -42,9 +38,7 @@
import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.List;

/**
*
//Synthetic comment -- @@ -349,58 +343,8 @@
*/
private ViewElementDescriptor getFqcnViewDescritor(String fqcn) {
AndroidXmlEditor editor = mNode.getEditor();
        if (editor instanceof LayoutEditor) {
            return ((LayoutEditor) editor).getFqcnViewDescritor(fqcn);
}

return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 280f518..9d0927a 100644

//Synthetic comment -- @@ -1190,6 +1190,7 @@
new AttributeInfo(xmlAttrLocalName, new Format[] { Format.STRING } )
);
UiAttributeNode uiAttr = desc.createUiNode(this);
        uiAttr.setDirty(true);
mUnknownUiAttributes.add(uiAttr);
return uiAttr;
}







