/*Add isSame on IDragElement, and sort primary to front

Change-Id:I7121b5f0e3714fec705387603f641bc14ed0ab3e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index 04373e1..a1571e2 100644

//Synthetic comment -- @@ -304,8 +304,9 @@
for (IDragElement element : elements) {
// This tries to determine if an INode corresponds to an
// IDragElement, by comparing their bounds.
                    if (element.isSame(it)) {
isDragged = true;
                        break;
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java
//Synthetic comment -- index d8a45b6..e2c1d5e 100644

//Synthetic comment -- @@ -717,6 +717,7 @@
// operation.
List<SelectionItem> selections = selectionManager.getSelections();
mDragSelection.clear();
            SelectionItem primary = null;

if (!selections.isEmpty()) {
// Is the cursor on top of a selected element?
//Synthetic comment -- @@ -724,6 +725,7 @@

for (SelectionItem cs : selections) {
if (!cs.isRoot() && cs.getRect().contains(p.x, p.y)) {
                        primary = cs;
insideSelection = true;
break;
}
//Synthetic comment -- @@ -732,7 +734,7 @@
if (!insideSelection) {
CanvasViewInfo vi = mCanvas.getViewHierarchy().findViewInfoAt(p);
if (vi != null && !vi.isRoot() && !vi.isHidden()) {
                        primary = selectionManager.selectSingle(vi);
insideSelection = true;
}
}
//Synthetic comment -- @@ -753,6 +755,8 @@
for (SelectionItem cs : selections) {
if (!cs.isRoot() && !cs.isHidden()) {
mDragSelection.add(cs);
                            } else if (cs == primary) {
                                primary = null;
}
}
}
//Synthetic comment -- @@ -763,7 +767,7 @@
if (mDragSelection.isEmpty()) {
CanvasViewInfo vi = mCanvas.getViewHierarchy().findViewInfoAt(p);
if (vi != null && !vi.isRoot() && !vi.isHidden()) {
                    primary = selectionManager.selectSingle(vi);
mDragSelection.addAll(selections);
}
}
//Synthetic comment -- @@ -773,7 +777,7 @@
e.doit = !mDragSelection.isEmpty();
int imageCount = mDragSelection.size();
if (e.doit) {
                mDragElements = SelectionItem.getAsElements(mDragSelection, primary);
GlobalCanvasDragInfo.getInstance().startDrag(mDragElements,
mDragSelection.toArray(new SelectionItem[imageCount]),
mCanvas, new Runnable() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java
//Synthetic comment -- index 06986cd..b918b00 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.Rect;

//Synthetic comment -- @@ -63,6 +65,10 @@
* Registers the XML elements being dragged.
*
* @param elements The elements being dragged
     * @param primary the "primary" element among the elements; when there is a
     *            single item dragged this will be the same, but in
     *            multi-selection it will be the element under the mouse as the
     *            selection was initiated
* @param selection The selection (which can be null, for example when the
*            user drags from the palette)
* @param sourceCanvas An object representing the source we are dragging
//Synthetic comment -- @@ -71,8 +77,11 @@
*            source. It should only be invoked if the drag operation is a
*            move, not a copy.
*/
    public void startDrag(
            @NonNull SimpleElement[] elements,
            @Nullable SelectionItem[] selection,
            @Nullable Object sourceCanvas,
            @Nullable Runnable removeSourceHandler) {
mCurrentElements = elements;
mCurrentSelection = selection;
mSourceCanvas = sourceCanvas;
//Synthetic comment -- @@ -93,6 +102,7 @@
}

/** Returns the elements being dragged. */
    @NonNull
