/*Cluster of improvements for merge tag views

This changeset contains various improvements around usage of the
<merge> tag. Some of these fixes require layoutlib 5.

* Use the new layoutlib support for rendering multiple children at the
  root level - they now show up in the Outline (provided you are
  running layoutlib 5), can be selected in the layout editor, etc.

* Add a drop handler such that you can drag into the <merge> view and
  get drop feedback (similar to the FrameLayout)

* If the <merge> is empty, we don't get any ViewInfos, so in that case
  manufacture a dummy view sized to the screen. Similarly, if we get
  back ViewInfos that are children of a <merge> tag in the UI model,
  create a <merge> view initialized to the bounding rectangle of these
  views and reparent the views to it.

* Support highlighting multiple views simultaneously when you select
  an include tag that renders into multiple views (because the root of
  the included layout was a <merge> tag).  Similarly, make "Show
  Included In" work properly for <merge> views, and make the overlay
  mask used to hide all included content also reveal only the primary
  selected views (when a view is included more than once.) (Also tweak
  the visual appearance of the mask, and use better icon for the view
  root in the included-root scenario.)

* Improve the algorithm which deals with render results with null
  keys. Use adjacent children that -do- have keys as constraints when
  attempting to match up views without keys and unreferenced model
  nodes. This fixes issuehttp://code.google.com/p/android/issues/detail?id=14188* Improve the way we pick views under the mouse. This used to search
  down the view hierarchy in sibling order. Instead, search in reverse
  sibling order since this will match what is drawn in the layout. For
  views like FrameLayout and <merge> views, the children are painted
  on top of ech other, so clicking on whatever is on top should choose
  that view, not some earlier sibling below it.

* Fix such that when you drag into the canvas, we *always* target the
  root node, even if it is not under the mouse. This is particularly
  important with <merge> tags, but this also helps if you for example
  have a LinearLayout as the root element, and the layout_height
  property is wrap_content instead of match_parent. In that case, the
  LinearLayout will *only* cover its children, so if you drag over the
  visual screen, it looks like you should be able to drop into the
  layout, but you cannot since it only covers its children. With this
  fix, all positions outside the root element's actual bounds are also
  considered targetting the root.

* Fix broken unit test, add new unit tests.

Change-Id:Id96a06a8763d02845af4531a47fe32afe703df2f*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/MergeRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/MergeRule.java
new file mode 100644
//Synthetic comment -- index 0000000..77f5c22

//Synthetic comment -- @@ -0,0 +1,37 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.ide.common.layout;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.MenuAction;

import java.util.List;

/**
 * Drop handler for the {@code <merge>} tag
 */
public class MergeRule extends FrameLayoutRule {
    // The <merge> tag behaves a lot like the FrameLayout; all children are added
    // on top of each other at (0,0)

