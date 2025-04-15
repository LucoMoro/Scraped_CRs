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
//Synthetic comment -- index 787a2c2..f4092e6 100644

//Synthetic comment -- @@ -403,8 +403,9 @@
}

@Override
    public void onRemovingChildren(@NonNull List<INode> deleted, @NonNull INode parent,
            boolean moved) {
        super.onRemovingChildren(deleted, parent, moved);

// Attempt to clean up spacer objects for any newly-empty rows or columns
// as the result of this deletion








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index e9cd5d5..aeb02c2 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ABOVE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BASELINE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BOTTOM;
//Synthetic comment -- @@ -36,8 +35,6 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_LEFT_OF;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_RIGHT_OF;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.util.XmlUtils.ANDROID_URI;

//Synthetic comment -- @@ -48,7 +45,6 @@
import com.android.ide.common.api.IGraphics;
import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -57,6 +53,7 @@
import com.android.ide.common.api.RuleAction;
import com.android.ide.common.api.SegmentType;
import com.android.ide.common.layout.relative.ConstraintPainter;
import com.android.ide.common.layout.relative.DeletionHandler;
import com.android.ide.common.layout.relative.GuidelinePainter;
import com.android.ide.common.layout.relative.MoveHandler;
import com.android.ide.common.layout.relative.ResizeHandler;
//Synthetic comment -- @@ -66,10 +63,8 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* An {@link IViewRule} for android.widget.RelativeLayout and all its derived
//Synthetic comment -- @@ -161,7 +156,7 @@
@Override
public DropFeedback onDropMove(@NonNull INode targetNode, @NonNull IDragElement[] elements,
@Nullable DropFeedback feedback, @NonNull Point p) {
        if (elements == null || elements.length == 0 || feedback == null) {
return null;
}

//Synthetic comment -- @@ -184,6 +179,10 @@
@Override
public void onDropped(final @NonNull INode targetNode, final @NonNull IDragElement[] elements,
final @Nullable DropFeedback feedback, final @NonNull Point p) {
        if (feedback == null) {
            return;
        }

final MoveHandler state = (MoveHandler) feedback.userData;

final Map<String, Pair<String, String>> idMap = getDropIdMap(targetNode, elements,
//Synthetic comment -- @@ -251,40 +250,14 @@
}

@Override
    public void onRemovingChildren(@NonNull List<INode> deleted, @NonNull INode parent,
            boolean moved) {
        super.onRemovingChildren(deleted, parent, moved);

        if (!moved) {
            DeletionHandler handler = new DeletionHandler(deleted, Collections.<INode>emptyList(),
                    parent);
            handler.updateConstraints();
}
}

//Synthetic comment -- @@ -303,6 +276,10 @@
public void onResizeUpdate(@Nullable DropFeedback feedback, @NonNull INode child,
@NonNull INode parent, @NonNull Rect newBounds,
int modifierMask) {
        if (feedback == null) {
            return;
        }

ResizeHandler state = (ResizeHandler) feedback.userData;
state.updateResize(feedback, child, newBounds, modifierMask);
}
//Synthetic comment -- @@ -310,6 +287,9 @@
@Override
public void onResizeEnd(@Nullable DropFeedback feedback, @NonNull INode child,
@NonNull INode parent, final @NonNull Rect newBounds) {
        if (feedback == null) {
            return;
        }
final ResizeHandler state = (ResizeHandler) feedback.userData;

child.editXml("Resize", new INodeHandler() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/ConstraintType.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/ConstraintType.java
//Synthetic comment -- index 7c6fae8..bb32086 100644

//Synthetic comment -- @@ -40,6 +40,8 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_LEFT_OF;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_RIGHT_OF;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.SegmentType;

import java.util.HashMap;
//Synthetic comment -- @@ -138,7 +140,8 @@
* @param attribute the name of the attribute to look up
* @return the corresponding {@link ConstraintType}
*/
    @Nullable
    public static ConstraintType fromAttribute(@NonNull String attribute) {
if (sNameToType == null) {
ConstraintType[] types = ConstraintType.values();
Map<String, ConstraintType> map = new HashMap<String, ConstraintType>(types.length);
//Synthetic comment -- @@ -169,6 +172,7 @@
* @param to the target edge
* @return a {@link ConstraintType}, or null
*/
    @Nullable
public static ConstraintType forMatch(boolean withParent, SegmentType from, SegmentType to) {
// Attached to parent edge?
if (withParent) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/DeletionHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/DeletionHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..5f3a351

//Synthetic comment -- @@ -0,0 +1,267 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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
package com.android.ide.common.layout.relative;

import static com.android.ide.common.layout.BaseViewRule.stripIdPrefix;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.relative.ConstraintType.LAYOUT_CENTER_HORIZONTAL;
import static com.android.ide.common.layout.relative.ConstraintType.LAYOUT_CENTER_VERTICAL;
import static com.android.util.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INode.IAttribute;
import com.android.ide.common.layout.LayoutConstants;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Handles deletions in a relative layout, transferring constraints across
 * deleted nodes
 * <p>
 * TODO: Consider adding the
 * {@link LayoutConstants#ATTR_LAYOUT_ALIGN_WITH_PARENT_MISSING} attribute to a
 * node if it's pointing to a node which is deleted and which has no transitive
 * reference to another node.
 */
public class DeletionHandler {
    private final INode mLayout;
    private final INode[] mChildren;
    private final List<INode> mDeleted;
    private final Set<String> mDeletedIds;
    private final Map<String, INode> mNodeMap;
    private final List<INode> mMoved;

    /**
     * Creates a new {@link DeletionHandler}
     *
     * @param deleted the deleted nodes
     * @param moved nodes that were moved (e.g. deleted, but also inserted elsewhere)
     * @param layout the parent layout of the deleted nodes
     */
    public DeletionHandler(@NonNull List<INode> deleted, @NonNull List<INode> moved,
            @NonNull INode layout) {
        mDeleted = deleted;
        mMoved = moved;
        mLayout = layout;

        mChildren = mLayout.getChildren();
        mNodeMap = Maps.newHashMapWithExpectedSize(mChildren.length);
        for (INode child : mChildren) {
            String id = child.getStringAttr(ANDROID_URI, ATTR_ID);
            if (id != null) {
                mNodeMap.put(stripIdPrefix(id), child);
            }
        }

        mDeletedIds = Sets.newHashSetWithExpectedSize(mDeleted.size());
        for (INode node : mDeleted) {
            String id = node.getStringAttr(ANDROID_URI, ATTR_ID);
            if (id != null) {
                mDeletedIds.add(stripIdPrefix(id));
            }
        }

        // Any widgets that remain (e.g. typically because they were moved) should
        // keep their incoming dependencies
        for (INode node : mMoved) {
            String id = node.getStringAttr(ANDROID_URI, ATTR_ID);
            if (id != null) {
                mDeletedIds.remove(stripIdPrefix(id));
            }
        }
    }

    @Nullable
    private static String getId(@NonNull IAttribute attribute) {
        if (attribute.getName().startsWith(ATTR_LAYOUT_PREFIX)
                && ANDROID_URI.equals(attribute.getUri())
                && !attribute.getName().startsWith(ATTR_LAYOUT_MARGIN)) {
            String id = attribute.getValue();
            // It might not be an id reference, so check manually rather than just
            // calling stripIdPrefix():
            if (id.startsWith(NEW_ID_PREFIX)) {
                return id.substring(NEW_ID_PREFIX.length());
            } else if (id.startsWith(ID_PREFIX)) {
                return id.substring(ID_PREFIX.length());
            }
        }

        return null;
    }

    /**
     * Updates the constraints in the layout to handle deletion of a set of
     * nodes. This ensures that any constraints pointing to one of the deleted
     * nodes are changed properly to point to a non-deleted node with similar
     * constraints.
     */
    public void updateConstraints() {
        if (mChildren.length == mDeleted.size()) {
            // Deleting everything: Nothing to be done
            return;
        }

        // Now remove incoming edges to any views that were deleted. If possible,
        // don't just delete them but replace them with a transitive constraint, e.g.
        // if we have "A <= B <= C" and "B" is removed, then we end up with "A <= C",

        for (INode child : mChildren) {
            if (mDeleted.contains(child)) {
                continue;
            }

            for (IAttribute attribute : child.getLiveAttributes()) {
                String id = getId(attribute);
                if (id != null) {
                    if (mDeletedIds.contains(id)) {
                        // Unset this reference to a deleted widget. It might be
                        // replaced if the pointed to node points to some other node
                        // on the same side, but it may use a different constraint name,
                        // or have none at all (e.g. parent).
                        String name = attribute.getName();
                        child.setAttribute(ANDROID_URI, name, null);

                        INode deleted = mNodeMap.get(id);
                        if (deleted != null) {
                            ConstraintType type = ConstraintType.fromAttribute(name);
                            if (type != null) {
                                transfer(deleted, child, type, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    private void transfer(INode deleted, INode target, ConstraintType targetType, int depth) {
        if (depth == 20) {
            // Prevent really deep flow or unbounded recursion in case there is a bug in
            // the cycle detection code
            return;
        }

        assert mDeleted.contains(deleted);

        for (IAttribute attribute : deleted.getLiveAttributes()) {
            String name = attribute.getName();
            ConstraintType type = ConstraintType.fromAttribute(name);
            if (type == null) {
                continue;
            }

            ConstraintType transfer = getCompatibleConstraint(type, targetType);
            if (transfer != null) {
                String id = getId(attribute);
                if (id != null) {
                    if (mDeletedIds.contains(id)) {
                        INode nextDeleted = mNodeMap.get(id);
                        if (nextDeleted != null) {
                            // Points to another deleted node: recurse
                            transfer(nextDeleted, target, targetType, depth + 1);
                        }
                    } else {
                        // Found an undeleted node destination: point to it directly.
                        // Note that we're using the
                        target.setAttribute(ANDROID_URI, transfer.name, attribute.getValue());
                    }
                } else {
                    // Pointing to parent or center etc (non-id ref): replicate this on the target
                    target.setAttribute(ANDROID_URI, name, attribute.getValue());
                }
            }
        }
    }

    /**
     * Determines if two constraints are in the same direction and if so returns
     * the constraint in the same direction. Rather than returning boolean true
     * or false, this returns the constraint which is sometimes modified. For
     * example, if you have a node which points left to a node which is centered
     * in parent, then the constraint is turned into center horizontal.
     */
    @Nullable
    private static ConstraintType getCompatibleConstraint(
            @NonNull ConstraintType first, @NonNull ConstraintType second) {
        if (first == second) {
            return first;
        }

        switch (second) {
            case ALIGN_LEFT:
            case LAYOUT_RIGHT_OF:
                switch (first) {
                    case LAYOUT_CENTER_HORIZONTAL:
                    case LAYOUT_LEFT_OF:
                    case ALIGN_LEFT:
                        return first;
                    case LAYOUT_CENTER_IN_PARENT:
                        return LAYOUT_CENTER_HORIZONTAL;
                }
                return null;

            case ALIGN_RIGHT:
            case LAYOUT_LEFT_OF:
                switch (first) {
                    case LAYOUT_CENTER_HORIZONTAL:
                    case ALIGN_RIGHT:
                    case LAYOUT_LEFT_OF:
                        return first;
                    case LAYOUT_CENTER_IN_PARENT:
                        return LAYOUT_CENTER_HORIZONTAL;
                }
                return null;

            case ALIGN_TOP:
            case LAYOUT_BELOW:
            case ALIGN_BASELINE:
                switch (first) {
                    case LAYOUT_CENTER_VERTICAL:
                    case ALIGN_TOP:
                    case LAYOUT_BELOW:
                    case ALIGN_BASELINE:
                        return first;
                    case LAYOUT_CENTER_IN_PARENT:
                        return LAYOUT_CENTER_VERTICAL;
                }
                return null;
            case ALIGN_BOTTOM:
            case LAYOUT_ABOVE:
                switch (first) {
                    case LAYOUT_CENTER_VERTICAL:
                    case ALIGN_BOTTOM:
                    case LAYOUT_ABOVE:
                    case ALIGN_BASELINE:
                        return first;
                    case LAYOUT_CENTER_IN_PARENT:
                        return LAYOUT_CENTER_VERTICAL;
                }
                return null;
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/GuidelineHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/GuidelineHandler.java
//Synthetic comment -- index 8ff159c..4ab11f1 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import static com.android.ide.common.api.SegmentType.RIGHT;
import static com.android.ide.common.api.SegmentType.TOP;
import static com.android.ide.common.layout.BaseLayoutRule.getMaxMatchDistance;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ABOVE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BASELINE;
//Synthetic comment -- @@ -51,6 +50,7 @@
import static com.android.ide.common.layout.LayoutConstants.VALUE_N_DP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.ide.common.layout.relative.ConstraintType.ALIGN_BASELINE;
import static com.android.util.XmlUtils.ANDROID_URI;
import static java.lang.Math.abs;

import com.android.ide.common.api.DropFeedback;
//Synthetic comment -- @@ -291,6 +291,11 @@
return false;
}

    /**
     * Checks for any cycles in the dependencies
     *
     * @param feedback the drop feedback state
     */
public void checkCycles(DropFeedback feedback) {
// Deliberate short circuit evaluation -- only list the first cycle
feedback.errorMessage = null;
//Synthetic comment -- @@ -657,6 +662,7 @@
}
}

    /** Breaks any cycles detected by the handler */
public void removeCycles() {
if (mHorizontalCycle != null) {
removeCycles(mHorizontalDeps);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/MoveHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/MoveHandler.java
//Synthetic comment -- index 3b73696..2386008 100644

//Synthetic comment -- @@ -23,8 +23,8 @@
import static com.android.ide.common.api.SegmentType.LEFT;
import static com.android.ide.common.api.SegmentType.RIGHT;
import static com.android.ide.common.api.SegmentType.TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.util.XmlUtils.ANDROID_URI;
import static java.lang.Math.abs;

import com.android.ide.common.api.DropFeedback;
//Synthetic comment -- @@ -48,8 +48,15 @@
* edges, and so on -- and picks the best among these.
*/
public class MoveHandler extends GuidelineHandler {
    private int mDraggedBaseline;

    /**
     * Creates a new {@link MoveHandler}.
     *
     * @param layout the layout element the handler is operating on
     * @param elements the elements being dragged in the move operation
     * @param rulesEngine the corresponding {@link IClientRulesEngine}
     */
public MoveHandler(INode layout, IDragElement[] elements, IClientRulesEngine rulesEngine) {
super(layout, rulesEngine);

//Synthetic comment -- @@ -158,6 +165,15 @@
}
}

    /**
     * Updates the handler for the given mouse move
     *
     * @param feedback the feedback handler
     * @param elements the elements being dragged
     * @param offsetX the new mouse X coordinate
     * @param offsetY the new mouse Y coordinate
     * @param modifierMask the keyboard modifiers pressed during the drag
     */
public void updateMove(DropFeedback feedback, IDragElement[] elements,
int offsetX, int offsetY, int modifierMask) {
mSnap = (modifierMask & DropFeedback.MODIFIER2) == 0;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/ResizeHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/ResizeHandler.java
//Synthetic comment -- index e4fff32..9383024 100644

//Synthetic comment -- @@ -23,8 +23,8 @@
import static com.android.ide.common.api.SegmentType.LEFT;
import static com.android.ide.common.api.SegmentType.RIGHT;
import static com.android.ide.common.api.SegmentType.TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.util.XmlUtils.ANDROID_URI;
import static java.lang.Math.abs;

import com.android.ide.common.api.DropFeedback;
//Synthetic comment -- @@ -43,10 +43,18 @@
* edges in a RelativeLayout.
*/
public class ResizeHandler extends GuidelineHandler {
    private final SegmentType mHorizontalEdgeType;
    private final SegmentType mVerticalEdgeType;

    /**
     * Creates a new {@link ResizeHandler}
     *
     * @param layout the layout containing the resized node
     * @param resized the node being resized
     * @param rulesEngine the applicable {@link IClientRulesEngine}
     * @param horizontalEdgeType the type of horizontal edge being resized, or null
     * @param verticalEdgeType the type of vertical edge being resized, or null
     */
public ResizeHandler(INode layout, INode resized,
IClientRulesEngine rulesEngine,
SegmentType horizontalEdgeType, SegmentType verticalEdgeType) {
//Synthetic comment -- @@ -57,7 +65,6 @@
assert horizontalEdgeType != CENTER_HORIZONTAL && verticalEdgeType != CENTER_HORIZONTAL;
assert horizontalEdgeType != CENTER_VERTICAL && verticalEdgeType != CENTER_VERTICAL;

mHorizontalEdgeType = horizontalEdgeType;
mVerticalEdgeType = verticalEdgeType;

//Synthetic comment -- @@ -161,6 +168,14 @@
return compatible;
}

    /**
     * Updates the handler for the given mouse resize
     *
     * @param feedback the feedback handler
     * @param child the node being resized
     * @param newBounds the new bounds of the resize rectangle
     * @param modifierMask the keyboard modifiers pressed during the drag
     */
public void updateResize(DropFeedback feedback, INode child, Rect newBounds,
int modifierMask) {
mSnap = (modifierMask & DropFeedback.MODIFIER2) == 0;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ClipboardSupport.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ClipboardSupport.java
//Synthetic comment -- index 343ee80..7e49bb7 100644

//Synthetic comment -- @@ -142,9 +142,7 @@
*/
public void cutSelectionToClipboard(List<SelectionItem> selection) {
copySelectionToClipboard(selection);
        deleteSelection(mCanvas.getCutLabel(), selection);
}

/**
//Synthetic comment -- @@ -170,9 +168,11 @@
for (SelectionItem cs : selection) {
CanvasViewInfo vi = cs.getViewInfo();
if (vi != null && vi.getParent() != null) {
                CanvasViewInfo parent = vi.getParent();
                assert parent != null;
if (title == null) {
                    title = parent.getName();
                } else if (!title.equals(parent.getName())) {
// More than one kind of parent selected.
title = null;
break;
//Synthetic comment -- @@ -210,6 +210,9 @@
new HashMap<NodeProxy, List<INode>>();
for (SelectionItem cs : selection) {
NodeProxy node = cs.getNode();
                    if (node == null) {
                        continue;
                    }
INode parent = node.getParent();
if (parent != null) {
List<INode> children = clusters.get(parent);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index c2a535e..520396d 100644

//Synthetic comment -- @@ -404,6 +404,7 @@
if (event.detail == DND.DROP_MOVE) {
GlobalCanvasDragInfo.getInstance().removeSource();
}
                    mTargetNode.applyPendingChanges();
}
});
} finally {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index a8438d8..7dc908b 100755

//Synthetic comment -- @@ -558,7 +558,8 @@
IViewRule parentRule = loadRule(parentUiNode);
if (parentRule != null) {
try {
                    parentRule.onRemovingChildren(children, parentNode,
                            mInsertType == InsertType.MOVE_WITHIN);
} catch (Exception e) {
AdtPlugin.log(e, "%s.onDispose() failed: %s",
parentRule.getClass().getSimpleName(),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java
//Synthetic comment -- index b9176f6..372e329 100644

//Synthetic comment -- @@ -36,8 +36,10 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.google.common.base.Splitter;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.IOException;
import java.io.StringWriter;
//Synthetic comment -- @@ -336,8 +338,24 @@
}

@Override
        public @NonNull IAttribute[] getLiveAttributes() {
            List<IAttribute> result = new ArrayList<IAttribute>();

            NamedNodeMap attributes = mElement.getAttributes();
            for (int i = 0, n = attributes.getLength(); i < n; i++) {
                Attr attribute = (Attr) attributes.item(i);
                result.add(new TestXmlAttribute(attribute));
            }
            return result.toArray(new IAttribute[result.size()]);
        }

        @Override
public boolean setAttribute(String uri, String localName, String value) {
            if (value == null) {
                mElement.removeAttributeNS(uri, localName);
            } else {
                mElement.setAttributeNS(uri, localName, value);
            }
return super.setAttribute(uri, localName, value);
}

//Synthetic comment -- @@ -395,6 +413,33 @@
}
}

    public static class TestXmlAttribute implements IAttribute {
        private Attr mAttribute;

        public TestXmlAttribute(Attr attribute) {
            this.mAttribute = attribute;
        }

        @Override
        public String getUri() {
            return mAttribute.getNamespaceURI();
        }

        @Override
        public String getName() {
            String name = mAttribute.getLocalName();
            if (name == null) {
                name = mAttribute.getName();
            }
            return name;
        }

        @Override
        public String getValue() {
            return mAttribute.getValue();
        }
    }

// Recursively initialize this node with the bounds specified in the given hierarchy
// dump (from ViewHierarchy's DUMP_INFO flag
public void assignBounds(String bounds) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/relative/DeletionHandlerTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/relative/DeletionHandlerTest.java
new file mode 100644
//Synthetic comment -- index 0000000..f5ec1ba

//Synthetic comment -- @@ -0,0 +1,445 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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
package com.android.ide.common.layout.relative;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;

import com.android.ide.common.api.INode;
import com.android.ide.common.layout.BaseViewRule;
import com.android.ide.common.layout.TestNode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class DeletionHandlerTest extends TestCase {
    public void testSimple() {
        String xml = "" +
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\"\n" +
            "    tools:ignore=\"HardcodedText\" >\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button1\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_alignParentLeft=\"true\"\n" +
            "        android:text=\"A\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button2\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_alignBaseline=\"@+id/button1\"\n" +
            "        android:layout_alignBottom=\"@+id/button1\"\n" +
            "        android:layout_toRightOf=\"@+id/button1\"\n" +
            "        android:text=\"B\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button3\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_alignBottom=\"@+id/button2\"\n" +
            "        android:layout_toRightOf=\"@+id/button2\"\n" +
            "        android:text=\"C\" />\n" +
            "\n" +
            "</RelativeLayout>";
        TestNode targetNode = TestNode.createFromXml(xml);
        assertNotNull(targetNode);

        TestNode button2 = TestNode.findById(targetNode, "@+id/button2");

        INode layout = button2.getParent();
        List<INode> deletedNodes = Collections.<INode>singletonList(button2);
        List<INode> movedNodes = Collections.<INode>emptyList();
        assertSame(layout, targetNode);
        layout.removeChild(button2);

        DeletionHandler handler = new DeletionHandler(deletedNodes, movedNodes, layout);
        handler.updateConstraints();

        String updated = TestNode.toXml(targetNode);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"match_parent\"\n" +
                "    tools:ignore=\"HardcodedText\">\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button1\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        android:layout_alignParentLeft=\"true\"\n" +
                "        android:text=\"A\">\n" +
                "    </Button>\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button3\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        android:layout_alignBaseline=\"@+id/button1\"\n" +
                "        android:layout_alignBottom=\"@+id/button1\"\n" +
                "        android:layout_toRightOf=\"@+id/button1\"\n" +
                "        android:text=\"C\">\n" +
                "    </Button>\n" +
                "\n" +
                "</RelativeLayout>",
                updated);
        assertFalse(updated.contains(BaseViewRule.stripIdPrefix(button2.getStringAttr(ANDROID_URI,
                ATTR_ID))));
    }

    public void testTransitive() {
        String xml = "" +
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\"\n" +
            "    tools:ignore=\"HardcodedText\" >\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button1\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_alignParentLeft=\"true\"\n" +
            "        android:layout_alignParentTop=\"true\"\n" +
            "        android:text=\"Above\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button2\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_alignParentLeft=\"true\"\n" +
            "        android:layout_below=\"@+id/button1\"\n" +
            "        android:text=\"A\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button3\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_alignBaseline=\"@+id/button2\"\n" +
            "        android:layout_alignBottom=\"@+id/button2\"\n" +
            "        android:layout_toRightOf=\"@+id/button2\"\n" +
            "        android:text=\"B\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button4\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_alignBottom=\"@+id/button3\"\n" +
            "        android:layout_toRightOf=\"@+id/button3\"\n" +
            "        android:text=\"C\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button5\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_alignBaseline=\"@+id/button4\"\n" +
            "        android:layout_alignBottom=\"@+id/button4\"\n" +
            "        android:layout_toRightOf=\"@+id/button4\"\n" +
            "        android:text=\"D\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button6\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_alignBottom=\"@+id/button5\"\n" +
            "        android:layout_toRightOf=\"@+id/button5\"\n" +
            "        android:text=\"E\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button7\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_alignLeft=\"@+id/button3\"\n" +
            "        android:layout_below=\"@+id/button3\"\n" +
            "        android:text=\"Button\" />\n" +
            "\n" +
            "    <CheckBox\n" +
            "        android:id=\"@+id/checkBox1\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_alignBaseline=\"@+id/button7\"\n" +
            "        android:layout_alignBottom=\"@+id/button7\"\n" +
            "        android:layout_toRightOf=\"@+id/button7\"\n" +
            "        android:text=\"CheckBox\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button8\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_below=\"@+id/checkBox1\"\n" +
            "        android:layout_toRightOf=\"@+id/checkBox1\"\n" +
            "        android:text=\"Button\" />\n" +
            "\n" +
            "</RelativeLayout>";
        TestNode targetNode = TestNode.createFromXml(xml);
        assertNotNull(targetNode);
        TestNode button7 = TestNode.findById(targetNode, "@+id/button7");
        TestNode checkBox = TestNode.findById(targetNode, "@+id/checkBox1");

        INode layout = button7.getParent();
        List<INode> deletedNodes = Arrays.<INode>asList(button7, checkBox);
        List<INode> movedNodes = Collections.<INode>emptyList();
        assertSame(layout, targetNode);
        layout.removeChild(button7);
        layout.removeChild(checkBox);

        DeletionHandler handler = new DeletionHandler(deletedNodes, movedNodes, layout);
        handler.updateConstraints();

        String updated = TestNode.toXml(targetNode);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"match_parent\"\n" +
                "    tools:ignore=\"HardcodedText\">\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button1\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        android:layout_alignParentLeft=\"true\"\n" +
                "        android:layout_alignParentTop=\"true\"\n" +
                "        android:text=\"Above\">\n" +
                "    </Button>\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button2\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        android:layout_alignParentLeft=\"true\"\n" +
                "        android:layout_below=\"@+id/button1\"\n" +
                "        android:text=\"A\">\n" +
                "    </Button>\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button3\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        android:layout_alignBaseline=\"@+id/button2\"\n" +
                "        android:layout_alignBottom=\"@+id/button2\"\n" +
                "        android:layout_toRightOf=\"@+id/button2\"\n" +
                "        android:text=\"B\">\n" +
                "    </Button>\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button4\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        android:layout_alignBottom=\"@+id/button3\"\n" +
                "        android:layout_toRightOf=\"@+id/button3\"\n" +
                "        android:text=\"C\">\n" +
                "    </Button>\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button5\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        android:layout_alignBaseline=\"@+id/button4\"\n" +
                "        android:layout_alignBottom=\"@+id/button4\"\n" +
                "        android:layout_toRightOf=\"@+id/button4\"\n" +
                "        android:text=\"D\">\n" +
                "    </Button>\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button6\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        android:layout_alignBottom=\"@+id/button5\"\n" +
                "        android:layout_toRightOf=\"@+id/button5\"\n" +
                "        android:text=\"E\">\n" +
                "    </Button>\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button8\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        android:layout_alignLeft=\"@+id/button3\"\n" +
                "        android:layout_below=\"@+id/button3\"\n" +
                "        android:text=\"Button\">\n" +
                "    </Button>\n" +
                "\n" +
                "</RelativeLayout>",
                updated);
        assertFalse(updated.contains(BaseViewRule.stripIdPrefix(button7.getStringAttr(ANDROID_URI,
                ATTR_ID))));
    }

    public void testCenter() {
        String xml =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\"\n" +
            "    tools:ignore=\"HardcodedText\" >\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button1\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_centerInParent=\"true\"\n" +
            "        android:text=\"Button\" />\n" +
            "\n" +
            "    <CheckBox\n" +
            "        android:id=\"@+id/checkBox1\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_below=\"@+id/button1\"\n" +
            "        android:layout_toRightOf=\"@+id/button1\"\n" +
            "        android:text=\"CheckBox\" />\n" +
            "\n" +
            "</RelativeLayout>";

        TestNode targetNode = TestNode.createFromXml(xml);
        assertNotNull(targetNode);
        TestNode button1 = TestNode.findById(targetNode, "@+id/button1");

        INode layout = button1.getParent();
        List<INode> deletedNodes = Collections.<INode>singletonList(button1);
        List<INode> movedNodes = Collections.<INode>emptyList();
        assertSame(layout, targetNode);
        layout.removeChild(button1);

        DeletionHandler handler = new DeletionHandler(deletedNodes, movedNodes, layout);
        handler.updateConstraints();

        String updated = TestNode.toXml(targetNode);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"match_parent\"\n" +
                "    tools:ignore=\"HardcodedText\">\n" +
                "\n" +
                "    <CheckBox\n" +
                "        android:id=\"@+id/checkBox1\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        android:layout_centerInParent=\"true\"\n" +
                "        android:text=\"CheckBox\">\n" +
                "    </CheckBox>\n" +
                "\n" +
                "</RelativeLayout>",
                updated);
        assertFalse(updated.contains(BaseViewRule.stripIdPrefix(button1.getStringAttr(ANDROID_URI,
                ATTR_ID))));

    }

    public void testMove() {
        String xml = "" +
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\"\n" +
            "    tools:ignore=\"HardcodedText\" >\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button1\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_alignParentLeft=\"true\"\n" +
            "        android:text=\"A\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button2\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_alignBaseline=\"@+id/button1\"\n" +
            "        android:layout_alignBottom=\"@+id/button1\"\n" +
            "        android:layout_toRightOf=\"@+id/button1\"\n" +
            "        android:text=\"B\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button3\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_alignBottom=\"@+id/button2\"\n" +
            "        android:layout_toRightOf=\"@+id/button2\"\n" +
            "        android:text=\"C\" />\n" +
            "\n" +
            "</RelativeLayout>";
        TestNode targetNode = TestNode.createFromXml(xml);
        assertNotNull(targetNode);

        TestNode button2 = TestNode.findById(targetNode, "@+id/button2");

        INode layout = button2.getParent();
        List<INode> deletedNodes = Collections.<INode>singletonList(button2);
        List<INode> movedNodes = Collections.<INode>singletonList(button2);
        assertSame(layout, targetNode);

        DeletionHandler handler = new DeletionHandler(deletedNodes, movedNodes, layout);
        handler.updateConstraints();

        String updated = TestNode.toXml(targetNode);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"match_parent\"\n" +
                "    tools:ignore=\"HardcodedText\">\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button1\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        android:layout_alignParentLeft=\"true\"\n" +
                "        android:text=\"A\">\n" +
                "    </Button>\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button2\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        android:layout_alignBaseline=\"@+id/button1\"\n" +
                "        android:layout_alignBottom=\"@+id/button1\"\n" +
                "        android:layout_toRightOf=\"@+id/button1\"\n" +
                "        android:text=\"B\">\n" +
                "    </Button>\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button3\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        android:layout_alignBottom=\"@+id/button2\"\n" +
                "        android:layout_toRightOf=\"@+id/button2\"\n" +
                "        android:text=\"C\">\n" +
                "    </Button>\n" +
                "\n" +
                "</RelativeLayout>",
                updated);
        assertTrue(updated.contains(BaseViewRule.stripIdPrefix(button2.getStringAttr(ANDROID_URI,
                ATTR_ID))));
    }
}








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/AbstractViewRule.java b/rule_api/src/com/android/ide/common/api/AbstractViewRule.java
//Synthetic comment -- index 7f05809..068580f 100644

//Synthetic comment -- @@ -47,8 +47,6 @@
return null;
}

@Override
@Nullable
public List<String> getSelectionHint(@NonNull INode parentNode, @NonNull INode childNode) {
//Synthetic comment -- @@ -69,12 +67,10 @@
@NonNull List<? extends INode> childNodes, @Nullable Object view) {
}

@Override
@Nullable
    public DropFeedback onDropEnter(@NonNull INode targetNode, @Nullable Object targetView,
            @Nullable IDragElement[] elements) {
return null;
}

//Synthetic comment -- @@ -86,7 +82,8 @@
}

@Override
    public void onDropLeave(@NonNull INode targetNode, @NonNull IDragElement[] elements,
            @Nullable DropFeedback feedback) {
// ignore
}

//Synthetic comment -- @@ -101,39 +98,42 @@


@Override
    public void onPaste(@NonNull INode targetNode, @Nullable Object targetView,
            @NonNull IDragElement[] pastedElements) {
}

@Override
    public void onCreate(@NonNull INode node, @NonNull INode parent,
            @NonNull InsertType insertType) {
}

@Override
    public void onChildInserted(@NonNull INode child, @NonNull INode parent,
            @NonNull InsertType insertType) {
}

    @Override
    public void onRemovingChildren(@NonNull List<INode> deleted, @NonNull INode parent,
            boolean moved) {
    }

@Override
@Nullable
    public DropFeedback onResizeBegin(@NonNull INode child, @NonNull INode parent,
            @Nullable SegmentType horizontalEdge,
            @Nullable SegmentType verticalEdge, @Nullable Object childView,
            @Nullable Object parentView) {
return null;
}

@Override
    public void onResizeUpdate(@Nullable DropFeedback feedback, @NonNull INode child,
            @NonNull INode parent, @NonNull Rect newBounds,
int modifierMask) {
}

@Override
    public void onResizeEnd(@Nullable DropFeedback feedback, @NonNull INode child,
            @NonNull INode parent, @NonNull Rect newBounds) {
}
}








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/IViewRule.java b/rule_api/src/com/android/ide/common/api/IViewRule.java
//Synthetic comment -- index c115795..b16df26 100644

//Synthetic comment -- @@ -298,19 +298,32 @@
@NonNull InsertType insertType);

/**
     * Called when one or more children are about to be deleted by the user.
     * Note that children deleted programmatically from view rules (via
* {@link INode#removeChild(INode)}) will not notify about deletion.
* <p>
     * Note that this method will be called under an edit lock, so rules can
     * directly add/remove nodes and attributes as part of the deletion handling
     * (and their actions will be part of the same undo-unit.)
     * <p>
     * Note that when children are moved (such as when you drag a child within a
     * LinearLayout to move it from one position among the children to another),
     * that will also result in a
     * {@link #onChildInserted(INode, INode, InsertType)} (with the
     * {@code InsertType} set to {@link InsertType#MOVE_WITHIN}) and a remove
     * via this {@link #onRemovingChildren(List, INode, boolean)} method. When
     * the deletion is occurring as part of a local move (insert + delete), the
     * {@code moved} parameter to this method is set to true.
*
* @param deleted a nonempty list of children about to be deleted
     * @param parent the parent of the deleted children (which still contains
     *            the children since this method is called before the deletion
     *            is performed)
     * @param moved when true, the nodes are being deleted as part of a local
     *            move (where copies are inserted elsewhere)
*/
    void onRemovingChildren(@NonNull List<INode> deleted, @NonNull INode parent,
            boolean moved);

/**
* Called by the IDE on the parent layout when a child widget is being resized. This