public SimpleElement[] getCurrentElements() {
return mCurrentElements;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionItem.java
//Synthetic comment -- index 5d49426..d104e37 100644

//Synthetic comment -- @@ -169,13 +169,39 @@
* @return An array of wrapper elements. Never null.
*/
@NonNull
    static SimpleElement[] getAsElements(@NonNull List<SelectionItem> items) {
        return getAsElements(items, null);
    }

    /**
     * Returns elements representing the given selection of canvas items.
     *
     * @param items Items to wrap in elements
     * @param primary The primary selected item which should be listed first
     * @return An array of wrapper elements. Never null.
     */
    @NonNull
    static SimpleElement[] getAsElements(
            @NonNull List<SelectionItem> items,
            @Nullable SelectionItem primary) {
        List<SimpleElement> elements = new ArrayList<SimpleElement>();

        if (primary != null) {
            CanvasViewInfo vi = primary.getViewInfo();
            SimpleElement e = vi.toSimpleElement();
            e.setSelectionItem(primary);
            elements.add(e);
        }

for (SelectionItem cs : items) {
            if (cs == primary) {
                // Already handled
                continue;
            }

            CanvasViewInfo vi = cs.getViewInfo();
SimpleElement e = vi.toSimpleElement();
            e.setSelectionItem(cs);
elements.add(e);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index 1450768..dc8a383 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionHandle.PIXEL_RADIUS;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.INode;
import com.android.ide.common.layout.GridLayoutRule;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -429,11 +430,15 @@

/**
* Removes all the currently selected item and only select the given item.
     * Issues a redraw() if the selection changes.
*
* @param vi The new selected item if non-null. Selection becomes empty if null.
     * @return the item selected, or null if the selection was cleared (e.g. vi was null)
*/
    @Nullable
    SelectionItem selectSingle(CanvasViewInfo vi) {
        SelectionItem item = null;

// reset alternate selection if any
mAltSelection = null;

//Synthetic comment -- @@ -449,13 +454,14 @@
if (!mSelections.isEmpty()) {
if (mSelections.size() == 1 && mSelections.getFirst().getViewInfo() == vi) {
// CanvasSelection remains the same, don't touch it.
                return mSelections.getFirst();
}
mSelections.clear();
}

if (vi != null) {
            item = createSelection(vi);
            mSelections.add(item);
if (vi.isInvisible()) {
redoLayout = true;
}
//Synthetic comment -- @@ -467,6 +473,8 @@
}

redraw();

        return item;
}

/** Returns true if the view hierarchy is showing exploded items. */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SimpleElement.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SimpleElement.java
//Synthetic comment -- index 4feff25..9acc8c2 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;

import java.util.ArrayList;
//Synthetic comment -- @@ -47,6 +48,7 @@

private IDragAttribute[] mCachedAttributes = null;
private IDragElement[] mCachedElements = null;
    private SelectionItem mSelectionItem;

/**
* Creates a new {@link SimpleElement} with the specified element name.
//Synthetic comment -- @@ -141,6 +143,43 @@
mElements.add(e);
}

    @Override
    public boolean isSame(@NonNull INode node) {
        if (mSelectionItem != null) {
            return node == mSelectionItem.getNode();
        } else {
            return node.getBounds().equals(mBounds);
        }
    }

    void setSelectionItem(@Nullable SelectionItem selectionItem) {
        mSelectionItem = selectionItem;
    }

    @Nullable
    SelectionItem getSelectionItem() {
        return mSelectionItem;
    }

    @Nullable
    static SimpleElement findPrimary(SimpleElement[] elements, SelectionItem primary) {
        if (elements == null || elements.length == 0) {
            return null;
        }

        if (elements.length == 1 || primary == null) {
            return elements[0];
        }

        for (SimpleElement element : elements) {
            if (element.getSelectionItem() == primary) {
                return element;
            }
        }

        return elements[0];
    }

// reader and writer methods

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestDragElement.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestDragElement.java
//Synthetic comment -- index eb0a432..5b55532 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;

import java.util.ArrayList;
//Synthetic comment -- @@ -150,5 +151,8 @@
+ mRect + "]";
}

    @Override
    public boolean isSame(INode node) {
        return node.getBounds().equals(getBounds());
    }
}








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/IDragElement.java b/rule_api/src/com/android/ide/common/api/IDragElement.java
//Synthetic comment -- index 885ba35..50a5014 100644

//Synthetic comment -- @@ -86,6 +86,14 @@
public abstract IDragElement[] getInnerElements();

/**
     * Returns true if the given {@link INode} represents this drag element
     *
     * @param node the node to be checked
     * @return true if the given node represents this drag element
     */
    public abstract boolean isSame(@NonNull INode node);

    /**
* An XML attribute in the {@link IDragElement}.
* <p/>
* The attribute is always represented by a namespace URI, a name and a value.








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/IViewRule.java b/rule_api/src/com/android/ide/common/api/IViewRule.java
//Synthetic comment -- index bcf4e89..c115795 100644

//Synthetic comment -- @@ -161,7 +161,9 @@
* @param targetView the corresponding View object for the target layout, or
*            null if not known
* @param elements an array of {@link IDragElement} element descriptors for
     *            the dragged views. When there are more than one element, the
     *            first element will always be the "primary" element (e.g. the
     *            one that the mouse is actively dragging.)
* @return a {@link DropFeedback} object with drop state (which will be
*         supplied to a follow-up {@link #onDropMove} call), or null if the
*         drop should be ignored
//Synthetic comment -- @@ -178,7 +180,9 @@
* @param targetNode the {@link INode} for the target layout receiving a
*            drop event
* @param elements an array of {@link IDragElement} element descriptors for
     *            the dragged views.  When there are more than one element, the
     *            first element will always be the "primary" element (e.g. the
     *            one that the mouse is actively dragging.)
* @param feedback the {@link DropFeedback} object created by
*            {@link #onDropEnter(INode, Object, IDragElement[])}
* @param where the current mouse drag position
//Synthetic comment -- @@ -211,7 +215,9 @@
* @param targetNode the {@link INode} for the target layout receiving a
*            drop event
* @param elements an array of {@link IDragElement} element descriptors for
     *            the dragged views.  When there are more than one element, the
     *            first element will always be the "primary" element (e.g. the
     *            one that the mouse is actively dragging.)
* @param feedback the {@link DropFeedback} object created by
*            {@link #onDropEnter(INode, Object, IDragElement[])}
*/
//Synthetic comment -- @@ -230,7 +236,9 @@
* @param targetNode the {@link INode} for the target layout receiving a
*            drop event
* @param elements an array of {@link IDragElement} element descriptors for
     *            the dragged views.  When there are more than one element, the
     *            first element will always be the "primary" element (e.g. the
     *            one that the mouse is actively dragging.)
* @param feedback the {@link DropFeedback} object created by
*            {@link #onDropEnter(INode, Object, IDragElement[])}
* @param where the mouse drop position







