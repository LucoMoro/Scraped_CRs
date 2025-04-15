/*Rewrite Outline drag & drop handler

Rewrite the drag source and drop target listeners for the Outline. The
drop target listener now uses the SWT Tree support for drag & drop
(such that you for example get drop position feedback lines between
siblings). You can now drag items within the outline to do precise
reordering, as well as target particular positions during drops,
either within the outline or from the canvas or the palette.

This changeset also fixes a number of other issues I ran into at the same time:

- Fix keyboard shortcuts such that they map to the same context as the
  canvas (e.g. when you activate the outline it shows the same undo
  context as if you click in the associated canvas)

- Fix a bug with context menu code when none of the options are
  selected in the XML

- Fix selection dispatch. If you had two side by side canvases,
  selecting items in the Outline would show highlights in both
  canvases; it now only causes selection syncing with the associated
  canvas.

Change-Id:I00c3c38fabf3711c826a3bc527356cbc77ad4a7e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java
//Synthetic comment -- index 2425ca6..eeb0478 100644

//Synthetic comment -- @@ -22,15 +22,16 @@

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IDragElement.IDragAttribute;
import com.android.ide.common.api.IGraphics;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.MenuAction;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

import java.util.Arrays;
import java.util.HashMap;
//Synthetic comment -- @@ -134,7 +135,7 @@
* old-id => tuple (String new-id, String fqcn) where fqcn is the FQCN of
* the element.
*/
    protected Map<String, Pair<String, String>> getDropIdMap(INode targetNode,
IDragElement[] elements, boolean createNewIds) {
Map<String, Pair<String, String>> idMap = new HashMap<String, Pair<String, String>>();

//Synthetic comment -- @@ -154,7 +155,8 @@
*
* @see #getDropIdMap
*/
    protected Map<String, Pair<String, String>> collectIds(Map<String, Pair<String, String>> idMap,
IDragElement[] elements) {
for (IDragElement element : elements) {
IDragAttribute attr = element.getAttribute(ANDROID_URI, ATTR_ID);
//Synthetic comment -- @@ -174,7 +176,7 @@
/**
* Used by #getDropIdMap to find new IDs in case of conflict.
*/
    protected Map<String, Pair<String, String>> remapIds(INode node,
Map<String, Pair<String, String>> idMap) {
// Visit the document to get a list of existing ids
Set<String> existingIdSet = new HashSet<String>();
//Synthetic comment -- @@ -208,7 +210,7 @@
/**
* Used by #remapIds to find a new ID for a conflicting element.
*/
    protected String findNewId(String fqcn, Set<String> existingIdSet) {
// Get the last component of the FQCN (e.g. "android.view.Button" =>
// "Button")
String name = fqcn.substring(fqcn.lastIndexOf('.') + 1);
//Synthetic comment -- @@ -228,7 +230,7 @@
/**
* Used by #getDropIdMap to find existing IDs recursively.
*/
    protected void collectExistingIds(INode root, Set<String> existingIdSet) {
if (root == null) {
return;
}
//Synthetic comment -- @@ -250,7 +252,7 @@
/**
* Transforms @id/name into @+id/name to treat both forms the same way.
*/
    protected String normalizeId(String id) {
if (id.indexOf("@+") == -1) { //$NON-NLS-1$
id = id.replaceFirst("@", "@+"); //$NON-NLS-1$ //$NON-NLS-2$
}
//Synthetic comment -- @@ -261,7 +263,7 @@
* For use by {@link BaseLayoutRule#addAttributes} A filter should return a
* valid replacement string.
*/
    public static interface AttributeFilter {
String replace(String attributeUri, String attributeName, String attributeValue);
}

//Synthetic comment -- @@ -316,7 +318,7 @@
* transform the value of all attributes of Format.REFERENCE. If filter is
* non-null, it's a filter that can rewrite the attribute string.
*/
    protected void addAttributes(INode newNode, IDragElement oldElement,
Map<String, Pair<String, String>> idMap, AttributeFilter filter) {

// A little trick here: when creating new UI widgets by dropping them
//Synthetic comment -- @@ -376,7 +378,7 @@
* Attributes are adjusted by calling addAttributes with idMap as necessary,
* with no closure filter.
*/
    protected void addInnerElements(INode newNode, IDragElement oldElement,
Map<String, Pair<String, String>> idMap) {

for (IDragElement element : oldElement.getInnerElements()) {
//Synthetic comment -- @@ -387,4 +389,45 @@
addInnerElements(childNode, element, idMap);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index 995d206..c098e00 100644

//Synthetic comment -- @@ -41,7 +41,6 @@

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* An {@link IViewRule} for android.widget.LinearLayout and all its derived
//Synthetic comment -- @@ -383,35 +382,7 @@

LinearDropData data = (LinearDropData) feedback.userData;
final int initialInsertPos = data.getInsertPos();

        // Collect IDs from dropped elements and remap them to new IDs
        // if this is a copy or from a different canvas.
        final Map<String, Pair<String, String>> idMap = getDropIdMap(targetNode, elements,
                feedback.isCopy || !feedback.sameCanvas);

        targetNode.editXml("Add elements to LinearLayout", new INodeHandler() {

            public void handle(INode node) {
                // Now write the new elements.
                int insertPos = initialInsertPos;
                for (IDragElement element : elements) {
                    String fqcn = element.getFqcn();

                    INode newChild = targetNode.insertChildAt(fqcn, insertPos);

                    // insertPos==-1 means to insert at the end. Otherwise
                    // increment the insertion position.
                    if (insertPos >= 0) {
                        insertPos++;
                    }

                    // Copy all the attributes, modifying them as needed.
                    addAttributes(newChild, element, idMap, DEFAULT_ATTR_FILTER);

                    addInnerElements(newChild, element, idMap);
                }
            }
        });
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index 79060d1..078e4f3 100644

//Synthetic comment -- @@ -558,6 +558,13 @@
for (MenuAction a2 : actions) {
MenuAction.Choices choice = (MenuAction.Choices) a2;
String current = choice.getCurrent();
boolean found = false;

if (current.indexOf(MenuAction.Choices.CHOICE_SEP) >= 0) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java
//Synthetic comment -- index bbd62fe..8365a37 100644

//Synthetic comment -- @@ -49,9 +49,6 @@
* gestures and in order to update the gestures along the way.
*/
public class GestureManager {
    /** Drag source data key */
    private static String KEY_DRAG_PREVIEW = "dragpreview"; //$NON-NLS-1$

/** The canvas which owns this GestureManager. */
private final LayoutCanvas mCanvas;

//Synthetic comment -- @@ -471,28 +468,6 @@
}

/**
     * Sets whether the given drag source is enabled for drag previews.
     * The default for a drag source is false.
     *
     * @param source the drag source in question
     * @param enable true to enable drag previews
     */
    public static void setDragPreviewEnabled(DragSource source, boolean enable) {
        source.setData(KEY_DRAG_PREVIEW, enable ? KEY_DRAG_PREVIEW : null);
    }

    /**
     * Returns true if the given drag source is enabled for drag previews.
     * The default for a drag source is false.
     *
     * @param source the drag source in question
     * @return true if the drag source allows drag previews
     */
    public static boolean isDragPreviewEnabled(DragSource source) {
        return source.getData(KEY_DRAG_PREVIEW) != null;
    }

    /**
* Our canvas {@link DragSourceListener}. Handles drag being started and
* finished and generating the drag data.
*/
//Synthetic comment -- @@ -608,12 +583,7 @@

// Render drag-images: Copy portions of the full screen render.
Image image = mCanvas.getImageOverlay().getImage();
                boolean enabled = false;
                if (e.widget instanceof DragSource) {
                    DragSource ds = (DragSource) e.widget;
                    enabled = isDragPreviewEnabled(ds);
                }
                if (enabled && image != null) {
/**
* Transparency of the dragged image ([0-255]). We're using 30%
* translucency to make the image faint and not obscure the drag
//Synthetic comment -- @@ -648,12 +618,10 @@
// View rules may need to know it as well
GlobalCanvasDragInfo dragInfo = GlobalCanvasDragInfo.getInstance();
Rect dragBounds = null;
                        if (dragInfo != null) {
                            int width = (int) (scale * boundingBox.width);
                            int height = (int) (scale * boundingBox.height);
                            dragBounds = new Rect(deltaX, deltaY, width, height);
                            dragInfo.setDragBounds(dragBounds);
                        }
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java
//Synthetic comment -- index 9315967..843ed11 100755

//Synthetic comment -- @@ -80,8 +80,14 @@
/** Unregisters elements being dragged. */
public void stopDrag() {
mCurrentElements = null;
mSourceCanvas = null;
mRemoveSourceHandler = null;
}

/** Returns the elements being dragged. */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index cf72e16..5668f05 100644

//Synthetic comment -- @@ -111,6 +111,8 @@
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
//Synthetic comment -- @@ -430,6 +432,19 @@
*/
public void selectionChanged(IWorkbenchPart part, ISelection selection) {
if (!(part instanceof LayoutEditor)) {
mCanvasViewer.setSelection(selection);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index cd411d2..a4808a0 100755

//Synthetic comment -- @@ -56,9 +56,7 @@
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
//Synthetic comment -- @@ -279,7 +277,6 @@

mDropTarget = createDropTarget(this);
mDragSource = createDragSource(this);
        GestureManager.setDragPreviewEnabled(mDragSource, true);
mGestureManager.registerListeners(mDragSource, mDropTarget);

if (mLayoutEditor == null) {
//Synthetic comment -- @@ -382,22 +379,6 @@
}

/**
     * Returns our {@link DragSourceListener}.
     * This is used by {@link OutlinePage} to delegate drag source events.
     */
    /* package */ DragSourceListener getDragListener() {
        return mGestureManager.getDragSourceListener();
    }

    /**
     * Returns our {@link DropTargetListener}.
     * This is used by {@link OutlinePage} to delegate drop target events.
     */
    /* package */ DropTargetListener getDropListener() {
        return mGestureManager.getDropTargetListener();
    }

    /**
* Returns the GCWrapper used to paint view rules.
*
* @return The GCWrapper used to paint view rules
//Synthetic comment -- @@ -445,6 +426,15 @@
}

/**
* Returns the {@link SelectionManager} associated with this canvas.
*
* @return The {@link SelectionManager} holding the selection for this
//Synthetic comment -- @@ -765,7 +755,8 @@
// TODO: Only set rendering target portion of the state
QualifiedName qname = ConfigurationComposite.NAME_CONFIG_STATE;
String state = leavingFile.getPersistentProperty(qname);
                            xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INITIAL_STATE, state);
} catch (CoreException e) {
// pass
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index 863b250..16f6fba 100644

//Synthetic comment -- @@ -32,7 +32,6 @@

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
//Synthetic comment -- @@ -346,7 +345,8 @@
}
}
// Select the newly dropped nodes
        if (!selectDropped(added)) {
// In some scenarios we can't find the actual view infos yet; this
// seems to happen when you drag from one canvas to another (see the
// related comment next to the setFocus() call below). In that case
//Synthetic comment -- @@ -354,7 +354,7 @@
// date.
Display.getDefault().asyncExec(new Runnable() {
public void run() {
                    selectDropped(added);
}
});
}
//Synthetic comment -- @@ -368,26 +368,6 @@
}

/**
     * Selects the given list of nodes in the canvas, and returns true iff the
     * attempt to select was successful.
     *
     * @param nodes The collection of nodes to be selected
     * @return True if and only if all nodes were successfully selected
     */
    private boolean selectDropped(Collection<INode> nodes) {
        final Collection<CanvasViewInfo> newChildren = new ArrayList<CanvasViewInfo>();
        for (INode node : nodes) {
            CanvasViewInfo viewInfo = mCanvas.getViewHierarchy().findViewInfoFor(node);
            if (viewInfo != null) {
                newChildren.add(viewInfo);
            }
        }
        mCanvas.getSelectionManager().selectMultiple(newChildren);

        return nodes.size() == newChildren.size();
    }

    /**
* Computes a suitable Undo label to use for a drop operation, such as
* "Drop Button in LinearLayout" and "Move Widgets in RelativeLayout".
*
//Synthetic comment -- @@ -396,7 +376,8 @@
* @param detail The DnD mode, as used in {@link DropTargetEvent#detail}.
* @return A string suitable as an undo-label for the drop event
*/
    private String computeUndoLabel(NodeProxy targetNode, SimpleElement[] elements, int detail) {
// Decide whether it's a move or a copy; we'll label moves specifically
// as a move and consider everything else a "Drop"
String verb = (detail == DND.DROP_MOVE) ? "Move" : "Drop";
//Synthetic comment -- @@ -425,7 +406,7 @@
* @param fqcn The fqcn to reduce
* @return The base name of the fqcn
*/
    private String getSimpleName(String fqcn) {
// Note that the following works even when there is no dot, since
// lastIndexOf will return -1 so we get fcqn.substring(-1+1) =
// fcqn.substring(0) = fqcn
//Synthetic comment -- @@ -452,20 +433,18 @@
// layout coordinates
GlobalCanvasDragInfo dragInfo = GlobalCanvasDragInfo.getInstance();
Rect dragBounds = null;
        if (dragInfo != null) {
            Rect controlDragBounds = dragInfo.getDragBounds();
            if (controlDragBounds != null) {
                CanvasTransform ht = mCanvas.getHorizontalTransform();
                CanvasTransform vt = mCanvas.getVerticalTransform();
                double horizScale = ht.getScale();
                double verticalScale = vt.getScale();
                int x = (int) (controlDragBounds.x / horizScale);
                int y = (int) (controlDragBounds.y / verticalScale);
                int w = (int) (controlDragBounds.w / horizScale);
                int h = (int) (controlDragBounds.h / verticalScale);

                dragBounds = new Rect(x, y, w, h);
            }
}
df.dragBounds = dragBounds;
}
//Synthetic comment -- @@ -477,7 +456,7 @@
*
* @return True if the data type is accepted.
*/
    private boolean checkDataType(DropTargetEvent event) {

SimpleXmlTransfer sxt = SimpleXmlTransfer.getInstance();









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDragListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDragListener.java
new file mode 100644
//Synthetic comment -- index 0000000..cf46533

//Synthetic comment -- @@ -0,0 +1,125 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java
new file mode 100644
//Synthetic comment -- index 0000000..e777e3b

//Synthetic comment -- @@ -0,0 +1,253 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 93d32fd..33032de 100755

//Synthetic comment -- @@ -44,19 +44,13 @@
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.ISelectionListener;
//Synthetic comment -- @@ -69,7 +63,7 @@
import java.util.ArrayList;

/**
 * An outline page for the GLE2 canvas view.
* <p/>
* The page is created by {@link LayoutEditor#getAdapter(Class)}. This means
* we have *one* instance of the outline page per open canvas editor.
//Synthetic comment -- @@ -79,8 +73,6 @@
* The underlying page is also a selection provider (via IContentOutlinePage)
* and as such it will broadcast selection changes to the site's selection service
* (on which both the layout editor part and the property sheet page listen.)
 *
 * @since GLE2
*/
public class OutlinePage extends ContentOutlinePage
implements ISelectionListener, INullSelectionListener {
//Synthetic comment -- @@ -102,17 +94,19 @@
*/
private MenuManager mMenuManager;

    /**
     * Drag source, created with the same attributes as the one from {@link LayoutCanvas}.
     * The drag source listener delegates to the current GraphicalEditorPart.
     */
    private DragSource mDragSource;

    /**
     * Drop target, created with the same attributes as the one from {@link LayoutCanvas}.
     * The drop target listener delegates to the current GraphicalEditorPart.
     */
    private DropTarget mDropTarget;

public OutlinePage(GraphicalEditorPart graphicalEditorPart) {
super();
//Synthetic comment -- @@ -129,8 +123,16 @@
tv.setLabelProvider(new LabelProvider());
tv.setInput(mRootWrapper);

// The tree viewer will hold CanvasViewInfo instances, however these
        // change each time the canvas is reloaded. OTOH liblayout gives us
// constant UiView keys which we can use to perform tree item comparisons.
tv.setComparer(new IElementComparer() {
public int hashCode(Object element) {
//Synthetic comment -- @@ -172,12 +174,6 @@
}
});

        mDragSource = LayoutCanvas.createDragSource(getControl());
        mDragSource.addDragListener(new DelegateDragListener());

        mDropTarget = LayoutCanvas.createDropTarget(getControl());
        mDropTarget.addDropListener(new DelegateDropListener());

setupContextMenu();

// Listen to selection changes from the layout editor
//Synthetic comment -- @@ -192,16 +188,6 @@

@Override
public void dispose() {
        if (mDragSource != null) {
            mDragSource.dispose();
            mDragSource = null;
        }

        if (mDropTarget != null) {
            mDropTarget.dispose();
            mDropTarget = null;
        }

mRootWrapper.setRoot(null);

getSite().getPage().removeSelectionListener(this);
//Synthetic comment -- @@ -209,7 +195,7 @@
}

/**
     * Invoked by {@link LayoutCanvas} to set the model (aka the root view info).
*
* @param rootViewInfo The root of the view info hierarchy. Can be null.
*/
//Synthetic comment -- @@ -300,6 +286,17 @@
}
}

// --- Content and Label Providers ---

/**
//Synthetic comment -- @@ -549,213 +546,22 @@
}
}


    // --- Drag Source ---

    /**
     * Delegates the drag events to the {@link LayoutCanvas}.
     * <p/>
     * We convert the drag coordinates from the bounding box {@link TreeViewer}'s
     * cell to the ones of the bounding of the corresponding canvas element.
     */
    private class DelegateDragListener implements DragSourceListener {

        private final Point mTempPoint = new Point(0, 0);

        public void dragStart(DragSourceEvent event) {
            if (!adjustEventCoordinates(event)) {
                event.doit = false;
                return;
            }
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                canvas.getDragListener().dragStart(event);
            }
        }

        public void dragSetData(DragSourceEvent event) {
            adjustEventCoordinates(event);
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                canvas.getDragListener().dragSetData(event);
            }
        }

        public void dragFinished(DragSourceEvent event) {
            adjustEventCoordinates(event);
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                canvas.getDragListener().dragFinished(event);
            }
        }

        /**
         * Finds the element under which the drag started and adjusts
         * its event coordinates to match the canvas *control* coordinates.
         * <p/>
         * Returns false if no element was found at the given position,
         * which is used to cancel the drag start.
         */
        private boolean adjustEventCoordinates(DragSourceEvent event) {
            if (event.x == mTempPoint.x && event.y == mTempPoint.y) {
                // Seems like the event is reusing the coordinates we last
                // converted. Avoid converting them twice. We only modified
                // the event struct in case of a successful conversion so
                // we can return true here.
                return true;
            }

            mTempPoint.x = event.x;
            mTempPoint.y = event.y;

            boolean result = viewerToCanvasControlCoordinates(mTempPoint);
            if (result) {
                event.x = mTempPoint.x;
                event.y = mTempPoint.y;
            }

            return result;
        }
}

    // --- Drop Target ---

    /**
     * Delegates drop events to the {@link LayoutCanvas}.
     * <p/>
     * We convert the drag/drop coordinates from the bounding box {@link TreeViewer}'s
     * cell to the ones of the bounding of the corresponding canvas element.
     */
    private class DelegateDropListener implements DropTargetListener {

        private final Point mTempPoint = new Point(0, 0);

        public void dragEnter(DropTargetEvent event) {
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                adjustEventCoordinates(canvas, event);
                canvas.getDropListener().dragEnter(event);
            }
        }

        public void dragLeave(DropTargetEvent event) {
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                adjustEventCoordinates(canvas, event);
                canvas.getDropListener().dragLeave(event);
            }
        }

        public void dragOperationChanged(DropTargetEvent event) {
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                adjustEventCoordinates(canvas, event);
                canvas.getDropListener().dragOperationChanged(event);
            }
        }

        public void dragOver(DropTargetEvent event) {
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                adjustEventCoordinates(canvas, event);
                canvas.getDropListener().dragOver(event);
            }
        }

        public void dropAccept(DropTargetEvent event) {
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                adjustEventCoordinates(canvas, event);
                canvas.getDropListener().dropAccept(event);
            }
        }

        public void drop(DropTargetEvent event) {
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                adjustEventCoordinates(canvas, event);
                canvas.getDropListener().drop(event);
            }
        }

        /**
         * Finds the element under which the drag started and adjusts
         * its event coordinates to match the canvas *control* coordinates.
         * <p/>
         * Returns false if no element was found at the given position,
         * which is used to cancel the drag start.
         */
        private boolean adjustEventCoordinates(LayoutCanvas canvas, DropTargetEvent event) {
            if (event.x == mTempPoint.x && event.y == mTempPoint.y) {
                // Seems like the event is reusing the coordinates we last
                // converted. Avoid converting them twice. We only modified
                // the event struct in case of a successful conversion so
                // we can return true here.
                return true;
            }

            // Note that whereas DragSource coordinates are relative to the
            // control, DropTarget coordinates are relative to the display.
            // So we need to convert from display to control (treeview) coordinates.
            Point p = getControl().toControl(event.x, event.y);

            mTempPoint.x = p.x;
            mTempPoint.y = p.y;

            boolean result = viewerToCanvasControlCoordinates(mTempPoint);
            if (result) {
                // We now convert the canvas control coordinates to display coordinates.
                p = canvas.toDisplay(mTempPoint);

                event.x = p.x;
                event.y = p.y;
            }

            return result;
        }
}

    /**
     * Finds the element under which the drag started and adjusts
     * its event coordinates to match the canvas *control* coordinates.
     * <p/>
     * I need to repeat this to make it extra clear: this returns canvas *control*
     * coordinates, not just "canvas coordinates".
     * <p/>
     * @param inOutXY The event x/y coordinates in input (in control coordinates).
     *   If the method returns true, it places the canvas *control* coordinates here.
     * @return false if no element was found at the given position, or true
     *   if the tree viewer cell was found and the coordinates were correctly converted.
     */
    private boolean viewerToCanvasControlCoordinates(Point inOutXY) {
        ViewerCell cell = getTreeViewer().getCell(inOutXY);
        if (cell != null) {
            Rectangle cr = cell.getBounds();
            Object item = cell.getElement();

            if (cr != null && !cr.isEmpty() && item instanceof CanvasViewInfo) {
                CanvasViewInfo vi = (CanvasViewInfo) item;
                Rectangle vir = vi.getAbsRect();

                // interpolate from the "cr" bounding box to the "vir" bounding box
                double ratio = (double) vir.width / (double) cr.width;
                int x = (int) (vir.x + ratio * (inOutXY.x - cr.x));
                ratio = (double) vir.height / (double) cr.height;
                int y = (int) (vir.y + ratio * (inOutXY.y - cr.y));

                LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
                if (canvas != null) {
                    com.android.ide.common.api.Point p =
                        canvas.layoutToControlPoint(x, y);

                    inOutXY.x = p.x;
                    inOutXY.y = p.y;
                    return true;
                }
            }
        }

        return false;
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index c00367b..ef75e8b 100755

//Synthetic comment -- @@ -561,6 +561,8 @@
null /* canvas */,
null /* removeSource */);
dragInfo.setDragBounds(dragBounds);
}

public void dragSetData(DragSourceEvent e) {
//Synthetic comment -- @@ -666,6 +668,8 @@
int offsetY = imageBounds.height / 2;
SwtUtils.setDragImageOffsets(event, offsetX, offsetY);
}
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index 9553159..b44deba 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -656,6 +657,26 @@
}
}

private void updateMenuActions() {
boolean hasSelection = !mSelections.isEmpty();
mCanvas.updateMenuActionState(hasSelection);
//Synthetic comment -- @@ -665,7 +686,7 @@
mCanvas.redraw();
}

    private SelectionItem createSelection(CanvasViewInfo vi) {
return new SelectionItem(vi, mCanvas.getRulesEngine(),
mCanvas.getNodeFactory());
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index ae894e6..1a0319e 100755

//Synthetic comment -- @@ -456,6 +456,15 @@
}
}

// ---- private ---

/**







