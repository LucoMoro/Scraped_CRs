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
//Synthetic comment -- index 2425ca6..82cc8d9 100644

//Synthetic comment -- @@ -22,15 +22,16 @@

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IGraphics;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.MenuAction;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.api.IDragElement.IDragAttribute;

import java.util.Arrays;
import java.util.HashMap;
//Synthetic comment -- @@ -134,7 +135,7 @@
* old-id => tuple (String new-id, String fqcn) where fqcn is the FQCN of
* the element.
*/
    protected static Map<String, Pair<String, String>> getDropIdMap(INode targetNode,
IDragElement[] elements, boolean createNewIds) {
Map<String, Pair<String, String>> idMap = new HashMap<String, Pair<String, String>>();

//Synthetic comment -- @@ -154,7 +155,7 @@
*
* @see #getDropIdMap
*/
    protected static Map<String, Pair<String, String>> collectIds(Map<String, Pair<String, String>> idMap,
IDragElement[] elements) {
for (IDragElement element : elements) {
IDragAttribute attr = element.getAttribute(ANDROID_URI, ATTR_ID);
//Synthetic comment -- @@ -174,7 +175,7 @@
/**
* Used by #getDropIdMap to find new IDs in case of conflict.
*/
    protected static Map<String, Pair<String, String>> remapIds(INode node,
Map<String, Pair<String, String>> idMap) {
// Visit the document to get a list of existing ids
Set<String> existingIdSet = new HashSet<String>();
//Synthetic comment -- @@ -208,7 +209,7 @@
/**
* Used by #remapIds to find a new ID for a conflicting element.
*/
    protected static String findNewId(String fqcn, Set<String> existingIdSet) {
// Get the last component of the FQCN (e.g. "android.view.Button" =>
// "Button")
String name = fqcn.substring(fqcn.lastIndexOf('.') + 1);
//Synthetic comment -- @@ -228,7 +229,7 @@
/**
* Used by #getDropIdMap to find existing IDs recursively.
*/
    protected static void collectExistingIds(INode root, Set<String> existingIdSet) {
if (root == null) {
return;
}
//Synthetic comment -- @@ -250,7 +251,7 @@
/**
* Transforms @id/name into @+id/name to treat both forms the same way.
*/
    protected static String normalizeId(String id) {
if (id.indexOf("@+") == -1) { //$NON-NLS-1$
id = id.replaceFirst("@", "@+"); //$NON-NLS-1$ //$NON-NLS-2$
}
//Synthetic comment -- @@ -261,7 +262,7 @@
* For use by {@link BaseLayoutRule#addAttributes} A filter should return a
* valid replacement string.
*/
    protected static interface AttributeFilter {
String replace(String attributeUri, String attributeName, String attributeValue);
}

//Synthetic comment -- @@ -316,7 +317,7 @@
* transform the value of all attributes of Format.REFERENCE. If filter is
* non-null, it's a filter that can rewrite the attribute string.
*/
    protected static void addAttributes(INode newNode, IDragElement oldElement,
Map<String, Pair<String, String>> idMap, AttributeFilter filter) {

// A little trick here: when creating new UI widgets by dropping them
//Synthetic comment -- @@ -376,7 +377,7 @@
* Attributes are adjusted by calling addAttributes with idMap as necessary,
* with no closure filter.
*/
    protected static void addInnerElements(INode newNode, IDragElement oldElement,
Map<String, Pair<String, String>> idMap) {

for (IDragElement element : oldElement.getInnerElements()) {
//Synthetic comment -- @@ -387,4 +388,45 @@
addInnerElements(childNode, element, idMap);
}
}

    /**
     * Insert the given elements into the given node at the given position
     *
     * @param targetNode the node to insert into
     * @param elements the elements to insert
     * @param createNewIds if true, generate new ids when there is a conflict
     * @param initialInsertPos index among targetnode's children which to insert the
     *            children
     */
    public static void insertAt(final INode targetNode, final IDragElement[] elements,
            final boolean createNewIds, final int initialInsertPos) {

        // Collect IDs from dropped elements and remap them to new IDs
        // if this is a copy or from a different canvas.
        final Map<String, Pair<String, String>> idMap = getDropIdMap(targetNode, elements,
                createNewIds);

        targetNode.editXml("Insert Elements", new INodeHandler() {

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
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index 995d206..c098e00 100644

//Synthetic comment -- @@ -41,7 +41,6 @@

import java.util.ArrayList;
import java.util.List;

/**
* An {@link IViewRule} for android.widget.LinearLayout and all its derived
//Synthetic comment -- @@ -383,35 +382,7 @@

LinearDropData data = (LinearDropData) feedback.userData;
final int initialInsertPos = data.getInsertPos();
        insertAt(targetNode, elements, feedback.isCopy || !feedback.sameCanvas, initialInsertPos);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index 3f58df1..c2d074a 100644

//Synthetic comment -- @@ -558,6 +558,13 @@
for (MenuAction a2 : actions) {
MenuAction.Choices choice = (MenuAction.Choices) a2;
String current = choice.getCurrent();
                if (current == null) {
                    // None of the choices were selected. This can for example happen if
                    // the user does not have an attribute for "layout_width" set on the element
                    // and the context menu is opened to see the width choices.
                    numOff++;
                    continue;
                }
boolean found = false;

if (current.indexOf(MenuAction.Choices.CHOICE_SEP) >= 0) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java
//Synthetic comment -- index bbd62fe..8365a37 100644

//Synthetic comment -- @@ -49,9 +49,6 @@
* gestures and in order to update the gestures along the way.
*/
public class GestureManager {
/** The canvas which owns this GestureManager. */
private final LayoutCanvas mCanvas;

//Synthetic comment -- @@ -471,28 +468,6 @@
}

/**
* Our canvas {@link DragSourceListener}. Handles drag being started and
* finished and generating the drag data.
*/
//Synthetic comment -- @@ -608,12 +583,7 @@

// Render drag-images: Copy portions of the full screen render.
Image image = mCanvas.getImageOverlay().getImage();
                if (image != null) {
/**
* Transparency of the dragged image ([0-255]). We're using 30%
* translucency to make the image faint and not obscure the drag
//Synthetic comment -- @@ -648,12 +618,10 @@
// View rules may need to know it as well
GlobalCanvasDragInfo dragInfo = GlobalCanvasDragInfo.getInstance();
Rect dragBounds = null;
                        int width = (int) (scale * boundingBox.width);
                        int height = (int) (scale * boundingBox.height);
                        dragBounds = new Rect(deltaX, deltaY, width, height);
                        dragInfo.setDragBounds(dragBounds);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java
//Synthetic comment -- index 9315967..843ed11 100755

//Synthetic comment -- @@ -80,8 +80,14 @@
/** Unregisters elements being dragged. */
public void stopDrag() {
mCurrentElements = null;
        mCurrentSelection = null;
mSourceCanvas = null;
mRemoveSourceHandler = null;
        mDragBounds = null;
    }

    public boolean isDragging() {
        return mCurrentElements != null;
}

/** Returns the elements being dragged. */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 914b33e..2e4f595 100644

//Synthetic comment -- @@ -112,6 +112,8 @@
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.PageBookView;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
//Synthetic comment -- @@ -431,6 +433,19 @@
*/
public void selectionChanged(IWorkbenchPart part, ISelection selection) {
if (!(part instanceof LayoutEditor)) {
            if (part instanceof PageBookView) {
                PageBookView pbv = (PageBookView) part;
                IPage currentPage = pbv.getCurrentPage();
                if (currentPage instanceof OutlinePage) {
                    LayoutCanvas canvas = getCanvasControl();
                    if (canvas != null && canvas.getOutlinePage() != currentPage) {
                        // The notification is not for this view; ignore
                        // (can happen when there are multiple pages simultaneously
                        // visible)
                        return;
                    }
                }
            }
mCanvasViewer.setSelection(selection);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 74f358e..5a9fbeb 100755

//Synthetic comment -- @@ -55,9 +55,7 @@
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
//Synthetic comment -- @@ -278,7 +276,6 @@

mDropTarget = createDropTarget(this);
mDragSource = createDragSource(this);
mGestureManager.registerListeners(mDragSource, mDropTarget);

if (mLayoutEditor == null) {
//Synthetic comment -- @@ -381,22 +378,6 @@
}

/**
* Returns the GCWrapper used to paint view rules.
*
* @return The GCWrapper used to paint view rules
//Synthetic comment -- @@ -444,6 +425,15 @@
}

/**
     * Returns the {@link OutlinePage} associated with this canvas
     *
     * @return the {@link OutlinePage} associated with this canvas
     */
    public OutlinePage getOutlinePage() {
        return mOutlinePage;
    }

    /**
* Returns the {@link SelectionManager} associated with this canvas.
*
* @return The {@link SelectionManager} holding the selection for this








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index 863b250..16f6fba 100644

//Synthetic comment -- @@ -32,7 +32,6 @@

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
//Synthetic comment -- @@ -346,7 +345,8 @@
}
}
// Select the newly dropped nodes
        final SelectionManager selectionManager = mCanvas.getSelectionManager();
        if (!selectionManager.selectDropped(added)) {
// In some scenarios we can't find the actual view infos yet; this
// seems to happen when you drag from one canvas to another (see the
// related comment next to the setFocus() call below). In that case
//Synthetic comment -- @@ -354,7 +354,7 @@
// date.
Display.getDefault().asyncExec(new Runnable() {
public void run() {
                    selectionManager.selectDropped(added);
}
});
}
//Synthetic comment -- @@ -368,26 +368,6 @@
}

/**
* Computes a suitable Undo label to use for a drop operation, such as
* "Drop Button in LinearLayout" and "Move Widgets in RelativeLayout".
*
//Synthetic comment -- @@ -396,7 +376,8 @@
* @param detail The DnD mode, as used in {@link DropTargetEvent#detail}.
* @return A string suitable as an undo-label for the drop event
*/
    public static String computeUndoLabel(NodeProxy targetNode,
            SimpleElement[] elements, int detail) {
// Decide whether it's a move or a copy; we'll label moves specifically
// as a move and consider everything else a "Drop"
String verb = (detail == DND.DROP_MOVE) ? "Move" : "Drop";
//Synthetic comment -- @@ -425,7 +406,7 @@
* @param fqcn The fqcn to reduce
* @return The base name of the fqcn
*/
    public static String getSimpleName(String fqcn) {
// Note that the following works even when there is no dot, since
// lastIndexOf will return -1 so we get fcqn.substring(-1+1) =
// fcqn.substring(0) = fqcn
//Synthetic comment -- @@ -452,20 +433,18 @@
// layout coordinates
GlobalCanvasDragInfo dragInfo = GlobalCanvasDragInfo.getInstance();
Rect dragBounds = null;
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
df.dragBounds = dragBounds;
}
//Synthetic comment -- @@ -477,7 +456,7 @@
*
* @return True if the data type is accepted.
*/
    private static boolean checkDataType(DropTargetEvent event) {

SimpleXmlTransfer sxt = SimpleXmlTransfer.getInstance();









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDragListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDragListener.java
new file mode 100644
//Synthetic comment -- index 0000000..cf46533

//Synthetic comment -- @@ -0,0 +1,125 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import java.util.ArrayList;

/** Drag listener for the outline page */
/* package */ class OutlineDragListener implements DragSourceListener {
    private TreeViewer mTreeViewer;
    private OutlinePage mOutlinePage;
    private final ArrayList<SelectionItem> mDragSelection = new ArrayList<SelectionItem>();
    private SimpleElement[] mDragElements;

    public OutlineDragListener(OutlinePage outlinePage, TreeViewer treeViewer) {
        super();
        mOutlinePage = outlinePage;
        mTreeViewer = treeViewer;
    }

    public void dragStart(DragSourceEvent e) {
        Tree tree = mTreeViewer.getTree();

        TreeItem overTreeItem = tree.getItem(new Point(e.x, e.y));
        if (overTreeItem == null) {
            // Not dragging over a tree item
            e.doit = false;
            return;
        }
        CanvasViewInfo over = getViewInfo(overTreeItem);
        if (over == null) {
            e.doit = false;
            return;
        }

        // The selection logic for the outline is much simpler than in the canvas,
        // because for one thing, the tree selection is updated synchronously on mouse
        // down, so it's not possible to start dragging a non-selected item.
        // We also don't deliberately disallow root-element dragging since you can
        // drag it into another form.
        final LayoutCanvas canvas = mOutlinePage.getEditor().getCanvasControl();
        SelectionManager selectionManager = canvas.getSelectionManager();
        TreeItem[] treeSelection = tree.getSelection();
        mDragSelection.clear();
        for (TreeItem item : treeSelection) {
            CanvasViewInfo viewInfo = getViewInfo(item);
            if (viewInfo != null) {
                mDragSelection.add(selectionManager.createSelection(viewInfo));
            }
        }
        SelectionManager.sanitize(mDragSelection);

        e.doit = !mDragSelection.isEmpty();
        int imageCount = mDragSelection.size();
        if (e.doit) {
            mDragElements = SelectionItem.getAsElements(mDragSelection);
            GlobalCanvasDragInfo.getInstance().startDrag(mDragElements,
                    mDragSelection.toArray(new SelectionItem[imageCount]),
                    canvas, new Runnable() {
                        public void run() {
                            canvas.getClipboardSupport().deleteSelection("Remove",
                                    mDragSelection);
                        }
                    });
            return;
        }

        e.detail = DND.DROP_NONE;
    }

    public void dragSetData(DragSourceEvent e) {
        if (TextTransfer.getInstance().isSupportedType(e.dataType)) {
            LayoutCanvas canvas = mOutlinePage.getEditor().getCanvasControl();
            e.data = SelectionItem.getAsText(canvas, mDragSelection);
            return;
        }

        if (SimpleXmlTransfer.getInstance().isSupportedType(e.dataType)) {
            e.data = mDragElements;
            return;
        }

        // otherwise we failed
        e.detail = DND.DROP_NONE;
        e.doit = false;
    }

    public void dragFinished(DragSourceEvent e) {
        // Unregister the dragged data.
        // Clear the selection
        mDragSelection.clear();
        mDragElements = null;
        GlobalCanvasDragInfo.getInstance().stopDrag();
    }

    private CanvasViewInfo getViewInfo(TreeItem item) {
        Object data = item.getData();
        if (data != null) {
            return OutlinePage.getViewInfo(data);
        }

        return null;
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java
new file mode 100644
//Synthetic comment -- index 0000000..d07b3fd

//Synthetic comment -- @@ -0,0 +1,226 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Drop listener for the outline page */
/*package*/ class OutlineDropListener extends ViewerDropAdapter {
    private final OutlinePage mOutlinePage;

    public OutlineDropListener(OutlinePage outlinePage, TreeViewer treeViewer) {
        super(treeViewer);
        mOutlinePage = outlinePage;
    }

    @Override
    public void dragEnter(DropTargetEvent event) {
        if (event.detail == DND.DROP_NONE && GlobalCanvasDragInfo.getInstance().isDragging()) {
            // For some inexplicable reason, we get DND.DROP_NONE from the palette
            // even though in its drag start we set DND.DROP_COPY, so correct that here...
            int operation = DND.DROP_COPY;
            event.detail = operation;
        }
        super.dragEnter(event);
    }

    @Override
    public boolean performDrop(Object data) {
        final DropTargetEvent event = getCurrentEvent();
        int location = determineLocation(event);
        if (location == LOCATION_NONE) {
            return false;
        }

        final SimpleElement[] elements;
        SimpleXmlTransfer sxt = SimpleXmlTransfer.getInstance();
        if (sxt.isSupportedType(event.currentDataType)) {
            if (data instanceof SimpleElement[]) {
                elements = (SimpleElement[]) data;
            } else {
                return false;
            }
        } else {
            return false;
        }
        if (elements.length == 0) {
            return false;
        }

        // Determine target:
        CanvasViewInfo parent = OutlinePage.getViewInfo(event.item.getData());
        if (parent == null) {
            return false;
        }

        int index = -1;
        UiViewElementNode parentNode = parent.getUiViewNode();
        if (location == LOCATION_BEFORE || location == LOCATION_AFTER) {
            UiViewElementNode node = parentNode;
            parent = parent.getParent();
            if (parent == null) {
                return false;
            }
            parentNode = parent.getUiViewNode();

            // Determine index
            index = 0;
            for (UiElementNode child : parentNode.getUiChildren()) {
                if (child == node) {
                    break;
                }
                index++;
            }
            if (location == LOCATION_AFTER) {
                index++;
            }
        }

        // Copy into new position.
        final LayoutCanvas canvas = mOutlinePage.getEditor().getCanvasControl();
        final NodeProxy targetNode = canvas.getNodeFactory().create(parentNode);

        // Record children of the target right before the drop (such that we can
        // find out after the drop which exact children were inserted)
        Set<INode> children = new HashSet<INode>();
        for (INode node : targetNode.getChildren()) {
            children.add(node);
        }

        String label = MoveGesture.computeUndoLabel(targetNode, elements, event.detail);
        final int indexFinal = index;
        canvas.getLayoutEditor().wrapUndoEditXmlModel(label, new Runnable() {
            public void run() {
                Object sourceCanvas = GlobalCanvasDragInfo.getInstance().getSourceCanvas();
                InsertType insertType;
                if (event.detail == DND.DROP_MOVE) {
                    insertType = InsertType.MOVE;
                } else if (sourceCanvas != null) {
                    insertType = InsertType.PASTE;
                } else {
                    insertType = InsertType.CREATE;
                }
                canvas.getRulesEngine().setInsertType(insertType);

                boolean createNew = event.detail == DND.DROP_COPY || sourceCanvas != canvas;
                BaseLayoutRule.insertAt(targetNode, elements, createNew, indexFinal);

                // Clean up drag if applicable
                if (event.detail == DND.DROP_MOVE) {
                    GlobalCanvasDragInfo.getInstance().removeSource();
                }
            }
        });

        // Now find out which nodes were added, and look up their corresponding
        // CanvasViewInfos
        final List<INode> added = new ArrayList<INode>();
        for (INode node : targetNode.getChildren()) {
            if (!children.contains(node)) {
                added.add(node);
            }
        }
        // Select the newly dropped nodes
        final SelectionManager selectionManager = canvas.getSelectionManager();
        if (!selectionManager.selectDropped(added)) {
            // In some scenarios we can't find the actual view infos yet; this
            // seems to happen when you drag from one canvas to another (see the
            // related comment next to the setFocus() call below). In that case
            // defer selection briefly until the view hierarchy etc is up to
            // date.
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    selectionManager.selectDropped(added);
                }
            });
        }

        canvas.redraw();

        return true;
    }

    @Override
    public boolean validateDrop(Object target, int operation,
            TransferData transferType) {
        DropTargetEvent event = getCurrentEvent();
        int location = determineLocation(event);
        if (location == LOCATION_NONE) {
            return false;
        }

        SimpleXmlTransfer sxt = SimpleXmlTransfer.getInstance();
        if (!sxt.isSupportedType(transferType)) {
            return false;
        }

        CanvasViewInfo parent = OutlinePage.getViewInfo(event.item.getData());
        if (parent == null) {
            return false;
        }

        UiViewElementNode parentNode = parent.getUiViewNode();

        if (location == LOCATION_ON) {
            // Targeting the middle of an item means to add it as a new child
            // of the given element. This is only allowed on some types of nodes.
            if (!parentNode.getDescriptor().hasChildren()) {
                return false;
            }
        }

        // Check that the drop target position is not a child or identical to
        // one of the dragged items
        SelectionItem[] sel = GlobalCanvasDragInfo.getInstance().getCurrentSelection();
        if (sel != null) {
            for (SelectionItem item : sel) {
                if (isAncestor(item.getViewInfo().getUiViewNode(), parentNode)) {
                    return false;
                }
            }
        }

        return true;
    }

    /** Returns true if the given parent node is an ancestor of the given child node  */
    private boolean isAncestor(UiElementNode parent, UiElementNode child) {
        while (child != null) {
            if (child == parent) {
                return true;
            }
            child = child.getUiParent();
        }
        return false;
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 93d32fd..33032de 100755

//Synthetic comment -- @@ -44,19 +44,13 @@
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.ISelectionListener;
//Synthetic comment -- @@ -69,7 +63,7 @@
import java.util.ArrayList;

/**
 * An outline page for the layout canvas view.
* <p/>
* The page is created by {@link LayoutEditor#getAdapter(Class)}. This means
* we have *one* instance of the outline page per open canvas editor.
//Synthetic comment -- @@ -79,8 +73,6 @@
* The underlying page is also a selection provider (via IContentOutlinePage)
* and as such it will broadcast selection changes to the site's selection service
* (on which both the layout editor part and the property sheet page listen.)
*/
public class OutlinePage extends ContentOutlinePage
implements ISelectionListener, INullSelectionListener {
//Synthetic comment -- @@ -102,17 +94,19 @@
*/
private MenuManager mMenuManager;

    /** Action to Select All in the tree */
    private final Action mTreeSelectAllAction = new Action() {
        @Override
        public void run() {
            getTreeViewer().getTree().selectAll();
            OutlinePage.this.fireSelectionChanged(getSelection());
        }

        @Override
        public String getId() {
            return ActionFactory.SELECT_ALL.getId();
        }
    };

public OutlinePage(GraphicalEditorPart graphicalEditorPart) {
super();
//Synthetic comment -- @@ -129,8 +123,16 @@
tv.setLabelProvider(new LabelProvider());
tv.setInput(mRootWrapper);

        int supportedOperations = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] {
            SimpleXmlTransfer.getInstance()
        };

        tv.addDropSupport(supportedOperations, transfers, new OutlineDropListener(this, tv));
        tv.addDragSupport(supportedOperations, transfers, new OutlineDragListener(this, tv));

// The tree viewer will hold CanvasViewInfo instances, however these
        // change each time the canvas is reloaded. OTOH layoutlib gives us
// constant UiView keys which we can use to perform tree item comparisons.
tv.setComparer(new IElementComparer() {
public int hashCode(Object element) {
//Synthetic comment -- @@ -172,12 +174,6 @@
}
});

setupContextMenu();

// Listen to selection changes from the layout editor
//Synthetic comment -- @@ -192,16 +188,6 @@

@Override
public void dispose() {
mRootWrapper.setRoot(null);

getSite().getPage().removeSelectionListener(this);
//Synthetic comment -- @@ -209,7 +195,7 @@
}

/**
     * Invoked by {@link LayoutCanvas} to set the model (a.k.a. the root view info).
*
* @param rootViewInfo The root of the view info hierarchy. Can be null.
*/
//Synthetic comment -- @@ -300,6 +286,17 @@
}
}

    /** Return the {@link CanvasViewInfo} associated with the given TreeItem's data field */
    /* package */ static CanvasViewInfo getViewInfo(Object viewData) {
        if (viewData instanceof RootWrapper) {
            return ((RootWrapper) viewData).getRoot();
        }
        if (viewData instanceof CanvasViewInfo) {
            return (CanvasViewInfo) viewData;
        }
        return null;
    }

// --- Content and Label Providers ---

/**
//Synthetic comment -- @@ -549,213 +546,22 @@
}
}

    /** Returns the associated editor with this outline */
    /* package */GraphicalEditorPart getEditor() {
        return mGraphicalEditorPart;
}

    @Override
    public void setActionBars(IActionBars actionBars) {
        super.setActionBars(actionBars);

        // Map Outline actions to canvas actions such that they share Undo context etc
        LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
        canvas.updateGlobalActions(actionBars);

        // Special handling for Select All since it's different than the canvas (will
        // include selecting the root etc)
        actionBars.setGlobalActionHandler(mTreeSelectAllAction.getId(), mTreeSelectAllAction);
        actionBars.updateActionBars();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index c00367b..830e884 100755

//Synthetic comment -- @@ -561,6 +561,9 @@
null /* canvas */,
null /* removeSource */);
dragInfo.setDragBounds(dragBounds);

            e.detail = DND.DROP_COPY;
            e.doit = true;
}

public void dragSetData(DragSourceEvent e) {
//Synthetic comment -- @@ -666,6 +669,9 @@
int offsetY = imageBounds.height / 2;
SwtUtils.setDragImageOffsets(event, offsetX, offsetY);
}

            event.doit = true;
            event.detail = DND.DROP_COPY;
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index 9553159..b44deba 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.INode;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -656,6 +657,26 @@
}
}

    /**
     * Selects the given list of nodes in the canvas, and returns true iff the
     * attempt to select was successful.
     *
     * @param nodes The collection of nodes to be selected
     * @return True if and only if all nodes were successfully selected
     */
    public boolean selectDropped(Collection<INode> nodes) {
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

private void updateMenuActions() {
boolean hasSelection = !mSelections.isEmpty();
mCanvas.updateMenuActionState(hasSelection);
//Synthetic comment -- @@ -665,7 +686,7 @@
mCanvas.redraw();
}

    /* package */ SelectionItem createSelection(CanvasViewInfo vi) {
return new SelectionItem(vi, mCanvas.getRulesEngine(),
mCanvas.getNodeFactory());
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index ae894e6..1a0319e 100755

//Synthetic comment -- @@ -456,6 +456,15 @@
}
}

    /**
     * Set the type of insert currently in progress
     *
     * @param insertType the insert type to use for the next operation
     */
    public void setInsertType(InsertType insertType) {
        mInsertType = insertType;
    }

// ---- private ---

/**







