/*Improvements to relative layout move and delete operations

This changeset improves the way the RelativeLayout editing support in
the layout editor handles deletions and moves.

First, during a move, if the move is simply within the same layout,
then the layout constraints are left alone such that if you for
example have

      A
      v
      B < C < D

and you move B up to be next to A, you end up with

      A < B < C < D

(It will however remove cycles if the move would result in them.)

Second, it now handles deletion better where deleting a view will
cause all references to any deleted views to be replaced by transitive
constraints.

For example, if you hve

     A < B < C < D

and you delete B and C, you end up with

     A < D

Change-Id:Icb9d3552e60aee20192f7941fe52be71ba52557f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridLayoutRule.java
//Synthetic comment -- index 4f25902..40063f2 100644

//Synthetic comment -- @@ -403,8 +403,9 @@
}

@Override
    public void onRemovingChildren(@NonNull List<INode> deleted, @NonNull INode parent) {
        super.onRemovingChildren(deleted, parent);

// Attempt to clean up spacer objects for any newly-empty rows or columns
// as the result of this deletion








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index e23f9f4..850d017 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ABOVE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BASELINE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BOTTOM;
//Synthetic comment -- @@ -36,8 +35,6 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_LEFT_OF;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_RIGHT_OF;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.utils.XmlUtils.ANDROID_URI;

//Synthetic comment -- @@ -48,7 +45,6 @@
import com.android.ide.common.api.IGraphics;
import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INode.IAttribute;
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -57,6 +53,7 @@
import com.android.ide.common.api.RuleAction;
import com.android.ide.common.api.SegmentType;
import com.android.ide.common.layout.relative.ConstraintPainter;
import com.android.ide.common.layout.relative.GuidelinePainter;
import com.android.ide.common.layout.relative.MoveHandler;
import com.android.ide.common.layout.relative.ResizeHandler;
//Synthetic comment -- @@ -66,10 +63,8 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* An {@link IViewRule} for android.widget.RelativeLayout and all its derived
//Synthetic comment -- @@ -161,7 +156,7 @@
@Override
public DropFeedback onDropMove(@NonNull INode targetNode, @NonNull IDragElement[] elements,
@Nullable DropFeedback feedback, @NonNull Point p) {
        if (elements == null || elements.length == 0) {
return null;
}

//Synthetic comment -- @@ -184,6 +179,10 @@
@Override
public void onDropped(final @NonNull INode targetNode, final @NonNull IDragElement[] elements,
final @Nullable DropFeedback feedback, final @NonNull Point p) {
final MoveHandler state = (MoveHandler) feedback.userData;

final Map<String, Pair<String, String>> idMap = getDropIdMap(targetNode, elements,
//Synthetic comment -- @@ -251,40 +250,14 @@
}

@Override
    public void onRemovingChildren(@NonNull List<INode> deleted, @NonNull INode parent) {
        super.onRemovingChildren(deleted, parent);

        // Remove any attachments pointing to the deleted nodes.

        // Produce set of attribute values that we want to delete if
        // present in a layout attribute
        Set<String> removeValues = new HashSet<String>(deleted.size() * 2);
        for (INode node : deleted) {
            String id = node.getStringAttr(ANDROID_URI, ATTR_ID);
            if (id != null) {
                removeValues.add(id);
                if (id.startsWith(NEW_ID_PREFIX)) {
                    removeValues.add(ID_PREFIX + stripIdPrefix(id));
                } else {
                    removeValues.add(NEW_ID_PREFIX + stripIdPrefix(id));
                }
            }
        }

        for (INode child : parent.getChildren()) {
            if (deleted.contains(child)) {
                continue;
            }
            for (IAttribute attribute : child.getLiveAttributes()) {
                if (attribute.getName().startsWith(ATTR_LAYOUT_PREFIX) &&
                        ANDROID_URI.equals(attribute.getUri())) {
                    String value = attribute.getValue();
                    if (removeValues.contains(value)) {
                        // Unset this reference to a deleted widget.
                        child.setAttribute(ANDROID_URI, attribute.getName(), null);
                    }
                }
            }
}
}

//Synthetic comment -- @@ -303,6 +276,10 @@
public void onResizeUpdate(@Nullable DropFeedback feedback, @NonNull INode child,
@NonNull INode parent, @NonNull Rect newBounds,
int modifierMask) {
ResizeHandler state = (ResizeHandler) feedback.userData;
state.updateResize(feedback, child, newBounds, modifierMask);
}
//Synthetic comment -- @@ -310,6 +287,9 @@
@Override
public void onResizeEnd(@Nullable DropFeedback feedback, @NonNull INode child,
@NonNull INode parent, final @NonNull Rect newBounds) {
final ResizeHandler state = (ResizeHandler) feedback.userData;

child.editXml("Resize", new INodeHandler() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/ConstraintType.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/ConstraintType.java
//Synthetic comment -- index 7c6fae8..bb32086 100644

//Synthetic comment -- @@ -40,6 +40,8 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_LEFT_OF;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_RIGHT_OF;

import com.android.ide.common.api.SegmentType;

import java.util.HashMap;
//Synthetic comment -- @@ -138,7 +140,8 @@
* @param attribute the name of the attribute to look up
* @return the corresponding {@link ConstraintType}
*/
    public static ConstraintType fromAttribute(String attribute) {
if (sNameToType == null) {
ConstraintType[] types = ConstraintType.values();
Map<String, ConstraintType> map = new HashMap<String, ConstraintType>(types.length);
//Synthetic comment -- @@ -169,6 +172,7 @@
* @param to the target edge
* @return a {@link ConstraintType}, or null
*/
public static ConstraintType forMatch(boolean withParent, SegmentType from, SegmentType to) {
// Attached to parent edge?
if (withParent) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/DeletionHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/DeletionHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..30f12c0

//Synthetic comment -- @@ -0,0 +1,267 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/GuidelineHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/GuidelineHandler.java
//Synthetic comment -- index e6b50ba..135cabe 100644

//Synthetic comment -- @@ -51,7 +51,6 @@
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.ide.common.layout.relative.ConstraintType.ALIGN_BASELINE;
import static com.android.utils.XmlUtils.ANDROID_URI;

import static java.lang.Math.abs;

import com.android.ide.common.api.DropFeedback;
//Synthetic comment -- @@ -292,6 +291,11 @@
return false;
}

public void checkCycles(DropFeedback feedback) {
// Deliberate short circuit evaluation -- only list the first cycle
feedback.errorMessage = null;
//Synthetic comment -- @@ -658,6 +662,7 @@
}
}

public void removeCycles() {
if (mHorizontalCycle != null) {
removeCycles(mHorizontalDeps);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/MoveHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/MoveHandler.java
//Synthetic comment -- index 7b237a2..cc1953a 100644

//Synthetic comment -- @@ -25,7 +25,6 @@
import static com.android.ide.common.api.SegmentType.TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.utils.XmlUtils.ANDROID_URI;

import static java.lang.Math.abs;

import com.android.ide.common.api.DropFeedback;
//Synthetic comment -- @@ -49,8 +48,15 @@
* edges, and so on -- and picks the best among these.
*/
public class MoveHandler extends GuidelineHandler {
    public int mDraggedBaseline;

public MoveHandler(INode layout, IDragElement[] elements, IClientRulesEngine rulesEngine) {
super(layout, rulesEngine);

//Synthetic comment -- @@ -159,6 +165,15 @@
}
}

public void updateMove(DropFeedback feedback, IDragElement[] elements,
int offsetX, int offsetY, int modifierMask) {
mSnap = (modifierMask & DropFeedback.MODIFIER2) == 0;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/ResizeHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/ResizeHandler.java
//Synthetic comment -- index d1d8f37..0e44724 100644

//Synthetic comment -- @@ -25,7 +25,6 @@
import static com.android.ide.common.api.SegmentType.TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.utils.XmlUtils.ANDROID_URI;

import static java.lang.Math.abs;

import com.android.ide.common.api.DropFeedback;
//Synthetic comment -- @@ -44,10 +43,18 @@
* edges in a RelativeLayout.
*/
public class ResizeHandler extends GuidelineHandler {
    public final INode mResized;
    public final SegmentType mHorizontalEdgeType;
    public final SegmentType mVerticalEdgeType;

public ResizeHandler(INode layout, INode resized,
IClientRulesEngine rulesEngine,
SegmentType horizontalEdgeType, SegmentType verticalEdgeType) {
//Synthetic comment -- @@ -58,7 +65,6 @@
assert horizontalEdgeType != CENTER_HORIZONTAL && verticalEdgeType != CENTER_HORIZONTAL;
assert horizontalEdgeType != CENTER_VERTICAL && verticalEdgeType != CENTER_VERTICAL;

        mResized = resized;
mHorizontalEdgeType = horizontalEdgeType;
mVerticalEdgeType = verticalEdgeType;

//Synthetic comment -- @@ -162,6 +168,14 @@
return compatible;
}

public void updateResize(DropFeedback feedback, INode child, Rect newBounds,
int modifierMask) {
mSnap = (modifierMask & DropFeedback.MODIFIER2) == 0;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ClipboardSupport.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ClipboardSupport.java
//Synthetic comment -- index 21e005f..dbb4144 100644

//Synthetic comment -- @@ -142,9 +142,7 @@
*/
public void cutSelectionToClipboard(List<SelectionItem> selection) {
copySelectionToClipboard(selection);
        deleteSelection(
                mCanvas.getCutLabel(),
                selection);
}

/**
//Synthetic comment -- @@ -170,9 +168,11 @@
for (SelectionItem cs : selection) {
CanvasViewInfo vi = cs.getViewInfo();
if (vi != null && vi.getParent() != null) {
if (title == null) {
                    title = vi.getParent().getName();
                } else if (!title.equals(vi.getParent().getName())) {
// More than one kind of parent selected.
title = null;
break;
//Synthetic comment -- @@ -210,6 +210,9 @@
new HashMap<NodeProxy, List<INode>>();
for (SelectionItem cs : selection) {
NodeProxy node = cs.getNode();
INode parent = node.getParent();
if (parent != null) {
List<INode> children = clusters.get(parent);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index c2a535e..520396d 100644

//Synthetic comment -- @@ -404,6 +404,7 @@
if (event.detail == DND.DROP_MOVE) {
GlobalCanvasDragInfo.getInstance().removeSource();
}
}
});
} finally {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index a8438d8..7dc908b 100755

//Synthetic comment -- @@ -558,7 +558,8 @@
IViewRule parentRule = loadRule(parentUiNode);
if (parentRule != null) {
try {
                    parentRule.onRemovingChildren(children, parentNode);
} catch (Exception e) {
AdtPlugin.log(e, "%s.onDispose() failed: %s",
parentRule.getClass().getSimpleName(),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java
//Synthetic comment -- index b9176f6..372e329 100644

//Synthetic comment -- @@ -36,8 +36,10 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.google.common.base.Splitter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.StringWriter;
//Synthetic comment -- @@ -336,8 +338,24 @@
}

@Override
public boolean setAttribute(String uri, String localName, String value) {
            mElement.setAttributeNS(uri, localName, value);
return super.setAttribute(uri, localName, value);
}

//Synthetic comment -- @@ -395,6 +413,33 @@
}
}

// Recursively initialize this node with the bounds specified in the given hierarchy
// dump (from ViewHierarchy's DUMP_INFO flag
public void assignBounds(String bounds) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/relative/DeletionHandlerTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/relative/DeletionHandlerTest.java
new file mode 100644
//Synthetic comment -- index 0000000..f5ec1ba

//Synthetic comment -- @@ -0,0 +1,445 @@








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/AbstractViewRule.java b/rule_api/src/com/android/ide/common/api/AbstractViewRule.java
//Synthetic comment -- index 7f05809..068580f 100644

//Synthetic comment -- @@ -47,8 +47,6 @@
return null;
}

    // ==== Selection ====

@Override
@Nullable
public List<String> getSelectionHint(@NonNull INode parentNode, @NonNull INode childNode) {
//Synthetic comment -- @@ -69,12 +67,10 @@
@NonNull List<? extends INode> childNodes, @Nullable Object view) {
}

    // ==== Drag & drop support ====

    // By default Views do not accept drag'n'drop.
@Override
@Nullable
    public DropFeedback onDropEnter(@NonNull INode targetNode, @Nullable Object targetView, @Nullable IDragElement[] elements) {
return null;
}

//Synthetic comment -- @@ -86,7 +82,8 @@
}

@Override
    public void onDropLeave(@NonNull INode targetNode, @NonNull IDragElement[] elements, @Nullable DropFeedback feedback) {
// ignore
}

//Synthetic comment -- @@ -101,39 +98,42 @@


@Override
    public void onPaste(@NonNull INode targetNode, @Nullable Object targetView, @NonNull IDragElement[] pastedElements) {
    }

    // ==== Create/Remove hooks ====

    @Override
    public void onCreate(@NonNull INode node, @NonNull INode parent, @NonNull InsertType insertType) {
}

@Override
    public void onChildInserted(@NonNull INode child, @NonNull INode parent, @NonNull InsertType insertType) {
}

@Override
    public void onRemovingChildren(@NonNull List<INode> deleted, @NonNull INode parent) {
}

    // ==== Resizing ====

@Override
@Nullable
    public DropFeedback onResizeBegin(@NonNull INode child, @NonNull INode parent, @Nullable SegmentType horizontalEdge,
            @Nullable SegmentType verticalEdge, @Nullable Object childView, @Nullable Object parentView) {
return null;
}

@Override
    public void onResizeUpdate(@Nullable DropFeedback feedback, @NonNull INode child, @NonNull INode parent, @NonNull Rect newBounds,
int modifierMask) {
}

@Override
    public void onResizeEnd(@Nullable DropFeedback feedback, @NonNull INode child, final @NonNull INode parent,
            final @NonNull Rect newBounds) {
}
}








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/IViewRule.java b/rule_api/src/com/android/ide/common/api/IViewRule.java
//Synthetic comment -- index c115795..b16df26 100644

//Synthetic comment -- @@ -298,19 +298,32 @@
@NonNull InsertType insertType);

/**
     * Called when one or more children are about to be deleted by the user. Note that
     * children deleted programmatically from view rules (via
* {@link INode#removeChild(INode)}) will not notify about deletion.
* <p>
     * Note that this method will be called under an edit lock, so rules can directly
     * add/remove nodes and attributes as part of the deletion handling (and their
     * actions will be part of the same undo-unit.)
*
* @param deleted a nonempty list of children about to be deleted
     * @param parent the parent of the deleted children (which still contains the children
     *            since this method is called before the deletion is performed)
*/
    void onRemovingChildren(@NonNull List<INode> deleted, @NonNull INode parent);

/**
* Called by the IDE on the parent layout when a child widget is being resized. This