    @Override
    public List<MenuAction> getContextMenu(INode selectedNode) {
        // Deliberately ignore super.getContextMenu(); we don't want to attempt to list
        // properties for the <merge> tag
        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 84647bd..0585f4f 100755

//Synthetic comment -- @@ -16,8 +16,11 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_MERGE;

import com.android.ide.common.api.Rect;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.MergeCookie;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.UiElementPullParser;
//Synthetic comment -- @@ -25,6 +28,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.util.Pair;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
//Synthetic comment -- @@ -34,8 +38,11 @@
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
* Maps a {@link ViewInfo} in a structure more adapted to our needs.
//Synthetic comment -- @@ -66,7 +73,7 @@
private final String mName;
private final Object mViewObject;
private final UiViewElementNode mUiViewNode;
    private CanvasViewInfo mParent;
private final ArrayList<CanvasViewInfo> mChildren = new ArrayList<CanvasViewInfo>();

/**
//Synthetic comment -- @@ -77,6 +84,17 @@
private boolean mExploded;

/**
     * Node sibling. This is usually null, but it's possible for a single node in the
     * model to have <b>multiple</b> separate views in the canvas, for example
     * when you {@code <include>} a view that has multiple widgets inside a
     * {@code <merge>} tag. In this case, all the views have the same node model,
     * the include tag, and selecting the include should highlight all the separate
     * views that are linked to this node. That's what this field is all about: it is
     * a <b>circular</b> list of all the siblings that share the same node.
     */
    private List<CanvasViewInfo> mNodeSiblings;

    /**
* Constructs a {@link CanvasViewInfo} initialized with the given initial values.
*/
private CanvasViewInfo(CanvasViewInfo parent, String name,
//Synthetic comment -- @@ -142,6 +160,77 @@
}

/**
     * For nodes that have multiple views rendered from a single node, such as the
     * children of a {@code <merge>} tag included into a separate layout, return the
     * "primary" view, the first view that is rendered
     */
    private CanvasViewInfo getPrimaryNodeSibling() {
        if (mNodeSiblings == null || mNodeSiblings.size() == 0) {
            return null;
        }

        return mNodeSiblings.get(0);
    }

    /**
     * Returns true if this view represents one view of many linked to a single node, and
     * where this is the primary view. The primary view is the one that will be shown
     * in the outline for example (since we only show nodes, not views, in the outline,
     * and therefore don't want repetitions when a view has more than one view info.)
     *
     * @return true if this is the primary view among more than one linked to a single
     *         node
     */
    private boolean isPrimaryNodeSibling() {
        return getPrimaryNodeSibling() == this;
    }

    /**
     * Returns the list of node sibling of this view (which <b>will include this
     * view</b>). For most views this is going to be null, but for views that share a
     * single node (such as widgets inside a {@code <merge>} tag included into another
     * layout), this will provide all the views that correspond to the node.
     *
     * @return a non-empty list of siblings (including this), or null
     */
    public List<CanvasViewInfo> getNodeSiblings() {
        return mNodeSiblings;
    }

    /**
     * Returns all the children of the canvas view info where each child corresponds to a
     * unique node. This is intended for use by the outline for example, where only the
     * actual nodes are displayed, not the views themselves.
     * <p>
     * Most views have their own nodes, so this is generally the same as
     * {@link #getChildren}, except in the case where you for example include a view that
     * has multiple widgets inside a {@code <merge>} tag, where all these widgets have the
     * same node (the {@code <merge>} tag).
     *
     * @return list of {@link CanvasViewInfo} objects that are children of this view,
     *         never null
     */
    public List<CanvasViewInfo> getUniqueChildren() {
        for (CanvasViewInfo info : mChildren) {
            if (info.mNodeSiblings != null) {
                // We have secondary children; must create a new collection containing
                // only non-secondary children
                List<CanvasViewInfo> children = new ArrayList<CanvasViewInfo>();
                for (CanvasViewInfo vi : mChildren) {
                    if (vi.mNodeSiblings == null) {
                        children.add(vi);
                    } else if (vi.isPrimaryNodeSibling()) {
                        children.add(vi);
                    }
                }
                return children;
            }
        }

        return mChildren;
    }

    /**
* Returns true if the specific {@link CanvasViewInfo} is a parent
* of this {@link CanvasViewInfo}. It can be a direct parent or any
* grand-parent higher in the hierarchy.
//Synthetic comment -- @@ -376,6 +465,32 @@
return null;
}

    /** Adds the given {@link CanvasViewInfo} as a new last child of this view */
    private void addChild(CanvasViewInfo child) {
        mChildren.add(child);
    }

    /** Adds the given {@link CanvasViewInfo} as a child at the given index */
    private void addChildAt(int index, CanvasViewInfo child) {
        mChildren.add(index, child);
    }

    /**
     * Removes the given {@link CanvasViewInfo} from the child list of this view, and
     * returns true if it was successfully removed
     *
     * @param child the child to be removed
     * @return true if it was a child and was removed
     */
    public boolean removeChild(CanvasViewInfo child) {
        return mChildren.remove(child);
    }

    @Override
    public String toString() {
        return "CanvasViewInfo [name=" + mName + ", node=" + mUiViewNode + "]";
    }

// ---- Factory functionality ----

/**
//Synthetic comment -- @@ -402,234 +517,488 @@
* @param root the root {@link ViewInfo} to build from
* @return a {@link CanvasViewInfo} hierarchy
*/
    public static Pair<CanvasViewInfo,List<Rectangle>> create(ViewInfo root) {
        return new Builder().create(root);
}

    /** Builder object which walks over a tree of {@link ViewInfo} objects and builds
     * up a corresponding {@link CanvasViewInfo} hierarchy. */
    private static class Builder {
        private Map<UiViewElementNode,List<CanvasViewInfo>> mMergeNodeMap;

        public Pair<CanvasViewInfo,List<Rectangle>> create(ViewInfo root) {
            Object cookie = root.getCookie();
            if (cookie == null) {
                // Special case: If the root-most view does not have a view cookie,
                // then we are rendering some outer layout surrounding this layout, and in
                // that case we must search down the hierarchy for the (possibly multiple)
                // sub-roots that correspond to elements in this layout, and place them inside
                // an outer view that has no node. In the outline this item will be used to
                // show the inclusion-context.
                CanvasViewInfo rootView = createView(null, root, 0, 0);
                addKeyedSubtrees(rootView, root, 0, 0);

                List<Rectangle> includedBounds = new ArrayList<Rectangle>();
                for (CanvasViewInfo vi : rootView.getChildren()) {
                    if (vi.isPrimaryNodeSibling()) {
                        includedBounds.add(vi.getAbsRect());
}
}

                // There are <merge> nodes here; see if we can insert it into the hierarchy
                if (mMergeNodeMap != null) {
                    // Locate all the nodes that have a <merge> as a parent in the node model,
                    // and where the view sits at the top level inside the include-context node.
                    UiViewElementNode merge = null;
                    List<CanvasViewInfo> merged = new ArrayList<CanvasViewInfo>();
                    for (Map.Entry<UiViewElementNode, List<CanvasViewInfo>> entry : mMergeNodeMap
                            .entrySet()) {
                        UiViewElementNode node = entry.getKey();
                        if (!hasMergeParent(node)) {
                            continue;
                        }
                        List<CanvasViewInfo> views = entry.getValue();
                        assert views.size() > 0;
                        CanvasViewInfo view = views.get(0); // primary
                        if (view.getParent() != rootView) {
                            continue;
                        }
                        UiElementNode parent = node.getUiParent();
                        if (merge != null && parent != merge) {
                            continue;
                        }
                        merge = (UiViewElementNode) parent;
                        merged.add(view);
}
                    if (merged.size() > 0) {
                        // Compute a bounding box for the merged views
                        Rectangle absRect = null;
                        for (CanvasViewInfo child : merged) {
                            Rectangle rect = child.getAbsRect();
                            if (absRect == null) {
                                absRect = rect;
} else {
                                absRect = absRect.union(rect);
                            }
                        }

                        CanvasViewInfo mergeView = new CanvasViewInfo(rootView, VIEW_MERGE, null,
                                merge, absRect, absRect);
                        for (CanvasViewInfo view : merged) {
                            if (rootView.removeChild(view)) {
                                mergeView.addChild(view);
                            }
                        }
                        rootView.addChild(mergeView);
                    }
                }

                return Pair.of(rootView, includedBounds);
            } else {
                // We have a view key at the top, so just go and create {@link CanvasViewInfo}
                // objects for each {@link ViewInfo} until we run into a null key.
                CanvasViewInfo rootView = addKeyedSubtrees(null, root, 0, 0);

                // Special case: look to see if the root element is really a <merge>, and if so,
                // manufacture a view for it such that we can target this root element
                // in drag & drop operations, such that we can show it in the outline, etc
                if (rootView != null && hasMergeParent(rootView.getUiViewNode())) {
                    CanvasViewInfo merge = new CanvasViewInfo(null, VIEW_MERGE, null,
                            (UiViewElementNode) rootView.getUiViewNode().getUiParent(),
                            rootView.getAbsRect(), rootView.getSelectionRect());
                    // Insert the <merge> as the new real root
                    rootView.mParent = merge;
                    merge.addChild(rootView);
                    rootView = merge;
                }

                return Pair.of(rootView, null);
            }
        }

        private boolean hasMergeParent(UiViewElementNode rootNode) {
            UiElementNode rootParent = rootNode.getUiParent();
            return (rootParent instanceof UiViewElementNode
                    && VIEW_MERGE.equals(rootParent.getDescriptor().getXmlName()));
        }

        /** Creates a {@link CanvasViewInfo} for a given {@link ViewInfo} but does not recurse */
        private CanvasViewInfo createView(CanvasViewInfo parent, ViewInfo root, int parentX,
                int parentY) {
            Object cookie = root.getCookie();
            UiViewElementNode node = null;
            if (cookie instanceof UiViewElementNode) {
                node = (UiViewElementNode) cookie;
            } else if (cookie instanceof MergeCookie) {
                cookie = ((MergeCookie) cookie).getCookie();
                if (cookie instanceof UiViewElementNode) {
                    node = (UiViewElementNode) cookie;
                    CanvasViewInfo view = createView(parent, root, parentX, parentY, node);
                    if (root.getCookie() instanceof MergeCookie && view.mNodeSiblings == null) {
                        List<CanvasViewInfo> v = mMergeNodeMap == null ?
                                null : mMergeNodeMap.get(node);
                        if (v != null) {
                            v.add(view);
                        } else {
                            v = new ArrayList<CanvasViewInfo>();
                            v.add(view);
                            if (mMergeNodeMap == null) {
                                mMergeNodeMap =
                                    new HashMap<UiViewElementNode, List<CanvasViewInfo>>();
                            }
                            mMergeNodeMap.put(node, v);
                        }
                        view.mNodeSiblings = v;
                    }

                    return view;
                }
            }

            return createView(parent, root, parentX, parentY, node);
        }

        /**
         * Creates a {@link CanvasViewInfo} for a given {@link ViewInfo} but does not recurse.
         * This method specifies an explicit {@link UiViewElementNode} to use rather than
         * relying on the view cookie in the info object.
         */
        private CanvasViewInfo createView(CanvasViewInfo parent, ViewInfo root, int parentX,
                int parentY, UiViewElementNode node) {

            int x = root.getLeft();
            int y = root.getTop();
            int w = root.getRight() - x;
            int h = root.getBottom() - y;

            x += parentX;
            y += parentY;

            Rectangle absRect = new Rectangle(x, y, w - 1, h - 1);

            if (w < SELECTION_MIN_SIZE) {
                int d = (SELECTION_MIN_SIZE - w) / 2;
                x -= d;
                w += SELECTION_MIN_SIZE - w;
            }

            if (h < SELECTION_MIN_SIZE) {
                int d = (SELECTION_MIN_SIZE - h) / 2;
                y -= d;
                h += SELECTION_MIN_SIZE - h;
            }

            Rectangle selectionRect = new Rectangle(x, y, w - 1, h - 1);

            return new CanvasViewInfo(parent, root.getClassName(), root.getViewObject(), node,
                    absRect, selectionRect);
        }

        /** Create a subtree recursively until you run out of keys */
        private CanvasViewInfo createSubtree(CanvasViewInfo parent, ViewInfo viewInfo,
                int parentX, int parentY) {
            assert viewInfo.getCookie() != null;

            CanvasViewInfo view = createView(parent, viewInfo, parentX, parentY);

            // Process children:
            parentX += viewInfo.getLeft();
            parentY += viewInfo.getTop();

            // See if we have any missing keys at this level
            int missingNodes = 0;
            int mergeNodes = 0;
            List<ViewInfo> children = viewInfo.getChildren();
            for (ViewInfo child : children) {
                // Only use children which have a ViewKey of the correct type.
                // We can't interact with those when they have a null key or
                // an incompatible type.
                Object cookie = child.getCookie();
                if (!(cookie instanceof UiViewElementNode)) {
                    if (cookie instanceof MergeCookie) {
                        mergeNodes++;
                    } else {
                        missingNodes++;
                    }
                }
            }

            if (missingNodes == 0 && mergeNodes == 0) {
                // No missing nodes; this is the normal case, and we can just continue to
                // recursively add our children
                for (ViewInfo child : children) {
                    CanvasViewInfo childView = createSubtree(view, child,
                            parentX, parentY);
                    view.addChild(childView);
                }

                // TBD: Emit placeholder views for keys that have no views?
            } else {
                // We don't have keys for one or more of the ViewInfos. There are many
                // possible causes: we are on an SDK platform that does not support
                // embedded_layout rendering, or we are including a view with a <merge>
                // as the root element.

                String containerName = view.getUiViewNode().getDescriptor().getXmlLocalName();
                if (containerName.equals(LayoutDescriptors.VIEW_INCLUDE)) {
                    // This is expected -- we don't WANT to get node keys for the content
                    // of an include since it's in a different file and should be treated
                    // as a single unit that cannot be edited (hence, no CanvasViewInfo
                    // children)
                } else {
                    // We are getting children with null keys where we don't expect it;
                    // this usually means that we are dealing with an Android platform
                    // that does not support {@link Capability#EMBEDDED_LAYOUT}, or
                    // that there are <merge> tags which are doing surprising things
                    // to the view hierarchy
                    LinkedList<UiViewElementNode> unused = new LinkedList<UiViewElementNode>();
                    for (UiElementNode child : view.getUiViewNode().getUiChildren()) {
                        if (child instanceof UiViewElementNode) {
                            unused.addLast((UiViewElementNode) child);
                        }
                    }
                    for (ViewInfo child : children) {
                        Object cookie = child.getCookie();
                        if (mergeNodes > 0 && cookie instanceof MergeCookie) {
                            cookie = ((MergeCookie) cookie).getCookie();
                        }
                        if (cookie != null) {
                            unused.remove(cookie);
                        }
                    }

                    if (unused.size() > 0 || mergeNodes > 0) {
                        if (unused.size() == missingNodes) {
                            // The number of unmatched elements and ViewInfos are identical;
                            // it's very likely that they match one to one, so just use these
                            for (ViewInfo child : children) {
                                if (child.getCookie() == null) {
                                    // Only create a flat (non-recursive) view
                                    CanvasViewInfo childView = createView(view, child, parentX,
                                            parentY, unused.removeFirst());
                                    view.addChild(childView);
                                } else {
                                    CanvasViewInfo childView = createSubtree(view, child, parentX,
                                            parentY);
                                    view.addChild(childView);
                                }
                            }
                        } else {
                            // We have an uneven match. In this case we might be dealing
                            // with <merge> etc.
                            // We have no way to associate elements back with the
                            // corresponding <include> tags if there are more than one of
                            // them. That's not a huge tragedy since visually you are not
                            // allowed to edit these anyway; we just need to make a visual
                            // block for these for selection and outline purposes.
                            addMismatched(view, parentX, parentY, children, unused);
                        }
                    } else {
                        // No unused keys, but there are views without keys.
                        // We can't represent these since all views must have node keys
                        // such that you can operate on them. Just ignore these.
                        for (ViewInfo child : children) {
                            if (child.getCookie() != null) {
                                CanvasViewInfo childView = createSubtree(view, child,
                                        parentX, parentY);
view.addChild(childView);
}
}
}
}
}

            return view;
}

        /**
         * We have various {@link ViewInfo} children with null keys, and/or nodes in
         * the corresponding UI model that are not referenced by any of the {@link ViewInfo}
         * objects. This method attempts to account for this, by matching the views in
         * the right order.
         */
        private void addMismatched(CanvasViewInfo parentView, int parentX, int parentY,
                List<ViewInfo> children, LinkedList<UiViewElementNode> unused) {
            UiViewElementNode afterNode = null;
            UiViewElementNode beforeNode = null;
            // We have one important clue we can use when matching unused nodes
            // with views: if we have a view V1 with node N1, and a view V2 with node N2,
            // then we can only match unknown node UN with unknown node UV if
            // V1 < UV < V2 and N1 < UN < N2.
            // We can use these constraints to do the matching, for example by
            // a simple DAG traversal. However, since the number of unmatched nodes
            // will typically be very small, we'll just do a simple algorithm here
            // which checks forwards/backwards whether a match is valid.
            for (int index = 0, size = children.size(); index < size; index++) {
                ViewInfo child = children.get(index);
                if (child.getCookie() != null) {
                    CanvasViewInfo childView = createSubtree(parentView, child, parentX, parentY);
                    parentView.addChild(childView);
                    if (child.getCookie() instanceof UiViewElementNode) {
                        afterNode = (UiViewElementNode) child.getCookie();
                    }
} else {
                    beforeNode = nextViewNode(children, index);

                    // Find first eligible node from unused
                    // TOD: What if there are more eligible? We need to process ALL views
                    // and all nodes in one go here

                    UiViewElementNode matching = null;
                    for (UiViewElementNode candidate : unused) {
                        if (afterNode == null || isAfter(afterNode, candidate)) {
                            if (beforeNode == null || isBefore(beforeNode, candidate)) {
                                matching = candidate;
                                break;
                            }
                        }
                    }

                    if (matching != null) {
                        unused.remove(matching);
                        CanvasViewInfo childView = createView(parentView, child, parentX, parentY,
                                matching);
                        parentView.addChild(childView);
                        afterNode = matching;
                    } else {
                        // We have no node for the view -- what do we do??
                        // Nothing - we only represent stuff in the outline that is in the
                        // source model, not in the render
                    }
                }
}

            // Add zero-bounded boxes for all remaining nodes since they need to show
            // up in the outline, need to be selectable so you can press Delete, etc.
            if (unused.size() > 0) {
                Map<UiViewElementNode, Integer> rankMap =
                    new HashMap<UiViewElementNode, Integer>();
                Map<UiViewElementNode, CanvasViewInfo> infoMap =
                    new HashMap<UiViewElementNode, CanvasViewInfo>();
                UiElementNode parent = unused.get(0).getUiParent();
                if (parent != null) {
                    int index = 0;
                    for (UiElementNode child : parent.getUiChildren()) {
                        UiViewElementNode node = (UiViewElementNode) child;
                        rankMap.put(node, index++);
                    }
                    for (CanvasViewInfo child : parentView.getChildren()) {
                        infoMap.put(child.getUiViewNode(), child);
                    }
                    List<Integer> usedIndexes = new ArrayList<Integer>();
                    for (UiViewElementNode node : unused) {
                        Integer rank = rankMap.get(node);
                        if (rank != null) {
                            usedIndexes.add(rank);
                        }
                    }
                    Collections.sort(usedIndexes);
                    for (int i = usedIndexes.size() - 1; i >= 0; i--) {
                        Integer rank = usedIndexes.get(i);
                        UiViewElementNode found = null;
                        for (UiViewElementNode node : unused) {
                            if (rankMap.get(node) == rank) {
                                found = node;
                                break;
                            }
                        }
                        if (found != null) {
                            Rectangle absRect = new Rectangle(parentX, parentY, 0, 0);
                            String name = found.getDescriptor().getXmlLocalName();
                            CanvasViewInfo v = new CanvasViewInfo(parentView, name, null, found,
                                    absRect, absRect);
                            // Find corresponding index in the parent view
                            List<CanvasViewInfo> siblings = parentView.getChildren();
                            int insertPosition = siblings.size();
                            for (int j = siblings.size() - 1; j >= 0; j--) {
                                CanvasViewInfo sibling = siblings.get(j);
                                UiViewElementNode siblingNode = sibling.getUiViewNode();
                                if (siblingNode != null) {
                                    Integer siblingRank = rankMap.get(siblingNode);
                                    if (siblingRank != null && siblingRank < rank) {
                                        insertPosition = j + 1;
                                        break;
                                    }
                                }
                            }
                            parentView.addChildAt(insertPosition, v);
                            unused.remove(found);
                        }
                    }
                }
                // Add in any remaining
                for (UiViewElementNode node : unused) {
                    Rectangle absRect = new Rectangle(parentX, parentY, 0, 0);
                    String name = node.getDescriptor().getXmlLocalName();
                    CanvasViewInfo v = new CanvasViewInfo(parentView, name, null, node, absRect,
                            absRect);
                    parentView.addChild(v);
                }
            }
        }

        private boolean isBefore(UiViewElementNode beforeNode, UiViewElementNode candidate) {
            UiElementNode parent = candidate.getUiParent();
            if (parent != null) {
                for (UiElementNode sibling : parent.getUiChildren()) {
                    if (sibling == beforeNode) {
                        return false;
                    } else if (sibling == candidate) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean isAfter(UiViewElementNode afterNode, UiViewElementNode candidate) {
            UiElementNode parent = candidate.getUiParent();
            if (parent != null) {
                for (UiElementNode sibling : parent.getUiChildren()) {
                    if (sibling == afterNode) {
                        return true;
                    } else if (sibling == candidate) {
                        return false;
                    }
                }
            }
            return false;
        }

        private UiViewElementNode nextViewNode(List<ViewInfo> children, int index) {
            int size = children.size();
            for (; index < size; index++) {
                ViewInfo child = children.get(index);
                if (child.getCookie() instanceof UiViewElementNode) {
                    return (UiViewElementNode) child.getCookie();
                }
}

return null;
}

        /** Search for a subtree with valid keys and add those subtrees */
        private CanvasViewInfo addKeyedSubtrees(CanvasViewInfo parent, ViewInfo viewInfo,
                int parentX, int parentY) {
            // We don't include MergeCookies when searching down for the first non-null key,
            // since this means we are in a "Show Included In" context, and the include tag itself
            // (which the merge cookie is pointing to) is still in the including-document rather
            // than the included document. Therefore, we only accept real UiViewElementNodes here,
            // not MergeCookies.
            if (viewInfo.getCookie() != null) {
                CanvasViewInfo subtree = createSubtree(parent, viewInfo, parentX, parentY);
                if (parent != null) {
                    parent.mChildren.add(subtree);
                }
                return subtree;
            } else {
                for (ViewInfo child : viewInfo.getChildren()) {
                    addKeyedSubtrees(parent, child, parentX + viewInfo.getLeft(), parentY
                            + viewInfo.getTop());
                }

                return null;
            }
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 8e94358..cb5c849 100644

//Synthetic comment -- @@ -1760,6 +1760,9 @@
* Called when the file changes triggered a redraw of the layout
*/
public void reloadLayout(final ChangeFlags flags, final boolean libraryChanged) {
            if (mConfigComposite.isDisposed()) {
                return;
            }
Display display = mConfigComposite.getDisplay();
display.asyncExec(new Runnable() {
public void run() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeOverlay.java
//Synthetic comment -- index 84f3e01..66adad8 100644

//Synthetic comment -- @@ -35,7 +35,7 @@
*/
public class IncludeOverlay extends Overlay {
/** Mask transparency - 0 is transparent, 255 is opaque */
    private static final int MASK_TRANSPARENCY = 160;

/** The associated {@link LayoutCanvas}. */
private LayoutCanvas mCanvas;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index 16f6fba..07ab41c 100644

//Synthetic comment -- @@ -521,6 +521,15 @@
vi = mCurrentView;
} else {
vi = mCanvas.getViewHierarchy().findViewInfoAt(p);

            // When dragging into the canvas, if you are not over any other view, target
            // the root element (since it may not "fill" the screen, e.g. if you have a linear
            // layout but have layout_height wrap_content, then the layout will only extend
            // to cover the children in the layout, not the whole visible screen area, which
            // may be surprising
            if (vi == null) {
                vi = mCanvas.getViewHierarchy().getRoot();
            }
}

boolean isMove = true;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index d7bc412..02f98b1 100755

//Synthetic comment -- @@ -407,7 +407,7 @@
}
}
if (element instanceof CanvasViewInfo) {
                List<CanvasViewInfo> children = ((CanvasViewInfo) element).getUniqueChildren();
if (children != null) {
return children.toArray();
}
//Synthetic comment -- @@ -497,6 +497,8 @@
element = vi.getUiViewNode();
}

            Image image = getImage(element);

if (element instanceof UiElementNode) {
UiElementNode node = (UiElementNode) element;
styledString = node.getStyledDescription();
//Synthetic comment -- @@ -549,6 +551,7 @@
if (includedWithin != null) {
styledString = new StyledString();
styledString.append(includedWithin.getDisplayName(), QUALIFIER_STYLER);
                    image = IconFactory.getInstance().getIcon(LayoutDescriptors.VIEW_INCLUDE);
}
}

//Synthetic comment -- @@ -559,7 +562,7 @@

cell.setText(styledString.toString());
cell.setStyleRanges(styledString.getStyleRanges());
           cell.setImage(image);
super.update(cell);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionOverlay.java
//Synthetic comment -- index 90aeebf..817f2f9 100644

//Synthetic comment -- @@ -63,7 +63,7 @@
NodeProxy node = s.getNode();
if (node != null) {
String name = s.getName();
                    paintSelection(gcWrapper, s.getViewInfo(), node, name, isMultipleSelection);
}
}

//Synthetic comment -- @@ -121,7 +121,7 @@
}

/** Called by the canvas when a view is being selected. */
    private void paintSelection(IGraphics gc, CanvasViewInfo view, INode selectedNode, String displayName,
boolean isMultipleSelection) {
Rect r = selectedNode.getBounds();

//Synthetic comment -- @@ -133,6 +133,18 @@
gc.fillRect(r);
gc.drawRect(r);

        // Paint sibling rectangles, if applicable
        List<CanvasViewInfo> siblings = view.getNodeSiblings();
        if (siblings != null) {
            for (CanvasViewInfo sibling : siblings) {
                if (sibling != view) {
                    r = SwtUtils.toRect(sibling.getSelectionRect());
                    gc.fillRect(r);
                    gc.drawRect(r);
                }
            }
        }

/* Label hidden pending selection visual design
if (displayName == null || isMultipleSelection) {
return;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java
//Synthetic comment -- index 738da30..01487b9 100644

//Synthetic comment -- @@ -16,21 +16,27 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_MERGE;

import com.android.ide.common.api.INode;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.util.Pair;

import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Node;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.RandomAccess;
import java.util.Set;

/**
//Synthetic comment -- @@ -52,7 +58,7 @@
}

/**
     * The CanvasViewInfo root created by the last call to {@link #setSession}
* with a valid layout.
* <p/>
* This <em>can</em> be null to indicate we're dealing with an empty document with
//Synthetic comment -- @@ -63,7 +69,7 @@
private CanvasViewInfo mLastValidViewInfoRoot;

/**
     * True when the last {@link #setSession} provided a valid {@link LayoutScene}.
* <p/>
* When false this means the canvas is displaying an out-dated result image & bounds and some
* features should be disabled accordingly such a drag'n'drop.
//Synthetic comment -- @@ -137,21 +143,42 @@
mSession = session;
mIsResultValid = (session != null && session.getResult().isSuccess());
mExplodedParents = false;
if (mIsResultValid && session != null) {
List<ViewInfo> rootList = session.getRootViews();

            Pair<CanvasViewInfo,List<Rectangle>> infos = null;

            if (rootList == null || rootList.size() == 0) {
                // Special case: Look to see if this is really an empty <merge> view,
                // which shows up without any ViewInfos in the merge. In that case we
                // want to manufacture an empty view, such that we can target the view
                // via drag & drop, etc.
                if (hasMergeRoot()) {
                    ViewInfo mergeRoot = createMergeInfo(session);
                    infos = CanvasViewInfo.create(mergeRoot);
                } else {
                    infos = null;
                }
} else {
                if (rootList.size() > 1 && hasMergeRoot()) {
                    ViewInfo mergeRoot = createMergeInfo(session);
                    mergeRoot.setChildren(rootList);
                    infos = CanvasViewInfo.create(mergeRoot);
                } else {
                    ViewInfo root = rootList.get(0);
                    if (root != null) {
                        infos = CanvasViewInfo.create(root);
                    } else {
                        infos = null;
                    }
                }
            }
            if (infos != null) {
                mLastValidViewInfoRoot = infos.getFirst();
                mIncludedBounds = infos.getSecond();
            } else {
                mLastValidViewInfoRoot = null;
                mIncludedBounds = null;
}

updateNodeProxies(mLastValidViewInfoRoot, null);
//Synthetic comment -- @@ -167,10 +194,41 @@
// Update the selection
mCanvas.getSelectionManager().sync(mLastValidViewInfoRoot);
} else {
            mIncludedBounds = null;
mInvisibleParents.clear();
}
}

    private ViewInfo createMergeInfo(RenderSession session) {
        BufferedImage image = session.getImage();
        ControlPoint imageSize = ControlPoint.create(mCanvas,
                ICanvasTransform.IMAGE_MARGIN + image.getWidth(),
                ICanvasTransform.IMAGE_MARGIN + image.getHeight());
        LayoutPoint layoutSize = imageSize.toLayout();
        UiDocumentNode model = mCanvas.getLayoutEditor().getUiRootNode();
        List<UiElementNode> children = model.getUiChildren();
        return new ViewInfo(VIEW_MERGE, children.get(0), 0, 0, layoutSize.x, layoutSize.y);
    }

    /**
     * Returns true if this view hierarchy corresponds to an editor that has a {@code
     * <merge>} tag at the root
     *
     * @return true if there is a {@code <merge>} at the root of this editor's document
     */
    private boolean hasMergeRoot() {
        UiDocumentNode model = mCanvas.getLayoutEditor().getUiRootNode();
        if (model != null) {
            List<UiElementNode> children = model.getUiChildren();
            if (children != null && children.size() > 0
                    && VIEW_MERGE.equals(children.get(0).getDescriptor().getXmlName())) {
                return true;
            }
        }

        return false;
    }

/**
* Creates or updates the node proxy for this canvas view info.
* <p/>
//Synthetic comment -- @@ -189,14 +247,6 @@

if (key != null) {
mCanvas.getNodeFactory().create(vi);
}

for (CanvasViewInfo child : vi.getChildren()) {
//Synthetic comment -- @@ -411,7 +461,15 @@
if (r.contains(p.x, p.y)) {

// try to find a matching child first
            // Iterate in REVERSE z order such that siblings on top
            // are checked before earlier siblings (this matters in layouts like
            // FrameLayout and in <merge> contexts where the views are sitting on top
            // of each other and we want to select the same view as the one drawn
            // on top of the others
            List<CanvasViewInfo> children = canvasViewInfo.getChildren();
            assert children instanceof RandomAccess;
            for (int i = children.size() - 1; i >= 0; i--) {
                CanvasViewInfo child = children.get(i);
CanvasViewInfo v = findViewInfoAt_Recursive(p, child);
if (v != null) {
return v;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index c5dbec5..fa05c37 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gre;

import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_MERGE;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.IDragElement;
//Synthetic comment -- @@ -614,6 +616,7 @@
String ruleClassName;
ClassLoader classLoader;
if (realFqcn.startsWith("android.") || //$NON-NLS-1$
                    realFqcn.equals(VIEW_MERGE) ||
// FIXME: Remove this special case as soon as we pull
// the MapViewRule out of this code base and bundle it
// with the add ons
//Synthetic comment -- @@ -628,6 +631,10 @@
classLoader = RulesEngine.class.getClassLoader();
int dotIndex = realFqcn.lastIndexOf('.');
String baseName = realFqcn.substring(dotIndex+1);
                // Capitalize rule class name to match naming conventions, if necessary (<merge>)
                if (Character.isLowerCase(baseName.charAt(0))) {
                    baseName = Character.toUpperCase(baseName.charAt(0)) + baseName.substring(1);
                }
ruleClassName = packageName + "." + //$NON-NLS-1$
baseName + "Rule"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java
//Synthetic comment -- index 657a7f8..547db8b 100644

//Synthetic comment -- @@ -17,15 +17,23 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.MergeCookie;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.util.Pair;

import org.eclipse.swt.graphics.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

//Synthetic comment -- @@ -66,7 +74,7 @@
ViewInfo child2 = new ViewInfo("Button", child2Node, 0, 20, 70, 25);
root.setChildren(Arrays.asList(child1, child2));

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -103,7 +111,7 @@
ViewInfo child21 = new ViewInfo("RadioButton", child21Node, 0, 20, 70, 25);
child2.setChildren(Arrays.asList(child21));

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -140,7 +148,7 @@
ViewInfo child21 = new ViewInfo("RadioButton", null, 0, 20, 70, 25);
child2.setChildren(Arrays.asList(child21));

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -181,7 +189,7 @@
ViewInfo child21 = new ViewInfo("RadioButton", null, 0, 20, 70, 25);
child2.setChildren(Arrays.asList(child21));

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -207,6 +215,57 @@
assertEquals(0, includedView.getChildren().size());
}

    public void testMergeMatching() throws Exception {
        // Test rendering of MULTIPLE included views or when there is no simple match
        // between view info and ui element node children

        UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
        ViewInfo root = new ViewInfo("LinearLayout", rootNode, 10, 10, 100, 100);
        UiViewElementNode child1Node = createNode(rootNode, "android.widget.Button", false);
        ViewInfo child1 = new ViewInfo("CheckBox", child1Node, 0, 0, 50, 20);
        UiViewElementNode multiChildNode1 = createNode(rootNode, "foo", true);
        UiViewElementNode multiChildNode2 = createNode(rootNode, "bar", true);
        ViewInfo child2 = new ViewInfo("RelativeLayout", null, 0, 20, 70, 25);
        ViewInfo child3 = new ViewInfo("AbsoluteLayout", null, 10, 40, 50, 15);
        root.setChildren(Arrays.asList(child1, child2, child3));
        ViewInfo child21 = new ViewInfo("RadioButton", null, 0, 20, 70, 25);
        child2.setChildren(Arrays.asList(child21));

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
        assertNotNull(rootView);
        assertEquals("LinearLayout", rootView.getName());
        assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
        assertEquals(new Rectangle(10, 10, 89, 89), rootView.getSelectionRect());
        assertNull(rootView.getParent());
        assertSame(rootNode, rootView.getUiViewNode());
        assertEquals(3, rootView.getChildren().size());

        CanvasViewInfo childView1 = rootView.getChildren().get(0);
        CanvasViewInfo includedView1 = rootView.getChildren().get(1);
        CanvasViewInfo includedView2 = rootView.getChildren().get(2);

        assertEquals("CheckBox", childView1.getName());
        assertSame(rootView, childView1.getParent());
        assertEquals(new Rectangle(10, 10, 49, 19), childView1.getAbsRect());
        assertEquals(new Rectangle(10, 10, 49, 19), childView1.getSelectionRect());
        assertSame(childView1.getUiViewNode(), child1Node);

        assertEquals("RelativeLayout", includedView1.getName());
        assertSame(multiChildNode1, includedView1.getUiViewNode());
        assertEquals("foo", includedView1.getUiViewNode().getDescriptor().getXmlName());
        assertSame(multiChildNode2, includedView2.getUiViewNode());
        assertEquals("AbsoluteLayout", includedView2.getName());
        assertEquals("bar", includedView2.getUiViewNode().getDescriptor().getXmlName());
        assertSame(rootView, includedView1.getParent());
        assertSame(rootView, includedView2.getParent());
        assertEquals(new Rectangle(10, 30, 69, 4), includedView1.getAbsRect());
        assertEquals(new Rectangle(10, 30, 69, 5), includedView1.getSelectionRect());
        assertEquals(new Rectangle(20, 50, 39, -26), includedView2.getAbsRect());
        assertEquals(new Rectangle(20, 35, 39, 5), includedView2.getSelectionRect());
        assertEquals(0, includedView1.getChildren().size());
        assertEquals(0, includedView2.getChildren().size());
    }

public void testMerge() throws Exception {
// Test rendering of MULTIPLE included views or when there is no simple match
// between view info and ui element node children
//Synthetic comment -- @@ -222,7 +281,7 @@
ViewInfo child21 = new ViewInfo("RadioButton", null, 0, 20, 70, 25);
child2.setChildren(Arrays.asList(child21));

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -240,14 +299,249 @@
assertEquals(new Rectangle(10, 10, 49, 19), childView1.getSelectionRect());
assertSame(childView1.getUiViewNode(), child1Node);

        assertEquals("RelativeLayout", includedView.getName());
assertSame(rootView, includedView.getParent());
        assertEquals(new Rectangle(10, 30, 69, 4), includedView.getAbsRect());
        assertEquals(new Rectangle(10, 30, 69, 5), includedView.getSelectionRect());
assertEquals(0, includedView.getChildren().size());
assertSame(multiChildNode, includedView.getUiViewNode());
}

    public void testInsertMerge() throws Exception {
        // Test rendering of MULTIPLE included views or when there is no simple match
        // between view info and ui element node children

        UiViewElementNode mergeNode = createNode("merge", true);
        UiViewElementNode rootNode = createNode(mergeNode, "android.widget.Button", false);
        ViewInfo root = new ViewInfo("Button", rootNode, 10, 10, 100, 100);

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
        assertNotNull(rootView);
        assertEquals("merge", rootView.getName());
        assertSame(rootView.getUiViewNode(), mergeNode);
        assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
        assertEquals(new Rectangle(10, 10, 89, 89), rootView.getSelectionRect());
        assertNull(rootView.getParent());
        assertSame(mergeNode, rootView.getUiViewNode());
        assertEquals(1, rootView.getChildren().size());

        CanvasViewInfo childView1 = rootView.getChildren().get(0);

        assertEquals("Button", childView1.getName());
        assertSame(rootView, childView1.getParent());
        assertEquals(new Rectangle(10, 10, 89, 89), childView1.getAbsRect());
        assertEquals(new Rectangle(10, 10, 89, 89), childView1.getSelectionRect());
        assertSame(childView1.getUiViewNode(), rootNode);
    }

    public void testUnmatchedMissing() throws Exception {
        UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
        ViewInfo root = new ViewInfo("LinearLayout", rootNode, 0, 0, 100, 100);
        List<ViewInfo> children = new ArrayList<ViewInfo>();
        // Should be matched up with corresponding node:
        Set<Integer> missingKeys = new HashSet<Integer>();
        // Should not be matched with any views, but should get view created:
        Set<Integer> extraKeys = new HashSet<Integer>();
        // Should not be matched with any nodes
        Set<Integer> extraViews = new HashSet<Integer>();
        int numViews = 30;
        missingKeys.add(0);
        missingKeys.add(4);
        missingKeys.add(14);
        missingKeys.add(29);
        extraKeys.add(9);
        extraKeys.add(20);
        extraKeys.add(22);
        extraViews.add(18);
        extraViews.add(24);

        List<String> expectedViewNames = new ArrayList<String>();
        List<String> expectedNodeNames = new ArrayList<String>();

        for (int i = 0; i < numViews; i++) {
            UiViewElementNode childNode = null;
            if (!extraViews.contains(i)) {
                childNode = createNode(rootNode, "childNode" + i, false);
            }
            Object cookie = missingKeys.contains(i) || extraViews.contains(i) ? null : childNode;
            ViewInfo childView = new ViewInfo("childView" + i, cookie,
                    0, i * 20, 50, (i + 1) * 20);
            children.add(childView);

            if (!extraViews.contains(i)) {
                expectedViewNames.add("childView" + i);
                expectedNodeNames.add("childNode" + i);
            }

            if (extraKeys.contains(i)) {
                createNode(rootNode, "extraNodeAt" + i, false);

                expectedViewNames.add("extraNodeAt" + i);
                expectedNodeNames.add("extraNodeAt" + i);
            }
        }
        root.setChildren(children);

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
        assertNotNull(rootView);

        // dump(root, 0);
        // dump(rootView, 0);

        assertEquals("LinearLayout", rootView.getName());
        assertNull(rootView.getParent());
        assertSame(rootNode, rootView.getUiViewNode());
        assertEquals(numViews + extraKeys.size() - extraViews.size(), rootNode.getUiChildren()
                .size());
        assertEquals(numViews + extraKeys.size() - extraViews.size(),
                rootView.getChildren().size());
        assertEquals(expectedViewNames.size(), rootView.getChildren().size());
        for (int i = 0, n = rootView.getChildren().size(); i < n; i++) {
            CanvasViewInfo childView = rootView.getChildren().get(i);
            String expectedViewName = expectedViewNames.get(i);
            String expectedNodeName = expectedNodeNames.get(i);
            assertEquals(expectedViewName, childView.getName());
            assertNotNull(childView.getUiViewNode());
            assertEquals(expectedNodeName, childView.getUiViewNode().getDescriptor().getXmlName());
        }
    }

    public void testMergeCookies() throws Exception {
        UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
        ViewInfo root = new ViewInfo("LinearLayout", rootNode, 0, 0, 100, 100);

        // Create the merge cookies in the opposite order to ensure that we don't
        // apply our own logic when matching up views with nodes
        LinkedList<MergeCookie> cookies = new LinkedList<MergeCookie>();
        for (int i = 0; i < 10; i++) {
            UiViewElementNode node = createNode(rootNode, "childNode" + i, false);
            cookies.addFirst(new MergeCookie(node));
        }
        Iterator<MergeCookie> it = cookies.iterator();
        ArrayList<ViewInfo> children = new ArrayList<ViewInfo>();
        for (int i = 0; i < 10; i++) {
            ViewInfo childView = new ViewInfo("childView" + i, it.next(), 0, i * 20, 50,
                    (i + 1) * 20);
            children.add(childView);
        }
        root.setChildren(children);

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
        assertNotNull(rootView);

        assertEquals("LinearLayout", rootView.getName());
        assertNull(rootView.getParent());
        assertSame(rootNode, rootView.getUiViewNode());
        for (int i = 0, n = rootView.getChildren().size(); i < n; i++) {
            CanvasViewInfo childView = rootView.getChildren().get(i);
            assertEquals("childView" + i, childView.getName());
            assertEquals("childNode" + (9 - i), childView.getUiViewNode().getDescriptor()
                    .getXmlName());
        }
    }

    public void testMergeCookies2() throws Exception {
        UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
        ViewInfo root = new ViewInfo("LinearLayout", rootNode, 0, 0, 100, 100);

        UiViewElementNode node1 = createNode(rootNode, "childNode1", false);
        UiViewElementNode node2 = createNode(rootNode, "childNode2", false);
        MergeCookie cookie1 = new MergeCookie(node1);
        MergeCookie cookie2 = new MergeCookie(node2);

        // Sets alternating merge cookies and checks whether the node sibling lists are
        // okay and merged correctly

        ArrayList<ViewInfo> children = new ArrayList<ViewInfo>();
        for (int i = 0; i < 10; i++) {
            Object cookie = (i % 2) == 0 ? cookie1 : cookie2;
            ViewInfo childView = new ViewInfo("childView" + i, cookie, 0, i * 20, 50, (i + 1) * 20);
            children.add(childView);
        }
        root.setChildren(children);

        Pair<CanvasViewInfo, List<Rectangle>> result = CanvasViewInfo.create(root);
        CanvasViewInfo rootView = result.getFirst();
        List<Rectangle> bounds = result.getSecond();
        assertNull(bounds);
        assertNotNull(rootView);

        assertEquals("LinearLayout", rootView.getName());
        assertNull(rootView.getParent());
        assertSame(rootNode, rootView.getUiViewNode());
        assertEquals(10, rootView.getChildren().size());
        assertEquals(2, rootView.getUniqueChildren().size());
        for (int i = 0, n = rootView.getChildren().size(); i < n; i++) {
            CanvasViewInfo childView = rootView.getChildren().get(i);
            assertEquals("childView" + i, childView.getName());
            Object cookie = (i % 2) == 0 ? node1 : node2;
            assertSame(cookie, childView.getUiViewNode());
            List<CanvasViewInfo> nodeSiblings = childView.getNodeSiblings();
            assertEquals(5, nodeSiblings.size());
        }
        List<CanvasViewInfo> nodeSiblings = rootView.getChildren().get(0).getNodeSiblings();
        for (int j = 0; j < 5; j++) {
            assertEquals("childView" + (j * 2), nodeSiblings.get(j).getName());
        }
        nodeSiblings = rootView.getChildren().get(1).getNodeSiblings();
        for (int j = 0; j < 5; j++) {
            assertEquals("childView" + (j * 2 + 1), nodeSiblings.get(j).getName());
        }
    }

    public void testIncludeBounds() throws Exception {
        UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
        ViewInfo root = new ViewInfo("included", null, 0, 0, 100, 100);

        UiViewElementNode node1 = createNode(rootNode, "childNode1", false);
        UiViewElementNode node2 = createNode(rootNode, "childNode2", false);
        MergeCookie cookie1 = new MergeCookie(node1);
        MergeCookie cookie2 = new MergeCookie(node2);

        // Sets alternating merge cookies and checks whether the node sibling lists are
        // okay and merged correctly

        ArrayList<ViewInfo> children = new ArrayList<ViewInfo>();
        for (int i = 0; i < 10; i++) {
            Object cookie = (i % 2) == 0 ? cookie1 : cookie2;
            ViewInfo childView = new ViewInfo("childView" + i, cookie, 0, i * 20, 50, (i + 1) * 20);
            children.add(childView);
        }
        root.setChildren(children);

        Pair<CanvasViewInfo, List<Rectangle>> result = CanvasViewInfo.create(root);
        CanvasViewInfo rootView = result.getFirst();
        List<Rectangle> bounds = result.getSecond();
        assertNotNull(rootView);

        assertEquals("included", rootView.getName());
        assertNull(rootView.getParent());
        assertNull(rootView.getUiViewNode());
        assertEquals(10, rootView.getChildren().size());
        assertEquals(2, rootView.getUniqueChildren().size());
        for (int i = 0, n = rootView.getChildren().size(); i < n; i++) {
            CanvasViewInfo childView = rootView.getChildren().get(i);
            assertEquals("childView" + i, childView.getName());
            Object cookie = (i % 2) == 0 ? node1 : node2;
            assertSame(cookie, childView.getUiViewNode());
            List<CanvasViewInfo> nodeSiblings = childView.getNodeSiblings();
            assertEquals(5, nodeSiblings.size());
        }
        List<CanvasViewInfo> nodeSiblings = rootView.getChildren().get(0).getNodeSiblings();
        for (int j = 0; j < 5; j++) {
            assertEquals("childView" + (j * 2), nodeSiblings.get(j).getName());
        }
        nodeSiblings = rootView.getChildren().get(1).getNodeSiblings();
        for (int j = 0; j < 5; j++) {
            assertEquals("childView" + (j * 2 + 1), nodeSiblings.get(j).getName());
        }

        // Only show the primary bounds as included
        assertEquals(2, bounds.size());
        assertEquals(new Rectangle(0, 0, 49, 19), bounds.get(0));
        assertEquals(new Rectangle(0, 20, 49, 19), bounds.get(1));
    }

/**
* Dumps out the given {@link ViewInfo} hierarchy to standard out.
* Useful during development.
//Synthetic comment -- @@ -261,11 +555,10 @@
System.out.println("Supports Embedded Layout=" + supportsEmbedding);
System.out.println("Rendering context=" + graphicalEditor.getIncludedWithin());
dump(root, 0);
}

/** Helper for {@link #dump(GraphicalEditorPart, ViewInfo)} */
    public static void dump(ViewInfo info, int depth) {
StringBuilder sb = new StringBuilder();
for (int i = 0; i < depth; i++) {
sb.append("    ");
//Synthetic comment -- @@ -285,7 +578,7 @@
sb.append(" ");
UiViewElementNode node = (UiViewElementNode) cookie;
sb.append("<");
            sb.append(node.getDescriptor().getXmlName());
sb.append("> ");
} else if (cookie != null) {
sb.append(" cookie=" + cookie);
//Synthetic comment -- @@ -297,4 +590,24 @@
dump(child, depth + 1);
}
}

    /** Helper for {@link #dump(GraphicalEditorPart, ViewInfo)} */
    public static void dump(CanvasViewInfo info, int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("    ");
        }
        sb.append(info.getName());
        sb.append(" [");
        sb.append(info.getAbsRect());
        sb.append("], node=");
        sb.append(info.getUiViewNode());

        System.out.println(sb.toString());

        for (CanvasViewInfo child : info.getChildren()) {
            dump(child, depth + 1);
        }
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactoryTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactoryTest.java
//Synthetic comment -- index 08f191a..277089f 100755

//Synthetic comment -- @@ -48,7 +48,7 @@
ViewElementDescriptor ved = new ViewElementDescriptor("xml", "com.example.MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120);
        CanvasViewInfo cvi = CanvasViewInfo.create(lvi).getFirst();

// Create a NodeProxy.
NodeProxy proxy = m.create(cvi);
//Synthetic comment -- @@ -95,7 +95,7 @@
ViewElementDescriptor ved = new ViewElementDescriptor("xml", "com.example.MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120);
        CanvasViewInfo cvi = CanvasViewInfo.create(lvi).getFirst();

// NodeProxies are cached. Creating the same one twice returns the same proxy.
NodeProxy proxy1 = m.create(cvi);
//Synthetic comment -- @@ -107,7 +107,7 @@
ViewElementDescriptor ved = new ViewElementDescriptor("xml", "com.example.MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120);
        CanvasViewInfo cvi = CanvasViewInfo.create(lvi).getFirst();

// NodeProxies are cached. Creating the same one twice returns the same proxy.
NodeProxy proxy1 = m.create(cvi);







